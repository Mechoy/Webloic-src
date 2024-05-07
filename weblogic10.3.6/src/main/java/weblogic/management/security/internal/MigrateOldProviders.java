package weblogic.management.security.internal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import weblogic.Home;
import weblogic.management.commo.JarFile;
import weblogic.management.commo.WebLogicMBeanMaker;
import weblogic.management.scripting.utils.WLSTProcess;
import weblogic.utils.FileUtils;
import weblogic.utils.JavaExec;
import weblogic.utils.StringUtils;
import weblogic.utils.jars.JarFileUtils;

public class MigrateOldProviders {
   static String LIB_DIR = "/server/lib";
   static String TYPES_DIR;
   static String TYPES_DIR_WO_SERVER;
   static String COMMO_DTD;
   static String beaHome;
   static String newBeaHome;
   static String typesDir;
   static int counter;
   static String OLD_TEMP_FILES;
   static String NEW_TEMP_FILES;
   static String newJarName;
   static String oldJarName;
   static String WL_MANAGEMENT;
   static String WL_MANAGEMENT_MBEAN;
   static String WL_SECURITY_PROVIDERS;
   static String WL_MANAGEMENT_SOURCE_IMPL;
   static String WL_MEDREC_SAMPLE;
   static String WL_AI;
   static String WLI_PLUGIN_MGR;
   static String WL_RDBMS_ATN;
   static String WL_WSRP_SP;
   static String TEMP_MDF_DIR;
   static String TEMP_TEMP_MDF_DIR;
   static File mdfDir;
   static File tempMDFDir;
   static File oldTempFilesDir;
   static File newTempFilesDir;
   static File classesDir;
   static String MIGRATED;
   static boolean verbose;
   static List providerJars;
   static List oldProviderJars;
   static List existingProviderJars;
   static boolean migrationDone;
   static boolean argsSatisfied;
   static boolean cleanup;
   static List messages;
   static String BINDER_SUFFIX;
   static String INFO_SUFFIX;
   static String XSD_SUFFIX;
   static String XSB_SUFFIX;
   private static boolean invokedFromInstaller;
   private static String weblogicJarPath;
   private static String toolsJarPath;
   private static String wlMgtMBeanJarPath;
   private static String wlMgmtMBeanImplJarPath;
   private static String antJarPath;
   private static String xbeanJarPath;
   private static String bindingJarPath;

   public static void main(String[] var0) throws Exception {
      run(var0);
   }

   public static List run(String[] var0) throws Exception {
      processArgs(var0);
      if (!argsSatisfied) {
         return messages;
      } else {
         try {
            execute();
            migrationDone = true;
         } finally {
            if (migrationDone) {
               cleanup();
               copyJars();
            } else {
               cleanup();
            }

         }

         return messages;
      }
   }

   public static boolean isInternalProvider(String var0) {
      return var0.equals(WL_MANAGEMENT) || var0.equals(WL_SECURITY_PROVIDERS) || var0.equals(WL_MANAGEMENT_SOURCE_IMPL) || var0.equals(WL_MANAGEMENT_MBEAN) || var0.equals(WL_AI) || var0.equals(WLI_PLUGIN_MGR) || var0.equals(WL_WSRP_SP) || var0.equals(WL_MEDREC_SAMPLE) || var0.equals(WL_RDBMS_ATN);
   }

   static void initVariables(String[] var0) {
      beaHome = var0[0];
      typesDir = beaHome + TYPES_DIR;
      if (!(new File(typesDir)).exists()) {
         typesDir = beaHome;
      }

      argsSatisfied = true;
   }

