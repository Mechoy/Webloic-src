package weblogic.servlet.proxy;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import weblogic.common.ResourceException;
import weblogic.common.resourcepool.PooledResource;
import weblogic.common.resourcepool.PooledResourceInfo;
import weblogic.common.resourcepool.ResourceCleanupHandler;

public class SocketConnResource implements PooledResource {
   private final String host;
   private final int port;
   private Socket conn;
   private long creationTime;
   private boolean used;
   private final String url;
   private PooledResourceInfo prInfo;

   SocketConnResource(String var1, int var2) {
      this.host = var1;
      this.port = var2;
      this.url = "Host:" + var1 + ":" + var2;
   }

   public PooledResourceInfo getPooledResourceInfo() {
      return this.prInfo;
   }

   public void setPooledResourceInfo(PooledResourceInfo var1) {
      this.prInfo = var1;
   }

   public void initialize() {
      this.createConnection();
   }

   public void setup() {
   }

   public void enable() {
      if (this.conn == null) {
         this.createConnection();
      }

   }

   public void disable() {
      this.destroy();
   }

   public void cleanup() throws ResourceException {
      this.destroy();
   }

   public void destroy() {
      if (this.conn != null) {
         try {
            this.conn.close();
            this.conn = null;
            this.creationTime = 0L;
         } catch (IOException var2) {
         }
      }

   }

   public int test() throws ResourceException {
      return 0;
   }

   public long getCreationTime() throws ResourceException {
      return this.creationTime;
   }

   public void setResourceCleanupHandler(ResourceCleanupHandler var1) {
   }

   public ResourceCleanupHandler getResourceCleanupHandler() {
      return null;
   }

   public void setUsed(boolean var1) {
      this.used = var1;
   }

   public boolean getUsed() {
      return this.used;
   }

   public String getGroupId() {
      return null;
   }

   public boolean needDestroyAfterRelease() {
      return false;
   }

   public void setDestroyAfterRelease() {
   }

   private void createConnection() {
      try {
         this.conn = new Socket(this.host, this.port);
         this.creationTime = System.currentTimeMillis();
         this.conn.setTcpNoDelay(true);
      } catch (UnknownHostException var2) {
      } catch (IOException var3) {
      }

   }

   final PrintStream getOutputStream() throws IOException {
      if (this.conn == null || this.conn.isClosed()) {
         this.createConnection();
      }

      return new PrintStream(this.conn.getOutputStream(), true);
   }

   final PushbackInputStream getInputStream() throws IOException {
      if (this.conn == null || this.conn.isClosed()) {
         this.createConnection();
      }

      return new PushbackInputStream(this.conn.getInputStream());
   }

   final String getHost() {
      return this.url;
   }
}
