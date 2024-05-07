package weblogic.servlet.ejb2jsp.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import weblogic.tools.ui.AWTUtils;

public class ArrayEditorDialog extends JDialog implements ActionListener {
   private String[] elements;
   private JTextField[] fields;
   private JTextField newField;
   private JButton[] buttons;
   private JButton addButton;
   private JButton okButton;
   private JButton cancelButton;
   private JPanel currentPanel;
   private JScrollPane scroll;
   private boolean trim;

   public ArrayEditorDialog(Frame var1, String var2, boolean var3, String[] var4) {
      this(var1, var2, var3, var4, true);
   }

   public ArrayEditorDialog(Frame var1, String var2, boolean var3, String[] var4, boolean var5) {
      super(var1, var2, var3);
      this.elements = var4;
      if (this.elements == null) {
         this.elements = new String[0];
      }

      this.trim = var5;
      this.scroll = new JScrollPane();
      this.getContentPane().add(this.scroll);
      this.makeComponents();
      this.myDoLayout();
   }

   private void makeComponents() {
      int var1 = this.elements.length;
      this.fields = new JTextField[var1];
      this.buttons = new JButton[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.fields[var2] = new JTextField(this.elements[var2]);
         this.buttons[var2] = new JButton("Delete");
         this.buttons[var2].addActionListener(this);
      }

      this.newField = new JTextField("");
      this.addButton = new JButton("Add New Element");
      this.okButton = new JButton("OK");
      this.cancelButton = new JButton("Cancel");
      this.addButton.addActionListener(this);
      this.okButton.addActionListener(this);
      this.cancelButton.addActionListener(this);
   }

   static void p(String var0) {
   }

   private void myDoLayout() {
      if (this.currentPanel != null) {
         this.currentPanel.removeAll();
      }

      this.currentPanel = new JPanel();
      this.currentPanel.setLayout(new GridBagLayout());
      GridBagConstraints var1 = new GridBagConstraints();
      var1.gridx = 0;
      var1.gridy = -1;
      var1.insets = new Insets(5, 5, 5, 5);

      for(int var2 = 0; var2 < this.fields.length; ++var2) {
         ++var1.gridy;
         var1.gridx = 0;
         var1.weightx = 1.0;
         var1.gridwidth = -1;
         var1.fill = 2;
         this.currentPanel.add(this.fields[var2], var1);
         ++var1.gridx;
         var1.anchor = 13;
         var1.fill = 0;
         var1.weightx = 0.0;
         var1.gridwidth = 1;
         this.currentPanel.add(this.buttons[var2], var1);
      }

      ++var1.gridy;
      var1.gridx = 0;
      var1.weightx = 1.0;
      var1.gridwidth = -1;
      var1.fill = 2;
      this.currentPanel.add(this.newField, var1);
      ++var1.gridx;
      var1.anchor = 13;
      var1.fill = 0;
      var1.weightx = 0.0;
      var1.gridwidth = 1;
      this.currentPanel.add(this.addButton, var1);
      ++var1.gridy;
      var1.gridx = 0;
      this.currentPanel.add(this.okButton, var1);
      ++var1.gridx;
      var1.anchor = 17;
      this.currentPanel.add(this.cancelButton, var1);
      this.scroll.setViewportView(this.currentPanel);
   }

   public void actionPerformed(ActionEvent var1) {
      p("actionPerformed");
      Object var2 = var1.getSource();

      int var3;
      for(var3 = 0; var3 < this.buttons.length; ++var3) {
         if (var2 == this.buttons[var3]) {
            p("button#" + var3);
            JTextField[] var4 = new JTextField[this.fields.length - 1];
            JButton[] var5 = new JButton[this.fields.length - 1];
            System.arraycopy(this.fields, 0, var4, 0, var3);
            System.arraycopy(this.fields, var3 + 1, var4, var3, this.fields.length - var3 - 1);
            System.arraycopy(this.buttons, 0, var5, 0, var3);
            System.arraycopy(this.buttons, var3 + 1, var5, var3, this.buttons.length - var3 - 1);
            this.fields = var4;
            this.buttons = var5;
            this.myDoLayout();
            this.repaint();
            return;
         }
      }

      if (var2 == this.addButton) {
         JTextField[] var6 = new JTextField[this.fields.length + 1];
         JButton[] var8 = new JButton[this.fields.length + 1];
         System.arraycopy(this.fields, 0, var6, 0, this.fields.length);
         var6[this.fields.length] = new JTextField(this.newField.getText());
         System.arraycopy(this.buttons, 0, var8, 0, this.buttons.length);
         var8[this.buttons.length] = new JButton("Delete");
         var8[this.buttons.length].addActionListener(this);
         this.fields = var6;
         this.buttons = var8;
         this.myDoLayout();
         this.repaint();
         this.newField.setText("");
      } else if (var2 == this.okButton) {
         this.elements = new String[this.fields.length];

         for(var3 = 0; var3 < this.elements.length; ++var3) {
            String var7 = this.fields[var3].getText();
            if (this.trim) {
               var7 = var7.trim();
            }

            this.elements[var3] = var7;
         }

         this.setVisible(false);
      } else if (var2 == this.cancelButton) {
         this.setVisible(false);
      }
   }

   public String[] getElements() {
      return this.elements;
   }

   public static void main(String[] var0) throws Exception {
      AWTUtils.initLookAndFeel();
      JFrame var1 = new JFrame("array edit test");
      var1.getContentPane().add(new JLabel("blah"));
      var1.setSize(500, 500);
      var1.setLocation(800, 300);
      var1.setVisible(true);
      String[] var2 = new String[7];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = "this is a field, #" + (var3 + 1);
      }

      ArrayEditorDialog var5 = new ArrayEditorDialog(var1, "edit values", true, var2);
      var5.pack();
      AWTUtils.centerOnWindow(var5, var1);
      var5.show();
      var2 = var5.getElements();
      System.err.println("updated elements are: ");

      for(int var4 = 0; var4 < var2.length; ++var4) {
         System.err.println(" " + var4 + var2[var4]);
      }

   }
}
