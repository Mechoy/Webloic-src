package weblogic.wsee.security.policy;

import java.math.BigInteger;
import weblogic.wsee.security.policy.assertions.MessageAgeAssertion;

public class TimestampPolicy {
   private short messageAgeSeconds;

   public TimestampPolicy(MessageAgeAssertion var1) {
      BigInteger var2 = var1.getXbean().getMessageAge().getAge();
      if (var2 != null) {
         this.messageAgeSeconds = (short)var2.intValue();
      }

   }

   public short getMessageAgeSeconds() {
      return this.messageAgeSeconds;
   }
}
