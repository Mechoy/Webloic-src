package weblogic.ant.taskdefs.build;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import weblogic.application.library.Library;
import weblogic.application.library.LibraryInitializer;
import weblogic.application.library.LibraryManager;
import weblogic.application.library.LibraryProcessingException;
import weblogic.application.library.LibraryReference;
import weblogic.application.library.LoggableLibraryProcessingException;
import weblogic.application.utils.LibraryUtils;
import weblogic.servlet.utils.WebAppLibraryUtils;
import weblogic.utils.BadOptionException;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;

public final class BuildXMLGen extends Tool {
   public BuildXMLGen(String[] var1) {
      super(var1);
      this.setRequireExtraArgs(true);
   }

   public void prepare() {
      this.opts.addOption("projectName", "project name", "name of the ANT project.");
      this.opts.addOption("d", "directory", "directory where build.xml is created.  Default is the current directory.");
      this.opts.addOption("projectName", "project name", "name of the ANT project.");
      this.opts.addOption("file", "build.xml", "name of the generated build file.");
      this.opts.addOption("username", "username", "user name for deploy commands.");
      this.opts.addOption("password", "password", "password for the user.");
      this.opts.addOption("adminurl", "url", "Administatrion Server URL.");
      this.opts.addOption("librarydir", "directories", "Comma-separated list of directories in which to look for libraries.");
      this.opts.addFlag("verbose", "Enable Verbose output.");
      this.opts.setUsageArgs("<source directory>");
   }

   public boolean isVerbose() {
      return "true".equalsIgnoreCase(this.opts.getOption("verbose", "false"));
   }

   public String getUserName() {
      return this.opts.getOption("username", "USERNAME");
   }

   public String getPassword() {
      return this.opts.getOption("password", "PASSWORD");
   }

   public String getBuildFileName() {
      return this.opts.getOption("file", "build.xml");
   }

   private String[] getLibraryDirs() {
      String var1 = this.opts.getOption("librarydir");
      return var1 == null ? new String[0] : StringUtils.splitCompletely(var1, ",");
   }

   public void runBody() throws ToolFailureException {
      String[] var2 = this.opts.args();
      File var1 = new File(var2[0]);
      if (!var1.exists()) {
         throw new ToolFailureException("Source directory: " + var1.getAbsolutePath() + " does not exist");
      } else if (!var1.isDirectory()) {
         throw new ToolFailureException("Source directory: " + var1.getAbsolutePath() + " is not a directory");
      } else {
         try {
            if (this.opts.getOption("d") == null) {
               this.opts.setOption("d", (new File(".")).getAbsolutePath());
            }
         } catch (BadOptionException var12) {
         }

         LibraryInitializer var3 = null;

         try {
            String var4 = this.opts.getOption("projectName", var1.getCanonicalFile().getName());
            String var5 = this.opts.getOption("adminurl", "iiop://localhost:7001");
            BuildXMLGenerator var6 = new BuildXMLGenerator(this.opts, var4, var1, this.getUserName(), this.getPassword(), this.getBuildFileName(), var5);
            String var7 = var6.getRootDirectoryName();
            if (var7 == null) {
               var7 = ".";
            }

            this.errorIfBuildFileExists(var7);
            var3 = this.getLibraryInitializer();
            var6.setLibraryManager(this.initLibraryManager(var1));
            var6.generate(new Object());
            System.out.println("Generated " + var7 + File.separatorChar + this.getBuildFileName());
         } catch (Exception var13) {
            throw new ToolFailureException("Code Generation Error", var13);
         } finally {
            if (var3 != null) {
               var3.cleanup();
            }

         }

      }
   }

