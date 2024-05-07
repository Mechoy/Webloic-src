package weblogic.jms.module.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.MulticastParamsBean;

public class MulticastParamsBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      MulticastParamsBean var2 = (MulticastParamsBean)var1;
      return new MulticastCustomizer(var2);
   }
}
