package weblogic.diagnostics.harvester;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import weblogic.diagnostics.descriptor.WLDFHarvestedTypeBean;
import weblogic.kernel.KernelStatus;
import weblogic.utils.AssertionError;

public class Validators {
   private static final String validatorClassName = "weblogic.diagnostics.harvester.internal.Validators";
   private static final Class validatorClass = getClass("weblogic.diagnostics.harvester.internal.Validators");
   private static Method beanValidator = getDeclaredMethod("validateHarvestedTypeBean", new Class[]{WLDFHarvestedTypeBean.class});
   private static Method typeValidator = getDeclaredMethod("validateConfiguredType", new Class[]{String.class});
   private static Method attributeValidator = getDeclaredMethod("validateConfiguredAttributes", new Class[]{String.class, String[].class});
   private static Method instanceValidator = getDeclaredMethod("validateConfiguredInstances", new Class[]{String[].class});

   private static Class getClass(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   private static Method getDeclaredMethod(String var0, Class[] var1) {
      try {
         return validatorClass.getDeclaredMethod(var0, var1);
      } catch (NoSuchMethodException var3) {
         throw new AssertionError(var3);
      }
   }

   public static void validateConfiguredType(String var0) throws IllegalArgumentException {
      if (KernelStatus.isServer()) {
         try {
            typeValidator.invoke((Object)null, var0);
         } catch (InvocationTargetException var3) {
            Throwable var2 = var3.getTargetException();
            if (var2 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)var3.getTargetException();
            } else {
               LogSupport.logUnexpectedException("Unable to validate type name: " + var0, var3);
               if (var2 instanceof RuntimeException) {
                  throw (RuntimeException)var2;
               } else {
                  throw new AssertionError(var3);
               }
            }
         } catch (RuntimeException var4) {
            LogSupport.logUnexpectedException("Unable to validate type name: " + var0, var4);
            throw var4;
         } catch (Exception var5) {
            LogSupport.logUnexpectedException("Unable to validate type name: " + var0, var5);
            throw (RuntimeException)var5;
         }
      }
   }

   public static void validateConfiguredAttribute(String var0, String var1) throws IllegalArgumentException {
      if (KernelStatus.isServer()) {
         try {
            attributeValidator.invoke((Object)null, var0, new String[]{var1});
         } catch (InvocationTargetException var4) {
            Throwable var3 = var4.getTargetException();
            if (var3 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)var4.getTargetException();
            } else {
               LogSupport.logUnexpectedException("Unable to validate attribute: " + var1 + " for type: " + var0, var4);
               if (var3 instanceof RuntimeException) {
                  throw (RuntimeException)var3;
               } else {
                  throw new AssertionError(var4);
               }
            }
         } catch (RuntimeException var5) {
            LogSupport.logUnexpectedException("Unable to validate attribute: " + var1 + " for type: " + var0, var5);
            throw var5;
         } catch (Exception var6) {
            LogSupport.logUnexpectedException("Unable to validate attribute: " + var1 + " for type: " + var0, var6);
            throw (RuntimeException)var6;
         }
      }
   }

   public static void validateConfiguredAttributes(WLDFHarvestedTypeBean var0) throws IllegalArgumentException {
      String var1 = var0.getName();
      String[] var2 = var0.getHarvestedAttributes();
      validateConfiguredAttributes(var1, var2);
   }

   public static void validateConfiguredAttributes(String var0, String[] var1) throws IllegalArgumentException {
      if (KernelStatus.isServer()) {
         try {
            attributeValidator.invoke((Object)null, var0, var1);
         } catch (InvocationTargetException var4) {
            Throwable var3 = var4.getTargetException();
            if (var3 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)var4.getTargetException();
            } else {
               LogSupport.logUnexpectedException("Unable to validate attribute list for bean: " + var0, var4);
               if (var3 instanceof RuntimeException) {
                  throw (RuntimeException)var3;
               } else {
                  throw new AssertionError(var4);
               }
            }
         } catch (RuntimeException var5) {
            LogSupport.logUnexpectedException("Unable to validate attribute list for bean: " + var0, var5);
            throw var5;
         } catch (Exception var6) {
            LogSupport.logUnexpectedException("Unable to validate attribute list for bean: " + var0, var6);
            throw (RuntimeException)var6;
         }
      }
   }

   public static void validateConfiguredInstances(String[] var0) throws IllegalArgumentException {
      if (KernelStatus.isServer()) {
         try {
            instanceValidator.invoke((Object)null, var0);
         } catch (InvocationTargetException var3) {
            Throwable var2 = var3.getTargetException();
            if (var2 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)var3.getTargetException();
            } else {
               LogSupport.logUnexpectedException("Unable to validate instance list for bean: ", var3);
               if (var2 instanceof RuntimeException) {
                  throw (RuntimeException)var2;
               } else {
                  throw new AssertionError(var3);
               }
            }
         } catch (RuntimeException var4) {
            LogSupport.logUnexpectedException("Unable to validate instance list for bean: ", var4);
            throw var4;
         } catch (Exception var5) {
            LogSupport.logUnexpectedException("Unable to validate instance list for bean: ", var5);
            throw (RuntimeException)var5;
         }
      }
   }

   public static void validateHarvestedTypeBean(WLDFHarvestedTypeBean var0) throws IllegalArgumentException {
      if (KernelStatus.isServer()) {
         try {
            beanValidator.invoke((Object)null, var0);
         } catch (InvocationTargetException var3) {
            Throwable var2 = var3.getTargetException();
            if (var2 instanceof IllegalArgumentException) {
               throw (IllegalArgumentException)var3.getTargetException();
            } else {
               LogSupport.logUnexpectedException("Unable to validate bean: " + var0.getName(), var3);
               if (var2 instanceof RuntimeException) {
                  throw (RuntimeException)var2;
               } else {
                  throw new AssertionError(var3);
               }
            }
         } catch (RuntimeException var4) {
            LogSupport.logUnexpectedException("Unable to validate bean: " + var0.getName(), var4);
            throw var4;
         } catch (Exception var5) {
            LogSupport.logUnexpectedException("Unable to validate bean: " + var0.getName(), var5);
            throw (RuntimeException)var5;
         }
      }
   }
}
