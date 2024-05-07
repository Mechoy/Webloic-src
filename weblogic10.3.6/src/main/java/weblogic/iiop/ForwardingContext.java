package weblogic.iiop;

public final class ForwardingContext extends ServiceContext {
   private ConnectionKey connectionKey;

   public ForwardingContext(ConnectionKey var1) {
      super(1111834889);
      this.connectionKey = var1;
   }

   protected ForwardingContext(IIOPInputStream var1) {
      super(1111834889);
      this.readEncapsulatedContext(var1);
   }

   public ConnectionKey getConnectionKey() {
      return this.connectionKey;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.connectionKey = new ConnectionKey(var1);
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      this.connectionKey.writeForChannel(var1);
   }

   public String toString() {
      return "ForwardingContext: connectionKey = " + this.connectionKey;
   }
}
