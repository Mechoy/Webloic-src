package weblogic.ejb.container.injection;

import com.oracle.pitchfork.interfaces.MetadataParseException;
import com.oracle.pitchfork.interfaces.ejb.BeanType;
import com.oracle.pitchfork.interfaces.ejb.EjbProxyMetadataI;
import com.oracle.pitchfork.interfaces.inject.DeploymentUnitMetadataI;
import com.oracle.pitchfork.interfaces.inject.EnricherI;
import com.oracle.pitchfork.interfaces.inject.Jsr250MetadataI;
import com.oracle.pitchfork.interfaces.inject.LifecycleEvent;
import com.oracle.pitchfork.interfaces.intercept.InterceptionMetadataI;
import com.oracle.pitchfork.interfaces.intercept.InterceptorMetadataI;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.MessageDrivenBean;
import javax.ejb.SessionBean;
import javax.ejb.SessionSynchronization;
import javax.interceptor.InvocationContext;
import weblogic.ejb.container.deployer.NamingConvention;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.ClientDrivenBeanInfo;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.MessageDrivenBeanInfo;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.ejb.container.interfaces.WLSessionBean;
import weblogic.ejb.spi.SessionBeanInfo;
import weblogic.j2ee.descriptor.AroundInvokeBean;
import weblogic.j2ee.descriptor.EjbCallbackBean;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.InterceptorBean;
import weblogic.j2ee.descriptor.InterceptorBindingBean;
import weblogic.j2ee.descriptor.J2eeEnvironmentBean;
import weblogic.j2ee.descriptor.LifecycleCallbackBean;
import weblogic.j2ee.descriptor.MessageDrivenBeanBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.j2ee.injection.J2eeComponentContributor;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.utils.collections.ConcurrentHashMap;

public class EjbComponentContributor extends J2eeComponentContributor {
   private final DeploymentInfo dinfo;
   private boolean defaultInterceptorAdded;
   private ConcurrentHashMap metadataMap;

   public EjbComponentContributor(DeploymentInfo var1, PitchforkContext var2) {
      super(var2);
      this.defaultInterceptorAdded = false;
      this.metadataMap = new ConcurrentHashMap();
      this.dinfo = var1;
   }

   public EjbComponentContributor(DeploymentInfo var1, ClassLoader var2, PitchforkContext var3) {
      this(var1, var3);
      if (var2 != null) {
         this.classLoader = var2;
      }

   }

   public void contribute(EnricherI var1) {
      EjbJarBean var2 = this.dinfo.getEjbDescriptorBean().getEjbJarBean();
      SessionBeanBean[] var3 = var2.getEnterpriseBeans().getSessions();
      int var4 = var3.length;

      int var5;
      BeanInfo var7;
      String var8;
      String var9;
      for(var5 = 0; var5 < var4; ++var5) {
         SessionBeanBean var6 = var3[var5];
         var7 = this.dinfo.getBeanInfo(var6.getEjbName());
         if (var7.isEJB30()) {
            var8 = var6.getEjbName();
            var9 = this.getEjbClassName(var7);
            this.debug("Convert environmentGroupBean to jsr250Metadata for EJB : " + var8);
            this.contribute(var1, var8, var9, (J2eeEnvironmentBean)var6);
         }
      }

      MessageDrivenBeanBean[] var10 = var2.getEnterpriseBeans().getMessageDrivens();
      var4 = var10.length;

      for(var5 = 0; var5 < var4; ++var5) {
         MessageDrivenBeanBean var11 = var10[var5];
         var7 = this.dinfo.getBeanInfo(var11.getEjbName());
         if (var7.isEJB30()) {
            var8 = var11.getEjbName();
            var9 = this.getEjbClassName(var7);
            this.debug("Convert environmentGroupBean to jsr250Metadata for EJB : " + var8);
            this.contribute(var1, var8, var9, (J2eeEnvironmentBean)var11);
         }
      }

   }

   public Jsr250MetadataI newJsr250Metadata(String var1, Class<?> var2, DeploymentUnitMetadataI var3) {
      EnvironmentInterceptorCallbackImpl var4 = new EnvironmentInterceptorCallbackImpl();
      EjbProxyMetadataI var5 = this.pitchforkContext.getPitchforkUtils().createEjbProxyMetadata(var1, var2, var3, var4);
      BeanInfo var6 = this.dinfo.getBeanInfo(var1);
      if (var6.isSessionBean()) {
         if (((SessionBeanInfo)var6).isStateful()) {
            var5.setBeanType(BeanType.STATEFUL);
         } else {
            var5.setBeanType(BeanType.STATELESS);
         }
      } else if (!var6.isEntityBean()) {
         var5.setBeanType(BeanType.MESSAGE_DRIVEN);
      }

      this.metadataMap.put(var1, var5);
      return var5;
   }

