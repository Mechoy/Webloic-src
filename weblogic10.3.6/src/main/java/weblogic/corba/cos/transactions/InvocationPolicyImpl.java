package weblogic.corba.cos.transactions;

import org.omg.CosTransactions.InvocationPolicy;
import weblogic.corba.idl.poa.PolicyImpl;

public class InvocationPolicyImpl extends PolicyImpl implements InvocationPolicy {
   public InvocationPolicyImpl(int var1) {
      super(55, var1);
   }

   public short ipv() {
      return (short)this.policy_value();
   }
}
