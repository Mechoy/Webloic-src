package weblogic.nodemanager.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.ConfigException;
import weblogic.security.Salt;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.security.internal.encryption.JSafeEncryptionServiceFactory;
import weblogic.utils.Hex;

public class Upgrader {
   private NMServerConfig conf;
   private static Logger nmLog = Logger.getLogger("weblogic.nodemanager");
   byte[] salt;
   byte[] key;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   public Upgrader(NMServerConfig var1) {
      this.conf = var1;
   }

   public static void upgrade(NMServerConfig var0) throws ConfigException, IOException {
      (new Upgrader(var0)).upgrade();
   }

   public void upgrade() throws ConfigException, IOException {
      this.upgradeDataProperties();
      this.upgradeConfigProperties();
   }

   private void loadDataProperties(File var1) throws IOException {
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
         throw new IOException(nmText.getInvalidDataFile(var1.toString()));
      }
   }

   public void upgradeDataProperties() {
      File var1 = new File(this.conf.getNMHome());
      File var2 = new File(var1, "nm_data.properties");
      if (!var2.exists()) {
         File var3 = new File(var1, "SerializedNodeManagerIni.dat");
         if (!var3.exists()) {
            var3 = new File(var1, "NodeManagerProperties");
            if (!var3.exists()) {
               return;
            }

            try {
               this.loadDataProperties(var3);
               byte[] var4 = this.salt;
               byte[] var6 = this.key;
               JSafeEncryptionServiceFactory var8 = new JSafeEncryptionServiceFactory();
               byte[] var5 = Salt.getRandomBytes(4);

               byte[] var7;
               try {
                  var7 = var8.reEncryptEncryptedSecretKey(var6, var4, var5, "0x194ce8ab97302f33a77c82de564091f1ac4873be", "0x1f48730ab4957122fccb2856671df094bcc294af");
               } catch (EncryptionServiceException var11) {
                  String var10 = "password";
                  if (this.conf != null) {
                     var10 = this.conf.getConfigProperties().getProperty("keyPassword");
                     if (var10 == null || var10.equals("")) {
                        var10 = "password";
                     }
                  }

                  var7 = var8.reEncryptEncryptedSecretKey(var6, var4, var5, var10, "0x1f48730ab4957122fccb2856671df094bcc294af");
               }

               NMProperties var9 = new NMProperties();
               var9.setProperty("nameHashKey", Hex.asHex(var5, var5.length));
               var9.setProperty("idHashKey", Hex.asHex(var7, var7.length));
               var9.save(var2, (String)null);
               var3.delete();
               log(Level.INFO, nmText.getNMDataPropsMigrated(var3.toString(), var2.toString()));
            } catch (Throwable var12) {
               log(Level.INFO, nmText.getNMDataPropsMigrateError(var3.toString(), var2.toString()), var12);
            }
         } else {
            log(Level.INFO, nmText.getNMDataPropsRenamed(var3.toString(), var2.toString()));
            if (!var3.renameTo(var2)) {
               log(Level.WARNING, nmText.getNMDataPropsRenameError(var3.toString()));
            }
         }

      }
   }

   private void upgradeConfigProperties() throws ConfigException, IOException {
      File var1 = new File(this.conf.getNMHome());
      NMProperties var2 = new NMProperties();
      File var3 = new File(var1, "nodemanager.properties");
      if (var3.exists()) {
         var2.loadWithComments(var3);
         boolean var4 = NMServerConfig.checkUpgrade(var2, true);
         var4 = SSLConfig.checkUpgrade(var2, new Encryptor(this.conf), true) || var4;
         if (var4) {
            log(Level.INFO, nmText.getSavingUpgradedProps(var3.toString()));
            var2.saveWithComments(var3);
         }

      }
   }

   public static void log(Level var0, String var1, Throwable var2) {
      LogRecord var3 = new LogRecord(var0, var1);
      var3.setParameters(new String[]{"Upgrade"});
      if (var2 != null) {
         var3.setThrown(var2);
      }

      nmLog.log(var3);
   }

   public static void log(Level var0, String var1) {
      log(var0, var1, (Throwable)null);
   }

   public static void upgrade(File var0, boolean var1) throws ConfigException, IOException {
      var0 = var0.getAbsoluteFile();
      if (var0.exists() && var0.isDirectory()) {
         NMProperties var2 = new NMProperties();
         File var3 = new File(var0, "nodemanager.properties");
         if (var3.exists()) {
            var2.load(var3);
         }

         var2.setProperty("NodeManagerHome", var0.getPath());
         if (var1) {
            var2.setProperty("LogLevel", "ALL");
            var2.setProperty("LogToStderr", "true");
         }

         NMServerConfig var4 = new NMServerConfig(var2);
         log(Level.INFO, nmText.getUpgradeStarted(var0.toString()));
         (new Upgrader(var4)).upgrade();
      } else {
         throw new IOException(nmText.getNMDirError(var0.toString()));
      }
   }

   public static void main(String[] var0) throws Throwable {
      boolean var1 = false;
      int var2 = 0;
      File var3 = new File(".");
      if ("-v".equals(var0[0]) || "-verbose".equals(var0[0])) {
         var1 = true;
         ++var2;
      }

      if (var0.length > var2) {
         var3 = new File(var0[0]);
         ++var2;
      }

      if (var0.length != var2) {
         throw new IllegalArgumentException("Usage: java weblogic.nodemanager.server.Upgrader [-v] [dir]");
      } else {
         upgrade(var3, var1);
      }
   }
}
