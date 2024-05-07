package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.taskdefs.Javadoc;
import org.apache.tools.ant.taskdefs.Move;
import org.apache.tools.ant.types.FileSet;

public class AntUtil {
   private AntUtil() {
   }

   public static void deleteDir(Project var0, File var1) throws BuildException {
      Delete var2 = new Delete();
      var2.setTaskName("AntUtil.deleteDir");
      var2.setProject(var0);
      var2.setDir(var1);
      var2.setIncludeEmptyDirs(true);
      var2.setDeleteOnExit(true);
      var2.execute();
   }

   public static void copyFile(Project var0, File var1, File var2) throws BuildException {
      Copy var3 = new Copy();
      var3.setProject(var0);
      var3.setTaskName("AntUtil.copyFile");
      var3.setFile(var1);
      var3.setTofile(var2);
      var3.setOverwrite(true);
      var3.execute();
   }

   public static void copyFiles(Project var0, List<FileSet> var1, File var2) throws BuildException {
      if (!var1.isEmpty()) {
         Copy var3 = new Copy();
         var3.setProject(var0);
         var3.setTaskName("AntUtil.copyFiles");
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            FileSet var5 = (FileSet)var4.next();
            var3.addFileset(var5);
         }

         var3.setTodir(var2);
         var3.setOverwrite(true);
         var3.execute();
      }
   }

   public static void moveFiles(Project var0, File var1, File var2, String var3) throws BuildException {
      Move var4 = new Move();
      var4.setTaskName("AntUtil.moveFiles");
      var4.setProject(var0);
      var4.setTodir(var2);
      FileSet var5 = new FileSet();
      var5.setDir(var1);
      var5.setIncludes(var3);
      var4.addFileset(var5);
      var4.execute();
   }

   public static void copyFiles(Project var0, File var1, File var2, String var3) throws BuildException {
      Copy var4 = new Copy();
      var4.setTaskName("AntUtil.copyFiles");
      var4.setProject(var0);
      var2.mkdirs();
      FileSet var5 = new FileSet();
      var5.setDir(var1);
      var5.setIncludes(var3);
      var4.addFileset(var5);
      var4.setTodir(var2);
      var4.execute();
   }

   public static void javadocFiles(Project var0, String var1, File var2) {
      Javadoc var3 = new Javadoc();
      var3.setTaskName("AntUtil.javadocFiles");
      var3.setProject(var0);
      var2.mkdirs();
      var3.setDestdir(var2);
      var3.setSourcefiles(var1);
      var3.execute();
   }

   public static File[] getFiles(Collection<FileSet> var0, Project var1) {
      if (var0.size() == 0) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            FileSet var4 = (FileSet)var3.next();
            loadFiles(var4, var2, var1);
         }

         return var2.size() == 0 ? null : (File[])var2.toArray(new File[var2.size()]);
      }
   }

   public static File[] getFiles(FileSet var0, Project var1) {
      ArrayList var2 = new ArrayList();
      loadFiles(var0, var2, var1);
      return (File[])var2.toArray(new File[var2.size()]);
   }

   private static void loadFiles(FileSet var0, List<File> var1, Project var2) {
      DirectoryScanner var3 = var0.getDirectoryScanner(var2);
      String[] var4 = var3.getIncludedFiles();
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         File var9 = new File(var0.getDir(var2), var8);
         if (var9.isFile()) {
            var1.add(var9);
         }
      }

   }

   public static Collection<FileSet> getFileSets(File[] var0, Project var1) {
      HashMap var2 = new HashMap();
      if (var0 != null) {
         File[] var3 = var0;
         int var4 = var0.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            FileSet var7 = (FileSet)var2.get(var6.getParentFile());
            if (var7 == null) {
               var7 = new FileSet();
               var7.setProject(var1);
               var7.setDir(var6.getParentFile());
               var2.put(var6.getParentFile(), var7);
            }

            var7.createInclude().setName(var6.getName());
         }
      }

      return var2.values();
   }
}