   public Jsr250MetadataI getMetadata(String var1) {
      return (Jsr250MetadataI)this.metadataMap.get(var1);
   }

   public ConcurrentHashMap getMetadataMap() {
      return this.metadataMap;
   }

   private String getEjbClassName(BeanInfo var1) {
      String var2;
      if (var1 instanceof ClientDrivenBeanInfo) {
         var2 = ((ClientDrivenBeanInfo)var1).getGeneratedBeanClassName();
      } else if (var1 instanceof MessageDrivenBeanInfo && ((MessageDrivenBeanInfo)var1).isIndirectlyImplMessageListener()) {
         var2 = ((MessageDrivenBeanInfo)var1).getGeneratedBeanClassName();
      } else {
         var2 = var1.getBeanClassName();
      }

      return var2;
   }

   private Set<Class> getEjbIntfClasses(BeanInfo var1) {
      HashSet var2 = new HashSet();
      if (var1 instanceof ClientDrivenBeanInfo) {
         if (var1 instanceof Ejb3SessionBeanInfo) {
            Ejb3SessionBeanInfo var3 = (Ejb3SessionBeanInfo)var1;
            if (var3.hasBusinessLocals()) {
               var2.addAll(var3.getBusinessLocals());
            }

            if (var3.hasBusinessRemotes()) {
               var2.addAll(var3.getBusinessRemotes());
            }
         }

         ClientDrivenBeanInfo var4 = (ClientDrivenBeanInfo)var1;
         if (var4.getLocalInterfaceClass() != null) {
            var2.add(var4.getLocalInterfaceClass());
         }

         if (var4.getRemoteInterfaceClass() != null) {
            var2.add(var4.getRemoteInterfaceClass());
         }
      } else {
         if (!(var1 instanceof MessageDrivenBeanInfo)) {
            throw new MetadataParseException(var1.getBeanClassName() + " is unknow bean type.");
         }

         var2.add(((MessageDrivenBeanInfo)var1).getMessagingTypeInterfaceClass());
      }

      return var2;
   }

   private Set<Method> getBeanControlInterfaceMethods(Class var1, Set<Class> var2) {
      HashSet var3 = new HashSet();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Class var5 = (Class)var4.next();
         this.addBeanControlInterfaceMethods(var1, var3, var5);
      }

