package weblogic.j2ee.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.ArrayMemberBean;

public class ArrayMemberBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      ArrayMemberBean var2 = (ArrayMemberBean)var1;
      return new ArrayMemberBeanCustomizer(var2);
   }
}
