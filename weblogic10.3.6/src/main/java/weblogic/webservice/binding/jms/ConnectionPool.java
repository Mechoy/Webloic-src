package weblogic.webservice.binding.jms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import javax.jms.JMSException;
import javax.naming.NamingException;
import weblogic.utils.collections.StackPool;

/** @deprecated */
public class ConnectionPool extends TimerTask {
   private static ConnectionPool instance = new ConnectionPool();
   private HashMap connections = new HashMap();

   private ConnectionPool() {
   }

   public static ConnectionPool getInstance() {
      return instance;
   }

   public void close() {
      synchronized(this.connections) {
         Iterator var2 = this.connections.values().iterator();

         while(var2.hasNext()) {
            StackPool var3 = (StackPool)var2.next();

            JMSConnection var4;
            while((var4 = (JMSConnection)var3.remove()) != null) {
               try {
                  var4.close();
               } catch (JMSException var7) {
               }
            }
         }

         this.connections.clear();
      }
   }

   JMSConnection getConnection(JMSBindingInfo var1) throws NamingException, JMSException {
      StackPool var2 = (StackPool)this.connections.get(var1);
      if (var2 == null) {
         var2 = new StackPool(32);
         this.connections.put(var1, var2);
      }

      JMSConnection var3 = (JMSConnection)var2.remove();
      if (var3 == null) {
         var3 = new JMSConnection(var1);
         var3.setPool(var2);
      }

      return var3;
   }

   public void run() {
   }
}
