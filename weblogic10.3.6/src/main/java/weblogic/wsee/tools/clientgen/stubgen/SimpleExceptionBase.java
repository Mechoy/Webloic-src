package weblogic.wsee.tools.clientgen.stubgen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import weblogic.wsee.util.jspgen.JspGenBase;
import weblogic.xml.schema.binding.internal.NameUtil;

public abstract class SimpleExceptionBase extends JspGenBase {
   protected String packageName = "examples.temp";
   protected String exceptionName;
   protected String partName;
   protected String javaTypeName;
   private String destDir = ".";
   private Set generated = new HashSet();

   public void setPackage(String var1) {
      this.packageName = var1;
   }

   public void setDestDir(String var1) {
      this.destDir = var1;
   }

   public Set getGenerated() {
      return this.generated;
   }

   public void visit(String var1, Class var2, String var3) throws IOException {
      if (!Exception.class.isAssignableFrom(var2)) {
         PrintStream var4 = null;
         this.partName = NameUtil.getJavaName(var3);
         this.javaTypeName = this.getTypeName(var2);
         this.exceptionName = NameUtil.getJAXRPCClassName(var1);
         if (this.destDir != null) {
            String var5 = this.destDir + File.separator + this.packageName.replace('.', File.separatorChar);
            (new File(var5)).mkdirs();
            File var6 = new File(var5, var1 + ".java");
            this.generated.add(var6.getAbsolutePath());
            var4 = new PrintStream(new FileOutputStream(var6), true);
            this.setOutput(var4);
         }

         this.generate();
         if (var4 != null) {
            var4.close();
         }

      }
   }

   private String getTypeName(Class var1) {
      String var2;
      for(var2 = ""; var1.getComponentType() != null; var2 = var2 + "[]") {
         var1 = var1.getComponentType();
      }

      return var1.getName() + var2;
   }
}
