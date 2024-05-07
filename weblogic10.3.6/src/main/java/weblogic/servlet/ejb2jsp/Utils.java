package weblogic.servlet.ejb2jsp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarFile;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.ejb.spi.EjbDescriptorFactory;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.EnterpriseBeansBean;
import weblogic.j2ee.descriptor.EntityBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBean;
import weblogic.servlet.ejb2jsp.dd.BeanDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBMethodDescriptor;
import weblogic.servlet.ejb2jsp.dd.EJBTaglibDescriptor;
import weblogic.servlet.ejb2jsp.dd.FilesystemInfoDescriptor;
import weblogic.servlet.ejb2jsp.dd.MethodParamDescriptor;
import weblogic.servlet.jsp.JspConfig;
import weblogic.utils.ArrayUtils;
import weblogic.utils.Getopt2;
import weblogic.utils.StringUtils;
import weblogic.utils.compiler.CompilerInvoker;
import weblogic.utils.io.XMLWriter;
import weblogic.utils.jars.JarFileObject;
import weblogic.utils.reflect.ReflectUtils;

public class Utils {
   public static final String RESOLVE_ERRORS_MESSAGE = "You may resolve errors in the following ways using the GUI ejb2jsp tool:\n - for duplicate tag names, you can rename tag(s) so that all tag\n   names are unique.\n - for duplicate tag names, individual tag(s) can be disabled, so\n   that their name does not cause a conflict.  The JSP tag for that\n   EJB method will not be generated.\n - for duplicate tag names in projects containing more than one EJB,\n   entire beans may be disabled.  JSP tags are not generated for\n   disabled beans, and their tag names will not cause conflicts.\n - for \"meaningless\" parameter names (arg0,arg1,...) each parameter\n   should be given a usable name.  This is done in the GUI tool by\n   editing the tag's attributes.  Reasonable parameters can also be\n   inferred by the tool in most cases, if the \"Source Path\" of the\n   project is set correctly, so that the tool can parse the parameter\n   names out of the .java source files for the EJB interface(s).\n";

   static void p(String var0) {
      System.err.println("[Utils]: " + var0);
   }

   public static String[] union(String[] var0, String[] var1) {
      ArrayList var2 = new ArrayList();

      int var3;
      for(var3 = 0; var3 < var0.length; ++var3) {
         var2.add(var0[var3]);
      }

      for(var3 = 0; var3 < var1.length; ++var3) {
         if (!var2.contains(var1[var3])) {
            var2.add(var1[var3]);
         }
      }

      String[] var4 = new String[var2.size()];
      var2.toArray(var4);
      return var4;
   }

   public static String[] delta(String[] var0, String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         var2.add(var0[var3]);
      }

