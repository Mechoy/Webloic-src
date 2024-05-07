package weblogic.management.deploy.internal;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.common.Debug;
import weblogic.deploy.internal.adminserver.operations.AbstractOperation;
import weblogic.deploy.internal.adminserver.operations.ActivateOperation;
import weblogic.deploy.internal.adminserver.operations.DeactivateOperation;
import weblogic.deploy.internal.adminserver.operations.DeployOperation;
import weblogic.deploy.internal.adminserver.operations.DistributeOperation;
import weblogic.deploy.internal.adminserver.operations.RedeployOperation;
import weblogic.deploy.internal.adminserver.operations.RemoveOperation;
import weblogic.deploy.internal.adminserver.operations.RetireOperation;
import weblogic.deploy.internal.adminserver.operations.StartOperation;
import weblogic.deploy.internal.adminserver.operations.StopForGracefulRetireOperation;
import weblogic.deploy.internal.adminserver.operations.StopOperation;
import weblogic.deploy.internal.adminserver.operations.UnprepareOperation;
import weblogic.deploy.internal.adminserver.operations.UpdateOperation;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.descriptor.DescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.ManagementException;
import weblogic.management.TargetAvailabilityStatus;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebDeploymentMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.deploy.DeploymentTaskRuntime;
import weblogic.management.deploy.status.StatusFactory;
import weblogic.management.provider.DomainAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RegistrationHandler;
import weblogic.management.provider.Service;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.AssertionError;
import weblogic.work.WorkManagerFactory;

public final class DeployerRuntimeImpl extends DomainRuntimeMBeanDelegate implements DeployerRuntimeMBean {
   private static final int RETIRE_TIME = Integer.parseInt(System.getProperty("weblogic.management.deploy.taskRetireTime", "300"));
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final long serialVersionUID = 3609892051956468810L;
   private static final Map taskMap = new LinkedHashMap();
   private Timer tasksRetirePoller = null;
   private int nextId = 0;

   public DeployerRuntimeImpl() throws ManagementException {
      throw new AssertionError("Constructor not valid on singleton MBean");
   }

