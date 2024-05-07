package weblogic.jms.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSException;
import weblogic.jms.common.JMSID;
import weblogic.rmi.extensions.AsyncResultListener;
import weblogic.utils.StackTraceUtilsClient;
import weblogic.work.WorkManager;

public abstract class Request extends weblogic.messaging.dispatcher.Request implements Runnable, AsyncResultListener, Externalizable {
   static final long serialVersionUID = -3580248041850964617L;

   public Request(JMSID var1, int var2) {
      super(var1, var2, VoidResponse.THE_ONE, InvocableManagerDelegate.delegate.getInvocableManager());
   }

   protected Throwable getAppException(String var1, Throwable var2) {
      return new JMSException(var1, var2);
   }

   public synchronized weblogic.messaging.dispatcher.Response getResult() throws javax.jms.JMSException {
      try {
         return super.getResult();
      } catch (Throwable var2) {
         return handleThrowable(var2);
      }
   }

   protected WorkManager getDefaultWorkManager() {
      return JMSDispatcherManager.getWorkManager();
   }

   public synchronized weblogic.messaging.dispatcher.Response useChildResult(Class var1) throws javax.jms.JMSException {
      weblogic.messaging.dispatcher.Response var2 = ((Request)super.getChild()).getResult();
      this.setResult(var2);
      this.setState(Integer.MAX_VALUE);
      return var2;
   }

   public static weblogic.messaging.dispatcher.Response handleThrowable(Throwable var0) throws javax.jms.JMSException {
      if (var0 instanceof RuntimeException) {
         throw (RuntimeException)var0;
      } else if (var0 instanceof Error) {
         throw (Error)var0;
      } else if (var0 instanceof javax.jms.JMSException) {
         throw (javax.jms.JMSException)StackTraceUtilsClient.getThrowableWithCause(var0);
      } else {
         throw new JMSException(var0.getMessage(), var0);
      }
   }

   public void dispatchAsync(JMSDispatcher var1, weblogic.messaging.dispatcher.Request var2) throws weblogic.messaging.dispatcher.DispatcherException {
      super.dispatchAsync(var1.getDelegate(), var2);
   }

   public void run() {
      try {
         JMSDispatcherManager.getLocalDispatcher().dispatchAsync(this);
      } catch (weblogic.messaging.dispatcher.DispatcherException var2) {
      }

   }

   public Request() {
      this.setAppInvocableManager(InvocableManagerDelegate.delegate.getInvocableManager());
      this.setAppVoidResponse(VoidResponse.THE_ONE);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1, new JMSID(), InvocableManagerDelegate.delegate.getInvocableManager());
   }
}
