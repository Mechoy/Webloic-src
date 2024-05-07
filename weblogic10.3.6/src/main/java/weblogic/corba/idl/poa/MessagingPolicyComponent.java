package weblogic.corba.idl.poa;

import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.TaggedComponent;

public final class MessagingPolicyComponent extends TaggedComponent {
   private PolicyImpl[] policies;

   public MessagingPolicyComponent(PolicyImpl[] var1) {
      super(2);
      this.policies = var1;
   }

   public MessagingPolicyComponent(IIOPInputStream var1) {
      super(2);
      this.read(var1);
   }

   public final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      int var4 = var1.read_ulong();
      this.policies = new PolicyImpl[var4];

      for(int var5 = 0; var5 < var4; ++var5) {
         this.policies[var5] = PolicyImpl.readPolicy(var1);
      }

      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_long(this.tag);
      long var2 = var1.startEncapsulation();

      for(int var4 = 0; var4 < this.policies.length; ++var4) {
         this.policies[var4].write(var1);
      }

      var1.endEncapsulation(var2);
   }
}
