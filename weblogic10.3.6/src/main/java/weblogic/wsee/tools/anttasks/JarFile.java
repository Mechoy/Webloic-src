package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Jar;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.ZipFileSet;
import weblogic.wsee.tools.WsBuildException;

class JarFile {
   private static final Zip.Duplicate DUPLICATE = new Zip.Duplicate();
   private final Task owningTask;
   private final File outputFile;
   private final File baseDir;
   private final List<ZipFileSet> zipFileSets = new ArrayList();

   JarFile(Task var1, File var2, File var3) {
      assert var1 != null;

      assert var2 != null;

      assert var3 != null;

      this.owningTask = var1;
      this.outputFile = var2;
      this.baseDir = var3;
   }

   void addZipFileSet(ZipFileSet var1) {
      this.zipFileSets.add(var1);
   }

   void addZipFileSets(List<ZipFileSet> var1) {
      this.zipFileSets.addAll(var1);
   }

   private void copy() {
      File var1 = this.outputFile;
      var1.mkdirs();
      Iterator var2 = this.zipFileSets.iterator();

      while(var2.hasNext()) {
         ZipFileSet var3 = (ZipFileSet)var2.next();
         Copy var4 = new Copy();
         var4.setTaskName(this.owningTask.getTaskName());
         var4.setProject(this.owningTask.getProject());
         var4.addFileset(var3);
         String var5 = var3.getPrefix(this.owningTask.getProject());
         String var6 = var5 == null ? "" : var5;
         var4.setTodir(new File(var1, var6));
         var4.execute();
      }

   }

   private void jar() throws WsBuildException {
      File var1 = this.outputFile;
      this.prepareJar(var1);
      Jar var2 = new Jar();
      var2.setDuplicate(DUPLICATE);
      Iterator var3 = this.zipFileSets.iterator();

      while(var3.hasNext()) {
         ZipFileSet var4 = (ZipFileSet)var3.next();
         var2.addFileset(var4);
      }

      var2.setProject(this.owningTask.getProject());
      var2.setTaskName(this.owningTask.getTaskName());
      var2.setBasedir(this.baseDir);
      var2.setDestFile(var1);
      var2.setExcludes("**/*");
      var2.execute();
      this.owningTask.log("Created JWS deployment outputFile: " + var2.getDestFile());
   }

   private void prepareJar(File var1) throws WsBuildException {
      if (var1.exists()) {
         this.owningTask.log("Deleting existing module outputFile " + var1.getAbsolutePath());
         if (!var1.delete()) {
            this.owningTask.log("Unable to delete module outputFile " + var1.getAbsolutePath());
         }
      }

      if (!var1.getParentFile().exists() && !var1.getParentFile().mkdirs()) {
         throw new WsBuildException("Unable to create output directory " + var1.getParentFile().getAbsolutePath());
      }
   }

   void write(boolean var1) throws WsBuildException {
      if (var1) {
         this.copy();
      } else {
         this.jar();
      }

      this.zipFileSets.clear();
   }

   String getName() {
      return this.outputFile.getName();
   }

   static {
      DUPLICATE.setValue("preserve");
   }
}
