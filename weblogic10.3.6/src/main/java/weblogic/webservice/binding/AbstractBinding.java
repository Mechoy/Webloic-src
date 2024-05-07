package weblogic.webservice.binding;

/** @deprecated */
public abstract class AbstractBinding implements Binding {
   private String replyTo;
   private String sender;
   private String destination;
   private BindingInfo bindingInfo;
   private int type;
   public static final int CLIENT_SIDE = 0;
   public static final int SERVER_SIDE = 1;

   public void setServerSide() {
      this.type = 1;
   }

   public void setClientSide() {
      this.type = 0;
   }

   public boolean isServerSide() {
      return this.type == 1;
   }

   public boolean isClientSide() {
      return this.type == 0;
   }

   public String getReplyTo() {
      return this.replyTo;
   }

   public void setReplyTo(String var1) {
      this.replyTo = var1;
   }

   public String getSender() {
      return this.sender;
   }

   public void setSender(String var1) {
      this.sender = var1;
   }

   public String getDestination() {
      return this.destination;
   }

   public void setDestination(String var1) {
      this.destination = var1;
   }

   protected void setBindingInfo(BindingInfo var1) {
      this.bindingInfo = var1;
   }

   public BindingInfo getBindingInfo() {
      return this.bindingInfo;
   }
}
