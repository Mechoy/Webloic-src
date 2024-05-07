package weblogic.servlet.internal;

import com.oracle.pitchfork.interfaces.WebComponentContributorBroker;
import com.oracle.pitchfork.interfaces.inject.DeploymentUnitMetadataI;
import com.oracle.pitchfork.interfaces.inject.EnricherI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.j2ee.descriptor.FilterBean;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.ListenerBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.injection.J2eeComponentContributor;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.management.DeploymentException;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.utils.WarUtils;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;

public final class WebComponentContributor extends J2eeComponentContributor implements WebComponentCreator {
   public static final String SPRING_WEB_XML_LOCATION = "/META-INF/spring-web.xml";
   private static final DebugLogger diLogger = DebugLogger.getDebugLogger("DebugWebAppDI");
   private WebAppBean webBean;
   private WebAppServletContext context;
   private boolean useDI = true;
   private ConcurrentHashMap<String, Jsr250MetadataI> diClasses = new ConcurrentHashMap();
   private boolean usesSpringExtensionModel = false;
   private WebComponentContributorBroker webCompContributorBroker;

   public WebComponentContributor(PitchforkContext var1) {
      super(var1);
      this.webCompContributorBroker = var1.getPitchforkUtils().createWebComponentContributorBroker();
   }

   public void initialize(WebAppServletContext var1) throws DeploymentException {
      this.context = var1;
      this.webBean = var1.getWebAppModule().getWebAppBean();
      this.useDI = WarUtils.isDIEnabled(this.webBean);
      if (!this.useDI) {
         dbg("Dependency injection is turned OFF for " + var1);
      } else {
         this.checkDuplicatedCallback(this.webBean.getPostConstructs(), LifecycleEvent.POST_CONSTRUCT);
         this.checkDuplicatedCallback(this.webBean.getPreDestroys(), LifecycleEvent.PRE_DESTROY);
         String var2 = null;
         if (var1.getWebAppModule().getWlWebAppBean() != null && var1.getWebAppModule().getWlWebAppBean().getComponentFactoryClassName().length > 0) {
            var2 = var1.getWebAppModule().getWlWebAppBean().getComponentFactoryClassName()[0];
         }

         this.usesSpringExtensionModel = this.pitchforkContext.isSpringComponentFactoryClassName();

         try {
            this.webCompContributorBroker.initialize(this.context.getServletClassLoader(), "/META-INF/spring-web.xml", PitchforkContext.getComponentFactoryClassName(var2), this.usesSpringExtensionModel, this);
         } catch (Throwable var4) {
            diLogger.debug("Exception when creating spring bean factory" + StackTraceUtils.throwable2StackTrace(var4));
            throw new DeploymentException(var4);
         }
      }
   }

   protected void contribute(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
   }

   public Jsr250MetadataI getMetadata(String var1) {
      throw new AssertionError("This method is not supported!");
   }

   static void dbg(String var0) {
      if (diLogger.isDebugEnabled()) {
         diLogger.debug(var0);
      }

   }

