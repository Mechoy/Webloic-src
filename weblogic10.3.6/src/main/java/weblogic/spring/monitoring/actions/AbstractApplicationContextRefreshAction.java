package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.spring.monitoring.SpringApplicationContextRuntimeMBeanImpl;
import weblogic.spring.monitoring.SpringRuntimeStatisticsHolder;
import weblogic.spring.monitoring.utils.AbstractApplicationContextDelegator;

public class AbstractApplicationContextRefreshAction extends BaseElapsedTimeAction {
   private static final long serialVersionUID = 1L;

   public AbstractApplicationContextRefreshAction() {
      super("SpringAbstractApplicationContextRefreshAction");
   }

   protected void updateRuntimeMBean(DiagnosticActionState var1) {
      ElapsedTimeActionState var2 = (ElapsedTimeActionState)var1;
      SpringApplicationContextRuntimeMBeanImpl var3 = SpringRuntimeStatisticsHolder.getGlobalSpringApplicationContextRuntimeMBeanImpl(this.getBeanFactory(var2.getSpringBean()));
      if (var3 != null) {
         var3.addRefresh(var2.getSucceeded(), var2.getElapsedTime());
      }

   }

   private Object getBeanFactory(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         AbstractApplicationContextDelegator var2 = new AbstractApplicationContextDelegator(var1);
         return var2.getBeanFactory();
      }
   }
}
