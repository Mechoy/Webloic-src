package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import weblogic.ant.taskdefs.utils.AntLibraryUtils;
import weblogic.ant.taskdefs.utils.LibraryElement;
import weblogic.utils.Executable;

public class Appc extends CompilerTask {
   private String source = null;
   private String output = null;
   private String iiopDirectory = null;
   private String idlDirectory = null;
   private String idlMethodSignatures = null;
   private String classpath = null;
   private String libdir = null;
   private String plan = null;
   private String clientJarOutputDir = null;
   private boolean forceGeneration = false;
   private boolean lineNumbers = false;
   private boolean basicClientJar = false;
   private boolean continueCompilation = false;
   private boolean idl = false;
   private boolean idlOverwrite = false;
   private boolean idlVerbose = false;
   private boolean idlNoValueTypes = false;
   private boolean idlNoAbstractInterfaces = false;
   private boolean idlFactories = false;
   private boolean idlVisibroker = false;
   private boolean idlOrbix = false;
   private boolean iiop = false;
   private boolean verbose = false;
   private boolean enableHotCodeGen = false;
   private Collection libraries = new ArrayList();

   public void setClasspath(String var1) {
      this.classpath = var1;
   }

   public void setSource(String var1) {
      this.source = var1;
   }

   public void setOutput(String var1) {
      this.output = var1;
   }

   public void setForceGeneration(boolean var1) {
      this.forceGeneration = var1;
   }

   public void setLineNumbers(boolean var1) {
      this.lineNumbers = var1;
   }

   public void setBasicClientJar(boolean var1) {
      this.basicClientJar = var1;
   }

