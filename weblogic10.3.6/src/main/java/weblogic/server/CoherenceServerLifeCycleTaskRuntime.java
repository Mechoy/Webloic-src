package weblogic.server;

import java.io.PrintWriter;
import weblogic.management.ManagementException;
import weblogic.management.runtime.CoherenceServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.management.runtime.TaskRuntimeMBean;
import weblogic.nodemanager.mbean.NodeManagerTask;

public class CoherenceServerLifeCycleTaskRuntime extends DomainRuntimeMBeanDelegate implements CoherenceServerLifeCycleTaskRuntimeMBean {
   private long beginTime = System.currentTimeMillis();
   private long endTime;
   private static int num = 0;
   private String description = null;
   private boolean isRunning = true;
   private String status = "TASK IN PROGRESS";
   private Exception exception;
   private NodeManagerTask nmTask = null;
   private String serverName = null;
   private String operation = null;

   public CoherenceServerLifeCycleTaskRuntime(CoherenceServerLifeCycleRuntime var1, String var2, String var3) throws ManagementException {
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

   public String getDescription() {
      return this.description;
   }

   public String getStatus() {
      throw new UnsupportedOperationException();
   }

   public boolean isRunning() {
      return this.nmTask != null ? !this.nmTask.isFinished() : this.isRunning;
   }

   public long getBeginTime() {
      throw new UnsupportedOperationException();
   }

   public long getEndTime() {
      if (this.nmTask != null) {
         return this.nmTask.getEndTime();
      } else {
         return this.isRunning ? -1L : this.endTime;
      }
   }

   public TaskRuntimeMBean[] getSubTasks() {
      throw new UnsupportedOperationException();
   }

   public TaskRuntimeMBean getParentTask() {
      throw new UnsupportedOperationException();
   }

   public void cancel() throws Exception {
      throw new UnsupportedOperationException();
   }

   public void printLog(PrintWriter var1) {
      throw new UnsupportedOperationException();
   }

   public Exception getError() {
      return this.nmTask != null ? this.nmTask.getError() : this.exception;
   }

   public boolean isSystemTask() {
      throw new UnsupportedOperationException();
   }

   public void setSystemTask(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public void setNMTask(NodeManagerTask var1) {
      this.nmTask = var1;
   }

   public void setError(Exception var1) {
      this.exception = var1;
   }

   public void setStatus(String var1) {
      this.status = var1;
   }

   public void setEndTime(long var1) {
      this.endTime = var1;
   }

   public void setIsRunning(boolean var1) {
      this.isRunning = var1;
   }
}
