package weblogic.wsee.connection.transport.jms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import javax.jms.JMSException;
import javax.naming.NamingException;
import weblogic.wsee.util.Verbose;

public class JmsQueueConnectionPool extends TimerTask {
   private static final boolean verbose = Verbose.isVerbose(JmsQueueConnectionPool.class);
   private static final int POOL_CAPACITY = 32;
   private static JmsQueueConnectionPool instance = new JmsQueueConnectionPool();
   private HashMap connections = new HashMap();
   private int numOfWaitings;

   private JmsQueueConnectionPool() {
   }

   public static JmsQueueConnectionPool getInstance() {
      return instance;
   }

   public synchronized void close() {
      Iterator var1 = this.connections.values().iterator();

      while(var1.hasNext()) {
         JmsQueueConnectionPoolInternal var2 = (JmsQueueConnectionPoolInternal)var1.next();
         var2.clear();
      }

      this.connections.clear();
   }

   JmsQueueConnection getNewConnection(JmsTransportInfo var1, JmsQueueConnectionPoolInternal var2) throws NamingException, IOException, JMSException {
      JmsQueueConnection var3 = new JmsQueueConnection(var1);
      var3.setPool(var2);
      var2.increaseCount();
      return var3;
   }

   synchronized JmsQueueConnection getConnection(JmsTransportInfo var1) throws NamingException, IOException, JMSException {
      JmsQueueConnectionPoolInternal var2 = (JmsQueueConnectionPoolInternal)this.connections.get(var1);
      if (var2 == null) {
         var2 = new JmsQueueConnectionPoolInternal(32);
         this.connections.put(var1, var2);
      }

      JmsQueueConnection var3 = var2.remove();
      if (var3 == null) {
         if (var2.connectionsCount() < 32) {
            var3 = this.getNewConnection(var1, var2);
         } else {
            while(var2.size() == 0 && var2.connectionsCount() >= 32 && var2.isAlive()) {
               ++this.numOfWaitings;

               try {
                  this.wait();
               } catch (InterruptedException var9) {
               } finally {
                  --this.numOfWaitings;
               }
            }

            if (!var2.isAlive()) {
               var3 = this.getConnection(var1);
            } else if (var2.connectionsCount() < 32) {
               var3 = this.getNewConnection(var1, var2);
            } else {
               var3 = var2.remove();
            }
         }
      }

      return var3;
   }

   synchronized void release(JmsQueueConnectionPoolInternal var1, JmsQueueConnection var2) {
      if (var2 != null) {
         if (!var2.isAlive()) {
            var2.closeIgoreException();
            if (var1.isAlive()) {
               var1.decreaseCount();
               this.notifyAll();
            }
         } else if (!var1.isAlive()) {
            var2.closeIgoreException();
            this.notifyAll();
         } else if (var1.add(var2) && this.numOfWaitings > 0) {
            this.notifyAll();
         }

      }
   }

   public void run() {
   }
}
