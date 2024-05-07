package weblogic.corba.idl.poa;

import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.IdUniquenessPolicyValue;

public class IdUniquenessPolicyImpl extends PolicyImpl implements IdUniquenessPolicy {
   public IdUniquenessPolicyImpl(int var1) {
      super(18, var1);
   }

   public IdUniquenessPolicyValue value() {
      return IdUniquenessPolicyValue.from_int(this.value);
   }
}
