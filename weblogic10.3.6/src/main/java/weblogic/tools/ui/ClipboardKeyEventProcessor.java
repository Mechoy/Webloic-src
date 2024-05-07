package weblogic.tools.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClipboardKeyEventProcessor extends KeyAdapter {
   private boolean controlPressed = false;

   public void keyPressed(KeyEvent var1) {
      if (var1.getKeyCode() == 17) {
         this.controlPressed = true;
      }

      if (this.controlPressed) {
         char var2 = var1.getKeyChar();
         if (var2 != 'c' && var2 == 'C') {
         }

         if (var2 != 'x' && var2 == 'X') {
         }

         if (var2 != 'v' && var2 == 'V') {
         }
      }

   }

   public void keyReleased(KeyEvent var1) {
      if (var1.getKeyCode() == 17) {
         this.controlPressed = false;
      }

   }
}
