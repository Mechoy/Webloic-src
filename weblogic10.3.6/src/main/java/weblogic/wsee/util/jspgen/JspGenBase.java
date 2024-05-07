package weblogic.wsee.util.jspgen;

import java.io.PrintStream;

public abstract class JspGenBase {
   protected PrintStream out;

   public JspGenBase() {
      this.out = System.out;
   }

   public void setOutput(PrintStream var1) {
      this.out = var1;
   }

   public void preGenerate() throws ScriptException {
   }

   public abstract void generate() throws ScriptException;

   public void postGenerate() throws ScriptException {
   }
}
