package weblogic.security.providers.authentication;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;

public interface IPlanetAuthenticatorMBean extends StandardInterface, DescriptorBean, LDAPAuthenticatorMBean {
   String getGroupFromNameFilter();

   void setGroupFromNameFilter(String var1) throws InvalidAttributeValueException;

   String getStaticMemberDNAttribute();

   void setStaticMemberDNAttribute(String var1) throws InvalidAttributeValueException;

   String getDynamicGroupObjectClass();

   void setDynamicGroupObjectClass(String var1) throws InvalidAttributeValueException;

   String getDynamicGroupNameAttribute();

   void setDynamicGroupNameAttribute(String var1) throws InvalidAttributeValueException;

   String getDynamicMemberURLAttribute();

   void setDynamicMemberURLAttribute(String var1) throws InvalidAttributeValueException;

   String getGuidAttribute();

   void setGuidAttribute(String var1) throws InvalidAttributeValueException;

   String getName();
}
