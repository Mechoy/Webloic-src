package weblogic.ant.taskdefs.antline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.Task;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;

public class AntTool {
   static boolean debug = System.getProperty(DEBUG_PROPERTY()) != null;
   private Class antTask;
   protected Getopt2 cmdOpts = new Getopt2();
   private Map antOpts = new HashMap();
   private List antArgs = new ArrayList();
   private int optCount = 0;

   static String DEBUG_PROPERTY() {
      return "anttool.debug";
   }

   public AntTool(String var1) throws AntLineException {
      this.antTask = loadClass(var1, Task.class);
      this.cmdOpts.setFailOnUnrecognizedOpts(true);
   }

   public void addOption(String var1, String var2, String var3, String var4, ArgConverter var5) {
      if (debug) {
         Debug.assertion(var2 != null);
         Debug.assertion(var2.length() > 0);
      }

      if (var1 == null) {
         this.antArgs.add(new AntOpt(var2, var5));
      } else {
         this.antOpts.put(var1, new AntOpt(var2, var5));
         if (var3 != null) {
            this.cmdOpts.addOption(var1, var3, var4);
         } else {
            this.cmdOpts.addFlag(var1, var4);
         }
      }

   }

   public void run(String[] var1) throws Exception {
      this.cmdOpts.grok(var1);
      Iterator var2 = this.antOpts.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         String var4 = this.cmdOpts.getOption(var3);
         if (var4 != null) {
            AntOpt var5 = (AntOpt)this.antOpts.get(var3);
            var5.setValue(var4);
         }
      }

      String[] var6 = this.cmdOpts.args();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         ((AntOpt)this.antArgs.get(var7)).setValue(var6[var7]);
      }

      ArrayList var8 = new ArrayList(this.antArgs);
      var8.addAll(this.antOpts.values());
      AntLauncher var9 = new AntLauncher(this.antTask, var8);
      var9.launch();
   }

   public void usageAndExit(String var1) {
      this.cmdOpts.usageAndExit(var1);
   }

   protected static Class loadClass(String var0, Class var1) throws AntLineException {
      Class var2 = null;

      try {
         var2 = Class.forName(var0);
      } catch (Exception var4) {
         throw new AntLineException("Could not load class " + var0 + ". Is it in the classpath?");
      }

      if (var1 != null && !var1.isAssignableFrom(var2)) {
         throw new AntLineException(var0 + " must be implement/extend " + var1.getName());
      } else {
         return var2;
      }
   }
}
