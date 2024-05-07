package weblogic.deploy.api.tools;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryLoggingUtils;
import weblogic.deploy.api.internal.PlanGenTextFormatter;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;

public class PlanGenerator {
   private static final String HELP = "help";
   private static final String DEBUG = "debug";
   private static final String PLAN = "plan";
   private static final String USEPLAN = "useplan";
   private static final String INSTALLROOT = "root";
   private static final String VARIABLES = "variables";
   private static final String DEPENDENCIES = "dependencies";
   private static final String DECLARATIONS = "declarations";
   private static final String CONFIGURABLES = "configurables";
   private static final String DYNAMIC = "dynamics";
   private static final String ALL = "all";
   private static final String NONE = "none";
   private static final String LIBRARY = "library";
   private static final String LIBRARYDIR = "librarydir";
   private static PlanGenTextFormatter cat = PlanGenTextFormatter.getInstance();
   private static boolean debug = false;
   private SessionHelper helper;
   private WebLogicDeploymentManager dm;
   private File app;
   private File plan = null;
   private WebLogicDeploymentConfiguration dc;
   private int exportArg = 0;
   private boolean globalVars = false;
   private static int EXPORT_NONE = -1;
   private Getopt2 opts;
   private static final String DEFAULT_PLAN = "plan.xml";
   private static final String NOEXIT = "noexit";
   private Set libraries = null;

   private PlanGenerator(Getopt2 var1) {
      this.opts = var1;
   }

   private void run() throws Exception {
      this.dm = SessionHelper.getDisconnectedDeploymentManager();
      this.helper = SessionHelper.getInstance(this.dm);
      this.setVariables();
      this.setExport();
      this.setRoot();
      this.setApp();
      this.setUsePlan();
      this.setPlan();
      this.setLibraries();
      this.setLibrarDirs();
      this.registerLibraries();
      this.showInputs();
      this.helper.initializeConfiguration();
      this.dc = this.helper.getConfiguration();
      this.dc.getPlan().setGlobalVariables(this.globalVars);
      if (this.exportArg != EXPORT_NONE) {
         System.out.println(cat.exporting());
         this.dc.export(this.exportArg);
      }

   }

   private void registerLibraries() {
      if (this.libraries != null) {
         Iterator var1 = this.libraries.iterator();

         while(var1.hasNext()) {
            LibraryData var2 = (LibraryData)var1.next();
            this.helper.registerLibrary(var2.getLocation(), var2.getName(), var2.getSpecificationVersion() == null ? null : var2.getSpecificationVersion().toString(), var2.getImplementationVersion());
         }

      }
   }

