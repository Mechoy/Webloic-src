package weblogic.ejb.container.swap;

import javax.ejb.EnterpriseBean;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.BeanManager;

public interface EJBSwap {
   String SWAP_VERBOSE_PROP = "weblogic.ejb.swap.verbose";
   String SWAP_DEBUG_PROP = "weblogic.ejb.swap.debug";

   void setup(BeanInfo var1, BeanManager var2, ClassLoader var3);

   EnterpriseBean read(Object var1) throws InternalException;

   void write(Object var1, Object var2) throws InternalException;

   void remove(Object var1);

   void cancelTrigger();

   void updateClassLoader(ClassLoader var1);

   void updateIdleTimeoutMS(long var1);
}
