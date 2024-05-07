package weblogic.jms.frontend;

import java.io.Serializable;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.common.CompletionRequest;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEProducerSendRequest;
import weblogic.jms.backend.BEUOOMember;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DestinationPeerGoneAdapter;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSFailover;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageContextImpl;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.UOOHelper;
import weblogic.jms.dd.DDConstants;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.Request;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.JMSOrderException;
import weblogic.jms.utils.tracing.MessageTimeStamp;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSProducerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionException;
import weblogic.messaging.interception.exceptions.InterceptionProcessorException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.exceptions.MessageContextException;
import weblogic.messaging.interception.interfaces.CarrierCallBack;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;
import weblogic.messaging.path.Member;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.collections.SecondChanceCacheMap;

public final class FEProducer extends RuntimeMBeanDelegate implements JMSProducerRuntimeMBean, Invocable, CarrierCallBack, DDConstants {
   static final long serialVersionUID = -2064739049461407314L;
   private volatile boolean inSend = false;
   private final JMSID producerId;
   private final Object statisticsLock = new Object();
   private long messagesSentCount = 0L;
   private long messagesPendingCount = 0L;
   private long bytesSentCount = 0L;
   private long bytesPendingCount = 0L;
   private final FESession session;
   private DestinationImpl producerDestination;
   private JMSDispatcher producerDispatcher;
   private final InvocableMonitor invocableMonitor;
   private static final int STALE_DEST_MAP_MAXSIZE = 10;
   private final SecondChanceCacheMap staleDestsLRUMap;
   private boolean pinned;
   private HashMap pinnedDests;
   private HashMap pinnedPersistentDests;
   private static final int REQUEST_COMPLETED = 1;
   private static final int REQUEST_IN_PROGRESS = 2;
   private InterceptionPointHandle[] IPHandles = new InterceptionPointHandle[2];
   private DestinationImpl[] IPDestinations = new DestinationImpl[2];
   private static final String[] IPStrings = new String[]{"Start", "After Authorization"};
   private static final int[] IPNextStates = new int[]{6, 7};
   private static final int START_IP = 0;
   private static final int POST_AUTH_IP = 1;
   private boolean interceptionSaidContinue = false;
   private static final int DONE = 1;
   private static final int IN_PROGRESS = 2;
   private FEProducerSendRequest currentRequest;
   private InterceptionProcessorException interceptionException = null;
   private static Object cacheUOOLock = new Object();
   private static Object interceptionPointLock = new Object();
   private CacheUOOMember cacheUOOMember;
   private String pathJndiName;
   private static int TTL_GUESS = 60000;
   private static int TTL_CONFIRMED = 3600000;
   private HashMap stickyDests;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static int nextGeneration = 1;

   public FEProducer(String var1, JMSID var2, FESession var3, DestinationImpl var4) throws JMSException, ManagementException {
      super(var1, var3);
      if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         JMSDebug.JMSFrontEnd.debug("FEProducer.<init>()");
      }

      this.producerId = var2;
      this.session = var3;
      this.invocableMonitor = FrontEnd.getFrontEnd().getInvocableMonitor();
      this.interpretProducerDestination(var4);
      if (this.pinned) {
         this.pinnedDests = new HashMap();
         this.pinnedPersistentDests = new HashMap();
      }

