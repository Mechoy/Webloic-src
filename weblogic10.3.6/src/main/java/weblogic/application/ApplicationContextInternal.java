package weblogic.application;

import java.io.File;
import java.util.Map;
import weblogic.application.io.Ear;
import weblogic.application.library.LibraryProvider;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.work.WorkManagerCollection;

public interface ApplicationContextInternal extends ApplicationContext, DescriptorUpdater {
   BasicDeploymentMBean getBasicDeploymentMBean();

   SystemResourceMBean getSystemResourceMBean();

   DomainMBean getProposedDomain();

   boolean requiresRestart();

   AuthenticatedSubject getDeploymentInitiator();

   WorkManagerCollection getWorkManagerCollection();

   boolean isEar();

   Ear getEar();

   ApplicationBean getApplicationDD();

   WeblogicApplicationBean getWLApplicationDD();

   File[] getApplicationPaths();

   String getApplicationFileName();

   String getStagingPath();

   String getOutputPath();

   J2EEApplicationRuntimeMBeanImpl getRuntime();

   ApplicationFileManager getApplicationFileManager();

   SplitDirectoryInfo getSplitDirectoryInfo();

   Map getEJBCacheMap();

   void setEJBCacheMap(Map var1);

   Map getEJBQueryCacheMap();

   Map getApplicationParameters();

   void setApplicationParameters(Map var1);

   Module[] getApplicationModules();

   boolean useJACC();

   LibraryProvider getLibraryProvider(String var1);

   DeploymentPlanBean findDeploymentPlan();

   SecurityRole getSecurityRole(String var1);

   File getDescriptorCacheDir();

   boolean isStaticDeploymentOperation();

   void addApplicationListener(ApplicationLifecycleListener var1);

   Object getSchemaTypeLoader(ClassLoader var1);

   void setSchemaTypeLoader(ClassLoader var1, Object var2);

   void clear();

   SubDeploymentMBean[] getLibrarySubDeployments();

   Object putUserObject(Object var1, Object var2);

   Object getUserObject(Object var1);

   Object removeUserObject(Object var1);

   void setProposedPersistenceUnitRegistry(PersistenceUnitRegistry var1);

   PersistenceUnitRegistry getProposedPersistenceUnitRegistry();

   AppDeploymentExtension getAppDeploymentExtension(String var1);

   Map<String, String> getModuleURItoIdMap();

   void setModuleURItoIdMap(Map<String, String> var1);
}
