package weblogic.xml.util;

import java.util.Hashtable;

public class Atom {
   String s;
   int hash;
   static Hashtable atoms = new Hashtable(500);

   Atom(String var1, int var2) {
      this.s = var1;
      this.hash = var2;
   }

   Atom() {
   }

   public static Atom create(String var0) {
      if (var0 == null) {
         return null;
      } else {
         Object var1 = atoms.get(var0);
         if (var1 == null) {
            Atom var2 = new Atom(var0, var0.hashCode());
            atoms.put(var0, var2);
            return var2;
         } else {
            return (Atom)var1;
         }
      }
   }

   public int hashCode() {
      return this.hash;
   }

   public String toString() {
      return this.s;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 != null && this.getClass() == var1.getClass() ? this.s.equals(((Atom)var1).s) : false;
      }
   }
}
