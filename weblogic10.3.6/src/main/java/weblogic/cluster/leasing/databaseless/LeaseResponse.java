package weblogic.cluster.leasing.databaseless;

import java.util.Arrays;
import weblogic.cluster.messaging.internal.ClusterResponse;

public final class LeaseResponse implements ClusterResponse {
   private final Object result;
   private final LeaseMessage message;
   private static final long serialVersionUID = -318207036285232155L;

   LeaseResponse(Object var1, LeaseMessage var2) {
      this.result = var1;
      this.message = var2;
   }

   Object getResult() {
      return this.result;
   }

   LeaseMessage getLeaseMessage() {
      return this.message;
   }

   public String toString() {
      return this.result instanceof Object[] ? "[LeaseResponse for " + this.message + " is " + Arrays.toString((Object[])((Object[])this.result)) + "]" : "[LeaseResponse for " + this.message + " is " + this.result + "]";
   }
}
