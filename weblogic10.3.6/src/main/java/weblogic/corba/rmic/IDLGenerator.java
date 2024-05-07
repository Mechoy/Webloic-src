package weblogic.corba.rmic;

import java.io.File;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import weblogic.iiop.IDLUtils;
import weblogic.rmi.utils.Utilities;
import weblogic.utils.Getopt2;
import weblogic.utils.compiler.CodeGenerationException;
import weblogic.utils.compiler.CodeGenerator;

public class IDLGenerator extends CodeGenerator implements IDLGeneratorOptions {
   private static final boolean debug = false;
   private static final boolean verbose = false;
   static boolean m_idlOverwrite = false;
   static String m_idlDirectory = null;
   static boolean m_idlVerbose = false;
   static String m_idlFile = null;
   static String m_idlMethodSignatures = null;
   static Output m_currentOutput = null;

   public IDLGenerator(Getopt2 var1) {
      super(var1);
      addIDLGeneratorOptions(var1);
   }

   public static void addIDLGeneratorOptions(Getopt2 var0) {
      var0.addFlag("idl", "Generate idl for remote interfaces");
      var0.addFlag("idlOverwrite", "Always overwrite existing IDL files");
      var0.addFlag("idlVerbose", "Display verbose information for IDL generation");
      var0.addFlag("idlStrict", "Generate IDL according to the OMG standard");
      var0.addFlag("idlFactories", "Generate factory methods for valuetypes");
      var0.addFlag("idlNoValueTypes", "Do not generate valuetypes and methods/attributes that contain them.");
      var0.addFlag("idlNoAbstractInterfaces", "Do not generate abstract interfaces and methods/attributes that contain them.");
      var0.addFlag("idlCompatibility", "Substitute structs for value types to generate CORBA 2.2 compatible IDL.");
      var0.markPrivate("idlCompatibility");
      var0.addOption("idlMethodSignatures", "", "Specify the method signatures used to trigger idl code generation.");
      var0.addFlag("idlVisibroker", "Generate IDL somewhat compatible with Visibroker 4.5 C++");
      var0.addFlag("idlOrbix", "Generate IDL somewhat compatible with Orbix 2000 2.0 C++");
      var0.addOption("idlDirectory", "directory", "Specify the directory where IDL files will be created (default : current directory)");
   }

   public String impl_class_type() {
      return m_currentOutput.getIDLType().getClassName();
   }

   public String forward_references_iterator() {
      StringBuffer var1 = new StringBuffer();

      try {
         IDLType var2 = m_currentOutput.getIDLType();
         Hashtable var3 = new Hashtable();
         var2.getReferences(var3);
         Enumeration var4 = var3.elements();

         while(var4.hasMoreElements()) {
            Object var5 = var4.nextElement();
            IDLType var6 = (IDLType)var5;
            Class var7 = var6.getJavaClass();
            if (var7.isAssignableFrom(var2.getJavaClass())) {
               var1.append(IDLUtils.generateInclude(var6.getDirectory(), var7));
            } else {
               var1.append(var6.getForwardDeclaration());
            }
         }
      } catch (CodeGenerationException var8) {
         var1.append("/*\n" + var8.toString() + "\n*/\n");
      }

      if (this.getIDLVerbose()) {
         var1.append("\n// Generated from " + m_currentOutput.getIDLType() + "\n");
      }

      return var1.toString();
   }

   public String guard_name() {
      IDLType var1 = m_currentOutput.getIDLType();
      return var1.getGuardName("");
   }

   public String main_declaration() {
      StringBuffer var1 = new StringBuffer("");
      IDLType var2 = m_currentOutput.getIDLType();
      Class var3 = var2.getJavaClass();

      try {
         boolean var4 = var2.isRequired();
         if (!var4) {
            var1.append("// valuetypes or abstract interfaces are excluded\n");
            var1.append("/*\n");
         }

         var1.append(IDLUtils.openModule(var3));
         var1.append(var2.getOpeningDeclaration());
         var1.append(var2.getOpenBrace());
         var1.append(var2.generateAttributes());
         var1.append(var2.generateMethods());
         var1.append(var2.generateExtraLines());
         var1.append("\n" + var2.getPragmaID() + "\n");
         var1.append(var2.getCloseBrace());
         var1.append(IDLUtils.closeModule(var3));
         if (!var4) {
            var1.append("*/\n");
            var1.append("// valuetypes or abstract interfaces are excluded\n");
         }
      } catch (CodeGenerationException var5) {
      }

      return var1.toString();
   }

