package weblogic.management.deploy.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.targetserver.state.DeploymentState;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.AppDeploymentRuntimeMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.runtime.DeploymentProgressObjectMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;

public final class DeploymentManagerImpl extends DomainRuntimeMBeanDelegate implements DeploymentManagerMBean, PropertyChangeListener {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private int progressObjectsMaxCount = 20;
   private ArrayList<DeploymentProgressObjectMBean> progressObjects = new ArrayList();
   private Map<String, AppDeploymentRuntimeImpl> appDeploymentRuntimes = null;
   private MBeanNotificationInfo[] mbeanNotificationInfo = null;
   private NotificationGenerator notificationGenerator = null;
   private long notificationSequence = 0L;
   private long timerDelay = 60000L;
   private long timerPeriod = 60000L;
   private long removalTimeout = 3600000L;

   DeploymentManagerImpl(String var1) throws ManagementException {
      super(var1);
      this.initAppDeploymentRuntimes();
      TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new TimerListener() {
         public void timerExpired(Timer var1) {
            DeploymentManagerImpl.this.removeExpiredDeploymentProgressObjects();
         }
      }, this.timerDelay, this.timerPeriod);
   }

   private synchronized void removeExpiredDeploymentProgressObjects() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.progressObjects.size(); ++var2) {
         DeploymentProgressObjectImpl var3 = (DeploymentProgressObjectImpl)this.progressObjects.get(var2);
         long var4 = var3.getEndTime();
         if (var4 > 0L && System.currentTimeMillis() - var4 > this.removalTimeout) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("removing expired progress object:  " + var3.getApplicationName());
            }

            var3.clear();
         } else {
            var1.add(var3);
         }
      }

      this.progressObjects.clear();
      this.progressObjects = var1;
   }

   public synchronized AppDeploymentRuntimeMBean[] getAppDeploymentRuntimes() {
      return (AppDeploymentRuntimeMBean[])this.appDeploymentRuntimes.values().toArray(new AppDeploymentRuntimeMBean[0]);
   }

   public synchronized AppDeploymentRuntimeMBean lookupAppDeploymentRuntime(String var1) {
      return (AppDeploymentRuntimeMBean)this.appDeploymentRuntimes.get(var1);
   }

   public synchronized DeploymentProgressObjectMBean[] getDeploymentProgressObjects() {
      return (DeploymentProgressObjectMBean[])this.progressObjects.toArray(new DeploymentProgressObjectMBean[0]);
   }

   public synchronized void setMaximumDeploymentProgressObjectsCount(int var1) {
      this.progressObjectsMaxCount = var1;
   }

   public int getMaximumDeploymentProgressObjectsCount() {
      return this.progressObjectsMaxCount;
   }

   public synchronized void purgeCompletedDeploymentProgressObjects() {
      if (this.progressObjects.size() != 0) {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < this.progressObjects.size(); ++var2) {
            DeploymentProgressObjectImpl var3 = (DeploymentProgressObjectImpl)this.progressObjects.get(var2);
            String var4 = var3.getState();
            if (!"STATE_COMPLETED".equals(var4) && !"STATE_FAILED".equals(var4)) {
               var1.add(var3);
            } else {
               var3.clear();
            }
         }

         this.progressObjects.clear();
         this.progressObjects = var1;
      }
   }

   synchronized DeploymentProgressObjectMBean allocateDeploymentProgressObject(String var1, DeploymentTaskRuntimeMBean var2, AppDeploymentMBean var3) throws ManagementException {
      this.removeDeploymentProgressObject(var1);
      if (this.progressObjects.size() < this.progressObjectsMaxCount) {
         DeploymentProgressObjectImpl var4 = new DeploymentProgressObjectImpl(var1, var2, var3);
         this.progressObjects.add(var4);
         return var4;
      } else {
         throw new ManagementException("Max count reached");
      }
   }

   public synchronized void removeDeploymentProgressObject(String var1) {
      if (this.progressObjects.size() != 0 && var1 != null) {
         for(int var2 = 0; var2 < this.progressObjects.size(); ++var2) {
            if (var1.equals(((DeploymentProgressObjectMBean)this.progressObjects.get(var2)).getApplicationName())) {
               try {
                  ((RuntimeMBeanDelegate)this.progressObjects.get(var2)).unregister();
               } catch (Exception var4) {
               }

               this.progressObjects.remove(var2);
            }
         }

      }
   }

   private synchronized void initAppDeploymentRuntimes() {
      if (this.appDeploymentRuntimes == null) {
         this.appDeploymentRuntimes = new HashMap();
      } else {
         this.appDeploymentRuntimes.clear();
      }

      DomainRuntimeServiceMBean var1 = ManagementService.getDomainAccess(kernelId).getDomainRuntimeService();
      DomainMBean var2 = var1.getDomainConfiguration();
      AppDeploymentMBean[] var3 = var2.getAppDeployments();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            try {
               AppDeploymentRuntimeImpl var5 = new AppDeploymentRuntimeImpl(var3[var4]);
               this.appDeploymentRuntimes.put(var3[var4].getName(), var5);
               this.sendNotification("weblogic.appdeployment.created", var5);
            } catch (Exception var6) {
            }
         }
      }

      var2.addPropertyChangeListener(this);
      AppRuntimeStateManager.getManager().addStateListener(this);
   }

   private synchronized void initAppDeploymentRuntimes(AppDeploymentMBean[] var1) {
      if (var1 != null) {
         HashMap var2 = new HashMap();

         AppDeploymentRuntimeImpl var5;
         for(int var3 = 0; var3 < var1.length; ++var3) {
            AppDeploymentRuntimeImpl var4 = (AppDeploymentRuntimeImpl)this.appDeploymentRuntimes.get(var1[var3].getName());
            if (var4 != null) {
               var2.put(var1[var3].getName(), var4);
               this.appDeploymentRuntimes.remove(var1[var3].getName());
            } else {
               try {
                  var5 = new AppDeploymentRuntimeImpl(var1[var3]);
                  var2.put(var1[var3].getName(), var5);
                  this.sendNotification("weblogic.appdeployment.created", var5);
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            }
         }

         if (this.appDeploymentRuntimes != null) {
            Collection var9 = this.appDeploymentRuntimes.values();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
               var5 = (AppDeploymentRuntimeImpl)var10.next();

               try {
                  this.sendNotification("weblogic.appdeployment.deleted", var5);
                  var5.unregister();
               } catch (Exception var7) {
               }
            }
         }

         this.appDeploymentRuntimes.clear();
         this.appDeploymentRuntimes = var2;
      }

   }

   public void propertyChange(PropertyChangeEvent var1) {
      if ("AppDeployments".equals(var1.getPropertyName())) {
         AppDeploymentMBean[] var2 = (AppDeploymentMBean[])((AppDeploymentMBean[])var1.getNewValue());
         this.initAppDeploymentRuntimes(var2);
      } else if ("State".equals(var1.getPropertyName())) {
         DeploymentState var4 = (DeploymentState)var1.getNewValue();
         if (var4 != null) {
            AppDeploymentRuntimeImpl var3 = (AppDeploymentRuntimeImpl)this.appDeploymentRuntimes.get(var4.getId());
            if (var3 != null) {
               var3.sendNotification(var4);
            }

            if (this.notificationGenerator != null) {
               this.sendNotification(translateState(var4.getCurrentState()), (AppDeploymentRuntimeImpl)this.appDeploymentRuntimes.get(var4.getId()));
            }
         }
      }

   }

   void setNotificationGenerator(NotificationGenerator var1) {
      this.notificationGenerator = var1;
   }

   private void sendNotification(String var1, AppDeploymentRuntimeImpl var2) {
      if (this.notificationGenerator != null) {
         try {
            ++this.notificationSequence;
            Notification var3 = new Notification(var1, this.notificationGenerator.getObjectName(), this.notificationSequence);
            String var4 = "com.bea:Type=AppDeploymentRuntime,Name=" + var2.getName();
            var3.setUserData(var4);
            this.notificationGenerator.sendNotification(var3);
         } catch (Throwable var5) {
         }
      }

   }

   static String translateState(String var0) {
      if ("STATE_NEW".equals(var0)) {
         return "weblogic.appdeployment.state.new";
      } else if ("STATE_PREPARED".equals(var0)) {
         return "weblogic.appdeployment.state.prepared";
      } else if ("STATE_ADMIN".equals(var0)) {
         return "weblogic.appdeployment.state.admin";
      } else if ("STATE_ACTIVE".equals(var0)) {
         return "weblogic.appdeployment.state.active";
      } else if ("STATE_RETIRED".equals(var0)) {
         return "weblogic.appdeployment.state.retired";
      } else if ("STATE_FAILED".equals(var0)) {
         return "weblogic.appdeployment.state.failed";
      } else {
         return "STATE_UPDATE_PENDING".equals(var0) ? "weblogic.appdeployment.state.update.pending" : "weblogic.appdeployment.state.unknown";
      }
   }
}