   public static String installerUpgradeSecurityProvider(String var0, String var1, String var2) throws Exception {
      String[] var3 = new String[]{var0, var1};
      invokedFromInstaller = true;
      weblogicJarPath = var1 + "/server/lib/weblogic.jar";
      toolsJarPath = var2;
      wlMgtMBeanJarPath = var1 + "/server/lib/mbeantypes/wlManagementMBean.jar";
      wlMgmtMBeanImplJarPath = var1 + "/server/lib/mbeantypes/wlManagementImplSource.jar";
      antJarPath = var1 + "/server/lib/ant/ant.jar";
      xbeanJarPath = var1 + "/server/lib/xbean.jar";
      bindingJarPath = var1 + "/server/lib/schema/weblogic-domain-binding.jar";
      run(var3);
      String[] var4 = new String[messages.size()];
      return StringUtils.join((String[])((String[])messages.toArray(var4)), "\n");
   }

   static void processArgs(String[] var0) throws Exception {
      if (var0.length == 0) {
         var0 = new String[0];
         var0[0] = Home.getFile().getParent();
      }

      if (var0.length > 3) {
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logIncorrectArgs();
         }

         printUsage();
      } else {
         if (var0.length == 1) {
            if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logMigratingOldProvidersFrom1Arg(var0[0]);
            }

            initVariables(var0);
         } else if (var0.length == 2) {
            initVariables(var0);
            if (var0[1].equalsIgnoreCase("-verbose")) {
               verbose = true;
            } else {
               newBeaHome = var0[1];
            }

            if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logMigratingOldProvidersFrom1Arg(var0[0]);
            }
         } else {
            if (var0.length != 3) {
               printUsage();
               return;
            }

            if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logMigratingOldProvidersFrom1Arg(var0[0]);
            }

            initVariables(var0);
            newBeaHome = var0[1];
            if (var0[2].equalsIgnoreCase("-verbose")) {
               verbose = true;
            }
         }

         populateExistingProviderJars();
         String var1 = System.getProperty("weblogic.Installing", "false");
         if (var1.toLowerCase(Locale.US).equals("true")) {
            printDebug("Invoked while installing hence upgrade of OOTB providers will be skipped");
         }

      }
   }

   private static void populateExistingProviderJars() {
      File var0 = null;
      if (newBeaHome != null) {
         var0 = new File(newBeaHome);
         File var1 = new File(newBeaHome + "/server/lib/mbeantypes");
         File var2 = new File(newBeaHome + "/server/lib/compatibility");
         if (var1.exists() && var2.exists()) {
            var0 = new File(newBeaHome + "/server/lib/mbeantypes");
         }
      } else {
         StringBuilder var10002 = new StringBuilder();
         Home.getHome();
         var0 = new File(var10002.append(Home.getPath()).append(File.separator).append(TYPES_DIR_WO_SERVER).toString());
      }

      File[] var3 = var0.listFiles();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            existingProviderJars.add(var3[var4].getName());
         }
      }

   }

   private static void printUsage() {
      System.out.println("Usage: java weblogic.management.security.internal.MigrateOldProviders <OLD_BEA_HOME_DIR> <NEW_BEA_HOME_DIR>");
      System.out.println("OR");
      System.out.println("Usage: java weblogic.management.security.internal.MigrateOldProviders <NEW_BEA_HOME_DIR>");
   }

   private static void copyJars() throws IOException {
      SecurityProviderUpgradeTextTextFormatter var0 = new SecurityProviderUpgradeTextTextFormatter();
      if (providerJars.isEmpty()) {
         printDebug(var0.NoJarsUpgraded());
         messages.add(var0.NoJarsUpgraded());
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logNoJarsUpgraded();
         }

      } else {
         Iterator var1 = providerJars.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            File var3 = new File(var2);
            File var4 = null;
            if (newBeaHome != null) {
               if ((new File(newBeaHome + File.separator + TYPES_DIR)).exists()) {
                  var4 = new File(newBeaHome + File.separator + TYPES_DIR, var3.getName());
               } else {
                  var4 = new File(newBeaHome, var3.getName());
               }
            } else {
               StringBuilder var10002 = new StringBuilder();
               Home.getHome();
               var4 = new File(var10002.append(Home.getPath()).append(File.separator).append(TYPES_DIR_WO_SERVER).toString(), var3.getName());
            }

            printDebug("Moving " + var3.getAbsolutePath() + " to " + var4.getAbsolutePath());
            if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logCopyProviderTo(var4.getAbsolutePath());
            }

            if (!var3.getAbsoluteFile().equals(var4.getAbsoluteFile())) {
               FileUtils.copy(var3, var4);
               FileUtils.remove(var3);
            }
         }

         printDebug(var0.completedUpgradeOf(providerJars.size()));
         messages.add(var0.completedUpgradeOf(providerJars.size()));
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logCompletedJars(providerJars.size());
         }

      }
   }

   public static boolean verifyOldJar(String var0) throws Exception {
      printDebug("Verifying if " + var0 + " is an old provider jar");
      boolean var1 = true;
      JarFile var2 = new JarFile(new File(typesDir + File.separator + var0));
      Enumeration var3 = var2.getEntries();
      int var4 = 0;

      while(var3.hasMoreElements()) {
         ZipEntry var5 = (ZipEntry)var3.nextElement();
         if (var5.getName().endsWith(BINDER_SUFFIX) || var5.getName().endsWith(INFO_SUFFIX) || var5.getName().endsWith(XSD_SUFFIX)) {
            ++var4;
         }

         if (var4 == 3) {
            var1 = false;
            break;
         }
      }

      if (var1) {
         printDebug("Yes " + var0 + " is a valid old security provider jar");
      } else {
         printDebug("Not an Old security jar, its new!!, will skip this file ... ");
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logSkippedCount(var0);
         }
      }

      return var1;
   }

   static void execute() throws Exception {
      printDebug("The types dir is " + typesDir);
      String[] var0 = (new File(typesDir)).list();
      if (var0 != null) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (invokedFromInstaller && isInternalProvider(var0[var1])) {
               if (!invokedFromInstaller) {
                  SecurityProviderUpgradeLogger.logSkippingJar(var0[var1]);
               }
            } else if (!var0[var1].equals(WL_MANAGEMENT) && !var0[var1].equals(WL_SECURITY_PROVIDERS) && !var0[var1].equals(WL_MANAGEMENT_SOURCE_IMPL) && !var0[var1].equals(WL_MANAGEMENT_MBEAN) && var0[var1].endsWith(".jar") && !var0[var1].endsWith("Upgraded.jar") && !var0[var1].startsWith("SecUpgrade_")) {
               String var2 = var0[var1].substring(0, var0[var1].length() - 4);
               if (!existingProviderJars.contains(var2 + ".jar") && !existingProviderJars.contains(var2 + "_Upgraded.jar")) {
                  newJarName = var0[var1];
                  if (!invokedFromInstaller) {
                     SecurityProviderUpgradeLogger.logNowProcessing(var0[var1]);
                  }

                  cleanup();
                  ++counter;
                  OLD_TEMP_FILES = "SecUpgrade_" + counter + "_oldTempFiles";
                  NEW_TEMP_FILES = "SecUpgrade_" + counter + "_newTempFiles";
                  TEMP_MDF_DIR = "SecUpgrade_" + counter + "mdfDir";
                  TEMP_TEMP_MDF_DIR = "SecUpgrade_" + counter + "tempMDFDir";
                  mdfDir = new File(TEMP_MDF_DIR);
                  if (mdfDir.exists()) {
                     FileUtils.remove(mdfDir);
                  }

                  mdfDir.mkdir();
                  oldTempFilesDir = new File(OLD_TEMP_FILES);
                  if (oldTempFilesDir.exists()) {
                     FileUtils.remove(oldTempFilesDir);
                  }

                  oldTempFilesDir.mkdir();
                  newTempFilesDir = new File(NEW_TEMP_FILES);
                  if (newTempFilesDir.exists()) {
                     FileUtils.remove(newTempFilesDir);
                  }

                  newTempFilesDir.mkdir();
                  classesDir = new File(newTempFilesDir.getAbsolutePath() + "ClassesDir");
                  Thread.sleep(5000L);
                  if (verifyOldJar(var0[var1]) && generateMDF(var0[var1])) {
                     try {
                        runWLMaker();
                        oldProviderJars.add(new File(typesDir, var0[var1]));
                     } catch (Exception var4) {
                        if (!invokedFromInstaller) {
                           System.out.println("Could not convert from MDF " + var0[var1] + ". This file " + "may not be a valid MDF file " + var4);
                           SecurityProviderUpgradeLogger.logCannotConvert(var0[var1]);
                        }
                     }
                  }
               } else if (!invokedFromInstaller) {
                  SecurityProviderUpgradeLogger.logSkippingJar(var0[var1]);
               }
            } else if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logSkippingJar(var0[var1]);
            }
         }

      }
   }

   private static boolean isValidMDF(File var0) throws Exception {
      try {
         printDebug("checking xml file " + var0.getAbsolutePath());
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         Document var3 = var2.parse(var0);
         DocumentType var4 = var3.getDoctype();
         return var4 != null && var4.getSystemId().equals("commo.dtd");
      } catch (Exception var5) {
         if (verbose) {
            var5.printStackTrace();
         }

         return false;
      }
   }

   private static void getFileNames(List var0, String var1, String var2, String var3, boolean var4) {
      String var5 = null;
      if (var2 == null) {
         var5 = var1;
      } else {
         var5 = var1 + File.separator + var2;
      }

      File var6 = new File(var5);
      String[] var7 = var6.list();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         String var9 = null;
         if (var2 != null) {
            var9 = var2 + File.separator + var7[var8];
         } else {
            var9 = var7[var8];
         }

         File var10 = new File(var1 + File.separator + var9);
         if (var10.isDirectory()) {
            getFileNames(var0, var1, var9, var3, var4);
         } else if (var10.getName().endsWith(var3)) {
            if (!var4) {
               var0.add(var9);
            } else {
               var0.add(var10);
            }
         }
      }

   }

   private static boolean generateMDF(String var0) throws Exception {
      SecurityProviderUpgradeTextTextFormatter var1 = new SecurityProviderUpgradeTextTextFormatter();
      JarFileUtils.extract(new File(typesDir + File.separator + var0), oldTempFilesDir);
      ArrayList var2 = new ArrayList();
      getFileNames(var2, oldTempFilesDir.getAbsolutePath(), (String)null, ".xml", false);
      String[] var3 = new String[var2.size()];
      tempMDFDir = new File("./" + TEMP_TEMP_MDF_DIR);
      tempMDFDir.mkdir();
      mdfDir = new File(TEMP_MDF_DIR);
      mdfDir.mkdir();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         File var6 = new File(oldTempFilesDir.getAbsolutePath() + "/" + var5);
         FileUtils.copy(var6, tempMDFDir);
      }

      copyCommoDTD(tempMDFDir);
      File[] var9 = tempMDFDir.listFiles();
      if (var9.length == 1) {
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logNoMDF(var0);
         }

         messages.add(var1.NoMDFs(var0));
         FileUtils.remove(oldTempFilesDir);
         FileUtils.remove(tempMDFDir);
         return false;
      } else {
         byte var10 = 0;

         for(int var7 = 0; var7 < var9.length; ++var7) {
            var3[var10] = var9[var7].getAbsolutePath();
            if (var3[var10].endsWith(".xml")) {
               File var8 = new File(var3[var10]);
               if (!isValidMDF(var8)) {
                  if (!invokedFromInstaller) {
                     SecurityProviderUpgradeLogger.logInvalidMDF(var8.getAbsolutePath());
                  }
               } else {
                  printDebug("Copying " + var3[var10] + " to " + mdfDir.getAbsolutePath());
                  FileUtils.copy(var8, mdfDir);
               }
            }
         }

         mdfDir.mkdir();
         copyCommoDTD((File)null);
         FileUtils.remove(tempMDFDir);
         return true;
      }
   }

   private static void copyCommoDTD(File var0) throws Exception {
      InputStream var1 = null;
      printDebug("Getting commo.dtd from resource as stream ... ");
      var1 = MigrateOldProviders.class.getResourceAsStream("commo.dtd");
      File var2 = null;
      if (var0 == null) {
         var2 = new File(mdfDir, COMMO_DTD);
      } else {
         var2 = new File(var0, COMMO_DTD);
      }

      FileUtils.writeToFile(var1, var2);
      printDebug("copied sucessfully from input stream");
      printDebug("Copied the commo.dtd to " + var2.getAbsolutePath());
   }

   private static void runWLMaker() throws Exception {
      SecurityProviderUpgradeTextTextFormatter var0 = new SecurityProviderUpgradeTextTextFormatter();
      String var1 = newJarName;
      if (mdfDir.list().length == 1) {
         printDebug("No MDF's found in the Provider Jar " + var1 + ". Migration Failed!!");
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logNoMDF(var1);
         }

      } else {
         ArrayList var2 = new ArrayList();
         getFileNames(var2, oldTempFilesDir.getAbsolutePath(), (String)null, "Impl.class", true);
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            File var4 = (File)var3.next();
            printDebug("copying " + var4.getAbsolutePath() + " to " + newTempFilesDir.getAbsolutePath());
            FileUtils.copy(var4, newTempFilesDir);
         }

         String var15 = newTempFilesDir.getAbsolutePath();
         String var5 = "-doBeanGen";
         String[] var6 = new String[]{"-files", var15, "-MDFDIR", mdfDir.getAbsolutePath(), var5};
         printDebug("Running the first Phase of WebLogicMBeanMaker ... ");
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logRunningFirstPhase(var1);
         }

         printDebug("Copying from " + oldTempFilesDir.getAbsolutePath() + " to " + classesDir.getAbsolutePath());
         System.setProperty("weblogic.FromSPUpgrade", "true");
         WebLogicMBeanMaker.main(var6);
         FileUtils.copy(oldTempFilesDir, classesDir);
         if (newJarName.endsWith(".jar")) {
            newJarName = newJarName.substring(0, newJarName.length() - 4);
            newJarName = newJarName + MIGRATED;
         } else {
            newJarName = newJarName + MIGRATED;
         }

         String[] var7 = new String[]{"-files", quoteIfContainsSpaces(newTempFilesDir.getAbsolutePath()), "-mjf", newJarName, var5};
         String var8 = StringUtils.join(var7, " ");
         if (!invokedFromInstaller) {
            SecurityProviderUpgradeLogger.logRunningSecondPhase(var1);
         }

         printDebug("Running the second Phase of WebLogicMBeanMaker ... ");
         JavaExec var9 = JavaExec.createCommand("weblogic.management.commo.WebLogicMBeanMaker " + var8);
         String var10 = System.getProperty("weblogic.DebugMigrateProviders", "false");
         var9.addDefaultClassPath();
         var9.addClassPath(classesDir);
         if (invokedFromInstaller) {
            var9.addClassPath(new File(weblogicJarPath));
            var9.addClassPath(new File(toolsJarPath));
            var9.addClassPath(new File(antJarPath));
            var9.addClassPath(new File(xbeanJarPath));
            var9.addSystemProp("weblogic.SPUpgrade.FromInstaller", "true");
            var9.addSystemProp("weblogic.SPUpgrade.MBeanJarPath", wlMgtMBeanJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.MBeanImplJarPath", wlMgmtMBeanImplJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.BindingJarPath", bindingJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.AntJarPath", antJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.XbeanJarPath", xbeanJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.WLSJarPath", weblogicJarPath);
            var9.addSystemProp("weblogic.SPUpgrade.ToolsJarPath", toolsJarPath);
         }

         String var11 = System.getProperty("targetNameSpace");
         if (var11 != null) {
            var9.addSystemProp("targetNameSpace", var11);
         }

         String var12 = System.getProperty("com.sun.xml.namespace.QName.useCompatibleSerialVersionUID");
         if (var12 != null) {
            var9.addSystemProp("com.sun.xml.namespace.QName.useCompatibleSerialVersionUID", var12);
         }

         Process var13 = var9.getProcess();
         boolean var14 = false;
         if (var10.toLowerCase(Locale.US).equals("true")) {
            var14 = true;
         }

         WLSTProcess.startIOThreads(var13, "MigrateProviders - SubProcess: ", var14);
         if (var13.waitFor() != 0) {
            System.out.println("Code generation failed. Use -Dweblogic.DebugMigrateProviders=true and rerun the provider migration to see the real error");
            cleanup();
         } else {
            printDebug("Created new Security provider jar " + newJarName + " from " + var1);
            messages.add(var0.createdNew(newJarName, var1));
            if (!invokedFromInstaller) {
               SecurityProviderUpgradeLogger.logNewFromOld(newJarName, var1);
            }

            providerJars.add(newJarName);
         }
      }
   }

   public static void cleanup() {
      if (cleanup) {
         printDebug("Cleaning up all the temporary files that are created ");
         File[] var0 = (new File(".")).listFiles();

         for(int var1 = 0; var1 < var0.length; ++var1) {
            if (var0[var1].getName().indexOf("SecUpgrade") != -1) {
               FileUtils.remove(var0[var1]);
            }
         }

         FileUtils.remove(new File("velocity.log"));
         File var2 = new File("./tempFileDirForSchema");
         FileUtils.remove(var2);
      }
   }

   private static String quoteIfContainsSpaces(String var0) {
      return var0 != null && var0.indexOf(" ") != -1 ? '"' + var0 + '"' : var0;
   }

   static void printDebug(String var0) {
      if (verbose) {
         System.out.println("<ProviderMigration> " + var0);
      }

   }

   static {
      TYPES_DIR = LIB_DIR + "/mbeantypes";
      TYPES_DIR_WO_SERVER = "lib" + File.separator + "mbeantypes";
      COMMO_DTD = "commo.dtd";
      beaHome = "";
      newBeaHome = null;
      typesDir = "";
      counter = 0;
      OLD_TEMP_FILES = "SecUpgrade_oldTempFiles";
      NEW_TEMP_FILES = "SecUpgrade_newTempFiles";
      newJarName = "";
      oldJarName = "";
      WL_MANAGEMENT = "wlManagement.jar";
      WL_MANAGEMENT_MBEAN = "wlManagementMBean.jar";
      WL_SECURITY_PROVIDERS = "wlSecurityProviders.jar";
      WL_MANAGEMENT_SOURCE_IMPL = "wlManagementImplSource.jar";
      WL_MEDREC_SAMPLE = "wlMedRecSampleAuthProvider.jar";
      WL_AI = "wlai-mbean.jar";
      WLI_PLUGIN_MGR = "wli-plugin-mgr-mbean.jar";
      WL_RDBMS_ATN = "rdbmsAtnProvider.jar";
      WL_WSRP_SP = "wsrp-security-providers.jar";
      TEMP_MDF_DIR = "SecUpgrade_mdfDir";
      TEMP_TEMP_MDF_DIR = "SecUpgrade_tempMDFDir";
      mdfDir = null;
      tempMDFDir = null;
      oldTempFilesDir = null;
      newTempFilesDir = null;
      classesDir = null;
      MIGRATED = "_Upgraded.jar";
      verbose = false;
      providerJars = new ArrayList();
      oldProviderJars = new ArrayList();
      existingProviderJars = new ArrayList();
      migrationDone = false;
      argsSatisfied = false;
      cleanup = true;
      messages = new ArrayList();
      BINDER_SUFFIX = "MBeanBinder.class";
      INFO_SUFFIX = "MBeanImplBeanInfo.class";
      XSD_SUFFIX = ".xsd";
      XSB_SUFFIX = ".xsd";
      invokedFromInstaller = false;
      weblogicJarPath = null;
      toolsJarPath = null;
      wlMgtMBeanJarPath = null;
      wlMgmtMBeanImplJarPath = null;
      antJarPath = null;
      xbeanJarPath = null;
      bindingJarPath = null;
   }
}
