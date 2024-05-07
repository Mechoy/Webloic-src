package weblogic.application.internal.library;

import java.util.Collection;
import java.util.HashSet;
import weblogic.application.Type;
import weblogic.application.internal.library.util.NodeModificationException;
import weblogic.application.internal.library.util.SortedNodeTree;
import weblogic.application.library.LibraryConstants;
import weblogic.application.library.LibraryDefinition;
import weblogic.application.library.LibraryReference;

public class LibraryRegistry {
   private static final LibraryRegistry registry = new LibraryRegistry();
   private final SortedNodeTree<RegistryKey, LibraryDefinition> libTree = new SortedNodeTree();

   private LibraryRegistry() {
   }

   public static LibraryRegistry getRegistry() {
      return registry;
   }

   public synchronized void register(LibraryDefinition var1) throws LibraryRegistrationException {
      this.validate(var1);
      RegistryKey[] var2 = RegistryKey.newInstance(var1);
      if (this.libTree.hasElement(var2)) {
         throw new LibraryRegistrationException("Cannot register the same Library twice - this Library has already been registered: " + var1.toString());
      } else {
         try {
            this.libTree.put(var2, var1);
         } catch (NodeModificationException var4) {
            this.handleNodeModificationException(var1, var4);
         }

      }
   }

   private void validate(LibraryDefinition var1) throws LibraryRegistrationException {
      if (var1.getName() == null) {
         throw new LibraryRegistrationException(LibraryConstants.NAME_MUST_BE_SET_ERROR);
      } else if (var1.getImplementationVersion() != null && var1.getSpecificationVersion() == null) {
         throw new LibraryRegistrationException(LibraryConstants.NO_IMPL_WITHOUT_SPEC_ERROR);
      }
   }

   public LibraryDefinition lookup(LibraryReference var1) {
      return this.lookup(RegistryKey.newInstance(var1), var1.getExactMatch(), var1.getType());
   }

   private synchronized LibraryDefinition lookup(RegistryKey[] var1, boolean var2, Type var3) {
      LibraryLookup var4 = new LibraryLookup(var1, var2);
      this.libTree.traverse(var4);
      LibraryDefinition var5 = var4.getMatch();
      return var5 != null && var3 != null && var3 != var5.getType() ? null : var5;
   }

   public synchronized void remove(LibraryDefinition var1) {
      try {
         this.validate(var1);
      } catch (LibraryRegistrationException var3) {
         return;
      }

      this.libTree.remove(RegistryKey.newInstance(var1));
   }

   public synchronized int size() {
      return this.libTree.size();
   }

   public synchronized String toString() {
      return this.libTree.toString();
   }

   public synchronized Collection<LibraryDefinition> getAll() {
      HashSet var1 = new HashSet();
      this.libTree.getAll(var1);
      return var1;
   }

   private void handleNodeModificationException(LibraryDefinition var1, NodeModificationException var2) throws LibraryRegistrationException {
      String var3 = null;
      if (var2.getDepth() == 2) {
         var3 = LibraryConstants.SPEC_VERSION_NAME;
      } else if (var2.getDepth() == 3) {
         var3 = LibraryConstants.IMPL_VERSION_NAME;
      }

      String var4 = null;
      if (var2.getType() == NodeModificationException.Type.ADDING_VALUE_TO_NON_LEAF_NODE) {
         var4 = "Must provide " + var3 + " for Library, " + "because it is set for all other registered Libraries with name \"" + var1.getName() + "\"";
      } else if (var2.getType() == NodeModificationException.Type.ADDING_EDGE_TO_LEAF_NODE) {
         var4 = "Cannot register Library \"" + var1.getName() + "\" with " + var3 + " set, because other registered Libraries with the " + "same name do not have " + var3 + " set";
      }

      throw new LibraryRegistrationException(var4);
   }
}
