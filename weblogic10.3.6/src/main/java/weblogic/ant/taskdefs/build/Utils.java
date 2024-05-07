package weblogic.ant.taskdefs.build;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public final class Utils {
   public static String joinFileList(List var0) {
      StringBuffer var1 = new StringBuffer();
      String var2 = "";
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         var1.append(var2);
         var2 = " ";
         var1.append(((File)var3.next()).getAbsolutePath());
      }

      return var1.toString();
   }

   public static boolean fileExists(File var0, String var1, String var2) {
      return (new File(var0.getAbsolutePath() + File.separatorChar + var1, var2)).exists();
   }
}
