package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface MachineMBean extends ConfigurationMBean {
   /** @deprecated */
   String[] getAddresses();

   /** @deprecated */
   void setAddresses(String[] var1) throws InvalidAttributeValueException;

   NodeManagerMBean getNodeManager();
}
