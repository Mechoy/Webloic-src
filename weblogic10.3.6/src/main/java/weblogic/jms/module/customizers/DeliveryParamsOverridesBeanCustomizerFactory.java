package weblogic.jms.module.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.DeliveryParamsOverridesBean;

public class DeliveryParamsOverridesBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      DeliveryParamsOverridesBean var2 = (DeliveryParamsOverridesBean)var1;
      return new DeliveryOverridesCustomizer(var2);
   }
}
