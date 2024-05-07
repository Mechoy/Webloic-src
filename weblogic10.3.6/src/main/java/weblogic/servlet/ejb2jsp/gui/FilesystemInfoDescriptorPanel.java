package weblogic.servlet.ejb2jsp.gui;

import java.awt.Color;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import weblogic.servlet.ejb2jsp.dd.FilesystemInfoDescriptor;
import weblogic.tools.ui.AWTUtils;
import weblogic.utils.StringUtils;

public class FilesystemInfoDescriptorPanel extends BasePanel implements ActionListener {
   FilesystemInfoDescriptor _bean;
   private static final String[] _saveAs_constrained = new String[]{"DIRECTORY", "JAR"};
   JLabel _EJBJarFile;
   JTextField _javacPath;
   JTextField _javacFlags;
   JTextField _package;
   JCheckBox _keepgenerated;
   JComboBox _saveAs;
   JTextField _saveDirTldFile;
   JTextField _saveDirClassDir;
   JTextField _saveJarFile;
   JTextField _saveJarTmpdir;
   JLabel[] _sourcePath;
   JButton _sourcePath_editButton;
   JPanel _sourcePath_panel;
   JLabel[] _compileClasspath;
   JButton _compileClasspath_editButton;
   JPanel _compileClasspath_panel;

