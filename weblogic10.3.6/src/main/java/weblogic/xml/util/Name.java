package weblogic.xml.util;

import java.util.Hashtable;

public class Name {
   static Hashtable names = new Hashtable(500);
   static Hashtable snames = new Hashtable(500);
   Atom nameSpace;
   String name;
   int hash;

   Name(String var1, Atom var2, int var3) {
      this.name = var1;
      this.nameSpace = var2;
      this.hash = var3;
   }

   public String getName() {
      return this.name;
   }

   public Atom getNameSpace() {
      return this.nameSpace;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         Name var2 = (Name)var1;
         if (this.nameSpace != null) {
            if (!this.nameSpace.equals(var2.nameSpace)) {
               return false;
            }
         } else if (var2.nameSpace != null) {
            return false;
         }

         return this.name.equals(var2.name);
      } else {
         return false;
      }
   }

   public static Name create(String var0) {
      if (var0 == null) {
         return null;
      } else {
         Object var1 = snames.get(var0);
         if (var1 == null) {
            int var2 = var0.hashCode();
            Name var3 = new Name(var0, (Atom)null, var2);
            snames.put(var0, var3);
            return var3;
         } else {
            return (Name)var1;
         }
      }
   }

   public static Name create(char[] var0, int var1, int var2) {
      Object var3 = snames.get(new String(var0, var1, var2));
      if (var3 == null) {
         String var4 = new String(var0, var1, var2);
         int var5 = var4.hashCode();
         Name var6 = new Name(var4, (Atom)null, var5);
         snames.put(var4, var6);
         return var6;
      } else {
         return (Name)var3;
      }
   }

   public static Name create(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         return var1 == null ? create(var0) : create(var0, Atom.create(var1));
      }
   }

   public static Name create(String var0, Atom var1) {
      if (var0 == null) {
         return null;
      } else if (var1 == null) {
         return create(var0);
      } else {
         int var2 = var0.hashCode() + var1.hashCode();
         Name var3 = new Name(var0, var1, var2);
         Object var4 = names.get(var3);
         if (var4 == null) {
            names.put(var3, var3);
            return var3;
         } else {
            return (Name)var4;
         }
      }
   }

   public String toString() {
      return this.nameSpace != null ? this.nameSpace.toString() + ":" + this.name : this.name;
   }

   public int hashCode() {
      return this.hash;
   }
}
