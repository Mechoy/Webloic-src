package weblogic.jms.frontend;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEBrowserCreateRequest;
import weblogic.jms.backend.BEConnection;
import weblogic.jms.backend.BEConnectionConsumerCloseRequest;
import weblogic.jms.backend.BEConnectionConsumerCommon;
import weblogic.jms.backend.BEConnectionConsumerCreateRequest;
import weblogic.jms.backend.BEConnectionStartRequest;
import weblogic.jms.backend.BEConnectionStopRequest;
import weblogic.jms.backend.BEDestinationCreateRequest;
import weblogic.jms.backend.BEServerSessionPoolCreateRequest;
import weblogic.jms.backend.BETemporaryDestinationDestroyRequest;
import weblogic.jms.client.JMSConnectionConsumer;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DestinationPeerGoneAdapter;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.InvalidClientIDException;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSConnectionConsumerCreateResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPeerGoneListener;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.PeerVersionable;
import weblogic.jms.common.SingularAggregatable;
import weblogic.jms.common.SingularAggregatableManager;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JMSSessionRuntimeMBean;
import weblogic.messaging.ID;
import weblogic.messaging.common.PrivilegedActionUtilities;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;

public final class FEConnection implements JMSPeerGoneListener, Invocable, PeerVersionable {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   static final long serialVersionUID = -3238866726804665330L;
   private long startStopSequenceNumber;
   private final HashMap temporaryDestinations = new HashMap();
   private final HashMap connectionConsumers = new HashMap();
   private final HashMap sessions = new HashMap();
   private final HashMap browsers = new HashMap();
   public static final String JMS_CONNECTION_CLIENTID = "weblogic.jms.connection.clientid";
   private boolean allowedToSetClientId = true;
   private boolean closed = false;
   private JMSDispatcher clientDispatcher;
   private final FEConnectionFactory connectionFactory;
   private final String mbeanName;
   private final JMSID connectionId;
   private String clientId;
   private int clientIdPolicy;
   private int subscriptionSharingPolicy;
   private final int deliveryMode;
   private final FrontEnd frontEnd;
   private final int priority;
   private final long timeToDeliver;
   private final long timeToLive;
   private final long sendTimeout;
   private final long redeliveryDelay;
   private final boolean userTransactionsEnabled;
   private final boolean allowCloseInOnMessage;
   private final long transactionTimeout;
   private final int messagesMaximum;
   private final int overrunPolicy;
   private final int acknowledgePolicy;
   private final boolean loadBalancingEnabled;
   private final boolean serverAffinityEnabled;
   private final String unitOfOrder;
   private final boolean isLocal;
   private boolean stopped = true;
   private String clientAddress = null;
   private static TransactionManager tranManager;
   private final InvocableMonitor invocableMonitor;
   private int state = 0;
   private byte peerVersion;
   private FEConnectionRuntimeDelegate myRuntimeDelegate;
   private int compressionThreshold;
   private transient int refCount;

   public final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   public FEConnection(FEConnectionFactory var1, String var2, JMSID var3, JMSDispatcher var4, int var5, int var6, long var7, long var9, long var11, long var13, String var15, int var16, int var17, long var18, boolean var20, boolean var21, int var22, int var23, int var24, boolean var25, boolean var26, String var27, int var28) throws JMSException, ManagementException {
      this.connectionFactory = var1;
      this.frontEnd = var1.getFrontEnd();
      this.mbeanName = var2;
      this.connectionId = var3;
      this.clientDispatcher = var4;
      this.deliveryMode = var5;
      this.priority = var6;
      this.timeToDeliver = var7;
      this.timeToLive = var9;
      this.sendTimeout = var11;
      this.redeliveryDelay = var13;
      this.transactionTimeout = var18;
      this.userTransactionsEnabled = var20;
      this.allowCloseInOnMessage = var21;
      this.messagesMaximum = var22;
      this.overrunPolicy = var23;
      this.acknowledgePolicy = var24;
      this.loadBalancingEnabled = var25;
      this.serverAffinityEnabled = var26;
      this.unitOfOrder = var27;
      this.compressionThreshold = var28;
      this.clientIdPolicy = var16;
      this.subscriptionSharingPolicy = var17;
      if (var15 != null) {
         this.setConnectionClientId(var15);
      }

      this.invocableMonitor = FrontEnd.getFrontEnd().getInvocableMonitor();
      var4.addDispatcherPeerGoneListener(this);
      if (var4.isLocal()) {
         this.isLocal = true;
         this.clientAddress = "local";
      } else {
         this.isLocal = false;
         this.clientAddress = ServerHelper.getClientAddress();
      }

      synchronized(this) {
         if (tranManager == null) {
            tranManager = TxHelper.getTransactionManager();
         }
      }

      this.myRuntimeDelegate = new FEConnectionRuntimeDelegate(var2, this, this.frontEnd.getService());
   }

