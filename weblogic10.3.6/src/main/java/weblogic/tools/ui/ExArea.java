package weblogic.tools.ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.StringTokenizer;
import javax.swing.JTextArea;
import javax.swing.border.Border;

class ExArea extends JTextArea {
   char[][] vals;
   int W;
   int H;
   int fh;
   static final int PAD = 10;
   boolean want2BeSeen = false;
   boolean outOfCTOR = false;
   static final int MAX_W = 600;
   static final int MAX_H = 600;

   public ExArea(String var1) {
      super(var1);
      this.parseStackTrace(var1);
      this.setEditable(false);
      this.outOfCTOR = true;
      this.setCaretPosition(0);
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      if (this.outOfCTOR) {
         this.parseStackTrace(this.getText());
      }

   }

   public void setBorder(Border var1) {
      super.setBorder(var1);
      if (this.outOfCTOR) {
         this.parseStackTrace(this.getText());
      }

   }

   private void parseStackTrace(String var1) {
      Font var2 = this.getFont();
      StringTokenizer var3 = new StringTokenizer(var1, "\t\n\r", false);
      this.vals = new char[var3.countTokens()][];
      FontMetrics var4 = Toolkit.getDefaultToolkit().getFontMetrics(var2);
      this.fh = var4.getHeight();
      this.W = this.H = 0;

      for(int var5 = 0; var5 < this.vals.length; ++var5) {
         if (var5 == 0) {
            this.vals[var5] = var3.nextToken().toCharArray();
         } else {
            this.vals[var5] = ("  " + var3.nextToken()).toCharArray();
         }

         this.W = Math.max(this.W, var4.charsWidth(this.vals[var5], 0, this.vals[var5].length));
         this.H += this.fh;
      }

      this.H += 40;
      this.W += 40;
      this.W = Math.min(this.W, 600);
      this.H = Math.min(this.H, 600);
      Border var7 = this.getBorder();
      if (var7 != null) {
         Insets var6 = var7.getBorderInsets(this);
         if (var6 != null) {
            this.H += var6.top + var6.bottom;
            this.W += var6.left + var6.right;
         }
      }
   }

   static void p(String var0) {
      System.err.println("[ExArea]: " + var0);
   }
}
