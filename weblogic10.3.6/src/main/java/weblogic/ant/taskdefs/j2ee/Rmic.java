package weblogic.ant.taskdefs.j2ee;

import java.io.File;
import java.rmi.Remote;
import java.util.Date;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;

public class Rmic extends CompilerTask {
   private String classname;
   private boolean verify = false;
   private boolean clusterable = false;
   private boolean nontransactional = false;
   private boolean serversidestubs = false;
   private boolean propagateenvironment = false;
   private boolean nomanglednames = false;
   private boolean iiop = false;
   private boolean idl = false;
   private boolean oneway = false;
   private String iiopDirectory = null;
   private String idlDirectory = null;
   private String replicahandler = null;
   private String replicaListRefreshInterval = null;
   private String descriptor = null;
   private String dgcpolicy = null;
   private boolean replicaAware = false;
   private String loadAlgorithm;
   private String callRouter;
   private boolean methodsAreIdempotent = false;
   private boolean stickToFirstServer = false;
   private String compiler;
   private boolean verbose = false;
   private String src;

   public void setSrcDir(String var1) {
      this.src = var1;
   }

   public void setClassname(String var1) {
      this.classname = var1;
   }

   public void setVerify(boolean var1) {
      this.verify = var1;
   }

   public void setIiopDirectory(String var1) {
      this.iiopDirectory = var1;
   }

   public void setIdlDirectory(String var1) {
      this.idlDirectory = var1;
   }

   public void setReplicaHandler(String var1) {
      this.replicahandler = var1;
   }

   public void setReplicaListRefreshInterval(String var1) {
      this.replicaListRefreshInterval = var1;
   }

   public void setClusterable(boolean var1) {
      this.clusterable = var1;
   }

   public void setReplicaAware(boolean var1) {
      this.replicaAware = var1;
   }

   public void setLoadAlgorithm(String var1) {
      this.loadAlgorithm = var1;
   }

   public void setCallRouter(String var1) {
      this.callRouter = var1;
   }

   public void setMethodsAreIdempotent(boolean var1) {
      this.methodsAreIdempotent = var1;
   }

