package weblogic.utils.application;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public enum WarDetector {
   instance;

   private final Set<String> suffixes = new HashSet(Arrays.asList(".war"));
   private final Set<String> markers;

   private WarDetector() {
      this.markers = new HashSet(Arrays.asList("WEB-INF" + File.separator + "web.xml"));
   }

   public void addSuffix(String var1) {
      this.suffixes.add(var1);
   }

   public void addExplodedApplicationMarker(String var1) {
      this.markers.add(var1);
   }

   public String[] getSuffixes() {
      return (String[])((String[])this.suffixes.toArray(new String[0]));
   }

   public boolean suffixed(String var1) {
      Iterator var2 = this.suffixes.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var1.endsWith(var3));

      return true;
   }

   public String stem(String var1) {
      Iterator var2 = this.suffixes.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return var1;
         }

         var3 = (String)var2.next();
      } while(!var1.endsWith(var3));

      return var1.substring(0, var1.indexOf(var3));
   }

   public boolean isWar(File var1) {
      if (var1.isDirectory()) {
         String[] var2 = (String[])((String[])this.markers.toArray(new String[0]));
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if ((new File(var1, var6)).exists()) {
               return true;
            }
         }

         if (this.suffixed(var1.getName())) {
            return true;
         }
      }

      return this.suffixed(var1.getName().toLowerCase());
   }
}
