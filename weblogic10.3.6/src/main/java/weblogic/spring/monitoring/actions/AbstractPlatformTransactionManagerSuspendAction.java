package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.spring.monitoring.SpringRuntimeStatisticsHolder;
import weblogic.spring.monitoring.SpringTransactionManagerRuntimeMBeanImpl;

public class AbstractPlatformTransactionManagerSuspendAction extends BaseElapsedTimeAction {
   private static final long serialVersionUID = 1L;

   public AbstractPlatformTransactionManagerSuspendAction() {
      super("SpringAbstractPlatformTransactionManagerSuspendAction");
   }

   protected void updateRuntimeMBean(DiagnosticActionState var1) {
      ElapsedTimeActionState var2 = (ElapsedTimeActionState)var1;
      SpringTransactionManagerRuntimeMBeanImpl var3 = SpringRuntimeStatisticsHolder.getGlobalSpringTransactionManagerRuntimeMBeanImpl(var2.getSpringBean());
      if (var3 != null) {
         var3.addSuspend(var2.getSucceeded());
      }

   }
}