   public String include_iterator() {
      StringBuffer var1 = new StringBuffer();

      try {
         IDLType var2 = m_currentOutput.getIDLType();
         Hashtable var3 = new Hashtable();
         var2.getReferences(var3);
         Enumeration var4 = var3.elements();

         while(var4.hasMoreElements()) {
            Object var5 = var4.nextElement();
            IDLType var6 = (IDLType)var5;
            if (var6.isRequired() && !var6.isAssignableFrom(var2)) {
               var1.append(var6.getIncludeDeclaration());
            }
         }
      } catch (CodeGenerationException var7) {
         var1.append("/*\n" + var7.toString() + "\n*/\n");
      }

      return var1.toString();
   }

   public String before_main_declaration() throws CodeGenerationException {
      IDLType var1 = m_currentOutput.getIDLType();
      return var1.beforeMainDeclaration();
   }

   public String after_main_declaration() throws CodeGenerationException {
      IDLType var1 = m_currentOutput.getIDLType();
      return var1.afterMainDeclaration();
   }

   protected void extractOptionValues(Getopt2 var1) {
      m_idlOverwrite = var1.hasOption("idlOverwrite");
      m_idlDirectory = var1.getOption("idlDirectory", (String)null);
      if (null == m_idlDirectory) {
         m_idlDirectory = super.getRootDirectoryName();
      } else {
         this.setRootDirectoryName(m_idlDirectory);
      }

      m_idlVerbose = var1.hasOption("idlVerbose");
      m_idlFile = var1.getOption("idlFile", (String)null);
      m_idlMethodSignatures = var1.getOption("idlMethodSignatures", (String)null);
      IDLOptions.setIDLStrict(var1.hasOption("idlStrict"));
      IDLOptions.setFactories(var1.hasOption("idlFactories"));
      IDLOptions.setNoValueTypes(var1.hasOption("idlNoValueTypes"));
      IDLOptions.setNoAbstract(var1.hasOption("idlNoAbstractInterfaces"));
      IDLOptions.setCompatibility(var1.hasOption("idlCompatibility"));
      IDLOptions.setVisibroker(var1.hasOption("idlVisibroker"));
      IDLOptions.setOrbix(var1.hasOption("idlOrbix"));
   }

   public String getIDLFile() {
      return m_idlFile;
   }

   String getIDLDirectory() {
      return m_idlDirectory != null ? m_idlDirectory.replace('/', File.separatorChar).replace('\\', File.separatorChar) : null;
   }

   public String getRootDirectoryName() {
      return this.getIDLDirectory();
   }

   boolean getIDLOverwrite() {
      return m_idlOverwrite;
   }

   boolean getIDLVerbose() {
      return m_idlVerbose;
   }

   boolean getIDLStrict() {
      return IDLOptions.getIDLStrict();
   }

   String getIDLMethodSignatures() {
      return m_idlMethodSignatures;
   }

