package weblogic.connector.common;

import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.ResourceAdapter;
import weblogic.connector.exception.RACommonException;
import weblogic.connector.exception.RAException;
import weblogic.connector.extensions.Suspendable;
import weblogic.connector.external.ConfigPropInfo;
import weblogic.connector.external.PropSetterTable;
import weblogic.j2ee.descriptor.ConfigPropertyBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;

public class Utils {
   public static void throwAsResourceException(String var0, Throwable var1) throws ResourceException {
      ResourceException var2 = new ResourceException(var0);
      var2.initCause(var1);
      throw var2;
   }

   public static void throwAsApplicationServerInternalException(String var0, Exception var1) throws ApplicationServerInternalException {
      ApplicationServerInternalException var2 = new ApplicationServerInternalException(var0);
      var2.initCause(var1);
      throw var2;
   }

   public static void setProperties(RAInstanceManager var0, Object var1, Collection var2, PropSetterTable var3) {
      if (var1 == null) {
         throw new AssertionError("obj == null");
      } else {
         Debug.enter("weblogic.connector.common.Utils", "setProperties( " + var1.getClass().getName() + " )");

         try {
            Iterator var4 = var2.iterator();
            Debug.println("Iterate through all the config properties and (optionally) set them in the obj");
            String var6 = "";

            while(true) {
               if (!var4.hasNext()) {
                  if (var6.length() > 0) {
                     Debug.logConfigPropWarning(var1.getClass().getName(), var0 != null ? var0.getModuleName() : "", var6);
                  }
                  break;
               }

               ConfigPropInfo var5 = (ConfigPropInfo)var4.next();
               if (var5.getValue() != null) {
                  try {
                     ConfigPropertyBean var7 = var3.getRAProperty(var5.getName());
                     Method var17 = var3.getSetMethod(var7);
                     if (var17 != null) {
                        invokeSetter(var17, var1, var5, var0);
                     }
                  } catch (RuntimeException var14) {
                     String var8 = Debug.getExceptionPropertyValueTypeMismatch(var5.getName(), var5.getType(), var5.getValue(), var14.toString());
                     var6 = var6 + var8 + PlatformConstants.EOL;
                  } catch (RACommonException var15) {
                     var6 = var6 + var15.getBaseMessage() + PlatformConstants.EOL;
                  }
               }
            }
         } finally {
            Debug.exit("weblogic.connector.common.Utils", "setProperties() ");
         }

      }
   }

   private static void invokeSetter(Method var0, Object var1, ConfigPropInfo var2, RAInstanceManager var3) throws RACommonException, NumberFormatException {
      String var4 = var2.getType();
      String var5 = var2.getValue();
      String var6 = var2.getName();
      if (var0 == null) {
         throw new AssertionError("writeMethod == null in call to Utils.invokeSetter()");
      } else {
         Object var7 = getValueByType(var5, var4);
         Object[] var8 = new Object[]{var7};
         AuthenticatedSubject var9 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

         try {
            var3.getAdapterLayer().invoke(var0, var1, var8, var9);
         } catch (InvocationTargetException var13) {
            Throwable var15 = var13.getCause();
            if (var15 == null || !(var15 instanceof PropertyVetoException)) {
               String var12 = Debug.getExceptionInvokeSetter(var6);
               throw new RACommonException(var12, var13);
            }

            Debug.logPropertyVetoWarning(var1.getClass().getName(), var6, var4, var5, var15.toString());
         } catch (IllegalAccessException var14) {
            String var11 = Debug.getExceptionInvokeSetter(var6);
            throw new RACommonException(var11, var14);
         }

      }
   }

   public static Object getValueByType(String var0, String var1) throws NumberFormatException {
      Object var2 = null;
      if (var1.equals("java.lang.String")) {
         var2 = var0;
      } else if (!var1.equals("java.lang.Character") && !var1.equals("char")) {
         if (!var1.equals("java.lang.Boolean") && !var1.equals("boolean")) {
            if (!var1.equals("java.lang.Integer") && !var1.equals("int")) {
               if (!var1.equals("java.lang.Double") && !var1.equals("double")) {
                  if (!var1.equals("java.lang.Byte") && !var1.equals("byte")) {
                     if (!var1.equals("java.lang.Short") && !var1.equals("short")) {
                        if (!var1.equals("java.lang.Long") && !var1.equals("long")) {
                           if (!var1.equals("java.lang.Float") && !var1.equals("float")) {
                              String var3 = Debug.getExceptionBadPropertyType(var1);
                              throw new AssertionError(var3);
                           }

                           var2 = Float.valueOf(var0);
                        } else {
                           var2 = Long.valueOf(var0);
                        }
                     } else {
                        var2 = Short.valueOf(var0);
                     }
                  } else {
                     var2 = Byte.valueOf(var0);
                  }
               } else {
                  var2 = Double.valueOf(var0);
               }
            } else {
               var2 = Integer.valueOf(var0);
            }
         } else {
            var2 = Boolean.valueOf(var0);
         }
      } else {
         var2 = new Character(var0.charAt(0));
      }

      return var2;
   }

   public static RAException consolidateException(RAException var0, Throwable var1) {
      if (var1 != null) {
         if (var0 == null) {
            var0 = new RAException();
         }

         var0.addError(var1);
         if (var1 instanceof RAException) {
            RAException var2 = (RAException)var1;
            Iterator var3 = var2.getErrors();
            Throwable var4 = null;

            while(var3.hasNext()) {
               var4 = (Throwable)var3.next();
               consolidateException(var0, var4);
            }
         }
      }

      return var0;
   }

   public static int getManagementCount() {
      return ManagementCountThreadLocal.get();
   }

   public static void startManagement() {
      ManagementCountThreadLocal.increment();
   }

   public static void stopManagement() {
      ManagementCountThreadLocal.decrement();
   }

   public static boolean isRAVersionable(RAInstanceManager var0, RAInstanceManager var1) {
      ResourceAdapter var2 = null;
      if (var1 != null) {
         var2 = var1.getResourceAdapter();
      }

      ResourceAdapter var3 = var0.getResourceAdapter();
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      AuthenticatedSubject var7 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      var4 = var1 == null || var2 != null && var2 instanceof Suspendable && var1.getAppContext().getRuntime().isEAR() && var0.getAdapterLayer().supportsVersioning((Suspendable)var2, var7) && !var1.getRAInfo().isEnableAccessOutsideApp();
      var5 = var3 != null && var3 instanceof Suspendable && var0.getAppContext().getRuntime().isEAR() && var0.getAdapterLayer().supportsInit((Suspendable)var3, var7) && var0.getAdapterLayer().supportsVersioning((Suspendable)var3, var7) && !var0.getRAInfo().isEnableAccessOutsideApp();
      var6 = var4 && var5;
      return var6;
   }

   public static Class[] getInterfaces(Class var0) {
      ArrayList var1 = new ArrayList();
      addInterfacesRecusively(var0, var1);
      Class[] var2 = new Class[var1.size()];
      return (Class[])((Class[])var1.toArray(var2));
   }

   private static void addInterfacesRecusively(Class var0, List var1) {
      Class[] var2 = var0.getInterfaces();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (!var1.contains(var2[var3])) {
            var1.add(var2[var3]);
         }

         addInterfacesRecusively(var2[var3], var1);
      }

      Class var4 = var0.getSuperclass();
      if (var4 != null) {
         addInterfacesRecusively(var4, var1);
      }

   }
}
