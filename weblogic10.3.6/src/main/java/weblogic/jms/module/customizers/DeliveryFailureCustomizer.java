package weblogic.jms.module.customizers;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.customizers.DeliveryFailureParamsBeanCustomizer;

public class DeliveryFailureCustomizer extends ParamsCustomizer implements DeliveryFailureParamsBeanCustomizer {
   private DestinationBean myParent;

   public DeliveryFailureCustomizer(DeliveryFailureParamsBean var1) {
      super((DescriptorBean)var1);
      DescriptorBean var2 = (DescriptorBean)var1;
      DescriptorBean var3 = var2.getParentBean();
      if (var3 instanceof DestinationBean) {
         this.myParent = (DestinationBean)var3;
      }

   }

   public TemplateBean getTemplateBean() {
      return super.getTemplateBean();
   }

   public String findSubDeploymentName() {
      return this.myParent == null ? null : this.myParent.getSubDeploymentName();
   }
}
