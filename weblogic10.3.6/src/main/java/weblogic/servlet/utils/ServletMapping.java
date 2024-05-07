package weblogic.servlet.utils;

import java.util.HashMap;
import weblogic.utils.collections.MatchMap;

public final class ServletMapping extends StandardURLMapping implements URLMapping {
   private final boolean strictPattern;
   private Object slashStarObject;

   public ServletMapping() {
      this(false, true);
   }

   public ServletMapping(boolean var1, boolean var2) {
      super(var1, var1);
      this.strictPattern = var2;
   }

   private ServletMapping(MatchMap var1, HashMap var2, boolean var3, boolean var4, boolean var5) {
      super(var1, var2, var3, var4);
      this.strictPattern = var5;
   }

   public void put(String var1, Object var2) {
      var1 = fixPattern(var1);
      if (!this.strictPattern && var1.equals("/*")) {
         if (this.slashStarObject == null) {
            ++this.size;
         }

         this.slashStarObject = var2;
      } else {
         super.put(var1, var2);
      }
   }

   public Object removePattern(String var1) {
      var1 = fixPattern(var1);
      if (!this.strictPattern && var1.equals("/*")) {
         if (this.slashStarObject != null) {
            --this.size;
         }

         Object var2 = this.slashStarObject;
         this.slashStarObject = null;
         return var2;
      } else {
         return super.removePattern(var1);
      }
   }

   public Object get(String var1) {
      if (this.strictPattern) {
         return super.get(var1);
      } else {
         var1 = var1.length() == 0 ? "/" : this.cased(var1);
         Object var2 = null;
         if ((var2 = this.getExactOrPathMatch(var1)) != null) {
            return var2;
         } else if ((var2 = this.getExtensionMatch(var1)) != null) {
            return var2;
         } else {
            return this.slashStarObject != null ? this.slashStarObject : this.getDefault();
         }
      }
   }

   public String[] keys() {
      String[] var1 = super.keys();
      if (this.slashStarObject != null) {
         var1[var1.length - 1] = "/*";
      }

      return var1;
   }

   public Object[] values() {
      Object[] var1 = super.values();
      if (this.slashStarObject != null) {
         var1[var1.length - 1] = this.slashStarObject;
      }

      return var1;
   }

   public Object clone() {
      ServletMapping var1 = new ServletMapping((MatchMap)this.matchMap.clone(), new HashMap(this.extensionMap), this.ignoreCase, this.ignoreExtensionCase, this.strictPattern);
      var1.setDefault(this.defaultObject);
      var1.size = this.size;
      var1.slashStarObject = this.slashStarObject;
      return var1;
   }

   private static String fixPattern(String var0) {
      if (!var0.startsWith("/") && !var0.startsWith("*.")) {
         var0 = "/" + var0;
      }

      if (var0.endsWith("/") && var0.length() > 1) {
         var0 = var0.substring(0, var0.length() - 1);
      }

      return var0;
   }
}
