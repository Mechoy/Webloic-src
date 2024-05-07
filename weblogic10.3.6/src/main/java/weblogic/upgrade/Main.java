package weblogic.upgrade;

import com.bea.plateng.plugin.helper.ExecutionPlanChoiceHelper;
import com.oracle.cie.common.ui.gui.SplashWindow;
import com.oracle.cie.wizard.WizardHelper;
import com.oracle.cie.wizard.WizardStatus;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;

public class Main {
   public static final String REDIRECTED_OUTPUT_FILE_KEY = Main.class.getName() + ".REDIRECTED_OUTPUT_FILE_KEY";
   private static boolean DEBUG = true;
   private static int NO_SYSTEM_EXIT_CALLED = 1298493394;
   public static final int TTY = 4;
   public static final int GUI = 5;
   public static final int SILENT = 6;
   private String execplan = null;
   private String silentxmlfile = null;
   private String logfile = null;
   private String[] wizardparams = new String[0];
   private int mode = 5;
   private boolean isDomainUpgrade = false;
   private boolean debug = false;

   public Main() {
      this.setMode("gui");
      this.setType("domain");
   }

   public Main(String[] var1) {
      this.setMode("gui");
      boolean var2 = this.showSplash(var1);
      this.setType("domain", var2);
   }

   private boolean showSplash(String[] var1) {
      String var2 = "gui";
      String var3 = "domain";

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (var1[var4].equalsIgnoreCase("-mode")) {
            ++var4;
            var2 = var1[var4];
         } else if (var1[var4].equalsIgnoreCase("-type")) {
            ++var4;
            var3 = var1[var4];
         }
      }

