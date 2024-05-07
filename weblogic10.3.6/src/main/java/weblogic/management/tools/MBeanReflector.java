package weblogic.management.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public final class MBeanReflector {
   private final Set attributes = new TreeSet();
   private final Set operations = new TreeSet();
   private final Class subject;

   public MBeanReflector(Class var1) {
      this.subject = var1;
      this.initialize(this.subject);
   }

   public MBeanReflector(String var1) {
      try {
         this.subject = AttributeInfo.Helper.findClass(var1);
      } catch (ClassNotFoundException var3) {
         throw new NoClassDefFoundError(var3.getMessage());
      }

      this.initialize(this.subject);
   }

   public Class getSubject() {
      return this.subject;
   }

   public Operation[] getOperations() {
      Operation[] var1 = new Operation[this.operations.size()];
      return (Operation[])((Operation[])this.operations.toArray(var1));
   }

   public Attribute[] getAttributes() {
      Attribute[] var1 = new Attribute[this.attributes.size()];
      return (Attribute[])((Attribute[])this.attributes.toArray(var1));
   }

   public Method getAttributeGetMethod(Attribute var1) {
      Class[] var2 = new Class[0];
      Method var3 = this.getMethod("get" + var1.getName(), var2);
      return var3 != null ? var3 : this.getMethod("is" + var1.getName(), var2);
   }

   public Method getAttributeSetMethod(Attribute var1) {
      Class[] var2 = new Class[]{var1.getType()};
      return this.getMethod("set" + var1.getName(), var2);
   }

   public Method getAttributeAddMethod(Attribute var1) {
      Class[] var2 = new Class[]{var1.getType().getComponentType()};
      return this.getMethod("add" + var1.getSingularName(), var2);
   }

   public Method getAttributeRemoveMethod(Attribute var1) {
      Class[] var2 = new Class[]{var1.getType().getComponentType()};
      return this.getMethod("remove" + var1.getSingularName(), var2);
   }

   private Method getMethod(String var1, Class[] var2) {
      try {
         return this.subject.getMethod(var1, var2);
      } catch (NoSuchMethodException var4) {
         return null;
      }
   }

   private void initialize(Class var1) {
      Method[] var2 = var1.getMethods();
      ArrayList var3 = new ArrayList();

      Method var5;
      Attribute var6;
      for(int var4 = 0; var4 < var2.length; ++var4) {
         var5 = var2[var4];
         if (var5.getName().startsWith("set")) {
            var3.add(var5);
         } else {
            var6 = MBeanReflector.Attribute.attributeFromMethod(var5);
            if (var6 != null) {
               this.attributes.add(var6);
            } else {
               this.operations.add(new Operation(var5));
            }
         }
      }

      Iterator var8 = var3.iterator();

      while(true) {
         do {
            if (!var8.hasNext()) {
               return;
            }

            var5 = (Method)var8.next();
            var6 = MBeanReflector.Attribute.attributeFromMethod(var5);
         } while(var6 != null && this.attributes.contains(var6));

         Operation var7 = new Operation(var5);
         this.operations.add(var7);
      }
   }

   public static final class Operation implements Comparable {
      private final Method method;

      Operation(Method var1) {
         this.method = var1;
      }

      public Method getMethod() {
         return this.method;
      }

      public int hashCode() {
         return this.method.getName().hashCode();
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof Operation)) {
            return false;
         } else {
            Operation var2 = (Operation)var1;
            if (var2.method == this.method) {
               return true;
            } else if (!var2.method.getName().equals(this.method.getName())) {
               return false;
            } else {
               Class[] var3 = this.method.getParameterTypes();
               Class[] var4 = var2.method.getParameterTypes();
               if (var3.length != var4.length) {
                  return false;
               } else {
                  for(int var5 = 0; var5 < var3.length; ++var5) {
                     if (var3[var5] != var4[var5]) {
                        return false;
                     }
                  }

                  return true;
               }
            }
         }
      }

      public int compareTo(Object var1) {
         Operation var2 = (Operation)var1;
         if (this.equals(var2)) {
            return 0;
         } else {
            int var3 = var2.method.getName().compareTo(this.method.getName());
            return var3 != 0 ? var3 : System.identityHashCode(var2) - System.identityHashCode(this);
         }
      }
   }

   public static final class Attribute implements Comparable {
      private final String name;
      private final Class type;

      Attribute(String var1, Class var2) {
         this.name = var1;
         this.type = var2;
      }

      private static Attribute attributeFromMethod(Method var0) {
         String var1 = var0.getName();
         if (var1.startsWith("get") && var0.getReturnType() != Void.TYPE && var0.getParameterTypes().length == 0) {
            return new Attribute(var1.substring(3), var0.getReturnType());
         } else if (var1.startsWith("is") && var0.getReturnType() == Boolean.TYPE && var0.getParameterTypes().length == 0) {
            return new Attribute(var1.substring(2), Boolean.TYPE);
         } else {
            return var1.startsWith("set") && var0.getReturnType() == Void.TYPE && var0.getParameterTypes().length == 1 ? new Attribute(var1.substring(3), var0.getParameterTypes()[0]) : null;
         }
      }

      public String getName() {
         return this.name;
      }

      public Class getType() {
         return this.type;
      }

      public String getSingularName() {
         if (this.name.endsWith("ies")) {
            return this.name.substring(0, this.name.length() - 3) + 'y';
         } else {
            return this.name.endsWith("s") ? this.name.substring(0, this.name.length() - 1) : this.name;
         }
      }

      public String getFieldName() {
         StringBuffer var1 = new StringBuffer(this.name);
         var1.setCharAt(0, Character.toLowerCase(var1.charAt(0)));

         for(int var2 = 1; var2 < var1.length() && !Character.isLowerCase(var1.charAt(var2)) && (var2 + 1 >= var1.length() || !Character.isLowerCase(var1.charAt(var2 + 1))); ++var2) {
            var1.setCharAt(var2, Character.toLowerCase(var1.charAt(var2)));
         }

         return var1.toString();
      }

      public int hashCode() {
         return this.name.hashCode();
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof Attribute)) {
            return false;
         } else {
            Attribute var2 = (Attribute)var1;
            return var2.type == this.type && var2.name.equals(this.name);
         }
      }

      public int compareTo(Object var1) {
         Attribute var2 = (Attribute)var1;
         if (this.equals(var2)) {
            return 0;
         } else {
            int var3 = this.name.compareTo(var2.name);
            return var3 != 0 ? var3 : System.identityHashCode(var2) - System.identityHashCode(this);
         }
      }
   }
}
