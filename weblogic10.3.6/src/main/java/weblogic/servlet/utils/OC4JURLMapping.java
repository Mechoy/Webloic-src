package weblogic.servlet.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.collections.MatchMap;

public class OC4JURLMapping extends StandardURLMapping implements URLMapping {
   final MatchMap wildcardMap;

   public OC4JURLMapping() {
      this(false, false);
   }

   public OC4JURLMapping(boolean var1, boolean var2) {
      super(var1, var2);
      this.wildcardMap = new MatchMap();
   }

   private OC4JURLMapping(MatchMap var1, HashMap var2, boolean var3, boolean var4, MatchMap var5) {
      super(var1, var2, var3, var4);
      this.wildcardMap = var5;
   }

   public void put(String var1, Object var2) {
      var1 = fixPattern(var1);
      int var3 = var1.indexOf("*");
      int var4 = var1.length();
      if (var3 == var4 - 1 && var1.charAt(var4 - 2) != '/') {
         this.putWildcardMapping(var1.substring(0, var4 - 1), (String)null, var2);
      } else if (var3 > 0 && var3 < var4 - 1) {
         this.putWildcardMapping(var1.substring(0, var3), var1.substring(var3 + 1), var2);
      } else {
         super.put(var1, var2);
      }

   }

   public Object get(String var1) {
      var1 = var1.length() == 0 ? "/" : this.cased(var1);
      Object var2 = null;
      if ((var2 = this.getExactOrPathMatch(var1)) != null) {
         return var2;
      } else if (!this.wildcardMap.isEmpty() && (var2 = this.getWildcardMatch(var1, true)) != null) {
         return var2;
      } else if ((var2 = this.getExtensionMatch(var1)) != null) {
         return var2;
      } else {
         return !this.wildcardMap.isEmpty() && (var2 = this.getWildcardMatch(var1, false)) != null ? var2 : this.getDefault();
      }
   }

   public Object removePattern(String var1) {
      var1 = fixPattern(var1);
      int var2 = var1.indexOf("*");
      int var3 = var1.length();
      if (var2 == var3 - 1 && var1.charAt(var3 - 2) != '/') {
         return this.removeWildcardMapping(var1.substring(0, var3 - 1), (String)null);
      } else {
         return var2 > 1 && var2 < var3 - 1 ? this.removeWildcardMapping(var1.substring(0, var2), var1.substring(var2 + 1)) : super.removePattern(var1);
      }
   }

   public String[] keys() {
      String[] var1 = super.keys();
      if (!this.wildcardMap.isEmpty()) {
         int var2 = getIndexOfFirstNull(var1);

         Map.Entry var4;
         for(Iterator var3 = this.wildcardMap.entrySet().iterator(); var3.hasNext(); var2 = ((WildcardNode)var4.getValue()).addPatterns((String)var4.getKey(), var1, var2)) {
            var4 = (Map.Entry)var3.next();
         }
      }

      return var1;
   }

   public Object[] values() {
      Object[] var1 = super.values();
      if (!this.wildcardMap.isEmpty()) {
         int var2 = getIndexOfFirstNull(var1);

         Map.Entry var4;
         for(Iterator var3 = this.wildcardMap.entrySet().iterator(); var3.hasNext(); var2 = ((WildcardNode)var4.getValue()).addPatternValues(var1, var2)) {
            var4 = (Map.Entry)var3.next();
         }
      }

      return var1;
   }

   public Object clone() {
      OC4JURLMapping var1 = new OC4JURLMapping((MatchMap)this.matchMap.clone(), new HashMap(this.extensionMap), this.ignoreCase, this.ignoreExtensionCase, (MatchMap)this.wildcardMap.clone());
      var1.setDefault(this.defaultObject);
      var1.size = this.size;
      return var1;
   }

   private Object putWildcardMapping(String var1, String var2, Object var3) {
      var1 = this.cased(var1);
      var2 = this.cased(var2);
      int var4 = var1.lastIndexOf(47);
      String var5 = var1.substring(0, var4);
      WildcardNode var6 = (WildcardNode)this.wildcardMap.get(var5);
      if (var6 != null) {
         Object var7 = var6.addValueToMapNode(var1.substring(var4), var2, var3);
         if (var7 == null) {
            ++this.size;
         }

         return var7;
      } else {
         var6 = new WildcardNode(var5);
         var6.addValueToMapNode(var1.substring(var4), var2, var3);
         this.wildcardMap.put(var5, var6);
         ++this.size;
         return null;
      }
   }

