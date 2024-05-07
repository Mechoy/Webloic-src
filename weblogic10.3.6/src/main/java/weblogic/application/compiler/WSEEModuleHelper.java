package weblogic.application.compiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import org.apache.openjpa.lib.meta.ClassAnnotationMetaDataFilter;
import weblogic.application.utils.ClassFinderMetaDataEnumaration;
import weblogic.application.utils.VirtualJarFileMetaDataIterator;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebservicesBean;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.wsee.deploy.WSEEDescriptor;

public class WSEEModuleHelper {
   private static Class[] identitytAnnotationClasses;
   private static ClassAnnotationMetaDataFilter identityAnnotationFilter;
   private WSEEDescriptor wsDescriptor;
   private WebservicesBean wsBean;
   private WeblogicWebservicesBean wlWsBean;
   private boolean isWar;

   public WSEEModuleHelper(CompilerCtx var1, VirtualJarFile var2, String var3, boolean var4) {
      this.isWar = var4;
      this.wsDescriptor = new WSEEDescriptor(var2, var1.getConfigDir(), var1.getPlanBean(), var3, var4);
      this.reloadDescriptors();
   }

   private void reloadDescriptors() {
      try {
         this.wsBean = this.wsDescriptor.getWebservicesBean();

         try {
            this.wlWsBean = this.wsDescriptor.getWeblogicWebservicesBean();
         } catch (IOException var2) {
            var2.printStackTrace();
            this.wlWsBean = null;
         } catch (XMLStreamException var3) {
            var3.printStackTrace();
            this.wlWsBean = null;
         }
      } catch (IOException var4) {
         var4.printStackTrace();
         this.wsBean = null;
      } catch (XMLStreamException var5) {
         var5.printStackTrace();
         this.wsBean = null;
      }

   }

   public WebservicesBean getWsBean() {
      return this.wsBean;
   }

   public WeblogicWebservicesBean getWlWsBean() {
      return this.wlWsBean;
   }

   public void mergeDescriptors(ClassFinder var1) {
      try {
         this.wsDescriptor.mergeWebServicesDescriptors(var1);
         this.wsDescriptor.mergeWeblogicWebServicesDescriptor(var1);
      } catch (IOException var3) {
         var3.printStackTrace();
      } catch (XMLStreamException var4) {
         var4.printStackTrace();
      }

      this.reloadDescriptors();
   }

   private Set getAnnotatedClassnames(VirtualJarFile var1) {
      HashSet var2 = new HashSet();
      VirtualJarFileMetaDataIterator var3 = new VirtualJarFileMetaDataIterator(var1, identityAnnotationFilter);

      try {
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var4 = var4.replace('/', '.');
            var4 = var4.substring(0, var4.length() - 6);
            var2.add(var4);
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      return var2;
   }

   private Set getAnnotatedClassnames(GenericClassLoader var1) {
      HashSet var2 = new HashSet();
      ClassFinderMetaDataEnumaration var3 = new ClassFinderMetaDataEnumaration(var1.getClassFinder(), identityAnnotationFilter);

      while(var3.hasMoreElements()) {
         var2.add((String)var3.nextElement());
      }

      return var2;
   }

   public void processAnnotations(GenericClassLoader var1, String[][] var2) throws ClassNotFoundException, ErrorCollectionException {
      Set var3 = this.getAnnotatedClassnames(var1);
      this.loadAndAppendOtherLinks(var3, var1, var2);
      this.processAnnotationsWithServiceLinks(var1, var2, var3);
   }

   private Set loadAndAppendOtherLinks(Set var1, ClassLoader var2, String[][] var3) throws ClassNotFoundException {
      String[][] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String[] var7 = var4[var6];
         if (var7[1] != null && !var1.contains(var7[1])) {
            Class var8 = var2.loadClass(var7[1]);

            for(int var9 = 0; var9 < identitytAnnotationClasses.length; ++var9) {
               if (var8.getAnnotation(identitytAnnotationClasses[var9]) != null) {
                  var1.add(var7[1]);
               }
            }
         }
      }

      return var1;
   }

   public void processAnnotations(VirtualJarFile var1, ClassLoader var2, String[][] var3) throws ClassNotFoundException, ErrorCollectionException {
      Set var4 = this.getAnnotatedClassnames(var1);
      this.loadAndAppendOtherLinks(var4, var2, var3);
      this.processAnnotationsWithServiceLinks(var2, var3, var4);
   }

   private void processAnnotationsWithServiceLinks(ClassLoader var1, String[][] var2, Set var3) throws ClassNotFoundException, ErrorCollectionException {
      if (var3.size() > 0) {
         HashMap var4 = new HashMap();
         String[][] var5 = var2;
         int var6 = var2.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String[] var8 = var5[var7];
            if (var3.contains(var8[1])) {
               var4.put(var8[0], var1.loadClass(var8[1]));
            }
         }

         this.processAnnotations((ClassLoader)var1, (Map)var4);
      }

   }

   private void processAnnotations(ClassLoader var1, Map<String, Class> var2) throws ClassNotFoundException, ErrorCollectionException {
      try {
         ArrayList var5 = new ArrayList();
         if (this.wsBean != null) {
            var5.add(this.wsBean);
         }

         if (this.wlWsBean != null) {
            var5.add(this.wlWsBean);
         }

         Class var14 = Class.forName("weblogic.wsee.tools.jws.jaxws.JAXWSAnnotationProcessor");
         Object var4 = var14.newInstance();
         Method var3 = var14.getDeclaredMethod("process", Collection.class, ClassLoader.class, Map.class, Boolean.TYPE);
         var3.invoke(var4, var5, var1, var2, this.isWar);
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            Object var8 = var7.next();
            if (var8 instanceof WebservicesBean) {
               this.wsBean = (WebservicesBean)var8;
            } else if (var8 instanceof WeblogicWebservicesBean) {
               this.wlWsBean = (WeblogicWebservicesBean)var8;
            }
         }

      } catch (ClassNotFoundException var9) {
         var9.printStackTrace();
         throw new AssertionError("Unable to instantiate Annotation processor class");
      } catch (InstantiationException var10) {
         var10.printStackTrace();
         throw new AssertionError("Unable to instantiate Annotation processor class");
      } catch (IllegalAccessException var11) {
         var11.printStackTrace();
         throw new AssertionError("Unable to instantiate Annotation processor class");
      } catch (NoSuchMethodException var12) {
         var12.printStackTrace();
         throw new AssertionError("Unable to instantiate Annotation processor class");
      } catch (InvocationTargetException var13) {
         Throwable var6 = var13.getTargetException();
         if (var6 instanceof ClassNotFoundException) {
            throw (ClassNotFoundException)var6;
         } else if (var6 instanceof ErrorCollectionException) {
            throw (ErrorCollectionException)var6;
         } else if (var6 instanceof NoClassDefFoundError) {
            throw (NoClassDefFoundError)var6;
         } else {
            var13.printStackTrace();
            throw new AssertionError("Unable to invoke Annotation processoror");
         }
      }
   }

   static {
      try {
         identitytAnnotationClasses = new Class[]{Class.forName("javax.jws.WebService"), Class.forName("javax.xml.ws.WebServiceProvider")};
         identityAnnotationFilter = new ClassAnnotationMetaDataFilter(identitytAnnotationClasses);
      } catch (Throwable var1) {
         identityAnnotationFilter = null;
      }

   }
}
