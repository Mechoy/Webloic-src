package weblogic.ejb.container.deployer;

import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.ws.WebServiceProvider;
import weblogic.application.ApplicationContextInternal;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.deployment.PersistenceUnitInfoImpl;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.compliance.ComplianceException;
import weblogic.ejb.container.compliance.Ejb30SessionBeanClassChecker;
import weblogic.ejb.container.dd.xml.DDUtils;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.DeploymentInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionHome;
import weblogic.ejb.container.interfaces.MethodInfo;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.spi.BusinessObject;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.InitMethodBean;
import weblogic.j2ee.descriptor.MethodParamsBean;
import weblogic.j2ee.descriptor.NamedMethodBean;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.RemoveMethodBean;
import weblogic.j2ee.descriptor.SessionBeanBean;
import weblogic.logging.Loggable;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.AssertionError;
import weblogic.utils.classloaders.GenericClassLoader;

final class Ejb3SessionBeanInfoImpl extends SessionBeanInfoImpl implements Ejb3SessionBeanInfo {
   private Set businessLocalInterfaces = new HashSet();
   private Set businessRemoteInterfaces = new HashSet();
   private Map jndiNameMap = new HashMap();
   private Map localBusinessImplClassMap = new HashMap();
   private Map remoteBusinessImplClassMap = new HashMap();
   private Map remoteBusinessIntfClassMap = new HashMap();
   private Map removeMethodToRetainIfExceptionMap = new HashMap();
   private Map interfaceMethodToRemoveMethodMap = new HashMap();
   private Set interfaceMethodWithoutRemove = new HashSet();
   private PersistenceContextRefBean[] extendedPersistenceContextRefs;

   public Ejb3SessionBeanInfoImpl(DeploymentInfo var1, CompositeMBeanDescriptor var2, GenericClassLoader var3) throws ClassNotFoundException, WLDeploymentException {
      super(var1, var2, var3);
      String[] var4 = var2.getBusinessLocals();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         Class var6 = this.loadClass(var4[var5]);
         this.businessLocalInterfaces.add(var6);
         this.checkClassLoaders(var2, var6);
      }

      String[] var9 = var2.getBusinessRemotes();

      for(int var10 = 0; var10 < var9.length; ++var10) {
         Class var7 = this.loadClass(var9[var10]);
         this.businessRemoteInterfaces.add(var7);
         this.checkClassLoaders(var2, var7);
         String var8 = var2.getBusinessJNDIName(var7);
         var8 = BaseEnvironmentBuilder.transformJNDIName(var8, var1.getApplicationName());
         if (var8 != null) {
            this.jndiNameMap.put(var7, var8);
         }
      }

      this.initializeMethodInfos();
      this.initializeRemoveMethodInfos();
      HashSet var11 = new HashSet();
      PersistenceContextRefBean[] var12 = this.getPersistenceContextRefs();

      for(int var13 = 0; var13 < var12.length; ++var13) {
         if ("Extended".equals(var12[var13].getPersistenceContextType())) {
            var11.add(var12[var13]);
         }
      }