      return var2.equalsIgnoreCase("GUI") && (var3.equalsIgnoreCase("domain") || var3.equalsIgnoreCase("wlsdomain"));
   }

   public void setType(String var1, boolean var2) {
      Frame var3 = null;

      try {
         try {
            if (var2) {
               URL var4 = Main.class.getClassLoader().getResource("resources/upgrade/splash_upgradeWiz.gif");
               if (var4 != null) {
                  var3 = SplashWindow.splash(Toolkit.getDefaultToolkit().createImage(var4));
               }
            }
         } catch (Throwable var8) {
         }

         this.setType(var1);
      } finally {
         if (var3 != null) {
            var3.setVisible(false);
            var3.dispose();
         }

      }

   }

   public void setSilentMode() {
      this.mode = 6;
   }

   public void setResponses(String var1) {
      this.silentxmlfile = var1;
      if ("-".equals(this.silentxmlfile)) {
         this.silentxmlfile = null;
      }

   }

   public void setType(String var1) {
      this.isDomainUpgrade = false;
      if (!var1.equals("domain") && !var1.equals("wlsdomain")) {
         if (var1.equals("nodemanager")) {
            this.setExecutionPlan("weblogic/upgrade/nodemanager/execplan.xml");
            if (resourceExists("weblogic-upgrade-nodemanager-responses.xml")) {
               this.setResponses("weblogic-upgrade-nodemanager-responses.xml");
            } else {
               this.setResponses("weblogic/upgrade/nodemanager/responses.xml");
            }
         } else {
            if (!var1.equals("securityproviders") && !var1.equals("securityprovider")) {
               throw new InvalidMainException("Invalid -type parameter: Valid options are domain, nodemanager, securityproviders");
            }

            this.setExecutionPlan("weblogic/upgrade/upgradesecurityproviders/execplan.xml");
            if (resourceExists("weblogic-upgrade-upgradesecurityproviders-responses.xml")) {
               this.setResponses("weblogic-upgrade-upgradesecurityproviders-responses.xml");
            } else {
               this.setResponses("weblogic/upgrade/upgradesecurityproviders/responses.xml");
            }
         }
      } else {
         if (resourceExists("weblogic-upgrade-domain-execplan.xml")) {
            this.setExecutionPlan("weblogic-upgrade-domain-execplan.xml");
         } else {
            this.setExecutionPlan(ExecutionPlanChoiceHelper.getUpgradeExecutionPlan());
            this.isDomainUpgrade = true;
         }

         if (resourceExists("weblogic-upgrade-domain-responses.xml")) {
            this.setResponses("weblogic-upgrade-domain-responses.xml");
         } else {
            this.setResponses("weblogic/upgrade/domain/responses.xml");
         }
      }

   }

   public void setDebug() {
      this.debug = true;
   }

   public void setLogFile(String var1) {
      this.logfile = var1;
   }

   public void setMode(String var1) {
      if (var1.equals("gui")) {
         this.mode = 5;
      } else {
         if (!var1.equals("silent")) {
            throw new InvalidMainException("Invalid -mode parameter: Valid options are gui or silent");
         }

         this.mode = 6;
      }

   }

   public void setExecutionPlan(String var1) {
      this.execplan = var1;
   }

   public void setExtraWizardParams(String[] var1) {
      this.wizardparams = var1;
      if (this.wizardparams == null) {
         this.wizardparams = new String[0];
      }

   }

   public int execute() throws Exception {
      ArrayList var1 = new ArrayList();
      if (this.mode == 6) {
         var1.add("-mode=silent");
         var1.add("-file=wcf/plugin_silent_wizard.xml");
         if (this.silentxmlfile != null) {
            var1.add("-p:plugin:plugin.silent.response.file=" + this.silentxmlfile);
         }
      } else if (this.mode == 5) {
         var1.add("-mode=gui");
         var1.add("-file=wcf/plugin_gui_wizard.xml");
      }

      if (this.logfile != null) {
         var1.add("-log=" + this.logfile);
      } else {
         var1.add("-log=stdout");
      }

      if (this.debug) {
         var1.add("-log_priority=debug");
      }

      var1.add("-p:plugin:plugin.executionPlan.file=" + this.execplan);
      var1.add("-p:plugin:I18N_PLUGIN=weblogic/upgrade/i18n_upgrade");
      var1.add("-p:plugin:TARGET_VERSION=" + UpgradeHelper.getTargetVersion());
      if (this.wizardparams != null) {
         var1.addAll(Arrays.asList((Object[])this.wizardparams));
      }

      String[] var2 = (String[])((String[])var1.toArray(new String[0]));
      log("Calling Wizard framework for upgrade: args2: " + var1);
      WizardStatus var3 = WizardHelper.invokeWizardAndWait(var2, (Observer)null);
      int var4 = -1;
      if (var3 != null) {
         var4 = var3.getExitCode();
      }

      return var4;
   }

   public static int doMain(String[] var0) throws Exception {
      if (var0.length == 1 && (var0[0].equals("-usage") || var0[0].equals("-?") || var0[0].equals("-help") || var0[0].equals("-h"))) {
         printHelp();
         return NO_SYSTEM_EXIT_CALLED;
      } else {
         Main var1 = new Main(var0);

         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2].equals("-mode")) {
               ++var2;
               var1.setMode(var0[var2]);
            } else if (var0[var2].equals("-responses")) {
               ++var2;
               var1.setResponses(var0[var2]);
            } else if (var0[var2].equals("-debug")) {
               var1.setDebug();
            } else if (var0[var2].equals("-type")) {
               ++var2;
               var1.setType(var0[var2]);
            } else if (var0[var2].equals("-out")) {
               ++var2;
               var1.setLogFile(var0[var2]);
            } else if (var0[var2].equals("-plan")) {
               ++var2;
               var1.setExecutionPlan(var0[var2]);
            } else if (var0[var2].equals("-wizardparams")) {
               ++var2;
               String[] var3 = new String[var0.length - var2];
               System.arraycopy(var0, var2, var3, 0, var3.length);
               var1.setExtraWizardParams(var3);
               break;
            }
         }

         if (var1.execplan == null) {
            System.out.println("No execution plan is specified. Please provide a type argument ('-type ...')");
            System.out.println("For usage information, type 'java weblogic.Upgrade -help'");
            return NO_SYSTEM_EXIT_CALLED;
         } else {
            if (var1.isDomainUpgrade && var1.mode == 6) {
               var1.execplan = "weblogic/upgrade/domain/execplan.xml";
            }

            return var1.execute();
         }
      }
   }

   private static void setSysProps() {
      if (System.getProperty("display.splash") == null) {
         System.setProperty("display.splash", "false");
      }

      if (System.getProperty("log4j.configuration") == null) {
         System.setProperty("log4j.configuration", "weblogic/upgrade/upgrade-log4j.properties");
      }

   }

   public static void main(String[] var0) {
      setSysProps();
      boolean var1 = false;

      int var6;
      try {
         var6 = doMain(var0);
      } catch (ArrayIndexOutOfBoundsException var3) {
         System.err.println("Invalid parameters entered");
         printHelp();
         var6 = -1;
      } catch (InvalidMainException var4) {
         System.err.println("Invalid Input: " + var4.getMessage());
         printHelp();
         var6 = -1;
      } catch (Throwable var5) {
         System.err.println("Exception caught from Main.doMain(...): " + var5);
         var5.printStackTrace(System.err);
         var6 = -1;
      }

      if (var6 != NO_SYSTEM_EXIT_CALLED) {
         System.exit(var6);
      }

   }

   public static Throwable getInnermostCause(Throwable var0) {
      Throwable var1 = var0.getCause();
      return var1 == null ? var0 : getInnermostCause(var1);
   }

   public static void printHelp() {
      String var0 = System.getProperty("line.separator");
      String var1 = "Usage: java weblogic.Upgrade [options]" + var0 + "Options and arguments:" + var0 + " -help             : Show this usage message" + var0 + "                     Synonym is -? OR -h OR -usage" + var0 + " -type <type>      : Run a specific type of upgrade." + var0 + "                     This sets up the exec plan, silent responses file, etc" + var0 + "                     Type is one of domain OR nodemanager OR securityproviders" + var0 + "                     Default: domain" + var0 + " -mode <mode>      : Specifies the mode" + var0 + "                     Type is one of gui OR silent" + var0 + "                     Default: gui" + var0 + " -responses <file> : Specify the resource location of the XML file defining the responses." + var0 + "                     Use only with silent mode" + var0 + "                     Give a - if there is no response file" + var0 + " -out <file>       : Specify the file to send standard output and error to." + var0 + "                     if not specified, standard output and error is used" + var0 + "                   " + var0 + " NOTE:             : All options are optional " + var0 + " ---Examples--- " + var0 + "  -mode silent -type domain" + var0 + "                   : Run in silent mode, with no responses provided, " + var0 + "                     against execution plan for domain upgrade" + var0 + "  -mode gui -type domain" + var0 + "                   : Same as above, but run the GUI" + var0 + "";
      System.out.println(var1);
   }

   private static void log(Object var0) {
      if (DEBUG) {
         if (var0 != null && var0 instanceof Throwable) {
            ((Throwable)var0).printStackTrace();
         } else {
            System.out.println(var0);
         }
      }

   }

   private static boolean resourceExists(String var0) {
      boolean var1 = false;
      URL var2 = getCL().getResource(var0);
      if (var2 != null) {
         var1 = true;
      } else {
         File var3 = new File(var0);
         if (var3.exists()) {
            var1 = true;
         }
      }

      return var1;
   }

   private static ClassLoader getCL() {
      return Main.class.getClassLoader();
   }

   private static void redirectOutErr(String var0) throws Exception {
      File var1 = new File(var0);
      var1.createNewFile();
      var1 = var1.getAbsoluteFile().getCanonicalFile();
      System.setProperty(REDIRECTED_OUTPUT_FILE_KEY, var1.getPath());
      FileOutputStream var2 = new FileOutputStream(var0, true);
      final PrintStream var3 = new PrintStream(var2);
      System.setOut(var3);
      System.setErr(var3);
      Runtime.getRuntime().addShutdownHook(new Thread() {
         public void run() {
            try {
               var3.close();
            } catch (Exception var2) {
            }

         }
      });
   }

   private static class InvalidMainException extends IllegalArgumentException {
      public InvalidMainException(String var1) {
         super(var1);
      }
   }
}
