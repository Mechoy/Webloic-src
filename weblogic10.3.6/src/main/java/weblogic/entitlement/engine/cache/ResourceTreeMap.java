package weblogic.entitlement.engine.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.security.spi.Resource;

public class ResourceTreeMap {
   private Map treeMap = new HashMap();

   public void insertKey(Resource var1, Key var2) {
      synchronized(this.treeMap) {
         this.getNode(var1).getKeys().add(var2);
      }
   }

   private TreeNode getNode(Resource var1) {
      String var2 = var1 != null ? var1.toString() : null;
      TreeNode var3 = (TreeNode)this.treeMap.get(var2);
      if (var3 == null) {
         if (var1 != null) {
            TreeNode var4 = this.getNode(var1.getParentResource());
            var3 = new TreeNode(var4, var2);
            var4.getChildren().add(var3);
         } else {
            var3 = new TreeNode((TreeNode)null, var2);
         }

         this.treeMap.put(var2, var3);
      }

      return var3;
   }

   public void removeKey(Key var1) {
      if (var1 != null) {
         Resource var2 = var1.getResource();
         synchronized(this.treeMap) {
            TreeNode var4 = (TreeNode)this.treeMap.get(var2 != null ? var2.toString() : null);
            if (var4 != null && var4.hasKeys()) {
               var4.getKeys().remove(var1);
            }
         }
      }

   }

   public Set resourceChanged(String var1) {
      HashSet var2 = new HashSet();
      synchronized(this.treeMap) {
         TreeNode var4 = (TreeNode)this.treeMap.remove(var1);
         if (var4 != null) {
            TreeNode var5 = var4.getParent();
            if (var5 != null) {
               var5.getChildren().remove(var4);
            }

            this.removeNode(var4, var2);
         }

         return var2;
      }
   }

   private void removeNode(TreeNode var1, Set var2) {
      if (var1.hasKeys()) {
         var2.addAll(var1.getKeys());
      }

      if (var1.hasChildren()) {
         Iterator var3 = var1.getChildren().iterator();

         while(var3.hasNext()) {
            TreeNode var4 = (TreeNode)var3.next();
            this.treeMap.remove(var4.getResource());
            this.removeNode(var4, var2);
         }
      }

   }

   private static class TreeNode {
      private String resource;
      private TreeNode parent;
      private Collection children;
      private Set keys;

      public TreeNode(TreeNode var1, String var2) {
         this.parent = var1;
         this.resource = var2;
      }

      public String getResource() {
         return this.resource;
      }

      public TreeNode getParent() {
         return this.parent;
      }

      public boolean hasChildren() {
         return this.children != null && !this.children.isEmpty();
      }

      public Collection getChildren() {
         if (this.children == null) {
            this.children = new ArrayList();
         }

         return this.children;
      }

      public boolean hasKeys() {
         return this.keys != null && !this.keys.isEmpty();
      }

      public Set getKeys() {
         if (this.keys == null) {
            this.keys = new HashSet();
         }

         return this.keys;
      }
   }
}
