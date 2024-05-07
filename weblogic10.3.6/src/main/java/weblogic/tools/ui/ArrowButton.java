package weblogic.tools.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.UIManager;

public class ArrowButton extends JButton {
   public static final int UP = 1;
   public static final int DOWN = 5;
   private int direction;
   private boolean allowFocusTraverse = true;
   private Dimension prefSize;

   public ArrowButton(int var1) {
      this.direction = var1 == 1 ? 1 : 5;
      this.setOpaque(false);
   }

   public Dimension getPreferredSize() {
      if (this.prefSize != null) {
         return new Dimension(this.prefSize.width, this.prefSize.height);
      } else {
         Dimension var1 = super.getPreferredSize();
         Font var2 = this.getFont();
         FontMetrics var3 = Toolkit.getDefaultToolkit().getFontMetrics(var2);
         int var4 = Math.min(var1.width, var3.getHeight() + 8);
         return new Dimension(var4, var4);
      }
   }

   public void setPreferredSize(Dimension var1) {
      this.prefSize = var1;
   }

   public void setFocusTraversable(boolean var1) {
      this.allowFocusTraverse = var1;
   }

   public boolean isFocusTraversable() {
      return this.allowFocusTraverse;
   }

   public void paint(Graphics var1) {
      Dimension var2 = this.getSize();
      super.paint(var1);
      int var3 = var2.width;
      int var4 = var2.height;
      if (this.getModel().isPressed()) {
         var1.translate(1, 1);
      }

      this.paintTriangle(var1, 3 * var3 / 8, 3 * var4 / 8, var4 / 4, this.direction, this.isEnabled());
   }

   public void paintTriangle(Graphics var1, int var2, int var3, int var4, int var5, boolean var6) {
      Color var7 = var1.getColor();
      int var10 = 0;
      var4 = Math.max(var4, 2);
      int var8 = var4 / 2;
      var1.translate(var2, var3);
      if (var6) {
         var1.setColor(Color.black);
      } else {
         var1.setColor(UIManager.getDefaults().getColor("controlShadow"));
      }

      int var9;
      switch (var5) {
         case 1:
            for(var9 = 0; var9 < var4; ++var9) {
               var1.drawLine(var8 - var9, var9, var8 + var9, var9);
            }

            if (!var6) {
               var1.setColor(UIManager.getDefaults().getColor("controlHighlight"));
               var1.drawLine(var8 - var9 + 2, var9, var8 + var9, var9);
            }
            break;
         case 5:
            if (!var6) {
               var1.translate(1, 1);
               var1.setColor(UIManager.getDefaults().getColor("controlHighlight"));

               for(var9 = var4 - 1; var9 >= 0; --var9) {
                  var1.drawLine(var8 - var9, var10, var8 + var9, var10);
                  ++var10;
               }

               var1.translate(-1, -1);
               var1.setColor(UIManager.getDefaults().getColor("controlShadow"));
            }

            var10 = 0;

            for(var9 = var4 - 1; var9 >= 0; --var9) {
               var1.drawLine(var8 - var9, var10, var8 + var9, var10);
               ++var10;
            }
      }

   }
}