   public void setContinueCompilation(boolean var1) {
      this.continueCompilation = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setEnableHotCodeGen(boolean var1) {
      this.enableHotCodeGen = var1;
   }

   public void setIiopDirectory(String var1) {
      this.iiopDirectory = var1;
   }

   public void setIdlDirectory(String var1) {
      this.idlDirectory = var1;
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

   public void setIdlNoAbstractInterfaces(boolean var1) {
      this.idlNoAbstractInterfaces = var1;
   }

   public void setIdlFactories(boolean var1) {
      this.idlFactories = var1;
   }

   public void setIdlVisibroker(boolean var1) {
      this.idlVisibroker = var1;
   }

   public void setIdlOrbix(boolean var1) {
      this.idlOrbix = var1;
   }

   public void setIiop(boolean var1) {
      this.iiop = var1;
   }

   public void setIdlMethodSignatures(String var1) {
      this.idlMethodSignatures = var1;
   }

   public void setlibraryDir(String var1) {
      this.libdir = var1;
   }

   public void setPlan(String var1) {
      this.plan = var1;
   }

   public void setClientJarOutputDir(String var1) {
      this.clientJarOutputDir = var1;
   }

   public void addConfiguredLibrary(LibraryElement var1) {
      if (var1.getFile() == null) {
         throw new BuildException("Location of Library must be set");
      } else {
         this.libraries.add(var1);
      }
   }

   public void execute() {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      ClassLoader var3 = this.getClass().getClassLoader();

      try {
         if (var2 != var3) {
            var1.setContextClassLoader(var3);
         }

         this.privateExecute();
      } finally {
         var1.setContextClassLoader(var2);
      }

   }

   private void privateExecute() throws BuildException {
      File var1 = this.project.resolveFile(this.source);
      File var2 = null;
      if (this.output != null) {
         var2 = this.project.resolveFile(this.output);
      }

      if (var1 == null) {
         throw new BuildException("Source must be specified");
      } else if (!var1.exists()) {
         throw new BuildException("Source not found: " + var1);
      } else if (var2 != null && var2.isFile() && var1.isFile() && var2.lastModified() > var1.lastModified()) {
         this.log(var2 + " is up to date", 3);
      } else {
         boolean var3 = !this.canFindJavelinClasses();
         if (var3) {
            this.setExit();
         }

         Vector var4 = super.getFlags();
         if (this.forceGeneration) {
            var4.addElement("-forceGeneration");
         }

         if (this.lineNumbers) {
            var4.addElement("-lineNumbers");
         }

         if (this.basicClientJar) {
            var4.addElement("-basicClientJar");
         }

         if (this.continueCompilation) {
            var4.addElement("-k");
         }

         if (this.verbose) {
            var4.addElement("-verbose");
         }

         if (this.idl) {
            var4.addElement("-idl");
         }

         if (this.idlOverwrite) {
            var4.addElement("-idlOverwrite");
         }

         if (this.idlVerbose) {
            var4.addElement("-idlVerbose");
         }

         if (this.idlNoValueTypes) {
            var4.addElement("-idlNoValueTypes");
         }

         if (this.idlNoAbstractInterfaces) {
            var4.addElement("-idlNoAbstractInterfaces");
         }

         if (this.idlFactories) {
            var4.addElement("-idlFactories");
         }

         if (this.idlVisibroker) {
            var4.addElement("-idlVisibroker");
         }

         if (this.idlOrbix) {
            var4.addElement("-idlOrbix");
         }

         if (this.iiop) {
            var4.addElement("-iiop");
         }

         if (this.enableHotCodeGen) {
            var4.addElement("-enableHotCodeGen");
         }

         if (this.iiopDirectory != null) {
            var4.addElement("-iiopDirectory");
            var4.addElement(this.iiopDirectory);
         }

         if (this.idlDirectory != null) {
            var4.addElement("-idlDirectory");
            var4.addElement(this.idlDirectory);
         }

         if (this.idlMethodSignatures != null) {
            var4.addElement("-idlMethodSignatures");
            var4.addElement(this.idlMethodSignatures);
         }

         if (var2 != null) {
            var4.addElement("-output");
            var4.addElement(var2.getAbsolutePath());
         }

         if (this.plan != null) {
            var4.addElement("-plan");
            var4.addElement(this.project.resolveFile(this.plan).getAbsolutePath());
         }

         if (this.clientJarOutputDir != null) {
            var4.addElement("-clientJarOutputDir");
            var4.addElement(this.project.resolveFile(this.clientJarOutputDir).getAbsolutePath());
         }

         this.addLibraryFlags(var4);
         var4.addElement(var1.getAbsolutePath());
         String[] var5 = this.getArgs(var4);
         if (var3) {
            this.execAppc(var5);
         } else {
            this.invokeMain("weblogic.appc", var5);
         }

      }
   }

   private boolean canFindJavelinClasses() {
      try {
         Class var1 = this.getClass().getClassLoader().loadClass("weblogic.servlet.jsp.JspcInvoker");
         Method var2 = var1.getMethod("canFindJavelinClasses");
         Object var3 = var2.invoke((Object)null);
         return (Boolean)var3;
      } catch (Exception var4) {
         var4.printStackTrace();
         return false;
      }
   }

   private void execAppc(String[] var1) throws BuildException {
      Executable var2 = new Executable(System.out, System.err);
      ArrayList var3 = new ArrayList();
      long var4 = Runtime.getRuntime().maxMemory();
      if (var4 == Long.MAX_VALUE) {
         var4 = Runtime.getRuntime().totalMemory();
      }

      var3.add("java");
      var3.add("-mx" + var4);
      var3.add("weblogic.appc");

      for(int var6 = 0; var6 < var1.length; ++var6) {
         var3.add(var1[var6]);
      }

      this.log("[APPC_TASK] exec'ing appc", 3);
      boolean var7 = var2.exec((String[])((String[])var3.toArray(new String[var3.size()])));
      if (!var7) {
         throw new BuildException("Appc failed, see error messages above");
      }
   }

   private void addLibraryFlags(Vector var1) {
      File var2 = null;
      if (this.libdir != null) {
         var2 = this.project.resolveFile(this.libdir);
      }

      List var3 = AntLibraryUtils.getLibraryFlags(var2, this.libraries);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         var1.addElement(var4.next());
      }

   }
}
