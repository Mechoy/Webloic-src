package weblogic.servlet.ejb2jsp;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class HomeInterfaceGenerator extends CodeGenerator {
   String imports;
   String iface;
   String rtype;
   String pkg;
   String simpleName;

   static void p(String var0) {
      System.err.println("[HIG]: " + var0);
   }

   public HomeInterfaceGenerator(Getopt2 var1, String var2, String var3, String var4) {
      super(var1);
      this.imports = var2;
      int var5 = var3.lastIndexOf(46);
      this.pkg = var3.substring(0, var5);
      this.iface = var3.substring(var5 + 1);
      this.rtype = var4;
   }

   public String importStatements() {
      return this.imports;
   }

   public String homeTagInterfaceName() {
      return this.iface;
   }

   public String remoteType() {
      return this.rtype;
   }

   public String packageStatement() {
      return "package " + this.pkg + ";";
   }

   public String getTemplatePath() {
      return "/weblogic/servlet/ejb2jsp/hometaginterface.j";
   }

   protected Enumeration outputs(Object[] var1) throws IOException, CodeGenerationException {
      TaglibOutput var2 = new TaglibOutput(this.iface + ".java", this.getTemplatePath(), this.pkg);
      Vector var3 = new Vector();
      var3.addElement(var2);
      return var3.elements();
   }
}
