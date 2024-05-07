package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.ejb11.EJBRefMBean;
import weblogic.management.descriptors.ejb11.EnterpriseBeanMBean;
import weblogic.management.descriptors.ejb11.EnvEntryMBean;

public interface MessageDrivenMBean extends EnterpriseBeanMBean {
   String getEJBClass();

   void setEJBClass(String var1);

   void setTransactionType(String var1);

   String getTransactionType();

   String getMessageSelector();

   void setMessageSelector(String var1);

   void setAcknowledgeMode(String var1);

   String getAcknowledgeMode();

   MessageDrivenDestinationMBean getMessageDrivenDestination();

   void setMessageDrivenDestination(MessageDrivenDestinationMBean var1);

   EnvEntryMBean[] getEnvEntries();

   void setEnvEntries(EnvEntryMBean[] var1);

   void addEnvEntry(EnvEntryMBean var1);

   void removeEnvEntry(EnvEntryMBean var1);

   EJBRefMBean[] getEJBRefs();

   void setEJBRefs(EJBRefMBean[] var1);

   void addEJBRef(EJBRefMBean var1);

   void removeEJBRef(EJBRefMBean var1);

   EJBLocalRefMBean[] getEJBLocalRefs();

   void setEJBLocalRefs(EJBLocalRefMBean[] var1);

   void addEJBLocalRef(EJBLocalRefMBean var1);

   void removeEJBLocalRef(EJBLocalRefMBean var1);

   SecurityIdentityMBean getSecurityIdentity();

   void setSecurityIdentity(SecurityIdentityMBean var1);

   weblogic.management.descriptors.ejb11.ResourceRefMBean[] getResourceRefs();

   void setResourceRefs(weblogic.management.descriptors.ejb11.ResourceRefMBean[] var1);

   void addResourceRef(weblogic.management.descriptors.ejb11.ResourceRefMBean var1);

   void removeResourceRef(weblogic.management.descriptors.ejb11.ResourceRefMBean var1);

   void setSubscriptionDurability(String var1);

   String getSubscriptionDurability();

   ResourceEnvRefMBean[] getResourceEnvRefs();

   void setResourceEnvRefs(ResourceEnvRefMBean[] var1);

   void addResourceEnvRef(ResourceEnvRefMBean var1);

   void removeResourceEnvRef(ResourceEnvRefMBean var1);
}
