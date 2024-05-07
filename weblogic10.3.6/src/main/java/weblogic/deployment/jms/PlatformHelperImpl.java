package weblogic.deployment.jms;

import java.security.AccessController;
import java.util.Hashtable;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.common.resourcepool.ResourcePool;
import weblogic.jndi.ClientEnvironmentFactory;
import weblogic.jndi.internal.ClientEnvironmentFactoryImpl;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class PlatformHelperImpl extends PlatformHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ClientEnvironmentFactory environmentFactory = new ClientEnvironmentFactoryImpl();

   public JMSSessionPoolRuntime createJMSSessionPoolRuntime(String var1, ResourcePool var2, JMSSessionPool var3) throws ManagementException {
      return new JMSSessionPoolRuntimeImpl(var1, var2, var3);
   }

   String getXAResourceName(String var1) {
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(KERNEL_ID);
      String var3 = var2.getDomainName();
      String var4 = var2.getServerName();
      return var3 + "." + var4 + "." + "JMSXASessionPool." + var1;
   }

   PlatformHelper.ForeignRefReturn checkForeignRef(Map var1) throws JMSException {
      InitialContext var2 = null;
      PlatformHelper.ForeignRefReturn var3 = new PlatformHelper.ForeignRefReturn(this);
      boolean var4 = false;
      var3.subject = null;
      var3.foundCreds = false;
      var3.userNameBuf = new StringBuffer();
      var3.passwdBuf = new StringBuffer();
      var3.connectionHealthChecking = "enabled";
      Object var5 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
      if (var5.equals(KERNEL_ID)) {
         var5 = SubjectManager.getSubjectManager().getAnonymousSubject();
      }

      Hashtable var6 = JMSConnectionHelper.getJNDIEnvironment(var1);
      Hashtable var7 = null;

      try {
         String var8 = JMSConnectionHelper.getJNDIName(var1);
         var2 = new InitialContext(var6);
         AuthenticatedSubject var10 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
         if (var10.equals(KERNEL_ID)) {
            var4 = true;
            SubjectManager.getSubjectManager().pushSubject(KERNEL_ID, (AbstractSubject)var5);
         }

         Object var9;
         try {
            var9 = var2.lookupLink(var8);
         } finally {
            if (var4) {
               SubjectManager.getSubjectManager().popSubject(KERNEL_ID);
            }

            var2.close();
         }

         SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);

         try {
            if (var9 instanceof ForeignOpaqueReference) {
               ForeignOpaqueReference var11 = (ForeignOpaqueReference)var9;
               if (var11.isFactory()) {
                  if (var11.getUsername() != null && var11.getUsername().length() > 0) {
                     var3.foundCreds = true;
                     var3.userNameBuf.append(var11.getUsername());
                     if (JMSPoolDebug.logger.isDebugEnabled()) {
                        JMSPoolDebug.logger.debug("Found credentials for connection factory with username " + var3.userNameBuf.toString());
                     }
                  }

                  if (var11.getPassword() != null && var11.getPassword().length() > 0) {
                     var3.foundCreds = true;
                     var3.passwdBuf.append(var11.getPassword());
                  }

                  var3.connectionHealthChecking = var11.getConnectionHealthChecking();
                  var7 = var11.getJNDIEnvironment();
               }
            }
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         if (var7 != null) {
            Context var22 = this.getForeignInitialContext(var7, var3);
            var22.close();
         }

         if (!var3.foundCreds && JMSPoolDebug.logger.isDebugEnabled()) {
            JMSPoolDebug.logger.debug("No credentials associated with connection factory");
         }

         return var3;
      } catch (NamingException var21) {
         if (JMSPoolDebug.logger.isDebugEnabled()) {
            JMSPoolDebug.logger.debug("Can't get credentials associated with connection factory: " + var21);
         }

         return var3;
      }
   }

   private Context getForeignInitialContext(Hashtable var1, PlatformHelper.ForeignRefReturn var2) throws NamingException {
      if (JMSPoolDebug.logger.isDebugEnabled()) {
         JMSPoolDebug.logger.debug("JMSConnectionHelper.getForeignInitialContext() + jndiEnv ");
      }

      InitialContext var3 = new InitialContext(var1);
      String var4 = (String)var1.get("java.naming.security.principal");
      String var5 = (String)var1.get("java.naming.security.credentials");
      if (var4 != null || var5 != null) {
         synchronized(this) {
            var2.subject = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
            if (JMSPoolDebug.logger.isDebugEnabled()) {
               JMSPoolDebug.logger.debug("JMSConnectionHelper.getForeignInitialContext(), subject " + var2.subject);
            }
         }
      }

      if (JMSPoolDebug.logger.isDebugEnabled()) {
         JMSPoolDebug.logger.debug("JMSConnectionHelper.getForeignInitialContext()  subject ****** url " + (String)var1.get("java.naming.provider.url"));
      }

      return var3;
   }
}
