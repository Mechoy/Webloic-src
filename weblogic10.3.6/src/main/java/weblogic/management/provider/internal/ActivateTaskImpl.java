package weblogic.management.provider.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.deploy.service.FailureDescription;
import weblogic.deploy.service.internal.DeploymentService;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.provider.ActivateTask;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;

public final class ActivateTaskImpl implements ActivateTask {
   private Map serversStateMap;
   private ArrayList changes;
   private AuthenticatedSubject user;
   private Map failedServersMap;
   private Set activatingServers = Collections.synchronizedSet(new HashSet(20));
   private DeploymentRequestTaskRuntimeMBean deploymentReqTask;
   private final String description;
   private int state;
   private long taskId;
   private long activateTimeoutTime;
   private final long beginTime = System.currentTimeMillis();
   private long endTime;
   private Exception error;
   private boolean subTaskErrorsAdded;
   private boolean waitingForEndFailureCallback;
   private boolean deploySucceededCalled;
   private boolean commitSucceededCalled;
   private boolean commitFailureOccurred;
   private boolean haveConfigDeployments;
   private EditLockManager lockMgr;
   private boolean editLockReleased;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");

   public ActivateTaskImpl(String var1, EditLockManager var2, boolean var3, ArrayList var4, AuthenticatedSubject var5, long var6, long var8, String[] var10) throws ManagementException {
      this.description = var1;
      this.taskId = var6;
      this.haveConfigDeployments = var3;
      if (var8 == Long.MAX_VALUE) {
         this.activateTimeoutTime = Long.MAX_VALUE;
      } else {
         this.activateTimeoutTime = System.currentTimeMillis() + var8;
      }

      if (!var3) {
         this.endTime = System.currentTimeMillis();
         this.setState(4);
         this.editLockReleased = true;
      } else {
         this.state = 1;

         for(int var11 = 0; var10 != null && var11 < var10.length; ++var11) {
            this.activatingServers.add(var10[var11]);
         }
      }

      this.changes = var4;
      this.user = var5;
      this.lockMgr = var2;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created activate task runtime with task " + var6 + " timeout " + new Date(this.activateTimeoutTime) + " state " + this.getStatus() + " user " + this.getUser());
      }

   }

   public final int getState() {
      return this.state == 4 && !this.commitSucceededCalled && this.deploymentReqTask != null && !this.deploymentReqTask.isComplete() ? 2 : this.state;
   }

   public final void setState(int var1) {
      synchronized(this) {
         this.state = var1;
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Setting state for activate task " + this.taskId + " to " + this.getStatus());
         }

         this.notifyAll();
      }
   }

   public Map getStateOnServers() {
      if (this.serversStateMap == null) {
         this.serversStateMap = new HashMap();
      }

      this.checkDeploymentSubTasksStatus();
      return this.serversStateMap;
   }

   public long getTaskId() {
      return this.taskId;
   }

   public final boolean updateServerState(String var1, int var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Updating state for activate task " + this.taskId + " server " + var1 + " to " + this.getStatusForState(var2));
      }

      Integer var3 = (Integer)this.getStateOnServers().get(var1);
      if (var2 == 2) {
         if (var3 == null) {
            this.getStateOnServers().put(var1, new Integer(var2));
         }
      } else if (var2 == 5) {
         this.getStateOnServers().put(var1, new Integer(var2));
      } else if (var2 == 4) {
         this.getStateOnServers().put(var1, new Integer(var2));
         this.activatingServers.remove(var1);
      } else if (var2 == 3) {
         this.getStateOnServers().put(var1, new Integer(var2));
         this.activatingServers.remove(var1);
      } else if (var2 == 6) {
         this.activatingServers.remove(var1);
         this.getStateOnServers().put(var1, new Integer(5));
      } else if (var2 == 7) {
         this.commitFailureOccurred = true;
         this.activatingServers.remove(var1);
         if (var3 == null || var3 != 5) {
            this.getStateOnServers().put(var1, new Integer(var2));
         }
      }

      if (var2 != 4 && var2 != 3 && var2 != 7) {
         if (var2 == 6) {
            try {
               String[] var4 = (String[])((String[])this.activatingServers.toArray(new String[0]));

               for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
                  String var6 = var4[var5];
                  if (this.getStateOnServers().get(var6) == null || this.getFailedServers().get(var6) != null) {
                     this.activatingServers.remove(var6);
                  }
               }
            } catch (Exception var7) {
            }

            if (this.activatingServers.size() == 0) {
               if (!this.isWaitingForEndFailureCallback()) {
                  this.setState(6);
               } else {
                  this.endTime = System.currentTimeMillis();
                  this.setState(5);
               }
            }
         } else if (var2 == 2 && this.activatingServers.size() == 0 && var3 != null && var3 == 3) {
            return true;
         }
      } else if (this.activatingServers.size() == 0) {
         if (this.commitFailureOccurred) {
            if (!this.isWaitingForEndFailureCallback()) {
               this.setState(7);
            } else {
               this.endTime = System.currentTimeMillis();
               this.setState(5);
            }
         } else {
            if (!this.deploySucceededCalled) {
               if (var2 == 3) {
                  return false;
               }

               return true;
            }

            this.endTime = System.currentTimeMillis();
            this.setState(4);
         }
      }

      return false;
   }

   public Iterator getChanges() {
      return this.changes != null ? this.changes.iterator() : null;
   }

   public String getUser() {
      return SubjectUtils.getUsername(this.user);
   }

   public final Map getFailedServers() {
      if (this.failedServersMap == null) {
         this.failedServersMap = new HashMap();
      }

      this.checkDeploymentSubTasksStatus();
      return this.failedServersMap;
   }

   public final void addFailedServer(String var1, Exception var2) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Adding failed server for activate task " + this.taskId + " server " + var1 + " reason " + var2);
      }

      this.getFailedServers().put(var1, var2);
      this.getStateOnServers().put(var1, new Integer(5));
      this.setError(var2);
   }

   public void deploySucceeded(FailureDescription[] var1) {
      this.deploySucceededCalled = true;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Deploy succeeded for activate task " + this.taskId + " releasing lock");
      }

      if (this.getState() != 4) {
         this.releaseEditLock();

         for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
            this.activatingServers.remove(var1[var2].getServer());
         }

         if (this.activatingServers.size() == 0 && !this.commitFailureOccurred) {
            this.endTime = System.currentTimeMillis();
            this.setState(4);
         } else if (this.getState() == 1) {
            this.setState(2);
         }
      }

   }

   public void commitSucceeded() {
      this.commitSucceededCalled = true;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Commit succeeded for activate task " + this.taskId);
      }

      this.releaseAndSetCommitted();
   }

   public void releaseAndSetCommitted() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Set state to committed " + this.taskId + " releasing lock");
      }

      if (this.getState() != 4) {
         this.releaseEditLock();
         this.endTime = System.currentTimeMillis();
         this.setState(4);
      }

   }

   public void waitForTaskCompletion() {
      this.waitForCompletion(this.activateTimeoutTime);
   }

   public void waitForTaskCompletion(long var1) {
      this.waitForCompletion(System.currentTimeMillis() + var1);
   }

   private void waitForCompletion(long var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Waiting for completion for activate task " + this.taskId + " timeoutTime is " + new Date(var1));
      }

      synchronized(this) {
         while(this.isRunning()) {
            try {
               long var4 = var1 - System.currentTimeMillis();
               if (var4 > 0L) {
                  this.wait(var4);
                  continue;
               }

               if (System.currentTimeMillis() >= this.activateTimeoutTime) {
                  this.endTime = System.currentTimeMillis();
                  this.setError(new RuntimeException("Timed out waiting for completion"));
                  this.setState(5);
                  continue;
               }
            } catch (InterruptedException var9) {
               continue;
            }

            return;
         }
      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Waited for completion of config for activate task " + this.taskId);
      }

      if (this.state != 5) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Waiting for completion of subtasks for activate task " + this.taskId + " timeoutTime is " + new Date(var1));
         }

         while(this.deploymentReqTask != null && !this.deploymentReqTask.isComplete() && this.getState() != 5) {
            if (System.currentTimeMillis() >= this.activateTimeoutTime) {
               this.setState(5);
               this.setError(new RuntimeException("Timed out waiting for completion of deployment"));
            } else {
               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var7) {
               }
            }
         }

         this.checkDeploymentSubTasksStatus();
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Waited for completion of subtasks for activate task " + this.taskId);
         }

         if (!this.haveConfigDeployments && this.getState() == 4) {
            try {
               this.releaseEditLock();
            } catch (IllegalStateException var8) {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Edit lock taken away", var8);
               }
            }
         }

      }
   }

   private void releaseEditLock() {
      if (!this.editLockReleased) {
         this.lockMgr.releaseEditLock(this.user);
         this.editLockReleased = true;
      }

   }

   public DeploymentRequestTaskRuntimeMBean getDeploymentRequestTaskRuntimeMBean() {
      return this.deploymentReqTask;
   }

   public final boolean isWaitingForEndFailureCallback() {
      return this.waitingForEndFailureCallback;
   }

   public final void setWaitingForEndFailureCallback(boolean var1) {
      this.waitingForEndFailureCallback = var1;
   }

   void setDeploymentRequestTaskRuntimeMBean(DeploymentRequestTaskRuntimeMBean var1) {
      this.deploymentReqTask = var1;
   }

   private synchronized void checkDeploymentSubTasksStatus() {
      if (this.deploymentReqTask != null && this.deploymentReqTask.isComplete() && !this.subTaskErrorsAdded) {
         this.subTaskErrorsAdded = true;
         Map var1 = this.deploymentReqTask.getFailedTargets();
         Set var2 = var1.entrySet();
         Iterator var3 = var2.iterator();

         while(var3 != null && var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            Exception var5 = (Exception)var4.getValue();
            String var6 = (String)var4.getKey();
            this.addFailedServer(var6, var5);
         }

      }
   }

   public final String getDescription() {
      return this.description;
   }

   public final String getStatus() {
      return this.getStatusForState(this.state);
   }

   private final String getStatusForState(int var1) {
      switch (var1) {
         case 0:
            return "STATE_NEW";
         case 1:
            return "STATE_DISTRIBUTING";
         case 2:
            return "STATE_DISTRIBUTED";
         case 3:
            return "STATE_PENDING";
         case 4:
            return "STATE_COMMITTED";
         case 5:
            return "STATE_FAILED";
         case 6:
            return "STATE_CANCELING";
         case 7:
            return "STATE_COMMIT_FAILING";
         default:
            return "STATE_UNKNOWN";
      }
   }

   public final void cancel() throws Exception {
      if (this.deploymentReqTask != null) {
         DeploymentService.getDeploymentService().cancel(this.deploymentReqTask.getDeploymentRequest());
      }
   }

   public final boolean isRunning() {
      return this.state == 1 || this.state == 2 || this.state == 3 || this.state == 6 || this.state == 7;
   }

   public final long getBeginTime() {
      return this.beginTime;
   }

   public final long getEndTime() {
      return this.endTime;
   }

   public final void setEndTime() {
      this.endTime = System.currentTimeMillis();
   }

   public final Exception getError() {
      if (this.error == null) {
         this.checkDeploymentSubTasksStatus();
      }

      return this.error;
   }

   public final void setError(Exception var1) {
      this.error = var1;
   }

   public final boolean isSystemTask() {
      return false;
   }

   public final void setSystemTask(boolean var1) {
   }
}
