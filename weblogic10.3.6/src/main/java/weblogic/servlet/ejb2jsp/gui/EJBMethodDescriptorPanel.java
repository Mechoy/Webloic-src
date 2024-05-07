package weblogic.servlet.ejb2jsp.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import weblogic.servlet.ejb2jsp.Utils;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.utils.StringUtils;

public class EJBMethodDescriptorPanel extends BasePanel implements ActionListener {
   static final long serialVersionUID = 2139021295039679016L;
   EJBMethodDescriptor _bean;
   JLabel _name;
   JTextField _tagName;
   JCheckBox _enabled;
   JCheckBox _evalOut;
   JLabel _targetType;
   JLabel _returnType;
   JLabel _signature;
   JTextField _info;

   public EJBMethodDescriptorPanel(EJBMethodDescriptor var1) {
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
      var3 = new Font(var3.getFontName(), 1, var3.getSize());
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Method Name");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._name = new JLabel("");
      this.add(this._name, var1);
      URL var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      ImageIcon var5 = new ImageIcon(var4);
      JButton var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "methodName");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Tag Name");
      var2.setToolTipText("sets the JSP tag name for this EJB method");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._tagName = new JTextField("");
      this._tagName.setToolTipText("sets the JSP tag name for this EJB method");
      this.add(this._tagName, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "tagName");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Generate tag");
      var2.setToolTipText("sets whether a tag should be generated for this method");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.gridwidth = 2;
      this._enabled = new JCheckBox("");
      this._enabled.setToolTipText("sets whether a tag should be generated for this method");
      this.add(this._enabled, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "tagEnabled");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      if (!Utils.isVoid(this._bean.getReturnType())) {
         var1.gridx = 0;
         var1.gridwidth = 1;
         ++var1.gridy;
         var1.weightx = 0.0;
         var1.fill = 0;
         var1.anchor = 13;
         var2 = new JLabel("Eval Out");
         var2.setToolTipText("setting this will automatically print the return value to the servlet output stream");
         var2.setFont(var3);
         var2.setOpaque(false);
         this.add(var2, var1);
         ++var1.gridx;
         var1.anchor = 17;
         var1.gridwidth = 2;
         this._evalOut = new JCheckBox("");
         this._evalOut.setToolTipText("setting this will automatically print the return value to the servlet output stream");
         this.add(this._evalOut, var1);
         var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
         var5 = new ImageIcon(var4);
         var6 = new JButton(var5);
         var6.addActionListener(Main.getInstance());
         var6.setBorder(new EmptyBorder(0, 0, 0, 0));
         var6.putClientProperty("help-anchor", "evalOut");
         var1.gridx += 2;
         var1.gridwidth = 1;
         var1.weightx = 0.0;
         var1.fill = 0;
         this.add(var6, var1);
      }

      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Target Type");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._targetType = new JLabel("");
      this.add(this._targetType, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "targetType");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Return Type");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._returnType = new JLabel("");
      this.add(this._returnType, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "returnType");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Method Signature");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._signature = new JLabel("");
      this.add(this._signature, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "signature");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      var1.gridx = 0;
      var1.gridwidth = 1;
      ++var1.gridy;
      var1.weightx = 0.0;
      var1.fill = 0;
      var1.anchor = 13;
      var2 = new JLabel("Description");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._info = new JTextField("");
      this.add(this._info, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "info");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
   }

   public void bean2fields() {
      this._name.setText(this._bean.getName());
      this._tagName.setText(this._bean.getTagName());
      this._enabled.setSelected(this._bean.isEnabled());
      if (this._evalOut != null) {
         this._evalOut.setSelected(this._bean.isEvalOut());
      }

      this._targetType.setText(this._bean.getTargetType());
      this._returnType.setText(this._bean.getReturnType());
      this._signature.setText(this._bean.getSignature());
      this._info.setText(this._bean.getInfo());
   }

   public void fields2bean() {
      String var1 = null;
      var1 = this._name.getText().trim();
      if (!var1.equals(this._bean.getName())) {
         this.dirty = true;
         this._bean.setName(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._tagName.getText().trim();
      if (!var1.equals(this._bean.getTagName())) {
         this.dirty = true;
         this._bean.setTagName(StringUtils.valueOf(var1));
      }

      var1 = null;
      if (this._bean.isEnabled() != this._enabled.isSelected()) {
         this.dirty = true;
         this._bean.setEnabled(this._enabled.isSelected());
      }

      if (this._evalOut != null) {
         var1 = null;
         if (this._bean.isEvalOut() != this._evalOut.isSelected()) {
            this.dirty = true;
            this._bean.setEvalOut(this._evalOut.isSelected());
         }
      }

      var1 = null;
      var1 = this._targetType.getText().trim();
      if (!var1.equals(this._bean.getTargetType())) {
         this.dirty = true;
         this._bean.setTargetType(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._returnType.getText().trim();
      if (!var1.equals(this._bean.getReturnType())) {
         this.dirty = true;
         this._bean.setReturnType(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._signature.getText().trim();
      if (!var1.equals(this._bean.getSignature())) {
         this.dirty = true;
      }

      var1 = null;
      var1 = this._info.getText().trim();
      if (!var1.equals(this._bean.getInfo())) {
         this.dirty = true;
         this._bean.setInfo(StringUtils.valueOf(var1));
      }

   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
   }

   public EJBMethodDescriptor getBean() {
      return this._bean;
   }

   public static void main(String[] var0) throws Exception {
      new JFrame("mytest");
   }
}
