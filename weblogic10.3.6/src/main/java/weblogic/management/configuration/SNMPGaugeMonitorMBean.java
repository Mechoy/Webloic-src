package weblogic.management.configuration;

public interface SNMPGaugeMonitorMBean extends SNMPJMXMonitorMBean {
   int getThresholdHigh();

   void setThresholdHigh(int var1);

   int getThresholdLow();

   void setThresholdLow(int var1);
}
