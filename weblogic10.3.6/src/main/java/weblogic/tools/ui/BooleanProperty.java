package weblogic.tools.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import javax.swing.JCheckBox;

public class BooleanProperty extends Property implements ActionListener {
   JCheckBox bool;

   public BooleanProperty(Object var1, PropertyDescriptor var2, String var3) {
      super(var1, var2, var3);
   }

   public BooleanProperty(PropertyDescriptor var1, String var2) {
      this((Object)null, var1, var2);
   }

   public boolean hasSeparateLabel() {
      return false;
   }

   public Component getComponent() {
      if (this.bool == null) {
         this.bool = new JCheckBox(this.label.getText());
         this.bool.addActionListener(this);
      }

      return this.bool;
   }

   public Object getCurrentUIValue() {
      return new Boolean(this.bool.isSelected());
   }

   public void setCurrentUIValue(Object var1) {
      this.bool.setSelected("true".equals(var1.toString()));
   }

   public void actionPerformed(ActionEvent var1) {
      if (this.autoCommit && var1 != null && var1.getSource() == this.getComponent()) {
         this.uiToModel();
      }

   }
}
