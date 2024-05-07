package weblogic.ejb.container.utils.ddconverter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import weblogic.utils.jars.RandomAccessJarFile;

public final class WorkingJar extends RandomAccessJarFile {
   private File m_destJarFile;
   private static final boolean debug = false;
   private static final Set excludes = new HashSet(Arrays.asList((Object[])(new String[]{"META-INF/ejb-jar.xml", "META-INF/weblogic-ejb-jar.xml", "META-INF/MANIFEST.MF"})));

   public WorkingJar(File var1) throws IOException {
      super(var1);
      this.m_destJarFile = var1;
   }

   public File getFile() {
      return this.m_destJarFile;
   }

   public WorkingJar(File var1, String[] var2) throws IOException {
      super(var1);
      this.copyJarWithExcludes(var2);
   }

   private void copyJarWithExcludes(String[] var1) throws IOException {
      label40:
      for(int var2 = 0; var2 < var1.length; ++var2) {
         JarFile var3 = new JarFile(var1[var2]);
         Enumeration var4 = var3.entries();

         while(true) {
            JarEntry var5;
            String var6;
            do {
               do {
                  if (!var4.hasMoreElements()) {
                     continue label40;
                  }

                  var5 = (JarEntry)var4.nextElement();
               } while(var5.isDirectory());

               var6 = var5.getName();
            } while(excludes.contains(var6));

            InputStream var7 = var3.getInputStream(var5);
            OutputStream var8 = this.writeEntry(var6, true);
            byte[] var9 = new byte[1000];
            boolean var10 = false;

            int var11;
            while((var11 = var7.read(var9)) >= 0) {
               var8.write(var9, 0, var11);
            }

            var8.close();
         }
      }

   }
}
