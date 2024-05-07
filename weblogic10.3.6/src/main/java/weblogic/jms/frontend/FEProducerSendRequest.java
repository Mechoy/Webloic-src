package weblogic.jms.frontend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import javax.jms.Message;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.jms.client.JMSProducer;
import weblogic.jms.common.Destination;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSProducerSendResponse;
import weblogic.jms.common.JMSUtilities;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.Request;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.messaging.dispatcher.Response;

public final class FEProducerSendRequest extends Request implements CompletionListener, Externalizable {
   static final long serialVersionUID = -6258361113844425358L;
   private static final int EXTVERSION61 = 1;
   private static final int EXTVERSION81 = 2;
   private static final int EXTVERSION902 = 4;
   private static final int EXTVERSION = 4;
   private static final int VERSION_MASK = 255;
   private static final int MESSAGE_MASK = 65280;
   private static final int DESTINATION_MASK = 983040;
   private static final int TIMEOUT_MASK = 1048576;
   private static final int NORESPONSE_MASK = 2097152;
   public static final int CHECK_UNIT_OF_ORDER = 2097152;
   private static final int MESSAGE_MASK_SHIFT = 8;
   private static final int DESTINATION_MASK_SHIFT = 16;
   static final int START = 0;
   static final int CONTINUE = 1;
   static final int TRY = 2;
   static final int RETRY = 3;
   static final int AFTER_START_IP = 4;
   static final int AFTER_POST_AUTH_IP = 5;
   static final int RETURN_FROM_START_IP = 6;
   static final int RETURN_FROM_POST_AUTH_IP = 7;
   static final int RELEASE_FANOUT = 8;
   private transient boolean infected;
   private MessageImpl message;
   private DestinationImpl destination;
   private long sendTimeout;
   private transient JMSDispatcher dispatcher;
   private transient Object failover;
   private transient int checkUnitOfOrder;
   private transient String pathServiceJndi;
   private transient Serializable uooKey;
   private transient boolean uooNoFailover;
   private transient int numberOfRetries;
   private transient FEProducerSendRequest[] subRequest;
   private transient String unitForRouting;
   private transient int dataLen;
   private transient Request backendRequest;
   private transient boolean noResponse;
   private int compressionThreshold;
   private transient Object authenticatedSubject;
   private transient Message messageState;
   private transient int deliveryModeState;
   private transient long timeToDeliverState;
   private transient int priorityState;
   private transient long timeToLiveState;
   private transient boolean forwardingState;
   private transient CompletionListener appListener;
   private transient JMSProducer jmsProducer;
   private boolean pushPopSubject;

   public FEProducerSendRequest(JMSID var1, MessageImpl var2, DestinationImpl var3, long var4, int var6, CompletionListener var7, JMSProducer var8, Message var9, int var10, long var11, int var13, long var14, boolean var16) {
      this(var1, var2, var3, var4, var6);
      if (var7 != null) {
         this.appListener = var7;
         this.jmsProducer = var8;
         this.messageState = var9;
         this.deliveryModeState = var10;
         this.timeToDeliverState = var11;
         this.priorityState = var13;
         this.timeToLiveState = var14;
         this.forwardingState = var16;
         this.setListener(this);
      }

   }

   public FEProducerSendRequest(JMSID var1, MessageImpl var2, DestinationImpl var3, long var4, int var6) {
      super(var1, 5129);
      this.noResponse = false;
      this.compressionThreshold = Integer.MAX_VALUE;
      this.pushPopSubject = false;
      this.message = var2;
      this.destination = var3;
      this.sendTimeout = var4;
      this.compressionThreshold = var6;
   }

   public final int getCompressionThreshold() {
      return this.compressionThreshold;
   }

   public void rememberOneWayState() {
   }

   public void setNoResponse(boolean var1) {
      this.noResponse = var1;
   }

   public boolean isNoResponse() {
      return this.noResponse;
   }

   public MessageImpl getMessage() {
      return this.message;
   }

   DestinationImpl getDestination() {
      return this.destination;
   }

   void setDestination(DestinationImpl var1) {
      this.destination = var1;
   }

   public int getDataLen() {
      return this.dataLen;
   }

   JMSDispatcher getDispatcher() {
      return this.dispatcher;
   }

   void setDispatcher(JMSDispatcher var1) {
      this.dispatcher = var1;
   }

   void setFailover(Object var1) {
      this.failover = var1;
   }

   Object getFailover() {
      return this.failover;
   }

   public long getSendTimeout() {
      return this.sendTimeout;
   }

   void setInfected(boolean var1) {
      this.infected = var1;
   }

   boolean isInfected() {
      return this.infected;
   }

   void setUooNoFailover(boolean var1) {
      this.uooNoFailover = var1;
   }

