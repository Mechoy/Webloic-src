package weblogic.jms.frontend;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEBrowserCreateRequest;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.BESessionAcknowledgeRequest;
import weblogic.jms.backend.BESessionCloseRequest;
import weblogic.jms.backend.BESessionCreateRequest;
import weblogic.jms.backend.BESessionRecoverRequest;
import weblogic.jms.backend.BESessionSetRedeliveryDelayRequest;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.DDTxLoadBalancingOptimizer;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.InvalidDestinationException;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageContextImpl;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSSessionRecoverResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageStatistics;
import weblogic.jms.common.PushTarget;
import weblogic.jms.common.Sequencer;
import weblogic.jms.common.TransactionRolledBackException;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.utils.tracing.MessageTimeStamp;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSConsumerRuntimeMBean;
import weblogic.management.runtime.JMSProducerRuntimeMBean;
import weblogic.management.runtime.JMSSessionRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.Transaction;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.work.IDBasedConstraintEnforcement;
import weblogic.work.WorkManagerFactory;

public final class FESession extends RuntimeMBeanDelegate implements PushTarget, JMSSessionRuntimeMBean, Invocable, JMSPeerGoneListener, DDTxLoadBalancingOptimizer {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final long serialVersionUID = -2124132935744596179L;
   private final JMSID sessionId;
   private final FEConnection connection;
   private HashMap sequencers = new HashMap();
   private long nextSequenceNumber = 1L;
   private final JMSService service;
   private final MessageStatistics statistics = new MessageStatistics();
   private final boolean transacted;
   private final boolean xaSession;
   private volatile boolean transactionInUse;
   private TransactionManager tranManager;
   private Transaction transactedSessionTx;
   private JMSException transactedException;
   private final InvocableMonitor invocableMonitor;
   private Hashtable tranDestinations;
   private Hashtable tranPersistentDestinations;
   private Set tranDispatchers;
   private AuthenticatedSubject subjectForQOS = null;
   private final int acknowledgeMode;
   private final int originalAcknowledgeMode;
   private String pushWorkManager;
   private JMSPushEntry firstUnackedPushEntry;
   private JMSPushEntry lastUnackedPushEntry;
   private UnackedMessage firstTranStatUnackedMessage;
   private UnackedMessage lastTranStatUnackedMessage;
   private final HashMap consumers = new HashMap();
   private long consumersHighCount = 0L;
   private long consumersTotalCount = 0L;
   private final HashMap producers = new HashMap();
   private long producersHighCount = 0L;
   private long producersTotalCount = 0L;
   private final HashMap browsers = new HashMap();
   private long browsersHighCount = 0L;
   private HashMap beDispatchers = new HashMap();
   private InterceptionPointHandle receiveIPHandle = null;
   private DestinationImpl receiveIPDestination = null;
   private static Object interceptionPointLock = new Object();
   private boolean disableMultiSend = false;
   private transient int refCount;

