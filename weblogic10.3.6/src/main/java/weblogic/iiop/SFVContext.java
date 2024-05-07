package weblogic.iiop;

public final class SFVContext extends ServiceContext {
   private byte maxFormatVersion;

   public byte getMaxFormatVersion() {
      return this.maxFormatVersion;
   }

   public SFVContext(byte var1) {
      super(17);
      this.maxFormatVersion = var1;
   }

   public SFVContext(IIOPInputStream var1) {
      super(17);
      this.readEncapsulatedContext(var1);
   }

   protected final void readEncapsulation(IIOPInputStream var1) {
      this.maxFormatVersion = var1.read_octet();
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_octet(this.maxFormatVersion);
   }

   public String toString() {
      return "SFVContext: " + this.maxFormatVersion;
   }
}
