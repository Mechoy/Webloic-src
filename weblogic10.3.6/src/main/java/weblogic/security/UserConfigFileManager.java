package weblogic.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import weblogic.security.internal.ServerAuthenticate;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.JSafeEncryptionServiceFactory;

public class UserConfigFileManager implements Map {
   static final String DEFAULT_KEY_FILE_NAME = "-WebLogicKey.properties";
   static final String DEFAULT_CONFIG_FILE_NAME = "-WebLogicConfig.properties";
   static final String DEFAULT_FILE_HOME_PROP = "user.home";
   static final String DEFAULT_FILE_USER_PROP = "user.name";
   static final String DEFAULT_FILE_USER = "user";
   static final String DEFAULT_FILE_HOME = ".";
   static final String DEFAULT_USERNAME_PROP = ".username";
   static final String DEFAULT_PASSWORD_PROP = ".password";
   static final String CONFIG_FILE_HEADER = "WebLogic User Configuration File";
   static final int VERSION = 2;
   private String configFileName = null;
   private FileInputStream configIn = null;
   private Properties configProperties = null;
   private String propName = null;
   private String keyFileName = null;
   private boolean debugUserConfig = false;
   private static ClearOrEncryptedService encrypter = null;
   static final String myvalue = "0xfe593a5c23b88c112b3c674e33ea4c7901e26a7c";

   public UserConfigFileManager() {
      this.configFileName = null;
      this.keyFileName = null;
   }

   public UserConfigFileManager(String var1, String var2) {
      this.configFileName = var1;
      this.keyFileName = var2;
   }

   public void setDebug(boolean var1) {
      this.debugUserConfig = var1;
   }

   public boolean getDebug() {
      return this.debugUserConfig;
   }

   public static UsernameAndPassword getUsernameAndPassword(String var0) {
      UserConfigFileManager var1 = new UserConfigFileManager();
      var1.debug("DBG: UserConfigFileManager.getUsernameAndPassword(-" + var0 + "-)");
      var1.setPropName(var0);

      try {
         var1.initEncryptionRead(false);
      } catch (KeyException var3) {
         return null;
      }

      return var1.retrieveUandPValues();
   }

   public static void setUsernameAndPassword(UsernameAndPassword var0, String var1) {
      UserConfigFileManager var2 = new UserConfigFileManager();
      var2.debug("DBG: UserConfigFileManager.setUsernameAndPassword(UsernameAndPassword, -" + var1 + "-)");
      var2.setPropName(var1);

      try {
         var2.initEncryption(true);
      } catch (KeyException var4) {
         return;
      }

      var0.setEncryption(encrypter);
      var2.putUandPValues(var0);
   }

   public static UsernameAndPassword getUsernameAndPassword(String var0, String var1, String var2) {
      UserConfigFileManager var3 = new UserConfigFileManager(var0, var1);
      var3.debug("DBG: UserConfigFileManager.getUsernameAndPassword(" + var0 + "," + var1 + "," + var2 + ")");

      try {
         var3.initEncryptionRead(false);
      } catch (KeyException var5) {
         return null;
      }

      var3.setPropName(var2);
      return var3.retrieveUandPValues();
   }

   public static void setUsernameAndPassword(UsernameAndPassword var0, String var1, String var2, String var3) {
      UserConfigFileManager var4 = new UserConfigFileManager(var1, var2);
      var4.debug("DBG: UserConfigFileManager.setUsernameAndPassword(UsernameAndPassword," + var1 + "," + var2 + "," + var3 + ")");

      try {
         var4.initEncryption(true);
      } catch (KeyException var6) {
         return;
      }

      var0.setEncryption(encrypter);
      var4.setPropName(var3);
      var4.putUandPValues(var0);
   }

   public static String getDefaultConfigFileName() {
      UserConfigFileManager var0 = new UserConfigFileManager();
      var0.debug("DBG: UserConfigFileManager.getDefaultConfigFileName()");
      var0.resolveConfigFileName();
      return var0.configFileName;
   }

   public static String getDefaultKeyFileName() {
      UserConfigFileManager var0 = new UserConfigFileManager();
      var0.debug("DBG: UserConfigFileManager.getDefaultKeyFileName()");
      var0.resolveKeyFileName();
      return var0.keyFileName;
   }

   public void load() throws FileNotFoundException, IOException {
      this.debug("DBG: UserConfigFileManager.load()");
      this.configProperties = new Properties();
      this.configIn = new FileInputStream(this.configFileName);
      this.configProperties.load(this.configIn);
      this.configIn.close();
   }

