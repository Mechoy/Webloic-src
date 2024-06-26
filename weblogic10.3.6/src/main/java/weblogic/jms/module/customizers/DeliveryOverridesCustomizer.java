package weblogic.jms.module.customizers;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.customizers.DeliveryParamsOverridesBeanCustomizer;

public class DeliveryOverridesCustomizer extends ParamsCustomizer implements DeliveryParamsOverridesBeanCustomizer {
   public DeliveryOverridesCustomizer(DeliveryParamsOverridesBean var1) {
      super((DescriptorBean)var1);
   }

   public TemplateBean getTemplateBean() {
      return super.getTemplateBean();
   }
}
