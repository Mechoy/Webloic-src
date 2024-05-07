package weblogic.tools.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import weblogic.utils.StackTraceUtils;

public class ExceptionDialog extends JDialog implements ActionListener, WindowListener, KeyListener {
   private static MarathonTextFormatter fmt = new MarathonTextFormatter();
   JLabel m_messageLabel;
   JButton ok;
   JButton details;
   ExArea ex;
   JScrollPane sp;

   public static void showExceptionDialog(Component var0, String var1, Throwable var2, boolean var3) {
      String var4;
      if (var3) {
         var4 = var2.toString();
      } else {
         var4 = var2.getClass().getName();
      }

      JOptionPane.showMessageDialog(var0, var4, var1, 2);
   }

   public ExceptionDialog(Frame var1, String var2, Throwable var3) {
      this(var1, var2, var3, (String)null, false);
   }

   public ExceptionDialog(Frame var1, String var2, Throwable var3, String var4) {
      this(var1, var2, var3, var4, false);
   }

   public void show() {
      this.ok.requestFocus();
      super.show();
   }

   public ExceptionDialog(Frame var1, String var2, Throwable var3, String var4, boolean var5) {
      super(var1, var2, true);
      this.m_messageLabel = null;
      GridBagLayout var6 = new GridBagLayout();
      GridBagConstraints var7 = new GridBagConstraints();
      var7.insets = new Insets(5, 5, 5, 5);
      this.getContentPane().setLayout(var6);
      URL var8 = getResourceURL("/com/sun/java/swing/plaf/windows/icons/Error.gif");
      String var9 = null;
      if (var5) {
         var9 = var3.toString();
      } else {
         var9 = var3.getClass().getName();
      }

      if (var8 != null) {
         this.m_messageLabel = new JLabel(var9, new ImageIcon(var8), 2);
         this.m_messageLabel.setIconTextGap(11);
      } else {
         this.m_messageLabel = new JLabel(var9);
      }

      var7.gridy = 0;
      var7.gridwidth = 2;
      var7.anchor = 10;
      var7.weightx = 1.0;
      var7.fill = 2;
      var7.anchor = 17;
      var6.setConstraints(this.m_messageLabel, var7);
      this.getContentPane().add(this.m_messageLabel);
      ++var7.gridy;
      var7.gridwidth = 0;
      JComponent var10 = this.makeButtons();
      var6.setConstraints(var10, var7);
      this.getContentPane().add(var10);
      ++var7.gridy;
      var7.anchor = 16;
      var7.insets = new Insets(0, 0, 0, 0);
      var7.fill = 1;
      var7.weightx = var7.weighty = 1.0;
      this.ex = new ExArea(StackTraceUtils.throwable2StackTrace(var3));
      this.ex.setVisible(false);
      EmptyBorder var12 = new EmptyBorder(11, 11, 11, 11) {
         public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
            Color var7 = var2.getColor();
            var2.setColor(ExceptionDialog.this.getBackground());
            var2.translate(var3, var4);
            var2.fillRect(var3, var4, var5, this.top);
            var2.fillRect(var3, var4, this.left, var6);
            var2.fillRect(var3, var6 - this.bottom, var5, this.bottom);
            var2.fillRect(var5 - this.right, var4, this.right, var6);
            var2.setColor(var7);
         }
      };
      CompoundBorder var13 = new CompoundBorder(var12, new BevelBorder(1));
      final ExArea var14 = this.ex;
      this.sp = new JScrollPane(this.ex) {
         public Dimension getPreferredSize() {
            Dimension var1;
            if (var14.isVisible()) {
               JScrollBar var2 = this.getHorizontalScrollBar();
               int var3 = var2.getPreferredSize().height;
               var1 = new Dimension(var14.W, var14.H + 2 * var3);
            } else {
               var1 = new Dimension(var14.W, 0);
            }

            return var1;
         }

         public Dimension getMaximumSize() {
            return this.getPreferredSize();
         }
      };
      this.sp.setBorder(var13);
      this.getContentPane().add(this.sp);
      this.setResizable(false);
      this.addWindowListener(this);
      this.pack();
      int var15 = this.sp.getPreferredSize().width;
      Dimension var16 = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension var17 = this.getPreferredSize();
      var15 = (var16.width - var17.width) / 2;
      int var18 = Math.max((var16.height - var17.height) / 2 - 100, 100);
      this.setLocation(new Point(var15, var18));
   }

   public void setMessage(String var1) {
      this.m_messageLabel.setText(var1);
   }

   private JComponent makeButtons() {
      JPanel var1 = new JPanel(new GridBagLayout());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(2, 2, 2, 2);
      var2.anchor = 17;
      var2.weightx = 1.0;
      var2.gridx = 0;
      var2.gridy = 0;
      var2.fill = 2;
      var1.add(new JLabel(""), var2);
      ++var2.gridx;
      var2.anchor = 13;
      var2.weightx = 0.0;
      var2.fill = 0;
      this.ok = new JButton(fmt.getContinue());
      this.ok.addKeyListener(this);
      this.ok.setOpaque(false);
      this.ok.addActionListener(this);
      var1.add(this.ok, var2);
      ++var2.gridx;
      this.details = new JButton(fmt.getShowDetails());
      this.details.setOpaque(false);
      this.details.addActionListener(this);
      var1.add(this.details, var2);
      return var1;
   }

   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      int var2 = this.sp.getPreferredSize().width;
      if (!this.ex.isVisible()) {
         var2 += 6;
      }

      var1.width = Math.max(var1.width, var2);
      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.ok) {
         this.setVisible(false);
         this.dispose();
      } else if (var2 == this.details) {
         if (!this.ex.isVisible()) {
            this.details.setLabel(fmt.getHideDetails());
            this.ex.setVisible(true);
         } else {
            this.details.setLabel(fmt.getShowDetails());
            this.ex.setVisible(false);
         }

         this.invalidate();
         this.pack();
      }

   }

   public void keyTyped(KeyEvent var1) {
      if (var1.getSource() == this.ok) {
         if (var1.getKeyCode() == 10 || var1.getKeyChar() == '\n' || var1.getKeyChar() == '\r') {
            this.setVisible(false);
            this.dispose();
         }
      }
   }

   public void keyPressed(KeyEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      this.setVisible(false);
      this.dispose();
   }

   public void windowClosed(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public static URL getResourceURL(String var0) {
      URL var1 = ExceptionDialog.class.getResource(var0);
      if (var1 == null) {
         var1 = ExceptionDialog.class.getResource("/weblogic/graphics/" + var0);
      }

      return var1;
   }

   public static void main(String[] var0) throws Exception {
      Util.initLookAndFeel("win");
      String[] var1 = new String[]{"Foo", "Bar", "Baz"};
      JFrame var2 = new JFrame("test");
      JList var3 = new JList();
      var3.setListData(var1);
      var2.getContentPane().add(var3);
      var2.setSize(300, 300);
      var2.setVisible(true);

      try {
         deep(0, 20);
      } catch (Exception var6) {
         ExceptionDialog var5 = new ExceptionDialog(var2, "Error", var6);
         var5.show();
      }

   }

   private static void deep(int var0, int var1) throws Exception {
      ++var0;
      if (var0 >= var1) {
         String var2 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD";
         throw new Exception(var2);
      } else {
         deep(var0, var1);
      }
   }
}