   public void refresh() throws IOException {
      this.debug("DBG: UserConfigFileManager.refresh()");
      throw new UnsupportedOperationException();
   }

   public void store() throws FileNotFoundException, IOException {
      this.debug("DBG: UserConfigFileManager.store()");
      FileOutputStream var1 = new FileOutputStream(this.configFileName);

      try {
         this.configProperties.store(var1, "WebLogic User Configuration File; " + String.valueOf(2));
         var1.close();
      } catch (ClassCastException var3) {
         throw new IOException("Could not convert property to correct type while storing configuration");
      }
   }

   public void clear() {
      this.debug("DBG: UserConfigFileManager.clear()");
      this.configProperties.clear();
   }

   public boolean containsKey(Object var1) {
      this.debug("DBG: UserConfigFileManager.containsKey(Object)");
      return this.configProperties.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      this.debug("DBG: UserConfigFileManager.containsValue(Object)");
      return this.configProperties.containsValue(var1);
   }

   public Set entrySet() {
      this.debug("DBG: UserConfigFileManager.entrySet()");
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object var1) {
      this.debug("DBG: UserConfigFileManager.equals(Object)");
      throw new UnsupportedOperationException();
   }

   public Object get(Object var1) {
      this.debug("DBG: UserConfigFileManager.get(Object)");
      throw new UnsupportedOperationException();
   }

   public int hashCode() {
      this.debug("DBG: UserConfigFileManager.hashCode()");
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      this.debug("DBG: UserConfigFileManager.isEmpty()");
      return this.configProperties.isEmpty();
   }

   public Set keySet() {
      this.debug("DBG: UserConfigFileManager.keySet()");
      throw new UnsupportedOperationException();
   }

   public Object put(Object var1, Object var2) {
      this.debug("DBG: UserConfigFileManager.put(Object, Object)");
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      this.debug("DBG: UserConfigFileManager.putAll(Map)");
      throw new UnsupportedOperationException();
   }

   public Object remove(Object var1) {
      this.debug("DBG: UserConfigFileManager.remove(Object)");
      throw new UnsupportedOperationException();
   }

   public int size() {
      this.debug("DBG: UserConfigFileManager.size()");
      throw new UnsupportedOperationException();
   }

   public Collection values() {
      this.debug("DBG: UserConfigFileManager.values()");
      throw new UnsupportedOperationException();
   }

   private void resolveConfigFileName() {
      this.debug("DBG: UserConfigFileManager.resolveConfigFile()");
      if (this.configFileName == null) {
         String var1 = System.getProperty("user.home");
         if (var1 == null) {
            var1 = ".";
         }

         String var2 = System.getProperty("user.name");
         if (var2 == null) {
            var2 = "user";
         }

         this.configFileName = new String(var1 + "/" + var2 + "-WebLogicConfig.properties");
         this.debug("DBG: default config file name: " + this.configFileName);
      } else {
         this.debug("DBG: config file name: " + this.configFileName);
      }

   }

   private void resolveKeyFileName() {
      this.debug("DBG: UserConfigFileManager.resolveKeyFileName()");
      if (this.keyFileName == null) {
         String var1 = System.getProperty("user.home");
         if (var1 == null) {
            var1 = ".";
         }

         String var2 = System.getProperty("user.name");
         if (var2 == null) {
            var2 = "user";
         }

         this.keyFileName = new String(var1 + "/" + var2 + "-WebLogicKey.properties");
         this.debug("DBG: default key file name: " + this.keyFileName);
      } else {
         this.debug("DBG: key file name: " + this.keyFileName);
      }

   }

   private void setPropName(String var1) {
      this.debug("DBG: UserConfigFileManager.setPropName(-" + (var1 == null ? "null" : var1) + "-)");
      this.propName = new String(var1);
   }

   private String getPropName() {
      this.debug("DBG: UserConfigFilemanager.getPropName(); returning: " + this.propName);
      return this.propName;
   }

