package weblogic.application.library;

import java.io.File;
import weblogic.application.internal.library.BasicLibraryData;

public class J2EELibraryReference extends LibraryReference {
   private final String contextRoot;

   J2EELibraryReference(BasicLibraryData var1, boolean var2, String var3) {
      super(var1, var2);
      this.contextRoot = var3;
   }

   public String getContextRoot() {
      return this.contextRoot;
   }

   public int hashCode() {
      return super.hashCode() ^ String.valueOf(this.contextRoot).hashCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof J2EELibraryReference)) {
         return false;
      } else {
         return super.equals(var1) && String.valueOf(this.contextRoot).equals(String.valueOf(((J2EELibraryReference)var1).contextRoot));
      }
   }

   protected void moreToString(StringBuffer var1) {
      var1.append(", ").append("exact-match").append(": ").append(this.getExactMatch());
      if (this.contextRoot != null) {
         var1.append(", ").append("context-root").append(": ").append(this.contextRoot);
      }

   }

   LibraryReference.LibEntry getCompositeEntry(Library var1) {
      return new J2EELibEntry(var1.getLocation(), this.getContextRoot());
   }

   static class J2EELibEntry extends LibraryReference.LibEntry {
      private String contextRoot;

      J2EELibEntry(File var1, String var2) {
         super(var1);
         this.contextRoot = var2;
      }

      public boolean equals(Object var1) {
         if (var1.getClass() != this.getClass() && !(var1 instanceof J2EELibEntry)) {
            return false;
         } else {
            J2EELibEntry var2 = (J2EELibEntry)var1;
            return this.location.equals(var2.location) && String.valueOf(this.contextRoot).equals(String.valueOf(var2.contextRoot));
         }
      }

      public int hashCode() {
         return this.location.hashCode() * 17 ^ String.valueOf(this.contextRoot).hashCode();
      }
   }
}
