package weblogic.wsee.reliability2.api_internal;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.WseeRmLogger;

public class WsrmLifecycleEventListenerRegistry {
   private static final Logger LOGGER = Logger.getLogger(WsrmLifecycleEventListenerRegistry.class.getName());
   private static final WsrmLifecycleEventListenerRegistry _instance = new WsrmLifecycleEventListenerRegistry();
   private List<WsrmLifecycleEventListener> _listenerList = new ArrayList();

   public static WsrmLifecycleEventListenerRegistry getInstance() {
      return _instance;
   }

   private WsrmLifecycleEventListenerRegistry() {
   }

   public void addListener(WsrmLifecycleEventListener var1) {
      this._listenerList.add(var1);
   }

   public void removeListener(WsrmLifecycleEventListener var1) {
      this._listenerList.remove(var1);
   }

   public void clearListeners() {
      this._listenerList.clear();
   }

   public void notifyEventType(WsrmLifecycleEvent.Type var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Notifying listeners of event: " + var1);
      }

      WsrmLifecycleEventListener[] var2 = (WsrmLifecycleEventListener[])this._listenerList.toArray(new WsrmLifecycleEventListener[this._listenerList.size()]);
      WsrmLifecycleEventListener[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WsrmLifecycleEventListener var6 = var3[var5];

         try {
            var6.onEventType(var1);
         } catch (Exception var8) {
            WseeRmLogger.logUnexpectedException(var8.toString(), var8);
         }
      }

   }
}
