package weblogic.wsee.util.jspgen;

import java.io.PrintStream;

public class Output {
   private PrintStream output;

   public Output(PrintStream var1) {
      this.output = var1;
   }

   public void print(Object var1) {
      this.output.print(var1 == null ? "null" : var1.toString());
   }

   public void println(Object var1) {
      this.output.println(var1 == null ? "null" : var1.toString());
   }
}
