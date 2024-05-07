package weblogic.corba.idl.poa;

import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.ImplicitActivationPolicyValue;

public class ImplicitActivationPolicyImpl extends PolicyImpl implements ImplicitActivationPolicy {
   public ImplicitActivationPolicyImpl(int var1) {
      super(20, var1);
   }

   public ImplicitActivationPolicyValue value() {
      return ImplicitActivationPolicyValue.from_int(this.value);
   }
}
