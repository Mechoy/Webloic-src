package weblogic.jms.module.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.ThresholdParamsBean;

public class ThresholdParamsBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      ThresholdParamsBean var2 = (ThresholdParamsBean)var1;
      return new ThresholdsCustomizer(var2);
   }
}
