package weblogic.management.tools;

import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;

public class XMLElementMBeanImplc extends Tool {
   private CodeGenerator generator;
   private CompilerInvoker compiler;

   private XMLElementMBeanImplc(String[] var1) {
      super(var1);
   }

   public static void main(String[] var0) throws Exception {
      (new XMLElementMBeanImplc(var0)).run();
   }

   public void prepare() {
      this.generator = new XMLElementMBeanImplGenerator(this.opts);
      this.compiler = new CompilerInvoker(this.opts);
   }

   public void runBody() throws Exception {
      String[] var1 = this.generator.generate(this.opts.args());
   }
}