      this.extendedPersistenceContextRefs = (PersistenceContextRefBean[])((PersistenceContextRefBean[])var11.toArray(new PersistenceContextRefBean[var11.size()]));
   }

   protected boolean needSetReplicationType(CompositeMBeanDescriptor var1) {
      if (this.getJNDIName() != null) {
         return true;
      } else {
         return var1.getBusinessRemotes().length > 0;
      }
   }

   public boolean hasWebserviceClientView() {
      return super.hasWebserviceClientView() || this.getBeanClass().isAnnotationPresent(WebService.class) || this.getBeanClass().isAnnotationPresent(WebServiceProvider.class);
   }

   public boolean hasBusinessRemotes() {
      return !this.businessRemoteInterfaces.isEmpty();
   }

   public boolean hasBusinessLocals() {
      return !this.businessLocalInterfaces.isEmpty();
   }

   public String[] getBusinessLocalNames() {
      return this.m_desc.getBusinessLocals();
   }

   public String[] getBusinessRemoteNames() {
      return this.m_desc.getBusinessRemotes();
   }

   public Map getRemoteBusinessJNDINames() {
      return this.jndiNameMap;
   }

   public Set getBusinessLocals() {
      return this.businessLocalInterfaces;
   }

   public Set getBusinessRemotes() {
      return this.businessRemoteInterfaces;
   }

   public boolean hasRemoteClientView() {
      return super.hasRemoteClientView() || !this.businessRemoteInterfaces.isEmpty();
   }

   public boolean hasLocalClientView() {
      return super.hasLocalClientView() || !this.businessLocalInterfaces.isEmpty();
   }

   public Class getGeneratedLocalBusinessImplClass(Class var1) {
      return (Class)this.localBusinessImplClassMap.get(var1);
   }

   public Class getGeneratedRemoteBusinessImplClass(Class var1) {
      return (Class)this.remoteBusinessImplClassMap.get(var1);
   }

   public Class getGeneratedRemoteBusinessIntfClass(Class var1) {
      return (Class)this.remoteBusinessIntfClassMap.get(var1);
   }

   private void initializeMethodInfos() throws WLDeploymentException {
      if (this.businessRemoteInterfaces.size() > 0) {
         List var1 = Arrays.asList((Object[])BusinessObject.class.getMethods());
         this.createMethodInfoImpls(var1, "Remote", this.remoteMethods);
      }

      Iterator var4 = this.businessRemoteInterfaces.iterator();

      Class var2;
      List var3;
      while(var4.hasNext()) {
         var2 = (Class)var4.next();
         var3 = Arrays.asList((Object[])var2.getMethods());
         this.createMethodInfoImpls(var3, "Remote", this.remoteMethods);
      }

      var4 = this.businessLocalInterfaces.iterator();

      while(var4.hasNext()) {
         var2 = (Class)var4.next();
         var3 = Arrays.asList((Object[])var2.getMethods());
         this.createMethodInfoImpls(var3, "Local", this.localMethods);
      }

   }

   private void initializeRemoveMethodInfos() throws ClassNotFoundException, WLDeploymentException {
      SessionBeanBean var1 = (SessionBeanBean)this.m_desc.getBean();
      Class var2 = this.loadClass(var1.getEjbClass());
      RemoveMethodBean[] var3 = var1.getRemoveMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         RemoveMethodBean var5 = var3[var4];
         NamedMethodBean var6 = var5.getBeanMethod();
         String var7 = var6.getMethodName();
         MethodParamsBean var8 = var6.getMethodParams();
         boolean var9 = var5.isRetainIfException();
         String var10 = null;

         try {
            if (var8 == null) {
               var10 = DDUtils.getMethodSignature(var7, new String[0]);
            } else {
               var10 = DDUtils.getMethodSignature(var7, var8.getMethodParams());
            }

            MethodInfo var11 = this.getRemoteMethodInfo(var10);
            if (var11 == null) {
               var11 = this.getLocalMethodInfo(var10);
            }

            try {
               Ejb30SessionBeanClassChecker.validateRemoveMethodToBeBusinessMethod(var1, var11, var10);
            } catch (ComplianceException var14) {
               throw new WLDeploymentException("Deploy failure: ", var14);
            }

            Method var12 = var11.getMethod();
            Method var13 = var2.getMethod(var12.getName(), var12.getParameterTypes());
            this.addRemoveMethod(var13, var9);
         } catch (NoSuchMethodException var15) {
            throw new AssertionError("Should not reach", var15);
         }
      }

   }

   private void addRemoveMethod(Method var1, boolean var2) {
      if (var2) {
         this.removeMethodToRetainIfExceptionMap.put(var1, Boolean.TRUE);
      } else {
         this.removeMethodToRetainIfExceptionMap.put(var1, Boolean.FALSE);
      }

   }

   protected boolean checkIfItsSafeToUseCallByReference() {
      boolean var1 = super.checkIfItsSafeToUseCallByReference();
      if (!this.businessRemoteInterfaces.isEmpty()) {
         Iterator var2 = this.businessRemoteInterfaces.iterator();

         while(var2.hasNext()) {
            Class var3 = (Class)var2.next();
            Method[] var4 = var3.getMethods();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               if (!this.checkIfMethodCanUseCallByReference(var4[var5])) {
                  return false;
               }
            }
         }
      }

      return var1;
   }

   protected void warnIfParameterNotSerializable() {
      super.warnIfParameterNotSerializable();
      if (!this.businessRemoteInterfaces.isEmpty()) {
         Iterator var1 = this.businessRemoteInterfaces.iterator();

         while(var1.hasNext()) {
            Class var2 = (Class)var1.next();
            Method[] var3 = var2.getMethods();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.warnIfParamNotSerializableForMethod(var3[var4]);
            }
         }
      }

   }

   public void prepare(ApplicationContextInternal var1, DeploymentInfo var2) throws WLDeploymentException {
      super.prepare(var1, var2);
      NamingConvention var3 = new NamingConvention(this.getBeanClassName(), this.getEJBName());
      Iterator var4 = this.businessRemoteInterfaces.iterator();

      Class var5;
      Class var6;
      while(var4.hasNext()) {
         var5 = (Class)var4.next();

         try {
            var6 = this.loadClass(var3.getRemoteBusinessImplClassName(var5));
            this.remoteBusinessImplClassMap.put(var5, var6);
            this.setMethodDescriptors((BaseEJBHomeIntf)null, var6, var5.getMethods(), "Remote");
            if (!Remote.class.isAssignableFrom(var5)) {
               Class var7 = this.loadClass(var3.getRemoteBusinessIntfClassName(var5));
               this.remoteBusinessIntfClassMap.put(var5, var7);
            }

            this.setMethodDescriptors((BaseEJBHomeIntf)null, var6, BusinessObject.class.getMethods(), "Remote");
         } catch (ClassNotFoundException var12) {
            throw new AssertionError("Unable to load class " + var3.getRemoteBusinessImplClassName(var5), var12);
         }
      }

      var4 = this.businessLocalInterfaces.iterator();

      while(var4.hasNext()) {
         var5 = (Class)var4.next();

         try {
            var6 = this.loadClass(var3.getLocalBusinessImplClassName(var5));
            this.localBusinessImplClassMap.put(var5, var6);
            this.setMethodDescriptors((BaseEJBHomeIntf)null, var6, var5.getMethods(), "Local");
         } catch (ClassNotFoundException var11) {
            throw new AssertionError("Unable to load class " + var3.getLocalBusinessImplClassName(var5), var11);
         }
      }

      Ejb3SessionHome var14;
      if (this.hasBusinessRemotes()) {
         var14 = (Ejb3SessionHome)this.getRemoteHome();
         var14.prepare();
      }

      if (this.hasBusinessLocals()) {
         var14 = (Ejb3SessionHome)this.getLocalHome();
         var14.prepare();
      }

      var4 = this.jndiNameMap.keySet().iterator();

      while(var4.hasNext()) {
         var5 = (Class)var4.next();
         String var15 = (String)this.jndiNameMap.get(var5);

         try {
            InitialContext var16 = new InitialContext();
            Object var8 = var16.lookup(var15);
            if (var8 != null) {
               Loggable var9 = EJBLogger.logJNDINameAlreadyInUseLoggable(this.getDisplayName(), var15);
               if (!(var8 instanceof Remote)) {
                  throw new WLDeploymentException(var9.getMessage());
               }

               Remote var10 = (Remote)var8;
               if (!ServerHelper.isClusterable(var10) || ServerHelper.isLocal(var10)) {
                  throw new WLDeploymentException(var9.getMessage());
               }
            }
         } catch (NamingException var13) {
         }
      }

   }

   public EjbJndiBinder getJndiBinder() {
      if (this.jndiBinder == null) {
         this.jndiBinder = new Ejb3SessionBinder(this);
      }

      return this.jndiBinder;
   }

   public boolean isRetainifException(Method var1) {
      Method var2 = this.getRemoveMethodFromBIMethod(var1);
      Boolean var3 = (Boolean)this.removeMethodToRetainIfExceptionMap.get(var2);
      return var3 != null ? var3 : false;
   }

   public boolean isRemoveMethod(Method var1) {
      Method var2 = this.getRemoveMethodFromBIMethod(var1);
      return var2 != null;
   }

   private Method getRemoveMethodFromBIMethod(Method var1) {
      if (this.interfaceMethodWithoutRemove.contains(var1)) {
         return null;
      } else {
         Method var2 = (Method)this.interfaceMethodToRemoveMethodMap.get(var1);
         if (var2 != null) {
            return var2;
         } else if (this.removeMethodToRetainIfExceptionMap.size() < 1) {
            return null;
         } else {
            String var3 = var1.getName();
            Class[] var4 = (Class[])var1.getParameterTypes();
            Iterator var5 = this.removeMethodToRetainIfExceptionMap.keySet().iterator();
            boolean var6 = false;

            do {
               Class[] var7;
               do {
                  do {
                     if (!var5.hasNext()) {
                        this.interfaceMethodWithoutRemove.add(var1);
                        return null;
                     }

                     var2 = (Method)var5.next();
                     var7 = var2.getParameterTypes();
                  } while(!var3.equals(var2.getName()));
               } while(var4.length != var7.length);

               var6 = true;

               for(int var8 = 0; var8 < var4.length; ++var8) {
                  if (!var4[var8].equals(var7)) {
                     var6 = false;
                     break;
                  }
               }
            } while(!var6);

            this.interfaceMethodToRemoveMethodMap.put(var1, var2);
            return var2;
         }
      }
   }

   public String getEjbCreateInitMethodName(Method var1) {
      String var2 = null;
      String var3 = var1.getName();
      SessionBeanBean var4 = (SessionBeanBean)this.m_desc.getBean();
      InitMethodBean[] var5 = var4.getInitMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         Class[] var7 = (Class[])var1.getParameterTypes();
         if (var5[var6].getCreateMethod().getMethodParams() != null) {
            String[] var8 = var5[var6].getCreateMethod().getMethodParams().getMethodParams();
            if (var8.length == var7.length && var5[var6].getCreateMethod().getMethodName().equalsIgnoreCase(var1.getName())) {
               boolean var9 = true;

               for(int var10 = 0; var10 < var7.length; ++var10) {
                  if (!var7[var10].getName().equalsIgnoreCase(var8[var10])) {
                     var9 = false;
                     break;
                  }
               }

               if (var9) {
                  var2 = var5[var6].getBeanMethod().getMethodName();
                  break;
               }
            }
         } else if (var5[var6].getCreateMethod().getMethodName().equalsIgnoreCase(var1.getName())) {
            return var5[var6].getBeanMethod().getMethodName();
         }
      }

      if (var2 == null) {
         var2 = "ejb" + var3.substring(0, 1).toUpperCase(Locale.ENGLISH) + var3.substring(1);
      }

      return var2;
   }

   public boolean containsExtendedPersistenceContextRefs() {
      return this.extendedPersistenceContextRefs.length > 0;
   }

   public PersistenceContextRefBean[] getExtendedPersistenceContextRefs() {
      return this.extendedPersistenceContextRefs;
   }

   public void activate(Context var1, Map var2, Map var3, DeploymentInfo var4, Context var5) throws WLDeploymentException {
      super.activate(var1, var2, var3, var4, var5);

      try {
         var5.bind("comp/EJBContext", new SessionContextProxyImpl());
      } catch (NamingException var7) {
         throw new WLDeploymentException("Error binding EJBContext: " + var7, var7);
      }
   }

   public void setPersistenceUnitRegistry(PersistenceUnitRegistry var1) {
      if (this.containsExtendedPersistenceContextRefs()) {
         PersistenceContextRefBean[] var2 = this.getExtendedPersistenceContextRefs();
         HashSet var3 = new HashSet();

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3.add(var2[var4].getPersistenceUnitName());
         }

         HashMap var9 = new HashMap();
         Class var5 = this.getGeneratedBeanClass();
         Iterator var6 = var3.iterator();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            PersistenceUnitInfoImpl var8 = var1.getPersistenceUnit(var7);
            var9.put(var7, var8);
         }

         StatefulSessionManager var10 = (StatefulSessionManager)this.getBeanManager();
         var10.setExtendedPersistenceContextMap(var9);
      }

      this.persistenceUnitRegistry = var1;
   }

   public void bindEJBRefs(Context var1) throws NamingException {
      super.bindEJBRefs(var1);
      if (this.hasBusinessRemotes()) {
         this.bindBusinessObjects(var1, (Ejb3SessionHome)this.getRemoteHome(), this.businessRemoteInterfaces);
      }

      if (this.hasBusinessLocals()) {
         this.bindBusinessObjects(var1, (Ejb3SessionHome)this.getLocalHome(), this.businessLocalInterfaces);
      }

   }

   public void unbindEJBRefs(Context var1) throws NamingException {
      super.unbindEJBRefs(var1);
      if (this.hasBusinessRemotes()) {
         this.unbindBusinessObjects(var1, this.businessRemoteInterfaces);
      }

      if (this.hasBusinessLocals()) {
         this.unbindBusinessObjects(var1, this.businessLocalInterfaces);
      }

   }

   private void bindBusinessObjects(Context var1, Ejb3SessionHome var2, Set var3) throws NamingException {
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Class var5 = (Class)var4.next();
         var1.bind(var5.getName(), var2.getBindableImpl(var5));
      }

   }

   private void unbindBusinessObjects(Context var1, Set var2) throws NamingException {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         var1.unbind(var4.getName());
      }

   }

   public void onUndeploy() {
      Iterator var1 = this.jndiNameMap.keySet().iterator();

      while(var1.hasNext()) {
         Class var2 = (Class)var1.next();
         Ejb3SessionHome var3 = (Ejb3SessionHome)this.getRemoteHome();
         Object var4 = var3.getBindableImpl(var2);
         if (Remote.class.isAssignableFrom(var2)) {
            try {
               ServerHelper.unexportObject(var4, true, true);
            } catch (NoSuchObjectException var6) {
            }
         }
      }

      try {
         this.getBeanManager().getEnvironmentContext().unbind("comp/EJBContext");
      } catch (NamingException var7) {
         if (debugLogger.isDebugEnabled()) {
            debug("error unbinding EJBContext from local environment: " + var7);
         }
      }

      super.onUndeploy();
   }

   private static void debug(String var0) {
      debugLogger.debug("[Ejb3SessionBeanInfoImpl] " + var0);
   }
}
