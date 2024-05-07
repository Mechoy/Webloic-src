package weblogic.ejb.container.pool;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.EntityBean;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BaseEJBHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.internal.EntityEJBHome;
import weblogic.ejb.container.internal.EntityEJBLocalHome;
import weblogic.ejb.container.manager.BaseEntityManager;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.j2ee.MethodInvocationHelper;
import weblogic.management.runtime.EJBPoolRuntimeMBean;
import weblogic.utils.AssertionError;
import weblogic.utils.StackTraceUtils;

public final class EntityPool extends Pool {
   private static final Object DUMMY_PK = new Object();
   private final BaseEJBHomeIntf ejbHome;
   private final EntityEJBHome remoteHome;
   private final EntityEJBLocalHome localHome;
   private final BaseEntityManager beanManager;
   private EJBObject eo;
   private EJBLocalObject elo;

   public EntityPool(EntityEJBHome var1, EntityEJBLocalHome var2, BaseEntityManager var3, BeanInfo var4, EJBPoolRuntimeMBean var5) throws WLDeploymentException {
      super(var3, var4, var5);
      this.beanClass = ((EntityBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.remoteHome = var1;
      this.localHome = var2;
      this.beanManager = var3;
      if (null == var1) {
         this.ejbHome = var2;
      } else {
         this.ejbHome = var1;
      }

      if (null != var1) {
         this.eo = var1.allocateEO(DUMMY_PK);
      }

      if (null != var2) {
         this.elo = var2.allocateELO(DUMMY_PK);
      }

      if (debugLogger.isDebugEnabled()) {
         debug("created: '" + this + "'");
      }

   }

   public EnterpriseBean getBean() throws InternalException {
      EnterpriseBean var1 = super.getBean();
      if (null == var1) {
         var1 = this.createBean();
         if (debugLogger.isDebugEnabled()) {
            debug("Allocate new: '" + var1 + "'");
         }
      }

      this.getPoolRuntime().incrementBeansInUseCount();
      return var1;
   }

   public EnterpriseBean getBean(long var1) {
      throw new AssertionError("getBean() with timeout not supported for Entity beans");
   }

   protected void removeBean(EnterpriseBean var1) {
      try {
         ((EntityBean)var1).unsetEntityContext();
      } catch (Throwable var3) {
         EJBLogger.logExceptionDuringEJBUnsetEntityContext(this.beanInfo.getEJBName(), StackTraceUtils.throwable2StackTrace(var3));
      }

   }

   public EnterpriseBean createBean() throws InternalException {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      MethodInvocationHelper.pushMethodObject(this.beanInfo);
      this.setFile2();

      EntityBean var4;
      try {
         this.pushEnvironment();
         ClassLoader var3 = this.beanInfo.getModuleClassLoader();
         var1.setContextClassLoader(var3);
         var4 = (EntityBean)this.beanManager.createBean(this.eo, this.elo);
      } finally {
         this.popEnvironment();
         var1.setContextClassLoader(var2);
         this.resetFile2();
         MethodInvocationHelper.popMethodObject(this.beanInfo);
      }

      return var4;
   }

   public void reset() {
      this.beanClass = ((EntityBeanInfo)this.beanInfo).getGeneratedBeanClass();
      this.cleanup();
   }

   private static void debug(String var0) {
      debugLogger.debug("[EntityPool] " + var0);
   }
}
