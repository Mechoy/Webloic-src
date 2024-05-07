package weblogic.servlet.ejb2jsp.gui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import weblogic.servlet.ejb2jsp.Utils;
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;
import weblogic.servlet.ejb2jsp.dd.FilesystemInfoDescriptor;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;
import weblogic.tools.ui.AWTUtils;
import weblogic.tools.ui.GUIPrintStream;
import weblogic.utils.io.XMLWriter;

public class Main implements TreeSelectionListener, ActionListener, WindowListener {
   public static final boolean debug = false;
   private Stack tasks = new Stack();
   EJBTaglibDescriptor currentBean;
   Hashtable panels = new Hashtable();
   String[] cmdLine;
   JScrollPane left;
   JScrollPane right;
   JFrame frame;
   JTree tree;
   JSplitPane split;
   JMenuBar mb;
   JMenu menu;
   JPanel splash;
   JTextArea compileTA;
   GUIPrintStream guiPS;
   Prefs prefs;
   JMenuItem saveMenu;
   JMenuItem saveAsMenu;
   JMenuItem quitMenu;
   JMenuItem openMenu;
   JMenuItem newMenu;
   JMenuItem compileMenu;
   JMenuItem resolveMenu;
   JMenuItem prefsMenu;
   JMenuItem helpMenu;
   JFrame docFrame;
   HelpFrame contextHelp;
   Image frameIcon = null;
   private static Main theMain;
   private boolean dirty = false;
   File currentDirectory = new File(".");
   File currentProject = null;

   static void p(String var0) {
   }

   private BasePanel currentPanel() {
      JViewport var1 = this.right.getViewport();
      Component var2 = var1.getView();
      if (var2 == null) {
         return null;
      } else {
         return var2 instanceof BasePanel ? (BasePanel)var2 : null;
      }
   }

   private void setClean() {
      if (this.currentPanel() != null) {
         this.currentPanel().setDirty(false);
      }

   }

   private void fields2bean() {
      BasePanel var1 = this.currentPanel();
      if (var1 != null) {
         try {
            var1.fields2bean();
            boolean var2 = var1.isDirty();
            if (var2) {
               p("panel " + var1.getClass().getName() + " says he's dirty");
            }

            this.dirty |= var2;
         } catch (Exception var3) {
            this.displayException(var3);
         }

      }
   }

   private void objectSelected(Object var1) throws Exception {
      Class var2 = var1.getClass();
      if (!var2.isArray()) {
         JViewport var3 = this.right.getViewport();
         FilesystemInfoDescriptorPanel var4 = null;
         if (var2 == FilesystemInfoDescriptor.class) {
            FilesystemInfoDescriptor var5 = (FilesystemInfoDescriptor)var1;
            var4 = new FilesystemInfoDescriptorPanel(var5);
            var3.setView((Component)null);
            var3.setView(var4);
         } else if (var2 == BeanDescriptor.class) {
            BeanDescriptor var8 = (BeanDescriptor)var1;
            BeanDescriptorPanel var6 = new BeanDescriptorPanel(var8);
            var3.setView((Component)null);
            var3.setView(var6);
         } else if (var2 == EJBTaglibDescriptor.class) {
            EJBTaglibDescriptor var10 = (EJBTaglibDescriptor)var1;
            EJBTaglibDescriptorPanel var7 = new EJBTaglibDescriptorPanel(var10);
            var3.setView((Component)null);
            var3.setView(var7);
         } else if (var2 == MethodParamDescriptor.class) {
            MethodParamDescriptor var12 = (MethodParamDescriptor)var1;
            MethodParamDescriptorPanel var9 = new MethodParamDescriptorPanel(var12);
            var3.setView((Component)null);
            var3.setView(var9);
         } else if (var2 == EJBMethodDescriptor.class) {
            EJBMethodDescriptor var13 = (EJBMethodDescriptor)var1;
            EJBMethodDescriptorPanel var11 = new EJBMethodDescriptorPanel(var13);
            var3.setView((Component)null);
            var3.setView(var11);
         } else {
            System.err.println("ERROR: I don't understand type " + var2.getName());
         }

      }
   }

   private void objectDeselected(Object var1) throws Exception {
      Class var2 = var1.getClass();
      if (!var2.isArray()) {
         JViewport var3 = this.right.getViewport();
         Component var4 = var3.getView();
         if (var4 != null && var4 instanceof BasePanel) {
            BasePanel var5 = null;
            var5 = (BasePanel)var4;
            var5.fields2bean();
         }

      }
   }

