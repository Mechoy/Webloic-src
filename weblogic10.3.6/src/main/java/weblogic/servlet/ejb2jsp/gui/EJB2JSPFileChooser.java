package weblogic.servlet.ejb2jsp.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class EJB2JSPFileChooser extends FileFilter {
   public boolean accept(File var1) {
      if (var1 != null) {
         if (var1.isDirectory()) {
            return true;
         }

         String var2 = var1.getName();
         if (var2 != null) {
            return var2.endsWith(".ejb2jsp");
         }
      }

      return false;
   }

   public String getDescription() {
      return "ejb2jsp project files";
   }
}
