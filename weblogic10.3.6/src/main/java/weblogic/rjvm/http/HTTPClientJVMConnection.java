package weblogic.rjvm.http;

import com.bea.security.utils.random.SecureRandomData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import weblogic.common.internal.VersionInfo;
import weblogic.kernel.KernelStatus;
import weblogic.management.commandline.tools.AdminToolHelper;
import weblogic.protocol.OutgoingMessage;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.RJVMEnvironment;
import weblogic.rjvm.RJVMLogger;
import weblogic.rjvm.TransportUtils;
import weblogic.utils.Debug;
import weblogic.utils.io.Chunk;

public class HTTPClientJVMConnection extends MsgAbbrevJVMConnection implements Runnable {
   private static final boolean ASSERT = true;
   private static final boolean DEBUG = false;
   private static final String URL_EXTENSION = "/a.tun";
   private boolean closed;
   private String host;
   private int port;
   private final ServerChannel networkChannel;
   private String connectionID = null;
   private String cookie = null;

   public final ServerChannel getChannel() {
      return this.networkChannel;
   }

   private static void drainStream(InputStream var0) throws IOException {
      if (var0 != null) {
         boolean var1 = false;

         int var2;
         do {
            var2 = var0.read();
         } while(var2 != -1);

         var0.close();
      }

   }

   private static Chunk readPacket(InputStream var0) throws IOException {
      Chunk var1 = Chunk.getChunk();

      try {
         int var2 = Chunk.chunkFully(var1, var0);
         Debug.assertion(var2 > 4);
      } finally {
         var0.close();
      }

      return var1;
   }

   HTTPClientJVMConnection(ServerChannel var1) {
      this.networkChannel = var1;
      this.closed = true;
   }

   URLConnection createURLConnection(URL var1) throws IOException {
      return RJVMEnvironment.getEnvironment().createURLConnection(var1, this.networkChannel);
   }

   public final String toString() {
      return super.toString() + " - id: '" + this.connectionID + "', host: '" + this.host + "', port: '" + this.port + " closed: '" + this.closed + "'";
   }

   public final InetAddress getLocalAddress() {
      return null;
   }

   public final int getLocalPort() {
      return -1;
   }

   private final String getRequestArgs() {
      return "?connectionID=" + this.connectionID + "&rand=" + SecureRandomData.getInstance().getRandomNonNegativeLong();
   }

   private final void handleNullResponse(URLConnection var1) throws ProtocolException {
      throw new ProtocolException("Tunneling result unspecified - is the HTTP server at host: '" + this.host + "' and port: '" + this.port + "' a WebLogic Server?");
   }

   public final synchronized void connect(InetAddress var1, int var2) throws IOException {
      if (!this.closed) {
         throw new ProtocolException("Already connected");
      } else {
         this.host = null;
         this.port = var2;
         this.host = var1.getHostName();
         String var3 = URLEncoder.encode(this.getProtocol().getProtocolName() + " dummy WLREQS " + VersionInfo.theOne().getReleaseVersion() + " dummy \n");
         URL var4 = new URL(this.getProtocol().getProtocolName(), this.host, var2, KernelStatus.getTunellingURL("/bea_wls_internal/HTTPClntLogin") + "/a.tun" + "?wl-login=" + var3 + "&rand=" + SecureRandomData.getInstance().getRandomNonNegativeLong() + "&" + "AS" + "=" + ABBREV_TABLE_SIZE + "&" + "HL" + "=" + 19);

         URLConnection var5;
         try {
            var5 = this.createURLConnection(var4);
         } catch (IOException var14) {
            RJVMLogger.logOpenFailed(var14);
            return;
         }

         var5.setUseCaches(false);
         InputStream var6 = null;

         try {
            var6 = var5.getInputStream();
            int var7 = 1;
            String var8 = null;

            while((var8 = var5.getHeaderFieldKey(var7++)) != null) {
               if (var8.equals("Set-Cookie")) {
                  var8 = var5.getHeaderField(var7 - 1);
                  int var9 = var8.indexOf(";");
                  if (var9 != -1) {
                     var8 = var8.substring(0, var9);
                  }

                  if (this.cookie == null) {
                     this.cookie = var8;
                  } else {
                     this.cookie = this.cookie + "; " + var8;
                  }
               }
            }

            String var16 = var5.getHeaderField("WL-Result");
            if (var16 == null) {
               this.handleNullResponse(var5);
            }

            if (!var16.equals("OK")) {
               throw new ProtocolException("Tunneling result not OK, result: '" + var16 + "'");
            }

            String var10 = var5.getHeaderField("WL-Version");
            if (var10 == null) {
               this.doDownGrade();
            }

            this.connectionID = var5.getHeaderField("Conn-Id");
            if (this.connectionID == null) {
               throw new ProtocolException("Tunneling could not ascertain a connection ID from the server");
            }

            this.readConnectionParams(var6);
            this.closed = false;
         } finally {
            drainStream(var6);
         }

      }
   }

