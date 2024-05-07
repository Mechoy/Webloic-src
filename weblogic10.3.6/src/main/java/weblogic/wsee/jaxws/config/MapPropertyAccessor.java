package weblogic.wsee.jaxws.config;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import java.io.Serializable;
import java.util.Map;

public class MapPropertyAccessor implements PropertyAccessor {
   protected PropertySource _propSource;
   protected Property _property;
   protected Class<? extends Serializable> _clazz;
   protected String _key;
   protected Map<String, Object> _map;

   public MapPropertyAccessor(@Nullable Class<? extends Serializable> var1, @NotNull String var2, @Nullable Map<String, Object> var3) {
      this(PropertySource.STUB, var1, var2, var3);
   }

   public MapPropertyAccessor(@NotNull String var1, @Nullable Map<String, Object> var2) {
      this(PropertySource.STUB, (Class)null, var1, var2);
   }

   protected MapPropertyAccessor(@NotNull PropertySource var1, @Nullable Class<? extends Serializable> var2, @NotNull String var3, @Nullable Map<String, Object> var4) {
      this._propSource = var1;
      this._clazz = var2;
      this._key = var3;
      this._map = var4;
   }

   public Property getProperty() {
      return this._property;
   }

   public void setProperty(Property var1) {
      this._property = var1;
      if (this._clazz == null) {
         this._clazz = this._property.getValueClass();
      }

   }

   public Class<? extends Serializable> getValueClass() {
      return this._clazz;
   }

   public PropertySource getSource() {
      return this._propSource;
   }

   public Object getValue() {
      if (this._map == null) {
         return null;
      } else if (this._map.containsKey(this._key)) {
         return this._map.get(this._key);
      } else {
         String var1 = this._key + "Str";
         if (!this._map.containsKey(var1)) {
            return null;
         } else {
            String var2 = (String)this._map.get(var1);
            if (this._clazz != Boolean.class && this._clazz != Boolean.TYPE) {
               if (this._clazz != Integer.class && this._clazz != Integer.TYPE) {
                  if (this._clazz != Short.class && this._clazz != Short.TYPE) {
                     if (this._clazz != Long.class && this._clazz != Long.TYPE) {
                        if (this._clazz != Float.class && this._clazz != Float.TYPE) {
                           return this._clazz != Double.class && this._clazz != Double.TYPE ? var2 : Double.parseDouble(var2);
                        } else {
                           return Float.parseFloat(var2);
                        }
                     } else {
                        return Long.parseLong(var2);
                     }
                  } else {
                     return Short.parseShort(var2);
                  }
               } else {
                  return Integer.parseInt(var2);
               }
            } else {
               return Boolean.valueOf(var2);
            }
         }
      }
   }
}
