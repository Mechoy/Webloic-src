package weblogic.corba.idl.poa;

import org.omg.Messaging.RelativeRequestTimeoutPolicy;

public class RelativeRequestTimeoutPolicyImpl extends PolicyImpl implements RelativeRequestTimeoutPolicy {
   private long expiry;

   public RelativeRequestTimeoutPolicyImpl(long var1) {
      super(31, 0);
      this.expiry = var1;
   }

   public long relative_expiry() {
      return this.expiry;
   }

   public long relativeExpiryMillis() {
      return this.expiry / 10000L;
   }
}
