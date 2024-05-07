package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;

public class Jspc extends CompilerTask {
   private String src;
   private String jspname;
   private String contextpath;
   private String superclass;
   private String encoding;
   private String packagename;
   private String docroot;
   private boolean skipJavac;
   private String compiler;
   private String compileall;

   public void setCompileAll(String var1) {
      this.compileall = var1;
   }

   public void setCompiler(String var1) {
      this.compiler = var1;
   }

   public void setSrc(String var1) {
      this.src = var1;
   }

   public void setJspname(String var1) {
      this.jspname = var1;
   }

   public void setContextPath(String var1) {
      this.contextpath = var1;
   }

   public void setSuperClass(String var1) {
      this.superclass = var1;
   }

   public void setEncoding(String var1) {
      this.encoding = var1;
   }

   public void setPackage(String var1) {
      this.packagename = var1;
   }

   public void setDocRoot(String var1) {
      this.docroot = var1;
   }

   public void setSkipJavac(boolean var1) {
      this.skipJavac = var1;
   }

   public void execute() throws BuildException {
      Vector var1 = super.getFlags();
      if (this.compileall != null) {
         var1.addElement("-compileAll");
         var1.addElement(this.compileall);
      }

      if (this.compiler != null) {
         var1.addElement("-compiler");
         var1.addElement(this.compiler);
      }

      if (this.contextpath != null) {
         var1.addElement("-contextPath");
         var1.addElement(this.contextpath);
      }

      if (this.superclass != null) {
         var1.addElement("-superclass");
         var1.addElement(this.superclass);
      }

      if (this.encoding != null) {
         var1.addElement("-encoding");
         var1.addElement(this.encoding);
      }

      if (this.packagename != null) {
         var1.addElement("-package");
         var1.addElement(this.packagename);
      }

      if (this.docroot != null) {
         var1.addElement("-docroot");
         var1.addElement(this.docroot);
      }

      if (this.skipJavac) {
         var1.addElement("-skipJavac");
      }

      int var2 = var1.size();
      File var3 = this.src != null ? this.project.resolveFile(this.src) : new File(".");
      File var4 = this.project.resolveFile(this.destdir);
      if (this.jspname == null) {
         DirectoryScanner var5 = this.getDirectoryScanner(var3);
         String[] var6 = var5.getIncludedFiles();
         this.scanDir(var3, var4, var6, var1);
      } else if (this.shouldCompile(this.jspname, var3, var4)) {
         var1.addElement(this.jspname);
      }

      if (var1.size() > var2) {
         String[] var7 = this.getArgs(var1);
         this.log("Compiling " + (var7.length - var2) + " jsps to " + this.destdir, 2);
         this.invokeMain("weblogic.jspc", var7);
      }

   }

   protected void scanDir(File var1, File var2, String[] var3, Vector var4) {
      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5].endsWith(".jsp") && this.shouldCompile(var3[var5], var1, var2)) {
            var4.addElement(var3[var5]);
         }
      }

   }

   private File getTarget(File var1, String var2) {
      StringBuffer var3 = new StringBuffer("");
      if (this.packagename != null) {
         var3.append(this.packagename.replace('.', File.separatorChar));
      } else {
         var3.append("jsp_servlet");
      }

      String var4 = var2.substring(0, var2.lastIndexOf(".jsp"));
      StringTokenizer var5 = new StringTokenizer(var4, "/\\");

      while(var5.hasMoreTokens()) {
         String var6 = var5.nextToken();
         var3.append(File.separatorChar);
         var3.append("_");
         var3.append(var6);
      }

      var3.append(".class");
      return new File(var1, var3.toString());
   }

   private boolean shouldCompile(String var1, File var2, File var3) {
      File var4 = new File(var2, var1);
      File var5 = this.getTarget(var3, var1);
      if (!var5.exists()) {
         return true;
      } else {
         if (var5.lastModified() > System.currentTimeMillis()) {
            this.log("Warning: file modified in the future: " + var5, 1);
         }

         return var4.lastModified() > var5.lastModified();
      }
   }
}
