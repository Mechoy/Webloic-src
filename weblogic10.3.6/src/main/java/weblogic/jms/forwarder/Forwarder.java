package weblogic.jms.forwarder;

import java.io.IOException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jms.client.SessionInternal;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageProcessor;
import weblogic.jms.common.PasswordStore;
import weblogic.jms.extensions.JMSForwardHelper;
import weblogic.jms.extensions.WLConnection;
import weblogic.jms.extensions.WLMessageProducer;
import weblogic.jms.forwarder.dd.DDLoadBalancerDelegate;
import weblogic.jms.forwarder.dd.internal.DDInfoImpl;
import weblogic.jms.forwarder.dd.internal.DDLoadBalancerDelegateImpl;
import weblogic.jms.forwarder.internal.SessionRuntimeContextImpl;
import weblogic.jndi.ClientEnvironment;
import weblogic.jndi.ClientEnvironmentFactory;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Sequence;
import weblogic.messaging.util.DeliveryList;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.store.PersistentStoreException;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.StackTraceUtilsClient;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class Forwarder implements TimerListener {
   private static final AbstractSubject kernelID = getKernelIdentity();
   private static final char[] key = new char[]{'S', 'a', 'F', ' ', 'I', 's', ' ', '5', 'U', 'n'};
   public static final int AT_MOST_ONCE = 1;
   public static final int EXACTLY_ONCE = 2;
   public static final int AT_LEAST_ONCE = 3;
   private static final String DEFAULT_CF = "weblogic.jms.ConnectionFactory";
   private static final int REDELIVERY_DELAY = 5000;
   private static final int REFRESH_MINIMUM = 1000;
   private final HashMap connectedForwarders = new HashMap();
   private final HashMap disconnectedForwarders = new HashMap();
   private Connection targetConn;
   private Session targetSession;
   private WLMessageProducer producer;
   private Timer timer;
   private ClientEnvironmentFactory environmentFactory;
   private Context remoteInitialCtx;
   private AbstractSubject subject;
   private String loginURL;
   private String username;
   private PasswordStore passwordStore;
   private Object passwordHandle;
   private int compressionThreshold;
   private String lastMsgId;
   private long retryDelayBase;
   private long retryDelayMaximum;
   private double retryDelayMultiplier;
   private long nextRetry;
   private int windowSize;
   private boolean scheduled;
   private boolean poisoned;
   private final MessageProcessor processor;
   private boolean localServerContext;
   private final Object scheduleLock;
   private long windowInterval;
   private long refreshInterval;
   private long connectionStart;
   private boolean forceResolveDNS;
   private static final boolean enableServerAffinity;

   private static final AbstractSubject getKernelIdentity() {
      try {
         return (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
      } catch (AccessControlException var1) {
         return null;
      }
   }

   public Forwarder(boolean var1, MessageProcessor var2, ClientEnvironmentFactory var3) {
      this.passwordStore = new PasswordStore(key);
      this.compressionThreshold = Integer.MAX_VALUE;
      this.retryDelayBase = 2000L;
      this.retryDelayMaximum = 180000L;
      this.retryDelayMultiplier = 1.0;
      this.windowSize = 10;
      this.scheduleLock = new Object();
      this.refreshInterval = Long.MAX_VALUE;
      this.forceResolveDNS = false;
      this.localServerContext = var1;
      this.processor = var2;
      this.environmentFactory = var3;
      Long var4 = Long.getLong("weblogic.jms.saf.WindowInterval", 0L);
      this.windowInterval = var4 == null ? 0L : var4;
      String var5 = System.getProperty("weblogic.jms.saf.RefreshInterval");
      if (var5 != null) {
         this.forceResolveDNS = true;
         this.refreshInterval = Long.parseLong(var5);
         if (this.refreshInterval < 1000L) {
            this.refreshInterval = 1000L;
         }
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Forwarder windowInterval:" + this.windowInterval + " refreshInterval:" + this.refreshInterval);
      }

   }

   public void start() {
      this.scheduleReconnect();
   }

   public void stop() {
      synchronized(this.scheduleLock) {
         if (this.poisoned) {
            return;
         }

         this.poisoned = true;
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.scheduled = false;
      }

      synchronized(this) {
         HashMap var2 = new HashMap(this.connectedForwarders);
         Iterator var3 = this.connectedForwarders.values().iterator();

         while(var3.hasNext()) {
            Subforwarder var4 = (Subforwarder)var3.next();
            var4.close();
         }

         if (var2.size() > 0) {
            synchronized(this.disconnectedForwarders) {
               this.disconnectedForwarders.putAll(var2);
            }
         }

         this.connectedForwarders.clear();
         if (this.targetConn != null) {
            this.pushSubject();

            try {
               this.targetConn.close();
            } catch (JMSException var14) {
            } finally {
               this.popSubject();
            }
         }

      }
   }

   private void schedule() {
      synchronized(this.scheduleLock) {
         if (!this.scheduled && !this.poisoned) {
            this.scheduled = true;
            if (this.timer != null) {
               this.timer.cancel();
            }

            TimerManagerFactory var2 = TimerManagerFactory.getTimerManagerFactory();
            TimerManager var3 = var2.getDefaultTimerManager();
            this.timer = var3.schedule(this, this.getNextRetry());
         }
      }
   }

   private void scheduleReconnect() {
      synchronized(this) {
         this.remoteInitialCtx = null;
         this.poisoned = false;
      }

      this.schedule();
   }

   private boolean reconnect() {
      synchronized(this.scheduleLock) {
         if (this.poisoned) {
            return false;
         }
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         if (this.remoteInitialCtx == null) {
            JMSDebug.JMSSAF.debug("Forwarder reconnect(): remoteInitialCtx= null");
         } else {
            try {
               JMSDebug.JMSSAF.debug("Forwarder reconnect(): java.naming.provider.url " + this.remoteInitialCtx.getEnvironment().get("java.naming.provider.url"));
            } catch (Throwable var17) {
               JMSDebug.JMSSAF.debug("Forwarder reconnect(): java.naming.provider.url ", var17);
            }
         }
      }

      synchronized(this) {
         Object var2 = null;

         try {
            this.connectTarget();
         } catch (SecurityException var13) {
            var2 = var13;
         } catch (NamingException var14) {
            var2 = var14;
         } catch (JMSException var15) {
            var2 = var15;
         } catch (Exception var16) {
            var2 = var16;
         }

         if (var2 != null) {
            this.remoteInitialCtx = null;
            this.reportDisconnectedToAll((Exception)var2);
            return true;
         } else {
            HashMap var3;
            synchronized(this.disconnectedForwarders) {
               var3 = new HashMap(this.disconnectedForwarders);
               this.disconnectedForwarders.clear();
            }

            boolean var10000;
            synchronized(var3) {
               Iterator var5 = var3.values().iterator();

               while(var5.hasNext()) {
                  Subforwarder var6 = (Subforwarder)var5.next();

                  try {
                     var6.connectLocalJMS();
                     var5.remove();
                     this.connectedForwarders.put(var6.sourceQueue.getName(), var6);
                  } catch (JMSException var18) {
                     if (JMSDebug.JMSSAF.isDebugEnabled()) {
                        JMSDebug.JMSSAF.debug(" subforwarder to " + var6.getTargetJNDI() + " failed to reconnect, due to " + var18);
                     }
                  } catch (NamingException var19) {
                     if (JMSDebug.JMSSAF.isDebugEnabled()) {
                        JMSDebug.JMSSAF.debug(" subforwarder to " + var6.getTargetJNDI() + " failed to reconnect, due to " + var19);
                     }
                  } catch (Exception var20) {
                     if (JMSDebug.JMSSAF.isDebugEnabled()) {
                        JMSDebug.JMSSAF.debug(" subforwarder to " + var6.getTargetJNDI() + " failed to reconnect, due to " + var20);
                     }
                  }
               }

               if (var3.size() > 0) {
                  synchronized(this.disconnectedForwarders) {
                     this.disconnectedForwarders.putAll(var3);
                  }

                  var10000 = true;
                  return var10000;
               }

               var10000 = false;
            }

            return var10000;
         }
      }
   }

   public void timerExpired(Timer var1) {
      synchronized(this.scheduleLock) {
         this.timer = null;
      }

      boolean var2 = this.reconnect();
      synchronized(this.scheduleLock) {
         this.scheduled = false;
      }

      if (var2) {
         this.schedule();
      } else {
         this.resetRetry();
      }

   }

   public void addSubforwarder(PersistentStoreXA var1, WorkManager var2, RuntimeHandler var3, Queue var4, String var5, int var6) {
      this.addSubforwarder(var1, var2, var3, var4, var5, var6, 2);
   }

   public void addSubforwarder(PersistentStoreXA var1, WorkManager var2, RuntimeHandler var3, Queue var4, String var5, int var6, int var7) {
      synchronized(this.disconnectedForwarders) {
         this.disconnectedForwarders.put(var4.getName(), new Subforwarder(var1, var2, var3, var4, var5, var6, var7));
      }

      this.schedule();
   }

   public synchronized void removeSubforwarder(Queue var1) {
      Subforwarder var2 = (Subforwarder)this.connectedForwarders.remove(var1.getName());
      if (var2 != null) {
         var2.close();
      }

      synchronized(this.disconnectedForwarders) {
         this.disconnectedForwarders.remove(var1.getName());
      }
   }

   private void reportDisconnectedToAll(Exception var1) {
      synchronized(this.disconnectedForwarders) {
         Iterator var3 = this.disconnectedForwarders.values().iterator();

         while(var3.hasNext()) {
            ((Subforwarder)var3.next()).reportDisconnected(var1);
         }

      }
   }

   private Context getInitialContext() throws NamingException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         String var1 = this.loginURL == null ? "LOCAL" : this.loginURL;
         JMSDebug.JMSSAF.debug("Retry for '" + var1 + (this.lastMsgId != null ? "' on behalf of '" + this.lastMsgId + "'" : "'") + " to '" + this.loginURL + "'");
      }

      ClientEnvironment var7 = this.environmentFactory.getNewEnvironment();
      if (this.loginURL != null) {
         var7.setProviderURL(this.loginURL);
      }

      var7.setSecurityPrincipal(this.username);
      String var2 = this.getPassword();
      var7.setSecurityCredentials(var2);
      var7.setEnableServerAffinity(enableServerAffinity);
      if (this.forceResolveDNS) {
         System.setProperty("weblogic.jndi.forceResolveDNSName", "true");
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Forwarder::getInitialContext()::Password=XXXX,username=" + this.username + " forceResolveDNS " + this.forceResolveDNS);
      }

      Context var3 = var7.getContext();
      synchronized(this) {
         this.subject = var7.getSubject();
      }

      this.popSubject();
      return var3;
   }

   private synchronized void connectTarget() throws SecurityException, JMSException, NamingException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("Forwarder connectTarget()");
      }

      if (this.remoteInitialCtx == null) {
         this.remoteInitialCtx = this.getInitialContext();
      }

      this.refreshDDLoadBalanceDelegate(this.targetConn, this.targetSession);
      this.pushSubject();

      try {
         ConnectionFactory var1 = (ConnectionFactory)this.remoteInitialCtx.lookup("weblogic.jms.ConnectionFactory");
         this.targetConn = var1.createConnection();
         ((WLConnection)this.targetConn).setReconnectPolicy(JMSConstants.RECONNECT_POLICY_NONE);
         this.targetSession = this.targetConn.createSession(true, 2);
         this.producer = (WLMessageProducer)this.targetSession.createProducer((Destination)null);
         this.producer.setCompressionThreshold(this.compressionThreshold);
         this.targetConn.start();
         this.refreshDDLoadBalanceDelegate(this.targetConn, this.targetSession);
         this.connectionStart = System.currentTimeMillis();
      } finally {
         this.popSubject();
      }

   }

   public void setCompressionThreshold(int var1) {
      this.compressionThreshold = var1;
      this.pushSubject();

      try {
         if (this.producer != null) {
            this.producer.setCompressionThreshold(var1);
         }
      } catch (JMSException var7) {
      } finally {
         this.popSubject();
      }

   }

   private synchronized void refreshDDLoadBalanceDelegate(Connection var1, Session var2) {
      Iterator var3 = this.connectedForwarders.values().iterator();

      Subforwarder var4;
      while(var3.hasNext()) {
         var4 = (Subforwarder)var3.next();
         var4.refreshDDLoadBalanceDelegate(this.remoteInitialCtx, var1, var2, this.subject);
      }

      var3 = this.disconnectedForwarders.values().iterator();

      while(var3.hasNext()) {
         var4 = (Subforwarder)var3.next();
         var4.refreshDDLoadBalanceDelegate(this.remoteInitialCtx, var1, var2, this.subject);
      }

   }

   private synchronized void pushSubject() {
      if (this.subject != null) {
         SubjectManager.getSubjectManager().pushSubject(kernelID, this.subject);
      }

   }

   private synchronized void popSubject() {
      if (this.subject != null) {
         SubjectManager.getSubjectManager().popSubject(kernelID);
      }

   }

   public void setLoginURL(String var1) {
      this.loginURL = var1;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public void setPassword(String var1) {
      if (this.passwordHandle != null) {
         this.passwordStore.removePassword(this.passwordHandle);
         this.passwordHandle = null;
      }

      try {
         this.passwordHandle = this.passwordStore.storePassword(var1);
      } catch (GeneralSecurityException var3) {
         throw new AssertionError(var3);
      }
   }

   private String getPassword() throws NamingException {
      String var1 = null;
      if (this.passwordHandle != null) {
         try {
            var1 = (String)this.passwordStore.retrievePassword(this.passwordHandle);
         } catch (GeneralSecurityException var3) {
            throw new NamingException(var3.getMessage());
         } catch (IOException var4) {
            throw new NamingException(var4.getMessage());
         }
      }

      return var1;
   }

   public void setRetryDelayBase(long var1) {
      synchronized(this.scheduleLock) {
         this.retryDelayBase = var1;
         this.resetRetry();
      }
   }

   public void setRetryDelayMaximum(long var1) {
      synchronized(this.scheduleLock) {
         this.retryDelayMaximum = var1;
      }
   }

   public void setRetryDelayMultiplier(double var1) {
      synchronized(this.scheduleLock) {
         this.retryDelayMultiplier = var1;
      }
   }

   public void setWindowSize(int var1) {
      this.windowSize = var1;
   }

   public void setWindowInterval(long var1) {
      this.windowInterval = var1;
   }

   private long getNextRetry() {
      if (this.nextRetry == 0L) {
         this.nextRetry = this.retryDelayBase;
      }

      long var1 = this.nextRetry;
      this.nextRetry = (long)((double)this.nextRetry * this.retryDelayMultiplier);
      if (this.nextRetry > this.retryDelayMaximum) {
         this.nextRetry = this.retryDelayMaximum;
      }

      return var1;
   }

   private void resetRetry() {
      synchronized(this.scheduleLock) {
         this.nextRetry = this.retryDelayBase;
      }
   }

   private void instantRetry() {
      synchronized(this.scheduleLock) {
         this.nextRetry = 1L;
      }
   }

   static {
      String var0 = System.getProperty("weblogic.jms.saf.enableServerAffinity", "false");
      enableServerAffinity = var0.equals("true");
   }

   private final class Subforwarder extends DeliveryList implements Listener {
      private static final int MAX_FAILURES = 100;
      private final PersistentStoreXA persistentStore;
      private final RuntimeHandler remoteEndpointRuntime;
      private final Queue sourceQueue;
      private final String targetJNDI;
      private Destination target;
      private ListenRequest listenRequest;
      private int nonPersistentQos = 1;
      private int persistentQos = 2;
      private boolean isTargetDD;
      private AtomicInteger failureCount = new AtomicInteger();
      private DDExactlyOnceForwardHelper ddExactlyOnceForwardHelper;

      Subforwarder(PersistentStoreXA var2, WorkManager var3, RuntimeHandler var4, Queue var5, String var6, int var7, int var8) {
         this.setWorkManager(var3);
         this.persistentStore = var2;
         this.remoteEndpointRuntime = var4;
         this.sourceQueue = var5;
         this.targetJNDI = var6;
         this.nonPersistentQos = var7;
         this.persistentQos = var8;
      }

      private void connectLocalJMS() throws JMSException, NamingException {
         try {
            this.target = this.lookupTargetDestination();
            this.verifyTargetSupportsSAF(this.target);
         } catch (JMSException var2) {
            this.reportDisconnected(var2);
            throw var2;
         } catch (NamingException var3) {
            this.reportDisconnected(var3);
            throw var3;
         } catch (Exception var4) {
            this.reportDisconnected(var4);
            throw new JMSException(var4.toString());
         }

         try {
            int var1 = Forwarder.this.windowSize;
            if (this.isTargetDD) {
               var1 = 1;
            }

            if (this.listenRequest != null) {
               this.listenRequest.incrementCount(var1);
            } else {
               this.listenRequest = this.sourceQueue.listen((Expression)null, var1, false, this, this, (String)null, WorkManagerFactory.getInstance().getSystem());
            }
         } catch (KernelException var5) {
            throw new weblogic.jms.common.JMSException(var5);
         }

         this.reportConnected();
      }

      private Destination lookupTargetDestination() throws NamingException, JMSException {
         Forwarder.this.pushSubject();

         Destination var2;
         try {
            Destination var1 = (Destination)Forwarder.this.remoteInitialCtx.lookup(this.targetJNDI);
            this.isTargetDD = var1 instanceof DistributedDestinationImpl;
            if (this.isTargetDD) {
               if (this.ddExactlyOnceForwardHelper != null) {
                  this.closeDDLoadBalancerDelegate();
               }

               this.ddExactlyOnceForwardHelper = new DDExactlyOnceForwardHelper((DistributedDestinationImpl)var1);
            }

            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug(this + " ddExactlyOnceForwardHelper= " + this.ddExactlyOnceForwardHelper + " target " + var1 + " targetJNDI " + this.targetJNDI);
            }

            var2 = var1;
         } finally {
            Forwarder.this.popSubject();
         }

         return var2;
      }

      private void verifyTargetSupportsSAF(Destination var1) throws JMSException {
         if (var1 instanceof DestinationImpl) {
            String[] var2 = ((DestinationImpl)var1).getSafAllowedArray();
            if (var2 == null || var2[0] == null || var2[0].equals("All")) {
               return;
            }
         }

         throw new JMSException("Endpoint '" + this.targetJNDI + "' does not allow Store-and-forwardInternal operation");
      }

      protected List getPendingMessages() {
         if (Forwarder.this.windowInterval <= 0L) {
            return super.getPendingMessages();
         } else {
            ArrayList var1 = new ArrayList();
            int var3 = 0;
            long var4 = System.currentTimeMillis();

            do {
               Object var2 = this.deliveryQueue.poll();
               if (var2 == null) {
                  long var6 = Forwarder.this.windowInterval - (System.currentTimeMillis() - var4);
                  if (var6 <= 0L) {
                     break;
                  }

                  try {
                     var2 = this.deliveryQueue.poll(var6);
                  } catch (InterruptedException var9) {
                  }
               }

               if (var2 != null) {
                  var1.add(var2);
                  ++var3;
               }
            } while(var3 < Forwarder.this.windowSize);

            return var1;
         }
      }

      protected void pushMessages(List var1) {
         long var2 = System.currentTimeMillis();
         long var4 = 0L;

         try {
            if (this.nonPersistentQos == 1) {
               if (this.persistentQos == 1) {
                  this.ackMessages(this.filterNonUOO(var1));
               } else {
                  this.ackMessages(this.filterNonPersistentAndNoUOO(var1));
               }
            } else if (this.persistentQos == 1) {
               this.ackMessages(this.filterPersistentAndNoUOO(var1));
            }
         } catch (KernelException var19) {
            Forwarder.this.schedule();
            return;
         }

         synchronized(Forwarder.this) {
            Iterator var7 = var1.iterator();

            while(true) {
               if (!var7.hasNext()) {
                  break;
               }

               ++var4;
               MessageElement var8 = (MessageElement)var7.next();
               MessageImpl var9 = (MessageImpl)var8.getMessage();
               boolean var10 = this.isTargetDD && var9.getUnitOfOrder() == null && (var9.getJMSDeliveryMode() == 2 && this.persistentQos == 2 || var9.getJMSDeliveryMode() == 1 && this.nonPersistentQos == 2);
               Sequence var11 = var8.getSequence();
               if (var11 != null) {
                  this.decorateMessageWithSequence(var8, var9);
               }

               Forwarder.this.processor.process(var9);
               this.setLastMessageIdPushed(var9);
               boolean var12 = !var7.hasNext();
               boolean var13 = this.forward(var9, var10, var12, var1);
               if (var13) {
                  return;
               }
            }
         }

         try {
            if (this.nonPersistentQos == 1) {
               if (this.persistentQos == 1) {
                  this.ackMessages(this.filterUOO(var1));
               } else {
                  this.ackMessages(this.filterPersistentOrUOO(var1));
               }
            } else if (this.persistentQos == 1) {
               this.ackMessages(this.filterNonPersistentOrUOO(var1));
            } else {
               this.ackMessages(var1);
            }
         } catch (KernelException var18) {
            Forwarder.this.schedule();
            return;
         }

         if (var2 - Forwarder.this.connectionStart > Forwarder.this.refreshInterval) {
            this.disconnectForwarder(new Exception("SAF refresh interval elapsed"));
            Forwarder.this.instantRetry();
            Forwarder.this.scheduleReconnect();
         }

         try {
            synchronized(Forwarder.this) {
               if (this.listenRequest != null) {
                  this.listenRequest.incrementCount(var1.size());
               }
            }
         } catch (KernelException var17) {
         }

         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug(this + " incrementCount " + var1.size());
         }

      }

      String getTargetJNDI() {
         return this.targetJNDI;
      }

      private void decorateMessageWithSequence(MessageElement var1, MessageImpl var2) {
         var2.setSAFSeqNumber(var1.getSequenceNum());
         var2.setSAFSequenceName(var1.getSequence().getName());
      }

      private boolean forward(MessageImpl var1, boolean var2, boolean var3, List var4) {
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Forwarder is trying to forward the message " + var1.getId() + "ddExactlyOnceForwardHelper= " + this.ddExactlyOnceForwardHelper);
         }

         boolean var5 = true;
         boolean var6 = true;

         try {
            Destination var7 = null;
            synchronized(var2 ? this.ddExactlyOnceForwardHelper.getDDLoabBalancerDelegate() : Forwarder.this) {
               try {
                  var7 = this.getEndpoint(var1, var2);
                  if (JMSDebug.JMSSAF.isDebugEnabled()) {
                     JMSDebug.JMSSAF.debug("Forwarder is trying to forward the message with id " + var1.getId() + " to " + var7);
                  }

                  this.forwardInternal(var1, var7, var3);
                  var5 = false;
                  var6 = false;
                  this.failureCount.set(0);
               } catch (JMSException var18) {
                  var5 = this.handleForwardFailure(var1, var7, var2, var4, var18);
               } catch (EndpointNotAvailableException var19) {
                  this.handleForwardFailure(var1, var7, var2, var4, var19);
               } catch (AssertionError var20) {
                  var5 = false;
                  throw var20;
               }
            }
         } finally {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("needsSchedule= " + var5 + " isExactlyOnceDDForwarding " + var2);
            }

            if (var5) {
               if (var2) {
                  this.closeDDLoadBalancerDelegate();
               }

               Forwarder.this.schedule();
            }

         }

         return var6;
      }

      private List filterNonPersistentAndNoUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MessageElement var4 = (MessageElement)var3.next();
            MessageImpl var5 = (MessageImpl)var4.getMessage();
            if (var5.getJMSDeliveryMode() == 1 && var5.getUnitOfOrder() == null) {
               var2.add(var4);
            }
         }

         return var2;
      }

      private List filterPersistentAndNoUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MessageElement var4 = (MessageElement)var3.next();
            MessageImpl var5 = (MessageImpl)var4.getMessage();
            if (var5.getJMSDeliveryMode() == 2 && var5.getUnitOfOrder() == null) {
               var2.add(var4);
            }
         }

         return var2;
      }

      private List filterPersistentOrUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(true) {
            MessageElement var4;
            MessageImpl var5;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (MessageElement)var3.next();
               var5 = (MessageImpl)var4.getMessage();
            } while(var5.getJMSDeliveryMode() != 2 && var5.getUnitOfOrder() == null);

            var2.add(var4);
         }
      }

      private List filterNonPersistentOrUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(true) {
            MessageElement var4;
            MessageImpl var5;
            do {
               if (!var3.hasNext()) {
                  return var2;
               }

               var4 = (MessageElement)var3.next();
               var5 = (MessageImpl)var4.getMessage();
            } while(var5.getJMSDeliveryMode() != 1 && var5.getUnitOfOrder() == null);

            var2.add(var4);
         }
      }

      private List filterNonUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MessageElement var4 = (MessageElement)var3.next();
            MessageImpl var5 = (MessageImpl)var4.getMessage();
            if (var5.getUnitOfOrder() == null) {
               var2.add(var4);
            }
         }

         return var2;
      }

      private List filterUOO(List var1) {
         LinkedList var2 = new LinkedList();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            MessageElement var4 = (MessageElement)var3.next();
            MessageImpl var5 = (MessageImpl)var4.getMessage();
            if (var5.getUnitOfOrder() != null) {
               var2.add(var4);
            }
         }

         return var2;
      }

      private void ackMessages(List var1) throws KernelException {
         try {
            KernelRequest var2 = this.sourceQueue.acknowledge(var1);
            if (var2 != null) {
               var2.getResult();
            }

            if (this.isTargetDD && this.ddExactlyOnceForwardHelper != null) {
               this.ddExactlyOnceForwardHelper.unfreezeDDLBTable();
            }

         } catch (KernelException var3) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Acknowledge failed " + var1.size());
            }

            throw var3;
         }
      }

      private void nakMessages(List var1, int var2) {
         try {
            KernelRequest var3 = new KernelRequest();
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Negatively acknowledging " + var1.size() + " messages with delay " + var2);
            }

            this.sourceQueue.negativeAcknowledge(var1, (long)var2, var3);
            var3.getResult();
         } catch (KernelException var4) {
         }

      }

      private void setLastMessageIdPushed(Message var1) {
         try {
            Forwarder.this.lastMsgId = var1.getJMSMessageID();
         } catch (JMSException var3) {
         }

      }

      private void forwardInternal(MessageImpl var1, Destination var2, boolean var3) throws JMSException {
         var1.setForward(true);
         var1.setJMSXUserID((String)null);
         var1.requestJMSXUserID(false);
         Forwarder.this.pushSubject();

         try {
            JMSForwardHelper.ForwardFromMessage(Forwarder.this.producer, var2, var1, true);
            if (var3) {
               Forwarder.this.targetSession.commit();
            }
         } finally {
            Forwarder.this.popSubject();
         }

      }

      private void closeDDLoadBalancerDelegate() {
         this.ddExactlyOnceForwardHelper.close();
      }

      private void refreshDDLoadBalanceDelegate(Context var1, Connection var2, Session var3, AbstractSubject var4) {
         if (this.ddExactlyOnceForwardHelper != null) {
            this.ddExactlyOnceForwardHelper.refreshDDLoadBalanceDelegate(var1, var2, var3, var4);
         }

      }

      private boolean hasNonFailedDDMembers() {
         return this.ddExactlyOnceForwardHelper != null ? this.ddExactlyOnceForwardHelper.hasNonFailedDDMembers() : false;
      }

      private boolean handleForwardFailure(MessageImpl var1, Destination var2, boolean var3, List var4, Exception var5) {
         boolean var6 = this.shouldPipelineBeStopped(var3) || this.failureCount.intValue() >= 100;
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("Forwarder failed to forward the message " + var1 + " to " + var2 + " stopPipeline= " + var6 + " hasNonFailedDDMembers " + this.hasNonFailedDDMembers() + " isExactlyOnceDDForwarding " + var3 + " failureCount " + this.failureCount.intValue() + " \n" + StackTraceUtilsClient.throwable2StackTrace(var5));
         }

         if (var6 && this.listenRequest != null) {
            this.listenRequest.stopAndWait();
            this.failureCount.set(0);
         }

         if (this.nonPersistentQos == 1) {
            if (this.persistentQos == 1) {
               var4 = this.filterUOO(var4);
            } else {
               var4 = this.filterPersistentOrUOO(var4);
            }
         } else if (this.persistentQos == 1) {
            var4 = this.filterNonPersistentOrUOO(var4);
         }

         var4.addAll(super.getPendingMessages());
         if (var3) {
            this.nakMessages(var4, 5000);
            if (var2 == null && !var6) {
               this.failureCount.incrementAndGet();
            }
         } else if (this.isTargetDD && var1.getUnitOfOrder() != null && this.hasNonFailedDDMembers()) {
            this.nakMessages(var4, 5000);
            this.failureCount.incrementAndGet();
         } else {
            this.nakMessages(var4, 0);
         }

         this.rollbackSession();
         if (var6) {
            this.disconnectForwarder(var5);
         }

         try {
            if (var2 != null && var3) {
               this.addFailedEndpoint(var1, (DestinationImpl)var2);
            }
         } catch (PersistentStoreException var15) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("addFailedEndpoint() failed \n" + StackTraceUtilsClient.throwable2StackTrace(var15));
            }

            throw new AssertionError(var15);
         }

         try {
            if (!var6 && this.listenRequest != null) {
               this.listenRequest.incrementCount(1);
            }

            if (var3 && !var6) {
               Object var7 = null;

               try {
                  try {
                     ((SessionInternal)Forwarder.this.targetSession).checkSAFClosed();
                  } catch (JMSException var9) {
                     if (JMSDebug.JMSSAF.isDebugEnabled()) {
                        JMSDebug.JMSSAF.debug("checkSAFClosed got JMSException:\n" + StackTraceUtilsClient.throwable2StackTrace(var9));
                     }

                     Forwarder.this.connectTarget();
                  }
               } catch (SecurityException var10) {
                  var7 = var10;
               } catch (JMSException var11) {
                  var7 = var11;
               } catch (NamingException var12) {
                  var7 = var12;
               } catch (Exception var13) {
                  var7 = var13;
               }

               if (var7 != null) {
                  if (JMSDebug.JMSSAF.isDebugEnabled()) {
                     JMSDebug.JMSSAF.debug("reconnectTarget got Exception: \n" + StackTraceUtilsClient.throwable2StackTrace((Throwable)var7));
                  }

                  var6 = true;
               }
            }
         } catch (KernelException var14) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("handleForwardFailure() got KernelException: \n" + StackTraceUtilsClient.throwable2StackTrace(var14));
            }
         }

         return var6;
      }

      void disconnectForwarder(Exception var1) {
         this.remoteEndpointRuntime.disconnected(var1);
         this.close();
         Subforwarder var2 = (Subforwarder)Forwarder.this.connectedForwarders.remove(this.sourceQueue.getName());
         synchronized(Forwarder.this.disconnectedForwarders) {
            if (var2 != null) {
               Forwarder.this.disconnectedForwarders.put(this.sourceQueue.getName(), var2);
            }

         }
      }

      private void rollbackSession() {
         Forwarder.this.pushSubject();

         try {
            Forwarder.this.targetSession.rollback();
         } catch (JMSException var6) {
         } finally {
            Forwarder.this.popSubject();
         }

      }

      private boolean shouldPipelineBeStopped(boolean var1) {
         return !var1 || !this.isTargetDD || !this.hasNonFailedDDMembers();
      }

      private Destination getEndpoint(MessageImpl var1, boolean var2) throws EndpointNotAvailableException {
         Destination var3 = null;
         if (var2) {
            var3 = this.ddExactlyOnceForwardHelper.getEndpoint(var1);
         } else {
            var3 = this.target;
         }

         if (var3 == null) {
            throw new EndpointNotAvailableException();
         } else {
            return var3;
         }
      }

      private void addFailedEndpoint(MessageImpl var1, DestinationImpl var2) throws PersistentStoreException {
         this.ddExactlyOnceForwardHelper.addFailedEndpoint(var1, var2);
      }

      private void close() {
         synchronized(Forwarder.this) {
            if (this.listenRequest != null) {
               this.listenRequest.stopAndWait();
            }

            this.listenRequest = null;
         }

         if (this.ddExactlyOnceForwardHelper != null) {
            this.closeDDLoadBalancerDelegate();
         }

         LinkedList var1 = new LinkedList();
         var1.addAll(super.getPendingMessages());
         if (var1.size() != 0) {
            KernelRequest var2 = new KernelRequest();

            try {
               this.sourceQueue.negativeAcknowledge(var1, 0L, false, var2);
               var2.getResult();
            } catch (KernelException var4) {
            }

         }
      }

      private void reportConnected() {
         this.remoteEndpointRuntime.connected();
      }

      private void reportDisconnected(Exception var1) {
         this.remoteEndpointRuntime.disconnected(var1);
      }

      public String toString() {
         return "[Subforward: targetJNDI = " + this.targetJNDI + "]";
      }

      private class EndpointNotAvailableException extends Exception {
         static final long serialVersionUID = -1747190651859301179L;

         private EndpointNotAvailableException() {
         }

         // $FF: synthetic method
         EndpointNotAvailableException(Object var2) {
            this();
         }
      }

      private class DDExactlyOnceForwardHelper {
         private DDLoadBalancerDelegate ddLoadBalancerDelegate;

         DDExactlyOnceForwardHelper(DistributedDestinationImpl var2) throws JMSException {
            DDInfoImpl var3 = new DDInfoImpl(var2.getDDJNDIName(), var2.getName(), var2.getDestinationInstanceType(), var2.getApplicationName(), var2.getModuleName(), var2.getLoadBalancingPolicy(), var2.getMessageForwardingPolicy());
            SessionRuntimeContextImpl var4 = null;
            synchronized(Forwarder.this) {
               if (Forwarder.this.forceResolveDNS) {
                  String var6 = null;

                  try {
                     var6 = Forwarder.this.getPassword();
                  } catch (NamingException var9) {
                  }

                  var4 = new SessionRuntimeContextImpl(Subforwarder.this.sourceQueue.getName(), Forwarder.this.remoteInitialCtx, Forwarder.this.loginURL, Forwarder.this.targetConn, Forwarder.this.targetSession, Forwarder.this.localServerContext, Forwarder.this.subject, Forwarder.this.username, var6, Forwarder.this.forceResolveDNS);
               } else {
                  var4 = new SessionRuntimeContextImpl(Subforwarder.this.sourceQueue.getName(), Forwarder.this.remoteInitialCtx, Forwarder.this.loginURL, Forwarder.this.targetConn, Forwarder.this.targetSession, Forwarder.this.localServerContext, Forwarder.this.subject);
               }
            }

            this.ddLoadBalancerDelegate = new DDLoadBalancerDelegateImpl(var4, var3, Subforwarder.this.persistentStore);
         }

         Object getDDLoabBalancerDelegate() {
            return this.ddLoadBalancerDelegate;
         }

         void unfreezeDDLBTable() {
            this.ddLoadBalancerDelegate.unfreezeDDLBTable();
         }

         Destination getEndpoint(MessageImpl var1) {
            synchronized(this.ddLoadBalancerDelegate) {
               return this.ddLoadBalancerDelegate.loadBalance(var1);
            }
         }

         void addFailedEndpoint(MessageImpl var1, DestinationImpl var2) throws PersistentStoreException {
            synchronized(this.ddLoadBalancerDelegate) {
               this.ddLoadBalancerDelegate.addFailedEndPoint(var1, var2);
            }
         }

         void close() {
            synchronized(this.ddLoadBalancerDelegate) {
               this.ddLoadBalancerDelegate.close();
            }
         }

         boolean hasNonFailedDDMembers() {
            synchronized(this.ddLoadBalancerDelegate) {
               return this.ddLoadBalancerDelegate.hasNonFailedDDMembers();
            }
         }

         void refreshDDLoadBalanceDelegate(Context var1, Connection var2, Session var3, AbstractSubject var4) {
            if (JMSDebug.JMSSAF.isDebugEnabled()) {
               JMSDebug.JMSSAF.debug("Forwarder for " + Subforwarder.this.targetJNDI + " is refreshing the session runtime context");
            }

            this.ddLoadBalancerDelegate.refreshSessionRuntimeContext(var1, var2, var3, var4);
         }

         public String toString() {
            return "[DDExactlyOnceForwardHelper: ddLoadBalancerDelegate = " + this.ddLoadBalancerDelegate + "]";
         }
      }
   }
}
