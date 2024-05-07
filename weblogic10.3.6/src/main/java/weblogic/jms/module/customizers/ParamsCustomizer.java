package weblogic.jms.module.customizers;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;

public class ParamsCustomizer {
   private DescriptorBean customized;

   public ParamsCustomizer(DescriptorBean var1) {
      this.customized = var1;
   }

   public TemplateBean getTemplateBean() {
      if (this.customized == null) {
         return null;
      } else {
         DescriptorBean var1 = this.customized.getParentBean();
         if (!(var1 instanceof DestinationBean)) {
            return null;
         } else {
            DestinationBean var2 = (DestinationBean)var1;
            return var2.getTemplate();
         }
      }
   }
}
