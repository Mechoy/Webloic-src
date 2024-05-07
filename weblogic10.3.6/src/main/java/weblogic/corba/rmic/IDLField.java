package weblogic.corba.rmic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import weblogic.iiop.IDLUtils;

public class IDLField implements Cloneable {
   protected String m_mangledName = null;
   protected Class m_type = null;
   protected boolean m_isConst = false;
   protected Object m_value = null;
   boolean m_isPublic = false;
   Field m_field = null;
   Class m_class = null;

   public IDLField(Class var1, Field var2) {
      this.init(var1, var2);
   }

   void init(Class var1, Field var2) {
      this.m_class = var1;
      this.m_field = var2;
      if (null != this.m_field) {
         int var3 = this.m_field.getModifiers();
         if (Modifier.isPublic(var3)) {
            this.m_isPublic = true;
         }

         this.m_isConst = Modifier.isPublic(var3) && Modifier.isStatic(var3) && Modifier.isFinal(var3);
         this.m_mangledName = IDLUtils.mangleAttributeName(this.m_class, var2);
         if (this.m_isConst) {
            try {
               this.m_value = var2.get((Object)null);
            } catch (IllegalAccessException var5) {
            }
         }

         this.m_type = var2.getType();
      }

   }

   public Field getField() {
      return this.m_field;
   }

   public Object getValue() {
      return this.m_value;
   }

   public Class getType() {
      return this.m_type;
   }

   public String getMangledName() {
      return this.m_mangledName;
   }

   public void setMangledName(String var1) {
      this.m_mangledName = var1;
   }

   public boolean isConst() {
      return this.m_isConst;
   }

   public boolean isPublic() {
      return this.m_isPublic;
   }

   public boolean isPrimitive() {
      return this.m_type.isPrimitive();
   }

   public String getName() {
      return this.m_field.getName();
   }

   public String toIDL() {
      StringBuffer var2 = new StringBuffer();

      String var1;
      try {
         var1 = IDLUtils.getIDLType(this.m_type);
      } catch (Exception var7) {
         return "// error generating the type for " + this.m_mangledName;
      }

      if (IDLOptions.getOrbix() && var1.endsWith(this.m_mangledName)) {
         var2.append("// ");
      }

      if (this.isConst()) {
         var2.append("const ");
      } else if (this.isPublic()) {
         var2.append("public ");
      } else {
         var2.append("private ");
      }

      var2.append(var1).append(" ").append(this.m_mangledName);
      if (null != this.m_value && IDLUtils.isValueType(this.m_value.getClass())) {
         var2.append(" = ");
         String var3 = this.m_value.toString();
         if (String.class.equals(this.m_value.getClass())) {
            var3 = '"' + var3 + '"';
         } else if (Character.class.equals(this.m_value.getClass())) {
            Character var4 = (Character)this.m_value;
            char var5 = var4;
            int var6 = Character.getNumericValue(var5) & '\uffff';
            var3 = "'\\u" + Integer.toHexString(var6) + "'";
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
