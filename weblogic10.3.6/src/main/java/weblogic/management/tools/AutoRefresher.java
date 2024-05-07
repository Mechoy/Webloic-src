package weblogic.management.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.management.InstanceNotFoundException;
import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import weblogic.management.Helper;
import weblogic.management.MBeanHome;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.deploy.AutoRefreshPoller;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.utils.compiler.Tool;

public class AutoRefresher extends Tool {
   public static final int DEFAULT_SLEEP_INTERVAL = 5000;
   public static final boolean DEFAULT_RECURSIVE = true;
   private static final boolean debug = false;
   private static boolean verbose = false;
   private AutoRefreshPoller poller;
   private DeployerRuntimeMBean deployer;
   private ApplicationMBean appMBean;
   private WebAppComponentMBean compMBean;
   private MBeanHome adminMBeanHome;
   private String adminURL;
   private String username;
   private String password;
   private String startDir;

   public AutoRefresher(String[] var1) {
      super(var1);
   }

   public static void main(String[] var0) {
      try {
         AutoRefresher var1 = new AutoRefresher(var0);
         var1.run();
      } catch (Exception var2) {
         System.out.println("exception caught: " + var2.getClass().getName());
         System.out.println(var2.getMessage());
         var2.printStackTrace();
      }

   }

   public void prepare() throws InstanceNotFoundException, NamingException {
      String var1 = "t3://localhost:7001";
      this.opts.addOption("adminURL", "adminURL", "(required) The URL for admin server, default is " + var1);
      this.opts.addOption("username", "username", "(required) The username to connect with.");
      this.opts.addOption("password", "password", "(required) The password to connect with.");
      this.opts.addOption("sleepInterval", "sleepInterval", "(optional) The frequency of the poller runs (in seconds). Enter 0 to run once. Default sleepInterval is 5 seconds.");
      this.opts.addOption("filter", "filter", "(optional) filename extension filter. If specified, the tool will only look for files with a given extention. You may specify more than one filter in a comma separated list. Ex: -filter jsp will look for only files matching *.jsp Ex: -filter jsp,html,gif will look for only files matching *.jsp, *.html, and *.gif");
      this.opts.addFlag("recursive", "(optional) Should this tool search subdirectories.");
      this.opts.addFlag("verbose", "(optional) If specified, prints out status information. ");
      this.opts.addOption("application", "application", "The name of the Application to which your WebAppComponent belongs.");
      this.opts.addOption("component", "component", "The name of the WebAppComponent.");
      this.opts.addArgFlag("path", "path", "The path to the directory containing the application you want to monitor. This should point to the root level of the application. The path may be specified as either an absolute or a relative path");
      this.opts.setUsageArgs("path\n");
   }

   public void runBody() throws InstanceNotFoundException, NamingException {
      try {
         StringBuffer var1 = new StringBuffer();
         if (!this.hasRequiredParameters(var1)) {
            this.opts.setUsageHeader(var1.toString());
            this.opts.usageAndExit("weblogic.AutoRefresher");
         }

         String[] var2 = this.opts.args();
         String var3 = var2[0];
         this.init(this.opts.getOption("adminURL"), this.opts.getOption("username"), this.opts.getOption("password"), this.opts.getOption("sleepInterval"), this.opts.getOption("application"), this.opts.getOption("component"), this.opts.getOption("filter"), this.opts.getOption("recursive"), this.opts.getOption("verbose"), var3);
         this.poller.runInSameThread();
      } catch (AuthenticationException var4) {
         throw new AuthenticationException("\nUnable to connect as user: " + this.opts.getOption("username") + ". Make sure the username and password are correct.");
      } catch (NamingException var5) {
         throw new NamingException("\nUnable to connect to: " + this.opts.getOption("adminURL") + ". Make sure that the url is correct, and that the admin server is running");
      } catch (InstanceNotFoundException var6) {
         System.err.println(var6.getMessage());
      } catch (Throwable var7) {
         System.err.println("\nCaught unexpected exception: " + var7.getClass().getName() + " " + var7.getMessage());
         var7.printStackTrace();
      }

   }

