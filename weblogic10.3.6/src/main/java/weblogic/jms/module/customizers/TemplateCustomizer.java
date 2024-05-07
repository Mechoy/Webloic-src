package weblogic.jms.module.customizers;

import weblogic.j2ee.descriptor.wl.DeliveryFailureParamsBean;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.GroupParamsBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.customizers.TemplateBeanCustomizer;

public class TemplateCustomizer implements TemplateBeanCustomizer {
   private TemplateBean customized;

   public TemplateCustomizer(TemplateBean var1) {
      this.customized = var1;
   }

   public DestinationBean findErrorDestination(String var1) {
      if (this.customized != null && var1 != null) {
         DeliveryFailureParamsBean var2 = this.customized.getDeliveryFailureParams();
         GroupParamsBean var3 = this.customized.lookupGroupParams(var1);
         if (var3 == null) {
            return var2.getErrorDestination();
         } else {
            DestinationBean var4 = var3.getErrorDestination();
            if (var4 != null) {
               return var4;
            } else {
               return var3.isSet("ErrorDestination") ? null : var2.getErrorDestination();
            }
         }
      } else {
         return null;
      }
   }
}
