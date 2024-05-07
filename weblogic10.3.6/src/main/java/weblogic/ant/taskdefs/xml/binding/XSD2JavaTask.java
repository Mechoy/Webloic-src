package weblogic.ant.taskdefs.xml.binding;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

public class XSD2JavaTask extends Task {
   private List mFileSets = null;
   private String mSchema = "";
   private String mPackage = null;
   private String mDestDir = null;
   private boolean mReadOnly = false;
   private boolean mNoValidation = false;
   private boolean mExtension = false;
   private String mSrc = null;
   private String mBinding = null;
   private static final String XJC_COMPILER = "com.sun.tools.xjc.Driver";
   private static final String SEP;
   private static final String DEPOT_ENV = "depot";
   private static final String SRC_ENV = "src";
   private static final String DEV = "dev";
   private static final String[] JAXB_JARS;

   public void setExtension(boolean var1) {
      this.mExtension = var1;
   }

   public void setSchema(String var1) {
      this.mSchema = var1;
   }

   public void setPackage(String var1) {
      this.mPackage = var1;
   }

   public void setDestDir(String var1) {
      this.mDestDir = var1;
   }

   public void setNoValidation(boolean var1) {
      this.mNoValidation = var1;
   }

   public void setReadOnly(boolean var1) {
      this.mReadOnly = var1;
   }

   public void setBinding(String var1) {
      this.mBinding = var1;
   }

   public void addFileSet(FileSet var1) {
      if (this.mFileSets == null) {
         this.mFileSets = new ArrayList();
      }

      this.mFileSets.add(var1);
   }

   public void execute() throws BuildException {
      String var1 = this.getSrcDir() + SEP;
      Java var2 = new Java();
      var2.setTaskName("xjc");
      var2.setProject(this.getProject());
      Path var3 = var2.createClasspath();

      for(int var4 = 0; var4 < JAXB_JARS.length; ++var4) {
         var3.addExisting(new Path(this.getProject(), var1 + JAXB_JARS[var4]));
      }

      var3.addExisting(Path.systemClasspath);
      var2.setClasspath(var3);
      var2.setFork(true);
      var2.setClassname("com.sun.tools.xjc.Driver");
      if (this.mDestDir != null) {
         var2.createArg().setLine("-d " + this.mDestDir);
      }

      if (this.mPackage != null) {
         var2.createArg().setLine("-p " + this.mPackage);
      }

      if (this.mBinding != null) {
         var2.createArg().setLine("-b " + this.mBinding);
      }

      if (this.mExtension) {
         var2.createArg().setLine("-extension");
      }

      if (this.mReadOnly) {
         var2.createArg().setLine("-readOnly");
      }

      if (this.mNoValidation) {
         var2.createArg().setLine("-nv");
      }

      if (this.mFileSets != null) {
         Iterator var10 = this.mFileSets.iterator();

         while(var10.hasNext()) {
            FileSet var5 = (FileSet)var10.next();
            DirectoryScanner var6 = var5.getDirectoryScanner(this.getProject());
            String[] var7 = var6.getIncludedFiles();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               File var9 = new File(var6.getBasedir(), var7[var8]);
               this.mSchema = this.mSchema + " " + var9;
            }
         }
      }

      var2.createArg().setLine(this.mSchema);
      this.log("Running xjc on '" + this.mSchema + "'");
      var2.execute();
   }

   private final String getSrcDir() {
      if (this.mSrc != null) {
         return this.mSrc;
      } else {
         Vector var1 = Execute.getProcEnvironment();
         if (var1 != null) {
            String var2 = null;
            String var3 = null;

            for(int var4 = 0; var4 < var1.size(); ++var4) {
               String var5 = (String)var1.get(var4);
               int var6 = var5.indexOf("=");
               if (var6 != -1 && var6 != var5.length() - 1) {
                  String var7 = var5.substring(0, var6).trim();
                  if (var7.equalsIgnoreCase("depot")) {
                     var2 = var5.substring(var6 + 1);
                  } else if (var7.equalsIgnoreCase("src")) {
                     var3 = var5.substring(var6 + 1);
                  }
               }
            }

            if (var2 != null && var3 != null) {
               return var2 + SEP + "dev" + SEP + var3;
            }
         }

         this.log("WARNING: could not determine src root, using '.'");
         return ".";
      }
   }

   static {
      SEP = File.separator;
      JAXB_JARS = new String[]{"3rdparty" + SEP + "jaxb" + SEP + "jaxb-api.jar", "3rdparty" + SEP + "jaxb" + SEP + "jaxb-libs.jar", "3rdparty" + SEP + "jaxb" + SEP + "jaxb-ri.jar", "3rdparty" + SEP + "jaxb" + SEP + "jaxb-xjc.jar", "3rdparty" + SEP + "jaxb" + SEP + "sax.jar", "3rdparty" + SEP + "jaxb" + SEP + "xercesImpl.jar"};
   }
}
