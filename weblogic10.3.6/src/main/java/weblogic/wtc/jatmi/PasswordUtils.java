package weblogic.wtc.jatmi;

import com.bea.core.jatmi.common.ntrace;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;

public final class PasswordUtils {
   public static final int MAXTIDENT = 30;

   public static String decryptPassword(String var0, String var1, String var2, String var3) {
      boolean var4 = ntrace.getTraceLevel() == 1000373;
      if (var4) {
         ntrace.doTrace("[/Utilities/decryptPassword/");
      }

      if (var0 != null && var1 != null && var2 != null) {
         try {
            BASE64Decoder var5 = new BASE64Decoder();
            byte[] var6 = var5.decodeBuffer(var1);
            byte[] var7 = var5.decodeBuffer(var2);
            byte[] var10;
            if (var3 != null && "AES".equalsIgnoreCase(var3)) {
               MessageDigest var17 = MessageDigest.getInstance("SHA-256");
               var17.update(var0.getBytes("UTF-8"), 0, var0.getBytes("UTF-8").length);
               var10 = var17.digest();
               SecretKeySpec var18 = new SecretKeySpec(var10, "AES");
               IvParameterSpec var19 = new IvParameterSpec(var6);
               Cipher var13 = Cipher.getInstance("AES/CBC/PKCS5Padding");
               var13.init(2, var18, var19);
               byte[] var14 = var13.doFinal(var7);
               String var15 = new String(var14, "UTF-8");
               if (var4) {
                  ntrace.doTrace("]/Utilities/decryptPassword/20/xxx");
               }

               return var15;
            } else {
               TPCrypt var9 = new TPCrypt();
               var10 = new byte[8];
               byte[] var11 = new byte[32];
               var9.pwToKey(var0, var10);
               var9.setInitializationVector(var6);
               var9.crypt(var7, var11, var7.length, 0);

               int var8;
               for(var8 = 0; var8 < 32 && var11[var8] != 0; ++var8) {
               }

               String var12 = new String(var11, 0, var8);
               if (var4) {
                  ntrace.doTrace("]/Utilities/decryptPassword/30/xxx");
               }

               return var12;
            }
         } catch (Exception var16) {
            if (var4) {
               ntrace.doTrace("]/Utilities/decryptPassword/30/" + var16);
            }

            return null;
         }
      } else {
         if (var4) {
            ntrace.doTrace("]/Utilities/decryptPassword/10");
         }

         return null;
      }
   }
}
