package weblogic.diagnostics.lifecycle;

import weblogic.diagnostics.accessor.AccessRuntime;
import weblogic.diagnostics.accessor.AccessorEnvironment;
import weblogic.diagnostics.accessor.WLSAccessorConfigurationProviderImpl;
import weblogic.diagnostics.accessor.WLSAccessorMBeanFactoryImpl;
import weblogic.diagnostics.accessor.WLSAccessorSecurityProviderImpl;
import weblogic.management.runtime.WLDFRuntimeMBean;
import weblogic.t3.srvr.ServerRuntime;

public class ArchiveLifecycleImpl implements DiagnosticComponentLifecycle {
   private static ArchiveLifecycleImpl singleton = new ArchiveLifecycleImpl();

   public static final DiagnosticComponentLifecycle getInstance() {
      return singleton;
   }

   public int getStatus() {
      return 4;
   }

   public void initialize() throws DiagnosticComponentLifecycleException {
      try {
         WLDFRuntimeMBean var1 = ServerRuntime.theOne().getWLDFRuntime();
         WLSAccessorMBeanFactoryImpl var2 = new WLSAccessorMBeanFactoryImpl();
         WLSAccessorConfigurationProviderImpl var3 = new WLSAccessorConfigurationProviderImpl(var2);
         WLSAccessorSecurityProviderImpl var4 = new WLSAccessorSecurityProviderImpl();
         AccessorEnvironment var5 = new AccessorEnvironment(var3, var4, var2);
         AccessRuntime.initialize(var5, var1);
      } catch (Exception var6) {
         throw new DiagnosticComponentLifecycleException(var6);
      }
   }

   public void enable() throws DiagnosticComponentLifecycleException {
   }

   public void disable() throws DiagnosticComponentLifecycleException {
   }
}
