package weblogic.ejb.container.internal.usertransactioncheck;

import javax.transaction.UserTransaction;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.internal.AllowedMethodsHelper;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.logging.Loggable;

public class SFSBUserTransactionProxy extends BaseUserTransactionProxy {
   public SFSBUserTransactionProxy(UserTransaction var1) {
      super(false, var1);
   }

   protected void checkAllowedInvoke() {
      WLEnterpriseBean var1 = AllowedMethodsHelper.getBean();
      int var2 = RuntimeHelper.getCurrentState(var1);
      if (var2 == 1) {
         Loggable var3 = EJBLogger.logSFSBIllegalInvokeUserTransactionMethodInSetSessionContextOrDILoggable();
         throw new IllegalStateException(var3.getMessage());
      } else {
         super.checkAllowedInvoke();
      }
   }
}
