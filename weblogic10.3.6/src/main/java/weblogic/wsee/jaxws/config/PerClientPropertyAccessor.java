package weblogic.wsee.jaxws.config;

import com.sun.istack.Nullable;
import java.io.Serializable;
import java.util.Map;

public class PerClientPropertyAccessor extends MapPropertyAccessor {
   public PerClientPropertyAccessor(String var1, @Nullable Map<String, Object> var2) {
      this((Class)null, var1, var2);
   }

   public PerClientPropertyAccessor(Class<? extends Serializable> var1, String var2, @Nullable Map<String, Object> var3) {
      super(PropertySource.PERCLIENT_CONFIG, var1, var2, var3);
   }
}
