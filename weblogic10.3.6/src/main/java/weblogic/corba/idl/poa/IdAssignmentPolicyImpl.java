package weblogic.corba.idl.poa;

import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdAssignmentPolicyValue;

public class IdAssignmentPolicyImpl extends PolicyImpl implements IdAssignmentPolicy {
   public IdAssignmentPolicyImpl(int var1) {
      super(19, var1);
   }

   public IdAssignmentPolicyValue value() {
      return IdAssignmentPolicyValue.from_int(this.value);
   }
}
