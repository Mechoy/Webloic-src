package weblogic.ejb.container.deployer;

import javax.ejb.EJBObject;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;

public class Ejb2JndiBinder implements EjbJndiBinder {
   protected ClientDrivenBeanInfo beanInfo;
   protected EjbJndiService jndiService;

   Ejb2JndiBinder(ClientDrivenBeanInfo var1) {
      this.beanInfo = var1;
      this.jndiService = new EjbJndiService(var1.getClientsOnSameServer());
   }

   private void bindHome() throws WLDeploymentException {
      ClientDrivenBeanInfo var1 = this.beanInfo;
      if (var1.hasRemoteClientView() && var1.hasDeclaredRemoteHome() && var1.getJNDIName() != null && !var1.getJNDIName().toString().equals(var1.getIsIdenticalKey().replace('.', '_') + "_Home")) {
         try {
            if (var1.getClusteringDescriptor().getHomeIsClusterable()) {
               this.jndiService.replicatedBind((Name)var1.getJNDIName(), var1.getRemoteHome());
            } else {
               this.jndiService.nonReplicatedBind((Name)var1.getJNDIName(), var1.getRemoteHome());
            }
         } catch (NamingException var6) {
            Loggable var3 = EJBLogger.logHomeJNDIRebindFailedLoggable(var1.getJNDIName().toString(), var6);
            throw new WLDeploymentException(var3.getMessage(), var6);
         }
      }

      if (var1.hasLocalClientView() && var1.hasDeclaredLocalHome() && var1.getLocalJNDIName() != null) {
         String var2 = var1.getComponentURI() + "#" + var1.getEJBName();
         LinkRef var8 = new LinkRef("java:app/ejb/" + var2 + "/local-home");

         try {
            this.jndiService.nonReplicatedBind((Name)var1.getLocalJNDIName(), var8);
         } catch (NamingException var7) {
            Loggable var5 = EJBLogger.logLocalHomeJNDIRebindFailedLoggable(var1.getLocalJNDIName() != null ? var1.getLocalJNDIName().toString() : "<unknown>", var7);
            throw new WLDeploymentException(var5.getMessage(), var7);
         }
      }

   }

   private void bindEJBObject() throws WLDeploymentException {
      if (!(this.beanInfo instanceof EntityBeanInfo)) {
         ClientDrivenBeanInfo var1 = this.beanInfo;
         if (!(var1.getBeanManager() instanceof StatefulSessionManager)) {
            if (var1.hasRemoteClientView()) {
               String var2 = var1.getIsIdenticalKey().replace('.', '_') + "_EO";
               EJBObject var3 = var1.getRemoteHome().allocateEO();

               try {
                  if (var1.getClusteringDescriptor().getStatelessBeanIsClusterable()) {
                     this.jndiService.replicatedBind((String)var2, var3);
                  } else {
                     this.jndiService.nonReplicatedBind((String)var2, var3);
                  }
               } catch (NamingException var6) {
                  Loggable var5 = EJBLogger.logStatelessEOJNDIBindErrorLoggable(var2, var6);
                  throw new WLDeploymentException(var5.getMessage(), var6);
               }
            }

         }
      }
   }

   public void bindToJNDI() throws WLDeploymentException {
      if (this.beanInfo.hasRemoteClientView() && this.beanInfo.getJNDIName() != null || this.beanInfo.hasLocalClientView() && this.beanInfo.getLocalJNDIName() != null) {
         try {
            this.bindEJBObject();
            this.bindHome();
         } catch (WLDeploymentException var2) {
            this.unbindFromJNDI();
            throw var2;
         }
      }

   }

   public void unbindFromJNDI() {
      this.jndiService.unbindAll();
   }
}
