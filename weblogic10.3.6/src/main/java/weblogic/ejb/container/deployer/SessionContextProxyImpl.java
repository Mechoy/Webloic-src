package weblogic.ejb.container.deployer;

import java.security.Identity;
import java.security.Principal;
import java.util.Properties;
import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;
import weblogic.ejb.container.internal.EJBContextManager;

public final class SessionContextProxyImpl implements SessionContext {
   private SessionContext getDelegate() {
      return (SessionContext)EJBContextManager.getEJBContext();
   }

   public MessageContext getMessageContext() {
      return this.getDelegate().getMessageContext();
   }

   public UserTransaction getUserTransaction() throws IllegalStateException {
      return this.getDelegate().getUserTransaction();
   }

   public Identity getCallerIdentity() {
      return this.getDelegate().getCallerIdentity();
   }

   public Principal getCallerPrincipal() {
      return this.getDelegate().getCallerPrincipal();
   }

   public EJBHome getEJBHome() {
      return this.getDelegate().getEJBHome();
   }

   public EJBLocalHome getEJBLocalHome() {
      return this.getDelegate().getEJBLocalHome();
   }

   public EJBObject getEJBObject() {
      return this.getDelegate().getEJBObject();
   }

   public EJBLocalObject getEJBLocalObject() {
      return this.getDelegate().getEJBLocalObject();
   }

   public Properties getEnvironment() {
      return this.getDelegate().getEnvironment();
   }

   public boolean getRollbackOnly() throws IllegalStateException {
      return this.getDelegate().getRollbackOnly();
   }

   public void setRollbackOnly() throws IllegalStateException {
      this.getDelegate().setRollbackOnly();
   }

   public boolean isCallerInRole(String var1) {
      return this.getDelegate().isCallerInRole(var1);
   }

   public boolean isCallerInRole(Identity var1) {
      return this.getDelegate().isCallerInRole(var1);
   }

   public TimerService getTimerService() {
      return this.getDelegate().getTimerService();
   }

   public Object getBusinessObject(Class var1) {
      return this.getDelegate().getBusinessObject(var1);
   }

   public Class getInvokedBusinessInterface() {
      return this.getDelegate().getInvokedBusinessInterface();
   }

   public Object lookup(String var1) throws IllegalArgumentException {
      return this.getDelegate().lookup(var1);
   }
}
