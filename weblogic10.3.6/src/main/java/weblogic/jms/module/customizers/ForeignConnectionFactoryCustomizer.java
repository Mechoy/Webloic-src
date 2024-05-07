package weblogic.jms.module.customizers;

import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.utils.NestedRuntimeException;

public class ForeignConnectionFactoryCustomizer {
   private ForeignConnectionFactoryBean customized;

   public ForeignConnectionFactoryCustomizer(ForeignConnectionFactoryBean var1) {
      this.customized = var1;
   }

   public String getPassword() {
      ClearOrEncryptedService var2;
      try {
         EncryptionService var1 = SerializedSystemIni.getEncryptionService();
         var2 = new ClearOrEncryptedService(var1);
      } catch (NestedRuntimeException var7) {
         return null;
      } catch (NullPointerException var8) {
         System.out.println("ERROR: Got an NPE(1).  Please fix CR186445");
         var8.printStackTrace();
         return null;
      }

      try {
         String var3 = var2.decrypt(new String(this.customized.getPasswordEncrypted()));
         return var3;
      } catch (EncryptionServiceException var5) {
         return null;
      } catch (NullPointerException var6) {
         System.out.println("ERROR: Got an NPE(2).  Please fix CR186445");
         var6.printStackTrace();
         return null;
      }
   }

   public void setPassword(String var1) throws IllegalArgumentException {
      if (var1 != null) {
         ClearOrEncryptedService var3;
         try {
            EncryptionService var2 = SerializedSystemIni.getEncryptionService();
            var3 = new ClearOrEncryptedService(var2);
         } catch (NestedRuntimeException var8) {
            throw new IllegalArgumentException("Could not get encryption service, likely the domain directory could not be found, " + var8);
         } catch (NullPointerException var9) {
            System.out.println("ERROR: Got an NPE(3).  Please fix CR186445");
            var9.printStackTrace();
            return;
         }

         byte[] var4;
         try {
            var4 = var3.encrypt(var1).getBytes();
         } catch (EncryptionServiceException var6) {
            throw new IllegalArgumentException("Could not encrypt the password, " + var6);
         } catch (NullPointerException var7) {
            System.out.println("ERROR: Got an NPE(4).  Please fix CR186445");
            var7.printStackTrace();
            return;
         }

         this.customized.setPasswordEncrypted(var4);
      }
   }
}
