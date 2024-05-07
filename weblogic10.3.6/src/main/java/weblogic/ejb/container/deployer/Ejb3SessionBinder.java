package weblogic.ejb.container.deployer;

import java.rmi.Remote;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionHome;
import weblogic.ejb.container.internal.AggregatableOpaqueReference;
import weblogic.ejb.container.internal.StatelessEJBHomeImpl;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.logging.Loggable;

public class Ejb3SessionBinder extends Ejb2JndiBinder implements EjbJndiBinder {
   Ejb3SessionBinder(Ejb3SessionBeanInfo var1) {
      super(var1);
   }

   private void bindRemoteProxy() throws NamingException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      if (!var1.isStateful() && !var1.getBusinessRemotes().isEmpty()) {
         Map var2 = var1.getRemoteBusinessJNDINames();
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            Class var4 = (Class)var3.next();
            if (!Remote.class.isAssignableFrom(var4)) {
               String var5 = (String)var2.get(var4);
               Object var6 = ((Ejb3SessionHome)var1.getRemoteHome()).getBindableImpl(var4);
               AggregatableOpaqueReference var8 = new AggregatableOpaqueReference(var1.getIsIdenticalKey(), var4.getName(), var6);
               boolean var7 = var1.getClusteringDescriptor().getStatelessBeanIsClusterable();
               this.jndiService.bind((String)var5, var8, var7);
            }
         }

      }
   }

   private void bindClusterableObject() throws NamingException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      if (!var1.isStateful() && !var1.getBusinessRemotes().isEmpty()) {
         Set var2 = var1.getBusinessRemotes();
         Map var3 = var1.getRemoteBusinessJNDINames();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Class var5 = (Class)var4.next();
            String var6 = (String)var3.get(var5);
            if (var6 != null) {
               boolean var7 = var1.getClusteringDescriptor().getStatelessBeanIsClusterable();
               if (Remote.class.isAssignableFrom(var5)) {
                  Object var8 = ((StatelessEJBHomeImpl)var1.getRemoteHome()).getBindableImpl(var5);
                  this.jndiService.bind(var6, var8, var7);
               } else {
                  String var10 = var1.getIsIdenticalKey().replace('.', '_') + "_" + var5.getSimpleName();
                  Object var9 = ((StatelessEJBHomeImpl)var1.getRemoteHome()).getBindableEO(var10);
                  this.jndiService.bind(var10, var9, var7);
               }
            }
         }

      }
   }

   private void bindOpaqueRefImpl() throws NamingException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      if (var1.isStateful() && !var1.getBusinessRemotes().isEmpty()) {
         Set var2 = var1.getBusinessRemotes();
         Map var3 = var1.getRemoteBusinessJNDINames();
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            Class var5 = (Class)var4.next();
            String var6 = (String)var3.get(var5);
            if (var6 != null) {
               Object var7 = ((Ejb3SessionHome)var1.getRemoteHome()).getBindableImpl(var5);
               if (!Remote.class.isAssignableFrom(var5)) {
                  var7 = new AggregatableOpaqueReference(var1.getIsIdenticalKey(), var5.getName(), var7);
               }

               this.jndiService.replicatedBind(var6, var7);
            }
         }

      }
   }

   public void bindToJNDI() throws WLDeploymentException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      if (var1.hasDeclaredRemoteHome() || var1.hasDeclaredLocalHome()) {
         super.bindToJNDI();
      }

      if (var1.hasBusinessRemotes()) {
         try {
            this.bindClusterableObject();
            String var2 = var1.getIsIdenticalKey().replace('.', '_') + "_Home";
            if (!var1.hasDeclaredRemoteHome() || var2.equals(var1.getJNDIName().toString())) {
               this.jndiService.replicatedBind((String)var2, var1.getRemoteHome());
            }

            this.bindOpaqueRefImpl();
            this.bindRemoteProxy();
         } catch (NamingException var4) {
            Loggable var3 = EJBLogger.logBusinessJNDIRebindFailedLoggable(var1.getJNDINameAsString(), var4.toString());
            this.unbindFromJNDI();
            throw new WLDeploymentException(var3.getMessage(), var4);
         }
      }
   }

   private void bindLocalBusinessIntf() throws NamingException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      if (var1.getBusinessLocals().size() > 0 && var1.getLocalJNDIName() != null) {
         String var2 = var1.getComponentURI() + "#" + var1.getEJBName();
         LinkRef var3 = new LinkRef("java:app/ejb/" + var2 + "/local-home");
         this.jndiService.nonReplicatedBind((Name)var1.getLocalJNDIName(), var3);
      }

   }
}
