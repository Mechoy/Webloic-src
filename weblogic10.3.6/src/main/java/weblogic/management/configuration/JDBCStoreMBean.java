package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface JDBCStoreMBean extends GenericJDBCStoreMBean, PersistentStoreMBean {
   JDBCSystemResourceMBean getDataSource();

   void setDataSource(JDBCSystemResourceMBean var1) throws InvalidAttributeValueException;

   JDBCConnectionPoolMBean getConnectionPool();

   void setConnectionPool(JDBCConnectionPoolMBean var1) throws InvalidAttributeValueException;

   int getDeletesPerBatchMaximum();

   void setDeletesPerBatchMaximum(int var1) throws InvalidAttributeValueException;

   int getInsertsPerBatchMaximum();

   void setInsertsPerBatchMaximum(int var1) throws InvalidAttributeValueException;

   int getDeletesPerStatementMaximum();

   void setDeletesPerStatementMaximum(int var1) throws InvalidAttributeValueException;

   int getWorkerCount();

   void setWorkerCount(int var1) throws InvalidAttributeValueException;

   int getWorkerPreferredBatchSize();

   void setWorkerPreferredBatchSize(int var1) throws InvalidAttributeValueException;

   int getThreeStepThreshold();

   void setThreeStepThreshold(int var1) throws InvalidAttributeValueException;
}