   private void readConnectionParams(InputStream var1) throws IOException {
      TransportUtils.BootstrapResult var2 = TransportUtils.readBootstrapParams(var1);
      if (!var2.isSuccess()) {
         throw new ProtocolException("Invalid parameter: " + var2.getInvalidLine());
      } else {
         this.init(var2.getAbbrevSize(), var2.getHeaderLength());
      }
   }

   public final synchronized void run() {
      try {
         while(!this.closed) {
            this.receiveAndDispatch();
         }

      } catch (ThreadDeath var7) {
         ThreadDeath var1 = var7;

         try {
            this.gotExceptionReceiving(var1);
            this.close();
         } finally {
            ;
         }

         throw var7;
      } catch (Throwable var8) {
         if (AdminToolHelper.shutdownCommand) {
            AdminToolHelper.shutdownCommand = false;
         } else {
            RJVMLogger.logExecuteFailed(var8);
         }

         this.gotExceptionReceiving(var8);
         this.close();
      }
   }

   public final void sendMsg(OutgoingMessage var1) throws IOException {
      if (this.closed) {
         throw new IOException("Connection closed");
      } else {
         URL var2 = new URL(this.getProtocol().getProtocolName(), this.host, this.port, KernelStatus.getTunellingURL("/bea_wls_internal/HTTPClntSend") + "/a.tun" + this.getRequestArgs());
         URLConnection var3 = this.createURLConnection(var2);
         var3.setUseCaches(false);
         var3.setRequestProperty("Content-Type", "application/octet-stream");
         if (this.cookie != null) {
            var3.setRequestProperty("Cookie", this.cookie);
         }

         InputStream var4 = null;

         try {
            var3.setDoOutput(true);
            OutputStream var5 = var3.getOutputStream();
            var1.writeTo(var5);
            var5.flush();
            var4 = var3.getInputStream();
            String var6 = var3.getHeaderField("WL-Result");

            try {
               if (var6 == null) {
                  this.handleNullResponse(var3);
               }

               if (!var6.equals("OK")) {
                  throw new ProtocolException("Tunneling result not OK, result: '" + var6 + "', id: '" + this.connectionID + "'");
               }
            } catch (ProtocolException var11) {
               throw var11;
            }
         } finally {
            drainStream(var4);
         }

      }
   }

   public final void close() {
      if (!this.closed) {
         this.closed = true;
      }
   }

   private final synchronized void receiveAndDispatch() throws IOException {
      if (!this.closed) {
         while(!this.closed) {
            URL var3 = new URL(this.getProtocol().getProtocolName(), this.host, this.port, KernelStatus.getTunellingURL("/bea_wls_internal/HTTPClntRecv") + "/a.tun" + this.getRequestArgs());
            URLConnection var4 = this.createURLConnection(var3);
            var4.setUseCaches(false);
            if (this.cookie != null) {
               var4.setRequestProperty("Cookie", this.cookie);
            }

            InputStream var1 = var4.getInputStream();
            String var2 = var4.getHeaderField("WL-Result");
            if (var2 == null) {
               this.handleNullResponse(var4);
            }

            if (var2.equals("RETRY")) {
               var1.close();
            } else {
               if (!var2.equals("OK")) {
                  throw new ProtocolException("Tunneling result not OK, result: '" + var2 + "', id: '" + this.connectionID + "'");
               }

               super.dispatch(readPacket(var1));
            }
         }

      }
   }
}