      return var3;
   }

   private void addBeanControlInterfaceMethods(Class var1, Set<Method> var2, Class var3) {
      Method[] var4 = var3.getDeclaredMethods();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         Method var7 = var4[var6];

         try {
            var2.add(var1.getMethod(var7.getName(), var7.getParameterTypes()));
         } catch (Exception var9) {
         }
      }

      Class[] var10 = var3.getInterfaces();
      var5 = var10.length;

      for(var6 = 0; var6 < var5; ++var6) {
         Class var11 = var10[var6];
         this.addBeanControlInterfaceMethods(var1, var2, var11);
      }

   }

   private void addBeanControlInterfaceMethodsForWebService(Class var1, Set<Method> var2, Collection var3) {
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Method var5 = ((MethodInfo)var4.next()).getMethod();

         try {
            var2.add(var1.getMethod(var5.getName(), var5.getParameterTypes()));
         } catch (Exception var7) {
         }
      }

   }

   protected void contribute(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
      this.buildInterceptionMetadata((InterceptionMetadataI)var1, var2);
   }

   private void buildInterceptionMetadata(InterceptionMetadataI var1, J2eeEnvironmentBean var2) {
      String var3 = null;
      String var4 = null;
      AroundInvokeBean[] var5 = null;
      BeanInfo var6 = this.dinfo.getBeanInfo(var1.getComponentName());
      Class var7 = null;
      String var8;
      if (var2 instanceof SessionBeanBean) {
         SessionBeanBean var9 = (SessionBeanBean)var2;
         var3 = var9.getEjbName();
         var4 = var9.getEjbClass();
         var5 = var9.getAroundInvokes();
         var7 = ((ClientDrivenBeanInfo)var6).getGeneratedBeanInterface();
         if (var6.getTimeoutMethod() != null) {
            var8 = var6.getTimeoutMethod().getName();
            ((EjbProxyMetadataI)var1).setTimeoutMethodName(var8);
         }
      } else {
         if (!(var2 instanceof MessageDrivenBeanBean)) {
            throw new MetadataParseException("Bean: " + var2 + " is not a SessionBean or MessageDrivenBean.");
         }

         MessageDrivenBeanBean var13 = (MessageDrivenBeanBean)var2;
         var3 = var13.getEjbName();
         var4 = var13.getEjbClass();
         var5 = var13.getAroundInvokes();
         var7 = ((MessageDrivenBeanInfo)var6).getMessagingTypeInterfaceClass();
         if (var6.getTimeoutMethod() != null) {
            var8 = var6.getTimeoutMethod().getName();
            ((EjbProxyMetadataI)var1).setTimeoutMethodName(var8);
         }
      }

      Set var14 = this.getEjbIntfClasses(var6);
      List var10 = var1.findBusinessInterfacesFromClassOrAnnotation(var1.getComponentClass());
      if (var2 instanceof SessionBeanBean) {
         var10.removeAll(var14);
         if (!var10.contains(SessionBean.class)) {
            var10.add(SessionBean.class);
         }
      } else if (var2 instanceof MessageDrivenBeanBean && !var10.contains(MessageDrivenBean.class)) {
         var10.add(MessageDrivenBean.class);
      }

      var10.remove(var7);
      Iterator var11 = var10.iterator();

      while(var11.hasNext()) {
         Class var12 = (Class)var11.next();
         var1.addBusinessInterface(var12);
      }

      Set var15 = this.getBeanControlInterfaceMethods(var7, var14);
      if (var2 instanceof SessionBeanBean && ((ClientDrivenBeanInfo)var6).hasWebserviceClientView()) {
         Collection var16 = ((ClientDrivenBeanInfo)var6).getAllWebserviceMethodInfos();
         this.addBeanControlInterfaceMethodsForWebService(var7, var15, var16);
      }

      var1.setBeanControlInterface(var7);
      var1.setBeanControlInterfaceMethods(var15);
      if (var6.isSessionBean()) {
         var1.getDeploymentUnitMetadata().registerContainerControlInterface(WLSessionBean.class);
         var1.getDeploymentUnitMetadata().registerContainerControlInterface(WLEnterpriseBean.class);
         if (SessionSynchronization.class.isAssignableFrom(var1.getComponentClass())) {
            var1.getDeploymentUnitMetadata().registerContainerControlInterface(SessionSynchronization.class);
         }
      } else {
         var1.getDeploymentUnitMetadata().registerContainerControlInterface(WLEnterpriseBean.class);
      }

      this.addDefaultInterceptors(var1, this.dinfo.getEjbDescriptorBean().getEjbJarBean());
      this.addClassLevelInterceptors(var1, this.dinfo.getEjbDescriptorBean().getEjbJarBean(), var3, var1.getComponentClass());
      this.addMethodLevelInterceptors(var1, this.dinfo.getEjbDescriptorBean().getEjbJarBean(), var3, var7);
      this.addSelfInterceptors(var1, var5, var4);
   }

   private void addDefaultInterceptors(InterceptionMetadataI var1, EjbJarBean var2) {
      if (!this.defaultInterceptorAdded) {
         if (var2.getAssemblyDescriptor() == null) {
            this.defaultInterceptorAdded = true;
         } else {
            HashSet var3 = new HashSet();
            InterceptorBindingBean[] var4 = var2.getAssemblyDescriptor().getInterceptorBindings();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               InterceptorBindingBean var7 = var4[var6];
               if ("*".equals(var7.getEjbName())) {
                  String[] var8 = null;
                  if (var7.getInterceptorOrder() != null) {
                     var8 = var7.getInterceptorOrder().getInterceptorClasses();
                  } else {
                     var8 = var7.getInterceptorClasses();
                  }

                  String[] var9 = var8;
                  int var10 = var8.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String var12 = var9[var11];
                     if (!var3.contains(var12)) {
                        InterceptorMetadataI var13 = this.createInterceptorMetadata(var12, (Method)null);
                        var13.setDefaultInterceptor(true);
                        var1.getDeploymentUnitMetadata().registerDefaultInterceptorMetadata(var13);
                        var3.add(var12);
                     }
                  }
               }
            }

            this.defaultInterceptorAdded = true;
         }
      }
   }

   private void addClassLevelInterceptors(InterceptionMetadataI var1, EjbJarBean var2, String var3, Class var4) {
      if (var2.getAssemblyDescriptor() != null) {
         InterceptorBindingBean[] var5 = var2.getAssemblyDescriptor().getInterceptorBindings();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            InterceptorBindingBean var8 = var5[var7];
            if (var3.equals(var8.getEjbName()) && var8.getMethod() == null) {
               if (var8.getInterceptorOrder() != null) {
                  this.createInterceptorOrder(var1, var8, (Method)null);
               } else {
                  String[] var9 = var8.getInterceptorClasses();
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String var12 = var9[var11];
                     InterceptorMetadataI var13 = this.createInterceptorMetadata(var12, (Method)null);
                     var13.setClassInterceptor(true);
                     var1.registerInterceptorMetadata(var13);
                  }

                  this.setExcludeInterceptors(var8, var1, var4);
               }
            }
         }

      }
   }

   private void addMethodLevelInterceptors(InterceptionMetadataI var1, EjbJarBean var2, String var3, Class var4) {
      if (var2.getAssemblyDescriptor() != null) {
         InterceptorBindingBean[] var5 = var2.getAssemblyDescriptor().getInterceptorBindings();
         int var6 = var5.length;

         label75:
         for(int var7 = 0; var7 < var6; ++var7) {
            InterceptorBindingBean var8 = var5[var7];
            if (var3.equals(var8.getEjbName()) && var8.getMethod() != null) {
               ArrayList var9 = new ArrayList();
               Method[] var10;
               int var24;
               if (var8.getMethod().getMethodParams() == null) {
                  var10 = var4.getDeclaredMethods();
                  int var11 = var10.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     Method var13 = var10[var12];
                     if (var8.getMethod().getMethodName().equals(var13.getName())) {
                        var9.add(var13);
                     }
                  }
               } else {
                  var10 = null;
                  String[] var20 = new String[0];
                  if (var8.getMethod().getMethodParams() != null) {
                     var20 = var8.getMethod().getMethodParams().getMethodParams();
                  }

                  Class[] var22 = new Class[var20.length];
                  var24 = 0;
                  String[] var14 = var20;
                  int var15 = var20.length;

                  for(int var16 = 0; var16 < var15; ++var16) {
                     String var17 = var14[var16];
                     var22[var24] = this.forName(var17, this.classLoader);
                     ++var24;
                  }

                  Method var18 = this.getDeclaredMethod(var4, var8.getMethod().getMethodName(), var22);
                  var9.add(var18);
               }

               Iterator var19 = var9.iterator();

               while(true) {
                  while(true) {
                     if (!var19.hasNext()) {
                        continue label75;
                     }

                     Method var21 = (Method)var19.next();
                     if (var8.getInterceptorOrder() != null) {
                        this.createInterceptorOrder(var1, var8, var21);
                     } else {
                        String[] var23 = var8.getInterceptorClasses();
                        var24 = var23.length;

                        for(int var25 = 0; var25 < var24; ++var25) {
                           String var26 = var23[var25];
                           InterceptorMetadataI var27 = this.createInterceptorMetadata(var26, var21);
                           var1.registerInterceptorMetadata(var27);
                        }

                        this.setExcludeInterceptors(var8, var1, var21);
                     }
                  }
               }
            }
         }

      }
   }

   private void addSelfInterceptors(InterceptionMetadataI var1, AroundInvokeBean[] var2, String var3) {
      AroundInvokeBean[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         AroundInvokeBean var7 = var4[var6];
         Class var8;
         if (var7.getClassName() != null) {
            var8 = this.loadClass(var7.getClassName(), this.classLoader);
         } else {
            var8 = this.loadClass(var3, this.classLoader);
         }

         Method var9 = this.getDeclaredMethod(var8, var7.getMethodName(), new Class[]{InvocationContext.class});
         var1.registerSelfInterceptorMethod(var9);
      }

   }

   private void createInterceptorOrder(InterceptionMetadataI var1, InterceptorBindingBean var2, Method var3) {
      String[] var4 = var2.getInterceptorOrder().getInterceptorClasses();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         InterceptorMetadataI var8 = this.createInterceptorMetadata(var7, var3);
         Object var9 = null;
         if (var3 != null) {
            var9 = var3;
         } else {
            var9 = var1.getComponentClass();
         }

         var1.registerInterceptorOrder(var8, var9);
      }

   }

   private InterceptorMetadataI createInterceptorMetadata(String var1, Method var2) {
      Class var3 = this.loadClass(var1, this.classLoader);
      InterceptorBean var4 = null;
      InterceptorBean[] var5 = this.dinfo.getEjbDescriptorBean().getEjbJarBean().getInterceptors().getInterceptors();
      InterceptorBean[] var6 = var5;
      int var7 = var5.length;

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         InterceptorBean var9 = var6[var8];
         if (var3.getName().equals(var9.getInterceptorClass())) {
            var4 = var9;
         }
      }

      this.debug("+++++adding interceptor=" + var3.getName() + " on method " + var2);
      ArrayList var12 = new ArrayList();
      AroundInvokeBean[] var13 = var4.getAroundInvokes();
      var8 = var13.length;

      for(int var16 = 0; var16 < var8; ++var16) {
         AroundInvokeBean var10 = var13[var16];
         Class var11;
         if (var3.getName().equals(var10.getClassName())) {
            var11 = var3;
         } else {
            var11 = this.loadClass(var10.getClassName(), this.classLoader);
         }

         var12.add(this.getDeclaredMethod(var11, var10.getMethodName(), new Class[]{InvocationContext.class}));
      }

      if (!Serializable.class.isAssignableFrom(var3)) {
         NamingConvention var14 = new NamingConvention(var1);
         var3 = this.loadClass(var14.getInterceptorImplClassName(), this.classLoader);
      }

      InterceptorMetadataI var15 = this.pitchforkContext.getPitchforkUtils().createInterceptorMetadata(var3, var12, var2);
      this.buildInjectionMetadata(var15, var4);
      this.addLifecycleMethods(var15, var4, true);
      return var15;
   }

   private void setExcludeInterceptors(InterceptorBindingBean var1, InterceptionMetadataI var2, Object var3) {
      if (var1.isExcludeClassInterceptors()) {
         var2.setExcludeClassInterceptors(var3);
         this.debug("-----excludeClassInterceptors for " + var3);
      }

      if (var1.isExcludeDefaultInterceptors()) {
         var2.setExcludeDefaultInterceptors(var3);
         this.debug("-----excludeDefaultInterceptors for " + var3);
      }

   }

   protected void addLifecycleMethods(Jsr250MetadataI var1, J2eeEnvironmentBean var2) {
      this.addLifecycleMethods(var1, var2, false);
   }

   protected void addLifecycleMethods(Jsr250MetadataI var1, J2eeEnvironmentBean var2, boolean var3) {
      super.addLifecycleMethods(var1, var2);
      if (var2 instanceof EjbCallbackBean) {
         EjbCallbackBean var4 = (EjbCallbackBean)var2;
         LifecycleCallbackBean[] var5 = var4.getPostActivates();
         int var6 = var5.length;

         int var7;
         LifecycleCallbackBean var8;
         for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            this.addLifecycleMethods(var1, var8, LifecycleEvent.POST_ACTIVATE);
         }

         var5 = var4.getPrePassivates();
         var6 = var5.length;

         for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            this.addLifecycleMethods(var1, var8, LifecycleEvent.PRE_PASSIVATE);
         }
      }

      if (!var3) {
         BeanInfo var9 = this.dinfo.getBeanInfo(var1.getComponentName());
         boolean var10 = var9.isSessionBean() && ((SessionBeanInfo)var9).isStateful();
         this.addEjb21LifecycleMethods(var1, LifecycleEvent.POST_CONSTRUCT, "ejbCreate", var10);
         boolean var11 = SessionBean.class.isAssignableFrom(var9.getBeanClass()) || MessageDrivenBean.class.isAssignableFrom(var9.getBeanClass());
         if (var11) {
            this.addEjb21LifecycleMethods(var1, LifecycleEvent.PRE_DESTROY, "ejbRemove", var10);
            if (var10) {
               this.addEjb21LifecycleMethods(var1, LifecycleEvent.POST_ACTIVATE, "ejbActivate", var10);
               this.addEjb21LifecycleMethods(var1, LifecycleEvent.PRE_PASSIVATE, "ejbPassivate", var10);
            }
         }

      }
   }

   private void addEjb21LifecycleMethods(Jsr250MetadataI var1, LifecycleEvent var2, String var3, boolean var4) {
      Method var5 = null;
      Method[] var6 = var1.getComponentClass().getDeclaredMethods();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Method var9 = var6[var8];
         if (var3.equals(var9.getName()) && var9.getParameterTypes().length == 0) {
            var5 = var9;
            break;
         }
      }

      if (var5 != null) {
         if ("ejbCreate".equals(var3) && (var5.getAnnotation(PostConstruct.class) != null || var4)) {
            return;
         }

         this.debug("+++++adding ejb2.x callbackMethod=" + var3 + " for bean " + var1.getComponentName());
         var1.registerLifecycleEventCallbackMethod(var2, var5);
      }

   }

   protected void debug(String var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("[EjbComponentContributor] " + var1);
      }

   }
}
