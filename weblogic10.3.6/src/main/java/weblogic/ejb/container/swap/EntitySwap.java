package weblogic.ejb.container.swap;

import javax.ejb.EnterpriseBean;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.manager.BaseEntityManager;

public final class EntitySwap implements EJBSwap {
   private BaseEntityManager beanManager;

   public void setup(BeanInfo var1, BeanManager var2, ClassLoader var3) {
      this.beanManager = (BaseEntityManager)var2;
   }

   public EnterpriseBean read(Object var1) throws InternalException {
      return this.beanManager.allocateBean();
   }

   public void write(Object var1, Object var2) {
   }

   public void remove(Object var1) {
   }

   public void cancelTrigger() {
   }

   public void updateClassLoader(ClassLoader var1) {
   }

   public void updateIdleTimeoutMS(long var1) {
   }
}