   FEConnectionRuntimeDelegate getRuntimeDelegate() {
      return this.myRuntimeDelegate;
   }

   JMSID getConnectionId() {
      return this.connectionId;
   }

   int getOverrunPolicy() {
      return this.overrunPolicy;
   }

   int getAcknowledgePolicy() {
      return this.acknowledgePolicy;
   }

   int getMessagesMaximum() {
      return this.messagesMaximum;
   }

   boolean isLocal() {
      return this.isLocal;
   }

   boolean isLoadBalancingEnabled() {
      return this.loadBalancingEnabled;
   }

   boolean isServerAffinityEnabled() {
      return this.serverAffinityEnabled;
   }

   synchronized long getStartStopSequenceNumber() {
      return this.startStopSequenceNumber;
   }

   int getPriority() {
      return this.priority;
   }

   int getDeliveryMode() {
      return this.deliveryMode;
   }

   long getTimeToDeliver() {
      return this.timeToDeliver;
   }

   long getTimeToLive() {
      return this.timeToLive;
   }

   long getSendTimeout() {
      return this.sendTimeout;
   }

   public long getRedeliveryDelay() {
      return this.redeliveryDelay;
   }

   boolean userTransactionsEnabled() {
      return this.userTransactionsEnabled;
   }

   boolean getAllowCloseInOnMessage() {
      return this.allowCloseInOnMessage;
   }

   long getTransactionTimeout() {
      return this.transactionTimeout;
   }

   boolean getAttachJMSXUserID() {
      return this.connectionFactory == null ? false : this.connectionFactory.isAttachJMSXUserId();
   }

   String getUnitOfOrder() {
      return this.unitOfOrder;
   }

   JMSDispatcher getClientDispatcher() throws JMSException {
      JMSDispatcher var1 = this.clientDispatcher;
      if (var1 == null) {
         throw new IllegalStateException("Connection is closed");
      } else {
         return var1;
      }
   }

   synchronized void markSuspending() {
      if (!this.closed && 0 == (this.state & 18)) {
         this.state = 2;
      }
   }

   synchronized void markShuttingDown() {
      if (!this.closed && 0 == (this.state & 16)) {
         this.state = 8;
      }
   }

   private synchronized boolean isSuspended() {
      return (this.state & 2) != 0;
   }

   synchronized void checkShutdownOrSuspended() throws JMSException {
      if (this.closed) {
         throw new weblogic.jms.common.JMSException("Connection is closed");
      } else if ((this.state & 26) != 0) {
         throw new weblogic.jms.common.JMSException("JMS server is shutdown or suspended");
      }
   }

   private synchronized void removeClientID() {
      if (this.clientId != null) {
         String var1 = "weblogic.jms.connection.clientid." + this.clientId;

         try {
            SingularAggregatableManager var2 = SingularAggregatableManager.findOrCreate();
            var2.singularUnbind(var1);
         } catch (JMSException var3) {
         }

         this.clientId = null;
      }
   }

   void normalClose() throws JMSException {
      this.close(false, (JMSException)null);
   }

