package weblogic.wsee.mc.processor;

import java.util.Arrays;
import java.util.List;

public enum McPollState {
   POLLING,
   ENABLED,
   EXPIRED,
   TERMINATED;

   private List<McPollState> _validNextStates;

   private void setValidNextStates(McPollState[] var1) {
      this._validNextStates = Arrays.asList(var1);
   }

   public boolean isValidTransition(McPollState var1) {
      return this._validNextStates.contains(var1);
   }

   static {
      ENABLED.setValidNextStates(new McPollState[]{POLLING, EXPIRED, TERMINATED});
      POLLING.setValidNextStates(new McPollState[]{ENABLED, EXPIRED, TERMINATED});
      EXPIRED.setValidNextStates(new McPollState[]{TERMINATED});
      TERMINATED.setValidNextStates(new McPollState[0]);
   }
}
