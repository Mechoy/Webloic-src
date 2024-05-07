package weblogic.wsee.jaxws.config;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import java.io.Serializable;
import java.lang.reflect.Method;

public abstract class BasePropertyAccessor implements PropertyAccessor {
   protected Property _property;
   protected Class<? extends Serializable> _clazz;
   protected PropertySource _source;
   protected Object _sourceObject;

   protected BasePropertyAccessor(@NotNull PropertySource var1, @Nullable Object var2) {
      this(var1, (Class)null, var2);
   }

   protected BasePropertyAccessor(@NotNull PropertySource var1, @Nullable Class<? extends Serializable> var2, @Nullable Object var3) {
      this._source = var1;
      this._clazz = var2;
      this._sourceObject = var3;
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
      return this._source;
   }

   protected String getMethodPrefix(boolean var1) {
      if (var1) {
         return "get";
      } else {
         return this._clazz != Boolean.class && this._clazz != Boolean.TYPE ? "get" : "is";
      }
   }

   public Object getValue() {
      if (this._sourceObject == null) {
         return null;
      } else {
         String var1 = this.getMethodPrefix(false) + this._property.getName();

         try {
            Method var2 = this._sourceObject.getClass().getMethod(var1);
            return var2.invoke(this._sourceObject);
         } catch (NoSuchMethodException var5) {
            var1 = this.getMethodPrefix(true) + this._property.getName();

            try {
               Method var3 = this._sourceObject.getClass().getMethod(var1);
               return var3.invoke(this._sourceObject);
            } catch (Exception var4) {
               throw new RuntimeException(var5.toString(), var5);
            }
         } catch (Exception var6) {
            throw new RuntimeException(var6.toString(), var6);
         }
      }
   }
}
