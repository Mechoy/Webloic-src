package weblogic.application.internal.library;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.application.internal.library.util.AbstractTraversal;
import weblogic.application.internal.library.util.RONode;
import weblogic.application.internal.library.util.Traversal;
import weblogic.application.library.LibraryDefinition;

class LibraryLookup extends AbstractTraversal<RegistryKey, LibraryDefinition> implements Traversal<RegistryKey, LibraryDefinition> {
   private final RegistryKey[] edges;
   private final boolean exactMatch;
   private int edgeIndex = -1;
   private RONode<RegistryKey, LibraryDefinition> matchNode = null;

   public LibraryLookup(RegistryKey[] var1, boolean var2) {
      this.edges = var1;
      this.exactMatch = var2;
   }

   public RegistryKey getNextEdge(RONode<RegistryKey, LibraryDefinition> var1) {
      ++this.edgeIndex;
      if (this.edgeIndex != this.edges.length && var1.hasChildren()) {
         RegistryKey var2 = this.edges[this.edgeIndex];
         if (var1.getDepth() == 2) {
            var2 = this.getSpecVersionEdge(var1, var2);
         } else if (var1.getDepth() == 3) {
            var2 = this.getImplVersionEdge(var1, var2);
         }

         return var2 != null && var1.hasEdge(var2) ? var2 : null;
      } else {
         return null;
      }
   }

   private RegistryKey getSpecVersionEdge(RONode<RegistryKey, LibraryDefinition> var1, RegistryKey var2) {
      return this.exactMatch && var2 != null ? var2 : this.getHighestVersion(var1, var2);
   }

   private RegistryKey getHighestVersion(RONode<RegistryKey, LibraryDefinition> var1, RegistryKey var2) {
      RegistryKey var3 = (RegistryKey)var1.getHighestEdge();
      if (var2 == null) {
         return var3;
      } else {
         return var3.compareTo(var2) >= 0 ? var3 : var2;
      }
   }

   private RegistryKey getImplVersionEdge(RONode<RegistryKey, LibraryDefinition> var1, RegistryKey var2) {
      if (this.exactMatch && var2 != null) {
         return var2;
      } else {
         boolean var3 = this.areComparable(var1.getEdges());
         if (var2 == null) {
            if (var3) {
               return this.getHighestVersion(var1, (RegistryKey)null);
            }

            if (var1.getNumChildren() == 1) {
               return (RegistryKey)var1.getEdges().iterator().next();
            }
         }

         return var2 != null && this.isComparable(var2) && var3 ? this.getHighestVersion(var1, var2) : var2;
      }
   }

   private boolean isComparable(RegistryKey var1) {
      return var1.isComparable();
   }

   private boolean areComparable(Set<RegistryKey> var1) {
      Iterator var2 = var1.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(((RegistryKey)var2.next()).isComparable());

      return false;
   }

   public boolean match() {
      return this.getMatch() != null;
   }

   public LibraryDefinition getMatch() {
      return this.matchNode == null ? null : (LibraryDefinition)this.matchNode.getValue();
   }

   public void visitLeaf(RONode<RegistryKey, LibraryDefinition> var1, List<RegistryKey> var2) {
      if (this.edgeIndex == this.edges.length - 1 || this.remainingEdgesAreNull()) {
         this.matchNode = var1;
      }

   }

   private boolean remainingEdgesAreNull() {
      for(int var1 = this.edgeIndex + 1; var1 < this.edges.length; ++var1) {
         if (this.edges[var1] != null) {
            return false;
         }
      }

      return true;
   }
}