   public void setStickToFirstServer(boolean var1) {
      this.stickToFirstServer = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setCompiler(String var1) {
      this.compiler = var1;
   }

   public void setTransactional(boolean var1) {
      this.nontransactional = !var1;
   }

   public void setNonTransactional(boolean var1) {
      this.nontransactional = var1;
   }

   public void setServerSideStubs(boolean var1) {
      this.serversidestubs = var1;
   }

   public void setPropagateEnvironment(boolean var1) {
      this.propagateenvironment = var1;
   }

   public void setNoMangledNames(boolean var1) {
      this.nomanglednames = var1;
   }

   public void setOneway(boolean var1) {
      this.oneway = var1;
   }

   public void setDescriptor(String var1) {
      if (this.src == null) {
         this.descriptor = var1;
      } else {
         this.descriptor = this.src + "/" + var1;
      }

   }

   public void setDgcPolicy(String var1) {
      this.dgcpolicy = var1;
   }

   public void setIiop(boolean var1) {
      this.iiop = var1;
   }

   public void setIdl(boolean var1) {
      this.idl = var1;
   }

   public void execute() throws BuildException {
      if (this.verify) {
         this.log("Verify has been turned on.", 2);
      }

      Vector var1 = super.getFlags();
      if (this.clusterable) {
         var1.addElement("-clusterable");
      }

      if (this.nontransactional) {
         var1.addElement("-nontransactional");
      }

      if (this.serversidestubs) {
         var1.addElement("-serverSideStubs");
      }

      if (this.propagateenvironment) {
         var1.addElement("-propagateEnvironment");
      }

      if (this.nomanglednames) {
         var1.addElement("-nomanglednames");
      }

      if (this.iiop) {
         var1.addElement("-iiop");
      }

      if (this.idl) {
         var1.addElement("-idl");
      }

      if (this.replicaAware) {
         var1.addElement("-replicaAware");
      }

      if (this.methodsAreIdempotent) {
         var1.addElement("-methodsAreIdempotent");
      }

      if (this.stickToFirstServer) {
         var1.addElement("-stickToFirstServer");
      }

      if (this.verbose) {
         var1.addElement("-verbose");
      }

      if (this.oneway) {
         var1.addElement("-oneway");
      }

      if (this.callRouter != null) {
         var1.addElement("-callRouter");
         var1.addElement(this.callRouter);
      }

      if (this.loadAlgorithm != null) {
         var1.addElement("-loadAlgorithm");
         var1.addElement(this.loadAlgorithm);
      }

      if (this.compiler != null) {
         var1.addElement("-compiler");
         var1.addElement(this.compiler);
      }

      if (this.iiopDirectory != null) {
         var1.addElement("-iiopDirectory");
         var1.addElement(this.iiopDirectory);
      }

      if (this.idlDirectory != null) {
         var1.addElement("-idlDirectory");
         var1.addElement(this.idlDirectory);
      }

      if (this.replicahandler != null) {
         var1.addElement("-replicaHandler");
         var1.addElement(this.replicahandler);
      }

      if (this.descriptor != null) {
         var1.addElement("-descriptor");
         var1.addElement(this.descriptor);
      }

      if (this.dgcpolicy != null) {
         var1.addElement("-dgcPolicy");
         var1.addElement(this.dgcpolicy);
      }

      int var2 = var1.size();
      File var3 = this.project.resolveFile(this.destdir);
      if (this.classname == null) {
         DirectoryScanner var4 = this.getDirectoryScanner(var3);
         String[] var5 = var4.getIncludedFiles();
         this.scanDir(var3, var5, this.verify, var1);
      } else if (this.shouldCompile(new File(var3, this.classname.replace('.', File.separatorChar) + ".class"))) {
         var1.addElement(this.classname);
      }

      if (var1.size() > var2) {
         String[] var6 = this.getArgs(var1);
         this.log("Compiling " + (var6.length - var2) + " classes to " + this.destdir, 2);
         if (this.verbose) {
            System.out.print("weblogic.rmic(");

            for(int var7 = 0; var7 < var6.length; ++var7) {
               System.out.print(var6[var7] + " ");
            }

            System.out.println(")");
         }

         this.invokeMain("weblogic.rmic", var6);
      }

   }

   protected void scanDir(File var1, String[] var2, boolean var3, Vector var4) {
      for(int var5 = 0; var5 < var2.length; ++var5) {
         File var6 = new File(var1, var2[var5]);
         if (var2[var5].endsWith(".class") && this.shouldCompile(var6)) {
            String var7 = var2[var5].replace(File.separatorChar, '.');
            var7 = var7.substring(0, var7.indexOf(".class"));
            boolean var8 = true;
            if (var3) {
               try {
                  Class var9 = Class.forName(var7);
                  if (var9.isInterface() || !this.isValidRmiRemote(var9)) {
                     var8 = false;
                  }
               } catch (ClassNotFoundException var10) {
                  this.log("Unable to verify class " + var7 + ". It could not be found.", 1);
               } catch (NoClassDefFoundError var11) {
                  this.log("Unable to verify class " + var7 + ". It is not defined.", 1);
               }
            }

            if (var8) {
               this.log("Adding: " + var7 + " to compile list", 3);
               var4.addElement(var7);
            }
         }
      }

   }

   private boolean isValidRmiRemote(Class var1) {
      Class var2 = Remote.class;
      if (var2.equals(var1)) {
         return true;
      } else {
         Class[] var3 = var1.getInterfaces();
         if (var3 != null) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var2.equals(var3[var4])) {
                  return true;
               }

               if (this.isValidRmiRemote(var3[var4])) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean shouldCompile(File var1) {
      long var2 = (new Date()).getTime();
      File var4 = new File(var1.getAbsolutePath().substring(0, var1.getAbsolutePath().indexOf(".class")) + "RTD.xml");
      if (!var4.exists()) {
         return true;
      } else if (var1.exists()) {
         if (var1.lastModified() > var2) {
            this.log("Warning: file modified in the future: " + var1, 1);
         }

         if (var1.lastModified() > var4.lastModified()) {
            return true;
         } else {
            if (this.descriptor != null) {
               File var5 = new File(this.descriptor);
               if (var5.exists() && var5.lastModified() > var4.lastModified()) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }
}
