package weblogic.management.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.channels.FileLock;
import java.security.AccessController;
import java.util.Properties;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.bootstrap.BootStrapConstants;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.PDevHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class DomainDirectoryService extends AbstractServerService implements BootStrapConstants {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private static final boolean nodeManagerBoot = Boolean.getBoolean("weblogic.system.NodeManagerBoot");
   private static final String startmode = System.getProperty("weblogic.management.startmode");
   private static FileLock serverLock;
   private static ManagementTextTextFormatter mgmtTextFormatter = ManagementTextTextFormatter.getInstance();
   private boolean resumeInvokedFirstTime = true;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void start() throws ServiceFailureException {
      try {
         this.ensureDomainExists();
         ServerLocks.getServerLock();
      } catch (ManagementException var2) {
         throw new ServiceFailureException(var2.getMessage(), var2);
      } catch (Exception var3) {
         throw new ServiceFailureException(var3.getMessage());
      }
   }

   public void stop() throws ServiceFailureException {
      ServerLocks.releaseServerLock();
   }

   public void halt() throws ServiceFailureException {
      this.stop();
   }

   private void ensureDomainExists() throws ManagementException {
      if (isCreateNeeded()) {
         String var1 = BootStrap.getDomainName();
         String var2 = BootStrap.getServerName();
         if (var1 == null) {
            var1 = "mydomain";
         }

         if (var2 == null) {
            var2 = "myserver";
         }

         File var3 = new File(DomainDir.getConfigDir());
         File var4 = new File(var3, "config.xml");
         String var5 = var4.getAbsolutePath();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("configFile: " + var4);
            debugLogger.debug("domainName: " + var1);
            debugLogger.debug("serverName: " + var2);
            debugLogger.debug("PropertyService: " + ManagementService.getPropertyService(kernelId));
         }

         if (nodeManagerBoot || "WinSvc".equalsIgnoreCase(startmode)) {
            String var9 = mgmtTextFormatter.failedToLocateConfigFile(var5);
            throw new ConfigurationException(var9);
         }

         boolean var6 = willGenerateConfigBasedOnProps() || willGenerateConfigInteractively(var4);
         if (!var6) {
            String var7 = mgmtTextFormatter.failedToLocateConfigFile(var5);
            String var8 = mgmtTextFormatter.noConfigFileWillNotGenerate(var7, "weblogic.management.GenerateDefaultConfig");
            throw new InteractiveConfigurationException(var8);
         }

         this.logNoConfig(var5);
         this.generateDomain(var1, var2);
      }

   }

   private void generateDomain(String var1, String var2) throws ManagementException {
      try {
         Class var3 = null;
         ClassLoader var4 = Thread.currentThread().getContextClassLoader();

         String var6;
         try {
            ClassLoader var5 = PDevHelper.getPDevClassLoader(this.getClass().getClassLoader());
            var3 = Class.forName("com.oracle.cie.domain.DomainInfoHelper", true, var5);
         } catch (ClassNotFoundException var21) {
            var6 = mgmtTextFormatter.unAvailableConfigWizardCompoment();
            throw new ManagementException(var6);
         } finally {
            Thread.currentThread().setContextClassLoader(var4);
         }

         ManagementService.getPropertyService(kernelId).initializeSecurityProperties(true);
         String var24 = System.getProperty("weblogic.ListenAddress");
         var6 = System.getProperty("weblogic.ListenPort");
         String var7 = ManagementService.getPropertyService(kernelId).getTimestamp1();
         String var8 = ManagementService.getPropertyService(kernelId).getTimestamp2();
         Properties var9 = new Properties();
         var9.put("DomainName", var1);
         var9.put("ServerName", var2);
         if (var24 != null) {
            var9.put("ListenAddress", var24);
         }

         if (var6 != null) {
            var9.put("ListenPort", var6);
         }

         var9.put("domain.OverwriteDomain", "true");
         String var10 = DomainDir.getRootDir();
         if (var10 == null) {
            var10 = (new File(".")).getAbsolutePath();
         } else {
            var10 = (new File(var10)).getAbsolutePath();
         }

         ManagementLogger.logGeneratingDomainDirectory(var10);
         long var11 = System.currentTimeMillis();
         Class[] var13 = new Class[]{String.class, String.class, String.class, Properties.class};
         Method var14 = var3.getMethod("createDefaultDomain", var13);
         Object[] var15 = new Object[]{var10, var7, var8, var9};
         var14.invoke(var3.newInstance(), var15);
         long var16 = System.currentTimeMillis();
         ManagementLogger.logDomainDirectoryGenerationComplete(var16 - var11);
      } catch (Exception var23) {
         throw new ManagementException("Failure during domain creation", var23);
      }
   }

   private void logNoConfig(String var1) {
      String var2 = mgmtTextFormatter.failedToLocateConfigFile(var1);
      ManagementLogger.logFailedToFindConfig(var2);
   }

   private static boolean willGenerateConfigInteractively(File var0) {
      if (willGenerateConfigBasedOnProps()) {
         return true;
      } else {
         String var1 = mgmtTextFormatter.getAffirmitaveGenerateConfigText();
         String var2 = mgmtTextFormatter.getNegativeGenerateConfigText();
         int var3 = 1;

         String var4;
         try {
            var4 = var0.getCanonicalPath();
         } catch (IOException var8) {
            var4 = var0.getAbsolutePath();
         }

         System.out.println("\n" + mgmtTextFormatter.failedToLocateConfigFile(var4));

         while(var3 < 4) {
            String var5 = null;

            try {
               BufferedReader var6 = new BufferedReader(new InputStreamReader(System.in));
               System.out.print("\n" + mgmtTextFormatter.getGenerateDefaultConfigInteractively(var1, var2) + ": ");
               var5 = var6.readLine();
               if (var5 != null && var5.equalsIgnoreCase(var1)) {
                  return true;
               }

               if (var5 != null && var5.equalsIgnoreCase(var2)) {
                  return false;
               }

               System.out.println("\n" + mgmtTextFormatter.getPleaseConfirmDeny(var1, var2));
               ++var3;
            } catch (Exception var7) {
               ++var3;
               debugLogger.debug("Unexpected Exception: " + var7, var7);
            }
         }

         return false;
      }
   }

   private static boolean willGenerateConfigBasedOnProps() {
      if (System.getProperty("gdc") != null) {
         return true;
      } else {
         return System.getProperty("weblogic.management.GenerateDefaultConfig") != null;
      }
   }

   public static boolean isCreateNeeded() throws ManagementException {
      return isCreateNeeded(new File(DomainDir.getRootDir()));
   }

   public static boolean isCreateNeeded(File var0) throws ManagementException {
      if (!ManagementService.getPropertyService(kernelId).isAdminServer()) {
         return false;
      } else {
         File var1 = new File(var0, "config");
         File var2 = new File(var0, BootStrap.getConfigFileName());
         File var3 = new File(var1, "config.xml");
         File var4 = findParentConfig(var0);
         if (!var2.exists() && !var3.exists()) {
            if (var4 != null && var4.exists()) {
               ConfigLogger.logConfigXMLFoundInParentDir(var4.getAbsolutePath());
            }

            return true;
         } else {
            return false;
         }
      }
   }

   private static File findParentConfig(File var0) {
      File var1 = null;
      String var2 = var0.getParent();
      if (var2 != null) {
         var1 = new File(var2, "config.xml");
      }

      return var1;
   }
}
