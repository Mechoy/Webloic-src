package weblogic.messaging.saf.internal;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.diagnostics.image.ImageManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceNotFoundException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.utils.GenericManagedService;
import weblogic.management.utils.GenericServiceManager;
import weblogic.messaging.saf.SAFEndpointManager;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFLogger;
import weblogic.messaging.saf.SAFTransport;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class SAFServerService extends AbstractServerService {
   private static final int STATE_INITIALIZING = 0;
   private static final int STATE_SUSPENDING = 1;
   private static final int STATE_SUSPENDED = 2;
   private static final int STATE_STARTED = 4;
   private static final int STATE_SHUTTING_DOWN = 8;
   private static final int STATE_CLOSED = 16;
   private int state = 0;
   private static SAFServerService singleton;
   private SAFServiceAdmin safAdmin;
   private SAFManagerImpl safManager;
   private boolean registered;
   private GenericManagedService safServerService;
   private HashMap agents = new HashMap();
   static final String IMAGE_NAME = "SAF";
   private final ImageSource IMAGE_SOURCE = new SAFDiagnosticImageSource();

   public SAFServerService() {
      singleton = this;
   }

   public static SAFServerService getService() {
      return singleton;
   }

   public void stop() throws ServiceFailureException {
      this.suspend(false);
      this.shutdown();
      this.unregisterDiagnosticImageSource();
   }

   public void halt() throws ServiceFailureException {
      this.suspend(true);
      this.shutdown();
      this.unregisterDiagnosticImageSource();
   }

   public void start() throws ServiceFailureException {
      this.safManager = (SAFManagerImpl)SAFManagerImpl.getManager();
      if (!this.registered) {
         GenericServiceManager var1 = GenericServiceManager.getManager();
         this.safServerService = var1.register(SAFAgentMBean.class, SAFServiceAdmin.class, true);
         this.registered = true;
      }

      this.safServerService.start();
      this.registerDiagnosticImageSource();
      synchronized(this) {
         this.state = 2;
      }

      SAFLogger.logSAFInitialized();
      synchronized(this) {
         if (this.state == 4) {
            return;
         }

         this.processAgentServerLifeCycleEvent(4, true);
         this.state = 4;
      }

      this.safManager.registerEndpointManager(2, this.getWsrmSAFEndpointManager());
      this.safManager.registerEndpointManager(3, this.getWsrmJaxwsSAFEndpointManager());
      this.safManager.registerTransport(this.getWsrmTransport());
      this.safManager.registerTransport(this.getWsrmJaxwsTransport());
      SAFLogger.logSAFStarted();
   }

   private SAFEndpointManager getWsrmSAFEndpointManager() {
      try {
         Class var1 = Class.forName("weblogic.wsee.reliability.WsrmEndpointManager");
         return (SAFEndpointManager)var1.newInstance();
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   private SAFEndpointManager getWsrmJaxwsSAFEndpointManager() {
      try {
         Class var1 = Class.forName("weblogic.wsee.reliability2.saf.WsrmSAFEndpointManager");
         return (SAFEndpointManager)var1.newInstance();
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   private SAFTransport getWsrmTransport() {
      try {
         Class var1 = Class.forName("weblogic.wsee.reliability.WsrmSAFTransport");
         return (SAFTransport)var1.newInstance();
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   private SAFTransport getWsrmJaxwsTransport() {
      try {
         Class var1 = Class.forName("weblogic.wsee.reliability2.saf.WsrmSAFTransport");
         return (SAFTransport)var1.newInstance();
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   private void suspend(boolean var1) throws ServiceFailureException {
      synchronized(this) {
         try {
            this.processAgentServerLifeCycleEvent(1, var1);
         } finally {
            this.state = 2;
         }
      }

      SAFLogger.logSAFSuspended();
   }

   private boolean isShutdown() {
      return this.state == 16 || this.state == 8;
   }

   void checkShutdown() throws ServiceFailureException {
      if (this.isShutdown()) {
         throw new ServiceFailureException("Store-and-forward Service is shutdown.");
      }
   }

   private void shutdown() throws ServiceFailureException {
      synchronized(this) {
         if (this.state == 16) {
            return;
         }

         this.state = 8;
      }

      this.safServerService.stop();
      this.registered = false;

      try {
         this.processAgentServerLifeCycleEvent(8, true);
      } finally {
         synchronized(this) {
            this.state = 16;
         }

         SAFLogger.logSAFShutdown();
      }

   }

   private void processAgentServerLifeCycleEvent(int var1, boolean var2) throws ServiceFailureException {
      Iterator var3;
      synchronized(this) {
         var3 = ((HashMap)this.agents.clone()).values().iterator();
         if (var1 == 8) {
            this.agents.clear();
         }
      }

      while(var3.hasNext()) {
         SAFAgentAdmin var4 = (SAFAgentAdmin)var3.next();
         switch (var1) {
            case 1:
               var4.suspend(var2);
               break;
            case 4:
               try {
                  var4.resume();
                  break;
               } catch (SAFException var6) {
                  SAFLogger.logErrorResumeAgent(var4.getName(), var6);
                  throw new ServiceFailureException("Failed to start SAF agent '" + var4.getName() + "', due to " + var6.getMessage());
               }
            case 8:
               var4.close();
         }
      }

   }

   public void registerDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();
      var1.registerImageSource("SAF", this.IMAGE_SOURCE);
   }

   private void unregisterDiagnosticImageSource() {
      ImageManager var1 = ImageManager.getInstance();

      try {
         var1.unregisterImageSource("SAF");
      } catch (ImageSourceNotFoundException var3) {
      }

   }

   public boolean isTargetsChangeAllowed(SAFAgentMBean var1) {
      TargetMBean[] var2 = var1.getTargets();
      if (var2 != null && var2.length != 0 && var2[0] instanceof MigratableTargetMBean) {
         SAFAgentAdmin var3 = this.getAgent(var1.getName());
         if (var3 != null && var3.isActiveForWSRM()) {
            return false;
         } else {
            if (var3 != null && var3.getMBean() != null) {
               TargetMBean[] var4 = var3.getMBean().getTargets();
               if (var4 == null || var4.length == 0) {
                  return true;
               }

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if ((var4[var5] instanceof ServerMBean || var4[var5] instanceof ClusterMBean) && var2[0] instanceof MigratableTargetMBean) {
                     return false;
                  }
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   synchronized void addAgent(SAFAgentAdmin var1) {
      this.agents.put(var1.getName(), var1);
   }

   synchronized SAFAgentAdmin getAgent(String var1) {
      return (SAFAgentAdmin)this.agents.get(var1);
   }

   synchronized SAFAgentAdmin removeAgent(String var1) {
      return (SAFAgentAdmin)this.agents.get(var1);
   }
}
