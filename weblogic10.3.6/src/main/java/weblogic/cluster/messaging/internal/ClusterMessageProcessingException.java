package weblogic.cluster.messaging.internal;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClusterMessageProcessingException extends RemoteException {
   private final ClusterResponse[] responses;
   private final HashMap failedServers;

   public ClusterMessageProcessingException(Exception var1) {
      super(var1.getMessage(), var1);
      this.responses = null;
      this.failedServers = null;
   }

   public ClusterMessageProcessingException(String var1) {
      super(var1);
      this.responses = null;
      this.failedServers = null;
   }

   public ClusterMessageProcessingException(ClusterResponse[] var1, HashMap var2) {
      super(getReason(var2));
      this.responses = var1;
      this.failedServers = var2;
   }

   public ClusterResponse[] getResponses() {
      return this.responses;
   }

   public HashMap getFailedServers() {
      return this.failedServers;
   }

   private static String getReason(Map var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var1 = new StringBuffer();
         Iterator var2 = var0.keySet().iterator();

         while(var2.hasNext()) {
            ServerInformation var3 = (ServerInformation)var2.next();
            var1.append("Server '" + var3.getServerName() + "' failed due to '" + var0.get(var3) + "'\n");
         }

         return var1.toString();
      }
   }
}
