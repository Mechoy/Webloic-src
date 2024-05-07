package weblogic.tools.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public abstract class ModelTabPanel extends BeanRowEditor {
   JTabbedPane jtp = new JTabbedPane();

   public ModelTabPanel() {
      this.setLayout(new GridBagLayout());
      GridBagConstraints var1 = new GridBagConstraints();
      var1.fill = 1;
      var1.weightx = var1.weighty = 1.0;
      this.add(this.jtp, var1);
   }

   public JTabbedPane getTabPane() {
      return this.jtp;
   }

   public void add(String var1, JComponent var2) {
      byte var3 = 2;
      var2.setBorder(new EmptyBorder(var3, var3, var3, var3));
      this.jtp.add(var1, var2);
   }

   public JComponent getFirstFocusComponent() {
      JComponent var1 = null;
      int var2 = this.jtp.getTabCount();
      boolean var3 = false;

      for(int var5 = 0; var1 == null && var5 < var2; ++var5) {
         Component var4 = this.jtp.getComponentAt(var5);
         if (var4 instanceof IBeanRowEditor) {
            var1 = ((IBeanRowEditor)var4).getFirstFocusComponent();
            this.jtp.setSelectedIndex(var5);
         }
      }

      if (var1 == null) {
         this.jtp.setSelectedIndex(0);
      }

      return var1;
   }

   public IValidationFeedback getFeedback() {
      IValidationFeedback var1 = super.getFeedback();
      if (var1 != null) {
         return var1;
      } else {
         int var2 = this.jtp.getTabCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            Component var4 = this.jtp.getComponentAt(var3);
            if (var4 instanceof IBeanRowEditor) {
               IBeanRowEditor var5 = (IBeanRowEditor)var4;
               var1 = var5.getFeedback();
               if (var1 != null) {
                  this.jtp.setSelectedIndex(var3);
                  break;
               }
            }
         }

         return var1;
      }
   }

   public void setAutoCommit(boolean var1) {
      super.setAutoCommit(var1);
      int var2 = this.jtp.getTabCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         Component var4 = this.jtp.getComponentAt(var3);
         if (var4 instanceof IBeanRowEditor) {
            ((IBeanRowEditor)var4).setAutoCommit(var1);
         }
      }

   }

   public void modelToUI() {
      int var1 = this.jtp.getTabCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         Component var3 = this.jtp.getComponentAt(var2);
         if (var3 instanceof ModelPanel) {
            ModelPanel var4 = (ModelPanel)var3;
            var4.modelToUI();
         }
      }

   }

   public void uiToModel() {
      int var1 = this.jtp.getTabCount();

      for(int var2 = 0; var2 < var1; ++var2) {
         Component var3 = this.jtp.getComponentAt(var2);
         if (var3 instanceof ModelPanel) {
            ModelPanel var4 = (ModelPanel)var3;
            var4.uiToModel();
         } else if (var3 instanceof IBeanRowEditor) {
            ((IBeanRowEditor)var3).uiToModel();
         }
      }

   }

   public abstract void setEditingBean(Object var1);

   public abstract Object createNewBean();

   private static void p(String var0) {
      System.err.println("[ModelTabPanel]: " + var0);
   }
}
