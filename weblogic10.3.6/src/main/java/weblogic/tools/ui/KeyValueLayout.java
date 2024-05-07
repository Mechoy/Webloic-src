package weblogic.tools.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import weblogic.utils.Debug;

public class KeyValueLayout implements LayoutManager {
   public static final String KEY = "Key";
   public static final String VALUE = "Value";
   public static final String CHOOSER = "Chooser";
   private boolean m_verbose = false;
   public static int VERTICAL_GAP = 5;
   public static int TOP_GAP = 5;
   public static int BOTTOM_GAP = 15;
   public static int LEFT_GAP = 10;
   public static int RIGHT_GAP = 10;
   public static int LABEL_WIDGET_OFFSET = 6;
   public static int CHOOSER_GAP = 5;
   private int HEIGHT = 20;
   private HashMap m_components = new HashMap();
   private ComponentDescription m_currentComponentDescription = null;

   public void layoutContainer(Container var1) {
      Insets var2 = var1.getInsets();
      int var3 = LEFT_GAP + var2.left;
      int var4 = TOP_GAP + var2.top;
      boolean var5 = true;
      boolean var6 = true;
      int var7 = (int)var1.getSize().getWidth() - LEFT_GAP - RIGHT_GAP - var2.left - var2.right;
      int var8 = this.HEIGHT;
      int var9 = this.HEIGHT;
      int var10 = var1.countComponents();
      String var11 = null;

      for(int var12 = 0; var12 < var10; ++var12) {
         Component var13 = var1.getComponent(var12);
         var8 = (int)var13.getPreferredSize().getHeight();
         if (var12 + 1 < var10) {
            var11 = (String)this.m_components.get(var1.getComponent(var12 + 1));
         }

         String var14 = (String)this.m_components.get(var13);
         Debug.assertion(null != var14, "KeyValueLayout: no constraint was specified for component " + var13.getClass());
         if ("Key".equals(var14)) {
            this.resizeComponent(var13, var14, var3, var4, (double)var7, (double)var8);
            var4 += var8 + VERTICAL_GAP - LABEL_WIDGET_OFFSET;
         } else if ("Value".equals(var14)) {
            var9 = var8;
            if ("Chooser".equals(var11)) {
               JComponent var15 = (JComponent)var1.getComponent(var12 + 1);
               int var16 = (int)var15.getPreferredSize().getWidth();
               this.resizeComponent(var13, var14, var3, var4, (double)(var7 - var16 - CHOOSER_GAP), (double)var8);
            } else {
               this.resizeComponent(var13, var14, var3, var4, (double)var7, (double)var8);
               var11 = null;
               var4 += var8 + VERTICAL_GAP;
            }
         } else {
            int var17 = (int)var13.getPreferredSize().getWidth();
            this.resizeComponent(var13, var14, var3 + var7 - var17, var4, (double)var17, (double)var9);
            var4 += var9 + VERTICAL_GAP;
         }
      }

   }

   private void resizeComponent(Component var1, String var2, int var3, int var4, double var5, double var7) {
      var1.setBounds(var3, var4, (int)var5, (int)var7);
   }

   public Dimension preferredLayoutSize(Container var1) {
      Dimension var2 = this.minimumLayoutSize(var1);
      return var2;
   }

   private int getMinimumHeight(Container var1, int var2) {
      boolean var3 = false;
      int var4 = 0;
      int var5 = var1.countComponents();

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         Component var7 = var1.getComponent(var6);
         String var8 = (String)this.m_components.get(var7);
         if (!"Chooser".equals(var8)) {
            var4 += (int)var7.getPreferredSize().getHeight();
         }

         if (var7 instanceof JComponent) {
            JComponent var9 = (JComponent)var7;
            Border var10 = var9.getBorder();
            if (var10 != null) {
               Insets var11 = var10.getBorderInsets(var9);
               if (var11 != null) {
                  var4 += var11.top;
                  var4 += var11.bottom;
               }
            }
         }
      }

      var6 = var4;
      Insets var12 = var1.getInsets();
      if (var1 instanceof JComponent) {
         Border var13 = ((JComponent)var1).getBorder();
         if (null != var13) {
            Insets var14 = var13.getBorderInsets(var1);
            if (null != var14) {
               var6 = var4 + var14.top + var14.bottom;
            }
         }
      }

      return var6 + BOTTOM_GAP + TOP_GAP;
   }

   public Dimension minimumLayoutSize(Container var1) {
      int var2 = var1.countComponents();
      Dimension var3 = new Dimension(100, this.getMinimumHeight(var1, var2));
      return var3;
   }

   public void addLayoutComponent(String var1, Component var2) {
      this.m_components.put(var2, var1);
   }

   public void removeLayoutComponent(Component var1) {
   }

   private void log(String var1) {
      if (this.m_verbose) {
         System.out.println("[KeyValueLayout] " + var1);
      }

   }

   private static void ppp(String var0) {
      System.out.println("[KeyValueLayout] " + var0);
   }

   public static void main(String[] var0) {
      JFrame var1 = new JFrame("Test KeyValue");
      JPanel var2 = new JPanel(new KeyValueLayout());
      JLabel var3 = new JLabel("Last Name:");
      JTextField var4 = new JTextField(30);
      new JButton("...");
      var2.add(var3, "Key");
      var2.add(var4, "Value");
      var2.add(new JLabel("First name"), "Key");
      var2.add(new JComboBox(), "Value");
      var2.add(new JLabel("First name"), "Key");
      var2.add(new JComboBox(), "Value");
      var2.add(new JLabel("First name"), "Key");
      var2.add(new JComboBox(), "Value");
      var2.add(new JCheckBox("Toggle"), "Key");

      for(int var6 = 0; var6 < 9; ++var6) {
         var2.add(new JLabel("Age"), "Key");
         var2.add(new NumberBox(1, 100, 30), "Value");
      }

      var2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "Test"));
      var1.getContentPane().add(var2);
      var1.pack();
      var1.setVisible(true);
   }
}
