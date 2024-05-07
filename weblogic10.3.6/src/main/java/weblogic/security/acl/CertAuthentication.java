package weblogic.security.acl;

import java.security.cert.Certificate;
import java.util.Vector;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.KernelMBean;
import weblogic.utils.NestedRuntimeException;

/** @deprecated */
public class CertAuthentication {
   private static CertAuthenticator certer = null;
   private static final Object sync = new Object();
   private static KernelMBean config = null;

   public static void setup() {
      if (certer == null) {
         synchronized(sync) {
            if (certer == null) {
               String var1 = Kernel.getConfig().getSSL().getCertAuthenticator();
               if (var1 != null) {
                  try {
                     certer = (CertAuthenticator)Class.forName(var1).newInstance();
                  } catch (Exception var4) {
                     throw new NestedRuntimeException("could not create certificate authenticator", var4);
                  }
               } else {
                  certer = new CertDenier();
               }
            }
         }
      }

   }

   public static User authenticate(String var0, Vector var1, boolean var2) {
      if (var1 != null && var1.size() != 0) {
         Certificate[] var3 = new Certificate[var1.size()];
         var1.copyInto(var3);
         return authenticate(var0, var3, var2);
      } else {
         return null;
      }
   }

   public static User authenticate(String var0, Certificate[] var1, boolean var2) {
      return var1 != null && var1.length != 0 && certer != null ? certer.authenticate(var0, var1, var2) : null;
   }

   private static class CertDenier implements CertAuthenticator {
      private CertDenier() {
      }

      public User authenticate(String var1, Certificate[] var2, boolean var3) {
         return null;
      }

      // $FF: synthetic method
      CertDenier(Object var1) {
         this();
      }
   }
}
