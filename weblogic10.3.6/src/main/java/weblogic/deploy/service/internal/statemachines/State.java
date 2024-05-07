package weblogic.deploy.service.internal.statemachines;

import weblogic.deploy.common.Debug;
import weblogic.deploy.service.internal.transport.CommonMessageSender;

public class State {
   protected final CommonMessageSender sender = CommonMessageSender.getInstance();

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof State) {
         State var2 = (State)var1;
         return this.toString().equals(var2.toString());
      } else {
         return false;
      }
   }

   public final int hashCode() {
      return this.toString().hashCode();
   }

   protected final void debug(String var1) {
      Debug.serviceDebug(var1);
   }

   protected final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   protected void fireStateTransitionEvent(State var1, String var2, long var3) {
      if (this.isDebugEnabled()) {
         this.debug("DeploymentService event : '" + var1.toString() + "." + var2 + "()' for deployment id '" + var3 + "'");
      }

   }
}
