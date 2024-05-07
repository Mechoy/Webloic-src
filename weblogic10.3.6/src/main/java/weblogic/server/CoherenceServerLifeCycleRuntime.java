package weblogic.server;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.management.ManagementException;
import weblogic.management.configuration.CoherenceServerMBean;
import weblogic.management.configuration.CoherenceServerStartMBean;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ManagedExternalServerMBean;
import weblogic.management.runtime.CoherenceServerLifeCycleRuntimeMBean;
import weblogic.management.runtime.CoherenceServerLifeCycleTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBeanDelegate;
import weblogic.nodemanager.mbean.NodeManagerRuntime;
import weblogic.nodemanager.mbean.NodeManagerTask;
import weblogic.work.ContextWrap;
import weblogic.work.WorkManagerFactory;

public class CoherenceServerLifeCycleRuntime extends DomainRuntimeMBeanDelegate implements CoherenceServerLifeCycleRuntimeMBean {
   private static final int TASK_AFTERLIFE_TIME_MILLIS = 1800000;
   private final Set<CoherenceServerLifeCycleTaskRuntime> tasks;
   private final String serverName;
   private final CoherenceServerMBean serverMBean;
   private String oldState;
   private String currentState;
   private int startCount;

   CoherenceServerLifeCycleRuntime(CoherenceServerMBean var1) throws ManagementException {
      super(var1.getName());
      this.serverMBean = var1;
      this.tasks = Collections.synchronizedSet(new HashSet());
      this.serverName = var1.getName();
   }

   public void clearOldServerLifeCycleTaskRuntimes() {
      synchronized(this.tasks) {
         Iterator var2 = this.tasks.iterator();

         while(var2.hasNext()) {
            CoherenceServerLifeCycleTaskRuntime var3 = (CoherenceServerLifeCycleTaskRuntime)var2.next();
            if (var3.getEndTime() > 0L && System.currentTimeMillis() - var3.getEndTime() > 1800000L) {
               try {
                  var3.unregister();
               } catch (ManagementException var6) {
               }

               var2.remove();
            }
         }

      }
   }