   private void putUandPValues(UsernameAndPassword var1) {
      this.debug("DBG: UserConfigFileManager.putUandPValues()");
      this.resolveConfigFileName();
      if (encrypter == null) {
         this.debug("DBG: encrypter is null; should already be initialized");
      } else {
         try {
            this.load();
         } catch (FileNotFoundException var4) {
            this.debug("DBG: FileNotFoundException loading config file, may not exist; NOT AN ERROR");
         } catch (IOException var5) {
            this.debug("DBG: IOException loading config file, may not exist; NOT AN ERROR");
         }

         try {
            if (this.ensureEncrypted()) {
               this.store();
            }
         } catch (FileNotFoundException var6) {
            System.err.println("Unable to create or write the user configuration file. Check that the filename is correctly specified.");
            return;
         } catch (IOException var7) {
            System.err.println("Unable to write the user configuration file.  Check that the directory is writable.");
            return;
         }

         this.setUandP(var1);

         try {
            this.store();
         } catch (IOException var3) {
            System.err.println("Unable to create or write the user configuration file. Check that the filename is correctly specified and writable.");
         }

      }
   }

   private UsernameAndPassword retrieveUandPValues() {
      this.debug("DBG: UserConfigFileManager.retrieveUandPValues()");
      this.resolveConfigFileName();
      if (encrypter == null) {
         this.debug("DBG: encrypter is null; should already be initialized");
         return null;
      } else {
         try {
            this.load();
         } catch (FileNotFoundException var2) {
            System.err.println("User configuration file was not found.  Check location of the file.");
            return null;
         } catch (IOException var3) {
            System.err.println("IO error loading user configuration file.  Check location and protection of file.");
            return null;
         }

         try {
            if (this.ensureEncrypted()) {
               this.store();
            }
         } catch (FileNotFoundException var4) {
            System.err.println("User configuration file was not found. Check location of the file.");
            return null;
         } catch (IOException var5) {
            System.err.println("IO error storing user configuration.  Check location and protection of user's configuration file.");
            return null;
         }

         return this.getUandP();
      }
   }

   private UsernameAndPassword getUandP() {
      this.debug("DBG: UserConfigFileManager.getUandP()");
      UsernameAndPassword var1 = new UsernameAndPassword();
      this.debug("DBG: UserConfigFileManager.getUandP(); setting encrypter on UsernameAndPassword");
      var1.setEncryption(encrypter);
      String var2;
      if (this.containsKey(this.getPropName() + ".username")) {
         var2 = this.configProperties.getProperty(this.getPropName() + ".username");
         var1.setUsername(encrypter.decrypt(var2));
      }

      if (this.containsKey(this.getPropName() + ".password")) {
         var2 = this.configProperties.getProperty(this.getPropName() + ".password");
         var1.setPassword(encrypter.decrypt(var2).toCharArray());
      }

      return var1;
   }

   private static final void clear(byte[] var0) {
      Arrays.fill(var0, (byte)0);
   }

   private void setUandP(UsernameAndPassword var1) {
      if (var1.getUsername() != null) {
         this.configProperties.setProperty(this.getPropName() + ".username", encrypter.encrypt(var1.getUsername()));
      }

      this.configProperties.setProperty(this.getPropName() + ".password", encrypter.encrypt(new String(var1.getPassword())));
   }

   private boolean verifyKeyCreate() {
      String var1 = System.getProperty("weblogic.management.confirmKeyfileCreation", "false");
      SecurityMessagesTextFormatter var2 = new SecurityMessagesTextFormatter();
      String var3;
      if (var1 != null && (var1 == null || var1.equalsIgnoreCase("true"))) {
         if (var1.equalsIgnoreCase("true")) {
            var3 = var2.getUserKeyConfigCreateNoPrompt();
            System.out.println(var3);
            return true;
         }
      } else {
         try {
            var3 = var2.getUserKeyConfigCreateNegative();
            String var4 = var2.getUserKeyConfigCreateAffirmative();
            String var5 = ServerAuthenticate.promptValue(var2.getUserKeyConfigCreatePrompt(var4, var3), true);
            if (var5 != null && var5.equalsIgnoreCase(var4)) {
               return true;
            }

            if (var5 != null && var5.equalsIgnoreCase(var3)) {
               return false;
            }

            var5 = ServerAuthenticate.promptValue(var2.getUserKeyConfigCreateConfig(var4, var3), true);
            if (var5 != null && var5.equalsIgnoreCase(var4)) {
               return true;
            }

            if (var5 != null && var5.equalsIgnoreCase(var3)) {
               return false;
            }

            System.out.println(var2.getUserKeyConfigCreateFailure());
            return false;
         } catch (Exception var6) {
            System.err.println("Error: Failed To Get Response from Standard Input");
         }
      }

      return false;
   }

