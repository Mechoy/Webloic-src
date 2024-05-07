package weblogic.tools.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import weblogic.Home;

public class Util {
   private static final boolean debug = false;
   private static int PATH_DISPLAY_THRESHOLD_LEN = 40;
   public static final int YES = 0;
   public static final int NO = 1;
   public static final int CANCEL = 2;

   public static void centerWindow(Window var0, Window var1) {
      Point var2 = var1.getLocation();
      Point var3 = new Point();
      Dimension var4 = var1.getSize();
      Dimension var5 = var0.getSize();
      var3.x = var2.x + var4.width / 2 - var5.width / 2;
      var3.y = var2.y + var4.height / 2 - var5.height / 2;
      if (var3.x < 0) {
         var3.x = var2.x;
      }

      if (var3.y < 0) {
         var3.y = var2.y;
      }

      var0.setLocation(var3);
   }

   public static void centerWindow(Window var0) {
      Point var1 = new Point();
      Dimension var2 = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension var3 = var0.getSize();
      var1.x = var2.width / 2 - var3.width / 2;
      var1.y = var2.height / 2 - var3.height / 2;
      if (var1.x < 0) {
         var1.x = 0;
      }

      if (var1.y < 0) {
         var1.y = 0;
      }

      var0.setLocation(var1);
   }

   public static void frontAndCenter(Window var0) {
      centerWindow(var0);
   }

   public static void frontAndCenter(Window var0, Window var1) {
      centerWindow(var0, var1);
   }

   public static String[] splitCompletely(String var0, String var1, boolean var2) {
      return splitCompletely(new StringTokenizer(var0, var1, var2));
   }

   public static String[] splitCompletely(String var0, String var1) {
      return splitCompletely(new StringTokenizer(var0, var1));
   }

   public static String[] splitCompletely(String var0) {
      return splitCompletely(new StringTokenizer(var0));
   }

   private static String[] splitCompletely(StringTokenizer var0) {
      int var1 = var0.countTokens();
      String[] var2 = new String[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = var0.nextToken();
      }

      return var2;
   }

