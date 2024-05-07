package weblogic.j2ee;

import com.bea.wls.redef.runtime.ClassRedefinitionRuntimeMBean;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.descriptor.DescriptorBean;
import weblogic.ejb.spi.QueryCache;
import weblogic.ejb.spi.ReInitializableCache;
import weblogic.health.HealthMonitorService;
import weblogic.health.HealthState;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.CoherenceClusterRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.management.runtime.MaxThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.MinThreadsConstraintRuntimeMBean;
import weblogic.management.runtime.QueryCacheRuntimeMBean;
import weblogic.management.runtime.RequestClassRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.management.runtime.WseeRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.work.RequestClassRuntimeMBeanImpl;
import weblogic.work.WorkManager;

public final class J2EEApplicationRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ApplicationRuntimeMBean, KodoPersistenceUnitParent {
   private static final long serialVersionUID = 5618173476456700945L;
   private final String appName;
   private final String appId;
   private final String appVersion;
   private int activeVersionState = 0;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private ReInitializableCache[] applicationCaches;
   private Set workManagerRuntimes = new HashSet();
   private Set minThreadsConstraintRuntimes = new HashSet();
   private Set maxThreadsConstraintRuntimes = new HashSet();
   private Set requestClassRuntimes = new HashSet();
   private ClassRedefinitionRuntimeMBean classRedefRuntime;
   private LibraryRuntimeMBean[] libraryRuntimes = null;
   private LibraryRuntimeMBean[] optionalPackages = null;
   private Set wseeRuntimes = new HashSet();
   private Map queryCacheRuntimes = new HashMap();
   private Map m_runtimePersistenUnit = new HashMap();
   private CoherenceClusterRuntimeMBean coherenceClusterRuntimeMBean;
   private final HashSet<WseeV2RuntimeMBean> wseeV2Runtimes = new HashSet();

   public J2EEApplicationRuntimeMBeanImpl(String var1, SystemResourceMBean var2) throws ManagementException {
      super(var1, (RuntimeMBean)null, true, (DescriptorBean)null);
      this.appName = var2.getName();
      this.appId = var2.getName();
      this.appVersion = null;
   }

   public J2EEApplicationRuntimeMBeanImpl(String var1, AppDeploymentMBean var2) throws ManagementException {
      super(var1, (RuntimeMBean)null, true, (DescriptorBean)null);
      this.appName = var2.getApplicationName();
      this.appId = var2.getApplicationIdentifier();
      this.appVersion = var2.getVersionIdentifier();
      HealthMonitorService.register(this.appId + "(Application)", this, false);
   }

   public String getApplicationName() {
      return this.appName;
   }

   public String getApplicationVersion() {
      return this.appVersion;
   }

   public int getActiveVersionState() {
      return this.activeVersionState;
   }

   public void setActiveVersionState(int var1) {
      int var2 = this.activeVersionState;
      this.activeVersionState = var1;
      this._postSet("ActiveVersionState", var2, this.activeVersionState);
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      int var1 = this.workManagerRuntimes.size();
      return (WorkManagerRuntimeMBean[])((WorkManagerRuntimeMBean[])this.workManagerRuntimes.toArray(new WorkManagerRuntimeMBean[var1]));
   }

   public WorkManagerRuntimeMBean lookupWorkManagerRuntime(String var1, String var2) {
      WorkManagerRuntimeMBean[] var3;
      if (var1 != null) {
         ComponentRuntimeMBean var4 = this.lookupComponentRuntime(var1);
         if (var4 == null) {
            return null;
         }

         var3 = var4.getWorkManagerRuntimes();
      } else {
         var3 = this.getWorkManagerRuntimes();
      }

      for(int var5 = 0; var5 < var3.length; ++var5) {
         if (var3[var5].getName().equals(var2)) {
            return var3[var5];
         }
      }

      return null;
   }

