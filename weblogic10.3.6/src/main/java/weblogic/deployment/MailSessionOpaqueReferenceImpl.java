package weblogic.deployment;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.jndi.OpaqueReference;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MailSessionMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public final class MailSessionOpaqueReferenceImpl implements OpaqueReference {
   private final String jndiName;
   private final String applicationName;
   private Properties props = null;
   private static Map jndiNameToMailSession = new HashMap();

   public MailSessionOpaqueReferenceImpl(String var1, String var2) {
      this.jndiName = var1;
      this.applicationName = var2;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      String var3 = BaseEnvironmentBuilder.transformJNDIName(this.jndiName, this.applicationName);
      MailSessionMBean var4 = (MailSessionMBean)jndiNameToMailSession.get(var3);
      if (var4 == null) {
         String var5 = var3.replace('/', '.');
         var4 = (MailSessionMBean)jndiNameToMailSession.get(var5);
         if (var4 == null) {
            var5 = var3.replace('.', '/');
         }

         var4 = (MailSessionMBean)jndiNameToMailSession.get(var5);
         if (var4 == null) {
            throw new AssertionError("Error received when trying to get a mail session with jndiName: " + var3);
         }
      }

      this.props = var4.getProperties();
      if (this.props == null) {
         this.props = new Properties();
      }

      try {
         return Session.getInstance(this.props, (Authenticator)null);
      } catch (Exception var6) {
         throw new AssertionError("Error received when trying to create a mail session", var6);
      }
   }

   static {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      DomainMBean var1 = ManagementService.getRuntimeAccess(var0).getDomain();
      MailSessionMBean[] var2 = var1.getMailSessions();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         jndiNameToMailSession.put(var2[var3].getJNDIName(), var2[var3]);
      }

   }
}