   public void valueChanged(TreeSelectionEvent var1) {
      TreePath var2 = var1.getOldLeadSelectionPath();
      TreePath var3 = var1.getNewLeadSelectionPath();
      Object var4 = null;
      Object var5 = null;
      DefaultMutableTreeNode var6 = null;
      if (var2 != null) {
         var6 = (DefaultMutableTreeNode)var2.getLastPathComponent();
         if (var6 != null) {
            var4 = var6.getUserObject();
         }
      }

      if (var3 != null) {
         var6 = (DefaultMutableTreeNode)var3.getLastPathComponent();
         if (var6 != null) {
            var5 = var6.getUserObject();
         }
      }

      if (var4 != null) {
         p("deselected type: " + var4.getClass().getName());

         try {
            this.objectDeselected(var4);
         } catch (Exception var9) {
            this.displayException(var9);
         }
      }

      if (var5 != null) {
         p("selected type: " + var5.getClass().getName());

         try {
            this.objectSelected(var5);
         } catch (Exception var8) {
            this.displayException(var8);
         }
      }

   }

   public Main(String[] var1) {
      this.cmdLine = var1;
      Thread var2 = new Thread(new MainWorker(this), "MainGuiWorker");
      var2.start();
   }

   private static void usage() {
      System.err.println("usage: Main <ejb-jar-file> <ejb-source-dir>, -or-");
      System.err.println("       Main <ejb2jsp-project-file>");
   }

   EJBTaglibDescriptor loadRootBean() throws Exception {
      if (this.cmdLine != null && this.cmdLine.length != 0) {
         if (this.cmdLine.length == 1) {
            if (this.cmdLine[0].endsWith(".ejb2jsp")) {
               return this.loadBeanFromFile(new File(this.cmdLine[0]));
            }

            if (this.cmdLine[0].endsWith(".jar")) {
               File var1 = new File(this.cmdLine[0]);
               var1 = new File(var1.getAbsolutePath());
               return this.loadFromPaths(var1.getAbsolutePath(), var1.getParent());
            }

            usage();
         }

         if (this.cmdLine.length != 2) {
            usage();
            return null;
         } else {
            try {
               return this.loadFromPaths(this.cmdLine[0], this.cmdLine[1]);
            } catch (Exception var2) {
               this.displayException(var2);
               return null;
            }
         }
      } else {
         return null;
      }
   }

   EJBTaglibDescriptor loadBeanFromFile(File var1) throws Exception {
      EJBTaglibDescriptor var2 = EJBTaglibDescriptor.load(var1);
      this.currentProject = var1;
      return var2;
   }

   public void pushTask(Runnable var1) {
      synchronized(this.tasks) {
         this.tasks.push(var1);
         this.tasks.notify();
      }
   }

   public Runnable getTask() throws InterruptedException {
      synchronized(this.tasks) {
         while(this.tasks.isEmpty()) {
            this.tasks.wait();
         }

         Runnable var2 = (Runnable)this.tasks.firstElement();
         this.tasks.removeElementAt(0);
         return var2;
      }
   }

   private boolean askToSave() {
      this.fields2bean();
      if (!this.dirty) {
         return true;
      } else {
         int var1 = JOptionPane.showConfirmDialog(this.frame, "Changes to the current project have not been saved.  Do you want to save now?", "Confirm Exit", 1, 1);
         if (var1 == 1) {
            return true;
         } else if (var1 == 2) {
            return false;
         } else {
            if (this.saveMenu.isEnabled()) {
               this.doSave();
            } else {
               this.doSaveAs();
            }

            return true;
         }
      }
   }

   public void doSave() {
      this.saveCurrentBean();
   }

