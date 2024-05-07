package weblogic.corba.rmic;

import java.lang.reflect.Field;
import weblogic.iiop.IDLUtils;
import weblogic.iiop.Utils;

public final class IDLAttribute extends IDLField {
   public static final int READONLY = 1;
   public static final int READWRITE = 2;
   public static final int CONST = 4;
   public static final int WRITEONLY = 8;
   int m_modifier;

   public IDLAttribute(String var1, int var2, Object var3, Class var4) {
      super((Class)null, (Field)null);
      this.m_mangledName = var1;
      this.m_modifier = var2;
      this.m_value = var3;
      this.m_type = var4;
   }

   public boolean isReadOnly() {
      return 1 == this.m_modifier;
   }

   public void setModifier(int var1) {
      this.m_modifier = var1;
   }

   public int getModifier() {
      return this.m_modifier;
   }

   public String getName() {
      return this.m_mangledName;
   }

   public String toIDL() {
      String var1;
      try {
         var1 = IDLUtils.getIDLType(this.m_type);
      } catch (Exception var4) {
         return "// error generating the type for " + this.m_mangledName;
      }

      StringBuffer var2 = new StringBuffer();
      if (IDLUtils.isValueType(this.m_type) && IDLOptions.getNoValueTypes() || Utils.isAbstractInterface(this.m_type) && IDLOptions.getNoAbstract() || IDLOptions.getOrbix() && var1.endsWith(this.m_mangledName)) {
         var2.append("// ");
      }

      if (this.isReadOnly()) {
         var2.append("readonly ");
      }

      var2.append("attribute ");
      var2.append(var1).append(" ").append(this.m_mangledName);
      if (null != this.m_value) {
         var2.append(" = ");
         String var3 = this.m_value.toString();
         if (String.class.equals(this.m_value.getClass())) {
            var3 = '"' + var3 + '"';
         }

         var2.append(var3);
      }

      var2.append(";\n");
      return var2.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