   private Object getWildcardMatch(String var1, boolean var2) {
      int var3 = var1.lastIndexOf(47);

      for(String var4 = var1.substring(0, var3); var4 != null; var4 = var3 <= 0 ? null : var4.substring(0, var3)) {
         Map.Entry var5 = this.wildcardMap.match(var4);
         if (var5 == null) {
            break;
         }

         WildcardNode var6 = (WildcardNode)var5.getValue();
         Object var7 = var6.match(var1, var2);
         if (var7 != null) {
            return var7;
         }

         var3 = var4.lastIndexOf(47);
      }

      return null;
   }

   private Object removeWildcardMapping(String var1, String var2) {
      var1 = this.cased(var1);
      var2 = this.cased(var2);
      int var3 = var1.lastIndexOf(47);
      String var4 = var1.substring(0, var3);
      WildcardNode var5 = (WildcardNode)this.wildcardMap.get(var4);
      if (var5 == null) {
         return null;
      } else {
         Object var6 = var5.removeValueFromMapNode(var1.substring(var3), var2);
         if (var6 != null) {
            --this.size;
         }

         return var6;
      }
   }

   private static String fixPattern(String var0) {
      if (var0.startsWith("/*.")) {
         return var0.substring(1);
      } else {
         return !var0.startsWith("/") && !var0.startsWith("*.") ? "/".concat(var0) : var0;
      }
   }

   private static int getIndexOfFirstNull(Object[] var0) {
      int var1;
      for(var1 = 0; var1 < var0.length && var0[var1] != null; ++var1) {
      }

      return var1;
   }

   private static class WildcardKey {
      final String prefix;
      final String suffix;
      volatile int hashCode;

      WildcardKey(String var1, String var2) {
         this.prefix = var1;
         this.suffix = var2;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof WildcardKey)) {
            return false;
         } else {
            boolean var10000;
            label43: {
               label29: {
                  WildcardKey var2 = (WildcardKey)var1;
                  if (var2.prefix != null) {
                     if (!var2.prefix.equals(this.prefix)) {
                        break label29;
                     }
                  } else if (this.prefix != null) {
                     break label29;
                  }

                  if (var2.suffix != null) {
                     if (var2.suffix.equals(this.suffix)) {
                        break label43;
                     }
                  } else if (this.suffix == null) {
                     break label43;
                  }
               }

               var10000 = false;
               return var10000;
            }

            var10000 = true;
            return var10000;
         }
      }

      public int hashCode() {
         int var1 = this.hashCode;
         if (var1 == 0) {
            byte var2 = 17;
            var1 = 31 * var2 + (this.prefix == null ? 0 : this.prefix.hashCode());
            var1 = 31 * var1 + (this.suffix == null ? 0 : this.suffix.hashCode());
            this.hashCode = var1;
         }

         return var1;
      }
   }

   private static class WildcardNode {
      final String pattern;
      final Map values;

      WildcardNode(String var1) {
         this.pattern = var1;
         this.values = new HashMap();
      }

      Object match(String var1, boolean var2) {
         String var3 = var1.substring(this.pattern.length());
         Iterator var4 = this.values.keySet().iterator();

         while(var4.hasNext()) {
            WildcardKey var5 = (WildcardKey)var4.next();
            if (var3.startsWith(var5.prefix)) {
               if (var2) {
                  if (var5.suffix == null) {
                     return this.values.get(var5);
                  }
               } else if (var5.suffix != null && var3.endsWith(var5.suffix)) {
                  return this.values.get(var5);
               }
            }
         }

         return null;
      }

      Object addValueToMapNode(String var1, String var2, Object var3) {
         return this.values.put(new WildcardKey(var1, var2), var3);
      }

      Object removeValueFromMapNode(String var1, String var2) {
         return this.values.remove(new WildcardKey(var1, var2));
      }

      int addPatterns(String var1, String[] var2, int var3) {
         WildcardKey var5;
         for(Iterator var4 = this.values.keySet().iterator(); var4.hasNext(); var2[var3++] = var1 + var5.prefix + "*" + (var5.suffix == null ? "" : var5.suffix)) {
            var5 = (WildcardKey)var4.next();
         }

         return var3;
      }

      int addPatternValues(Object[] var1, int var2) {
         for(Iterator var3 = this.values.values().iterator(); var3.hasNext(); var1[var2++] = var3.next()) {
         }

         return var2;
      }
   }
}
