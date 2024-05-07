package weblogic.corba.rmic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import sun.rmi.rmic.Main;
import weblogic.corba.rmi.Stub;
import weblogic.kernel.Kernel;
import weblogic.rmi.extensions.server.DescriptorHelper;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.rmic.Remote2JavaConstants;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Debug;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerator;

public final class Remote2Corba extends CodeGenerator implements Remote2JavaConstants {
   private static final boolean debug = false;
   private boolean verbose = false;
   private Getopt2 opts;
   private String iiopDirectory;
   private boolean triesToUseClassLoader = false;
   private boolean disableHotCodGen = false;
   public static final String IIOP_VERBOSE = "verbose";

   public Remote2Corba(Getopt2 var1) {
      super(var1);
      addGeneratorOptions(var1);
      this.opts = var1;
      if (this.verbose) {
         Debug.say("opts ok");
      }

   }

   public static void addGeneratorOptions(Getopt2 var0) {
      var0.addFlag("iiop", "Generate iiop stubs from servers");
      var0.addFlag("iiopTie", "Generate CORBA skeletons, uses Sun's rmic.");
      var0.addFlag("iiopSun", "Use Sun's rmic for generating CORBA stubs.");
      var0.addOption("iiopDirectory", "directory", "Specify the directory where IIOP proxy classes will be written (overrides target directory)");
   }

   String getIIOPDirectory() {
      return this.iiopDirectory;
   }

   protected void extractOptionValues(Getopt2 var1) {
      this.iiopDirectory = var1.getOption("iiopDirectory", (String)null);
      this.verbose = var1.hasOption("verbose");
      this.disableHotCodGen = var1.hasOption("disableHotCodeGen");
      if (Utilities.getClassLoader() != null && this.iiopDirectory != null) {
         this.triesToUseClassLoader = true;
         this.iiopDirectory = super.getRootDirectoryName();
      }

      if (null == this.iiopDirectory) {
         this.iiopDirectory = super.getRootDirectoryName();
      }

      if (null == this.iiopDirectory) {
         this.iiopDirectory = ".";
      }

   }

   public Enumeration outputs(Object[] var1) throws Exception {
      Kernel.ensureInitialized();
      String[] var2 = (String[])((String[])var1);
      boolean var3 = this.opts.hasOption("keepgenerated");
      boolean var4 = this.opts.hasOption("iiopTie");
      boolean var5 = this.opts.hasOption("iiopSun") || var4;
      String var6 = this.getIIOPDirectory();
      String var7 = this.opts.getOption("classpath");
      Class[] var10;
      int var11;
      if (var5) {
         if (this.opts.hasOption("descriptor")) {
            System.err.println("WARNING: -descriptor not supported in combination with -iiopTie or -iiopSun, ignored.");
         }

         ArrayList var8 = new ArrayList();
         var8.add("-iiop");
         var8.add("-always");
         if (var3) {
            var8.add("-keepgenerated");
         }

         if (var7 != null) {
            var8.add("-classpath");
            var8.add(var7);
         }

         if (var6 != null) {
            var8.add("-d");
            var8.add(var6);
         }

         int var9;
         if (var4) {
            for(var9 = 0; var9 < var2.length; ++var9) {
               var8.add(var2[var9]);
            }
         } else {
            var8.add("-nolocalstubs");

            for(var9 = 0; var9 < var2.length; ++var9) {
               var10 = StubGenerator.getAllInterfaces(Utilities.classForName(var2[var9]));

               for(var11 = 0; var11 < var10.length; ++var11) {
                  var8.add(var10[var11].getName());
               }
            }
         }

         String[] var14 = new String[var8.size()];
         Object[] var15 = var8.toArray();

         for(var11 = 0; var11 < var15.length; ++var11) {
            var14[var11] = (String)var15[var11];
         }

         if (this.verbose) {
            StringBuffer var17 = new StringBuffer("sun.rmi.rmic ");

            for(int var12 = 0; var12 < var14.length; ++var12) {
               var17.append(var14[var12] + " ");
            }

            Debug.say(var17.toString());
         }

         if (this.triesToUseClassLoader) {
            System.err.println("WARNING: iiopDirectory not supported in combination with this classloader, ignored.");
         }

         Main var18 = new Main(System.out, "rmic");
         var18.compile(var14);
      } else {
         int var13;
         for(var13 = 0; var13 < var2.length; ++var13) {
            if (this.verbose) {
               Debug.say("weblogic.rmic -iiop -d " + this.getIIOPDirectory() + " " + var2[var13]);
            }

            Class var16 = Utilities.classForName(var2[var13]);
            if (!var16.isInterface()) {
               this.generateStub(var16);
            }

            var10 = StubGenerator.getAllInterfaces(var16);

            for(var11 = 0; var11 < var10.length; ++var11) {
               this.generateStub(var10[var11]);
            }
         }

         if (this.disableHotCodGen) {
            for(var13 = 0; var13 < var2.length; ++var13) {
               this.generateIIOPStub(var2[var13]);
            }
         }
      }

      return (new Hashtable()).elements();
   }

   private void generateStub(Class var1) throws Exception {
      if (this.verbose) {
         Debug.say("weblogic.rmic -iiop -d " + this.getIIOPDirectory() + var1.getName());
      }

      StubGenerator var2 = new StubGenerator(var1);
      String var3 = var2.getClassName().replace('.', File.separatorChar) + ".class";
      ensureDirectoryExists(this.getIIOPDirectory(), var3);
      FileOutputStream var4 = new FileOutputStream(this.getIIOPDirectory() + File.separatorChar + var3);
      var2.write(var4);
      var4.close();
   }

   private static void ensureDirectoryExists(String var0, String var1) {
      String var2 = "";
      int var3 = var1.lastIndexOf(File.separatorChar);
      if (var3 != -1) {
         var2 = var1.substring(0, var3);
      }

      String var4 = var0 + File.separatorChar + var2;
      File var5 = new File(var4);
      var5.mkdirs();
   }

   public void generateIIOPStubs() {
   }

   private void generateIIOPStub(String var1) throws ClassNotFoundException {
      FileOutputStream var2 = null;

      try {
         Kernel.ensureInitialized();
         ClassLoader var3 = Utilities.getClassLoader();
         Class var4 = null;

         try {
            var4 = Class.forName(var1);
         } catch (ClassNotFoundException var20) {
            if (var3 != null) {
               var4 = var3.loadClass(var1);
            }
         }

         RuntimeDescriptor var5 = DescriptorHelper.getDescriptor(var4);
         weblogic.rmi.internal.StubGenerator var6 = new weblogic.rmi.internal.StubGenerator(var5, var1 + "_IIOP_WLStub", Stub.class.getName());
         String var7 = var6.getClassName().replace('.', File.separatorChar);
         File var8 = new File(super.getRootDirectoryName());
         var8.mkdir();
         var8 = new File(super.getRootDirectoryName() + File.separatorChar + var7 + ".class");
         String var9 = var8.getParent();
         if (var9 != null) {
            File var10 = new File(var9);
            if (!var10.exists()) {
               var10.mkdirs();
            }
         }

         var2 = new FileOutputStream(var8);
         var6.write(var2);
      } catch (Exception var21) {
         var21.printStackTrace();
         System.exit(1);
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var19) {
         }

      }

   }
}
