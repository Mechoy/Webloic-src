package weblogic.servlet.ejb2jsp.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Prefs {
   public String compiler;
   public String webapp;
   public String sourceDir;
   private static final String PROP_FILE = ".ejb2jsp.properties";

   public String toString() {
      return "compiler=" + this.compiler + " app=" + this.webapp + " src=" + this.sourceDir;
   }

   public void load() throws IOException {
      String var1 = System.getProperty("user.home");
      File var2 = new File(var1);
      File var3 = new File(var2, ".ejb2jsp.properties");
      String var4 = (new File(var2, "webapp")).getAbsolutePath();
      String var5 = (new File(var2, "src")).getAbsolutePath();
      if (var3.exists()) {
         FileInputStream var6 = new FileInputStream(var3);
         Properties var7 = new Properties();

         try {
            var7.load(var6);
         } finally {
            var6.close();
         }

         this.compiler = var7.getProperty("compiler", "javac");
         this.webapp = var7.getProperty("webapp", var4);
         this.sourceDir = var7.getProperty("sourceDir", var5);
      } else {
         this.compiler = "javac";
         this.webapp = var4;
         this.sourceDir = var5;
      }

   }

   public void save() throws IOException {
      Properties var1 = new Properties();
      var1.setProperty("compiler", this.compiler);
      var1.setProperty("webapp", this.webapp);
      var1.setProperty("sourceDir", this.sourceDir);
      String var2 = System.getProperty("user.home");
      File var3 = new File(var2);
      File var4 = new File(var3, ".ejb2jsp.properties");
      FileOutputStream var5 = new FileOutputStream(var4);

      try {
         var1.store(var5, "ejb2jsp user preferences");
      } finally {
         var5.close();
      }

   }
}
