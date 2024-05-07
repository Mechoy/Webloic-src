package weblogic.servlet.ejb2jsp.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;

public class EJBTaglibDescriptorPanel extends BasePanel implements ActionListener {
   EJBTaglibDescriptor _bean;

   public EJBTaglibDescriptorPanel(EJBTaglibDescriptor var1) {
      this._bean = var1;
      this.addFields();
      this.bean2fields();
   }

   private Frame getParentFrame() {
      Object var1;
      for(var1 = this; var1 != null && !(var1 instanceof Frame); var1 = ((Component)var1).getParent()) {
      }

      if (var1 == null) {
         throw new RuntimeException("not contained in frame?");
      } else {
         return (Frame)var1;
      }
   }

   private void addFields() {
      this.setLayout(new GridBagLayout());
      GridBagConstraints var1 = new GridBagConstraints();
      var1.gridx = 0;
      var1.gridy = -1;
      var1.insets = new Insets(5, 5, 5, 5);
      JLabel var2 = new JLabel("");
      Font var3 = var2.getFont();
      new Font(var3.getFontName(), 1, var3.getSize());
   }

   public void bean2fields() {
   }

   public void fields2bean() {
   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
   }

   public EJBTaglibDescriptor getBean() {
      return this._bean;
   }

   public static void main(String[] var0) throws Exception {
      new JFrame("mytest");
   }
}
