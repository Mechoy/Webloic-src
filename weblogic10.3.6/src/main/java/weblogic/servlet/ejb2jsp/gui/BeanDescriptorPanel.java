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
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;
import weblogic.utils.StringUtils;

public class BeanDescriptorPanel extends BasePanel implements ActionListener {
   BeanDescriptor _bean;
   JLabel _EJBName;
   JLabel _homeType;
   JLabel _remoteType;
   JLabel _EJBType;
   JCheckBox _enabled;
   JTextField _JNDIName;

   public BeanDescriptorPanel(BeanDescriptor var1) {
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
      var2 = new JLabel("EJB Name");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._EJBName = new JLabel("");
      this.add(this._EJBName, var1);
      URL var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      ImageIcon var5 = new ImageIcon(var4);
      JButton var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "EJBName");
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
      var2 = new JLabel("Home Type");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._homeType = new JLabel("");
      this.add(this._homeType, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "homeType");
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
      var2 = new JLabel("Remote Type");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._remoteType = new JLabel("");
      this.add(this._remoteType, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "remoteType");
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
      var2 = new JLabel("EJB Type");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._EJBType = new JLabel("");
      this.add(this._EJBType, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "EJBType");
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
      var2 = new JLabel("tags enabled");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.gridwidth = 2;
      this._enabled = new JCheckBox("");
      this.add(this._enabled, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "ejbEnabled");
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
      var2 = new JLabel("JNDI Name");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._JNDIName = new JTextField("");
      this.add(this._JNDIName, var1);
      var4 = this.getClass().getResource("/weblogic/graphics/ques.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "JNDIName");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
   }

   public void bean2fields() {
      this._EJBName.setText(this._bean.getEJBName());
      this._homeType.setText(this._bean.getHomeType());
      this._remoteType.setText(this._bean.getRemoteType());
      this._EJBType.setText(this._bean.getEJBType());
      this._enabled.setSelected(this._bean.isEnabled());
      this._JNDIName.setText(this._bean.getJNDIName());
   }

   public void fields2bean() {
      String var1 = null;
      var1 = this._EJBName.getText().trim();
      if (!var1.equals(this._bean.getEJBName())) {
         this.dirty = true;
         this._bean.setEJBName(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._homeType.getText().trim();
      if (!var1.equals(this._bean.getHomeType())) {
         this.dirty = true;
         this._bean.setHomeType(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._remoteType.getText().trim();
      if (!var1.equals(this._bean.getRemoteType())) {
         this.dirty = true;
         this._bean.setRemoteType(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._EJBType.getText().trim();
      if (!var1.equals(this._bean.getEJBType())) {
         this.dirty = true;
         this._bean.setEJBType(StringUtils.valueOf(var1));
      }

      var1 = null;
      if (this._bean.isEnabled() != this._enabled.isSelected()) {
         this.dirty = true;
         this._bean.setEnabled(this._enabled.isSelected());
      }

      var1 = null;
      var1 = this._JNDIName.getText().trim();
      if (!var1.equals(this._bean.getJNDIName())) {
         this.dirty = true;
         this._bean.setJNDIName(StringUtils.valueOf(var1));
      }

   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
   }

   public BeanDescriptor getBean() {
      return this._bean;
   }

   public static void main(String[] var0) throws Exception {
      new JFrame("mytest");
   }
}
