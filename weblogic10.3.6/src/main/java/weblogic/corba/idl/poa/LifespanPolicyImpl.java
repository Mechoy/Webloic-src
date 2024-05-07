package weblogic.corba.idl.poa;

import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.LifespanPolicyValue;

public class LifespanPolicyImpl extends PolicyImpl implements LifespanPolicy {
   public LifespanPolicyImpl(int var1) {
      super(17, var1);
   }

   public LifespanPolicyValue value() {
      return LifespanPolicyValue.from_int(this.value);
   }
}
