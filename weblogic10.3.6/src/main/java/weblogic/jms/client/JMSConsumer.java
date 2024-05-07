package weblogic.jms.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedExceptionAction;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.InvalidSelectorException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.extensions.ConsumerClosedException;
import weblogic.jms.frontend.FEConsumerSetListenerRequest;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.CompletionListener;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;
import weblogic.security.subject.AbstractSubject;
import weblogic.transaction.TransactionHelper;
import weblogic.utils.expressions.ExpressionEvaluator;
import weblogic.utils.expressions.ExpressionParserException;

public final class JMSConsumer implements ConsumerInternal, Reconnectable, Cloneable, Invocable {
   private static final String EXPRESSION_PARSER_CLASS = "weblogic.utils.expressions.ExpressionParser";
   private volatile JMSID consumerId;
   private volatile boolean closeInProgress;
   private long expectedSequenceNumber;
   private final JMSSession session;
   private final DestinationImpl destination;
   private final String selector;
   private final String name;
   private final boolean noLocal;
   private ExpressionEvaluator expressionEvaluator;
   private int windowMaximum;
   private int windowCurrent;
   private int windowThreshold;
   private String runtimeMBeanName;
   private JMSMessageContext messageListenerContext;
   private boolean debugHybridConsumer = false;
   private boolean isClosed;
   private WLConsumerImpl wlConsumerImpl;
   private JMSConsumer replacementConsumer;
   private final byte destinationFlags;
   private ConsumerReconnectInfo consumerReconnectInfo;

   JMSConsumer(JMSSession var1, String var2, DestinationImpl var3, String var4, boolean var5, int var6, byte var7) throws JMSException {
      this.name = var2;
      this.session = var1;
      this.destination = var3;
      this.selector = var4;
      this.noLocal = var5;
      if (var1.getAcknowledgeMode() != 128) {
         this.windowMaximum = var6;
         this.windowCurrent = var6;
         this.windowThreshold = var6 + 1 >> 1;
      } else if (var4 != null && var4.trim().length() > 0) {
         this.expressionEvaluator = createExpressionEvaluator(var4);
      }

      this.destinationFlags = var7;
   }

   public Object clone() throws CloneNotSupportedException {
      JMSConsumer var1 = (JMSConsumer)super.clone();
      return var1;
   }

   WLConsumerImpl getWLConsumerImpl() {
      return this.wlConsumerImpl;
   }

   public ReconnectController getReconnectController() {
      return this.wlConsumerImpl;
   }

   public Reconnectable getReconnectState(int var1) throws CloneNotSupportedException {
      JMSConsumer var2 = (JMSConsumer)this.clone();
      var2.replacementConsumer = this;
      this.closeInProgress = true;
      return var2;
   }

   public Reconnectable preCreateReplacement(Reconnectable var1) throws JMSException {
      ConsumerReconnectInfo var2 = this.consumerReconnectInfo.getClone();
      var2.setLastExposedMsgId(this.session.getLastExposedMsgId());
      var2.setLastAckMsgId(this.session.getLastAckMsgId());
      if (this.isDurable()) {
         var2.setServerDestId((JMSID)null);
      }

      JMSConsumer var3 = ((JMSSession)var1).setupConsumer(this.destination, this.selector, this.noLocal, this.name, this.destinationFlags, var2);
      var3.windowCurrent = this.windowCurrent;
      MessageListener var4 = null;
      if (this.messageListenerContext != null) {
         var4 = this.messageListenerContext.getMessageListener();
      }

      if (var4 != null && !(var4 instanceof JMSSystemMessageListener)) {
         var3.setMessageListener(var4);
      }

      ((JMSSession)var1).mapReplacementConsumer(this.replacementConsumer, var3);
      this.replacementConsumer = var3;
      return var3;
   }

   public void postCreateReplacement() {
      this.replacementConsumer.setWlConsumerImpl(this.wlConsumerImpl);
      this.wlConsumerImpl.setPhysicalReconnectable(this.replacementConsumer);
   }

   public void forgetReconnectState() {
      this.replacementConsumer = null;
   }

