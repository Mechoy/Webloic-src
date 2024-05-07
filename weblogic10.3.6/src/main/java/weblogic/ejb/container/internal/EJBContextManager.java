package weblogic.ejb.container.internal;

import javax.ejb.EJBContext;
import weblogic.ejb.container.interfaces.WLEnterpriseBean;
import weblogic.kernel.ThreadLocalStack;

public final class EJBContextManager {
   private static final ThreadLocalStack threadStorage = new ThreadLocalStack(true);

   public static void pushBean(Object var0) {
      threadStorage.push(var0);
   }

   public static void popBean() {
      threadStorage.pop();
   }

   public static void pushEjbContext(Object var0) {
      threadStorage.push(var0);
   }

   public static void popEjbContext() {
      threadStorage.pop();
   }

   private static Object peek() {
      return threadStorage.peek();
   }

   public static WLEnterpriseBean getBean() {
      return (WLEnterpriseBean)peek();
   }

   public static EJBContext getEJBContext() {
      return peek() instanceof EJBContext ? (EJBContext)threadStorage.peek() : ((WLEnterpriseBean)peek()).__WL_getEJBContext();
   }
}
