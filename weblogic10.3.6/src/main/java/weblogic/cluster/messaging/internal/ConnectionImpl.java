package weblogic.cluster.messaging.internal;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import weblogic.utils.io.ChunkedDataOutputStream;

public class ConnectionImpl implements Connection {
   protected static final boolean DEBUG;
   private Socket socket;
   private boolean isDead;
   private ServerConfigurationInformation info;
   private String serverId;
   private static final String delimiter = "-";

   public ConnectionImpl(ServerConfigurationInformation var1) {
      this.info = var1;
      if (var1 != null && var1.getCreationTime() > 1L) {
         this.serverId = var1.getServerName() + "-" + var1.getCreationTime();
      }

   }

   public ConnectionImpl(Socket var1) throws IOException {
      this.socket = var1;
      this.socket.setTcpNoDelay(true);
   }

   protected OutputStream getOutputStream() throws IOException {
      try {
         return this.socket.getOutputStream();
      } catch (IOException var2) {
         this.close();
         throw var2;
      }
   }

   public final boolean isDead() {
      return this.isDead;
   }

   public final ServerConfigurationInformation getConfiguration() {
      return this.info;
   }

   public void send(Message var1) throws IOException {
      ChunkedDataOutputStream var2 = new ChunkedDataOutputStream();
      ObjectOutputStream var3 = null;

      try {
         this.skipHeader(var2);
         var3 = new ObjectOutputStream(var2);
         var3.writeObject(var1);
         int var4 = var2.getSize();
         if (DEBUG) {
            this.debug("writing length " + var4);
         }

         var2.setPosition(0);
         this.writeHeader(var2);
         var2.writeInt(var4);
         if (DEBUG) {
            this.debug("writing data " + var2.getBuffer());
         }

         var2.writeTo(this.getOutputStream());
      } catch (IOException var8) {
         this.close();
         throw var8;
      } finally {
         if (var3 != null) {
            this.close(var3);
         }

      }

   }

   private void close(Closeable var1) {
      try {
         var1.close();
      } catch (IOException var3) {
      }

   }

   protected void skipHeader(ChunkedDataOutputStream var1) {
      var1.skip(Message.HEADER_LENGTH);
   }

   protected void writeHeader(ChunkedDataOutputStream var1) {
      var1.writeBytes("CLUSTER-BROADCAST");
   }

   protected void debug(String var1) {
      Environment.getLogService().debug("[Connection][" + this.info + "]" + var1);
   }

   public void handleIncomingMessage(InputStream var1) throws IOException {
      if (DEBUG) {
         this.debug("reading message from input stream ...");
      }

      ObjectInputStream var2 = null;

      final Message var3;
      try {
         var2 = new ObjectInputStream(var1);
         var3 = (Message)var2.readObject();
      } catch (ClassNotFoundException var8) {
         throw new AssertionError(var8);
      } finally {
         if (var2 != null) {
            this.close(var2);
         }

      }

      if (DEBUG) {
         this.debug("received message: " + var3);
      }

      if (this.info == null) {
         this.info = var3.getForwardingServer();
      }

      this.serverId = var3.getForwardingServer().getServerName() + "-" + var3.getForwardingServer().getCreationTime();
      if (DEBUG) {
         this.debug("dispatching to group manager with serverId: " + this.serverId);
      }

      Environment.executeDispatchMessage(new Runnable() {
         public void run() {
            Environment.getGroupManager().handleMessage(var3, ConnectionImpl.this);
         }
      });
   }

   public String getServerId() {
      return this.serverId;
   }

   public void close() {
      this.isDead = true;

      try {
         if (this.socket != null) {
            this.socket.close();
         }
      } catch (IOException var2) {
      }

   }

   public final void setSocket(Socket var1) throws IOException {
      this.socket = var1;
      this.socket.setTcpNoDelay(true);
   }

   static {
      DEBUG = Environment.DEBUG;
   }
}
