package weblogic.management.mbeanservers.edit.internal;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.Change;
import weblogic.management.mbeanservers.edit.ServerStatus;
import weblogic.management.mbeanservers.internal.ServiceImpl;
import weblogic.management.provider.ActivateTask;

public class ActivationTaskMBeanImpl extends ServiceImpl implements ActivationTaskMBean {
   private ActivateTask activateTask;
   private ConfigurationManagerMBeanImpl manager;
   private boolean completed;
   private int state;
   private ServerStatus[] serverStatus;
   private Exception taskError;
   private long beginTime;
   private long endTime;
   private String user;
   private Change[] changes;
   private String taskId;

   public ActivationTaskMBeanImpl(ConfigurationManagerMBeanImpl var1, ActivateTask var2) {
      super(Long.toString(var2.getTaskId()), ActivationTaskMBean.class.getName(), var1);
      this.activateTask = var2;
      this.manager = var1;
   }

   public int getState() {
      return this.completed ? this.state : this.activateTask.getState();
   }

   public ServerStatus[] getStatusByServer() {
      if (this.completed) {
         return this.serverStatus;
      } else {
         Map var1 = this.activateTask.getStateOnServers();
         Map var2 = this.activateTask.getFailedServers();
         ServerStatus[] var3 = new ServerStatus[var1.size()];
         Set var4 = var1.entrySet();
         int var5 = 0;

         for(Iterator var6 = var4.iterator(); var6 != null && var6.hasNext(); ++var5) {
            Map.Entry var7 = (Map.Entry)var6.next();
            Exception var8 = (Exception)var2.get(var7.getKey());
            var3[var5] = new ServerStatusImpl((String)var7.getKey(), (Integer)var7.getValue(), var8);
         }

         return var3;
      }
   }

   public String getDetails() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Activation Task started at " + this.getStartTime() + "\n");
      var1.append("User that initiated this task " + this.getUser() + "\n");
      var1.append("Changes that are being activated are: \n");
      Change[] var2 = this.getChanges();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.append("    " + var2[var3].toString());
      }

      var1.append("Status of this activation per server: \n");
      ServerStatus[] var5 = this.getStatusByServer();

      for(int var4 = 0; var4 < var5.length; ++var4) {
         var1.append("  ServerName : " + var5[var4].getServerName() + "\n");
         var1.append("    Status : " + this.statusString(var5[var4].getServerState()) + "\n");
         if (var5[var4].getServerState() == 5) {
            var1.append("    Exception : " + var5[var4].getServerException() + "\n");
         }
      }

      return var1.toString();
   }

   private String statusString(int var1) {
      if (var1 == 0) {
         return "NEW";
      } else if (var1 == 4) {
         return "COMMITTED";
      } else if (var1 == 2) {
         return "DISTRIBUTED";
      } else if (var1 == 1) {
         return "DISTRIBUTING";
      } else if (var1 == 5) {
         return "FAILED";
      } else if (var1 == 3) {
         return "PENDING";
      } else if (var1 == 6) {
         return "CANCELING";
      } else {
         return var1 == 7 ? "COMMIT_FAILING" : "NEW";
      }
   }

   public Exception getError() {
      return this.completed ? this.taskError : this.activateTask.getError();
   }

   public long getStartTime() {
      return this.completed ? this.beginTime : this.activateTask.getBeginTime();
   }

   public long getCompletionTime() {
      return this.completed ? this.endTime : this.activateTask.getEndTime();
   }

   public String getUser() {
      return this.completed ? this.user : this.activateTask.getUser();
   }

   public Change[] getChanges() {
      return this.completed ? this.changes : this.manager.convertBeanUpdatesToChanges(this.activateTask.getChanges());
   }

   public void waitForTaskCompletion() {
      if (!this.completed) {
         this.activateTask.waitForTaskCompletion();
      }
   }

   public void waitForTaskCompletion(long var1) {
      if (!this.completed) {
         this.activateTask.waitForTaskCompletion(var1);
      }
   }

   public ActivateTask getActivateTask() {
      return this.activateTask;
   }

   void movingToCompleted() {
      this.state = this.getState();
      this.serverStatus = this.getStatusByServer();
      this.taskError = this.getError();
      this.beginTime = this.getStartTime();
      this.endTime = this.getCompletionTime();
      this.user = this.getUser();
      this.changes = this.getChanges();
      this.taskId = this.getName();
      this.completed = true;
      this.activateTask = null;
   }
}
