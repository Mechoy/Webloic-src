package weblogic.servlet.ejb2jsp.gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;
import weblogic.servlet.ejb2jsp.dd.FilesystemInfoDescriptor;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;

public class EJBTaglibDescriptorTree extends JTree {
   EJBTaglibDescriptor _bean;
   DefaultTreeModel defaultModel = null;

   public EJBTaglibDescriptorTree(EJBTaglibDescriptor var1) throws Exception {
      this._bean = var1;
      this.defaultModel = new DefaultTreeModel(this.makeRootNode());
      this.setModel(this.defaultModel);
   }

   private TreeNode makeRootNode() throws Exception {
      MyNode var1 = null;
      var1 = new MyNode(this._bean, true, (String)null);
      this.add_nodes_EJBTaglibDescriptor(var1, this._bean);
      return var1;
   }

   public EJBTaglibDescriptor getBean() {
      return this._bean;
   }

   private void add_nodes_EJBTaglibDescriptor(DefaultMutableTreeNode var1, EJBTaglibDescriptor var2) throws Exception {
      if (var2 != null) {
         BeanDescriptor[] var3 = var2.getBeans();
         if (var3 != null && var3.length > 0) {
            DefaultMutableTreeNode var4 = var1;

            for(int var5 = 0; var5 < var3.length; ++var5) {
               MyNode var6 = new MyNode(var3[var5], true);
               var4.add(var6);
               this.add_nodes_BeanDescriptor(var6, var3[var5]);
            }
         }

         MyNode var7 = new MyNode(var2.getFileInfo(), (String)null);
         var1.add(var7);
         this.add_nodes_FilesystemInfoDescriptor(var7, var2.getFileInfo());
      }
   }

   private void add_nodes_FilesystemInfoDescriptor(DefaultMutableTreeNode var1, FilesystemInfoDescriptor var2) throws Exception {
      if (var2 != null) {
         ;
      }
   }

   private void add_nodes_MethodParamDescriptor(DefaultMutableTreeNode var1, MethodParamDescriptor var2) throws Exception {
      if (var2 != null) {
         ;
      }
   }

   private void add_nodes_EJBMethodDescriptor(DefaultMutableTreeNode var1, EJBMethodDescriptor var2) throws Exception {
      if (var2 != null) {
         MethodParamDescriptor[] var3 = var2.getParams();
         if (var3 != null && var3.length > 0) {
            DefaultMutableTreeNode var4 = var1;

            for(int var5 = 0; var5 < var3.length; ++var5) {
               MyNode var6 = new MyNode(var3[var5], true);
               var4.add(var6);
               this.add_nodes_MethodParamDescriptor(var6, var3[var5]);
            }
         }

      }
   }

   private void add_nodes_BeanDescriptor(DefaultMutableTreeNode var1, BeanDescriptor var2) throws Exception {
      if (var2 != null) {
         EJBMethodDescriptor[] var3 = var2.getHomeMethods();
         MyNode var4;
         int var5;
         MyNode var6;
         if (var3 != null && var3.length > 0) {
            var4 = new MyNode(var3, true, "Home Methods");
            var1.add(var4);

            for(var5 = 0; var5 < var3.length; ++var5) {
               var6 = new MyNode(var3[var5], true);
               var4.add(var6);
               this.add_nodes_EJBMethodDescriptor(var6, var3[var5]);
            }
         }

         var3 = var2.getEJBMethods();
         if (var3 != null && var3.length > 0) {
            var4 = new MyNode(var3, true, "EJB Methods");
            var1.add(var4);

            for(var5 = 0; var5 < var3.length; ++var5) {
               var6 = new MyNode(var3[var5], true);
               var4.add(var6);
               this.add_nodes_EJBMethodDescriptor(var6, var3[var5]);
            }
         }

      }
   }
}
