package weblogic.security.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Permission;
import weblogic.security.SecurityLogger;
import weblogic.security.service.SecurityServiceException;

public class MakePermission {
   public static Permission makePermission(String var0, String var1, String var2) throws SecurityServiceException {
      try {
         Class var3 = Class.forName(var0, false, Thread.currentThread().getContextClassLoader());
         Class[] var4;
         Object[] var5;
         if (var2 != null) {
            var4 = new Class[]{String.class, String.class};
            var5 = new Object[]{var1, var2};
         } else if (var1 != null) {
            var4 = new Class[]{String.class};
            var5 = new Object[]{var1};
         } else {
            var4 = new Class[0];
            var5 = new Object[0];
         }

         Constructor var6 = var3.getConstructor(var4);
         return (Permission)var6.newInstance(var5);
      } catch (ClassNotFoundException var7) {
         throw new SecurityServiceException(SecurityLogger.getCantFindPermission(var0), var7);
      } catch (NoSuchMethodException var8) {
         throw new SecurityServiceException(SecurityLogger.getNoAppropriateConstructor(var0), var8);
      } catch (InstantiationException var9) {
         throw new SecurityServiceException(SecurityLogger.getCantInstantiateClass(var0), var9);
      } catch (IllegalAccessException var10) {
         throw new SecurityServiceException(SecurityLogger.getNoPermissionToInstantiate(var0), var10);
      } catch (IllegalArgumentException var11) {
         throw new SecurityServiceException(SecurityLogger.getIncorrectArgForConstructor(var0), var11);
      } catch (InvocationTargetException var12) {
         throw new SecurityServiceException(SecurityLogger.getExcInConstructor(var0), var12);
      }
   }
}
