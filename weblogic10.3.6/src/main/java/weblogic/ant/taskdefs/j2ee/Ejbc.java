package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;

public class Ejbc extends CompilerTask {
   protected String version;
   protected String source;
   protected String target;
   protected String iiopDirectory;
   protected String idlMethodSignatures;
   protected boolean idlOverwrite;
   protected boolean idlVerbose;
   protected boolean idlNoValueTypes;
   protected boolean iiop;
   protected boolean idl;
   protected boolean disableHotCodeGen;

   public void setDisableHotCodeGen(boolean var1) {
      this.disableHotCodeGen = var1;
   }

   public void setTarget(String var1) {
      this.target = var1;
   }

   public void setSource(String var1) {
      this.source = var1;
   }

   public void setIiopDirectory(String var1) {
      this.iiopDirectory = var1;
   }

   public void setIdl(boolean var1) {
      this.idl = var1;
   }

   public void setIdlOverwrite(boolean var1) {
      this.idlOverwrite = var1;
   }

   public void setIdlVerbose(boolean var1) {
      this.idlVerbose = var1;
   }

   public void setIdlNoValueTypes(boolean var1) {
      this.idlNoValueTypes = var1;
   }

   public void setIiop(boolean var1) {
      this.iiop = var1;
   }

   public void setIdlMethodSignatures(String var1) {
      this.idlMethodSignatures = var1;
   }

   protected Vector getFlags() {
      Vector var1 = super.getFlags();
      if (this.iiop) {
         var1.addElement("-iiop");
      }

      if (this.idl) {
         var1.addElement("-idl");
      }

      if (this.idlOverwrite) {
         var1.addElement("-idlOverwrite");
      }

      if (this.idlVerbose) {
         var1.addElement("-idlVerbose");
      }

      if (this.idlNoValueTypes) {
         var1.addElement("-idlNoValueTypes");
      }

      if (this.debug || this.disableHotCodeGen) {
         var1.addElement("-disableHotCodeGen");
      }

      if (this.iiopDirectory != null) {
         var1.addElement("-iiopDirectory");
         var1.addElement(this.iiopDirectory);
      }

      if (this.idlMethodSignatures != null) {
         var1.addElement("-idlMethodSignatures");
         var1.addElement(this.idlMethodSignatures);
      }

      if (this.compiler != null) {
         if (this.compiler.equals("javac")) {
            var1.addElement("-compilerclass");
            var1.addElement("com.sun.tools.javac.Main");
         } else {
            var1.addElement("-compiler");
            var1.addElement(this.compiler);
         }
      }

      if (this.version != null) {
         var1.addElement("-source");
         var1.addElement(this.version);
         var1.addElement("-target");
         var1.addElement(this.version);
      }

      if (!var1.contains("-classpath")) {
         Path var2 = this.getCompileClasspath();
         var1.addElement("-classpath");
         var1.addElement(var2.toString());
      }

      return var1;
   }

   public void execute() throws BuildException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      var1.setContextClassLoader(this.getClass().getClassLoader());
      if (System.getProperty("weblogic.home") == null) {
         String var3 = this.getProject().getProperty("wl.home");
         if (var3 != null) {
            System.setProperty("weblogic.home", var3);
         }
      }

      try {
         this.supressCompiler = true;
         File var13 = this.project.resolveFile(this.source);
         File var4 = this.project.resolveFile(this.target);
         if (var13 == null) {
            throw new BuildException("Source must be specified");
         }

         if (!var13.exists()) {
            throw new BuildException("Source not found: " + var13);
         }

         if (this.target == null) {
            throw new BuildException("Target must be specified");
         }

         if (!var13.exists() || !var4.exists() || !var4.isFile() || var4.lastModified() <= var13.lastModified()) {
            Vector var5 = this.getFlags();
            var5.addElement(var13.getAbsolutePath());
            var5.addElement(var4.getAbsolutePath());
            String[] var6 = this.getArgs(var5);

            try {
               this.invokeMain("weblogic.ejbc", var6);
               return;
            } catch (BuildException var11) {
               this.log(var11.getMessage(), 0);
               throw var11;
            }
         }

         this.log(var4 + " is up to date", 3);
      } finally {
         var1.setContextClassLoader(var2);
      }

   }
}
