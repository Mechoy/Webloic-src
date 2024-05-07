package weblogic.j2ee;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.MailSessionMBean;
import weblogic.management.internal.DeploymentHandler;
import weblogic.management.internal.DeploymentHandlerContext;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public final class RMCFactoryDeployer implements DeploymentHandler {
   private final Map runtimes = new HashMap();
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Context ctx;

   public RMCFactoryDeployer() {
      try {
         Hashtable var1 = new Hashtable();
         var1.put("weblogic.jndi.createIntermediateContexts", "true");
         var1.put("weblogic.jndi.replicateBindings", "false");
         this.ctx = new InitialContext(var1);
      } catch (Exception var2) {
         throw new AssertionError("Cannot intialize Resource Manager Connection Factory resources because could not get JNDI context: " + var2.toString());
      }
   }

   public void prepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
   }

   public void activateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws DeploymentException {
      if (var1 instanceof MailSessionMBean) {
         this.deployMailSession((MailSessionMBean)var1);
      }

   }

   public void deactivateDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) {
      if (var1 instanceof MailSessionMBean) {
         try {
            this.undeployMailSession((MailSessionMBean)var1);
         } catch (DeploymentException var4) {
            J2EELogger.logFailedToUndeployMailSession(var4);
         }
      }

   }

   public void unprepareDeployment(DeploymentMBean var1, DeploymentHandlerContext var2) throws UndeploymentException {
   }

   private void deployMailSession(MailSessionMBean var1) throws DeploymentException {
      Properties var2 = var1.getProperties();
      Session var3 = null;

      try {
         if (var2 == null) {
            var2 = new Properties();
         }

         var3 = Session.getInstance(var2, (Authenticator)null);
      } catch (Exception var8) {
         throw new DeploymentException("Error received when trying to create a mail session", var8);
      }

      String var4 = var1.getJNDIName();

      try {
         if (var3 == null) {
            throw new DeploymentException("Unable to create a mail session to bind to JNDI Name " + var4);
         }

         this.ctx.bind(var4, var3);
      } catch (NamingException var7) {
         throw new DeploymentException("Could not bind a mail session to JNDI name " + var4 + " ", var7);
      }

      J2EELogger.logDeployedMailSession(var4);

      try {
         MailSessionRuntimeMBeanImpl var5 = new MailSessionRuntimeMBeanImpl(var4);
         this.runtimes.put(var4, var5);
         ManagementService.getRuntimeAccess(kernelId).getServerRuntime().addMailSessionRuntime(var5);
      } catch (ManagementException var6) {
         this.undeployMailSession(var1);
         throw new DeploymentException(var6);
      }
   }

   private void undeployMailSession(MailSessionMBean var1) throws DeploymentException {
      String var2 = var1.getJNDIName();

      try {
         this.ctx.unbind(var2);
      } catch (NamingException var13) {
         throw new DeploymentException("Could not unbind a mail session from JNDI name " + var2 + " ", var13);
      } finally {
         MailSessionRuntimeMBeanImpl var6 = (MailSessionRuntimeMBeanImpl)this.runtimes.get(var2);
         if (var6 != null) {
            try {
               ManagementService.getRuntimeAccess(kernelId).getServerRuntime().removeMailSessionRuntime(var6);
               var6.unregister();
            } catch (ManagementException var12) {
               throw new DeploymentException(var12);
            }
         }

      }

      J2EELogger.logUndeployedMailSession(var2);
   }
}