      this.staleDestsLRUMap = new SecondChanceCacheMap(this.producerDestination == null ? 10 : 1);
      if (this.producerDestination != null) {
         if (!(var4 instanceof DistributedDestinationImpl)) {
            try {
               this.producerDispatcher = JMSDispatcherManager.dispatcherFindOrCreate(this.producerDestination.getDispatcherId());
            } catch (DispatcherException var6) {
               if (DDManager.findDDHandlerByMemberName(this.producerDestination.getName()) != null) {
                  throw new weblogic.jms.common.JMSException("Error creating producer for destination " + this.producerDestination.getName(), var6);
               }
            }

            if (this.producerDispatcher != null) {
               this.addAndNormalizeDestination(this.producerDestination, this.producerDispatcher);
            }
         }

      }
   }

   private void interpretProducerDestination(DestinationImpl var1) throws JMSException {
      if (var1 == null) {
         this.pinned = !this.session.getConnection().isLoadBalancingEnabled();
      } else {
         this.pinned = false;
         FEDDHandler var2 = DDManager.findFEDDHandlerByDDName(var1.getName());
         if (var2 != null) {
            if (!var1.isQueue() && !var2.isDDPartitionedDistributedTopic() || !this.session.getConnection().isLoadBalancingEnabled()) {
               var1 = var2.producerLoadBalance(false, this.session);
               if (var1 == null) {
                  throw new weblogic.jms.common.JMSException("Fail to find the destination");
               }

               this.pinned = true;
            }
         } else {
            this.pinned = true;
         }

         if (!(var1 instanceof DistributedDestinationImpl) && var1.getDestinationId() == null) {
            try {
               var1 = this.session.getConnection().createDestination(var1);
            } catch (Throwable var4) {
               throw JMSUtilities.jmsExceptionThrowable("Destination " + var1.getName() + " not found", var4);
            }
         }

         this.producerDestination = var1;
      }
   }

   private void addAndNormalizeDestination(DestinationImpl var1, JMSDispatcher var2) {
      if (var1 == null) {
         throw new AssertionError("Destination should never be null here");
      } else {
         DestinationPeerGoneAdapter var3 = new DestinationPeerGoneAdapter(var1, this.session.getConnection());
         synchronized(var2) {
            DestinationPeerGoneAdapter var5 = (DestinationPeerGoneAdapter)var2.addDispatcherPeerGoneListener(var3);
            if (var5 != null && !var5.equals(var3)) {
               DestinationImpl var6 = var5.getDestinationImpl();
               if (var6 != null) {
                  this.producerDestination = var6;
               }

            }
         }
      }
   }

   void incMessagesSentCount(long var1) {
      synchronized(this.statisticsLock) {
         ++this.messagesSentCount;
         this.bytesSentCount += var1;
      }
   }

   void incMessagesPendingCount(long var1) {
      synchronized(this.statisticsLock) {
         ++this.messagesPendingCount;
         this.bytesPendingCount += var1;
      }
   }

   void decMessagesPendingCount(long var1) {
      synchronized(this.statisticsLock) {
         --this.messagesPendingCount;
         this.bytesPendingCount -= var1;
      }
   }

   public long getBytesPendingCount() {
      synchronized(this.statisticsLock) {
         return this.bytesPendingCount;
      }
   }

   public long getBytesSentCount() {
      synchronized(this.statisticsLock) {
         return this.bytesSentCount;
      }
   }

   public long getMessagesPendingCount() {
      synchronized(this.statisticsLock) {
         return this.messagesPendingCount;
      }
   }

   public long getMessagesSentCount() {
      synchronized(this.statisticsLock) {
         return this.messagesSentCount;
      }
   }

   public void addPinnedDest(DestinationImpl var1) {
      if (this.pinnedDests != null && this.pinnedPersistentDests != null && var1 != null) {
         if (((DistributedDestinationImpl)var1).isPersistent()) {
            this.pinnedDests.put(var1.getName(), var1);
            this.pinnedPersistentDests.put(var1.getName(), var1);
         } else {
            this.pinnedDests.put(var1.getName(), var1);
         }

      }
   }

   public DestinationImpl getPinnedDest(DestinationImpl var1, boolean var2) {
      if (this.pinnedDests != null && this.pinnedPersistentDests != null && var1 != null) {
         DestinationImpl var3;
         if (var2) {
            var3 = (DestinationImpl)this.pinnedPersistentDests.get(var1.getName());
         } else {
            var3 = (DestinationImpl)this.pinnedDests.get(var1.getName());
         }

         if (var3 != null && var3.isStale()) {
            this.cleanFailure(var3);
            var3 = null;
         }

         return var3;
      } else {
         return null;
      }
   }

   private void cleanFailure(DestinationImpl var1) {
      if (this.pinnedDests != null && this.pinnedPersistentDests != null) {
         this.pinnedDests.remove(var1.getName());
         this.pinnedPersistentDests.remove(var1.getName());
         if (this.session != null && this.session.isTransacted()) {
            this.session.cleanFailure(var1);
         }

      }
   }

   private void responseCheck(Object var1) {
   }

   private boolean sameNamedDestination(DestinationImpl var1, DestinationImpl var2) {
      return var1 != null && var2 != null && var1.getServerName().equals(var2.getServerName()) && var1.getName().equals(var2.getName());
   }

   private int interceptionPoint(int var1, FEProducerSendRequest var2) throws JMSException {
      synchronized(interceptionPointLock) {
         this.interceptionException = null;
         InterceptionPointHandle var4 = this.IPHandles[var1];
         DestinationImpl var5 = this.IPDestinations[var1];
         if (var4 != null && !this.sameNamedDestination(var5, var2.getDestination())) {
            try {
               MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(var4);
            } catch (InterceptionServiceException var10) {
               throw new AssertionError("Failure to unregister" + var10);
            }

            var4 = null;
            this.IPHandles[var1] = null;
            this.IPDestinations[var1] = null;
         }

         if (var4 == null) {
            String[] var6 = new String[3];
            this.IPDestinations[var1] = var2.getDestination();
            var6[0] = var2.getDestination().getServerName();
            var6[1] = var2.getDestination().getName();
            var6[2] = IPStrings[var1];
            if (var6[0] == null) {
               var6[0] = new String();
            }

            if (var6[1] == null) {
               var6[1] = new String();
            }

            try {
               var4 = MessageInterceptionService.getSingleton().registerInterceptionPoint("JMS", var6);
            } catch (InterceptionServiceException var9) {
               throw new weblogic.jms.common.JMSException("FAILED registerInterceptionPoint " + var9);
            }

            this.IPHandles[var1] = var4;
         }

         try {
            if (var4.hasAssociation()) {
               JMSMessageContextImpl var15 = new JMSMessageContextImpl(var2.getMessage());
               var15.setDestination(var2.getDestination());
               var15.setUser(JMSSecurityHelper.getSimpleAuthenticatedName());
               var15.setFailover((JMSFailover)var2.getFailover());
               var2.setState(IPNextStates[var1]);
               var2.needOutsideResult();
               this.currentRequest = var2;
               var4.processAsync(var15, this);
               byte var10000 = 2;
               return var10000;
            }
         } catch (InterceptionException var11) {
            throw new weblogic.jms.common.JMSException("Processor: " + var11);
         } catch (MessageContextException var12) {
            throw new weblogic.jms.common.JMSException("Processor: " + var12);
         } catch (InterceptionServiceException var13) {
            throw new weblogic.jms.common.JMSException("Processor: " + var13);
         }

         return 1;
      }
   }

   private void initializeRoutingCriteria(FEProducerSendRequest var1) throws JMSException {
      assert var1.getDestination() != null;

      String var2 = var1.getMessage().getUnitOfOrder();
      if (var1.getDestination() instanceof DistributedDestinationImpl || var2 != null) {
         String var3 = null;

         try {
            var3 = var1.getMessage().getStringProperty("JMS_BEA_UnitOfWork");
         } catch (JMSException var5) {
            throw new AssertionError("We don't have exceptions on getProperty in the server");
         }

         if (var2 != null && var3 != null) {
            throw new weblogic.jms.common.JMSException("A JMS message cannot have both a Unit Of Order property and a Unit Of Work Property");
         } else if (var1.getDestination() instanceof DistributedDestinationImpl) {
            if (var2 != null) {
               var1.setUnitForRouting(var2);
            } else {
               DDHandler var4 = DDManager.findDDHandlerByDDName(var1.getDestination().getName());
               if (var4 != null && var4.isUOWDestination()) {
                  var1.setUnitForRouting(var3);
               } else {
                  var1.setUnitForRouting((String)null);
               }
            }

         }
      }
   }

   private void pickFirstDestination(FEProducerSendRequest var1) throws JMSException {
      if (var1.getDestination() == null) {
         throw new weblogic.jms.common.JMSException("Null destination");
      } else {
         this.initializeRoutingCriteria(var1);
         DestinationImpl var2 = var1.getDestination();
         if (this.pinned) {
            var1.setDestination(this.getPinnedDest(var1.getDestination(), var1.getMessage().getAdjustedDeliveryMode() == 2));
         } else {
            var1.setDestination((DestinationImpl)null);
         }

         if (var1.getDestination() == null || var1.getUnitForRouting() != null) {
            var1.setDestination(var2);
            if (var1.getUnitForRouting() != null) {
               this.selectUOOMember(var1, this.session);
            } else {
               var1.setDestination(this.computeTypicalLoadBalance((FEDDHandler)null, var1, this.session));
            }
         }

         if (this.pinned && var1.getDestination() instanceof DistributedDestinationImpl) {
            this.addPinnedDest(var1.getDestination());
         }

         if (var1.getDestination() == null) {
            throw new weblogic.jms.common.JMSException("no failover destination");
         }
      }
   }

   private static boolean updateInitCause(String var0, Throwable var1) {
      if (var1.getCause() == null) {
         try {
            var1.initCause(new JMSException(var0));
            return false;
         } catch (Throwable var3) {
         }
      }

      return true;
   }

   private static JMSException linkedException(JMSException var0, String var1) {
      try {
         var0.setLinkedException(new weblogic.jms.common.JMSException(var1));
      } catch (IllegalStateException var3) {
      }

      return var0;
   }

   private JMSException cannotFailoverException(String var1, Throwable var2) throws JMSException {
      if (var2 instanceof JMSException) {
         JMSException var3 = (JMSException)var2;
         if (updateInitCause(var1, var3) && var3.getLinkedException() == null) {
            linkedException(var3, var1);
         }

         throw var3;
      } else {
         updateInitCause(var1, var2);
         throw JMSUtilities.throwJMSOrRuntimeException(var2);
      }
   }

   private void determineFailOver(FEProducerSendRequest var1, Throwable var2) throws JMSException {
      FEDDHandler var3 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
      if (var3 == null) {
         throw this.cannotFailoverException("failover is null", var2);
      } else {
         JMSFailover var4 = var3.getProducerFailover((DistributedDestinationImpl)var1.getDestination(), var2, var1.getMessage().getAdjustedDeliveryMode() == 2, this.session);
         if (var4 == null) {
            throw this.cannotFailoverException("failover is null", var2);
         } else {
            var1.setFailover(var4);
         }
      }
   }

   private void stickyMaybePickNext(FEProducerSendRequest var1, Throwable var2) {
      if (!JMSFailover.isRecoverableFailure(var2)) {
         this.ratifyDestinationMember(var1);
         var1.setDestination((DestinationImpl)null);
      } else {
         if (this.stickyDests != null && this.stickyDests.get(var1.getDestination().getName()) != null) {
            if (var1.getNumberOfRetries() == 0) {
               DistributedDestinationImpl var3 = (DistributedDestinationImpl)var1.getDestination();
               var1.setDestination(DDManager.findDDImplByMemberName(var3.getInstanceName()));
               if (var1.getDestination() != null) {
                  this.ratifyDestinationMember(var1);
               }
            } else {
               var1.setDestination((DestinationImpl)null);
            }
         } else {
            var1.setDestination(((JMSFailover)var1.getFailover()).failover((DistributedDestinationImpl)var1.getDestination(), var2));
         }

      }
   }

   private void pickNextDestination(FEProducerSendRequest var1, Throwable var2) throws JMSException {
      if (var1.getUOONoFailover()) {
         throw this.cannotFailoverException("DD UUO cannot failover", var2);
      } else {
         if (var1.getFailover() == null) {
            this.determineFailOver(var1, var2);
         }

         FEDDHandler var3 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
         if (var3.getLoadBalancingPolicy() == 2) {
            this.stickyMaybePickNext(var1, var2);
         } else {
            var1.setDestination(((JMSFailover)var1.getFailover()).failover((DistributedDestinationImpl)var1.getDestination(), var2));
         }

         if (var1.getDestination() == null) {
            if (var2 instanceof JMSException) {
               throw this.cannotFailoverException("No destination to failover. ", var2);
            } else {
               throw new weblogic.jms.common.JMSException("No failover destination. ", var2);
            }
         }
      }
   }

   private void checkAndProcessStaleness(FEProducerSendRequest var1) throws weblogic.jms.common.JMSException {
      DestinationImpl var2 = var1.getDestination();
      boolean var3 = var2.isStale();
      DestinationImpl var4 = (DestinationImpl)this.staleDestsLRUMap.get(var2);
      if (var4 != null) {
         var1.setDispatcher((JMSDispatcher)null);
         var1.setDestination(var4);
      } else if (var3) {
         var1.setDispatcher((JMSDispatcher)null);

         try {
            var4 = this.session.getConnection().createDestination(var2);
            this.staleDestsLRUMap.put(var2, var4);
            var1.setDestination(var4);
         } catch (JMSException var6) {
            throw new weblogic.jms.common.JMSException("JMSException while creating destination. ", var6);
         }
      }
   }

   private void updateProducerDestination(FEProducerSendRequest var1) {
      if (this.producerDestination != null) {
         DestinationImpl var2 = this.producerDestination;
         this.addAndNormalizeDestination(var1.getDestination(), var1.getDispatcher());
         if (this.producerDispatcher != null) {
            this.producerDispatcher.removeDispatcherPeerGoneListener(new DestinationPeerGoneAdapter(var2, (FEConnection)null));
            this.producerDispatcher = var1.getDispatcher();
         }

      }
   }

   private void findDispatcher(FEProducerSendRequest var1) throws JMSException {
      if (var1.getDispatcher() == null && var1.getDestination() != null) {
         try {
            SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);
            var1.setDispatcher(JMSDispatcherManager.dispatcherFindOrCreate(var1.getDestination().getDispatcherId()));
         } catch (DispatcherException var7) {
            throw new weblogic.jms.common.JMSException("Error producing message for destination " + var1.getDestination().getName(), var7);
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         this.updateProducerDestination(var1);
      }

   }

   private void setupTransactionRelated(FEProducerSendRequest var1) throws JMSException {
      if (this.session.isTransacted() && !var1.isInfected()) {
         if (var1.getMessage().propertyExists("JMS_BEA_SAF_SEQUENCE_NAME")) {
            this.session.transactedInfect(true);
         } else {
            this.session.transactedInfect();
         }

         var1.setInfected(true);
      }

   }

   private int doDispatch(FEProducerSendRequest var1, Request var2) throws JMSException {
      var1.getMessage().setClientId(this.session.getConnection().getConnectionClientId());
      BEProducerSendRequest var3 = new BEProducerSendRequest(var1.getDestination().getId(), var1.getMessage(), var1.getDestination().isQueue() ? null : this.session.getConnection().getJMSID(), var1.getSendTimeout(), this.producerId);
      var1.setBackendRequest(var3);
      if (var1.getDestination() instanceof DistributedDestinationImpl) {
         var3.setCheckUOO(var1.getCheckUOO());
      }

      if (JMSDebug.JMSMessagePath.isDebugEnabled() || JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         messageOrFrontEndDebug("FRONTEND/FEProducer: Dispatching message to BACKEND/BEDestination");
      }

      try {
         var2.dispatchAsync(var1.getDispatcher(), var3);
      } catch (DispatcherException var14) {
         throw new weblogic.jms.common.JMSException("Error sending message", var14);
      }

      if (var1.isNoResponse()) {
         return 1;
      } else {
         int var4 = -117;
         byte var5 = -117;

         byte var7;
         try {
            synchronized(var3) {
               if (var3.getState() != Integer.MAX_VALUE) {
                  if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                     var4 = var3.getState();
                  }

                  if (var3.getState() != -42 || !var3.hasResults() && !var1.hasResults()) {
                     var5 = 2;
                     var7 = 2;
                     return var7;
                  }

                  var5 = 1;
                  var7 = 1;
                  return var7;
               }

               var5 = 1;
               var7 = 1;
            }
         } finally {
            if (var4 != -117) {
               JMSDebug.JMSFrontEnd.debug("feproducer doDispatch state=" + var4 + ", returnState=" + var5 + ", hasResults=" + var3.hasResults() + ", " + var3);
            }

         }

         return var7;
      }
   }

   private static void messageOrFrontEndDebug(String var0) {
      if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
         JMSDebug.JMSMessagePath.debug(var0);
      } else {
         JMSDebug.JMSFrontEnd.debug(var0);
      }

   }

   private void updateStatistics(FEProducerSendRequest var1) {
      MessageImpl var2 = var1.getMessage();
      if (!var1.isInfected() && !this.session.isTransacted()) {
         this.incMessagesSentCount(var2.getPayloadSize() + (long)var2.getUserPropertySize());
         this.session.getStatistics().incrementSentCount(var2);
      } else {
         this.session.transactionStat((FEConsumer)null, this, var2);
      }

   }

   private void cleanupTransactionRelated(FEProducerSendRequest var1) throws JMSException {
      if (var1.isInfected()) {
         this.session.transactedDisinfect();
         var1.setInfected(false);
      }

   }

   private void sendRetryDestination(FEProducerSendRequest var1) throws JMSException {
      Throwable var2 = null;

      while(var1.getState() != Integer.MAX_VALUE) {
         switch (var1.getState()) {
            case 1:
               break;
            case 3:
               var1.setUpPushPopSubject(true);
               this.setupFailover(var1);
               this.throwIfUOOFanoutMessage(var1, var2);
               this.pickNextDestination(var1, var2);
               var1.setNumberOfRetries(var1.getNumberOfRetries() + 1);
               continue;
            case 4:
               this.pickFirstDestination(var1);
               AuthenticatedSubject var3 = JMSSecurityHelper.getCurrentSubject();
               var1.setAuthenticatedSubject(var3);
            case 2:
               if (var1.getDestination() != null && this.interceptionPoint(1, var1) == 2) {
                  return;
               }
            case 5:
               Throwable var5;
               try {
                  this.findDispatcher(var1);
               } catch (JMSException var20) {
                  var5 = var20.getCause();
                  if (var5 instanceof DispatcherException) {
                     var2 = var5;
                     var1.setState(3);
                     continue;
                  }

                  throw var20;
               }

               this.setupTransactionRelated(var1);
               var1.setState(1);

               try {
                  if (var1.getPushPopSubject()) {
                     SecurityServiceManager.pushSubject(KERNEL_ID, (AuthenticatedSubject)var1.getAuthenticatedSubject());
                  }

                  if (this.doDispatch(var1, var1) != 2) {
                     break;
                  }
               } catch (JMSOrderException var21) {
                  this.setupFailover(var1);
                  this.throwIfUOOFanoutMessage(var1, var21);
                  this.processUOOCache(var1, (Object)null, var21);
                  continue;
               } catch (JMSException var22) {
                  var5 = var22.getCause();
                  if (var5 instanceof DispatcherException) {
                     var2 = var5;
                     var1.setState(3);
                     continue;
                  }

                  throw var22;
               } finally {
                  if (var1.getPushPopSubject()) {
                     SecurityServiceManager.popSubject(KERNEL_ID);
                  }

               }

               return;
            default:
               continue;
         }

         Response var4;
         try {
            this.setupTransactionRelated(var1);
            if (var1.getBackendRequest().hasResults()) {
               var4 = var1.getBackendRequest().getResult();
            } else {
               var1.getBackendRequest().waitForNotRunningResult();
               var4 = var1.getBackendRequest().getResult();
            }

            var1.setResult(var4);
         } catch (Error var16) {
            throw var16;
         } catch (RuntimeException var17) {
            throw var17;
         } catch (JMSOrderException var18) {
            this.setupFailover(var1);
            this.throwIfUOOFanoutMessage(var1, var18);
            this.processUOOCache(var1, (Object)null, var18);
            continue;
         } catch (Throwable var19) {
            JMSOrderException var6 = this.findOrderExceptionCause(var19);
            if (var6 != null) {
               this.setupFailover(var1);
               this.throwIfUOOFanoutMessage(var1, var6);
               this.processUOOCache(var1, (Object)null, var6);
               continue;
            }

            if (!JMSFailover.isRecoverableFailure(var19)) {
               var1.setState(Integer.MAX_VALUE);
               var1.resumeRequest(var19, var1.getBackendRequest().isCollocated());
               return;
            }

            var2 = var19;
            var1.setState(3);
            continue;
         }

         this.responseCheck(var4);
         this.processUOOCache(var1, var4, (JMSOrderException)null);
         this.ratifyDestinationMember(var1);
         var1.setState(Integer.MAX_VALUE);
         return;
      }

   }

   private void throwIfUOOFanoutMessage(FEProducerSendRequest var1, Throwable var2) throws JMSException {
      if (var1.getMessage().getControlOpcode() == 196608) {
         throw this.cannotFailoverException("control DD cannot failover", var2);
      }
   }

   private void ratifyDestinationMember(FEProducerSendRequest var1) {
      FEDDHandler var2 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
      if (var2 != null) {
         if (var2.getLoadBalancingPolicy() == 2) {
            if (this.stickyDests == null) {
               this.stickyDests = new HashMap();
            }

            this.stickyDests.put(var1.getDestination().getName(), var1.getDestination());
         }
      }
   }

   private void setupFailover(FEProducerSendRequest var1) {
      this.cleanFailure(var1.getDestination());
      var1.setDispatcher((JMSDispatcher)null);
      var1.clearState();
      var1.setState(2);
   }

   private void validateMessageOnSingleDestination(FEProducerSendRequest var1) throws JMSException {
      this.initializeRoutingCriteria(var1);
   }

   private void sendSingleDestination(FEProducerSendRequest var1) throws JMSException {
      while(var1.getState() != Integer.MAX_VALUE) {
         switch (var1.getState()) {
            case 4:
               this.validateMessageOnSingleDestination(var1);
               if (this.producerDestination == null) {
                  this.pickFirstDestination(var1);
                  if (var1.getDestination() instanceof DistributedDestinationImpl) {
                     var1.setState(2);
                     this.sendRetryDestination(var1);
                     return;
                  }
               } else {
                  var1.setDispatcher(this.producerDispatcher);
               }
            case 2:
               this.checkAndProcessStaleness(var1);
               if (this.interceptionPoint(1, var1) == 2) {
                  return;
               }
            case 5:
               this.findDispatcher(var1);
               this.setupTransactionRelated(var1);
               var1.setState(1);
               if (this.doDispatch(var1, var1) == 2) {
                  return;
               }
            case 1:
               try {
                  Response var2 = null;
                  this.setupTransactionRelated(var1);
                  if (var1.isNoResponse()) {
                     var1.setState(Integer.MAX_VALUE);
                  } else {
                     var2 = var1.getBackendRequest().getResult();
                     this.responseCheck(var2);
                     var1.setResult(var2);
                     var1.setState(Integer.MAX_VALUE);
                  }

                  return;
               } catch (JMSException var3) {
                  this.handleStaleDest(var1, var3);
               }
            case 3:
               var1.clearResult();
               var1.setState(2);
         }
      }

   }

   private void handleStaleDest(FEProducerSendRequest var1, JMSException var2) throws JMSException {
      if (FESession.isStaleDestEx(var2) && var1.getNumberOfRetries() == 0) {
         var1.getDestination().markStale();
         var1.setNumberOfRetries(var1.getNumberOfRetries() + 1);
         var1.setState(3);
      } else {
         throw var2;
      }
   }

   private void checkInterceptionReturn(FEProducerSendRequest var1, int var2) throws JMSException {
      if (this.interceptionException != null) {
         throw new JMSException("Interception exception" + this.interceptionException);
      } else {
         if (!this.interceptionSaidContinue) {
            JMSMessageId var3;
            try {
               var3 = JMSService.getService().getNextMessageId();
            } catch (ManagementException var5) {
               throw new weblogic.jms.common.JMSException("Failed to get MessageID: " + var5);
            }

            var1.setResult(new JMSProducerSendResponse(var3));
            var1.setState(Integer.MAX_VALUE);
         } else {
            var1.setState(var2);
         }

      }
   }

   public void onCallBack(boolean var1) {
      this.interceptionSaidContinue = var1;
      this.currentRequest.resumeExecution(true);
      this.currentRequest = null;
   }

   public void onException(InterceptionProcessorException var1) {
      this.interceptionException = var1;
      this.currentRequest.resumeExecution(true);
      this.currentRequest = null;
   }

   private int send(FEProducerSendRequest var1) throws JMSException {
      this.session.checkShutdownOrSuspended();
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("FEProducer.send() : state = " + var1.getState());
      }

      boolean var2;
      do {
         boolean var3 = false;
         var2 = false;

         int var4;
         try {
            switch (var1.getState()) {
               case 0:
                  MessageTimeStamp.record(1, var1.getMessage());
                  if (this.inSend) {
                  }

                  this.inSend = true;
                  MessageImpl var12 = var1.getMessage();
                  boolean var5 = false;
                  if (var12.getId() == null) {
                     var12.setId(JMSService.getJMSService().getNextMessageId());
                     var5 = true;
                  }

                  if (var12.getJMSExpiration() != 0L) {
                     var12.setJMSExpiration((var5 ? var12.getJMSTimestamp() : System.currentTimeMillis()) + var12.getJMSExpiration());
                  }

                  if (var12.getDeliveryTime() > 0L) {
                     var12.setDeliveryTime((var5 ? var12.getJMSTimestamp() : System.currentTimeMillis()) + var12.getDeliveryTime());
                  }

                  if (var12.isForwardable()) {
                     var12.incForwardsCount();
                     var12.requestJMSXUserID(false);
                  } else {
                     if (this.session.getConnection().getAttachJMSXUserID()) {
                        var12.requestJMSXUserID(true);
                     } else {
                        var12.requestJMSXUserID(false);
                     }

                     var12.setJMSXUserID((String)null);
                  }

                  if (this.producerDestination != null) {
                     var1.setDestination(this.producerDestination);
                  }

                  SAFReplyHandler.process(var12);
                  if (this.interceptionPoint(0, var1) == 2) {
                     int var6 = var1.getState();
                     return var6;
                  }

                  var1.setState(4);
               case 1:
               case 2:
               case 3:
               case 4:
               case 5:
               default:
                  DestinationImpl var13 = var1.getDestination();
                  DestinationImpl var7 = this.getDestinationForInterop(var13);
                  if (var7 != null) {
                     var13 = var7;
                     var1.setDestination(var7);
                  }

                  if (var13 instanceof DistributedDestinationImpl) {
                     this.sendRetryDestination(var1);
                  } else {
                     this.sendSingleDestination(var1);
                  }

                  var3 = true;
                  continue;
               case 6:
                  this.checkInterceptionReturn(var1, 4);
                  var2 = true;
                  continue;
               case 7:
                  this.checkInterceptionReturn(var1, 5);
                  var2 = true;
                  continue;
               case 8:
            }

            var4 = this.releaseFanoutComplete(var1);
         } finally {
            if (!var3) {
               this.inSend = false;
               this.cleanupTransactionRelated(var1);
            }

         }

         return var4;
      } while(var2);

      if (var1.getState() == Integer.MAX_VALUE) {
         this.inSend = false;
         this.updateStatistics(var1);
      }

      this.cleanupTransactionRelated(var1);
      return var1.getState();
   }

   private DestinationImpl getDestinationForInterop(DestinationImpl var1) {
      if (var1 instanceof DistributedDestinationImpl) {
         return null;
      } else {
         DistributedDestinationImpl var2 = DDManager.findDDImplByDDName(var1.getName());
         return var2;
      }
   }

   public JMSID getJMSID() {
      return this.producerId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(weblogic.messaging.dispatcher.Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 4617:
            this.session.checkShutdownOrSuspended();
            this.session.producerClose(this);
            this.removeDispatcher();
            var1.setResult(new VoidResponse());
            var1.setState(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
         case 5129:
            FEProducerSendRequest var2 = (FEProducerSendRequest)var1;
            if (var2.getMessage().getControlOpcode() != 65536) {
               return this.send(var2);
            }

            return this.controlSequenceReleaseFanout(var2);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }
   }

   private int controlSequenceReleaseFanout(FEProducerSendRequest var1) throws JMSException {
      DestinationImpl var2;
      if (this.producerDestination != null) {
         var2 = this.producerDestination;
      } else {
         var2 = var1.getDestination();
      }

      if (!(var2 instanceof DistributedDestinationImpl)) {
         return this.send(var1);
      } else {
         FEDDHandler var3 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
         if (var3 == null) {
            return this.send(var1);
         } else {
            DDHandler var4 = var3.getDDHandler();
            int var5 = var4.getNumberOfMembers();
            if (var5 == 0) {
               return this.send(var1);
            } else {
               Object var7 = null;
               FEProducerSendRequest[] var8 = new FEProducerSendRequest[var5];

               int var6;
               try {
                  SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

                  for(var6 = 0; var6 < var5; ++var6) {
                     DistributedDestinationImpl var9 = var4.getMemberByIndex(var6).getDDImpl();
                     var8[var6] = new FEProducerSendRequest(this.producerId, var1.getMessage().cloneit(), var9, var1.getSendTimeout(), var1.getCompressionThreshold());

                     try {
                        var8[var6].setDispatcher(JMSDispatcherManager.dispatcherFindOrCreate(var9.getDispatcherId()));
                     } catch (DispatcherException var18) {
                        var7 = new weblogic.jms.common.JMSException(var18);
                     }
                  }
               } finally {
                  SecurityServiceManager.popSubject(KERNEL_ID);
               }

               var1.needOutsideResult();

               for(var6 = 0; var6 < var5; ++var6) {
                  try {
                     if (var8[var6].getDispatcher() != null) {
                        this.doDispatch(var8[var6], var1);
                     } else {
                        assert var7 != null;
                     }
                  } catch (JMSException var20) {
                     if (var7 == null) {
                        var7 = var20;
                     }
                  }
               }

               if (var7 != null) {
                  throw var7;
               } else {
                  var1.setSubRequest(var8);
                  synchronized(var1) {
                     if (!var1.fanoutCompleteSuspendIfHaveChildren(false)) {
                        var1.setState(8);
                        return 8;
                     }
                  }

                  return this.releaseFanoutComplete(var1);
               }
            }
         }
      }
   }

   private int releaseFanoutComplete(FEProducerSendRequest var1) throws JMSException {
      synchronized(var1) {
         var1.setState(Integer.MAX_VALUE);
         if (this.hasProducerResult(var1)) {
            return Integer.MAX_VALUE;
         }
      }

      FEProducerSendRequest[] var2 = var1.getSubRequest();
      JMSProducerSendResponse var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         synchronized(var2[var4]) {
            if (var2[var4].hasResults()) {
               var3 = (JMSProducerSendResponse)var2[var4].getResult();
               break;
            }
         }
      }

      assert var3 != null;

      synchronized(var1) {
         if (this.hasProducerResult(var1)) {
            return Integer.MAX_VALUE;
         } else {
            var1.setResult(var3);
            return Integer.MAX_VALUE;
         }
      }
   }

   private boolean hasProducerResult(FEProducerSendRequest var1) throws JMSException {
      Response var2 = null;
      if (var1.hasResults()) {
         var2 = var1.getResult();
         if (var2 instanceof JMSProducerSendResponse) {
            return true;
         }
      }

      assert var2 == null;

      return false;
   }

   void closeProducer() {
      synchronized(cacheUOOLock) {
         this.cacheUOOMember = null;
      }

      synchronized(interceptionPointLock) {
         InterceptionPointHandle var2 = this.IPHandles[0];
         InterceptionPointHandle var3 = this.IPHandles[1];
         DestinationImpl var4 = this.IPDestinations[0];
         DestinationImpl var5 = this.IPDestinations[1];
         if (var4 != null && (var4.getType() == 8 || var4.getType() == 4)) {
            try {
               if (var2 != null && !var2.hasAssociation()) {
                  MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(var2);
                  this.IPHandles[0] = null;
                  this.IPDestinations[0] = null;
               }
            } catch (InterceptionServiceException var10) {
               JMSLogger.logFailedToUnregisterInterceptionPoint(var10);
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FEProducer.close(), Failure to unregister startIPHandle " + var10);
               }
            }
         }

         if (var5 != null && (var5.getType() == 8 || var5.getType() == 4)) {
            try {
               if (var3 != null && !var3.hasAssociation()) {
                  MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(var3);
                  this.IPHandles[1] = null;
                  this.IPDestinations[1] = null;
               }
            } catch (InterceptionServiceException var9) {
               JMSLogger.logFailedToUnregisterInterceptionPoint(var9);
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FEProducer.close(), Failure to unregister postAuthIPHandle " + var9);
               }
            }
         }

      }
   }

   private void selectUOOMember(FEProducerSendRequest var1, FESession var2) throws JMSException {
      if (var1.getDestination() instanceof DistributedDestinationImpl) {
         FEDDHandler var3 = DDManager.findFEDDHandlerByDDName(var1.getDestination().getName());
         if (var3 == null) {
            throw new JMSOrderException("could not find distributed destination " + var1.getDestination().getName());
         } else {
            var1.setUooNoFailover(true);
            String var4 = var3.getUnitOfOrderRouting();
            if ("Hash".equals(var4)) {
               var1.setDestination(UOOHelper.getHashBasedDestination(var3, var1.getUnitForRouting()));
            } else {
               if (PathHelper.retired && PathHelper.PathSvcVerbose.isDebugEnabled()) {
                  PathHelper.PathSvcVerbose.debug("FEProducer DD:" + var1.getDestination().getName() + ", JndiName " + this.pathJndiName);
               }

               assert "PathService".equals(var4);

               this.pathJndiName = PathHelper.DEFAULT_PATH_SERVICE_JNDI;
               var1.setCheckUOO(2097152);
               synchronized(cacheUOOLock) {
                  KeyString var6;
                  if (this.cacheUOOMember != null && this.cacheUOOMember.getKey().getStringId().equals(var1.getUnitForRouting()) && this.cacheUOOMember.getKey().getAssemblyId().equals(var1.getDestination().getName())) {
                     var6 = this.cacheUOOMember.getKey();
                  } else {
                     this.cacheUOOMember = null;
                     var6 = new KeyString((byte)1, var1.getDestination().getName().intern(), var1.getUnitForRouting().intern());
                  }

                  if (this.cacheUOOMember != null && this.cacheUOOMember.isCancelled()) {
                     this.cacheUOOMember = null;
                  }

                  var1.setUOOInfo(this.pathJndiName, var6);

                  BEUOOMember var7;
                  try {
                     var7 = (BEUOOMember)PathHelper.manager().cachedGet(this.pathJndiName, var6, 576);
                  } catch (NamingException var14) {
                     PathHelper.PathSvcVerbose.debug("FEProd cache unavailable Key:" + var6, var14);
                     throw new JMSOrderException(var14.getMessage(), var14);
                  } catch (Throwable var15) {
                     PathHelper.PathSvc.debug("FEProd get Key:" + var6, var15);
                     throw JMSUtilities.throwJMSOrRuntimeException(PathHelper.wrapExtensionImpl(var15));
                  }

                  if (var7 != this.cacheUOOMember) {
                     if (var7 instanceof CacheUOOMember) {
                        if (this.cacheUOOMember != null && this.cacheUOOMember.isCancelled()) {
                           this.cacheUOOMember = null;
                        }

                        if (this.cacheUOOMember == null) {
                           this.cacheUOOMember = (CacheUOOMember)var7;
                        } else {
                           this.cacheUOOMember.merge(var7);
                        }
                     } else if (this.cacheUOOMember == null) {
                        this.cacheUOOMember = new CacheUOOMember(var6, var7, System.currentTimeMillis() + (long)TTL_GUESS);
                     } else if (var7 != null) {
                        this.cacheUOOMember.merge(var7);
                     }
                  }

                  if (this.cacheUOOMember != null) {
                     if (!UOOHelper.cacheUpToDate(var3, (String)this.cacheUOOMember.getMemberId(), this.cacheUOOMember.getLastHasConsumers(), var1.getMessage())) {
                        this.cacheUOOMember = null;
                     } else {
                        this.cacheUOOMember.setLastHasConsumers(UOOHelper.hasConsumers(var3, (String)this.cacheUOOMember.getMemberId()));
                     }
                  }

                  DistributedDestinationImpl var8;
                  if (this.cacheUOOMember == null) {
                     var8 = (DistributedDestinationImpl)this.computeTypicalLoadBalance(var3, var1, var2);
                     if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
                        PathHelper.PathSvcVerbose.debug("FEProd cache miss Key:" + var6 + ", balance:" + var1.getDestination().getName());
                     }

                     this.cacheUOOMember = new CacheUOOMember(var6, var8.getMemberName(), var8.getServerName(), var8.getNonSystemSubscriberConsumers() != 0, (long)TTL_GUESS);
                  } else {
                     var8 = null;
                  }

                  if (PathHelper.retired && PathHelper.PathSvcVerbose.isDebugEnabled()) {
                     PathHelper.PathSvcVerbose.debug("FEProd cache hit Key:" + var6 + ", got:" + this.cacheUOOMember);
                  }

                  String var9 = this.cacheUOOMember.getStringId();
                  var1.setDestination(DDManager.findDDImplByMemberName(var9));
                  if (var1.getDestination() == null) {
                     throw new JMSOrderException("unable to reach member " + var9 + " of " + var3.getName() + " from keys " + var3.getDDHandler().debugKeys() + (var8 == null ? " cached " : " typicalLoadBalance ") + this.cacheUOOMember.getStringId());
                  } else if (var7 instanceof CacheUOOMember) {
                     if (var7 != this.cacheUOOMember) {
                        ((CacheUOOMember)var7).copyValues(this.cacheUOOMember, this.cacheUOOMember.getExpireTime());
                     }

                  } else {
                     try {
                        CompletionRequest var10 = new CompletionRequest();
                        PathHelper.manager().cachedPutIfAbsent(this.pathJndiName, this.cacheUOOMember.getKey(), this.cacheUOOMember, 512, var10);
                        var10.getResult();
                     } catch (NamingException var12) {
                        PathHelper.PathSvcVerbose.debug("FEProd cache unavailable Key:" + var6, var12);
                        throw new JMSOrderException(var12.getMessage(), var12);
                     } catch (Throwable var13) {
                        PathHelper.PathSvc.debug("FEProd get Key:" + var6, var13);
                        throw JMSUtilities.throwJMSOrRuntimeException(PathHelper.wrapExtensionImpl(var13));
                     }

                  }
               }
            }
         }
      }
   }

   private DestinationImpl computeTypicalLoadBalance(FEDDHandler var1, FEProducerSendRequest var2, FESession var3) throws JMSException {
      if (var1 == null) {
         assert var2.getDestination() != null;

         var1 = DDManager.findFEDDHandlerByDDName(var2.getDestination().getName());
      }

      if (var1 == null) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("DD named: " + var2.getDestination().getName() + " not found");
         }

         return var2.getDestination();
      } else {
         if (var1.getLoadBalancingPolicy() == 2 && this.stickyDests != null) {
            DestinationImpl var4 = (DestinationImpl)this.stickyDests.get(var2.getDestination().getName());
            if (var4 != null) {
               return var4;
            }
         }

         boolean var6 = var2.getMessage().getAdjustedDeliveryMode() == 2;
         DestinationImpl var5 = var1.producerLoadBalance(var6, var3);
         if (var5 != null) {
            return var5;
         } else {
            if (JMSDebug.JMSMessagePath.isDebugEnabled() || JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("Load Balancer can't find a candidate for load balancing for DD: " + var2.getDestination().getName());
            }

            throw new weblogic.jms.common.JMSException("Distributed Destination " + var2.getDestination().getName() + " does not have any member destinations which are active");
         }
      }
   }

   private JMSOrderException findOrderExceptionCause(Throwable var1) {
      while(var1 != null) {
         if (var1 instanceof JMSOrderException) {
            return (JMSOrderException)var1;
         }

         var1 = var1.getCause();
      }

      return null;
   }

   private void processUOOCache(FEProducerSendRequest var1, Object var2, JMSOrderException var3) throws JMSOrderException {
      if (JMSDebug.JMSMessagePath.isDebugEnabled() || JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         messageOrFrontEndDebug("FEProducer DD UOO Failover recalculation");
      }

      BEUOOMember var4;
      if (var3 != null) {
         JMSOrderException var5 = var3;

         while(true) {
            var4 = (BEUOOMember)var5.getMember();
            if (var4 != null) {
               var5.setMember((Serializable)null);
               var1.setDestination(DDManager.findDDImplByMemberName((String)var4.getMemberId()));
               if (var1.getDestination() == null) {
                  throw new JMSOrderException("unable to contact member " + var4.getMemberId() + ", keys are " + DDManager.debugKeys(), var3);
               }
               break;
            }

            var5 = this.findOrderExceptionCause(var5.getCause());
            if (var5 == null) {
               throw var3;
            }
         }
      } else {
         JMSProducerSendResponse var22 = (JMSProducerSendResponse)var2;
         var4 = (BEUOOMember)((BEUOOMember)var22.getUOOInfo());
         var22.setUOOInfo((Serializable)null);
      }

      var1.setNumberOfRetries(var1.getNumberOfRetries() + 1);
      if (var1.getMessage().getControlOpcode() == 196608) {
         synchronized(cacheUOOLock) {
            if (this.cacheUOOMember != null) {
               this.cacheUOOMember.setCancelled(true);

               try {
                  PathHelper.manager().cachedRemove(this.pathJndiName, this.cacheUOOMember.getKey(), this.cacheUOOMember, 512, new CompletionRequest());
               } catch (NamingException var17) {
                  throw new JMSOrderException(var17.getMessage(), var17);
               } finally {
                  this.cacheUOOMember = null;
               }

            }
         }
      } else if (var4 == null) {
         if (var1.getUOOKey() != null) {
            synchronized(cacheUOOLock) {
               if (this.cacheUOOMember != null) {
                  this.cacheUOOMember.setExpireTime(TTL_CONFIRMED);
               }
            }
         }

      } else {
         if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
            PathHelper.PathSvcVerbose.debug("FE Caching update " + var4 + " for key " + var1.getUOOKey());
         }

         synchronized(cacheUOOLock) {
            assert this.cacheUOOMember != null;

            this.cacheUOOMember.copyValues(var4, System.currentTimeMillis() + (long)TTL_CONFIRMED);
         }
      }
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Producer");
      var2.writeAttribute("id", this.producerId != null ? this.producerId.toString() : "");
      var2.writeAttribute("messagesSentCount", String.valueOf(this.messagesSentCount));
      var2.writeAttribute("messagesPendingCount", String.valueOf(this.messagesPendingCount));
      var2.writeAttribute("bytesSentCount", String.valueOf(this.bytesSentCount));
      var2.writeAttribute("bytesPendingCount", String.valueOf(this.bytesPendingCount));
      var2.writeAttribute("isPinned", String.valueOf(this.pinned));
      if (this.cacheUOOMember != null) {
         var2.writeAttribute("uooMemberKey", this.cacheUOOMember.getKey().toString());
      }

      if (this.producerDestination != null) {
         var2.writeStartElement("Destination");
         JMSDiagnosticImageSource.dumpDestinationImpl(var2, this.producerDestination);
         var2.writeEndElement();
      }

      if (this.pinnedDests != null) {
         HashMap var3 = (HashMap)this.pinnedDests.clone();
         var2.writeStartElement("PinnedDestinations");
         var2.writeAttribute("currentCount", String.valueOf(var3.size()));
         Iterator var4 = var3.values().iterator();

         while(var4.hasNext()) {
            DestinationImpl var5 = (DestinationImpl)var4.next();
            var2.writeStartElement("Destination");
            JMSDiagnosticImageSource.dumpDestinationImpl(var2, var5);
            var2.writeEndElement();
         }

         var2.writeEndElement();
      }

      FEProducerSendRequest var6 = this.currentRequest;
      if (var6 != null) {
         var2.writeStartElement("CurrentSendRequest");
         var2.writeAttribute("jmsMessageID", var6.getMessage().getJMSMessageID().toString());
         var2.writeAttribute("sendTimeout", String.valueOf(var6.getSendTimeout()));
         var2.writeEndElement();
      }

      var2.writeEndElement();
   }

   public void removeDispatcher() {
      if (this.producerDispatcher != null) {
         this.producerDispatcher.removeDispatcherPeerGoneListener(new DestinationPeerGoneAdapter(this.producerDestination, (FEConnection)null));
      }

   }

   private class CacheUOOMember extends BEUOOMember implements ExtendedBEUOOMember {
      static final long serialVersionUID = 1771787601821860231L;
      private transient KeyString keyString;
      private transient boolean cancelled;
      private transient boolean lastHasConsumers;
      private transient long expireTime;

      CacheUOOMember(KeyString var2, BEUOOMember var3, long var4) {
         super(var3.getStringId(), var3.getWLServerName(), var3.getDynamic());
         this.keyString = var2;
         this.lastHasConsumers = false;
         this.copyValues(var3, var4);
      }

      CacheUOOMember(KeyString var2, String var3, String var4, boolean var5, long var6) {
         super(var3, var4, true);
         this.keyString = var2;
         this.lastHasConsumers = var5;
         this.expireTime = var6;
      }

      public CacheUOOMember() {
      }

      private void copyValues(BEUOOMember var1, long var2) {
         this.serverName = var1.getWLServerName();
         this.generation = var1.getGeneration();
         this.timestamp = var1.getTimeStamp();
         this.id = var1.getStringId();
         this.dynamic = var1.getDynamic();
         this.expireTime = var2;
         if (var1 instanceof CacheUOOMember) {
            this.keyString = ((CacheUOOMember)var1).keyString;
            this.lastHasConsumers = ((CacheUOOMember)var1).lastHasConsumers;
         }

      }

      private void merge(BEUOOMember var1) {
         long var2;
         if (var1 instanceof CacheUOOMember) {
            if (this.expireTime > ((CacheUOOMember)var1).expireTime) {
               return;
            }

            var2 = ((CacheUOOMember)var1).expireTime;
         } else {
            var2 = System.currentTimeMillis() + (long)FEProducer.TTL_GUESS;
            this.lastHasConsumers = false;
         }

         this.copyValues(var1, var2);
      }

      private KeyString getKey() {
         return this.keyString;
      }

      private void setLastHasConsumers(boolean var1) {
         this.lastHasConsumers = var1;
      }

      private boolean getLastHasConsumers() {
         return this.lastHasConsumers;
      }

      private long setExpireTime(int var1) {
         this.expireTime = System.currentTimeMillis() + (long)var1;
         return this.expireTime;
      }

      private long getExpireTime() {
         return this.expireTime;
      }

      private boolean isCancelled() {
         return this.cancelled;
      }

      private void setCancelled(boolean var1) {
         this.cancelled = var1;
      }
   }

   public interface ExtendedBEUOOMember extends Member {
      boolean getDynamic();

      String getStringId();

      void setTimestamp(long var1);

      void setGeneration(int var1);
   }
}