   private boolean ensureEncrypted() {
      this.debug("DBG: UserConfigFileManager.ensureEncrypted()");
      boolean var1 = false;
      if (this.configProperties.isEmpty()) {
         return var1;
      } else {
         Enumeration var2 = this.configProperties.propertyNames();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            String var4 = this.configProperties.getProperty(var3);
            this.debug("DBG: UserConfigFileManager.ensureEncrypted(); checking: " + var3);
            if (!encrypter.isEncrypted(var4)) {
               var1 = true;
               this.debug("DBG: UserConfigFileManager.ensureEncrypted(); encrypting: " + var4);
               this.configProperties.setProperty(var3, encrypter.encrypt(var4));
            }
         }

         return var1;
      }
   }

   private void initEncryptionRead(boolean var1) throws KeyException {
      this.debug("DBG: UserConfigFileManager.initEncryptionRead(boolean " + var1 + ")");
      this.resolveKeyFileName();
      File var2 = new File(this.keyFileName);
      if (var2.exists()) {
         if (var1) {
            SecurityMessagesTextFormatter var3 = new SecurityMessagesTextFormatter();
            System.out.println(var3.getUsingExistingKeyFile());
         }

         try {
            FileInputStream var13 = new FileInputStream(var2);
            int var4 = var13.read();
            byte[] var5 = new byte[var4];
            var13.read(var5);
            int var6 = var13.read();
            if (var6 > 2) {
               System.err.println("Version mismatch between key and supported version; will try to continue");
            }

            var4 = var13.read();
            byte[] var7 = new byte[var4];
            var13.read(var7);
            byte[] var8 = null;
            if (var6 >= 2) {
               var4 = var13.read();
               var8 = new byte[var4];
               var13.read(var8);
            }

            var13.close();
            JSafeEncryptionServiceFactory var9 = new JSafeEncryptionServiceFactory();
            EncryptionService var10 = var9.getEncryptionService(var5, "0xfe593a5c23b88c112b3c674e33ea4c7901e26a7c", var7, var8);
            encrypter = new ClearOrEncryptedService(var10);
         } catch (FileNotFoundException var11) {
            System.err.println("Error: Secret key file was not found. Check the location of the key file.");
         } catch (IOException var12) {
            System.err.println("Error: Not able to read secret key file. Check the location and access priviledges of key file.");
         }
      } else {
         throw new KeyException();
      }
   }

   private void initEncryption(boolean var1) throws KeyException {
      this.debug("DBG: UserConfigFileManager.initEncryption(boolean " + var1 + ")");
      boolean var2 = false;

      try {
         this.initEncryptionRead(var1);
      } catch (KeyException var12) {
         var2 = true;
      }

      if (var2) {
         JSafeEncryptionServiceFactory var3 = new JSafeEncryptionServiceFactory();
         this.resolveKeyFileName();

         try {
            if (this.verifyKeyCreate()) {
               this.debug("DBG: key creation verified");
               File var4 = new File(this.keyFileName);
               if (var4.exists()) {
                  var4.delete();
               }

               var4.createNewFile();
               FileOutputStream var5 = new FileOutputStream(var4);
               byte[] var6 = Salt.getRandomBytes(4);
               byte[] var7 = var3.createEncryptedSecretKey(var6, "0xfe593a5c23b88c112b3c674e33ea4c7901e26a7c");
               byte[] var8 = var3.createAESEncryptedSecretKey(var6, "0xfe593a5c23b88c112b3c674e33ea4c7901e26a7c");
               var5.write(var6.length);
               var5.write(var6);
               var5.write(2);
               var5.write(var7.length);
               var5.write(var7);
               var5.write(var8.length);
               var5.write(var8);
               var5.flush();
               var5.close();
               EncryptionService var9 = var3.getEncryptionService(var6, "0xfe593a5c23b88c112b3c674e33ea4c7901e26a7c", var7, var8);
               encrypter = new ClearOrEncryptedService(var9);
            } else {
               throw new KeyException("Secured key storage aborted");
            }
         } catch (FileNotFoundException var10) {
            System.err.println("Error: Not able to write secret key file.  Check the specified location for the file.");
         } catch (IOException var11) {
            System.err.println("Error: Not able to write secret key file.  Check the location and priviledges of the specified file location.");
         }
      }
   }

   void debug(String var1) {
      if (this.getDebug()) {
         System.out.println(var1);
      }

   }
}
