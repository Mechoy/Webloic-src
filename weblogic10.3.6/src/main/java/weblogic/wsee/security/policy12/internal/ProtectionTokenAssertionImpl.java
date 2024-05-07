package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.KerberosToken;
import weblogic.wsee.security.policy12.assertions.ProtectionToken;
import weblogic.wsee.security.policy12.assertions.SamlToken;
import weblogic.wsee.security.policy12.assertions.SecureConversationToken;
import weblogic.wsee.security.policy12.assertions.X509Token;
import weblogic.wsee.security.wssp.KerberosTokenAssertion;
import weblogic.wsee.security.wssp.ProtectionTokenAssertion;
import weblogic.wsee.security.wssp.SamlTokenAssertion;
import weblogic.wsee.security.wssp.SecureConversationTokenAssertion;
import weblogic.wsee.security.wssp.X509TokenAssertion;

public class ProtectionTokenAssertionImpl implements ProtectionTokenAssertion {
   SecureConversationTokenAssertionImpl secTokenAsstImpl;
   X509TokenAssertion x509TokenAsstImpl;
   KerberosTokenAssertion kerberosTokenAsstImpl;
   SamlTokenAssertionImpl samlTokenAsstImpl;

   ProtectionTokenAssertionImpl(ProtectionToken var1) {
      SecureConversationToken var2 = var1.getSecureConversationToken();
      if (var2 != null) {
         this.secTokenAsstImpl = new SecureConversationTokenAssertionImpl(var2);
      }

      X509Token var3 = var1.getX509Token();
      if (var3 != null) {
         this.x509TokenAsstImpl = new X509TokenAssertionImpl(var3);
      }

      KerberosToken var4 = var1.getKerberosToken();
      if (var4 != null) {
         this.kerberosTokenAsstImpl = new KerberosTokenAssertionImpl(var4);
      }

      SamlToken var5 = var1.getSamlToken();
      if (var5 != null) {
         this.samlTokenAsstImpl = new SamlTokenAssertionImpl(var5);
         this.samlTokenAsstImpl.setConfirmationMethodHolderOfKey();
      }

   }

   public SecureConversationTokenAssertion getSecureConversationTokenAssertion() {
      return this.secTokenAsstImpl;
   }

   public X509TokenAssertion getX509TokenAssertion() {
      return this.x509TokenAsstImpl;
   }

   public KerberosTokenAssertion getKerberosTokenAssertion() {
      return this.kerberosTokenAsstImpl;
   }

   public SamlTokenAssertion getSamlTokenAssertion() {
      return this.samlTokenAsstImpl;
   }
}
