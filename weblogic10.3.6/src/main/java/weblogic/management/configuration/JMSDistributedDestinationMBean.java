package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

/** @deprecated */
public interface JMSDistributedDestinationMBean extends JMSVirtualDestinationMBean {
   /** @deprecated */
   JMSTemplateMBean createJMSTemplate(String var1);

   /** @deprecated */
   void destroyJMSTemplate(JMSTemplateMBean var1);

   /** @deprecated */
   JMSTemplateMBean getJMSTemplate();

   /** @deprecated */
   void setJMSTemplate(JMSTemplateMBean var1);

   JMSTemplateMBean getTemplate();

   void setTemplate(JMSTemplateMBean var1);

   String getLoadBalancingPolicy();

   void setLoadBalancingPolicy(String var1) throws InvalidAttributeValueException;
}
