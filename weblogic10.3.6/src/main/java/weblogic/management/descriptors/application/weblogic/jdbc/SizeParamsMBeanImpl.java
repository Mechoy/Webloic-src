package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SizeParamsMBeanImpl extends XMLElementMBeanDelegate implements SizeParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_shrinkFrequencySeconds = false;
   private int shrinkFrequencySeconds;
   private boolean isSet_shrinkingEnabled = false;
   private boolean shrinkingEnabled;
   private boolean isSet_maxCapacity = false;
   private int maxCapacity;
   private boolean isSet_highestNumWaiters = false;
   private int highestNumWaiters;
   private boolean isSet_shrinkPeriodMinutes = false;
   private int shrinkPeriodMinutes;
   private boolean isSet_initialCapacity = false;
   private int initialCapacity;
   private boolean isSet_highestNumUnavailable = false;
   private int highestNumUnavailable;
   private boolean isSet_capacityIncrement = false;
   private int capacityIncrement;

   public int getShrinkFrequencySeconds() {
      return this.shrinkFrequencySeconds;
   }

   public void setShrinkFrequencySeconds(int var1) {
      int var2 = this.shrinkFrequencySeconds;
      this.shrinkFrequencySeconds = var1;
      this.isSet_shrinkFrequencySeconds = var1 != -1;
      this.checkChange("shrinkFrequencySeconds", var2, this.shrinkFrequencySeconds);
   }

   public boolean isShrinkingEnabled() {
      return this.shrinkingEnabled;
   }

   public void setShrinkingEnabled(boolean var1) {
      boolean var2 = this.shrinkingEnabled;
      this.shrinkingEnabled = var1;
      this.isSet_shrinkingEnabled = true;
      this.checkChange("shrinkingEnabled", var2, this.shrinkingEnabled);
   }

   public int getMaxCapacity() {
      return this.maxCapacity;
   }

   public void setMaxCapacity(int var1) {
      int var2 = this.maxCapacity;
      this.maxCapacity = var1;
      this.isSet_maxCapacity = var1 != -1;
      this.checkChange("maxCapacity", var2, this.maxCapacity);
   }

   public int getHighestNumWaiters() {
      return this.highestNumWaiters;
   }

   public void setHighestNumWaiters(int var1) {
      int var2 = this.highestNumWaiters;
      this.highestNumWaiters = var1;
      this.isSet_highestNumWaiters = var1 != -1;
      this.checkChange("highestNumWaiters", var2, this.highestNumWaiters);
   }

   public int getShrinkPeriodMinutes() {
      return this.shrinkPeriodMinutes;
   }

   public void setShrinkPeriodMinutes(int var1) {
      int var2 = this.shrinkPeriodMinutes;
      this.shrinkPeriodMinutes = var1;
      this.isSet_shrinkPeriodMinutes = var1 != -1;
      this.checkChange("shrinkPeriodMinutes", var2, this.shrinkPeriodMinutes);
   }

   public int getInitialCapacity() {
      return this.initialCapacity;
   }

   public void setInitialCapacity(int var1) {
      int var2 = this.initialCapacity;
      this.initialCapacity = var1;
      this.isSet_initialCapacity = var1 != -1;
      this.checkChange("initialCapacity", var2, this.initialCapacity);
   }

   public int getHighestNumUnavailable() {
      return this.highestNumUnavailable;
   }

   public void setHighestNumUnavailable(int var1) {
      int var2 = this.highestNumUnavailable;
      this.highestNumUnavailable = var1;
      this.isSet_highestNumUnavailable = var1 != -1;
      this.checkChange("highestNumUnavailable", var2, this.highestNumUnavailable);
   }

   public int getCapacityIncrement() {
      return this.capacityIncrement;
   }

   public void setCapacityIncrement(int var1) {
      int var2 = this.capacityIncrement;
      this.capacityIncrement = var1;
      this.isSet_capacityIncrement = var1 != -1;
      this.checkChange("capacityIncrement", var2, this.capacityIncrement);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<size-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</size-params>\n");
      return var2.toString();
   }
}
