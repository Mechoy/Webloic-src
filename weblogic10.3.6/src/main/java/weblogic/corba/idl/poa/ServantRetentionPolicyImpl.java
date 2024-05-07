package weblogic.corba.idl.poa;

import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ServantRetentionPolicyValue;

public class ServantRetentionPolicyImpl extends PolicyImpl implements ServantRetentionPolicy {
   public ServantRetentionPolicyImpl(int var1) {
      super(21, var1);
   }

   public ServantRetentionPolicyValue value() {
      return ServantRetentionPolicyValue.from_int(this.value);
   }
}
