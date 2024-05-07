package weblogic.deploy.event;

import java.util.EventListener;
import weblogic.deploy.common.Debug;
import weblogic.management.configuration.BasicDeploymentMBean;

public class VetoableDeploymentEvent extends BaseDeploymentEvent {
   private static final long serialVersionUID = 2451377168814537861L;
   public static final BaseDeploymentEvent.EventType APP_ACTIVATE = new BaseDeploymentEvent.EventType("AppActivated", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (VetoableDeploymentEvent.isDebugEnabled()) {
            VetoableDeploymentEvent.debug("EventType('AppActivated'): vetoableApplicationActivate() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         VetoableDeploymentListener var3 = (VetoableDeploymentListener)var1;
         var3.vetoableApplicationDeploy((VetoableDeploymentEvent)var2);
      }
   });
   public static final BaseDeploymentEvent.EventType APP_DEPLOY = new BaseDeploymentEvent.EventType("AppDeployed", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (VetoableDeploymentEvent.isDebugEnabled()) {
            VetoableDeploymentEvent.debug("EventType('AppDeployed'): vetoableApplicationDeploy() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         ((VetoableDeploymentListener)var1).vetoableApplicationDeploy((VetoableDeploymentEvent)var2);
      }
   });
   public static final BaseDeploymentEvent.EventType APP_START = new BaseDeploymentEvent.EventType("AppStarted", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (VetoableDeploymentEvent.isDebugEnabled()) {
            VetoableDeploymentEvent.debug("EventType('AppStarted'): vetoableApplicationStart() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         VetoableDeploymentListener var3 = (VetoableDeploymentListener)var1;
         if (!(var3 instanceof ApplicationVersionLifecycleListenerAdapter)) {
            var3.vetoableApplicationDeploy((VetoableDeploymentEvent)var2);
         }

      }
   });
   public static final BaseDeploymentEvent.EventType APP_UNDEPLOY = new BaseDeploymentEvent.EventType("AppDeleted", new BaseDeploymentEvent.ListenerAdapter() {
      public void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException {
         if (VetoableDeploymentEvent.isDebugEnabled()) {
            VetoableDeploymentEvent.debug("EventType('AppDeleted'): vetoableApplicationUndeploy() fired on : '" + var1 + "' for the event : '" + var2 + "'");
         }

         ((VetoableDeploymentListener)var1).vetoableApplicationUndeploy((VetoableDeploymentEvent)var2);
      }
   });
   private final boolean isNewAppDeployment;
   private final SecurityInfo securityInfo = new SecurityInfo(this);

   private VetoableDeploymentEvent(Object var1, BaseDeploymentEvent.EventType var2, BasicDeploymentMBean var3, boolean var4, boolean var5, String[] var6, String[] var7) {
      super(var1, var2, var3, var4, var6, var7);
      this.isNewAppDeployment = var5;
   }

   protected String toStringContent() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toStringContent()).append(",").append("isNewApp=").append(this.isNewAppDeployment).append(",").append("SecurityInfo=").append(this.securityInfo);
      return var1.toString();
   }

   public static VetoableDeploymentEvent create(Object var0, BaseDeploymentEvent.EventType var1, BasicDeploymentMBean var2, boolean var3, String[] var4, String[] var5) {
      return new VetoableDeploymentEvent(var0, var1, var2, false, var3, var4, var5);
   }

   public static VetoableDeploymentEvent create(Object var0, BaseDeploymentEvent.EventType var1, BasicDeploymentMBean var2, boolean var3, boolean var4, String[] var5, String[] var6) {
      return new VetoableDeploymentEvent(var0, var1, var2, var3, var4, var5, var6);
   }

   public boolean isNewAppDeployment() {
      return this.isNewAppDeployment;
   }

   public SecurityInfo getSecurityInfo() {
      return this.securityInfo;
   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   private static void debug(String var0) {
      Debug.deploymentDebug("<VetoableDeploymentEvent>: " + var0);
   }
}
