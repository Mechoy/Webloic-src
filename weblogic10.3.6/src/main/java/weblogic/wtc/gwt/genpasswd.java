package weblogic.wtc.gwt;

import java.security.MessageDigest;
import java.text.Collator;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
import weblogic.wtc.jatmi.TPCrypt;

public final class genpasswd {
   public static void main(String[] var0) throws Exception {
      if (var0.length < 3) {
         System.out.println("Usage: genpasswd [-Daes] Key <LocalPassword|RemotePassword|AppPassword> <local|remote|application>");
      } else {
         BASE64Encoder var1 = new BASE64Encoder();
         String var2;
         String var3;
         String var4;
         String var5;
         String var6;
         byte[] var8;
         byte[] var11;
         if (var0.length == 4) {
            if (!"-Daes".equals(var0[0])) {
               System.out.println("Usage: genpasswd [-Daes] Key <LocalPassword|RemotePassword|AppPassword> <local|remote|application>");
               return;
            }

            var5 = var0[1];
            var6 = var0[2];
            if (var6.length() > 31) {
               System.out.println("password length can not be over 31 characters long!");
               return;
            }

            var4 = var0[3];
            MessageDigest var7 = MessageDigest.getInstance("SHA-256");
            var7.update(var5.getBytes("UTF-8"), 0, var5.getBytes("UTF-8").length);
            var8 = var7.digest();
            SecretKeySpec var9 = new SecretKeySpec(var8, "AES");
            Cipher var10 = Cipher.getInstance("AES/CBC/PKCS5Padding");
            var10.init(1, var9);
            var11 = var10.doFinal(var6.getBytes("UTF-8"));
            var2 = var1.encode(var11);
            var3 = var1.encode(var10.getIV());
         } else {
            var5 = var0[0];
            var6 = var0[1];
            if (var6.length() > 31) {
               System.out.println("password length can not be over 31 characters long!");
               return;
            }

            TPCrypt var14 = new TPCrypt();
            var4 = var0[2];
            var8 = new byte[8];
            System.arraycopy(var14.randKey(), 0, var8, 0, var8.length);
            byte[] var15 = new byte[8];
            var14.pwToKey(var5, var15);
            byte[] var16 = var6.getBytes();
            var11 = new byte[32];
            byte[] var12 = new byte[32];
            Arrays.fill(var11, (byte)0);
            Arrays.fill(var12, (byte)0);
            System.arraycopy(var16, 0, var11, 0, var16.length);
            var14.setInitializationVector(var8);
            var14.crypt(var11, var12, var11.length, 1);
            var2 = var1.encode(var12);
            var3 = var1.encode(var8);
         }

         Collator var13 = Collator.getInstance();
         var13.setStrength(0);
         if (var13.compare(var4, "local") == 0) {
            System.out.println("Local Password   : " + var2);
            System.out.println("Local Password IV: " + var3);
         } else if (var13.compare(var4, "remote") == 0) {
            System.out.println("Remote Password   : " + var2);
            System.out.println("Remote Password IV: " + var3);
         } else if (var13.compare(var4, "application") == 0) {
            System.out.println("App Password   : " + var2);
            System.out.println("App Password IV: " + var3);
         } else {
            System.out.println("Only local, remote, or application keyword are allowed");
         }

      }
   }
}