   public void contribute(EnricherI var1) {
      Debug.assertion(this.context != null, "not initialized");
      if (this.useDI) {
         Set var2 = super.getInjectableTargetClasses(this.webBean);
         List var3 = Arrays.asList(var1.getRegisteredBeanDefinitionNames());
         ServletBean[] var4 = this.webBean.getServlets();
         int var5 = var4.length;

         int var6;
         String var8;
         for(var6 = 0; var6 < var5; ++var6) {
            ServletBean var7 = var4[var6];
            var8 = var7.getServletClass();
            if (var8 != null && (var3.contains(var8) || this.containsAssignableClass(var2, var8))) {
               dbg("injecting metadata for Servlet " + var8);
               this.contribute(var1, var8);
            }
         }

         FilterBean[] var9 = this.webBean.getFilters();
         var5 = var9.length;

         for(var6 = 0; var6 < var5; ++var6) {
            FilterBean var13 = var9[var6];
            var8 = var13.getFilterClass();
            if (var3.contains(var8) || this.containsAssignableClass(var2, var8)) {
               dbg("injecting metadata for Filter " + var8);
               this.contribute(var1, var8);
            }
         }

         ListenerBean[] var10 = this.webBean.getListeners();
         var5 = var10.length;

         for(var6 = 0; var6 < var5; ++var6) {
            ListenerBean var14 = var10[var6];
            var8 = var14.getListenerClass();
            if (var3.contains(var8) || this.containsAssignableClass(var2, var8)) {
               dbg("injecting metadata for Listener " + var8);
               this.contribute(var1, var8);
            }
         }

         Iterator var11 = this.context.getHelper().getTagHandlers(false).iterator();

         while(true) {
            String var15;
            do {
               Object var12;
               if (!var11.hasNext()) {
                  var11 = this.context.getHelper().getTagListeners(false).iterator();

                  while(true) {
                     do {
                        if (!var11.hasNext()) {
                           var11 = this.context.getHelper().getManagedBeanClasses().iterator();

                           while(true) {
                              do {
                                 if (!var11.hasNext()) {
                                    return;
                                 }

                                 var12 = var11.next();
                                 var15 = (String)var12;
                              } while(!var3.contains(var15) && !this.containsAssignableClass(var2, var15));

                              dbg("injecting metadata for managed bean" + var15);
                              this.contribute(var1, var15);
                           }
                        }

                        var12 = var11.next();
                        var15 = (String)var12;
                     } while(!var3.contains(var15) && !this.containsAssignableClass(var2, var15));

                     dbg("injecting metadata for tld listener " + var15);
                     this.contribute(var1, var15);
                  }
               }

               var12 = var11.next();
               var15 = (String)var12;
            } while(!var3.contains(var15) && !this.containsAssignableClass(var2, var15));

            dbg("injecting metadata for taglib handler " + var15);
            this.contribute(var1, var15);
         }
      }
   }

   private boolean containsAssignableClass(Set var1, String var2) {
      if (var1.contains(var2)) {
         return true;
      } else {
         Class var3 = null;

         try {
            var3 = this.context.getServletClassLoader().loadClass(var2);
         } catch (ClassNotFoundException var5) {
            return false;
         }

         do {
            if (var3.equals(Object.class)) {
               return false;
            }

            var3 = var3.getSuperclass();
         } while(!var1.contains(var3.getName()));

         return true;
      }
   }

   private void contribute(EnricherI var1, String var2) {
      this.contribute(var1, var2, var2, this.webBean);
   }

   public Jsr250MetadataI newJsr250Metadata(String var1, Class<?> var2, DeploymentUnitMetadataI var3) {
      Debug.assertion(this.context != null, "not initialized");
      Jsr250MetadataI var4 = this.webCompContributorBroker.createJsr250Metadata(var3, var1, var2);
      this.diClasses.put(var1, var4);
      return var4;
   }

