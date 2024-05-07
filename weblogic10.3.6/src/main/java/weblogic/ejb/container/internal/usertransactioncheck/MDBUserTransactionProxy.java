package weblogic.ejb.container.internal.usertransactioncheck;

import javax.transaction.UserTransaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.logging.Loggable;

public class MDBUserTransactionProxy extends BaseUserTransactionProxy {
   public MDBUserTransactionProxy(UserTransaction var1) {
      super(false, var1);
   }

   protected void checkAllowedInvoke() {
      WLEnterpriseBean var1 = AllowedMethodsHelper.getBean();
      int var2 = RuntimeHelper.getCurrentState(var1);
      Loggable var3;
      if (var2 == 4) {
         var3 = EJBLogger.logMDBIllegalInvokeUserTransactionMethodInEjbCreateOrPostConstructLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 16) {
         var3 = EJBLogger.logMDBIllegalInvokeUserTransactionMethodInEjbRemoveOrPreDestroyLoggable();
         throw new IllegalStateException(var3.getMessage());
      } else if (var2 == 1) {
         var3 = EJBLogger.logMDBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable();
         throw new IllegalStateException(var3.getMessage());
      }
   }
}
