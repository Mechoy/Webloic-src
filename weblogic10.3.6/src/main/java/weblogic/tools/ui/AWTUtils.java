package weblogic.tools.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

public class AWTUtils {
   private static final boolean debug = false;
   protected static boolean confirm = false;

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
         URL var1 = AWTUtils.class.getResource(var0);
         if (var1 == null) {
            var1 = AWTUtils.class.getResource("/weblogic/graphics/" + var0);
         }

         Toolkit var2 = Toolkit.getDefaultToolkit();
         return var2.createImage((ImageProducer)var1.getContent());
      } catch (Exception var3) {
         return null;
      }
   }

   public static URL getResourceURL(String var0) {
      URL var1 = AWTUtils.class.getResource(var0);
      if (var1 == null) {
         var1 = AWTUtils.class.getResource("/weblogic/graphics/" + var0);
      }

      return var1;
   }

   public static GridBagConstraints gbc(int var0, int var1, int var2, int var3) {
      GridBagConstraints var4 = new GridBagConstraints();
      var4.fill = 1;
      var4.gridx = var0;
      var4.gridy = var1;
      var4.gridwidth = var2;
      var4.gridheight = var3;
      if (var4.gridwidth > 1) {
         var4.weightx = (double)var4.gridwidth;
      }

      var4.insets = new Insets(5, 5, 5, 5);
      return var4;
   }

   public static void frontAndCenter(Window var0) {
      Dimension var1 = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension var2 = var0.getSize();
      int var3 = (var1.width - var2.width) / 2;
      int var4 = (var1.height - var2.height) / 2;
      var0.setLocation(new Point(var3, var4));
   }

   public static void centerOnWindow(Window var0, Window var1) {
      if (var1 != null && var1.isVisible()) {
         Dimension var2 = var0.getSize();
         Dimension var3 = var1.getSize();
         Point var4 = var1.getLocation();
         if (var3.width - var2.width < 20 && var3.height - var2.height < 20) {
            var4.x += 20;
         } else {
            var4.x += (var3.width - var2.width) / 2;
         }

         var4.y += (var3.height - var2.height) / 2;
         var4.x = Math.max(var4.x, 5);
         var4.y = Math.max(var4.y, 5);
         var0.setLocation(var4);
      } else {
         frontAndCenter(var0);
      }
   }

   public static Point getNextCascadeLocation(Point var0, Window var1) {
      int var2 = var0.x + 20;
      int var3 = var0.y + 20;
      Dimension var4 = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension var5 = var1.getSize();
      if (var2 + var5.height > var4.height || var3 + var5.height > var4.height) {
         var2 = 0;
         var3 = 0;
      }

      return new Point(var2, var3);
   }

   public static boolean confirm(Frame var0, String var1) {
      int var2 = JOptionPane.showConfirmDialog(var0, "Confirm", var1, 0);
      boolean var3 = false;
      switch (var2) {
         case 0:
            var3 = true;
            break;
         case 1:
            var3 = false;
            break;
         default:
            throw new Error("Boolean logic fails us!");
      }

      return var3;
   }

   public static void message(Frame var0, String var1) {
      JOptionPane.showMessageDialog(var0, "Error", var1, 0);
   }

   public static boolean confirm(String var0) {
      return confirm((Frame)null, var0);
   }

   public static void message(String var0) {
      message((Frame)null, var0);
   }

   public static void initLookAndFeel() {
      initLookAndFeel((String)null, false);
   }

   public static void initLookAndFeel(String var0, boolean var1) {
      LookAndFeel var2 = UIManager.getLookAndFeel();
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
         } catch (Exception var8) {
            if (var1) {
               System.err.println("Could not set LookAndFeel to " + var0 + ": " + var8.toString());
            }

            var0 = null;
         }
      }

      try {
         String var3 = System.getProperty("os.name");
         if (var3 != null) {
            var3 = var3.toLowerCase();
            if (var3.indexOf("digital unix") >= 0 || var3.indexOf("linux") >= 0 || var3.indexOf("solaris") >= 0) {
               UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
               return;
            }
         }
      } catch (Exception var7) {
      }

      if (var0 == null) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var6) {
            if (var1) {
               System.err.println("Trying to set SystemLookAndFeel: " + var6.toString());
            }

            try {
               UIManager.setLookAndFeel(var2);
            } catch (Exception var5) {
            }
         }
      }

   }
}
