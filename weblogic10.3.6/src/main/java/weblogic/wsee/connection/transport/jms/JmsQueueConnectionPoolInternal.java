package weblogic.wsee.connection.transport.jms;

import weblogic.utils.collections.StackPool;

public class JmsQueueConnectionPoolInternal {
   private static final long POOL_REFRESH_TIME = 180000L;
   private StackPool pool = null;
   private int connectionsCount = 0;
   private boolean alive = true;
   private long lastRefreshTime = 0L;

   public JmsQueueConnectionPoolInternal(int var1) {
      this.pool = new StackPool(var1);
      this.connectionsCount = 0;
      this.alive = true;
      this.lastRefreshTime = System.currentTimeMillis();
   }

   public JmsQueueConnection remove() {
      return this.pool == null ? null : (JmsQueueConnection)this.pool.remove();
   }

   public boolean add(JmsQueueConnection var1) {
      return this.pool == null ? false : this.pool.add(var1);
   }

   public int size() {
      return this.pool == null ? 0 : this.pool.size();
   }

   public int connectionsCount() {
      return this.connectionsCount;
   }

   public void increaseCount() {
      ++this.connectionsCount;
   }

   public void decreaseCount() {
      --this.connectionsCount;
   }

   public boolean needRefresh() {
      if (!this.alive) {
         return true;
      } else {
         if (System.currentTimeMillis() - this.lastRefreshTime > 180000L) {
            this.alive = false;
         }

         return !this.alive;
      }
   }

   public boolean isAlive() {
      return this.alive;
   }

   public void clear() {
      if (this.pool != null) {
         JmsQueueConnection var1;
         while((var1 = (JmsQueueConnection)this.pool.remove()) != null) {
            var1.closeIgoreException();
         }

      }
   }
}
