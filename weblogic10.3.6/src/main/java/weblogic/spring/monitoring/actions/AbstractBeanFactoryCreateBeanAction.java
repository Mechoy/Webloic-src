package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.spring.monitoring.SpringApplicationContextRuntimeMBeanImpl;
import weblogic.spring.monitoring.SpringRuntimeStatisticsHolder;

public class AbstractBeanFactoryCreateBeanAction extends BaseElapsedTimeAction {
   private static final long serialVersionUID = 1L;

   public AbstractBeanFactoryCreateBeanAction() {
      super("SpringAbstractBeanFactoryCreateBeanAction");
   }

   public DiagnosticActionState createState() {
      return new CreateBeanElapsedTimeActionState();
   }

   protected void setArguments(Object[] var1, DiagnosticActionState var2) {
      super.setArguments(var1, var2);
      if (var1 != null && var1.length >= 3) {
         CreateBeanElapsedTimeActionState var3 = (CreateBeanElapsedTimeActionState)var2;
         var3.setAbstractBeanDefinition(var1[2]);
      }
   }

   protected void updateRuntimeMBean(DiagnosticActionState var1) {
      CreateBeanElapsedTimeActionState var2 = (CreateBeanElapsedTimeActionState)var1;
      SpringApplicationContextRuntimeMBeanImpl var3 = SpringRuntimeStatisticsHolder.getGlobalSpringApplicationContextRuntimeMBeanImpl(var2.getSpringBean());
      if (var3 != null) {
         if (var2.isSingleton()) {
            var3.addSingletonBeanCreation(var2.getElapsedTime());
         } else if (var2.isPrototype()) {
            var3.addPrototypeBeanCreation(var2.getElapsedTime());
         } else {
            String var4 = var2.getScopeName();
            if (var4 != null) {
               var3.addCustomScopeBeanCreation(var4, var2.getElapsedTime());
            }
         }
      }

   }
}
