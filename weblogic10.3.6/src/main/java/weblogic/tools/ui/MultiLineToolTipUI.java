package weblogic.tools.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicToolTipUI;

class MultiLineToolTipUI extends BasicToolTipUI {
   private String[] strs;
   private int maxWidth = 0;

   public void paint(Graphics var1, JComponent var2) {
      Font var3 = var1.getFont();
      FontMetrics var4 = Toolkit.getDefaultToolkit().getFontMetrics(var3);
      int var5 = var4.getHeight();
      Dimension var6 = var2.getSize();
      var1.setColor(var2.getBackground());
      var1.fillRect(0, 0, var6.width, var6.height);
      var1.setColor(var2.getForeground());
      if (this.strs != null) {
         for(int var7 = 0; var7 < this.strs.length; ++var7) {
            var1.drawString(this.strs[var7], 3, var5 * (var7 + 1));
         }
      }

   }

   public Dimension getPreferredSize(JComponent var1) {
      Font var2 = var1.getFont();
      FontMetrics var3 = Toolkit.getDefaultToolkit().getFontMetrics(var2);
      String var4 = ((JToolTip)var1).getTipText();
      if (var4 == null) {
         var4 = "";
      }

      BufferedReader var5 = new BufferedReader(new StringReader(var4));
      int var6 = 0;
      Vector var7 = new Vector();

      String var8;
      int var9;
      try {
         while((var8 = var5.readLine()) != null) {
            var9 = SwingUtilities.computeStringWidth(var3, var8);
            var6 = var6 < var9 ? var9 : var6;
            var7.addElement(var8);
         }
      } catch (IOException var10) {
         var10.printStackTrace();
      }

      int var11 = var7.size();
      if (var11 < 1) {
         this.strs = null;
         var11 = 1;
      } else {
         this.strs = new String[var11];
         var7.copyInto(this.strs);
      }

      var9 = var3.getHeight() * var11;
      this.maxWidth = var6;
      return new Dimension(var6 + 6, var9 + 6);
   }
}
