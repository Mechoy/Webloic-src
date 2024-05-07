package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.ejb.container.dd.ClusteringDescriptor;

public interface ClientDrivenBeanInfo extends BeanInfo {
   Set JAVA_CLASS_METHOD_EXCLUDES = new HashSet(Arrays.asList((Object[])(new String[]{"hashCode", "getClass", "wait", "equals", "notify", "notifyAll", "toString", "setEntityContext", "unsetEntityContext", "ejbActivate", "ejbPassivate", "ejbLoad", "ejbStore", "ejbCreate", "ejbPostCreate", "ejbRemove"})));

   Name getLocalJNDIName();

   String getLocalJNDINameAsString();

   String getGeneratedBeanClassName();

   Class getGeneratedBeanClass();

   String getGeneratedBeanInterfaceName();

   Class getGeneratedBeanInterface();

   Class getHomeClass();

   Class getLocalHomeClass();

   Class getWebserviceObjectClass();

   String getHomeInterfaceName();

   String getLocalHomeInterfaceName();

   Class getHomeInterfaceClass();

   Class getLocalHomeInterfaceClass();

   Class getRemoteClass();

   Class getLocalClass();

   String getRemoteInterfaceName();

   String getLocalInterfaceName();

   String getServiceEndpointName();

   Class getRemoteInterfaceClass();

   Class getLocalInterfaceClass();

   Class getServiceEndpointClass();

   MethodInfo getRemoteMethodInfo(String var1);

   MethodInfo getRemoteMethodInfo(String var1, String[] var2);

   MethodInfo getRemoteMethodInfo(Method var1);

   MethodInfo getHomeMethodInfo(String var1);

   MethodInfo getHomeMethodInfo(String var1, String[] var2);

   MethodInfo getHomeMethodInfo(Method var1);

   Collection getAllRemoteMethodInfos();

   Collection getAllHomeMethodInfos();

   Collection getAllLocalMethodInfos();

   Collection getAllLocalHomeMethodInfos();

   Collection getAllWebserviceMethodInfos();

   ClusteringDescriptor getClusteringDescriptor();

   BaseEJBRemoteHomeIntf getRemoteHome();

   BaseEJBLocalHomeIntf getLocalHome();

   boolean hasDeclaredRemoteHome();

   boolean hasDeclaredLocalHome();

   boolean hasRemoteClientView();

   boolean hasLocalClientView();

   boolean hasWebserviceClientView();

   void bindEJBRefs(Context var1) throws NamingException;

   void unbindEJBRefs(Context var1) throws NamingException;

   PersistenceUnitRegistry getPersistenceUnitRegistry();

   void setPersistenceUnitRegistry(PersistenceUnitRegistry var1);
}
