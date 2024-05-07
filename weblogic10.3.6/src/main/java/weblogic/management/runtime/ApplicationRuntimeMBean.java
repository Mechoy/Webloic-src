package weblogic.management.runtime;

import com.bea.wls.redef.runtime.ClassRedefinitionRuntimeMBean;
import weblogic.health.HealthFeedback;
import weblogic.health.HealthState;
import weblogic.work.WorkManager;

public interface ApplicationRuntimeMBean extends RuntimeMBean, HealthFeedback {
   int UNPREPARED = 0;
   int PREPARED = 1;
   int ACTIVATED = 2;

   String getApplicationName();

   String getApplicationVersion();

   int getActiveVersionState();

   void setActiveVersionState(int var1);

   /** @deprecated */
   ComponentRuntimeMBean[] lookupComponents();

   ComponentRuntimeMBean[] getComponentRuntimes();

   LibraryRuntimeMBean[] getLibraryRuntimes();

   LibraryRuntimeMBean[] getOptionalPackageRuntimes();

   WorkManagerRuntimeMBean[] getWorkManagerRuntimes();

   WorkManagerRuntimeMBean lookupWorkManagerRuntime(String var1, String var2);

   WorkManagerRuntimeMBean lookupWorkManagerRuntime(WorkManager var1);

   /** @deprecated */
   WseeRuntimeMBean[] getWseeRuntimes();

   /** @deprecated */
   void addWseeRuntime(WseeRuntimeMBean var1);

   WseeV2RuntimeMBean[] getWseeV2Runtimes();

   WseeV2RuntimeMBean lookupWseeV2Runtime(String var1);

   MinThreadsConstraintRuntimeMBean lookupMinThreadsConstraintRuntime(String var1);

   RequestClassRuntimeMBean lookupRequestClassRuntime(String var1);

   MaxThreadsConstraintRuntimeMBean lookupMaxThreadsConstraintRuntime(String var1);

   MaxThreadsConstraintRuntimeMBean[] getMaxThreadsConstraintRuntimes();

   MinThreadsConstraintRuntimeMBean[] getMinThreadsConstraintRuntimes();

   RequestClassRuntimeMBean[] getRequestClassRuntimes();

   boolean isEAR();

   boolean hasApplicationCache();

   void reInitializeApplicationCachesAndPools();

   QueryCacheRuntimeMBean[] getQueryCacheRuntimes();

   QueryCacheRuntimeMBean lookupQueryCacheRuntime(String var1);

   KodoPersistenceUnitRuntimeMBean[] getKodoPersistenceUnitRuntimes();

   KodoPersistenceUnitRuntimeMBean getKodoPersistenceUnitRuntime(String var1);

   ClassRedefinitionRuntimeMBean getClassRedefinitionRuntime();

   HealthState getHealthState();

   CoherenceClusterRuntimeMBean getCoherenceClusterRuntime();

   void setCoherenceClusterRuntime(CoherenceClusterRuntimeMBean var1);
}
