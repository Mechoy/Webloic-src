package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import weblogic.deployment.PersistenceUnitRegistry;
import weblogic.j2ee.descriptor.PersistenceContextRefBean;

public interface Ejb3SessionBeanInfo extends SessionBeanInfo {
   boolean hasBusinessLocals();

   boolean hasBusinessRemotes();

   String[] getBusinessLocalNames();

   String[] getBusinessRemoteNames();

   Map getRemoteBusinessJNDINames();

   Set getBusinessLocals();

   Set getBusinessRemotes();

   Class getGeneratedLocalBusinessImplClass(Class var1);

   Class getGeneratedRemoteBusinessImplClass(Class var1);

   Class getGeneratedRemoteBusinessIntfClass(Class var1);

   String getEjbCreateInitMethodName(Method var1);

   boolean isRemoveMethod(Method var1);

   boolean isRetainifException(Method var1);

   boolean containsExtendedPersistenceContextRefs();

   PersistenceContextRefBean[] getExtendedPersistenceContextRefs();

   void setPersistenceUnitRegistry(PersistenceUnitRegistry var1);
}
