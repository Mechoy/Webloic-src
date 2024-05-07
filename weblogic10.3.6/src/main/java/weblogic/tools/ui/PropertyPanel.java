package weblogic.tools.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class PropertyPanel extends BeanRowEditor implements LayoutManager {
   PropertySet ps;
   private JComponent[] comps;
   private Insets insets = new Insets(5, 5, 5, 5);

   static void p(String var0) {
      System.err.println("[PPanel]: " + var0);
   }

   protected PropertyPanel() {
   }

   public PropertyPanel(PropertySet var1) {
      this.ps = var1;
      this.layoutProps();
   }

   public PropertyPanel(Class var1, PropertyInfo[] var2) {
      this.ps = new PropertySet(var1, var2);
      this.layoutProps();
   }

   public PropertyPanel(Object var1, Class var2, Object[][] var3) {
      this.ps = new PropertySet(var1, var2, var3);
      this.layoutProps();
   }

   public Property findPropByName(String var1) {
      return this.ps.findPropByName(var1);
   }

   public void setAutoCommit(boolean var1) {
      this.ps.setAutoCommit(var1);
   }

   public JComponent getFirstFocusComponent() {
      JComponent var1 = null;
      if (this.ps != null) {
         Property[] var2 = this.ps.getProps();
         if (var2.length > 0) {
            var1 = (JComponent)var2[0].getComponent();
         }
      }

      return var1;
   }

   public void setEditingBean(Object var1) {
      this.ps.setBean(var1);
   }

   public IValidationFeedback getFeedback() {
      ValidationFeedback var1 = null;
      Property[] var2 = this.ps.getProps();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         Property var4 = var2[var3];
         if (var4.isRequired() && var4.isUIEmpty()) {
            String var5 = '"' + deAnnotate(var4.getLabel().getText()) + "\" must be entered";
            var1 = new ValidationFeedback(var5, var4.getComponent());
            break;
         }
      }

      return var1;
   }

   public static String deAnnotate(String var0) {
      if (var0.startsWith("*")) {
         var0 = var0.substring(1);
      }

      if (var0.endsWith(":")) {
         var0 = var0.substring(0, var0.length() - 1);
      }

      return var0.trim();
   }

   public void modelToUI() {
      this.ps.modelToUI();
   }

   public Object createNewBean() {
      return this.ps.createNewBean();
   }

   public void uiToModel() {
      this.ps.uiToModel();
   }

   private void layoutPropsKeyValuePanel() {
      Property[] var1 = this.ps.getProps();
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Property var4 = var1[var3];
         if (var4.hasSeparateLabel()) {
            var2.add(var4.getLabel());
         } else {
            var2.add(UIFactory.getLabel(""));
         }

         var2.add(var1[var3].getComponent());
      }

      JComponent[] var5 = new JComponent[var2.size()];
      var2.toArray(var5);
   }

   private static void ppp(String var0) {
      System.out.println("[PropertyPanel] " + var0);
   }

   private void layoutProps() {
      this.layoutPropsKeyValuePanel();
   }

   public void addLayoutComponent(String var1, Component var2) {
   }

   public void layoutContainer(Container var1) {
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int var4 = this.insets.top;
      Border var5 = this.getBorder();
      if (var5 != null) {
         Insets var6 = var5.getBorderInsets(this);
         if (var6 != null) {
            var4 += var6.top;
         }
      }

      int var10 = var2 - this.insets.left - this.insets.right;

      for(int var7 = 0; var7 < this.comps.length; ++var7) {
         Dimension var8 = this.comps[var7].getPreferredSize();
         int var9 = var10;
         if (this.comps[var7] instanceof JComboBox || this.comps[var7] instanceof NumberBox) {
            var9 = var8.width;
         }

         if (!(this.comps[var7] instanceof JTextField)) {
            var4 += this.insets.top;
         }

         this.comps[var7].setBounds(this.insets.left, var4, var9, var8.height);
         var4 += var8.height;
      }

   }

   public Dimension minimumLayoutSize(Container var1) {
      return this.preferredLayoutSize(var1);
   }

   public Dimension preferredLayoutSize(Container var1) {
      int var2 = 100;
      int var3 = this.insets.top;
      Border var4 = this.getBorder();
      if (var4 != null) {
         Insets var5 = var4.getBorderInsets(this);
         if (var5 != null) {
            var3 += var5.top;
            var3 += var5.bottom;
         }
      }

      for(int var7 = 0; var7 < this.comps.length; ++var7) {
         Dimension var6 = this.comps[var7].getPreferredSize();
         var2 = Math.max(var2, var6.width);
         var3 += this.insets.top;
         var3 += var6.height;
      }

      return new Dimension(var2, var3);
   }

   public void removeLayoutComponent(Component var1) {
   }

   public JComponent getComponent() {
      return this;
   }
}
