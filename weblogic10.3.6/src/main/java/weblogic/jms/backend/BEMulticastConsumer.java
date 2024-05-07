package weblogic.jms.backend;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import javax.jms.JMSException;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.multicast.JMSTMSocket;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.ListenRequest;
import weblogic.messaging.kernel.Listener;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;

final class BEMulticastConsumer extends BEDeliveryList implements Listener {
   private static final int DEFAULT_WINDOW_SIZE = 256;
   private final DestinationImpl destination;
   private final Queue queue;
   private final int port;
   private final byte ttl;
   private final InetAddress group;
   private long sequenceNum;
   private final JMSTMSocket socket;
   private ListenRequest listenRequest;
   private boolean started;

   BEMulticastConsumer(BackEnd var1, Queue var2, DestinationImpl var3, InetAddress var4, int var5, byte var6, JMSTMSocket var7) {
      super(var1);
      this.setWorkManager(var1.getWorkManager());
      this.queue = var2;
      this.destination = var3;
      this.group = var4;
      this.port = var5;
      this.ttl = var6;
      this.socket = var7;
   }

   Queue getQueue() {
      return this.queue;
   }

   synchronized void stop() {
      if (this.started) {
         if (this.listenRequest != null) {
            this.listenRequest.stop();
         }

         this.started = false;
      }
   }

   synchronized void start() throws JMSException {
      if (!this.started) {
         try {
            this.setWorkManager(this.getBackEnd().getWorkManager());
            this.listenRequest = this.queue.listen((Expression)null, 256, true, this, this, (String)null, this.getBackEnd().getWorkManager());
         } catch (KernelException var2) {
            throw new weblogic.jms.common.JMSException("Error creating consumer on kernel queue", var2);
         }

         this.started = true;
      }
   }

   protected void pushMessages(List var1) {
      ListenRequest var2;
      synchronized(this) {
         var2 = this.listenRequest;
      }

      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         MessageImpl var4 = (MessageImpl)((MessageElement)var3.next()).getMessage();

         try {
            synchronized(this.socket) {
               this.socket.send(var4, this.destination, var4.getConnectionId(), this.group, this.port, this.ttl, (long)(this.sequenceNum++));
            }
         } catch (IOException var9) {
            JMSDebug.JMSBackEnd.debug("Error forwarding multicast message", var9);
         }

         try {
            var2.incrementCount(1);
         } catch (KernelException var7) {
            JMSDebug.JMSBackEnd.debug("Error incrementing window for multicast request", var7);
         }
      }

   }
}
