package weblogic.servlet.proxy;

import java.rmi.RemoteException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.timers.NakedTimerListener;
import weblogic.timers.Timer;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.work.WorkManagerFactory;

public final class HealthCheckTrigger implements NakedTimerListener {
   private static final boolean DEBUG = true;
   private final Environment env;
   private final String host;
   private final String port;
   private final String toStringMsg;
   private Context ctx;
   private HealthCheck healthCheck;
   private int healthCheckInterval;
   private int healthCheckFailureCount = 0;
   private int maxHealthCheckInterval;
   private int maxRetriesCount = 0;
   private int maxRetries = 3;
   private int serverID = -1;
   private int originalHealthCheckInterval;

   public HealthCheckTrigger(String var1, String var2, int var3, int var4, int var5) {
      this.healthCheckInterval = var3 * 1000;
      this.maxHealthCheckInterval = var5 * 1000;
      this.host = var1;
      this.port = var2;
      this.maxRetries = var4;
      this.env = new Environment();
      this.env.setProviderUrl("t3://" + var1 + ":" + var2);
      TimerManagerFactory.getTimerManagerFactory().getTimerManager("HTTPProxy", WorkManagerFactory.getInstance().getSystem()).schedule(this, (long)var3);
      this.originalHealthCheckInterval = this.healthCheckInterval;
      this.toStringMsg = "Trigger for server running at host " + var1 + " and port " + var2;
   }

   synchronized void setHealthCheckInterval(int var1) {
      if (var1 < this.maxHealthCheckInterval) {
         this.healthCheckInterval = var1;
      } else {
         this.healthCheckInterval = this.maxHealthCheckInterval;
      }

   }

   public final void timerExpired(Timer var1) {
      try {
         if (this.healthCheck == null) {
            this.createConnection();
         }

         this.healthCheck.ping();
         this.reset();
      } catch (NamingException var8) {
         ++this.healthCheckFailureCount;
      } catch (RemoteException var9) {
         ++this.healthCheckFailureCount;
      } finally {
         if (this.healthCheckFailureCount > this.maxRetries) {
            this.healthCheckFailureCount = this.maxRetries;
            if (this.maxHealthCheckInterval != this.healthCheckInterval) {
               this.setHealthCheckInterval(this.maxRetriesCount++ * this.healthCheckInterval / 10 + this.healthCheckInterval);
            }
         } else if (this.healthCheckFailureCount == this.maxRetries && this.serverID != -1) {
            ServerManager.getServerManager().removeServer(this.serverID);
            Debug.say("REMOVED SERVER " + this.serverID);
            this.serverID = -1;
            this.healthCheck = null;
         }

      }

   }

   private void reset() {
      this.healthCheckFailureCount = 0;
      this.maxRetriesCount = 0;
   }

   private void createConnection() throws NamingException, RemoteException {
      this.ctx = this.env.getInitialContext();
      this.healthCheck = (HealthCheck)this.ctx.lookup("HealthCheck");
      this.serverID = this.healthCheck.getServerID();
      ServerManager.getServerManager().addServer(this.serverID, this.host, Integer.parseInt(this.port));
      this.reset();
      this.setHealthCheckInterval(this.originalHealthCheckInterval);
      Debug.say("ADDED SERVER " + this.serverID);
   }

   public String toString() {
      return this.toStringMsg;
   }
}
