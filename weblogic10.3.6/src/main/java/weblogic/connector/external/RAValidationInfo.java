package weblogic.connector.external;

import java.util.Collection;
import weblogic.j2ee.descriptor.ConfigPropertyBean;

public interface RAValidationInfo {
   boolean isCompliant();

   boolean isInbound();

   boolean hasRAbean();

   boolean hasRAxml();

   boolean isLinkRef();

   String getLinkRef();

   PropSetterTable getRAPropSetterTable();

   String getModuleName();

   PropSetterTable getAdminPropSetterTable(String var1);

   PropSetterTable getConnectionFactoryPropSetterTable(String var1);

   ConfigPropertyBean getProperty(String var1, Collection var2);

   Collection getAllAdminPropSetters();

   Collection getAllConnectionFactoryPropSetters();
}
