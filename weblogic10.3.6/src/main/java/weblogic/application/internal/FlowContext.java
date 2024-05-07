package weblogic.application.internal;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.Context;
import javax.security.jacc.PolicyConfiguration;
import javax.xml.stream.XMLStreamException;
import weblogic.application.AppDeploymentExtension;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.ApplicationFileManager;
import weblogic.application.ApplicationLifecycleListener;
import weblogic.application.Module;
import weblogic.application.ModuleManager;
import weblogic.application.SplitDirectoryInfo;
import weblogic.application.UpdateListener;
import weblogic.application.internal.library.LibraryManagerAggregate;
import weblogic.application.io.Ear;
import weblogic.deploy.event.ApplicationVersionLifecycleListenerAdapter;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.security.acl.internal.AuthenticatedSubject;

public interface FlowContext extends ApplicationContextInternal, UpdateListener.Registration {
   boolean isAdminState();

   void setAdminState(boolean var1);

   void setSplitDir();

   boolean isSplitDir();

   AppDDHolder getProposedPartialRedeployDDs();

   void setProposedPartialRedeployDDs(AppDDHolder var1);

   void setAdditionalModuleUris(Map var1);

   boolean isRedeployOperation();

   boolean isStopOperation();

   int getDeploymentOperation();

   boolean isInternalApp();

   LibraryManagerAggregate getLibraryManagerAggregate();

   List getUpdateListeners();

   Context getRootContext();

   void setRootContext(Context var1);

   void setApplicationModules(Module[] var1);

   Module[] getStartingModules();

   void setStartingModules(Module[] var1);

   Module[] getStoppingModules();

   void setStoppingModules(Module[] var1);

   String[] getStoppedModules();

   void setStoppedModules(String[] var1);

   Map getCustomModuleFactories();

   ModuleManager getModuleManager();

   void setAppLevelRoleMappings(Map var1);

   void setDescriptorCacheDir(File var1);

   void setApplicationDescriptor(ApplicationDescriptor var1) throws IOException, XMLStreamException;

   PolicyConfiguration[] getJACCPolicyConfigurations();

   void setCustomModuleFactories(Map var1);

   void setEJBQueryCacheMap(Map var1);

   void setSplitDirectoryInfo(SplitDirectoryInfo var1);

   void setApplicationFileManager(ApplicationFileManager var1);

   void setRuntime(J2EEApplicationRuntimeMBeanImpl var1);

   void setApplicationSecurityRealmName(String var1);

   void setApplicationPaths(File[] var1);

   void setEar(Ear var1);

   ApplicationBean getApplicationDD();

   WeblogicExtensionBean getWLExtensionDD();

   void setEnvContext(Context var1);

   ApplicationLifecycleListener[] getApplicationListeners();

   void setApplicationListeners(ApplicationLifecycleListener[] var1);

   void setApplicationVersionListenerAdapter(ApplicationVersionLifecycleListenerAdapter var1);

   void setPartialRedeployURIs(String[] var1);

   String[] getPartialRedeployURIs();

   void setAppListenerIdentityMappings(Map var1);

   AuthenticatedSubject getAppListenerIdentity(ApplicationLifecycleListener var1);

   void addAppDeploymentExtension(AppDeploymentExtension var1, ExtensionType var2);

   Set<AppDeploymentExtension> getAppDeploymentExtensions(ExtensionType var1);

   void clearAppDeploymentExtensions();

   public static enum ExtensionType {
      PRE,
      POST;
   }
}
