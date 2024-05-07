package weblogic.server;

import java.io.IOException;
import java.io.PrintWriter;
import weblogic.management.ManagementException;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.ServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.nodemanager.mbean.NodeManagerTask;

public class ServerLifeCycleTaskRuntime extends DomainRuntimeMBeanDelegate implements ServerLifeCycleTaskRuntimeMBean {
   private long beginTime = System.currentTimeMillis();
   private long endTime;
   private String description = null;
   private boolean isRunning = true;
   private String status = "TASK IN PROGRESS";
   private Exception exception;
   private static int num = 0;
   private boolean sysTask = false;
   private NodeManagerTask nmTask = null;
   private String serverName = null;
   private String operation = null;

   public ServerLifeCycleTaskRuntime(ServerLifeCycleRuntime var1, String var2, String var3) throws ManagementException {
      super("_" + getSequenceNumber() + "_" + var3, var1, true, "Tasks");
      this.description = var2;
      this.serverName = var1.getName();
      this.operation = var3;
   }

   private static synchronized int getSequenceNumber() {
      return num++;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getOperation() {
      return this.operation;
   }

   public void cancel() throws Exception {
      if (this.nmTask == null) {
         throw new Exception("Operation can not be cancelled ...");
      } else {
         this.nmTask.cancel();
      }
   }

   public long getBeginTime() {
      return this.nmTask != null ? this.nmTask.getBeginTime() : this.beginTime;
   }

   public void setEndTime(long var1) {
      this.endTime = var1;
   }

   public long getEndTime() {
      if (this.nmTask != null) {
         return this.nmTask.getEndTime();
      } else {
         return this.isRunning ? -1L : this.endTime;
      }
   }

   public void unregister() throws ManagementException {
      super.unregister();
      if (this.nmTask != null) {
         try {
            this.nmTask.cleanup();
         } catch (IOException var2) {
            throw new ManagementException(var2);
         }
      }

   }

   public void setError(Exception var1) {
      this.exception = var1;
   }

   public Exception getError() {
      return this.nmTask != null ? this.nmTask.getError() : this.exception;
   }

   public void setStatus(String var1) {
      this.status = var1;
   }

   public String getStatus() {
      return this.nmTask != null ? this.nmTask.getStatus() : this.status;
   }

   public void setisRunning(boolean var1) {
      this.isRunning = var1;
   }

   public boolean isRunning() {
      return this.nmTask != null ? !this.nmTask.isFinished() : this.isRunning;
   }

   public String getDescription() {
      return this.description;
   }

   public NodeManagerTask getNMTask() {
      return this.nmTask;
   }

   public void setNMTask(NodeManagerTask var1) {
      this.nmTask = var1;
   }

   public void setIsRunning(boolean var1) {
      this.isRunning = var1;
   }

   public boolean isSystemTask() {
      return this.sysTask;
   }

   public void setSystemTask(boolean var1) {
      this.sysTask = var1;
   }

   public TaskRuntimeMBean[] getSubTasks() {
      return null;
   }

   public TaskRuntimeMBean getParentTask() {
      return null;
   }

   public void printLog(PrintWriter var1) {
      var1.println("This feature is to be replaced as per CR184195");
   }
}
