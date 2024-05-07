package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import javax.ejb.EJBContext;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.naming.Context;
import javax.transaction.Transaction;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.management.runtime.EJBRuntimeMBean;

public interface BeanManager {
   void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4) throws WLDeploymentException;

   void setIsDeployed(boolean var1);

   boolean getIsDeployed();

   void undeploy();

   void remove();

   EJBContext allocateContext(EnterpriseBean var1, Object var2);

   EnterpriseBean preInvoke(InvocationWrapper var1) throws InternalException;

   void postInvoke(InvocationWrapper var1) throws InternalException;

   EnterpriseBean preHomeInvoke(InvocationWrapper var1) throws InternalException;

   void postHomeInvoke(InvocationWrapper var1) throws InternalException;

   void destroyInstance(InvocationWrapper var1, Throwable var2) throws InternalException;

   void destroyPooledInstance(InvocationWrapper var1, Throwable var2) throws InternalException;

   void beforeCompletion(InvocationWrapper var1) throws InternalException;

   void beforeCompletion(Object var1) throws InternalException;

   void beforeCompletion(Collection var1, Transaction var2) throws InternalException;

   void afterCompletion(InvocationWrapper var1);

   void afterCompletion(Object var1);

   void afterCompletion(Collection var1, Transaction var2, int var3, Object var4);

   EJBObject remoteCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException;

   EJBLocalObject localCreate(InvocationWrapper var1, Method var2, Method var3, Object[] var4) throws InternalException;

   void remove(InvocationWrapper var1) throws InternalException;

   EJBObject remoteFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException;

   Object localFindByPrimaryKey(InvocationWrapper var1, Object var2) throws InternalException;

   EJBObject remoteScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   EJBLocalObject localScalarFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   Enumeration enumFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   Collection collectionFinder(InvocationWrapper var1, Method var2, Object[] var3) throws InternalException;

   EJBRuntimeMBean getEJBRuntimeMBean();

   void beanImplClassChangeNotification();

   void releaseBean(InvocationWrapper var1);

   void handleUncommittedLocalTransaction(InvocationWrapper var1) throws InternalException;

   boolean usesBeanManagedTx();

   Context getEnvironmentContext();

   BeanInfo getBeanInfo();

   TimerManager getTimerManager();

   void reInitializePool();

   void reInitializeCacheAndPool();
}
