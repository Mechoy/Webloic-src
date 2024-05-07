package weblogic.ejb.container.compliance;

import java.util.HashSet;
import java.util.Iterator;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.CMPInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.persistence.PersistenceType;
import weblogic.ejb20.dd.DescriptorErrorInfo;
import weblogic.utils.Debug;
import weblogic.utils.ErrorCollectionException;

public final class CmpJarChecker extends BaseComplianceChecker {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private DeploymentInfo di;

   public CmpJarChecker(DeploymentInfo var1) {
      this.di = var1;
      if (debug) {
         Debug.assertion(var1 != null);
      }

   }

   public void checkCmpJar() throws ErrorCollectionException {
      PersistenceType var1 = null;
      boolean var2 = true;
      ErrorCollectionException var3 = new ErrorCollectionException();
      Iterator var4 = this.di.getBeanInfos().iterator();

      while(var4.hasNext()) {
         BeanInfo var5 = (BeanInfo)var4.next();
         if (var5 instanceof EntityBeanInfo) {
            EntityBeanInfo var6 = (EntityBeanInfo)var5;
            if (!var6.getIsBeanManagedPersistence()) {
               CMPInfo var7 = var6.getCMPInfo();
               if (var7.uses20CMP()) {
                  PersistenceType var8 = var7.getPersistenceType();
                  if (var2) {
                     var1 = var8;
                     var2 = false;
                  } else {
                     if (var1 != var8) {
                        var3.add(new ComplianceException(this.fmt.NOT_ALL_BEANS_USE_SAME_PERSISTENCE(var6.getEJBName()), new DescriptorErrorInfo("<persistence-type>", var6.getEJBName(), var8)));
                     }

                     if (!var3.isEmpty()) {
                        throw var3;
                     }
                  }
               }
            }
         }
      }

   }

   public void checkUniqueAbstractSchemaNames() throws ErrorCollectionException {
      HashSet var1 = new HashSet();
      ErrorCollectionException var2 = new ErrorCollectionException();
      Iterator var3 = this.di.getBeanInfos().iterator();

      while(var3.hasNext()) {
         BeanInfo var4 = (BeanInfo)var3.next();
         if (var4 instanceof EntityBeanInfo) {
            EntityBeanInfo var5 = (EntityBeanInfo)var4;
            if (!var5.getIsBeanManagedPersistence()) {
               CMPInfo var6 = var5.getCMPInfo();
               if (var6.uses20CMP() && var6.getAbstractSchemaName() != null && !var6.getAbstractSchemaName().equals("")) {
                  if (var1.contains(var6.getAbstractSchemaName())) {
                     var2.add(new ComplianceException(this.fmt.ABSTRACT_SCHEMA_NAME_NOT_UNIQUE(var5.getEJBName()), new DescriptorErrorInfo("<abstract-schema-name>", var5.getEJBName(), var6.getAbstractSchemaName())));
                  }

                  var1.add(var6.getAbstractSchemaName());
               }
            }
         }
      }

      if (!var2.isEmpty()) {
         throw var2;
      }
   }
}