   public FilesystemInfoDescriptorPanel(FilesystemInfoDescriptor var1) {
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
      var2 = new JLabel("EJB Jar File");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._EJBJarFile = new JLabel("");
      this.add(this._EJBJarFile, var1);
      URL var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      ImageIcon var5 = new ImageIcon(var4);
      JButton var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "EJBJarFile");
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
      var2 = new JLabel("Java Compiler");
      var2.setToolTipText("sets the compiler to use for building");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._javacPath = new JTextField("");
      this._javacPath.setToolTipText("sets the compiler to use for building");
      this.add(this._javacPath, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "javacPath");
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
      var2 = new JLabel("Extra Flags");
      var2.setToolTipText("sets extra compiler flags for build, if needed");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._javacFlags = new JTextField("");
      this._javacFlags.setToolTipText("sets extra compiler flags for build, if needed");
      this.add(this._javacFlags, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "javacFlags");
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
      var2 = new JLabel("java package");
      var2.setToolTipText("sets the output package for generated java files");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._package = new JTextField("");
      this._package.setToolTipText("sets the output package for generated java files");
      this.add(this._package, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "package");
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
      var2 = new JLabel("keep generated code");
      var2.setToolTipText("sets if generated code is saved after a build");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.gridwidth = 2;
      this._keepgenerated = new JCheckBox("");
      this._keepgenerated.setToolTipText("sets if generated code is saved after a build");
      this.add(this._keepgenerated, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "keepgenerated");
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
      var2 = new JLabel("Output Type");
      var2.setToolTipText("Output can be to a WEB-INF/classes DIRECTORY or a taglib JAR");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.gridwidth = 2;
      this._saveAs = new JComboBox(_saveAs_constrained);
      this._saveAs.setToolTipText("Output can be to a WEB-INF/classes DIRECTORY or a taglib JAR");
      this.add(this._saveAs, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "saveAs");
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
      var2 = new JLabel("DIRECTORY .tld File");
      var2.setToolTipText("sets the .tld location for DIRECTORY output");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._saveDirTldFile = new JTextField("");
      this._saveDirTldFile.setToolTipText("sets the .tld location for DIRECTORY output");
      this.add(this._saveDirTldFile, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "saveDirTldFile");
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
      var2 = new JLabel("DIRECTORY classes");
      var2.setToolTipText("sets classes location for DIRECTORY output");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._saveDirClassDir = new JTextField("");
      this._saveDirClassDir.setToolTipText("sets classes location for DIRECTORY output");
      this.add(this._saveDirClassDir, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "saveDirClassDir");
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
      var2 = new JLabel("JAR Output File");
      var2.setToolTipText("sets the output jar file when output type is JAR");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._saveJarFile = new JTextField("");
      this._saveJarFile.setToolTipText("sets the output jar file when output type is JAR");
      this.add(this._saveJarFile, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "saveJarFile");
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
      var2 = new JLabel("JAR tmp dir");
      var2.setToolTipText("a tmp directory used for packaging JAR output");
      var2.setFont(var3);
      var2.setOpaque(false);
      this.add(var2, var1);
      ++var1.gridx;
      var1.anchor = 17;
      var1.fill = 2;
      var1.weightx = 1.0;
      var1.gridwidth = 2;
      this._saveJarTmpdir = new JTextField("");
      this._saveJarTmpdir.setToolTipText("a tmp directory used for packaging JAR output");
      this.add(this._saveJarTmpdir, var1);
      var4 = this.getClass().getResource("/weblogic/tools/ui/images/prefs.gif");
      var5 = new ImageIcon(var4);
      var6 = new JButton(var5);
      var6.addActionListener(Main.getInstance());
      var6.setBorder(new EmptyBorder(0, 0, 0, 0));
      var6.putClientProperty("help-anchor", "saveJarTmpdir");
      var1.gridx += 2;
      var1.gridwidth = 1;
      var1.weightx = 0.0;
      var1.fill = 0;
      this.add(var6, var1);
      JPanel var7 = new JPanel();
      var7.setToolTipText("sets directory of the EJB sources");
      LineBorder var8 = new LineBorder(Color.black);
      TitledBorder var9 = new TitledBorder(var8, "EJB Source Path", 0, 0, var3);
      var7.setBorder(var9);
      var7.setLayout(new GridBagLayout());
      this._sourcePath_panel = var7;
      this._sourcePath_editButton = new JButton("Edit sourcePath elements...");
      this._sourcePath_editButton.addActionListener(this);
      this._sourcePath_setFields();
      var1.gridx = 0;
      ++var1.gridy;
      var1.gridwidth = 3;
      var1.fill = 2;
      var1.weightx = 1.0;
      this.add(var7, var1);
      var7 = new JPanel();
      var7.setToolTipText("If this EJB jar depends on additional classes when it is compiled, you should add those additional classpath elements here.");
      var8 = new LineBorder(Color.black);
      var9 = new TitledBorder(var8, "Extra Compile Classpath", 0, 0, var3);
      var7.setBorder(var9);
      var7.setLayout(new GridBagLayout());
      this._compileClasspath_panel = var7;
      this._compileClasspath_editButton = new JButton("Edit compileClasspath elements...");
      this._compileClasspath_editButton.addActionListener(this);
      this._compileClasspath_setFields();
      var1.gridx = 0;
      ++var1.gridy;
      var1.gridwidth = 3;
      var1.fill = 2;
      var1.weightx = 1.0;
      this.add(var7, var1);
   }

   public void bean2fields() {
      this._EJBJarFile.setText(this._bean.getEJBJarFile());
      this._javacPath.setText(this._bean.getJavacPath());
      this._javacFlags.setText(this._bean.getJavacFlags());
      this._package.setText(this._bean.getPackage());
      this._keepgenerated.setSelected(this._bean.getKeepgenerated());
      this._saveAs.setSelectedItem(this._bean.getSaveAs());
      this._saveDirTldFile.setText(this._bean.getSaveDirTldFile());
      this._saveDirClassDir.setText(this._bean.getSaveDirClassDir());
      this._saveJarFile.setText(this._bean.getSaveJarFile());
      this._saveJarTmpdir.setText(this._bean.getSaveJarTmpdir());
      String[] var1 = this._bean.getSourcePath();

      int var2;
      for(var2 = 0; var2 < var1.length; ++var2) {
         this._sourcePath[var2].setText("" + var1[var2]);
      }

      var1 = this._bean.getCompileClasspath();

      for(var2 = 0; var2 < var1.length; ++var2) {
         this._compileClasspath[var2].setText("" + var1[var2]);
      }

   }

   public void fields2bean() {
      String var1 = null;
      var1 = this._EJBJarFile.getText().trim();
      if (!var1.equals(this._bean.getEJBJarFile())) {
         this.dirty = true;
         this._bean.setEJBJarFile(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._javacPath.getText().trim();
      if (!var1.equals(this._bean.getJavacPath())) {
         this.dirty = true;
         this._bean.setJavacPath(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._javacFlags.getText().trim();
      if (!var1.equals(this._bean.getJavacFlags())) {
         this.dirty = true;
         this._bean.setJavacFlags(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._package.getText().trim();
      if (!var1.equals(this._bean.getPackage())) {
         this.dirty = true;
         this._bean.setPackage(StringUtils.valueOf(var1));
      }

      var1 = null;
      if (this._bean.getKeepgenerated() != this._keepgenerated.isSelected()) {
         this.dirty = true;
         this._bean.setKeepgenerated(this._keepgenerated.isSelected());
      }

      var1 = null;
      var1 = (String)this._saveAs.getSelectedItem();
      String var2 = this._bean.getSaveAs();
      if (!var1.equals(var2)) {
         this.dirty = true;
         this._bean.setSaveAs(var1);
      }

      var1 = null;
      var1 = this._saveDirTldFile.getText().trim();
      if (!var1.equals(this._bean.getSaveDirTldFile())) {
         this.dirty = true;
         this._bean.setSaveDirTldFile(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._saveDirClassDir.getText().trim();
      if (!var1.equals(this._bean.getSaveDirClassDir())) {
         this.dirty = true;
         this._bean.setSaveDirClassDir(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._saveJarFile.getText().trim();
      if (!var1.equals(this._bean.getSaveJarFile())) {
         this.dirty = true;
         this._bean.setSaveJarFile(StringUtils.valueOf(var1));
      }

      var1 = null;
      var1 = this._saveJarTmpdir.getText().trim();
      if (!var1.equals(this._bean.getSaveJarTmpdir())) {
         this.dirty = true;
         this._bean.setSaveJarTmpdir(StringUtils.valueOf(var1));
      }

      var1 = null;
      String[] var4 = new String[this._sourcePath.length];

      int var3;
      for(var3 = 0; var3 < this._sourcePath.length; ++var3) {
         var1 = this._sourcePath[var3].getText().trim();
         var4[var3] = var1;
      }

      this._bean.setSourcePath(var4);
      var1 = null;
      var4 = new String[this._compileClasspath.length];

      for(var3 = 0; var3 < this._compileClasspath.length; ++var3) {
         var1 = this._compileClasspath[var3].getText().trim();
         var4[var3] = var1;
      }

      this._bean.setCompileClasspath(var4);
   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      Frame var3;
      String[] var4;
      String[] var5;
      int var6;
      ArrayEditorDialog var7;
      if (var2 == this._sourcePath_editButton) {
         var3 = this.getParentFrame();
         var4 = this._bean.getSourcePath();
         if (var4 == null) {
            var4 = new String[0];
         }

         var5 = new String[var4.length];

         for(var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = "" + var4[var6];
         }

         var7 = new ArrayEditorDialog(var3, "Edit sourcePath Elements", true, var5);
         var7.pack();
         AWTUtils.centerOnWindow(var7, var3);
         var7.show();
         var5 = var7.getElements();
         this._bean.setSourcePath(var5);
         this._sourcePath_setFields();
      }

      if (var2 == this._compileClasspath_editButton) {
         var3 = this.getParentFrame();
         var4 = this._bean.getCompileClasspath();
         if (var4 == null) {
            var4 = new String[0];
         }

         var5 = new String[var4.length];

         for(var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = "" + var4[var6];
         }

         var7 = new ArrayEditorDialog(var3, "Edit compileClasspath Elements", true, var5);
         var7.pack();
         AWTUtils.centerOnWindow(var7, var3);
         var7.show();
         var5 = var7.getElements();
         this._bean.setCompileClasspath(var5);
         this._compileClasspath_setFields();
      }

   }

   private void _sourcePath_setFields() {
      JPanel var1 = this._sourcePath_panel;
      var1.removeAll();
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(1, 1, 1, 1);
      var2.gridx = 0;
      var2.gridy = -1;
      var2.gridheight = 1;
      var2.anchor = 17;
      String[] var3 = this._bean.getSourcePath();
      int var4 = 0;
      if (var3 != null) {
         var4 = var3.length;
      }

      this._sourcePath = new JLabel[var4];

      for(int var5 = 0; var5 < var4; ++var5) {
         ++var2.gridy;
         this._sourcePath[var5] = new JLabel("" + var3[var5]);
         var2.fill = 2;
         var2.weightx = 1.0;
         var2.gridx = 0;
         var1.add(this._sourcePath[var5], var2);
      }

      ++var2.gridy;
      var2.fill = 0;
      var2.weightx = 0.0;
      var2.gridx = 0;
      var2.insets = new Insets(5, 5, 5, 5);
      var1.add(this._sourcePath_editButton, var2);
      var1.invalidate();
      this.invalidate();
      var1.doLayout();
      this.repaint();
      var1.repaint();
   }

   private void _compileClasspath_setFields() {
      JPanel var1 = this._compileClasspath_panel;
      var1.removeAll();
      GridBagConstraints var2 = new GridBagConstraints();
      var2.insets = new Insets(1, 1, 1, 1);
      var2.gridx = 0;
      var2.gridy = -1;
      var2.gridheight = 1;
      var2.anchor = 17;
      String[] var3 = this._bean.getCompileClasspath();
      int var4 = 0;
      if (var3 != null) {
         var4 = var3.length;
      }

      this._compileClasspath = new JLabel[var4];

      for(int var5 = 0; var5 < var4; ++var5) {
         ++var2.gridy;
         this._compileClasspath[var5] = new JLabel("" + var3[var5]);
         var2.fill = 2;
         var2.weightx = 1.0;
         var2.gridx = 0;
         var1.add(this._compileClasspath[var5], var2);
      }

      ++var2.gridy;
      var2.fill = 0;
      var2.weightx = 0.0;
      var2.gridx = 0;
      var2.insets = new Insets(5, 5, 5, 5);
      var1.add(this._compileClasspath_editButton, var2);
      var1.invalidate();
      this.invalidate();
      var1.doLayout();
      this.repaint();
      var1.repaint();
   }

   public FilesystemInfoDescriptor getBean() {
      return this._bean;
   }

   public static void main(String[] var0) throws Exception {
      new JFrame("mytest");
   }
}