   private void saveCurrentBean() {
      this.fields2bean();
      XMLWriter var1 = null;

      try {
         FileOutputStream var2 = new FileOutputStream(this.currentProject);
         var1 = new XMLWriter(var2);
         this.currentBean.toXML(var1);
         var1.flush();
         var1.close();
         var2.close();
         this.dirty = false;
         var1 = null;
      } catch (Exception var11) {
         this.displayException(var11);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Exception var10) {
            }
         }

      }

   }

   public void doCompile() {
      int var1 = 0;
      p("docompile " + var1++);
      this.fields2bean();
      p("docompile " + var1++);
      GUIPrintStream var2 = this.getGUIPrintStream();
      p("docompile " + var1++);

      try {
         p("docompile " + var1++);
         this.right.getViewport().setView(this.compileTA);
         p("docompile " + var1++);
         PrintStream var3 = new PrintStream(var2);
         p("docompile " + var1++);
         Utils.compile(this.currentBean, var3);
         p("docompile " + var1++);
         var3.flush();
         p("docompile " + var1++);
      } catch (Exception var4) {
         this.displayException(var4);
      }

   }

   public void doSaveAs() {
      try {
         JFileChooser var1 = new JFileChooser(this.currentDirectory);
         var1.setFileFilter(new EJB2JSPFileChooser());
         boolean var2 = false;

         do {
            int var3 = var1.showDialog(this.frame, "Save ejb2jsp project");
            if (var3 == 1) {
               return;
            }

            this.currentDirectory = var1.getCurrentDirectory();
            File var4 = var1.getSelectedFile();
            String var5 = var4.getName();
            if (var5 != null) {
               if (!var5.endsWith(".ejb2jsp")) {
                  var5 = var5 + ".ejb2jsp";
                  var4 = new File(new File(var4.getParent()), var5);
               }

               if (var4.exists()) {
                  var3 = JOptionPane.showConfirmDialog(this.frame, "A file called \"" + var5 + "\" already exists.  Do " + "you want to overwrite it?", "Confirm file overwrite", 0, 3);
                  if (var3 == 1) {
                     continue;
                  }
               }

               this.currentProject = var4;
               this.saveCurrentBean();
               this.saveMenu.setEnabled(true);
               var2 = true;
            }
         } while(!var2);
      } catch (Exception var6) {
         this.displayException(var6);
      }

   }

   private void doExit() {
      try {
         this.prefs.save();
      } catch (Exception var5) {
         var5.printStackTrace();
      } finally {
         System.exit(0);
      }

   }

   public void doQuit() {
      if (this.askToSave()) {
         this.doExit();
      }

   }

   public void doOpen() {
      p("doOpen");

      try {
         JFileChooser var1 = new JFileChooser(this.currentDirectory);
         var1.setFileFilter(new EJB2JSPFileChooser());
         int var2 = var1.showDialog(this.frame, "Open ejb2jsp project");
         if (var2 == 0) {
            this.currentDirectory = var1.getCurrentDirectory();
            File var3 = var1.getSelectedFile();
            EJBTaglibDescriptor var4 = this.loadBeanFromFile(var3);
            this.setRootBean(var4);
            this.currentProject = var3;
            this.saveMenu.setEnabled(true);
            this.saveAsMenu.setEnabled(true);
            this.compileMenu.setEnabled(true);
         }
      } catch (Exception var5) {
         this.displayException(var5);
      }

   }

   public void doResolve() {
      try {
         Utils.resolveSources(this.currentBean);
      } catch (Exception var2) {
      }

   }

   public void doHelp() {
      try {
         this.initHelp();
         if (this.docFrame != null) {
            this.docFrame.setVisible(true);
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void doPrefs() {
      try {
         PreferencesDialog var1 = new PreferencesDialog(this.frame, "Preferences", true, this.prefs);
         var1.pack();
         AWTUtils.centerOnWindow(var1, this.frame);
         var1.show();
         this.prefs = var1.getPrefs();
      } catch (Exception var2) {
         this.displayException(var2);
      }

   }

   public void doNew() {
      try {
         JFileChooser var1 = new JFileChooser(this.currentDirectory);
         var1.setFileFilter(new JarFileChooser());
         int var2 = var1.showDialog(this.frame, "Open EJB Jar File");
         if (var2 == 1) {
            return;
         }

         this.currentDirectory = var1.getCurrentDirectory();
         File var3 = this.currentDirectory;
         File var4 = var1.getSelectedFile();
         this.currentBean = this.loadFromPaths(var4.getAbsolutePath(), var3.getAbsolutePath());
         this.currentBean.getFileInfo().setJavacPath(this.prefs.compiler);
      } catch (Exception var5) {
         this.displayException(var5);
      }

   }

   private EJBTaglibDescriptor loadFromPaths(String var1, String var2) throws Exception {
      try {
         if (this.prefs.sourceDir != null && (this.prefs.sourceDir = this.prefs.sourceDir.trim()).length() > 0) {
            var2 = this.prefs.sourceDir + File.pathSeparator + var2;
         }

         EJBTaglibDescriptor var3 = Utils.createDefaultDescriptor(var1, var2, this.prefs.webapp);
         this.setRootBean(var3);
         this.currentProject = null;
         this.saveMenu.setEnabled(false);
         this.saveAsMenu.setEnabled(true);
         this.compileMenu.setEnabled(true);
         return var3;
      } catch (Exception var4) {
         this.displayException(var4);
         return null;
      }
   }

   private File fixJChooserDirBug(File var1) {
      if (var1.exists() && var1.isDirectory()) {
         return var1;
      } else {
         String var2 = var1.getAbsolutePath().replace('/', File.separatorChar);
         int var3 = var2.lastIndexOf(File.separatorChar);
         if (var3 <= 0) {
            return var1;
         } else {
            String var4 = var2.substring(var3 + 1);
            String var5 = var2.substring(0, var3);
            var3 = var5.lastIndexOf(File.separatorChar);
            if (var3 <= 0) {
               return var1;
            } else {
               String var6 = var5.substring(var3 + 1);
               return var4.equals(var6) ? new File(var5) : var1;
            }
         }
      }
   }

   public void setRootBean(EJBTaglibDescriptor var1) {
      this.tree = null;
      this.left.setViewportView((Component)null);
      this.currentBean = var1;
      int var2 = this.split.getWidth();
      this.split.setDividerLocation(var2 / 4);
      if (var1 == null) {
         this.saveAsMenu.setEnabled(false);
         this.saveMenu.setEnabled(false);
         this.compileMenu.setEnabled(false);
         this.resolveMenu.setEnabled(false);
      } else {
         this.resolveMenu.setEnabled(true);

         try {
            this.tree = new EJBTaglibDescriptorTree(var1);
         } catch (Exception var4) {
            this.displayException(var4);
            return;
         }

         this.tree.addTreeSelectionListener(this);
         this.left.setViewportView(this.tree);
      }
   }

   private Image getFrameIcon() {
      if (this.frameIcon == null) {
         this.frameIcon = AWTUtils.loadImage(this.getClass().getClassLoader(), "/weblogic/graphics/W.gif");
      }

      return this.frameIcon;
   }

   private void initSplash() {
      this.splash = new JPanel();
      Image var1 = AWTUtils.loadImage(this.getClass().getClassLoader(), "/weblogic/graphics/logo-trans.gif");
      if (var1 != null) {
         ImageIcon var2 = new ImageIcon(var1);
         this.splash.add(new JLabel(var2));
         p("initSplash: added Image=" + var1);
      } else {
         p("initSplash: cannot find image");
      }

   }

   private void initHelp() {
      try {
         if (this.docFrame != null) {
            return;
         }

         URL var1 = this.getClass().getResource("documentation.html");
         InputStream var2 = var1.openStream();
         byte[] var3 = new byte[512];
         StringBuffer var4 = new StringBuffer();

         int var5;
         while((var5 = var2.read(var3)) > 0) {
            var4.append(new String(var3, 0, var5));
         }

         var2.close();
         JEditorPane var6 = new JEditorPane("text/html", var4.toString());
         var6.setEditable(false);
         JScrollPane var7 = new JScrollPane(var6);
         this.docFrame = new JFrame("documentation");
         Image var8 = this.getFrameIcon();
         if (var8 != null) {
            this.docFrame.setIconImage(var8);
         }

         this.docFrame.getContentPane().add(var7);
         this.docFrame.setSize(500, 400);
         this.docFrame.setLocation(200, 200);
         this.helpMenu.setEnabled(true);
         var1 = this.getClass().getResource("help.html");
         this.contextHelp = new HelpFrame("EJB to JSP Help", this.frame, var1);
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public static Main getInstance() {
      return theMain;
   }

   public void actionPerformed(ActionEvent var1) {
      Object var2 = var1.getSource();
      if (var2 instanceof JComponent) {
         JComponent var3 = (JComponent)var2;
         String var4 = (String)var3.getClientProperty("help-anchor");
         if (var4 != null) {
            p("help anchor: '" + var4 + "'");
            this.contextHelp.scroll2anchor(var4);
            return;
         }
      }

      Runnable var5 = null;
      if (var2 == this.saveMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doSave();
            }
         };
      } else if (var2 == this.saveAsMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doSaveAs();
            }
         };
      } else if (var2 == this.quitMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doQuit();
            }
         };
      } else if (var2 == this.openMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doOpen();
            }
         };
      } else if (var2 == this.newMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doNew();
            }
         };
      } else if (var2 == this.compileMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doCompile();
            }
         };
      } else if (var2 == this.resolveMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doResolve();
            }
         };
      } else if (var2 == this.helpMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doHelp();
            }
         };
      } else if (var2 == this.prefsMenu) {
         var5 = new Runnable() {
            public void run() {
               Main.this.doPrefs();
            }
         };
      }

      if (var5 != null) {
         this.pushTask(var5);
      }

   }

   public void start() throws Exception {
      this.prefs = new Prefs();
      this.prefs.load();
      this.initSplash();
      this.left = new JScrollPane((Component)null);
      this.right = new JScrollPane(this.splash);
      this.split = new JSplitPane(1, true, this.left, this.right);
      this.frame = new JFrame("EJB To JSP Tool");
      Image var1 = this.getFrameIcon();
      if (var1 != null) {
         this.frame.setIconImage(var1);
      }

      this.frame.addWindowListener(this);
      this.frame.getContentPane().add(this.split);
      this.mb = new JMenuBar();
      this.menu = new JMenu("File");
      this.menu.add(this.newMenu = new JMenuItem("New..."));
      this.menu.add(this.openMenu = new JMenuItem("Open..."));
      this.menu.add(this.compileMenu = new JMenuItem("Build Project"));
      this.menu.add(this.saveMenu = new JMenuItem("Save"));
      this.menu.add(this.saveAsMenu = new JMenuItem("Save As..."));
      this.menu.add(this.quitMenu = new JMenuItem("Quit"));
      this.menu.add(this.resolveMenu = new JMenuItem("Resolve Attributes..."));
      this.menu.add(this.prefsMenu = new JMenuItem("Preferences..."));
      this.resolveMenu.setEnabled(false);
      this.resolveMenu.setToolTipText("resolves tag attribute names against EJB interface sources");
      this.openMenu.setToolTipText("opens an existing ejb2jsp project file");
      this.newMenu.setToolTipText("creates a new ejb2jsp project from an EJB jar file");
      this.compileMenu.setToolTipText("builds the current project into the JSP tag library");
      this.openMenu.addActionListener(this);
      this.newMenu.addActionListener(this);
      this.compileMenu.addActionListener(this);
      this.saveMenu.addActionListener(this);
      this.saveAsMenu.addActionListener(this);
      this.quitMenu.addActionListener(this);
      this.resolveMenu.addActionListener(this);
      this.prefsMenu.addActionListener(this);
      this.mb.add(this.menu);
      this.helpMenu = new JMenuItem("Help");
      this.menu.add(this.helpMenu);
      this.helpMenu.addActionListener(this);
      this.helpMenu.setEnabled(false);
      Runnable var2 = new Runnable() {
         public void run() {
            Main.this.initHelp();
         }
      };
      this.pushTask(var2);
      this.frame.setJMenuBar(this.mb);
      this.frame.setSize(800, 500);
      this.frame.setLocation(200, 200);
      this.setRootBean(this.loadRootBean());
      this.split.setDividerLocation(200);
      this.frame.setVisible(true);
   }

   public static void main(String[] var0) throws Exception {
      System.setProperty("line.separator", "\n");
      System.setProperty("javax.xml.parsers.SAXParserFactory", "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl");
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
      AWTUtils.initLookAndFeel();
      Main var1 = new Main(var0);
      theMain = var1;
      var1.start();
   }

   private GUIPrintStream getGUIPrintStream() {
      if (this.guiPS == null) {
         this.compileTA = new JTextArea("", 20, 30);
         this.guiPS = new GUIPrintStream(this.compileTA);
      }

      this.compileTA.setText("");
      return this.guiPS;
   }

   public void displayException(Throwable var1) {
      System.err.println("error occurred: " + var1);
      var1.printStackTrace();
      String var2 = var1.toString();
      int var3 = var2.indexOf(13);
      int var4 = var2.indexOf(10);
      int var5 = Math.min(var3, var4);
      if (var5 > 0) {
         var2 = var2.substring(0, var5);
      }

      JOptionPane.showMessageDialog(this.frame, var2, "Error occurred", 0);
   }

   public void windowOpened(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
      if (this.askToSave()) {
         this.doExit();
      } else {
         this.frame.setVisible(true);
      }

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
}
