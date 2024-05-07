package weblogic.tools.ui;

import java.awt.Component;
import java.beans.PropertyDescriptor;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class StringProperty extends Property {
   JTextField text;
   boolean inui2model;
   boolean emptyIsNull;
   JComponent focusComponent;

   public StringProperty(Object var1, PropertyDescriptor var2, String var3, boolean var4) {
      super(var1, var2, var3, var4);
      this.emptyIsNull = true;
      this.getComponent();
   }

   public StringProperty(PropertyDescriptor var1, String var2, boolean var3) {
      this((Object)null, var1, var2, var3);
   }

   public boolean isUIEmpty() {
      if (this.text == null) {
         return false;
      } else {
         String var1 = this.text.getText();
         return var1 == null || var1.trim().length() == 0;
      }
   }

   public void setAutoCommit(boolean var1) {
      super.setAutoCommit(var1);
   }

   public void uiToModel() {
      if (this.isRequired() && !this.isAutoCommit()) {
         if (!this.inui2model) {
            try {
               this.inui2model = true;
               String var1 = (String)this.getCurrentUIValue();
               if (var1 != null && var1.trim().length() != 0) {
                  super.uiToModel();
               } else {
                  Object var2 = this.invokeGetter();
                  this.setCurrentUIValue(var2);
                  String var3 = "Invalid field";
                  String var4 = "\"" + this.getLabel().getText() + "\" must be non-empty";
                  JOptionPane.showMessageDialog(this.text.getParent(), var4, var3, 0);
                  if (this.focusComponent == null) {
                     this.text.requestFocus();
                  } else {
                     this.focusComponent.requestFocus();
                  }
               }
            } finally {
               this.inui2model = false;
            }

         }
      } else {
         super.uiToModel();
      }
   }

   public void setFocusComponent(JComponent var1) {
      this.focusComponent = var1;
   }

   public Component getComponent() {
      if (this.text == null) {
         this.text = new JTextField();
      }

      return this.text;
   }

   public Object getCurrentUIValue() {
      String var1 = this.text.getText();
      if (var1 != null && this.emptyIsNull) {
         if ((var1 = var1.trim()).length() == 0) {
            var1 = null;
         }

         return var1;
      } else {
         return var1;
      }
   }

   public void setCurrentUIValue(Object var1) {
      if (var1 == null) {
         var1 = "";
      }

      this.text.setText(var1.toString());
   }

   public void setEmptyIsNull(boolean var1) {
      this.emptyIsNull = var1;
   }

   public boolean getEmptyIsNull() {
      return this.emptyIsNull;
   }
}