   public Enumeration outputs(Object[] var1) throws Exception {
      String[] var2 = (String[])((String[])var1);
      Hashtable var3 = new Hashtable();
      Hashtable var4 = new Hashtable();
      Hashtable var5 = new Hashtable();
      IDLType.resetUsedTypes();
      this.processOverrides();

      for(int var6 = 0; var6 < var2.length; ++var6) {
         Class var7 = Utilities.classForName(var2[var6]);
         Class var8 = IDLUtils.getRemoteInterface(var7);
         if (var8 != null) {
            IDLTypeRemote var9 = new IDLTypeRemote(var8, (Class)null);
            var3.put(var9.getFileName(), var9);
            Class[] var10 = IDLUtils.getClasses(var8, false, true);

            for(int var11 = 0; var11 < var10.length; ++var11) {
               IDLType.createType(var10[var11], var8);
            }
         } else {
            IDLType.createType(var7, (Class)null);
         }

         Enumeration var12 = IDLType.getUsedTypes().elements();

         IDLType var13;
         while(var12.hasMoreElements()) {
            var13 = (IDLType)var12.nextElement();
            if (var13.isRequired()) {
               var3.put(var13.getFileName(), var13);
            }
         }

         while(!var3.isEmpty()) {
            this.generateIDLFiles(var3);
            var5.putAll(var3);
            var3.clear();
            var12 = IDLType.getUsedTypes().elements();

            while(var12.hasMoreElements()) {
               var13 = (IDLType)var12.nextElement();
               if (!var5.containsKey(var13.getFileName()) && var13.isRequired()) {
                  var3.put(var13.getFileName(), var13);
               }
            }
         }
      }

      return var4.elements();
   }

   void processOverrides() throws Exception {
      IDLType.clearOverrides();
      if (this.getIDLMethodSignatures() != null) {
         StringTokenizer var1 = new StringTokenizer(this.getIDLMethodSignatures(), ";", false);
         StringBuffer var2 = new StringBuffer("(idlMethodSignatures");

         while(var1.hasMoreTokens()) {
            var2.append("(").append(var1.nextToken()).append("generate true)");
         }

         var2.append(")");
         String var3 = var2.toString();
         StructureTokenizer var4 = new StructureTokenizer(new StringReader(var2.toString()));
         this.createOverrides(var4.parseStructure());
      }
   }

   void createOverrides(Structure var1) {
      Hashtable var2 = var1.elements;
      Enumeration var5 = var2.keys();

      for(int var6 = 0; var5.hasMoreElements(); ++var6) {
         String var3 = (String)var5.nextElement();
         String var4 = null;
         Hashtable var7 = (Hashtable)var2.get(var3);
         Enumeration var8 = var7.keys();

         for(int var9 = 0; var8.hasMoreElements(); ++var9) {
            Object var10 = var8.nextElement();
            if (var10 instanceof String) {
               String var11 = (String)var10;
               if (var11.compareToIgnoreCase("generate") == 0) {
                  String var12 = (String)var7.get(var11);
                  if (var12.compareToIgnoreCase("true") == 0) {
                  }
               }

               var4 = var11;
            }
         }

         IDLType.registerOverride(var3, var4);
      }

   }

   public void generateIDLFiles(Hashtable var1) {
      Enumeration var2 = var1.elements();

      while(var2.hasMoreElements()) {
         IDLType var3 = (IDLType)var2.nextElement();
         String var4 = this.getIDLDirectory();
         String var5 = var3.getFileName();
         if (null != var4) {
            var5 = var4 + File.separatorChar + var5;
         }

         boolean var6 = IDLUtils.isARemote(var3.getJavaClass()) | this.getIDLOverwrite();
         if (!var6) {
            File var7 = new File(var5);
            if (!var7.exists()) {
               var6 = true;
            }
         }

         if (var6) {
            if (this.getIDLVerbose()) {
               System.out.println("Generating " + var5 + " for type " + var3);
            }

            Output var10 = new Output(var3.getFileName(), "idl.j", var3.getPackageName(), var3);

            try {
               this.prepare(var10);
               this.generateCode(var10);
               this.cleanup();
            } catch (Exception var9) {
            }
         } else if (this.getIDLVerbose()) {
            System.out.println("Skipping " + var5);
         }
      }

   }

   protected void prepare(CodeGenerator.Output var1) throws Exception {
      m_currentOutput = (Output)var1;
      super.prepare(var1);
   }

   public static IDLType getCurrentType() {
      return m_currentOutput.getIDLType();
   }

   private static class Output extends CodeGenerator.Output {
      private IDLType m_type;

      Output(String var1, String var2, String var3, IDLType var4) {
         super(var1, var2, var3);
         this.m_type = var4;
      }

      public IDLType getIDLType() {
         return this.m_type;
      }
   }
}
