package weblogic.application.internal.flow;

import weblogic.application.internal.Flow;
import weblogic.application.internal.FlowContext;
import weblogic.management.DeploymentException;

public final class TailLifecycleFlow extends BaseLifecycleFlow implements Flow {
   public TailLifecycleFlow(FlowContext var1) {
      super(var1);
   }

   public void activate() throws DeploymentException {
      this.postStart();
   }

   public void deactivate() throws DeploymentException {
      this.preStop();
   }
}