   private LibraryManager initLibraryManager(File var1) throws ToolFailureException {
      LibraryManager var2 = new LibraryManager(LibraryUtils.initAppReferencer());
      LibraryReference[] var3 = this.initAppLibRefs(var1);
      var2.lookup(var3);

      LibraryReference[] var4;
      try {
         var4 = WebAppLibraryUtils.initAllWebLibRefs(var1);
         var2.lookup(var4);
      } catch (ToolFailureException var6) {
         if (this.isVerbose()) {
            var6.printStackTrace();
         }

         System.out.println("Problem processing weblogic.xml, ignoring any library references it may contain.");
      }

      try {
         var4 = LibraryUtils.initAllOptPacks(var1);
         var2.lookup(var4);
      } catch (LibraryProcessingException var5) {
         if (this.isVerbose()) {
            var5.printStackTrace();
         }

         System.out.println("Problem processing META-INF/MANIFEST.MF, ignoring any optional packages it may contain.");
      }

      return var2;
   }

   private void errorIfBuildFileExists(String var1) throws ToolFailureException {
      File var2 = new File(var1, this.getBuildFileName());
      if (var2.exists()) {
         throw new ToolFailureException("Build File already exists, use -file to specify alternative build file name");
      }
   }

   private LibraryInitializer getLibraryInitializer() {
      LibraryInitializer var1 = new LibraryInitializer();
      if (this.isVerbose()) {
         var1.setVerbose();
      } else {
         var1.setSilent();
      }

      String[] var2 = this.getLibraryDirs();
      File var3 = null;

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3 = new File(var2[var4]);

         try {
            var1.registerLibdir(var3.getPath());
         } catch (LoggableLibraryProcessingException var6) {
            var6.getLoggable().log();
         }
      }