   public PeerInfo getFEPeerInfo() {
      return this.session.getFEPeerInfo();
   }

   public boolean isReconnectControllerClosed() {
      return this.wlConsumerImpl == null || this.wlConsumerImpl.isClosed();
   }

   public final boolean isClosed() {
      return this.isClosed || this.closeInProgress;
   }

   public String getWLSServerName() {
      return this.session.getConnection().getWLSServerName();
   }

   public ClientRuntimeInfo getParentInfo() {
      return this.session;
   }

   public String getRuntimeMBeanName() {
      return this.runtimeMBeanName;
   }

   final void setWlConsumerImpl(WLConsumerImpl var1) {
      this.wlConsumerImpl = var1;
   }

   public void setConsumerReconnectInfo(ConsumerReconnectInfo var1) {
      this.consumerReconnectInfo = var1;
   }

   boolean hasTemporaryDestination() {
      DestinationImpl var1 = this.destination;
      return var1 != null && (var1.getType() == 4 || var1.getType() == 8);
   }

   public final JMSSession getSession() {
      return this.session;
   }

   public final void setRuntimeMBeanName(String var1) {
      this.runtimeMBeanName = var1;
   }

   public final String toString() {
      return this.session.getConnection().getRuntimeMBeanName() + "." + this.session.getRuntimeMBeanName() + "." + this.getRuntimeMBeanName();
   }

   private void incrementWindowCurrent(int var1, boolean var2) throws JMSException {
      this.session.consumerIncrementWindowCurrent(this.consumerId, var1, var2);
      this.windowCurrent += var1;
      if (this.windowCurrent > this.windowMaximum) {
         this.windowCurrent = this.windowMaximum;
      }

   }

   public final void decrementWindowCurrent(boolean var1) throws JMSException {
      if (--this.windowCurrent < this.windowThreshold) {
         this.incrementWindowCurrent(this.windowMaximum - this.windowCurrent, var1);
      }

   }

   public final void setWindowCurrent(int var1) {
      this.windowCurrent = var1;
   }

   public final int getWindowCurrent() {
      return this.windowCurrent;
   }

   public final int getWindowMaximum() {
      return this.windowMaximum;
   }

   public final ExpressionEvaluator getExpressionEvaluator() {
      return this.expressionEvaluator;
   }

   public final void setClosed(boolean var1) {
      this.isClosed = var1;
   }

   public final void setId(JMSID var1) {
      this.consumerId = var1;
   }

