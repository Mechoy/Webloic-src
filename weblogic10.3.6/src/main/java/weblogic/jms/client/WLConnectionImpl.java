package weblogic.jms.client;

import java.util.Enumeration;
import javax.jms.ConnectionConsumer;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.ServerSessionPool;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSession;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.JMSConstants;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSException;
import weblogic.jms.common.JMSID;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.extensions.WLConnection;

public class WLConnectionImpl extends ReconnectController implements ConnectionInternal {
   private final Object reconnectLock = new Object();
   private JMSConnectionFactory jmsConnectionFactory;
   private int reconnectPolicy;
   private long reconnectBlockingMillis;
   private long totalReconnectPeriodMillis;
   private long lastReconnectTimer;
   private long firstReconnectTime;
   private JMSContext connectionEstablishContext;

   public WLConnectionImpl(JMSConnectionFactory var1, JMSConnection var2) {
      super((ReconnectController)null, var2);
      this.jmsConnectionFactory = var1;
      this.connectionEstablishContext = new JMSContext();
   }

   protected ReconnectController getParent() {
      return this;
   }

   protected WLConnectionImpl getWLConnectionImpl() {
      return this;
   }

   protected JMSConnectionFactory getJmsConnectionFactory() {
      return this.jmsConnectionFactory;
   }

   protected JMSConnection getPhysicalJMSConnection() {
      return (JMSConnection)this.getPhysical();
   }

   Object getConnectionStateLock() {
      return this.reconnectLock;
   }

   protected JMSContext getConnectionEstablishContext() {
      return this.connectionEstablishContext;
   }

   void setConnectionEstablishContext(JMSContext var1) {
      this.connectionEstablishContext = var1;
   }

   ConsumerReconnectInfo computeConsumerReconnectInfo() {
      synchronized(this.getConnectionStateLock()) {
         byte var2 = 11;
         if (this.mergeCloseAndReconnect(var2) == 0) {
            return null;
         }
      }

      ConsumerReconnectInfo var1 = new ConsumerReconnectInfo();
      var1.setClientDispatcherId(JMSDispatcherManager.getLocalDispatcher().getId());
      var1.setClientJMSID(JMSID.create());
      var1.setDelayServerClose(90000L);
      return var1;
   }

   int mergeCloseAndReconnect(int var1) {
      int var2 = this.getState();
      return var2 != -1280 && var2 != -2304 && reconnectPolicyHas(var1, this.getReconnectPolicyInternal()) ? this.getReconnectPolicyInternal() : 0;
   }

   static boolean reconnectPolicyHas(int var0, int var1) {
      return (var0 & var1) == var0 && var0 != 0;
   }

   boolean rememberReconnectState(JMSConnection var1, int var2) {
      if (this.getPhysical() != var1) {
         return false;
      } else {
         int var3 = this.getState();
         if (0 == var3 || 1028 == var3) {
            this.jmsConnectionFactory = this.jmsConnectionFactory;
            boolean var4 = var1.getClientIDInternal() == null;
            byte var5;
            if (var4) {
               var5 = 1;
            } else {
               var5 = 9;
            }

            int var6 = this.mergeCloseAndReconnect(var5);
            String var7;
            if (var6 == 0) {
               if (this.getLastProblem() == null) {
                  var7 = this.missingBits(var6, var5);
               } else {
                  var7 = null;
               }
            } else if (var1.hasTemporaryDestination()) {
               var6 = 0;
               var7 = "connection used temporary destinations";
            } else {
               var7 = null;
            }

            if (var6 != 0 && var1.getPreDisconnectState() == null) {
               try {
                  var1.getReconnectState(var6);
               } catch (CloneNotSupportedException var9) {
                  var1.recurseSetNoRetry(this);
                  throw new AssertionError(var9);
               }

               this.setRecursiveStateNotify(var2);
               return true;
            }

            if (this.getLastProblem() == null) {
               this.setLastProblem(new JMSException(var7));
            }
         }

         var1.setPreDisconnectState((JMSConnection)null);
         var1.recurseSetNoRetry(this);
         return false;
      }
   }