   public FESession(FEConnection var1, String var2, JMSID var3, boolean var4, boolean var5, int var6, String var7) throws ManagementException {
      super(var2, var1.getRuntimeDelegate());
      this.connection = var1;
      this.sessionId = var3;
      this.transacted = var4;
      this.xaSession = var5;
      this.invocableMonitor = FrontEnd.getFrontEnd().getInvocableMonitor();
      this.pushWorkManager = var7;
      this.service = var1.getFrontEnd().getService();
      this.originalAcknowledgeMode = var6;
      if (var4) {
         this.tranManager = TxHelper.getTransactionManager();
         this.acknowledgeMode = 2;
         this.tranDestinations = new Hashtable();
         this.tranPersistentDestinations = new Hashtable();
         this.tranDispatchers = Collections.synchronizedSet(new HashSet());
      } else {
         this.acknowledgeMode = var6;
      }

      String var8 = System.getProperty("weblogic.jms.DisableMultiSender");
      String var9 = System.getProperty("weblogic.jms.DisablePushEnvelope");
      if (var8 != null && Boolean.valueOf(var8) == Boolean.TRUE || var9 != null && Boolean.valueOf(var9) == Boolean.TRUE) {
         System.err.println("JMS FE Multi Sender DISABLED");
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FRONTEND/FESession (id: " + this.sessionId + ") : Disable MultiSend ");
         }

         this.disableMultiSend = true;
      }

   }

   private synchronized AuthenticatedSubject getSubjectForQOS() {
      return this.subjectForQOS;
   }

   synchronized void updateQOS() {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (var1 != null) {
         if (this.subjectForQOS == null || this.subjectForQOS.getQOS() < var1.getQOS()) {
            this.subjectForQOS = var1;
         }

      }
   }

   public boolean isServerAffinityEnabled() {
      return this.connection.isServerAffinityEnabled();
   }

   public boolean visited(DistributedDestinationImpl var1) {
      return var1 != null && this.tranDispatchers != null ? this.tranDispatchers.contains(var1.getDispatcherId()) : false;
   }

   public void addVisitedDispatcher(DistributedDestinationImpl var1) {
      if (var1 != null && this.tranDispatchers != null) {
         this.tranDispatchers.add(var1.getDispatcherId());
      }
   }

   public void addCachedDest(DistributedDestinationImpl var1) {
      if (this.tranDestinations != null && this.tranPersistentDestinations != null && var1 != null) {
         if (var1.isPersistent()) {
            this.tranDestinations.put(var1.getName(), var1);
            this.tranPersistentDestinations.put(var1.getName(), var1);
         } else {
            this.tranDestinations.put(var1.getName(), var1);
         }

      }
   }

   public DistributedDestinationImpl getCachedDest(String var1, boolean var2) {
      if (this.tranDestinations != null && this.tranPersistentDestinations != null && var1 != null) {
         DistributedDestinationImpl var3;
         if (var2) {
            var3 = (DistributedDestinationImpl)this.tranPersistentDestinations.get(var1);
         } else {
            var3 = (DistributedDestinationImpl)this.tranDestinations.get(var1);
         }

         if (var3 != null && var3.isStale()) {
            this.cleanFailure(var3);
            var3 = null;
         }

         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            if (var3 != null) {
               System.out.println("Session Pick: " + var3.getInstanceName());
            } else {
               System.out.println("Session Pick: null");
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public void cleanFailure(DestinationImpl var1) {
      if (var1 != null) {
         if (this.tranDestinations != null) {
            this.tranDestinations.remove(var1.getName());
         }

         if (this.tranPersistentDestinations != null) {
            this.tranPersistentDestinations.remove(var1.getName());
         }

         if (this.tranDispatchers != null) {
            this.tranDispatchers.remove(var1.getDispatcherId());
         }

      }
   }

   public void cleanAll() {
      if (this.tranDestinations != null) {
         this.tranDestinations.clear();
      }

      if (this.tranPersistentDestinations != null) {
         this.tranPersistentDestinations.clear();
      }

      if (this.tranDispatchers != null) {
         this.tranDispatchers.clear();
      }

   }

   FEConnection getConnection() {
      return this.connection;
   }

   long getNextSequenceNumber() {
      return (long)(this.nextSequenceNumber++);
   }

   private long getSequenceNumber() {
      return this.nextSequenceNumber;
   }

   private void close(long var1) throws JMSException {
      this.close(false, var1, (JMSException)null);
   }

   void close(boolean var1, long var2, JMSException var4) throws JMSException {
      weblogic.jms.common.JMSException var6 = null;
      synchronized(interceptionPointLock) {
         if (this.receiveIPHandle != null && this.receiveIPDestination != null && (this.receiveIPDestination.getType() == 8 || this.receiveIPDestination.getType() == 4)) {
            try {
               if (!this.receiveIPHandle.hasAssociation()) {
                  MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(this.receiveIPHandle);
                  this.receiveIPHandle = null;
               }
            } catch (InterceptionServiceException var65) {
               JMSLogger.logFailedToUnregisterInterceptionPoint(var65);
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.close(), Failure to unregister " + var65);
               }
            }
         }
      }

      try {
         synchronized(this) {
            if (this.transacted && this.transactedSessionTx != null) {
               weblogic.jms.common.JMSException var10 = null;
               this.tranManager.forceResume(this.transactedSessionTx);

               try {
                  this.tranManager.rollback();
               } catch (SystemException var60) {
                  var10 = new weblogic.jms.common.JMSException(var60.getMessage(), var60);
               } finally {
                  this.transactedSessionTx = null;
                  this.transactedException = null;
                  this.transactionInUse = false;
                  this.cleanAll();
                  this.lastTranStatUnackedMessage = null;
                  this.firstTranStatUnackedMessage = null;
                  if (var10 != null) {
                     throw var10;
                  }

               }
            }
         }
      } finally {
         JMSPushEntry var5;
         HashMap var7;
         HashMap var8;
         synchronized(this) {
            this.transactionInUse = false;
            this.transactedException = null;
            Iterator var18 = ((HashMap)this.consumers.clone()).values().iterator();

            while(var18.hasNext()) {
               try {
                  JMSID var19 = ((FEConsumer)var18.next()).getJMSID();
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("FRONTEND/FESession (id: " + this.sessionId + ") : Closing consumer " + var19);
                  }

                  if (var4 != null) {
                     this.pushException(6, var19, (weblogic.jms.common.JMSException)var4);
                  }

                  this.consumerRemove(var19);
               } catch (Throwable var59) {
               }
            }

            var18 = ((HashMap)this.producers.clone()).values().iterator();

            while(var18.hasNext()) {
               try {
                  FEProducer var68 = (FEProducer)var18.next();
                  if (JMSDebug.JMSCommon.isDebugEnabled()) {
                     JMSDebug.JMSCommon.debug("FRONTEND/FESession (id: " + this.sessionId + ") : Closing Producer " + var68.getJMSID());
                  }

                  this.producerRemove(var68.getJMSID());
                  var68.removeDispatcher();
               } catch (Throwable var58) {
               }
            }

            var18 = ((HashMap)this.browsers.clone()).values().iterator();

            while(true) {
               if (!var18.hasNext()) {
                  var5 = this.firstUnackedPushEntry;
                  this.firstUnackedPushEntry = this.lastUnackedPushEntry = null;
                  var7 = this.beDispatchers;
                  this.beDispatchers = new HashMap();
                  var8 = this.sequencers;
                  this.sequencers = new HashMap();
                  var18 = var7.values().iterator();

                  while(var18.hasNext()) {
                     ((JMSDispatcher)var18.next()).removeDispatcherPeerGoneListener(this);
                  }
                  break;
               }

               try {
                  this.browserRemove(((FEBrowser)var18.next()).getJMSID());
               } catch (Throwable var57) {
               }
            }
         }

         if (var2 != 0L) {
            while(var5 != null && var5.getFrontEndSequenceNumber() != var2) {
               var5 = var5.getNextUnacked();
            }

            for(; var5 != null; var5 = var5.getPrevUnacked()) {
               JMSDispatcher var17 = var5.getDispatcher();
               var17 = (JMSDispatcher)var7.remove(var17.getId());

               try {
                  if (var17 != null) {
                     var17.dispatchSync(new BESessionCloseRequest(var1, this.sessionId, var5.getBackEndSequenceNumber()));
                  }
               } catch (Throwable var56) {
                  var6 = new weblogic.jms.common.JMSException("Error closing session", var56);
               }
            }
         }

         Iterator var67 = var7.values().iterator();
         BESessionCloseRequest var69 = new BESessionCloseRequest(var1, this.sessionId, 0L);

         while(var67.hasNext()) {
            try {
               JMSDispatcher var70 = (JMSDispatcher)var67.next();
               var70.dispatchSync(var69);
               var69.clearResult();
            } catch (Throwable var55) {
               var6 = new weblogic.jms.common.JMSException("Error closing session", var55);
            }
         }

         Iterator var71 = var8.values().iterator();

         while(var71.hasNext()) {
            Sequencer var20 = (Sequencer)var71.next();
            InvocableManagerDelegate.delegate.invocableRemove(13, var20.getJMSID());
         }

         try {
            PrivilegedActionUtilities.unregister(this, KERNEL_ID);
         } catch (ManagementException var54) {
            JMSLogger.logErrorUnregisteringFrontEndSession(this.getConnection().getFrontEnd().getMbeanName(), this, var54);
         }

      }

      this.connection.sessionRemove(this);
      if (var6 != null) {
         throw var6;
      }
   }

   private int producerCreate(FEProducerCreateRequest var1) throws JMSException {
      DestinationImpl var2 = var1.getDestination();
      this.checkShutdownOrSuspended();
      final JMSID var3 = this.service.getNextId();
      final String var4 = "producer" + var3.getCounter();
      final DestinationImpl var6 = var2;

      FEProducer var5;
      try {
         try {
            var5 = (FEProducer)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
               public Object run() throws ManagementException, JMSException {
                  return new FEProducer(var4, var3, FESession.this, var6);
               }
            });
         } catch (PrivilegedActionException var10) {
            throw var10.getException();
         }

         this.producerAdd(var5);
      } catch (Exception var11) {
         this.cleanFailure(var2);
         if (var11 instanceof JMSException) {
            throw (JMSException)var11;
         }

         if (var11 instanceof ManagementException) {
            ManagementException var8 = (ManagementException)var11;
            Object var9 = var8.getNestedException();
            if (var9 == null) {
               var9 = var8;
            }

            throw new weblogic.jms.common.JMSException("Error creating producer" + ((Throwable)var9).getMessage(), var8);
         }

         throw new weblogic.jms.common.JMSException("Error creating producer " + var11.getMessage(), var11);
      }

      var1.setResult(new FEProducerCreateResponse(var3, var5.getName()));
      var1.setState(Integer.MAX_VALUE);
      return var1.getState();
   }

   void producerClose(FEProducer var1) {
      this.producerRemove(var1.getJMSID());
   }

   private synchronized void producerAdd(FEProducer var1) throws JMSException {
      if (this.producers.put(var1.getJMSID(), var1) == null) {
         InvocableManagerDelegate.delegate.invocableAdd(9, var1);
         this.producersHighCount = Math.max(this.producersHighCount, (long)this.producers.size());
         ++this.producersTotalCount;
      }

   }

   private synchronized void producerRemove(JMSID var1) {
      if (this.producers != null) {
         while(true) {
            IDBasedConstraintEnforcement var2 = IDBasedConstraintEnforcement.getInstance();
            synchronized(var2) {
               if (var2.getExecutingCount(var1.getCounter()) + var2.getPendingCount(var1.getCounter()) == 0) {
                  break;
               }
            }

            try {
               Thread.sleep(500L);
            } catch (InterruptedException var6) {
            }
         }

         FEProducer var8 = (FEProducer)this.producers.remove(var1);
         if (var8 != null) {
            var8.closeProducer();
            InvocableManagerDelegate.delegate.invocableRemove(9, var1);

            try {
               PrivilegedActionUtilities.unregister(var8, KERNEL_ID);
            } catch (ManagementException var5) {
               JMSLogger.logErrorUnregisteringProducer(this.getConnection().getFrontEnd().getMbeanName(), var8, var5);
            }

         }
      }
   }

   void checkShutdownOrSuspended() throws JMSException {
      this.connection.checkShutdownOrSuspended();
   }

   public synchronized JMSProducerRuntimeMBean[] getProducers() {
      JMSProducerRuntimeMBean[] var1 = new JMSProducerRuntimeMBean[this.producers.size()];
      Iterator var2 = this.producers.values().iterator();

      for(int var3 = 0; var2.hasNext(); var1[var3++] = (JMSProducerRuntimeMBean)var2.next()) {
      }

      return var1;
   }

   public synchronized long getProducersCurrentCount() {
      return (long)this.producers.size();
   }

   public long getProducersHighCount() {
      return this.producersHighCount;
   }

   public synchronized long getProducersTotalCount() {
      return this.producersTotalCount;
   }

   public int getSubscriptionSharingPolicy() throws JMSException {
      this.checkShutdownOrSuspended();
      return this.connection.getSubscriptionSharingPolicyAsInt();
   }

   HashMap getConsumersMap() {
      return this.consumers;
   }

   HashMap getBEDispatchers() {
      return this.beDispatchers;
   }

   public Sequencer setUpBackEndSession(DispatcherId var1) throws JMSException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("FESession.setUpBackEndSession()");
      }

      boolean var2;
      long var3;
      synchronized(this.connection) {
         var2 = this.connection.isStopped();
         var3 = this.connection.getStartStopSequenceNumber();
      }

      byte var5 = this.connection.getPeerVersion();
      synchronized(this) {
         JMSDispatcher var7 = (JMSDispatcher)this.beDispatchers.get(var1);
         if (var7 != null) {
            return (Sequencer)this.sequencers.get(var1);
         } else {
            try {
               var7 = JMSDispatcherManager.dispatcherFindOrCreate(var1);
            } catch (DispatcherException var10) {
               throw new weblogic.jms.common.JMSException("Error creating session", var10);
            }

            Sequencer var8 = new Sequencer(this, var7);
            var7.dispatchSync(new BESessionCreateRequest(JMSDispatcherManager.getLocalDispatcher().getId(), this.connection.getJMSID(), this.getJMSID(), var8.getJMSID(), this.transacted, this.xaSession, this.acknowledgeMode, var2, var3, var5, this.connection.getAddressAndMBeanInfo(), this.pushWorkManager));
            InvocableManagerDelegate.delegate.invocableAdd(13, var8);
            this.sequencers.put(var1, var8);
            this.beDispatchers.put(var1, var7);
            var7.addDispatcherPeerGoneListener(this);
            return var8;
         }
      }
   }

   private int consumerCreate(final FEConsumerCreateRequest var1) throws JMSException {
      this.checkShutdownOrSuspended();
      Object var2 = null;
      FEDDHandler var3 = null;
      ConsumerReconnectInfo var4 = var1.getConsumerReconnectInfo();
      DestinationImpl var5;
      if (var4 != null && var4.getServerDestId() == null && var1.getName() != null && this.connection.getConnectionClientId() != null) {
         var5 = var1.getDestination();

         try {
            var5 = this.connection.createDestination(var5.getServerName(), var5.getName(), var5.getType());
            var1.setDestination(var5);
            var4.setServerDestId(var5.getDestinationId());
            var1.setConsumerReconnectInfo(var4);
         } catch (Throwable var20) {
            throw JMSUtilities.jmsExceptionThrowable("Destination " + var5.getName() + " not found", var20);
         }
      }

      if (!var1.getDestination().isQueue()) {
         String var23 = var1.getName();
         if (var23 != null && DDManager.isDD(var1.getDestination().getName())) {
            throw new weblogic.jms.common.JMSException("Topic must not be Distributed Topic");
         }

         if (var23 != null && this.connection.getConnectionClientId() != null) {
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("in FESessionConsumer durable");
            }

            if (var1.getSelector() != null && var1.getSelector().trim().equals("TRUE")) {
               var1.setSelector((String)null);
            }

            if (this.getConnection().getClientIdPolicy() == 0) {
               try {
                  JMSService var10000 = this.service;
                  DurableSubscription var6 = (DurableSubscription)JMSService.getContext().lookup(BEConsumerImpl.JNDINameForSubscription(BEConsumerImpl.clientIdPlusName(this.connection.getConnectionClientId(), var1.getName())));
                  if (DurableSubscription.noLocalAndSelectorMatch(var6, var1.getNoLocal(), var1.getSelector())) {
                     if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                        JMSDebug.JMSFrontEnd.debug("in FESessionConsumer matched consumer");
                     }

                     if (var1.getDestination().equals(var6.getDestinationImpl())) {
                        var2 = var6.getDestinationImpl();
                     }
                  } else if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                     JMSDebug.JMSFrontEnd.debug("FESession re/create new durable subscriber");
                  }
               } catch (NamingException var19) {
               }
            } else if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FESession re/create new durable subscriber using an Unrestricted ClientID");
            }
         } else {
            var3 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
            if (var3 != null && JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("will load balance non-durable on dist Topic " + var3.getName());
            }
         }
      }

      if (var2 == null && var3 == null) {
         var2 = var1.getDestination();
         var3 = DDManager.findFEDDHandlerByDDName(((DestinationImpl)var2).getName());
      }

      if (var3 != null) {
         if (var1.getDestination().isQueue() || var1.getName() != null && this.connection.getConnectionClientId() != null) {
            var2 = var3.consumerLoadBalance(this);
         } else {
            if (var3.getName().equals(var1.getDestination().getName())) {
               var2 = var3.consumerLoadBalance((FESession)null);
            } else {
               DistributedDestinationImpl var24 = DDManager.findDDImplByDDName(var1.getDestination().getName());
               if (var24 == null) {
                  throw new JMSException("Destination not found " + var1.getDestination().getName());
               }

               if (var24.isLocal()) {
                  var2 = var24;
               } else {
                  var2 = var3.consumerLoadBalance((FESession)null);
               }
            }

            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("local non durable topic available " + (var2 == null ? null : ((DistributedDestinationImpl)var2).getInstanceName()));
            }
         }
      } else if (var2 != null && ((DestinationImpl)var2).getDestinationId() == null) {
         try {
            var2 = this.connection.createDestination(((DestinationImpl)var2).getServerName(), ((DestinationImpl)var2).getName(), ((DestinationImpl)var2).getType());
         } catch (Throwable var18) {
            throw JMSUtilities.jmsExceptionThrowable("Destination " + ((DestinationImpl)var2).getName() + " not found", var18);
         }
      }

      if (var2 == null) {
         throw new JMSException("Destination not found " + var1.getDestination().getName());
      } else {
         var5 = null;

         Sequencer var26;
         try {
            var26 = this.setUpBackEndSession(((DestinationImpl)var2).getDispatcherId());
         } catch (JMSException var22) {
            if (var2 instanceof DistributedDestinationImpl) {
               throw var22;
            }

            try {
               var2 = this.connection.createDestination(((DestinationImpl)var2).getServerName(), ((DestinationImpl)var2).getName(), ((DestinationImpl)var2).getType());
               var26 = this.setUpBackEndSession(((DestinationImpl)var2).getDispatcherId());
            } catch (JMSException var17) {
               throw var22;
            }
         }

         final Object var25 = var2;
         final Sequencer var7 = var26;
         final JMSID var8 = this.service.getNextId();
         final String var9 = "consumer" + var8.getCounter();
         final AuthenticatedSubject var10 = JMSSecurityHelper.getCurrentSubject();
         final String var12 = JMSSecurityHelper.getSimpleAuthenticatedName();

         FEConsumer var11;
         try {
            try {
               var11 = (FEConsumer)SecurityServiceManager.runAs(KERNEL_ID, KERNEL_ID, new PrivilegedExceptionAction() {
                  public Object run() throws ManagementException, JMSException {
                     return new FEConsumer(var9, FESession.this, var7, FESession.this.connection.getConnectionClientId(), (DestinationImpl)var25, var8, var12, var10, var1);
                  }
               });
            } catch (PrivilegedActionException var16) {
               throw var16.getException();
            }

            this.consumerAdd(var11);
         } catch (Exception var21) {
            this.cleanFailure((DestinationImpl)var2);
            if (var21 instanceof JMSException) {
               throw (JMSException)var21;
            }

            if (var21 instanceof ManagementException) {
               ManagementException var14 = (ManagementException)var21;
               Object var15 = var14.getNestedException();
               if (var15 == null) {
                  var15 = var14;
               }

               throw new weblogic.jms.common.JMSException("Error creating consumer " + ((Throwable)var15).getMessage(), var14);
            }

            throw new weblogic.jms.common.JMSException("Error creating consumer " + var21.getMessage(), var21);
         }

         var1.setResult(new FEConsumerCreateResponse(var11.getJMSID(), var11.getName(), var11.getConsumerReconnectInfo()));
         var1.setState(Integer.MAX_VALUE);
         return var1.getState();
      }
   }

   private synchronized FEConsumer consumerFind(JMSID var1) throws JMSException {
      FEConsumer var2 = (FEConsumer)this.consumers.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new weblogic.jms.common.JMSException("Consumer not found, " + var1);
      }
   }

   private synchronized void consumerAdd(FEConsumer var1) throws JMSException {
      if (this.consumers.put(var1.getJMSID(), var1) == null) {
         this.consumersHighCount = Math.max(this.consumersHighCount, (long)this.consumers.size());
         ++this.consumersTotalCount;
      }

      InvocableManagerDelegate.delegate.invocableAdd(10, var1);
   }

   synchronized FEConsumer consumerRemove(JMSID var1) throws JMSException {
      FEConsumer var2 = (FEConsumer)this.consumers.remove(var1);
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("Consumer not found, " + var1);
      } else {
         InvocableManagerDelegate.delegate.invocableRemove(10, var1);

         try {
            PrivilegedActionUtilities.unregister(var2, KERNEL_ID);
         } catch (ManagementException var8) {
            JMSLogger.logErrorUnregisteringConsumer(this.getConnection().getFrontEnd().getMbeanName(), var2, var8);
         } finally {
            var2.getBackEndDispatcher().removeDispatcherPeerGoneListener(this);
            var2.getBackEndDispatcher().removeDispatcherPeerGoneListener(var2);
         }

         return var2;
      }
   }

   long consumerClose(FEConsumer var1, long var2) {
      synchronized(this) {
         if (var2 != 0L) {
            JMSPushEntry var4;
            for(var4 = this.firstUnackedPushEntry; var4 != null && var4.getFrontEndSequenceNumber() != var2; var4 = var4.getNextUnacked()) {
            }

            while(var4 != null && !var4.getConsumerId().equals(var1.getJMSID())) {
               var4 = var4.getPrevUnacked();
            }

            if (var4 == null) {
               var2 = 0L;
            } else {
               var2 = var4.getBackEndSequenceNumber();
            }
         }

         return var2;
      }
   }

   private synchronized void browserAdd(FEBrowser var1) throws JMSException {
      if (this.browsers.put(var1.getJMSID(), var1) == null) {
         InvocableManagerDelegate.delegate.invocableAdd(11, var1);
         this.browsersHighCount = Math.max(this.browsersHighCount, (long)this.browsers.size());
      }

   }

   private int browserCreate(FEBrowserCreateRequest var1) throws JMSException {
      this.checkShutdownOrSuspended();
      switch (var1.getState()) {
         case 0:
            DestinationImpl var2 = var1.getDestination();
            FEDDHandler var3 = DDManager.findFEDDHandlerByDDName(var2.getName());
            if (var3 != null) {
               var2 = var3.consumerLoadBalance((FESession)null);
            }

            BEBrowserCreateRequest var4 = new BEBrowserCreateRequest(this.sessionId, var2.getDestinationId(), var1.getMessageSelector());
            synchronized(var1) {
               var1.rememberChild(var4);
               var1.setState(1);
            }

            try {
               DispatcherId var5 = var2.getDispatcherId();
               var1.setDispatcher(JMSDispatcherManager.dispatcherFindOrCreate(var5));
               this.setUpBackEndSession(var5);
               var1.dispatchAsync(var1.getDispatcher(), var4);
            } catch (DispatcherException var7) {
               throw new weblogic.jms.common.JMSException("Error creating browser", var7);
            }

            return var1.getState();
         case 1:
         default:
            JMSBrowserCreateResponse var9 = (JMSBrowserCreateResponse)var1.useChildResult(JMSBrowserCreateResponse.class);
            FEBrowser var6 = new FEBrowser(this.connection, this, var9.getBrowserId(), var1.getDispatcher());
            this.browserAdd(var6);
            return Integer.MAX_VALUE;
      }
   }

   synchronized void browserRemove(JMSID var1) throws JMSException {
      if (this.browsers.remove(var1) == null) {
         throw new weblogic.jms.common.JMSException("Browser not found, " + var1);
      } else {
         InvocableManagerDelegate.delegate.invocableRemove(11, var1);
      }
   }

   public synchronized JMSConsumerRuntimeMBean[] getConsumers() {
      JMSConsumerRuntimeMBean[] var1 = new JMSConsumerRuntimeMBean[this.consumers.size()];
      Iterator var2 = this.consumers.values().iterator();

      for(int var3 = 0; var2.hasNext(); var1[var3++] = (JMSConsumerRuntimeMBean)var2.next()) {
      }

      return var1;
   }

   public synchronized long getConsumersCurrentCount() {
      return (long)this.consumers.size();
   }

   public synchronized long getConsumersHighCount() {
      return this.consumersHighCount;
   }

   public synchronized long getConsumersTotalCount() {
      return this.consumersTotalCount;
   }

   private int recover(FESessionRecoverRequest var1) throws JMSException {
      return var1.getPipelineGeneration() == 0 ? this.recover81(var1) : this.recover90(var1);
   }

   private int recover81(FESessionRecoverRequest var1) throws JMSException {
      boolean var2 = var1.doRollback();
      int var3 = var1.getState();
      Object var4 = null;
      var3 = this.recover81Init(var3, var1, var2);
      switch (var3) {
         case 0:
            this.recover81TransactionSetup(var1);
         case 1:
            long var5 = var1.getLastSequenceNumber();
            var3 = 2;
            var1.setState(2);
            JMSPushEntry var7;
            JMSPushEntry var8;
            HashMap var9;
            synchronized(this) {
               var1.setLastSequenceNumber(this.getSequenceNumber());
               var8 = var7 = this.firstUnackedPushEntry;
               this.firstUnackedPushEntry = this.lastUnackedPushEntry = null;
               var9 = (HashMap)this.sequencers.clone();
            }

            var1.needOutsideResult();
            this.recover81Statistics(var8, var5);
            if (var5 != 0L) {
               while(var7 != null && var7.getFrontEndSequenceNumber() != var5) {
                  var7 = var7.getNextUnacked();
               }
            }

            if (var7 != null) {
               for(; var7 != null; var7 = var7.getPrevUnacked()) {
                  Sequencer var10 = (Sequencer)var9.remove(var7.getDispatcher().getId());
                  if (var10 != null) {
                     BESessionRecoverRequest var11 = new BESessionRecoverRequest(this.sessionId, var7.getBackEndSequenceNumber(), var10, 0);

                     try {
                        var11.setNext(var1.getChildRequests());
                        var1.setChildRequests(var11);
                        var1.dispatchAsync(var10.getDispatcher(), var11);
                     } catch (DispatcherException var15) {
                        if (var4 == null) {
                           var4 = new weblogic.jms.common.JMSException("Error recovering messages", var15);
                        }
                     }
                  }
               }
            }

            if (var2) {
               if (var1.fanoutCompleteSuspendIfHaveChildren(false)) {
                  return var3;
               }
            } else if (var1.fanoutComplete(false)) {
               return var3;
            }
         case 2:
            var3 = Integer.MAX_VALUE;
            var1.setState(Integer.MAX_VALUE);

            for(BESessionRecoverRequest var16 = (BESessionRecoverRequest)var1.getChildRequests(); var16 != null; var16 = (BESessionRecoverRequest)var16.getNext()) {
               try {
                  Response var6 = var16.getResult();
                  var16.getSequencer().changeExpectedSequenceNumberCanHaveRemainder(((JMSSessionRecoverResponse)var6).getSequenceNumber());
               } catch (Throwable var14) {
                  if (var14 instanceof JMSException) {
                     var4 = (JMSException)var14;
                  } else {
                     var4 = new weblogic.jms.common.JMSException("Error recovering session", var14);
                  }
               }
            }

            if (var4 != null) {
               if (!var2) {
                  throw var4;
               }
            } else {
               var1.setResult(new JMSSessionRecoverResponse(var1.getLastSequenceNumber()));
               if (!var2) {
                  return var3;
               }
            }
         case 3:
            break;
         default:
            return var3;
      }

      if (this.transactedException == null) {
         this.transactedException = (JMSException)var4;
      }

      this.rollbackAfterRecover();
      return var3;
   }

   private int recover81Init(int var1, FESessionRecoverRequest var2, boolean var3) throws JMSException {
      if (var1 == 0 && !var3) {
         this.checkShutdownOrSuspended();
         var1 = 1;
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FERecover start");
         }
      }

      return var1;
   }

   private final JMSPushEntry recover81Statistics(JMSPushEntry var1, long var2) {
      JMSPushEntry var4;
      for(var4 = null; var1 != null; var1 = var1.getNextUnacked()) {
         if (var1.getFrontEndSequenceNumber() <= var2) {
            var4 = var1;
         }

         this.statistics.decrementPendingCount(var1.getMessageSize());

         FEConsumer var5;
         try {
            var5 = this.consumerFind(var1.getConsumerId());
         } catch (JMSException var7) {
            continue;
         }

         var5.statistics.decrementPendingCount(var1.getMessageSize());
      }

      return var4;
   }

   private void recover81TransactionSetup(FESessionRecoverRequest var1) {
      var1.setTranInfo(1);
      if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         JMSDebug.JMSFrontEnd.debug("FERollback start");
      }

      synchronized(this) {
         if (this.transactionInUse) {
            if (this.transactedException == null) {
               this.transactedException = new weblogic.jms.common.JMSException("Only one thread may use a JMS Session at a time.");
            }
         } else {
            this.transactionInUse = true;
         }

      }
   }

   private int recover90(FESessionRecoverRequest var1) throws JMSException {
      int var2 = var1.getState();
      JMSException var3 = null;
      var2 = this.recover81Init(var2, var1, var1.doRollback());
      BESessionRecoverRequest var4;
      switch (var2) {
         case 0:
            this.recover81TransactionSetup(var1);
         case 1:
            boolean var10 = true;
            var1.setState(2);
            synchronized(this) {
               JMSPushEntry var6 = this.recover81Statistics(this.firstUnackedPushEntry, var1.getLastSequenceNumber());
               var4 = this.get90beRecoverRequests(var6, var1.getPipelineGeneration());
               this.firstUnackedPushEntry = this.lastUnackedPushEntry = null;
            }

            var1.setChildRequests(var4);

            while(var4 != null) {
               try {
                  BESessionRecoverRequest var5 = (BESessionRecoverRequest)var4;
                  var4 = (BESessionRecoverRequest)var4.getNext();
                  var5.getSequencer().getDispatcher().dispatchSyncTran(var5);
               } catch (JMSException var9) {
                  if (var3 == null) {
                     var3 = var9;
                  }
               }
            }

            if (var1.doRollback()) {
               if (var3 != null && this.transactedException == null) {
                  this.transactedException = var3;
               }

               this.rollbackAfterRecover();
            }

            if (var3 != null) {
               throw var3;
            }
         case 2:
            for(var4 = (BESessionRecoverRequest)var1.getChildRequests(); var4 != null; var4 = (BESessionRecoverRequest)var4.getNext()) {
               Response var11 = var4.getResult();
            }

            var1.setResult(new JMSSessionRecoverResponse(var1.getLastSequenceNumber()));
            var2 = Integer.MAX_VALUE;
            var1.setState(Integer.MAX_VALUE);
            return var2;
         default:
            throw new AssertionError();
      }
   }

   private final BESessionRecoverRequest get90beRecoverRequests(JMSPushEntry var1, int var2) {
      BESessionRecoverRequest var3 = null;
      HashMap var4 = (HashMap)this.sequencers.clone();

      while(true) {
         while(var1 != null) {
            Sequencer var5 = (Sequencer)var4.remove(var1.getDispatcher().getId());
            if (var5 == null) {
               var1 = var1.getPrevUnacked();
            } else {
               var3 = this.one90beRecoverRequest(var5, var1.getBackEndSequenceNumber(), var2, var3);

               for(var1 = var1.getPrevUnacked(); var1 != null && var1.getSequencerId() == var5.getJMSID(); var1 = var1.getPrevUnacked()) {
               }
            }
         }

         if (var4.isEmpty()) {
            return var3;
         }

         for(Iterator var6 = var4.values().iterator(); var6.hasNext(); var3 = this.one90beRecoverRequest((Sequencer)var6.next(), 0L, var2, var3)) {
         }

         return var3;
      }
   }

   private BESessionRecoverRequest one90beRecoverRequest(Sequencer var1, long var2, int var4, BESessionRecoverRequest var5) {
      if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         JMSDebug.JMSFrontEnd.debug("FESession recover message " + var2);
      }

      BESessionRecoverRequest var6 = new BESessionRecoverRequest(this.sessionId, var2, var1, var4);
      var6.setNext(var5);
      return var6;
   }

   private int acknowledge(FESessionAcknowledgeRequest var1) throws JMSException {
      boolean var2 = false;
      Object var3 = null;
      weblogic.jms.common.JMSException var4 = null;
      BESessionAcknowledgeRequest var5 = null;
      BESessionAcknowledgeRequest var6 = null;
      long var7 = var1.getLastSequenceNumber();
      boolean var9 = var1.doCommit();
      int var10 = var1.getState();
      if (var10 == 0 && !var9) {
         this.checkShutdownOrSuspended();
         var10 = 1;
      }

      label458: {
         label457: {
            label456: {
               label484: {
                  JMSPushEntry var11;
                  switch (var10) {
                     case 0:
                        var1.setTranInfo(1);
                        if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                           JMSDebug.JMSFrontEnd.debug("FESession.commit " + this.hashCode());
                        }

                        boolean var12;
                        synchronized(this) {
                           var11 = this.lastUnackedPushEntry;
                           var12 = this.lastUnackedPushEntry == null;
                           if (this.transactionInUse) {
                              if (this.transactedException == null) {
                                 this.transactedException = new weblogic.jms.common.JMSException("Only one thread may use a JMS Session at a time.");
                              }
                           } else if (this.transactedException == null && var7 != 0L) {
                              while(var11 != null && var11.getFrontEndSequenceNumber() != var7) {
                                 var11 = var11.getPrevUnacked();
                              }
                           }
                        }

                        try {
                           int var13;
                           if (this.transactedException != null) {
                              var2 = true;
                              var3 = this.transactedException;
                              var13 = Integer.MAX_VALUE;
                              return var13;
                           }

                           try {
                              if (this.transactedSessionTx == null && (var11 == null || var12)) {
                                 if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                                    JMSDebug.JMSFrontEnd.debug("JMSSession.commit had no work");
                                 }

                                 var13 = Integer.MAX_VALUE;
                                 return var13;
                              }

                              this.transactedInfect();
                           } catch (Throwable var28) {
                              this.transactedSessionTx = null;
                              this.cleanAll();
                              var3 = var28;
                              var2 = true;
                              int var14 = Integer.MAX_VALUE;
                              return var14;
                           }

                           if (var11 != null || var7 == 0L && !var12) {
                              var10 = 1;
                           }
                        } finally {
                           if (var10 != 1) {
                              this.commitAfterAcknowledge(var2, var1, (Throwable)var3);
                              return Integer.MAX_VALUE;
                           }

                        }
                     case 1:
                        break;
                     case 2:
                        break label484;
                     case 3:
                        break label456;
                     default:
                        break label458;
                  }

                  var10 = 2;
                  synchronized(this) {
                     var11 = this.lastUnackedPushEntry;

                     while(true) {
                        if (var11 == null || var7 == var11.getFrontEndSequenceNumber()) {
                           if (var11 != null) {
                              if ((this.firstUnackedPushEntry = var11.getNextUnacked()) != null) {
                                 this.firstUnackedPushEntry.setPrevUnacked((JMSPushEntry)null);
                              } else {
                                 this.lastUnackedPushEntry = null;
                              }
                           }
                           break;
                        }

                        var11 = var11.getPrevUnacked();
                     }
                  }

                  if (var11 == null) {
                     if (!var9) {
                        var1.setResult(new VoidResponse());
                     } else {
                        if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                           JMSDebug.JMSFrontEnd.debug("FESession.acknowlege unackedPushEntry null, sequenceNumber=" + var7);
                        }

                        this.commitAfterAcknowledge(var2, var1, new weblogic.jms.common.JMSException("commit message not found"));
                     }

                     return Integer.MAX_VALUE;
                  }

                  var1.needOutsideResult();

                  for(HashMap var30 = new HashMap(); var11 != null; var11 = var11.getPrevUnacked()) {
                     try {
                        FEConsumer var15 = this.consumerFind(var11.getConsumerId());
                        var15.statistics.decrementPendingCount(var11.getMessageSize());
                        var15.statistics.incrementReceivedCount(var11.getMessageSize());
                     } catch (JMSException var24) {
                     }

                     this.statistics.decrementPendingCount(var11.getMessageSize());
                     this.statistics.incrementReceivedCount(var11.getMessageSize());
                     JMSDispatcher var32 = var11.getDispatcher();
                     BESessionAcknowledgeRequest var33 = new BESessionAcknowledgeRequest(this.sessionId, var11.getBackEndSequenceNumber());
                     if (var30.put(var32.getId(), var32) == null) {
                        if (var5 == null) {
                           var6 = var33;
                           var5 = var33;
                        } else {
                           var6.setNext(var33);
                           var6 = var33;
                        }

                        try {
                           var1.dispatchAsync(var32, var33);
                        } catch (DispatcherException var27) {
                           if (var4 == null) {
                              var4 = new weblogic.jms.common.JMSException("Error acknowledging messages", var27);
                           }
                        }
                     }
                  }

                  var1.rememberChild(var5);
                  if (var9) {
                     if (var1.fanoutCompleteSuspendIfHaveChildren(true)) {
                        break label457;
                     }
                  } else if (var1.fanoutComplete(true)) {
                     break label457;
                  }
               }

               if (!var9) {
                  Response var31 = this.checkChildExceptions(var1);
                  var1.setResult(var31);
                  return Integer.MAX_VALUE;
               }
            }

            this.commitAfterAcknowledge(var2, var1, (Throwable)var3);
            var10 = Integer.MAX_VALUE;
            break label458;
         }

         if (var4 != null) {
            throw var4;
         }
      }

      var1.setState(var10);
      return var10;
   }

   private JMSException transactedException(String var1, Throwable var2) {
      if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         JMSDebug.JMSFrontEnd.debug("throwTransactedException() B " + var1, var2);
      }

      if (!(var2 instanceof JMSException) || ((JMSException)var2).getErrorCode() == null || !((JMSException)var2).getErrorCode().equals("ReservedRollbackOnly")) {
         var1 = var2.getMessage() + ":" + var1;
      }

      if (this.transactedSessionTx != null) {
         try {
            int var3 = this.transactedSessionTx.getStatus();
            if (var3 == 9 || var3 == 4 || var3 == 1) {
               return new TransactionRolledBackException(var1, "ReservedRollbackOnly", var2);
            }
         } catch (SystemException var5) {
         }
      }

      return new weblogic.jms.common.JMSException(var1, var2);
   }

   private void throwTransactedException(String var1, Throwable var2) throws JMSException {
      JMSException var3 = this.transactedException(var1, var2);
      if (this.transactedException == null) {
         this.transactedException = var3;
      }

      throw var3;
   }

   public synchronized void transactedInfect() throws JMSException {
      this.transactedInfect(false);
   }

   public synchronized void transactedInfect(boolean var1) throws JMSException {
      String var2;
      if (this.transactionInUse) {
         if (this.transactedException == null) {
            var2 = "Only one thread may use a JMS Session at a time.";
            this.throwTransactedException(var2, new weblogic.jms.common.JMSException(var2));
         }

         throw this.transactedException;
      } else if (this.transactedSessionTx != null && this.transactedException != null) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FESession.transactedInfect() failed ", this.transactedException);
         }

         throw this.transactedException;
      } else {
         var2 = null;

         try {
            if (this.transactedSessionTx != null) {
               var2 = "error resuming transacted session's internal transaction";
               this.tranManager.resume(this.transactedSessionTx);
               this.transactionInUse = true;
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.transactedInfect() resume tx=" + TxHelper.getTransaction());
               }
            } else {
               var2 = "error beginning transacted session's internal transaction";
               int var3;
               if (var1) {
                  var3 = FEConnectionFactory.DEFAULT_SAF_TX_TIMEOUT;
               } else if (this.connection.getTransactionTimeout() > 2147483647L) {
                  var3 = Integer.MAX_VALUE;
               } else if (this.connection.getTransactionTimeout() > 0L) {
                  var3 = (int)this.connection.getTransactionTimeout();
               } else {
                  var3 = 3600;
               }

               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.transactedInfect() begin, timeout = " + var3);
               }

               this.tranManager.setTransactionTimeout(var3);
               this.tranManager.begin("JMS Internal");
               this.transactedSessionTx = TxHelper.getTransaction();
               this.transactionInUse = true;
               synchronized(this) {
                  this.transactedSessionTx.setName(this.getName());
               }

               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.transactedInfect() begin = " + this.transactedSessionTx);
               }
            }
         } catch (Exception var7) {
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FESession.transactedInfect() failed ", var7);
            }

            this.throwTransactedException(var2, var7);
         }

      }
   }

   synchronized void transactedDisinfect() throws JMSException {
      try {
         javax.transaction.Transaction var1 = this.tranManager.suspend();
         if (this.transactedSessionTx != null && this.transactedSessionTx != var1 && var1 != null) {
            Error var2 = new Error("dis transactedSessionTx !suspended|null");
            JMSLogger.logStackTrace(var2);
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FESession.transactedDisinfect() expected:" + this.transactedSessionTx + ", but got:" + var1);
            }

            throw var2;
         }

         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FESession.transactedDisinfect() suspend tx = " + this.transactedSessionTx);
         }
      } catch (SystemException var7) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FESession.pTransactedDisinfect() failed", var7);
         }

         this.throwTransactedException("SystemException suspending transacted session's internal transaction", var7);
      } finally {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("disinfected in finally");
         }

         this.transactionInUse = false;
      }

   }

   private synchronized void transactedUnackedStatistics(boolean var1) {
      for(UnackedMessage var2 = this.firstTranStatUnackedMessage; var2 != null; var2 = var2.getNext()) {
         if (var1) {
            var2.commitTransactedStatistics(this);
         } else {
            var2.rollbackTransactedStatistics(this);
         }
      }

      this.lastTranStatUnackedMessage = this.firstTranStatUnackedMessage = null;
   }

   private void rollbackAfterRecover() throws JMSException {
      Object var1 = this.transactedException;

      try {
         synchronized(this) {
            if (this.transactedSessionTx == null) {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.rollback had no tran");
               }

               return;
            }

            try {
               this.tranManager.forceResume(this.transactedSessionTx);
            } catch (Exception var12) {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.rollback resume failed", var12);
               }

               var1 = var12;
               return;
            }
         }

         try {
            this.tranManager.rollback();
         } catch (Throwable var11) {
            var1 = var11;
         }
      } finally {
         this.transactedException = null;
         this.transactionInUse = false;
         this.transactedSessionTx = null;
         this.cleanAll();
         this.tranManager.forceSuspend();
         this.transactedUnackedStatistics(false);
         if (var1 != null) {
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FERollback ends", (Throwable)var1);
            }

            throw new weblogic.jms.common.JMSException(((Throwable)var1).getMessage(), (Throwable)var1);
         }

         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FERollback end");
         }

      }

   }

   private Response checkChildExceptions(FESessionAcknowledgeRequest var1) throws JMSException {
      VoidResponse var2 = null;

      for(Request var3 = var1.getChild(); var3 != null; var3 = var3.getNext()) {
         try {
            var2 = (VoidResponse)var3.getResult();
         } catch (JMSException var5) {
            throw var5;
         } catch (Throwable var6) {
            return FESessionAcknowledgeRequest.handleThrowable(var6);
         }
      }

      if (var2 == null) {
         var2 = new VoidResponse();
      }

      return var2;
   }

   private void commitAfterAcknowledge(boolean var1, FESessionAcknowledgeRequest var2, Throwable var3) throws JMSException {
      try {
         if (var3 == null) {
            try {
               this.checkChildExceptions(var2);
            } catch (Throwable var33) {
               var3 = var33;
            }
         }

         if (var3 == null && this.transactedSessionTx != null && !var1) {
            try {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.commitAfterAck is called");
               }

               this.tranManager.commit();
               this.transactedUnackedStatistics(true);
            } catch (Throwable var32) {
               var3 = var32;
               var1 = false;
               this.transactedUnackedStatistics(false);
            }
         }
      } finally {
         this.transactionInUse = false;
         this.transactedException = null;
         this.transactedSessionTx = null;
         this.cleanAll();

         try {
            if (var1) {
               this.transactedUnackedStatistics(false);
               if (TxHelper.getTransaction() != null) {
                  try {
                     if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                        JMSDebug.JMSFrontEnd.debug("FESession.commitAfterAck forceRollback");
                     }

                     this.tranManager.rollback();
                  } catch (Throwable var31) {
                     var2.clearResult();
                     throw JMSUtilities.jmsExceptionThrowable("commit failed, then follback exception", var31);
                  }
               }
            }
         } finally {
            this.tranManager.forceSuspend();
         }

         if (var3 != null) {
            if (var3 instanceof javax.jms.TransactionRolledBackException && ((JMSException)var3).getErrorCode() != null && ((JMSException)var3).getErrorCode().equals("ReservedRollbackOnly")) {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FESession.commitAfterAck throw error code", var3);
               }

               var2.clearResult();
               throw JMSUtilities.jmsExceptionThrowable(var3.getMessage(), var3);
            }

            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FESession.commitAfterAck throws", var3);
            }

            var2.clearResult();
            throw this.transactedException("commit failure", var3);
         }

         var2.setResult(new VoidResponse());
      }

   }

   void transactionStat(FEConsumer var1, FEProducer var2, MessageImpl var3) {
      if (!this.transacted) {
         if (var1 != null) {
            var1.statistics.incrementReceivedCount(var3);
            this.statistics.incrementReceivedCount(var3);
         } else {
            var2.incMessagesSentCount(var3.getPayloadSize() + (long)var3.getUserPropertySize());
            this.statistics.incrementSentCount(var3);
         }

      } else {
         this.statistics.incrementPendingCount(var3);
         if (var1 != null) {
            var1.statistics.incrementPendingCount(var3);
         } else {
            var2.incMessagesPendingCount(var3.getPayloadSize() + (long)var3.getUserPropertySize());
         }

         UnackedMessage var4 = new UnackedMessage(var1, var2, var3);
         synchronized(this) {
            var4.setPrev(this.lastTranStatUnackedMessage);
            if (this.firstTranStatUnackedMessage == null) {
               this.firstTranStatUnackedMessage = var4;
            } else {
               this.lastTranStatUnackedMessage.setNext(var4);
            }

            this.lastTranStatUnackedMessage = var4;
         }
      }
   }

   public String getAcknowledgeMode() {
      if (this.originalAcknowledgeMode == 4) {
         return "None";
      } else if (this.originalAcknowledgeMode == 128) {
         return "Multicast None";
      } else if (this.originalAcknowledgeMode == 2) {
         return "Client";
      } else {
         return this.originalAcknowledgeMode == 1 ? "Auto" : "Dups-Ok";
      }
   }

   MessageStatistics getStatistics() {
      return this.statistics;
   }

   public long getBytesPendingCount() {
      return this.statistics.getBytesPendingCount();
   }

   public long getBytesReceivedCount() {
      return this.statistics.getBytesReceivedCount();
   }

   public long getBytesSentCount() {
      return this.statistics.getBytesSentCount();
   }

   public long getMessagesPendingCount() {
      return this.statistics.getMessagesPendingCount();
   }

   public long getMessagesReceivedCount() {
      return this.statistics.getMessagesReceivedCount();
   }

   public long getMessagesSentCount() {
      return this.statistics.getMessagesSentCount();
   }

   public boolean isTransacted() {
      return this.transacted;
   }

   private int setRedeliveryDelay(FESessionSetRedeliveryDelayRequest var1) throws JMSException {
      switch (var1.getState()) {
         case 0:
            this.checkShutdownOrSuspended();
            DispatcherException var5 = null;
            Iterator var4;
            synchronized(this) {
               var4 = ((HashMap)this.beDispatchers.clone()).values().iterator();
            }

            BESessionSetRedeliveryDelayRequest var3 = null;
            BESessionSetRedeliveryDelayRequest var2 = null;

            while(var4.hasNext()) {
               BESessionSetRedeliveryDelayRequest var6 = new BESessionSetRedeliveryDelayRequest(this.sessionId, var1.getRedeliveryDelay());
               if (var2 == null) {
                  var3 = var6;
                  var2 = var6;
               } else {
                  var3.setNext(var6);
                  var3 = var6;
               }

               try {
                  var1.dispatchAsync((JMSDispatcher)var4.next(), var6);
               } catch (DispatcherException var10) {
                  var5 = var10;
               }
            }

            if (var5 != null) {
               throw new weblogic.jms.common.JMSException("Error setting redelivery delay", var5);
            } else {
               synchronized(var1) {
                  var1.setState(2);
                  var1.rememberChild(var2);
                  return 2;
               }
            }
         default:
            VoidResponse var13 = null;

            for(Request var12 = var1.getChild(); var12 != null; var12 = var12.getNext()) {
               var13 = (VoidResponse)((BESessionSetRedeliveryDelayRequest)var12).getResult();
            }

            if (var13 == null) {
               var13 = new VoidResponse();
            }

            var1.setResult(var13);
            return Integer.MAX_VALUE;
      }
   }

   void addUnackedPushEntry(JMSPushEntry var1, long var2) {
      var1.setMessageSize(var2);
      var1.setFrontEndSequenceNumber(this.getNextSequenceNumber());
      if (this.acknowledgeMode != 4) {
         var1.setPrevUnacked(this.lastUnackedPushEntry);
         var1.setNextUnacked((JMSPushEntry)null);
         if (this.firstUnackedPushEntry == null) {
            this.firstUnackedPushEntry = var1;
         } else {
            this.lastUnackedPushEntry.setNextUnacked(var1);
         }

         this.lastUnackedPushEntry = var1;
      }
   }

   public void pushMessage(MessageImpl var1, JMSPushEntry var2) {
      long var3 = var1.getPayloadSize() + (long)var1.getUserPropertySize();
      synchronized(this) {
         if (!var2.getClientResponsibleForAcknowledge()) {
            if (this.acknowledgeMode != 4) {
               this.statistics.incrementPendingCount(var3);
            }

            this.addUnackedPushEntry(var2, var3);
         } else {
            this.statistics.incrementReceivedCount(var3);
         }

      }
   }

   private MessageImpl receiveInterceptionPoint(DestinationImpl var1, MessageImpl var2) throws JMSException {
      synchronized(interceptionPointLock) {
         if (this.receiveIPHandle != null && this.receiveIPDestination != var1) {
            try {
               MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(this.receiveIPHandle);
            } catch (InterceptionServiceException var8) {
               throw new AssertionError("Failure to unregister" + var8);
            }

            this.receiveIPHandle = null;
         }

         if (this.receiveIPHandle == null) {
            this.receiveIPDestination = var1;
            String[] var4 = new String[]{var1.getServerName(), var1.getName(), "Receive"};
            if (var4[0] == null) {
               var4[0] = new String();
            }

            if (var4[1] == null) {
               var4[1] = new String();
            }

            try {
               this.receiveIPHandle = MessageInterceptionService.getSingleton().registerInterceptionPoint("JMS", var4);
            } catch (InterceptionServiceException var7) {
               throw new weblogic.jms.common.JMSException("FAILED registerInterceptionPoint " + var7);
            }
         }

         try {
            if (this.receiveIPHandle.hasAssociation()) {
               var2 = var2.copy();
               JMSMessageContextImpl var11 = new JMSMessageContextImpl(var2);
               this.receiveIPHandle.process(var11);
               MessageImpl var10000 = var2;
               return var10000;
            }
         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException("FAILED in interception " + var9);
         }

         return null;
      }
   }

   public void pushMessage(JMSPushRequest var1) {
      JMSPushRequest var4 = var1;
      JMSPushRequest var5 = null;
      synchronized(this) {
         while(var1 != null) {
            MessageImpl var7 = var1.getMessage();
            if (this.connection.getCompressionThreshold() < var1.getCompressionThreshold()) {
               var1.setCompressionThreshold(this.connection.getCompressionThreshold());
            }

            long var8 = var7.getPayloadSize() + (long)var7.getUserPropertySize();
            JMSPushEntry var3 = null;
            JMSPushEntry var2 = var1.getFirstPushEntry();

            while(var2 != null) {
               try {
                  FEConsumer var10 = this.consumerFind(var2.getConsumerId());
                  if (var2.getClientResponsibleForAcknowledge()) {
                     this.statistics.incrementReceivedCount(var8);
                     var10.statistics.incrementReceivedCount(var8);
                     var2.setFrontEndSequenceNumber(this.getNextSequenceNumber());
                  } else {
                     if (this.acknowledgeMode != 4) {
                        this.statistics.incrementPendingCount(var8);
                        var10.statistics.incrementPendingCount(var8);
                     }

                     this.addUnackedPushEntry(var2, var8);
                  }

                  MessageImpl var11 = this.receiveInterceptionPoint(var10.getDestination(), var7);
                  if (var11 != null) {
                     if (var3 == null && var2.getNext() == null) {
                        var1.setMessage(var11);
                     } else {
                        JMSPushRequest var12 = new JMSPushRequest();
                        var12.setNext(var4);
                        var4 = var12;
                        var12.setMessage(var11);
                        var12.setFirstPushEntry(var2);
                        if (var3 == null) {
                           var1.setFirstPushEntry(var2.getNext());
                        } else {
                           var3.setNext(var2.getNext());
                        }

                        JMSPushEntry var13 = new JMSPushEntry();
                        var13.setNext(var2.getNext());
                        var2.setNext((JMSPushEntry)null);
                        var2 = var13;
                     }
                  } else {
                     var3 = var2;
                  }
               } catch (JMSException var31) {
                  if (var3 == null) {
                     var1.setFirstPushEntry(var2.getNext());
                  } else {
                     var3.setNext(var2.getNext());
                  }
               } finally {
                  var2 = var2.getNext();
               }
            }

            if (var1.getFirstPushEntry() == null) {
               if (var5 == null) {
                  var4 = (JMSPushRequest)var1.getNext();
               } else {
                  var5.setNext(var1.getNext());
               }
            }

            MessageTimeStamp.record(5, var1.getMessage());
            var5 = var1;
            var1 = (JMSPushRequest)var1.getNext();
         }
      }

      if (var4 != null) {
         try {
            JMSDispatcher var6 = this.connection.getClientDispatcher();
            if (var6.isLocal()) {
               var1 = new JMSPushRequest(var4);
            } else {
               var1 = var4;
            }

            var1.setInvocableType(4);
            if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
               JMSDebug.JMSMessagePath.debug("FRONTEND/FESession (id: " + this.sessionId + ") : " + "Pushing to the client, message " + var1.getMessage().getJMSMessageID());
            }

            AuthenticatedSubject var34 = (AuthenticatedSubject)CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(var6, this.getSubjectForQOS(), false);
            if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
               JMSDebug.JMSCrossDomainSecurity.debug("Push messages:subject to use = " + var34);
            }

            if (this.disableMultiSend && !var6.isLocal()) {
               WorkManagerFactory.getInstance().getSystem().schedule(new PushDispatchThread(var6, var1, var34));
            } else {
               SecurityServiceManager.pushSubject(KERNEL_ID, var34);

               try {
                  var6.dispatchNoReply(var1);
               } finally {
                  SecurityServiceManager.popSubject(KERNEL_ID);
               }
            }
         } catch (JMSException var30) {
            JMSLogger.logErrorPushingMessage(var30.toString(), var30);
         }
      }

   }

   public JMSID getJMSID() {
      return this.sessionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 520:
            try {
               return this.browserCreate((FEBrowserCreateRequest)var1);
            } catch (JMSException var4) {
               this.checkStaleDestination(var4, var1);
               return this.browserCreate((FEBrowserCreateRequest)var1);
            }
         case 2824:
            try {
               return this.consumerCreate((FEConsumerCreateRequest)var1);
            } catch (JMSException var3) {
               this.checkStaleDestination(var3, var1);
               return this.consumerCreate((FEConsumerCreateRequest)var1);
            }
         case 4872:
            return this.producerCreate((FEProducerCreateRequest)var1);
         case 6152:
            return this.acknowledge((FESessionAcknowledgeRequest)var1);
         case 6408:
            this.checkShutdownOrSuspended();
            this.close(((FESessionCloseRequest)var1).getLastSequenceNumber());
            break;
         case 6920:
            return this.recover((FESessionRecoverRequest)var1);
         case 7176:
            this.setRedeliveryDelay((FESessionSetRedeliveryDelayRequest)var1);
            break;
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }

      var1.setResult(new VoidResponse());
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   void checkStaleDestination(JMSException var1, Request var2) throws JMSException {
      if (!isStaleDestEx(var1)) {
         throw var1;
      } else {
         DestinationImpl var4;
         if (var2 instanceof FEBrowserCreateRequest) {
            FEBrowserCreateRequest var3 = (FEBrowserCreateRequest)var2;
            if (var3.getNumberOfRetries() > 0) {
               throw var1;
            }

            var4 = var3.getDestination();
            if (var4 instanceof DistributedDestinationImpl) {
               throw var1;
            }

            var3.setDestination(this.createFromStaleDestination(var4));
            var3.setNumberOfRetries(var3.getNumberOfRetries() + 1);
         } else {
            if (!(var2 instanceof FEConsumerCreateRequest)) {
               throw var1;
            }

            FEConsumerCreateRequest var5 = (FEConsumerCreateRequest)var2;
            if (var5.getNumberOfRetries() > 0) {
               throw var1;
            }

            var4 = var5.getDestination();
            if (var4 instanceof DistributedDestinationImpl) {
               throw var1;
            }

            var5.setDestination(this.createFromStaleDestination(var4));
            var5.setNumberOfRetries(var5.getNumberOfRetries() + 1);
         }

         var2.clearResult();
         var2.setState(0);
      }
   }

   private DestinationImpl createFromStaleDestination(DestinationImpl var1) throws JMSException {
      DestinationImpl var2 = this.getConnection().createDestination(var1);
      var2.setDispatcherId(var1.getDispatcherId());
      return var2;
   }

   static boolean isStaleDestEx(JMSException var0) {
      Object var1 = null;

      for(var1 = var0; var1 != null; var1 = ((Throwable)var1).getCause()) {
         if (var1 instanceof InvalidDestinationException) {
            return true;
         }
      }

      return false;
   }

   private synchronized JMSDispatcher beDispatcherRemove(DispatcherId var1) {
      JMSDispatcher var2 = (JMSDispatcher)this.beDispatchers.remove(var1);
      Sequencer var3 = (Sequencer)this.sequencers.remove(var1);
      if (var3 != null) {
         InvocableManagerDelegate.delegate.invocableRemove(13, var3.getJMSID());
      }

      return var2;
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("FEConsumer.jmsPeerGone()");
      }

      this.beDispatcherRemove(var2.getId());
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      HashMap var3 = (HashMap)this.producers.clone();
      HashMap var4 = (HashMap)this.consumers.clone();
      var2.writeStartElement("Session");
      var2.writeAttribute("id", this.sessionId != null ? this.sessionId.toString() : "");
      var2.writeAttribute("isTransacted", String.valueOf(this.transacted));
      var2.writeAttribute("consumersCurrentCount", String.valueOf(var4.size()));
      var2.writeAttribute("consumersHighCount", String.valueOf(this.consumersHighCount));
      var2.writeAttribute("consumersTotalCount", String.valueOf(this.consumersTotalCount));
      var2.writeAttribute("producersCurrentCount", String.valueOf(var3.size()));
      var2.writeAttribute("producersHighCount", String.valueOf(this.producersHighCount));
      var2.writeAttribute("producersTotalCount", String.valueOf(this.producersTotalCount));
      var2.writeAttribute("browsersCurrentCount", String.valueOf(this.browsers.size()));
      var2.writeAttribute("browsersHighCount", String.valueOf(this.browsersHighCount));
      this.statistics.dump(var1, var2);
      var2.writeStartElement("Producers");
      Iterator var5 = var3.values().iterator();

      while(var5.hasNext()) {
         FEProducer var6 = (FEProducer)var5.next();
         var6.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeStartElement("Consumers");
      var5 = var4.values().iterator();

      while(var5.hasNext()) {
         FEConsumer var7 = (FEConsumer)var5.next();
         var7.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }

   public void pushException(int var1, JMSID var2, JMSException var3) throws JMSException {
      JMSPushExceptionRequest var4 = new JMSPushExceptionRequest(var1, var2, (weblogic.jms.common.JMSException)var3);
      if (this.getSubjectForQOS() == null) {
         this.updateQOS();
      }

      AuthenticatedSubject var5 = (AuthenticatedSubject)CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.getConnection().getClientDispatcher(), this.getSubjectForQOS(), false);
      SecurityServiceManager.pushSubject(KERNEL_ID, var5);

      try {
         this.getConnection().getClientDispatcher().dispatchNoReply(var4);
      } catch (Exception var11) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("Failure pushing exception " + var11);
         }

         throw new JMSException("Failure pushing exception " + var11);
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

   }

   private class PushDispatchThread implements Runnable {
      JMSPushRequest pushRequest;
      JMSDispatcher dispatcher;
      AuthenticatedSubject subject;

      private PushDispatchThread(JMSDispatcher var2, JMSPushRequest var3, AuthenticatedSubject var4) {
         this.pushRequest = var3;
         this.dispatcher = var2;
         this.subject = var4;
      }

      public void run() {
         SecurityServiceManager.pushSubject(FESession.KERNEL_ID, this.subject);

         try {
            this.dispatcher.dispatchNoReply(this.pushRequest);
         } catch (JMSException var6) {
            JMSLogger.logErrorPushingMessage(var6.toString(), var6);
         } finally {
            SecurityServiceManager.popSubject(FESession.KERNEL_ID);
         }

      }

      // $FF: synthetic method
      PushDispatchThread(JMSDispatcher var2, JMSPushRequest var3, AuthenticatedSubject var4, Object var5) {
         this(var2, var3, var4);
      }
   }
}
