package weblogic.ant.taskdefs.antline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;
import weblogic.webservice.util.script.GenBase;

public class AntLineTask extends Task {
   static boolean debug = System.getProperty(DEBUG_PROPERTY()) != null;
   private String packageName;
   private String commandName;
   private String taskName;
   private File destDir = new File(".");
   private boolean keepGenerated = true;
   private List options = new ArrayList();

   static String DEBUG_PROPERTY() {
      return "antline.debug";
   }

   public void setPackagename(String var1) {
      this.packageName = var1;
   }

   public void setCommandname(String var1) {
      this.commandName = var1;
   }

   public void setTaskName(String var1) {
      this.taskName = var1;
   }

   public void setDestdir(File var1) {
      this.destDir = var1;
   }

   public void setKeepgenerated(boolean var1) {
      this.keepGenerated = var1;
   }

   public Option createOption() {
      Option var1 = new Option();
      this.options.add(var1);
      return var1;
   }

   public void execute() {
      this.validateAttributes();
      this.log("Generating command line tool " + this.packageName + "." + this.commandName + " ...");
      String var1 = "/" + this.getMyPackage().replace('.', '/');
      File var2 = null;
      PrintStream var3 = null;

      try {
         GenBase var4 = new GenBase(var1 + "/tool.cg", false);
         var4.setVar("packageName", this.packageName);
         var4.setVar("cmdName", this.commandName);
         var4.setVar("taskName", this.taskName);
         var4.setVar("options", this.options);
         StringBuffer var5 = new StringBuffer();
         Iterator var6 = this.options.iterator();

         while(var6.hasNext()) {
            Option var7 = (Option)var6.next();
            String var8 = "\"" + var7.getOptname() + "\"";
            String var9 = "\"" + var7.getAntname() + "\"";
            String var10 = quotedValOrNullText(var7.getArg());
            String var11 = quotedValOrNullText(var7.getDescription());
            String var12 = quotedValOrNullText(var7.getConverter());
            var5.append("    tool.addOption(");
            var5.append(var8);
            var5.append(", ");
            var5.append(var9);
            var5.append(", ");
            var5.append(var10);
            var5.append(", ");
            var5.append(var11);
            var5.append(", ");
            var5.append(var12);
            var5.append(");\n");
         }

         var4.setVar("addOptions", var5.toString());
         var2 = new File(this.destDir, this.packageName.replace('.', '/') + "/" + this.commandName + ".java");
         var2.getParentFile().mkdirs();
         if (debug) {
            System.out.println("Outputting Java file " + var2);
         }

         var3 = new PrintStream(new FileOutputStream(var2));
         var4.setOutput(var3);
         var4.gen();
      } catch (Exception var22) {
         throw new BuildException(var22);
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }
         } catch (Exception var21) {
            throw new BuildException(var21);
         }

      }

      this.log("Compiling " + var2 + " ...");
      this.javacFile(var2, this.destDir);
      if (!this.keepGenerated) {
         System.out.println("Deleting file " + var2);

         try {
            var2.delete();
         } catch (Exception var20) {
            throw new BuildException(var20);
         }
      }

   }

   private void validateAttributes() throws BuildException {
      if (this.packageName == null) {
         throw new BuildException("the packageName attribute must be set");
      } else if (this.commandName == null) {
         throw new BuildException("the commandName attribute must be set");
      } else if (this.taskName == null) {
         throw new BuildException("the taskName attribute must be set");
      } else {
         Iterator var1 = this.options.iterator();

         while(var1.hasNext()) {
            Option var2 = (Option)var1.next();
            var2.validateAttributes();
         }

      }
   }

   private void javacFile(File var1, File var2) throws BuildException {
      Javac var3 = new Javac();
      var3.setProject(this.getProject());
      var3.setTaskName(this.getTaskName());

      try {
         var3.setSrcdir(new Path(this.getProject(), var1.getParentFile().getCanonicalPath()));
      } catch (IOException var5) {
         throw new BuildException(var5);
      }

      var3.setIncludes(var1.getName());
      var3.setDestdir(var2);
      var3.execute();
   }

   private String getMyPackage() {
      String var1 = this.getClass().getName();
      return var1.substring(0, var1.lastIndexOf(46));
   }

   private static String quotedValOrNullText(String var0) {
      return var0 == null ? "null" : "\"" + cleanupText(var0) + "\"";
   }

   private static String cleanupText(String var0) {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      var0 = var0.trim();

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         if (Character.isWhitespace(var4)) {
            if (!var2) {
               var1.append(' ');
            }

            var2 = true;
         } else {
            if (var4 == '"') {
               var1.append('\\');
            }

            var1.append(var4);
            var2 = false;
         }
      }

      return var1.toString();
   }
}
