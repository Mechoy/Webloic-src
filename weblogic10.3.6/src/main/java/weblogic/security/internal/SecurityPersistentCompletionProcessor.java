package weblogic.security.internal;

import javax.management.InvalidAttributeValueException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.FileRealmMBean;
import weblogic.management.configuration.PasswordPolicyMBean;
import weblogic.management.configuration.RealmMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;
import weblogic.security.Salt;
import weblogic.utils.Hex;

public class SecurityPersistentCompletionProcessor implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      this.completeDomainCredential(var1);
      this.complete6xRealms(var1);
   }

   private void completeDomainCredential(DomainMBean var1) throws UpdateException {
      SecurityConfigurationMBean var2 = var1.getSecurityConfiguration();
      if (var2.getCredential() == null) {
         byte var3 = 32;
         String var4 = Hex.asHex(Salt.getRandomBytes(var3));

         try {
            var2.setCredential(var4);
         } catch (InvalidAttributeValueException var6) {
            throw new UpdateException(var6);
         }
      }

   }

   private void complete6xRealms(DomainMBean var1) throws UpdateException {
      SecurityMBean var2 = var1.getSecurity();
      if (var2.getRealm() == null) {
         String var3 = "wl_default_realm";
         RealmMBean var4 = var1.lookupRealm(var3);
         if (var4 == null) {
            var4 = var1.createRealm(var3);
         }

         try {
            var2.setRealm(var4);
         } catch (InvalidAttributeValueException var9) {
            throw new UpdateException(var9);
         }
      }

      RealmMBean var10 = var2.getRealm();
      String var11;
      if (var10.getFileRealm() == null) {
         var11 = "wl_default_file_realm";
         FileRealmMBean var5 = var1.lookupFileRealm(var11);
         if (var5 == null) {
            var5 = var1.createFileRealm(var11);
         }

         try {
            var10.setFileRealm(var5);
         } catch (InvalidAttributeValueException var8) {
            throw new UpdateException(var8);
         }
      }

      if (var2.getPasswordPolicy() == null) {
         var11 = "wl_default_password_policy";
         PasswordPolicyMBean var12 = var1.lookupPasswordPolicy(var11);
         if (var12 == null) {
            var12 = var1.createPasswordPolicy(var11);
         }

         try {
            var2.setPasswordPolicy(var12);
         } catch (InvalidAttributeValueException var7) {
            throw new UpdateException(var7);
         }
      }

   }
}
