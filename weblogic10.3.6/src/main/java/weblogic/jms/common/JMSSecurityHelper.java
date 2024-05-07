package weblogic.jms.common;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import weblogic.management.ManagementException;
import weblogic.security.SimpleCallbackHandler;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.JMSResource;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class JMSSecurityHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final AbstractSubject anonymous = SubjectManager.getSubjectManager().getAnonymousSubject();
   private PrincipalAuthenticator pa;
   private AuthorizationManager am;
   private static JMSSecurityHelper securityHelper;
   private static Hashtable destinationMap;

   public JMSSecurityHelper() throws ManagementException {
      if (JMSDebug.JMSConfig.isDebugEnabled()) {
         JMSDebug.JMSConfig.debug("Initializing JMS Security Helper");
      }

      String var1 = "weblogicDEFAULT";
      this.pa = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(KERNEL_ID, var1, ServiceType.AUTHENTICATION);
      this.am = (AuthorizationManager)SecurityServiceManager.getSecurityService(KERNEL_ID, var1, ServiceType.AUTHORIZE);
      if (this.pa == null || this.am == null) {
         throw new RuntimeException("Security Services Unavailable");
      }
   }

   public static synchronized JMSSecurityHelper getSecurityHelper() throws ManagementException {
      if (securityHelper == null) {
         securityHelper = new JMSSecurityHelper();
      }

      return securityHelper;
   }

   public static JMSSecurityHelper getJMSSecurityHelper() {
      return securityHelper;
   }

   public void mapDestinationName(String var1, String var2) {
      if (destinationMap == null) {
         destinationMap = new Hashtable();
      }

      destinationMap.put(var1, var2);
   }

   public void unmapDestinationName(String var1) {
      if (destinationMap != null) {
         destinationMap.remove(var1);
      }
   }

   public PrincipalAuthenticator getPrincipalAuthenticator() {
      return this.pa;
   }

   public AuthorizationManager getAuthorizationManager() {
      return this.am;
   }

   public static boolean authenticate(String var0, String var1) {
      try {
         return getJMSSecurityHelper().getPrincipalAuthenticator().authenticate(new SimpleCallbackHandler(var0, var1)) != null;
      } catch (LoginException var3) {
         return false;
      }
   }

   public static void checkPermission(JMSResource var0) throws JMSSecurityException {
      AuthenticatedSubject var1 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      checkPermission(var0, var1);
   }

   public static AuthenticatedSubject getCurrentSubject() {
      AuthenticatedSubject var0 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      return var0;
   }

   public static void checkPermission(JMSResource var0, AuthenticatedSubject var1) throws JMSSecurityException {
      if (var0 != null) {
         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Creating JMS resource for " + var0.getActionName() + " with " + "  applicationName = " + var0.getApplicationName() + ", moduleName = " + var0.getModule() + " and resource name = " + var0.getResourceName() + " and type = " + var0.getDestinationType());
         }

         if (!getJMSSecurityHelper().getAuthorizationManager().isAccessAllowed(var1, var0, (ContextHandler)null)) {
            throw new JMSSecurityException("Access denied to resource: " + var0);
         }
      }
   }

   public static String getSimpleAuthenticatedName() {
      return SubjectUtils.getUsername(getCurrentSubject().getSubject());
   }

   public static AuthenticatedSubject authenticatedSubject(String var0, String var1) throws LoginException {
      return getJMSSecurityHelper().getPrincipalAuthenticator().authenticate(new SimpleCallbackHandler(var0, var1));
   }

   public static final Object doAsJNDIOperation(AbstractSubject var0, PrivilegedExceptionAction var1) throws NamingException, JMSException {
      try {
         return var0.doAs(KERNEL_ID, var1);
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof NamingException) {
            throw (NamingException)var3;
         } else {
            throw new JMSException(var3);
         }
      }
   }

   public static final Object doAs(AbstractSubject var0, PrivilegedExceptionAction var1) throws javax.jms.JMSException {
      try {
         return var0.doAs(KERNEL_ID, var1);
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof javax.jms.JMSException) {
            throw (javax.jms.JMSException)var3;
         } else {
            throw new JMSException(var3);
         }
      }
   }

   public static final boolean isServerIdentity(AuthenticatedSubject var0) {
      return SecurityServiceManager.isKernelIdentity(var0) || SecurityServiceManager.isServerIdentity(var0);
   }

   public static final AbstractSubject getAnonymousSubject() {
      return anonymous;
   }

   public static void pushSubject(AuthenticatedSubject var0) {
      SubjectManager.getSubjectManager().pushSubject(KERNEL_ID, var0);
   }
}
