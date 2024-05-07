package weblogic.corba.idl.poa;

import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.ServiceContext;

public final class MessagingPolicy extends ServiceContext {
   private PolicyImpl[] policies;

   public MessagingPolicy(PolicyImpl[] var1) {
      super(7);
      this.policies = var1;
   }

   public MessagingPolicy(IIOPInputStream var1) {
      super(7);
      this.readEncapsulatedContext(var1);
   }

   protected final void readEncapsulation(IIOPInputStream var1) {
      int var2 = var1.read_ulong();
      this.policies = new PolicyImpl[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.policies[var3] = PolicyImpl.readPolicy(var1);
      }

   }

   public final void write(IIOPOutputStream var1) {
      for(int var2 = 0; var2 < this.policies.length; ++var2) {
         this.policies[var2].write(var1);
      }

   }

   public String toString() {
      return "MessagingPolicy";
   }
}
