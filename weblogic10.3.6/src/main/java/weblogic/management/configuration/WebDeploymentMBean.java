package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;

public interface WebDeploymentMBean extends DeploymentMBean {
   /** @deprecated */
   WebServerMBean[] getWebServers();

   /** @deprecated */
   void setWebServers(WebServerMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean addWebServer(WebServerMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean removeWebServer(WebServerMBean var1) throws DistributedManagementException;

   VirtualHostMBean[] getVirtualHosts();

   void setVirtualHosts(VirtualHostMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean addVirtualHost(VirtualHostMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean removeVirtualHost(VirtualHostMBean var1) throws DistributedManagementException;

   VirtualHostMBean[] getDeployedVirtualHosts();

   void setDeployedVirtualHosts(VirtualHostMBean[] var1);
}
