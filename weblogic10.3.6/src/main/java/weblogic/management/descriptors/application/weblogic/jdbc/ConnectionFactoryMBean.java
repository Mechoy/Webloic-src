package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBean;

public interface ConnectionFactoryMBean extends XMLElementMBean {
   String getFactoryName();

   void setFactoryName(String var1);

   ConnectionPropertiesMBean getConnectionProperties();

   void setConnectionProperties(ConnectionPropertiesMBean var1);
}