   public final JMSID getJMSID() {
      return this.consumerId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public final InvocableMonitor getInvocableMonitor() {
      return null;
   }

   public final boolean isDurable() {
      return this.name != null;
   }

   public final synchronized long getExpectedSequenceNumber() {
      return this.expectedSequenceNumber;
   }

   public final synchronized void setExpectedSequenceNumber(long var1) {
      this.setExpectedSequenceNumber(var1, false);
   }

   public final synchronized void setExpectedSequenceNumber(long var1, boolean var3) {
      if (var3 || var1 > this.expectedSequenceNumber) {
         this.expectedSequenceNumber = var1;
      }

   }

   public final Destination getDestination() {
      return this.destination;
   }

   public final String getMessageSelector() throws JMSException {
      this.checkClosed();
      return this.selector;
   }

   public final MessageListener getMessageListener() throws JMSException {
      this.checkClosed();
      return this.messageListenerContext != null ? this.messageListenerContext.getMessageListener() : null;
   }

   public final JMSMessageContext getMessageListenerContext() {
      return this.messageListenerContext;
   }

   public final void setMessageListener(MessageListener var1) throws JMSException {
      this.setMessageListener(var1, -1L);
   }

   final void setMessageListener(final MessageListener var1, final long var2) throws JMSException {
      if (!KernelStatus.isServer()) {
         this.setMessageListenerInternal(var1, var2);
      } else {
         AbstractSubject var6 = CrossDomainSecurityManager.getCrossDomainSecurityUtil().getRemoteSubject(this.getSession().getConnection().getFrontEndDispatcher(), CrossDomainSecurityManager.getCurrentSubject(), true);
         if (JMSDebug.JMSCrossDomainSecurity.isDebugEnabled()) {
            JMSDebug.JMSCrossDomainSecurity.debug("setMessageListener:   subject to use = " + var6);
         }

         CrossDomainSecurityManager.doAs(var6, new PrivilegedExceptionAction() {
            public Object run() throws JMSException {
               JMSConsumer.this.setMessageListenerInternal(var1, var2);
               return null;
            }
         });
      }
   }

   private final void setMessageListenerInternal(MessageListener var1, long var2) throws JMSException {
      synchronized(this.session) {
         synchronized(this) {
            this.checkClosed();
            if (var2 != -1L) {
               this.session.setRealLastSequenceNumber(var2);
            }

            if (this.session.getMessageListener() != null) {
               throw new IllegalStateException(JMSClientExceptionLogger.logMessageListenerExistsLoggable());
            }

            if (var1 instanceof JMSSystemMessageListenerImpl2) {
               this.session.markAsSystemMessageListener(true);
            } else {
               this.session.markAsSystemMessageListener(false);
            }

            try {
               if (this.getMessageListener() == null && var1 != null) {
                  this.session.incrementConsumerListenerCount();
               } else {
                  if (this.getMessageListener() == null || var1 != null) {
                     return;
                  }

                  this.session.decrementConsumerListenerCount();
               }

               try {
                  Response var6 = this.session.getConnection().getFrontEndDispatcher().dispatchSync(new FEConsumerSetListenerRequest(this.consumerId, var1 != null, this.session.getLastSequenceNumber()));
               } catch (JMSException var15) {
                  var1 = null;
                  throw var15;
               }
            } finally {
               this.messageListenerContext = new JMSMessageContext(var1);
            }
         }

      }
   }

   public final Message receive() throws JMSException {
      return this.receiveInternal(Long.MAX_VALUE, (CompletionListener)null);
   }

   public final Message receiveNoWait() throws JMSException {
      return this.receiveInternal(9223372036854775806L, (CompletionListener)null);
   }

   public final Message receive(long var1) throws JMSException {
      return this.receiveInternal(var1, (CompletionListener)null);
   }

   Message receiveInternal(long var1, CompletionListener var3) throws JMSException {
      Object var4 = null;
      synchronized(this.session) {
         synchronized(this) {
            this.checkClosed();
            if (this.session.getAcknowledgeMode() == 128) {
               throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSynchronousMulticastReceiveLoggable());
            }

            if (var1 == 0L) {
               var1 = Long.MAX_VALUE;
            } else if (var1 < 0L) {
               throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInvalidTimeoutLoggable(var1));
            }

            try {
               var4 = (JMSSystemMessageListener)this.getMessageListener();
            } catch (ClassCastException var17) {
               throw new IllegalStateException(JMSClientExceptionLogger.logListenerExistsLoggable());
            }

            if (var4 == null) {
               if (!this.session.prefetchDisabled()) {
                  int var7 = this.session.getConnection().getSynchronousPrefetchMode();
                  if (this.destination.isTopic() && var7 > 0 || this.destination.isQueue() && var7 == 1 || System.getProperty("weblogic.jms.MessagePrefetch2") != null) {
                     if (this.session.prefetchStarted()) {
                        throw new UnsupportedOperationException(JMSClientExceptionLogger.logMultiplePrefetchConsumerPerSessionLoggable().getMessage());
                     }

                     if (!this.session.isTransacted() && this.session.userTransactionsEnabled() && TransactionHelper.getTransactionHelper().getTransaction() != null) {
                        this.session.disablePrefetch();
                     }

                     if (this.session.consumersCount() > 1) {
                        this.session.disablePrefetch();
                     }

                     if (!this.session.prefetchDisabled()) {
                        this.setMessageListener((MessageListener)(var4 = new JMSSystemMessageListenerImpl2(this)));
                        this.session.startPrefetch();
                     }
                  }
               }
            } else {
               if (!this.session.prefetchStarted()) {
                  throw new IllegalStateException(JMSClientExceptionLogger.logListenerExistsLoggable());
               }

               if (!this.session.isTransacted() && this.session.userTransactionsEnabled() && TransactionHelper.getTransactionHelper().getTransaction() != null) {
                  throw new UnsupportedOperationException(JMSClientExceptionLogger.logUserTXNotSupportPrefetchConsumerPerSessionLoggable().getMessage());
               }
            }

            this.session.setState(2);
         }
      }

