package weblogic.security.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import weblogic.security.SecurityLogger;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.internal.encryption.JSafeEncryptionServiceFactory;
import weblogic.utils.Hex;

public final class SerializedNMService {
   private static final String randomString = "0x1f48730ab4957122fccb2856671df094bcc294af";
   private static final boolean DEBUG = false;

   private static void debug(String var0) {
   }

   public static ClearOrEncryptedService getEncryptionService(String var0) {
      Properties var1 = new Properties();

      try {
         FileInputStream var2 = new FileInputStream(var0);
         var1.load(var2);
         var2.close();
      } catch (FileNotFoundException var6) {
         SecurityLogger.logNodeManagerPropertiesNotFound();
         return null;
      } catch (IOException var7) {
         SecurityLogger.logNodeManagerPropertiesError();
         return null;
      }

      byte[] var8 = var1.getProperty("nameHashkey").getBytes();
      byte[] var3 = var1.getProperty("idHashkey").getBytes();
      JSafeEncryptionServiceFactory var4 = new JSafeEncryptionServiceFactory();
      EncryptionService var5 = var4.getEncryptionService(Hex.fromHexString(var8, var8.length), "0x1f48730ab4957122fccb2856671df094bcc294af", Hex.fromHexString(var3, var3.length));
      return new ClearOrEncryptedService(var5);
   }
}
