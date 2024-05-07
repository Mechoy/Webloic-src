package weblogic.ejb.container.internal;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.MessageDrivenContext;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.logging.Loggable;

public final class MessageDrivenEJBContextImpl extends BaseEJBContext implements MessageDrivenContext {
   public MessageDrivenEJBContextImpl(BeanManager var1) {
      super((EnterpriseBean)null, var1, (BaseEJBHome)null, (BaseEJBLocalHome)null, (EJBObject)null, (EJBLocalObject)null);
   }

   public boolean isCallerInRole(String var1) {
      Loggable var2 = EJBLogger.logmdbCannotInvokeThisMethodLoggable("isCallerInRole()");
      throw new IllegalStateException(var2.getMessage());
   }

   public EJBHome getEJBHome() {
      Loggable var1 = EJBLogger.logmdbCannotInvokeThisMethodLoggable("getEJBHome()");
      throw new IllegalStateException(var1.getMessage());
   }

   public EJBLocalHome getEJBLocalHome() {
      Loggable var1 = EJBLogger.logmdbCannotInvokeThisMethodLoggable("getEJBLocalHome()");
      throw new IllegalStateException(var1.getMessage());
   }

   public Object getPrimaryKey() {
      Loggable var1 = EJBLogger.logmdbCannotInvokeThisMethodLoggable("getPrimaryKey()");
      throw new IllegalStateException(var1.getMessage());
   }
}
