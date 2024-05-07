package weblogic.wsee.util;

import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorManager;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.ActivationConfigPropertyBean;
import weblogic.j2ee.descriptor.ContainerTransactionBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.MethodBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;

public final class EjbDescriptorCreator {
   private static DescriptorManager _descMan = new DescriptorManager();

   public static EjbDescriptorBean createEjbDescriptorBean(String var0, String var1, String var2, String var3, String var4) {
      EjbJarBean var5 = setupMDBEjbJarBean(var0, var1, var2, var4);
      WeblogicEjbJarBean var6 = setupMDBWeblogicEjbJarBean(var0, var3);
      EjbDescriptorBean var7 = new EjbDescriptorBean();
      var7.setEjbJarBean(var5);
      var7.setWeblogicEjbJarBean(var6);
      return var7;
   }

   public static EjbDescriptorBean createEjbDescriptorBean(String var0, String var1, String var2, String var3, String var4, int var5) {
      EjbJarBean var6 = setupMDBEjbJarBean(var0, var1, var2, var4);
      WeblogicEjbJarBean var7 = setupMDBWeblogicEjbJarBean(var0, var3, var5);
      EjbDescriptorBean var8 = new EjbDescriptorBean();
      var8.setEjbJarBean(var6);
      var8.setWeblogicEjbJarBean(var7);
      return var8;
   }

   private static EjbJarBean setupMDBEjbJarBean(String var0, String var1, String var2, String var3) {
      Descriptor var4 = _descMan.createDescriptorRoot(EjbJarBean.class);
      EjbJarBean var5 = (EjbJarBean)var4.getRootBean();
      MessageDrivenBeanBean var6 = var5.createEnterpriseBeans().createMessageDriven();
      var6.setEjbName(var0);
      var6.setEjbClass(var1);
      ActivationConfigBean var7 = var6.createActivationConfig();
      ActivationConfigPropertyBean var8 = var7.createActivationConfigProperty();
      var8.setActivationConfigPropertyName("destinationType");
      var8.setActivationConfigPropertyValue(var2);
      ActivationConfigPropertyBean var9 = var7.createActivationConfigProperty();
      var9.setActivationConfigPropertyName("messageSelector");
      var9.setActivationConfigPropertyValue(var3);
      ContainerTransactionBean var10 = var5.createAssemblyDescriptor().createContainerTransaction();
      MethodBean var11 = var10.createMethod();
      var11.setEjbName(var0);
      var11.setMethodName("*");
      var10.setTransAttribute("Required");
      return var5;
   }

   private static WeblogicEjbJarBean setupMDBWeblogicEjbJarBean(String var0, String var1) {
      Descriptor var2 = _descMan.createDescriptorRoot(WeblogicEjbJarBean.class);
      WeblogicEjbJarBean var3 = (WeblogicEjbJarBean)var2.getRootBean();
      WeblogicEnterpriseBeanBean var4 = var3.createWeblogicEnterpriseBean();
      var4.setEjbName(var0);
      var4.createMessageDrivenDescriptor().setDestinationJNDIName(var1);
      return var3;
   }

   private static WeblogicEjbJarBean setupMDBWeblogicEjbJarBean(String var0, String var1, int var2) {
      WeblogicEjbJarBean var3 = setupMDBWeblogicEjbJarBean(var0, var1);
      WeblogicEnterpriseBeanBean var4 = var3.getWeblogicEnterpriseBeans()[0];
      var4.createTransactionDescriptor().setTransTimeoutSeconds(var2);
      return var3;
   }

   public static void setDispatchPolicy(EjbDescriptorBean var0, String var1) {
      WeblogicEnterpriseBeanBean[] var2 = var0.getWeblogicEjbJarBean().getWeblogicEnterpriseBeans();
      if (var2 != null && var2.length == 1) {
         var2[0].setDispatchPolicy(var1);
      }

   }
}
