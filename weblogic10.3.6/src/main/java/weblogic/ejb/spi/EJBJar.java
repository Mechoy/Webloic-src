package weblogic.ejb.spi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.io.Archive;
import weblogic.application.io.Ear;
import weblogic.application.io.Jar;
import weblogic.utils.classloaders.ClassFinder;

public class EJBJar {
   private final Archive archive;
   private final ClassFinder classfinder;

   public EJBJar(String var1, File var2) throws IOException {
      if (var2.isDirectory()) {
         this.archive = new ExplodedEJB(var1, new File[]{var2});
      } else {
         this.archive = new Jar(var1, var2);
      }

      this.classfinder = this.archive.getClassFinder();
   }

   public EJBJar(String var1, ApplicationContextInternal var2) throws IOException {
      Ear var3 = var2.getEar();
      if (var3 == null) {
         File var4 = new File(var2.getStagingPath());
         if (!var4.exists()) {
            throw new FileNotFoundException("Unable to find ejb-jar for uri " + var1 + " at path " + var4.getAbsolutePath());
         }

         if (var4.isDirectory()) {
            this.archive = new ExplodedEJB(var1, new File[]{var4});
         } else {
            this.archive = new Jar(var1, var4);
         }
      } else {
         File[] var5 = var3.getModuleRoots(var1);
         if (var5.length == 0) {
            throw new FileNotFoundException("Unable to find ejb-jar with uri " + var1 + " in ear at " + var2.getStagingPath());
         }

         if (var5.length == 1 && !var5[0].isDirectory()) {
            this.archive = new Jar(var1, var5[0]);
         } else {
            this.archive = new ExplodedEJB(var1, var5);
         }
      }

      this.classfinder = this.archive.getClassFinder();
   }

   public ClassFinder getClassFinder() {
      return this.classfinder;
   }

   public void remove() {
      this.archive.remove();
   }
}
