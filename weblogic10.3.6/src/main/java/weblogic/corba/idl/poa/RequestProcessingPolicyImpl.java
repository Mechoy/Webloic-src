package weblogic.corba.idl.poa;

import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;

public class RequestProcessingPolicyImpl extends PolicyImpl implements RequestProcessingPolicy {
   public RequestProcessingPolicyImpl(int var1) {
      super(22, var1);
   }

   public RequestProcessingPolicyValue value() {
      return RequestProcessingPolicyValue.from_int(this.value);
   }
}
