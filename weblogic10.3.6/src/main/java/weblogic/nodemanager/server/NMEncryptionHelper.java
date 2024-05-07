package weblogic.nodemanager.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.security.internal.encryption.EncryptionServiceException;

public final class NMEncryptionHelper {
   private static final boolean DEBUG = false;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();

   private static void p(String var0) {
   }

   public static String getNMSecretHash(String var0, String var1, String var2) throws IOException, EncryptionServiceException {
      if (var0 != null && var1 != null && var2 != null) {
         DomainDir var3 = new DomainDir(var0);
         UserInfo var4 = new UserInfo(var3);
         var4.set(var1, var2);
         return var4.getHash();
      } else {
         return null;
      }
   }

   public static synchronized void updateNMHash(String var0, String var1, byte[] var2) {
      DomainDir var3 = new DomainDir(var0);
      File var4 = var3.getSecretFile();
      String var5 = new String(var2);

      try {
         UserInfo var6 = new UserInfo(var3);
         boolean var7 = true;
         if (var4.exists()) {
            var6.load(var4);
            if (var6.verify(var1, var5)) {
               var7 = false;
            }
         }

         if (var7) {
            var6.set(var1, var5);
            var6.save(var4);
         }
      } catch (Throwable var8) {
         NMServer.nmLog.log(Level.WARNING, nmText.getErrorUpdatingSecretFile(var4.getPath()), var8);
      }

   }
}
