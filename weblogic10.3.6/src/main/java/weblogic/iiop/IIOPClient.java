package weblogic.iiop;

import java.io.IOException;
import weblogic.kernel.Kernel;
import weblogic.protocol.ServerChannel;
import weblogic.security.service.SubjectManagerImpl;
import weblogic.server.channels.DynamicListenThread;

public final class IIOPClient {
   private static boolean enabled = false;

   public static boolean isEnabled() {
      return enabled || Kernel.isServer();
   }

   public static synchronized void initialize() {
      if (!isEnabled()) {
         if (IIOPClientService.load()) {
            SubjectManagerImpl.ensureInitialized();
            Kernel.ensureInitialized();
            IIOPClientService.resumeClient();

            try {
               InitialReferences.initializeClientInitialReferences();
            } catch (IOException var3) {
            }

            MuxableSocketIIOP.initialize();
            IIOPService.txMechanism = 0;
            ServerChannel var0 = ProtocolHandlerIIOP.getProtocolHandler().getDefaultServerChannel();
            if (var0.getPort() != -1) {
               try {
                  DynamicListenThread.createClientListener(var0);
               } catch (SecurityException var2) {
               }
            }

            enabled = true;
         }
      }
   }

   public static void shutdown() {
      MuxableSocketIIOP.disable();
      enabled = false;
   }
}
