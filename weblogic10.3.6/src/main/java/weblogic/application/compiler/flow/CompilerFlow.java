package weblogic.application.compiler.flow;

import weblogic.application.compiler.CompilerCtx;
import weblogic.utils.compiler.ToolFailureException;

public abstract class CompilerFlow {
   protected final CompilerCtx ctx;
   protected static boolean debug = false;

   public CompilerFlow(CompilerCtx var1) {
      this.ctx = var1;
   }

   public abstract void compile() throws ToolFailureException;

   public abstract void cleanup() throws ToolFailureException;

   protected void say(String var1) {
      this.say(var1, false);
   }

   protected void say(String var1, boolean var2) {
      if (var2) {
         System.out.println("");
      }

      System.out.println(var1);
   }
}
