package weblogic.wsee.jaxws.config;

import java.io.Serializable;

public class PerServicePropertyAccessor extends BasePropertyAccessor {
   public PerServicePropertyAccessor(Object var1) {
      this((Class)null, var1);
   }

   public PerServicePropertyAccessor(Class<? extends Serializable> var1, Object var2) {
      super(PropertySource.PERSERVICE_CONFIG, var1, var2);
   }
}