   boolean getUOONoFailover() {
      return this.uooNoFailover;
   }

   void setUnitForRouting(String var1) {
      this.unitForRouting = var1;
   }

   String getUnitForRouting() {
      return this.unitForRouting;
   }

   public void setCheckUOO(int var1) {
      this.checkUnitOfOrder = var1;
   }

   int getCheckUOO() {
      return this.checkUnitOfOrder;
   }

   int getNumberOfRetries() {
      return this.numberOfRetries;
   }

   public void setNumberOfRetries(int var1) {
      this.numberOfRetries = var1;
   }

   FEProducerSendRequest[] getSubRequest() {
      return this.subRequest;
   }

   void setSubRequest(FEProducerSendRequest[] var1) {
      this.subRequest = var1;
   }

   public Request getBackendRequest() {
      return this.backendRequest;
   }

   public void setBackendRequest(Request var1) {
      this.backendRequest = var1;
   }

   public void setUOOInfo(String var1, Serializable var2) {
      this.pathServiceJndi = var1;
      this.uooKey = var2;
   }

   public String getPathServiceJndi() {
      return this.pathServiceJndi;
   }

   public Object getUOOKey() {
      return this.uooKey;
   }

   public int remoteSignature() {
      return this.noResponse ? 64 : 19;
   }

   public Response createResponse() {
      return new JMSProducerSendResponse();
   }

   public void onCompletion(Object var1) {
      CompletionListener var2;
      synchronized(this) {
         if (this.appListener == null) {
            return;
         }

         var2 = this.appListener;
         this.appListener = null;
      }

      try {
         this.jmsProducer.completeAsyncSend(var1, this, this.destination, this.messageState, this.message, this.deliveryModeState, this.timeToDeliverState, this.priorityState, this.timeToLiveState, this.forwardingState, var2);
      } catch (Throwable var5) {
         var2.onException(var5);
      }

   }

   public void onException(Throwable var1) {
      CompletionListener var2;
      synchronized(this) {
         if (this.appListener == null) {
            return;
         }

         var2 = this.appListener;
         this.appListener = null;
      }

      var2.onException(var1);
   }

   public FEProducerSendRequest() {
      this.noResponse = false;
      this.compressionThreshold = Integer.MAX_VALUE;
      this.pushPopSubject = false;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      int var2 = 0;
      var1 = this.getVersionedStream(var1);
      PeerInfo var3 = ((PeerInfoable)var1).getPeerInfo();
      if (var3.compareTo(PeerInfo.VERSION_61) < 0) {
         throw JMSUtilities.versionIOException(0, 1, 2);
      } else {
         byte var4;
         if (var3.compareTo(PeerInfo.VERSION_81) < 0) {
            var4 = 1;
         } else if (var3.compareTo(PeerInfo.VERSION_901) < 0) {
            var4 = 2;
         } else {
            var4 = 4;
         }

         int var5 = var4 | this.message.getType() << 8;
         var5 |= Destination.getDestinationType(this.destination, 16);
         if (var4 >= 2 && this.sendTimeout != 10L) {
            var5 |= 1048576;
         }

         if (var4 >= 4 && this.noResponse) {
            var5 |= 2097152;
         }

         var1.writeInt(var5);
         super.writeExternal(var1);
         if (this.noResponse) {
            var2 = MessageImpl.getPosition(var1);
         }

         this.message.writeExternal(MessageImpl.createJMSObjectOutputWrapper(var1, this.compressionThreshold, false));
         if (this.noResponse) {
            this.dataLen = MessageImpl.getPosition(var1) - var2;
         }

         if (this.destination != null) {
            this.destination.writeExternal(var1);
         }

         if ((var5 & 1048576) != 0) {
            var1.writeLong(this.sendTimeout);
         }

      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      int var3 = var2 & 255;
      if (var3 >= 1 && var3 <= 4) {
         super.readExternal(var1);
         int var4 = (var2 & '\uff00') >>> 8;
         this.message = MessageImpl.createMessageImpl((byte)var4);
         this.message.readExternal(var1);
         int var5 = (var2 & 983040) >>> 16;
         this.destination = Destination.createDestination((byte)var5, var1);
         if ((var2 & 1048576) != 0) {
            this.sendTimeout = var1.readLong();
         }

         this.noResponse = (var2 & 2097152) != 0;
      } else {
         throw JMSUtilities.versionIOException(var3, 1, 4);
      }
   }

   void setUpPushPopSubject(boolean var1) {
      this.pushPopSubject = var1;
   }

   boolean getPushPopSubject() {
      return this.pushPopSubject;
   }

   void setAuthenticatedSubject(Object var1) {
      this.authenticatedSubject = var1;
   }

   Object getAuthenticatedSubject() {
      return this.authenticatedSubject;
   }
}