   void close(boolean var1, JMSException var2) throws JMSException {
      Object var4 = null;
      InvocableManagerDelegate.delegate.invocableRemove(7, this.connectionId);
      synchronized(this) {
         try {
            if (this.closed) {
               return;
            }

            this.allowedToSetClientId = false;
            this.closed = true;
            this.clientDispatcher.removeDispatcherPeerGoneListener(this);

            try {
               this.stop();
            } catch (JMSException var35) {
               if (var4 == null) {
                  var4 = var35;
               }
            }

            Iterator var3 = ((HashMap)this.sessions.clone()).values().iterator();

            while(var3.hasNext()) {
               try {
                  ((FESession)var3.next()).close(var1, 0L, var2);
               } catch (JMSException var36) {
                  if (var4 == null) {
                     var4 = var36;
                  }
               }
            }

            Object var6 = null;
            var3 = ((HashMap)this.temporaryDestinations.clone()).values().iterator();
            JMSDispatcher var7 = null;

            while(var3.hasNext()) {
               DestinationImpl var8 = (DestinationImpl)var3.next();

               try {
                  var7 = JMSDispatcherManager.dispatcherFindOrCreate(var8.getDispatcherId());
                  var7.dispatchSync(new BETemporaryDestinationDestroyRequest(var8.getBackEndId().getId(), var8.getDestinationId()));
               } catch (DispatcherException var37) {
                  if (var4 == null) {
                     var4 = var37;
                  }
               } catch (JMSException var38) {
                  if (var4 == null) {
                     var4 = var38;
                  }
               } finally {
                  if (!var7.isLocal()) {
                     var7.removeDispatcherPeerGoneListener(new DestinationPeerGoneAdapter(var8, (FEConnection)null));
                  }

                  var7 = null;
               }
            }

            var3 = ((HashMap)this.connectionConsumers.clone()).values().iterator();

            while(var3.hasNext()) {
               try {
                  BEConnectionConsumerCommon var45 = (BEConnectionConsumerCommon)var3.next();
                  this.connectionConsumerClose(var45.getJMSID());
               } catch (JMSException var40) {
                  if (var4 == null) {
                     var4 = var40;
                  }
               }
            }

            var3 = ((HashMap)this.browsers.clone()).values().iterator();

            while(var3.hasNext()) {
               try {
                  this.browserRemove(((FEBrowser)var3.next()).getJMSID());
               } catch (JMSException var41) {
                  if (var4 == null) {
                     var4 = var41;
                  }
               }
            }

            try {
               if (this.myRuntimeDelegate != null) {
                  PrivilegedActionUtilities.unregister(this.myRuntimeDelegate, KERNEL_ID);
               }
            } catch (ManagementException var42) {
               JMSLogger.logErrorUnregisteringFrontEndConnection(this.frontEnd.getMbeanName(), this, var42);
               if (var4 == null) {
                  var4 = var42;
               }
            }

            if (var4 != null) {
               if (var4 instanceof JMSException) {
                  throw (JMSException)var4;
               }

               throw new weblogic.jms.common.JMSException("Error closing connection", (Throwable)var4);
            }
         } finally {
            this.removeClientID();
            if (this.clientDispatcher != null) {
               JMSDispatcherManager.removeDispatcherReference(this.clientDispatcher);
               this.clientDispatcher = null;
            }

            this.myRuntimeDelegate = null;
         }

      }
   }

   public synchronized boolean isStopped() {
      return this.stopped;
   }

   private synchronized void start() throws JMSException {
      ++this.startStopSequenceNumber;
      this.stopped = false;
      this.allowedToSetClientId = false;
      Iterator var1 = this.sessions.values().iterator();
      HashMap var2 = new HashMap();

      while(var1.hasNext()) {
         FESession var3 = (FESession)var1.next();
         synchronized(var3) {
            HashMap var5 = var3.getBEDispatchers();
            Iterator var6 = var5.values().iterator();

            while(var6.hasNext()) {
               JMSDispatcher var7 = (JMSDispatcher)var6.next();
               var2.put(var7.getId(), var7);
            }
         }
      }

      if (!this.connectionConsumers.isEmpty()) {
         JMSDispatcher var11 = JMSDispatcherManager.getLocalDispatcher();
         var2.put(var11.getId(), var11);
      }

      Throwable var12 = null;
      var1 = var2.values().iterator();

      while(var1.hasNext()) {
         JMSDispatcher var4 = (JMSDispatcher)var1.next();

         try {
            var4.dispatchSync(new BEConnectionStartRequest(this.connectionId, this.startStopSequenceNumber));
         } catch (Throwable var9) {
            var12 = var9;
         }
      }

      if (var12 != null) {
         throw new weblogic.jms.common.JMSException(var12.toString(), var12);
      }
   }

   synchronized void stop() throws JMSException {
      if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
         JMSDebug.JMSFrontEnd.debug("FEConnection.stop()");
      }

