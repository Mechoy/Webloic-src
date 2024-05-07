package weblogic.logging;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class WeblogicLogfileFilter implements FilenameFilter {
   private String wlsLogfile;
   private static final int NUM_OF_DIGITS_IN_SUFFIX = String.valueOf(99999).length();
   private Pattern pattern;

   public WeblogicLogfileFilter(String var1) {
      this.pattern = null;
      this.wlsLogfile = var1;
      if (this.wlsLogfile.indexOf(37) > -1) {
         try {
            String var2 = Pattern.compile("%.*?%").matcher(var1).replaceAll(".*?") + "([0-9]{" + NUM_OF_DIGITS_IN_SUFFIX + "})*";
            this.pattern = Pattern.compile(var2);
         } catch (PatternSyntaxException var3) {
            this.pattern = null;
         }
      }

   }

   public WeblogicLogfileFilter(File var1) {
      this(var1.getName());
   }

   public boolean accept(File var1, String var2) {
      if (this.pattern != null) {
         Matcher var5 = this.pattern.matcher(var2);
         return var5.matches();
      } else {
         int var3 = this.wlsLogfile.length();
         if (!var2.startsWith(this.wlsLogfile)) {
            return false;
         } else if (var2.length() != var3 + NUM_OF_DIGITS_IN_SUFFIX) {
            return false;
         } else {
            for(int var4 = 0; var4 < NUM_OF_DIGITS_IN_SUFFIX; ++var4) {
               if (!Character.isDigit(var2.charAt(var3 + var4))) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
