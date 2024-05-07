package weblogic.spring.monitoring.actions;

import weblogic.diagnostics.instrumentation.AbstractDiagnosticAction;
import weblogic.diagnostics.instrumentation.AroundDiagnosticAction;
import weblogic.diagnostics.instrumentation.DiagnosticActionState;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.spring.monitoring.SpringApplicationContextRuntimeMBeanImpl;
import weblogic.spring.monitoring.SpringRuntimeStatisticsHolder;

public class AbstractBeanFactoryRegisterScopeAction extends AbstractDiagnosticAction implements AroundDiagnosticAction {
   private static final long serialVersionUID = 1L;

   public AbstractBeanFactoryRegisterScopeAction() {
      this.setType("SpringAbstractBeanFactoryRegisterScopeAction");
   }

   public String[] getAttributeNames() {
      return null;
   }

   public DiagnosticActionState createState() {
      return new ScopeArgActionState();
   }

   public boolean requiresArgumentsCapture() {
      return true;
   }

   public void preProcess(JoinPoint var1, DiagnosticActionState var2) {
      this.setArguments(((DynamicJoinPoint)var1).getArguments(), var2);
   }

   public void postProcess(JoinPoint var1, DiagnosticActionState var2) {
      this.updateRuntimeMBean(var2);
   }

   private void setArguments(Object[] var1, DiagnosticActionState var2) {
      ScopeArgActionState var3 = (ScopeArgActionState)var2;
      if (var1 != null && var1.length != 0) {
         var3.setSpringBean(var1[0]);
         if (var1.length >= 1) {
            var3.setScopeName((String)var1[1]);
         }

      }
   }

   private void updateRuntimeMBean(DiagnosticActionState var1) {
      ScopeArgActionState var2 = (ScopeArgActionState)var1;
      if (var2.getSucceeded()) {
         SpringApplicationContextRuntimeMBeanImpl var3 = SpringRuntimeStatisticsHolder.getGlobalSpringApplicationContextRuntimeMBeanImpl(var2.getSpringBean());
         if (var3 != null) {
            var3.addCustomScope(var2.getScopeName());
         }
      }

   }
}
