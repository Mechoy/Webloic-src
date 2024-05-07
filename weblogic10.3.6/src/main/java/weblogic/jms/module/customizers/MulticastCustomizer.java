package weblogic.jms.module.customizers;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.MulticastParamsBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.customizers.MulticastParamsBeanCustomizer;

public class MulticastCustomizer extends ParamsCustomizer implements MulticastParamsBeanCustomizer {
   public MulticastCustomizer(MulticastParamsBean var1) {
      super((DescriptorBean)var1);
   }

   public TemplateBean getTemplateBean() {
      return super.getTemplateBean();
   }
}
