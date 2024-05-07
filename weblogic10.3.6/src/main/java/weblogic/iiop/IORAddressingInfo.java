package weblogic.iiop;

public final class IORAddressingInfo {
   final int selected_profile_index;
   final IOR ior;

   public IORAddressingInfo(IOR var1, int var2) {
      this.selected_profile_index = var2;
      this.ior = var1;
   }

   public IORAddressingInfo(IIOPInputStream var1) {
      this.selected_profile_index = var1.read_long();
      this.ior = new IOR(var1);
   }

   public void write(IIOPOutputStream var1) {
      var1.write_long(this.selected_profile_index);
      this.ior.write(var1);
   }
}
