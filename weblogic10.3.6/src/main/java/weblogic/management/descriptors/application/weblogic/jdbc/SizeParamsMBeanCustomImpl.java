package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class SizeParamsMBeanCustomImpl extends XMLElementMBeanDelegate implements SizeParamsMBean {
   private String descrEncoding = null;
   private String descriptorVersion = null;
   private Integer initialCapacity;
   private Integer maxCapacity;
   private Integer capacityIncrement;
   private Boolean shrinkingEnabled;
   private Integer shrinkPeriodMinutes;
   private Integer shrinkFrequencySeconds;
   private Integer highestNumWaiters;
   private Integer highestNumUnavailable;

   public void setEncoding(String var1) {
      this.descrEncoding = var1;
   }

   public void setVersion(String var1) {
      String var2 = this.descriptorVersion;
      this.descriptorVersion = var1;
      this.checkChange("version", var2, this.descriptorVersion);
   }

   public void setInitialCapacity(int var1) {
      Integer var2 = this.initialCapacity;
      this.initialCapacity = new Integer(var1);
      this.checkChange("initialCapacity", var2, this.initialCapacity);
   }

   public void setMaxCapacity(int var1) {
      Integer var2 = this.maxCapacity;
      this.maxCapacity = new Integer(var1);
      this.checkChange("maxCapacity", var2, this.maxCapacity);
   }

   public void setCapacityIncrement(int var1) {
      Integer var2 = this.capacityIncrement;
      this.capacityIncrement = new Integer(var1);
      this.checkChange("capacityIncrement", var2, this.capacityIncrement);
   }

   public void setShrinkingEnabled(boolean var1) {
      Boolean var2 = this.shrinkingEnabled;
      this.shrinkingEnabled = new Boolean(var1);
      this.checkChange("shrinkingEnabled", var2, this.shrinkingEnabled);
   }

   public void setShrinkPeriodMinutes(int var1) {
      Integer var2 = this.shrinkPeriodMinutes;
      this.shrinkPeriodMinutes = new Integer(var1);
      this.checkChange("shrinkPeriodMinutes", var2, this.shrinkPeriodMinutes);
   }

   public void setShrinkFrequencySeconds(int var1) {
      Integer var2 = this.shrinkFrequencySeconds;
      this.shrinkFrequencySeconds = new Integer(var1);
      this.checkChange("shrinkFrequencySeconds", var2, this.shrinkFrequencySeconds);
   }

   public void setHighestNumWaiters(int var1) {
      Integer var2 = this.highestNumWaiters;
      this.highestNumWaiters = new Integer(var1);
      this.checkChange("highestNumWaiters", var2, this.highestNumWaiters);
   }

   public void setHighestNumUnavailable(int var1) {
      Integer var2 = this.highestNumUnavailable;
      this.highestNumUnavailable = new Integer(var1);
      this.checkChange("highestNumUnavailable", var2, this.highestNumUnavailable);
   }

   public String getEncoding() {
      return this.descrEncoding;
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public int getInitialCapacity() {
      return this.initialCapacity != null ? this.initialCapacity : 1;
   }

   public int getMaxCapacity() {
      return this.maxCapacity != null ? this.maxCapacity : 15;
   }

   public int getCapacityIncrement() {
      return this.capacityIncrement != null ? this.capacityIncrement : 1;
   }

   public boolean isShrinkingEnabled() {
      return this.shrinkingEnabled != null ? this.shrinkingEnabled : true;
   }

   public int getShrinkPeriodMinutes() {
      return this.shrinkPeriodMinutes != null ? this.shrinkPeriodMinutes : 15;
   }

   public int getShrinkFrequencySeconds() {
      return this.shrinkFrequencySeconds != null ? this.shrinkFrequencySeconds : 900;
   }

   public int getHighestNumWaiters() {
      return this.highestNumWaiters != null ? this.highestNumWaiters : Integer.MAX_VALUE;
   }

   public int getHighestNumUnavailable() {
      return this.highestNumUnavailable != null ? this.highestNumUnavailable : 0;
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<size-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1 + 2)).append("<initial-capacity>").append(this.getInitialCapacity()).append("</initial-capacity>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<max-capacity>").append(this.getMaxCapacity()).append("</max-capacity>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<capacity-increment>").append(this.getCapacityIncrement()).append("</capacity-increment>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<shrinking-enabled>").append(ToXML.capitalize((new Boolean(this.isShrinkingEnabled())).toString())).append("</shrinking-enabled>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<shrink-period-minutes>").append(this.getShrinkPeriodMinutes()).append("</shrink-period-minutes>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<shrink-frequency-seconds>").append(this.getShrinkFrequencySeconds()).append("</shrink-frequency-seconds>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<highest-num-waiters>").append(this.getHighestNumWaiters()).append("</highest-num-waiters>\n");
      var2.append(ToXML.indent(var1 + 2)).append("<highest-num-unavailable>").append(this.getHighestNumUnavailable()).append("</highest-num-unavailable>\n");
      var2.append(ToXML.indent(var1)).append("</size-params>\n");
      return var2.toString();
   }
}
