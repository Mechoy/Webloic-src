package weblogic.t3.srvr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import weblogic.kernel.FinalThreadLocalList;
import weblogic.kernel.Kernel;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.ManagementException;
import weblogic.management.bootstrap.BootStrap;
import weblogic.management.configuration.ConfigurationException;
import weblogic.net.http.Handler;
import weblogic.security.utils.SSLSetup;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class PreConfigBootService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      try {
         boolean var1 = Boolean.getBoolean("weblogic.nodemanager.ServiceEnabled");
         String var2;
         if (!var1) {
            var2 = System.getProperty("weblogic.Stdout");
            if (var2 != null) {
               System.setOut(getPrintStream(var2));
            }
         }

         var2 = System.getProperty("weblogic.Stderr");
         if (var2 != null) {
            System.setErr(getPrintStream(var2));
         }

         T3SrvrLogger.logServerStarting(System.getProperty("java.vm.name"), System.getProperty("java.vm.version"), System.getProperty("java.vm.vendor"));
         String var3 = BootStrap.getWebLogicHome();
         if (var3 == null) {
            throw new ConfigurationException("Property weblogic.home must be set to run WebLogic Server.  This should be set to the location of your WebLogic Server install (i.e. -Dweblogic.home=/bea/wlserver[version])");
         } else {
            SSLSetup.initForServer();
            Handler.init();
            Kernel.setIsServer(true);
            FinalThreadLocalList.initialize();
         }
      } catch (ConfigurationException var4) {
         T3SrvrLogger.logConfigFailure(var4.getMessage());
         throw new ServiceFailureException(var4);
      } catch (ManagementException var5) {
         throw new ServiceFailureException(var5);
      } catch (IOException var6) {
         throw new ServiceFailureException(var6);
      }
   }

   private static PrintStream getPrintStream(String var0) throws IOException {
      File var1 = new File(var0);
      if (!var1.exists()) {
         File var2 = var1.getParentFile();
         if (var2 != null && !var2.exists()) {
            var2.mkdirs();
         }
      }

      return new PrintStream(new FileOutputStream(var0, true));
   }

   public void stop() {
      this.shutdown();
   }

   public void halt() {
      this.shutdown();
   }

   public void shutdown() {
   }
}
