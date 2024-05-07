package weblogic.security.providers.authentication;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.authentication.AuthenticatorMBean;
import weblogic.management.security.authentication.GroupReaderMBean;
import weblogic.management.security.authentication.MemberGroupListerMBean;
import weblogic.management.security.authentication.UserReaderMBean;

public interface WindowsNTAuthenticatorMBean extends StandardInterface, DescriptorBean, AuthenticatorMBean, UserReaderMBean, GroupReaderMBean, MemberGroupListerMBean {
   String getProviderClassName();

   String getDescription();

   String getVersion();

   String getDomainControllers();

   void setDomainControllers(String var1) throws InvalidAttributeValueException;

   String[] getDomainControllerList();

   void setDomainControllerList(String[] var1) throws InvalidAttributeValueException;

   String getBadDomainControllerRetry();

   void setBadDomainControllerRetry(String var1) throws InvalidAttributeValueException;

   Integer getBadDomainControllerRetryInterval();

   void setBadDomainControllerRetryInterval(Integer var1) throws InvalidAttributeValueException;

   String getMapUPNNames();

   void setMapUPNNames(String var1) throws InvalidAttributeValueException;

   String getLogonType();

   void setLogonType(String var1) throws InvalidAttributeValueException;

   String getMapNTDomainName();

   void setMapNTDomainName(String var1) throws InvalidAttributeValueException;

   String getName();
}
