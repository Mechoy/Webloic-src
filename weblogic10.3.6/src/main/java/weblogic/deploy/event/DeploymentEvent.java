package weblogic.deploy.event;

import java.util.EventListener;
import weblogic.deploy.common.Debug;
import weblogic.management.configuration.AppDeploymentMBean;

public class DeploymentEvent extends BaseDeploymentEvent {
   private static final long serialVersionUID = 5996987879320598338L;
   public static final BaseDeploymentEvent.EventType APP_ACTIVATED = new BaseDeploymentEvent.EventType("AppActivated", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (DeploymentEvent.isDebugEnabled()) {
            DeploymentEvent.debug("EventType('AppActivated'): applicationActivated() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         DeploymentEventListener var3 = (DeploymentEventListener)var1;
         var3.applicationDeployed((DeploymentEvent)var2);
      }
   });
   public static final BaseDeploymentEvent.EventType APP_DEPLOYED = new BaseDeploymentEvent.EventType("AppDeployed", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (DeploymentEvent.isDebugEnabled()) {
            DeploymentEvent.debug("EventType('AppDeployed'): applicationDeployed() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         ((DeploymentEventListener)var1).applicationDeployed((DeploymentEvent)var2);
      }
   });
   public static final BaseDeploymentEvent.EventType APP_REDEPLOYED = new BaseDeploymentEvent.EventType("AppRedeployed", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (DeploymentEvent.isDebugEnabled()) {
            DeploymentEvent.debug("EventType('AppRedeployed'): applicationRedeployed() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         ((DeploymentEventListener)var1).applicationRedeployed((DeploymentEvent)var2);
      }
   });
   public static final BaseDeploymentEvent.EventType APP_STARTED = new BaseDeploymentEvent.EventType("AppStarted", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (DeploymentEvent.isDebugEnabled()) {
            DeploymentEvent.debug("EventType('AppStarted'): applicationStarted() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         DeploymentEventListener var3 = (DeploymentEventListener)var1;
         if (!(var3 instanceof ApplicationVersionLifecycleListenerAdapter)) {
            var3.applicationDeployed((DeploymentEvent)var2);
         }

      }
   });
   public static final BaseDeploymentEvent.EventType APP_DELETED = new BaseDeploymentEvent.EventType("AppDeleted", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (DeploymentEvent.isDebugEnabled()) {
            DeploymentEvent.debug("EventType('AppDeleted'): applicationDeleted() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         ((DeploymentEventListener)var1).applicationDeleted((DeploymentEvent)var2);
      }
   });

   private DeploymentEvent(Object var1, BaseDeploymentEvent.EventType var2, AppDeploymentMBean var3, boolean var4, String[] var5, String[] var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public static DeploymentEvent create(Object var0, BaseDeploymentEvent.EventType var1, AppDeploymentMBean var2, String[] var3, String[] var4) {
      return new DeploymentEvent(var0, var1, var2, false, var3, var4);
   }

   public static DeploymentEvent create(Object var0, BaseDeploymentEvent.EventType var1, AppDeploymentMBean var2, boolean var3, String[] var4, String[] var5) {
      return new DeploymentEvent(var0, var1, var2, var3, var4, var5);
   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   private static void debug(String var0) {
      Debug.deploymentDebug("<DeploymentEvent>: " + var0);
   }
}
