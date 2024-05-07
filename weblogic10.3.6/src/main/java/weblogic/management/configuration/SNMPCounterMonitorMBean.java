package weblogic.management.configuration;

public interface SNMPCounterMonitorMBean extends SNMPJMXMonitorMBean {
   int getThreshold();

   void setThreshold(int var1);

   int getOffset();

   void setOffset(int var1);

   int getModulus();

   void setModulus(int var1);
}
