package weblogic.wsee.reliability;

import java.util.HashMap;
import java.util.Map;

/** @deprecated */
public class ReliabilityErrorListenerRegistry {
   private static ReliabilityErrorListenerRegistry _instance;
   private Map<String, ReliabilityErrorListener> _listenerMap = new HashMap();

   public static ReliabilityErrorListenerRegistry getInstance() {
      Class var0 = ReliabilityErrorListenerRegistry.class;
      synchronized(ReliabilityErrorListenerRegistry.class) {
         if (_instance == null) {
            _instance = new ReliabilityErrorListenerRegistry();
         }
      }

      return _instance;
   }

   private ReliabilityErrorListenerRegistry() {
   }

   public synchronized void registerListener(String var1, ReliabilityErrorListener var2) {
      this._listenerMap.put(var1, var2);
   }

   public synchronized ReliabilityErrorListener unregisterListener(String var1) {
      return (ReliabilityErrorListener)this._listenerMap.remove(var1);
   }

   public synchronized ReliabilityErrorListener getListener(String var1) {
      return (ReliabilityErrorListener)this._listenerMap.get(var1);
   }
}
