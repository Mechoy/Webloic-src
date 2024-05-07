package weblogic.deploy.api.spi.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.OperationUnsupportedException;
import javax.enterprise.deploy.spi.status.ClientConfiguration;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.deploy.TargetImpl;
import weblogic.deploy.api.spi.deploy.TargetModuleIDImpl;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.deploy.utils.TaskCompletionNotificationListener;
import weblogic.management.ManagementException;
import weblogic.management.deploy.TargetStatus;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;

public class ProgressObjectImpl implements ProgressObject, Serializable {
   private static final long serialVersionUID = 1L;
   private static final boolean debug = Debug.isDebug("status");
   private transient StateType state;
   private transient CommandType cmd;
   private transient ActionType action;
   private String msg;
   private transient WebLogicDeploymentManager dm;
   private String task;
   private transient List tmids;
   private transient Set listeners;
   private transient Throwable error;
   private transient List pending;
   private transient DeploymentTaskRuntimeMBean dtrm;
   private transient boolean haveDtrm;
   private transient TaskCompletionNotificationListener taskCompletionListener;

   public ProgressObjectImpl(CommandType var1, String var2, TargetModuleID[] var3, WebLogicDeploymentManager var4) {
      this.state = StateType.RUNNING;
      this.action = ActionType.EXECUTE;
      this.msg = null;
      this.tmids = Collections.synchronizedList(new ArrayList());
      this.listeners = Collections.synchronizedSet(new HashSet());
      this.error = null;
      this.pending = Collections.synchronizedList(new ArrayList());
      this.dtrm = null;
      this.haveDtrm = false;
      this.taskCompletionListener = null;
      this.cmd = var1;
      this.task = var2;
      this.dm = var4;
      this.copyInto(var3, this.pending);
      if (debug) {
         this.dumpPO();
      }

   }

