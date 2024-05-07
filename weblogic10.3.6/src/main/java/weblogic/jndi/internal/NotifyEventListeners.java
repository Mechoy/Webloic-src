package weblogic.jndi.internal;

import java.util.Iterator;
import java.util.List;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingListener;
import javax.naming.event.ObjectChangeListener;

public final class NotifyEventListeners {
   private static final boolean DEBUG = true;
   private final List listeners;
   private final NamingEvent namingEvent;
   private final int action;

   NotifyEventListeners(List var1, NamingEvent var2, int var3) {
      this.listeners = var1;
      this.namingEvent = var2;
      this.action = var3;
   }

   public void notifyListeners() {
      Iterator var1 = this.listeners.iterator();

      while(var1.hasNext()) {
         NamingListener var2 = (NamingListener)var1.next();
         if (var2 instanceof ObjectChangeListener) {
            this.handleObjectChangeListener(var2);
         } else {
            if (!(var2 instanceof NamespaceChangeListener)) {
               throw new AssertionError(" Unknown event listener " + var2 + '\t' + var2.getClass().getName());
            }

            this.handleNamespaceChangeListener(this.action, var2);
         }
      }

   }

   private void handleObjectChangeListener(NamingListener var1) {
      ObjectChangeListener var2 = (ObjectChangeListener)var1;
      var2.objectChanged(this.namingEvent);
   }

   private void handleNamespaceChangeListener(int var1, NamingListener var2) {
      NamespaceChangeListener var3 = (NamespaceChangeListener)var2;
      switch (var1) {
         case 0:
            var3.objectAdded(this.namingEvent);
            break;
         case 1:
            var3.objectRemoved(this.namingEvent);
            break;
         case 2:
            var3.objectRenamed(this.namingEvent);
            break;
         default:
            throw new AssertionError("Unknown action: " + var1 + " on listener " + var2);
      }

   }
}
