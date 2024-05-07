package weblogic.ant.taskdefs.management;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class WLDeploy extends Task {
   private static final Class[] MAIN_SIGNATURE = new Class[]{String[].class};
   private String adminurl;
   private boolean debug;
   private boolean external_stage;
   private boolean stage;
   private String id;
   private String name;
   private boolean nostage;
   private boolean nowait;
   private String password;
   private boolean remote;
   private File source;
   private String targets;
   private int timeout = -1;
   private String user;
   private boolean verbose;
   private String action;
   private FileSet files;
   private String deltaFiles;
   private boolean upload;
   private String altappdd;
   private String altwlsappdd;
   private String userConfigFile;
   private String userKeyFile;
   private boolean failonerror = true;
   private boolean deleteFiles = false;
   private boolean isLibrary = false;
   private String libSpecVer;
   private String libImplVer;
   private boolean adminmode;
   private boolean graceful;
   private boolean ignoresessions;
   private String appversion;
   private String planversion;
   private int retiretimeout = -1;
   private String submoduletargets;
   private String securityModel;
   private String plan;
   private String output;
   private boolean usenonexclusivelock = false;
   private boolean noversion = false;
   private boolean allversions = false;
   private boolean enableSecurityValidation = false;

   public void setEnableSecurityValidation(boolean var1) {
      this.enableSecurityValidation = var1;
   }

   public void setAllversions(boolean var1) {
      this.allversions = var1;
   }

   public void setDeleteFiles(boolean var1) {
      this.deleteFiles = var1;
   }

   public void setOutput(String var1) {
      this.output = var1;
   }

   public void setNoversion(boolean var1) {
      this.noversion = var1;
   }

   public void setUsenonexclusivelock(boolean var1) {
      this.usenonexclusivelock = var1;
   }

   public void setAdminmode(boolean var1) {
      this.adminmode = var1;
   }

   public void setGraceful(boolean var1) {
      this.graceful = var1;
   }

   public void setIgnoresessions(boolean var1) {
      this.ignoresessions = var1;
   }

   public void setAppversion(String var1) {
      this.appversion = var1;
   }

   public void setPlanversion(String var1) {
      this.planversion = var1;
   }

   public void setRetiretimeout(int var1) {
      this.retiretimeout = var1;
   }

   public void setSubmoduletargets(String var1) {
      this.submoduletargets = var1;
   }

   public void setSecurityModel(String var1) {
      this.securityModel = var1;
   }

   public void setPlan(String var1) {
      this.plan = var1;
   }

   public void setAction(String var1) {
      this.action = var1;
   }

   public void setAdminUrl(String var1) {
      this.adminurl = var1;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public void setExternalStage(boolean var1) {
      this.external_stage = var1;
   }

   public void setStage(boolean var1) {
      this.stage = var1;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setNoStage(boolean var1) {
      this.nostage = var1;
   }

   public void setNoWait(boolean var1) {
      this.nowait = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setRemote(boolean var1) {
      this.remote = var1;
   }

   public void setSource(File var1) {
      this.source = var1;
   }

   public void setTargets(String var1) {
      this.targets = var1;
   }

   public void setTimeout(int var1) {
      this.timeout = var1;
   }

   public void setUser(String var1) {
      this.user = var1;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public void setUserConfigFile(String var1) {
      this.userConfigFile = var1;
   }

   public void setUserKeyFile(String var1) {
      this.userKeyFile = var1;
   }

   public void addFiles(FileSet var1) {
      this.files = var1;
   }

   public void setDeltaFiles(String var1) {
      this.deltaFiles = var1;
   }

   public void setUpload(boolean var1) {
      this.upload = var1;
   }

   public void setAltappdd(String var1) {
      this.altappdd = var1;
   }

   public void setAltwlsappdd(String var1) {
      this.altwlsappdd = var1;
   }

   public void setFailonerror(boolean var1) {
      this.failonerror = var1;
   }

   public void setLibrary(boolean var1) {
      this.isLibrary = var1;
   }

   public void setLibSpecVer(String var1) {
      this.libSpecVer = var1;
   }

   public void setLibImplVer(String var1) {
      this.libImplVer = var1;
   }

   public void execute() throws BuildException {
      try {
         ArrayList var1 = new ArrayList();
         if (this.debug) {
            var1.add("-debug");
         }

         if (this.external_stage) {
            var1.add("-external_stage");
         }

         if (this.stage) {
            var1.add("-stage");
         }

         if (this.nostage) {
            var1.add("-nostage");
         }

         if (this.nowait) {
            var1.add("-nowait");
         }

         if (this.remote) {
            var1.add("-remote");
         }

         if (this.verbose) {
            var1.add("-verbose");
         }

         if (this.upload) {
            var1.add("-upload");
         }

         if (this.noversion) {
            var1.add("-noversion");
         }

         if (this.usenonexclusivelock) {
            var1.add("-usenonexclusivelock");
         }

         if (this.deleteFiles) {
            var1.add("-delete_files");
         }

         if (this.allversions) {
            var1.add("-allversions");
         }

         if (this.enableSecurityValidation) {
            var1.add("-enableSecurityValidation");
         }

         var1.add("-noexit");
         if (this.name != null) {
            var1.add("-name");
            var1.add(this.name);
         }

         if (this.source != null) {
            var1.add("-source");
            var1.add(this.source.toString());
         }

         if (this.targets != null) {
            var1.add("-targets");
            var1.add(this.targets);
         }

         if (this.adminurl != null) {
            var1.add("-adminurl");
            var1.add(this.adminurl);
         }

         if (this.user != null) {
            var1.add("-user");
            var1.add(this.user);
            if (this.password != null) {
               var1.add("-password");
               var1.add(this.password);
            }
         } else {
            if (this.userConfigFile != null) {
               var1.add("-userconfigfile");
               var1.add(this.userConfigFile);
            }

            if (this.userKeyFile != null) {
               var1.add("-userkeyfile");
               var1.add(this.userKeyFile);
            }
         }

         if (this.id != null) {
            var1.add("-id");
            var1.add(this.id);
         }

         if (this.timeout != -1) {
            var1.add("-timeout");
            var1.add(this.timeout + "");
         }

         if (this.action != null) {
            var1.add("-" + this.action);
         }

         if (this.altappdd != null) {
            var1.add("-altappdd");
            var1.add(this.altappdd);
         }

         if (this.altwlsappdd != null) {
            var1.add("-altwlsappdd");
            var1.add(this.altwlsappdd);
         }

         if (this.isLibrary) {
            var1.add("-library");
         }

         if (this.libSpecVer != null) {
            var1.add("-libspecver");
            var1.add(this.libSpecVer);
         }

         if (this.libImplVer != null) {
            var1.add("-libimplver");
            var1.add(this.libImplVer);
         }

         if (this.appversion != null) {
            var1.add("-appversion");
            var1.add(this.appversion);
         }

         if (this.planversion != null) {
            var1.add("-planversion");
            var1.add(this.planversion);
         }

         if (this.submoduletargets != null) {
            var1.add("-submoduletargets");
            var1.add(this.submoduletargets);
         }

         if (this.securityModel != null) {
            var1.add("-securityModel");
            var1.add(this.securityModel);
         }

         if (this.plan != null) {
            var1.add("-plan");
            var1.add(this.plan);
         }

         if (this.output != null) {
            var1.add("-output");
            var1.add(this.output);
         }

         if (this.adminmode) {
            var1.add("-adminmode");
         }

         if (this.graceful) {
            var1.add("-graceful");
         }

         if (this.ignoresessions) {
            var1.add("-ignoresessions");
         }

         if (this.retiretimeout != -1) {
            var1.add("-retiretimeout");
            var1.add(this.retiretimeout + "");
         }

         String[] var5 = this.getArgs(var1);
         System.out.print("weblogic.Deployer ");

         for(int var3 = 0; var3 < var5.length; ++var3) {
            System.out.print(var5[var3]);
            if ("-password".equals(var5[var3])) {
               System.out.print(" ******** ");
               ++var3;
            } else {
               System.out.print(" ");
            }
         }

         System.out.println("");
         this.invokeMain("weblogic.Deployer", var5);
      } catch (Exception var4) {
         Throwable var2 = var4.getCause();
         if (this.failonerror) {
            if (var2 == null) {
               throw new BuildException(var4);
            }

            throw new BuildException(var2);
         }

         if (this.verbose) {
            if (var2 == null) {
               System.out.println(var4.getMessage());
            } else {
               System.out.println(var2.getMessage());
            }
         }
      }

   }

   protected String[] getArgs(List var1) {
      ArrayList var2 = new ArrayList(var1);
      boolean var3 = "redeploy".equals(this.action);
      if (this.deltaFiles != null && var3) {
         List var9 = this.splitDeltaFiles(this.deltaFiles);
         var2.addAll(var9);
         return (String[])((String[])var2.toArray(new String[var2.size()]));
      } else {
         if (this.files != null) {
            if (var3) {
               this.log("Option 'files' is deprecated for redeploy operation. Please use option 'deltaFiles'.");
            }

            DirectoryScanner var4 = this.files.getDirectoryScanner(this.project);
            String[] var5 = var4.getIncludedFiles();
            File var6 = var4.getBasedir();

            for(int var7 = 0; var7 < var5.length; ++var7) {
               File var8 = new File(var6, var5[var7]);
               var2.add(var8.getPath());
            }
         }

         return (String[])((String[])var2.toArray(new String[var2.size()]));
      }
   }

   protected void invokeMain(String var1, String[] var2) throws Exception {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      File var3 = new File(this.getProject().getProperty("dest") + "/classes");
      URL[] var4 = new URL[]{var3.toURL()};
      URLClassLoader var5 = new URLClassLoader(var4, this.getClass().getClassLoader());
      Class var6 = var5.loadClass(var1);
      Method var7 = null;
      var7 = var6.getMethod("mainWithExceptions", MAIN_SIGNATURE);
      Object[] var8 = new Object[]{var2};
      var7.invoke((Object)null, var8);
   }

   private List splitDeltaFiles(String var1) {
      ArrayList var2 = new ArrayList();
      StringTokenizer var3 = new StringTokenizer(var1, " ,");

      while(var3.hasMoreTokens()) {
         var2.add(var3.nextToken());
      }

      return var2;
   }
}