   private void init(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10) throws NamingException, InstanceNotFoundException {
      verbose = false;
      if (var9 != null) {
         verbose = true;
      }

      this.adminURL = var1;
      this.username = var2;
      this.password = var3;
      this.startDir = var10;

      try {
         if (verbose) {
            System.out.println("");
         }

         if (verbose) {
            System.out.println("connecting to admin server: <" + var1 + "> application: <" + var5 + "> component: <" + var6 + ">");
         }

         this.adminMBeanHome = Helper.getAdminMBeanHome(var2, var3, var1);
         RemoteMBeanServer var11 = this.adminMBeanHome.getMBeanServer();
         String var12 = this.adminMBeanHome.getActiveDomain().getName();
         this.appMBean = (ApplicationMBean)this.adminMBeanHome.getAdminMBean(var5, "Application", var12);
         ComponentMBean[] var13 = this.appMBean.getComponents();

         for(int var14 = 0; var14 < var13.length; ++var14) {
            ComponentMBean var15 = var13[var14];
            if (var15.getName().equals(var6)) {
               this.compMBean = (WebAppComponentMBean)var15;
            }
         }

         if (this.compMBean == null) {
            throw new InstanceNotFoundException("no web app component " + var6 + " in application " + var5);
         }
      } catch (ClassCastException var18) {
         System.err.println("no web app component " + var6 + " in application " + var5);
         this.opts.usageAndExit("weblogic.AutoRefresher");
      } catch (IllegalArgumentException var19) {
         System.err.println(var19.getMessage());
         this.opts.usageAndExit("weblogic.AutoRefresher");
      } catch (InstanceNotFoundException var20) {
         System.err.println(var20.getMessage());
         this.opts.usageAndExit("weblogic.AutoRefresher");
      }

      StringBuffer var21 = new StringBuffer();
      if (!this.workingDirMatchesDeployDir(var10, var21)) {
         System.err.println(var21.toString());
         this.opts.usageAndExit("weblogic.AutoRefresher");
      }

      int var22 = 5000;
      if (var4 != null && var4.length() != 0) {
         var22 = Integer.parseInt(var4);
      }

      AutoRefreshPollerFileFilter var23 = null;
      if (var7 != null && var7.length() != 0) {
         var23 = new AutoRefreshPollerFileFilter(var7);
      }

      boolean var24 = true;
      if (var8 == null) {
         var24 = false;
      }

      File var25 = new File(var10);

      try {
         var25 = var25.getCanonicalFile();
      } catch (IOException var17) {
      }

      this.poller = new AutoRefreshPoller(var25, var24, (long)var22, var23, this.appMBean, this.compMBean, this.adminMBeanHome, var1, var2, var3, verbose);
   }

   private boolean workingDirMatchesDeployDir(String var1, StringBuffer var2) {
      String var3 = this.appMBean.getPath();
      String var4 = this.compMBean.getURI();
      File var5 = new File(var1 + File.separatorChar + var4);
      if (!var5.exists()) {
         var2.append("Error: Working directory stucture is inconsistent with application directory Component directory specified does not exist on the client.\nComponent path on server: " + var4 + "\n");
         return false;
      } else {
         String var6 = new String((new File(var3)).getName());
         String var7 = new String((new File(var1)).getName());
         if (var7.equals(var6)) {
            return true;
         } else {
            var2.append("Error: Working directory stucture is inconsistent with application directory struture on the server.\nApplication directory is inconsistent\nApplication path name on server: " + var6 + "\n" + "Application path name on client: " + var7 + "\n");
            return false;
         }
      }
   }

   private boolean hasRequiredParameters(StringBuffer var1) {
      String[] var2 = this.opts.args();
      boolean var3 = true;
      if (!this.opts.hasOption("adminURL")) {
         var3 = false;
         var1.append("  Error: Missing required flag: adminURL.\n");
      }

      if (!this.opts.hasOption("username")) {
         var3 = false;
         var1.append("  Error: Missing required flag: username.\n");
      }

      if (!this.opts.hasOption("password")) {
         var3 = false;
         var1.append("  Error: Missing required flag: password.\n");
      }

      if (!this.opts.hasOption("application")) {
         var3 = false;
         var1.append("  Error: Missing required flag: application.\n");
      }

      if (!this.opts.hasOption("component")) {
         var3 = false;
         var1.append("  Error: Missing required flag: component.\n");
      }

      if (var2.length != 1) {
         var3 = false;
         var1.append("  Error: Missing required argument: <path>\n");
      }

      if (var2.length > 1) {
         var3 = false;
         var1.append("  Error: Too many args: ");

         for(int var4 = 1; var4 < var2.length; ++var4) {
            var1.append(var2[var4]);
            var1.append(" ");
         }

         var1.append("\n");
      }

      if (var2.length > 0) {
         File var5 = new File(var2[0]);
         if (!var5.exists()) {
            var1.append("  Error: Path to application does not exist:  " + var5 + "\n");
            var3 = false;
         }
      }

      if (var1.length() > 0) {
         var1.insert(0, "\n");
      }

      return var3;
   }

   private void dumpArrayList(ArrayList var1) {
      StringBuffer var2 = new StringBuffer("DUMPING ARRAY LIST\n");
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         var2.append((String)var3.next());
         var2.append("\n");
      }

      System.out.println(var2.toString());
   }

   private void dumpStringArray(String[] var1) {
      StringBuffer var2 = new StringBuffer("DUMPING STRING ARRAY\n");

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(var1[var3] + "\n");
      }

      System.out.println(var2.toString());
   }
}
