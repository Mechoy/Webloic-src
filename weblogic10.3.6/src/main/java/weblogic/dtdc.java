package weblogic;

import java.util.ArrayList;
import java.util.Arrays;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;
import weblogic.xml.dtdc.DTD2Java;
import weblogic.xml.dtdc.DTD2Parser;

public class dtdc extends Tool {
   protected CodeGenerator dtd2java;
   protected CodeGenerator dtd2parser;
   protected CompilerInvoker compiler;
   private boolean returnCompilerErrors;

   public dtdc(String[] var1) {
      super(var1);
   }

   public String getCompilerErrors() {
      return this.compiler.getCompilerErrors();
   }

   public void setWantCompilerErrors(boolean var1) {
      this.returnCompilerErrors = var1;
   }

   public void prepare() {
      this.opts.addFlag("noparser", "Disable generation of the parser");
      this.dtd2java = new DTD2Java(this.opts);
      this.dtd2parser = new DTD2Parser(this.opts);
      this.compiler = new CompilerInvoker(this.opts);
   }

   public void runBody() throws Exception {
      String[] var1 = this.dtd2java.generate(this.opts.args());
      String[] var2 = this.opts.hasOption("noparser") ? new String[0] : this.dtd2parser.generate(this.opts.args());
      if (!this.opts.hasOption("nowrite")) {
         this.compiler.setWantCompilerErrors(this.returnCompilerErrors);
         ArrayList var3 = new ArrayList();
         var3.addAll(Arrays.asList((Object[])var1));
         var3.addAll(Arrays.asList((Object[])var2));
         this.compiler.compile((String[])((String[])var3.toArray(new String[var3.size()])));
      }
   }

   public static void main(String[] var0) throws Exception {
      (new dtdc(var0)).run();
   }
}
