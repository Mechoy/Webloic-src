package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import weblogic.application.utils.AnnotationDetector;
import weblogic.j2ee.J2EEUtils;
import weblogic.utils.classloaders.JarClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public final class EJBJarUtils {
   public static AnnotationDetector ejbAnnotationDetector;
   private static Method m_isAnnotationPresentMethod;

   public static boolean hasEJBSources(File var0) throws IOException {
      return var0.isDirectory() && ejbAnnotationDetector != null && ejbAnnotationDetector.hasAnnotatedSources(var0);
   }

   public static boolean isEJB(File var0) throws IOException {
      if (var0.isDirectory()) {
         File var8 = new File(var0, J2EEUtils.EJB_DD_PATH);
         if (var8.exists() && var8.length() > 0L) {
            return true;
         }

         if (ejbAnnotationDetector != null && ejbAnnotationDetector.isAnnotated(var0)) {
            return true;
         }
      } else if (var0.getName().endsWith(".jar") && var0.exists()) {
         JarFile var1 = null;

         boolean var3;
         try {
            var1 = new JarFile(var0);
            ZipEntry var2 = var1.getEntry("META-INF/ejb-jar.xml");
            if (var2 == null || var2.getSize() == 0L) {
               if (ejbAnnotationDetector == null || !ejbAnnotationDetector.isAnnotated((ZipFile)var1)) {
                  return false;
               }

               var3 = true;
               return var3;
            }

            var3 = true;
         } finally {
            if (var1 != null) {
               var1.close();
            }

         }

         return var3;
      }

      return false;
   }

   public static boolean isEJB(VirtualJarFile var0) throws IOException {
      ZipEntry var1 = var0.getEntry("META-INF/ejb-jar.xml");
      if (var1 != null && var1.getSize() != 0L) {
         return true;
      } else {
         return ejbAnnotationDetector != null && ejbAnnotationDetector.isAnnotated(var0);
      }
   }

   private static Class loadClass(File var0, String var1) throws IOException {
      Class var2 = null;

      try {
         String var3 = ".class";
         if (var1.endsWith(var3)) {
            (new StringBuilder()).append("file:///").append(var0.getAbsolutePath()).toString();
            JarClassLoader var5 = new JarClassLoader(var0);
            int var6 = var1.lastIndexOf(var3);
            String var7 = var1.substring(0, var6).replace('/', '.');
            var2 = var5.loadClass(var7);
            var5.close();
         }
      } catch (ClassNotFoundException var8) {
      } catch (NoClassDefFoundError var9) {
      } catch (MalformedURLException var10) {
         var10.printStackTrace();
      }

      return var2;
   }

   private static boolean isAnnotationPresent(Class var0) {
      boolean var1 = false;

      try {
         Class var2 = Class.forName("weblogic.dbeans.annotations.Entity");
         if (null == m_isAnnotationPresentMethod) {
            m_isAnnotationPresentMethod = var0.getClass().getMethod("isAnnotationPresent", Class.class);
         }

         Boolean var3 = (Boolean)m_isAnnotationPresentMethod.invoke(var0, var2);
         var1 = var3;
      } catch (SecurityException var4) {
      } catch (NoSuchMethodException var5) {
      } catch (ClassNotFoundException var6) {
      } catch (IllegalArgumentException var7) {
      } catch (IllegalAccessException var8) {
      } catch (InvocationTargetException var9) {
      }

      return var1;
   }

   public static void main(String[] var0) throws IOException {
      if (var0.length != 1) {
         System.out.println("Usage EjbJarUtils <file>");
         System.exit(-1);
      }

      System.out.println("Do we have annotations in the module? " + isEJB(new File(var0[0])));
   }

   static {
      try {
         ejbAnnotationDetector = new AnnotationDetector(new Class[]{Class.forName("javax.ejb.MessageDriven"), Class.forName("javax.ejb.Stateful"), Class.forName("javax.ejb.Stateless")});
      } catch (Throwable var1) {
         ejbAnnotationDetector = null;
      }

      m_isAnnotationPresentMethod = null;
   }
}
