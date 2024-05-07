package weblogic.wsee.security.wss.policy.wssp;

import java.math.BigInteger;
import weblogic.wsee.security.policy.assertions.MessageAgeAssertion;
import weblogic.wsee.security.wss.policy.TimestampPolicy;

public class TimestampPolicyImpl implements TimestampPolicy {
   private short messageAgeSeconds = -1;
   private boolean includeTimestamp = false;
   private boolean signTimestampRequired = false;

   public TimestampPolicyImpl() {
   }

   public TimestampPolicyImpl(MessageAgeAssertion var1) {
      BigInteger var2 = var1.getXbean().getMessageAge().getAge();
      if (var2 != null) {
         this.messageAgeSeconds = (short)var2.intValue();
      }

   }

   public short getMessageAgeSeconds() {
      return this.isIncludeTimestamp() && this.messageAgeSeconds == -1 ? 0 : this.messageAgeSeconds;
   }

   public boolean isIncludeTimestamp() {
      return this.includeTimestamp;
   }

   public void setIncludeTimestamp(boolean var1) {
      this.includeTimestamp = var1;
   }

   public void setMessageAgeSeconds(short var1) {
      this.messageAgeSeconds = var1;
   }

   public boolean isSignTimestampRequired() {
      return this.signTimestampRequired;
   }

   public void setSignTimestampRequired(boolean var1) {
      this.signTimestampRequired = var1;
   }

   public void setSignTimestampRequired() {
      this.signTimestampRequired = true;
   }
}
