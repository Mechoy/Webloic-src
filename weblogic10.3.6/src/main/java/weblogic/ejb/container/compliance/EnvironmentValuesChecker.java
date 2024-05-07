package weblogic.ejb.container.compliance;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.MessageDestinationRefBean;
import weblogic.utils.ErrorCollectionException;

public final class EnvironmentValuesChecker extends BaseComplianceChecker {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private DeploymentInfo di;

   public EnvironmentValuesChecker(DeploymentInfo var1) {
      this.di = var1;
   }

   public void checkEnvironmentValues() throws ErrorCollectionException {
      ErrorCollectionException var1 = new ErrorCollectionException();
      Collection var2 = this.di.getBeanInfos();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         BeanInfo var4 = (BeanInfo)var3.next();
         this.checkEnvEntries(var4, var1);
         this.checkMessageDestinationRefs(var4, var1);
         this.checkEjbRefs(var4, var1);
         this.checkEjbLocalRefs(var4, var1);
      }

      if (!var1.isEmpty()) {
         throw var1;
      }
   }

   private void checkEjbRefs(BeanInfo var1, ErrorCollectionException var2) {
      if (!var1.isEJB30()) {
         Map var3 = var1.getAllEJBReferenceJNDINames();
         Iterator var4 = var1.getAllEJBReferences().iterator();

         while(var4.hasNext()) {
            EjbRefBean var5 = (EjbRefBean)var4.next();
            if (!var3.containsKey(var5.getEjbRefName())) {
               String var6 = var5.getEjbLink();
               if (var6 == null) {
                  var2.add(new ComplianceException(this.fmt.EJB_REF_MUST_HAVE_EJB_LINK_OR_REF_DESC(var1.getEJBName(), var5.getEjbRefName())));
               } else {
                  BeanInfo var7 = this.di.getBeanInfo(var6);
                  if (var7 != null && !(var7 instanceof ClientDrivenBeanInfo)) {
                     var2.add(new ComplianceException(this.fmt.ILLEGAL_LOCAL_EJB_LINK_TO_MDB(var1.getEJBName(), var5.getEjbRefName())));
                  }
               }
            }

            if (var5.getHome() == null && var5.getRemote() == null) {
               var2.add(new ComplianceException("The ejb-ref " + var5.getEjbRefName() + " does not have a home or remote interface configured."));
            }
         }

      }
   }

   private void checkEjbLocalRefs(BeanInfo var1, ErrorCollectionException var2) {
      if (!var1.isEJB30()) {
         Map var3 = var1.getAllEJBLocalReferenceJNDINames();
         Iterator var4 = var1.getAllEJBLocalReferences().iterator();

         while(var4.hasNext()) {
            EjbLocalRefBean var5 = (EjbLocalRefBean)var4.next();
            if (!var3.containsKey(var5.getEjbRefName())) {
               String var6 = var5.getEjbLink();
               if (var6 == null) {
                  var2.add(new ComplianceException(this.fmt.EJB_REF_MUST_HAVE_EJB_LINK_OR_REF_DESC(var1.getEJBName(), var5.getEjbRefName())));
               } else {
                  BeanInfo var7 = this.di.getBeanInfo(var6);
                  if (var7 != null && !(var7 instanceof ClientDrivenBeanInfo)) {
                     var2.add(new ComplianceException(this.fmt.ILLEGAL_LOCAL_EJB_LINK_TO_MDB(var1.getEJBName(), var5.getEjbRefName())));
                  }
               }
            }

            if (var5.getLocalHome() == null && var5.getLocal() == null) {
               var2.add(new ComplianceException("The ejb-local-ref " + var5.getEjbRefName() + " does not have a local-home or local interface configured."));
            }
         }

      }
   }

   private void checkMessageDestinationRefs(BeanInfo var1, ErrorCollectionException var2) {
      if (!var1.isEJB30()) {
         Collection var3 = var1.getAllMessageDestinationReferences();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            MessageDestinationRefBean var5 = (MessageDestinationRefBean)var4.next();
            if (var5.getMessageDestinationLink() == null) {
               var2.add(new ComplianceException(this.fmt.MESSAGE_DESTINATION_REF_NOT_LINKED(var5.getMessageDestinationRefName(), var1.getEJBName())));
            }
         }

      }
   }

   private void checkEnvEntries(BeanInfo var1, ErrorCollectionException var2) {
      if (!var1.isEJB30()) {
         Collection var3 = var1.getAllEnvironmentEntries();
         Iterator var4 = var3.iterator();

         while(true) {
            EnvEntryBean var5;
            String var6;
            String var7;
            do {
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  var5 = (EnvEntryBean)var4.next();
                  var6 = var5.getEnvEntryType();
                  var7 = var5.getEnvEntryValue();
               } while("java.lang.String".equals(var6));
            } while(var7 != null && var7.trim().length() != 0);

            var2.add(new ComplianceException(this.fmt.ENV_VALUE_CANNOT_BE_NULL(var5.getEnvEntryName())));
         }
      }
   }
}
