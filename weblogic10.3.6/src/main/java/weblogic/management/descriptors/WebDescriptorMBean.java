package weblogic.management.descriptors;

import weblogic.management.descriptors.webapp.WebAppDescriptorMBean;
import weblogic.management.descriptors.webappext.WebAppExtDescriptorMBean;
import weblogic.management.descriptors.webservice.WebServicesMBean;

public interface WebDescriptorMBean extends XMLElementMBean, TopLevelDescriptorMBean {
   void setWebAppDescriptor(WebAppDescriptorMBean var1);

   WebAppDescriptorMBean getWebAppDescriptor();

   void setWebAppExtDescriptor(WebAppExtDescriptorMBean var1);

   WebAppExtDescriptorMBean getWebAppExtDescriptor();

   void setWebServices(WebServicesMBean var1);

   WebServicesMBean getWebServices();
}
