package weblogic.jms.client;

import java.io.IOException;
import java.util.StringTokenizer;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.transaction.Transaction;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.JMSEnvironment;
import weblogic.jms.common.AlreadyClosedException;
import weblogic.jms.common.BytesMessageImpl;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.HdrMessageImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageId;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.JMSWorkContextHelper;
import weblogic.jms.common.LostServerException;
import weblogic.jms.common.MapMessageImpl;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.ProducerSendResponse;
import weblogic.jms.common.StreamMessageImpl;
import weblogic.jms.common.TextMessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.frontend.FEProducerSendRequest;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.transaction.TransactionHelper;

public final class JMSProducer implements ProducerInternal, Invocable, Reconnectable, Cloneable {
   private static int ONEWAYSENDCONSECUTIVEMESSAGELIMIT = 320000;
   private final JMSSession session;
   private volatile JMSID producerId;
   private volatile boolean closeInProgress;
   private final DestinationImpl destination;
   private int deliveryMode;
   private int priority;
   private long timeToDeliver = -1L;
   private long timeToLive;
   private int redeliveryLimit = -1;
   private long sendTimeout;
   private boolean messageIdsDisabled;
   private boolean messageTimestampsDisabled;
   private boolean flowControlEnabled;
   private final int flowMinimum;
   private final int flowMaximum;
   private final double flowDecrease;
   private final int flowIncrease;
   private final long flowInterval;
   private PeerInfo peerInfo;
   private double flowRateCurrent;
   private long backOffTime;
   private boolean needsFlowControl;
   private long currentTime;
   private long elapsedTime;
   private long lastTimeChanged;
   private long lastTimeLeave;
   private long asyncSendFlowControlTime;
   private final String runtimeMBeanName;
   private String unitOfOrderName;
   private String sequenceName;
   private int compressionThreshold = Integer.MAX_VALUE;
   private WLProducerImpl wlProducerImpl;
   private byte destinationFlags;
   private JMSProducer replacementProducer;
   private int oneWaySendMode;
   private int oneWayWindowSize;
   private static boolean ONEWAYSENDENABLED = false;
   private static int oneWaySendModeConf = 0;
   private static int oneWayWindowSizeConf = 1;
   private static final int MODE_SYNC_TRAN = 0;
   private static final int MODE_SYNC_NO_TRAN = 1;
   private static final int MODE_SYNC_NO_TRAN_WITH_ID = 2;
   private static final int MODE_NO_REPLY_WITH_ID = 3;
   private static final int MODE_ASYNC_TRAN = 4;
   private static final int MODE_ASYNC_NO_TRAN = 5;
   int count = 0;
   int totalConsecutiveOneWaySendMessageSize = 0;

   JMSProducer(JMSSession var1, JMSID var2, DestinationImpl var3, String var4) {
      this.session = var1;
      this.producerId = var2;
      this.destination = var3;
      this.runtimeMBeanName = var4;
      this.peerInfo = var1.getConnection().getFEPeerInfo();
      this.deliveryMode = var1.getDeliveryMode();
      this.priority = var1.getPriority();
      this.timeToLive = var1.getTimeToLive();
      this.sendTimeout = var1.getSendTimeout();
      JMSConnection var5 = var1.getConnection();
      this.flowControlEnabled = var5.isFlowControlEnabled();
      this.flowMinimum = var5.getFlowMinimum();
      this.flowMaximum = var5.getFlowMaximum();
      this.flowIncrease = var5.getFlowIncrease();
      this.flowDecrease = var5.getFlowDecrease();
      this.flowInterval = var5.getFlowInterval();
      this.flowRateCurrent = (double)this.flowMaximum;
      this.backOffTime = (long)(1000.0 / this.flowRateCurrent);
      this.compressionThreshold = var1.getConnection().getCompressionThreshold();
      this.oneWaySendMode = var5.getOneWaySendMode();
      this.oneWayWindowSize = var5.getOneWaySendWindowSize();
      if (this.oneWaySendMode != 0 && JMSEnvironment.getJMSEnvironment().isThinClient()) {
         this.oneWaySendMode = 0;
      }

   }

   public Object clone() throws CloneNotSupportedException {
      JMSProducer var1 = (JMSProducer)super.clone();
      return var1;
   }

   public ReconnectController getReconnectController() {
      return this.wlProducerImpl;
   }

