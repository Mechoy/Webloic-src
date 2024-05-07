package weblogic.servlet.proxy;

import java.util.ArrayList;
import java.util.HashMap;

final class ServerManager {
   private final HashMap jvmidToIndexMap;
   private final ServiceUnavailableException exception;
   private final ArrayList serverList;
   private int index;
   private int size;

   private ServerManager() {
      this.index = 0;
      this.jvmidToIndexMap = new HashMap(5);
      this.serverList = new ArrayList();
      this.exception = new ServiceUnavailableException();
   }

   static ServerManager getServerManager() {
      return ServerManager.SingletonMaker.singleton;
   }

   synchronized ServerFactory getServerFactory(int var1) throws ServiceUnavailableException {
      if (this.size == 0) {
         throw this.exception;
      } else {
         Integer var2 = (Integer)this.jvmidToIndexMap.get(new Integer(var1));
         boolean var3 = true;
         if (var2 != null) {
            int var4 = var2;
            return (ServerFactory)this.serverList.get(var4);
         } else {
            this.index = this.index++ % this.size;
            return (ServerFactory)this.serverList.get(this.index);
         }
      }
   }

   private void reset() {
      this.size = this.serverList.size();
      this.jvmidToIndexMap.clear();

      for(int var1 = 0; var1 < this.size; ++var1) {
         ServerFactory var2 = (ServerFactory)this.serverList.get(var1);
         this.jvmidToIndexMap.put(new Integer(var2.hashCode()), new Integer(var1));
      }

   }

   synchronized void addServer(int var1, String var2, int var3) {
      ServerFactory var4 = new ServerFactory(var1, var2, var3);
      this.serverList.add(var4);
      this.size = this.serverList.size();
      this.jvmidToIndexMap.put(new Integer(var1), new Integer(this.size - 1));
   }

   synchronized void removeServer(int var1) {
      Integer var2 = new Integer(var1);
      Integer var3 = (Integer)this.jvmidToIndexMap.get(var2);
      this.jvmidToIndexMap.remove(var2);
      int var4 = var3;
      ServerFactory var5 = (ServerFactory)this.serverList.remove(var4);
      var5.cleanup();
      this.reset();
   }

   // $FF: synthetic method
   ServerManager(Object var1) {
      this();
   }

   static class SingletonMaker {
      static final ServerManager singleton = new ServerManager();
   }
}
