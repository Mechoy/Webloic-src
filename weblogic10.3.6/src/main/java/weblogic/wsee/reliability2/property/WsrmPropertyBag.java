package weblogic.wsee.reliability2.property;

import com.sun.istack.Nullable;
import com.sun.xml.ws.addressing.WsaPropertyBag;
import com.sun.xml.ws.api.PropertySet;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.PropertySet.Property;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.TransportBackChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.wsee.jaxws.framework.PropertySetUtil;
import weblogic.wsee.jaxws.framework.SerializableWSEndpointReference;
import weblogic.wsee.jaxws.persistence.PacketPersistencePropertyBag;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;

public class WsrmPropertyBag extends PropertySet {
   public static final String INTERNAL_SEND = "weblogic.wsee.reliability2.InternalSend";
   public static final String INTERNAL_RECEIVE = "weblogic.wsee.reliability2.InternalReceive";
   public static final String REPLYTO_FROM_REQUEST = "weblogic.wsee.reliability2.ReplyToFromRequest";
   public static final String FAULTTO_FROM_REQUEST = "weblogic.wsee.reliability2.FaultToFromRequest";
   public static final String INBOUND_MSGID = "weblogic.wsee.reliability2.MessageIdFromRequest";
   public static final String DEST_MSGINFO_FROM_REQUEST = "weblogic.wsee.reliability2.DestMsgInfoFromRequest";
   public static final String INBOUND_SEQUENCE_ID = "weblogic.wsee.reliability2.IncomingSequenceID";
   public static final String OUTBOUND_SEQUENCE_ID = "weblogic.wsee.reliability2.OutgoingSequenceID";
   public static final String INBOUND_OP_NAME = "weblogic.wsee.reliability2.InboundOperationName";
   public static final String OUTBOUND_OP_NAME = "weblogic.wsee.reliability2.OutboundOperationName";
   public static final String OUTBOUND_MSG_NEEDS_DEST_SEQ_ID = "weblogic.wsee.reliability2.OutboundMsgNeedsDestSeqId";
   public static final String INBOUND_ENDPOINT_ADDRESS = "weblogic.wsee.reliability2.InboundEndpointAddress";
   public static final String RESPONSE_TO_SUSPENDED_FIBER = "weblogic.wsee.reliability2.ResponseToSuspendedFiber";
   public static final String TRANSPORT_BACK_CHANNEL = "weblogic.wsee.reliability2.TransportBackChannel";
   public static final String BUFFERED_MESSAGE_JMS_DELIVERY_COUNT = "weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount";
   public static PropertySetUtil.PropertySetRetriever<WsrmPropertyBag> propertySetRetriever = PropertySetUtil.getRetriever(WsrmPropertyBag.class);
   private static final PropertySet.PropertyMap model = parse(WsrmPropertyBag.class);
   public static final Set<String> PERSISTENT_PROP_NAMES = new HashSet();
   private Packet _packet;
   private boolean _internalSend = false;
   private boolean _internalReceive = false;
   @Nullable
   private SerializableWSEndpointReference _replyToFromRequest;
   @Nullable
   private SerializableWSEndpointReference _faultToFromRequest;
   @Nullable
   private String _inboundMsgId;
   private DestinationMessageInfo _destMsgInfoFromRequest;
   private String _inboundSequenceId;
   private String _outboundSequenceId;
   private String _inboundOpName;
   private String _outboundOpName;
   private boolean _outboundMsgNeedsDestSeqId;
   private String _incomingEndpointAddress;
   private boolean _responseToSuspendedFiber;
   @Nullable
   private TransportBackChannel _backChannel;
   private int _bufferedMessageJmsDeliveryCount;

   protected PropertySet.PropertyMap getPropertyMap() {
      return model;
   }

   public WsrmPropertyBag(Packet var1) {
      this._packet = var1;
      this._outboundMsgNeedsDestSeqId = false;
   }

