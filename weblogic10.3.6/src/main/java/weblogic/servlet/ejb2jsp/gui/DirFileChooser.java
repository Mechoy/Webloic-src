package weblogic.servlet.ejb2jsp.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

class DirFileChooser extends FileFilter {
   public boolean accept(File var1) {
      return var1.isDirectory();
   }

   public String getDescription() {
      return "Directories";
   }
}
