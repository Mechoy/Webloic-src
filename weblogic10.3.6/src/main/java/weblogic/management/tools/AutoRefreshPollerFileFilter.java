package weblogic.management.tools;

import java.io.File;
import java.io.FileFilter;
import weblogic.utils.StringUtils;

public class AutoRefreshPollerFileFilter implements FileFilter {
   public static final boolean debug = false;
   private String[] filter;

   public AutoRefreshPollerFileFilter(String var1) {
      if (var1 != null) {
         this.filter = StringUtils.splitCompletely(var1, ",");

         for(int var2 = 0; var2 < this.filter.length; ++var2) {
         }
      }

   }

   public boolean accept(File var1) {
      if (this.filter == null) {
         return true;
      } else {
         String var2 = var1.getName();

         for(int var3 = 0; var3 < this.filter.length; ++var3) {
            if (var2.endsWith(this.filter[var3]) && !var1.isDirectory()) {
               return true;
            }
         }

         return false;
      }
   }
}
