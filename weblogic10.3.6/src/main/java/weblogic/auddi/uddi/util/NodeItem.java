package weblogic.auddi.uddi.util;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeItem {
   private boolean m_canRead;
   private String m_key;
   protected String m_value;
   private String m_name;
   protected String m_tag;
   private DefaultMutableTreeNode m_treeNode;

   public NodeItem(String var1, String var2, String var3) {
      this("KeyedReference", var1, var2, var3);
   }

   public NodeItem(String var1, String var2, String var3, String var4) {
      this.m_canRead = true;
      this.m_key = var2;
      if (this.m_key != null && this.m_key.equals("")) {
         this.m_key = null;
      }

      this.m_tag = var1;
      this.m_value = var4;
      this.m_name = var3;
   }

   public void setTreeNode(DefaultMutableTreeNode var1) {
      this.m_treeNode = var1;
   }

   public void setTag(String var1) {
      this.m_tag = var1;
   }

   public void setKey(String var1) {
      this.m_key = var1;
   }

   public NodeItem(Node var1) {
      this.m_canRead = true;
      this.parse(var1);
   }

   public boolean isReadable() {
      return this.m_canRead;
   }

   public void setReadable(boolean var1) {
      this.m_canRead = var1;
   }

   private void parse(Node var1) {
      NamedNodeMap var2 = var1.getAttributes();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            String var4 = ((Attr)var2.item(var3)).getName();
            if (var4.equalsIgnoreCase("key") || var4.equalsIgnoreCase("tmodelkey")) {
               this.m_key = ((Attr)var2.item(var3)).getValue();
               if (this.m_key.trim().equals("")) {
                  this.m_key = null;
               }
            }

            if (var4.equalsIgnoreCase("name") || var4.equalsIgnoreCase("keyname")) {
               this.m_name = ((Attr)var2.item(var3)).getValue();
            }

            if (var4.equalsIgnoreCase("value") || var4.equalsIgnoreCase("keyvalue")) {
               this.m_value = ((Attr)var2.item(var3)).getValue();
            }
         }
      }

   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String var1) {
      this.m_name = var1;
   }

   public String getValue() {
      return this.m_value;
   }

   private TreeNode getLeaf(TreeNode var1) {
      return var1.isLeaf() ? var1 : this.getLeaf(var1.getChildAt(0));
   }

   private TreeNode getAncestorWithKey(TreeNode var1) {
      Object var2 = ((DefaultMutableTreeNode)var1.getParent()).getUserObject();
      if (var2 instanceof NodeItem) {
         NodeItem var3 = (NodeItem)var2;
         return var3.m_key != null ? var1.getParent() : this.getAncestorWithKey(var1.getParent());
      } else {
         return var1;
      }
   }

   public String getKey() {
      String var1 = this.m_key;
      if (var1 == null && this.m_treeNode != null) {
         DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)this.getAncestorWithKey(this.m_treeNode);
         Object var3 = var2.getUserObject();
         if (var3 != null) {
            NodeItem var4 = (NodeItem)var3;
            var1 = var4.getKey();
         }
      }

      if (var1 == null) {
         var1 = "";
      }

      return var1;
   }

   public String getKeyOld() {
      String var1 = this.m_key;
      if (var1 == null && this.m_treeNode != null) {
         DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)this.getLeaf(this.m_treeNode);
         Object var3 = var2.getUserObject();
         if (var3 != null) {
            NodeItem var4 = (NodeItem)var3;
            var1 = var4.getKey();
         }
      }

      if (var1 == null) {
         var1 = "";
      }

      return var1;
   }

   public String asTagString() {
      return "<" + this.m_tag + " key=\"" + this.getKey() + "\" name=\"" + this.m_name + "\" value=\"" + this.m_value + "\"/>";
   }

   public String asTagHead() {
      return "<" + this.m_tag + " key=\"" + this.getKey() + "\" name=\"" + this.m_name + "\" value=\"" + this.m_value + "\">";
   }

   public String asTagTail() {
      return "</" + this.m_tag + ">";
   }

   public String asKRString() {
      return "<keyedReference key=\"" + this.getKey() + "\" name=\"" + this.m_name + "\" value=\"" + this.m_value + "\"/>";
   }

   public String toString() {
      return this.m_name;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof NodeItem)) {
         return false;
      } else {
         NodeItem var2 = (NodeItem)var1;
         return var2.getName().equals(this.m_name) && var2.getValue().equals(this.m_value);
      }
   }
}
