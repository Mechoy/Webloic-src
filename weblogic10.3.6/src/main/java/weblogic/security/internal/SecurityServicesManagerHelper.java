package weblogic.security.internal;

import java.util.Hashtable;
import weblogic.security.spi.SecurityServices;

public class SecurityServicesManagerHelper {
   private static Hashtable securityServices = new Hashtable();

   protected static void registerSecurityServices(SecurityServices var0, String var1) {
      securityServices.put(var1, var0);
   }

   public static SecurityServices getSecurityServices(String var0) {
      return (SecurityServices)securityServices.get(var0);
   }
}
