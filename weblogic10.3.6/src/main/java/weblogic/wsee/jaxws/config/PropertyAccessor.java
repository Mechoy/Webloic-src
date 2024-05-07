package weblogic.wsee.jaxws.config;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import java.io.Serializable;

public interface PropertyAccessor {
   @NotNull
   Property getProperty();

   void setProperty(@NotNull Property var1);

   @NotNull
   Class<? extends Serializable> getValueClass();

   @NotNull
   PropertySource getSource();

   @Nullable
   Object getValue();
}