      MessageImpl var6;
      try {
         MessageImpl var5;
         if (var4 == null) {
            var5 = this.session.receiveMessage(this, var1, var3);
            return var5;
         }

         var5 = (MessageImpl)((JMSSystemMessageListener)var4).receive(var1);
         var6 = this.session.afterReceive(var5, this.consumerId, var3);
      } catch (Exception var18) {
         throw JMSSession.handleException(var18);
      } finally {
         this.session.clearState(2);
      }

      return var6;
   }

   public final void close() throws JMSException {
      this.close(-1L);
   }

   final void close(long var1) throws JMSException {
      this.session.consumerClose(this, var1);
      if (this.messageListenerContext != null && this.messageListenerContext.getMessageListener() instanceof JMSSystemMessageListener) {
         this.session.markAsSystemMessageListener(false);
      }

      if (this.isDurable()) {
         this.removeDurableConsumer();
      }

   }

   private synchronized void checkClosed() throws JMSException {
      if (this.isClosed()) {
         throw new IllegalStateException(JMSClientExceptionLogger.logClosedConsumerLoggable());
      }
   }

   public final void publicCheckClosed() throws JMSException {
      this.checkClosed();
   }

   public final void removeDurableConsumer() {
      if (this.session.getConnection() != null) {
         this.session.getConnection().removeDurableSubscriber(this.name);
      }

   }

   public final Topic getTopic() throws JMSException {
      this.checkClosed();
      return this.destination;
   }

   public final boolean privateGetNoLocal() {
      return this.noLocal;
   }

   public final boolean getNoLocal() throws JMSException {
      this.checkClosed();
      return this.noLocal;
   }

   public final int getSubscriptionSharingPolicy() throws JMSException {
      this.checkClosed();
      return this.session.getSubscriptionSharingPolicy();
   }

   public final Queue getQueue() throws JMSException {
      this.checkClosed();
      return this.destination;
   }

   private int pushException(Request var1) {
      JMSPushExceptionRequest var2 = (JMSPushExceptionRequest)var1;
      weblogic.jms.common.JMSException var3 = var2.getException();
      if (var3 instanceof ConsumerClosedException) {
         ((ConsumerClosedException)var3).setConsumer(this);
      }

      if (this.isDurable()) {
         this.removeDurableConsumer();
      }

      try {
         synchronized(this) {
            this.setClosed(true);
         }

         this.session.onException(var3);
      } catch (Throwable var7) {
         JMSClientExceptionLogger.logStackTrace(var7);
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   public final int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 15366:
            return this.pushException(var1);
         default:
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSuchMethod4Loggable(var1.getMethodId()));
      }
   }

   private static ExpressionEvaluator createExpressionEvaluator(String var0) throws weblogic.jms.common.JMSException, InvalidSelectorException {
      Class var2;
      try {
         var2 = Class.forName("weblogic.utils.expressions.ExpressionParser");
      } catch (ClassNotFoundException var6) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logMulticastSelectorsLoggable());
      }

      try {
         Object var3 = var2.newInstance();
         Method var11 = var2.getMethod("parse", String.class);
         ExpressionEvaluator var1 = (ExpressionEvaluator)var11.invoke(var3, var0);
         return var1;
      } catch (IllegalAccessException var7) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInternalErrorLoggable(var7));
      } catch (NoSuchMethodException var8) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInternalError2Loggable(var8));
      } catch (InstantiationException var9) {
         throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logInternalError3Loggable(var9));
      } catch (InvocationTargetException var10) {
         Throwable var4 = var10.getTargetException();
         String var5;
         if (var4 instanceof ExpressionParserException) {
            var5 = var4.getMessage();
         } else {
            var5 = JMSClientExceptionLogger.logInvalidSelectorLoggable(var4).getMessage();
         }

         throw new InvalidSelectorException(var5);
      }
   }
}
