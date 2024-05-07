package weblogic.deploy.internal;

import java.util.Comparator;
import java.util.HashMap;
import weblogic.deploy.service.internal.DeploymentServiceLogger;
import weblogic.management.configuration.BasicDeploymentMBean;

public class DeploymentOrder {
   public static final DeploymentType[] DEPLOYMENT_ORDER;
   public static final Comparator COMPARATOR;
   private static final HashMap orderMap;

   public static final boolean isBeforeDeploymentHandler(BasicDeploymentMBean var0) {
      for(int var1 = 0; var1 < DEPLOYMENT_ORDER.length; ++var1) {
         DeploymentType var2 = DEPLOYMENT_ORDER[var1];
         if (var2 == DeploymentType.DEPLOYMENT_HANDLER) {
            return false;
         }

         if (var2.isInstance(var0)) {
            return true;
         }
      }

      return true;
   }

   public static int getCachedDeploymentOrder(BasicDeploymentMBean var0) {
      Integer var1 = (Integer)orderMap.get(var0);
      return var1 != null ? var1 : var0.getDeploymentOrder();
   }

   public static Object addToDeploymentOrderCache(BasicDeploymentMBean var0, int var1) {
      return orderMap.put(var0, new Integer(var1));
   }

   public static Object removeFromDeploymentOrderCache(BasicDeploymentMBean var0) {
      return orderMap.remove(var0);
   }

   static {
      DEPLOYMENT_ORDER = new DeploymentType[]{DeploymentType.CACHE, DeploymentType.INTERNAL_APP, DeploymentType.JDBC_SYS_RES, DeploymentType.DEPLOYMENT_HANDLER, DeploymentType.JMS_SYS_RES, DeploymentType.RESOURCE_DEPENDENT_DEPLOYMENT_HANDLER, DeploymentType.STARTUP_CLASS, DeploymentType.WLDF_SYS_RES, DeploymentType.LIBRARY, DeploymentType.DEFAULT_APP, DeploymentType.COHERENCE_CLUSTER_SYS_RES, DeploymentType.CUSTOM_SYS_RES};
      COMPARATOR = new Comparator() {
         public int compare(Object var1, Object var2) {
            if (var1 == var2) {
               return 0;
            } else {
               for(int var3 = 0; var3 < DeploymentOrder.DEPLOYMENT_ORDER.length; ++var3) {
                  DeploymentType var4 = DeploymentOrder.DEPLOYMENT_ORDER[var3];
                  if (var4.isInstance(var1) && !var4.isInstance(var2)) {
                     return -1;
                  }

                  if (!var4.isInstance(var1) && var4.isInstance(var2)) {
                     return 1;
                  }

                  if (var4.isInstance(var1) && var4.isInstance(var2)) {
                     return var4.compare(var1, var2);
                  }
               }

               String var5 = DeploymentServiceLogger.unrecognizedTypes(var1.getClass().getName(), var2.getClass().getName());
               throw new ClassCastException(var5);
            }
         }
      };
      orderMap = new HashMap();
   }
}
