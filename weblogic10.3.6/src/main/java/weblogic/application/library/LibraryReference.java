package weblogic.application.library;

import java.io.File;
import weblogic.application.Type;
import weblogic.application.internal.library.BasicLibraryData;

public abstract class LibraryReference {
   private final BasicLibraryData libData;
   private final boolean exactMatch;

   protected LibraryReference(BasicLibraryData var1, boolean var2, Type var3) {
      this(var1, var2);
      this.libData.setType(var3);
   }

   protected LibraryReference(BasicLibraryData var1, boolean var2) {
      this.libData = var1;
      this.exactMatch = var2;
   }

   public String getName() {
      return this.libData.getName();
   }

   public String getSpecificationVersion() {
      return this.libData.getSpecificationVersion() == null ? null : this.libData.getSpecificationVersion().toString();
   }

   public String getImplementationVersion() {
      return this.libData.getImplementationVersion();
   }

   public Type getType() {
      return this.libData.getType();
   }

   public boolean getExactMatch() {
      return this.exactMatch;
   }

   public BasicLibraryData getLibData() {
      return this.libData;
   }

   protected abstract void moreToString(StringBuffer var1);

   public int hashCode() {
      return String.valueOf(this.getName()).hashCode() ^ String.valueOf(this.getSpecificationVersion()).hashCode() ^ String.valueOf(this.getImplementationVersion()).hashCode() ^ String.valueOf(this.exactMatch).hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof LibraryReference)) {
         return false;
      } else {
         LibraryReference var2 = (LibraryReference)var1;
         return this.getName().equals(var2.getName()) && String.valueOf(this.getSpecificationVersion()).equals(String.valueOf(var2.getSpecificationVersion())) && String.valueOf(var2.getImplementationVersion()).equals(String.valueOf(var2.getImplementationVersion())) && this.exactMatch == var2.exactMatch;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.libData.toString());
      this.moreToString(var1);
      return var1.toString();
   }

   LibEntry getCompositeEntry(Library var1) {
      return new LibEntry(var1.getLocation());
   }

   static class LibEntry {
      protected File location;

      LibEntry(File var1) {
         this.location = var1;
      }

      public boolean equals(Object var1) {
         if (this.getClass() != var1.getClass() && !(var1 instanceof LibEntry)) {
            return false;
         } else {
            LibEntry var2 = (LibEntry)var1;
            return this.location.equals(var2.location);
         }
      }

      public int hashCode() {
         return this.location.hashCode();
      }
   }
}
