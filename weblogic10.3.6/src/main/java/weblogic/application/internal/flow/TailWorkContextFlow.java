package weblogic.application.internal.flow;

import weblogic.application.ApplicationContextInternal;
import weblogic.application.internal.Flow;
import weblogic.management.DeploymentException;

public final class TailWorkContextFlow extends BaseWorkContextFlow implements Flow {
   public TailWorkContextFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      this.unsetBindApplicationIdCtx("TailWorkContextFlow.prepare");
   }

   public void activate() throws DeploymentException {
      this.unsetBindApplicationIdCtx("TailWorkContextFlow.activate");
   }

   public void deactivate() throws DeploymentException {
      this.setBindApplicationIdCtx("TailWorkContextFlow.deactivate");
   }

   public void unprepare() throws DeploymentException {
      this.setBindApplicationIdCtx("TailWorkContextFlow.unprepare");
   }

   public void remove() throws DeploymentException {
      this.setBindApplicationIdCtx("TailWorkContextFlow.remove");
   }

   public void start() throws DeploymentException {
      this.unsetBindApplicationIdCtx("TailWorkContextFlow.start");
   }

   public void stop() throws DeploymentException {
      this.setBindApplicationIdCtx("TailWorkContextFlow.stop");
   }
}
