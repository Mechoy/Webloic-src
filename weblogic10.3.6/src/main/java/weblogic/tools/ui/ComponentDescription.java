package weblogic.tools.ui;

import java.awt.Component;

class ComponentDescription {
   public Component key = null;
   public Component value = null;
   public Component chooser = null;

   public String toString() {
      return "[ComponentDescription key:" + this.key.getClass() + " val:" + this.value.getClass() + (null != this.chooser ? this.chooser.getClass().toString() : "");
   }
}
