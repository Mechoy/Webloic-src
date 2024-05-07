package weblogic.security;

import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public abstract class SecurityEnvironment {
   private static SecurityEnvironment singleton;

   public static SecurityEnvironment getSecurityEnvironment() {
      if (singleton == null) {
         try {
            singleton = (SecurityEnvironment)Class.forName("weblogic.security.WLSSecurityEnvironmentImpl").newInstance();
         } catch (Exception var5) {
            try {
               singleton = (SecurityEnvironment)Class.forName("weblogic.security.CESecurityEnvironmentImpl").newInstance();
            } catch (Exception var4) {
               try {
                  singleton = (SecurityEnvironment)Class.forName("weblogic.security.ClientSecurityEnvironmentImpl").newInstance();
               } catch (Exception var3) {
                  throw new IllegalArgumentException(var3.toString());
               }
            }
         }
      }

      return singleton;
   }

   public static void setSecurityEnvironment(SecurityEnvironment var0) {
      singleton = var0;
   }

   public abstract Logger getServerLogger();

   public abstract void decrementOpenSocketCount(SSLSocket var1);
}
