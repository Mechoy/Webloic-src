package weblogic.iiop;

public class Profile {
   static final int TAG_INTERNET_IOP = 0;
   static final int TAG_MULTIPLE_COMPONENTS = 1;
   private final int tag;
   private byte[] buf;

   public Profile(int var1) {
      this.tag = var1;
   }

   public void read(IIOPInputStream var1) {
      this.buf = var1.read_octet_sequence();
   }

   public void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      var1.write_long(this.buf.length);
      var1.write_octet_array(this.buf, 0, this.buf.length);
   }

   public String toString() {
      return "Unknown profile tag=" + this.tag;
   }
}
