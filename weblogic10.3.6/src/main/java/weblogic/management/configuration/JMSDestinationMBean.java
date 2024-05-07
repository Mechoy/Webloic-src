package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JMSDestinationMBean extends JMSDestCommonMBean, JMSConstants {
   JMSTemplateMBean getTemplate();

   void setTemplate(JMSTemplateMBean var1) throws InvalidAttributeValueException;

   String getJNDIName();

   void setJNDIName(String var1) throws InvalidAttributeValueException;

   boolean isJNDINameReplicated();

   void setJNDINameReplicated(boolean var1) throws InvalidAttributeValueException;

   String getStoreEnabled();

   void setStoreEnabled(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getMessagesPagingEnabled();

   /** @deprecated */
   void setMessagesPagingEnabled(String var1) throws InvalidAttributeValueException;

   /** @deprecated */
   String getBytesPagingEnabled();

   /** @deprecated */
   void setBytesPagingEnabled(String var1) throws InvalidAttributeValueException;
}
