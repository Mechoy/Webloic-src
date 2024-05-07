package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;

public interface SystemResourceMBean extends BasicDeploymentMBean, ConfigurationExtensionMBean {
   String getSourcePath();

   DescriptorBean getResource();

   void setName(String var1) throws InvalidAttributeValueException, ManagementException;
}
