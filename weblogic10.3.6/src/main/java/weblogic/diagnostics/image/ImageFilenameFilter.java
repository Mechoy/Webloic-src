package weblogic.diagnostics.image;

import java.io.File;
import java.io.FilenameFilter;

class ImageFilenameFilter implements FilenameFilter {
   private String filenameToFilter;

   ImageFilenameFilter(String var1) {
      this.filenameToFilter = var1;
   }

   public boolean accept(File var1, String var2) {
      return var2.startsWith(this.filenameToFilter);
   }
}
