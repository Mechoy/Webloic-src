package weblogic.jms.backend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.ServerSession;
import javax.jms.ServerSessionPool;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.jms.client.JMSServerSessionPool;
import weblogic.jms.client.SessionInternal;
import weblogic.jms.common.ConsumerReconnectInfo;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSDiagnosticImageSource;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.frontend.FESession;
import weblogic.management.configuration.JMSConnectionConsumerMBean;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.KernelRequest;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;

public final class BEConnectionConsumerImpl extends BEConsumerImpl implements BEConnectionConsumerCommon {
   private ServerSessionPool sessionPool;
   private BEConnectionConsumerRuntimeDelegate delegate;

   BEConnectionConsumerImpl(JMSID var1, BEDestinationImpl var2, ServerSessionPool var3, Queue var4, Expression var5, String var6, int var7, long var8, int var10) throws JMSException {
      super(var2.getBackEnd());
      this.init(var3);
      BEConsumerCreateRequest var11 = new BEConsumerCreateRequest((JMSID)null, (JMSID)null, var1, (String)null, (String)null, (JMSID)null, var6, false, var7, var10, var8, (String)null, (ConsumerReconnectInfo)null);
      super.init((BESessionImpl)null, var2, var4, var5, var10, false, var11);
   }

   BEConnectionConsumerImpl(JMSID var1, BEDestinationImpl var2, ServerSessionPool var3, Queue var4, String var5, boolean var6, String var7, String var8, int var9, long var10, int var12) throws JMSException {
      super(var2.getBackEnd());
      this.init(var3);
      BEConsumerCreateRequest var13 = new BEConsumerCreateRequest((JMSID)null, (JMSID)null, var1, var7, var8, (JMSID)null, var5, var6, var9, var12, var10, (String)null, (ConsumerReconnectInfo)null);
      super.init((BESessionImpl)null, var2, var4, (Expression)null, var12, false, var13);
   }

   private void init(ServerSessionPool var1) {
      this.sessionPool = var1;
      this.setStateFlag(4);
   }

   public void initialize(JMSConnectionConsumerMBean var1) throws JMSException {
      this.delegate = new BEConnectionConsumerRuntimeDelegate(this, var1);
   }

   public void close() throws JMSException {
      this.sessionPool = null;
      if (this.delegate != null) {
         this.delegate.close();
      }

      super.close(0L);
   }

   public ServerSessionPool getServerSessionPool() throws JMSException {
      if (this.sessionPool == null) {
         throw new weblogic.jms.common.JMSException("ConnectionConsumer closed");
      } else {
         return this.sessionPool;
      }
   }

   public synchronized int getMessagesMaximum() {
      return this.windowSize;
   }

   public synchronized void setMessagesMaximum(int var1) {
      this.windowSize = var1;
   }

   protected void pushMessages(List var1) {
      ServerSession var2 = null;
      ArrayList var3 = new ArrayList(var1.size());

      try {
         var2 = this.getServerSessionPool().getServerSession();
         SessionInternal var4 = (SessionInternal)var2.getSession();
         FESession var31 = (FESession)InvocableManagerDelegate.delegate.invocableFind(8, var4.getJMSID());
         var31.setUpBackEndSession(this.destination.getDestinationImpl().getDispatcherId());
         BESessionImpl var6 = (BESessionImpl)InvocableManagerDelegate.delegate.invocableFind(16, var4.getJMSID());
         JMSDispatcher var7 = JMSDispatcherManager.getLocalDispatcher();
         synchronized(var6) {
            Iterator var9 = var1.iterator();

            while(true) {
               MessageElement var10;
               boolean var13;
               do {
                  do {
                     if (!var9.hasNext()) {
                        if (!var3.isEmpty()) {
                           if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                              JMSDebug.JMSBackEnd.debug("Starting server session with " + var3.size() + " messages");
                           }

                           var2.start();
                        }

                        var2 = null;
                        return;
                     }

                     var10 = (MessageElement)var9.next();
                     long var11 = var6.getNextSequenceNumber();
                     var10.setUserSequenceNum(var11);
                     var10.setUserData(this);
                     var3.add(var10);
                     var13 = this.allowsImplicitAcknowledge();
                     if (!var13) {
                        var6.addPendingMessage(var10, this);
                     }

                     if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                        JMSDebug.JMSBackEnd.debug("Pushing " + var10.getMessage() + " as seq number " + var11 + ". implicitAcknowledge = " + var13);
                     }

                     JMSPushEntry var15 = new JMSPushEntry((JMSID)null, this.id, var11, 0L, var10.getDeliveryCount(), 0);
                     var15.setDispatcher(var7);
                     var15.setClientResponsibleForAcknowledge(var13);
                     MessageImpl var16 = (MessageImpl)var10.getMessage();
                     var31.pushMessage(var16, var15);
                     var4.setPipelineGeneration(0);
                     var4.pushMessage(var16, var15);
                     this.adjustUnackedCount(1);
                  } while(!var13);
               } while(this.isKernelAutoAcknowledge());

               try {
                  KernelRequest var17 = this.queue.acknowledge(var10);
                  if (var17 != null) {
                     var17.getResult();
                  }
               } catch (KernelException var27) {
                  if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                     JMSDebug.JMSBackEnd.debug("Unexpected exception while implicitly acknowledging: " + var27, var27);
                  }
               }
            }
         }
      } catch (JMSException var29) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Error pushing messages: " + var29, var29);
         }

         try {
            KernelRequest var5 = new KernelRequest();
            this.listenRequest.getQueue().negativeAcknowledge(var3, this.getRedeliveryDelay(), var5);
            var5.getResult();
         } catch (KernelException var26) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Error nacking kernel messages that were delivered", var26);
            }
         }
      } finally {
         if (var2 != null) {
            ((BEServerSession)var2).goBackInPool();
         }

      }

   }

   public Runnable deliver(ListenRequest var1, List var2) {
      return this.deliver(var2);
   }

   public Runnable deliver(ListenRequest var1, MessageElement var2) {
      return this.deliver(var2);
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("ConnectionConsumer");
      this.dumpCommon(var2);
      String var3 = "";
      if (this.sessionPool != null && this.sessionPool instanceof JMSServerSessionPool) {
         JMSID var4 = ((JMSServerSessionPool)this.sessionPool).getServerSessionPoolId();
         if (var4 != null) {
            var3 = var4.toString();
         }
      }

      var2.writeAttribute("serverSessionPoolID", var3);
      var2.writeEndElement();
   }
}
