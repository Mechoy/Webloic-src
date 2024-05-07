package weblogic.diagnostics.lifecycle;

import weblogic.management.ManagementException;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public final class DiagnosticFoundationService extends AbstractServerService {
   private static final DiagnosticComponentLifecycle[] components = ComponentRegistry.getFoundationWLDFComponents();
   private static DiagnosticFoundationService diagnosticFoundationInstance;
   private boolean initialized;
   private WLDFRuntimeMBeanImpl wldfRuntime;

   public DiagnosticFoundationService() {
      Class var1 = DiagnosticFoundationService.class;
      synchronized(DiagnosticFoundationService.class) {
         if (diagnosticFoundationInstance == null) {
            diagnosticFoundationInstance = this;
         } else {
            throw new IllegalStateException("Attempt to instantiate multiple instances.");
         }
      }
   }

   public static synchronized DiagnosticFoundationService getInstance() {
      if (diagnosticFoundationInstance == null) {
         diagnosticFoundationInstance = new DiagnosticFoundationService();
      }

      return diagnosticFoundationInstance;
   }

   public DiagnosticComponentLifecycle[] getComponents() {
      return components;
   }

   public synchronized void start() throws ServiceFailureException {
      if (!this.initialized) {
         try {
            this.wldfRuntime = new WLDFRuntimeMBeanImpl();
         } catch (ManagementException var4) {
            throw new ServiceFailureException(var4);
         }

         for(int var1 = 0; var1 < components.length; ++var1) {
            try {
               components[var1].initialize();
            } catch (DiagnosticComponentLifecycleException var3) {
               throw new ServiceFailureException(var3);
            }
         }

         this.initialized = true;
         DebugLifecycleUtility.debugHandlerStates(components);
      }
   }

   public void stop() throws ServiceFailureException {
      DebugLifecycleUtility.debugHandlerStates(components);
      if (this.initialized) {
         this.disableComponents();
         this.initialized = false;
      }
   }

   public void halt() throws ServiceFailureException {
      if (this.initialized) {
         this.disableComponents();
         this.initialized = false;
         DebugLifecycleUtility.debugHandlerStates(components);
      }
   }

   private void disableComponents() throws ServiceFailureException {
      for(int var1 = components.length - 1; var1 >= 0; --var1) {
         try {
            components[var1].disable();
         } catch (DiagnosticComponentLifecycleException var3) {
            throw new ServiceFailureException(var3);
         }
      }

   }
}
