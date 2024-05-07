package weblogic.jms.common;

import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.JMSResource;

public interface JMSDestinationSecurity {
   void checkSendPermission(AuthenticatedSubject var1) throws JMSSecurityException;

   void checkSendPermission() throws JMSSecurityException;

   void checkReceivePermission(AuthenticatedSubject var1) throws JMSSecurityException;

   void checkReceivePermission() throws JMSSecurityException;

   void checkBrowsePermission(AuthenticatedSubject var1) throws JMSSecurityException;

   void checkBrowsePermission() throws JMSSecurityException;

   JMSResource getJMSResourceForSend();

   JMSResource getJMSResourceForReceive();

   JMSResource getJMSResourceForBrowse();
}