   String missingBits(int var1, int var2) {
      String var3 = "";
      String var4 = "Cannot reconnect, missing";
      if ((1 & var2) > (1 & var1)) {
         var4 = var4 + var3 + " ConnectionReconnect";
         var3 = ", and";
      }

      if ((2 & var2) > (2 & var1)) {
         var4 = var4 + var3 + " SessionReconnect";
         var3 = ", and";
      }

      if ((8 & var2) > (8 & var1)) {
         var4 = var4 + var3 + " ConsumerReconnect";
      }

      return var4;
   }

   void processReconnectTimer(JMSConnection var1) {
      synchronized(this.getConnectionStateLock()) {
         var1.clearTimerInfo();
         String var3;
         if (this.getPhysical() != var1) {
            var3 = "WLConnectionImpl Debug ignoring reconnect, match " + this.getPhysical() + " !=" + var1;
         } else if (this.getState() != 1280 && this.getState() != 512) {
            var3 = "WLConnectionImpl Debug ignoring reconnect, state " + this.getStringState(this.getState());
         } else if (var1.getPreDisconnectState() == null) {
            var3 = "null predisconnect state in " + this.getStringState(this.getState());
         } else {
            var3 = null;
         }

         if (var3 != null) {
            if (TODOREMOVEDebug) {
               System.err.println(var3);
            }

            if (JMSDebug.JMSCommon.isDebugEnabled()) {
               JMSDebug.JMSCommon.debug(var3);
            }

            return;
         }

         this.setupReconnectSchedule(this.getPhysicalJMSConnection(), 1536);
      }

      var1.reconnect();
   }

   void reconnectComplete(JMSConnection var1, JMSConnection var2, Throwable var3) {
      try {
         synchronized(this.getConnectionStateLock()) {
            if (var1 == this.getPhysical()) {
               try {
                  int var5 = this.getState();
                  switch (var5) {
                     case -2304:
                     case -1280:
                     case 0:
                     case 512:
                     case 1028:
                     case 1040:
                     case 1280:
                        break;
                     case 1536:
                        this.setLastProblem(var3);
                        if (var3 == null && var2 != null) {
                           var1.postCreateReplacement();
                           var2.copyExceptionContext(var1);
                           var2 = null;
                           this.setRecursiveStateNotify(0);
                        } else if (var2 == this.getPhysical()) {
                           var2 = null;
                        }
                        break;
                     default:
                        this.wrongState(var5);
                  }
               } finally {
                  if (this.getState() == 1536) {
                     this.setRecursiveStateNotify(512);
                     var1.scheduleReconnectTimer();
                  }

               }
            }
         }
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (javax.jms.JMSException var21) {
         }

      }

   }

   private JMSConnection getJMSConnection() {
      return (JMSConnection)this.getPhysicalWaitForState();
   }

   public void setReconnectPolicy(String var1) throws IllegalArgumentException {
      int var2 = validateAndConvertReconnectPolicy(var1);
      synchronized(this.getConnectionStateLock()) {
         this.reconnectPolicy = var2;
      }
   }

   public String getReconnectPolicy() {
      int var1;
      synchronized(this.getConnectionStateLock()) {
         var1 = this.getReconnectPolicyInternal();
      }

      return convertReconnectPolicy(var1);
   }

   int getReconnectPolicyInternal() {
      return this.reconnectPolicy;
   }

   public static int validateAndConvertReconnectPolicy(String var0) throws IllegalArgumentException {
      if (JMSConstants.RECONNECT_POLICY_NONE.equals(var0)) {
         return 0;
      } else if (!JMSConnection.isT3Client()) {
         throw new IllegalArgumentException("only t3 client supports reconnect");
      } else if (JMSConstants.RECONNECT_POLICY_PRODUCER.equals(var0)) {
         return 7;
      } else if (JMSConstants.RECONNECT_POLICY_ALL.equals(var0)) {
         return 15;
      } else {
         throw new IllegalArgumentException("Unrecognized ReconnectPolicy " + var0);
      }
   }

   static String convertReconnectPolicy(int var0) throws IllegalArgumentException {
      switch (var0) {
         case 0:
            return JMSConstants.RECONNECT_POLICY_NONE;
         case 7:
            return JMSConstants.RECONNECT_POLICY_PRODUCER;
         case 15:
            return JMSConstants.RECONNECT_POLICY_ALL;
         default:
            throw new IllegalArgumentException("Unrecognized ReconnectPolicy " + var0);
      }
   }

