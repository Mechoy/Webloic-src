package weblogic.ejb.container.compliance;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.rmi.RemoteException;
import java.util.Map;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import weblogic.ejb.container.interfaces.BeanInfo;

final class ComplianceUtils {
   private static final boolean debug = false;

   static boolean methodThrowsException(Method var0, Class var1) {
      Class[] var2 = var0.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isAssignableFrom(var1)) {
            return true;
         }
      }

      return false;
   }

   static boolean methodThrowsException_correct(Method var0, Class var1) {
      Class[] var2 = var0.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].isAssignableFrom(var1)) {
            return true;
         }
      }

      return false;
   }

   static boolean checkApplicationException(Class var0, Class var1) {
      boolean var2 = true;
      if (!var1.isAssignableFrom(var0) && !Exception.class.isAssignableFrom(var0)) {
         var2 = false;
      }

      return var2;
   }

   static boolean methodThrowsExceptionAssignableFrom(Method var0, Class var1) {
      Class[] var2 = var0.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var1.isAssignableFrom(var2[var3])) {
            return true;
         }
      }

      return false;
   }

   static boolean methodThrowsExactlyException(Method var0, Class var1) {
      Class[] var2 = var0.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   static boolean localExposeThroughRemote(Method var0) {
      Class var1 = var0.getReturnType();
      if (!EJBLocalObject.class.isAssignableFrom(var1) && !EJBLocalHome.class.isAssignableFrom(var1)) {
         Class[] var2 = (Class[])var0.getParameterTypes();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (EJBLocalObject.class.isAssignableFrom(var2[var3]) || EJBLocalHome.class.isAssignableFrom(var2[var3])) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   static boolean methodTakesNoArgs(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      return var1.length == 0;
   }

   static boolean returnTypesMatch(Method var0, Method var1) {
      Class var2 = var0.getReturnType();
      Class var3 = var1.getReturnType();
      return var2.equals(var3);
   }

   static void exceptionTypesMatch(Method var0, Method var1) throws ExceptionTypeMismatchException {
      Class[] var2 = var1.getExceptionTypes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Class var4 = var2[var3];
         if (!RuntimeException.class.isAssignableFrom(var4) && !methodThrowsException(var0, var4)) {
            throw new ExceptionTypeMismatchException(var1, var4);
         }
      }

   }

   static boolean classHasPublicNoArgCtor(Class var0) {
      Constructor[] var1 = var0.getConstructors();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         Class[] var3 = var1[var2].getParameterTypes();
         if (var3.length == 0) {
            int var4 = var1[var2].getModifiers();
            if (Modifier.isPublic(var4)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isLegalRMIIIOPType(Class var0) {
      boolean var1 = true;
      return var1;
   }

   static boolean isApplicationException(Class var0) {
      return false;
   }

   static boolean isApplicationException(BeanInfo var0, Method var1, Class var2) {
      if (RemoteException.class.isAssignableFrom(var2)) {
         return false;
      } else {
         if (var0.isEJB30()) {
            if (Error.class.isAssignableFrom(var2)) {
               return false;
            }

            if (!RuntimeException.class.isAssignableFrom(var2)) {
               return true;
            }

            for(Map var3 = var0.getDeploymentInfo().getApplicationExceptions(); !var2.equals(Object.class); var2 = var2.getSuperclass()) {
               String var4 = var2.getName();
               if (var3.containsKey(var4)) {
                  return true;
               }
            }
         } else {
            if (RuntimeException.class.isAssignableFrom(var2)) {
               return false;
            }

            Class[] var5 = var1.getExceptionTypes();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].isAssignableFrom(var2)) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
