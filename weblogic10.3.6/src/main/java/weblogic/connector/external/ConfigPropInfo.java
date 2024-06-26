package weblogic.connector.external;

import weblogic.j2ee.descriptor.ConfigPropertyBean;

public interface ConfigPropInfo {
   String getDescription();

   String[] getDescriptions();

   String getName();

   String getType();

   String getValue();

   ConfigPropertyBean getConfigPropertyBean();
}
