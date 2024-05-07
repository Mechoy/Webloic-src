package weblogic.tools.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;

public class NumberProperty extends Property implements PropertyChangeListener {
   NumberBox nbox;

   public NumberProperty(Object var1, PropertyDescriptor var2, String var3) {
      super(var1, var2, var3);
      this.getComponent();
   }

   public NumberProperty(PropertyDescriptor var1, String var2) {
      this((Object)null, var1, var2);
   }

   public Component getComponent() {
      if (this.nbox == null) {
         this.nbox = new NumberBox(0, Integer.MAX_VALUE, 0);
         this.nbox.addPropertyChangeListener(this);
      }

      return this.nbox;
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1.getSource() == this.nbox && "value".equalsIgnoreCase(var1.getPropertyName()) && this.isAutoCommit()) {
         this.uiToModel();
      }

   }

   public void setMin(int var1) {
      if (var1 != -1) {
         this.nbox.setMin(var1);
      }

   }

   public void setMax(int var1) {
      if (var1 != -1) {
         this.nbox.setMax(var1);
      }

   }

   public void setIncrement(int var1) {
      if (var1 != -1) {
         this.nbox.setIncrement(var1);
      }

   }

   public Object getCurrentUIValue() {
      int var1 = 0;
      if (this.nbox != null) {
         var1 = this.nbox.getValue();
      }

      return new Integer(var1);
   }

   public void setCurrentUIValue(Object var1) {
      Integer var2 = (Integer)var1;
      this.getComponent();
      this.nbox.setValue(var2);
   }
}
