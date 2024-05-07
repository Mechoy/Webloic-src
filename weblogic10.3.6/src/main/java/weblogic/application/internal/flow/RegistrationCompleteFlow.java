package weblogic.application.internal.flow;

import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.management.DeploymentException;

public final class RegistrationCompleteFlow extends BaseFlow implements Flow {
   public RegistrationCompleteFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      var1.registrationComplete();
   }

   public void forceProductionToAdmin(AdminModeCompletionBarrier var1) throws DeploymentException {
      var1.registrationComplete();
   }
}
