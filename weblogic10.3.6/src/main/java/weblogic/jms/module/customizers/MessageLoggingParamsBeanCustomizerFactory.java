package weblogic.jms.module.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.MessageLoggingParamsBean;

public class MessageLoggingParamsBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      MessageLoggingParamsBean var2 = (MessageLoggingParamsBean)var1;
      return new MessageLoggingCustomizer(var2);
   }
}
