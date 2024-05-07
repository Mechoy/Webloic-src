package weblogic.management.deploy;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.InstanceNotFoundException;
import weblogic.deploy.utils.DeployerHelper;
import weblogic.deploy.utils.DeployerHelperException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

public class AutoRefreshPoller extends GenericAppPoller {
   private FileFilter filter;
   private ApplicationMBean appMBean;
   private String appName;
   private String compName;
   private DeployerRuntimeMBean deployer;
   private DeployerHelper deployerHelper;
   private String adminURL;
   private String username;
   private String password;
   private File startDir;

   public AutoRefreshPoller(File var1, boolean var2, long var3, FileFilter var5, ApplicationMBean var6, WebAppComponentMBean var7, MBeanHome var8, String var9, String var10, String var11, boolean var12) {
      super(var1, var2, var3);
      this.startDir = var1;
      this.verbose = var12;
      this.filter = var5;
      this.appMBean = var6;
      this.appName = var6.getName();
      this.compName = var7.getName();
      this.adminURL = var9;
      this.username = var10;
      this.password = var11;

      try {
         this.deployer = DeployerRuntime.getDeployerRuntime(var8);
      } catch (InstanceNotFoundException var14) {
      }

      this.deployerHelper = new DeployerHelper(var8);
   }

   public void doActivate() {
      try {
         ArrayList var1 = this.getActivateFileList();
         if (var1.size() > 0) {
            String[] var2 = this.arrayListToStringArray(var1);
            var2 = this.convertPaths(var2);
            if (this.verbose) {
               this.dumpStringArray("activating files: ", var2);
            }

            this.uploadSource(var2);
            DeploymentData var3 = new DeploymentData(var2);
            var3.addTargetsForComponent(this.appMBean, this.compName);
            DeploymentTaskRuntimeMBean var4 = this.deployer.activate((String)null, this.appName, (String)null, var3, (String)null);
            if (this.verbose) {
               System.out.println(var4.getDescription() + "( " + var4.getStatus() + " )");
               TargetStatus[] var5 = var4.getTargets();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  Exception[] var7 = var5[var6].getMessages();

                  for(int var8 = 0; var8 < var7.length; ++var8) {
                     Exception var9 = var7[var8];
                     System.out.println(var9.getMessage());
                  }
               }
            }
         }
      } catch (ManagementException var10) {
         System.out.println(var10.getMessage());
      }

   }

   public void doDeactivate() {
      try {
         ArrayList var1 = this.getDeactivateFileList();
         if (var1.size() > 0) {
            String[] var2 = this.arrayListToStringArray(var1);
            var2 = this.convertPaths(var2);
            if (this.verbose) {
               this.dumpStringArray("removing files: ", var2);
            }

            DeploymentData var3 = new DeploymentData(var2);
            var3.addTargetsForComponent(this.appMBean, this.compName);
            var3.setDelete(true);
            DeploymentTaskRuntimeMBean var4 = this.deployer.activate((String)null, this.appName, (String)null, var3, (String)null);
            if (this.verbose) {
               System.out.println(var4.getDescription() + "( " + var4.getStatus() + " )");
               TargetStatus[] var5 = var4.getTargets();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  Exception[] var7 = var5[var6].getMessages();

                  for(int var8 = 0; var8 < var7.length; ++var8) {
                     Exception var9 = var7[var8];
                     System.out.println(var9.getMessage());
                  }
               }
            }
         }
      } catch (ManagementException var10) {
         System.out.println(var10.getMessage());
      }

   }

   private String[] arrayListToStringArray(ArrayList var1) {
      String[] var2 = new String[var1.size()];
      Iterator var3 = var1.iterator();

      for(int var4 = 0; var3.hasNext(); ++var4) {
         var2[var4] = (String)var3.next();
      }

      return var2;
   }

   private String[] convertPaths(String[] var1) {
      int var2 = var1.length;
      String[] var3 = new String[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5 = this.startDir.getAbsolutePath().length() + 1;
         int var6 = var1[var4].length();
         String var7 = new String(var1[var4].substring(var5, var6));
         var3[var4] = var7;
      }

      return var3;
   }

   private void uploadSource(String[] var1) {
      String var2 = this.startDir.getAbsolutePath();

      try {
         var2 = this.startDir.getCanonicalPath();
      } catch (IOException var5) {
      }

      String var3 = null;

      try {
         var3 = this.deployerHelper.uploadSource(this.adminURL, this.username, this.password, var2, var1, this.appName);
         if (this.verbose) {
            System.out.println("uploaded files to: " + var3);
         }
      } catch (DeployerHelperException var6) {
         if (this.verbose) {
            System.out.println(var6.getMessage());
            this.dumpStringArray("could not upload files: ", var1);
         }
      }

   }

   protected final boolean shouldActivate(File var1) {
      if (this.filter != null) {
         return this.filter.accept(var1) && super.shouldActivate(var1);
      } else {
         boolean var2 = super.shouldActivate(var1) && !var1.isDirectory();
         return var2;
      }
   }

   protected final boolean shouldDeactivate(File var1) {
      if (this.filter != null) {
         return this.filter.accept(var1) && super.shouldDeactivate(var1);
      } else {
         boolean var2 = super.shouldDeactivate(var1) && !var1.isDirectory();
         return var2;
      }
   }
}
