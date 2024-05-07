package weblogic.servlet.internal;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import javax.annotation.security.DeclareRoles;
import javax.xml.ws.WebServiceProvider;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import weblogic.application.utils.ClassFinderMetaDataEnumaration;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.J2eeAnnotationProcessor;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.FilterMappingBean;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.servlet.annotation.WLFilter;
import weblogic.servlet.annotation.WLInitParam;
import weblogic.servlet.annotation.WLServlet;
import weblogic.servlet.utils.WarUtils;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class WebAnnotationProcessorImpl extends J2eeAnnotationProcessor implements WebAnnotationProcessor {
   private static final Class[] COMMON_ANNOTATION_CLASSES = new Class[]{WLServlet.class, WLFilter.class, javax.jws.WebService.class, WebServiceProvider.class};
   private ClassAnnotationMetaDataFilter annotationFilter = this.getAnnotationDataFilter();
   private JSFManagedBeanAnnotation jsfAnnotations;

   public WebAppBean processAnnotationsOnClone(GenericClassLoader var1, WebAppBean var2, WebAppHelper var3) throws ClassNotFoundException, ErrorCollectionException {
      if (!WarUtils.isAnnotationEnabled(var2)) {
         return var2;
      } else {
         Descriptor var4 = (Descriptor)((DescriptorBean)var2).getDescriptor().clone();
         WebAppBean var5 = (WebAppBean)var4.getRootBean();
         this.processAnnotations(var1, var5, var3);
         return var5;
      }
   }

   public void processAnnotations(GenericClassLoader var1, WebAppBean var2, WebAppHelper var3) throws ClassNotFoundException, ErrorCollectionException {
      if (WarUtils.isAnnotationEnabled(var2)) {
         long var4 = System.currentTimeMillis();
         dbg("Processing annotations... ");
         HashSet var6 = new HashSet();
         Iterator var7;
         Object var8;
         if (var3 != null) {
            var7 = var3.getAnnotatedClasses(this).iterator();

            label70:
            while(true) {
               while(true) {
                  if (!var7.hasNext()) {
                     break label70;
                  }

                  var8 = var7.next();
                  Class var9 = var1.loadClass((String)var8);
                  if (var9.isAnnotationPresent(WLServlet.class)) {
                     this.processWLServletAnnotation(var9, var2);
                  } else if (var9.isAnnotationPresent(WLFilter.class)) {
                     this.processWLFilterAnnotation(var9, var2);
                  } else if (!var9.isAnnotationPresent(javax.jws.WebService.class) && !var9.isAnnotationPresent(WebServiceProvider.class)) {
                     if (this.jsfAnnotations.isAnnotedWithManagedBean(var9)) {
                        var6.add(var9.getName());
                     }
                  } else {
                     dbg("Processing @WebService annotation on " + var9);
                     if (!var9.isInterface()) {
                        mergeWebAppWebService(var9, var2);
                     }
                  }
               }
            }
         }

         dbg("Processing J2EE annotations ..");
         this.processJ2eeAnnotations(var1, var2);
         dbg("Processing tag-class annotations ..");
         if (var3 != null) {
            var7 = var3.getTagHandlers(true).iterator();

            while(var7.hasNext()) {
               var8 = var7.next();
               this.processJ2eeAnnotations(var1.loadClass((String)var8), var2);
            }
         }

         dbg("Processing listener-class annotations ..");
         if (var3 != null) {
            var7 = var3.getTagListeners(true).iterator();

            while(var7.hasNext()) {
               var8 = var7.next();
               this.processJ2eeAnnotations(var1.loadClass((String)var8), var2);
            }
         }

         dbg("Processing managed-bean-class annotations ..");
         if (var3 != null) {
            var7 = var3.getManagedBeanClasses(var6).iterator();

            while(var7.hasNext()) {
               var8 = var7.next();
               this.processJ2eeAnnotations(var1.loadClass((String)var8), var2);
            }
         }

         dbg("Annotation processing took " + (System.currentTimeMillis() - var4) + " ms");
      }
   }

   public List getAnnotatedClasses(ClassFinder var1) {
      ArrayList var2 = null;

      for(ClassFinderMetaDataEnumaration var3 = new ClassFinderMetaDataEnumaration(var1, this.annotationFilter); var3.hasMoreElements(); var2.add(var3.nextElement())) {
         if (var2 == null) {
            var2 = new ArrayList();
         }
      }

      return (List)(var2 == null ? Collections.EMPTY_LIST : var2);
   }

   public static void mergeWebAppWebService(Class var0, WebAppBean var1) {
      ServletBean var2 = null;
      ServletBean[] var3 = var1.getServlets();
      ServletBean[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ServletBean var7 = var4[var6];
         if (var0.getName().equals(var7.getServletClass())) {
            var2 = var7;
            break;
         }
      }

      if (var2 == null) {
         var2 = var1.createServlet();
         var2.setServletName(var0.getName());
         var2.setServletClass(var0.getName());
         var2.setLoadOnStartup("1");
      }

      ServletMappingBean var10 = null;
      ServletMappingBean[] var11 = var1.getServletMappings();
      ServletMappingBean[] var12 = var11;
      int var13 = var11.length;

      for(int var8 = 0; var8 < var13; ++var8) {
         ServletMappingBean var9 = var12[var8];
         if (var2.getServletName().equals(var9.getServletName())) {
            var10 = var9;
            break;
         }
      }

      if (var10 == null) {
         var10 = var1.createServletMapping();
         var10.setServletName(var2.getServletName());
         var10.setUrlPatterns(new String[]{getWebServiceName(var0)});
      }

   }

   private static String getWebServiceName(Class var0) {
      String var1 = null;
      javax.jws.WebService var2 = (javax.jws.WebService)var0.getAnnotation(javax.jws.WebService.class);
      if (var2 != null) {
         var1 = var2.serviceName();
      } else {
         WebServiceProvider var3 = (WebServiceProvider)var0.getAnnotation(WebServiceProvider.class);
         if (var3 != null) {
            var1 = var3.serviceName();
         }
      }

      if (var1 == null || var1.trim().equals("")) {
         var1 = var0.getSimpleName() + "Service";
      }

      return var1;
   }

   public void processJ2eeAnnotations(ClassLoader var1, WebAppBean var2) throws ClassNotFoundException, ErrorCollectionException {
      if (WarUtils.isAnnotationEnabled(var2)) {
         this.processServlets(var2, var1);
         this.processFilters(var2, var1);
         this.processListeners(var2, var1);
         this.validate(var1, (DescriptorBean)var2, false);
         this.throwProcessingErrors();
      }
   }

   private void processServlets(WebAppBean var1, ClassLoader var2) throws ClassNotFoundException {
      ServletBean[] var3 = var1.getServlets();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getServletClass() != null) {
               Class var5 = var2.loadClass(var3[var4].getServletClass());
               this.processJ2eeAnnotations(var5, var1);
               this.processRunAs(var5, (DescriptorBean)var3[var4]);
               this.processDeclareRoles(var5, var3[var4], var1);
            }
         }

      }
   }

   private void processFilters(WebAppBean var1, ClassLoader var2) throws ClassNotFoundException {
      FilterBean[] var3 = var1.getFilters();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            Class var5 = var2.loadClass(var3[var4].getFilterClass());
            this.processJ2eeAnnotations(var5, var1);
         }

      }
   }

   private void processListeners(WebAppBean var1, ClassLoader var2) throws ClassNotFoundException {
      ListenerBean[] var3 = var1.getListeners();
      if (var3 != null && var3.length != 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            Class var5 = var2.loadClass(var3[var4].getListenerClass());
            this.processJ2eeAnnotations(var5, var1);
         }

      }
   }

   private void processWeblogicWebAnnotations(VirtualJarFile var1, ClassLoader var2, WebAppBean var3) {
      File[] var4 = var1.getRootFiles();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].exists()) {
            VirtualJarFile var6 = null;

            try {
               var6 = VirtualJarFactory.createVirtualJar(var4[var5]);
               Iterator var7 = var6.getEntries("/WEB-INF/classes/");

               while(var7.hasNext()) {
                  ZipEntry var8 = (ZipEntry)var7.next();
                  String var9 = var8.getName();
                  if (var9.endsWith(".class")) {
                     var9 = var9.substring(16);
                     var9 = var9.replace('/', '.');
                     var9 = var9.substring(0, var9.length() - 6);
                     Class var10 = var2.loadClass(var9);
                     if (var10.isAnnotationPresent(WLServlet.class)) {
                        this.processWLServletAnnotation(var10, var3);
                     } else if (var10.isAnnotationPresent(WLFilter.class)) {
                        this.processWLFilterAnnotation(var10, var3);
                     }
                  }
               }
            } catch (Exception var19) {
               var19.printStackTrace();
            } finally {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (IOException var18) {
                  }
               }

            }
         }
      }

   }

   private void processWLFilterAnnotation(Class var1, WebAppBean var2) {
      WLFilter var3 = (WLFilter)var1.getAnnotation(WLFilter.class);
      String var4 = getName(var3.name(), var1);
      dbg("Processing @WLFilter annotation on " + var1);
      FilterBean var5 = var2.lookupFilter(var4);
      if (var5 == null) {
         var5 = var2.createFilter(var4);
      }

      if (!var5.isFilterClassSet()) {
         var5.setFilterClass(var1.getName());
      }

      WLInitParam[] var6 = var3.initParams();

      for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
         ParamValueBean var8 = var5.lookupInitParam(var6[var7].name());
         if (var8 == null) {
            var8 = var5.createInitParam(var6[var7].name());
         }

         if (!var8.isParamValueSet()) {
            var8.setParamValue(var6[var7].value());
         }
      }

      String[] var9 = var3.mapping();
      if (var9 != null && var9.length > 0) {
         FilterMappingBean var10 = var2.createFilterMapping();
         var10.setFilterName(var4);
         var10.setUrlPatterns(var9);
      }

   }

   private void processWLServletAnnotation(Class var1, WebAppBean var2) {
      WLServlet var3 = (WLServlet)var1.getAnnotation(WLServlet.class);
      String var4 = getName(var3.name(), var1);
      ServletBean var5 = var2.lookupServlet(var4);
      if (var5 == null) {
         var5 = var2.createServlet(var4);
      }

      dbg("Processing @WLServlet annotation on " + var1);
      if (!var5.isServletClassSet()) {
         var5.setServletClass(var1.getName());
      }

      if (!var5.isLoadOnStartupSet() && var3.loadOnStartup() != -1) {
         var5.setLoadOnStartup(Integer.toString(var3.loadOnStartup()));
      }

      String var6 = var3.runAs();
      if (var6.length() > 0 && !var5.isRunAsSet()) {
         RunAsBean var7 = var5.createRunAs();
         var7.setRoleName(var6);
      }

      WLInitParam[] var10 = var3.initParams();

      for(int var8 = 0; var10 != null && var8 < var10.length; ++var8) {
         ParamValueBean var9 = var5.lookupInitParam(var10[var8].name());
         if (var9 == null) {
            var9 = var5.createInitParam(var10[var8].name());
         }

         if (!var9.isParamValueSet()) {
            var9.setParamValue(var10[var8].value());
         }
      }

      String[] var11 = var3.mapping();
      if (var11 != null && var11.length > 0) {
         this.addUrlPatterns(var2, var4, var11);
      }

   }

   private static String getName(String var0, Class var1) {
      if (var0 == null || var0.length() == 0) {
         var0 = var1.getSimpleName();
      }

      return var0;
   }

   private void addUrlPatterns(WebAppBean var1, String var2, String[] var3) {
      ServletMappingBean var4 = null;
      ServletMappingBean[] var5 = var1.getServletMappings();
      if (var5 != null) {
         for(int var6 = 0; var6 < var5.length && var4 == null; ++var6) {
            if (var5[var6].getServletName() != null && var5[var6].getServletName().equals(var2) || var5[var6].getServletName() == null && var2 == null) {
               var4 = var5[var6];
            }
         }
      }

      if (var4 == null) {
         var4 = var1.createServletMapping();
         var4.setServletName(var2);
      }

      if (var4.getUrlPatterns() == null) {
         var4.setUrlPatterns(var3);
      } else {
         List var8 = Arrays.asList(var4.getUrlPatterns());

         for(int var7 = 0; var7 < var3.length; ++var7) {
            if (!var8.contains(var3[var7])) {
               var4.addUrlPattern(var3[var7]);
            }
         }
      }

   }

   private static void dbg(String var0) {
      WebComponentContributor.dbg(var0);
   }

   protected void perhapsDeclareRunAs(DescriptorBean var1, String var2) {
      ServletBean var3 = (ServletBean)var1;
      if (var3.getRunAs() == null) {
         RunAsBean var4 = var3.createRunAs();
         var4.setRoleName(var2);
      }

   }

   private void processDeclareRoles(Class var1, ServletBean var2, WebAppBean var3) {
      if (var1.isAnnotationPresent(DeclareRoles.class)) {
         DeclareRoles var4 = (DeclareRoles)var1.getAnnotation(DeclareRoles.class);
         this.perhapsDeclareRoles(var3, var4.value());
      }

   }

   private void perhapsDeclareRoles(WebAppBean var1, String[] var2) {
      HashSet var3 = new HashSet();
      SecurityRoleBean[] var4 = var1.getSecurityRoles();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         SecurityRoleBean var7 = var4[var6];
         var3.add(var7.getRoleName());
      }

      String[] var9 = var2;
      var5 = var2.length;

      for(var6 = 0; var6 < var5; ++var6) {
         String var10 = var9[var6];
         if (!var3.contains(var10)) {
            SecurityRoleBean var8 = var1.createSecurityRole();
            var8.setRoleName(var10);
         }
      }

   }

   private ClassAnnotationMetaDataFilter getAnnotationDataFilter() {
      if (this.jsfAnnotations == null) {
         this.jsfAnnotations = WebAnnotationProcessorImpl.JSFManagedBeanAnnotation.create(Thread.currentThread().getContextClassLoader());
      }

      Class var1 = this.jsfAnnotations.getManagedBeanAnnotation();
      if (var1 == null) {
         return new ClassAnnotationMetaDataFilter(COMMON_ANNOTATION_CLASSES);
      } else {
         int var2 = COMMON_ANNOTATION_CLASSES.length + 1;
         Class[] var3 = new Class[var2];
         System.arraycopy(COMMON_ANNOTATION_CLASSES, 0, var3, 0, COMMON_ANNOTATION_CLASSES.length);
         var3[var2 - 1] = this.jsfAnnotations.getManagedBeanAnnotation();
         return new ClassAnnotationMetaDataFilter(var3);
      }
   }

   private static class JSFManagedBeanAnnotation {
      public static final String MANAGEDBEAN = "javax.faces.bean.ManagedBean";
      private Class<? extends Annotation> managedBean = null;

      public static JSFManagedBeanAnnotation create(ClassLoader var0) {
         Class var1 = null;

         try {
            var1 = var0.loadClass("javax.faces.bean.ManagedBean");
         } catch (Exception var3) {
         }

         return new JSFManagedBeanAnnotation(var1);
      }

      private JSFManagedBeanAnnotation(Class var1) {
         this.managedBean = var1;
      }

      public Class<? extends Annotation> getManagedBeanAnnotation() {
         return this.managedBean;
      }

      public boolean isAnnotedWithManagedBean(Class var1) {
         return this.managedBean != null && var1.isAnnotationPresent(this.managedBean);
      }
   }
}
