package weblogic.deploy.event;

import java.security.AccessController;
import java.util.EventListener;
import java.util.EventObject;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class BaseDeploymentEvent extends EventObject {
   private static final long serialVersionUID = -7782817013969844896L;
   private EventType type;
   private BasicDeploymentMBean deployMBean;
   private boolean isStaticAppDeployment;
   private String[] modules;
   private String[] targets;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   protected BaseDeploymentEvent(Object var1, EventType var2, BasicDeploymentMBean var3, boolean var4, String[] var5, String[] var6) {
      super(var1);
      this.type = var2;
      this.deployMBean = var3;
      this.isStaticAppDeployment = var4;
      if (var5 != null && var5.length > 0) {
         this.modules = var5;
      }

      if (var6 != null && var6.length > 0) {
         this.targets = var6;
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getClass().getName()).append("{").append(this.toStringContent()).append("}");
      return var1.toString();
   }

   protected String toStringContent() {
      StringBuffer var1 = new StringBuffer();
      var1.append("EventType=").append(this.type).append(",").append("BasicDeployment=").append(this.deployMBean).append(",").append("isStaticAppDeploy=").append(this.isStaticAppDeployment).append(",").append("Modules=").append(this.modules).append(",").append("Targets=").append(this.targets);
      return var1.toString();
   }

   public AppDeploymentMBean getAppDeployment() {
      return !(this.deployMBean instanceof AppDeploymentMBean) ? null : (AppDeploymentMBean)this.deployMBean;
   }

   public SystemResourceMBean getSystemResource() {
      return !(this.deployMBean instanceof SystemResourceMBean) ? null : (SystemResourceMBean)this.deployMBean;
   }

   public String[] getModules() {
      return this.modules;
   }

   public String[] getTargets() {
      return this.targets;
   }

   public boolean isAdminServer() {
      return ManagementService.getRuntimeAccess(kernelId).isAdminServer();
   }

   public boolean isStaticAppDeployment() {
      return this.isStaticAppDeployment;
   }

   public EventType getType() {
      return this.type;
   }

   ListenerAdapter getListenerAdapter() {
      return this.type.getListenerAdapter();
   }

   interface ListenerAdapter {
      void notifyListener(EventListener var1, BaseDeploymentEvent var2) throws DeploymentVetoException;
   }

   public static class EventType {
      private String name;
      private ListenerAdapter adapter;

      protected EventType(String var1, ListenerAdapter var2) {
         this.name = var1;
         this.adapter = var2;
      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      private ListenerAdapter getListenerAdapter() {
         return this.adapter;
      }
   }
}
