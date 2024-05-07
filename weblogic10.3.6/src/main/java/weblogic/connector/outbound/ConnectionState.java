package weblogic.connector.outbound;

public class ConnectionState {
   private boolean connectionClosed = false;
   private boolean connectionFinalized = false;

   public synchronized void setConnectionClosed(boolean var1) {
      this.connectionClosed = var1;
   }

   public synchronized void setConnectionFinalized(boolean var1) {
      this.connectionFinalized = var1;
   }

   public synchronized boolean isConnectionClosed() {
      return this.connectionClosed;
   }

   public synchronized boolean isConnectionFinalized() {
      return this.connectionFinalized;
   }
}
