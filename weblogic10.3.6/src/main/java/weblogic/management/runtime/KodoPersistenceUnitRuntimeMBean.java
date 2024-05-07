package weblogic.management.runtime;

public interface KodoPersistenceUnitRuntimeMBean extends RuntimeMBean {
   KodoDataCacheRuntimeMBean[] getDataCacheRuntimes();

   KodoQueryCacheRuntimeMBean[] getQueryCacheRuntimes();

   KodoQueryCompilationCacheRuntimeMBean getQueryCompilationCacheRuntime();

   String getPersistenceUnitName();
}
