package weblogic.wsee.jaxws.config;

import com.sun.istack.Nullable;
import java.io.Serializable;
import weblogic.wsee.config.WebServiceMBeanFactory;

public class VmWidePropertyAccessor extends BasePropertyAccessor {
   public VmWidePropertyAccessor() {
      this((Class)null, WebServiceMBeanFactory.getInstance());
   }

   public VmWidePropertyAccessor(@Nullable Object var1) {
      this((Class)null, var1);
   }

   public VmWidePropertyAccessor(@Nullable Class<? extends Serializable> var1, @Nullable Object var2) {
      super(PropertySource.VMWIDE_MBEAN, var1, var2);
   }
}
