package weblogic.management.deploy.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.TargetStatus;
import weblogic.management.runtime.DeploymentProgressObjectMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;

public final class DeploymentProgressObjectImpl extends DomainRuntimeMBeanDelegate implements DeploymentProgressObjectMBean {
   DeploymentTaskRuntimeMBean deploymentTaskRuntime;
   AppDeploymentMBean deployable;
   private List<Throwable> exceptions = new ArrayList();

   public DeploymentProgressObjectImpl(String var1, DeploymentTaskRuntimeMBean var2, AppDeploymentMBean var3) throws ManagementException {
      super(var1);
      this.deploymentTaskRuntime = var2;
      this.deployable = var3;
   }

   public String getId() {
      return this.deploymentTaskRuntime != null ? this.deploymentTaskRuntime.getId() : null;
   }

   public int getOperationType() {
      if (this.deploymentTaskRuntime != null) {
         int var1 = this.deploymentTaskRuntime.getTask();
         if (var1 == 7) {
            return 1;
         }

         if (var1 == 8) {
            return 2;
         }
      }

      return 0;
   }

   public String getApplicationName() {
      return this.getName();
   }

   public AppDeploymentMBean getAppDeploymentMBean() {
      return this.deployable;
   }

   public String getState() {
      if (this.deploymentTaskRuntime != null) {
         int var1 = this.deploymentTaskRuntime.getState();
         if (0 == var1) {
            return "STATE_INITIALIZED";
         }

         if (1 == var1) {
            return "STATE_RUNNING";
         }

         if (2 == var1) {
            return "STATE_COMPLETED";
         }

         if (3 == var1) {
            return "STATE_FAILED";
         }

         if (4 == var1) {
            return "STATE_DEFERRED";
         }
      }

      return "Unknown";
   }

   public String[] getTargets() {
      ArrayList var1 = new ArrayList();
      if (this.deploymentTaskRuntime != null) {
         TargetStatus[] var2 = this.deploymentTaskRuntime.getTargets();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               var1.add(var2[var3].getTarget());
            }
         }
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   public String[] getFailedTargets() {
      ArrayList var1 = new ArrayList();
      if (this.deploymentTaskRuntime != null && this.deploymentTaskRuntime instanceof DeploymentTaskRuntime) {
         Map var2 = ((DeploymentTaskRuntime)this.deploymentTaskRuntime).getFailedTargets();
         if (var2 != null) {
            Set var3 = var2.keySet();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               var1.add((String)var4.next());
            }
         }
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   public String[] getMessages() {
      return this.deploymentTaskRuntime != null ? (String[])((String[])this.deploymentTaskRuntime.getTaskMessages().toArray(new String[0])) : new String[0];
   }

   public RuntimeException[] getExceptions(String var1) {
      ArrayList var2 = new ArrayList();
      if (this.deploymentTaskRuntime != null) {
         Exception[] var3 = this.deploymentTaskRuntime.findTarget(var1).getMessages();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var2.add(ExceptionTranslator.translateException(var3[var4]));
         }
      }

      return (RuntimeException[])var2.toArray(new RuntimeException[0]);
   }

   public RuntimeException[] getRootExceptions() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.exceptions.size(); ++var2) {
         var1.add(ExceptionTranslator.translateException((Throwable)this.exceptions.get(var2)));
      }

      return (RuntimeException[])var1.toArray(new RuntimeException[0]);
   }

   public void cancel() throws RuntimeException {
      if (this.deploymentTaskRuntime != null) {
         try {
            this.deploymentTaskRuntime.cancel();
         } catch (Throwable var2) {
            throw ExceptionTranslator.translateException(var2);
         }
      }

   }

   public void setDeploymentTaskRuntime(DeploymentTaskRuntimeMBean var1) {
      this.deploymentTaskRuntime = var1;
   }

   public DeploymentTaskRuntimeMBean getDeploymentTaskRuntime() {
      return this.deploymentTaskRuntime;
   }

   public void clear() {
      this.deploymentTaskRuntime = null;

      try {
         this.unregister();
      } catch (Exception var2) {
      }

   }

   public long getBeginTime() {
      return this.deploymentTaskRuntime != null && this.deploymentTaskRuntime instanceof DeploymentTaskRuntime ? ((DeploymentTaskRuntime)this.deploymentTaskRuntime).getBeginTime() : 0L;
   }

   public long getEndTime() {
      return this.deploymentTaskRuntime != null && this.deploymentTaskRuntime instanceof DeploymentTaskRuntime ? ((DeploymentTaskRuntime)this.deploymentTaskRuntime).getEndTime() : 0L;
   }

   public void addException(Throwable var1) {
      this.exceptions.add(var1);
   }
}
