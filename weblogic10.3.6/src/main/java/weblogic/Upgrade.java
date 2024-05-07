package weblogic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import weblogic.descriptor.DescriptorClassLoader;
import weblogic.management.utils.PDevHelper;
import weblogic.utils.Executable;

public class Upgrade {
   public static void main(String[] var0) {
      if (!PDevLibraryInClasspath()) {
         SetPDevLibraryAndForkUpgrade(var0);
      } else {
         String var1 = System.getProperty("prod.props.file");
         if (var1 == null) {
            File var2 = new File(Home.getFile(), ".." + File.separator + ".product.properties");

            try {
               if (var2.exists()) {
                  System.setProperty("prod.props.file", var2.getCanonicalPath());
               }
            } catch (IOException var9) {
            }
         }

         ClassLoader var10 = DescriptorClassLoader.getClassLoader();
         Thread.currentThread().setContextClassLoader(var10);

         try {
            Class var3 = Class.forName("weblogic.upgrade.Main", true, var10);
            Method var4 = var3.getMethod("main", String[].class);
            var4.invoke(var3, var0);
         } catch (ClassNotFoundException var5) {
            throw new AssertionError(var5);
         } catch (NoSuchMethodException var6) {
            throw new AssertionError(var6);
         } catch (IllegalAccessException var7) {
            throw new AssertionError(var7);
         } catch (InvocationTargetException var8) {
            throw new AssertionError(var8);
         }
      }
   }

   private static boolean PDevLibraryInClasspath() {
      if (System.getProperty("weblogic.upgrade.forked") != null) {
         return true;
      } else {
         try {
            Class.forName("com.bea.plateng.plugin.PlugInDefinition", true, Thread.currentThread().getContextClassLoader());
            return true;
         } catch (Exception var1) {
            return false;
         }
      }
   }

   private static void SetPDevLibraryAndForkUpgrade(String[] var0) {
      ArrayList var1 = new ArrayList();
      var1.add(System.getProperty("java.home") + "/bin/java");
      Enumeration var2 = System.getProperties().propertyNames();

      while(true) {
         String var3;
         do {
            if (!var2.hasMoreElements()) {
               var1.add("-Dweblogic.upgrade.forked");
               String var8 = System.getProperty("java.class.path");
               var8 = PDevHelper.addPDevLibraryToClasspath(var8);
               var8 = var8.replaceAll("\\\\\\\\", "/");
               var8 = var8.replaceAll("\\\\", "/");
               var8 = var8.replace('\\', '/');
               var1.add("-classpath");
               var1.add(var8);
               var1.add("weblogic.Upgrade");
               String[] var9 = var0;
               int var4 = var0.length;

               int var5;
               for(var5 = 0; var5 < var4; ++var5) {
                  String var6 = var9[var5];
                  var1.add(var6);
               }

               var9 = (String[])((String[])var1.toArray(new String[0]));
               Executable var10 = new Executable(System.out, System.err);
               var10.exec(var9);

               try {
                  var5 = var10.getExitValue();
                  System.exit(var5);
               } catch (Throwable var7) {
                  System.err.println("Exception caught from Main.doMain(...): " + var7);
                  var7.printStackTrace(System.err);
                  System.exit(-1);
               }

               return;
            }

            var3 = (String)var2.nextElement();
         } while(!var3.startsWith("weblogic") && !var3.startsWith("com"));

         var1.add("-D" + var3 + "=" + System.getProperty(var3));
      }
   }
}