   private Object getNewInstance(String var1, ClassLoader var2) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
      Class var3;
      if (this.usesSpringExtensionModel && !var1.startsWith("weblogic")) {
         if (!this.diClasses.containsKey(var1)) {
            var3 = var2.loadClass(var1);
            return var3.newInstance();
         } else {
            return this.webCompContributorBroker.getNewInstance(var1);
         }
      } else {
         var3 = var2.loadClass(var1);
         Object var4 = var3.newInstance();

         try {
            this.inject(var4);
            this.notifyPostConstruct(var4);
            return var4;
         } catch (Exception var6) {
            return var3.newInstance();
         }
      }
   }

   public Servlet createServletInstance(String var1) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
      Debug.assertion(this.context != null, "not initialized");
      return (Servlet)this.getNewInstance(var1, this.context.getServletClassLoader());
   }

   public Filter createFilterInstance(String var1) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
      Debug.assertion(this.context != null, "not initialized");
      return (Filter)this.getNewInstance(var1, this.context.getServletClassLoader());
   }

   public EventListener createListenerInstance(String var1) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
      Debug.assertion(this.context != null, "not initialized");
      return (EventListener)this.getNewInstance(var1, this.context.getServletClassLoader());
   }

   public void inject(Object var1) {
      if (this.useDI) {
         String var2 = var1.getClass().getName();

         try {
            Jsr250MetadataI var3 = (Jsr250MetadataI)this.diClasses.get(var2);
            if (var3 != null) {
               var3.inject(var1);
            }

         } catch (RuntimeException var4) {
            this.diClasses.remove(var2);
            HTTPLogger.logDependencyInjectionFailed(this.context.getLogContext(), var2, var4);
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Dependency injection failed for class " + var2, var4);
            }

            throw var4;
         }
      }
   }

   public void notifyPostConstruct(Object var1) {
      this.invokeLifecycleMethods(var1, LifecycleEvent.POST_CONSTRUCT);
   }

   public void notifyPreDestroy(Object var1) {
      this.invokeLifecycleMethods(var1, LifecycleEvent.PRE_DESTROY);
   }

   private void invokeLifecycleMethods(Object var1, LifecycleEvent var2) {
      if (this.useDI) {
         Jsr250MetadataI var3 = (Jsr250MetadataI)this.diClasses.get(var1.getClass().getName());
         if (var3 != null) {
            var3.invokeLifecycleMethods(var1, var2);
         }

      }
   }

   private void checkDuplicatedCallback(LifecycleCallbackBean[] var1, LifecycleEvent var2) throws DeploymentException {
      if (var1 != null && var1.length >= 2) {
         HashMap var3 = new HashMap(var1.length);
         LifecycleCallbackBean[] var4 = var1;
         int var5 = var1.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            LifecycleCallbackBean var7 = var4[var6];
            String var8 = (String)var3.put(var7.getLifecycleCallbackClass(), var7.getLifecycleCallbackMethod());
            if (var8 != null && !var8.equals(var7.getLifecycleCallbackMethod())) {
               throw new DeploymentException("There are multiple lifecycle callbacks declared for event " + var2 + " on class: " + var7.getLifecycleCallbackClass());
            }
         }

      }
   }

   protected void addLifecycleMethods(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
      LifecycleCallbackBean[] var3 = this.getComponentCallbackBeans(var1, var2.getPostConstructs());

      int var4;
      for(var4 = var3.length - 1; var4 > -1; --var4) {
         this.addLifecycleMethods(var1, var3[var4], LifecycleEvent.POST_CONSTRUCT);
      }

      var3 = this.getComponentCallbackBeans(var1, var2.getPreDestroys());

      for(var4 = 0; var4 < var3.length; ++var4) {
         this.addLifecycleMethods(var1, var3[var4], LifecycleEvent.PRE_DESTROY);
      }

   }

   private LifecycleCallbackBean[] getComponentCallbackBeans(Jsr250MetadataI var1, LifecycleCallbackBean[] var2) {
      if (var2 != null && var2.length != 0) {
         TreeSet var3 = this.sortCallbackBeans(var1, var2);
         var3 = this.removeOverridenCallbackBeans(var3);
         return (LifecycleCallbackBean[])var3.toArray(new LifecycleCallbackBean[var3.size()]);
      } else {
         return new LifecycleCallbackBean[0];
      }
   }

   private TreeSet<LifecycleCallbackBean> sortCallbackBeans(Jsr250MetadataI var1, LifecycleCallbackBean[] var2) {
      TreeSet var3 = new TreeSet(new Comparator() {
         public int compare(Object var1, Object var2) {
            LifecycleCallbackBean var3 = (LifecycleCallbackBean)var1;
            LifecycleCallbackBean var4 = (LifecycleCallbackBean)var2;
            Class var5 = WebComponentContributor.this.loadClass(var3.getLifecycleCallbackClass(), WebComponentContributor.this.classLoader);
            Class var6 = WebComponentContributor.this.loadClass(var4.getLifecycleCallbackClass(), WebComponentContributor.this.classLoader);
            if (!var5.isAssignableFrom(var6)) {
               return -1;
            } else if (var5 != var6) {
               return 1;
            } else {
               return var3.getLifecycleCallbackMethod().equals(var4.getLifecycleCallbackMethod()) ? 0 : 1;
            }
         }
      });
      Class var4 = this.loadClass(var1.getComponentName(), this.classLoader);
      LifecycleCallbackBean[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         LifecycleCallbackBean var8 = var5[var7];
         Class var9 = this.loadClass(var8.getLifecycleCallbackClass(), this.classLoader);
         if (var9.isAssignableFrom(var4)) {
            var3.add(var8);
         }
      }

      return var3;
   }

   private TreeSet<LifecycleCallbackBean> removeOverridenCallbackBeans(TreeSet<LifecycleCallbackBean> var1) {
      HashSet var2 = new HashSet(var1.size());
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         LifecycleCallbackBean var4 = (LifecycleCallbackBean)var3.next();
         String var5 = var4.getLifecycleCallbackMethod();
         if (!var2.contains(var5)) {
            var2.add(var5);
         } else {
            Class var6 = this.loadClass(var4.getLifecycleCallbackClass(), this.classLoader);
            Method var7 = this.getDeclaredMethod(var6, var5, new Class[0]);
            if (!Modifier.isPrivate(var7.getModifiers())) {
               var3.remove();
            }
         }
      }

      return var1;
   }
}
