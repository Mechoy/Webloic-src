package weblogic.wsee.buffer2.internal.common;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.WseeCoreLogger;

public final class JmsSessionPool {
   private static final Logger LOGGER = Logger.getLogger(JmsSessionPool.class.getName());
   private static final ConcurrentLinkedQueue<QueueSession> _sessionQueue = new ConcurrentLinkedQueue();
   private final QueueConnectionFactory factory;
   private volatile QueueConnection connection;
   private final boolean transacted;
   private final int acknowledgeMode;

   private static void drainPool() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("JmsSessionPool draining " + _sessionQueue.size() + " queue sessions");
      }

      Iterator var0 = _sessionQueue.iterator();

      while(var0.hasNext()) {
         QueueSession var1 = (QueueSession)var0.next();

         try {
            var1.close();
         } catch (Exception var3) {
            WseeCoreLogger.logUnexpectedException(var3.toString(), var3);
         }
      }

   }

   public JmsSessionPool(QueueConnectionFactory var1, boolean var2, int var3) {
      this.factory = var1;
      this.transacted = var2;
      this.acknowledgeMode = var3;
   }

   public final QueueSession take() {
      QueueSession var1 = (QueueSession)_sessionQueue.poll();
      if (var1 == null) {
         return this.create();
      } else {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("JmsSessionPool took QueueSession from pool. Remaining: " + _sessionQueue.size() + " queue sessions");
         }

         return var1;
      }
   }

   public final void recycle(QueueSession var1) {
      _sessionQueue.offer(var1);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("JmsSessionPool recycled QueueSession back into pool. Current count: " + _sessionQueue.size() + " queue sessions");
      }

   }

   protected QueueSession create() {
      try {
         if (this.connection == null) {
            synchronized(this) {
               if (this.connection == null) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("JmsSessionPool creating QueueConnection to populate the pool");
                  }

                  this.connection = this.factory.createQueueConnection();
               }
            }
         }

         QueueSession var1;
         synchronized(this) {
            var1 = this.connection.createQueueSession(this.transacted, this.acknowledgeMode);
            this.connection.start();
         }

         return var1;
      } catch (JMSException var6) {
         throw new WebServiceException("Could not create JMS resource: " + var6);
      }
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread() {
         public void run() {
            if (JmsSessionPool.LOGGER.isLoggable(Level.FINE)) {
               JmsSessionPool.LOGGER.fine("JmsSessionPool ShutdownHook firing. Killing " + JmsSessionPool._sessionQueue.size() + " queue sessions");
            }

            JmsSessionPool.drainPool();
         }
      });
   }
}
