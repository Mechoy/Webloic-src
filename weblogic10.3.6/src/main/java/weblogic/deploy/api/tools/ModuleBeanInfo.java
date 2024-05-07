package weblogic.deploy.api.tools;

import weblogic.deploy.api.shared.WebLogicModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.jms.module.DefaultingHelper;

public class ModuleBeanInfo extends ModuleInfo {
   private WeblogicModuleBean module = null;
   DescriptorBean beanTree;

   protected ModuleBeanInfo(WeblogicModuleBean var1, WebLogicModuleType var2, DescriptorBean var3) {
      this.name = var1.getName();
      this.type = var2;
      this.module = var1;
      this.beanTree = var3;
   }

   public String[] getSubDeployments() {
      if (this.subDeployments != null) {
         return this.subDeployments;
      } else {
         if (WebLogicModuleType.JMS.equals(this.type)) {
            JMSBean var1 = this.getJMSBeanRoot();
            this.subDeployments = DefaultingHelper.getSubDeploymentNames(var1);
         }

         return this.subDeployments;
      }
   }

   private JMSBean getJMSBeanRoot() {
      return this.beanTree != null ? (JMSBean)this.beanTree : null;
   }
}
