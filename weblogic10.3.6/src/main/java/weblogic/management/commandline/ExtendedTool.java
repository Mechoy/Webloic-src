package weblogic.management.commandline;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.Tool;

public class ExtendedTool extends Tool {
   private HashMap childCommandMap = new HashMap();
   protected PrintStream printStream;

   public ExtendedTool() {
      this.printStream = System.out;
   }

   public void addSubCommand(String var1, ExtendedTool var2) {
      this.childCommandMap.put(var1, var2);
   }

   public void removeSubCommand(String var1) {
      this.childCommandMap.remove(var1);
   }

   public Getopt2 getOpts() {
      return this.opts;
   }

   public void prepare() {
      this.opts = new Getopt2();
      Iterator var1 = this.childCommandMap.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         ExtendedTool var3 = (ExtendedTool)this.childCommandMap.get(var2);
         String var4 = var3.getOpts().getUsageArgs();
         String var5 = var3.getOpts().getUsageHeader();
         this.opts.addArgFlag(var2, "<" + var4 + ">", var5);
      }

      if (this.childCommandMap.size() > 0) {
         this.opts.setFailOnUnrecognizedOpts(false);
         this.setRequireExtraArgs(true);
      }

   }

   public final void run(String[] var1) throws Exception {
      if (this.childCommandMap.size() == 0) {
         super.run(var1);
      } else {
         this.run(var1, 0);
      }

   }

   private void run(String[] var1, int var2) throws Exception {
      if (this.childCommandMap.size() == 0) {
         super.run(var1);
      } else {
         ExtendedTool var3 = null;
         int var4 = var2;

         for(int var5 = var1.length; var4 < var5; ++var4) {
            String var6 = var1[var4];
            String var7 = var6.startsWith("-") ? var6.substring(1) : var6;
            if ((var3 = (ExtendedTool)this.childCommandMap.get(var7)) != null) {
               var1[var4] = "";
               var3.run(var1, var4 + 1);
               return;
            }
         }

      }
   }

   public void runBody() throws Exception {
      throw new IllegalAccessError("This method should be overwritten by extended Class Or addSubCommand() should be called ");
   }

   public static void main(String[] var0) throws Exception {
      new ExtendedTool();
   }
}
