package weblogic.management.tools;

import java.util.HashSet;
import java.util.Iterator;
import weblogic.utils.compiler.Tool;

public class mbeaninfo extends Tool {
   private static final String ALL = "ALL_MBEANS";

   private mbeaninfo(String[] var1) {
      super(var1);
   }

   public static void main(String[] var0) throws Exception {
      try {
         if (var0.length == 0) {
            var0 = new String[]{"ALL_MBEANS"};
         }

         (new mbeaninfo(var0)).run();
      } catch (Exception var2) {
         var2.printStackTrace();
         System.exit(1);
      }

   }

   public void prepare() throws Exception {
      this.opts.addFlag("verbose", "Verbose output.");
   }

   public void runBody() throws Exception {
      boolean var1 = this.opts.hasOption("verbose");
      String[] var2 = this.opts.args();
      HashSet var3 = new HashSet();

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3.add(var2[var4]);
      }

      String var5;
      for(Iterator var6 = var3.iterator(); var6.hasNext(); var5 = (String)var6.next()) {
      }

   }
}
