package weblogic.cluster.messaging.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public final class GroupMemberImpl implements GroupMember {
   private static final boolean DEBUG;
   private static final int RETRY_COUNT = 2;
   private long startTime;
   private final ServerConfigurationInformation configuration;
   private long lastArrivalTime;
   private Connection connection = null;
   private ReentrantLock lock = new ReentrantLock(true);
   ArrayList connectionList = new ArrayList();
   private static final int FIRST_LOCK_ACQUIRE_TIME_MILLIS;
   private static final int SECOND_LOCK_ACQUIRE_TIME_MILLIS;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   private static int initProperty(String var0, int var1) {
      try {
         return Integer.getInteger(var0, var1);
      } catch (SecurityException var3) {
         return var1;
      }
   }

   public GroupMemberImpl(ServerConfigurationInformation var1, long var2) {
      this.configuration = var1;
      this.startTime = var2;
   }

   public GroupMemberImpl(ServerConfigurationInformation var1) {
      this.configuration = var1;
   }

   public ServerConfigurationInformation getConfiguration() {
      return this.configuration;
   }

   public long getStartTime() {
      return this.startTime;
   }

   public void send(Message var1) throws IOException {
      boolean var2 = false;

      try {
         var2 = this.acquireLock(false);
         if (!var2) {
            throw new IOException("Timed out");
         }

         this.sendMessage(var1);
      } finally {
         if (var2) {
            this.unLock();
         }

      }

   }

   private void sendMessage(Message var1) throws IOException {
      IOException var2 = null;
      int var3 = 0;

      while(var3 < 2) {
         try {
            if (this.connection == null || this.connection.isDead()) {
               ConnectionManager var4 = Environment.getConnectionManager();
               this.connection = var4.createConnection(this.configuration);
            }

            if (this.connection != null && !this.connection.isDead()) {
               if (DEBUG) {
                  debug("Send message to " + this.configuration.getServerName() + ". Retry count " + var3);
               }

               this.connection.send(var1);
            }

            Iterator var7 = this.connectionList.iterator();

            while(var7.hasNext()) {
               Connection var5 = (Connection)var7.next();
               if (!var5.isDead()) {
                  if (DEBUG) {
                     debug("Send message to " + var5.getServerId());
                  }

                  var5.send(var1);
               } else if (DEBUG) {
                  debug("Connection for duplicate member: " + var5.getServerId() + " is dead.  Unable to deliver message");
               }
            }

            return;
         } catch (IOException var6) {
            var2 = var6;
            if (DEBUG && DEBUG) {
               debug("Failed to send message to " + this.configuration.getServerName() + "> Excpetion:" + var6);
            }

            ++var3;
         }
      }

      throw var2;
   }

   public void receive(Message var1, Connection var2) {
      if (DEBUG) {
         debug("received message: " + var1 + " from " + var2.getConfiguration().getServerName());
      }

      if (DEBUG) {
         debug("local group: " + GroupManagerImpl.getInstance().getLocalGroup());
      }

      GroupManagerImpl.getInstance().getLocalGroup().forward(var1, var2);
   }

   public void setLastMessageArrivalTime(long var1) {
      this.lastArrivalTime = var1;
   }

   public long getLastArrivalTime() {
      return this.lastArrivalTime;
   }

   private static void debug(String var0) {
      Environment.getLogService().debug("[GroupMember] " + var0);
   }

   public int compareTo(Object var1) {
      if (!$assertionsDisabled && !(var1 instanceof GroupMemberImpl)) {
         throw new AssertionError();
      } else {
         GroupMemberImpl var2 = (GroupMemberImpl)var1;
         return this.configuration.compareTo(var2.configuration);
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof GroupMemberImpl)) {
         return false;
      } else {
         GroupMemberImpl var2 = (GroupMemberImpl)var1;
         return this.configuration.equals(var2.configuration);
      }
   }

   public int hashCode() {
      return this.configuration.hashCode();
   }

   public String toString() {
      return this.configuration.getServerName();
   }

   public boolean addConnection(Connection var1) {
      if (DEBUG) {
         debug("Adding connection to " + this + " with conn: " + var1 + " and serverId: " + var1.getServerId());
      }

      boolean var2 = false;
      boolean var3 = false;

      try {
         var2 = this.acquireLock(true);
         if (var2) {
            if (this.connection == null) {
               this.connection = var1;
               var3 = true;
            } else if (this.connection.getServerId() == null && var1.getServerId() != null) {
               this.connection = var1;
               var3 = true;
            } else if (this.connection.getServerId() != null && var1.getServerId() != null && !this.connection.getServerId().equals(var1.getServerId())) {
               Iterator var4 = this.connectionList.iterator();
               boolean var5 = false;

               while(var4.hasNext()) {
                  Connection var6 = (Connection)var4.next();
                  if (var6.getServerId() != null && var6.getServerId().equals(var1.getServerId())) {
                     var5 = true;
                     break;
                  }
               }

               if (!var5) {
                  if (DEBUG) {
                     debug("adding connection to the connectionList with serverId: " + var1.getServerId());
                  }

                  this.connectionList.add(var1);
               }
            }
         }
      } finally {
         if (var2) {
            this.unLock();
         }

      }

      return var3;
   }

   private boolean acquireLock(boolean var1) {
      boolean var2 = false;

      try {
         var2 = this.lock.tryLock((long)FIRST_LOCK_ACQUIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
         if (!var2) {
            if (var1) {
               return var2;
            }

            int var3 = this.lock.getQueueLength();
            if (var3 > 0) {
               if (DEBUG) {
                  debug("Unable to acquirelock to write to " + this.configuration.getServerName() + ".Total threads waiting to acquire lock=" + var3);
               }
            } else {
               var2 = this.lock.tryLock((long)SECOND_LOCK_ACQUIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
            }
         }
      } catch (InterruptedException var4) {
      }

      return var2;
   }

   private void unLock() {
      if (this.lock.isHeldByCurrentThread()) {
         this.lock.unlock();
      }

   }

   static {
      $assertionsDisabled = !GroupMemberImpl.class.desiredAssertionStatus();
      DEBUG = Environment.DEBUG;
      FIRST_LOCK_ACQUIRE_TIME_MILLIS = initProperty("weblogic.unicast.sendTimeoutMillis", 1000);
      SECOND_LOCK_ACQUIRE_TIME_MILLIS = initProperty("weblogic.unicast.sendBackoffTimeoutMillis", 10000);
   }
}
