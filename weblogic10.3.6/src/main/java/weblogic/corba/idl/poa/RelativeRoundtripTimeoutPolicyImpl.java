package weblogic.corba.idl.poa;

import org.omg.Messaging.RelativeRoundtripTimeoutPolicy;

public class RelativeRoundtripTimeoutPolicyImpl extends PolicyImpl implements RelativeRoundtripTimeoutPolicy {
   private long expiry;

   public RelativeRoundtripTimeoutPolicyImpl(long var1) {
      super(32, 0);
      this.expiry = var1;
   }

   public long relative_expiry() {
      return this.expiry;
   }

   public long relativeExpiryMillis() {
      return this.expiry / 10000L;
   }
}
