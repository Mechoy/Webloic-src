package weblogic.wsee.tools.jarmaker;

import weblogic.utils.compiler.Tool;
import weblogic.wsee.util.Verbose;

public class Main extends Tool {
   private static final boolean verbose = Verbose.isVerbose(Main.class);

   public void prepare() throws Exception {
      this.opts.setUsageArgs("[options]");
      this.opts.addOption("destdir", "destdir", "Name of the output file");
      this.opts.addOption("package", "package", "Name of the package");
      this.opts.addOption("wsdl", "wsdl", "location of the WSDL file");
   }

   public void runBody() throws Exception {
      throw new Error("NIY");
   }
}
