package weblogic.diagnostics.lifecycle;

import java.security.AccessController;
import java.util.HashMap;
import weblogic.diagnostics.accessor.AccessorUtils;
import weblogic.diagnostics.archive.DiagnosticStoreRepository;
import weblogic.diagnostics.i18n.DiagnosticsLogger;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.WLDFServerDiagnosticMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.store.PersistentStoreException;
import weblogic.store.admin.RuntimeHandlerImpl;

public final class DiagnosticSystemService extends AbstractServerService {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DiagnosticComponentLifecycle[] components = ComponentRegistry.getNonFoundationWLDFComponents();
   private static DiagnosticSystemService diagnosticSystemInstance;
   private boolean initialized;

   public DiagnosticSystemService() {
      Class var1 = DiagnosticSystemService.class;
      synchronized(DiagnosticSystemService.class) {
         if (diagnosticSystemInstance == null) {
            diagnosticSystemInstance = this;
         } else {
            throw new IllegalStateException("Attempt to instantiate multiple instances.");
         }
      }
   }

   public static synchronized DiagnosticSystemService getInstance() {
      if (diagnosticSystemInstance == null) {
         diagnosticSystemInstance = new DiagnosticSystemService();
      }

      return diagnosticSystemInstance;
   }

   public DiagnosticComponentLifecycle[] getComponents() {
      return components;
   }

   public void start() throws ServiceFailureException {
      if (!this.initialized) {
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         DiagnosticsLogger.logDiagnosticsInitializing(var1.getName());
         String var2 = AccessorUtils.getDiagnosticStoreDirectory();
         WLDFServerDiagnosticMBean var3 = var1.getServerDiagnosticConfig();
         HashMap var4 = new HashMap();
         var4.put("FileLockingEnabled", var3.isDiagnosticStoreFileLockingEnabled());
         var4.put("IoBufferSize", var3.getDiagnosticStoreIoBufferSize());
         var4.put("BlockSize", var3.getDiagnosticStoreBlockSize());
         var4.put("MaxFileSize", var3.getDiagnosticStoreMaxFileSize());
         var4.put("MinWindowBufferSize", var3.getDiagnosticStoreMinWindowBufferSize());
         var4.put("MaxWindowBufferSize", var3.getDiagnosticStoreMaxWindowBufferSize());

         try {
            DiagnosticStoreRepository.getInstance().getStore(var2, var4, KernelStatus.isServer() ? new RuntimeHandlerImpl() : null);
         } catch (PersistentStoreException var8) {
            throw new ServiceFailureException(var8);
         }

         for(int var5 = 0; var5 < components.length; ++var5) {
            try {
               components[var5].initialize();
            } catch (DiagnosticComponentLifecycleException var7) {
               throw new ServiceFailureException(var7);
            }
         }

         this.initialized = true;
         DebugLifecycleUtility.debugHandlerStates(components);
      }
   }

   public void stop() throws ServiceFailureException {
      if (this.initialized) {
         DiagnosticsLogger.logDiagnosticsStopping(ManagementService.getRuntimeAccess(kernelId).getServer().getName());

         for(int var1 = components.length - 1; var1 >= 0; --var1) {
            try {
               components[var1].disable();
            } catch (DiagnosticComponentLifecycleException var3) {
               throw new ServiceFailureException(var3);
            }
         }

         this.initialized = false;
         DebugLifecycleUtility.debugHandlerStates(components);
      }
   }

   public void halt() throws ServiceFailureException {
      if (this.initialized) {
         DiagnosticsLogger.logDiagnosticsStopping(ManagementService.getRuntimeAccess(kernelId).getServer().getName());

         for(int var1 = components.length - 1; var1 >= 0; --var1) {
            try {
               components[var1].disable();
            } catch (DiagnosticComponentLifecycleException var3) {
               throw new ServiceFailureException(var3);
            }
         }

         this.initialized = false;
         DebugLifecycleUtility.debugHandlerStates(components);
      }
   }

   public static boolean isInitialized() {
      return diagnosticSystemInstance != null && diagnosticSystemInstance.initialized;
   }
}