   public Reconnectable getReconnectState(int var1) throws CloneNotSupportedException {
      JMSProducer var2 = (JMSProducer)this.clone();
      this.closeInProgress = true;
      return var2;
   }

   public Reconnectable preCreateReplacement(Reconnectable var1) throws JMSException {
      JMSProducer var2 = ((JMSSession)var1).setupJMSProducer(this.destination, this.destinationFlags);
      if (this.unitOfOrderName == null && var2.getUnitOfOrder() != null || this.unitOfOrderName != null && !this.unitOfOrderName.equals(var2.getUnitOfOrder())) {
         var2.setUnitOfOrder(this.unitOfOrderName);
      }

      var2.sequenceName = this.sequenceName;
      var2.messageIdsDisabled = this.messageIdsDisabled;
      var2.messageTimestampsDisabled = this.messageTimestampsDisabled;
      var2.deliveryMode = this.deliveryMode;
      var2.priority = this.priority;
      var2.timeToDeliver = this.timeToDeliver;
      var2.timeToLive = this.timeToLive;
      var2.redeliveryLimit = this.redeliveryLimit;
      var2.sendTimeout = this.sendTimeout;
      this.replacementProducer = var2;
      return var2;
   }

   public void postCreateReplacement() {
      this.replacementProducer.setWlProducerImpl(this.wlProducerImpl);
      this.wlProducerImpl.setPhysicalReconnectable(this.replacementProducer);
   }

   public boolean isReconnectControllerClosed() {
      return this.wlProducerImpl == null || this.wlProducerImpl.isClosed();
   }

   public void forgetReconnectState() {
      this.replacementProducer = null;
   }

   public PeerInfo getFEPeerInfo() {
      return this.session.getFEPeerInfo();
   }

   public boolean isClosed() {
      return this.producerId == null || this.closeInProgress;
   }

   public String getWLSServerName() {
      return this.session.getConnection().getWLSServerName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return this.session;
   }

