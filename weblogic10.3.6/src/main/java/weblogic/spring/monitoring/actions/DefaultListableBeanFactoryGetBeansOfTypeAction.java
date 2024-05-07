package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.spring.monitoring.SpringApplicationContextRuntimeMBeanImpl;
import weblogic.spring.monitoring.SpringRuntimeStatisticsHolder;

public class DefaultListableBeanFactoryGetBeansOfTypeAction extends BaseElapsedTimeAction {
   private static final long serialVersionUID = 1L;

   public DefaultListableBeanFactoryGetBeansOfTypeAction() {
      super("SpringDefaultListableBeanFactoryGetBeansOfTypeAction");
   }

   protected void updateRuntimeMBean(DiagnosticActionState var1) {
      ElapsedTimeActionState var2 = (ElapsedTimeActionState)var1;
      SpringApplicationContextRuntimeMBeanImpl var3 = SpringRuntimeStatisticsHolder.getGlobalSpringApplicationContextRuntimeMBeanImpl(var2.getSpringBean());
      if (var3 != null) {
         var3.addGetBeansOfTypeExecution(var2.getSucceeded(), var2.getElapsedTime());
      }

   }
}
