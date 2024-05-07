package weblogic.tools.ui;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class ListProperty extends Property {
   JComboBox jc;
   boolean selectFirstElement;
   boolean allowEditing;
   JTextField editor;

   public ListProperty(Object var1, PropertyDescriptor var2, String var3, String[] var4, boolean var5) {
      super(var1, var2, var3, var5);
      this.selectFirstElement = false;
      this.allowEditing = false;
      this.jc = new SortedComboBox(var4);
      this.jc.addFocusListener(this);
      Component var6 = this.jc.getEditor().getEditorComponent();
      if (var6 instanceof JTextField) {
         this.editor = (JTextField)var6;
      }

   }

   public ListProperty(PropertyDescriptor var1, String var2, String[] var3, boolean var4) {
      this((Object)null, var1, var2, var3, var4);
   }

   public boolean isUIEmpty() {
      if (this.allowEditing) {
         String var1 = this.editor.getText();
         if (var1 != null && var1.trim().length() > 0) {
            return false;
         }
      }

      return this.jc.getSelectedIndex() == -1;
   }

   public Component getComponent() {
      return this.jc;
   }

   public void setAllowEditing(boolean var1) {
      this.jc.setEditable(this.allowEditing = var1);
   }

   public boolean getAllowEditing() {
      return this.allowEditing;
   }

   public Object getCurrentUIValue() {
      if (this.allowEditing) {
         String var2 = this.editor.getText();
         if (var2 != null) {
            var2 = var2.trim();
            if (var2.length() == 0) {
               var2 = null;
            }
         }

         return var2;
      } else {
         Object var1 = this.jc.getSelectedItem();
         if (var1 != null) {
            return var1;
         } else {
            this.jc.setSelectedIndex(-1);
            return this.jc.getSelectedItem();
         }
      }
   }

   public void setSelectFirstElement(boolean var1) {
      this.selectFirstElement = var1;
   }

   public boolean getSelectFirstElement() {
      return this.selectFirstElement;
   }

   public void setCurrentUIValue(Object var1) {
      this.jc.setSelectedItem(var1);
      if (this.jc.getSelectedItem() == null) {
         if (this.getSelectFirstElement() && this.jc.getItemCount() > 0) {
            this.jc.setSelectedIndex(0);
         } else {
            this.jc.setSelectedIndex(-1);
         }
      }

   }
}
