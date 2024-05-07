package weblogic.jms.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ServerSessionPool;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDestinationSecurity;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSMessageEventLogListener;
import weblogic.jms.common.JMSMessageLogHelper;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSSQLFilter;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.extensions.ConsumerClosedException;
import weblogic.logging.jms.JMSMessageLogger;
import weblogic.messaging.kernel.Event;
import weblogic.messaging.kernel.EventListener;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.Filter;
import weblogic.messaging.kernel.GroupAddEvent;
import weblogic.messaging.kernel.GroupRemoveEvent;
import weblogic.messaging.kernel.InvalidExpressionException;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.MessageAddEvent;
import weblogic.messaging.kernel.MessageEvent;
import weblogic.messaging.kernel.MessageExpirationEvent;
import weblogic.messaging.kernel.MessageReceiveEvent;
import weblogic.messaging.kernel.MessageRedeliveryLimitEvent;
import weblogic.messaging.kernel.MessageRemoveEvent;
import weblogic.messaging.kernel.MessageSendEvent;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.UnitOfWorkEvent;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;

public class BEQueueImpl extends BEDestinationImpl implements JMSMessageEventLogListener, EventListener {
   private Queue queue;
   private Filter sqlFilter;
   private final Map browsers = new HashMap();
   private boolean isNewlyCreated;

   public BEQueueImpl(BackEnd var1, String var2, boolean var3, JMSDestinationSecurity var4) throws JMSException {
      super(var1, var2, var3, var4);
      Queue var5 = var1.findKernelQueue(var2);
      if (var5 == null) {
         var5 = var1.createKernelQueue(var2, (Map)null);
         this.isNewlyCreated = true;
      }

      this.setKernel(var5);
   }

   protected void setKernel(Queue var1) throws JMSException {
      super.setKernel(var1);
      this.queue = var1;
   }

   public void open() throws JMSException {
      super.open();
      this.sqlFilter = new JMSSQLFilter(this.queue.getKernel());
      this.queue.setFilter(this.sqlFilter);
      this.queue.setComparator(this.comparator);

      try {
         this.queue.setProperty("RedirectionListener", this);
      } catch (KernelException var2) {
         throw new weblogic.jms.common.JMSException(var2);
      }

      this.queue.addListener(this);
      if (this.isMessageLoggingEnabled() && !this.backEnd.isMemoryLow()) {
         this.resumeMessageLogging();
      }

      BEExtension var1 = this.getExtension();
      if (var1 != null) {
         var1.restorePersistentState(this.queue);
         addPropertyFlags(this.queue, "Logging", 16);
      }

   }

   public int getDestinationTypeIndicator() {
      return this.isTemporary() ? 4 : 1;
   }

   public synchronized void setDestinationKeysList(List var1) {
      super.setDestinationKeysList(var1);
      if (this.queue != null) {
         this.queue.setComparator(this.comparator);
      }

   }

   private Expression createFilterExpression(String var1) throws JMSException {
      if (var1 != null) {
         try {
            return this.sqlFilter.createExpression(new JMSSQLExpression(var1));
         } catch (InvalidExpressionException var3) {
            throw new InvalidSelectorException(var3.toString());
         } catch (KernelException var4) {
            throw new weblogic.jms.common.JMSException(var4);
         }
      } else {
         return null;
      }
   }

   protected BEConsumerImpl createConsumer(BESessionImpl var1, boolean var2, BEConsumerCreateRequest var3) throws JMSException {
      if (var3.getName() != null) {
         throw new weblogic.jms.common.JMSException("Durable consumers are not supported on queues");
      } else {
         this.checkShutdownOrSuspendedNeedLock("create consumer");
         BEConsumerImpl var4 = new BEConsumerImpl(var1, this, this.queue, this.createFilterExpression(var3.getSelector()), 0, false, var3);
         this.addConsumer(var4);
         if (var2) {
            var4.start();
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Created a new consumer with ID " + var3.getConsumerId() + " on queue " + this.name);
         }

         return var4;
      }
   }

   protected BEConnectionConsumerImpl createConnectionConsumer(JMSID var1, ServerSessionPool var2, String var3, String var4, String var5, boolean var6, int var7, long var8, boolean var10, boolean var11) throws JMSException {
      if (var10) {
         throw new weblogic.jms.common.JMSException("Durable consumers are not supported on queues");
      } else {
         this.checkShutdownOrSuspendedNeedLock("create connection consumer");
         BEConnectionConsumerImpl var12 = new BEConnectionConsumerImpl(var1, this, var2, this.queue, this.createFilterExpression(var5), var5, var7, var8, 0);
         this.addConsumer(var12);
         if (var11) {
            var12.start();
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Created a new ConnectionConsumer with ID " + var1 + " on queue " + this.name);
         }

         return var12;
      }
   }

