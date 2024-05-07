package weblogic.tools.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class RowEditorDialog extends JDialog implements ActionListener {
   private static final MarathonTextFormatter fmt = new MarathonTextFormatter();
   JButton ok;
   JButton cancel;
   boolean okclicked = false;
   private IBeanRowEditor editor;
   Frame owner;
   private boolean m_wasCancelled = false;
   private static final int MAX_HEIGHT = 600;
   private static final int MAX_WIDTH = 500;

   public RowEditorDialog(Frame var1, String var2, boolean var3, IBeanRowEditor var4) {
      super(var1, var2, var3);
      this.owner = var1;
      this.editor = var4;
      JPanel var5 = new JPanel();
      var5.setLayout(new GridBagLayout());
      GridBagConstraints var6 = new GridBagConstraints();
      var6.gridx = 0;
      var6.gridy = 0;
      var6.insets = new Insets(5, 5, 5, 5);
      var6.weightx = var6.weighty = 1.0;
      var6.fill = 1;
      JScrollPane var7 = new JScrollPane(this.editor.getComponent());
      var5.add(var7, var6);
      ++var6.gridy;
      var6.weighty = 0.0;
      var6.weightx = 1.0;
      JPanel var8 = this.makeButtonPane();
      var5.add(var8, var6);
      this.getContentPane().add(var5);
      this.getRootPane().setDefaultButton(this.ok);
      this.pack();
   }

   public void pack() {
      super.pack();
      Dimension var1 = this.editor.getComponent().getSize();
      var1.width = (int)Math.max(var1.getWidth(), 500.0);
      var1.height = (int)Math.max(var1.getHeight(), 600.0);
      this.setSize(var1);
   }

   public IBeanRowEditor getBeanRowEditor() {
      return this.editor;
   }

   public final void editObject(Object var1) {
      this.setAutoCommit(false);
      this.editor.setEditingBean(var1);
      this.okclicked = false;
      this.pack();
      AWTUtils.centerOnWindow(this, this.owner);
      JComponent var2 = this.editor.getFirstFocusComponent();
      if (var2 != null) {
         var2.requestFocus();
      }

      this.setVisible(true);
   }

   public boolean wasCancelled() {
      return this.m_wasCancelled;
   }

   public void onEscPressed() {
      this.m_wasCancelled = true;
   }

   public final Object addObject() {
      Object var1 = this.editor.createNewBean();
      this.editObject(var1);
      return this.okclicked ? var1 : null;
   }

   private JPanel makeButtonPane() {
      JPanel var1 = new JPanel();
      this.ok = new JButton("OK");
      this.cancel = new JButton("Cancel");
      var1.setLayout(new GridBagLayout());
      GridBagConstraints var2 = new GridBagConstraints();
      var2.gridx = 0;
      var2.gridy = 0;
      var2.weightx = 1.0;
      var2.anchor = 13;
      var1.add(this.ok, var2);
      ++var2.gridx;
      var2.weightx = 0.0;
      var2.insets.left = 5;
      var1.add(this.cancel, var2);
      this.ok.addActionListener(this);
      this.cancel.addActionListener(this);
      return var1;
   }

   public void actionPerformed(ActionEvent var1) {
      if (var1.getSource() == this.ok) {
         this.m_wasCancelled = false;
         IValidationFeedback var2 = this.editor.getFeedback();
         if (var2 != null) {
            this.showError(var2);
            return;
         }

         this.editor.uiToModel();
         this.okclicked = true;
      } else if (var1.getSource() == this.cancel) {
         this.m_wasCancelled = true;
      }

      this.setAutoCommit(true);
      this.setVisible(false);
   }

   private void showError(IValidationFeedback var1) {
      Component var2 = var1.getFocusComponent();
      JOptionPane.showMessageDialog(this, var1.getMessage(), fmt.getIncompleteSettings(), 0);
      if (var2 != null) {
         var2.requestFocus();
      }

   }

   private void setAutoCommit(boolean var1) {
      this.editor.setAutoCommit(var1);
   }

   public void processKeyEvent(KeyEvent var1) {
      int var2 = var1.getKeyCode();
      if (var2 == 112) {
         Component var3 = SwingUtilities.findFocusOwner(this);
         Object var4 = null;
      }
   }

   private static void ppp(String var0) {
      System.out.println("[RowEditorDialog] " + var0);
   }
}
