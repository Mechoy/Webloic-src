package weblogic.cluster;

final class BasicConflictHandler implements ConflictHandler {
   public BasicConflictHandler() {
   }

   public void conflictStart(ServiceOffer var1) {
      if (var1.isClusterable()) {
         ClusterLogger.logConflictStartInCompatibleClusterableObject(var1.name(), var1.serviceName());
      } else {
         ClusterLogger.logConflictStartNonClusterableObject(var1.name(), var1.serviceName());
      }

   }

   public void conflictStop(ServiceOffer var1) {
      ClusterLogger.logConflictStop(var1.name(), var1.serviceName());
   }
}