   public WorkManagerRuntimeMBean lookupWorkManagerRuntime(WorkManager var1) {
      return var1 == null ? null : this.lookupWorkManagerRuntime(var1.getModuleName(), var1.getName());
   }

   private ComponentRuntimeMBean lookupComponentRuntime(String var1) {
      ComponentRuntimeMBean[] var2 = this.getComponentRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public boolean addWorkManager(WorkManagerRuntimeMBean var1) {
      return this.workManagerRuntimes.add(var1);
   }

   public QueryCacheRuntimeMBean[] getQueryCacheRuntimes() {
      Collection var1 = this.queryCacheRuntimes.values();
      return (QueryCacheRuntimeMBean[])((QueryCacheRuntimeMBean[])var1.toArray(new QueryCacheRuntimeMBean[0]));
   }

   public QueryCacheRuntimeMBean lookupQueryCacheRuntime(String var1) {
      return (QueryCacheRuntimeMBean)this.queryCacheRuntimes.get(var1);
   }

   public void setQueryCacheRuntimes(Map var1) throws ManagementException {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         QueryCache var4 = (QueryCache)var1.get(var3);
         QueryCacheRuntimeMBean var5 = var4.createRuntimeMBean(var3, this);
         this.queryCacheRuntimes.put(var3, var5);
      }

   }

   public void setLibraryRuntimes(LibraryRuntimeMBean[] var1) {
      this.libraryRuntimes = var1;
   }

   public void setOptionalPackageRuntimes(LibraryRuntimeMBean[] var1) {
      this.optionalPackages = var1;
   }

   public LibraryRuntimeMBean[] getOptionalPackageRuntimes() {
      return this.optionalPackages;
   }

   public LibraryRuntimeMBean[] getLibraryRuntimes() {
      return this.libraryRuntimes;
   }

   public void addWseeRuntime(WseeRuntimeMBean var1) {
      this.wseeRuntimes.add(var1);
   }

   public WseeRuntimeMBean[] getWseeRuntimes() {
      int var1 = this.wseeRuntimes.size();
      return (WseeRuntimeMBean[])((WseeRuntimeMBean[])this.wseeRuntimes.toArray(new WseeRuntimeMBean[var1]));
   }

   public WseeV2RuntimeMBean[] getWseeV2Runtimes() {
      synchronized(this.wseeV2Runtimes) {
         int var2 = this.wseeV2Runtimes.size();
         return (WseeV2RuntimeMBean[])this.wseeV2Runtimes.toArray(new WseeV2RuntimeMBean[var2]);
      }
   }

   public WseeV2RuntimeMBean lookupWseeV2Runtime(String var1) {
      WseeV2RuntimeMBean var2 = null;
      synchronized(this.wseeV2Runtimes) {
         Iterator var4 = this.wseeV2Runtimes.iterator();

         while(var4.hasNext()) {
            WseeV2RuntimeMBean var5 = (WseeV2RuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.add(var1);
      }
   }

   public void removeWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.remove(var1);
      }
   }

   public MinThreadsConstraintRuntimeMBean lookupMinThreadsConstraintRuntime(String var1) {
      MinThreadsConstraintRuntimeMBean[] var2 = this.getMinThreadsConstraintRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public RequestClassRuntimeMBean lookupRequestClassRuntime(String var1) {
      RequestClassRuntimeMBean[] var2 = this.getRequestClassRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public MaxThreadsConstraintRuntimeMBean lookupMaxThreadsConstraintRuntime(String var1) {
      MaxThreadsConstraintRuntimeMBean[] var2 = this.getMaxThreadsConstraintRuntimes();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].getName().equals(var1)) {
            return var2[var3];
         }
      }

