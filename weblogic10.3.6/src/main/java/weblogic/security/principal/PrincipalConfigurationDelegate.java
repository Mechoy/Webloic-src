package weblogic.security.principal;

import com.bea.common.security.ApiLogger;

public abstract class PrincipalConfigurationDelegate {
   private static PrincipalConfigurationDelegate instance = null;
   private static final String OPDELEGATEIMPL = "com.bea.common.security.principal.OPPrincipalConfigurationDelegateImpl";
   private static final String WLSDELEGATEIMPL = "weblogic.security.principal.WLSPrincipalConfigurationDelegateImpl";

   public static synchronized PrincipalConfigurationDelegate getInstance() {
      if (instance != null) {
         return instance;
      } else {
         try {
            if (isOnWLS()) {
               try {
                  instance = (PrincipalConfigurationDelegate)Class.forName("weblogic.security.principal.WLSPrincipalConfigurationDelegateImpl", true, Thread.currentThread().getContextClassLoader()).newInstance();
               } catch (RuntimeException var1) {
                  System.out.println("WLS ManagedService is not up running. Fall back to use system properties for configuration.");
                  instance = (PrincipalConfigurationDelegate)Class.forName("com.bea.common.security.principal.OPPrincipalConfigurationDelegateImpl", true, Thread.currentThread().getContextClassLoader()).newInstance();
               }
            } else {
               instance = (PrincipalConfigurationDelegate)Class.forName("com.bea.common.security.principal.OPPrincipalConfigurationDelegateImpl", true, Thread.currentThread().getContextClassLoader()).newInstance();
            }
         } catch (InstantiationException var2) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipalConfigurationDelegate(var2));
         } catch (IllegalAccessException var3) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipalConfigurationDelegate(var3));
         } catch (ClassNotFoundException var4) {
            throw new IllegalStateException(ApiLogger.getUnableToInstantiatePrincipalConfigurationDelegate(var4));
         }

         return instance;
      }
   }

   public abstract boolean isEqualsCaseInsensitive();

   public abstract boolean isEqualsCompareDnAndGuid();

   private static boolean isOnWLS() {
      boolean isOnWLS = false;

      try {
         Class.forName("weblogic.version");
         isOnWLS = true;
      } catch (ClassNotFoundException var2) {
         isOnWLS = false;
      }

      return isOnWLS;
   }
}
