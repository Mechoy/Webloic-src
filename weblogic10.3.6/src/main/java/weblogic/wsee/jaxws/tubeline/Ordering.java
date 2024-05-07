package weblogic.wsee.jaxws.tubeline;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.ws.WebServiceException;

public class Ordering extends AbstractCollection<TubelineAssemblerItem> {
   private Map<String, Set<String>> before = new HashMap();
   private Map<String, Set<String>> after = new HashMap();
   private Set<String> names = new HashSet();
   private List<TubelineAssemblerItem> internal = new ArrayList();

   public Ordering() {
   }

   public Ordering(Collection<TubelineAssemblerItem> var1) {
      this.addAll(var1);
   }

   public Iterator<TubelineAssemblerItem> iterator() {
      return new Iterator<TubelineAssemblerItem>() {
         private Collection<TubelineAssemblerItem> used = new HashSet();
         private Set<String> added = new HashSet();
         private int nextIndex = -1;
         private int removeIndex = -1;

         public boolean hasNext() {
            if (this.nextIndex >= 0) {
               return true;
            } else {
               while(this.used.size() != Ordering.this.internal.size()) {
                  ListIterator var1 = Ordering.this.internal.listIterator(Ordering.this.internal.size());

                  while(var1.hasPrevious()) {
                     TubelineAssemblerItem var2 = (TubelineAssemblerItem)var1.previous();
                     if (!this.used.contains(var2)) {
                        Object var3 = (Set)Ordering.this.after.get(var2.getName());
                        if (var3 != null) {
                           var3 = new HashSet((Collection)var3);
                           ((Set)var3).retainAll(Ordering.this.names);
                        }

                        if (var3 == null || this.added.containsAll((Collection)var3)) {
                           this.nextIndex = var1.previousIndex() + 1;
                           this.added.add(var2.getName());
                           return true;
                        }
                     }
                  }
               }

               return false;
            }
         }

         public TubelineAssemblerItem next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               TubelineAssemblerItem var1 = (TubelineAssemblerItem)Ordering.this.internal.get(this.nextIndex);
               this.removeIndex = this.nextIndex;
               this.nextIndex = -1;
               this.used.add(var1);
               return var1;
            }
         }

         public void remove() {
            if (this.removeIndex == -1) {
               throw new IllegalStateException();
            } else {
               Ordering.this.internal.remove(this.removeIndex);
               this.removeIndex = -1;
            }
         }
      };
   }

   public boolean add(TubelineAssemblerItem var1) {
      String var2 = var1.getName();
      Set var3 = var1.getGoBefore();
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            this.addRelationship(var5, var2);
         }
      }

      Set var8 = var1.getGoAfter();
      if (var8 != null) {
         Iterator var7 = var8.iterator();

         while(var7.hasNext()) {
            String var6 = (String)var7.next();
            this.addRelationship(var2, var6);
         }
      }

      this.names.add(var2);
      return this.internal.add(var1);
   }

   private void addRelationship(String var1, String var2) {
      Set var3 = (Set)this.before.get(var1);
      Set var4 = (Set)this.after.get(var2);
      if (var3 != null && var3.contains(var2) || var4 != null && var4.contains(var1)) {
         throw new WebServiceException("TubelineAssemblerItemS contain irreconcilable before/after requiremens due to " + var1 + " and " + var2);
      } else {
         Object var5 = (Set)this.after.get(var1);
         if (var5 == null) {
            var5 = new HashSet();
            this.after.put(var1, var5);
         }

         boolean var6 = ((Set)var5).add(var2);
         Object var7 = (Set)this.before.get(var2);
         if (var7 == null) {
            var7 = new HashSet();
            this.before.put(var2, var7);
         }

         boolean var8 = ((Set)var7).add(var1);
         Iterator var9;
         String var10;
         if (var6 && var4 != null) {
            var9 = var4.iterator();

            while(var9.hasNext()) {
               var10 = (String)var9.next();
               if (!var2.equals(var10)) {
                  this.addRelationship(var1, var10);
               }
            }
         }

         if (var8 && var3 != null) {
            var9 = var3.iterator();

            while(var9.hasNext()) {
               var10 = (String)var9.next();
               if (!var1.equals(var10)) {
                  this.addRelationship(var10, var2);
               }
            }
         }

      }
   }

   public int size() {
      return this.internal.size();
   }
}
