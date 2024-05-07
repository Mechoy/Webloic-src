package weblogic.j2ee.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.MemberBean;

public class MemberBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      MemberBean var2 = (MemberBean)var1;
      return new MemberBeanCustomizer(var2);
   }
}
