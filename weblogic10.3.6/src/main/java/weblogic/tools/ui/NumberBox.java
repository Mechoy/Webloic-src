package weblogic.tools.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class NumberBox extends JPanel implements ActionListener, KeyListener, FocusListener, InputMethodListener {
   public static String PROPERTY_VALUE = "Value";
   JTextField tf;
   ArrowButton up;
   ArrowButton down;
   private int min;
   private int max;
   private int val;
   private int inc;
   PropertyChangeSupport pcs;
   private static final int BUTTON_WIDTH = 14;
   private static final int BUTTON_HEIGHT = 8;
   boolean inFL;

   public NumberBox(int var1, int var2, int var3, int var4) {
      this();
      this.min = var1;
      this.max = var2;
      this.setValue(var3);
      this.inc = var4;
      double var5 = (double)var2;
      int var7 = (int)Math.ceil(Math.log(var5) / Math.log(10.0));
      this.tf.setColumns(var7);
   }

   public NumberBox(int var1, int var2, int var3) {
      this(var1, var2, var3, 1);
   }

   public NumberBox() {
      super(new GridBagLayout());
      this.min = 0;
      this.max = Integer.MAX_VALUE;
      this.val = 0;
      this.inc = 1;
      this.pcs = null;
      this.inFL = false;
      this.init();
   }

   private void setNoDraw(int var1) {
      int var2 = this.val;
      this.val = var1;
      this.pcs.firePropertyChange(PROPERTY_VALUE, var2, this.val);
   }

   private void justSet(int var1) {
      this.setNoDraw(var1);
      String var2 = String.valueOf(this.val);
      this.tf.setText(var2);
      this.tf.setCaretPosition(var2.length());
   }

   public void setValue(int var1) {
      if (var1 <= this.max && var1 >= this.min) {
         this.justSet(var1);
      }

   }

   public int getValue() {
      return this.val;
   }

   public void setMin(int var1) {
      if (var1 < this.max) {
         this.min = var1;
         this.adjustToBounds();
      }

   }

   public int getMin() {
      return this.min;
   }

   public void setMax(int var1) {
      if (var1 > this.min) {
         this.adjustToBounds();
      }

      double var2 = (double)var1;
      int var4 = (int)Math.ceil(Math.log(var2) / Math.log(10.0));
      this.tf.setColumns(var4);
      this.doLayout();
   }

   public int getMax() {
      return this.max;
   }

   public void setIncrement(int var1) {
      this.inc = var1;
   }

   public int getIncrement() {
      return this.inc;
   }

   public void setTextBackground(Color var1) {
      this.tf.setBackground(var1);
   }

   protected void illegalValueHook() {
      String var1 = "A value between " + this.getMin() + " and " + this.getMax() + " must be entered.";
      JOptionPane.showMessageDialog(this, var1, "Illegal number", 2);
      if (this.getValue() < this.getMin()) {
         this.setValue(this.getMin());
      } else if (this.getValue() < this.getMax()) {
         this.setValue(this.getMax());
      }

      this.requestFocus();
   }

   public void setEditable(boolean var1) {
      this.tf.setEditable(var1);
   }

   private void init() {
      this.pcs = new PropertyChangeSupport(this);
      this.setOpaque(false);
      GridBagLayout var1 = (GridBagLayout)this.getLayout();
      GridBagConstraints var2 = new GridBagConstraints();
      this.tf = UIFactory.getTextField("");
      this.tf.setColumns(10);
      Border var3 = this.tf.getBorder();
      this.setBorder(var3);
      this.tf.setBorder(new EmptyBorder(0, 0, 0, 0));
      this.tf.addKeyListener(this);
      this.tf.addFocusListener(this);
      this.tf.addActionListener(this);
      var2.fill = 2;
      var2.gridheight = 0;
      var2.gridx = var2.gridy = 0;
      var2.weightx = 0.0;
      var2.weighty = 0.0;
      var2.insets = new Insets(0, 0, 0, 0);
      var1.setConstraints(this.tf, var2);
      this.add(this.tf);
      this.up = new ArrowButton(1);
      var2.fill = 0;
      var2.gridheight = 1;
      var2.gridy = 0;
      var2.gridx = 1;
      var2.weightx = 0.0;
      this.up.addActionListener(this);
      this.up.addKeyListener(this);
      this.up.setFocusTraversable(false);
      var1.setConstraints(this.up, var2);
      this.add(this.up);
      var2.gridy = 1;
      this.down = new ArrowButton(5);
      this.down.addActionListener(this);
      this.down.addKeyListener(this);
      this.down.setFocusTraversable(false);
      var1.setConstraints(this.down, var2);
      this.add(this.down);
      Dimension var4 = new Dimension(14, 8);
      this.up.setPreferredSize(var4);
      this.down.setPreferredSize(var4);
   }

   public void doLayout() {
      Dimension var1 = this.getSize();
      int var2 = 0;
      int var3 = 0;
      int var4 = var1.width;
      int var5 = var1.height;
      Border var6 = this.getBorder();
      if (var6 != null) {
         Insets var7 = var6.getBorderInsets(this);
         var2 += var7.left;
         var3 += var7.top;
         var4 -= var7.right;
         var5 -= var7.bottom;
      }

      Dimension var13 = this.up.getPreferredSize();
      Dimension var8 = this.tf.getPreferredSize();
      int var9 = var4 - var13.width;
      int var11 = var13.width;
      int var12 = (var5 - var3) / 2;
      this.up.setBounds(var9, var3, var11, var12);
      int var10 = var3 + var12;
      this.down.setBounds(var9, var10, var11, var12);
      var11 = var4 - var11 - var2;
      var12 = var5 - var3;
      this.tf.setBounds(var2, var3, var11, var12);
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      this.pcs.addPropertyChangeListener(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      this.pcs.removePropertyChangeListener(var1);
   }

   public void setEnabled(boolean var1) {
      this.tf.setEnabled(var1);
      this.up.setEnabled(var1);
      this.down.setEnabled(var1);
      super.setEnabled(var1);
   }

   public void setToolTipText(String var1) {
      this.tf.setToolTipText(var1);
      this.up.setToolTipText(var1);
      this.down.setToolTipText(var1);
      super.setToolTipText(var1);
   }

   public void requestFocus() {
      this.tf.requestFocus();
   }

   private void adjustToBounds() {
      if (this.val > this.min) {
         this.justSet(this.min);
      } else if (this.val > this.max) {
         this.justSet(this.max);
      }

   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 == this.up) {
         if (this.val + this.inc <= this.max && this.val + this.inc >= this.min) {
            this.justSet(this.val + this.inc);
         }
      } else if (var2 == this.down) {
         if (this.val - this.inc >= this.min && this.val - this.inc <= this.max) {
            this.justSet(this.val - this.inc);
         }
      } else if (var2 == this.tf && (this.getValue() > this.getMax() || this.getValue() < this.getMin())) {
         this.illegalValueHook();
      }

   }

   public void focusGained(FocusEvent var1) {
   }

   public void focusLost(FocusEvent var1) {
      if (!this.inFL) {
         try {
            this.inFL = true;
            String var2 = this.tf.getText();
            if (var2 != null && var2.length() != 0) {
               this.justSet(this.getInt(var2));
            } else {
               this.justSet(this.getMin());
            }

            if (this.getValue() > this.getMax() || this.getValue() < this.getMin()) {
               this.illegalValueHook();
            }
         } finally {
            this.inFL = false;
         }

      }
   }

   public void keyPressed(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 38) {
         this.justSet(this.getValue() + this.inc);
         var1.consume();
      } else if (var2 == 40) {
         this.justSet(this.getValue() - this.inc);
         var1.consume();
      }

   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyTyped(KeyEvent var1) {
      int var2 = this.tf.getCaretPosition();
      char var3 = var1.getKeyChar();
      String var4;
      if (var3 == '-') {
         var4 = this.tf.getText();
         if ("".equals(var4) || "0".equals(var4)) {
            this.tf.setText("-");
         }
      } else if (var3 != '\b' && var3 != 127) {
         if (Character.isDigit(var3)) {
            var4 = this.tf.getText();
            String[] var10 = this.clearSelection();
            String var11 = var10[0] + var3 + var10[1];

            try {
               this.getInt(var11);
               this.tf.setText(var11);
            } catch (NumberFormatException var8) {
               var1.consume();
               return;
            }

            var2 = var10[0].length() + 1;
            int var12 = this.tf.getText().length();
            if (var2 > var12) {
               var2 = var12;
            }

            this.tf.setCaretPosition(var2);
         }
      } else {
         int var9 = this.tf.getSelectionStart();
         int var5 = this.tf.getSelectionEnd();
         String[] var6 = this.clearSelection();
         if (var9 >= var5 && var6[0].length() > 0) {
            var6[0] = var6[0].substring(0, var6[0].length() - 1);
         }

         String var7 = var6[0] + var6[1];
         this.tf.setText(var7);
         this.tf.setCaretPosition(var6[0].length());
      }

      var1.consume();
   }

   private String[] clearSelection() {
      String[] var1 = new String[2];
      String var2 = this.tf.getText();
      int var3 = this.tf.getCaretPosition();
      int var4 = this.tf.getSelectionStart();
      int var5 = this.tf.getSelectionEnd();
      if (var4 >= var5) {
         var1[0] = var2.substring(0, var3);
         var1[1] = var2.substring(var3);
         return var1;
      } else {
         var1[0] = var2.substring(0, var4);
         var1[1] = var2.substring(var5);
         var2 = var1[0] + var1[1];
         this.tf.setText(var2);
         var5 = Math.min(var5, var2.length());
         this.tf.setCaretPosition(var5);
         return var1;
      }
   }

   public void caretPositionChanged(InputMethodEvent var1) {
   }

   public void inputMethodTextChanged(InputMethodEvent var1) {
   }

   private int getInt(String var1) {
      int var2 = 0;
      if (var1 != null && (var1 = var1.trim()).length() > 0) {
         if (var1.equals("-")) {
            return 0;
         }

         long var3 = Long.parseLong(var1);
         if (var3 > 2147483647L || var3 < -2147483648L) {
            throw new NumberFormatException("out of range: " + var1);
         }

         var2 = Integer.parseInt(var1);
      }

      return var2;
   }

   public static void main(String[] var0) {
      Util.initLookAndFeel("win");
      JFrame var1 = new JFrame("Number Box Test");
      NumberBox var2 = new NumberBox();
      var2.setMin(0);
      var2.setMax(1000);
      var2.setValue(50);
      Container var3 = var1.getContentPane();
      GridBagLayout var4 = new GridBagLayout();
      GridBagConstraints var5 = new GridBagConstraints();
      var3.setLayout(var4);
      var5.insets = new Insets(5, 5, 5, 5);
      var5.gridx = 0;
      var5.gridy = 0;
      var5.gridwidth = 1;
      JLabel var6 = new JLabel("min " + var2.getMin());
      var4.setConstraints(var6, var5);
      var3.add(var6);
      var5.gridx = 1;
      var6 = new JLabel("max " + var2.getMax());
      var4.setConstraints(var6, var5);
      var3.add(var6);
      var5.gridx = 0;
      var5.gridy = 1;
      var5.fill = 1;
      var4.setConstraints(var2, var5);
      var3.add(var2);
      WindowAdapter var7 = new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            System.exit(0);
         }
      };
      var1.addWindowListener(var7);
      var1.setSize(300, 300);
      var1.setVisible(true);
   }

   private void p(String var1) {
      System.err.println("[NumBox (min=" + this.getMin() + " val=" + this.getValue() + ")]: " + var1);
   }
}
