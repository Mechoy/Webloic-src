package weblogic.wsee.reliability2.api_internal;

public class WsrmLifecycleEvent {
   private Type _type;

   public WsrmLifecycleEvent(Type var1) {
      this._type = var1;
   }

   public Type getType() {
      return this._type;
   }

   public static enum Type {
      RECV_CREATE_SEQ(Side.SERVICE, Direction.NONE, "CreateSequence received and processed, before sending CreateSequenceResponse"),
      SERV_IN_MSG_BEFORE_ACCEPT(Side.SERVICE, Direction.INBOUND, "A sequence message was received, but has not yet been accepted/ack'd"),
      SERV_IN_MSG_AFTER_ACCEPT(Side.SERVICE, Direction.INBOUND, "A sequence message was received, and has now been accepted. A pending ack is registered, and will soon be delivered either by piggybacking or by sending a standalone ack message"),
      SERV_IN_MSG_AFTER_BUFFERING(Side.SERVICE, Direction.INBOUND, "A sequence message was received, and has now been buffered. The request will soon be processed off this queue and into the web service method."),
      SERV_IN_MSG_AFTER_ACK(Side.SERVICE, Direction.INBOUND, "A sequence message was received, and has now been accepted/ack'd. The ack may have been delivered via piggyback or standalone ack"),
      SERV_IN_MSG_UPON_DEBUFFER(Side.SERVICE, Direction.INBOUND, "A sequence message was received, accepted and enqueued, and has been dequeued for further processing"),
      SERV_IN_MSG_BEFORE_RESPONSE_SEND(Side.SERVICE, Direction.INBOUND, "A sequence message was received, accepted and processed, and now the response is ready to be sent (either directly or via sender/retry logic on the service-side)."),
      SERV_OUT_FOUND_ACK(Side.SERVICE, Direction.OUTBOUND, "An RM ack was detected, but has not been handled yet."),
      SERV_OUT_BEFORE_SEND_RESPONSE(Side.SERVICE, Direction.OUTBOUND, "An RM response has been buffered and is about to be delivered back to the client async response endpoint."),
      CLIENT_CREATE_SEQ(Side.CLIENT, Direction.NONE, "A CreateSequence message is about to be sent."),
      CLIENT_CREATE_SEQ_RES(Side.CLIENT, Direction.NONE, "A CreateSequenceResponse message has been received, but not fully processed."),
      CLIENT_SEQUENCE_CREATED(Side.CLIENT, Direction.NONE, "A CreateSequenceResponse message has been received, and sequence is ready for use."),
      CLIENT_SECURITY_PROPS_BEFORE_SAVE(Side.CLIENT, Direction.NONE, "An RM sequence or protocol message has been sent, and any updated security properties (including new/renewed SCT) are about to be saved into sequence state."),
      CLIENT_OUT_FOUND_ACK(Side.CLIENT, Direction.OUTBOUND, "An RM ack was detected, but has not been handled yet."),
      CLIENT_OUT_BEFORE_SEND_REQUEST(Side.CLIENT, Direction.OUTBOUND, "An RM request has been buffered and is about to be delivered to the service endpoint."),
      AFTER_RSTR_BEFORE_CREATE_SEQ(Side.CLIENT, Direction.OUTBOUND, "After WSSC RST/RSTR handshake, before create sequence is sent to the Receiver."),
      CLIENT_IN_RESPONSE(Side.CLIENT, Direction.INBOUND, "Some response message has been received (RM protocol or sequence message is unknown yet), but has not been fully processed yet."),
      CLIENT_IN_MSG_BEFORE_ACCEPT(Side.CLIENT, Direction.INBOUND, "A sequence message was received, but has not yet been accepted/ack'd"),
      CLIENT_IN_MSG_AFTER_ACCEPT(Side.CLIENT, Direction.INBOUND, "A request sequence message was received, and has now been accepted. A pending ack is registered, and will soon be delivered either by piggybacking or by sending a standalone ack message"),
      CLIENT_IN_MSG_AFTER_BUFFERING(Side.CLIENT, Direction.INBOUND, "A sequence message was received, and has now been buffered. This message will soon be processed off the queue and into the web service method impl."),
      CLIENT_IN_MSG_AFTER_ACK(Side.CLIENT, Direction.INBOUND, "A sequence message was received, and has now been accepted/ack'd. The ack may have been delivered via piggyback or standalone ack"),
      CLIENT_IN_MSG_UPON_DEBUFFER(Side.CLIENT, Direction.INBOUND, "A sequence message was received, accepted and enqueued, and has been dequeued for further processing");

      private Side _side;
      private Direction _direction;
      private String _description;

      private Type(Side var3, Direction var4, String var5) {
         this._side = var3;
         this._direction = var4;
         this._description = var5;
      }

      public Side getSide() {
         return this._side;
      }

      public Direction getDirection() {
         return this._direction;
      }

      public String getDescription() {
         return this._description;
      }

      public String toString() {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.name());
         var1.append(" (");
         var1.append(this._side).append("/").append(this._direction);
         var1.append("): ");
         var1.append(this._description);
         return var1.toString();
      }
   }
}
