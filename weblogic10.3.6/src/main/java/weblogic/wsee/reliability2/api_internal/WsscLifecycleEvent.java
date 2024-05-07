package weblogic.wsee.reliability2.api_internal;

public class WsscLifecycleEvent {
   private Type _type;

   public WsscLifecycleEvent(Type var1) {
      this._type = var1;
   }

   public Type getType() {
      return this._type;
   }

   public static enum Type {
      RECV_RST_BEFORE_SAVE(Side.SERVICE, "Received an RST and getting ready to save it to SCTStore"),
      RECV_RST_AFTER_SAVE(Side.SERVICE, "Received an RST and just finished saving it to SCTStore"),
      RECV_RST_BEFORE_RENEW(Side.SERVICE, "Received an SCT renew request, and we're about to renew/store the SCT in the SCTStore."),
      RECV_RST_AFTER_RENEW(Side.SERVICE, "Received an SCT renew request, and we've saved the renewed SCT in the SCTStore."),
      SEND_RST_BEFORE_RSTR(Side.CLIENT, "Sending an SCT create request, before we've saved SCT in the client-side SCTStore."),
      WSRM_SEND_AFTER_RSTR(Side.CLIENT, "Received an SCT create response, and we've saved the SCT in the client-side SCTStore.");

      private Side _side;
      private String _description;

      private Type(Side var3, String var4) {
         this._side = var3;
         this._description = var4;
      }

      public Side getDirection() {
         return this._side;
      }

      public String getDescription() {
         return this._description;
      }
   }
}