   long getFirstReconnectTime() {
      return this.firstReconnectTime;
   }

   long getLastReconnectTimer() {
      return this.lastReconnectTimer;
   }

   void clearLastReconnectTimer() {
      this.lastReconnectTimer = 0L;
   }

   void updateFirstReconnectTime() {
      this.firstReconnectTime = System.currentTimeMillis();
   }

   void updateLastReconnectTimer() {
      long var1 = this.getWLConnectionImpl().getTotalReconnectPeriodMillis();
      if (var1 == -1L) {
         this.lastReconnectTimer = Long.MAX_VALUE;
      } else {
         try {
            this.lastReconnectTimer = System.currentTimeMillis() + var1;
         } catch (RuntimeException var4) {
            this.lastReconnectTimer = Long.MAX_VALUE;
         }
      }

   }

   public void setReconnectBlockingMillis(long var1) throws IllegalArgumentException {
      validateReconnectMillis(var1);
      synchronized(this.getConnectionStateLock()) {
         this.reconnectBlockingMillis = var1;
      }
   }

   public long getReconnectBlockingMillis() {
      synchronized(this.getConnectionStateLock()) {
         return this.getReconnectBlockingInternal();
      }
   }

   long getReconnectBlockingInternal() {
      return this.reconnectBlockingMillis;
   }

   public static void validateReconnectMillis(long var0) throws IllegalArgumentException {
      if (var0 < -1L) {
         throw new IllegalArgumentException("values less than -1 (infinite blocking time) are illegal");
      }
   }

   public long getTotalReconnectPeriodMillis() {
      return this.totalReconnectPeriodMillis;
   }

   public void setTotalReconnectPeriodMillis(long var1) {
      validateReconnectMillis(var1);
      synchronized(this.getConnectionStateLock()) {
         this.totalReconnectPeriodMillis = var1;
      }
   }

   public final JMSDispatcher getFrontEndDispatcher() throws javax.jms.JMSException {
      return this.getJMSConnection().getFrontEndDispatcher();
   }

   public JMSID getJMSID() {
      return this.getJMSConnection().getJMSID();
   }

   public PeerInfo getFEPeerInfo() {
      return this.getJMSConnection().getFEPeerInfo();
   }

   public final void setAllowCloseInOnMessage(boolean var1) {
      this.getJMSConnection().setAllowCloseInOnMessage(var1);
   }

   public final boolean getUserTransactionsEnabled() {
      return this.getJMSConnection().getUserTransactionsEnabled();
   }

   public boolean isXAServerEnabled() {
      return this.getJMSConnection().isXAServerEnabled();
   }

   public String getWLSServerName() {
      return this.getJMSConnection().getWLSServerName();
   }

   public String getRuntimeMBeanName() {
      return this.getJMSConnection().getRuntimeMBeanName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return this.getJMSConnection().getParentInfo();
   }

   public void setDispatchPolicy(String var1) {
      this.getJMSConnection().setDispatchPolicy(var1);
   }

   public void setAcknowledgePolicy(int var1) {
      this.getJMSConnection().setAcknowledgePolicy(var1);
   }

   public int getAcknowledgePolicy() {
      return this.getJMSConnection().getAcknowledgePolicy();
   }

