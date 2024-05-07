package weblogic.iiop;

import weblogic.security.acl.internal.AuthenticatedUser;

public final class VendorInfoSecurity extends ServiceContext {
   protected AuthenticatedUser user;
   public static final VendorInfoSecurity ANONYMOUS = new VendorInfoSecurity();

   public VendorInfoSecurity() {
      super(1111834882);
   }

   public VendorInfoSecurity(AuthenticatedUser var1) {
      super(1111834882);
      this.user = var1;
   }

   public AuthenticatedUser getUser() {
      return this.user;
   }

   protected VendorInfoSecurity(IIOPInputStream var1) {
      super(1111834882);
      this.readEncapsulatedContext(var1);
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.user = (AuthenticatedUser)var1.read_value();
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      var1.write_value(this.user);
   }

   public String toString() {
      return "VendorInfoSecurity Context: " + this.user;
   }
}
