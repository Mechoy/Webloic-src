package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JDBCMultiPoolMBean extends DeploymentMBean {
   void setJDBCSystemResource(JDBCSystemResourceMBean var1);

   JDBCSystemResourceMBean getJDBCSystemResource();

   String getACLName();

   void setACLName(String var1) throws InvalidAttributeValueException;

   JDBCConnectionPoolMBean[] getPoolList();

   void setPoolList(JDBCConnectionPoolMBean[] var1) throws InvalidAttributeValueException;

   void setLoadBalance(boolean var1) throws InvalidAttributeValueException;

   boolean isLoadBalance();

   void setHighAvail(boolean var1) throws InvalidAttributeValueException;

   boolean isHighAvail();

   void setAlgorithmType(String var1);

   String getAlgorithmType();

   String getConnectionPoolFailoverCallbackHandler();

   void setConnectionPoolFailoverCallbackHandler(String var1);

   void setFailoverRequestIfBusy(boolean var1);

   boolean getFailoverRequestIfBusy();

   void setHealthCheckFrequencySeconds(int var1);

   int getHealthCheckFrequencySeconds();
}
