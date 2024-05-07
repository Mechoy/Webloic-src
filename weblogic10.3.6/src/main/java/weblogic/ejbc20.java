package weblogic;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import weblogic.ejb.container.compliance.EJBComplianceTextFormatter;
import weblogic.ejb.container.deployer.EJBDescriptorMBeanUtils;
import weblogic.ejb.spi.EJBC;
import weblogic.ejb.spi.EJBCFactory;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.kernel.Kernel;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.BadOptionException;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.FileUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.ClasspathClassFinder2;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.compiler.ICompilerFactory;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.JarFileUtils;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public class ejbc20 extends Tool {
   private EJBComplianceTextFormatter fmt = new EJBComplianceTextFormatter();
   private static final boolean debug = false;
   private static final boolean verbose = false;
   private static final String EJBC_WORKING_DIR = "ejbcgen";
   private String sourceJarFileName;
   private String targetJarFileName;
   private File inputJar;
   private String appId;
   private EjbDescriptorBean ejbDescriptor;
   private File outputDir;
   private boolean runFromCmdLine = true;
   protected ICompilerFactory compilerFactory = null;
   private boolean doClose = true;
   private boolean createOutputJar = true;
   private VirtualJarFile myJar = null;
   private GenericClassLoader m_classLoader;
   private Class[] bugs = new Class[]{Error.class, NullPointerException.class, IndexOutOfBoundsException.class, AssertionError.class};

   public ejbc20(String[] var1) {
      super(var1);
      if (Kernel.isServer()) {
         this.runFromCmdLine = false;
      }

   }

   public void setRunFromCmdLine(boolean var1) {
      this.runFromCmdLine = var1;
   }

   public void prepare() {
      this.opts.addFlag("nodeploy", "Do not unpack jar files into the target dir.");
      this.opts.setUsageArgs("<source directory or jar file> [<target directory or jar file>]");
      this.opts.addFlag("idl", "Generate idl for remote interfaces");
      this.opts.addFlag("idlOverwrite", "Always overwrite existing IDL files");
      this.opts.addFlag("idlVerbose", "Display verbose information for IDL generation");
      this.opts.addFlag("idlNoValueTypes", "Do not generate valuetypes and methods/attributes that contain them.");
      this.opts.addFlag("idlNoAbstractInterfaces", "Do not generate abstract interfaces and methods/attributes that contain them.");
      this.opts.addFlag("idlFactories", "Generate factory methods for valuetypes.");
      this.opts.addFlag("idlVisibroker", "Generate IDL somewhat compatible with Visibroker 4.5 C++.");
      this.opts.addFlag("idlOrbix", "Generate IDL somewhat compatible with Orbix 2000 2.0 C++.");
      this.opts.addOption("idlDirectory", "dir", "Specify the directory where IDL files will be created (default : target directory or jar)");
      this.opts.addFlag("iiop", "Generate CORBA stubs.");
      this.opts.addFlag("iiopSun", "Use Sun's rmic for generating CORBA stubs.");
      this.opts.markPrivate("iiopSun");
      this.opts.addFlag("iiopTie", "Generate CORBA skeletons, uses Sun's rmic.");
      this.opts.markPrivate("iiopTie");
      this.opts.addOption("iiopDirectory", "dir", "Specify the directory where IIOP stub files will be written (default : target directory or jar)");
      this.opts.addOption("idlMethodSignatures", "", "Specify the method signatures used to trigger idl code generation.");
      this.opts.addFlag("idlCompatibility", "Substitute structs for value types to generate CORBA 2.2 compatible IDL.");
      this.opts.markPrivate("idlCompatibility");
      this.opts.addOption("dispatchPolicy", "policy", "Specify the Dispatch Policy for this EJB");
      this.opts.markPrivate("dispatchPolicy");
      this.opts.addFlag("stickToFirstServer", "Enables sticky load balancing");
      this.opts.markPrivate("stickToFirstServer");
      this.opts.addFlag("nocompliance", "Do not run the EJB Compliance Checker.");
      this.opts.addFlag("forceGeneration", "Force generation of wrapper classes.  Without this flag the classes may not be regenerated if it is determined to be unnecessary.");
      this.opts.addFlag("basicClientJar", "Generate a client jar that does not contain deployment descriptors.");
      this.opts.addFlag("convertDDs", "Convert old 1.1 deployment descriptors to 7.0. If this flag is set, ejbc invokes ddconverter to convert to 70 descriptors.");
      this.opts.addFlag("disableHotCodeGen", "Generate ejb stub and skel as part of ejbc. Avoid HotCodeGen to have better performance.");
      new CompilerInvoker(this.opts);
      this.opts.markPrivate("nowrite");
      this.opts.markPrivate("commentary");
      this.opts.markPrivate("nodeploy");
      this.opts.markPrivate("O");
      this.opts.markPrivate("d");
      this.opts.markPrivate("nocompliance");

      try {
         this.opts.setFlag("nowarn", true);
      } catch (BadOptionException var2) {
         throw new AssertionError(var2);
      }
   }

   private String getSourceJarFileName(Getopt2 var1) {
      String[] var2 = var1.args();
      return var2[0];
   }

   private void makeOutputDir(String var1) throws ToolFailureException {
      this.outputDir = new File(var1);
      if (!this.outputDir.exists()) {
         if (!this.outputDir.mkdir()) {
            throw new ToolFailureException("ERROR: Ejbc ould not create temporary working directory:" + this.outputDir.getAbsolutePath() + ".  Please ensure that this directory can be created.");
         }
      } else if (!this.outputDir.canWrite()) {
         throw new ToolFailureException("ERROR: Ejbc can not write to the temporary working directory:" + this.outputDir.getAbsolutePath() + ".  Please ensure that you have write permission for this directory.  You may also specify an alternative output directory on the weblogic.ejbc command line.");
      }

   }

   private String getTargetJarFileName(Getopt2 var1) throws ToolFailureException {
      String[] var2 = var1.args();
      this.targetJarFileName = var1.getOption("d");
      if (this.targetJarFileName == null) {
         if (var2.length == 1) {
            this.targetJarFileName = var2[0];
         } else {
            this.targetJarFileName = var2[1];
         }
      }

      if (!this.targetJarFileName.endsWith(".jar") && !this.targetJarFileName.endsWith(".JAR")) {
         this.makeOutputDir(this.targetJarFileName);
         this.targetJarFileName = null;
      } else {
         this.outputDir = new File("ejbcgen");
         if (this.outputDir.exists()) {
            FileUtils.remove(new File("ejbcgen"));
         }

         this.makeOutputDir("ejbcgen");
      }

      return this.targetJarFileName;
   }

   private void validateToolInputs() throws ToolFailureException {
      if (this.opts.args().length >= 1 && this.opts.args().length <= 2) {
         this.sourceJarFileName = this.getSourceJarFileName(this.opts);
         if (this.inputJar == null) {
            this.inputJar = new File(this.sourceJarFileName);
         }

         if (!this.inputJar.exists()) {
            throw new ToolFailureException("ERROR: source file: " + this.sourceJarFileName + " could not be found.");
         } else if (!this.inputJar.isDirectory() && !this.sourceJarFileName.endsWith(".jar")) {
            throw new ToolFailureException("ERROR: You must specify a source directory or ejb-jar file ending with the suffix .jar to run weblogic.ejbc");
         } else {
            this.appId = this.inputJar.getName();
            this.targetJarFileName = this.getTargetJarFileName(this.opts);

            try {
               this.opts.setOption("d", this.outputDir.getPath());
            } catch (BadOptionException var2) {
               throw new AssertionError(var2);
            }
         }
      } else {
         this.opts.usageError("weblogic.ejbc");
         throw new ToolFailureException("ERROR: incorrect command-line.");
      }
   }

   private File backupJar(File var1) throws ToolFailureException {
      File var2 = new File(var1 + "SAVE");
      if (var2.exists() && !var2.delete()) {
         throw new ToolFailureException("ERROR: Could not delete old backup file: " + var2.getAbsolutePath());
      } else {
         try {
            FileUtils.copy(var1, var2);
         } catch (IOException var4) {
            throw new ToolFailureException("ERROR: Could not create a backup file " + var2.getAbsolutePath());
         }

         if (!var1.delete()) {
            throw new ToolFailureException("ERROR: Could not delete previous jar " + var1.getAbsolutePath());
         } else {
            return var2;
         }
      }
   }

   private void createOutputJar(String var1) throws ToolFailureException {
      File var2 = new File(var1);
      File var3 = null;
      if (var2.exists()) {
         var3 = this.backupJar(var2);
      }

      try {
         JarFileUtils.createJarFileFromDirectory(var1, this.outputDir);
         if (var3 != null) {
            var3.delete();
         }

         FileUtils.remove(this.outputDir);
      } catch (Exception var5) {
         if (var2.exists()) {
            var2.delete();
         }

         if (var3 != null && var3.exists()) {
            var3.renameTo(var2);
            throw new ToolFailureException("ERROR: Could not create output jar, restoring previous jar.  The error was " + var5);
         } else {
            throw new ToolFailureException("ERROR: Could not create output jar.  The error was:" + var5);
         }
      }
   }

   public void setClose(boolean var1) {
      this.doClose = var1;
   }

   public void setClassLoader(ClassLoader var1) {
      this.m_classLoader = (GenericClassLoader)var1;
   }

   public void setJarFile(VirtualJarFile var1) {
      this.myJar = var1;
   }

   public void setCreateOutputJar(boolean var1) {
      this.createOutputJar = var1;
   }

   public void runBody() throws ToolFailureException, ErrorCollectionException {
      this.validateToolInputs();
      VirtualJarFile var1 = null;
      if (this.myJar != null) {
         var1 = this.myJar;
      } else {
         try {
            var1 = VirtualJarFactory.createVirtualJar(this.inputJar);
         } catch (IOException var19) {
            throw new ToolFailureException("ERROR: Error processing input Jar file: " + var19);
         }
      }

      if (this.targetJarFileName != null || !this.inputJar.equals(this.outputDir)) {
         try {
            if (this.outputDir != null && var1 != null) {
               this.inform(this.fmt.getExpandJar(var1.getName(), this.outputDir.getPath()));
               JarFileUtils.extract(var1, this.outputDir);
            }
         } catch (IOException var18) {
            throw new ToolFailureException("ERROR: Error expanding input Jar file: " + var18);
         }
      }

      ClasspathClassFinder2 var2 = null;
      if (this.m_classLoader == null) {
         String var3 = this.outputDir.getAbsolutePath();
         if (this.opts.hasOption("classpath")) {
            StringBuffer var4 = new StringBuffer();
            var4.append(var3);
            var4.append(File.pathSeparator);
            var4.append(this.opts.getOption("classpath"));
            var4.append(File.pathSeparator);
            var3 = var4.toString();
         }

         var2 = new ClasspathClassFinder2(var3);
         ClassLoader var24 = this.getClass().getClassLoader();
         this.m_classLoader = new GenericClassLoader(var2, var24);
         this.m_classLoader.setAnnotation(new Annotation(this.appId, this.appId));
      }

      if (this.opts.hasOption("convertDDs")) {
         this.convertOldDescriptorsToLatest(var1);
      }

      if (this.ejbDescriptor == null) {
         this.inform(this.fmt.getExtractingDesc(var1.getName()));

         try {
            this.ejbDescriptor = this.getDescriptorFromJar(var1, true, this.m_classLoader);
         } catch (ErrorCollectionException var22) {
            ErrorCollectionException var23 = var22;
            if (this.runFromCmdLine) {
               var22.printStackTrace();
               var23 = this.formatErrorsInCollection(var22);
               var23.add(new ToolFailureException("ERROR: ejbc couldn't load descriptor from jar"));
            }

            throw var23;
         }
      }

      try {
         this.opts.setOption("classpath", this.m_classLoader.getClassPath());
      } catch (BadOptionException var17) {
         throw new AssertionError(var17);
      }

      EJBC var26 = EJBCFactory.createEJBC(this.opts);
      var26.setCompilerFactory(this.compilerFactory);
      this.inform(this.fmt.getCompilingJar(var1.getName()));

      try {
         var26.compileEJB(this.m_classLoader, (EJBComponentMBean)null, this.ejbDescriptor, var1);
      } catch (ErrorCollectionException var20) {
         ErrorCollectionException var25 = var20;
         if (this.runFromCmdLine) {
            var25 = this.formatErrorsInCollection(var20);
            var25.add(new ToolFailureException("ERROR: ejbc couldn't invoke compiler"));
         }

         throw var25;
      } finally {
         if (this.doClose) {
            try {
               var1.close();
            } catch (Exception var16) {
            }
         }

         if (var2 != null) {
            var2.close();
         }

      }

      if (this.targetJarFileName != null && this.createOutputJar) {
         this.inform(this.fmt.getCreatingOutputJar(this.targetJarFileName));
         this.createOutputJar(this.targetJarFileName);
      }

      if (this.runFromCmdLine) {
         System.out.println("ejbc successful.");
      } else {
         this.inform("\n" + this.fmt.getEJBCSuccess());
      }

   }

   private void convertOldDescriptorsToLatest(VirtualJarFile var1) throws ErrorCollectionException {
      EjbDescriptorBean var2 = null;

      try {
         var2 = this.getDescriptorFromJar(var1, false, (GenericClassLoader)null);
      } catch (ErrorCollectionException var6) {
         ErrorCollectionException var3 = var6;
         if (this.runFromCmdLine) {
            var3 = this.formatErrorsInCollection(var6);
            var3.add(new ToolFailureException("ERROR: ejbc couldn't find descriptor"));
         }

         throw var3;
      }

      ErrorCollectionException var4;
      try {
         ProcessorFactory var8 = new ProcessorFactory();
         var8.setValidating(false);
         if (this.runFromCmdLine) {
            System.out.println("Converting old descriptor files");
         }

         EJBDescriptorMBeanUtils.loadWeblogicRDBMSJarMBeans(var2, var1, var8, false);
      } catch (Exception var7) {
         var4 = new ErrorCollectionException();
         var4.add(var7);
         var4 = this.formatErrorsInCollection(var4);
         var4.add(new ToolFailureException("ERROR: ejbc couldn't load MBeans"));
         throw var4;
      }

      try {
         weblogic.ejb.container.utils.DDConverter.convertTo11Latest(var2);
      } catch (Exception var5) {
         var4 = new ErrorCollectionException();
         var4.add(var5);
         var4 = this.formatErrorsInCollection(var4);
         var4.add(new ToolFailureException("ERROR: ejbc couldn't convert old decsriptors"));
         throw var4;
      }

      this.persistNewDescriptors(var2);
   }

   private void persistNewDescriptors(EjbDescriptorBean var1) throws ErrorCollectionException {
      try {
         this.renameOldDescriptors();
         var1.usePersistenceDestination(this.outputDir.getAbsolutePath());
         var1.persist();
      } catch (IOException var4) {
         ErrorCollectionException var3 = new ErrorCollectionException();
         var3.add(var4);
         var3 = this.formatErrorsInCollection(var3);
         var3.add(new ToolFailureException("ERROR: ejbc couldn't rename decsriptors"));
         throw var3;
      }
   }

   private void renameOldDescriptors() {
      File var1 = new File(this.outputDir.getAbsolutePath());
      String[] var2 = var1.list();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         System.out.println("the file names are " + var2[var3]);
         if (var2[var3].equalsIgnoreCase("META-INF")) {
            File var4 = new File(this.outputDir.getAbsolutePath() + System.getProperty("file.separator") + var2[var3]);
            if (var4.isDirectory()) {
               String[] var5 = var4.list();

               for(int var6 = 0; var6 < var5.length; ++var6) {
                  System.out.println("the xml file names are " + var5[var6]);
                  if (var5[var6].equalsIgnoreCase("ejb-jar.xml") || var5[var6].equalsIgnoreCase("weblogic-ejb-jar.xml") || var5[var6].equalsIgnoreCase("weblogic-cmp-rdbms-jar.xml")) {
                     String var7 = var4.getAbsolutePath() + System.getProperty("file.separator") + var5[var6];
                     (new File(var7)).renameTo(new File(var7 + ".old"));
                  }
               }
            }
         }
      }

   }

   public ErrorCollectionException formatErrorsInCollection(ErrorCollectionException var1) {
      Collection var2 = var1.getExceptions();
      ErrorCollectionException var3 = new ErrorCollectionException(var1.getBaseMessage());
      String var4 = null;
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Throwable var6 = (Throwable)var5.next();
         var4 = this.formatExceptionMessage(var6);
         var3.add(new ToolFailureException(var4));
      }

      return var3;
   }

   public String formatExceptionMessage(Throwable var1) {
      String var2 = var1.getMessage() + "\n";
      String var3 = "ERROR: Error from ejbc: ";
      StringBuffer var4 = new StringBuffer();
      if (var1 instanceof ToolFailureException) {
         var4.append(var2);
      } else {
         var4.append(var3);
         if (var1 instanceof ClassNotFoundException) {
            var4.append("Unable to load a class specified in your ejb-jar.xml: " + var2);
         } else if (var1 instanceof XMLProcessingException) {
            XMLProcessingException var5 = (XMLProcessingException)var1;
            var4.append("Error processing '" + var5.getFileName() + "': " + var2);
         } else {
            var4.append(var2);
         }

         if (this.opts.hasOption("verbose") || var2 == null || this.isBug(var1)) {
            var4.append("\n" + StackTraceUtils.throwable2StackTrace(var1));
         }
      }

      return var4.toString();
   }

   private EjbDescriptorBean getDescriptorFromJar(VirtualJarFile var1, boolean var2, GenericClassLoader var3) throws ErrorCollectionException {
      ErrorCollectionException var4 = new ErrorCollectionException();
      EjbDescriptorBean var5 = null;

      try {
         if (var2) {
            var5 = EjbDescriptorFactory.createReadOnlyDescriptorFromJarFile(var1, var3);
         } else {
            var5 = EjbDescriptorFactory.createDescriptorFromJarFile(var1);
         }

         return var5;
      } catch (XMLProcessingException var7) {
         var4.add(new ToolFailureException("ERROR: ejbc found errors while processing the descriptor for " + this.sourceJarFileName + ": \n"));
         var4.add(var7);
         throw var4;
      } catch (XMLParsingException var8) {
         var4.add(new ToolFailureException("ERROR: ejbc found errors while parsing the descriptor for " + this.sourceJarFileName + ": \n"));
         var4.add(var8);
         throw var4;
      } catch (Exception var9) {
         var4.add(new ToolFailureException("ERROR: Error creating descriptor from jar file " + this.sourceJarFileName + ": "));
         var4.add(var9);
         throw var4;
      }
   }

   private boolean isBug(Throwable var1) {
      for(int var2 = 0; var2 < this.bugs.length; ++var2) {
         if (this.bugs[var2].isAssignableFrom(var1.getClass())) {
            return true;
         }
      }

      return false;
   }

   private void inform(String var1) {
   }

   public ICompilerFactory getCompilerFactory() {
      return this.compilerFactory;
   }

   public void setCompilerFactory(ICompilerFactory var1) {
      this.compilerFactory = var1;
   }

   public static void main(String[] var0) throws Exception {
      System.out.println("\nDEPRECATED: The weblogic.ejbc20 compiler is deprecated and will be removed in a future version of WebLogic Server.  Please use weblogic.ejbc instead.\n");
      (new ejbc20(var0)).run();
   }
}
