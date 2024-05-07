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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;
import weblogic.utils.StringUtils;

public class MethodParamDescriptorPanel extends BasePanel implements ActionListener {
   MethodParamDescriptor _bean;
   private static final String[] _default_constrained = new String[]{"NONE", "EXPRESSION", "METHOD"};
   JTextField _name;
   JLabel _type;
   JComboBox _default;
   JTextField _defaultValue;
   JTextArea _defaultMethod;

   public MethodParamDescriptorPanel(MethodParamDescriptor var1) {
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
      var2 = new JLabel("Attribute Name");
      var2.setToolTipText("sets the attribute name.  The name should not conflict with names of other attributes of this tag.");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._name = new JTextField("");
      this._name.setToolTipText("sets the attribute name.  The name should not conflict with names of other attributes of this tag.");
      this.add(this._name, var1);
      URL var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      ImageIcon var5 = new ImageIcon(var4);
      JButton var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "attributeName");
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
      var2 = new JLabel("Attribute Type");
      var2.setToolTipText("Describes the expected java type for this attribute");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._type = new JLabel("");
      this._type.setToolTipText("Describes the expected java type for this attribute");
      this.add(this._type, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "type");
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
      var2 = new JLabel("Default Attribute Value");
      var2.setToolTipText("sets if/how to get a default value for this attribute if it isn't specified in a tag");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.gridwidth = 2;
      this._default = new JComboBox(_default_constrained);
      this._default.setToolTipText("sets if/how to get a default value for this attribute if it isn't specified in a tag");
      this.add(this._default, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "default");
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
      var2 = new JLabel("Default Expression");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._defaultValue = new JTextField("");
      this.add(this._defaultValue, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "defaultValue");
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
      var2 = new JLabel("Default Method Body");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 1;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._defaultMethod = new JTextArea("", 15, 40);
      this._defaultMethod.setBorder(new BevelBorder(1));
      this.add(this._defaultMethod, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "defaultMethod");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
   }

   public void bean2fields() {
      this._name.setText(this._bean.getName());
      this._type.setText(this._bean.getType());
      this._default.setSelectedItem(this._bean.getDefault());
      this._defaultValue.setText(this._bean.getDefaultValue());
      this._defaultMethod.setText(this._bean.getDefaultMethod());
   }

   public void fields2bean() {
      String var1 = null;
      var1 = this._name.getText().trim();
      if (!var1.equals(this._bean.getName())) {
         this.dirty = true;
         this._bean.setName(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._type.getText().trim();
      if (!var1.equals(this._bean.getType())) {
         this.dirty = true;
         this._bean.setType(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = (String)this._default.getSelectedItem();
      String var2 = this._bean.getDefault();
      if (!var1.equals(var2)) {
         this.dirty = true;
         this._bean.setDefault(var1);
      }

      var1 = null;
      var1 = this._defaultValue.getText().trim();
      if (!var1.equals(this._bean.getDefaultValue())) {
         this.dirty = true;
         this._bean.setDefaultValue(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._defaultMethod.getText().trim();
      if (!var1.equals(this._bean.getDefaultMethod())) {
         this.dirty = true;
         this._bean.setDefaultMethod(StringUtils.valueOf(var1));
      }

   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
   }

   public MethodParamDescriptor getBean() {
      return this._bean;
   }

   public static void main(String[] var0) throws Exception {
      new JFrame("mytest");
   }
}
