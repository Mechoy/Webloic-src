package weblogic.socket.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RegexpPool {
   private static final String STAR = "*";
   private boolean matchAll = false;
   private ArrayList prefixes = null;
   private ArrayList suffixes = null;
   private HashMap strings = null;

   public boolean add(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null expression string");
      } else {
         boolean var2 = false;
         if (var1.equals("*")) {
            var2 = this.matchAll;
            this.matchAll = true;
         } else {
            String var3;
            if (var1.startsWith("*")) {
               var3 = var1.substring(1);
               if (this.suffixes == null) {
                  this.suffixes = new ArrayList();
               } else {
                  var2 = this.suffixes.contains(var3);
               }

               if (!var2) {
                  this.suffixes.add(var3);
               }
            } else if (var1.endsWith("*")) {
               var3 = var1.substring(0, var1.length() - 1);
               if (this.prefixes == null) {
                  this.prefixes = new ArrayList();
               } else {
                  var2 = this.prefixes.contains(var3);
               }

               if (!var2) {
                  this.prefixes.add(var3);
               }
            } else {
               if (this.strings == null) {
                  this.strings = new HashMap();
               }

               var2 = this.strings.put(var1, var1) != null;
            }
         }

         return !var2;
      }
   }

   public boolean remove(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null expression string");
      } else if (var1.equals("*")) {
         boolean var2 = this.matchAll;
         this.matchAll = false;
         return var2;
      } else if (var1.startsWith("*")) {
         return this.suffixes != null && this.suffixes.remove(var1.substring(1));
      } else if (!var1.endsWith("*")) {
         return this.strings.remove(var1) != null;
      } else {
         return this.prefixes != null && this.prefixes.remove(var1.substring(0, var1.length() - 1));
      }
   }

   public boolean match(String var1) {
      if (this.matchAll) {
         return true;
      } else if (this.strings != null && this.strings.containsKey(var1)) {
         return true;
      } else {
         int var2;
         int var3;
         if (this.suffixes != null) {
            var2 = 0;

            for(var3 = this.suffixes.size(); var2 < var3; ++var2) {
               if (var1.endsWith((String)this.suffixes.get(var2))) {
                  return true;
               }
            }
         }

         if (this.prefixes != null) {
            var2 = 0;

            for(var3 = this.prefixes.size(); var2 < var3; ++var2) {
               if (var1.startsWith((String)this.prefixes.get(var2))) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}
