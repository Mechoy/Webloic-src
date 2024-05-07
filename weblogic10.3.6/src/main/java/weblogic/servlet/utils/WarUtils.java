package weblogic.servlet.utils;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.xml.stream.XMLStreamException;
import weblogic.application.utils.ClassLoaderUtils;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.wl.ContainerDescriptorBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.LibraryRefBean;
import weblogic.j2ee.descriptor.wl.PreferApplicationPackagesBean;
import weblogic.j2ee.descriptor.wl.PreferApplicationResourcesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.servlet.internal.WebAppDescriptor;
import weblogic.utils.Debug;
import weblogic.utils.application.WarDetector;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ToolFailureException;
import weblogic.utils.jars.VirtualJarFile;

public final class WarUtils {
   private static final String WEB_XML;
   private static final String WEB_XML_URI = "WEB-INF/web.xml";
   public static final String WEBLOGIC_XML;
   private static final String WEB_SERVICES_XML;
   private static final String WEB_SERVICES_URI = "WEB-INF/web-services.xml";
   private static final String JSF_RI_LIB_NAME = "jsf";
   private static final String FACES_SERVLET_NAME = "javax.faces.webapp.FacesServlet";
   private static final String JAVAX_FACES_CONFIG_FILES = "javax.faces.CONFIG_FILES";
   private static final boolean diDisabled;

   public static boolean isWar(File var0) throws IOException {
      return WarDetector.instance.isWar(var0);
   }

   public static boolean isPre15War(File var0) throws IOException {
      return var0.isDirectory() ? (new File(var0, WEB_XML)).exists() : existsInWar(var0, "WEB-INF/web.xml");
   }

   public static boolean isWebServices(File var0) throws IOException {
      return var0.isDirectory() ? (new File(var0, WEB_SERVICES_XML)).exists() : existsInWar(var0, "WEB-INF/web-services.xml");
   }

   private static boolean existsInWar(File var0, String var1) throws IOException {
      ZipFile var2 = null;

      boolean var3;
      try {
         var2 = new ZipFile(var0);
         var3 = var2.getEntry(var1) != null;
      } catch (ZipException var12) {
         throw new ZipException("Error opening file - " + var0.getPath() + " Message - " + var12.getMessage());
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var11) {
            }
         }

      }

      return var3;
   }

   public static WebAppDescriptor getWebAppDescriptor(File var0, VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      return var0 != null ? new WebAppDescriptor(var0, (File)null, var2, var3, var4) : new WebAppDescriptor(var1, var2, var3, var4);
   }

   public static WeblogicWebAppBean getWlWebAppBean(WebAppDescriptor var0) throws ToolFailureException {
      WeblogicWebAppBean var1 = null;

      try {
         var1 = var0.getWeblogicWebAppBean();
         return var1;
      } catch (IOException var3) {
         throw new ToolFailureException(var3.getMessage());
      } catch (XMLStreamException var4) {
         throw new ToolFailureException(var4.getMessage());
      }
   }

   public static WebAppBean getWebAppBean(WebAppDescriptor var0) throws ToolFailureException {
      WebAppBean var1 = null;

      try {
         var1 = var0.getWebAppBean();
         return var1;
      } catch (IOException var3) {
         throw new ToolFailureException(var3.getMessage());
      } catch (XMLStreamException var4) {
         throw new ToolFailureException(var4.getMessage());
      }
   }

   public static boolean isJsfApplication(WebAppBean var0, WeblogicWebAppBean var1) {
      int var3;
      if (var1 != null) {
         LibraryRefBean[] var2 = var1.getLibraryRefs();
         if (var2 != null) {
            for(var3 = 0; var3 < var2.length; ++var3) {
               if ("jsf".equals(var2[var3].getLibraryName())) {
                  return true;
               }
            }
         }
      }

      if (var0 != null) {
         ServletBean[] var4 = var0.getServlets();
         if (var4 != null) {
            for(var3 = 0; var3 < var4.length; ++var3) {
               if ("javax.faces.webapp.FacesServlet".equals(var4[var3].getServletClass())) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static String getFacesConfigFiles(WebAppBean var0) {
      if (var0 == null) {
         return null;
      } else {
         ParamValueBean[] var1 = var0.getContextParams();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            if ("javax.faces.CONFIG_FILES".equals(var1[var2].getParamName())) {
               return var1[var2].getParamValue();
            }
         }

         return null;
      }
   }

   public static boolean isDIEnabled(WebAppBean var0) {
      if (diDisabled) {
         return false;
      } else {
         String var1 = ((DescriptorBean)var0).getDescriptor().getOriginalVersionInfo();
         Debug.assertion(var1 != null);

         try {
            return (double)Float.parseFloat(var1) >= 2.5 && var0.getVersion() != null;
         } catch (Exception var3) {
            return false;
         }
      }
   }

   public static boolean isAnnotationEnabled(WebAppBean var0) {
      return isDIEnabled(var0) && !var0.isMetadataComplete();
   }

   public static boolean configureFCL(WeblogicWebAppBean var0, GenericClassLoader var1, boolean var2) throws ToolFailureException {
      PreferApplicationPackagesBean var3 = null;
      PreferApplicationResourcesBean var4 = null;
      if (var0 != null && var0.getContainerDescriptors() != null && var0.getContainerDescriptors().length > 0) {
         ContainerDescriptorBean var5 = var0.getContainerDescriptors()[0];
         var3 = var5.getPreferApplicationPackages();
         var4 = var5.getPreferApplicationResources();
         if ((var3 != null && var3.getPackageNames().length > 0 || var4 != null && var4.getResourceNames().length > 0) && var5.isPreferWebInfClasses()) {
            throw new ToolFailureException("Neither <prefer-application-packages> nor <prefer-application-resources> can be specified when <prefer-web-inf-classes> is turned on in weblogic.xml");
         }
      }

      if (var2) {
         ClassLoaderUtils.initFilterPatterns(var3, var4, var1);
         return false;
      } else {
         return var3 != null && var3.getPackageNames().length > 0 || var4 != null && var4.getResourceNames().length > 0;
      }
   }

   static {
      WEB_XML = "WEB-INF" + File.separator + "web.xml";
      WEBLOGIC_XML = "WEB-INF" + File.separator + "weblogic.xml";
      WEB_SERVICES_XML = "WEB-INF" + File.separator + "web-services.xml";
      diDisabled = Boolean.getBoolean("weblogic.servlet.DIDisabled");
   }
}
