package weblogic.corba.cos.transactions;

import org.omg.CosTransactions.OTSPolicy;
import weblogic.corba.idl.poa.PolicyImpl;

public class OTSPolicyImpl extends PolicyImpl implements OTSPolicy {
   public OTSPolicyImpl(int var1) {
      super(56, var1);
   }

   public short tpv() {
      return (short)this.policy_value();
   }
}
