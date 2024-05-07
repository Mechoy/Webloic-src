package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.MatchingTask;
import weblogic.ant.taskdefs.utils.AntLibraryUtils;
import weblogic.ant.taskdefs.utils.LibraryElement;
import weblogic.application.compiler.AppMerge;

public final class AppMergeTask extends MatchingTask {
   private String source = null;
   private String output = null;
   private String libdir = null;
   private String plan = null;
   private boolean verbose = false;
   private Collection libraries = new ArrayList();

   public void setSource(String var1) {
      this.source = var1;
   }

   public void setOutput(String var1) {
      this.output = var1;
   }

   public void setlibraryDir(String var1) {
      this.libdir = var1;
   }

   public void setPlan(String var1) {
      this.plan = var1;
   }

   public void addConfiguredLibrary(LibraryElement var1) {
      this.libraries.add(var1);
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void execute() {
      ArrayList var1 = new ArrayList();
      File var2 = null;
      if (this.output != null) {
         var2 = this.project.resolveFile(this.output);
         var1.add("-output");
         var1.add(var2.getAbsolutePath());
      }

      File var3 = this.project.resolveFile(this.source);
      if (var3 == null) {
         throw new BuildException("Source must be specified");
      } else if (!var3.exists()) {
         throw new BuildException("Source not found: " + var3);
      } else if (var2 != null && var2.isFile() && var3.isFile() && var2.lastModified() > var3.lastModified()) {
         this.log(var2 + " is up to date", 3);
      } else {
         if (this.plan != null) {
            var1.add("-plan");
            var1.add(this.project.resolveFile(this.plan).getAbsolutePath());
         }

         if (this.verbose) {
            var1.add("-verbose");
         }

         var1.add("-noexit");
         File var4 = null;
         if (this.libdir != null) {
            var4 = this.project.resolveFile(this.libdir);
         }

         var1.addAll(AntLibraryUtils.getLibraryFlags(var4, this.libraries));
         var1.add(var3.getAbsolutePath());
         this.runAppMerge((String[])((String[])var1.toArray(new String[var1.size()])));
      }
   }

   private void runAppMerge(String[] var1) {
      try {
         AppMerge.main(var1);
      } catch (Exception var3) {
         throw new BuildException(var3);
      }
   }
}
