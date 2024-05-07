package weblogic.ejb.container.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.security.jacc.PolicyConfiguration;
import weblogic.ejb.container.persistence.spi.Dependents;
import weblogic.ejb.container.persistence.spi.Relationships;
import weblogic.ejb.spi.EjbDescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageDestinationDescriptorBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.security.jacc.RoleMapper;

public interface DeploymentInfo extends weblogic.ejb.spi.DeploymentInfo {
   String getApplicationName();

   String getModuleURI();

   String getEJBComponentName();

   String getJACCPolicyContextId();

   PolicyConfiguration getJACCPolicyConfig();

   String getJACCCodeSource();

   RoleMapper getJACCRoleMapper();

   String getJarFileName();

   Map getApplicationExceptions();

   Set<Class<?>> getUncheckedAppExceptionClasses();

   String getClientJarFileName();

   boolean isDynamicQueriesEnabled();

   BeanInfo getBeanInfo(String var1);

   String getRunAsRoleAssignment(String var1);

   SecurityRoleMapping getDeploymentRoles();

   Relationships getRelationships();

   Dependents getDependents();

   Collection getMessageDestinationDescriptors();

   MessageDestinationDescriptorBean getMessageDestinationDescriptor(String var1);

   boolean isEnableBeanClassRedeploy();

   EjbDescriptorBean getEjbDescriptorBean();

   List getInterceptorBeans(String var1);

   PitchforkContext getPitchforkContext();
}
