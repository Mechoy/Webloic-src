package weblogic.tools.ui;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import javax.swing.JComboBox;

public class ObjectProperty extends Property {
   JComboBox jc;
   private boolean allowNull;
   private static final Object NULL_OBJECT = new Object() {
      public String toString() {
         return "";
      }
   };

   public ObjectProperty(Object var1, PropertyDescriptor var2, String var3, Object[] var4, boolean var5) {
      super(var1, var2, var3, var5);
      this.allowNull = false;
      this.jc = new SortedComboBox(var4);
      this.jc.addFocusListener(this);
   }

   public ObjectProperty(PropertyDescriptor var1, String var2, Object[] var3, boolean var4) {
      this((Object)null, var1, var2, var3, var4);
   }

   public boolean isUIEmpty() {
      return this.jc.getSelectedIndex() == -1 || this.jc.getSelectedItem() == NULL_OBJECT;
   }

   public Component getComponent() {
      return this.jc;
   }

   public void setAllowNull(boolean var1) {
      if (var1 != this.allowNull) {
         this.allowNull = var1;
         if (!this.allowNull) {
            this.jc.removeItemAt(0);
         } else {
            int var2 = this.jc.getItemCount();
            Object[] var3 = new Object[var2 + 1];
            var3[0] = NULL_OBJECT;

            for(int var4 = 1; var4 <= var2; ++var4) {
               var3[var4] = this.jc.getItemAt(var4);
            }

            this.setConstrained(var3);
         }

      }
   }

   public Object getCurrentUIValue() {
      Object var1 = this.jc.getSelectedItem();
      if (var1 == null && this.allowNull) {
         return null;
      } else if (var1 == NULL_OBJECT) {
         return null;
      } else if (var1 != null) {
         return var1;
      } else if (this.jc.getItemCount() == 0) {
         return null;
      } else {
         this.jc.setSelectedIndex(0);
         return this.jc.getSelectedItem();
      }
   }

   public void setCurrentUIValue(Object var1) {
      if (var1 == null) {
         this.jc.setSelectedIndex(-1);
      } else {
         this.jc.setSelectedItem(var1);
      }
   }

   public void setConstrained(Object[] var1) {
      this.jc.removeAllItems();
      if (this.allowNull) {
         this.jc.addItem(NULL_OBJECT);
      }

      for(int var2 = 0; var1 != null && var2 < var1.length; ++var2) {
         this.jc.addItem(var1[var2]);
      }

   }

   private static void p(String var0) {
      System.err.println("[ObjProp]: " + var0);
   }
}
