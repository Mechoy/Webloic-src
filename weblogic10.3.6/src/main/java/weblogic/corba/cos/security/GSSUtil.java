package weblogic.corba.cos.security;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

public class GSSUtil {
   private static final boolean DEBUG = false;
   static final byte[] encodedGSSNTOID = new byte[]{6, 6, 43, 6, 1, 5, 6, 4};
   static final byte[] encodedGSSUPOID = new byte[]{6, 6, 103, -127, 2, 1, 1, 1};

   public static byte[] createGSSUPGSSNTExportedName(String var0) {
      byte[] var1 = var0.getBytes();
      int var2 = 4 + encodedGSSUPOID.length + 4 + var1.length;
      byte[] var3 = new byte[var2];
      int var4 = 0;
      var3[var4++] = 4;
      var3[var4++] = 1;
      var3[var4++] = (byte)(encodedGSSUPOID.length >>> 8);
      var3[var4++] = (byte)(encodedGSSUPOID.length >>> 0);
      System.arraycopy(encodedGSSUPOID, 0, var3, var4, encodedGSSUPOID.length);
      var4 += encodedGSSUPOID.length;
      var3[var4++] = (byte)(var1.length >>> 24);
      var3[var4++] = (byte)(var1.length >>> 16);
      var3[var4++] = (byte)(var1.length >>> 8);
      var3[var4++] = (byte)(var1.length >>> 0);
      System.arraycopy(var1, 0, var3, var4, var1.length);
      return var3;
   }

   public static String extractGSSUPGSSNTExportedName(byte[] var0) {
      int var1 = var0.length;
      if (var0[0] == 4 && var0[1] == 1) {
         int var2 = (var0[2] & 255) << 8 | var0[3] & 255;
         if (var2 != encodedGSSUPOID.length) {
            return null;
         } else {
            byte[] var3 = new byte[var2];
            System.arraycopy(var0, 4, var3, 0, var2);
            if (!Arrays.equals(var3, encodedGSSUPOID)) {
               return null;
            } else {
               int var4 = 4 + encodedGSSUPOID.length;
               int var5 = (var0[var4] & 255) << 24 | (var0[var4 + 1] & 255) << 16 | (var0[var4 + 2] & 255) << 8 | var0[var4 + 3] & 255;
               var4 += 4;
               byte[] var6 = new byte[var5];
               System.arraycopy(var0, var4, var6, 0, var5);
               return new String(var6);
            }
         }
      } else {
         return null;
      }
   }

   public static byte[] getGSSUPMech() {
      return encodedGSSUPOID;
   }

   public static boolean isGSSUPMech(byte[] var0) {
      return Arrays.equals(var0, getGSSUPMech());
   }

   public static byte[] getGSSUPToken(byte[] var0) {
      byte[] var1 = getGSSUPMech();
      int var2 = var1.length + var0.length;
      int var3 = getDERNumOctets(var2);
      int var4 = 1 + var3 + var1.length + var0.length;
      byte[] var5 = new byte[var4];
      int var6 = 0;
      var5[var6++] = 96;
      if (var2 < 128) {
         var5[var6++] = (byte)var2;
      } else {
         var5[var6++] = (byte)(var3 + 127);
         switch (var3) {
            case 2:
               var5[var6++] = (byte)(var2 & 255);
               break;
            case 3:
               var5[var6++] = (byte)(var2 >> 8 & 255);
               var5[var6++] = (byte)(var2 & 255);
               break;
            case 4:
               var5[var6++] = (byte)(var2 >> 16 & 255);
               var5[var6++] = (byte)(var2 >> 8 & 255);
               var5[var6++] = (byte)(var2 & 255);
               break;
            case 5:
               var5[var6++] = (byte)(var2 >> 24);
               var5[var6++] = (byte)(var2 >> 16 & 255);
               var5[var6++] = (byte)(var2 >> 8 & 255);
               var5[var6++] = (byte)(var2 & 255);
         }
      }

      System.arraycopy(var1, 0, var5, var6, var1.length);
      var6 += var1.length;
      System.arraycopy(var0, 0, var5, var6, var0.length);
      return var5;
   }