   public String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   public final void setCompressionThreshold(int var1) throws JMSException {
      if (var1 < 0) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidCompressionThresholdLoggable().getMessage());
      } else {
         this.compressionThreshold = var1;
      }
   }

   public final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   void setId(JMSID var1) {
      this.producerId = var1;
   }

   public JMSID getJMSID() {
      return this.producerId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   public Destination getDestination() throws JMSException {
      this.checkClosed();
      return this.destination;
   }

   public void send(Destination var1, Message var2) throws JMSException {
      this.sendWithListener((CompletionListener)null, var1, var2);
   }

   private void sendWithListener(CompletionListener var1, Destination var2, Message var3) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestinationLoggable().getMessage());
      } else {
         this.sendInternal(var2, var3, this.deliveryMode, this.priority, this.timeToLive, var1);
      }
   }

   public void send(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.sendWithListener((CompletionListener)null, var1, var2, var3, var4, var5);
   }

   private void sendWithListener(CompletionListener var1, Destination var2, Message var3, int var4, int var5, long var6) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestination2Loggable().getMessage());
      } else {
         this.sendInternal(var2, var3, var4, var5, var6, var1);
      }
   }

   public void send(Message var1) throws JMSException {
      this.sendWithListener((CompletionListener)null, var1);
   }

   private void sendWithListener(CompletionListener var1, Message var2) throws JMSException {
      if (this.destination == null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logNeedDestinationLoggable().getMessage());
      } else {
         this.sendInternal(this.destination, var2, this.deliveryMode, this.priority, this.timeToLive, var1);
      }
   }

   public void send(Message var1, int var2, int var3, long var4) throws JMSException {
      this.sendWithListener((CompletionListener)null, var1, var2, var3, var4);
   }

   private void sendWithListener(CompletionListener var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.destination == null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logNeedDestination2Loggable().getMessage());
      } else {
         this.sendInternal(this.destination, var2, var3, var4, var5, var1);
      }
   }

   public void send(Queue var1, Message var2) throws JMSException {
      this.send((Destination)var1, var2);
   }

   public void send(Queue var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.send((Destination)var1, var2, var3, var4, var5);
   }

   public Queue getQueue() throws JMSException {
      return (Queue)this.getDestination();
   }

   public Topic getTopic() throws JMSException {
      return (Topic)this.getDestination();
   }

   public void publish(Message var1) throws JMSException {
      this.send(var1);
   }

   public void publish(Message var1, int var2, int var3, long var4) throws JMSException {
      this.send(var1, var2, var3, var4);
   }

   public void publish(Topic var1, Message var2) throws JMSException {
      this.send((Destination)var1, var2);
   }

   public void publish(Topic var1, Message var2, int var3, int var4, long var5) throws JMSException {
      this.send((Destination)var1, var2, var3, var4, var5);
   }

   private void forwardInternal(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (!(var2 instanceof MessageImpl)) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNotForwardable3Loggable());
      } else if (this.peerInfo.getMajor() < 9) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logUnsupportedLoggable());
      } else if (!((MessageImpl)var2).isForwardable()) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNotForwardable2Loggable());
      } else {
         this.deliveryInternal(var1, var2, var3, var4, var5, true, (CompletionListener)null);
      }
   }

   private void sendInternal(Destination var1, Message var2, int var3, int var4, long var5, CompletionListener var7) throws JMSException {
      this.deliveryInternal(var1, var2, var3, var4, var5, false, var7);
   }

   public static void sendReturn(ProducerSendResponse var0, Message var1, MessageImpl var2, boolean var3, long var4, long var6, int var8, int var9, Destination var10) throws JMSException {
      JMSMessageId var11 = var0.getMessageId();
      if (!var3) {
         var2.setId(var11);
         var2.setOldMessage(!var0.get90StyleMessageId());
      }

      if (var1 == var2) {
         if (var4 != 0L) {
            var2.setDeliveryTime(var11.getTimestamp() + var4);
         }

         var2.setJMSDestinationImpl((DestinationImpl)var10);
      } else {
         try {
            var1.setJMSDestination(var10);
         } catch (ClassCastException var15) {
         } catch (InvalidDestinationException var16) {
         } catch (JMSException var17) {
         }

         var1.setJMSDeliveryMode(var2.getJMSDeliveryMode());
         var1.setJMSPriority(var2.getJMSPriority());
         if (var1 instanceof MessageImpl) {
            if (!var3) {
               ((MessageImpl)var1).setId(var11);
               ((MessageImpl)var1).setOldMessage(var2.isOldMessage());
            }
         } else {
            var1.setJMSTimestamp(var2.getJMSTimestamp());

            try {
               var1.setJMSMessageID(var2.getJMSMessageID());
            } catch (Throwable var14) {
            }
         }
      }

      if (var2.getJMSExpiration() != 0L) {
         var1.setJMSExpiration(var11.getTimestamp() + var6);
      } else {
         var1.setJMSExpiration(0L);
      }

      try {
         var8 = var0.getDeliveryMode();
         if (var8 != -1) {
            var1.setJMSDeliveryMode(var8);
         }
      } catch (JMSException var13) {
      }

      var9 = var0.getPriority();
      if (var9 != -1) {
         var1.setJMSPriority(var9);
      }

      var6 = var0.getTimeToLive();
      if (var6 != -1L) {
         var1.setJMSExpiration(var11.getTimestamp() + var6);
      }

      if (var1 instanceof MessageImpl) {
         var4 = var0.getTimeToDeliver();
         if (var4 != -1L) {
            ((MessageImpl)var1).setDeliveryTime(var11.getTimestamp() + var4);
         }

         int var12 = var0.getRedeliveryLimit();
         if (var12 != 0) {
            ((MessageImpl)var1).setJMSRedeliveryLimit(var12);
         }
      }

   }

   private void deliveryInternal(Destination var1, Message var2, int var3, int var4, long var5, boolean var7, CompletionListener var8) throws JMSException {
      if (var1 == null) {
         throw new weblogic.jms.common.InvalidDestinationException(JMSClientExceptionLogger.logNullDestination2Loggable().getMessage());
      } else if (!(var1 instanceof DestinationImpl)) {
         throw new weblogic.jms.common.InvalidDestinationException(JMSClientExceptionLogger.logForeignDestination2Loggable().getMessage());
      } else if (this.session.getType() == 2 && !((DestinationImpl)var1).isQueue()) {
         throw new weblogic.jms.common.InvalidDestinationException(JMSClientExceptionLogger.logMustBeAQueueLoggable(var1.toString()).getMessage());
      } else if (this.session.getType() == 1 && !((DestinationImpl)var1).isTopic()) {
         throw new weblogic.jms.common.InvalidDestinationException(JMSClientExceptionLogger.logMustBeATopicLoggable(var1.toString()).getMessage());
      } else {
         if (var8 == null) {
            this.doFlowControl();
         } else {
            this.asyncSendFlowControlTime = this.getFlowControlSleepTime();
         }

         MessageImpl var9;
         try {
            var9 = (MessageImpl)var2;
            var9.resetUserPropertySize();
            if (!var7) {
               var9.setForward(false);
               var9.resetForwardsCount();
               var9.setOldMessage(false);
               var9.setJMSXUserID((String)null);
               if ((var9.getSAFSequenceName() != null || var9.getSAFSeqNumber() != 0L) && !var9.getKeepSAFSequenceNameAndNumber()) {
                  var9.setSAFSequenceName((String)null);
                  var9.setSAFSeqNumber(0L);
               }
            }

            if (this.session.getConnection().isLocal()) {
               var9 = var9.copy();
               Destination var10 = var9.getJMSReplyTo();
               if (var10 != null && var10 instanceof DestinationImpl) {
                  try {
                     var9.setJMSReplyTo((Destination)((DestinationImpl)var10).clone());
                  } catch (CloneNotSupportedException var19) {
                  }
               }
            }
         } catch (ClassCastException var20) {
            var9 = convertMessage(var2);
         }

         if (var5 == 0L) {
            var5 = 0L;
         }

         try {
            var9.setJMSExpiration(var5);
            long var21 = this.getTimeToDeliverInternal();
            var9.setDeliveryTime(var21);
            var9.setJMSRedeliveryLimit(this.redeliveryLimit);
            var9.setJMSDestinationImpl((DestinationImpl)null);
            if (!var7) {
               var9.setId((JMSMessageId)null);
            }

            var9.setJMSDeliveryMode(var3);
            var9.setJMSPriority(var4);
            var9.setDDForwarded(false);
            var9.setDeliveryCount(0);
            var9.setUnitOfOrderName(this.unitOfOrderName);
            JMSWorkContextHelper.infectMessage(var9);
            if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
               JMSDebug.JMSMessagePath.debug("CLIENT/JMSProducer (id: " + this.producerId + ") : " + "Sending message deliveryMode = " + var3 + " priority = " + var4 + " timeToLive = " + var5 + " timeToDeliver = " + var21 + " redeliveryLimit = " + this.redeliveryLimit);
            }

            JMSProducerSendResponse var12 = (JMSProducerSendResponse)this.toFEProducer(var1, var2, var9, var3, var21, var4, var5, var7, var8);
         } finally {
            ;
         }
      }
   }

   private void wrappedSendReturn(JMSProducerSendResponse var1, Destination var2, Message var3, MessageImpl var4, int var5, long var6, int var8, long var9, boolean var11) throws JMSException {
      sendReturn(var1, var3, var4, var11, var6, var9, var5, var8, var2);
      if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
         JMSDebug.JMSMessagePath.debug("CLIENT/JMSProducer (id: " + this.producerId + ") : " + "Successfully sent message " + var3.getJMSMessageID());
      }

      this.updateFlowControl(var1);
   }

   private static MessageImpl convertMessage(Message var0) throws JMSException {
      try {
         if (var0 instanceof BytesMessage) {
            return new BytesMessageImpl((BytesMessage)var0);
         } else if (var0 instanceof MapMessage) {
            return new MapMessageImpl((MapMessage)var0);
         } else if (var0 instanceof ObjectMessage) {
            return new ObjectMessageImpl((ObjectMessage)var0);
         } else if (var0 instanceof StreamMessage) {
            return new StreamMessageImpl((StreamMessage)var0);
         } else {
            return (MessageImpl)(var0 instanceof TextMessage ? new TextMessageImpl((TextMessage)var0) : new HdrMessageImpl(var0));
         }
      } catch (IOException var2) {
         throw JMSUtilities.jmsExceptionThrowable(JMSClientExceptionLogger.logErrorConvertingForeignMessageLoggable().getMessage(), var2);
      }
   }

   public void setDisableMessageID(boolean var1) throws JMSException {
      this.checkClosed();
      this.messageIdsDisabled = var1;
   }

   public boolean getDisableMessageID() throws JMSException {
      this.checkClosed();
      return this.messageIdsDisabled;
   }

   public void setDisableMessageTimestamp(boolean var1) throws JMSException {
      this.checkClosed();
      this.messageTimestampsDisabled = var1;
   }

   public boolean getDisableMessageTimestamp() throws JMSException {
      this.checkClosed();
      return this.messageTimestampsDisabled;
   }

   public void setDeliveryMode(int var1) throws JMSException {
      this.checkClosed();
      if (var1 != 2 && var1 != 1) {
         if (var1 != -1) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidDeliveryModeLoggable());
         }

         this.deliveryMode = 2;
      } else {
         this.deliveryMode = var1;
      }

   }

   public int getDeliveryMode() throws JMSException {
      this.checkClosed();
      return this.deliveryMode;
   }

   public void setPriority(int var1) throws JMSException {
      this.checkClosed();
      if (var1 >= 0 && var1 <= 9) {
         this.priority = var1;
      } else {
         if (var1 != -1) {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidPriorityLoggable());
         }

         this.priority = 4;
      }

   }

   public int getPriority() throws JMSException {
      this.checkClosed();
      return this.priority;
   }

   public long getTimeToDeliver() throws JMSException {
      this.checkClosed();
      return this.getTimeToDeliverInternal();
   }

   private long getTimeToDeliverInternal() {
      return this.timeToDeliver == -1L ? this.session.getConnection().getTimeToDeliver() : this.timeToDeliver;
   }

   public void setTimeToDeliver(long var1) throws JMSException {
      this.checkClosed();
      if (var1 < -1L) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidTimeToDeliverLoggable().getMessage());
      } else {
         this.timeToDeliver = var1;
      }
   }

   public int getRedeliveryLimit() throws JMSException {
      this.checkClosed();
      return this.redeliveryLimit;
   }

   public void setRedeliveryLimit(int var1) throws JMSException {
      this.checkClosed();
      if (var1 < -1) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidRedeliveryLimitLoggable().getMessage());
      } else {
         this.redeliveryLimit = var1;
      }
   }

   public long getSendTimeout() throws JMSException {
      this.checkClosed();
      return this.sendTimeout;
   }

   public void setSendTimeout(long var1) throws JMSException {
      this.checkClosed();
      if (var1 < 0L) {
         throw new JMSException(JMSClientExceptionLogger.logInvalidSendTimeoutLoggable().getMessage());
      } else {
         this.sendTimeout = var1;
      }
   }

   public void setTimeToLive(long var1) throws JMSException {
      this.checkClosed();
      this.timeToLive = var1;
   }

   public long getTimeToLive() throws JMSException {
      this.checkClosed();
      return this.timeToLive;
   }

   public void close() throws JMSException {
      JMSID var1;
      synchronized(this) {
         if (this.isClosed()) {
            return;
         }

         Object var3 = this.wlProducerImpl == null ? this : this.wlProducerImpl.getConnectionStateLock();
         synchronized(var3) {
            var1 = this.producerId;
            this.producerId = null;
         }
      }

      this.session.producerClose(var1);
   }

   boolean hasTemporaryDestination() {
      DestinationImpl var1 = this.destination;
      return var1 != null && (var1.getType() == 4 || var1.getType() == 8);
   }

   JMSSession getSession() {
      return this.session;
   }

   public String toString() {
      return this.session.getConnection().getRuntimeMBeanName() + "." + this.session.getRuntimeMBeanName() + "." + this.getRuntimeMBeanName();
   }

   private void checkClosed() throws JMSException {
      if (this.isClosed()) {
         Object var1 = this.wlProducerImpl == null ? this : this.wlProducerImpl.getConnectionStateLock();
         synchronized(var1){}

         try {
            if (this.isReconnectControllerClosed()) {
               throw new AlreadyClosedException(JMSClientExceptionLogger.logClosedProducerLoggable());
            } else {
               throw new LostServerException(JMSClientExceptionLogger.logLostServerConnectionLoggable());
            }
         } finally {
            ;
         }
      }
   }

   public final void publicCheckClosed() throws JMSException {
      this.checkClosed();
   }

   public int invoke(Request var1) {
      return Integer.MAX_VALUE;
   }

   public void setUnitOfOrder(String var1) throws JMSException {
      this.checkClosed();
      if (this.peerInfo.getMajor() < 9) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logUnsupportedLoggable().getMessage());
      } else {
         this.unitOfOrderName = var1;
      }
   }

   public void setUnitOfOrder() throws JMSException {
      this.setUnitOfOrder(this.session.getJMSID().toString());
   }

   public String getUnitOfOrder() throws JMSException {
      this.checkClosed();
      return this.unitOfOrderName;
   }

   private Object toFEProducer(Destination var1, Message var2, MessageImpl var3, int var4, long var5, int var7, long var8, boolean var10, CompletionListener var11) throws JMSException {
      JMSDispatcher var12 = this.session.getConnection().getFrontEndDispatcher();
      if (ONEWAYSENDENABLED) {
         this.oneWaySendMode = oneWaySendModeConf;
         this.oneWayWindowSize = oneWayWindowSizeConf;
      }

      boolean var13 = (this.oneWaySendMode == 1 || this.oneWaySendMode == 2 && ((DestinationImpl)var1).isTopic()) && !(var1 instanceof DistributedDestinationImpl) && this.getUnitOfOrder() == null && var3.getJMSDeliveryMode() == 1 && !((DestinationImpl)var1).isStale();
      var13 = var13 && ((DestinationImpl)var1).getDispatcherId().isSameServer(var12.getId());
      JMSProducerSendResponse var14 = null;
      boolean var15 = false;
      FEProducerSendRequest var17 = new FEProducerSendRequest(this.producerId, var3, this.destination == null ? (DestinationImpl)var1 : null, this.sendTimeout, this.compressionThreshold, var11, this, var2, var4, var5, var7, var8, var10);
      if (JMSDebug.JMSMessagePath.isDebugEnabled()) {
         JMSDebug.JMSMessagePath.debug("CLIENT/JMSProducer (id: " + this.producerId + ") : " + "Dispatching message to FRONTEND/FEProducer" + " for destination: " + var1);
      }

      byte var16;
      JMSMessageId var19;
      if (!this.session.isTransacted() && this.session.userTransactionsEnabled()) {
         if (var11 == null) {
            var16 = 0;
         } else {
            var16 = 4;
         }
      } else if (!this.session.isTransacted() && var13) {
         boolean var18 = this.flowControlEnabled;
         if (var18) {
            this.flowControlEnabled = false;
         }

         if (this.count == Integer.MAX_VALUE) {
            this.count = 0;
         }

         if (++this.count % this.oneWayWindowSize != 1 && this.oneWayWindowSize > 1 && this.totalConsecutiveOneWaySendMessageSize <= ONEWAYSENDCONSECUTIVEMESSAGELIMIT) {
            var19 = var3.getId();
            var15 = var19 != null;
            if (var19 == null) {
               var19 = JMSMessageId.create();
            }

            var14 = new JMSProducerSendResponse(var19);
            var14.set90StyleMessageId();
            var14.setDeliveryMode(1);
            var14.setPriority(var7);
            var14.setTimeToLive(var8);
            var14.setTimeToDeliver(var5);
            var14.setMessage(var3);
            var14.setRequest(var17);
            var17.setNoResponse(true);
            var3.setId(var19);
            if (var3.getDeliveryTime() > 0L) {
               var14.setTimeToDeliver(var3.getDeliveryTime());
            }

            var16 = 3;
         } else {
            this.totalConsecutiveOneWaySendMessageSize = 0;
            if (var11 == null) {
               var16 = 2;
            } else {
               var16 = 5;
            }
         }
      } else {
         if (this.session.isTransacted()) {
            this.session.setPendingWork(true);
         }

         if (var11 == null) {
            var16 = 1;
         } else {
            var16 = 5;
         }
      }

      var3.setSerializeDestination(false);

      try {
         Object var33;
         switch (var16) {
            case 0:
               var33 = var12.dispatchSyncTran(var17);
               break;
            case 1:
               var33 = var12.dispatchSyncNoTran(var17);
               break;
            case 2:
               var33 = var12.dispatchSyncNoTranWithId(var17, this.getJMSID().getCounter());
               break;
            case 3:
               var17.setResult(var14);
               var12.dispatchNoReplyWithId(var17, this.getJMSID().getCounter());
               this.totalConsecutiveOneWaySendMessageSize += var17.getDataLen();
               var33 = var14;
               break;
            case 4:
               var12.dispatchAsync(var17);
               var19 = null;
               return var19;
            case 5:
               Transaction var34 = TransactionHelper.getTransactionHelper().getTransactionManager().forceSuspend();

               try {
                  var12.dispatchAsync(var17);
               } finally {
                  if (var34 != null) {
                     TransactionHelper.getTransactionHelper().getTransactionManager().forceResume(var34);
                  }

               }

               Object var20 = null;
               return var20;
            default:
               var33 = null;
         }

         this.wrappedSendReturn((JMSProducerSendResponse)var33, var1, var2, var3, var4, var5, var7, var8, var10);
         if (var11 != null) {
            var17.onCompletion(var33);
         }

         Object var35 = var33;
         return var35;
      } catch (Exception var31) {
         if (var16 == 3 && !var15) {
            var3.setId((JMSMessageId)null);
         }

         if (var31 instanceof JMSException) {
            throw (JMSException)var31;
         } else {
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logErrorSendingMessageLoggable(), var31);
         }
      } finally {
         var3.setSerializeDestination(true);
      }
   }

   public void completeAsyncSend(Object var1, FEProducerSendRequest var2, DestinationImpl var3, Message var4, MessageImpl var5, int var6, long var7, int var9, long var10, boolean var12, CompletionListener var13) {
      JMSProducerSendResponse var14;
      try {
         var2.getMessage().setSerializeDestination(true);
         var14 = (JMSProducerSendResponse)var1;
         this.wrappedSendReturn(var14, var3, var4, var5, var6, var7, var9, var10, var12);
      } catch (Throwable var16) {
         var13.onException(var16);
         return;
      }

      var14.setMessage(var4);
      var14.setAsyncFlowControlTime(this.asyncSendFlowControlTime);
      var13.onCompletion(var14);
   }

   public void forward(Destination var1, Message var2) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestinationLoggable().getMessage());
      } else {
         this.forwardInternal(var1, var2, this.deliveryMode, this.priority, this.timeToLive);
      }
   }

   public void forward(Message var1) throws JMSException {
      if (this.destination == null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logNeedDestinationLoggable().getMessage());
      } else {
         this.forwardInternal(this.destination, var1, this.deliveryMode, this.priority, this.timeToLive);
      }
   }

   public void forward(Message var1, int var2, int var3, long var4) throws JMSException {
      if (this.destination == null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logNeedDestinationLoggable().getMessage());
      } else {
         this.forwardInternal(this.destination, var1, var2, var3, var4);
      }
   }

   public void forward(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestination2Loggable().getMessage());
      } else {
         this.forwardInternal(var1, var2, var3, var4, var5);
      }
   }

   private final void updateFlowControl(JMSProducerSendResponse var1) {
      if (this.flowControlEnabled && var1 != null) {
         if (var1.getNeedsFlowControl()) {
            this.needsFlowControl = true;
            if (var1.getFlowControlTime() == -1L) {
               if (this.flowRateCurrent > (double)this.flowMaximum) {
                  this.flowRateCurrent = (double)this.flowMaximum;
               }

               if (this.currentTime - this.lastTimeChanged >= this.flowInterval && this.flowRateCurrent > (double)this.flowMinimum) {
                  this.flowRateCurrent *= this.flowDecrease;
                  if (this.flowRateCurrent < (double)this.flowMinimum) {
                     this.flowRateCurrent = (double)this.flowMinimum;
                  }

                  this.lastTimeChanged = this.currentTime;
                  this.backOffTime = (long)(1000.0 / this.flowRateCurrent);
               }
            } else {
               this.backOffTime = var1.getFlowControlTime();
            }

            this.lastTimeLeave = this.currentTime;
         } else {
            this.needsFlowControl = false;
            if (this.currentTime - this.lastTimeChanged >= this.flowInterval && this.flowRateCurrent < (double)this.flowMaximum) {
               this.flowRateCurrent += (double)this.flowIncrease;
               this.lastTimeChanged = this.currentTime;
               this.backOffTime = (long)(1000.0 / this.flowRateCurrent);
            }
         }

      }
   }

   private final void doFlowControl() {
      long var1 = this.getFlowControlSleepTime();
      if (var1 != 0L) {
         try {
            Thread.sleep(var1);
         } catch (Exception var4) {
         }

      }
   }

   private final long getFlowControlSleepTime() {
      if (!this.flowControlEnabled) {
         return 0L;
      } else {
         this.currentTime = System.currentTimeMillis();
         this.elapsedTime = this.currentTime - this.lastTimeLeave;
         long var1 = 0L;
         if (this.flowControlEnabled && (this.needsFlowControl || this.flowRateCurrent <= (double)this.flowMaximum) && this.elapsedTime <= this.backOffTime) {
            var1 = this.backOffTime - this.elapsedTime;
            this.currentTime = System.currentTimeMillis() + var1;
         }

         return var1;
      }
   }

   public void setSequence(String var1) throws JMSException {
      if (this.peerInfo.getMajor() < 9) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logUnsupportedLoggable().getMessage());
      } else {
         this.sequenceName = var1;
      }
   }

   public String getSequence() throws JMSException {
      return this.sequenceName;
   }

   public void reserveUnitOfOrderWithSequence() throws JMSException {
      this.processSequenceInternal(196608, this.destination, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void reserveSequence(Destination var1, Message var2, int var3, int var4, long var5) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestinationLoggable().getMessage());
      } else {
         this.processSequenceInternal(196608, var1, var3, var4, var5);
      }
   }

   public void releaseSequenceAndUnitOfOrder(boolean var1) throws JMSException {
      int var2;
      if (var1) {
         var2 = 65536;
      } else {
         var2 = 131072;
      }

      this.processSequenceInternal(var2, this.destination, this.deliveryMode, this.priority, this.timeToLive);
   }

   public void releaseSequenceAndUnitOfOrder(Destination var1, Message var2, int var3, int var4, long var5, boolean var7) throws JMSException {
      if (this.destination != null) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logCannotOverrideDestinationLoggable().getMessage());
      } else {
         int var8;
         if (var7) {
            var8 = 65536;
         } else {
            var8 = 131072;
         }

         this.processSequenceInternal(var8, var1, var3, var4, var5);
      }
   }

   private void processSequenceInternal(int var1, Destination var2, int var3, int var4, long var5) throws JMSException {
      if (this.peerInfo.getMajor() < 9) {
         throw new UnsupportedOperationException(JMSClientExceptionLogger.logUnsupportedLoggable().getMessage());
      } else if (this.sequenceName == null) {
         throw new UnsupportedOperationException("null sequence not permitted");
      } else {
         HdrMessageImpl var7 = new HdrMessageImpl();
         var7.setSAFSequenceName(this.sequenceName);
         var7.setControlOpcode(var1);
         this.deliveryInternal(var2, var7, var3, var4, var5, false, (CompletionListener)null);
      }
   }

   void setWlProducerImpl(WLProducerImpl var1) {
      this.wlProducerImpl = var1;
   }

   void setDestinationFlags(byte var1) {
      this.destinationFlags = var1;
   }

   public void sendAsync(Message var1, CompletionListener var2) {
      try {
         this.sendWithListener(var2, var1);
      } catch (JMSException var4) {
         var2.onException(var4);
      }

   }

   public void sendAsync(Message var1, int var2, int var3, long var4, CompletionListener var6) {
      try {
         this.sendWithListener(var6, var1, var2, var3, var4);
      } catch (JMSException var8) {
         var6.onException(var8);
      }

   }

   public void sendAsync(Destination var1, Message var2, CompletionListener var3) {
      try {
         this.sendWithListener(var3, var1, var2);
      } catch (JMSException var5) {
         var3.onException(var5);
      }

   }

   public void sendAsync(Destination var1, Message var2, int var3, int var4, long var5, CompletionListener var7) {
      try {
         this.sendWithListener(var7, var1, var2, var3, var4, var5);
      } catch (JMSException var9) {
         var7.onException(var9);
      }

   }

   static {
      try {
         if (System.getProperty("weblogic.jms.client.onewaysendconfigs") != null) {
            ONEWAYSENDENABLED = true;
            String var0 = System.getProperty("weblogic.jms.client.onewaysendconfigs");
            StringTokenizer var1 = new StringTokenizer(var0);
            if (var1.hasMoreTokens()) {
               oneWaySendModeConf = JMSConnection.convertOneWaySendMode(var1.nextToken());
            }

            if (var1.hasMoreTokens()) {
               oneWayWindowSizeConf = Integer.parseInt(var1.nextToken());
            }

            if (var1.hasMoreTokens()) {
               ONEWAYSENDCONSECUTIVEMESSAGELIMIT = Integer.parseInt(var1.nextToken());
            }
         }
      } catch (RuntimeException var2) {
      }

   }
}
