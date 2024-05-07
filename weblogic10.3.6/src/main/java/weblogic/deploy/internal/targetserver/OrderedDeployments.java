package weblogic.deploy.internal.targetserver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.Collection;
import java.util.TreeMap;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.DeploymentOrder;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.ArrayUtils;

public class OrderedDeployments {
   private static final TreeMap deployments;
   private static PropertyChangeListener changeListener;
   private static final AuthenticatedSubject kernelId;

   public static Collection getDeployments() {
      return deployments.values();
   }

   public static BasicDeployment getOrCreateBasicDeployment(BasicDeploymentMBean var0) {
      if (var0 == null) {
         return null;
      } else {
         BasicDeployment var1 = (BasicDeployment)deployments.get(var0);
         if (var1 == null) {
            var1 = DeployHelper.createDeployment(var0);
            addDeployment(var0, var1);
            registerChangeListenerIfNeeded();
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("OrderedDeployments cannot find " + var0 + ", created AppDeployment=" + var1);
               if (var0 instanceof AppDeploymentMBean) {
                  Debug.deploymentDebug(var0 + " isActive=" + ApplicationVersionUtils.isActiveVersion((AppDeploymentMBean)var0));
               }
            }
         } else if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("OrderedDeployments found " + var0 + ", AppDeployment=" + var1);
            if (var0 instanceof AppDeploymentMBean) {
               Debug.deploymentDebug(var0 + " isActive=" + ApplicationVersionUtils.isActiveVersion((AppDeploymentMBean)var0));
            }
         }

         return var1;
      }
   }

   public static void removeBasicDeployment(BasicDeploymentMBean var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("OrderedDeployments remove " + var0);
         if (var0 instanceof AppDeploymentMBean) {
            Debug.deploymentDebug(var0 + " isActive=" + ApplicationVersionUtils.isActiveVersion((AppDeploymentMBean)var0));
         }
      }

      removeDeployment(var0);
   }

   public static void addDeployment(Object var0, Object var1) {
      deployments.put(var0, var1);
      if (var0 instanceof BasicDeploymentMBean) {
         BasicDeploymentMBean var2 = (BasicDeploymentMBean)var0;
         int var3 = var2.getDeploymentOrder();
         DeploymentOrder.addToDeploymentOrderCache((BasicDeploymentMBean)var0, var3);
      }

   }

   public static void removeDeployment(Object var0) {
      deployments.remove(var0);
      if (var0 instanceof BasicDeploymentMBean) {
         DeploymentOrder.removeFromDeploymentOrderCache((BasicDeploymentMBean)var0);
      }

   }

   private static DomainMBean getDomain() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain();
   }

   private static void refreshBasicDeployment(BasicDeploymentMBean var0) {
      if (var0 != null) {
         BasicDeployment var1 = (BasicDeployment)deployments.get(var0);
         if (var1 == null) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("OrderedDeployments refresh rtTreeBean=" + var0 + " cannot find BasicDeployment");
            }

         } else {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("OrderedDeployments refresh rtTreeBean=" + var0 + ", basicDep=" + var1);
            }

            BasicDeploymentMBean var2 = var1.getDeploymentMBean();
            removeDeployment(var2);
            var1.resetMBean(var0);
            addDeployment(var0, var1);
         }
      }
   }

   private static void registerChangeListenerIfNeeded() {
      if (changeListener == null) {
         changeListener = new BasicDeploymentChangeListener();
         getDomain().addPropertyChangeListener(changeListener);
      }

   }

   static {
      deployments = new TreeMap(DeploymentOrder.COMPARATOR);
      kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   }

   private static class BasicDeploymentChangeListener implements PropertyChangeListener {
      private BasicDeploymentChangeListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1 != null && var1.getNewValue() instanceof BasicDeploymentMBean[]) {
            ArrayUtils.computeDiff((Object[])((Object[])var1.getOldValue()), (Object[])((Object[])var1.getNewValue()), new ArrayUtils.DiffHandler() {
               public void addObject(Object var1) {
                  OrderedDeployments.refreshBasicDeployment((BasicDeploymentMBean)var1);
               }

               public void removeObject(Object var1) {
               }
            });
         }

      }

      // $FF: synthetic method
      BasicDeploymentChangeListener(Object var1) {
         this();
      }
   }
}
