package weblogic.servlet.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.utils.collections.MatchMap;

public class StandardURLMapping implements URLMapping {
   protected final MatchMap matchMap;
   protected final HashMap extensionMap;
   protected boolean ignoreExtensionCase;
   protected boolean ignoreCase;
   protected Object defaultObject;
   protected int size;

   public StandardURLMapping() {
      this(false, false);
   }

   public StandardURLMapping(boolean var1, boolean var2) {
      this(new MatchMap(), new HashMap(), var1, var2);
   }

   protected StandardURLMapping(MatchMap var1, HashMap var2, boolean var3, boolean var4) {
      this.matchMap = var1;
      this.extensionMap = var2;
      this.ignoreCase = var3;
      this.ignoreExtensionCase = var4;
      this.defaultObject = null;
      this.size = 0;
   }

   public void put(String var1, Object var2) {
      Object var3 = null;
      if (var1.equals("/")) {
         var3 = this.putDefaultMapping(var2);
      } else if (var1.startsWith("*.")) {
         var3 = this.putExtensionMapping(var1, var2);
      } else if (var1.startsWith("/") && var1.endsWith("/*")) {
         var3 = this.putPathMapping(var1, var2);
      } else {
         var3 = this.putExactMapping(var1, var2);
      }

      if (var3 == null) {
         ++this.size;
      }

   }

   public void remove(String var1) {
      this.removePattern(var1);
   }

   public Object removePattern(String var1) {
      Object var2 = null;
      if (var1.equals("/")) {
         var2 = this.removeDefaultMapping();
      } else if (var1.startsWith("*.")) {
         var2 = this.removeExtensionMapping(var1);
      } else if (var1.startsWith("/") && var1.endsWith("/*")) {
         var2 = this.removePathMapping(var1);
      } else {
         var2 = this.removeExactMapping(var1);
      }

      if (var2 != null) {
         --this.size;
      }

      return var2;
   }

   public Object get(String var1) {
      var1 = var1.length() == 0 ? "/" : this.cased(var1);
      Object var2 = null;
      if ((var2 = this.getExactOrPathMatch(var1)) != null) {
         return var2;
      } else {
         return (var2 = this.getExtensionMatch(var1)) != null ? var2 : this.getDefault();
      }
   }

   public void setDefault(Object var1) {
      this.defaultObject = var1;
   }

   public Object getDefault() {
      return this.defaultObject;
   }

   public String[] keys() {
      String[] var1 = new String[this.size()];
      int var2 = 0;
      if (this.defaultObject != null) {
         var1[var2++] = "/";
      }

      Iterator var3;
      Map.Entry var4;
      for(var3 = this.matchMap.entrySet().iterator(); var3.hasNext(); var2 = ((Node)var4.getValue()).addKey((String)var4.getKey(), var1, var2)) {
         var4 = (Map.Entry)var3.next();
      }

      for(var3 = this.extensionMap.keySet().iterator(); var3.hasNext(); var1[var2++] = "*." + var3.next()) {
      }

      return var1;
   }

   public Object[] values() {
      Object[] var1 = new Object[this.size()];
      int var2 = 0;
      if (this.defaultObject != null) {
         var1[var2++] = this.defaultObject;
      }

      Iterator var3;
      Map.Entry var4;
      for(var3 = this.matchMap.entrySet().iterator(); var3.hasNext(); var2 = ((Node)var4.getValue()).addValue(var1, var2)) {
         var4 = (Map.Entry)var3.next();
      }

      for(var3 = this.extensionMap.values().iterator(); var3.hasNext(); var1[var2++] = var3.next()) {
      }

      return var1;
   }

   public int size() {
      return this.size;
   }

   public void setCaseInsensitive(boolean var1) {
      this.ignoreCase = var1;
   }

   public boolean isCaseInsensitive() {
      return this.ignoreCase;
   }

   public void setExtensionCaseInsensitive(boolean var1) {
      this.ignoreExtensionCase = var1;
   }

   public boolean isExtensionCaseInsensitive() {
      return this.ignoreExtensionCase;
   }

   public Object clone() {
      StandardURLMapping var1 = new StandardURLMapping((MatchMap)this.matchMap.clone(), new HashMap(this.extensionMap), this.ignoreCase, this.ignoreExtensionCase);
      var1.setDefault(this.defaultObject);
      var1.size = this.size;
      return var1;
   }

   protected Object putDefaultMapping(Object var1) {
      Object var2 = this.defaultObject;
      this.defaultObject = var1;
      return var2;
   }

   protected Object removeDefaultMapping() {
      Object var1 = this.defaultObject;
      this.defaultObject = null;
      return var1;
   }

   protected Object putExtensionMapping(String var1, Object var2) {
      return this.extensionMap.put(this.casedExtension(var1.substring(2)), var2);
   }

