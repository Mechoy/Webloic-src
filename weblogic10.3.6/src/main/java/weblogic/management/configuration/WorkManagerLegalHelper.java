package weblogic.management.configuration;

import java.util.HashSet;

public final class WorkManagerLegalHelper {
   public static void validateMaxThreadsConstraint(MaxThreadsConstraintMBean var0) {
      String var1 = var0.getConnectionPoolName();
      if (var0.getCount() > 0 && var1 != null && var1.trim().length() > 0) {
         throw new IllegalArgumentException("Count and ConnectionPoolName cannot be set together. Please set either count or connection pool name but not both");
      }
   }

   public static void validateWorkManager(WorkManagerMBean var0) {
      int var1 = 0;
      if (var0.getFairShareRequestClass() != null) {
         ++var1;
      }

      if (var0.getResponseTimeRequestClass() != null) {
         ++var1;
      }

      if (var0.getContextRequestClass() != null) {
         ++var1;
      }

      if (var1 > 1) {
         throw new IllegalArgumentException("WorkManagerMBean cannot have more than one RequestClass. Please choose either a FairShareRequestClass, ResponseTimeRequestClass, or ContextRequestClass but not more than one");
      }
   }

   public static void validateTargets(WorkManagerMBean var0, DeploymentMBean var1) {
      if (var0 != null && var1 != null) {
         HashSet var2 = new HashSet();
         TargetMBean[] var3 = var0.getTargets();
         if (var3 != null) {
            int var4;
            for(var4 = 0; var4 < var3.length; ++var4) {
               var2.addAll(var3[var4].getServerNames());
            }

            var3 = var1.getTargets();
            if (var3 != null) {
               for(var4 = 0; var4 < var3.length; ++var4) {
                  if (var2.removeAll(var3[var4].getServerNames())) {
                     return;
                  }
               }

               throw new IllegalArgumentException("WorkManagerMBean '" + var0.getName() + "' refers to a constraint or request class '" + var1.getName() + "' but they are deployed on targets that have no servers in common. " + "Please deploy the mbeans so that they have at least one server in " + "common.");
            }
         }
      }
   }
}
