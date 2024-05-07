package weblogic.jms.dispatcher;

import javax.jms.JMSException;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.DispatcherPeerGoneListener;
import weblogic.utils.StackTraceUtilsClient;

public final class DispatcherAdapter implements JMSDispatcher {
   private final Dispatcher delegate;

   DispatcherAdapter(Dispatcher var1) {
      this.delegate = var1;
   }

   public void dispatchNoReply(weblogic.messaging.dispatcher.Request var1) throws JMSException {
      try {
         this.delegate.dispatchNoReply(var1);
      } catch (weblogic.messaging.dispatcher.DispatcherException var3) {
         throw convertToJMSExceptionAndThrow(var3);
      }
   }

   public void dispatchNoReplyWithId(weblogic.messaging.dispatcher.Request var1, int var2) throws JMSException {
      try {
         this.delegate.dispatchNoReplyWithId(var1, var2);
      } catch (weblogic.messaging.dispatcher.DispatcherException var4) {
         throw convertToJMSExceptionAndThrow(var4);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSync(weblogic.messaging.dispatcher.Request var1) throws JMSException {
      try {
         return this.delegate.dispatchSync(var1);
      } catch (weblogic.messaging.dispatcher.DispatcherException var3) {
         throw convertToJMSExceptionAndThrow(var3);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncTran(weblogic.messaging.dispatcher.Request var1) throws JMSException {
      try {
         return this.delegate.dispatchSyncTran(var1);
      } catch (weblogic.messaging.dispatcher.DispatcherException var3) {
         throw convertToJMSExceptionAndThrow(var3);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncNoTran(weblogic.messaging.dispatcher.Request var1) throws JMSException {
      try {
         return this.delegate.dispatchSyncNoTran(var1);
      } catch (weblogic.messaging.dispatcher.DispatcherException var3) {
         throw convertToJMSExceptionAndThrow(var3);
      }
   }

   public weblogic.messaging.dispatcher.Response dispatchSyncNoTranWithId(weblogic.messaging.dispatcher.Request var1, int var2) throws JMSException {
      try {
         return this.delegate.dispatchSyncNoTranWithId(var1, var2);
      } catch (weblogic.messaging.dispatcher.DispatcherException var4) {
         throw convertToJMSExceptionAndThrow(var4);
      }
   }

   public DispatcherId getId() {
      return this.delegate.getId();
   }

   public boolean isLocal() {
      return this.delegate.isLocal();
   }

   public void dispatchAsync(weblogic.messaging.dispatcher.Request var1) throws weblogic.messaging.dispatcher.DispatcherException {
      this.delegate.dispatchAsync(var1);
   }

   public DispatcherPeerGoneListener addDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
      return this.delegate.addDispatcherPeerGoneListener(var1);
   }

   public void removeDispatcherPeerGoneListener(DispatcherPeerGoneListener var1) {
      this.delegate.removeDispatcherPeerGoneListener(var1);
   }

   public Dispatcher getDelegate() {
      return this.delegate;
   }

   private static JMSException convertToJMSExceptionAndThrow(weblogic.messaging.dispatcher.DispatcherException var0) throws JMSException {
      Throwable var1;
      for(var1 = var0.getCause(); var1 instanceof weblogic.messaging.dispatcher.DispatcherException; var1 = var1.getCause()) {
      }

      if (var1 instanceof JMSException) {
         return (JMSException)StackTraceUtilsClient.getThrowableWithCause(var1);
      } else if (var1 instanceof RuntimeException) {
         throw (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      } else {
         throw new weblogic.jms.common.JMSException(var0);
      }
   }
}
