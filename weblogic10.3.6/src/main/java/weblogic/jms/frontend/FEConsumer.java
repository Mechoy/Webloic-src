package weblogic.jms.frontend;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Vector;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.JMSLogger;
import weblogic.jms.backend.BEConsumerCloseRequest;
import weblogic.jms.backend.BEConsumerCreateRequest;
import weblogic.jms.backend.BEConsumerCreateResponse;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.BEConsumerIncrementWindowCurrentRequest;
import weblogic.jms.backend.BEConsumerIsActiveRequest;
import weblogic.jms.backend.BEConsumerIsActiveResponse;
import weblogic.jms.backend.BEConsumerReceiveRequest;
import weblogic.jms.backend.BEConsumerSetListenerRequest;
import weblogic.jms.backend.BERemoveSubscriptionRequest;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.DSManager;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSConsumerReceiveResponse;
import weblogic.jms.common.JMSConsumerSetListenerResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageContextImpl;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.MessageStatistics;
import weblogic.jms.common.Sequencer;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.ConsumerClosedException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSConsumerRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.interception.MessageInterceptionService;
import weblogic.messaging.interception.exceptions.InterceptionProcessorException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.messaging.interception.interfaces.CarrierCallBack;
import weblogic.messaging.interception.interfaces.InterceptionPointHandle;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class FEConsumer extends RuntimeMBeanDelegate implements JMSConsumerRuntimeMBean, Invocable, JMSPeerGoneListener, CarrierCallBack {
   static final long serialVersionUID = -8556954068817891651L;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private JMSID consumerId;
   private JMSDispatcher backEndDispatcher;
   private Sequencer sequencer;
   public static final String JNDI_SUBSCRIPTIONNAME = "weblogic.jms.internal.subscription";
   final MessageStatistics statistics = new MessageStatistics();
   private InvocableMonitor invocableMonitor;
   private FESession session;
   private DestinationImpl destination;
   private String selector;
   private boolean isDurable = false;
   private boolean isLocal = true;
   private String subject = null;
   private AuthenticatedSubject authenticatedSubject;
   private InterceptionPointHandle receiveIPHandle = null;
   private DestinationImpl receiveIPDestination = null;
   private static final int DONE = 1;
   private static final int IN_PROGRESS = 2;
   private Request currentRequest;
   private static Object interceptionPointLock = new Object();
   private ConsumerReconnectInfo consumerReconnectInfo;
   private int subscriptionSharingPolicy;
   private transient int refCount;

   public FEConsumer(String var1, FESession var2, Sequencer var3, String var4, DestinationImpl var5, JMSID var6, String var7, AuthenticatedSubject var8, FEConsumerCreateRequest var9) throws JMSException, ManagementException {
      super(var1, var2);

      try {
         byte var10 = 1;
         this.session = var2;
         this.sequencer = var3;
         this.destination = var5;
         this.consumerId = var6;
         this.subject = var7;
         this.authenticatedSubject = var8;
         this.subscriptionSharingPolicy = var9.getSubscriptionSharingPolicy();
         if (this.subscriptionSharingPolicy == -1) {
            this.subscriptionSharingPolicy = this.session.getSubscriptionSharingPolicy();
         }

         try {
            this.backEndDispatcher = JMSDispatcherManager.dispatcherFindOrCreate(var5.getDispatcherId());
            if (!this.backEndDispatcher.isLocal()) {
               this.isLocal = false;
            }
         } catch (DispatcherException var40) {
            throw new weblogic.jms.common.JMSException("Error creating consumer", var40);
         }

         if (var4 != null && var9.getName() != null) {
            this.isDurable = true;
         }

         FrontEnd var11 = var2.getConnection().getFrontEnd();
         this.invocableMonitor = var11.getInvocableMonitor();
         this.selector = var9.getSelector();
         DurableSubscription var13;
         if (this.isDurable) {
            if (this.getSession().getConnection().getClientIdPolicy() == 0) {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("in FEConsumer durable with restricted clientID");
               }

               DurableSubscription var12 = DSManager.manager().lookup(BEConsumerImpl.JNDINameForSubscription(BEConsumerImpl.clientIdPlusName(var4, var9.getName())));
               if (var12 != null) {
                  var13 = new DurableSubscription(BEConsumerImpl.clientIdPlusName(var4, var9.getName(), this.getSession().getConnection().getClientIdPolicy(), var5.getName(), var5.getServerName()), var5, var9.getSelector(), var9.getNoLocal(), this.getSession().getConnection().getClientIdPolicy(), this.subscriptionSharingPolicy);
                  Vector var14 = var12.getDSVector();
                  boolean var15 = false;

                  for(int var43 = 0; var43 < var14.size(); ++var43) {
                     DurableSubscription var16 = (DurableSubscription)var14.elementAt(var43);
                     if (var16.equalsForSerialized(var13)) {
                        var10 = 0;
                     } else {
                        if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                           JMSDebug.JMSFrontEnd.debug("in FEConsumer create new consumer");
                        }

                        JMSServerId var17 = var16.getBackEndId();
                        if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                           JMSDebug.JMSFrontEnd.debug("in FEConsumer first remove old consumer");
                        }

                        JMSDispatcher var18;
                        try {
                           var18 = JMSDispatcherManager.dispatcherFindOrCreate(var17.getDispatcherId());
                        } catch (DispatcherException var39) {
                           throw new weblogic.jms.common.JMSException("Error creating consumer", var39);
                        }

                        if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                           JMSDebug.JMSFrontEnd.debug("in FEConnection remove consumer");
                        }

                        try {
                           SecurityServiceManager.pushSubject(KERNEL_ID, var8);
                           var18.dispatchSync(new BERemoveSubscriptionRequest(var17, var16.getDestinationImpl().getTopicName(), var4, this.getSession().getConnection().getClientIdPolicy(), var9.getName()));
                        } finally {
                           SecurityServiceManager.popSubject(KERNEL_ID);
                        }
                     }
                  }
               }
            } else if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("in FEConsumer durable with unrestricted clientID");
            }
         } else if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("in FEConsumer not durable");
         }

         var13 = null;

         Response var42;
         try {
            SecurityServiceManager.pushSubject(KERNEL_ID, var8);
            var42 = this.backEndDispatcher.dispatchSync(new BEConsumerCreateRequest(var2.getConnection().getJMSID(), var2.getJMSID(), var6, var4, this.getSession().getConnection().getClientIdPolicy(), var9.getName(), var5.getDestinationId(), var9.getSelector(), var9.getNoLocal(), var9.getMessagesMaximum(), var10, var9.getRedeliveryDelay(), var7, var9.getConsumerReconnectInfo(), this.subscriptionSharingPolicy));
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         if (!this.isLocal) {
            this.backEndDispatcher.addDispatcherPeerGoneListener(this.getSession());
            this.backEndDispatcher.addDispatcherPeerGoneListener(this);
         }

         this.consumerReconnectInfo = ((BEConsumerCreateResponse)var42).getConsumerReconnectInfo();
      } catch (Exception var41) {
         try {
            PrivilegedActionUtilities.unregister(this, KERNEL_ID);
         } catch (ManagementException var36) {
         }

         if (var41 instanceof RuntimeException) {
            throw (RuntimeException)var41;
         } else if (var41 instanceof JMSException) {
            throw (JMSException)var41;
         } else if (var41 instanceof ManagementException) {
            throw (ManagementException)var41;
         } else {
            throw new weblogic.jms.common.JMSException(var41);
         }
      }
   }

   public ConsumerReconnectInfo getConsumerReconnectInfo() {
      return this.consumerReconnectInfo;
   }

   JMSDispatcher getBackEndDispatcher() {
      return this.backEndDispatcher;
   }

   private FESession getSession() {
      return this.session;
   }

   private Sequencer getSequencer() {
      return this.sequencer;
   }

   private void close(FEConsumerCloseRequest var1) throws JMSException {
      this.session.checkShutdownOrSuspended();
      long var2 = this.session.consumerClose(this, var1.getLastSequenceNumber());
      synchronized(interceptionPointLock) {
         if (this.receiveIPHandle != null && this.getDestination() != null && (this.getDestination().getType() == 8 || this.getDestination().getType() == 4)) {
            try {
               if (!this.receiveIPHandle.hasAssociation()) {
                  MessageInterceptionService.getSingleton().unRegisterInterceptionPoint(this.receiveIPHandle);
                  this.receiveIPHandle = null;
               }
            } catch (InterceptionServiceException var13) {
               JMSLogger.logFailedToUnregisterInterceptionPoint(var13);
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("FEConsumer.close(), Failure to unregister " + var13);
               }
            }
         }
      }

      try {
         this.backEndDispatcher.dispatchSync(new BEConsumerCloseRequest(this.consumerId, var2));
      } finally {
         this.session.consumerRemove(this.consumerId);
      }

   }

   private int pushException(Request var1) throws JMSException {
      JMSPushExceptionRequest var2 = (JMSPushExceptionRequest)var1;

      try {
         this.getSession().pushException(6, this.consumerId, var2.getException());
      } catch (Exception var4) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("Error pushing exception ", var4);
         }
      }

      if (var2.getException() instanceof ConsumerClosedException) {
         this.session.consumerRemove(this.consumerId);
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   private int setListener(Request var1) throws JMSException {
      this.session.checkShutdownOrSuspended();
      FEConsumerSetListenerRequest var2 = (FEConsumerSetListenerRequest)var1;
      if (var2.getHasListener()) {
         this.session.updateQOS();
      }

      switch (var2.getState()) {
         case 0:
            BEConsumerSetListenerRequest var3 = new BEConsumerSetListenerRequest(this.consumerId, var2.getHasListener(), var2.getLastSequenceNumber());
            synchronized(var2) {
               var2.rememberChild(var3);
               var2.setState(1);
            }

            try {
               var2.dispatchAsync(this.getBackEndDispatcher(), var3);
               break;
            } catch (DispatcherException var6) {
               throw new weblogic.jms.common.JMSException("Error setting listener", var6);
            }
         default:
            var2.useChildResult(JMSConsumerSetListenerResponse.class);
      }

      return var2.getState();
   }

   private int incrementWindow(Request var1) throws JMSException {
      this.session.checkShutdownOrSuspended();
      FEConsumerIncrementWindowCurrentRequest var2 = (FEConsumerIncrementWindowCurrentRequest)var1;
      JMSServerUtilities.anonDispatchNoReply(new BEConsumerIncrementWindowCurrentRequest(this.consumerId, var2.getWindowIncrement(), var2.getClientResponsibleForAcknowledge()), this.backEndDispatcher);
      var2.setResult(new VoidResponse());
      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   private int incrementWindowOneWay(Request var1) throws JMSException {
      this.session.checkShutdownOrSuspended();
      FEConsumerIncrementWindowCurrentOneWayRequest var2 = (FEConsumerIncrementWindowCurrentOneWayRequest)var1;
      JMSServerUtilities.anonDispatchNoReply(new BEConsumerIncrementWindowCurrentRequest(this.consumerId, var2.getWindowIncrement(), var2.getClientResponsibleForAcknowledge()), this.backEndDispatcher);
      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   public void onCallBack(boolean var1) {
      this.currentRequest.resumeExecution(true);
   }

   public void onException(InterceptionProcessorException var1) {
      System.out.println("Processor throws exception" + var1);
      this.currentRequest.resumeExecution(true);
   }

   private int receiveInterceptionPoint(DestinationImpl var1, MessageImpl var2) throws JMSException {
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
               JMSMessageContextImpl var11 = new JMSMessageContextImpl(var2);
               this.receiveIPHandle.processAsync(var11, this);
               byte var10000 = 2;
               return var10000;
            }
         } catch (Exception var9) {
            throw new weblogic.jms.common.JMSException("FAILED in interception " + var9);
         }

         return 1;
      }
   }

   private int receive(Request var1) throws JMSException {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("FEConsumer.receive()");
      }

      this.session.checkShutdownOrSuspended();
      FEConsumerReceiveRequest var2 = (FEConsumerReceiveRequest)var1;
      switch (var2.getState()) {
         case 0:
            if (this.session.isTransacted()) {
               this.session.transactedInfect();
            }

            BEConsumerReceiveRequest var3 = new BEConsumerReceiveRequest(this.consumerId, var2.getTimeout());
            synchronized(var2) {
               var2.rememberChild(var3);
               var2.setState(1);
            }

            try {
               var2.dispatchAsync(this.getBackEndDispatcher(), var3);
            } catch (DispatcherException var19) {
               throw new weblogic.jms.common.JMSException("Error receiving message", var19);
            }
         default:
            return var2.getState();
         case 1:
            try {
               JMSConsumerReceiveResponse var5 = (JMSConsumerReceiveResponse)var2.useChildResult(JMSConsumerReceiveResponse.class);
               if (var5 == null) {
                  throw new AssertionError("receive got a null response");
               }

               MessageImpl var4 = var5.getMessage();
               if (var4 != null) {
                  var5.setCompressionThreshold(this.session.getConnection().getCompressionThreshold());
               }

               if (var4 != null) {
                  var5.setCompressionThreshold(this.session.getConnection().getCompressionThreshold());
                  if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
                     JMSDebug.JMSMessagePath.debug("FRONTEND/FEConsumer (id: " + this.consumerId + ") : " + "Receipt of message " + var4.getJMSMessageID());
                  }

                  if (var5.isTransactional()) {
                     this.session.transactionStat(this, (FEProducer)null, var4);
                  } else {
                     boolean var6 = var4.getClientResponsibleForAcknowledge();
                     if (var6) {
                        this.statistics.incrementReceivedCount(var4);
                        this.getSession().getStatistics().incrementReceivedCount(var4);
                     } else {
                        this.statistics.incrementPendingCount(var4);
                        this.getSession().getStatistics().incrementPendingCount(var4);
                     }

                     long var7 = var5.getSequenceNumber();
                     JMSPushEntry var9;
                     synchronized(this.session) {
                        var9 = new JMSPushEntry(this.getSequencer().getJMSID(), this.consumerId, var7, this.session.getNextSequenceNumber(), var4.getDeliveryCount(), 0);
                     }

                     var9.setClientResponsibleForAcknowledge(var6);
                     var9.setDispatcher(this.getBackEndDispatcher());
                     if (!var6) {
                        this.session.addUnackedPushEntry(var9, var4.getPayloadSize() + (long)var4.getUserPropertySize());
                     }

                     var5.setSequenceNumber(var9.getFrontEndSequenceNumber());
                  }
               }
            } finally {
               if (this.session.isTransacted()) {
                  this.session.transactedDisinfect();
               }

            }

            return Integer.MAX_VALUE;
      }
   }

   public long getBytesPendingCount() {
      return this.statistics.getBytesPendingCount();
   }

   public long getBytesReceivedCount() {
      return this.statistics.getBytesReceivedCount();
   }

   public String getSubscriptionSharingPolicy() {
      return FEConnectionFactory.getSubscriptionSharingPolicyAsString(this.subscriptionSharingPolicy);
   }

   public String getClientID() {
      return this.session.getConnection().getConnectionClientId();
   }

   public String getClientIDPolicy() {
      return this.session.getConnection().getClientIdPolicy() == 1 ? JMSConstants.CLIENT_ID_POLICY_UNRESTRICTED_STRING : JMSConstants.CLIENT_ID_POLICY_RESTRICTED_STRING;
   }

   public String getDestinationName() {
      return this.destination.getName();
   }

   public String getMemberDestinationName() {
      return this.destination.getMemberName();
   }

   public DestinationImpl getDestination() {
      return this.destination;
   }

   public long getMessagesPendingCount() {
      return this.statistics.getMessagesPendingCount();
   }

   public long getMessagesReceivedCount() {
      return this.statistics.getMessagesReceivedCount();
   }

   public boolean isActive() throws RemoteException {
      try {
         Response var1 = this.backEndDispatcher.dispatchSync(new BEConsumerIsActiveRequest(this.getJMSID()));
         return ((BEConsumerIsActiveResponse)var1).consumerIsActive;
      } catch (Throwable var2) {
         throw new RemoteException("Error setting consumer state, " + var2.toString());
      }
   }

   public boolean isDurable() {
      return this.isDurable;
   }

   public String getSelector() {
      return this.selector;
   }

   public JMSID getJMSID() {
      return this.consumerId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 2570:
            this.close((FEConsumerCloseRequest)var1);
            break;
         case 3082:
            this.incrementWindow(var1);
            break;
         case 3338:
            return this.receive(var1);
         case 3594:
            return this.setListener(var1);
         case 15370:
            return this.pushException(var1);
         case 17418:
            this.incrementWindowOneWay(var1);
            break;
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }

      var1.setResult(new VoidResponse());
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
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

      FESession var3 = this.getSession();

      try {
         var3.consumerRemove(this.getJMSID());
      } catch (Exception var5) {
      }

      try {
         var3.pushException(6, this.consumerId, new ConsumerClosedException((MessageConsumer)null, "Connection to JMSServer was lost"));
      } catch (Throwable var6) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("remote error?", var6);
         }
      }

   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Consumer");
      var2.writeAttribute("id", this.consumerId != null ? this.consumerId.toString() : "");
      var2.writeAttribute("isDurable", String.valueOf(this.isDurable));
      var2.writeAttribute("isLocal", String.valueOf(this.isLocal));
      var2.writeAttribute("selector", this.selector != null ? this.selector : "");
      this.statistics.dump(var1, var2);
      var2.writeEndElement();
   }
}
