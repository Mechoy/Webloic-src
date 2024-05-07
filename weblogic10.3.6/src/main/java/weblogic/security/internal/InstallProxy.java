package weblogic.security.internal;

import java.util.Properties;
import weblogic.security.acl.internal.FileRealm;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.utils.AdminAccount;
import weblogic.security.utils.KeyStoreInstall;

public final class InstallProxy {
   public byte[] getSalt(String var1) throws Throwable {
      return SerializedSystemIni.getSalt(var1);
   }

   public void convertFromClearTextPasswords(String var1, byte[] var2, Properties var3, Properties var4, Properties var5) throws Throwable {
      FileRealm.convertFromClearTextPasswords(var1, var2, var3, var4, var5);
   }

   public void generateLDIF(String var1, String var2, String var3, String var4) throws Throwable {
      AdminAccount.setupAdminAccount(var1, var2, var3, var4);
   }

   public void generateKeyStore() throws Exception {
      KeyStoreInstall.initDefaultKeyStore();
   }

   public String encrypt(String var1, String var2) throws Throwable {
      EncryptionService var3 = SerializedSystemIni.getEncryptionService(var2);
      ClearOrEncryptedService var4 = new ClearOrEncryptedService(var3);
      String var5 = var4.encrypt(var1);
      return (String)var5;
   }
}
