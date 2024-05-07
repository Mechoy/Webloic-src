package weblogic.iiop;

public final class VendorInfoTrace extends ServiceContext {
   private byte[] trace;

   public VendorInfoTrace() {
      super(1111834890);
   }

   public VendorInfoTrace(byte[] var1) {
      super(1111834890);
      this.trace = var1;
   }

   public byte[] getTrace() {
      return this.trace;
   }

   protected VendorInfoTrace(IIOPInputStream var1) {
      super(1111834890);
      this.readEncapsulatedContext(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.trace = var1.read_octet_sequence();
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_octet_sequence(this.trace);
   }

   public String toString() {
      return "VendorInfoTrace buffer: " + this.trace;
   }
}
