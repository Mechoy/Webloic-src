package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JMSJDBCStoreMBean extends JMSStoreMBean, GenericJDBCStoreMBean {
   void setDelegatedBean(JDBCStoreMBean var1);

   JDBCStoreMBean getDelegatedBean();

   JDBCConnectionPoolMBean getConnectionPool();

   void setConnectionPool(JDBCConnectionPoolMBean var1) throws InvalidAttributeValueException;
}
