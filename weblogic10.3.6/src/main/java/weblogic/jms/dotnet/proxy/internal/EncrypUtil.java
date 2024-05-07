package weblogic.jms.dotnet.proxy.internal;

import java.util.Hashtable;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dotnet.transport.Transport;

public class EncrypUtil {
   public static final String USERNAME = "username";
   public static final String PASSWORD = "password";
   private static final String AES_PREFIX = "{AES}";

   static Hashtable<String, String> decrypt(Transport var0, Hashtable<String, String> var1) throws Exception {
      Hashtable var2 = new Hashtable(var1);
      Cipher var3 = createCipher(var0);
      var2 = decode(var3, "java.naming.security.principal", var2);
      var2 = decode(var3, "java.naming.security.credentials", var2);
      return var2;
   }

   static String decryptString(Transport var0, String var1) throws Exception {
      Cipher var2 = createCipher(var0);
      return decryptString(var2, var1);
   }

   private static Hashtable<String, String> decode(Cipher var0, String var1, Hashtable<String, String> var2) throws Exception {
      byte var3 = 33;
      byte var4 = 13;
      Hashtable var5 = new Hashtable(var2);
      if (var5.containsKey(var1)) {
         String var6 = (String)var5.get(var1);
         if (var6 != null && var6.startsWith("{AES}")) {
            var6 = var6.substring("{AES}".length());
            byte[] var7 = (new BASE64Decoder()).decodeBuffer(var6);
            byte[] var8 = var0.doFinal(var7);
            int var10 = var8.length - var3 - var4;
            if (var10 < 0) {
               throw new Exception("The encrypted data for " + var1 + " is not right");
            }

            String var9;
            if (var10 > 0) {
               byte[] var11 = new byte[var10];
               System.arraycopy(var8, var3, var11, 0, var10);
               var9 = new String(var11, "US-ASCII");
            } else {
               var9 = "";
            }

            if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
               JMSDebug.JMSDotNetProxy.debug("Encrypted = true  for " + var1 + "; partial encrypted text : " + var6.substring(0, 5));
            }

            var5.put(var1, var9);
         } else if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Encrypted = false  for " + var1 + "; partial text : " + "*****");
         }
      }

      return var5;
   }

   private static Cipher createCipher(Transport var0) throws Exception {
      long var1 = var0.getScratchId();
      byte[] var3 = new byte[16];

      for(int var4 = 0; var4 < 8; ++var4) {
         var3[var4] = (byte)((int)var1);
         var1 >>>= 8;
      }

      var3[8] = -19;
      var3[9] = 29;
      var3[10] = -17;
      var3[11] = 74;
      var3[12] = -101;
      var3[13] = 37;
      var3[14] = -119;
      var3[15] = -95;
      byte[] var8 = new byte[]{123, var3[3], 34, var3[0], -8, var3[2], 34, var3[7], 121, var3[6], -67, var3[1], 100, var3[4], 99, -40};
      Cipher var5 = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec var6 = new SecretKeySpec(var3, "AES");
      IvParameterSpec var7 = new IvParameterSpec(var8);
      var5.init(2, var6, var7);
      return var5;
   }

   private static String decryptString(Cipher var0, String var1) throws Exception {
      byte var2 = 33;
      byte var3 = 13;
      String var4;
      if (var1 != null && var1.startsWith("{AES}")) {
         String var5 = var1;
         var1 = var1.substring("{AES}".length());
         byte[] var6 = (new BASE64Decoder()).decodeBuffer(var1);
         byte[] var7 = var0.doFinal(var6);
         int var8 = var7.length - var2 - var3;
         if (var8 <= 0) {
            throw new Exception("The encrypted data for " + var1 + " is not right");
         }

         byte[] var9 = new byte[var8];
         System.arraycopy(var7, var2, var9, 0, var8);
         var4 = new String(var9, "US-ASCII");
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Encrypted = true; partial encrypted text : " + var5.substring(0, 5));
         }
      } else {
         if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
            JMSDebug.JMSDotNetProxy.debug("Encrypted = false; partial text : *****");
         }

         var4 = var1;
      }

      return var4;
   }
}