   private static int getDERNumOctets(int var0) {
      if (var0 < 128) {
         return 1;
      } else if (var0 < 256) {
         return 2;
      } else if (var0 < 65536) {
         return 3;
      } else {
         return var0 < 16777216 ? 4 : 5;
      }
   }

   public static byte[] getGSSUPInnerToken(byte[] var0) {
      int var1 = 0;
      int var2 = var0.length;
      if (var0.length >= 6 && var0[var1++] == 96) {
         boolean var3 = false;
         int var4;
         if (var0[var1] < 128 && var0[var1] >= 0) {
            byte var6 = var0[var1++];
         } else {
            var4 = var0[var1++] & 127;
            int var5;
            switch (var4) {
               case 1:
                  var5 = var0[var1++] & 255;
                  break;
               case 2:
                  var5 = var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
                  break;
               case 3:
                  var5 = var0[var1++] << 16 & 16711680 | var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
                  break;
               case 4:
                  var5 = var0[var1++] << 24 & -16777216 | var0[var1++] << 16 & 16711680 | var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
            }
         }

         for(var4 = 0; var4 < encodedGSSUPOID.length; ++var4) {
            if (var1 >= var2 || encodedGSSUPOID[var4] != var0[var1]) {
               return null;
            }

            ++var1;
         }

         byte[] var7 = new byte[var2 - var1];
         System.arraycopy(var0, var1, var7, 0, var2 - var1);
         return var7;
      } else {
         return null;
      }
   }

   public static X509Certificate[] getX509CertChain(byte[] var0) {
      int var1 = 0;
      if (var0 != null && var0.length >= 2 && var0[var1++] == 48) {
         int var2 = 0;
         if (var0[var1] < 128 && var0[var1] >= 0) {
            var2 = var0[var1++];
         } else {
            int var3 = var0[var1++] & 127;
            switch (var3) {
               case 1:
                  var2 = var0[var1++] & 255;
                  break;
               case 2:
                  var2 = var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
                  break;
               case 3:
                  var2 = var0[var1++] << 16 & 16711680 | var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
                  break;
               case 4:
                  var2 = var0[var1++] << 24 & -16777216 | var0[var1++] << 16 & 16711680 | var0[var1++] << 8 & '\uff00' | var0[var1++] & 255;
            }
         }

         if (var2 != var0.length - var1) {
            return null;
         } else {
            ByteArrayInputStream var7 = new ByteArrayInputStream(var0, var1, var2);
            ArrayList var4 = new ArrayList();

            try {
               CertificateFactory var5 = CertificateFactory.getInstance("X.509");

               while(var7.available() > 0) {
                  var4.add(var5.generateCertificate(var7));
               }
            } catch (CertificateException var6) {
               return null;
            }

            return (X509Certificate[])((X509Certificate[])var4.toArray(new X509Certificate[var4.size()]));
         }
      } else {
         return null;
      }
   }

   public static String getQuotedGSSUserName(String var0) {
      int var2 = var0.indexOf(64);
      if (var2 < 0) {
         return var0;
      } else {
         StringBuffer var3 = new StringBuffer();
         if (var2 > 0) {
            var3.append(var0.substring(0, var2));
         }

         var3.append('\\');
         var3.append('@');
         var3.append(var0.substring(var2 + 1));
         String var1 = var3.toString();
         return var1;
      }
   }

   public static String getUnquotedGSSUserName(String var0) {
      int var2 = var0.indexOf(92);
      if (var2 < 0) {
         return var0;
      } else {
         StringBuffer var3 = new StringBuffer();
         if (var2 > 0) {
            var3.append(var0.substring(0, var2));
         }

         var3.append(var0.substring(var2 + 1));
         String var1 = var3.toString();
         return var1;
      }
   }

   private static void p(String var0) {
      System.out.println("<GSSUtil>: " + var0);
   }
}
