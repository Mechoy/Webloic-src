package weblogic.ejb.container.internal.usertransactioncheck;

import javax.transaction.UserTransaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.logging.Loggable;

public class SLSBUserTransactionProxy extends BaseUserTransactionProxy {
   public SLSBUserTransactionProxy(UserTransaction var1) {
      super(false, var1);
   }

   protected void checkAllowedInvoke() {
      WLEnterpriseBean var1 = AllowedMethodsHelper.getBean();
      int var2 = RuntimeHelper.getCurrentState(var1);
      Loggable var3;
      if (var2 == 4) {
         var3 = EJBLogger.logSLSBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstructLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 16) {
         var3 = EJBLogger.logSLSBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroyLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 1) {
         var3 = EJBLogger.logSLSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable();
         throw new IllegalStateException(var3.getMessage());
      }
   }
}
