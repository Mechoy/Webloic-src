package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.naming.Context;
import javax.naming.Name;
import javax.security.jacc.PolicyConfiguration;
import weblogic.application.ApplicationContextInternal;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.RuntimeHelper;
import weblogic.ejb.spi.EJBValidationInfo;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;
import weblogic.j2ee.descriptor.PersistenceUnitRefBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.RoleMapper;

public interface BeanInfo extends weblogic.ejb.spi.BeanInfo, BeanUpdateListener, EJBValidationInfo {
   DeploymentInfo getDeploymentInfo();

   int getTransactionTimeoutMS();

   String getComponentURI();

   String getComponentName();

   Name getJNDIName();

   String getJNDINameAsString();

   String getDispatchPolicy();

   boolean getStickToFirstServer();

   int getRemoteClientTimeout();

   String getIsIdenticalKey();

   boolean getIsResourceRef();

   String getDisplayName();

   String getBeanClassName();

   Collection getAllEnvironmentEntries();

   Collection getAllEJBReferences();

   Collection getAllEJBLocalReferences();

   Collection getAllServiceReferences();

   Collection getAllServiceReferenceDescriptions();

   Iterator getAllMethodInfosIterator();

   Collection getAllBeanMethodInfos();

   MethodInfo getBeanMethodInfo(String var1);

   weblogic.ejb.container.internal.MethodDescriptor getEjbTimeoutMethodDescriptor();

   Map getAllEJBReferenceJNDINames();

   Map getAllEJBLocalReferenceJNDINames();

   Collection getAllResourceReferences();

   Map getAllResourceReferenceJNDINames(String var1);

   Collection getAllResourceEnvReferences();

   Collection getAllWlResourceReferences();

   Collection getAllWlResourceEnvReferences();

   Collection getAllMessageDestinationReferences();

   Map getAllResourceEnvReferenceJNDINames(String var1);

   Collection getAllSecurityRoleReferences();

   SecurityRoleReference getSecurityRoleReference(String var1);

   PersistenceContextRefBean[] getPersistenceContextRefs();

   PersistenceUnitRefBean[] getPersistenceUnitRefs();

   ClassLoader getClassLoader();

   ClassLoader getModuleClassLoader();

   boolean useCallByReference();

   String getNetworkAccessPoint();

   boolean getClientsOnSameServer();

   CachingDescriptor getCachingDescriptor();

   IIOPSecurityDescriptor getIIOPSecurityDescriptor();

   boolean isWarningDisabled(String var1);

   String getRunAsPrincipalName();

   AuthenticatedSubject getRunAsSubject() throws Exception;

   String getCreateAsPrincipalName();

   String getRemoveAsPrincipalName();

   String getPassivateAsPrincipalName();

   void setRuntimeHelper(RuntimeHelper var1);

   String getJACCPolicyContextId();

   PolicyConfiguration getJACCPolicyConfig();

   String getJACCCodeSource();

   RoleMapper getJACCRoleMapper();

   void assignDefaultTXAttributesIfNecessary();

   boolean usesBeanManagedTx();

   void setEjbComponentCreator(EjbComponentCreator var1);

   EjbComponentCreator getEjbComponentCreator();

   boolean isEJB30();

   boolean isTimerDriven();

   boolean isClusteredTimers();

   Method getTimeoutMethod();

   String getTimerStoreName();

   BeanManager getBeanManager();

   void setupBeanManager(EJBComponentRuntimeMBeanImpl var1);

   void init() throws ClassNotFoundException, WLDeploymentException;

   void prepare(ApplicationContextInternal var1, DeploymentInfo var2) throws WLDeploymentException;

   void activate(Context var1, Map var2, Map var3, DeploymentInfo var4, Context var5) throws WLDeploymentException;

   void onUndeploy();

   void updateImplClassLoader() throws WLDeploymentException;

   void updateTransactionTimeoutSeconds(int var1);

   void unprepare();
}
