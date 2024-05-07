package weblogic.auddi.util;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public abstract class ConnectionPool implements Shutdownable, Initializable {
   private String m_poolName;
   private Hashtable m_pool;
   private Hashtable m_busyPool;
   private int m_systemPoolSize = 0;
   private int m_connectionCounter = -1;
   private boolean m_isInitialized = false;

   public abstract int getPoolIncrement();

   public abstract int getPoolInitialSize();

   public abstract int getPoolMaxSize();

   public abstract int getPoolSystemMaxSize();

   public void initialize() throws InitializationException {
      Logger.trace("+ConnectionPool.initialize()");

      try {
         this.m_pool = new Hashtable();
         this.m_busyPool = new Hashtable();
         this.uninitialize();
         this.m_connectionCounter = 0;
         this.createConnections(this.getPoolInitialSize());
         this.m_isInitialized = true;
      } catch (ConnectException var2) {
         Logger.error((Throwable)var2);
         Logger.trace("-EXCEPTION(InitializationException) ConnectionPool.initialize()");
         throw new InitializationException(var2);
      } catch (ShutdownException var3) {
         Logger.error((Throwable)var3);
         Logger.trace("-EXCEPTION(InitializationException) ConnectionPool.initialize()");
         throw new InitializationException(var3);
      }

      Logger.trace("-ConnectionPool.initialize()");
   }

   private void uninitialize() throws ShutdownException {
      Logger.trace("+ConnectionPool.uninitialize()");
      this.shutdown();
      Logger.trace("-ConnectionPool.uninitialize()");
   }

   public ConnectionPool(String var1) throws ConnectException {
      Logger.trace("+ConnectionPool.CTOR()");
      this.m_poolName = var1;
      ShutdownManager.registerApp(this);
      Logger.trace("-ConnectionPool.CTOR()");
   }

   public String getName() {
      return this.m_poolName;
   }

   protected boolean createConnections(int var1) throws ConnectException {
      return this.createConnections(var1, this.m_pool, this.getPoolMaxSize());
   }

   protected boolean createConnections(int var1, Hashtable var2, int var3) throws ConnectException {
      Logger.trace("+ConnectionPool.createConnections(), >> " + this.m_poolName);
      if (var3 - var2.size() - this.m_busyPool.size() <= 0) {
         Logger.trace("-ConnectionPool.createConnections()");
         return false;
      } else {
         if (var1 > var3 - var2.size() - this.m_busyPool.size()) {
            var1 = var3 - var2.size() - this.m_busyPool.size();
         }

         for(int var4 = 0; var4 < var1; ++var4) {
            Connection var5 = this.getNewConnection();
            var2.put(var5.getConnectionObject(), var5);
         }

         Logger.trace("-ConnectionPool.createConnections()");
         return true;
      }
   }

   public abstract Connection getNewConnection() throws ConnectException;

   private int getTotal() {
      return this.m_pool.size() + this.m_busyPool.size();
   }

   public synchronized Connection getConnectionFromPool() throws ConnectException {
      if (!this.m_isInitialized) {
         try {
            this.initialize();
         } catch (InitializationException var4) {
            Logger.error((Throwable)var4);
            Logger.trace("-EXCEPTION(ConnectException) ConnectionPool.initialize()");
            throw new ConnectException(var4);
         }
      }

      ++this.m_connectionCounter;
      boolean var1 = false;
      if (this.m_pool.size() == 0) {
         var1 = this.createConnections(this.getPoolIncrement());
         if (!var1) {
            if (this.m_busyPool.size() < this.getPoolSystemMaxSize()) {
               Connection var5 = this.getNewConnection();
               ++this.m_systemPoolSize;
               return var5;
            }

            Logger.error("NO MORE CONNECTIONS LEFT...");
            return null;
         }
      }

      if (this.m_pool.size() > 0) {
         Object var2 = this.m_pool.keySet().iterator().next();
         Connection var3 = (Connection)this.m_pool.remove(var2);
         this.m_busyPool.put(var3.getConnectionObject(), var3);
         return var3;
      } else {
         return null;
      }
   }

   public synchronized void returnConnectionToPool(Connection var1) throws ConnectException {
      if (this.m_systemPoolSize > 0) {
         --this.m_systemPoolSize;
         var1.close();
      } else {
         Object var2 = var1.getConnectionObject();
         this.m_busyPool.remove(var2);
         this.m_pool.put(var2, var1);
      }

   }

   public int getBusySize() {
      return this.m_busyPool.size();
   }

   public int getSystemPoolSize() {
      return this.m_systemPoolSize;
   }

   public int getFreeSize() {
      return this.m_pool.size();
   }

   public int getCounter() {
      return this.m_connectionCounter;
   }

   public void resetCounter() {
      this.m_connectionCounter = 0;
   }

   public void shutdown() throws ShutdownException {
      Logger.trace("+ConnectionPool.shutdown()");

      try {
         Iterator var1 = this.m_pool.entrySet().iterator();

         while(true) {
            Connection var2;
            if (!var1.hasNext()) {
               var1 = this.m_busyPool.entrySet().iterator();

               while(var1.hasNext()) {
                  var2 = (Connection)((Map.Entry)var1.next()).getValue();
                  var2.close();
               }

               this.m_pool = new Hashtable();
               this.m_busyPool = new Hashtable();
               break;
            }

            var2 = (Connection)((Map.Entry)var1.next()).getValue();
            var2.close();
         }
      } catch (ConnectException var3) {
         Logger.trace("-EXCEPTION(ConnectException): ConnectionPool.shutdown()");
         throw new ShutdownException(var3);
      }

      Logger.trace("-ConnectionPool.shutdown()");
   }

   public void printStats(int var1) {
      Logger.Log(var1, "m_connectionCounter : " + this.m_connectionCounter);
      Logger.Log(var1, "           busySize : " + this.getBusySize());
      Logger.Log(var1, "           freeSize : " + this.getFreeSize());
      Logger.Log(var1, "     systemPoolSize : " + this.getSystemPoolSize());
   }
}