      ++this.startStopSequenceNumber;
      if (this.stopped) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("FEConnection.stop(): connection stopped already, returns");
         }

      } else {
         this.stopped = true;
         this.allowedToSetClientId = false;
         HashMap var1 = new HashMap();
         Iterator var2 = this.sessions.values().iterator();

         while(var2.hasNext()) {
            FESession var3 = (FESession)var2.next();
            synchronized(var3) {
               HashMap var5 = var3.getConsumersMap();
               Iterator var6 = var5.values().iterator();

               while(var6.hasNext()) {
                  FEConsumer var7 = (FEConsumer)var6.next();
                  JMSDispatcher var8 = var7.getBackEndDispatcher();

                  try {
                     var1.put(var8.getId(), var8);
                  } catch (Exception var12) {
                  }
               }
            }
         }

         if (!this.connectionConsumers.isEmpty()) {
            JMSDispatcher var14 = JMSDispatcherManager.getLocalDispatcher();
            var1.put(var14.getId(), var14);
         }

         var2 = var1.values().iterator();
         Exception var15 = null;

         while(var2.hasNext()) {
            JMSDispatcher var4 = (JMSDispatcher)var2.next();

            try {
               var4.dispatchSync(new BEConnectionStopRequest(this.connectionId, this.startStopSequenceNumber, this.isSuspended()));
            } catch (Exception var11) {
               var15 = var11;
            }
         }

         if (var15 != null) {
            throw new weblogic.jms.common.JMSException(var15.toString(), var15);
         } else {
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("FEConnection.stop() returns");
            }

         }
      }
   }

   private int sessionCreate(FESessionCreateRequest var1) throws JMSException {
      this.checkShutdownOrSuspended();
      JMSID var2 = this.sessionCreateHelper(var1);
      FESession var3 = this.sessionFind(var2);
      var1.setResult(new FESessionCreateResponse(var2, var3.getName()));
      var1.setState(Integer.MAX_VALUE);
      return var1.getState();
   }

   private JMSID sessionCreateHelper(FESessionCreateRequest var1) throws JMSException {
      this.allowedToSetClientId = false;
      JMSID var2 = this.getFrontEnd().getService().getNextId();
      String var3 = "session" + var2.getCounter();
      SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

      FESession var4;
      try {
         var4 = new FESession(this, var3, var2, var1.getTransacted(), var1.getXASession(), var1.getAcknowledgeMode(), var1.getPushWorkManager());
      } catch (ManagementException var10) {
         throw new weblogic.jms.common.JMSException("Error creating front end session: " + var10, var10);
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

      this.sessionAdd(var4, this.myRuntimeDelegate);
      return var2;
   }

   private synchronized void sessionAdd(FESession var1, FEConnectionRuntimeDelegate var2) throws JMSException {
      if (var2 == null) {
         throw new weblogic.jms.common.JMSException("connection has already closed " + this.getJMSID());
      } else {
         if (this.sessions.put(var1.getJMSID(), var1) == null) {
            var2.increaseSessionsTotalCount();
            var2.setSessionsHighCount(Math.max(var2.getSessionsHighCount(), (long)this.sessions.size()));
            InvocableManagerDelegate.delegate.invocableAdd(8, var1);
         }

      }
   }

   synchronized void sessionRemove(FESession var1) {
      this.sessions.remove(var1.getJMSID());
      InvocableManagerDelegate.delegate.invocableRemove(8, var1.getJMSID());
   }

   private FESession sessionFind(JMSID var1) throws JMSException {
      FESession var2 = (FESession)this.sessions.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new weblogic.jms.common.JMSException("Session not found");
      }
   }

   private synchronized void addTemporaryDestination(DestinationImpl var1) {
      this.temporaryDestinations.put(var1.getDestinationId(), var1);
   }

   public synchronized DestinationImpl removeTemporaryDestination(JMSID var1) {
      return (DestinationImpl)this.temporaryDestinations.remove(var1);
   }

   public DestinationImpl createDestination(DestinationImpl var1) throws JMSException {
      return this.createDestination(var1.getServerName(), var1.getName(), var1.getType());
   }

   public DestinationImpl createDestination(String var1, String var2, int var3) throws JMSException {
      JMSServerId var6 = FEManager.refreshBackEndId(var1);
      DestinationImpl var4;
      if ((var4 = this.frontEnd.findBackEndDestination(var6, var2)) == null) {
         JMSDispatcher var5;
         try {
            var5 = JMSDispatcherManager.dispatcherFindOrCreate(var6.getDispatcherId());
         } catch (DispatcherException var8) {
            throw new weblogic.jms.common.JMSException("Back End JMSDispatcher not found");
         }

         Response var7 = var5.dispatchSync(new BEDestinationCreateRequest(var6.getId(), var2, var3, false));
         var4 = ((JMSDestinationCreateResponse)var7).getDestination();
         this.frontEnd.addBackEndDestination(var6, var2, var4, this);
         this.checkShutdownOrSuspended();
      }

      return var4;
   }

   private static String debugClassCastException(String var0, String var1, Object var2) {
      String var3 = "destination for jndi " + var0 + " has backend " + var1 + " found object <";
      if (var2 == null) {
         var3 = var3 + var2 + ">";
      } else {
         var3 = var3 + var2.getClass().getName() + " " + var2 + ">";
      }

      return var3;
   }

   private int serverSessionPoolCreate(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FEServerSessionPoolCreateRequest var2 = (FEServerSessionPoolCreateRequest)var1;

      try {
         JMSDispatcher var3 = JMSDispatcherManager.dispatcherFindOrCreate(var2.getBackEndId().getDispatcherId());
         Response var4 = var3.dispatchSync(new BEServerSessionPoolCreateRequest(var2.getBackEndId().getId(), this.connectionFactory.getJMSConnectionFactory(), var2.getSessionMaximum(), var2.getAcknowledgeMode(), var2.isTransacted(), var2.getMessageListenerClass(), var2.getClientData()));
         var2.setResult(var4);
      } catch (DispatcherException var5) {
         throw new weblogic.jms.common.JMSException("Error creating server session pool", var5);
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   private synchronized void connectionConsumerAdd(BEConnectionConsumerCommon var1) throws JMSException {
      this.checkShutdownOrSuspended();
      if (this.connectionConsumers.put(var1.getJMSID(), var1) == null) {
         this.myRuntimeDelegate.increaseConnectionConsumersCurrentCount();
         if (this.myRuntimeDelegate.getConnectionConsumersCurrentCount() > this.myRuntimeDelegate.getConnectionConsumersHighCount()) {
            this.myRuntimeDelegate.increaseConnectionConsumersTotalCount();
         }
      }

   }

   private synchronized void connectionConsumerRemove(BEConnectionConsumerCommon var1) {
      if (this.connectionConsumers.remove(var1.getJMSID()) != null) {
         this.myRuntimeDelegate.decreaseConnectionConsumersCurrentCount();
      }

   }

   private synchronized BEConnectionConsumerCommon connectionConsumerFind(JMSID var1) throws JMSException {
      BEConnectionConsumerCommon var2 = (BEConnectionConsumerCommon)this.connectionConsumers.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         throw new weblogic.jms.common.JMSException("ConnectionConsumer not found");
      }
   }

   private int connectionConsumerCreate(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FEConnectionConsumerCreateRequest var2 = (FEConnectionConsumerCreateRequest)var1;
      DestinationImpl var3 = var2.getDestination();
      ServerSessionPool var4 = var2.getServerSessionPool();
      FEDDHandler var5 = null;

      assert var3 != null;

      var5 = DDManager.findFEDDHandlerByDDName(var3.getName());
      if (var5 != null) {
         var3 = var5.connectionConsumerLoadBalance();
         if (var3 == null) {
            throw new JMSException("Destination not found on this server");
         }
      }

      if (var3.getDestinationId() == null) {
         var3 = this.createDestination(var3.getServerName(), var3.getName(), var3.getType());
      }

      JMSServerId var6 = var3.getBackEndId();
      JMSID var7 = var3.getDestinationId();
      boolean var8;
      long var9;
      synchronized(this) {
         var8 = this.stopped;
         var9 = this.startStopSequenceNumber;
      }

      Response var11;
      try {
         var11 = JMSDispatcherManager.dispatcherFindOrCreate(var3.getDispatcherId()).dispatchSync(new BEConnectionConsumerCreateRequest(var6, this.connectionId, var4, this, var7, var2.isDurable(), var2.getMessageSelector(), var2.getMessagesMaximum(), var8, var9));
      } catch (DispatcherException var13) {
         throw new weblogic.jms.common.JMSException("Error creating connection consumer", var13);
      }

      ConnectionConsumer var12 = ((JMSConnectionConsumerCreateResponse)var11).getConnectionConsumer();
      this.connectionConsumerAdd((BEConnectionConsumerCommon)var12);
      if (!this.isLocal) {
         JMSConnectionConsumer var15 = new JMSConnectionConsumer(this.connectionId, var4, ((BEConnectionConsumerCommon)var12).getJMSID());
         ((JMSConnectionConsumerCreateResponse)var11).setConnectionConsumer(var15);
      }

      var2.setResult(var11);
      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   private void connectionConsumerClose(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FEConnectionConsumerCloseRequest var2 = (FEConnectionConsumerCloseRequest)var1;
      this.connectionConsumerClose(var2.getConnectionConsumerId());
   }

   private void connectionConsumerClose(JMSID var1) throws JMSException {
      BEConnectionConsumerCommon var2 = this.connectionConsumerFind(var1);
      this.connectionConsumerRemove(var2);
      JMSDispatcher var3 = ((BEConnection)InvocableManagerDelegate.delegate.invocableFind(15, this.connectionId)).getDispatcher();
      BEConnectionConsumerCloseRequest var4 = new BEConnectionConsumerCloseRequest(this.connectionId, var1);
      var3.dispatchSync(var4);
   }

   public String getConnectionClientId() {
      return this.clientId;
   }

   public int getClientIdPolicy() {
      return this.clientIdPolicy;
   }

   private int setConnectionClientId(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FEConnectionSetClientIdRequest var2 = (FEConnectionSetClientIdRequest)var1;
      String var3 = var2.getClientId();
      this.clientIdPolicy = var2.getClientIdPolicy();
      String var4 = "weblogic.jms.connection.clientid." + var3;
      if (this.clientIdPolicy == 1) {
         if (var2.getState() == 0) {
            this.checkIfAllowedToSetClientID();
         }

         this.clientId = var3;
         var2.setResult(new VoidResponse());
         return Integer.MAX_VALUE;
      } else {
         SingularAggregatableManager var5 = SingularAggregatableManager.findOrCreate();
         switch (var2.getState()) {
            case 0:
               this.checkIfAllowedToSetClientID();
               FEClientIDSingularAggregatable var6 = new FEClientIDSingularAggregatable(var3, this.connectionId);
               var2.setFocascia(var6);
               var2.setState(1);
               var5.singularBindStart(var4, var6, var2);
               return var2.getState();
            case 1:
               String var7;
               if ((var7 = var5.singularBindFinish((SingularAggregatable)var2.getFocascia(), var2)) != null) {
                  throw new InvalidClientIDException("Client id, " + var3 + ", is in use.  The reason for rejection is \"" + var7 + "\"");
               }

               this.clientId = var3;
               if (var2.hasResults()) {
                  var2.getResult();
                  var2.setResult(new VoidResponse());
               }

               return Integer.MAX_VALUE;
            default:
               throw new JMSException("Unknown state: " + var2.getState());
         }
      }
   }

   private void checkIfAllowedToSetClientID() throws IllegalStateException {
      if (this.clientId != null) {
         throw new IllegalStateException("ClientID has already been set");
      } else if (!this.allowedToSetClientId) {
         throw new IllegalStateException("ClientID needs to be set first thing after creating connection");
      }
   }

   private void setConnectionClientId(String var1) throws JMSException {
      this.checkIfAllowedToSetClientID();
      if (this.clientIdPolicy == 0) {
         String var4 = "weblogic.jms.connection.clientid." + var1;
         FEClientIDSingularAggregatable var3 = new FEClientIDSingularAggregatable(var1, this.connectionId);
         SingularAggregatableManager var2 = SingularAggregatableManager.findOrCreate();
         String var5;
         if ((var5 = var2.singularBind(var4, var3)) != null) {
            throw new InvalidClientIDException("Client id, " + var1 + ", is in use.  The reason for rejection is \"" + var5 + "\"");
         }
      }

      this.clientId = var1;
   }

   public String getSubscriptionSharingPolicy() {
      return FEConnectionFactory.getSubscriptionSharingPolicyAsString(this.subscriptionSharingPolicy);
   }

   public int getSubscriptionSharingPolicyAsInt() {
      return this.subscriptionSharingPolicy;
   }

   public String toString() {
      return this.mbeanName;
   }

   public int incrementRefCount() {
      return ++this.refCount;
   }

   public int decrementRefCount() {
      return --this.refCount;
   }

   public void dispatcherPeerGone(Exception var1, Dispatcher var2) {
      if (JMSDebug.JMSDispatcher.isDebugEnabled()) {
         JMSDebug.JMSDispatcher.debug("FEConnection.jmsPeerGone()");
      }

      AuthenticatedSubject var3 = JMSSecurityHelper.getCurrentSubject();
      if (var3 == null || JMSSecurityHelper.isServerIdentity(var3)) {
         var3 = SubjectUtils.getAnonymousSubject();
      }

      SecurityServiceManager.pushSubject(KERNEL_ID, var3);

      try {
         this.close(true, (JMSException)null);
      } catch (JMSException var9) {
         if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
            JMSDebug.JMSFrontEnd.debug("peer gone exception", var9);
         }
      } finally {
         SecurityServiceManager.popSubject(KERNEL_ID);
      }

   }

   public FrontEnd getFrontEnd() {
      return this.frontEnd;
   }

   public synchronized HashMap getSessionMap() {
      return this.sessions;
   }

   public synchronized HashMap getBrowserMap() {
      return this.browsers;
   }

   public synchronized HashMap getConnectionConsumerMap() {
      return this.connectionConsumers;
   }

   public synchronized HashMap getTemporaryDestinationMap() {
      return this.temporaryDestinations;
   }

   public JMSSessionRuntimeMBean[] getSessions() {
      return this.myRuntimeDelegate.getSessions();
   }

   public long getSessionsCurrentCount() {
      return this.myRuntimeDelegate.getSessionsCurrentCount();
   }

   public long getSessionsHighCount() {
      return this.myRuntimeDelegate.getSessionsHighCount();
   }

   public long getSessionsTotalCount() {
      return this.myRuntimeDelegate.getSessionsTotalCount();
   }

   public synchronized void setPeerVersion(byte var1) {
      this.peerVersion = var1;
   }

   public synchronized byte getPeerVersion() {
      return this.peerVersion;
   }

   public void finalize() {
      try {
         super.finalize();
         this.removeClientID();
      } catch (Throwable var2) {
      }

   }

   private int destroyTemporaryDestination(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      DestinationImpl var2 = null;
      FETemporaryDestinationDestroyRequest var3 = (FETemporaryDestinationDestroyRequest)var1;
      switch (var3.getState()) {
         case 0:
            var2 = (DestinationImpl)this.temporaryDestinations.get(var3.getDestinationId());
            if (var2 == null) {
               var3.setResult(new VoidResponse());
               var3.setState(Integer.MAX_VALUE);
               return Integer.MAX_VALUE;
            } else {
               BETemporaryDestinationDestroyRequest var4 = new BETemporaryDestinationDestroyRequest(var2.getBackEndId().getId(), var3.getDestinationId());
               synchronized(var3) {
                  var3.rememberChild(var4);
                  var3.setState(1);
               }

               try {
                  var3.setDispatcher(JMSDispatcherManager.dispatcherFindOrCreate(var2.getDispatcherId()));
                  var3.dispatchAsync(var3.getDispatcher(), var4);
               } catch (DispatcherException var7) {
                  throw new weblogic.jms.common.JMSException("Error deleting temporary destination", var7);
               }

               return var3.getState();
            }
         case 1:
         default:
            var3.useChildResult(VoidResponse.class);
            var2 = this.removeTemporaryDestination(var3.getDestinationId());
            if (!var3.getDispatcher().isLocal()) {
               var3.getDispatcher().removeDispatcherPeerGoneListener(new DestinationPeerGoneAdapter(var2, (FEConnection)null));
            }

            return Integer.MAX_VALUE;
      }
   }

   private int createTemporaryDestination(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FETempDestinationFactory var2 = FEManager.getTemporaryDestinationFactory();
      DestinationImpl var3 = null;
      synchronized(this) {
         var3 = (DestinationImpl)var2.createTempDestination(JMSDispatcherManager.getLocalDispatcher().getId(), this.getJMSID(), this.stopped, ((FETemporaryDestinationCreateRequest)var1).getDestType(), this.startStopSequenceNumber, this.getAddressAndMBeanInfo());
      }

      this.addTemporaryDestination(var3);

      JMSDispatcher var4;
      try {
         var4 = JMSDispatcherManager.dispatcherFindOrCreate(var3.getDispatcherId());
      } catch (DispatcherException var6) {
         throw new weblogic.jms.common.JMSException("Error creating temporary destination", var6);
      }

      if (!var4.isLocal()) {
         var4.addDispatcherPeerGoneListener(new DestinationPeerGoneAdapter(var3, this));
      }

      var1.setState(Integer.MAX_VALUE);
      var1.setResult(new FETemporaryDestinationCreateResponse(var3));
      return Integer.MAX_VALUE;
   }

   private synchronized void browserAdd(FEBrowser var1) throws JMSException {
      if (this.browsers.put(var1.getJMSID(), var1) == null) {
         InvocableManagerDelegate.delegate.invocableAdd(11, var1);
         this.myRuntimeDelegate.setBrowsersHighCount(Math.max(this.myRuntimeDelegate.getBrowsersHighCount(), (long)this.browsers.size()));
         this.myRuntimeDelegate.increaseBrowsersTotalCount();
      }

   }

   private int browserCreate(Request var1) throws JMSException {
      this.checkShutdownOrSuspended();
      FEBrowserCreateRequest var2 = (FEBrowserCreateRequest)var1;
      switch (var2.getState()) {
         case 0:
            DestinationImpl var3 = var2.getDestination();
            FEDDHandler var4 = null;

            assert var3 != null;

            var4 = DDManager.findFEDDHandlerByDDName(var3.getName());
            if (var4 != null) {
               var3 = var4.consumerLoadBalance((FESession)null);
            }

            BEBrowserCreateRequest var5 = new BEBrowserCreateRequest((JMSID)null, var3.getDestinationId(), var2.getMessageSelector());
            synchronized(var2) {
               var2.rememberChild(var5);
               var2.setState(1);
            }

            try {
               var2.setDispatcher(JMSDispatcherManager.dispatcherFindOrCreate(var3.getDispatcherId()));
               var2.dispatchAsync(var2.getDispatcher(), var5);
               break;
            } catch (DispatcherException var8) {
               throw new weblogic.jms.common.JMSException("Error creating browser", var8);
            }
         case 1:
            JMSBrowserCreateResponse var6 = (JMSBrowserCreateResponse)var2.useChildResult(JMSBrowserCreateResponse.class);
            FEBrowser var7 = new FEBrowser(this, (FESession)null, var6.getBrowserId(), var2.getDispatcher());
            this.browserAdd(var7);
      }

      return var2.getState();
   }

   synchronized void browserRemove(JMSID var1) throws JMSException {
      if (this.browsers.remove(var1) == null) {
         throw new weblogic.jms.common.JMSException("Browser not found, " + var1);
      } else {
         InvocableManagerDelegate.delegate.invocableRemove(11, var1);
      }
   }

   String getAddressAndMBeanInfo() {
      return this.clientAddress + "|" + JMSService.getJMSService().getMbeanName();
   }

   String getAddress() {
      return this.clientAddress.substring(1);
   }

   public JMSID getJMSID() {
      return this.connectionId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 519:
            return this.browserCreate(var1);
         case 1031:
            this.checkShutdownOrSuspended();
            this.normalClose();
            break;
         case 1287:
            this.connectionConsumerClose(var1);
            break;
         case 1543:
            return this.connectionConsumerCreate(var1);
         case 1799:
            return this.setConnectionClientId(var1);
         case 2055:
            this.checkShutdownOrSuspended();
            this.start();
            break;
         case 2311:
            this.checkShutdownOrSuspended();
            this.stop();
            break;
         case 5895:
            return this.serverSessionPoolCreate(var1);
         case 6663:
            return this.sessionCreate((FESessionCreateRequest)var1);
         case 7431:
            return this.destroyTemporaryDestination(var1);
         case 7687:
            return this.createTemporaryDestination(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }

      var1.setResult(new VoidResponse());
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Connection");
      var2.writeAttribute("id", this.connectionId != null ? this.connectionId.toString() : "");
      var2.writeAttribute("clientID", this.clientId != null ? this.clientId : "");
      var2.writeAttribute("browsersCurrentCount", String.valueOf(this.browsers.size()));
      var2.writeAttribute("deliveryMode", this.deliveryMode == 2 ? "persistent" : "non-persistent");
      var2.writeAttribute("priority", String.valueOf(this.priority));
      var2.writeAttribute("timeToDeliver", String.valueOf(this.timeToDeliver));
      var2.writeAttribute("timeToLive", String.valueOf(this.timeToLive));
      var2.writeAttribute("sendTimeout", String.valueOf(this.sendTimeout));
      var2.writeAttribute("redeliveryDelay", String.valueOf(this.redeliveryDelay));
      var2.writeAttribute("userTransactionsEnabled", String.valueOf(this.userTransactionsEnabled));
      var2.writeAttribute("messagesMaximum", String.valueOf(this.messagesMaximum));
      var2.writeAttribute("overrunPolicy", String.valueOf(this.overrunPolicy));
      var2.writeAttribute("acknowledgePolicy", String.valueOf(this.acknowledgePolicy));
      var2.writeAttribute("loadBalancingEnabled", String.valueOf(this.loadBalancingEnabled));
      var2.writeAttribute("serverAffinityEnabled", String.valueOf(this.serverAffinityEnabled));
      var2.writeAttribute("unifOfOrder", this.unitOfOrder != null ? this.unitOfOrder : "");
      var2.writeAttribute("isLocal", String.valueOf(this.isLocal));
      var2.writeAttribute("clientAddress", this.clientAddress != null ? this.clientAddress : "");
      var2.writeAttribute("state", JMSService.getStateName(this.state));
      var2.writeAttribute("peerVersion", String.valueOf(this.peerVersion));
      var2.writeStartElement("Sessions");
      HashMap var3 = (HashMap)this.sessions.clone();
      var2.writeAttribute("currentCount", String.valueOf(var3.size()));
      Iterator var4 = var3.values().iterator();

      while(var4.hasNext()) {
         FESession var5 = (FESession)var4.next();
         var5.dump(var1, var2);
      }

      var2.writeEndElement();
      var2.writeStartElement("ConnectionConsumers");
      HashMap var8 = (HashMap)this.connectionConsumers.clone();
      var2.writeAttribute("currentCount", String.valueOf(var8.size()));
      var4 = var8.values().iterator();

      while(var4.hasNext()) {
         BEConnectionConsumerCommon var6 = (BEConnectionConsumerCommon)var4.next();
         var6.dumpRef(var1, var2);
      }

      var2.writeEndElement();
      var2.writeStartElement("TemporaryDestinations");
      HashMap var9 = (HashMap)this.temporaryDestinations.clone();
      var2.writeAttribute("currentCount", String.valueOf(var9.size()));
      var4 = var9.values().iterator();

      while(var4.hasNext()) {
         DestinationImpl var7 = (DestinationImpl)var4.next();
         JMSDiagnosticImageSource.dumpDestinationImpl(var2, var7);
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }
}
