package weblogic.jms.adapter;

import java.util.Vector;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ManagedConnection;

public class EventListenerManager implements ConnectionEventListener {
   private Vector listeners = new Vector();
   private ManagedConnection mc;

   public EventListenerManager(ManagedConnection var1) {
      this.mc = var1;
   }

   public void sendEvent(int var1, Exception var2, Object var3) {
      Vector var4 = null;
      synchronized(this) {
         var4 = (Vector)this.listeners.clone();
      }

      ConnectionEvent var5 = null;
      if (var2 == null) {
         var5 = new ConnectionEvent(this.mc, var1);
      } else {
         var5 = new ConnectionEvent(this.mc, var1, var2);
      }

      if (var3 != null) {
         var5.setConnectionHandle(var3);
      }

      int var6 = var4.size();

      for(int var7 = 0; var7 < var6; ++var7) {
         ConnectionEventListener var8 = (ConnectionEventListener)var4.elementAt(var7);
         switch (var1) {
            case 1:
               var8.connectionClosed(var5);
               break;
            case 2:
               var8.localTransactionStarted(var5);
               break;
            case 3:
               var8.localTransactionCommitted(var5);
               break;
            case 4:
               var8.localTransactionRolledback(var5);
               break;
            case 5:
               var8.connectionErrorOccurred(var5);
               break;
            default:
               throw new IllegalArgumentException("Illegal eventType: " + var1);
         }
      }

   }

   public synchronized void addConnectorListener(ConnectionEventListener var1) {
      this.listeners.addElement(var1);
   }

   public synchronized void removeConnectorListener(ConnectionEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void connectionClosed(ConnectionEvent var1) {
   }

   public void connectionErrorOccurred(ConnectionEvent var1) {
      this.sendEvent(5, var1.getException(), (Object)null);
   }

   public void localTransactionStarted(ConnectionEvent var1) {
   }

   public void localTransactionRolledback(ConnectionEvent var1) {
   }

   public void localTransactionCommitted(ConnectionEvent var1) {
   }
}
