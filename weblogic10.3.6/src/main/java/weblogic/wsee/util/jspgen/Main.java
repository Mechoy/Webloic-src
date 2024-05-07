package weblogic.wsee.util.jspgen;

import java.io.File;
import java.io.IOException;

public class Main implements ResourceProvider {
   private String jspfile;
   private String outputFileName;
   private String className;
   private String packageName;
   private String outputDir;
   private File root;

   public Main(String var1, String var2) throws IOException, ScriptException {
      this.jspfile = var1;
      this.outputDir = var2;
      this.root = (new File(var1)).getParentFile();
      if (this.root == null) {
         this.root = new File(".");
      }

      int var3 = var1.lastIndexOf(".");
      String var4 = var3 == -1 ? var1 : var1.substring(0, var3);
      var3 = var4.lastIndexOf("/");
      this.className = var3 == -1 ? var4 : var4.substring(var3 + 1, var4.length());
   }

   public Main(String var1, String var2, boolean var3) throws IOException {
      this(var1, var2);
      if (var3) {
         this.generate();
      }

   }

   public String getFullyQualifiedClassName() {
      return this.packageName + "." + this.className;
   }

   public String getJavaFileName() {
      return this.outputFileName;
   }

   public String getResource(String var1) throws ScriptException {
      File var2 = var1.startsWith(File.separator) ? new File(var1) : new File(this.root, var1);
      return Util.fileToString(var2.getPath());
   }

   private void generate() throws ScriptException {
      String var1 = Util.fileToString(this.jspfile);
      StringBuffer var2 = new StringBuffer();
      StringBuffer var3 = new StringBuffer();
      StringBuffer var4 = new StringBuffer();
      LightJspParser var5 = new LightJspParser(var1, this);
      var5.parse(var2, var4, var3);
      StringBuffer var6 = new StringBuffer();
      String var7 = var5.getBaseClass();
      if (var7 == null) {
         var7 = JspGenBase.class.getName();
      }

      var6.append(var3);
      var6.append("public class ").append(this.className).append(" ");
      var6.append("extends ");
      var6.append(var7).append("{\n");
      var6.append(var4);
      var6.append(var2);
      var6.append("}");
      this.packageName = var5.getPackageName();
      if (this.packageName == null) {
         throw new ScriptException("Package name not specified in " + this.jspfile);
      } else {
         this.outputFileName = this.outputDir + File.separator + this.packageName.replace('.', File.separatorChar) + File.separator + this.className + ".java";
         Util.stringToFile(this.outputFileName, var6.toString());
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 2) {
         System.out.println("usage: java Main <output-dir> <jspgen-files>");
      } else {
         for(int var1 = 1; var1 < var0.length; ++var1) {
            Main var2 = new Main(var0[var1], var0[0]);
            var2.generate();
         }
      }

   }
}
