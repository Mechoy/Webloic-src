package weblogic.servlet.ejb2jsp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

class MyNode extends DefaultMutableTreeNode {
   String label;

   public MyNode(Object var1) {
      super(var1);
   }

   public MyNode(Object var1, String var2) {
      super(var1);
      this.label = var2;
   }

   public MyNode(Object var1, boolean var2) {
      super(var1, var2);
   }

   public MyNode(Object var1, boolean var2, String var3) {
      super(var1, var2);
      this.label = var3;
   }

   public String toString() {
      return this.label != null ? this.label : super.toString();
   }
}
