package weblogic.upgrade.domain;

import java.util.ArrayList;
import java.util.Enumeration;
import weblogic.version;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.ManagementLogger;
import weblogic.management.internal.ServerLocks;
import weblogic.management.upgrade.ConfigFileHelper;
import weblogic.management.utils.PDevHelper;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Executable;

public class DomainUpgradeServerService extends AbstractServerService {
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDomainUpgradeServerService");

   public void start() throws ServiceFailureException {
      try {
         if (ConfigFileHelper.isUpgradeNeeded()) {
            ManagementLogger.logDomainUpgrading(version.getReleaseBuildVersion());
            this.doUpgrade();
         }

      } catch (ConfigFileHelper.UpgradeNotWantedException var2) {
         throw new ServiceFailureException(var2.getMessage());
      } catch (ManagementException var3) {
         throw new ServiceFailureException(var3.getMessage(), var3);
      }
   }

   public void doUpgrade() throws ServiceFailureException {
      try {
         if (!Boolean.getBoolean("weblogic.ProductionModeEnabled")) {
            SerializedSystemIni.getEncryptionService();
         }

         ArrayList var1 = new ArrayList();
         var1.add(System.getProperty("java.home") + "/bin/java");
         Enumeration var2 = System.getProperties().propertyNames();

         while(true) {
            String var3;
            do {
               if (!var2.hasMoreElements()) {
                  var1.add("-Dweblogic.upgrade.forked");
                  String var9 = System.getProperty("java.class.path");
                  var9 = PDevHelper.addPDevLibraryToClasspath(var9);
                  var9 = var9.replaceAll("\\\\\\\\", "/");
                  var9 = var9.replaceAll("\\\\", "/");
                  var9 = var9.replace('\\', '/');
                  var1.add("-classpath");
                  var1.add(var9);
                  var1.add("weblogic.Upgrade");
                  var1.add("-mode");
                  var1.add("silent");
                  var1.add("-type");
                  var1.add("domain");
                  if (DEBUG.isDebugEnabled()) {
                     var1.add("-debug");
                     DEBUG.debug("Running DomainUpgradeServerService process with command line list: " + var1);
                  }

                  String[] var10 = (String[])((String[])var1.toArray(new String[0]));
                  Executable var4 = new Executable(System.out, System.err);
                  ServerLocks.releaseServerLock();
                  var4.exec(var10);
                  ServerLocks.getServerLock();
                  int var5 = var4.getExitValue();
                  if (DEBUG.isDebugEnabled()) {
                     DEBUG.debug(" DomainUpgradeServerService process exit code is " + var5);
                  }

                  if (var5 != 0) {
                     String var6 = "Error upgrading the domain. Look at your standard output and error for more information";
                     throw new ServiceFailureException(var6);
                  }

                  return;
               }

               var3 = (String)var2.nextElement();
            } while(!var3.startsWith("weblogic") && !var3.startsWith("com"));

            var1.add("-D" + var3 + "=" + System.getProperty(var3));
         }
      } catch (ServiceFailureException var7) {
         throw var7;
      } catch (Exception var8) {
         throw new ServiceFailureException(var8);
      }
   }

   public void halt() throws ServiceFailureException {
   }

   public void stop() throws ServiceFailureException {
   }
}