   public static void initLookAndFeel(String var0) {
      LookAndFeel var1 = UIManager.getLookAndFeel();
      if (var0 != null) {
         try {
            if (var0.equals("java")) {
               UIManager.setLookAndFeel("javax.swing.jlf.JLFLookAndFeel");
            } else if (var0.equals("basic")) {
               UIManager.setLookAndFeel("javax.swing.plaf.basic.BasicLookAndFeel");
            } else if (var0.equals("metal")) {
               UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } else if (var0.equals("motif")) {
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            } else if (var0.startsWith("win")) {
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } else {
               UIManager.setLookAndFeel(var0);
            }

            return;
         } catch (Exception var7) {
            var0 = null;
         }
      }

      try {
         String var2 = System.getProperty("os.name");
         if (var2 != null) {
            var2 = var2.toLowerCase();
            if (var2.indexOf("digital unix") >= 0 || var2.indexOf("linux") >= 0 || var2.indexOf("solaris") >= 0) {
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
               return;
            }
         }
      } catch (Exception var6) {
      }

      if (var0 == null) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var5) {
            try {
               UIManager.setLookAndFeel(var1);
            } catch (Exception var4) {
            }
         }
      }

   }

   public static Image loadImage(ClassLoader var0, String var1) {
      URL var2 = var0.getResource(var1);
      if (var2 == null) {
         return loadImage(var1);
      } else {
         Toolkit var3 = Toolkit.getDefaultToolkit();

         try {
            return var3.createImage((ImageProducer)var2.getContent());
         } catch (IOException var5) {
            return null;
         }
      }
   }

   public static Image loadImage(String var0) {
      try {
         URL var1 = Util.class.getResource(var0);
         if (var1 == null) {
            var1 = Util.class.getResource("/weblogic/graphics/" + var0);
         }

         Toolkit var2 = Toolkit.getDefaultToolkit();
         return var2.createImage((ImageProducer)var1.getContent());
      } catch (Exception var3) {
         return null;
      }
   }

   public static URL getResourceURL(String var0) {
      URL var1 = null;
      int var2 = var0.lastIndexOf(35);
      if (var2 >= 0) {
         String var3 = var0.substring(var2 + 1);
         var0 = var0.substring(0, var2);
         var1 = Util.class.getResource(var0);
         if (var1 != null) {
            try {
               var1 = new URL(var1, '#' + var3);
            } catch (MalformedURLException var5) {
            }
         }
      } else {
         var1 = Util.class.getResource(var0);
         if (var1 == null) {
            var1 = Util.class.getResource("/weblogic/graphics/" + var0);
         }
      }

      return var1;
   }

   public static String getResourceString(String var0, ResourceBundle var1) {
      try {
         return var1.getString(var0);
      } catch (MissingResourceException var4) {
         String var3 = "Missing string resource(" + var0 + ")";
         System.out.println(var3);
         return var3;
      }
   }

   public static char getResourceChar(String var0, ResourceBundle var1) {
      try {
         return var1.getString(var0).charAt(0);
      } catch (MissingResourceException var3) {
         var3.printStackTrace();
         return '\u0000';
      }
   }

   public static void setTextFieldEnabled(JTextField var0, boolean var1) {
      var0.setEnabled(var1);
      var0.repaint();
   }

   public static void setLabelEnabled(JLabel var0, boolean var1) {
      var0.setEnabled(var1);
      var0.repaint();
   }

   public static void setPanelEnabled(JPanel var0, boolean var1) {
      Component[] var2 = var0.getComponents();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] instanceof JPanel) {
            setPanelEnabled((JPanel)var2[var3], var1);
         }

         if (var2[var3] instanceof JTextField) {
            setTextFieldEnabled((JTextField)var2[var3], var1);
         } else if (var2[var3] instanceof JLabel) {
            setLabelEnabled((JLabel)var2[var3], var1);
         } else {
            var2[var3].setEnabled(var1);
            var2[var3].repaint();
         }
      }

   }

   public static JFrame getComponentFrame(Component var0) {
      return (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, var0);
   }

   public static Window getComponentWindow(Component var0) {
      return (Window)SwingUtilities.getAncestorOfClass(Window.class, var0);
   }

   public static String getTypeName(Class var0) {
      if (var0.isArray()) {
         try {
            Class var1 = var0;
            int var2 = 0;

            StringBuffer var3;
            for(var3 = new StringBuffer(); var1.isArray(); var1 = var1.getComponentType()) {
               ++var2;
            }

            var3.append(var1.getName());

            for(int var4 = 0; var4 < var2; ++var4) {
               var3.append("[]");
            }

            return var3.toString();
         } catch (Throwable var5) {
         }
      }

      return var0.getName();
   }

   public static String format(String var0, Object[] var1) {
      return MessageFormat.format(var0, var1);
   }

   public static File getFile(String var0) {
      File var1 = new File(var0);

      try {
         return new File(var1.getCanonicalPath());
      } catch (IOException var3) {
         return var1;
      }
   }

   public static String getPath(File var0) {
      if (var0 != null) {
         try {
            return var0.getCanonicalPath();
         } catch (Exception var2) {
            return var0.getAbsolutePath();
         }
      } else {
         return null;
      }
   }

   public static String getPathLeaf(String var0) {
      if (var0.indexOf(File.separatorChar) != -1) {
         var0 = var0.replace(File.separatorChar, '/');
      }

      return var0.substring(var0.lastIndexOf(47) + 1);
   }

   public static String getPathParent(String var0) {
      if (var0.indexOf(File.separatorChar) != -1) {
         var0 = var0.replace(File.separatorChar, '/');
      }

      return var0.substring(0, var0.lastIndexOf(47) + 1);
   }

   public static boolean mkdirs(File var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("null directory file");
      } else {
         if (!var0.exists()) {
            String var1 = getPath(var0);
            int var2 = var1.length();
            if (var2 > PATH_DISPLAY_THRESHOLD_LEN) {
               var1 = "..." + var1.substring(var2 - PATH_DISPLAY_THRESHOLD_LEN);
            }

            String var3 = "The directory\n" + var1 + "\ndoes not exist. Create it?";
            if (confirm((Component)null, "Create directory?", var3)) {
               return var0.mkdirs();
            }
         }

         return true;
      }
   }

   public static boolean confirm(Component var0, String var1, String var2) {
      Object[] var3 = new Object[]{"Yes", "No"};
      int var4 = JOptionPane.showOptionDialog(var0, var2, var1, 0, 3, (Icon)null, var3, var3[1]);
      boolean var5 = false;
      switch (var4) {
         case 0:
            var5 = true;
            break;
         case 1:
            var5 = false;
            break;
         default:
            throw new Error("Boolean logic fails us!");
      }

      return var5;
   }

   public static int confirmWithCancel(Component var0, String var1, String var2) {
      Object[] var3 = new Object[]{"Yes", "No", "Cancel"};
      return JOptionPane.showOptionDialog(var0, var2, var1, 1, 3, (Icon)null, var3, var3[0]);
   }

   public static void copyFile(InputStream var0, File var1) throws IOException {
      byte[] var2 = new byte[1024];
      FileOutputStream var3 = new FileOutputStream(var1);
      BufferedOutputStream var4 = new BufferedOutputStream(var3);

      int var5;
      while((var5 = var0.read(var2)) > 0) {
         var4.write(var2, 0, var5);
      }

      var0.close();
      var4.close();
   }

   public static void copyFile(File var0, File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var0);
      BufferedInputStream var3 = new BufferedInputStream(var2);
      copyFile((InputStream)var3, var1);
   }

   public static URL toURL(File var0) throws MalformedURLException {
      String var1 = var0.getAbsolutePath();
      if (File.separatorChar != '/') {
         var1 = var1.replace(File.separatorChar, '/');
      }

      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      if (!var1.endsWith("/") && var0.isDirectory()) {
         var1 = var1 + "/";
      }

      return new URL("file", "", var1);
   }

   public static void gatherFiles(File var0, Hashtable var1) {
      String[] var2 = var0.list();
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            File var3 = new File(var0, var2[var4]);
            if (var3.isDirectory()) {
               gatherFiles(var3, var1);
            } else {
               var1.put(var3.getPath(), var3);
            }
         }
      }

   }

   public static void clearFiles(File var0) {
      if (var0 != null && var0.isDirectory() && var0.exists()) {
         String[] var1 = var0.list();
         if (var1 != null) {
            for(int var3 = 0; var3 < var1.length; ++var3) {
               File var2 = new File(var0, var1[var3]);
               var2.delete();
            }
         }
      }

   }

   public static String getFileURL(String var0) {
      String var1 = Home.getPath();
      var1 = var1.replace(File.separatorChar, '/');
      return var1 + "/classes" + var0;
   }

   private static void focusOnFirstWidget(JComponent var0) {
      if (null != var0) {
         JComponent var1 = null;
         JComponent[] var2 = findAllFocusableComponents(var0);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            JComponent var4 = var2[var3];
            if (var3 == 0) {
               var1 = var4;
            } else {
               ppp("THIS:" + var4.getX() + " " + var4.getY());
               ppp("FIRST:" + var1.getX() + " " + var1.getY());
               if (var4.getX() < var1.getX() && var4.getY() < var1.getY()) {
                  ppp("NEW FIRST COMP AT " + var4.getX() + " " + var4.getY());
                  var1 = var4;
               }
            }
         }

         if (null != var1) {
            var1.requestFocus();
         }
      }

   }

   private static JComponent[] findAllFocusableComponents(Component var0) {
      ArrayList var1 = new ArrayList();
      if (var0 instanceof JComponent) {
         findAllFocusableComponents((JComponent)var0, var1);
      }

      return (JComponent[])((JComponent[])var1.toArray(new JComponent[0]));
   }

   private static void findAllFocusableComponents(Component var0, List var1) {
      if (var0 instanceof JComponent) {
         Component[] var2 = ((JComponent)var0).getComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (isInputWidget(var2[var3])) {
               var1.add(var2[var3]);
            } else {
               ArrayList var4 = new ArrayList();
               findAllFocusableComponents(var2[var3], var4);
               Iterator var5 = var4.iterator();

               while(var5.hasNext()) {
                  Object var6 = var5.next();
                  var1.add(var6);
               }
            }
         }
      }

   }

   private static boolean isInputWidget(Component var0) {
      return var0 instanceof JComboBox || var0 instanceof JTextComponent || var0 instanceof JToggleButton || var0 instanceof NumberBox;
   }

   private static void ppp(String var0) {
      System.out.println("[Util] " + var0);
   }
}
