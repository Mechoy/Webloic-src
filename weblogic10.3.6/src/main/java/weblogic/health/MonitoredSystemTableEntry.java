package weblogic.health;

import weblogic.management.runtime.RuntimeMBean;

final class MonitoredSystemTableEntry {
   private final String key;
   private final boolean isCritical;
   private final HealthFeedback hf;
   private final String MBeanName;
   private final String MBeanType;

   MonitoredSystemTableEntry(String var1, RuntimeMBean var2, boolean var3) {
      this.key = var1;
      this.MBeanName = var2.getName();
      this.MBeanType = var2.getType();
      this.isCritical = var3;
      this.hf = (HealthFeedback)var2;
   }

   MonitoredSystemTableEntry(String var1, HealthFeedback var2, boolean var3) {
      this.key = var1;
      this.hf = var2;
      this.isCritical = var3;
      this.MBeanName = "";
      this.MBeanType = "";
   }

   String getKey() {
      return this.key;
   }

   HealthFeedback getHealthFeedback() {
      return this.hf;
   }

   boolean getIsCritical() {
      return this.isCritical;
   }

   String getMBeanName() {
      return this.MBeanName;
   }

   String getMBeanType() {
      return this.MBeanType;
   }
}
