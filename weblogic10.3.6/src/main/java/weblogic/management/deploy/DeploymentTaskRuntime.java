package weblogic.management.deploy;

import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.ReflectionException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.deploy.beans.factory.InvalidTargetException;
import weblogic.deploy.common.Debug;
import weblogic.deploy.event.BaseDeploymentEvent;
import weblogic.deploy.event.DeploymentEvent;
import weblogic.deploy.event.DeploymentEventManager;
import weblogic.deploy.event.VetoableDeploymentEvent;
import weblogic.deploy.internal.TargetHelper;
import weblogic.deploy.internal.adminserver.DeploymentManager;
import weblogic.deploy.internal.adminserver.EditAccessHelper;
import weblogic.deploy.internal.adminserver.operations.AbstractOperation;
import weblogic.deploy.internal.adminserver.operations.OperationHelper;
import weblogic.deploy.internal.adminserver.operations.RemoveOperation;
import weblogic.deploy.internal.targetserver.DeployHelper;
import weblogic.deploy.service.DeploymentRequestSubTask;
import weblogic.deploy.service.internal.DeploymentRequestTaskRuntimeMBeanImpl;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.logging.Loggable;
import weblogic.management.ApplicationException;
import weblogic.management.DeploymentException;
import weblogic.management.DeploymentNotification;
import weblogic.management.ManagementException;
import weblogic.management.RemoteNotificationListener;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.LibraryMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.configuration.WebAppComponentMBean;
import weblogic.management.configuration.WebDeploymentMBean;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.deploy.internal.AppRuntimeStateManager;
import weblogic.management.deploy.internal.ComponentTargetValidator;
import weblogic.management.deploy.internal.DeployerRuntimeLogger;
import weblogic.management.deploy.internal.RetirementManager;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.management.utils.AppDeploymentHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.FileUtils;
import weblogic.utils.StringUtils;

public final class DeploymentTaskRuntime extends DomainRuntimeMBeanDelegate implements DeploymentTaskRuntimeMBean, DeploymentRequestSubTask, BeanUpdateListener {
   private static final long serialVersionUID = 7987828709785973087L;
   private final int task;
   private final DeploymentAction deploymentAction;
   private final String sourcePath;
   private final DeploymentData requestData;
   private String[] targets;
   private boolean hasTargets;
   private TargetStatus[] targetsStatus;
   private Map targetStatusMap = new HashMap();
   private Map serverToTargetStatusMap = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private HashSet pendingServers = new HashSet();
   private int deploymentTaskStatus = 0;
   private String deploymentTaskStatusMessage;
   private String applicationName;
   private String applicationId;
   private String applicationVersionIdentifier;
   private int failedStatusCount;
   private final String taskId;
   private long startTime;
   private long endTime;
   private Exception lastException;
   private ApplicationMBean applicationMBean;
   private AppDeploymentMBean appDeployMBean;
   private BasicDeploymentMBean basicDeployMBean;
   private String applicationDisplayName;
   private volatile boolean failedTaskAsTargetNotUp;
   private int cancelState = 0;
   private boolean isNewApplication;
   private final Set unreachableTargets;
   private final Map versionTargetStatusMap = new HashMap();
   private static final transient DeployerRuntimeTextTextFormatter fmt = DeployerRuntimeTextTextFormatter.getInstance();
   private static final String TASK_NAME_PREFIX = "ADTR-";
   private final String saveSource;
   private String deploymentTaskDescription;
   private int notifLevel = 1;
   private boolean sysTask;
   private DeploymentTaskListener appListener;
   private final transient ArrayList taskMessages = new ArrayList();
   private static transient DomainMBean domainMBean;
   private static DeploymentManager depMgr;
   private transient DeploymentRequestTaskRuntimeMBean myParent;
   private transient DomainMBean editableDomainMBean;
   private final transient boolean isAControlOperation;
   private static final AppRuntimeStateManager appRTStateMgr = AppRuntimeStateManager.getManager();
   private boolean retired = false;
   private boolean configChange = false;
   private transient AuthenticatedSubject subject = null;
   private boolean pendingActivation = false;
   private HashMap failedTargets = new HashMap();
   private transient AbstractOperation adminOperation = null;
   private transient DeploymentTaskRuntime delegate = null;
   private transient List delegators = Collections.synchronizedList(new ArrayList());
   private Locale clientLocale = null;
   private boolean inUse;
   private final HashSet handlers = new HashSet();

   public DeploymentTaskRuntime(String var1, BasicDeploymentMBean var2, DeploymentData var3, String var4, int var5, DomainMBean var6, boolean var7, boolean var8) throws ManagementException {
      super("ADTR-" + var4, ManagementService.getDomainAccess(kernelId).getDomainRuntime().getDeployerRuntime(), false);
      this.setClientLocale();
      this.initMBeans(var2);
      this.taskId = var4;
      this.task = var5;
      this.isAControlOperation = var7;
      this.configChange = var8;
      this.deploymentAction = DeploymentTaskRuntime.DeploymentAction.getDeploymentAction(var5);
      this.sourcePath = var1;
      if (var1 == null) {
         this.saveSource = this.getSourcePath();
      } else {
         this.saveSource = var1;
      }

      if (var3 == null) {
         this.requestData = new DeploymentData();
      } else {
         this.requestData = var3;
      }

      if (var6 != null) {
         this.editableDomainMBean = var6;
      }

      this.initializeTask();
      this.setDescription();
      if (var3 != null) {
         this.isNewApplication = var3.isNewApplication();
      }

      this.unreachableTargets = new HashSet();
      this.register();
   }

   private AuthenticatedSubject getSubject() {
      return this.subject;
   }

   public void setSubject(AuthenticatedSubject var1) {
      this.debugSay("subject set to " + var1);
      this.subject = var1;
      if (this.callerOwnsEditLock() && this.editableDomainMBean != null) {
         this.editableDomainMBean.addBeanUpdateListener(this);
      }

   }

   public void initMBeans(BasicDeploymentMBean var1) {
      this.basicDeployMBean = var1;
      if (var1 instanceof AppDeploymentMBean) {
         this.appDeployMBean = (AppDeploymentMBean)var1;
         this.applicationMBean = this.appDeployMBean.getAppMBean();
      }

   }

   public boolean isPendingActivation() {
      return this.pendingActivation;
   }

   public void setPendingActivation(boolean var1) {
      this.pendingActivation = var1;
      if (var1) {
         this.addMessage(DeployerRuntimeLogger.pendingActivationLoggable().getMessage(this.getClientLocale()));
      }

   }

   private String getSourcePath() {
      return this.basicDeployMBean != null ? this.basicDeployMBean.getSourcePath() : null;
   }

   private DomainMBean getDomain() {
      if (this.editableDomainMBean != null) {
         return this.editableDomainMBean;
      } else {
         if (domainMBean == null) {
            Class var1 = DeploymentTaskRuntime.class;
            synchronized(DeploymentTaskRuntime.class) {
               if (domainMBean == null) {
                  domainMBean = ManagementService.getRuntimeAccess(kernelId).getDomain();
               }
            }
         }

         return domainMBean;
      }
   }

   private static DeploymentManager getDepMgr() {
      if (depMgr == null) {
         depMgr = DeploymentManager.getInstance(kernelId);
      }

      return depMgr;
   }

   public AppDeploymentMBean getAppDeploymentMBean() {
      return this.endTime > 0L ? this.getCurrentApp() : this.appDeployMBean;
   }

