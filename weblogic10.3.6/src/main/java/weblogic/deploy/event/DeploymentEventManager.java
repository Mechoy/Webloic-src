package weblogic.deploy.event;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventListenerProxy;
import java.util.Iterator;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DeploymentEventManager {
   private static Object deployEventListenersLock = "DeployEventListeners";
   private static ArrayList deployEventListeners = new ArrayList();
   private static Object vetoableDeployListenersLock = "VetoDeployListeners";
   private static ArrayList vetoableDeployListeners = new ArrayList();
   private static Object vetoableSystemResourceListenersLock = "VetoSystemResourceListeners";
   private static ArrayList vetoableSystemResourceListeners = new ArrayList();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public static void addDeploymentEventListener(DeploymentEventListener var0) {
      internalAddDeploymentEventListener(var0, false);
   }

   public static void addDeploymentEventListener(DeploymentEventListener var0, boolean var1) {
      internalAddDeploymentEventListener(var0, var1);
   }

   public static void addDeploymentEventListener(String var0, DeploymentEventListener var1) throws DeploymentException {
      internalAddDeploymentEventListener(new DeploymentEventListenerProxy(var0, var1), false);
   }

   private static void internalAddDeploymentEventListener(EventListener var0, boolean var1) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Add DeploymentEventListener " + var0);
      }

      if (var0 != null) {
         if (!var1 || ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
            synchronized(deployEventListenersLock) {
               ArrayList var3 = new ArrayList(deployEventListeners);
               var3.add(var0);
               deployEventListeners = var3;
            }
         }
      }
   }

   public static boolean removeDeploymentEventListener(DeploymentEventListener var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Remove DeploymentEventListener " + var0);
      }

      if (var0 == null) {
         return false;
      } else {
         synchronized(deployEventListenersLock) {
            if (deployEventListeners.size() == 0) {
               return false;
            } else {
               ArrayList var2 = new ArrayList(deployEventListeners);
               boolean var3 = removeListener(var0, var2);
               deployEventListeners = var2;
               return var3;
            }
         }
      }
   }

   public static void sendDeploymentEvent(DeploymentEvent var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Send deployment event: " + var0);
      }

      if (var0 != null) {
         ArrayList var1;
         synchronized(deployEventListenersLock) {
            var1 = deployEventListeners;
         }

         BaseDeploymentEvent.ListenerAdapter var2 = var0.getListenerAdapter();
         Throwable var3 = null;
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            EventListener var5 = (EventListener)var4.next();
            if (isEventEnabled(var5, var0)) {
               try {
                  var2.notifyListener(getListener(var5), var0);
               } catch (Throwable var8) {
                  Throwable var7 = new Throwable(var8.toString());
                  var7.setStackTrace(var8.getStackTrace());
                  var7.initCause(var3);
                  var3 = var7;
               }
            }
         }

         if (var3 != null) {
            DeployerRuntimeLogger.logSendDeploymentEventError(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var0.getAppDeployment()), var0.getType().getName(), var3);
         }

      }
   }

   public static void addVetoableDeploymentListener(VetoableDeploymentListener var0) throws DeploymentException {
      internalAddVetoableDeploymentListener(var0);
   }

   public static void addVetoableDeploymentListener(String var0, VetoableDeploymentListener var1) throws DeploymentException {
      internalAddVetoableDeploymentListener(new DeploymentEventListenerProxy(var0, var1));
   }

   private static void internalAddVetoableDeploymentListener(EventListener var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Add VetoableDeploymentListener: " + var0);
      }

      if (var0 != null) {
         synchronized(vetoableDeployListenersLock) {
            ArrayList var2 = new ArrayList(vetoableDeployListeners);
            var2.add(var0);
            vetoableDeployListeners = var2;
         }
      }
   }

   public static boolean removeVetoableDeploymentListener(VetoableDeploymentListener var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Remove VetoableDeploymentListener: " + var0);
      }

      if (var0 == null) {
         return false;
      } else {
         synchronized(vetoableDeployListenersLock) {
            if (vetoableDeployListeners.size() == 0) {
               return false;
            } else {
               ArrayList var2 = new ArrayList(vetoableDeployListeners);
               boolean var3 = removeListener(var0, var2);
               vetoableDeployListeners = var2;
               return var3;
            }
         }
      }
   }

   public static void addVetoableSystemResourceDeploymentListener(VetoableDeploymentListener var0) throws DeploymentException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Add VetoableSystemResourceDeploymentListener: " + var0);
      }

      if (var0 != null) {
         synchronized(vetoableSystemResourceListenersLock) {
            ArrayList var2 = new ArrayList(vetoableSystemResourceListeners);
            var2.add(var0);
            vetoableSystemResourceListeners = var2;
         }
      }
   }

   public static boolean removeVetoableSystemResourceDeploymentListener(VetoableDeploymentListener var0) {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Remove VetoableSystemResourceDeploymentListener: " + var0);
      }

      if (var0 == null) {
         return false;
      } else {
         synchronized(vetoableSystemResourceListenersLock) {
            if (vetoableSystemResourceListeners.size() == 0) {
               return false;
            } else {
               ArrayList var2 = new ArrayList(vetoableSystemResourceListeners);
               boolean var3 = removeListener(var0, var2);
               vetoableSystemResourceListeners = var2;
               return var3;
            }
         }
      }
   }

   public static void sendVetoableDeploymentEvent(VetoableDeploymentEvent var0) throws DeploymentException {
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Send vetoable deploy event: " + var0);
      }

      if (var0 != null) {
         ArrayList var1 = null;
         Object var2 = var0.getAppDeployment();
         if (var2 != null) {
            synchronized(vetoableDeployListenersLock) {
               var1 = vetoableDeployListeners;
            }
         } else {
            var2 = var0.getSystemResource();
            if (var2 != null) {
               synchronized(vetoableSystemResourceListenersLock) {
                  var1 = vetoableSystemResourceListeners;
               }
            }
         }

         if (var1 != null) {
            BaseDeploymentEvent.ListenerAdapter var3 = var0.getListenerAdapter();
            Throwable var4 = null;
            Iterator var5 = var1.iterator();

            while(var5.hasNext()) {
               EventListener var6 = (EventListener)var5.next();
               if (isEventEnabled(var6, var0)) {
                  try {
                     var3.notifyListener(getListener(var6), var0);
                  } catch (DeploymentVetoException var9) {
                     throw new DeploymentException(var9.getMessage(), var9);
                  } catch (Throwable var10) {
                     Throwable var8 = new Throwable(var10.toString());
                     var8.setStackTrace(var10.getStackTrace());
                     var8.initCause(var4);
                     var4 = var8;
                  }
               }
            }

            if (var4 != null) {
               DeployerRuntimeLogger.logSendVetoableDeployEventError(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var2), var4);
            }

         }
      }
   }

   private static boolean removeListener(EventListener var0, ArrayList var1) {
      boolean var2 = false;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EventListener var4 = (EventListener)var3.next();
         if (var4 instanceof EventListenerProxy) {
            var4 = ((EventListenerProxy)var4).getListener();
         }

         if (var4 == var0) {
            var3.remove();
            var2 = true;
            break;
         }
      }

      return var2;
   }

   private static boolean isEventEnabled(EventListener var0, BaseDeploymentEvent var1) {
      if (!(var0 instanceof DeploymentEventListenerProxy)) {
         return true;
      } else {
         String var2 = ((DeploymentEventListenerProxy)var0).getAppName();
         AppDeploymentMBean var3 = var1.getAppDeployment();
         return var3 != null && (var3.getApplicationName().equals(var2) || var3.getName().equals(var2));
      }
   }

   private static EventListener getListener(EventListener var0) {
      return var0 instanceof EventListenerProxy ? ((EventListenerProxy)var0).getListener() : var0;
   }
}