   DeployerRuntimeImpl(String var1) throws ManagementException {
      super(var1);
      DomainAccess var2 = ManagementService.getDomainAccess(kernelId);
      var2.addRegistrationHandler(new RegistrationHandler() {
         public void registered(RuntimeMBean var1, DescriptorBean var2) {
         }

         public void unregistered(RuntimeMBean var1) {
            if (var1 instanceof DeploymentTaskRuntimeMBean) {
               DeployerRuntimeImpl.taskMap.remove(((DeploymentTaskRuntimeMBean)var1).getId());
            }

         }

         public void registeredCustom(ObjectName var1, Object var2) {
         }

         public void unregisteredCustom(ObjectName var1) {
         }

         public void registered(Service var1) {
         }

         public void unregistered(Service var1) {
         }
      });
      this.tasksRetirePoller = this.startTasksRetirePoller();
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Created deployer: " + this);
      }

   }

   private Timer startTasksRetirePoller() {
      long var1 = 30000L;
      Timer var3 = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.management.TasksRetirePoller", WorkManagerFactory.getInstance().getSystem()).schedule(new TasksRetirePoller(), 0L, var1);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Started TasksRetirePoller to execute  repeatedly for every 30 seconds");
      }

      return var3;
   }

   private void checkAndMarkRetiredTasks() {
      Iterator var1 = taskMap.values().iterator();

      while(var1.hasNext()) {
         DeploymentTaskRuntimeMBean var2 = (DeploymentTaskRuntimeMBean)var1.next();
         if (!var2.isRetired() && !var2.isRunning()) {
            long var3 = var2.getEndTime();
            long var5 = var3 + (long)(RETIRE_TIME * 1000);
            long var7 = System.currentTimeMillis();
            if (var7 >= var5) {
               var2.setRetired();
            }
         }
      }

   }

   public DeploymentTaskRuntimeMBean activate(String var1, String var2, String var3, DeploymentData var4, String var5) throws ManagementException {
      return this.activate(var1, var2, var3, var4, var5, true);
   }

   public DeploymentTaskRuntimeMBean activate(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      this.checkDeployerActions(var5, var2, 1);
      return this.performDeployerActions(var1, var2, var3, var4, var5, var6, new ActivateOperation());
   }

   public DeploymentTaskRuntimeMBean deactivate(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.deactivate(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean deactivate(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 3);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new DeactivateOperation());
   }

   public DeploymentTaskRuntimeMBean remove(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.remove(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean remove(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      byte var5 = 4;
      this.checkDeployerActions(var3, var1, var5);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new RemoveOperation(var5));
   }

   public DeploymentTaskRuntimeMBean unprepare(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.unprepare(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean unprepare(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 5);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new UnprepareOperation());
   }

   public DeploymentTaskRuntimeMBean distribute(String var1, String var2, DeploymentData var3, String var4) throws ManagementException {
      return this.distribute(var1, var2, var3, var4, true);
   }

   public DeploymentTaskRuntimeMBean distribute(String var1, String var2, DeploymentData var3, String var4, boolean var5) throws ManagementException {
      var2 = this.ensureAppName(var2);
      this.assertNameIsNonNull(var2, DeployHelper.getTaskName(6));
      this.checkDeployerActions(var4, var2, 6);
      String var6 = DeployHelper.getStagingModeFromOptions(var3);
      return this.performDeployerActions(var1, var2, var6, var3, var4, var5, new DistributeOperation());
   }

   public DeploymentTaskRuntimeMBean deploy(String var1, String var2, String var3, DeploymentData var4, String var5) throws ManagementException {
      return this.deploy(var1, var2, var3, var4, var5, true);
   }

   public DeploymentTaskRuntimeMBean deploy(String var1, String var2, String var3, DeploymentData var4, String var5, boolean var6) throws ManagementException {
      this.checkDeployerActions(var5, var2, 11);
      return this.performDeployerActions(var1, var2, var3, var4, var5, var6, new DeployOperation());
   }

   public DeploymentTaskRuntimeMBean undeploy(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.undeploy(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean undeploy(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      byte var5 = 12;
      this.checkDeployerActions(var3, var1, var5);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new RemoveOperation(var5));
   }

   public DeploymentTaskRuntimeMBean start(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.start(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean start(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      var1 = this.ensureAppName(var1);
      this.assertNameIsNonNull(var1, DeployHelper.getTaskName(7));
      this.checkDeployerActions(var3, var1, 7);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new StartOperation());
   }

   public DeploymentTaskRuntimeMBean stop(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.stop(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean stop(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 8);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new StopOperation());
   }

   public DeploymentTaskRuntimeMBean redeploy(String var1, DeploymentData var2, String var3) throws ManagementException {
      return this.redeploy(var1, var2, var3, true);
   }

   public DeploymentTaskRuntimeMBean redeploy(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 9);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new RedeployOperation());
   }

   public DeploymentTaskRuntimeMBean redeploy(String var1, String var2, DeploymentData var3, String var4, boolean var5) throws ManagementException {
      this.checkDeployerActions(var4, var2, 9);
      return this.performDeployerActions(var1, var2, (String)null, var3, var4, var5, new DeployOperation(true));
   }

   public DeploymentTaskRuntimeMBean update(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 10);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new UpdateOperation());
   }

   public DeploymentTaskRuntimeMBean stopForGracefulRetire(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 8);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new StopForGracefulRetireOperation());
   }

   public DeploymentTaskRuntimeMBean retire(String var1, DeploymentData var2, String var3, boolean var4) throws ManagementException {
      this.checkDeployerActions(var3, var1, 13);
      return this.performDeployerActions((String)null, var1, (String)null, var2, var3, var4, new RetireOperation());
   }

   public DeploymentTaskRuntimeMBean[] list() {
      return (DeploymentTaskRuntimeMBean[])((DeploymentTaskRuntimeMBean[])taskMap.values().toArray(new DeploymentTaskRuntimeMBean[taskMap.size()]));
   }

   public DeploymentTaskRuntimeMBean[] getDeploymentTaskRuntimes() {
      return this.list();
   }

   public DeploymentTaskRuntimeMBean query(String var1) {
      DeploymentTaskRuntime var2 = this.getTaskRuntime(var1);
      return var2;
   }

   public boolean removeTask(String var1) {
      if (var1 != null && var1.length() != 0) {
         try {
            DeploymentTaskRuntime var2 = this.getTaskRuntime(var1);
            if (var2 != null) {
               ((DomainRuntimeMBeanDelegate)var2).unregister();
               return true;
            }
         } catch (Exception var3) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Remove DeploymentTaskRuntimeMBean failed for id: " + var1 + ", ex:" + var3.getMessage());
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public String[] purgeRetiredTasks() {
      ArrayList var1 = new ArrayList();
      List var2 = this.getRetiredTasks();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         DeploymentTaskRuntimeMBean var4 = (DeploymentTaskRuntimeMBean)var3.next();
         String var5 = var4.getId();
         this.removeTask(var5);
         var1.add(var5);
      }

      if (var1.size() == 0) {
         return new String[0];
      } else {
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug(" +++ taskIds removed : " + var1);
         }

         String[] var6 = new String[var1.size()];
         var6 = (String[])((String[])var1.toArray(var6));
         return var6;
      }
   }

   private List getRetiredTasks() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = taskMap.values().iterator();

      while(var2.hasNext()) {
         DeploymentTaskRuntimeMBean var3 = (DeploymentTaskRuntimeMBean)var2.next();
         if (var3.isRetired()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private void assertNameIsNonNull(String var1, String var2) throws ManagementException {
      if (var1 == null) {
         Loggable var3 = DeployerRuntimeLogger.logNullAppLoggable("null", var2);
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   private synchronized int getNextTaskId() {
      return this.nextId++;
   }

   public DeploymentTaskRuntime getTaskRuntime(String var1) {
      return (DeploymentTaskRuntime)taskMap.get(var1);
   }

   public void registerTaskRuntime(String var1, DeploymentTaskRuntimeMBean var2) {
      taskMap.put(var1, var2);
   }

   private void ensureTaskIsNotAlreadyRunning(String var1, int var2) throws ManagementException {
      if (var1 != null) {
         DeploymentTaskRuntime var3 = this.getTaskRuntime(var1);
         if (var3 != null) {
            Loggable var4 = DeployerRuntimeLogger.logTaskInUseLoggable(var1, DeployHelper.getTaskName(var2), this.name);
            var4.log();
            throw new ManagementException(var4.getMessage());
         }
      }
   }

   private void ensureDeploymentServiceStarted(String var1, int var2) throws ManagementException {
      if (!DeploymentServerService.isStarted()) {
         Loggable var3 = DeployerRuntimeLogger.logDeploymentServiceNotStartedLoggable(var1, DeployHelper.getTaskName(var2));
         var3.log();
         throw new ManagementException(var3.getMessage());
      }
   }

   private void ensureNoTaskConflict(String var1, int var2) throws ManagementException {
      if (7 == var2) {
         DeploymentTaskRuntimeMBean[] var3 = this.list();
         if (var3.length > 0) {
            for(int var4 = 0; var4 < var3.length; ++var4) {
               if (var3[var4].getState() == 1 && var3[var4].getTask() == 8 && var3[var4].getApplicationName().equals(var1)) {
                  Loggable var5 = DeployerRuntimeLogger.logTaskConflictLoggable(var1, DeployHelper.getTaskName(var2), DeployHelper.getTaskName(var3[var4].getTask()));
                  var5.log();
                  throw new ManagementException(var5.getMessage());
               }
            }
         }
      }

   }

   private void checkDeployerActions(String var1, String var2, int var3) throws ManagementException {
      this.ensureTaskIsNotAlreadyRunning(var1, var3);
      this.ensureDeploymentServiceStarted(var2, var3);
      this.ensureNoTaskConflict(var2, var3);
   }

   private DeploymentTaskRuntimeMBean performDeployerActions(final String var1, final String var2, final String var3, DeploymentData var4, final String var5, final boolean var6, final AbstractOperation var7) throws ManagementException {
      if (var5 == null || var5.equals(" ")) {
         var5 = Integer.toString(this.getNextTaskId());
         if (Debug.isDeploymentDebugEnabled()) {
            Debug.deploymentDebug("Creating DeploymentTaskRuntime with id: " + var5);
         }
      }

      final AuthenticatedSubject var8 = SecurityServiceManager.getCurrentSubject(kernelId);
      if (Debug.isDeploymentDebugEnabled()) {
         Debug.deploymentDebug("Deployment operation subject: " + var8);
      }

      final DeploymentData var9 = var4 == null ? new DeploymentData() : var4;
      Object var11 = SecurityServiceManager.runAs(kernelId, var8, new PrivilegedAction() {
         public Object run() {
            Object var1x;
            try {
               var1x = var7.execute(var1, var2, var3, var9, var5, var6, var8);
            } catch (ManagementException var3x) {
               var1x = var3x;
            }

            return var1x;
         }
      });
      if (var11 instanceof ManagementException) {
         throw (ManagementException)var11;
      } else {
         return (DeploymentTaskRuntimeMBean)var11;
      }
   }

   public DeploymentMBean[] getDeployments(TargetMBean var1) {
      return AppMBeanUsages.getDeployments(var1);
   }

   private String ensureAppName(String var1) {
      return ApplicationVersionUtils.getApplicationName(var1);
   }

   public Map getAvailabilityStatusForApplication(String var1, boolean var2) throws InstanceNotFoundException {
      HashMap var3 = new HashMap();
      ApplicationMBean var4 = ApplicationVersionUtils.getActiveAppDeployment(var1).getAppMBean();
      ComponentMBean[] var5 = var4.getComponents();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            Map var7 = this.getAvailabilityStatusForComponent(var5[var6], false);
            var3.put(var5[var6].getName(), var7);
         }
      }

      return var3;
   }

   public Map getAvailabilityStatusForComponent(ComponentMBean var1, boolean var2) throws InstanceNotFoundException {
      HashMap var3 = new HashMap();
      ApplicationMBean var4 = ApplicationVersionUtils.getActiveAppDeployment(var1.getApplication().getName()).getAppMBean();
      TargetMBean[] var5 = var1.getTargets();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length; ++var6) {
            TargetAvailabilityStatus var7 = this.getAvailabilityStatusForComponentTarget(var4, var1, var5[var6]);
            var3.put(var5[var6].getName(), var7);
         }
      }

      if (var1 instanceof WebDeploymentMBean) {
         VirtualHostMBean[] var16 = ((WebDeploymentMBean)var1).getVirtualHosts();
         if (var16 != null) {
            for(int var18 = 0; var18 < var16.length; ++var18) {
               TargetAvailabilityStatus var8 = this.getAvailabilityStatusForComponentTarget(var4, var1, var16[var18]);
               var3.put(var16[var18].getName(), var8);
            }
         }
      }

      ArrayList var17 = new ArrayList();
      TargetMBean[] var19 = var1.getActivatedTargets();
      if (var19 != null) {
         for(int var20 = 0; var20 < var19.length; ++var20) {
            String var9 = var19[var20].getName();
            if (!var3.containsKey(var9) && !var17.contains(var9)) {
               Object var10 = null;
               DomainMBean var11 = ManagementService.getRuntimeAccess(kernelId).getDomain();
               ServerMBean var12 = var11.lookupServer(var9);
               ClusterMBean var13 = null;
               VirtualHostMBean var14 = null;
               if (var12 != null) {
                  var10 = var12;
               } else if ((var13 = var11.lookupCluster(var9)) != null) {
                  var10 = var13;
               } else if ((var14 = var11.lookupVirtualHost(var9)) != null) {
                  var10 = var14;
               }

               if (var10 != null) {
                  TargetAvailabilityStatus var15 = this.getAvailabilityStatusForComponentTarget(var4, var1, (ConfigurationMBean)var10);
                  if (var15 != null) {
                     var3.put(var9, var15);
                  }
               }
            }
         }
      }

      return var3;
   }

   private TargetAvailabilityStatus getAvailabilityStatusForComponentTarget(ApplicationMBean var1, ComponentMBean var2, ConfigurationMBean var3) throws InstanceNotFoundException {
      TargetAvailabilityStatus var4 = StatusFactory.getInstance().createStatus(var1, var3);
      boolean var5 = var1.getStagingMode().equals("stage");
      String var6 = var3.getName();
      TargetMBean[] var7 = var2.getActivatedTargets();
      if (var3 instanceof VirtualHostMBean) {
         TargetMBean[] var8 = ((VirtualHostMBean)var3).getTargets();

         for(int var9 = 0; var9 < var8.length; ++var9) {
            this.updateAvailabilityStatusFromActivatedTargets(var7, var3, var8[var9].getName(), var4);
         }
      } else {
         this.updateAvailabilityStatusFromActivatedTargets(var7, var3, var6, var4);
      }

      if (var5) {
         String[] var10 = var2.getApplication().getStagedTargets();
         var4.updateUnavailabilityStatus(Arrays.asList(var10));
      }

      if (Debug.isDeploymentDebugEnabled()) {
         this.printStatus(var4);
      }

      return var4;
   }

   private void updateAvailabilityStatusFromActivatedTargets(TargetMBean[] var1, ConfigurationMBean var2, String var3, TargetAvailabilityStatus var4) {
      for(int var5 = 0; var5 < var1.length; ++var5) {
         if (var1[var5].getName().equals(var3)) {
            Set var6 = createComponentTarget(var1[var5], var2);
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
               ComponentTarget var8 = (ComponentTarget)var7.next();
               var4.updateAvailabilityStatus(var8);
            }

            return;
         }
      }

   }

   private static Set createComponentTarget(TargetMBean var0, ConfigurationMBean var1) {
      HashSet var2 = new HashSet();
      ServerMBean[] var3;
      int var4;
      if (var1 != null && var1 instanceof VirtualHostMBean) {
         if (var0 instanceof ServerMBean) {
            var2.add(new ComponentTarget(var1.getName(), var0.getName(), 3));
         } else if (var0 instanceof ClusterMBean) {
            var3 = ((ClusterMBean)var0).getServers();

            for(var4 = 0; var4 < var3.length; ++var4) {
               var2.add(new ComponentTarget(var1.getName(), var0.getName(), var3[var4].getName(), 3));
            }
         }
      } else if (var0 instanceof ServerMBean) {
         var2.add(new ComponentTarget(var0.getName(), var0.getName(), 1));
      } else if (var0 instanceof ClusterMBean) {
         var3 = ((ClusterMBean)var0).getServers();

         for(var4 = 0; var4 < var3.length; ++var4) {
            var2.add(new ComponentTarget(var0.getName(), var3[var4].getName(), 2));
         }
      }

      return var2;
   }

   private void printStatus(TargetAvailabilityStatus var1) {
      Set var2;
      Iterator var3;
      switch (var1.getTargetType()) {
         case 1:
            Debug.deploymentDebug("Target Name:" + var1.getTargetName() + " Target Type: Server");
            Debug.deploymentDebug("Deployment Status:" + var1.getDeploymentStatus());
            Debug.deploymentDebug("Availability Status: " + var1.getAvailabilityStatus());
            break;
         case 2:
            Debug.deploymentDebug("Target Name:" + var1.getTargetName() + " Target Type: Cluster");
            Debug.deploymentDebug("Deployment Status:" + var1.getDeploymentStatus());
            var2 = var1.getServersAvailabilityStatus();
            var3 = var2.iterator();

            while(var3.hasNext()) {
               this.printStatus((TargetAvailabilityStatus)var3.next());
            }

            return;
         case 3:
            Debug.deploymentDebug("Target Name:" + var1.getTargetName() + " Target Type: VirtualHost");
            Debug.deploymentDebug("Deployment Status:" + var1.getDeploymentStatus());
            var2 = var1.getClustersAvailabilityStatus();
            var3 = var2.iterator();

            while(var3.hasNext()) {
               this.printStatus((TargetAvailabilityStatus)var3.next());
            }

            Set var4 = var1.getServersAvailabilityStatus();
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               this.printStatus((TargetAvailabilityStatus)var5.next());
            }
      }

   }

   private final class TasksRetirePoller implements TimerListener {
      private TasksRetirePoller() {
      }

      public final void timerExpired(Timer var1) {
         try {
            DeployerRuntimeImpl.this.checkAndMarkRetiredTasks();
         } catch (Throwable var3) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("TaskRetirePoller failed to mark tasks as retired due to an exception : ", var3);
            }
         }

      }

      // $FF: synthetic method
      TasksRetirePoller(Object var2) {
         this();
      }
   }
}
