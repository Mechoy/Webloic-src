package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.management.DistributedManagementException;

/** @deprecated */
public interface JMSTemplateMBean extends JMSDestCommonMBean, ConfigurationMBean, JMSConstants {
   JMSDestinationMBean[] getDestinations();

   boolean addDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   boolean removeDestination(JMSDestinationMBean var1) throws InvalidAttributeValueException, DistributedManagementException;

   /** @deprecated */
   boolean isMessagesPagingEnabled();

   /** @deprecated */
   void setMessagesPagingEnabled(boolean var1) throws InvalidAttributeValueException;

   /** @deprecated */
   boolean isBytesPagingEnabled();

   /** @deprecated */
   void setBytesPagingEnabled(boolean var1) throws InvalidAttributeValueException;

   void useDelegates(DomainMBean var1, JMSBean var2, TemplateBean var3);
}
