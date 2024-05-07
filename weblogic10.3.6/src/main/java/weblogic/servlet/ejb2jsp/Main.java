package weblogic.servlet.ejb2jsp;

import java.io.File;
import java.io.FileOutputStream;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;
import weblogic.utils.compiler.CodeGenerator;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.Tool;
import weblogic.utils.io.XMLWriter;

public class Main extends Tool {
   static void p(String var0) {
      System.err.println("[Main]: " + var0);
   }

   public static void main(String[] var0) throws Exception {
      System.setProperty("line.separator", "\n");
      System.setProperty("javax.xml.parsers.SAXParserFactory", "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl");
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl");

      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (var0[var1].equals("-projectfile")) {
            String[] var2 = new String[var0.length + 1];
            System.arraycopy(var0, 0, var2, 0, var0.length);
            var2[var0.length] = "_BOGUS_";
            var0 = var2;
            break;
         }
      }

      try {
         (new Main(var0)).run();
      } catch (Throwable var3) {
         var3.printStackTrace();
         System.exit(1);
      }

   }

   Main(String[] var1) throws Exception {
      super(var1);
      this.opts.addOption("sourcePath", "directory path", "look in this path for source files of EJB interface(s)");
      this.opts.addAlias("sourceDir", "sourcePath");
      this.opts.addOption("package", "java-pkg", "put results into this java pkg");
      this.opts.addOption("tld", "tld-path", "save the taglib's tld to this file");
      this.opts.addFlag("nokeepgenerated", "delete the generated java files");
      this.opts.addFlag("enableBaseEJB", "enable generation of tags for base EJB methods (e.g., EJBObject.remove(), EJBHome.getHomeHandle(), etc).  These are disabled by default, as they typically cause conflicts in projects with multiple EJBs.");
      this.opts.addOption("projectfile", "project descriptor file", "generate using parameters in previously generated descriptor file");
      this.opts.addOption("saveproject", "project file location", "save generated descriptor file to this location");
      new CompilerInvoker(this.opts);
      CodeGenerator var10001 = new CodeGenerator(this.opts) {
      };
   }

   private void fail(String var1) {
      System.err.println("Fatal error: " + var1);
   }

   public void prepare() throws Exception {
   }

   public void runBody() throws Exception {
      EJBTaglibDescriptor var1 = null;
      String var2 = null;
      if ((var2 = this.opts.getOption("projectfile")) != null) {
         var1 = EJBTaglibDescriptor.load(new File(var2));
         byte var3 = 0;
         boolean var10 = false;

         label95: {
            XMLWriter var4;
            label96: {
               try {
                  var10 = true;
                  Utils.compile(var1, System.out);
                  var10 = false;
                  break label96;
               } catch (Exception var11) {
                  String var5 = var11.toString();
                  if (var11 instanceof RuntimeException) {
                     var5 = var11.getMessage();
                  }

                  System.err.println("project build failed: " + var5);
                  var3 = 1;
                  var10 = false;
               } finally {
                  if (var10) {
                     if ((var2 = this.opts.getOption("saveproject")) == null) {
                        System.exit(var3);
                     }

                     XMLWriter var7 = new XMLWriter(new FileOutputStream(var2));
                     var1.toXML(var7);
                     var7.flush();
                     var7.close();
                     System.out.println("project file written to: " + var2);
                  }
               }

               if ((var2 = this.opts.getOption("saveproject")) == null) {
                  System.exit(var3);
               }

               var4 = new XMLWriter(new FileOutputStream(var2));
               var1.toXML(var4);
               var4.flush();
               var4.close();
               System.out.println("project file written to: " + var2);
               break label95;
            }

            if ((var2 = this.opts.getOption("saveproject")) == null) {
               System.exit(var3);
            }

            var4 = new XMLWriter(new FileOutputStream(var2));
            var1.toXML(var4);
            var4.flush();
            var4.close();
            System.out.println("project file written to: " + var2);
         }

         System.exit(var3);
      } else {
         throw new RuntimeException("");
      }
   }
}
