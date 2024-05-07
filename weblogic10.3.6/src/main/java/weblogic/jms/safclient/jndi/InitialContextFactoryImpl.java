package weblogic.jms.safclient.jndi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import weblogic.jms.extensions.ClientSAF;
import weblogic.jms.extensions.ClientSAFDuplicateException;
import weblogic.jms.extensions.ClientSAFFactory;

public class InitialContextFactoryImpl implements InitialContextFactory {
   private static NamingException getNamingException(Throwable var0) {
      NamingException var1 = new NamingException(var0.getMessage());
      var1.setRootCause(var0);
      return var1;
   }

   public Context getInitialContext(Hashtable var1) throws NamingException {
      String var2 = (String)var1.get("java.naming.provider.url");
      ClientSAF var3;
      if (var2 == null) {
         try {
            var3 = ClientSAFFactory.getClientSAF();
         } catch (ClientSAFDuplicateException var58) {
            var3 = var58.getDuplicate();
         } catch (JMSException var59) {
            throw getNamingException(var59);
         }
      } else {
         URL var4;
         try {
            var4 = new URL(var2);
         } catch (MalformedURLException var57) {
            throw getNamingException(var57);
         }

         URI var5;
         try {
            var5 = new URI(var4.toString());
         } catch (URISyntaxException var56) {
            var5 = null;
         }

         File var6 = null;
         if (var5 != null) {
            File var7;
            try {
               var7 = new File(var5);
            } catch (IllegalArgumentException var55) {
               var7 = null;
            }

            if (var7 != null) {
               var6 = var7.getParentFile();
            }
         }

         InputStream var64;
         try {
            var64 = var4.openStream();
         } catch (IOException var54) {
            throw getNamingException(var54);
         }

         try {
            if (var6 == null) {
               try {
                  var3 = ClientSAFFactory.getClientSAF(var64);
               } catch (ClientSAFDuplicateException var51) {
                  var3 = var51.getDuplicate();
               } catch (JMSException var52) {
                  throw getNamingException(var52);
               }
            } else {
               try {
                  var3 = ClientSAFFactory.getClientSAF(var6, var64);
               } catch (ClientSAFDuplicateException var49) {
                  var3 = var49.getDuplicate();
               } catch (JMSException var50) {
                  throw getNamingException(var50);
               }
            }
         } finally {
            try {
               var64.close();
            } catch (IOException var46) {
               throw getNamingException(var46);
            }
         }
      }

      Object var61 = var1.get("java.naming.security.credentials");
      char[] var62 = null;
      boolean var63 = false;
      if (var61 != null) {
         if (var61 instanceof char[]) {
            var62 = (char[])((char[])var61);
         } else {
            if (!(var61 instanceof String)) {
               throw new NamingException("The SECURITY_CREDENTIALS field must either be a String or char[].    Instead, it is of type " + var61.getClass().getName());
            }

            var62 = ((String)var61).toCharArray();
            var63 = true;
         }
      }

      boolean var29 = false;

      try {
         var29 = true;
         var3.open(var62);
         var29 = false;
      } catch (JMSException var48) {
         throw getNamingException(var48);
      } finally {
         if (var29) {
            if (var63) {
               for(int var12 = 0; var12 < var62.length; ++var12) {
                  var62[var12] = 'y';
               }
            }

         }
      }

      if (var63) {
         for(int var65 = 0; var65 < var62.length; ++var65) {
            var62[var65] = 'y';
         }
      }

      try {
         return var3.getContext();
      } catch (JMSException var47) {
         throw getNamingException(var47);
      }
   }
}
