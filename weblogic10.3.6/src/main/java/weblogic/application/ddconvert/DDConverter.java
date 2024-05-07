package weblogic.application.ddconvert;

import java.io.File;
import java.io.IOException;
import weblogic.application.ApplicationFileManager;
import weblogic.utils.FileUtils;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFile;

public final class DDConverter extends Tool {
   private File inputFile;
   private File workingDir;
   private boolean deleteWorkingDir;

   public DDConverter(String[] var1) {
      super(var1);
   }

   private static void p(String var0) {
      ConvertCtx.debug(var0);
   }

   public void prepare() {
      this.setUsageName("weblogic.DDConverter");
      this.opts.setUsageArgs("<EAR, JAR, RAR or WAR file/directory>");
      this.opts.addOption("d", "dir", "Directory where descriptors are written");
      this.opts.addFlag("verbose", "Turns on additional output for debugging");
      this.opts.addFlag("quiet", "Turns off output except for errors");
      this.opts.markPrivate("version");
   }

   private ConvertCtx setupFiles(String var1) throws ToolFailureException, IOException {
      boolean var2 = this.opts.hasOption("verbose");
      boolean var3 = this.opts.hasOption("quiet");
      this.inputFile = new File(var1);
      if (!this.inputFile.exists()) {
         throw new ToolFailureException("InputDirectory: " + this.inputFile + " does not exist or could not be read.");
      } else {
         if (var2) {
            p("inputFile " + this.inputFile.getAbsolutePath());
         }

         String var4 = this.opts.getOption("d");
         if (var4 == null) {
            throw new ToolFailureException("-d must be specified to indicate where DDConverter should write the descriptors");
         } else {
            File var5 = new File(var4);
            if (var2) {
               p("outputDir " + var5.getAbsolutePath());
            }

            if (var5.exists()) {
               if (!var5.isDirectory()) {
                  throw new ToolFailureException("-d " + var5 + " is not a directory.  Please specify a directory for output.");
               }
            } else {
               var5.mkdirs();
            }

            if (this.inputFile.isDirectory()) {
               this.workingDir = this.inputFile;
               this.deleteWorkingDir = false;
               if (var2) {
                  p("Using exploded dir " + this.workingDir.getAbsolutePath());
               }
            } else {
               this.workingDir = FileUtils.createTempDir(this.inputFile.getName());
               this.deleteWorkingDir = true;
               if (var2) {
                  p("extracting jar to " + this.workingDir.getAbsolutePath());
               }

               JarFileUtils.extract(this.inputFile, this.workingDir);
            }

            return new ConvertCtx(ApplicationFileManager.newInstance(this.workingDir), var5, var2, var3);
         }
      }
   }

   private void example() {
      this.opts.usageError("weblogic.DDConverter");
      System.out.println("");
      System.out.println("Example: java weblogic.DDConverter -d tmpdir my.ear");
      System.out.println("");
   }

   public void runBody() throws ToolFailureException, IOException, DDConvertException {
      String[] var1 = this.opts.args();
      if (var1 != null && var1.length != 0) {
         ConvertCtx var2 = this.setupFiles(var1[0]);
         VirtualJarFile var3 = null;

         try {
            var3 = var2.getAppVJF();
            Converter var4 = ConverterFactory.findConverter(var2, var3);
            if (var4 == null) {
               throw new ToolFailureException("The application at " + this.inputFile + " was not recognized as a valid application.\n\n" + "\nFor EAR Files, ensure there is a META-INF/application.xml." + "\nFor EJB-JAR files, ensure there is a META-INF/ejb-jar.xml" + "\nFor WAR files, ensure there is a WEB-INF/web.xml" + "\nFor RAR files, ensure there is a META-INF/ra.xml");
            }

            if (!var2.isQuiet()) {
               var4.printStartMessage(this.inputFile.getName());
            }

            var4.convertDDs(var2, var3, var2.getOutputDir());
            if (!var2.isQuiet()) {
               var4.printEndMessage(this.inputFile.getName());
            }
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var10) {
               }
            }

         }

         if (this.deleteWorkingDir) {
            FileUtils.remove(this.workingDir);
         }

      } else {
         this.example();
      }
   }

   public static void main(String[] var0) throws Exception {
      (new DDConverter(var0)).run();
   }
}