   public synchronized void printLog(PrintWriter var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var3 = this.getDelegate();
         this.debugSay("Delegating printLog() to delegate : " + var3);
         var3.printLog(var1);
      } else {
         Iterator var2 = this.taskMessages.iterator();

         while(var2.hasNext()) {
            var1.println((String)var2.next());
         }

      }
   }

   public synchronized List getTaskMessages() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getTaskMessages() to delegate : " + var1);
         return var1.getTaskMessages();
      } else {
         return (ArrayList)this.taskMessages.clone();
      }
   }

   public final boolean isAControlOperation() {
      return this.isAControlOperation;
   }

   public TargetStatus findTarget(String var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var3 = this.getDelegate();
         this.debugSay("Delegating findTarget() to delegate : " + var3);
         return var3.getTargetStatus(var1);
      } else {
         TargetStatus var2 = this.getTargetStatus(var1);
         return var2 != null ? var2.copy() : var2;
      }
   }

   private TargetStatus getTargetStatus(String var1) {
      return (TargetStatus)this.targetStatusMap.get(var1);
   }

   public synchronized void updateTargetStatus(String var1, int var2, Exception var3) {
      if (this.deploymentTaskStatus == 1) {
         List var4 = this.getTargetStatusesForServer(var1);
         this.debugSay(" Pending Servers : " + this.pendingServers);
         if (var4 != null) {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               TargetStatus var6 = (TargetStatus)var5.next();
               this.debugSay("Updating target status to " + var2 + " for target " + var6.getTarget() + " from server " + var1);
               this.doUpdateTargetStatus(var6, var2, var3);
               this.addStatusMessage(var2, this.applicationDisplayName, var1);
            }

            if (var2 == 4 && var1 != null && var1.length() > 0) {
               this.addUnreachableTarget(var1);
            }
         } else {
            this.debugSay("Could not find a target status objects for target " + var1 + " - ignoring the status and will proceed by" + " setting the status on the task : " + var2);
            if (this.targetsStatus != null) {
               for(int var7 = 0; var7 < this.targetsStatus.length; ++var7) {
                  if (this.targetsStatus[var7].isTargetListEmpty()) {
                     this.doUpdateTargetStatus(this.targetsStatus[var7], var2, new Exception(DeployerRuntimeLogger.emptyCluster(this.targetsStatus[var7].getTarget())));
                  }
               }
            }
         }

         if (var3 != null) {
            Loggable var8 = DeployerRuntimeLogger.logExceptionReceivedLoggable(this.getDescription(), var3.getMessage());
            this.addMessage(var8.getMessage(this.getClientLocale()));
            this.addMessagesFromApplicationException(var3);
            this.lastException = var3;
         }

         if (var2 == 2 || var2 == 3 || var2 == 4) {
            this.pendingServers.remove(var1);
            this.debugSay("Removed target: " + var1 + " from pending server " + "list");
            this.debugSay(this.dumpPendingServerList());
            if (var2 == 2) {
               if (var3 != null) {
                  this.debugSay("DeploymentTaskRuntime: Adding target '" + var1 + "' with exception: " + var3 + " to failed targets list");
                  this.failedTargets.put(var1, var3);
               }

               ++this.failedStatusCount;
            }
         }

         if (this.pendingServers.isEmpty()) {
            if (this.failedStatusCount == 0) {
               this.finishUp(true, this.targets);
            } else {
               this.handleFailure();
            }
         }

         this.debugSay("Current status is " + this.deploymentTaskStatusMessage);
         this.debugSay(this.deploymentAction.getDescription() + " for " + this.applicationDisplayName + " took : " + (System.currentTimeMillis() - this.startTime) + " millis");
      }
   }

   private void addStatusMessage(int var1, String var2, String var3) {
      String var4 = DeployHelper.getTaskName(this.task, this.getClientLocale());
      switch (var1) {
         case 0:
            this.addMessage(DeployerRuntimeLogger.logInitStatusLoggable(var4, this.applicationDisplayName, var3).getMessage(this.getClientLocale()));
            break;
         case 1:
            this.addMessage(DeployerRuntimeLogger.logProgressStatusLoggable(var4, this.applicationDisplayName, var3).getMessage(this.getClientLocale()));
            break;
         case 2:
            this.addMessage(DeployerRuntimeLogger.logFailedStatusLoggable(var4, this.applicationDisplayName, var3).getMessage(this.getClientLocale()));
            break;
         case 3:
            this.addMessage(DeployerRuntimeLogger.logSuccessStatusLoggable(var4, this.applicationDisplayName, var3).getMessage(this.getClientLocale()));
            break;
         case 4:
            this.addMessage(DeployerRuntimeLogger.logUnavailableStatusLoggable(var4, this.applicationDisplayName, var3).getMessage(this.getClientLocale()));
      }

   }

   private void doUpdateTargetStatus(TargetStatus var1, int var2, Exception var3) {
      var1.setState(var2);
      if (var3 != null) {
         this.debugSay("adding exception for target (" + var3.getMessage() + ")");
         var1.addMessage(var3);
      }

   }

   public void handleFailure() {
      this.finishUp(false, (String[])null);
   }

   private void finishUp(boolean var1, String[] var2) {
      if (!this.isComplete()) {
         this.debugSay("Completing task...");
         this.endTime = System.currentTimeMillis();
         this.removeAppListener();
         this.retirePreviousActiveVersion();
         if (this.getAdminOperation() instanceof RemoveOperation) {
            boolean var3 = this.getDeploymentData().getDeploymentOptions().isGracefulProductionToAdmin();
            if (!var3) {
               RetirementManager.waitForRetirementCompleteIfNeeded(this.getApplicationName(), this.getApplicationVersionIdentifier());
               if (Debug.isDeploymentDebugEnabled()) {
                  Debug.deploymentDebug("DTR.finishup for, app=" + this.getApplicationName() + ", version=" + this.getApplicationVersionIdentifier());
               }
            }
         }

         if (var1) {
            if (this.task == 4) {
               if (this.getCurrentApp() == null) {
                  removeAppRuntimeState(this.applicationId);
               } else {
                  removeAppRuntimeStateForTargets(this.applicationId, var2);
               }
            }

            if (this.task == 4 && this.getCurrentApp() == null) {
               this.removeUploadedSource();
            }

            if (this.unreachableTargets.isEmpty()) {
               this.setState(2);
            } else {
               this.setState(4);
            }
         } else {
            this.setState(3);
         }

         this.logCompletion();
      }
   }

   private static void removeAppRuntimeState(String var0) {
      try {
         AppRuntimeStateManager.getManager().remove(var0);
      } catch (ManagementException var2) {
      }

   }

   private static void removeAppRuntimeStateForTargets(String var0, String[] var1) {
      try {
         AppRuntimeStateManager.getManager().removeTargets(var0, var1);
      } catch (ManagementException var3) {
      }

   }

   private void removeUploadedSource() {
      String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getUploadDirectoryName();
      if (this.appDeployMBean != null) {
         if (var1.startsWith("." + File.separator)) {
            var1 = var1.substring(2);
         }

         String var2 = this.appDeployMBean.getSourcePath();
         this.removeIfUploaded(var2, var1);
         var2 = this.appDeployMBean.getPlanDir();
         this.removeIfUploaded(var2, var1);
      }

   }

   private void removeIfUploaded(String var1, String var2) {
      if (var1 != null && var1.indexOf(var2) != -1) {
         File var3 = new File(var2);
         File var4 = new File(var1);
         File var5 = null;

         do {
            var5 = var4.getParentFile();
            FileUtils.remove(var4);
            var4 = var5;
         } while(!var3.equals(var5) && var5.listFiles().length == 0);
      }

   }

   private void addMessagesFromApplicationException(Exception var1) {
      if (var1 instanceof ApplicationException) {
         ApplicationException var2 = (ApplicationException)var1;
         Hashtable var3 = var2.getModuleErrors();
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            String var6 = (String)var3.get(var5);
            Loggable var7 = DeployerRuntimeLogger.logModuleMessageLoggable(var5, var6);
            this.addMessage(var7.getMessage(this.getClientLocale()));
         }
      }

   }

   public void start() throws ManagementException {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating start() to delegate : " + var1);
         var1.start();
      } else {
         try {
            getDepMgr().startDeploymentTask(this, this.applicationDisplayName);
         } catch (Throwable var3) {
            Loggable var2 = DeployerRuntimeLogger.logExceptionOccurredLoggable(this.deploymentAction.getDescription(), this.applicationDisplayName, new Exception(var3));
            var2.log();
            if (var3 instanceof ManagementException) {
               throw (ManagementException)var3;
            } else {
               throw new ManagementException(var3);
            }
         }
      }
   }

   public void prepareToStart() throws ManagementException {
      if (this.hasDelegate()) {
         this.debugSay("Doing nothing in prepareToStart() since this task has been delegated to : " + this.getDelegate());
      } else {
         this.updateIntendedState();
         this.setUpStartedStateVariables();

         try {
            if (this.hasTargets && this.applicationMBean != null) {
               switch (this.task) {
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 8:
                  case 12:
                  case 13:
                  default:
                     break;
                  case 6:
                     this.destage();
                     break;
                  case 9:
                  case 10:
                     if (this.isNewApplication) {
                        Loggable var4 = DeployerRuntimeLogger.logNullAppLoggable(this.applicationDisplayName, this.deploymentAction.getDescription());
                        var4.log();
                        throw new ManagementException(var4.getMessage());
                     }
                  case 1:
                  case 7:
                  case 11:
                     if (this.sourcePath != null) {
                        this.destage();
                     }

                     if (this.task == 9 && (this.applicationMBean.getDeploymentType().equals(ApplicationMBean.TYPE_EAR) || this.applicationMBean.getDeploymentType().equals(ApplicationMBean.TYPE_COMPONENT))) {
                        this.destage();
                     }

                     if (this.task == 9 && (this.applicationMBean.getDeploymentType().equals(ApplicationMBean.TYPE_EXPLODED_COMPONENT) || this.applicationMBean.getDeploymentType().equals(ApplicationMBean.TYPE_EXPLODED_EAR)) && this.requestData.getFiles() == null) {
                        this.destage();
                     }

                     this.addTargets(this.task != 7);
               }
            }

         } catch (ManagementException var3) {
            ManagementException var1 = var3;

            for(int var2 = 0; var2 < this.targetsStatus.length; ++var2) {
               this.updateTargetStatus(this.targetsStatus[var2].getTarget(), 2, var1);
            }

            if (!this.isComplete()) {
               this.setLastException(var1);
               this.handleFailure();
            }

            throw var1;
         }
      }
   }

   public void cancel() throws Exception {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating cancel() to delegate : " + var1);
         var1.cancel();
      } else {
         if (this.myParent == null) {
            this.prepareToCancel();
         } else {
            this.myParent.cancel();
         }

      }
   }

   public void prepareToCancel() throws Exception {
      if (this.hasDelegate()) {
         this.debugSay("Doing nothing in prepareToCancel() since this task has been delegated to : " + this.getDelegate());
      } else {
         synchronized(this) {
            this.setCancelState(2);
            if (this.deploymentTaskStatus == 2 || this.deploymentTaskStatus == 3) {
               Loggable var1 = DeployerRuntimeLogger.logErrorCannotCancelCompletedTaskLoggable(this.taskId);
               var1.log();
               this.lastException = new UnsupportedOperationException(var1.getMessage());
               throw this.lastException;
            }
         }
      }
   }

   private void setUpStartedStateVariables() throws ManagementException {
      this.assertNotAlreadyStarted();
      DeployerRuntimeLogger.logStartedDeployment(this.getDescription(), this.applicationDisplayName);
      this.setPendingActivation(false);
      this.setState(1);
      this.updateAllTargetStatus(1);
      this.startTime = System.currentTimeMillis();
      this.validateAppDeploy();
      if (isDebugEnabled()) {
         StringBuffer var1 = new StringBuffer();
         var1.append("Starting deployment of " + this.applicationDisplayName + " at " + new Date(this.startTime));
         if (this.targetStatusMap != null) {
            var1.append(" to: '");
            Iterator var2 = this.targetStatusMap.keySet().iterator();

            while(var2.hasNext()) {
               var1.append((String)var2.next());
               var1.append(" ");
            }

            var1.append("'");
         }

         this.debugSay(var1.toString());
      }

   }

   private void assertNotAlreadyStarted() throws ManagementException {
      if (this.getState() != 0) {
         Loggable var1 = DeployerRuntimeLogger.logAlreadyStartedLoggable();
         var1.log();
         throw new ManagementException(var1.getMessage());
      }
   }

   private void replaceTargets(TargetMBean[] var1, ComponentMBean var2) {
      TargetMBean var3 = null;
      if (var1 != null) {
         try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               var3 = var1[var4];
               if (var3 != null) {
                  var2.addTarget(var3);
               }
            }
         } catch (InvalidAttributeValueException var7) {
            Loggable var10 = DeployerRuntimeLogger.logNoSuchTargetLoggable(var3.getName());
            var10.log();
            this.setLastException(var3.getName(), new ManagementException(var10.getMessage(), var7));
         } catch (ManagementException var8) {
            this.debugSay("Rcvd mgmt exception: " + var8.toString());
            this.setLastException(var3.getName(), var8);
         } catch (UndeclaredThrowableException var9) {
            this.debugSay("Rcvd unknown exception: " + var9.toString());
            Object var5 = var9.getUndeclaredThrowable();
            if (var5 instanceof ReflectionException) {
               var5 = ((ReflectionException)var5).getTargetException();
            }

            Loggable var6 = DeployerRuntimeLogger.logAddTargetLoggable(var3.getName(), var2.getName());
            var6.log();
            this.setLastException(var3.getName(), new ManagementException(var6.getMessage(), (Throwable)var5));
         }
      }

   }

   private static void initializeDeploymentDataTypes(DeploymentData var0) {
      String[] var1 = var0.getTargets();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            var0.setTargetType(var1[var2], TargetHelper.getTypeForTarget(var1[var2]));
         }

      }
   }

   private void initializeTask() throws ManagementException {
      if (this.appDeployMBean != null) {
         this.applicationName = this.appDeployMBean.getApplicationName();
         this.applicationId = this.appDeployMBean.getApplicationIdentifier();
         this.applicationVersionIdentifier = this.appDeployMBean.getVersionIdentifier();
      } else {
         this.applicationId = this.applicationName = this.basicDeployMBean.getName();
      }

      this.applicationDisplayName = ApplicationVersionUtils.getDisplayName(this.basicDeployMBean);
      this.addListenerToAppMBean();
      this.debugSay("Initializing deployment task for: " + this.basicDeployMBean);
      if (!this.requestData.hasTargets()) {
         this.requestData.addTargetsFromConfig(this.basicDeployMBean);
      }

      this.initTargetDataInTask(this.requestData);
      this.setState(0);
      this.debugSay("Normalized task info: " + this.requestData.toString());
   }

   private void setTargetTypes(DeploymentData var1) throws ManagementException {
      try {
         this.debugSay("Validating target list: " + StringUtils.join(var1.getTargets(), ","));
         initializeDeploymentDataTypes(var1);
      } catch (IllegalArgumentException var4) {
         Loggable var3 = DeployerRuntimeLogger.logUnconfigTargetsLoggable(new ArrayList(Arrays.asList((Object[])var1.getTargets())));
         var3.log();
         throw this.setLastException(new ManagementException(var3.getMessage(), var4));
      }
   }

   private synchronized String dumpPendingServerList() {
      StringBuffer var1 = new StringBuffer();
      var1.append("PendingServers set : ");
      if (this.pendingServers == null) {
         var1.append("null");
      } else {
         Iterator var2 = this.pendingServers.iterator();

         while(var2.hasNext()) {
            var1.append(" ");
            var1.append((String)var2.next());
         }
      }

      return var1.toString();
   }

   private void initTargetDataInTask(DeploymentData var1) throws ManagementException {
      if (var1.hasTargets()) {
         this.hasTargets = true;
         this.setTargetTypes(this.requestData);
         Set var2 = var1.getAllLogicalTargets();

         try {
            this.pendingServers = (HashSet)var1.getAllTargetedServers(var2, this.editableDomainMBean);
            this.debugSay(this.dumpPendingServerList());
         } catch (InvalidTargetException var4) {
            this.debugSay("initTargetDataInTask: InvalidTargetException for target: " + var4.toString());
            throw new ManagementException(var4);
         }

         String[] var3 = (String[])((String[])var2.toArray(new String[0]));
         this.targets = var3;
         this.initializeTargetStatuses(var2);
      }

   }

   private void initializeTargetStatuses(Set var1) {
      int var2 = var1.size();
      Iterator var3 = var1.iterator();

      int var4;
      String var5;
      for(var4 = 0; var3.hasNext(); ++var4) {
         var5 = (String)var3.next();
         if (var5.trim().equals("")) {
            --var2;
         }
      }

      this.targetsStatus = new TargetStatus[var2];
      var3 = var1.iterator();
      var4 = 0;

      while(var3.hasNext()) {
         var5 = (String)var3.next();
         if (!var5.trim().equals("")) {
            this.targetsStatus[var4] = new TargetStatus(var5);
            this.addToServerToTargetStatusMap(var5, this.targetsStatus[var4]);
            ++var4;
         }
      }

      for(int var6 = 0; var6 < this.targetsStatus.length; ++var6) {
         this.debugSay("Adding: " + this.targetsStatus[var6].getTarget() + " to targetStatusMap");
         this.targetStatusMap.put(this.targetsStatus[var6].getTarget(), this.targetsStatus[var6]);
      }

   }

   private AppDeploymentMBean lookupAppDeployment(String var1) {
      return this.editableDomainMBean != null ? this.editableDomainMBean.lookupAppDeployment(var1) : null;
   }

   private static ServerMBean lookupServer(String var0, DomainMBean var1) {
      return var1.lookupServer(var0);
   }

   private ServerMBean lookupServer(String var1) {
      ServerMBean var2 = null;
      if (this.editableDomainMBean != null) {
         var2 = lookupServer(var1, this.editableDomainMBean);
      }

      if (var2 == null) {
         var2 = lookupServer(var1, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var2;
   }

   private static ClusterMBean lookupCluster(String var0, DomainMBean var1) {
      return var1.lookupCluster(var0);
   }

   private ClusterMBean lookupCluster(String var1) {
      ClusterMBean var2 = null;
      if (this.editableDomainMBean != null) {
         var2 = lookupCluster(var1, this.editableDomainMBean);
      }

      if (var2 == null) {
         var2 = lookupCluster(var1, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var2;
   }

   private static JMSServerMBean lookupJMSServer(String var0, DomainMBean var1) {
      return var1.lookupJMSServer(var0);
   }

   private static SAFAgentMBean lookupSAFAgent(String var0, DomainMBean var1) {
      return var1.lookupSAFAgent(var0);
   }

   private static VirtualHostMBean lookupVirtualHost(String var0, DomainMBean var1) {
      return var1.lookupVirtualHost(var0);
   }

   private JMSServerMBean lookupJMSServer(String var1) {
      JMSServerMBean var2 = null;
      if (this.editableDomainMBean != null) {
         var2 = lookupJMSServer(var1, this.editableDomainMBean);
      }

      if (var2 == null) {
         var2 = lookupJMSServer(var1, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var2;
   }

   private SAFAgentMBean lookupSAFAgent(String var1) {
      SAFAgentMBean var2 = null;
      if (this.editableDomainMBean != null) {
         var2 = lookupSAFAgent(var1, this.editableDomainMBean);
      }

      if (var2 == null) {
         var2 = lookupSAFAgent(var1, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var2;
   }

   private VirtualHostMBean lookupVirtualHost(String var1) {
      VirtualHostMBean var2 = null;
      if (this.editableDomainMBean != null) {
         var2 = lookupVirtualHost(var1, this.editableDomainMBean);
      }

      if (var2 == null) {
         var2 = lookupVirtualHost(var1, ManagementService.getRuntimeAccess(kernelId).getDomain());
      }

      return var2;
   }

   private void addToServerToTargetStatusMap(String var1, TargetStatus var2) {
      ServerMBean var3 = this.lookupServer(var1);
      if (var3 != null) {
         this.debugSay("Adding " + var3 + " to server target status");
         var2.setTargetListEmpty(false);
         this.checkAndAddToServerToTargetStatusMap(var1, var2);
      } else {
         ClusterMBean var4 = this.lookupCluster(var1);
         if (var4 == null) {
            JMSServerMBean var9 = this.lookupJMSServer(var1);
            if (var9 != null) {
               TargetMBean[] var10 = var9.getTargets();
               this.addLogicTargetToMap(var10, var9.getName(), var2);
            } else {
               SAFAgentMBean var11 = this.lookupSAFAgent(var1);
               if (var11 != null) {
                  TargetMBean[] var7 = var11.getTargets();
                  this.addLogicTargetToMap(var7, var11.getName(), var2);
               } else {
                  VirtualHostMBean var12 = this.lookupVirtualHost(var1);
                  if (var12 != null) {
                     TargetMBean[] var8 = var12.getTargets();
                     this.addLogicTargetToMap(var8, var12.getName(), var2);
                  }
               }
            }
         } else {
            ServerMBean[] var5 = var4.getServers();
            var2.setTargetListEmpty(var5 == null || var5.length == 0);

            for(int var6 = 0; var6 < var5.length; ++var6) {
               this.debugSay("Adding " + var5[var6].getName() + " to target status for cluster: " + var4.getName());
               this.checkAndAddToServerToTargetStatusMap(var5[var6].getName(), var2);
            }
         }

         this.dumpServerToTargetStatusMap();
      }

   }

   private void checkAndAddToServerToTargetStatusMap(String var1, TargetStatus var2) {
      Object var3 = (List)this.serverToTargetStatusMap.get(var1);
      if (var3 == null) {
         var3 = new ArrayList();
         ((List)var3).add(var2);
      } else if (!((List)var3).contains(var2)) {
         ((List)var3).add(var2);
      }

      this.serverToTargetStatusMap.put(var1, var3);
   }

   private void addLogicTargetToMap(TargetMBean[] var1, String var2, TargetStatus var3) {
      if (var1 != null && var1.length != 0) {
         HashSet var4 = new HashSet();

         for(int var5 = 0; var5 < var1.length; ++var5) {
            Set var6 = var1[var5].getServerNames();
            if (var6 != null) {
               if (!var4.isEmpty() && !Collections.disjoint(var4, var6)) {
                  if (!var4.containsAll(var6)) {
                     Iterator var7 = var6.iterator();

                     while(var7.hasNext()) {
                        String var8 = (String)var7.next();
                        if (!var4.contains(var8)) {
                           var4.add(var8);
                        }
                     }
                  }
               } else {
                  var4.addAll(var6);
               }
            }
         }

         this.debugSay("Collected distinct server list : " + var4);
         var3.setTargetListEmpty(var4.isEmpty());
         Iterator var9 = var4.iterator();

         while(var9.hasNext()) {
            String var10 = (String)var9.next();
            this.debugSay("Adding " + var10 + " to target status for target: " + var2);
            this.checkAndAddToServerToTargetStatusMap(var10, var3);
         }

      } else {
         var3.setTargetListEmpty(true);
      }
   }

   private void dumpServerToTargetStatusMap() {
      if (isDebugEnabled() && this.serverToTargetStatusMap != null) {
         StringBuffer var1 = new StringBuffer();
         var1.append("serverToTargetStatusMap for task: ");
         var1.append(this.taskId);
         Iterator var2 = this.serverToTargetStatusMap.keySet().iterator();

         while(true) {
            List var4;
            do {
               if (!var2.hasNext()) {
                  Debug.deploymentDebug(var1.toString());
                  return;
               }

               String var3 = (String)var2.next();
               var1.append("\ntarget: ");
               var1.append(var3);
               var1.append(" statuses: ");
               var4 = (List)this.serverToTargetStatusMap.get(var3);
            } while(var4 == null);

            Iterator var5 = var4.iterator();

            for(byte var6 = 0; var5.hasNext(); var1.append(var5.next())) {
               if (var6 > 0) {
                  var1.append(", ");
               }
            }

            var1.append("\n");
         }
      }
   }

   private void logCompletion() {
      int var1 = this.getState();
      switch (var1) {
         case 2:
            DeployerRuntimeLogger.logTaskSuccess(this.getDescription());
            break;
         case 3:
            Exception var2 = this.getError();
            String var3;
            if (this.applicationDisplayName != null) {
               var3 = DeployerRuntimeLogger.logTaskFailed(this.applicationDisplayName, DeployHelper.getTaskName(this.task));
            } else {
               var3 = DeployerRuntimeLogger.logTaskFailedNoApp(DeployHelper.getTaskName(this.task));
            }

            if (var2 != null) {
               DeployerRuntimeLogger.logTrace(var3, var2);
            }
            break;
         case 4:
            DeployerRuntimeLogger.logTaskDeferred(this.getDescription());
      }

   }

   private void destage() {
      this.applicationMBean.unstageTargets(this.targets);
   }

   public synchronized void updatePendingServersWithSuccess() {
      if (this.deploymentTaskStatus == 1) {
         if (this.pendingServers == null || this.pendingServers.isEmpty()) {
            this.updateTargetStatus((String)null, 3, (Exception)null);
            return;
         }

         HashSet var1 = (HashSet)this.pendingServers.clone();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            this.updateTargetStatus(var3, 3, (Exception)null);
         }
      }

   }

   public void updateAllTargetsWithSuccessForMerging() {
      this.setState(1);
      this.updateAllTargetStatus(1);
      this.updatePendingServersWithSuccess();
   }

   private void updateAllTargetStatus(int var1) {
      TargetStatus[] var2 = this.targetsStatus;
      if (var2 != null) {
         synchronized(this) {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (this.deploymentTaskStatus == 1) {
                  var2[var4].setState(var1);
               }
            }

         }
      }
   }

   private void addTargets(boolean var1) throws ManagementException {
      Object var2 = null;
      ComponentMBean[] var3 = this.applicationMBean.getComponents();

      int var4;
      for(var4 = 0; var4 < var3.length; ++var4) {
         ComponentTargetValidator var5 = new ComponentTargetValidator((ComponentMBean)var3[var4]);
         Iterator var6 = this.requestData.getTargetsForModule(var3[var4].getName()).iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            this.debugSay("Checking target " + var7);
            var5.addTarget(var7, this.editableDomainMBean, var1);
         }
      }

      for(var4 = 0; var4 < this.targets.length; ++var4) {
         boolean var17 = true;
         String var18 = this.targets[var4];
         String[] var19 = this.requestData.getModulesForTarget(var18);
         int var8 = this.requestData.getTargetType(var18);
         Object var16;
         if (var19 != null && var19.length != 0) {
            this.debugSay("Modules Specified");
            HashSet var9 = new HashSet();

            for(int var10 = 0; var10 < var19.length; ++var10) {
               var9 = this.addComponentToSet(var9, var19[var10]);
            }

            var16 = (DeploymentMBean[])((DeploymentMBean[])var9.toArray(new DeploymentMBean[var9.size()]));
         } else {
            this.debugSay("No Modules Specified");
            var16 = this.applicationMBean.getComponents();
            var17 = false;
         }

         Loggable var21;
         try {
            for(int var20 = 0; var20 < ((Object[])var16).length; ++var20) {
               var2 = ((Object[])var16)[var20];
               if (var8 == 3) {
                  if (var2 instanceof WebDeploymentMBean) {
                     VirtualHostMBean var23 = this.getDomain().lookupVirtualHost(var18);
                     this.debugSay("Adding virtual host " + var18 + " for module, " + ((DeploymentMBean)var2).getName());
                     if (var23 != null) {
                        ((WebAppComponentMBean)var2).addVirtualHost(var23);
                     }
                  } else if (var17) {
                     var21 = DeployerRuntimeLogger.logInvalidTargetForComponentLoggable(this.applicationDisplayName, ((DeploymentMBean)var2).getName(), var18);
                     var21.log();
                     throw new ManagementException(var21.getMessage());
                  }
               } else {
                  TargetMBean[] var24 = new TargetMBean[]{this.getTargetMBean(var18)};
               }
            }
         } catch (InvalidAttributeValueException var12) {
            var21 = DeployerRuntimeLogger.logNoSuchTargetLoggable(var18);
            var21.log();
            this.setLastException(var18, new ManagementException(var21.getMessage(), var12));
         } catch (ManagementException var13) {
            this.debugSay("Rcvd mgmt exception: " + var13.toString());
            this.setLastException(var18, var13);
         } catch (UndeclaredThrowableException var14) {
            this.debugSay("Rcvd unknown exception: " + var14.toString());
            Object var22 = var14.getUndeclaredThrowable();
            if (var22 instanceof ReflectionException) {
               var22 = ((ReflectionException)var22).getTargetException();
            }

            Loggable var11 = DeployerRuntimeLogger.logAddTargetLoggable(var18, ((DeploymentMBean)var2).getName());
            var11.log();
            this.setLastException(var18, new ManagementException(var11.getMessage(), (Throwable)var22));
         } catch (IllegalArgumentException var15) {
            var21 = DeployerRuntimeLogger.logAddTargetLoggable(var18, ((DeploymentMBean)var2).getName());
            var21.log();
            this.setLastException(var18, new ManagementException(var21.getMessage(), var15));
         }
      }

   }

   private HashSet addComponentToSet(HashSet var1, String var2) {
      this.debugSay("looking for component " + var2);
      ComponentMBean[] var3 = this.applicationMBean.getComponents();
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         ComponentMBean var6 = var3[var5];
         this.debugSay("found name = " + var6.getName() + ", with uri " + var6.getURI());
         if (var5 == var3.length - 1) {
            var4.append(var6.getName() + " ");
         } else {
            var4.append(var6.getName() + ", ");
         }

         if (var6.getName().equals(var2)) {
            var1.add(var6);
            this.debugSay("Found the component " + var2);
         }
      }

      return var1;
   }

   private synchronized void addMessage(String var1) {
      this.taskMessages.add(var1);
   }

   private void setClientLocale() {
      JMXContext var1 = JMXContextHelper.getJMXContext(false);
      if (var1 != null) {
         this.clientLocale = var1.getLocale();
      }

   }

   private Locale getClientLocale() {
      return this.clientLocale;
   }

   private void addListenerToAppMBean() {
      if (this.applicationMBean != null) {
         this.getDeploymentObject().addNotificationListener(this.appListener, new DeployFilter(), this.getId());
      }

   }

   private void removeAppListener() {
      if (this.getDeploymentObject() != null) {
         try {
            this.getDeploymentObject().removeNotificationListener(this.appListener);
         } catch (ListenerNotFoundException var2) {
            this.debugSay("remove listener ex: " + var2.toString());
         }
      }

      this.appListener = null;
   }

   public void setNotificationLevel(int var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating setNotificationLevel() to delegate : " + var2);
         var2.setNotificationLevel(var1);
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var1 > 2) {
            var1 = 2;
         }

         this.notifLevel = var1;
      }
   }

   public void setState(int var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var13 = this.getDelegate();
         this.debugSay("Delegating setState() to delegate : " + var13);
         var13.setState(var1);
      } else {
         boolean var2 = false;
         StringBuffer var3 = new StringBuffer();
         var3.append(this.deploymentAction.getDescription()).append(" ");

         try {
            switch (var1) {
               case 0:
                  var3.append(fmt.init());
                  break;
               case 1:
                  var3.append(fmt.running());
                  break;
               case 2:
                  var3.append(fmt.completed());
                  this.notifyAppDeployEnded();
                  var2 = true;
                  break;
               case 3:
                  var3.append(fmt.failed());
                  this.notifyAppDeployEnded();
                  var2 = true;
                  break;
               case 4:
                  var3.append(fmt.deferred());
                  this.notifyAppDeployEnded();
                  var2 = true;
                  break;
               default:
                  return;
            }

            int var4;
            synchronized(this) {
               var4 = this.deploymentTaskStatus;
               this.deploymentTaskStatus = var1;
               this.deploymentTaskStatusMessage = var3.toString();
               this.debugSay("New Status for task " + this.taskId + " is " + this.deploymentTaskStatus + ":" + this.deploymentTaskStatusMessage);
            }

            this._postSet("State", var4, var1);
         } finally {
            if (var2) {
               this.reset();
            }

         }
      }
   }

   public void setSystemTask(boolean var1) {
      this.sysTask = var1;
   }

   private void reset() {
      this.debugSay("Resetting task " + this.getId());
      this.appDeployMBean = null;
      this.applicationMBean = null;
      this.versionTargetStatusMap.clear();
      this.basicDeployMBean = null;
      this.appListener = null;
      this.adminOperation = null;
      if (!this.callerOwnsEditLock()) {
         this.editableDomainMBean = null;
      } else if (this.editableDomainMBean != null) {
         this.editableDomainMBean.removeBeanUpdateListener(this);
         this.editableDomainMBean = null;
      }

      if (this.myParent != null) {
         try {
            ((DeploymentRequestTaskRuntimeMBeanImpl)this.myParent).unregister();
         } catch (Throwable var3) {
            var3.printStackTrace();
         }

         this.myParent = null;
      }

      if (!this.delegators.isEmpty()) {
         Iterator var1 = this.delegators.iterator();

         while(var1.hasNext()) {
            DeploymentTaskRuntime var2 = (DeploymentTaskRuntime)var1.next();
            var2.reset();
         }

         this.delegators.clear();
      }

      if (this.configChange) {
         try {
            this.unregister();
         } catch (ManagementException var4) {
            if (Debug.isDeploymentDebugEnabled()) {
               Debug.deploymentDebug("Remove DeploymentTaskRuntimeMBean failed for id: " + this.taskId + ", ex:" + var4.getMessage());
            }
         }
      }

   }

   private AppDeploymentMBean getCurrentApp() {
      AppDeploymentMBean var1 = ApplicationVersionUtils.getAppDeployment(this.getDomain(), this.applicationId, (String)null);
      return var1;
   }

   private boolean callerOwnsEditLock() {
      if (this.getSubject() == null) {
         return false;
      } else {
         DeploymentManager var1 = DeploymentManager.getInstance(kernelId);
         EditAccessHelper var2 = var1.getEditAccessHelper(kernelId);
         return var2.isCurrentEditor(this.getSubject());
      }
   }

   private void setDescription() {
      String var1 = DeployHelper.getTaskName(this.task);
      Set var2 = this.requestData.getAllLogicalTargets();
      String var3 = StringUtils.join((String[])((String[])var2.toArray(new String[var2.size()])), ",");
      Loggable var4;
      if (this.requestData.isLibrary()) {
         var4 = DeployerRuntimeLogger.logLibraryDescriptionLoggable(this.applicationDisplayName, var3, var1);
      } else {
         var4 = DeployerRuntimeLogger.logDescriptionLoggable(this.applicationDisplayName, var3, var1);
      }

      this.deploymentTaskDescription = var4.getMessage();
      this.debugSay("New task: " + this.deploymentTaskDescription);
   }

   private ManagementException setLastException(String var1, ManagementException var2) {
      this.lastException = var2;
      if (var1 != null) {
         TargetStatus var3 = this.getTargetStatus(var1);
         if (var3 != null) {
            var3.addMessage(var2);
            var3.setState(2);
         }
      }

      return var2;
   }

   private ManagementException setLastException(ManagementException var1) {
      return this.setLastException((String)null, var1);
   }

   public RuntimeMBean getMBean() {
      return this;
   }

   public String getSource() {
      return this.saveSource;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public String getApplicationId() {
      return this.applicationId;
   }

   public String getDescription() {
      return this.deploymentTaskDescription;
   }

   public String getStatus() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getStatus() to delegate : " + var1);
         return var1.getStatus();
      } else {
         return this.deploymentTaskStatusMessage;
      }
   }

   public boolean isRunning() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating isRunning() to delegate : " + var2);
         return var2.isRunning();
      } else {
         int var1 = this.getState();
         return this.cancelState != 8 && var1 != 2 && var1 != 3 && var1 != 4;
      }
   }

   public long getBeginTime() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getBeginTime() to delegate : " + var1);
         return var1.getBeginTime();
      } else {
         return this.startTime;
      }
   }

   public long getEndTime() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getEndTime() to delegate : " + var1);
         return var1.getEndTime();
      } else {
         return this.endTime;
      }
   }

   public Exception getError() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getError() to delegate : " + var1);
         return var1.getError();
      } else {
         return this.lastException;
      }
   }

   public int getTask() {
      return this.task;
   }

   public ApplicationMBean getDeploymentObject() {
      if (this.endTime > 0L) {
         AppDeploymentMBean var1 = this.getCurrentApp();
         return var1 == null ? null : var1.getAppMBean();
      } else {
         return this.applicationMBean;
      }
   }

   public BasicDeploymentMBean getDeploymentMBean() {
      return this.basicDeployMBean;
   }

   public String getApplicationVersionIdentifier() {
      return this.applicationVersionIdentifier;
   }

   public DeploymentData getDeploymentData() {
      return this.requestData;
   }

   public TargetStatus[] getTargets() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var3 = this.getDelegate();
         this.debugSay("Delegating getTargets() to delegate : " + var3);
         return var3.getTargets();
      } else {
         TargetStatus[] var1 = null;
         if (this.targetsStatus != null) {
            var1 = new TargetStatus[this.targetsStatus.length];

            for(int var2 = 0; var2 < this.targetsStatus.length; ++var2) {
               var1[var2] = this.targetsStatus[var2].copy();
            }
         }

         return var1;
      }
   }

   public String getId() {
      return this.taskId;
   }

   public int getNotificationLevel() {
      return this.notifLevel;
   }

   public Map getVersionTargetStatusMap() {
      return this.versionTargetStatusMap;
   }

   public boolean isInUse() {
      return this.inUse;
   }

   public void setInUse(boolean var1) {
      this.inUse = var1;
   }

   public synchronized int getState() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getState() to delegate : " + var1);
         return var1.getState();
      } else {
         return this.deploymentTaskStatus;
      }
   }

   public boolean isSystemTask() {
      return this.sysTask;
   }

   private boolean hasFiles() {
      return this.requestData != null && this.requestData.getFiles() != null && this.requestData.getFiles().length > 0;
   }

   public boolean hasTargets() {
      return this.hasTargets;
   }

   public TaskRuntimeMBean[] getSubTasks() {
      return null;
   }

   public TaskRuntimeMBean getParentTask() {
      return null;
   }

   public TaskRuntimeMBean getMyParent() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getMyParent() to delegate : " + var1);
         return var1.getMyParent();
      } else {
         return this.myParent;
      }
   }

   public void setMyParent(TaskRuntimeMBean var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating setMyParent() to delegate : " + var2);
         var2.setMyParent(var1);
      } else {
         this.myParent = (DeploymentRequestTaskRuntimeMBean)var1;
      }
   }

   public synchronized boolean isComplete() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating isComplete() to delegate : " + var1);
         return var1.isComplete();
      } else if (this.deploymentTaskStatus != 2 && this.deploymentTaskStatus != 3 && this.deploymentTaskStatus != 4) {
         this.debugSay("DeploymentTaskRuntime: isComplete status for task  id: " + this.getId() + " is 'false' " + "deploymentStatus: " + this.deploymentTaskStatus + ", pending servers: " + this.dumpPendingServerList());
         return false;
      } else {
         return true;
      }
   }

   public final synchronized Map getFailedTargets() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getFailedTargets() to delegate : " + var1);
         return var1.getFailedTargets();
      } else {
         return (Map)this.failedTargets.clone();
      }
   }

   public HashSet getAllPhysicalServers() {
      HashSet var1 = new HashSet();
      Set var2 = this.serverToTargetStatusMap.keySet();
      var1.addAll(var2);
      return var1;
   }

   private TargetMBean getTargetMBean(String var1) {
      Object var2 = this.getDomain().lookupServer(var1);
      if (var2 == null) {
         var2 = this.getDomain().lookupCluster(var1);
      }

      return (TargetMBean)var2;
   }

   private List getTargetStatusesForServer(String var1) {
      this.dumpServerToTargetStatusMap();
      return (List)this.serverToTargetStatusMap.get(var1);
   }

   public boolean isTaskFailedAsTargetNotUp() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating isTaskFailedAsTargetNotUp() to delegate : " + var1);
         return var1.isTaskFailedAsTargetNotUp();
      } else {
         return this.failedTaskAsTargetNotUp;
      }
   }

   public void setTaskFailedAsTargetNotUp(boolean var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating setTaskFailedAsTargetNotUp() to delegate : " + var2);
         var2.setTaskFailedAsTargetNotUp(var1);
      } else {
         this.failedTaskAsTargetNotUp = true;
      }
   }

   private void debugSay(String var1) {
      if (isDebugEnabled()) {
         Debug.deploymentDebug("[" + this.taskId + "]:" + var1);
      }

   }

   private static boolean isDebugEnabled() {
      return Debug.isDeploymentDebugEnabled();
   }

   public void setCancelState(int var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating setCancelState() to delegate : " + var2);
         var2.setCancelState(var1);
      } else {
         synchronized(this) {
            this.cancelState = var1;
         }

         this.debugSay("cancel state set to " + this.getCancelStateString());
         if (this.cancelState == 8) {
            this.reset();
         }

      }
   }

   public synchronized int getCancelState() {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var1 = this.getDelegate();
         this.debugSay("Delegating getCancelState() to delegate : " + var1);
         return var1.getCancelState();
      } else {
         return this.cancelState;
      }
   }

   private void validateAppDeploy() throws ManagementException {
      if (this.basicDeployMBean != null) {
         boolean var1 = this.requestData == null ? false : this.requestData.isNewApplication();
         String[] var2 = this.requestData == null ? null : this.requestData.getModules();
         String[] var3 = this.requestData == null ? null : this.requestData.getTargets();
         if (this.task == 1) {
            DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, VetoableDeploymentEvent.APP_ACTIVATE, this.basicDeployMBean, var1, var2, var3));
         } else if (this.task != 11 && this.task != 9 && this.task != 10) {
            if (this.task == 7) {
               DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, VetoableDeploymentEvent.APP_START, this.basicDeployMBean, var1, var2, var3));
            } else if (this.task == 4 || this.task == 12) {
               DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, VetoableDeploymentEvent.APP_UNDEPLOY, this.basicDeployMBean, false, var2, var3));
            }
         } else {
            DeploymentEventManager.sendVetoableDeploymentEvent(VetoableDeploymentEvent.create(this, VetoableDeploymentEvent.APP_DEPLOY, this.basicDeployMBean, var1, var2, var3));
         }

      }
   }

   private void notifyAppDeployEnded() {
      if (this.appDeployMBean != null) {
         String[] var1 = this.requestData == null ? null : this.requestData.getModules();
         String[] var2 = this.requestData == null ? null : this.requestData.getTargets();
         if (this.task == 1) {
            DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, DeploymentEvent.APP_ACTIVATED, this.appDeployMBean, var1, var2));
         } else if (this.task != 11 && this.task != 9 && this.task != 10) {
            if (this.task == 7) {
               DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, DeploymentEvent.APP_STARTED, this.appDeployMBean, var1, var2));
            } else if ((this.task == 4 || this.task == 12) && this.lookupAppDeployment(this.applicationId) == null) {
               DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, DeploymentEvent.APP_DELETED, this.appDeployMBean, false, var1, var2));
            }
         } else {
            BaseDeploymentEvent.EventType var3 = DeploymentEvent.APP_DEPLOYED;
            if (this.task == 9 || this.task == 10) {
               var3 = DeploymentEvent.APP_REDEPLOYED;
            }

            DeploymentEventManager.sendDeploymentEvent(DeploymentEvent.create(this, var3, this.appDeployMBean, var1, var2));
         }

      }
   }

   private void retirePreviousActiveVersion() {
      if (this.appDeployMBean != null && this.appDeployMBean.getVersionIdentifier() != null && (this.task == 1 || this.task == 11 || this.task == 7 || this.task == 9) && !this.hasFiles() && this.failedStatusCount == 0) {
         AppDeploymentMBean var1 = getPrevActiveAppDeployment(this.appDeployMBean);

         try {
            appRTStateMgr.setActiveVersion(this.applicationId, true);
         } catch (ManagementException var4) {
            DeployerRuntimeLogger.logErrorPersistingActiveAppState(this.getDescription(), ManagementException.unWrapExceptions(var4));
         }

         if (!(this.appDeployMBean instanceof LibraryMBean)) {
            if (var1 != null && !this.appDeployMBean.getVersionIdentifier().equals(var1.getVersionIdentifier())) {
               try {
                  RetirementManager.retire(var1, this.requestData);
               } catch (ManagementException var3) {
                  DeployerRuntimeLogger.logRetirementFailed(ApplicationVersionUtils.getDisplayName((BasicDeploymentMBean)var1), var3);
               }
            }

         }
      }
   }

   private static AppDeploymentMBean getPrevActiveAppDeployment(AppDeploymentMBean var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getApplicationName();
         boolean var2 = ApplicationVersionUtils.isAdminMode(var0);
         String var3 = var0.getVersionIdentifier();
         if (var3 == null) {
            return var0;
         } else {
            DomainMBean var4 = ManagementService.getRuntimeAccess(kernelId).getDomain();
            AppDeploymentMBean[] var5 = AppDeploymentHelper.getAppsAndLibs(var4);
            if (var5 == null) {
               return null;
            } else {
               for(int var6 = 0; var6 < var5.length; ++var6) {
                  AppDeploymentMBean var7 = var5[var6];
                  if (var7.getApplicationName().equals(var1) && !var3.equals(var7.getVersionIdentifier()) && ApplicationVersionUtils.isAdminMode(var7) == var2 && (var7.getVersionIdentifier() == null || appRTStateMgr.isActiveVersion(var7))) {
                     return var7;
                  }
               }

               return null;
            }
         }
      }
   }

   public void addUnreachableTarget(String var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var2 = this.getDelegate();
         this.debugSay("Delegating addUnreachableTarget() to delegate : " + var2);
         var2.addUnreachableTarget(var1);
      } else {
         this.debugSay(" Adding unreachable target : " + var1);
         this.unreachableTargets.add(var1);
      }
   }

   public void waitForTaskCompletion(long var1) {
      if (this.hasDelegate()) {
         DeploymentTaskRuntime var3 = this.getDelegate();
         this.debugSay("Delegating waitForTaskCompletion() to delegate : " + var3);
         var3.waitForTaskCompletion(var1);
      } else {
         while(this.isRunning() && (var1 <= 0L || var1 > System.currentTimeMillis())) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var4) {
            }
         }

      }
   }

   public boolean isRetired() {
      return this.retired;
   }

   public void setRetired() {
      this.debugSay("<" + new Date() + "> <" + Thread.currentThread().getName() + "> Setting the task with id '" + this.getId() + "' as retired");
      this.retired = true;
      this.lastException = null;
      this.failedTargets.clear();
      this.targetStatusMap.clear();
      this.serverToTargetStatusMap.clear();
      this.versionTargetStatusMap.clear();
      this.targetsStatus = null;
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      this.debugSay(" +++ undo changes invoked on EditableDomain");
      DescriptorBean var2 = var1.getSourceBean();
      this.debugSay(" +++ SourceBean on the event : " + var2);
      DescriptorBean var3 = var1.getProposedBean();
      this.debugSay(" +++ proposedBean on the event : " + var3);
      this.debugSay(" +++ editableDomainMBean : " + this.editableDomainMBean);
      if (var2 == this.editableDomainMBean || this.editableDomainMBean == null) {
         if (this.adminOperation != null) {
            this.adminOperation.undoChangesTriggeredByUser();
         }

         this.remove();
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   public void addDelegator(DeploymentTaskRuntime var1) throws DeploymentException {
      if (this == var1) {
         throw new DeploymentException("Task '" + this.getId() + "' cannot be " + "set as deletate to itself");
      } else {
         synchronized(this) {
            this.delegators.add(var1);
            var1.setDelegate(this);
         }
      }
   }

   private void setDelegate(DeploymentTaskRuntime var1) throws DeploymentException {
      if (this.hasDelegate()) {
         throw new DeploymentException("task '" + this.getId() + "' has already " + "been delegated to '" + this.getDelegate().getId() + "' and hence " + "cannot be delegated again to '" + this.delegate.getId() + "'");
      } else {
         this.delegate = var1;
      }
   }

   private boolean hasDelegate() {
      return this.delegate != null;
   }

   private DeploymentTaskRuntime getDelegate() {
      return this.delegate;
   }

   public boolean isNewSource() {
      return this.sourcePath != null;
   }

   public void addHandler(DeploymentCompatibilityEventHandler var1) {
      synchronized(this.handlers) {
         this.handlers.add(var1);
      }
   }

   private final String getCancelStateString() {
      switch (this.cancelState) {
         case 0:
            return "CANCEL_STATE_NONE";
         case 1:
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            return "cancel state indeterminate";
         case 2:
            return "CANCEL_STATE_STARTED";
         case 4:
            return "CANCEL_STATE_FAILED";
         case 8:
            return "CANCEL_STATE_COMPLETED";
      }
   }

   public AbstractOperation getAdminOperation() {
      return this.adminOperation;
   }

   public void setAdminOperation(AbstractOperation var1) {
      this.adminOperation = var1;
   }

   private void updateIntendedState() throws ManagementException {
      if (isDebugEnabled()) {
         this.debugSay("Updating intended state from task with task type : " + this.getTask());
      }

      String var1 = null;
      switch (this.getTask()) {
         case 1:
         case 7:
         case 9:
         case 11:
            OperationHelper.setAdminMode(this.getAppDeploymentMBean(), this.getDeploymentData(), "STATE_ACTIVE");
            break;
         case 2:
         case 6:
            var1 = "STATE_PREPARED";
            String var2 = appRTStateMgr.getIntendedState(this.getAppDeploymentMBean().getName());
            if (var2 != null) {
               var1 = var2;
            }

            OperationHelper.setState(this.getAppDeploymentMBean(), this.getDeploymentData(), var1);
         case 3:
         case 4:
         case 5:
         case 10:
         case 12:
         default:
            break;
         case 8:
            var1 = "STATE_PREPARED";
            if (this.requestData.hasModuleTargets() && this.appDeployMBean != null) {
               var1 = appRTStateMgr.getIntendedState(this.appDeployMBean.getName());
            }

            OperationHelper.setAdminMode(this.getAppDeploymentMBean(), this.getDeploymentData(), var1);
            break;
         case 13:
            OperationHelper.setState(this.getAppDeploymentMBean(), this.getDeploymentData(), "STATE_NEW");
      }

   }

   public void remove() {
      DeployerRuntimeMBean var1 = (DeployerRuntimeMBean)this.getParent();
      if (var1 != null) {
         this.debugSay(" +++ removing Task due to undo changes triggered by user : " + this.taskId);
         var1.removeTask(this.taskId);
      }

      this.setCancelState(8);
   }

   public abstract static class DeploymentAction {
      static final DeploymentAction DEPLOY_TASK_ACTIVATE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageActivate();
         }
      };
      static final DeploymentAction DEPLOY_TASK_PREPARE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messagePrepare();
         }
      };
      static final DeploymentAction DEPLOY_TASK_DEACTIVATE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageDeactivate();
         }
      };
      static final DeploymentAction DEPLOY_TASK_REMOVE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageRemove();
         }
      };
      static final DeploymentAction DEPLOY_TASK_UNPREPARE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageUnprepare();
         }
      };
      static final DeploymentAction DEPLOY_TASK_DISTRIBUTE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageDistribute();
         }
      };
      static final DeploymentAction DEPLOY_TASK_START = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageStart();
         }
      };
      static final DeploymentAction DEPLOY_TASK_STOP = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageStop();
         }
      };
      static final DeploymentAction DEPLOY_TASK_REDEPLOY = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageRedeploy();
         }
      };
      static final DeploymentAction DEPLOY_TASK_UPDATE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageUpdate();
         }
      };
      static final DeploymentAction DEPLOY_TASK_DEPLOY = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageDeploy();
         }
      };
      static final DeploymentAction DEPLOY_TASK_RETIRE = new DeploymentAction() {
         String getDescription() {
            return DeploymentTaskRuntime.fmt.messageRetire();
         }
      };

      abstract String getDescription();

      static DeploymentAction getDeploymentAction(int var0) {
         switch (var0) {
            case 1:
               return DEPLOY_TASK_ACTIVATE;
            case 2:
               return DEPLOY_TASK_PREPARE;
            case 3:
               return DEPLOY_TASK_DEACTIVATE;
            case 4:
               return DEPLOY_TASK_REMOVE;
            case 5:
               return DEPLOY_TASK_UNPREPARE;
            case 6:
               return DEPLOY_TASK_DISTRIBUTE;
            case 7:
               return DEPLOY_TASK_START;
            case 8:
               return DEPLOY_TASK_STOP;
            case 9:
               return DEPLOY_TASK_REDEPLOY;
            case 10:
               return DEPLOY_TASK_UPDATE;
            case 11:
               return DEPLOY_TASK_DEPLOY;
            case 12:
            default:
               throw new AssertionError("Unexpected deployment action identifier: " + var0);
            case 13:
               return DEPLOY_TASK_RETIRE;
         }
      }
   }

   static final class DeployFilter implements NotificationFilter, Serializable {
      public boolean isNotificationEnabled(Notification var1) {
         return var1 instanceof DeploymentNotification;
      }
   }

   private final class DeploymentTaskListener implements RemoteNotificationListener {
      public void handleNotification(Notification var1, Object var2) {
         if (var1 instanceof DeploymentNotification) {
            DeploymentNotification var3 = (DeploymentNotification)var1;
            DeploymentTaskRuntime.this.debugSay("DTRM rcvd notification: " + var3.getMessage());
            String var4 = var3.getTask();
            boolean var5 = false;
            if (var4 != null && var2 != null && var2 instanceof String) {
               var5 = var4.equals(var2);
               if (var5) {
                  DeploymentTaskRuntime.this.debugSay("Received matching notification for task: " + var4);
               } else {
                  DeploymentTaskRuntime.this.debugSay("Skipping non-matching notification for task: " + var4);
               }
            } else {
               DeploymentTaskRuntime.this.debugSay("Received non-matching notification for task: " + var4);
               if (var2 instanceof String) {
                  DeploymentTaskRuntime.this.debugSay("Skipping this notification for task: " + var2);
               }
            }

            if (var5) {
               Loggable var6;
               if (var3.isAppNotification()) {
                  var6 = DeployerRuntimeLogger.logAppNotificationLoggable(var3.getAppName(), var3.getServerName(), var3.getPhase());
                  var6.log();
                  DeploymentTaskRuntime.this.addMessage(var6.getMessage(DeploymentTaskRuntime.this.getClientLocale()));
               } else if (var3.isModuleNotification()) {
                  var6 = null;
                  if (var3.isBeginTransition()) {
                     var6 = DeployerRuntimeLogger.logStartTransitionLoggable(var3.getAppName(), var3.getModuleName(), var3.getCurrentState(), var3.getTargetState(), var3.getServerName());
                  } else if (var3.isEndTransition()) {
                     var6 = DeployerRuntimeLogger.logSuccessfulTransitionLoggable(var3.getAppName(), var3.getModuleName(), var3.getCurrentState(), var3.getTargetState(), var3.getServerName());
                  } else if (var3.isFailedTransition()) {
                     var6 = DeployerRuntimeLogger.logFailedTransitionLoggable(var3.getAppName(), var3.getModuleName(), var3.getCurrentState(), var3.getTargetState(), var3.getServerName());
                  }

                  if (var6 != null) {
                     DeploymentTaskRuntime.this.addMessage(var6.getMessage(DeploymentTaskRuntime.this.getClientLocale()));
                  }
               }
            }
         }

      }
   }
}
