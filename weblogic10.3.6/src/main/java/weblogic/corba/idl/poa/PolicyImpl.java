package weblogic.corba.idl.poa;

import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyHelper;
import org.omg.CORBA_2_3.portable.ObjectImpl;
import weblogic.iiop.IIOPInputStream;
import weblogic.iiop.IIOPOutputStream;

public class PolicyImpl extends ObjectImpl implements Policy {
   protected int type;
   protected int value;
   private byte[] policy_data;

   public PolicyImpl(int var1, int var2) {
      this.type = var1;
      this.value = var2;
   }

   public PolicyImpl(PolicyImpl var1) {
      this.type = var1.type;
      this.value = var1.value;
   }

   public PolicyImpl(int var1, IIOPInputStream var2) {
      this.type = var1;
      this.policy_data = var2.read_octet_sequence();
   }

   public String[] _ids() {
      return new String[]{PolicyHelper.id()};
   }

   public int policy_type() {
      return this.type;
   }

   public int policy_value() {
      return this.value;
   }

   public void destroy() {
   }

   public Policy copy() {
      return new PolicyImpl(this);
   }

   static PolicyImpl readPolicy(IIOPInputStream var0) {
      int var1 = var0.read_ulong();
      switch (var1) {
         case 28:
            return new RequestEndTimePolicyImpl(var0);
         case 30:
            return new ReplyEndTimePolicyImpl(var0);
         default:
            return new PolicyImpl(var1, var0);
      }
   }

   protected void readEncapsulatedPolicy(IIOPInputStream var1) {
      throw new AssertionError();
   }

   protected void writeEncapsulatedPolicy(IIOPOutputStream var1) {
      throw new AssertionError();
   }

   protected final void read(IIOPInputStream var1) {
      long var2 = var1.startEncapsulation();
      this.readEncapsulatedPolicy(var1);
      var1.endEncapsulation(var2);
   }

   public final void write(IIOPOutputStream var1) {
      var1.write_ulong(this.type);
      if (this.policy_data != null) {
         var1.write_octet_sequence(this.policy_data);
      } else {
         long var2 = var1.startEncapsulation();
         this.writeEncapsulatedPolicy(var1);
         var1.endEncapsulation(var2);
      }

   }
}
