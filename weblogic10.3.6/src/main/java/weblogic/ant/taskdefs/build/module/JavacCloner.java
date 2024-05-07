package weblogic.ant.taskdefs.build.module;

import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.tools.ant.taskdefs.Javac;

final class JavacCloner {
   private AccessorMethod[] methods;
   private static JavacCloner theOne = null;

   static synchronized JavacCloner getJavaCloner() {
      if (theOne == null) {
         theOne = new JavacCloner();
      }

      return theOne;
   }

   private JavacCloner() {
      ArrayList var1 = new ArrayList();
      Method[] var2 = Javac.class.getMethods();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].getName();
            if (var4.startsWith("get")) {
               String var5 = "s" + var4.substring(1);

               try {
                  Method var6 = Javac.class.getMethod(var5, var2[var3].getReturnType());
                  var1.add(new AccessorMethod(var2[var3], var6));
               } catch (NoSuchMethodException var7) {
               }
            }
         }
      }

      this.methods = new AccessorMethod[var1.size()];
      this.methods = (AccessorMethod[])((AccessorMethod[])var1.toArray(this.methods));
   }

   public void copy(Javac var1, Javac var2) {
      for(int var3 = 0; var3 < this.methods.length; ++var3) {
         this.methods[var3].copy(var1, var2);
      }

      this.copyCompilerArgs(var1, var2);
   }

   private void copyCompilerArgs(Javac var1, Javac var2) {
      try {
         Method var3 = Javac.class.getMethod("createCompilerArg", (Class[])null);
         Method var4 = Javac.class.getMethod("getCurrentCompilerArgs", (Class[])null);
         Object var5 = var4.invoke(var1, (Object[])null);
         if (var5 == null) {
            return;
         }

         Object[] var6 = (Object[])((Object[])var5);

         for(int var7 = 0; var7 < var6.length; ++var7) {
            Object var8 = var3.invoke(var2, (Object[])null);
            Method var9 = var8.getClass().getMethod("setLine", String.class);
            var9.invoke(var8, String.valueOf(var6[var7]));
         }
      } catch (Throwable var10) {
         System.err.println("Error processing compilerarg elements, set sys prop weblogic.debug.wlcompile for stacktrace");
         if (Boolean.getBoolean("weblogic.debug.wlcompile")) {
            throw new AssertionError(var10);
         }
      }

   }

   private static class AccessorMethod {
      private final Method getMethod;
      private final Method setMethod;

      AccessorMethod(Method var1, Method var2) {
         this.getMethod = var1;
         this.setMethod = var2;
      }

      public void copy(Object var1, Object var2) {
         try {
            Object var3 = this.getMethod.invoke(var1, (Object[])null);
            this.setMethod.invoke(var2, var3);
         } catch (Exception var4) {
            throw new AssertionError(var4);
         }
      }
   }
}
