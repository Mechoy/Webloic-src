package weblogic.wsee.jaxws.config;

import java.io.Serializable;

public class Property<T extends Serializable> {
   private String _name;
   private Class _clazz;
   private T _defaultValue;
   private PropertyAccessor[] _sources;

   public Property(String var1, Class<T> var2, T var3, PropertyAccessor... var4) {
      this._name = var1;
      this._clazz = var2;
      this._defaultValue = var3;
      this._sources = var4;
      PropertyAccessor[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         PropertyAccessor var8 = var5[var7];
         var8.setProperty(this);
      }

   }

   public String getName() {
      return this._name;
   }

   public Class<T> getValueClass() {
      return this._clazz;
   }

   public T getValue() {
      return this.getValueInfo().value;
   }

   public Property<T>.ValueInfo getValueInfo() {
      ValueInfo var1 = new ValueInfo();
      Object var2 = null;
      PropertyAccessor[] var3 = this._sources;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PropertyAccessor var6 = var3[var5];
         var2 = var6.getValue();
         if (var2 != null) {
            var1.source = var6.getSource();
            break;
         }
      }

      if (var2 != null) {
         var1.value = (Serializable)this.sanitizeValue(var2);
      } else {
         var1.value = this._defaultValue;
         var1.source = PropertySource.DEFAULT_VALUE;
      }

      return var1;
   }

   private Object sanitizeValue(Object var1) {
      if ((this._clazz == Boolean.class || this._clazz == Boolean.TYPE) && var1.getClass() != this._clazz) {
         var1 = Boolean.valueOf(var1.toString());
      }

      Serializable var2;
      try {
         var2 = (Serializable)var1;
      } catch (ClassCastException var4) {
         var2 = this._defaultValue;
      }

      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this._name);
      var1.append(": ");
      ValueInfo var2 = this.getValueInfo();
      boolean var3 = var2.value instanceof String;
      if (var3) {
         var1.append("'");
      }

      var1.append(var2.value);
      if (var3) {
         var1.append("'");
      }

      var1.append(" (");
      var1.append(var2.source);
      var1.append(")");
      return var1.toString();
   }

   class ValueInfo {
      T value;
      PropertySource source;
   }
}
