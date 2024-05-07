package weblogic.management.descriptors.application.weblogic;

import weblogic.management.descriptors.XMLElementMBean;
import weblogic.management.descriptors.application.weblogic.jdbc.ConnectionFactoryMBean;
import weblogic.management.descriptors.application.weblogic.jdbc.DriverParamsMBean;
import weblogic.management.descriptors.application.weblogic.jdbc.PoolParamsMBean;

public interface JdbcConnectionPoolMBean extends XMLElementMBean {
   String getDataSourceName();

   void setDataSourceName(String var1);

   ConnectionFactoryMBean getConnectionFactory();

   void setConnectionFactory(ConnectionFactoryMBean var1);

   PoolParamsMBean getPoolParams();

   void setPoolParams(PoolParamsMBean var1);

   DriverParamsMBean getDriverParams();

   void setDriverParams(DriverParamsMBean var1);

   String getAclName();

   void setAclName(String var1);
}
