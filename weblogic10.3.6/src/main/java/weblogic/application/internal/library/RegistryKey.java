package weblogic.application.internal.library;

import java.io.Serializable;
import java.util.ArrayList;
import weblogic.application.internal.library.util.DeweyDecimal;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryReference;

class RegistryKey implements Serializable, Comparable {
   private final DeweyDecimal comparableKey;
   private final String keyStringRepr;
   private final boolean isComparable;

   static RegistryKey[] newInstance(LibraryReference var0) {
      return newInstance(var0.getLibData(), true);
   }

   static RegistryKey[] newInstance(LibraryDefinition var0) {
      return newInstance(var0.getLibData(), false);
   }

   static RegistryKey[] newInstance(BasicLibraryData var0, boolean var1) {
      ArrayList var2 = new ArrayList(3);
      var2.add(new RegistryKey(var0.getName()));
      if (var0.getSpecificationVersion() != null) {
         var2.add(new RegistryKey(var0.getSpecificationVersion()));
      } else if (var1) {
         var2.add((Object)null);
      }

      if (var0.getImplementationVersion() != null) {
         var2.add(new RegistryKey(var0.getImplementationVersion()));
      } else if (var1) {
         var2.add((Object)null);
      }

      return (RegistryKey[])((RegistryKey[])var2.toArray(new RegistryKey[var2.size()]));
   }

   private RegistryKey(DeweyDecimal var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot make RegistryKey with null DeweyDecimal");
      } else {
         this.comparableKey = var1;
         this.isComparable = true;
         this.keyStringRepr = var1.toString();
      }
   }

   private RegistryKey(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot make RegistryKey with null String");
      } else {
         DeweyDecimal var2 = initDeweyDecimal(var1);
         if (var2 == null) {
            this.comparableKey = null;
            this.isComparable = false;
            this.keyStringRepr = var1;
         } else {
            this.comparableKey = var2;
            this.isComparable = true;
            this.keyStringRepr = var2.toString();
         }

      }
   }

   public int compareTo(Object var1) {
      return this.compareTo((RegistryKey)var1);
   }

   public int compareTo(RegistryKey var1) {
      return this.isComparable() && var1.isComparable() ? this.getComparableKey().compareTo(var1.getComparableKey()) : this.toString().compareTo(var1.toString());
   }

   public boolean isComparable() {
      return this.isComparable;
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RegistryKey)) {
         return false;
      } else {
         RegistryKey var2 = (RegistryKey)var1;
         return this.keyStringRepr.equals(var2.toString());
      }
   }

   public String toString() {
      return this.keyStringRepr;
   }

   private DeweyDecimal getComparableKey() {
      return this.comparableKey;
   }

   private static DeweyDecimal initDeweyDecimal(String var0) {
      try {
         return new DeweyDecimal(var0);
      } catch (NumberFormatException var2) {
         return null;
      }
   }
}
