package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.management.ManagementException;

/** @deprecated */
public interface AdminServerMBean extends ConfigurationMBean {
   /** @deprecated */
   ServerMBean getServer();

   /** @deprecated */
   DomainMBean getActiveDomain();

   String getName();

   void setName(String var1) throws InvalidAttributeValueException, ManagementException;
}
