package weblogic.management;

import java.util.Date;
import javax.management.Notification;
import javax.management.ObjectName;

/** @deprecated */
public final class DeploymentNotification extends Notification {
   private static final long serialVersionUID = 1L;
   private static long sequenceNumber = 0L;
   private String appName;
   private String serverName;
   private String moduleName;
   private String transition;
   private String currentState;
   private String targetState;
   private boolean appNotification;
   private boolean moduleNotification;
   private String appPhase;
   public static final String PREPARING = "preparing";
   public static final String PREPARED = "prepared";
   public static final String ACTIVATING = "activating";
   public static final String ACTIVATED = "activated";
   public static final String DEACTIVATING = "deactivating";
   public static final String DEACTIVATED = "deactivated";
   public static final String UNPREPARING = "unpreparing";
   public static final String UNPREPARED = "unprepared";
   public static final String FAILED = "failed";
   public static final String DISTRIBUTING = "distributing";
   public static final String DISTRIBUTED = "distributed";
   public static final String TYPE_APPLICATION = "weblogic.deployment.application";
   public static final String TYPE_MODULE = "weblogic.deployment.application.module";
   public static final String TRANSITION_BEGIN = "begin";
   public static final String TRANSITION_END = "end";
   public static final String TRANSITION_FAILED = "failed";
   public static final String STATE_UNPREPARED = "unprepared";
   public static final String STATE_PREPARED = "prepared";
   public static final String STATE_ACTIVE = "active";
   public static final String STATE_START = "start";
   private String task;

   public String getTask() {
      return this.task;
   }

   public void setTask(String var1) {
      this.task = var1;
   }

   public DeploymentNotification(ObjectName var1, long var2, String var4, String var5, String var6) {
      super("weblogic.deployment.application", var1, var2, (new Date()).getTime(), var6 + "." + var4 + "." + var5);
      this.appName = null;
      this.serverName = null;
      this.moduleName = null;
      this.transition = null;
      this.currentState = null;
      this.targetState = null;
      this.appNotification = false;
      this.moduleNotification = false;
      this.task = null;
      this.appName = var5;
      this.serverName = var4;
      this.appNotification = true;
      this.appPhase = var6;
      if (var6.endsWith("ed")) {
         this.transition = "end";
      } else {
         this.transition = "begin";
      }

      if (var6.equals("distributing")) {
         this.currentState = "start";
         this.targetState = "unprepared";
      } else if (var6.equals("distributed")) {
         this.currentState = "start";
         this.targetState = "unprepared";
      } else if (var6.equals("preparing")) {
         this.currentState = "unprepared";
         this.targetState = "prepared";
      } else if (var6.equals("prepared")) {
         this.currentState = "unprepared";
         this.targetState = "prepared";
      } else if (var6.equals("activating")) {
         this.currentState = "prepared";
         this.targetState = "active";
      } else if (var6.equals("activated")) {
         this.currentState = "prepared";
         this.targetState = "active";
      } else if (var6.equals("unpreparing")) {
         this.currentState = "prepared";
         this.targetState = "unprepared";
      } else if (var6.equals("unprepared")) {
         this.currentState = "prepared";
         this.targetState = "unprepared";
      } else if (var6.equals("failed")) {
         this.transition = "failed";
      }

   }

   public DeploymentNotification(ObjectName var1, long var2, String var4, String var5, String var6, String var7, String var8, String var9, long var10) {
      super("weblogic.deployment.application.module", var1, var2, var10, var4 + "." + var5 + "." + var6 + "." + var7 + "." + var8 + "." + var9);
      this.appName = null;
      this.serverName = null;
      this.moduleName = null;
      this.transition = null;
      this.currentState = null;
      this.targetState = null;
      this.appNotification = false;
      this.moduleNotification = false;
      this.task = null;
      this.appName = var5;
      this.serverName = var4;
      this.moduleName = var6;
      this.transition = var7;
      this.currentState = var8;
      this.targetState = var9;
      this.moduleNotification = true;
   }

   public DeploymentNotification(ObjectName var1, long var2, String var4, String var5, String var6, String var7, String var8, String var9) {
      this(var1, var2, var4, var5, var6, var7, var8, var9, (new Date()).getTime());
   }

   public static long getChangeNotificationCount() {
      return sequenceNumber;
   }

   private static synchronized long generateSequenceNumber() {
      return (long)(sequenceNumber++);
   }

   public String getPhase() {
      return this.appPhase;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getServerName() {
      return this.serverName;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getTransition() {
      return this.transition;
   }

   public boolean isEndTransition() {
      return "end".equals(this.transition);
   }

   public boolean isBeginTransition() {
      return "begin".equals(this.transition);
   }

   public boolean isFailedTransition() {
      return "failed".equals(this.transition);
   }

   public String getCurrentState() {
      return this.currentState;
   }

   public String getTargetState() {
      return this.targetState;
   }

   public boolean isAppNotification() {
      return this.appNotification;
   }

   public boolean isModuleNotification() {
      return this.moduleNotification;
   }

   public String toString() {
      return "[DeploymentNotification: " + "AppName:" + this.appName + ", ModName:" + this.moduleName + ", ServerName:" + this.serverName + ", " + this.transition + "." + this.currentState + "." + this.targetState + "]";
   }
}
