package weblogic.messaging.saf.common;

import weblogic.messaging.saf.SAFResult;

public final class AgentDeliverResponse {
   private SAFResultImpl result;

   public AgentDeliverResponse(SAFResultImpl var1) {
      this.result = var1;
   }

   public SAFResult getResult() {
      return this.result;
   }
}
