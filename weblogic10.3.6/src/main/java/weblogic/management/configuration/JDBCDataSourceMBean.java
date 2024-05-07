package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JDBCDataSourceMBean extends DeploymentMBean {
   void setJDBCSystemResource(JDBCSystemResourceMBean var1);

   JDBCSystemResourceMBean getJDBCSystemResource();

   String getJNDIName();

   void setJNDIName(String var1) throws InvalidAttributeValueException;

   String getJNDINameSeparator();

   void setJNDINameSeparator(String var1) throws InvalidAttributeValueException;

   String getPoolName();

   void setPoolName(String var1) throws InvalidAttributeValueException;

   boolean isRowPrefetchEnabled();

   void setRowPrefetchEnabled(boolean var1);

   int getRowPrefetchSize();

   void setRowPrefetchSize(int var1);

   int getStreamChunkSize();

   void setStreamChunkSize(int var1);

   boolean isWaitForConnectionEnabled();

   void setWaitForConnectionEnabled(boolean var1);

   int getConnectionWaitPeriod();

   void setConnectionWaitPeriod(int var1);
}
