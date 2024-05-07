package weblogic.corba.idl.poa;

import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.PortableServer.ThreadPolicy;
import org.omg.PortableServer.ThreadPolicyValue;

public class ThreadPolicyImpl extends PolicyImpl implements ThreadPolicy {
   public ThreadPolicyImpl(int var1) {
      super(16, var1);
      if (var1 != 0) {
         throw new NO_IMPLEMENT();
      }
   }

   public ThreadPolicyValue value() {
      return ThreadPolicyValue.from_int(this.value);
   }
}