   public BEBrowser createBrowser(BESession var1, String var2) throws JMSException {
      this.checkShutdownOrSuspendedNeedLock("create browser");
      BEBrowserImpl var3 = new BEBrowserImpl(var1, this, this.queue, var2);
      synchronized(this) {
         this.browsers.put(var3.getJMSID(), var3);
         return var3;
      }
   }

   synchronized void removeBrowser(JMSID var1) {
      this.browsers.remove(var1);
   }

   protected void closeAllBrowsers(String var1) {
      ArrayList var2;
      synchronized(this) {
         var2 = new ArrayList(this.browsers.values());
         this.browsers.clear();
      }

      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         try {
            BEBrowserImpl var4 = (BEBrowserImpl)var3.next();
            BESession var5 = var4.getSession();
            var4.close();
            if (var5 != null) {
               BEConnection var6 = var5.getConnection();
               if (var6 != null) {
                  JMSServerUtilities.anonDispatchNoReply(new JMSPushExceptionRequest(11, var4.getJMSID(), new ConsumerClosedException((MessageConsumer)null, var1)), var6.getDispatcher());
               }
            }
         } catch (JMSException var7) {
         }
      }

   }

   public Queue getKernelQueue() {
      return this.queue;
   }

   public final void setMessageLoggingEnabled(boolean var1) {
      if ((!super.isMessageLoggingEnabled() || !var1) && (super.isMessageLoggingEnabled() || var1)) {
         super.setMessageLoggingEnabled(var1);

         try {
            if (super.isMessageLoggingEnabled() && !this.backEnd.isMemoryLow()) {
               this.resumeMessageLogging();
            } else if (!var1) {
               this.suspendMessageLogging();
            }
         } catch (JMSException var3) {
         }

      }
   }

   public JMSMessageLogger getJMSMessageLogger() {
      return this.backEnd.getJMSMessageLogger();
   }

   public void onEvent(Event var1) {
      if (var1 instanceof MessageSendEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageAddEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageReceiveEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageExpirationEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageRedeliveryLimitEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof MessageRemoveEvent) {
         this.onMessageEvent((MessageEvent)var1);
      } else if (var1 instanceof UnitOfWorkEvent) {
         if (this.getExtension() != null) {
            UnitOfWorkEvent var2 = (UnitOfWorkEvent)var1;
            if (var2.isAdd()) {
               this.getExtension().unitOfWorkAddEvent(var2.getUnitOfWork());
            } else {
               this.getExtension().unitOfWorkRemoveEvent(var2.getUnitOfWork());
            }
         }
      } else if (var1 instanceof GroupRemoveEvent) {
         if (this.getExtension() != null) {
            this.getExtension().groupRemoveEvent(((GroupRemoveEvent)var1).getGroup().getName());
         }
      } else if (var1 instanceof GroupAddEvent && this.getExtension() != null) {
         this.getExtension().groupAddEvent(((GroupAddEvent)var1).getGroup().getName());
      }

   }

   private final void onMessageEvent(MessageEvent var1) {
      JMSMessageLogHelper.logMessageEvent(this, var1);
   }

   public void resumeMessageLogging() throws JMSException {
      if (this.destination != null) {
         addPropertyFlags(this.destination, "Logging", 15);
      }
   }

   public void suspendMessageLogging() throws JMSException {
      if (this.destination != null) {
         removePropertyFlags(this.destination, "Logging", 15);
      }
   }

   public boolean isMessageLogging() {
      if (this.destination == null) {
         return false;
      } else {
         synchronized(this.destination) {
            Integer var2 = (Integer)this.destination.getProperty("Logging");
            if (var2 == null) {
               return false;
            } else {
               int var3 = var2;
               return (var3 & 15) == 15;
            }
         }
      }
   }

   public void setQuota(Quota var1) throws BeanUpdateFailedException {
      HashMap var2 = new HashMap();

      try {
         var2.put("Quota", var1);
         this.destination.setProperties(var2);
      } catch (KernelException var4) {
         throw new BeanUpdateFailedException("Messaging Kernel failed to act on the quota", var4);
      }
   }

   public boolean isNewlyCreated() {
      return this.isNewlyCreated;
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Queue");
      super.dump(var1, var2);
      var2.writeEndElement();
   }
}