      return null;
   }

   public boolean addMaxThreadsConstraint(MaxThreadsConstraintRuntimeMBean var1) {
      return this.maxThreadsConstraintRuntimes.add(var1);
   }

   public MaxThreadsConstraintRuntimeMBean[] getMaxThreadsConstraintRuntimes() {
      int var1 = this.maxThreadsConstraintRuntimes.size();
      return (MaxThreadsConstraintRuntimeMBean[])((MaxThreadsConstraintRuntimeMBean[])this.maxThreadsConstraintRuntimes.toArray(new MaxThreadsConstraintRuntimeMBean[var1]));
   }

   public boolean addMinThreadsConstraint(MinThreadsConstraintRuntimeMBean var1) {
      return this.minThreadsConstraintRuntimes.add(var1);
   }

   public boolean addRequestClass(RequestClassRuntimeMBeanImpl var1) {
      return this.requestClassRuntimes.add(var1);
   }

   public RequestClassRuntimeMBean[] getRequestClassRuntimes() {
      int var1 = this.requestClassRuntimes.size();
      return (RequestClassRuntimeMBean[])((RequestClassRuntimeMBean[])this.requestClassRuntimes.toArray(new RequestClassRuntimeMBean[var1]));
   }

   public MinThreadsConstraintRuntimeMBean[] getMinThreadsConstraintRuntimes() {
      int var1 = this.minThreadsConstraintRuntimes.size();
      return (MinThreadsConstraintRuntimeMBean[])((MinThreadsConstraintRuntimeMBean[])this.minThreadsConstraintRuntimes.toArray(new MinThreadsConstraintRuntimeMBean[var1]));
   }

   public synchronized void removeChild(RuntimeMBeanDelegate var1) {
      if (var1 instanceof WorkManagerRuntimeMBean) {
         this.workManagerRuntimes.remove(var1);
      } else if (var1 instanceof RequestClassRuntimeMBean) {
         this.requestClassRuntimes.remove(var1);
      } else if (var1 instanceof MinThreadsConstraintRuntimeMBean) {
         this.minThreadsConstraintRuntimes.remove(var1);
      } else if (var1 instanceof MaxThreadsConstraintRuntimeMBean) {
         this.maxThreadsConstraintRuntimes.remove(var1);
      } else if (var1 instanceof WseeRuntimeMBean) {
         this.wseeRuntimes.remove(var1);
      } else if (var1 instanceof KodoPersistenceUnitRuntimeMBean) {
         this.m_runtimePersistenUnit.remove(((KodoPersistenceUnitRuntimeMBean)var1).getPersistenceUnitName());
      }

      super.removeChild(var1);
   }

   public void unregister() throws ManagementException {
      super.unregister();
      HealthMonitorService.unregister(this.appId + "(Application)");
      Iterator var1 = this.workManagerRuntimes.iterator();

      while(var1.hasNext()) {
         RuntimeMBeanDelegate var2 = (RuntimeMBeanDelegate)var1.next();
         var2.unregister();
      }

      Iterator var8 = this.maxThreadsConstraintRuntimes.iterator();

      while(var8.hasNext()) {
         RuntimeMBeanDelegate var3 = (RuntimeMBeanDelegate)var8.next();
         var3.unregister();
      }

      Iterator var9 = this.minThreadsConstraintRuntimes.iterator();

      while(var9.hasNext()) {
         RuntimeMBeanDelegate var4 = (RuntimeMBeanDelegate)var9.next();
         var4.unregister();
      }

      Iterator var10 = this.requestClassRuntimes.iterator();

      while(var10.hasNext()) {
         RuntimeMBeanDelegate var5 = (RuntimeMBeanDelegate)var10.next();
         var5.unregister();
      }

      Iterator var11 = this.wseeRuntimes.iterator();

      while(var11.hasNext()) {
         RuntimeMBeanDelegate var6 = (RuntimeMBeanDelegate)var11.next();
         var6.unregister();
      }

      Iterator var12 = this.queryCacheRuntimes.keySet().iterator();

      while(var12.hasNext()) {
         RuntimeMBeanDelegate var7 = (RuntimeMBeanDelegate)((RuntimeMBeanDelegate)this.queryCacheRuntimes.get((String)((String)var12.next())));
         var7.unregister();
      }

      ServerRuntimeMBean var13 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
      ((RuntimeMBeanDelegate)var13).removeChild(this);
   }

   public ComponentRuntimeMBean[] lookupComponents() {
      return this.getComponentRuntimes();
   }

   private void addCRM(List var1, ComponentRuntimeMBean[] var2) {
      if (var2 != null && var2.length != 0) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3] != null) {
               var1.add(var2[var3]);
            }
         }

      }
   }

   public ComponentRuntimeMBean[] getComponentRuntimes() {
      Module[] var1 = this.getAppCtx().getApplicationModules();
      if (var1 == null) {
         return new ComponentRuntimeMBean[0];
      } else {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.addCRM(var2, var1[var3].getComponentRuntimeMBeans());
         }

         return (ComponentRuntimeMBean[])((ComponentRuntimeMBean[])var2.toArray(new ComponentRuntimeMBean[var2.size()]));
      }
   }

   private ApplicationContextInternal getAppCtx() {
      ApplicationAccess var1 = ApplicationAccess.getApplicationAccess();
      return var1.getApplicationContext(this.appId);
   }

   public boolean isEAR() {
      return this.getAppCtx().getApplicationDD() != null;
   }

   public void setApplicationCaches(ReInitializableCache[] var1) {
      this.applicationCaches = var1;
   }

   public boolean hasApplicationCache() {
      return this.applicationCaches != null && this.applicationCaches.length > 0;
   }

   public void reInitializeApplicationCachesAndPools() {
      if (this.applicationCaches != null) {
         for(int var1 = 0; var1 < this.applicationCaches.length; ++var1) {
            this.applicationCaches[var1].reInitializeCacheAndPools();
         }
      }

   }

   public HealthState getHealthState() {
      ArrayList var1 = new ArrayList();
      int var2 = 0;
      Iterator var3 = this.workManagerRuntimes.iterator();

      while(var3.hasNext()) {
         WorkManagerRuntimeMBean var4 = (WorkManagerRuntimeMBean)var3.next();
         int var5 = var4.getHealthState().getState();
         if (var5 != 0) {
            if (var5 > var2) {
               var2 = var5;
            }

            var1.addAll(Arrays.asList(var4.getHealthState().getReasonCode()));
         }
      }

      if (var1.size() == 0) {
         return new HealthState(var2);
      } else {
         String[] var6 = new String[var1.size()];
         var1.toArray(var6);
         return new HealthState(var2, var6);
      }
   }

   public KodoPersistenceUnitRuntimeMBean[] getKodoPersistenceUnitRuntimes() {
      KodoPersistenceUnitRuntimeMBean[] var1 = new KodoPersistenceUnitRuntimeMBean[this.m_runtimePersistenUnit.size()];
      var1 = (KodoPersistenceUnitRuntimeMBean[])((KodoPersistenceUnitRuntimeMBean[])this.m_runtimePersistenUnit.values().toArray(var1));
      return var1;
   }

   public KodoPersistenceUnitRuntimeMBean getKodoPersistenceUnitRuntime(String var1) {
      return (KodoPersistenceUnitRuntimeMBean)this.m_runtimePersistenUnit.get(var1);
   }

   public void addKodoPersistenceUnit(KodoPersistenceUnitRuntimeMBean var1) {
      this.m_runtimePersistenUnit.put(var1.getPersistenceUnitName(), var1);
   }

   public ClassRedefinitionRuntimeMBean getClassRedefinitionRuntime() {
      return this.classRedefRuntime;
   }

   public void setClassRedefinitionRuntime(ClassRedefinitionRuntimeMBean var1) {
      this.classRedefRuntime = var1;
   }

   public CoherenceClusterRuntimeMBean getCoherenceClusterRuntime() {
      return this.coherenceClusterRuntimeMBean;
   }

   public void setCoherenceClusterRuntime(CoherenceClusterRuntimeMBean var1) {
      this.coherenceClusterRuntimeMBean = var1;
   }
}
