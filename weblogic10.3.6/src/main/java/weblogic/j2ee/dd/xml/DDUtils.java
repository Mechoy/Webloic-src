package weblogic.j2ee.dd.xml;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.zip.ZipEntry;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.dd.J2EEDeploymentDescriptor;
import weblogic.j2ee.descriptors.ApplicationDescriptorMBeanImpl;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.descriptors.ApplicationDescriptorMBean;
import weblogic.management.descriptors.Encoding;
import weblogic.management.descriptors.application.J2EEApplicationDescriptorMBean;
import weblogic.management.descriptors.application.weblogic.WeblogicApplicationMBean;
import weblogic.utils.Debug;
import weblogic.utils.io.XMLDeclaration;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.ProcessorFactoryException;
import weblogic.xml.process.SAXProcessorException;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;
import weblogic.xml.process.XMLProcessor;

public final class DDUtils {
   private static final boolean debug = Debug.getCategory("weblogic.j2ee.dd").isEnabled();
   public static final String J2EE12_EAR_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN";
   public static final String J2EE12_EAR_SYSTEM_ID = "http://java.sun.com/j2ee/dtds/application_1_2.dtd";
   public static final String J2EE13_EAR_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN";
   public static final String J2EE13_EAR_SYSTEM_ID = "http://java.sun.com/dtd/application_1_3.dtd";
   public static final String J2EE12_EAR_LOADER_CLASS = "weblogic.j2ee.dd.xml.J2EEDeploymentDescriptorLoader_J2EE12";
   public static final String J2EE13_EAR_LOADER_CLASS = "weblogic.j2ee.dd.xml.J2EEDeploymentDescriptorLoader_J2EE13";
   public static final String WLSAPP_EAR_LOADER_CLASS_700 = "weblogic.j2ee.dd.xml.WebLogicApplication_1_0";
   public static final String WLSAPP_EAR_LOADER_CLASS_810 = "weblogic.j2ee.dd.xml.WebLogicApplication_2_0";
   public static final String WLSAPP_EAR_LOADER_CLASS_900 = "weblogic.j2ee.dd.xml.WebLogicApplication_3_0";
   public static final String J2EE12_EAR_LOCAL_DTD_NAME = "application_1_2.dtd";
   public static final String WLSAPP_EAR_PUBLIC_ID_700 = "-//BEA Systems, Inc.//DTD WebLogic Application 7.0.0//EN";
   public static final String WLSAPP_EAR_PUBLIC_ID_810 = "-//BEA Systems, Inc.//DTD WebLogic Application 8.1.0//EN";
   public static final String WLSAPP_EAR_PUBLIC_ID_900 = "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN";
   public static final String WLSAPP_EAR_SYSTEM_ID_700 = "http://www.bea.com/servers/wls700/dtd/weblogic-application_1_0.dtd";
   public static final String WLSAPP_EAR_SYSTEM_ID_810 = "http://www.bea.com/servers/wls810/dtd/weblogic-application_2_0.dtd";
   public static final String WLSAPP_EAR_SYSTEM_ID_900 = "http://www.bea.com/servers/wls900/dtd/weblogic-application_3_0.dtd";
   public static final String WLSAPP_EAR_LOCAL_DTD_NAME = "weblogic-application_3_0.dtd";
   public static final String WLSAPP_EAR_PUBLIC_DTD_NAME = "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN";
   public static final String J2EE_EAR_STD_DESCR = "META-INF/application.xml";
   public static final String J2EE_EAR_WL_DESCR = "META-INF/weblogic-application.xml";
   public static final String WEBLOGIC_APPLICATION_PUBLIC_ID = "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN";
   public static final String WEBLOGIC_APPLICATION_SYSTEM_ID = "http://www.bea.com/servers/wls900/dtd/weblogic-application_3_0.dtd";
   public static final String WEBLOGIC_APPLICATION_LOADER_CLASS_700 = "weblogic.j2ee.dd.xml.WebLogicApplication_1_0";
   public static final String WEBLOGIC_APPLICATION_LOADER_CLASS_810 = "weblogic.j2ee.dd.xml.WebLogicApplication_2_0";
   public static final String WEBLOGIC_APPLICATION_LOADER_CLASS_900 = "weblogic.j2ee.dd.xml.WebLogicApplication_3_0";
   public static final String WEBLOGIC_APPLICATION_LOCAL_DTD_NAME = "weblogic-application_3_0.dtd";
   public static final String WEBLOGIC_APPLICATION_STD_DESCR = "META-INF/weblogic-application.xml";
   public static final String WL_DOCTYPE = "<!DOCTYPE weblogic-application PUBLIC '-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN' 'http://www.bea.com/servers/wls900/dtd/weblogic-application_3_0.dtd'>\n";
   public static final String[] validPublicIds = new String[]{"-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "-//BEA Systems, Inc.//DTD WebLogic Application 7.0.0//EN", "-//BEA Systems, Inc.//DTD WebLogic Application 8.1.0//EN", "-//BEA Systems, Inc.//DTD WebLogic Application 9.0.0//EN"};
   private static final String[] STD_DESCRIPTOR_PATHS;
   private static final String[] WLS_DESCRIPTOR_PATHS;
   public static final int MARK_SIZE = 1048576;

   public static ApplicationDescriptorMBean loadDeploymentDescriptor(VirtualJarFile var0, File var1, File var2) throws DeploymentException {
      if (debug) {
         Debug.say("loadDeploymentDescriptor \nVirtualJarFile : " + var0.getName() + "\nstdAppDD : " + (var1 != null ? var1.getPath() : "null") + "\nwlsAppDD : " + (var2 != null ? var2.getPath() : "null"));
      }

      ApplicationDescriptorMBeanImpl var3 = new ApplicationDescriptorMBeanImpl();
      return loadDeploymentDescriptor(var3, var0, var1, var2);
   }

   public static ApplicationDescriptorMBean loadDeploymentDescriptor(ApplicationDescriptorMBean var0, VirtualJarFile var1, File var2, File var3) throws DeploymentException {
      if (var0 == null) {
         var0 = new ApplicationDescriptorMBeanImpl();
      }

      loadStdDescriptor((ApplicationDescriptorMBean)var0, var1, var2);
      loadWlsDescriptor((ApplicationDescriptorMBean)var0, var1, var3);
      return (ApplicationDescriptorMBean)var0;
   }

   public static J2EEApplicationDescriptorMBean loadStdDescriptor(ApplicationDescriptorMBean var0, VirtualJarFile var1, File var2) throws DeploymentException {
      if (debug) {
         Debug.say("loadStdDescriptor \nVirtualJarFile : " + var1.getName() + "\nstdAppDD : " + (var2 != null ? var2.getPath() : "null"));
      }

      String var3 = findFirstEntry(var1, STD_DESCRIPTOR_PATHS);
      if (debug) {
         Debug.say("loadStdDescriptor \nddURI : " + var3);
      }

      if (var3 == null && var2 == null) {
         return null;
      } else {
         if (var0 == null) {
            var0 = new ApplicationDescriptorMBeanImpl();
         }

         String var5;
         try {
            processXML((ApplicationDescriptorMBean)var0, var1, var3, var2);
         } catch (IOException var7) {
            var5 = "Error while loading descriptors: " + var7;
            throw new DeploymentException(var5, var7);
         } catch (XMLProcessingException var8) {
            Object var10 = var8.getNestedException();
            if (var10 instanceof SAXProcessorException) {
               var10 = ((SAXProcessorException)var10).getException();
            }

            if (var10 instanceof DeploymentException) {
               throw (DeploymentException)var10;
            }

            String var6 = "Error while loading descriptors: " + var8;
            throw new DeploymentException(var6, var8);
         } catch (XMLParsingException var9) {
            var5 = "Error while loading descriptors: " + var9;
            throw new DeploymentException(var5, var9);
         }

         return ((ApplicationDescriptorMBean)var0).getJ2EEApplicationDescriptor();
      }
   }

   public static WeblogicApplicationMBean loadWlsDescriptor(ApplicationDescriptorMBean var0, VirtualJarFile var1, File var2) throws DeploymentException {
      if (debug) {
         Debug.say("loadWlsDescriptor \nVirtualJarFile : " + var1.getName() + "\nstdAppDD : " + (var2 != null ? var2.getPath() : "null"));
      }

      String var3 = findFirstEntry(var1, WLS_DESCRIPTOR_PATHS);
      if (debug) {
         Debug.say("loadWlsdDescriptor \nddURI : " + var3);
      }

      if (var3 == null && var2 == null) {
         return null;
      } else {
         if (var0 == null) {
            var0 = new ApplicationDescriptorMBeanImpl();
         }

         String var5;
         try {
            processXML((ApplicationDescriptorMBean)var0, var1, var3, var2);
         } catch (IOException var7) {
            var5 = "Error while loading descriptors: " + var7;
            throw new DeploymentException(var5, var7);
         } catch (XMLProcessingException var8) {
            Object var10 = var8.getNestedException();
            if (var10 instanceof SAXProcessorException) {
               var10 = ((SAXProcessorException)var10).getException();
            }

            if (var10 instanceof DeploymentException) {
               throw (DeploymentException)var10;
            }

            String var6 = "Error while loading descriptors: " + var8;
            throw new DeploymentException(var6, var8);
         } catch (XMLParsingException var9) {
            var5 = "Error while loading descriptors: " + var9;
            throw new DeploymentException(var5, var9);
         }

         return ((ApplicationDescriptorMBean)var0).getWeblogicApplicationDescriptor();
      }
   }

   private static void processXML(ApplicationDescriptorMBean var0, VirtualJarFile var1, String var2, File var3) throws IOException, XMLProcessingException, XMLParsingException {
      Object var4 = null;

      try {
         if (var3 != null) {
            var4 = new FileInputStream(var3);
            processXML((InputStream)var4, var0, var3.getPath());
         } else {
            ZipEntry var5 = var1.getEntry(var2);
            if (var5 == null) {
               throw new FileNotFoundException("Could not find " + var2);
            }

            var4 = var1.getInputStream(var5);
            processXML((InputStream)var4, var0, var2);
         }
      } finally {
         if (var4 != null) {
            ((InputStream)var4).close();
         }

      }

   }

   private static void processXML(InputStream var0, ApplicationDescriptorMBean var1, String var2) throws XMLProcessingException, XMLParsingException, IOException {
      if (!((InputStream)var0).markSupported()) {
         var0 = new BufferedInputStream((InputStream)var0);
      }

      String var3 = getXMLEncoding((InputStream)var0, var2);
      ((InputStream)var0).mark(1048576);
      XMLProcessor var4 = null;

      try {
         var4 = (new ProcessorFactory()).getProcessor((InputStream)var0, validPublicIds);
      } catch (ProcessorFactoryException var12) {
         throw new XMLProcessingException(var12, var2);
      }

      ((InputStream)var0).reset();
      if (var4 instanceof J2EEDeploymentDescriptorLoader) {
         J2EEDeploymentDescriptorLoader var5 = (J2EEDeploymentDescriptorLoader)var4;
         J2EEDeploymentDescriptor var6 = new J2EEDeploymentDescriptor();
         var1.setJ2EEApplicationDescriptor(var6);
         var6.setEncoding(var3);
         var5.setDD(var6);

         try {
            var5.process((InputStream)var0);
         } catch (XMLParsingException var10) {
            var10.setFileName(var2);
            throw var10;
         } catch (XMLProcessingException var11) {
            var11.setFileName(var2);
            throw var11;
         }
      } else {
         if (!(var4 instanceof WADDLoader)) {
            throw new XMLProcessingException("Invalid descriptor file: " + var2);
         }

         WADDLoader var13 = (WADDLoader)var4;
         var13.setApplicationDescriptor(var1);

         try {
            var13.process((InputStream)var0);
         } catch (XMLParsingException var8) {
            var8.setFileName(var2);
            throw var8;
         } catch (XMLProcessingException var9) {
            var9.setFileName(var2);
            throw var9;
         }

         WeblogicApplicationMBean var14 = var1.getWeblogicApplicationDescriptor();
         var14.setEncoding(var3);
      }

   }

   private static String findFirstEntry(VirtualJarFile var0, String[] var1) {
      if (var1 != null && var0 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            ZipEntry var3 = var0.getEntry(var1[var2]);
            if (var3 != null) {
               return var1[var2];
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static InputStream getEntry(VirtualJarFile var0, String var1) throws IOException {
      ZipEntry var2 = var0.getEntry(var1);
      if (var2 == null) {
         throw new FileNotFoundException(var1 + " not found in jar file");
      } else {
         return var0.getInputStream(var2);
      }
   }

   private static String getXMLEncoding(InputStream var0, String var1) throws IOException {
      String var2 = null;
      var0.mark(1048576);

      try {
         XMLDeclaration var3 = new XMLDeclaration();
         var3.parse(var0);
         var2 = var3.getEncoding();
      } finally {
         var0.reset();
      }

      validateEncoding(var2, var1);
      return var2;
   }

   private static void validateEncoding(String var0, String var1) throws IOException {
      if (var0 != null && Encoding.getIANA2JavaMapping(var0) == null && Encoding.getJava2IANAMapping(var0) == null && !Charset.isSupported(var0)) {
         Loggable var2 = J2EELogger.logDescriptorUsesInvalidEncodingLoggable(var1, var0);
         throw new UnsupportedEncodingException(var2.getMessage());
      }
   }

   public static void main(String[] var0) {
      try {
         if (var0.length <= 0) {
            Debug.say("Usage : java weblogic.j2ee.dd.xml.DDUtils $jarfilename");
            return;
         }

         File var1 = new File(var0[0]);
         ApplicationDescriptorMBeanImpl var2 = new ApplicationDescriptorMBeanImpl();
         System.out.println("OPEN " + var1);
         VirtualJarFile var3 = VirtualJarFactory.createVirtualJar(var1);
         ApplicationDescriptorMBean var4 = loadDeploymentDescriptor(var2, var3, (File)null, (File)null);
         J2EEApplicationDescriptorMBean var5 = var4.getJ2EEApplicationDescriptor();
         WeblogicApplicationMBean var6 = var4.getWeblogicApplicationDescriptor();
         Debug.say("Printing xml file ......");
         Debug.say(var5.toXML(2));
         Debug.say("=============");
         Debug.say(var6 != null ? var6.toXML(2) : "WLS descriptor null");
         Debug.say("Printed xml file ......");
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   static {
      STD_DESCRIPTOR_PATHS = new String[]{"META-INF/application.xml", "META-INF/application.xml".toLowerCase(Locale.US)};
      WLS_DESCRIPTOR_PATHS = new String[]{"META-INF/weblogic-application.xml", "META-INF/weblogic-application.xml".toLowerCase(Locale.US)};
   }
}