   public static void flagPersistentPropsOnPacket(Packet var0) {
      propertySetRetriever.getFromPacket(var0);
      PacketPersistencePropertyBag var1 = (PacketPersistencePropertyBag)PacketPersistencePropertyBag.propertySetRetriever.getFromPacket(var0);
      var1.getPersistablePropertyBagClassNames().add(WsrmPropertyBag.class.getName());
      Iterator var2 = PERSISTENT_PROP_NAMES.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.getPersistablePropertyNames().add(var3);
      }

   }

   public void capturePersistentPropsFromJaxWsRi(AddressingVersion var1) {
      WSEndpointReference var2;
      if (this._packet.supports("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest")) {
         var2 = (WSEndpointReference)this._packet.get("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest");
         this._replyToFromRequest = new SerializableWSEndpointReference(var2, var1);
      }

      if (this._packet.supports("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest")) {
         var2 = (WSEndpointReference)this._packet.get("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest");
         this._faultToFromRequest = new SerializableWSEndpointReference(var2, var1);
      }

      if (this._packet.supports("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest")) {
         this._inboundMsgId = (String)this._packet.get("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest");
      }

   }

   public void restorePersistentPropsIntoJaxWsRi(AddressingVersion var1, SOAPVersion var2) {
      this.restoreReplyToIntoJaxwsRi(var1, var2);
      this.restoreFaultToIntoJaxwsRi(var1, var2);
      this.restoreMessageIdIntoJaxwsRi(var1, var2);
   }

   private void restoreFaultToIntoJaxwsRi(AddressingVersion var1, SOAPVersion var2) {
      if (this._faultToFromRequest != null) {
         this.ensureWsaPropertyBag(var1, var2);
         this._packet.put("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest", this._faultToFromRequest.getRef());
      }

   }

   private void restoreReplyToIntoJaxwsRi(AddressingVersion var1, SOAPVersion var2) {
      if (this._replyToFromRequest != null) {
         this.ensureWsaPropertyBag(var1, var2);
         this._packet.put("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest", this._replyToFromRequest.getRef());
      }

   }

   private void restoreMessageIdIntoJaxwsRi(AddressingVersion var1, SOAPVersion var2) {
      if (this._inboundMsgId != null) {
         this.ensureWsaPropertyBag(var1, var2);
         this._packet.put("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest", this._inboundMsgId);
      }

   }

   private void ensureWsaPropertyBag(AddressingVersion var1, SOAPVersion var2) {
      WsaPropertyBag var3 = (WsaPropertyBag)this._packet.getSatellite(WsaPropertyBag.class);
      if (var3 == null) {
         var3 = new WsaPropertyBag(var1, var2, this._packet);
         this._packet.addSatellite(var3);
      }

   }

   @Property({"weblogic.wsee.reliability2.InternalSend"})
   public boolean getInternalSend() {
      return this._internalSend;
   }

   public void setInternalSend(boolean var1) {
      this._internalSend = var1;
   }

   @Property({"weblogic.wsee.reliability2.InternalReceive"})
   public boolean getInternalReceive() {
      return this._internalReceive;
   }

   public void setInternalReceive(boolean var1) {
      this._internalReceive = var1;
   }

   @Property({"weblogic.wsee.reliability2.ReplyToFromRequest"})
   @Nullable
   public SerializableWSEndpointReference getReplyToFromRequest() {
      return this._replyToFromRequest;
   }

   public void setReplyToFromRequest(@Nullable SerializableWSEndpointReference var1) {
      this._replyToFromRequest = var1;
   }

   @Property({"weblogic.wsee.reliability2.FaultToFromRequest"})
   @Nullable
   public SerializableWSEndpointReference getFaultToFromRequest() {
      return this._faultToFromRequest;
   }

   public void setFaultToFromRequest(@Nullable SerializableWSEndpointReference var1) {
      this._faultToFromRequest = var1;
   }

   @Property({"weblogic.wsee.reliability2.MessageIdFromRequest"})
   @Nullable
   public String getInboundMessageId() {
      return this._inboundMsgId;
   }

   public void setInboundMessageId(@Nullable String var1) {
      this._inboundMsgId = var1;
   }

   @Property({"weblogic.wsee.reliability2.DestMsgInfoFromRequest"})
   public DestinationMessageInfo getDestMessageInfoFromRequest() {
      return this._destMsgInfoFromRequest;
   }

   public void setDestMessageInfoFromRequest(DestinationMessageInfo var1) {
      this._destMsgInfoFromRequest = var1;
   }

   @Property({"weblogic.wsee.reliability2.TransportBackChannel"})
   public TransportBackChannel getTransportBackChannel() {
      return this._backChannel;
   }

   public void setTransportBackChannel(TransportBackChannel var1) {
      this._backChannel = var1;
   }

   @Property({"weblogic.wsee.buffer.BufferedMessageJmsDeliveryCount"})
   public int getBufferedMessageJmsDeliveryCount() {
      return this._bufferedMessageJmsDeliveryCount;
   }

   public void setBufferedMessageJmsDeliveryCount(int var1) {
      this._bufferedMessageJmsDeliveryCount = var1;
   }

   @Property({"weblogic.wsee.reliability2.OutgoingSequenceID"})
   public String getOutboundSequenceId() {
      return this._outboundSequenceId;
   }

   public void setOutboundSequenceId(String var1) {
      this._outboundSequenceId = var1;
   }

   @Property({"weblogic.wsee.reliability2.IncomingSequenceID"})
   public String getInboundSequenceId() {
      return this._inboundSequenceId;
   }

   public void setInboundSequenceId(String var1) {
      this._inboundSequenceId = var1;
   }

   @Property({"weblogic.wsee.reliability2.InboundOperationName"})
   public String getInboundWsdlOperationName() {
      return this._inboundOpName;
   }

   public void setInboundWsdlOperationName(String var1) {
      this._inboundOpName = var1;
   }

   @Property({"weblogic.wsee.reliability2.InboundEndpointAddress"})
   public String getInboundEndpointAddress() {
      return this._incomingEndpointAddress;
   }

   public void setInboundEndpointAddress(String var1) {
      this._incomingEndpointAddress = var1;
   }

   @Property({"weblogic.wsee.reliability2.ResponseToSuspendedFiber"})
   public boolean getResponseToSuspendedFiber() {
      return this._responseToSuspendedFiber;
   }

   public void setResponseToSuspendedFiber(boolean var1) {
      this._responseToSuspendedFiber = var1;
   }

   @Property({"weblogic.wsee.reliability2.OutboundOperationName"})
   public String getOutboundWsdlOperationName() {
      return this._outboundOpName;
   }

   public void setOutboundWsdlOperationName(String var1) {
      this._outboundOpName = var1;
   }

   @Property({"weblogic.wsee.reliability2.OutboundMsgNeedsDestSeqId"})
   public boolean getOutboundMsgNeedsDestSeqId() {
      return this._outboundMsgNeedsDestSeqId;
   }

   public void setOutboundMsgNeedsDestSeqId(boolean var1) {
      this._outboundMsgNeedsDestSeqId = var1;
   }

   static {
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.InternalSend");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.InternalReceive");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.ReplyToFromRequest");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.FaultToFromRequest");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.MessageIdFromRequest");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.DestMsgInfoFromRequest");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.IncomingSequenceID");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.OutgoingSequenceID");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.InboundOperationName");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.OutboundOperationName");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.OutboundMsgNeedsDestSeqId");
      PERSISTENT_PROP_NAMES.add("weblogic.wsee.reliability2.InboundEndpointAddress");
   }
}