      ArrayList var5 = new ArrayList();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (!var2.contains(var1[var4])) {
            var5.add(var1[var4]);
         }
      }

      String[] var6 = new String[var5.size()];
      var5.toArray(var6);
      return var6;
   }

   public static String[] splitPath(String var0) {
      var0 = var0.replace('/', File.separatorChar);
      String[] var1 = StringUtils.splitCompletely(var0, File.pathSeparator);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = abs(var1[var2]);
      }

      return var1;
   }

   public static String flattenPath(String[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(var0[var2].replace('/', File.separatorChar));
         if (var2 != var0.length - 1) {
            var1.append(File.pathSeparator);
         }
      }

      return var1.toString();
   }

   public static void compile(EJBTaglibDescriptor var0, PrintStream var1) throws Exception {
      String[] var2 = var0.getErrors();
      if (var2 != null && var2.length > 0) {
         var1.println("[ejb2jsp] cannot compile taglib, it has " + var2.length + " error(s):");

         for(int var8 = 0; var8 < var2.length; ++var8) {
            var1.println(' ' + var2[var8]);
         }

         throw new RuntimeException("taglib descriptor had errors");
      } else {
         EJBTaglibGenerator var3 = var0.createGenerator();
         String[] var4 = var3.generateSources();
         var1.println("[ejb2jsp]: generated the following sources: ");

         for(int var5 = 0; var5 < var4.length; ++var5) {
            var1.println(" " + var4[var5]);
         }

         CompilerInvoker var9 = getCompiler(var0);
         var9.setWantCompilerErrors(true);
         var9.setNoExit(true);

         try {
            var9.compile(var4);
         } catch (Exception var7) {
            var1.println("compilation failed:\n---");
            var1.println("" + var9.getCompilerErrors());
            var1.println("---\n" + var7);
            throw new IOException("compilation failed: " + var9.getCompilerErrors());
         }

         doPackaging(var0, var3);
         FilesystemInfoDescriptor var6 = var0.getFileInfo();
         if (var6.saveAsDirectory()) {
            var1.println("TLD file written to " + var6.getSaveDirTldFile());
            var1.println("classes compiled to directory " + var6.getSaveDirClassDir());
         } else {
            var1.println("Taglib jar file written to " + var6.getSaveJarFile());
         }

         var1.println("[ejb2jsp]: compile successful.");
      }
   }

   public static void doPackaging(EJBTaglibDescriptor var0, EJBTaglibGenerator var1) throws Exception {
      if (var0.getFileInfo().saveAsDirectory()) {
         String var2 = var0.getFileInfo().getSaveDirTldFile();
         var2 = var2.replace('/', File.separatorChar);
         PrintStream var3 = new PrintStream(new FileOutputStream(var2));

         try {
            var3.print(var1.toXML());
            var3.flush();
         } finally {
            var3.close();
         }
      } else {
         File var14 = new File(var0.getFileInfo().getSaveJarTmpdir().replace('/', File.separatorChar));
         File var15 = new File(var14, "META-INF");
         var15.mkdirs();
         if (!var15.isDirectory()) {
            throw new Exception("cannot make directory: " + var15.getAbsolutePath());
         }

         File var4 = new File(var15, "taglib.tld");
         PrintStream var5 = new PrintStream(new FileOutputStream(var4));

         try {
            var5.print(var1.toXML());
            var5.flush();
         } finally {
            var5.close();
         }

         String var6 = var0.getFileInfo().getSaveJarFile();
         JarFileObject var7 = JarFileObject.makeJar(var6, var14);
         var7.save();
      }

   }

   public static CompilerInvoker getCompiler(EJBTaglibDescriptor var0) throws Exception {
      FilesystemInfoDescriptor var1 = var0.getFileInfo();
      String[] var2 = var1.getBuiltinClasspath();
      String var3 = flattenPath(var2);
      var2 = var1.getCompileClasspath();
      if (var2 != null && var2.length > 0) {
         var3 = var3 + File.pathSeparator + flattenPath(var2);
      }

      String var4 = null;
      if (var1.saveAsDirectory()) {
         var4 = var1.getSaveDirClassDir();
      } else {
         var4 = var1.getSaveJarTmpdir();
      }

      var3 = var3 + File.pathSeparator + var4;
      String[] var5 = JspConfig.parseFlags(var1.getJavacFlags());
      Getopt2 var6 = new Getopt2();
      CompilerInvoker var7 = new CompilerInvoker(var6);
      var6.setOption("classpath", var3);
      var6.setOption("compiler", var1.getJavacPath());
      var6.grok(var0.getFileInfo().getCompileCommand());
      var7.setExtraCompileFlags(var5);
      return var7;
   }

   public static List getAllEJBeans(EjbDescriptorBean var0) {
      ArrayList var1 = new ArrayList();
      EjbJarBean var2 = var0.getEjbJarBean();
      WeblogicEjbJarBean var3 = var0.getWeblogicEjbJarBean();
      WeblogicEnterpriseBeanBean[] var4 = var3.getWeblogicEnterpriseBeans();
      int var5 = 0;
      EnterpriseBeansBean var6 = var2.getEnterpriseBeans();
      EntityBeanBean[] var7 = var6.getEntities();

      String var10;
      String var11;
      String var12;
      EJBean var13;
      for(int var8 = 0; var7 != null && var8 < var7.length; ++var8) {
         String var9 = null;
         var10 = null;
         var11 = null;
         var12 = null;
         if (var7[var8].getLocal() != null) {
            var9 = var7[var8].getLocalHome();
            var10 = var7[var8].getLocal();
            var11 = var4[var5++].getLocalJNDIName();
         } else {
            var9 = var7[var8].getHome();
            var10 = var7[var8].getRemote();
            var11 = var4[var5++].getJNDIName();
         }

         var12 = var7[var8].getEjbName();
         var13 = new EJBean(var12, var10, var9, var11, true, false, false);
         var1.add(var13);
      }

      SessionBeanBean[] var16 = var6.getSessions();

      for(int var17 = 0; var16 != null && var17 < var16.length; ++var17) {
         var10 = null;
         var11 = null;
         var12 = null;
         var13 = null;
         if (var16[var17].getLocal() != null) {
            var10 = var16[var17].getLocalHome();
            var11 = var16[var17].getLocal();
            var12 = var4[var5++].getLocalJNDIName();
         } else {
            var10 = var16[var17].getHome();
            var11 = var16[var17].getRemote();
            var12 = var4[var5++].getJNDIName();
         }

         String var18 = var16[var17].getEjbName();
         boolean var14 = "Stateful".equalsIgnoreCase(var16[var17].getSessionType());
         EJBean var15 = new EJBean(var18, var11, var10, var12, false, true, var14);
         var1.add(var15);
      }

      return var1;
   }

   public static void main(String[] var0) throws Exception {
      System.setProperty("javax.xml.parsers.SAXParserFactory", "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl");
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
      JarFile var1 = new JarFile(var0[0]);
      EjbDescriptorBean var2 = EjbDescriptorFactory.createDescriptorFromJarFile(var1);
      var1.close();
      File var3 = new File(var0[0]);
      new Getopt2();
      EJBTaglibDescriptor var5 = createDefaultDescriptor(var3, getAllEJBeans(var2), "C:/tmp");
      XMLWriter var6 = new XMLWriter(System.out);
      var5.toXML(var6);
      var6.flush();
   }

   public static String abs(String var0) {
      File var1 = new File(var0.replace('/', File.separatorChar));
      return var1.getAbsolutePath();
   }

   public static EJBTaglibDescriptor createDefaultDescriptor(String var0, String var1, String var2) throws Exception {
      JarFile var3 = new JarFile(var0);
      EjbDescriptorBean var4 = EjbDescriptorFactory.createDescriptorFromJarFile(var3);
      List var5 = getAllEJBeans(var4);
      var3.close();
      EJBTaglibDescriptor var6 = null;
      var6 = createDefaultDescriptor(new File(var0), var5, var2);
      String[] var7 = StringUtils.splitCompletely(var1, File.pathSeparator);
      var6.getFileInfo().setSourcePath(var7);
      resolveSources(var6);
      return var6;
   }

   static boolean isWin32() {
      try {
         String var0 = System.getProperty("os.name");
         if (var0 == null) {
            return false;
         } else {
            var0 = var0.toLowerCase(Locale.ENGLISH);
            return var0.indexOf("windows") >= 0;
         }
      } catch (Exception var1) {
         var1.printStackTrace();
         return false;
      }
   }

   public static EJBTaglibDescriptor createDefaultDescriptor(File var0, List var1, String var2) throws Exception {
      EJBTaglibDescriptor var3 = new EJBTaglibDescriptor();
      FilesystemInfoDescriptor var4 = var3.getFileInfo();
      var4.setEJBJarFile(var0.getAbsolutePath());
      var4.setJavacPath("javac");
      var4.setSourcePath(new String[0]);
      var4.setSaveAs("DIRECTORY");
      String var5 = var3.toString();
      int var6 = var5.lastIndexOf(46);
      if (var6 > 0) {
         var5 = var5.substring(0, var6);
      }

      String var7 = "/tmp/" + var5 + "_tags";
      if (isWin32()) {
         var7 = "C:\\TEMP\\" + var5 + "_tags";
      }

      var4.setSaveJarTmpdir(var7 + File.separatorChar + "jar_tmp");
      if (!var2.toUpperCase(Locale.ENGLISH).endsWith("WEB-INF")) {
         var2 = var2 + File.separatorChar + "WEB-INF";
      }

      String var8 = var2 + File.separatorChar + "lib" + File.separatorChar + var5 + "-tags.jar";
      String var9 = var2 + File.separatorChar + "classes";
      String var10 = var2 + File.separatorChar + var5 + "-tags.tld";
      var4.setSaveJarFile(var8);
      var4.setSaveDirClassDir(var9);
      var4.setSaveDirTldFile(var10);
      BeanDescriptor[] var11 = new BeanDescriptor[var1.size()];
      Iterator var12 = var1.iterator();
      ClassLoader var13 = var3.getClassLoader();

      for(int var14 = 0; var14 < var11.length; ++var14) {
         var11[var14] = createBeanDescriptor((EJBean)var12.next(), var13);
      }

      var3.setBeans(var11);
      if (var11.length > 0) {
         String var15 = var11[0].getRemoteType();
         var6 = var15.lastIndexOf(46);
         if (var6 > 0) {
            var15 = var15.substring(0, var6);
         }

         var15 = var15 + ".jsp_tags";
         var4.setPackage(var15);
      }

      var3.setEnableBaseEJB(false);
      return var3;
   }

   private static BeanDescriptor createBeanDescriptor(EJBean var0, ClassLoader var1) throws Exception {
      BeanDescriptor var2 = new BeanDescriptor();
      var2.setRemoteType(var0.getRemoteInterfaceName());
      var2.setHomeType(var0.getHomeInterfaceName());
      var2.setJNDIName(var0.getJNDIName());
      var2.setEJBName(var0.getEJBName());
      String var3 = null;
      if (var0.isEntityBean()) {
         var3 = "ENTITY";
      } else if (var0.isStatefulSessionBean()) {
         var3 = "STATEFUL";
      } else {
         var3 = "STATELESS";
      }

      var2.setEJBType(var3);
      Class var4 = var1.loadClass(var0.getRemoteInterfaceName());
      Class var5 = var1.loadClass(var0.getHomeInterfaceName());
      Enumeration var6 = ReflectUtils.distinctInterfaceMethods(var4);
      ArrayList var7 = new ArrayList();

      while(var6.hasMoreElements()) {
         Method var8 = (Method)var6.nextElement();
         EJBMethodDescriptor var9 = method2descriptor(var8);
         var9.setTargetType(var0.getRemoteInterfaceName());
         var7.add(var9);
      }

      EJBMethodDescriptor[] var11 = new EJBMethodDescriptor[var7.size()];
      var7.toArray(var11);
      var2.setEJBMethods(var11);
      if (var2.isStatefulBean()) {
         var7.clear();
         var6 = ReflectUtils.distinctInterfaceMethods(var5);

         while(var6.hasMoreElements()) {
            Method var12 = (Method)var6.nextElement();
            EJBMethodDescriptor var10 = method2descriptor(var12);
            var10.setTargetType(var0.getHomeInterfaceName());
            var10.setTagName("home-" + var10.getTagName());
            var7.add(var10);
         }

         var11 = new EJBMethodDescriptor[var7.size()];
         var7.toArray(var11);
         var2.setHomeMethods(var11);
      }

      var2.resolveBaseMethods();
      return var2;
   }

   public static String unArray(Class var0) {
      int var1;
      for(var1 = 0; var0.isArray(); var0 = var0.getComponentType()) {
         ++var1;
      }

      String var2;
      for(var2 = var0.getName(); var1 > 0; --var1) {
         var2 = var2 + "[]";
      }

      return var2;
   }

   static EJBMethodDescriptor method2descriptor(Method var0) {
      EJBMethodDescriptor var1 = new EJBMethodDescriptor();
      var1.setName(var0.getName());
      var1.setTagName(var0.getName());
      var1.setInfo("");
      String var2 = unArray(var0.getReturnType());
      var1.setReturnType(var2);
      var1.setParams(method2params(var0));
      return var1;
   }

   static MethodParamDescriptor[] method2params(Method var0) {
      Class[] var1 = var0.getParameterTypes();
      if (var1 == null) {
         return new MethodParamDescriptor[0];
      } else {
         MethodParamDescriptor[] var2 = new MethodParamDescriptor[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            MethodParamDescriptor var4 = var2[var3] = new MethodParamDescriptor();
            var4.setType(unArray(var1[var3]));
            var4.setName("arg" + var3);
            var4.setDefault("NONE");
            var4.setDefaultValue("");
            var4.setDefaultMethod("");
         }

         return var2;
      }
   }

   public static void resolveSources(EJBTaglibDescriptor var0) throws Exception {
      ClassLoader var1 = var0.getClassLoader();
      String[] var2 = var0.getFileInfo().getSourcePath();
      BeanDescriptor[] var3 = var0.getBeans();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         BeanDescriptor var5 = var3[var4];
         System.out.println("[ejb2jsp] resolving sources for bean " + var5.getEJBName() + ":");
         Class var6 = var1.loadClass(var5.getRemoteType());
         resolveSources(var5, var6, var2);
         var6 = var1.loadClass(var5.getHomeType());
         resolveSources(var5, var6, var2);
         EJBMethodDescriptor[] var7 = var5.getUnresolvedMethods();
         if (var7 != null && var7.length > 0) {
            System.err.println("[ejb2jsp] WARNING: the following methods are unresolved: ");

            for(int var8 = 0; var8 < var7.length; ++var8) {
               System.err.println(" " + var7[var8].getSignature() + " on type " + var7[var8].getTargetType());
            }
         }
      }

   }

   private static void resolveSources(BeanDescriptor var0, Class var1, String[] var2) throws Exception {
      Enumeration var3 = ReflectUtils.allInterfaces(var1);

      do {
         if (!isBaseEJBClass(var1)) {
            try {
               resolveSingleSource(var0, var1, var2);
            } catch (Exception var5) {
               System.err.println("[ejb2jsp] WARNING: cannot resolve source file(s): " + var5);
            }
         }
      } while(var3.hasMoreElements() && (var1 = (Class)var3.nextElement()) != null);

   }

   private static void resolveSingleSource(BeanDescriptor var0, Class var1, String[] var2) throws Exception {
      InputStream var3 = findSource(var1, var2);

      try {
         EJB2JSPLexer var4 = new EJB2JSPLexer(var3);
         var4.setDescriptor(var0);
         var4.parse();
      } finally {
         var3.close();
      }

   }

   private static InputStream findSource(Class var0, String[] var1) throws FileNotFoundException {
      String var2 = var0.getName().replace('.', File.separatorChar) + ".java";
      String var3 = var2;
      int var4 = var2.lastIndexOf(File.separatorChar);
      if (var4 > 0) {
         var3 = var2.substring(var4 + 1);
      }

      for(int var5 = 0; var5 < var1.length; ++var5) {
         var1[var5] = var1[var5].replace('/', File.separatorChar);
         File var6 = new File(var1[var5], var2);
         if (var6.exists()) {
            return new FileInputStream(var6);
         }

         var6 = new File(var1[var5], var3);
         if (var6.exists()) {
            return new FileInputStream(var6);
         }
      }

      throw new FileNotFoundException("cannot resolve " + var2 + " or " + var3 + " in path " + ArrayUtils.toString(var1));
   }

   static boolean isBaseEJBClass(Class var0) {
      return isBaseEJBClass(var0.getName());
   }

   static boolean isBaseEJBClass(String var0) {
      return "javax.ejb.EJBObject".equals(var0) || "javax.ejb.EJBHome".equals(var0) || "java.rmi.Remote".equals(var0);
   }

   static boolean isBaseEJBMethod(Method var0) {
      return isBaseEJBClass(var0.getDeclaringClass());
   }

   public static boolean isPrimitive(String var0) {
      return "boolean".equals(var0) || "int".equals(var0) || "short".equals(var0) || "long".equals(var0) || "byte".equals(var0) || "char".equals(var0) || "float".equals(var0) || "double".equals(var0);
   }

   public static boolean isPrimitive(Class var0) {
      return isPrimitive(var0.getName());
   }

   public static boolean isVoid(Class var0) {
      return var0 == Void.class || var0 == Void.TYPE;
   }

   public static boolean isVoid(String var0) {
      return var0.equals("void") || var0.equals("Void") || var0.equals("java.lang.Void");
   }

   public static String primitive2Object(Class var0) {
      if (var0 == Boolean.TYPE) {
         return "Boolean";
      } else if (var0 == Byte.TYPE) {
         return "Byte";
      } else if (var0 == Double.TYPE) {
         return "Double";
      } else if (var0 == Integer.TYPE) {
         return "Integer";
      } else if (var0 == Float.TYPE) {
         return "Float";
      } else if (var0 == Long.TYPE) {
         return "Long";
      } else if (var0 == Character.TYPE) {
         return "Character";
      } else if (var0 == Short.TYPE) {
         return "Short";
      } else {
         throw new IllegalArgumentException("type " + var0.getName() + " not primitive");
      }
   }

   public static String primitive2Object(String var0) {
      if (var0.equals("boolean")) {
         return "Boolean";
      } else if (var0.equals("byte")) {
         return "Byte";
      } else if (var0.equals("double")) {
         return "Double";
      } else if (var0.equals("int")) {
         return "Integer";
      } else if (var0.equals("float")) {
         return "Float";
      } else if (var0.equals("long")) {
         return "Long";
      } else if (var0.equals("char")) {
         return "Character";
      } else if (var0.equals("short")) {
         return "Short";
      } else {
         throw new IllegalArgumentException("type " + var0 + " not primitive");
      }
   }

   public static String primitive2Object(String var0, String var1) {
      return "new " + primitive2Object(var0) + "(" + var1 + ")";
   }

   public static String primitive2Object(Class var0, String var1) {
      return "new " + primitive2Object(var0) + "(" + var1 + ")";
   }
}