   private void copyInto(TargetModuleID[] var1, List var2) {
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.add(var1[var3]);
         }

      }
   }

   public ProgressObjectImpl(CommandType var1, String var2, WebLogicDeploymentManager var3) {
      this(var1, (String)null, new TargetModuleID[0], var3);
      this.msg = var2;
      this.state = StateType.FAILED;
   }

   public ProgressObjectImpl(CommandType var1, Throwable var2, WebLogicDeploymentManager var3) {
      this(var1, (String)null, new TargetModuleID[0], var3);
      Throwable var4 = ManagementException.unWrapExceptions(var2);
      this.msg = var4.toString();
      this.state = StateType.FAILED;
      this.error = var4;
   }

   public ProgressObjectImpl(CommandType var1, WebLogicDeploymentManager var2) {
      this(var1, (String)null, new TargetModuleID[0], var2);
      this.msg = SPIDeployerLogger.noop(var1.toString());
      this.state = StateType.COMPLETED;
   }

   public DeploymentStatus getDeploymentStatus() {
      this.getResultTargetModuleIDs();
      return new DeploymentStatusImpl(this.state, this.cmd, this.action, this.msg, this.error);
   }

   public TargetModuleID[] getResultTargetModuleIDs() {
      this.updateState();
      if (this.state != StateType.RELEASED && this.pending.size() > 0) {
         this.updateTmids();
      }

      TargetModuleID[] var1 = (TargetModuleID[])((TargetModuleID[])this.tmids.toArray(new TargetModuleID[0]));
      if (this.dm != null && this.dm.getServerConnection() != null) {
         TargetModuleID[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            TargetModuleID var5 = var2[var4];
            this.dm.getServerConnection().populateWarUrlInChildren(var5);
         }
      }

      return var1;
   }

   private void updateTmids() {
      try {
         this.getDtrm();
         if (this.dtrm != null) {
            TargetStatus[] var1 = this.dtrm.getTargets();
            if (var1 == null) {
               return;
            }

            for(int var2 = 0; var2 < var1.length; ++var2) {
               TargetStatus var3 = var1[var2];
               if (var3.getState() != 1 && var3.getState() != 0) {
                  List var4 = this.getPendingTmidsForTarget(var3.getTarget());
                  if (!var4.isEmpty()) {
                     for(int var5 = 0; var5 < var4.size(); ++var5) {
                        TargetModuleID var6 = (TargetModuleID)var4.get(var5);
                        if (var3.getState() == 3 || var3.getState() == 4) {
                           if (debug) {
                              Debug.say("adding successful tmid: " + var6.toString());
                           }

                           TargetModuleID var7 = this.dm.getServerConnection().getModuleCache().getResultTmids(var6.getModuleID(), var6.getTarget());
                           if (var7 == null) {
                              var7 = var6;
                           }

                           this.tmids.add(var7);
                        }

                        this.pending.remove(var6);
                     }
                  }
               }
            }
         } else if (this.state == StateType.RUNNING) {
            this.setState(StateType.RELEASED);
         }
      } catch (Throwable var8) {
         SPIDeployerLogger.logConnectionError(var8.getMessage(), var8);
         if (this.state == StateType.RUNNING) {
            this.setState(StateType.RELEASED);
         }

         this.dtrm = null;
      }

   }

   private List getPendingTmidsForTarget(String var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < this.pending.size(); ++var3) {
         TargetModuleID var4 = (TargetModuleID)this.pending.get(var3);
         if (var4.getTarget().getName().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private void updateState() {
      if (this.state != StateType.RELEASED) {
         if (!this.dm.isConnected() && this.state == StateType.RUNNING) {
            this.setState(StateType.RELEASED);
         }

      }
   }

   public ClientConfiguration getClientConfiguration(TargetModuleID var1) {
      return null;
   }

   public boolean isCancelSupported() {
      return true;
   }

   public void cancel() throws OperationUnsupportedException {
      DeploymentTaskRuntimeMBean var1 = this.getDtrm();
      if (var1 != null) {
         try {
            if (debug) {
               Debug.say("Cancelling task " + this.getTask());
            }

            var1.cancel();
         } catch (Exception var3) {
            if (debug) {
               Debug.say("Cancel of task " + this.getTask() + " failed: " + var3.toString());
            }
         }
      }

   }

   public boolean isStopSupported() {
      return false;
   }

   public void stop() throws OperationUnsupportedException {
      throw new OperationUnsupportedException(SPIDeployerLogger.unsupported("stop"));
   }

   public void addProgressListener(ProgressListener var1) {
      if (!this.listeners.add(var1) && debug) {
         Debug.say("Listener already registered: " + var1.toString());
      }

      if (this.state != StateType.RUNNING) {
         this.reportEvent();
      }

   }

   public void removeProgressListener(ProgressListener var1) {
      if (!this.listeners.remove(var1) && debug) {
         Debug.say("Listener not registered: " + var1.toString());
      }

   }

   public final void setTaskCompletionListener(TaskCompletionNotificationListener var1) {
      this.taskCompletionListener = var1;
   }

   public final TaskCompletionNotificationListener getTaskCompletionListener() {
      return this.taskCompletionListener;
   }

   public String getTask() {
      return this.task;
   }

   public void reportEvent(String var1, String var2, String var3, String var4) throws ServerConnectionException {
      this.getResultTargetModuleIDs();
      this.msg = var4;
      if (this.listeners.size() != 0) {
         Iterator var5 = this.getTmidsForServer(var3).iterator();

         while(true) {
            TargetModuleIDImpl var6;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               var6 = (TargetModuleIDImpl)var5.next();
            } while(!var6.getModuleID().equals(var2) && !var6.getModuleID().equals(var1));

            this.broadcastReport(var4, var6);
         }
      }
   }

   public void reportEvent(String var1, String var2, String var3, String var4, Exception var5) {
      this.msg = var4;
      if (this.listeners.size() != 0) {
         this.broadcastReport(SPIDeployerLogger.reportErrorEvent(var1, var2, var3, var4, var5.toString()), (TargetModuleID)null);
      }
   }

   public void reportEvent(String var1) {
      this.msg = var1;
      if (this.listeners.size() != 0) {
         this.broadcastReport(var1, (TargetModuleID)null);
      }
   }

   public void reportEvent() {
      this.getResultTargetModuleIDs();
      if (this.listeners.size() != 0) {
         if (this.tmids != null && this.tmids.size() > 0) {
            for(int var1 = 0; var1 < this.tmids.size(); ++var1) {
               TargetModuleID var2 = (TargetModuleID)this.tmids.get(var1);
               if (debug) {
                  Debug.say("reporting final event for " + var2.getModuleID());
               }

               this.broadcastReport(this.msg, var2);
            }
         } else {
            this.broadcastReport(this.msg, (TargetModuleID)null);
         }

      }
   }

   public void setState(StateType var1) {
      if (debug) {
         Debug.say("Updating state to " + var1.toString());
      }

      this.state = var1;
   }

   public void setAction(ActionType var1) {
      if (debug) {
         Debug.say("Updating action to " + var1.toString());
      }

      this.action = var1;
   }

   public void setMessage(String var1) {
      if (debug) {
         Debug.say("Updating message to " + var1);
      }

      this.msg = var1;
   }

   public void setError(Throwable var1) {
      if (var1 != null) {
         var1 = ManagementException.unWrapExceptions(var1);
         this.setMessage(var1.toString());
         this.error = var1;
      }
   }

   private void broadcastReport(String var1, TargetModuleID var2) {
      DeploymentStatusImpl var3 = (DeploymentStatusImpl)this.getDeploymentStatus();
      var3.setMessage(var1);
      ProgressEvent var4 = new ProgressEvent(this, var2, var3);
      ProgressListener[] var5 = (ProgressListener[])((ProgressListener[])this.listeners.toArray(new ProgressListener[0]));
      ProgressListener[] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         ProgressListener var9 = var6[var8];
         var9.handleProgressEvent(var4);
      }

   }

   private Set getTmidsForServer(String var1) throws ServerConnectionException {
      Set var2 = this.getTargetsForServer(var1);
      HashSet var3 = new HashSet();
      if (this.tmids != null) {
         for(int var4 = 0; var4 < this.tmids.size(); ++var4) {
            TargetModuleID var5 = (TargetModuleID)this.tmids.get(var4);
            if (var2.contains(var5.getTarget())) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   private Set getTargetsForServer(String var1) throws ServerConnectionException {
      HashSet var5 = new HashSet();
      if (this.tmids != null) {
         for(int var6 = 0; var6 < this.tmids.size(); ++var6) {
            TargetModuleIDImpl var4 = (TargetModuleIDImpl)this.tmids.get(var6);
            Iterator var2 = var4.getServersForTarget().iterator();

            while(var2.hasNext()) {
               TargetImpl var3 = (TargetImpl)var2.next();
               if (var3.getName().equals(var1)) {
                  var5.add(var4.getTarget());
                  break;
               }
            }
         }
      }

      return var5;
   }

   private void dumpPO() {
      Debug.say("Constructed ProgressObject: ");
      if (this.cmd != null) {
         Debug.say("Command: " + this.cmd.toString());
      }

      if (this.task != null) {
         Debug.say("Task: " + this.task.toString());
      }

      Debug.say("TMIDs:");
      int var1;
      if (this.tmids != null) {
         for(var1 = 0; var1 < this.tmids.size(); ++var1) {
            Debug.say(this.tmids.get(var1).toString());
         }
      }

      Debug.say("Pending TMIDs:");
      if (this.pending != null) {
         for(var1 = 0; var1 < this.pending.size(); ++var1) {
            Debug.say(this.pending.get(var1).toString());
         }
      }

   }

   public DeploymentTaskRuntimeMBean getDtrm() {
      if (!this.haveDtrm) {
         this.dtrm = this.dm.getHelper().getTaskByID(this.getTask());
         this.haveDtrm = true;
      }

      return this.dtrm;
   }
}
