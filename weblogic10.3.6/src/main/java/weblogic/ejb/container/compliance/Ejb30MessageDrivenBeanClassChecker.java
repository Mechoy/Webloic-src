package weblogic.ejb.container.compliance;

import java.lang.reflect.Method;
import java.util.Set;
import javax.ejb.MessageDrivenBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.container.deployer.CompositeMBeanDescriptor;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.wl.MessageDrivenDescriptorBean;

public final class Ejb30MessageDrivenBeanClassChecker extends MessageDrivenBeanClassChecker {
   private DeploymentInfo di;
   private EjbJarBean ejbJarBean;
   private BeanInfo bi;

   public Ejb30MessageDrivenBeanClassChecker(BeanInfo var1) {
      super(var1);
      this.bi = var1;
      this.di = var1.getDeploymentInfo();
      EjbDescriptorBean var2 = this.di.getEjbDescriptorBean();
      this.ejbJarBean = var2.getEjbJarBean();
   }

   public void checkMDBInterfaceConstraints() throws ComplianceException {
      String var1 = this.bi.getBeanClassName();
      String var2 = this.bi.getEJBName();
      LifecycleCallbackBean[] var3 = null;
      LifecycleCallbackBean[] var4 = null;
      MessageDrivenBeanBean[] var5 = this.ejbJarBean.getEnterpriseBeans().getMessageDrivens();

      String var9;
      for(int var6 = 0; var6 < var5.length; ++var6) {
         BeanInfo var7 = this.di.getBeanInfo(var5[var6].getEjbName());
         if (var7.isEJB30()) {
            MessageDrivenBeanBean var8 = (MessageDrivenBeanBean)var5[var6];
            var9 = var8.getEjbClass();
            if (var9.equals(var1)) {
               var3 = var8.getPreDestroys();
               var4 = var8.getPostConstructs();
               break;
            }
         }
      }

      Class var12 = this.bi.getBeanClass();

      try {
         Method var13 = var12.getMethod("ejbCreate", (Class[])null);
         if (var4 != null && var4.length > 0) {
            for(int var15 = 0; var15 < var4.length; ++var15) {
               var9 = var4[var15].getLifecycleCallbackMethod();
               String var10 = var4[var15].getLifecycleCallbackClass();
               if (var10.equals(var12.getName()) && !var9.equals("ejbCreate")) {
                  throw new ComplianceException(EJBComplianceTextFormatter.getInstance().MDB_POSTCONSTRUCT_NOT_APPLY_EJBCREATE(var2));
               }
            }
         }
      } catch (NoSuchMethodException var11) {
      }

      if (MessageDrivenBean.class.isAssignableFrom(var12) && var3 != null && var3.length > 0) {
         for(int var14 = 0; var14 < var3.length; ++var14) {
            String var16 = var3[var14].getLifecycleCallbackMethod();
            var9 = var3[var14].getLifecycleCallbackClass();
            if (var9.equals(var12.getName()) && !var16.equals("ejbRemove")) {
               throw new ComplianceException(EJBComplianceTextFormatter.getInstance().MDB_PREDESTROY_NOT_APPLY_EJBREMOVE(var2));
            }
         }
      }

   }

   public static boolean validateForConflictingConfiguration(CompositeMBeanDescriptor var0, Set var1, String var2) throws ComplianceException {
      MessageDrivenDescriptorBean var3 = var0.getWl60Bean().getMessageDrivenDescriptor();
      boolean var4 = var1.contains("CONNECTIONFACTORYRESOURCELINK") || var3.getConnectionFactoryResourceLink() != null || var1.contains("DESTINATIONRESOURCELINK") || var3.getDestinationResourceLink() != null;
      boolean var5 = var1.contains("DESTINATIONJNDINAME") || var0.getDestinationJNDIName() != null || var1.contains("INITIALCONTEXTFACTORY") || isSet("InitialContextFactory", var3) || var1.contains("PROVIDERURL") || var3.getProviderUrl() != null || var1.contains("CONNECTIONFACTORYJNDINAME") || isSet("ConnectionFactoryJNDIName", var3);
      if (var4 && var5) {
         throw new ComplianceException("The ConnectionFactoryResourceLink and DestinationResourceLink configuration attributes cannot be mixed with any of the following settings: DestinationJNDIName, InitialContextFactory, ProviderUrl, ConnectionFactoryJNDIName.  Please ensure they are mutually exclusive for ejb " + var2 + ".");
      } else {
         boolean var6 = var1.contains("RESOURCEADAPTERJNDINAME") || var3.getResourceAdapterJNDIName() != null;
         boolean var7 = var1.contains("JMSCLIENTID") || var3.getJmsClientId() != null || var1.contains("JMSPOLLINGINTERVALSECONDS") || isSet("JmsPollingIntervalSeconds", var3) || var1.contains("MAXMESSAGESINTRANSACTION") || isSet("MaxMessagesInTransaction", var3) || var1.contains("DURABLESUBSCRIPTIONDELETION") || isSet("DurableSubscriptionDeletion", var3) || var1.contains("DISTRIBUTEDDESTINATIONCONNECTION") || isSet("DistributedDestinationConnection", var3) || var1.contains("USE81STYLEPOLLING") || isSet("Use81StylePolling", var3) || var1.contains("MINIMIZEAQSESSIONS") || isSet("GenerateUniqueJmsClientId", var3);
         if (!var6 || !var4 && !var5 && !var7) {
            return true;
         } else {
            throw new ComplianceException("The ResourceAdapterJNDIName configuration attributes cannot be mixed with any of the following settings: JmsClientId, JmsPollingIntervalSeconds, MaxMessagesInTransaction, DurableSubscriptionDeletion, DistributedDestinationConnection,use81StylePolling,ConnectionFactoryResourceLink, DestinationResourceLink, InitialContextFactory, providerUrl, connectionFactoryJNDIName, destinationJNDIName. .  Please ensure they are mutually exclusive for ejb " + var2 + ".");
         }
      }
   }

   private static boolean isSet(String var0, Object var1) {
      return ((DescriptorBean)var1).isSet(var0);
   }
}
