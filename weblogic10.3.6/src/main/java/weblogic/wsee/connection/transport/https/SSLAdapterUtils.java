package weblogic.wsee.connection.transport.https;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SSLAdapterUtils {
   public static final String PEM_PREAMBLE_STRING = "-----BEGIN ";
   public static final byte[] PEM_PREAMBLE_BYTES = "-----BEGIN ".getBytes();
   private static final String JKS_KEYSTORE_TYPE = "JKS";

   public static byte[] getFileBytes(InputStream var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("stream cannot be null");
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();
         byte[] var2 = new byte[4096];
         boolean var3 = false;

         try {
            int var6;
            while((var6 = var0.read(var2)) > 0) {
               var1.write(var2, 0, var6);
            }

            while(true) {
               if (var6 >= 0) {
                  continue;
               }
            }
         } catch (IOException var5) {
            var5.printStackTrace();
         }

         byte[] var4 = var1.toByteArray();
         return var4;
      }
   }

   public static boolean containsPEMdata(byte[] var0) {
      boolean var1 = true;
      int var2 = 0;

      do {
         var1 = PEM_PREAMBLE_BYTES[var2] == var0[var2];
         ++var2;
      } while(var2 < PEM_PREAMBLE_BYTES.length && var1);

      return var1;
   }

   public static KeyStore openKeyStore(InputStream var0) throws KeyManagementException {
      try {
         KeyStore var1 = KeyStore.getInstance("JKS");
         var1.load(var0, (char[])null);
         return var1;
      } catch (KeyStoreException var2) {
         throw new KeyManagementException(var2);
      } catch (NoSuchAlgorithmException var3) {
         throw new KeyManagementException(var3);
      } catch (IOException var4) {
         throw new KeyManagementException(var4);
      } catch (CertificateException var5) {
         throw new KeyManagementException(var5);
      }
   }
}