   private void setLibrarDirs() {
      if (this.opts.hasOption("librarydir")) {
         File var1 = new File(this.opts.getOption("librarydir"));

         try {
            LibraryLoggingUtils.checkLibdirIsValid(var1);
         } catch (LoggableLibraryProcessingException var4) {
            throw new IllegalArgumentException(var4);
         }

         File[] var2 = var1.listFiles();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.addLibrary(LibraryData.newEmptyInstance(var2[var3]));
         }

      }
   }

   private void setLibraries() {
      if (this.opts.hasOption("library")) {
         String[] var1 = StringUtils.splitCompletely(this.opts.getOption("library"), ",");

         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2];
            String var4 = var1[var2];
            int var5 = var1[var2].indexOf("@");
            if (var5 > -1) {
               var3 = var1[var2].substring(0, var5);
               var4 = var1[var2].substring(var5);
            }

            File var6 = new File(var3);

            try {
               this.addLibrary(this.parseLibraryArg(var6, var4));
            } catch (LoggableLibraryProcessingException var8) {
               throw new IllegalArgumentException(var8);
            }
         }

      }
   }

   private void addLibrary(LibraryData var1) {
      if (this.libraries == null) {
         this.libraries = new HashSet();
      }

      this.libraries.add(var1);
   }

   private LibraryData parseLibraryArg(File var1, String var2) throws LoggableLibraryProcessingException {
      LibraryLoggingUtils.checkLibraryExists(var1);
      if (var2.indexOf("@") == -1) {
         return LibraryData.newEmptyInstance(var1);
      } else {
         String[] var3 = StringUtils.splitCompletely(var2, "@");
         String var4 = null;
         String var5 = null;
         String var6 = null;

         for(int var7 = 0; var7 < var3.length; ++var7) {
            if (var3[var7].indexOf("=") != -1) {
               String[] var8 = StringUtils.splitCompletely(var3[var7], "=");
               if (var8[0].equalsIgnoreCase("name")) {
                  var4 = var8[1];
               }

               if (var8[0].equalsIgnoreCase("libspecver")) {
                  var5 = var8[1];
               }

               if (var8[0].equalsIgnoreCase("libimplver")) {
                  var6 = var8[1];
               }
            }
         }

         return LibraryLoggingUtils.initLibraryData(var4, var5, var6, var1);
      }
   }

   private void setPlan() {
      if (this.opts.hasOption("plan")) {
         this.plan = new File(this.opts.getOption("plan"));
      } else {
         this.plan = this.helper.getPlan();
      }

      if (this.plan == null) {
         this.setImplicitPlan();
      }

      if (this.plan == null) {
         this.plan = new File("plan.xml");
      }

      if (this.plan.exists() && !this.opts.hasOption("useplan")) {
         this.helper.setPlan(this.plan);
      }

   }

   private void setImplicitPlan() {
      if (this.helper.getApplicationRoot() != null) {
         File var1 = new File(this.helper.getApplicationRoot(), "plan");
         if (!var1.exists()) {
            var1.mkdirs();
         }

         if (var1.isDirectory()) {
            this.plan = new File(var1, "plan.xml");
         }
      }

   }

   private void setUsePlan() {
      if (this.opts.hasOption("useplan")) {
         if (debug) {
            Debug.say(" UsePlan set. So, setting plan on helper to : " + this.opts.getOption("useplan"));
         }

         this.helper.setPlan(new File(this.opts.getOption("useplan")));
         if (debug) {
            Debug.say(" Plan on helper set to : " + this.helper.getPlan());
         }
      }

   }

   private void setApp() {
      if (this.opts.args().length > 0) {
         this.helper.setApplication(new File(this.opts.args()[0]));
      }

      this.app = this.helper.getApplication();
      if (this.app == null) {
         throw new IllegalArgumentException(cat.noApp());
      }
   }

   private void setRoot() {
      if (this.opts.hasOption("root")) {
         this.helper.setApplicationRoot(new File(this.opts.getOption("root")));
      }

   }

   private void showInputs() {
      if (this.helper.getPlan() != null) {
         System.out.println(cat.genningWithPlan(this.app.getPath(), this.helper.getPlan().getPath()));
      } else {
         System.out.println(cat.genning(this.app.getPath()));
      }

      System.out.println(cat.exportOptions(this.exportAsString(this.exportArg)));
      if (this.libraries != null) {
         System.out.println(cat.merging());
         Iterator var1 = this.libraries.iterator();

         while(var1.hasNext()) {
            LibraryData var2 = (LibraryData)var1.next();
            System.out.println(cat.libLocation(var2.getLocation().getPath()));
            System.out.println(cat.libName(var2.getName()));
            System.out.println(cat.libSpec(var2.getSpecificationVersion() == null ? null : var2.getSpecificationVersion().toString()));
            System.out.println(cat.libImpl(var2.getImplementationVersion()));
            System.out.println();
         }
      }

   }

   private String exportAsString(int var1) {
      switch (var1) {
         case 0:
            return "dependencies";
         case 1:
            return "declarations";
         case 2:
            return "configurables";
         case 3:
            return "all";
         case 4:
            return "dynamics";
         default:
            return "none";
      }
   }

   private void setExport() {
      if (this.opts.hasOption("dependencies")) {
         this.exportArg = 0;
      } else if (this.opts.hasOption("declarations")) {
         this.exportArg = 1;
      } else if (this.opts.hasOption("configurables")) {
         this.exportArg = 2;
      } else if (this.opts.hasOption("all")) {
         this.exportArg = 3;
      } else if (this.opts.hasOption("none")) {
         this.exportArg = EXPORT_NONE;
      } else if (this.opts.hasOption("dynamics")) {
         this.exportArg = 4;
      }

   }

   private void setVariables() {
      if (this.opts.hasOption("variables")) {
         String var1 = this.opts.getOption("variables");
         if ("global".equals(var1)) {
            this.globalVars = true;
         } else {
            if (!"unique".equals(var1)) {
               throw new IllegalArgumentException(cat.badVariables(var1));
            }

            this.globalVars = false;
         }
      }

   }

   private void save() throws IOException, ConfigurationException {
      System.out.println(cat.saving(this.plan.getCanonicalPath()));
      this.helper.setPlan(this.plan);
      this.helper.savePlan();
   }

   private void close() {
      if (this.helper != null) {
         this.helper.close();
      }

   }

   public static void main(String[] var0) throws Exception {
      Getopt2 var2 = new Getopt2();
      PlanGenerator var3 = null;
      boolean var4 = false;

      try {
         var2.addFlag("debug", cat.debug());
         var2.addFlag("noexit", cat.noexit());
         var2.addOption("plan", "myplan.xml", cat.plan());
         var2.addOption("useplan", "myplan.xml", cat.useplan());
         var2.addOption("root", "/weblogic/install/myapp", "application install root");
         var2.addFlag("dependencies", cat.dependencies());
         var2.addFlag("declarations", cat.declarations());
         var2.addFlag("configurables", cat.configurables());
         var2.addFlag("dynamics", cat.dynamics());
         var2.addFlag("all", cat.all());
         var2.addFlag("none", cat.none());
         var2.addOption("variables", "global", cat.variables());
         var2.addOption("library", "/mylibs/lib.ear@name=mylib,/mylibs/lib2.ear@name=otherlib@libspecver=1@libimplver=2", cat.libraries());
         var2.addOption("librarydir", "/mylibs", cat.libraryDir());
         var2.setUsageArgs(cat.application());
         var2.grok(var0);
         if (!var2.hasOption("help") && var0.length != 0) {
            if (var2.hasOption("debug")) {
               setDebug();
            }

            var3 = new PlanGenerator(var2);
            var3.run();
            var3.save();
            return;
         }

         usage(var2);
      } catch (Throwable var10) {
         if (var2.hasOption("noexit")) {
            if (var10 instanceof Exception) {
               throw (Exception)var10;
            }

            throw new Exception(var10);
         }

         if (debug) {
            var10.printStackTrace();
         } else {
            String var6 = var10.getMessage();
            if (var6 != null) {
               System.out.println(var10.getMessage());
            } else {
               var10.printStackTrace();
            }
         }

         var4 = true;
         return;
      } finally {
         if (var3 != null) {
            var3.close();
         }

         if (var4 && !var2.hasOption("noexit")) {
            System.exit(1);
         }

      }

   }

   private static void usage(Getopt2 var0) {
      var0.usageError("weblogic.PlanGenerator");
      System.out.println(cat.usage());
   }

   private static void setDebug() {
      debug = true;
      System.setProperty("weblogic.deployer.debug", "all");
   }
}
