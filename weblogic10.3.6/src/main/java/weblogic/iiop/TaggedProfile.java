package weblogic.iiop;

public final class TaggedProfile {
   public int tag;
   public byte[] profile_data;

   public TaggedProfile() {
   }

   public TaggedProfile(IIOPInputStream var1) {
      this.read(var1);
   }

   public TaggedProfile(int var1, byte[] var2) {
      this.tag = var1;
      this.profile_data = var2;
   }

   public void read(IIOPInputStream var1) {
      this.tag = var1.read_ulong();
      this.profile_data = var1.read_octet_sequence();
   }

   public void write(IIOPOutputStream var1) {
      var1.write_ulong(this.tag);
      var1.write_octet_sequence(this.profile_data);
   }
}
