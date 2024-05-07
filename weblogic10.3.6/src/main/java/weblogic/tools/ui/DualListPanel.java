package weblogic.tools.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DualListPanel extends JPanel implements ActionListener, ListSelectionListener {
   static final String IMG_BASE_PATH = "/weblogic/tools/ui/graphics/";
   static final String LEFT_PATH = "/weblogic/tools/ui/graphics/arrow_left_enabled.gif";
   static final String RIGHT_PATH = "/weblogic/tools/ui/graphics/arrow_right_enabled.gif";
   private static final int DEFAULT_LIST_HEIGHT = 250;
   protected JList selected;
   protected JList total;
   protected JButton left;
   protected JButton right;
   protected JLabel selLabel;
   protected JLabel totalLabel;
   protected int m_listHeight = 250;
   private static final int LABEL_LIST_GAP = 2;
   private static final int OUTSIDE_GAP = 5;
   static String[] totalData = new String[]{"Larry", "Moe", "Curly", "Fred", "Wilma", "Barney", "Betty"};
   static String[] selData = new String[]{"Moe", "Curly", "Barney", "Shemp"};

   public DualListPanel(String var1, String var2) {
      this.add(this.selected = new JList());
      this.add(this.total = new JList());
      this.add(this.left = this.makeImageButton("/weblogic/tools/ui/graphics/arrow_left_enabled.gif"));
      this.add(this.right = this.makeImageButton("/weblogic/tools/ui/graphics/arrow_right_enabled.gif"));
      this.add(this.selLabel = new JLabel(var1));
      this.add(this.totalLabel = new JLabel(var2));
      this.selLabel.setLabelFor(this.selected);
      this.totalLabel.setLabelFor(this.total);
      this.selected.setBorder(new BevelBorder(1));
      this.total.setBorder(new BevelBorder(1));
      this.left.addActionListener(this);
      this.right.addActionListener(this);
      this.selected.addListSelectionListener(this);
      this.total.addListSelectionListener(this);
      this.total.setSelectionMode(0);
      this.selected.setSelectionMode(0);
      this.syncEnabled();
   }

   public Object[] getUnselected() {
      return getListData(this.total);
   }

   public Object[] getTotal() {
      Object[] var1 = this.getSelected();
      Object[] var2 = this.getUnselected();
      Object[] var3 = new Object[var1.length + var2.length];
      System.arraycopy(var1, 0, var3, 0, var1.length);
      System.arraycopy(var2, 0, var3, var1.length, var2.length);
      return var3;
   }

   public Object[] getSelected() {
      return getListData(this.selected);
   }

   public void setTotal(Object[] var1) {
      Object[] var2 = this.getSelected();
      Object[] var3 = computeLeftover(var2, var1);
      this.total.setListData(var3);
      Object[] var4 = removeBadSels(var2, var1);
      this.selected.setListData(var4);
   }

   public void setSelected(Object[] var1) {
      Object[] var2 = this.getTotal();
      Object[] var3 = computeLeftover(var1, var2);
      this.total.setListData(var3);
      Object[] var4 = removeBadSels(var1, var2);
      this.selected.setListData(var4);
   }

   protected void listsChanged() {
   }

   static Object[] removeBadSels(Object[] var0, Object[] var1) {
      if (var0 == null) {
         var0 = new Object[0];
      }

      if (var1 == null) {
         var1 = new Object[0];
      }

      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         boolean var4 = false;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var0[var3].equals(var1[var5])) {
               var4 = true;
               break;
            }
         }

         if (var4) {
            var2.add(var0[var3]);
         }
      }

      Object[] var6 = new Object[var2.size()];
      var2.toArray(var6);
      return var6;
   }

   static Object[] computeLeftover(Object[] var0, Object[] var1) {
      if (var0 == null) {
         var0 = new Object[0];
      }

      if (var1 == null) {
         var1 = new Object[0];
      }

      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         boolean var4 = true;

         for(int var5 = 0; var5 < var0.length; ++var5) {
            if (var1[var3].equals(var0[var5])) {
               var4 = false;
               break;
            }
         }

         if (var4) {
            var2.add(var1[var3]);
         }
      }

      Object[] var6 = new Object[var2.size()];
      var2.toArray(var6);
      return var6;
   }

   static Object[] getListData(JList var0) {
      ListModel var1 = var0.getModel();
      ArrayList var2 = new ArrayList();
      int var3 = var1.getSize();

      for(int var4 = 0; var4 < var3; ++var4) {
         var2.add(var1.getElementAt(var4));
      }

      Object[] var5 = new Object[var3];
      var2.toArray(var5);
      return var5;
   }

   private void syncEnabled() {
      this.right.setEnabled(!this.selected.isSelectionEmpty());
      this.left.setEnabled(!this.total.isSelectionEmpty());
   }

   public void actionPerformed(ActionEvent var1) {
      try {
         this.action(var1);
      } finally {
         this.syncEnabled();
      }

   }

   public void setListHeight(int var1) {
      this.m_listHeight = var1;
   }

   private void action(ActionEvent var1) {
      JList var2;
      JList var3;
      if (var1.getSource() == this.right) {
         var2 = this.selected;
         var3 = this.total;
      } else {
         var2 = this.total;
         var3 = this.selected;
      }

      int var4 = var2.getSelectedIndex();
      if (var4 != -1) {
         Object var5 = var2.getSelectedValue();
         var2.setListData(remove(getListData(var2), var4));
         var3.setListData(add(getListData(var3), var5));
         var2.clearSelection();
         var3.clearSelection();
         int var6 = var2.getModel().getSize();
         if (var4 >= var6) {
            var4 = var6 - 1;
         }

         var2.setSelectedIndex(var4);
         this.listsChanged();
      }
   }

   static Object[] remove(Object[] var0, int var1) {
      if (var1 >= 0 && var1 <= var0.length - 1) {
         Object[] var2 = new Object[var0.length - 1];
         System.arraycopy(var0, 0, var2, 0, var1);
         if (var1 != var2.length) {
            System.arraycopy(var0, var1 + 1, var2, var1, var2.length - var1);
         }

         return var2;
      } else {
         return var0;
      }
   }

   static Object[] add(Object[] var0, Object var1) {
      Object[] var2 = new Object[var0.length + 1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      var2[var0.length] = var1;
      return var2;
   }

   public void valueChanged(ListSelectionEvent var1) {
      this.syncEnabled();
   }

   public Dimension getPreferredSize() {
      return this.layout(false);
   }

   public void doLayout() {
      this.layout(true);
   }

   private Dimension layout(boolean var1) {
      Border var2 = this.getBorder();
      Insets var3 = null;
      if (var2 != null) {
         var3 = var2.getBorderInsets(this);
      } else {
         var3 = new Insets(0, 0, 0, 0);
      }

      int var4 = var3.left + 5;
      int var5 = var3.left + 5;
      int var6 = var3.top + 5;
      int var7 = var3.bottom + 5;
      Dimension var8 = null;
      int var9 = 100;
      int var10 = this.m_listHeight;
      var9 = Math.max(this.selLabel.getPreferredSize().width, var9);
      var9 = Math.max(this.totalLabel.getPreferredSize().width, var9);
      var8 = this.selected.getPreferredSize();
      var9 = Math.max(var9, var8.width);
      if (var10 == 250) {
         var10 = Math.max(var10, var8.height);
      }

      var8 = this.total.getPreferredSize();
      var9 = Math.max(var9, var8.width);
      if (var10 == 250) {
         var10 = Math.max(var10, var8.height);
      }

      var10 = Math.min(var10, 500);
      int var11 = this.selLabel.getPreferredSize().height;
      var11 = Math.max(this.totalLabel.getPreferredSize().height, var11);
      var8 = this.selLabel.getPreferredSize();
      if (var1) {
         this.selLabel.setBounds(var4, var6, var8.width, var8.height);
      }

      int var12 = var6 + var8.height + 2;
      if (var1) {
         this.selected.setBounds(var4, var12, var9, var10);
      }

      int var13 = this.left.getPreferredSize().width;
      var13 = Math.max(this.right.getPreferredSize().width, var13);
      var13 += 10;
      var8 = this.totalLabel.getPreferredSize();
      if (var1) {
         this.totalLabel.setBounds(var4 + var9 + var13, var6, var8.width, var8.height);
         this.total.setBounds(var4 + var9 + var13, var12, var9, var10);
      }

      byte var14 = 10;
      var8 = this.left.getPreferredSize();
      int var15 = var8.width;
      int var16 = var8.height;
      int var17 = var4 + var9 + 5;
      int var18 = var12 + var10 / 2 - var16 - var14 / 2;
      if (var1) {
         this.right.setBounds(var17, var18, var15, var16);
      }

      var18 = var12 + var10 / 2 + var14 / 2;
      if (var1) {
         this.left.setBounds(var17, var18, var15, var16);
         return null;
      } else {
         Dimension var19 = new Dimension();
         var19.width = 0;
         var19.width += var4;
         var19.width += var9 * 2;
         var19.width += var13;
         var19.width += var5;
         var19.height = 0;
         var19.height += var6;
         var19.height += var11;
         var19.height += 2;
         var19.height += var10;
         var19.height += var7;
         return var19;
      }
   }

   private static void p(String var0) {
      System.err.println("[DualListPanel]: " + var0);
   }

   private JButton makeImageButton(String var1) {
      ImageIcon var2 = new ImageIcon(Util.loadImage(var1));
      JButton var3 = new JButton(var2);
      Dimension var4 = new Dimension(var2.getIconWidth(), var2.getIconHeight());
      var3.setPreferredSize(var4);
      return var3;
   }

   public static void main(String[] var0) {
      JFrame var1 = new JFrame("test");
      DualListPanel var2 = new DualListPanel("Selected", "Available");
      var2.setTotal(totalData);
      var2.setSelected(selData);
      var2.setBorder(new TitledBorder("Widget Test"));
      var1.getContentPane().add(var2);
      var1.setLocation(100, 100);
      var1.pack();
      var1.setVisible(true);
   }
}
