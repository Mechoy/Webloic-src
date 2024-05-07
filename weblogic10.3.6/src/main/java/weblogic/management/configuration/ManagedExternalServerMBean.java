package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.ManagementException;

public interface ManagedExternalServerMBean extends ConfigurationMBean {
   void setName(String var1) throws InvalidAttributeValueException, ManagementException;

   String getName();

   MachineMBean getMachine();

   void setMachine(MachineMBean var1) throws InvalidAttributeValueException;

   boolean getAutoRestart();

   void setAutoRestart(boolean var1);

   int getRestartIntervalSeconds();

   void setRestartIntervalSeconds(int var1) throws InvalidAttributeValueException;

   int getRestartMax();

   void setRestartMax(int var1) throws InvalidAttributeValueException;

   int getRestartDelaySeconds();

   void setRestartDelaySeconds(int var1) throws InvalidAttributeValueException;

   int getNMSocketCreateTimeoutInMillis();

   void setNMSocketCreateTimeoutInMillis(int var1) throws InvalidAttributeValueException, DistributedManagementException;

   ManagedExternalServerStartMBean getManagedExternalServerStart();
}
