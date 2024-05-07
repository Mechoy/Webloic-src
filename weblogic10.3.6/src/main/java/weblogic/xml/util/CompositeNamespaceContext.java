package weblogic.xml.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import weblogic.utils.collections.CombinedIterator;

public class CompositeNamespaceContext implements NamespaceContext {
   private final ArrayList contexts;

   public CompositeNamespaceContext(Collection var1) {
      this.contexts = new ArrayList(var1.size());
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         NamespaceContext var3 = (NamespaceContext)var2.next();
         if (var3 == null) {
            throw new IllegalArgumentException("null contexts not allowed");
         }

         var1.add(var3);
      }

   }

   public CompositeNamespaceContext(NamespaceContext var1, NamespaceContext var2) {
      if (var1 != null && var2 != null) {
         this.contexts = new ArrayList(2);
         this.contexts.add(var1);
         this.contexts.add(var2);
      } else {
         throw new IllegalArgumentException("null contexts not allowed");
      }
   }

   public String getNamespaceURI(String var1) {
      ArrayList var2 = this.contexts;
      int var3 = 0;

      for(int var4 = var2.size(); var3 < var4; ++var3) {
         NamespaceContext var5 = (NamespaceContext)var2.get(var3);
         String var6 = var5.getNamespaceURI(var1);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   public String getPrefix(String var1) {
      ArrayList var2 = this.contexts;
      int var3 = 0;

      for(int var4 = var2.size(); var3 < var4; ++var3) {
         NamespaceContext var5 = (NamespaceContext)var2.get(var3);
         String var6 = var5.getPrefix(var1);
         if (var6 != null) {
            return var6;
         }
      }

      return null;
   }

   public Iterator getPrefixes(String var1) {
      ArrayList var2 = this.contexts;
      int var3 = var2.size();
      Iterator[] var4 = new Iterator[var3];

      for(int var5 = 0; var5 < var3; ++var5) {
         NamespaceContext var6 = (NamespaceContext)var2.get(var5);
         var4[var5] = var6.getPrefixes(var1);
      }

      return new CombinedIterator(var4);
   }
}
