package weblogic.wsee.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WsLifeCycleListenerRegistry {
   private static WsLifeCycleListenerRegistry instance = new WsLifeCycleListenerRegistry();
   private List<WsLifeCycleListener> listeners = new ArrayList();

   private WsLifeCycleListenerRegistry() {
   }

   public static WsLifeCycleListenerRegistry getInstance() {
      return instance;
   }

   public synchronized void register(WsLifeCycleListener var1) {
      this.listeners.add(var1);
   }

   public synchronized void remove(WsLifeCycleListener var1) {
      this.listeners.remove(var1);
   }

   public synchronized void onEvent(WsLifeCycleEvent var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         WsLifeCycleListener var3 = (WsLifeCycleListener)var2.next();
         var3.onEvent(var1);
      }

   }
}
