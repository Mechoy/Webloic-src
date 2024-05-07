package weblogic.iiop;

import java.io.IOException;
import java.rmi.RemoteException;
import weblogic.corba.client.spi.ServiceManager;
import weblogic.corba.j2ee.naming.NameParser;
import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.corba.orb.ORBHelperImpl;
import weblogic.corba.server.naming.ReferenceHelperImpl;
import weblogic.corba.utils.ClassInfo;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.security.internal.server.ServerSubjectPusher;
import weblogic.kernel.Kernel;
import weblogic.protocol.ClientEnvironment;
import weblogic.rmi.extensions.DisconnectMonitorListImpl;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class IIOPClientService extends AbstractServerService {
   private static boolean loaded = false;
   private static final String FALSE_PROP = "false";
   private static final String TRUE_PROP = "true";
   private static boolean initialized = false;
   private static final DebugCategory debugStartup = Debug.getCategory("weblogic.iiop.startup");
   private static final DebugLogger debugIIOPStartup = DebugLogger.getDebugLogger("DebugIIOPStartup");
   public static String locationForwardPolicy;
   public static byte defaultGIOPMinorVersion = 2;
   public static boolean useSerialFormatVersion2 = false;
   public static boolean reconnectOnBootstrap = false;
   public static boolean useLocateRequest = false;

   public static boolean load() {
      if (loaded) {
         return loaded;
      } else if (!"false".equals(System.getProperty("weblogic.system.enableIIOP")) && !"false".equals(System.getProperty("weblogic.system.iiop.enable"))) {
         if ("true".equals(System.getProperty("weblogic.system.iiop.reconnectOnBootstrap"))) {
            reconnectOnBootstrap = true;
         }

         try {
            loaded = true;
            ClientEnvironment.loadEnvironment();
            if ("weblogic.iiop.UtilDelegateImpl".equals(System.getProperty("javax.rmi.CORBA.UtilClass"))) {
               if (Kernel.isServer()) {
                  IIOPLogger.logEnabled();
               }
            } else {
               IIOPLogger.logUtilClassNotInstalled(System.getProperty("javax.rmi.CORBA.UtilClass"));
            }

            if (!"weblogic.iiop.PortableRemoteObjectDelegateImpl".equals(System.getProperty("javax.rmi.CORBA.PortableRemoteObjectClass"))) {
               IIOPLogger.logPROClassNotInstalled(System.getProperty("javax.rmi.CORBA.PortableRemoteObjectClass"));
            }

            ReferenceHelper.setReferenceHelper(new ReferenceHelperImpl());
            ServiceManager.setSecurityManager(new ServerSubjectPusher());
            return true;
         } catch (Throwable var1) {
            return false;
         }
      } else {
         return false;
      }
   }

   private void initialize() throws ServiceFailureException {
      if (!initialized) {
         initialized = true;
         if (load()) {
            ORBHelper.setORBHelper(new ORBHelperImpl());
            ProtocolHandlerIIOP.getProtocolHandler();
            ProtocolHandlerIIOPS.getProtocolHandler();
            MuxableSocketIIOP.initialize();

            try {
               InitialReferences.initializeClientInitialReferences();
            } catch (RemoteException var2) {
               throw new ServiceFailureException(var2);
            } catch (IOException var3) {
               throw new ServiceFailureException(var3);
            }
         }
      }
   }

   public void stop() {
      this.halt();
   }

   public void halt() {
      MuxableSocketIIOP.disable();
   }

   public static void resumeClient() {
      boolean var0 = debugStartup.isEnabled() || debugIIOPStartup.isDebugEnabled();
      useLocateRequest = Kernel.getConfig().getIIOP().getUseLocateRequest();
      if (var0) {
         IIOPLogger.logLocateRequest(useLocateRequest ? "on" : "off");
      }

      defaultGIOPMinorVersion = (byte)Kernel.getConfig().getIIOP().getDefaultMinorVersion();
      if (var0) {
         IIOPLogger.logGIOPVersion(defaultGIOPMinorVersion);
      }

      locationForwardPolicy = Kernel.getConfig().getIIOP().getLocationForwardPolicy();
      if (var0) {
         IIOPLogger.logLocationForwardPolicy(locationForwardPolicy);
      }

      useSerialFormatVersion2 = Kernel.getConfig().getIIOP().getUseSerialFormatVersion2();
      NameParser.initialize(defaultGIOPMinorVersion);
      String var1 = Kernel.getConfig().getIIOP().getDefaultCharCodeset();
      String var2 = Kernel.getConfig().getIIOP().getDefaultWideCharCodeset();
      CodeSet.setDefaults(CodeSet.getOSFCodeset(var1), CodeSet.getOSFCodeset(var2));
      if (var0) {
         IIOPLogger.logCodeSet("char", var1, "0x" + Integer.toHexString(CodeSet.getDefaultCharCodeSet()));
      }

      if (var0) {
         IIOPLogger.logCodeSet("wchar", var2, "0x" + Integer.toHexString(CodeSet.getDefaultWcharCodeSet()));
      }

      CodeSetsComponent.resetDefault();
      ClassInfo.initialize(Kernel.getConfig().getIIOP().getUseFullRepositoryIdList());
      DisconnectMonitorListImpl.getDisconnectMonitorList().addDisconnectMonitor(new DisconnectMonitorImpl());
   }

   public void start() throws ServiceFailureException {
      this.initialize();
      resumeClient();
   }
}
