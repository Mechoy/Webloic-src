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

public class PreferencesDialog extends JDialog implements ActionListener {
   private Prefs p;
   private JTextField compiler;
   private JTextField webapp;
   private JTextField src;
   private JButton okButton;
   private JButton cancelButton;
   private JPanel currentPanel;
   private JScrollPane scroll;

   public PreferencesDialog(Frame var1, String var2, boolean var3, Prefs var4) {
      super(var1, var2, var3);
      this.p = var4;
      JPanel var5 = new JPanel();
      var5.setLayout(new GridBagLayout());
      GridBagConstraints var6 = new GridBagConstraints();
      var6.gridx = 0;
      var6.gridy = -1;
      var6.insets = new Insets(5, 5, 5, 5);
      ++var6.gridy;
      var6.gridx = 0;
      var6.weightx = 0.0;
      var6.gridwidth = -1;
      var6.fill = 0;
      JLabel var7 = new JLabel("Java Compiler");
      var5.add(var7, var6);
      ++var6.gridx;
      var6.anchor = 13;
      var6.gridwidth = 0;
      var6.fill = 2;
      var6.weightx = 1.0;
      this.compiler = new JTextField(var4.compiler);
      var5.add(this.compiler, var6);
      ++var6.gridy;
      var6.gridx = 0;
      var6.weightx = 0.0;
      var6.gridwidth = -1;
      var6.fill = 0;
      var7 = new JLabel("Webapp Directory");
      var5.add(var7, var6);
      ++var6.gridx;
      var6.anchor = 13;
      var6.gridwidth = 0;
      var6.fill = 2;
      var6.weightx = 1.0;
      this.webapp = new JTextField(var4.webapp);
      var5.add(this.webapp, var6);
      ++var6.gridy;
      var6.gridx = 0;
      var6.weightx = 0.0;
      var6.gridwidth = -1;
      var6.fill = 0;
      var7 = new JLabel("Source Directory");
      var5.add(var7, var6);
      ++var6.gridx;
      var6.anchor = 13;
      var6.gridwidth = 0;
      var6.fill = 2;
      var6.weightx = 1.0;
      this.src = new JTextField(var4.sourceDir);
      var5.add(this.src, var6);
      ++var6.gridy;
      var6.gridx = 0;
      var6.weightx = 0.0;
      var6.gridwidth = -1;
      var6.fill = 0;
      this.okButton = new JButton("OK");
      this.okButton.addActionListener(this);
      var5.add(this.okButton, var6);
      ++var6.gridx;
      this.cancelButton = new JButton("Cancel");
      this.cancelButton.addActionListener(this);
      var5.add(this.cancelButton, var6);
      this.getContentPane().add(var5);
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.okButton) {
         this.fields2Prefs();
      }

      this.setVisible(false);
   }

   private void fields2Prefs() {
      Prefs var1 = new Prefs();
      var1.compiler = this.compiler.getText().trim();
      var1.sourceDir = this.src.getText().trim();
      var1.webapp = this.webapp.getText().trim();
      this.p = var1;
   }

   public Prefs getPrefs() {
      return this.p;
   }

   public static void main(String[] var0) throws Exception {
      AWTUtils.initLookAndFeel();
      JFrame var1 = new JFrame("array edit test");
      var1.getContentPane().add(new JLabel("blah"));
      var1.setSize(500, 500);
      var1.setLocation(800, 300);
      var1.setVisible(true);
      Prefs var2 = new Prefs();
      var2.compiler = "javac";
      var2.webapp = "C:/tmp/webapp";
      var2.sourceDir = "C:/weblogic/dev/src_ag/samples";
      PreferencesDialog var3 = new PreferencesDialog(var1, "prefs", true, var2);
      var3.pack();
      AWTUtils.centerOnWindow(var3, var1);
      var3.show();
      System.err.println("prefs are: " + var3.getPrefs());
      System.exit(0);
   }
}
