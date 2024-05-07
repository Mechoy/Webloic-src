package weblogic.health;

import java.io.Serializable;

public final class HealthState implements Serializable {
   private static final long serialVersionUID = -8954060526499390055L;
   public static final int HEALTH_OK = 0;
   public static final int HEALTH_WARN = 1;
   public static final int HEALTH_CRITICAL = 2;
   public static final int HEALTH_FAILED = 3;
   public static final int HEALTH_OVERLOADED = 4;
   public static final String LOW_MEMORY_REASON = "server is low on memory";
   private static final String[] NULL_REASONS = new String[0];
   private final int state;
   private final String[] reasonCode;
   private String subsystemName;
   private static final int[] logicalSeverityOrder = new int[]{0, 1, 2, 4, 3};
   private boolean isCritical;
   private String mbeanName;
   private String mbeanType;

   public HealthState(int var1) {
      this(var1, NULL_REASONS);
   }

   public HealthState(int var1, String var2) {
      this.state = var1;
      if (var2 == null) {
         this.reasonCode = NULL_REASONS;
      } else {
         this.reasonCode = new String[]{var2};
      }

   }

   public HealthState(int var1, String[] var2) {
      this.state = var1;
      if (var2 == null) {
         this.reasonCode = NULL_REASONS;
      } else {
         this.reasonCode = var2;
      }

   }

   public String getSubsystemName() {
      return this.subsystemName;
   }

   public int getState() {
      return this.state;
   }

   public int compareSeverityTo(HealthState var1) {
      if (var1.getState() == this.getState()) {
         return 0;
      } else {
         int var2 = logicalSeverityOrder[var1.getState()];
         int var3 = logicalSeverityOrder[this.getState()];
         return var3 - var2;
      }
   }

   public String[] getReasonCode() {
      return this.reasonCode;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer("Component:" + this.subsystemName + ",");
      var1.append("State:" + mapToString(this.state) + ",");
      var1.append("MBean:" + this.mbeanName + ",");
      var1.append("ReasonCode:[");
      if (this.reasonCode != null && this.reasonCode.length > 0) {
         for(int var2 = 0; var2 < this.reasonCode.length - 1; ++var2) {
            var1.append(this.reasonCode[var2] + ",");
         }

         var1.append(this.reasonCode[this.reasonCode.length - 1]);
      }

      var1.append("]");
      return var1.toString();
   }

   public static String mapToString(int var0) {
      switch (var0) {
         case 0:
            return "HEALTH_OK";
         case 1:
            return "HEALTH_WARN";
         case 2:
            return "HEALTH_CRITICAL";
         case 3:
            return "HEALTH_FAILED";
         case 4:
            return "HEALTH_OVERLOADED";
         default:
            return "UNKNOWN";
      }
   }

   public void setSubsystemName(String var1) {
      this.subsystemName = var1;
   }

   public void setCritical(boolean var1) {
      this.isCritical = var1;
   }

   public boolean isCritical() {
      return this.isCritical;
   }

   public void setMBeanName(String var1) {
      this.mbeanName = var1;
   }

   public String getMBeanName() {
      return this.mbeanName;
   }

   public void setMBeanType(String var1) {
      this.mbeanType = var1;
   }

   public String getMBeanType() {
      return this.mbeanType;
   }
}
