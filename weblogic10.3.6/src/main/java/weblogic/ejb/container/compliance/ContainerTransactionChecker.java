package weblogic.ejb.container.compliance;

import java.util.HashSet;
import java.util.Set;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.AssemblyDescriptorBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.MethodBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.logging.Loggable;

public final class ContainerTransactionChecker extends BaseComplianceChecker {
   private DeploymentInfo di;
   private Set ejbNamesWithValidatedCTs = new HashSet();

   public ContainerTransactionChecker(DeploymentInfo var1) {
      this.di = var1;
   }

   public void checkContainerTransaction() {
      EjbDescriptorBean var1 = this.di.getEjbDescriptorBean();
      EjbJarBean var2 = var1.getEjbJarBean();
      AssemblyDescriptorBean var3 = var1.getEjbJarBean().getAssemblyDescriptor();
      if (null != var3) {
         ContainerTransactionBean[] var4 = var3.getContainerTransactions();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            MethodBean[] var6 = var4[var5].getMethods();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               this.validateContainerTransaction(var2, var6[var7].getEjbName());
            }
         }
      }

   }

   private void validateContainerTransaction(EjbJarBean var1, String var2) {
      if (!this.ejbNamesWithValidatedCTs.contains(var2)) {
         EnterpriseBeansBean var3 = var1.getEnterpriseBeans();
         SessionBeanBean[] var4 = var3.getSessions();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getEjbName().equals(var2) && var4[var5].getTransactionType().equals("Bean")) {
               Loggable var6 = EJBLogger.logContainerTransactionSetForBeanManagedEJBLoggable(var2);
               var6.log();
            }
         }

         if (var3 instanceof EnterpriseBeansBean) {
            MessageDrivenBeanBean[] var9 = var3.getMessageDrivens();

            for(int var7 = 0; var7 < var9.length; ++var7) {
               if (var9[var7].getEjbName().equals(var2) && var9[var7].getTransactionType().equals("Bean")) {
                  Loggable var8 = EJBLogger.logContainerTransactionSetForBeanManagedEJBLoggable(var2);
                  var8.log();
               }
            }
         }

         this.ejbNamesWithValidatedCTs.add(var2);
      }
   }
}
