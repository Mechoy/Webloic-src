package weblogic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import weblogic.application.ApplicationDescriptor;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.ModuleBean;
import weblogic.utils.io.StreamUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class ClientDeployer {
   private static final int SUFFIX_LENGTH = ".runtime.xml".length();

   public static void main(String[] var0) throws Exception {
      if (var0.length < 2) {
         usage();
      }

      VirtualJarFile var1 = null;
      File var2 = new File(var0[0]);
      if (var2.isDirectory()) {
         var1 = VirtualJarFactory.createVirtualJar(var2);
      } else {
         var1 = VirtualJarFactory.createVirtualJar(new JarFile(var0[0]));
      }

      File var3 = var2.getParentFile();

      for(int var4 = 1; var4 < var0.length; ++var4) {
         processClientJar(var1, var0[var4] + ".jar", new File(var3, var0[var4] + ".runtime.xml"));
      }

   }

   public static File deploy(File var0, JarFile var1, File var2) {
      String var3 = getJarFile(var0.getName());
      File var4 = new File(var2, var3);

      try {
         processClientJar((JarFile)var1, var3, var0, new BufferedOutputStream(new FileOutputStream(var4)));
      } catch (Exception var6) {
         System.out.println("Deployment problem ear=" + var1.getName() + ", runtime=" + var0 + ", problem=" + var6);
         var6.printStackTrace();
      }

      return var4;
   }

   private static void usage() {
      System.out.println("Usage: java weblogic.ClientDeployer <ear-file> <client>...");
      System.out.println("  where the ear-file directory contains client.runtime.xml for every client");
      System.out.println("Example: 'java weblogic.ClientDeployer app.ear client', where app.ear contains client.jar and current directory contains client.runtime.xml");
      System.exit(1);
   }

   private static String getJarFile(String var0) {
      return var0.substring(0, var0.length() - SUFFIX_LENGTH) + ".jar";
   }

   static void processClientJar(VirtualJarFile var0, String var1, File var2) throws IOException, ParserConfigurationException, SAXException {
      processClientJar((VirtualJarFile)var0, var1, var2, new FileOutputStream(var1));
   }

   static void processClientJar(JarFile var0, String var1, File var2) throws IOException, ParserConfigurationException, SAXException {
      processClientJar((JarFile)var0, var1, var2, new FileOutputStream(var1));
   }

   private static void processClientJar(JarFile var0, String var1, File var2, OutputStream var3) throws IOException, ParserConfigurationException, SAXException {
      processClientJar(VirtualJarFactory.createVirtualJar(var0), var1, var2, var3);
   }

   static void processClientJar(VirtualJarFile var0, String var1, File var2, OutputStream var3) throws IOException, ParserConfigurationException, SAXException {
      ZipInputStream var4;
      try {
         var4 = new ZipInputStream(var0.getInputStream(var0.getEntry(var1)));
      } catch (NullPointerException var25) {
         throw new IOException("No entry " + var1 + " found in ear file");
      }

      ZipOutputStream var5 = new ZipOutputStream(var3);
      String var6 = findAltDDUri(var0, var1);

      for(ZipEntry var7 = var4.getNextEntry(); var7 != null; var7 = var4.getNextEntry()) {
         if (var6 == null || !"META-INF/application-client.xml".equals(var7.getName())) {
            if (var7.getName().equals(var6)) {
               var5.putNextEntry(new ZipEntry("META-INF/application-client.xml"));
               StreamUtils.writeTo(var4, var5);
            } else {
               var5.putNextEntry(var7);
               StreamUtils.writeTo(var4, var5);
            }
         }
      }

      if (var6 != null) {
         var5.putNextEntry(new ZipEntry("META-INF/application-client.xml"));
         InputStream var28 = null;

         try {
            var28 = var0.getInputStream(new ZipEntry(var6));
            StreamUtils.writeTo(var28, var5);
         } catch (NullPointerException var24) {
            throw new IOException("Your client-jar specified an alt-dd of " + var6 + " but that uri was not found in the EAR");
         } finally {
            if (var28 != null) {
               var28.close();
            }

         }
      }

      if (var2.exists()) {
         System.out.println("Using weblogic-application-client.xml from " + var2.getAbsolutePath());
         var5.putNextEntry(new ZipEntry("META-INF/weblogic-application-client.xml"));
         FileInputStream var29 = null;

         try {
            var29 = new FileInputStream(var2);
            StreamUtils.writeTo(var29, var5);
         } finally {
            if (var29 != null) {
               try {
                  var29.close();
               } catch (Exception var23) {
               }
            }

         }
      }

      var5.close();
      var4.close();
   }

   private static String findAltDDUri(VirtualJarFile var0, String var1) {
      try {
         ApplicationDescriptor var2 = new ApplicationDescriptor(var0);
         ApplicationBean var3 = var2.getApplicationDescriptor();
         if (var3 == null) {
            return null;
         } else {
            ModuleBean[] var4 = var3.getModules();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (var1.equals(var4[var5].getJava())) {
                  return var4[var5].getAltDd();
               }
            }

            System.out.println("Warning: client-jar " + var1 + " was not found in the application.xml");
            return null;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
         return null;
      }
   }
}
