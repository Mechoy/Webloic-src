package weblogic.ldap;

import javax.management.InvalidAttributeValueException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.EmbeddedLDAPMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;
import weblogic.security.Salt;
import weblogic.utils.Hex;

public class EmbeddedLDAPPersistentCompletionProcessor implements ConfigurationProcessor {
   private static final int CRED_LEN = 13;
   private static String credential = null;

   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      EmbeddedLDAPMBean var2 = var1.getEmbeddedLDAP();
      if (var2 == null) {
         throw new AssertionError("null embedded ldap mbean");
      } else if (var2.getCredential() == null) {
         if (credential == null) {
            byte[] var3 = Salt.getRandomBytes(13);
            credential = Hex.asHex(var3);
         }

         try {
            var2.setCredential(credential);
         } catch (InvalidAttributeValueException var4) {
            throw new UpdateException(var4);
         }
      }
   }
}