      return var1;
   }

   private LibraryReference[] initAppLibRefs(File var1) {
      try {
         return LibraryUtils.initLibRefs(var1);
      } catch (LibraryProcessingException var3) {
         if (this.isVerbose()) {
            var3.printStackTrace();
         }

         System.out.println("Problem processing weblogic-application.xml, ignoring any library references it may contain.");
         return null;
      }
   }

   public static void main(String[] var0) throws Exception {
      (new BuildXMLGen(var0)).run();
   }

   public static class BuildXMLGenerator extends CodeGenerator {
      private final String projectName;
      private final File srcDir;
      private final File[] componentDirs;
      private int componentIndex = 0;
      private final String userName;
      private final String password;
      private final String buildFileName;
      private final String adminurl;
      private final Set skipDirs = new HashSet();
      private Library[] libraries = new Library[0];
      private LibraryReference[] unresolvedRefs = new LibraryReference[0];
      private int libraryIndex = 0;
      private int unresolvedRefIndex = 0;
      private Collection allLibraryTargets = new LinkedHashSet();
      private boolean resolvedRef = true;

      public BuildXMLGenerator(Getopt2 var1, String var2, File var3, String var4, String var5, String var6, String var7) {
         super(var1);
         this.projectName = var2;
         this.srcDir = var3;
         this.userName = var4;
         this.password = var5;
         this.buildFileName = var6;
         this.adminurl = var7;
         this.componentDirs = var3.listFiles(new FileFilter() {
            public boolean accept(File var1) {
               return var1.isDirectory();
            }
         });
         this.skipDirs.add("META-INF");
         this.skipDirs.add("APP-INF");
      }

      private void setLibraryManager(LibraryManager var1) {
         HashSet var2 = new HashSet();
         var2.addAll(Arrays.asList(var1.getReferencedLibraries()));
         this.libraries = (Library[])((Library[])var2.toArray(new Library[var2.size()]));
         this.unresolvedRefs = var1.getUnresolvedReferences();
      }

      public String srcdir() {
         return this.srcDir.getPath();
      }

      public String adminurl() {
         return this.adminurl;
      }

      public String project_name() {
         return this.projectName;
      }

      public String generator() {
         return "weblogic.BuildXMLGen";
      }

      public String user() {
         return this.userName;
      }

      public String password() {
         return this.password;
      }

      public String build_components() throws CodeGenerationException {
         if (this.componentDirs != null && this.componentDirs.length != 0) {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < this.componentDirs.length; ++var2) {
               this.componentIndex = var2;
               if (!this.skipDirs.contains(this.component_name())) {
                  var1.append(this.parse(this.getProductionRule("build_component")));
               }
            }

            return var1.toString();
         } else {
            return "";
         }
      }

      public String redeploy_components() throws CodeGenerationException {
         if (this.componentDirs != null && this.componentDirs.length != 0) {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < this.componentDirs.length; ++var2) {
               this.componentIndex = var2;
               if (!this.skipDirs.contains(this.component_name())) {
                  var1.append(this.parse(this.getProductionRule("redeploy_component")));
               }
            }

            return var1.toString();
         } else {
            return "";
         }
      }

      public String component_name() {
         return this.componentDirs[this.componentIndex].getName();
      }

      public String target_name() {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.component_name());
         var1.append("@${servername}");
         return var1.toString();
      }

      private boolean hasLibraries() {
         return this.libraries.length > 0 || this.unresolvedRefs.length > 0;
      }

      public String nested_libraries() throws CodeGenerationException {
         StringBuffer var1 = new StringBuffer();
         if (this.hasLibraries()) {
            var1.append(">").append(System.getProperty("line.separator"));
         }

         if (this.libraries.length > 0) {
            var1.append(this.parse(this.getProductionRule("app_libraries_rule")));
         }

         if (this.unresolvedRefs.length > 0) {
            var1.append(this.parse(this.getProductionRule("unresolved_applib_refs_rule")));
         }

         return var1.toString();
      }

      public String wlcompile_end() {
         return this.hasLibraries() ? "    </wlcompile>" : " />";
      }

      public String appc_end() {
         return this.hasLibraries() ? "    </wlappc>" : " />";
      }

      public String app_libraries() throws CodeGenerationException {
         if (this.libraries.length == 0) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < this.libraries.length; ++var2) {
               this.libraryIndex = var2;
               var1.append(this.parse(this.getProductionRule("app_library")));
               this.trimRightHandSide(var1);
               if (var2 < this.libraries.length - 1) {
                  var1.append(System.getProperty("line.separator"));
               }
            }

            return var1.toString();
         }
      }

      public String unresolved_applib_refs() throws CodeGenerationException {
         if (this.unresolvedRefs.length == 0) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer();

            for(int var2 = 0; var2 < this.unresolvedRefs.length; ++var2) {
               this.unresolvedRefIndex = var2;
               var1.append(this.parse(this.getProductionRule("unresolved_applib_ref")));
               this.trimRightHandSide(var1);
               if (var2 < this.unresolvedRefs.length - 1) {
                  var1.append(System.getProperty("line.separator"));
               }
            }

            return var1.toString();
         }
      }

      public String unresolved_library_ref() {
         LibraryReference var1 = this.unresolvedRefs[this.unresolvedRefIndex];
         StringBuffer var2 = new StringBuffer(var1.getName());
         if (var1.getSpecificationVersion() != null) {
            var2.append("-").append(var1.getSpecificationVersion());
            if (var1.getImplementationVersion() != null) {
               var2.append("-").append(var1.getImplementationVersion());
            }
         }

         return var2.toString();
      }

      public String library_file() {
         return this.resolvedRef ? this.libraries[this.libraryIndex].getLocation().getPath() : this.unresolvedRefs[this.unresolvedRefIndex].getName();
      }

      public String library_name() {
         return this.resolvedRef ? this.libraries[this.libraryIndex].getName() : this.unresolvedRefs[this.unresolvedRefIndex].getName();
      }

      public String library_target_name() {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.library_name());
         if (this.getSpec() != null) {
            var1.append("-").append(this.getSpec());
         }

         if (this.getImpl() != null) {
            var1.append("-").append(this.getImpl());
         }

         if (this.resolvedRef) {
            this.allLibraryTargets.add(var1.toString());
         }

         return var1.toString();
      }

      public String libspecver() {
         return this.getSpec() == null ? "" : " libspecver=\"" + this.getSpec() + "\"";
      }

      public String libimplver() {
         return this.getImpl() == null ? "" : " libimplver=\"" + this.getImpl() + "\"";
      }

      private String getSpec() {
         return this.resolvedRef ? this.libraries[this.libraryIndex].getSpecificationVersion() : this.unresolvedRefs[this.unresolvedRefIndex].getSpecificationVersion();
      }

      private String getImpl() {
         return this.resolvedRef ? this.libraries[this.libraryIndex].getImplementationVersion() : this.unresolvedRefs[this.unresolvedRefIndex].getImplementationVersion();
      }

      public String deploy_libraries() throws CodeGenerationException {
         return this.generate_deployment_target("deploy");
      }

      public String generate_deployment_target(String var1) throws CodeGenerationException {
         if (!this.hasLibraries()) {
            return "";
         } else {
            StringBuffer var2 = new StringBuffer();
            int var3;
            if (this.libraries.length > 0) {
               this.resolvedRef = true;

               for(var3 = 0; var3 < this.libraries.length; ++var3) {
                  this.libraryIndex = var3;
                  var2.append(this.parse(this.getProductionRule(var1 + "_library")));
               }
            }

            if (this.unresolvedRefs.length > 0) {
               this.resolvedRef = false;

               for(var3 = 0; var3 < this.unresolvedRefs.length; ++var3) {
                  this.unresolvedRefIndex = var3;
                  var2.append(this.parse(this.getProductionRule(var1 + "_library_unresolved_ref")));
               }
            }

            return var2.toString();
         }
      }

      public String deploy_all_libraries() throws CodeGenerationException {
         return this.allLibraryTargets.isEmpty() ? "" : this.parse(this.getProductionRule("deploy_all_libraries_rule"));
      }

      public String all_libraries_deploy_targets() throws CodeGenerationException {
         if (this.allLibraryTargets.isEmpty()) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer();
            Iterator var2 = this.allLibraryTargets.iterator();

            while(var2.hasNext()) {
               var1.append("deploy.lib.").append(var2.next().toString());
               if (var2.hasNext()) {
                  var1.append(",");
               }
            }

            return var1.toString();
         }
      }

      public String undeploy_libraries() throws CodeGenerationException {
         return this.generate_deployment_target("undeploy");
      }

      public String undeploy_all_libraries() throws CodeGenerationException {
         return this.allLibraryTargets.isEmpty() ? "" : this.parse(this.getProductionRule("undeploy_all_libraries_rule"));
      }

      public String all_libraries_undeploy_targets() throws CodeGenerationException {
         if (this.allLibraryTargets.isEmpty()) {
            return "";
         } else {
            StringBuffer var1 = new StringBuffer();
            Iterator var2 = this.allLibraryTargets.iterator();

            while(var2.hasNext()) {
               var1.append("undeploy.lib.").append(var2.next().toString());
               if (var2.hasNext()) {
                  var1.append(",");
               }
            }

            return var1.toString();
         }
      }

      private void trimRightHandSide(StringBuffer var1) {
         int var2;
         for(var2 = var1.length() - 1; var2 >= 0 && Character.isWhitespace(var1.charAt(var2)); --var2) {
         }

         var1.delete(var2 + 1, var1.length());
      }

      protected Enumeration outputs(List var1) throws Exception {
         if (this.verboseCodegen) {
            Debug.say("outputs called");
         }

         BuildXMLGenOutput var2 = new BuildXMLGenOutput();
         var2.setTemplate("buildxml.j");
         var2.setOutputFile(this.buildFileName);
         Vector var3 = new Vector();
         var3.add(var2);
         return var3.elements();
      }

      public static class BuildXMLGenOutput extends CodeGenerator.Output {
      }
   }
}
