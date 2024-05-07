package weblogic.iiop;

public final class SFVComponent extends TaggedComponent {
   public static final byte SF_VERSION_1 = 1;
   public static final byte SF_VERSION_2 = 2;
   public static final TaggedComponent VERSION_2 = new SFVComponent((byte)2);
   private byte maxFormatVersion;

   public SFVComponent(byte var1) {
      super(38);
      this.maxFormatVersion = var1;
   }

   public byte getMaxFormatVersion() {
      return this.maxFormatVersion;
   }

   public SFVComponent(IIOPInputStream var1) {
      super(38);
      this.read(var1);
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.maxFormatVersion = var1.read_octet();
      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();
      var1.write_octet(this.maxFormatVersion);
      var1.endEncapsulation(var2);
   }

   public String toString() {
      return "SFVComponent: " + this.maxFormatVersion;
   }
}