   public String getJMSVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getJMSVersion();
   }

   public int getJMSMajorVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getJMSMajorVersion();
   }

   public int getJMSMinorVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getJMSMinorVersion();
   }

   public String getJMSProviderName() throws javax.jms.JMSException {
      return this.getJMSConnection().getJMSProviderName();
   }

   public String getProviderVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getProviderVersion();
   }

   public int getProviderMajorVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getProviderMajorVersion();
   }

   public int getProviderMinorVersion() throws javax.jms.JMSException {
      return this.getJMSConnection().getProviderMinorVersion();
   }

   public Enumeration getJMSXPropertyNames() throws javax.jms.JMSException {
      return this.getJMSConnection().getJMSXPropertyNames();
   }

   public Session createSession(boolean var1, int var2) throws javax.jms.JMSException {
      long var3 = System.currentTimeMillis();
      JMSConnection var5 = this.getPhysicalJMSConnection();

      try {
         return var5.createSession(var1, var2);
      } catch (JMSException var7) {
         return this.computeJMSConnection(var3, var5, var7).createSession(var1, var2);
      }
   }

   public String getClientID() throws javax.jms.JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return var3.getClientID();
      } catch (JMSException var5) {
         return this.computeJMSConnection(var1, var3, var5).getClientID();
      }
   }

   public static int validateAndConvertClientIdPolicy(String var0) throws IllegalArgumentException {
      if (WLConnection.CLIENT_ID_POLICY_RESTRICTED.equals(var0)) {
         return 0;
      } else if (WLConnection.CLIENT_ID_POLICY_UNRESTRICTED.equals(var0)) {
         return 1;
      } else {
         throw new IllegalArgumentException("Unrecognized clientIdPolicy " + var0);
      }
   }

   public static String convertClientIdPolicy(int var0) throws IllegalArgumentException {
      switch (var0) {
         case 0:
            return WLConnection.CLIENT_ID_POLICY_RESTRICTED;
         case 1:
            return WLConnection.CLIENT_ID_POLICY_UNRESTRICTED;
         default:
            throw new IllegalArgumentException("Unrecognized clientIdPolicy " + var0);
      }
   }

   public String getClientIDPolicy() {
      return this.getJMSConnection().getClientIDPolicy();
   }

   public void setClientID(String var1, String var2) throws javax.jms.JMSException, IllegalArgumentException {
      long var3 = System.currentTimeMillis();
      JMSConnection var5 = this.getPhysicalJMSConnection();
      if (this.getFEPeerInfo().compareTo(PeerInfo.VERSION_1033) < 0) {
         throw new javax.jms.JMSException("Unsupported Operation. Only " + PeerInfo.VERSION_1033.getVersionAsString() + " and later supports this feature");
      } else {
         try {
            var5.setClientID(var1, var2);
         } catch (JMSException var7) {
            this.computeJMSConnection(var3, var5, var7).setClientID(var1, var2);
         }

      }
   }

   public void setClientID(String var1) throws javax.jms.JMSException {
      long var2 = System.currentTimeMillis();
      JMSConnection var4 = this.getPhysicalJMSConnection();

      try {
         var4.setClientID(var1);
      } catch (JMSException var6) {
         this.computeJMSConnection(var2, var4, var6).setClientID(var1);
      }

   }

   public String getSubscriptionSharingPolicy() {
      return this.getJMSConnection().getSubscriptionSharingPolicy();
   }

   public void setSubscriptionSharingPolicy(String var1) throws javax.jms.JMSException, IllegalArgumentException {
      long var2 = System.currentTimeMillis();
      JMSConnection var4 = this.getPhysicalJMSConnection();
      if (this.getFEPeerInfo().compareTo(PeerInfo.VERSION_1033) < 0) {
         throw new javax.jms.JMSException("Unsupported Operation. Only " + PeerInfo.VERSION_1033.getVersionAsString() + " and later supports this feature");
      } else {
         try {
            var4.setSubscriptionSharingPolicy(var1);
         } catch (JMSException var6) {
            this.computeJMSConnection(var2, var4, var6).setSubscriptionSharingPolicy(var1);
         }

      }
   }

   public static int getSubscriptionSharingPolicyAsInt(String var0) {
      if (var0.equals(JMSConstants.SUBSCRIPTION_EXCLUSIVE)) {
         return 0;
      } else if (var0.equals(JMSConstants.SUBSCRIPTION_SHARABLE)) {
         return 1;
      } else {
         throw new IllegalArgumentException("Unrecognized SubscriptionSharingPolicy " + var0);
      }
   }

   public static String getSubscriptionSharingPolicyAsString(int var0) {
      switch (var0) {
         case 0:
            return JMSConstants.SUBSCRIPTION_EXCLUSIVE;
         case 1:
            return JMSConstants.SUBSCRIPTION_SHARABLE;
         default:
            throw new IllegalArgumentException("Unrecognized SubscriptionSharingPolicy " + var0);
      }
   }

   public ConnectionMetaData getMetaData() throws javax.jms.JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return var3.getMetaData();
      } catch (JMSException var5) {
         return this.computeJMSConnection(var1, var3, var5).getMetaData();
      }
   }

   public ExceptionListener getExceptionListener() throws javax.jms.JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return var3.getExceptionListener();
      } catch (JMSException var5) {
         return this.computeJMSConnection(var1, var3, var5).getExceptionListener();
      }
   }

   public void setExceptionListener(ExceptionListener var1) throws javax.jms.JMSException {
      long var2 = System.currentTimeMillis();
      JMSConnection var4 = this.getPhysicalJMSConnection();

      try {
         var4.setExceptionListener(var1);
      } catch (JMSException var6) {
         this.computeJMSConnection(var2, var4, var6).setExceptionListener(var1);
      }

   }

   public void start() throws javax.jms.JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         var3.start();
      } catch (JMSException var5) {
         this.computeJMSConnection(var1, var3, var5).start();
      }

   }

   public void stop() throws javax.jms.JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         var3.stop();
      } catch (JMSException var5) {
         if (!var3.isConnected()) {
            throw var5;
         }

         this.computeJMSConnection(var1, var3, var5).stop();
      }

   }

   public ConnectionConsumer createConnectionConsumer(Destination var1, String var2, ServerSessionPool var3, int var4) throws javax.jms.JMSException {
      long var5 = System.currentTimeMillis();
      JMSConnection var7 = this.getPhysicalJMSConnection();

      try {
         return var7.createConnectionConsumer(var1, var2, var3, var4);
      } catch (JMSException var9) {
         return this.computeJMSConnection(var5, var7, var9).createConnectionConsumer(var1, var2, var3, var4);
      }
   }

   public QueueSession createQueueSession(boolean var1, int var2) throws javax.jms.JMSException {
      long var3 = System.currentTimeMillis();
      JMSConnection var5 = this.getPhysicalJMSConnection();

      try {
         return var5.createQueueSession(var1, var2);
      } catch (JMSException var7) {
         return this.computeJMSConnection(var3, var5, var7).createQueueSession(var1, var2);
      }
   }

   public ConnectionConsumer createConnectionConsumer(Queue var1, String var2, ServerSessionPool var3, int var4) throws javax.jms.JMSException {
      long var5 = System.currentTimeMillis();
      JMSConnection var7 = this.getPhysicalJMSConnection();

      try {
         return var7.createConnectionConsumer(var1, var2, var3, var4);
      } catch (JMSException var9) {
         return this.computeJMSConnection(var5, var7, var9).createConnectionConsumer(var1, var2, var3, var4);
      }
   }

   public TopicSession createTopicSession(boolean var1, int var2) throws javax.jms.JMSException {
      long var3 = System.currentTimeMillis();
      JMSConnection var5 = this.getPhysicalJMSConnection();

      try {
         return var5.createTopicSession(var1, var2);
      } catch (JMSException var7) {
         return this.computeJMSConnection(var3, var5, var7).createTopicSession(var1, var2);
      }
   }

   public ConnectionConsumer createConnectionConsumer(Topic var1, String var2, ServerSessionPool var3, int var4) throws javax.jms.JMSException {
      long var5 = System.currentTimeMillis();
      JMSConnection var7 = this.getPhysicalJMSConnection();

      try {
         return var7.createConnectionConsumer(var1, var2, var3, var4);
      } catch (JMSException var9) {
         return this.computeJMSConnection(var5, var7, var9).createConnectionConsumer(var1, var2, var3, var4);
      }
   }

   public ConnectionConsumer createDurableConnectionConsumer(Topic var1, String var2, String var3, ServerSessionPool var4, int var5) throws javax.jms.JMSException {
      long var6 = System.currentTimeMillis();
      JMSConnection var8 = this.getPhysicalJMSConnection();

      try {
         return var8.createDurableConnectionConsumer(var1, var2, var3, var4, var5);
      } catch (JMSException var10) {
         return this.computeJMSConnection(var6, var8, var10).createDurableConnectionConsumer(var1, var2, var3, var4, var5);
      }
   }
}