   public CoherenceServerLifeCycleTaskRuntimeMBean forceShutdown() throws ServerLifecycleException {
      CoherenceServerLifeCycleTaskRuntime var3;
      try {
         if (this.getState() == "SHUTDOWN") {
            throw new ServerLifecycleException("Can not get to the relevant ServerRuntimeMBean for server " + this.serverName + ". Server is in SHUTDOWN state and cannot be reached.");
         }

         CoherenceServerLifeCycleTaskRuntime var1 = new CoherenceServerLifeCycleTaskRuntime(this, "Forcefully shutting down " + this.serverName + " server ...", "forceShutdown");
         this.tasks.add(var1);
         ShutdownRequest var2 = new ShutdownRequest(var1);
         WorkManagerFactory.getInstance().getSystem().schedule(new ContextWrap(var2));
         var3 = var1;
      } catch (ManagementException var8) {
         throw new ServerLifecycleException(var8);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var3;
   }

   public int getNodeManagerRestartCount() {
      return this.startCount > 0 ? this.startCount - 1 : 0;
   }

   public String getState() {
      String var1 = this.getStateNodeManager();
      if (var1 == null || "UNKNOWN".equalsIgnoreCase(var1)) {
         var1 = "UNKNOWN";
      }

      this.clearOldServerLifeCycleTaskRuntimes();
      return var1;
   }

   private String getStateNodeManager() {
      MachineMBean var1 = this.serverMBean.getMachine();
      if (var1 == null) {
         return null;
      } else {
         try {
            return NodeManagerRuntime.getInstance(var1).getState((ManagedExternalServerMBean)this.serverMBean);
         } catch (IOException var3) {
            return null;
         }
      }
   }

   public int getStateVal() {
      String var1 = this.getState().intern();
      if (var1 != "STARTING" && var1 != "FAILED_RESTARTING") {
         if (var1 == "SHUTTING_DOWN") {
            return 7;
         } else if (var1 == "FORCE_SHUTTING_DOWN") {
            return 18;
         } else if (var1 == "STANDBY") {
            return 3;
         } else if (var1 == "ADMIN") {
            return 17;
         } else if (var1 == "SUSPENDING") {
            return 4;
         } else if (var1 == "RESUMING") {
            return 6;
         } else if (var1 == "RUNNING") {
            return 2;
         } else if (var1 == "SHUTDOWN") {
            return 0;
         } else if (var1 == "FAILED") {
            return 8;
         } else if (var1 == "ACTIVATE_LATER") {
            return 13;
         } else if (var1 == "FAILED_NOT_RESTARTABLE") {
            return 14;
         } else {
            return var1 == "FAILED_MIGRATABLE" ? 15 : 9;
         }
      } else {
         return 1;
      }
   }

   public CoherenceServerLifeCycleTaskRuntimeMBean[] getTasks() {
      return (CoherenceServerLifeCycleTaskRuntimeMBean[])this.tasks.toArray(new CoherenceServerLifeCycleTaskRuntimeMBean[this.tasks.size()]);
   }

   public void setState(String var1) {
      synchronized(this) {
         if (var1 == null || var1.equalsIgnoreCase(this.currentState)) {
            return;
         }

         this.oldState = this.currentState;
         this.currentState = var1;
         if (!"SHUTTING_DOWN".equals(var1) && !"FORCE_SHUTTING_DOWN".equals(var1) && !"STARTING".equals(var1) && !"RESUMING".equals(var1) && "RUNNING".equals(var1)) {
         }
      }

      this._postSet("State", this.oldState, this.currentState);
   }

   public CoherenceServerLifeCycleTaskRuntimeMBean start() throws ServerLifecycleException {
      CoherenceServerLifeCycleTaskRuntime var2;
      try {
         CoherenceServerLifeCycleTaskRuntime var1 = new CoherenceServerLifeCycleTaskRuntime(this, "Starting " + this.serverName + " server ...", "start");
         this.tasks.add(var1);
         this.startServer(var1);
         updateTaskMBeanOnCompletion(var1);
         var2 = var1;
      } catch (ManagementException var7) {
         throw new ServerLifecycleException(var7);
      } finally {
         this.clearOldServerLifeCycleTaskRuntimes();
      }

      return var2;
   }

   private void startServer(CoherenceServerLifeCycleTaskRuntime var1) {
      try {
         NodeManagerRuntime var2 = null;
         var2 = NodeManagerRuntime.getInstance((ManagedExternalServerMBean)this.serverMBean);
         NodeManagerTask var3 = var2.start((ManagedExternalServerMBean)this.serverMBean);
         ++this.startCount;
         var1.setNMTask(var3);
      } catch (SecurityException var4) {
         var1.setError(var4);
      } catch (IOException var5) {
         var1.setError(var5);
      }

   }

   void cleanup() {
   }

   private boolean useNodeManagerToShutdown() throws IOException {
      CoherenceServerStartMBean var1 = this.serverMBean.getCoherenceServerStart();
      if (var1 == null) {
         return false;
      } else {
         MachineMBean var2 = this.serverMBean.getMachine();
         if (var2 == null) {
            return false;
         } else {
            NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(var2);
            if (var3 == null) {
               return false;
            } else {
               var3.kill((ManagedExternalServerMBean)this.serverMBean);
               return true;
            }
         }
      }
   }

   private static void updateTaskMBeanOnCompletion(CoherenceServerLifeCycleTaskRuntime var0) {
      if (var0.getError() != null) {
         var0.setStatus("FAILED");
      } else {
         var0.setStatus("TASK COMPLETED");
      }

      var0.setEndTime(System.currentTimeMillis());
      var0.setIsRunning(false);
   }

   private final class ShutdownRequest implements Runnable {
      private final CoherenceServerLifeCycleTaskRuntime taskMBean;

      ShutdownRequest(CoherenceServerLifeCycleTaskRuntime var2) {
         this.taskMBean = var2;
      }

      public void run() {
         try {
            CoherenceServerLifeCycleRuntime.this.useNodeManagerToShutdown();
         } catch (IOException var6) {
            this.taskMBean.setError(var6);
         } finally {
            CoherenceServerLifeCycleRuntime.updateTaskMBeanOnCompletion(this.taskMBean);
         }

      }
   }
}
