package weblogic.security.jacc;

import javax.security.jacc.PolicyContextException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.logging.Loggable;
import weblogic.security.SecurityLogger;

public abstract class RoleMapperFactory {
   private static String ROLEMAPPERFACTORY_NAME = "weblogic.security.jacc.RoleMapperFactory.provider";
   private static RoleMapperFactory rmFactory;
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");

   public RoleMapperFactory() {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactory noarg constructor");
      }

   }

   public static RoleMapperFactory getRoleMapperFactory() throws ClassNotFoundException, PolicyContextException {
      if (rmFactory != null) {
         return rmFactory;
      } else {
         String[] var0 = new String[]{null};
         String var1 = null;

         Loggable var3;
         try {
            Class var2 = null;
            var0[0] = System.getProperty(ROLEMAPPERFACTORY_NAME);
            var1 = var0[0];
            if (var1 == null) {
               throw new ClassNotFoundException(SecurityLogger.getJACCPropertyNotSet(ROLEMAPPERFACTORY_NAME));
            }

            var2 = Class.forName(var1, true, ClassLoader.getSystemClassLoader());
            Object var8 = var2.newInstance();
            rmFactory = (RoleMapperFactory)var8;
         } catch (ClassNotFoundException var4) {
            var3 = SecurityLogger.logJACCRoleMapperFactoryProviderClassNotFoundLoggable(var1, var4);
            throw new ClassNotFoundException(var3.getMessageText(), var4);
         } catch (IllegalAccessException var5) {
            var3 = SecurityLogger.logJACCRoleMapperFactoryProviderClassNotFoundLoggable(var1, var5);
            throw new PolicyContextException(var3.getMessageText(), var5);
         } catch (InstantiationException var6) {
            var3 = SecurityLogger.logJACCRoleMapperFactoryProviderClassNotFoundLoggable(var1, var6);
            throw new PolicyContextException(var3.getMessageText(), var6);
         } catch (ClassCastException var7) {
            var3 = SecurityLogger.logJACCRoleMapperFactoryProviderClassNotFoundLoggable(var1, var7);
            throw new PolicyContextException(var3.getMessageText(), var7);
         }

         return rmFactory;
      }
   }

   public abstract RoleMapper getRoleMapper(String var1, boolean var2);

   public abstract RoleMapper getRoleMapper(String var1, String var2, boolean var3);

   public abstract RoleMapper getRoleMapperForContextID(String var1);

   public abstract void removeRoleMapper(String var1);
}
