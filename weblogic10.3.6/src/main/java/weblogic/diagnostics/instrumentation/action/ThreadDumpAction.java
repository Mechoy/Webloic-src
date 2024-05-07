package weblogic.diagnostics.instrumentation.action;

import java.security.AccessController;
import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.EventQueue;
import weblogic.diagnostics.instrumentation.InstrumentationEvent;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.StatelessDiagnosticAction;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JVMRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class ThreadDumpAction extends AbstractDiagnosticAction implements StatelessDiagnosticAction {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ServerRuntimeMBean serverRuntime;

   public ThreadDumpAction() {
      this.setType("ThreadDumpAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public void process(JoinPoint var1) {
      InstrumentationEvent var2 = this.createInstrumentationEvent(var1, false);
      if (var2 != null) {
         String var3 = null;

         try {
            if (this.serverRuntime == null) {
               this.serverRuntime = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
               if (this.serverRuntime != null) {
                  JVMRuntimeMBean var4 = this.serverRuntime.getJVMRuntime();
                  if (var4 != null) {
                     var3 = var4.getThreadStackDump();
                  }
               }
            }

            if (var3 == null) {
               var3 = "UNAVAILABLE";
            }

            var2.setPayload(var3);
            EventQueue.getInstance().enqueue(var2);
         } catch (Exception var5) {
         }
      }

   }
}
