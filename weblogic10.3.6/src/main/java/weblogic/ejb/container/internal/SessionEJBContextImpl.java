package weblogic.ejb.container.internal;

import java.rmi.RemoteException;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.EnterpriseBean;
import javax.ejb.SessionContext;
import javax.xml.rpc.handler.MessageContext;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.Ejb3LocalHome;
import weblogic.ejb.container.interfaces.Ejb3RemoteHome;
import weblogic.ejb.container.interfaces.SessionBeanInfo;
import weblogic.ejb.container.interfaces.WLSessionEJBContext;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;

public final class SessionEJBContextImpl extends BaseEJBContext implements SessionContext, WLSessionEJBContext {
   private MessageContext messageContext;
   private Class businessInterfaceClass = null;
   private Object primaryKey = null;

   public SessionEJBContextImpl(EnterpriseBean var1, BeanManager var2, BaseEJBHome var3, BaseEJBLocalHome var4, EJBObject var5, EJBLocalObject var6) {
      super(var1, var2, var3, var4, var5, var6);
   }

   public void setBusinessInterfaceClass(Class var1) {
      this.businessInterfaceClass = var1;
   }

   public void setPrimaryKey(Object var1) {
      this.primaryKey = var1;
   }

   public MessageContext getMessageContext() {
      this.checkAllowedToGetMessageContext();
      return this.messageContext;
   }

   public void setMessageContext(MessageContext var1) {
      this.messageContext = var1;
   }

   public EJBHome getEJBHome() throws IllegalStateException {
      if (this.ejbObject == null) {
         Loggable var1 = EJBLogger.logEjbBeanWithoutHomeInterfaceLoggable(this.ejbHome.getDisplayName(), "");
         throw new IllegalStateException(var1.getMessage());
      } else {
         return super.getEJBHome();
      }
   }

   public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
      if (this.ejbLocalObject == null) {
         Loggable var1 = EJBLogger.logEjbBeanWithoutHomeInterfaceLoggable(this.ejbHome.getDisplayName(), "local");
         throw new IllegalStateException(var1.getMessage());
      } else {
         return super.getEJBLocalHome();
      }
   }

   private boolean needConsiderReplicationService() throws IllegalStateException {
      if (this.remoteHome instanceof Ejb3RemoteHome) {
         try {
            return ((Ejb3RemoteHome)this.remoteHome).needToConsiderReplicationService();
         } catch (RemoteException var2) {
            throw new IllegalStateException(var2);
         }
      } else {
         return false;
      }
   }

   public EJBObject getEJBObject() throws IllegalStateException {
      if (this.needConsiderReplicationService()) {
         try {
            return (EJBObject)((Ejb3RemoteHome)this.remoteHome).getComponentImpl(this.primaryKey);
         } catch (RemoteException var2) {
            throw new IllegalStateException(var2);
         }
      } else if (this.ejbObject == null) {
         Loggable var1 = EJBLogger.logEjbBeanWithoutHomeInterfaceLoggable(this.ejbHome.getDisplayName(), "");
         throw new IllegalStateException(var1.getMessage());
      } else {
         return super.getEJBObject();
      }
   }

   public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
      if (this.ejbLocalObject == null) {
         Loggable var1 = EJBLogger.logEjbBeanWithoutHomeInterfaceLoggable(this.ejbHome.getDisplayName(), "local");
         throw new IllegalStateException(var1.getMessage());
      } else {
         return super.getEJBLocalObject();
      }
   }

   public Object getBusinessObject(Class var1) throws IllegalStateException {
      if (!(this.remoteHome instanceof Ejb3RemoteHome) && !(this.localHome instanceof Ejb3LocalHome)) {
         Loggable var6 = EJBLogger.logBeanIsNotEJB3BeanLoggable(this.ejbHome.getDisplayName());
         throw new IllegalStateException(var6.getMessage());
      } else {
         Object var2 = null;
         if (this.remoteHome instanceof Ejb3RemoteHome) {
            try {
               var2 = ((Ejb3RemoteHome)this.remoteHome).getBusinessImpl(this.primaryKey, var1);
               if (var2 != null) {
                  return var2;
               }
            } catch (RemoteException var5) {
               throw new AssertionError("A remote exception is gotten when trying to get the business object for " + var1);
            }
         }

         if (this.localHome instanceof Ejb3LocalHome) {
            var2 = ((Ejb3LocalHome)this.localHome).getBusinessImpl(this.primaryKey, var1);
            if (var2 != null) {
               return var2;
            }
         }

         if (var2 == null) {
            String var3 = var1 == null ? "null" : var1.toString();
            Loggable var4 = EJBLogger.logEjbNoImplementBusinessInterfaceLoggable(this.ejbHome.getDisplayName(), var3);
            throw new IllegalStateException(var4.getMessage());
         } else {
            return var2;
         }
      }
   }

   public Class getInvokedBusinessInterface() throws IllegalStateException {
      if (this.businessInterfaceClass == null) {
         Loggable var1 = EJBLogger.logBeanNotInvokedThroughBusinessInterfaceLoggable();
         throw new IllegalStateException(var1.getMessage());
      } else {
         return this.businessInterfaceClass;
      }
   }

   protected void checkAllowedToGetTimerService() {
      SessionBeanInfo var1 = (SessionBeanInfo)this.beanManager.getBeanInfo();
      if (var1.isStateful()) {
         Loggable var2 = EJBLogger.logStatefulSessionBeanAttemptToAccessTimerServiceLoggable();
         throw new IllegalStateException(var2.getMessage());
      } else {
         super.checkAllowedToGetTimerService();
      }
   }

   public void checkAllowedToGetMessageContext() {
      SessionBeanInfo var1 = (SessionBeanInfo)this.beanManager.getBeanInfo();
      if (var1.isStateful()) {
         Loggable var2 = EJBLogger.logIllegalCallEJBContextMethodLoggable();
         throw new IllegalStateException(var2.getMessage());
      } else {
         super.checkAllowedToGetMessageContext();
      }
   }
}
