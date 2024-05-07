package weblogic.ejb.container.dd.xml;

import java.util.HashMap;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.EntityDescriptorBean;
import weblogic.j2ee.descriptor.wl.PersistenceBean;
import weblogic.j2ee.descriptor.wl.StatefulSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.StatelessSessionDescriptorBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.logging.Loggable;
import weblogic.xml.process.SAXValidationException;

public final class WLDD51Helper {
   public Object beanDesc;
   public boolean setBeanDesc = false;
   private HashMap pStorage = new HashMap();

   public void initialize(EjbDescriptorBean var1, WeblogicEnterpriseBeanBean var2) {
      EnterpriseBeansBean var3 = var1.getEjbJarBean().getEnterpriseBeans();
      Object var4 = null;
      SessionBeanBean[] var5 = var3.getSessions();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (var5[var6].getEjbName().equals(var2.getEjbName())) {
            var4 = var5[var6];
            break;
         }
      }

      if (null == var4) {
         EntityBeanBean[] var8 = var3.getEntities();

         for(int var7 = 0; var7 < var8.length; ++var7) {
            if (var8[var7].getEjbName().equals(var2.getEjbName())) {
               var4 = var8[var7];
               break;
            }
         }
      }

      if (var4 instanceof EntityBeanBean) {
         EntityDescriptorBean var9 = var2.createEntityDescriptor();
         var9.createEntityCache();
         var9.getEntityCache().setConcurrencyStrategy("Exclusive");
         var9.createPersistence();
         var9.createEntityClustering();
         var9.createPool();
         this.beanDesc = var9;
      } else if (var4 instanceof SessionBeanBean) {
         if (((SessionBeanBean)var4).getSessionType().equals("Stateless")) {
            StatelessSessionDescriptorBean var10 = var2.createStatelessSessionDescriptor();
            var10.createStatelessClustering();
            var10.createPool();
            this.beanDesc = var10;
         } else {
            StatefulSessionDescriptorBean var11 = var2.createStatefulSessionDescriptor();
            var11.createStatefulSessionCache();
            var11.createStatefulSessionClustering();
            this.beanDesc = var11;
         }
      }

   }

   public boolean isStateless() {
      return this.beanDesc instanceof StatelessSessionDescriptorBean;
   }

   public boolean isStateful() {
      return this.beanDesc instanceof StatefulSessionDescriptorBean;
   }

   public boolean isEntity() {
      return this.beanDesc instanceof EntityDescriptorBean;
   }

   public StatelessSessionDescriptorBean getStatelessDescriptor() {
      this.setBeanDesc = true;
      return (StatelessSessionDescriptorBean)this.beanDesc;
   }

   public StatefulSessionDescriptorBean getStatefulDescriptor() {
      this.setBeanDesc = true;
      return (StatefulSessionDescriptorBean)this.beanDesc;
   }

   public EntityDescriptorBean getEntityDescriptor() {
      this.setBeanDesc = true;
      return (EntityDescriptorBean)this.beanDesc;
   }

   public PersistenceBean getPersistence() {
      return this.getEntityDescriptor().getPersistence() == null ? this.getEntityDescriptor().createPersistence() : this.getEntityDescriptor().getPersistence();
   }

   public void addPersistenceType(String var1, String var2, String var3) {
      this.pStorage.put(var1 + var2, var3);
   }

   public String getPersistenceStorage(String var1, String var2, String var3) throws SAXValidationException {
      if (!this.pStorage.containsKey(var1 + var2)) {
         Loggable var4 = EJBLogger.logpersistentTypeMissingLoggable(var1, var2, var3);
         throw new SAXValidationException(var4.getMessage());
      } else {
         return (String)this.pStorage.get(var1 + var2);
      }
   }
}