   protected Object removeExtensionMapping(String var1) {
      return this.extensionMap.remove(this.casedExtension(var1.substring(2)));
   }

   protected Object getExtensionMatch(String var1) {
      int var2 = var1.lastIndexOf(46);
      return var2 >= 0 ? this.extensionMap.get(this.casedExtension(var1.substring(var2 + 1))) : null;
   }

   protected Object putPathMapping(String var1, Object var2) {
      var1 = this.cased(var1);
      int var3 = var1.length();
      String var4 = var1.substring(0, var3 - 2);
      FullMatchNode var5 = (FullMatchNode)this.matchMap.get(var4);
      if (var5 != null) {
         var5.patternValue = var2;
      } else {
         this.matchMap.put(var4, new FullMatchNode(var2, var3 - 2));
      }

      MatchMap.Entry var6 = this.matchMap.put(var1.substring(0, var3 - 1), new Node(var2));
      return var6 == null ? null : ((Node)var6.getValue()).patternValue;
   }

   protected Object removePathMapping(String var1) {
      var1 = this.cased(var1);
      int var2 = var1.length();
      Node var3 = (Node)this.matchMap.remove(var1.substring(0, var2 - 1));
      if (var3 == null) {
         return null;
      } else {
         String var4 = var1.substring(0, var2 - 2);
         FullMatchNode var5 = (FullMatchNode)this.matchMap.get(var4);
         if (var5 != null && var5.patternValue != null) {
            var5.patternValue = null;
            if (var5.exactValue == null) {
               this.matchMap.remove(var4);
            }
         }

         return var3.patternValue;
      }
   }

   protected Object putExactMapping(String var1, Object var2) {
      var1 = this.cased(var1);
      int var3 = var1.length();
      FullMatchNode var4 = (FullMatchNode)this.matchMap.get(var1);
      if (var4 == null) {
         this.matchMap.put(var1, new FullMatchNode((Object)null, var3, var2));
         return null;
      } else {
         Object var5 = var4.exactValue;
         var4.exactValue = var2;
         return var5;
      }
   }

   protected Object removeExactMapping(String var1) {
      var1 = this.cased(var1);
      FullMatchNode var2 = (FullMatchNode)this.matchMap.get(var1);
      if (var2 == null) {
         return null;
      } else {
         Object var3 = var2.exactValue;
         var2.exactValue = null;
         if (var2.patternValue == null) {
            this.matchMap.remove(var1);
         }

         return var3;
      }
   }

   protected Object getExactOrPathMatch(String var1) {
      String var2 = var1;

      do {
         Map.Entry var3 = this.matchMap.match(var2);
         if (var3 == null) {
            break;
         }

         Node var4 = (Node)var3.getValue();
         Object var5 = var4.match(var2);
         if (var5 != null) {
            return var5;
         }

         var2 = var4.shorterKey(var2);
      } while(var2 != null);

      return null;
   }

   protected String cased(String var1) {
      if (var1 == null) {
         return null;
      } else {
         return this.ignoreCase ? var1.toLowerCase() : var1;
      }
   }

   protected String casedExtension(String var1) {
      if (var1 == null) {
         return null;
      } else {
         return this.ignoreExtensionCase ? var1.toLowerCase() : var1;
      }
   }

   private static final class FullMatchNode extends Node {
      final int keyLength;

      FullMatchNode(Object var1, int var2) {
         this(var1, var2, (Object)null);
      }

      FullMatchNode(Object var1, int var2, Object var3) {
         super(var1);
         this.keyLength = var2;
         this.exactValue = var3;
      }

      Object match(String var1) {
         if (var1.length() != this.keyLength) {
            return null;
         } else {
            return this.exactValue == null ? this.patternValue : this.exactValue;
         }
      }

      int addKey(String var1, String[] var2, int var3) {
         if (this.exactValue != null) {
            var2[var3++] = var1;
         }

         return var3;
      }

      int addValue(Object[] var1, int var2) {
         if (this.exactValue != null) {
            var1[var2++] = this.exactValue;
         }

         return var2;
      }

      String shorterKey(String var1) {
         int var2 = var1.lastIndexOf(47, this.keyLength - 1);
         return var2 >= 0 ? var1.substring(0, var2 + 1) : null;
      }
   }

   private static class Node {
      Object patternValue;
      Object exactValue;

      Node(Object var1) {
         this.patternValue = var1;
      }

      Object match(String var1) {
         return this.patternValue;
      }

      int addKey(String var1, String[] var2, int var3) {
         var2[var3++] = var1 + "*";
         return var3;
      }

      int addValue(Object[] var1, int var2) {
         var1[var2++] = this.patternValue;
         return var2;
      }

      boolean isExactMatch() {
         return this.exactValue != null;
      }

      String shorterKey(String var1) {
         return null;
      }
   }
}
