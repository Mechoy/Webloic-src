package weblogic.corba.client.spi;

import weblogic.jndi.security.SubjectPusher;

public class ServiceManager {
   public static String DEFAULT_SECURITY_MANAGER = "weblogic.jndi.security.internal.server.ServerSubjectPusher";
   private static SubjectPusher secManager = null;

   public static SubjectPusher getSecurityManager() {
      if (secManager == null) {
         secManager = (SubjectPusher)createDefaultManager(DEFAULT_SECURITY_MANAGER);
      }

      return secManager;
   }

   public static void setSecurityManager(SubjectPusher var0) {
      secManager = var0;
   }

   private static Object createDefaultManager(String var0) {
      try {
         return Class.forName(var0).newInstance();
      } catch (ClassNotFoundException var2) {
         throw new Error(var2.toString());
      } catch (InstantiationException var3) {
         throw new Error(var3.toString());
      } catch (IllegalAccessException var4) {
         throw new Error(var4.toString());
      }
   }
}
