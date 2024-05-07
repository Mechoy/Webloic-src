package weblogic.ejb.container.dd;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;

public final class EnvironmentEntry extends BaseDescriptor {
   private static boolean debug = System.getProperty("weblogic.ejb.deployment.debug") != null;
   public static Set VALID_ENV_ENTRY_TYPES = new HashSet(Arrays.asList((Object[])(new String[]{"java.lang.String", "java.lang.Boolean", "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Short", "java.lang.Long", "java.lang.Byte", "java.lang.Character"})));
   private String name;
   private String type;
   private String value;
   private String description;

   public EnvironmentEntry() {
      super((String)null);
   }

   public void setName(String var1) {
      if (debug) {
         System.err.println("setName(" + var1 + ")");
      }

      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setDescription(String var1) {
      if (debug) {
         System.err.println("setDescription(" + var1 + ")");
      }

      this.description = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setType(String var1) {
      if (debug) {
         System.err.println("setType(" + var1 + ")");
      }

      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public String getValueString() {
      return this.value;
   }

   public Object getValue() {
      return this.getValue(this.getClass().getClassLoader());
   }

   public Object getValue(ClassLoader var1) {
      if (debug) {
         Debug.assertion(var1 != null);
         Debug.assertion(VALID_ENV_ENTRY_TYPES.contains(this.type));
      }

      try {
         Class var2 = var1.loadClass(this.type);
         if ("java.lang.Character".equals(this.type)) {
            if (this.value.length() > 0) {
               return new Character(this.value.charAt(0));
            } else {
               throw new Exception(" env-entry for type java.lang.Character had length = 0 ");
            }
         } else {
            Constructor var3 = var2.getConstructor(String.class);
            return var3.newInstance(this.value);
         }
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public void validateSelf() {
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
}
