package weblogic.j2ee.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.AnnotatedClassBean;

public class AnnotatedClassBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      AnnotatedClassBean var2 = (AnnotatedClassBean)var1;
      return new AnnotatedClassBeanCustomizer(var2);
   }
}
