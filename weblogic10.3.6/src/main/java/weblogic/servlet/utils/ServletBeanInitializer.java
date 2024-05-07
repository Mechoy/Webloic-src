package weblogic.servlet.utils;

import javax.servlet.ServletRequest;
import weblogic.utils.bean.BeanInitializer;
import weblogic.utils.bean.ConversionException;

public final class ServletBeanInitializer extends BeanInitializer {
   public final void initializeBean(Object var1, ServletRequest var2) throws ConversionException {
      this.initializeBean(var1, new BeanInitializer.DataRetrieverHelper(var2) {
         public Object get(String var1) {
            return ((ServletRequest)this.data).getParameter(var1);
         }
      });
   }
}
