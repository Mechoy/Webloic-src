package weblogic.tools.ui;

import javax.swing.JPanel;

public abstract class ModelPanel extends JPanel {
   protected boolean dirty;

   public boolean isDirty() {
      return this.dirty;
   }

   public void setDirty(boolean var1) {
      this.dirty = var1;
   }

   public abstract void modelToUI();

   public abstract void uiToModel();

   public void setEditingBean(Object var1) {
   }
}
