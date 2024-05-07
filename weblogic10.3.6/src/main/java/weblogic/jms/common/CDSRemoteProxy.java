package weblogic.jms.common;

import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.dispatcher.DispatcherAdapter;
import weblogic.jms.dispatcher.DispatcherWrapper;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CrossDomainManager;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.DispatcherWrapperState;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.security.subject.AbstractSubject;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public final class CDSRemoteProxy implements Invocable, JMSPeerGoneListener {
   private final List listenerDispatcherList = Collections.synchronizedList(new LinkedList());
   private static CDSRemoteProxy singleton = new CDSRemoteProxy();
   private transient int refCount;

   public static synchronized CDSRemoteProxy getSingleton() {
      return singleton;
   }

   private int processDDMembershipRequest(DDMembershipRequest var1) throws javax.jms.JMSException {
      String var2 = var1.getDDConfigName();
      String var3 = var1.getDDJndiName();
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("processDDMembershipRequest(): configName: " + var2 + " and jndiName: " + var3);
      }

      DispatcherWrapper var4 = var1.getDispatcherWrapper();
      JMSDispatcher var5 = null;

      try {
         var5 = JMSDispatcherManager.addDispatcherReference(var4);
         var5.addDispatcherPeerGoneListener(this);
      } catch (DispatcherException var11) {
         throw new JMSException(var11.getMessage(), var11);
      }

      ListenerDispatcher var6 = new ListenerDispatcher(var2, var3, var5);
      this.listenerDispatcherList.add(var6);
      DDMemberInformation[] var7 = var6.getDDMemberInformation();
      DDMembershipResponse var8 = new DDMembershipResponse(var7);
      var1.setResult(var8);
      var1.setState(Integer.MAX_VALUE);
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         StringBuffer var9 = new StringBuffer();
         if (var7 != null) {
            for(int var10 = 0; var10 < var7.length; ++var10) {
               var9.append("\nMember[" + var10 + "]=" + var7[var10].toString());
            }
         } else {
            var9.append("null");
         }

         JMSDebug.JMSCDS.debug("processDDMembershipRequest(): Returning the DDMembershipResponse back to the caller with local distributed destination member information: " + var9.toString());
      }

      return Integer.MAX_VALUE;
   }

   private int processDDMembershipCancelRequest(DDMembershipCancelRequest var1) {
      String var2 = var1.getDDJndiName();
      synchronized(this.listenerDispatcherList) {
         ListIterator var4 = this.listenerDispatcherList.listIterator();

         while(var4.hasNext()) {
            ListenerDispatcher var5 = (ListenerDispatcher)var4.next();
            if (var5.getJNDIName().equals(var2)) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("processDDMembershipCancelRequest(): Removing the remote dispatcher " + var5 + ", from the remoteDispatchers map for DD JNDI name " + var2);
               }

               var4.remove();
               var5.peerGone();
            }
         }

         return Integer.MAX_VALUE;
      }
   }

   public int invoke(Request var1) throws Throwable {
      switch (var1.getMethodId()) {
         case 18455:
            return this.processDDMembershipRequest((DDMembershipRequest)var1);
         case 18967:
            return this.processDDMembershipCancelRequest((DDMembershipCancelRequest)var1);
         default:
            throw new JMSException("No such method " + var1.getMethodId());
      }
   }

   public JMSID getJMSID() {
      return null;
   }

   public ID getId() {
      return null;
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSCDS.isDebugEnabled()) {
         JMSDebug.JMSCDS.debug("CDSServer.jmsPeerGone() " + var2.getId());
      }

      this.peerGone(var2);
   }

   private synchronized void peerGone(Dispatcher var1) {
      if (var1.getId() != null) {
         synchronized(this.listenerDispatcherList) {
            ListIterator var3 = this.listenerDispatcherList.listIterator();

            while(var3.hasNext()) {
               ListenerDispatcher var4 = (ListenerDispatcher)var3.next();
               Dispatcher var5 = var4 != null && var4.getDispatcher() != null ? ((DispatcherAdapter)var4.getDispatcher()).getDelegate() : null;
               if (var5 != null && var5.getId() != null && var5.getId().equals(var1.getId()) && CrossDomainManager.getCrossDomainUtil().isSameDomain(var5, var1)) {
                  if (JMSDebug.JMSCDS.isDebugEnabled()) {
                     JMSDebug.JMSCDS.debug("peerGone(): Dispatcher " + var4.getDispatcher() + " for DD (JNDI Name: " + var4.getJNDIName() + ") is affected by this peerGone, will be removed " + "from remoteDispatchers map " + "- CALLEE TO THE PEER case");
                  }

                  var3.remove();
                  var4.distributedDestinationGone(var1.getId());
               }
            }

         }
      }
   }

   private static boolean supportDispatchWithId(JMSDispatcher var0) {
      PeerInfo var1 = null;
      Dispatcher var2 = null;
      if (var0 instanceof DispatcherAdapter) {
         var2 = ((DispatcherAdapter)var0).getDelegate();
         if (var2 instanceof DispatcherWrapperState) {
            var1 = ((DispatcherWrapperState)var2).getPeerInfo();
         }
      } else if (var0 instanceof DispatcherWrapperState) {
         var1 = ((DispatcherWrapperState)var0).getPeerInfo();
      }

      return var1 != null && var1.compareTo(PeerInfo.VERSION_910) >= 0;
   }

   private static final class ListenerDispatcher implements CDSListListener, TimerListener {
      private JMSDispatcher dispatcher;
      private String jndiName;
      private String configName;
      private DDMemberInformation[] ddMemberInformation;
      private Timer timer;
      private long lastTime = 0L;
      private Object timerLock = new Object();
      private boolean timerSet = false;
      private AbstractSubject subject = null;

      public ListenerDispatcher(String var1, String var2, JMSDispatcher var3) throws javax.jms.JMSException {
         this.dispatcher = var3;
         this.jndiName = var2;
         this.configName = var1;
         this.ddMemberInformation = CDSServer.getSingleton().registerListener(this);
         this.subject = CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(var3);
      }

      public JMSDispatcher getDispatcher() {
         return this.dispatcher;
      }

      public DDMemberInformation[] getDDMemberInformation() {
         return this.ddMemberInformation;
      }

      public AbstractSubject getSubject() {
         return this.subject;
      }

      public AbstractSubject getForeignSubject() throws NamingException {
         return null;
      }

      public void setForeign(Hashtable var1) {
      }

      public boolean isLocal() {
         return false;
      }

      public void listChange(DDMemberInformation[] var1) {
         long var2 = 0L;
         this.ddMemberInformation = var1;
         synchronized(this.timerLock) {
            if (!this.timerSet) {
               this.timerSet = true;
               if (this.lastTime == 0L) {
                  var2 = 0L;
               } else {
                  long var5 = System.currentTimeMillis();
                  if (var5 - this.lastTime > 3000L) {
                     var2 = 0L;
                  } else {
                     var2 = 3000L - (var5 - this.lastTime);
                  }
               }

               if (var2 == 0L) {
                  this.timerExpired((Timer)null);
               } else {
                  this.timer = this.getTimerManager().schedule(this, var2);
               }
            }

         }
      }

      public void distributedDestinationGone(DispatcherId var1) {
         if (this.dispatcher != null) {
            DispatcherWrapper var2 = JMSDispatcherManager.getLocalDispatcherWrapper();
            final DDMembershipCancelRequest var3 = new DDMembershipCancelRequest(this.jndiName, var2);
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("Dispatching DDMembershipCancelRequest for " + this.jndiName);
            }

            final JMSDispatcher var4 = this.dispatcher;

            try {
               CrossDomainSecurityManager.doAs(this.subject, new PrivilegedExceptionAction() {
                  public Object run() throws javax.jms.JMSException {
                     if (CDSRemoteProxy.supportDispatchWithId(var4)) {
                        var4.dispatchNoReplyWithId(var3, ListenerDispatcher.this.jndiName.hashCode());
                     } else {
                        var4.dispatchNoReply(var3);
                     }

                     return null;
                  }
               });
            } catch (javax.jms.JMSException var6) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Exception in dispatching DDMembershipCancelRequest for " + this.jndiName, var6);
               }
            }

         }
      }

      public DistributedDestinationImpl getDistributedDestinationImpl() {
         return null;
      }

      public Context getContext() {
         return null;
      }

      public String getProviderURL() {
         return null;
      }

      public String getJNDIName() {
         return this.jndiName;
      }

      public String getConfigName() {
         return this.configName;
      }

      public void peerGone() {
         CDSServer.getSingleton().unregisterListener(this);
         this.dispatcher = null;
         if (this.timer != null) {
            this.timer.cancel();
         }

      }

      public void timerExpired(Timer var1) {
         final JMSDispatcher var2 = this.dispatcher;
         if (var2 != null) {
            synchronized(this.timerLock) {
               this.lastTime = System.currentTimeMillis();
               this.timerSet = false;
            }

            DispatcherWrapper var3 = JMSDispatcherManager.getLocalDispatcherWrapper();
            final DDMembershipPushRequest var4 = new DDMembershipPushRequest(this.configName, this.jndiName, this.ddMemberInformation, var3);
            if (JMSDebug.JMSCDS.isDebugEnabled()) {
               JMSDebug.JMSCDS.debug("Dispatching DDMembershipPushRequest for " + this.jndiName);
            }

            try {
               CrossDomainSecurityManager.doAs(this.subject, new PrivilegedExceptionAction() {
                  public Object run() throws javax.jms.JMSException {
                     if (CDSRemoteProxy.supportDispatchWithId(var2)) {
                        var2.dispatchNoReplyWithId(var4, ListenerDispatcher.this.jndiName.hashCode());
                     } else {
                        var2.dispatchNoReply(var4);
                     }

                     return null;
                  }
               });
            } catch (javax.jms.JMSException var7) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Exception in dispatching DDMembershipPushRequest for " + this.jndiName, var7);
               }
            }

         }
      }

      private TimerManager getTimerManager() {
         return TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.common.CDSRemoteProxy" + this.configName, WorkManagerFactory.getInstance().getSystem());
      }
   }
}
