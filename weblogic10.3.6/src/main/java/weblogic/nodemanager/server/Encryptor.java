package weblogic.nodemanager.server;

import java.io.File;
import java.io.IOException;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.ConfigException;
import weblogic.security.Salt;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.security.internal.encryption.EncryptionServiceFactory;
import weblogic.security.internal.encryption.JSafeEncryptionServiceFactory;
import weblogic.utils.Hex;

class Encryptor {
   private byte[] salt;
   private byte[] key;
   private ClearOrEncryptedService ces;
   static final String ESP = "0x1f48730ab4957122fccb2856671df094bcc294af";
   static final String OESP = "0x194ce8ab97302f33a77c82de564091f1ac4873be";
   NMServerConfig conf;
   private boolean useOESP = false;
   static final String NAME_HASH_KEY_PROP = "nameHashKey";
   static final String ID_HASH_KEY_PROP = "idHashKey";
   static final String OLD_NAME_HASH_KEY_PROP = "nameHashkey";
   static final String OLD_ID_HASH_KEY_PROP = "idHashkey";
   public static final String NM_DATA_PROPERTIES = "nm_data.properties";
   public static final String NM_DATA_PROPERTIES_1 = "SerializedNodeManagerIni.dat";
   public static final String NM_DATA_PROPERTIES_2 = "NodeManagerProperties";
   public static final String KEY_PASSWORD_PROP = "keyPassword";
   public static final String DEFAULT_KEY_PASSWORD = "password";

   private File getEDataFile(String var1) {
      File var2 = new File(var1, "nm_data.properties");
      if (!var2.exists()) {
         var2 = new File(var1, "SerializedNodeManagerIni.dat");
         if (!var2.exists()) {
            var2 = new File(var1, "NodeManagerProperties");
            if (!var2.exists()) {
               var2 = null;
            } else {
               this.useOESP = true;
            }
         }
      }

      return var2;
   }

   Encryptor(String var1) throws ConfigException, IOException {
      this.ces = null;
      File var2 = this.getEDataFile(var1);
      if (var2 != null) {
         this.loadProperties(var2);
         JSafeEncryptionServiceFactory var3 = new JSafeEncryptionServiceFactory();
         this.ces = new ClearOrEncryptedService(var3.getEncryptionService(this.salt, "0x1f48730ab4957122fccb2856671df094bcc294af", this.key));
      } else {
         throw new IOException(NodeManagerTextTextFormatter.getInstance().getDataFileNotFound(var1));
      }
   }

   Encryptor(NMServerConfig var1) throws ConfigException, IOException {
      JSafeEncryptionServiceFactory var2 = new JSafeEncryptionServiceFactory();
      this.conf = var1;
      String var3 = var1.getNMHome();
      File var4 = this.getEDataFile(var3);
      if (var4 != null) {
         this.loadProperties(var4);
      } else {
         this.createProperties(var2, new File(var3, "nm_data.properties"));
      }

      this.initService(var2);
   }

   private void loadProperties(File var1) throws ConfigException, IOException {
      NMProperties var2 = new NMProperties();
      var2.load(var1);
      String var3 = var2.getProperty("nameHashKey");
      if (var3 == null) {
         var3 = var2.getProperty("nameHashkey");
      }

      String var4 = var2.getProperty("idHashKey");
      if (var4 == null) {
         var4 = var2.getProperty("idHashkey");
      }

      if (var3 != null && var4 != null) {
         byte[] var5 = var3.getBytes();
         byte[] var6 = var4.getBytes();
         this.salt = Hex.fromHexString(var5, var5.length);
         this.key = Hex.fromHexString(var6, var6.length);
      } else {
         throw new ConfigException(NodeManagerTextTextFormatter.getInstance().getInvalidDataFile(var1.toString()));
      }
   }

   private void createProperties(EncryptionServiceFactory var1, File var2) throws IOException {
      NMProperties var3 = new NMProperties();
      this.salt = Salt.getRandomBytes(4);
      this.key = var1.createEncryptedSecretKey(this.salt, "0x1f48730ab4957122fccb2856671df094bcc294af");
      var3.setProperty("nameHashKey", Hex.asHex(this.salt, this.salt.length));
      var3.setProperty("idHashKey", Hex.asHex(this.key, this.key.length));
      var3.save(var2, (String)null);
   }

   private void initService(EncryptionServiceFactory var1) {
      String var2 = null;

      try {
         this.ces = new ClearOrEncryptedService(var1.getEncryptionService(this.salt, this.useOESP ? "0x194ce8ab97302f33a77c82de564091f1ac4873be" : "0x1f48730ab4957122fccb2856671df094bcc294af", this.key));
      } catch (EncryptionServiceException var6) {
         if (!this.useOESP || this.conf == null) {
            throw (InternalError)(new InternalError(NodeManagerTextTextFormatter.getInstance().getEncryptionServiceFailure())).initCause(var6);
         }

         var2 = this.conf.getConfigProperties().getProperty("keyPassword");
         if (var2 == null || var2.equals("")) {
            var2 = "password";
         }

         try {
            this.ces = new ClearOrEncryptedService(var1.getEncryptionService(this.salt, var2, this.key));
         } catch (EncryptionServiceException var5) {
            throw (InternalError)(new InternalError(NodeManagerTextTextFormatter.getInstance().getEncryptionServiceFailure())).initCause(var5);
         }
      }

   }

   String encrypt(String var1) {
      if (var1 != null && !this.ces.isEncrypted(var1)) {
         var1 = this.ces.encrypt(var1);
      }

      return var1;
   }

   String decrypt(String var1) {
      if (var1 != null && this.ces.isEncrypted(var1)) {
         var1 = this.ces.decrypt(var1);
      }

      return var1;
   }

   ClearOrEncryptedService getCES() {
      return this.ces;
   }
}
