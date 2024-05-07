package weblogic.deployment.descriptors;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.utils.AssertionError;

public class EnvironmentEntry extends BaseDescriptor implements DDValidationErrorCodes {
   public static Set VALID_ENV_ENTRY_TYPES = new HashSet(Arrays.asList((Object[])(new String[]{"java.lang.String", "java.lang.Boolean", "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Short", "java.lang.Long", "java.lang.Byte", "java.lang.Character"})));
   private String name;
   private String type;
   private String value;
   private String description;

   public EnvironmentEntry() {
      super("weblogic.deployment.descriptors.DDValidationBundle");
   }

   public EnvironmentEntry(String var1) {
      this();
      this.name = var1;
   }

   public EnvironmentEntry(String var1, String var2) {
      this(var1);
      this.setType(var2);
   }

   public EnvironmentEntry(String var1, String var2, String var3) {
      this(var1, var2);
      this.setValue(var3);
   }

   public EnvironmentEntry(String var1, String var2, String var3, String var4) {
      this(var1, var2, var3);
      this.setDescription(var4);
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setTypeClass(Class var1) {
      this.setType(var1.getName());
   }

   public Class getTypeClass() {
      return this.getNamedType(this.type);
   }

   public String getValueString() {
      return this.value;
   }

   public void setValueString(String var1) {
      this.setValue(var1);
   }

   public Object getValue() throws IllegalValueException, IllegalTypeException {
      Class[] var2 = new Class[1];
      String[] var3 = new String[1];
      var2[0] = String.class;
      var3[0] = this.value;
      Class var4 = this.getNamedType(this.type);
      if (var4 == null) {
         String var8 = this.localizer.getFormattedMsg("INVALID_ENV_TYPE", this.type);
         throw new IllegalTypeException(var8);
      } else {
         try {
            if (this.value == null) {
               return var4.newInstance();
            } else if (var4.getName().equals("java.lang.Character")) {
               if (this.value.trim().length() > 1) {
                  throw new IllegalValueException("");
               } else {
                  char var5 = this.value.charAt(0);
                  return new Character(var5);
               }
            } else {
               Constructor var1 = var4.getConstructor(var2);
               return var1.newInstance(var3);
            }
         } catch (Exception var7) {
            String var6 = this.localizer.getFormattedMsg("ENV_VALUE_NOT_OF_TYPE", this.value, this.type);
            throw new IllegalValueException(var6);
         }
      }
   }

   private Class getNamedType(String var1) {
      String var2 = var1;
      if (!VALID_ENV_ENTRY_TYPES.contains(var1)) {
         var2 = "java.lang." + var1;
         if (!VALID_ENV_ENTRY_TYPES.contains(var2)) {
            return null;
         }
      }

      try {
         return Class.forName(var2);
      } catch (ClassNotFoundException var4) {
         throw new AssertionError(var4);
      }
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public void setValue(Class var1, String var2) {
      this.setValue(var1.getName(), var2);
   }

   public void setValue(String var1, String var2) {
      this.setType(var1);
      this.setValue(var2);
   }

   public void validateSelf() {
      if (this.name == null || this.name.length() == 0) {
         this.addError("NO_ENV_ENTRY_NAME_SET");
      }

      if (this.value == null || this.value.length() == 0) {
         this.addError("NO_ENV_ENTRY_VALUE_SET");
      }

      if (this.getNamedType(this.type) == null) {
         this.addError("INVALID_ENV_TYPE", this.type);
      } else if (this.value != null && this.value.length() != 0) {
         try {
            this.getValue();
         } catch (IllegalValueException var2) {
            this.addError("ENV_VALUE_NOT_OF_TYPE", this.value, this.type);
         } catch (IllegalTypeException var3) {
            this.addError("INVALID_ENV_TYPE", this.type);
         }
      }

   }

   public Iterator getSubObjectsIterator() {
      return Collections.EMPTY_SET.iterator();
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof EnvironmentEntry && this.name.equals(((EnvironmentEntry)var1).name);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("name [" + this.name + "] ");
      var1.append("type [" + this.type + "] ");
      var1.append("value [" + this.value + "] ");
      var1.append("description [" + this.description + "] ");
      return var1.toString();
   }
}
