package weblogic.application.internal.library;

import weblogic.application.Type;
import weblogic.application.library.LibraryReference;

public class OptionalPackageReference extends LibraryReference {
   private final String src;

   public OptionalPackageReference(BasicLibraryData var1, String var2) {
      super(var1, false, (Type)null);
      this.src = var2;
   }

   protected void moreToString(StringBuffer var1) {
      var1.append(", ").append("referenced from").append(": ").append(this.src);
   }
}
