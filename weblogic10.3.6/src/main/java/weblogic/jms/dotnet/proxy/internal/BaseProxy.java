package weblogic.jms.dotnet.proxy.internal;

import javax.jms.JMSException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;

public abstract class BaseProxy {
   protected static final int STARTED = 0;
   protected static final int CLOSED = 1;
   protected long serviceId;
   protected BaseProxy parent;
   protected int state;
   static final boolean debug = false;
   protected static final AuthenticatedSubject anonymous;

   BaseProxy(long var1, BaseProxy var3) {
      this.serviceId = var1;
      this.parent = var3;
   }

   long getServiceId() {
      return this.serviceId;
   }

   BaseProxy getParent() {
      return this.parent;
   }

   abstract InitialContextProxy getContext();

   abstract Transport getTransport();

   abstract AbstractSubject getSubject();

   protected void debug(String var1) {
      System.out.println("[" + this.getClass().getName() + "]: " + var1);
   }

   protected boolean isShutdown() {
      return ProxyManagerImpl.getProxyManager().isShutdown();
   }

   protected synchronized boolean isClosed() {
      return (this.state & 1) != 0;
   }

   protected synchronized void checkShutdownOrClosed(String var1) throws JMSException {
      if (this.isShutdown()) {
         throw new JMSException("The JMS servics is shutting down");
      } else if (this.isClosed()) {
         throw new JMSException(var1);
      }
   }

   abstract void remove(long var1);

   static {
      JMSSecurityHelper.getJMSSecurityHelper();
      anonymous = (AuthenticatedSubject)JMSSecurityHelper.getAnonymousSubject();
   }
}
