package weblogic.iiop;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.corba.cos.naming.RootNamingContextImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.kernel.Kernel;
import weblogic.security.acl.internal.SecurityServiceImpl;
import weblogic.server.ServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class IIOPService implements ServerService {
   private static final String FALSE_PROP = "false";
   public static final int TX_DISABLED_MECHANISM = 0;
   public static final int TX_OTS_MECHANISM = 1;
   public static final int TX_JTA_MECHANISM = 2;
   public static final int TX_OTS11_MECHANISM = 3;
   public static int txMechanism = 1;
   private static final DebugCategory debugStartup = Debug.getCategory("weblogic.iiop.startup");
   private static final DebugLogger debugIIOPStartup = DebugLogger.getDebugLogger("DebugIIOPStartup");
   private static volatile boolean enabled = false;

   public static boolean load() {
      return IIOPClientService.load();
   }

   public static void setTGIOPEnabled(boolean var0) {
      enabled = var0;
   }

   public static boolean isTGIOPEnabled() {
      return enabled;
   }

   public void stop() throws ServiceFailureException {
      this.halt();
   }

   public void halt() throws ServiceFailureException {
      MuxableSocketIIOP.disable();
   }

   public String getName() {
      return "CorbaService";
   }

   public String getVersion() {
      return "CORBA 2.3, IIOP 1.2, RMI-IIOP SFV2, OTS 1.2, CSIv2 Level 0 + Stateful";
   }

   public void start() throws ServiceFailureException {
      if ("false".equals(System.getProperty("weblogic.system.iiop.enableTxInterop"))) {
         txMechanism = 0;
      }

      boolean var1 = debugStartup.isEnabled() || debugIIOPStartup.isDebugEnabled();

      try {
         InitialReferences.initializeServerInitialReferences();
      } catch (RemoteException var4) {
         throw new ServiceFailureException(var4);
      } catch (IOException var5) {
         throw new ServiceFailureException(var5);
      }

      IORManager.initialize();
      if (txMechanism > 0) {
         String var2 = Kernel.getConfig().getIIOP().getTxMechanism();
         if (var2.equalsIgnoreCase("JTA")) {
            txMechanism = 2;
         } else if (var2.equalsIgnoreCase("OTSv11")) {
            txMechanism = 3;
         } else if (var2.equalsIgnoreCase("none")) {
            txMechanism = 0;
         }

         if (var1) {
            IIOPLogger.logJTAEnabled(var2);
         }
      } else if (var1) {
         IIOPLogger.logJTAEnabled("none");
      }

      ClusterServices.initialize();
      Environment var8 = new Environment();
      var8.setCreateIntermediateContexts(true);

      try {
         var8.getInitialContext().bind("weblogic.cosnaming.NameService", RootNamingContextImpl.getRootNamingContext());
         if (var1) {
            IIOPLogger.logCosNamingService("weblogic.cosnaming.NameService");
         }
      } catch (NamingException var7) {
         if (var1) {
            IIOPLogger.logCosNamingServiceFailed(var7);
         }
      }

      try {
         var8.setReplicateBindings(false);
         Context var3 = var8.getInitialContext();
         var3.bind("weblogic/security/SecurityManager", SecurityServiceImpl.getSingleton());
         if (var1) {
            IIOPLogger.logSecurityService("weblogic/security/SecurityManager");
         }
      } catch (NamingException var6) {
         if (var1) {
            IIOPLogger.logSecurityServiceFailed(var6);
         }
      }

   }
}
