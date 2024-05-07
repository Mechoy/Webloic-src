package weblogic.rjvm.t3.client;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.StringTokenizer;
import weblogic.common.T3Exception;
import weblogic.common.internal.VersionInfo;
import weblogic.protocol.OutgoingMessage;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.rjvm.MsgAbbrevJVMConnection;
import weblogic.rjvm.TransportUtils;
import weblogic.security.service.ContextHandler;
import weblogic.socket.AsyncOutputStream;
import weblogic.socket.BaseAbstractMuxableSocket;
import weblogic.socket.Login;
import weblogic.socket.MaxMessageSizeExceededException;
import weblogic.socket.NIOConnection;
import weblogic.socket.SocketMuxer;
import weblogic.socket.UnrecoverableConnectException;
import weblogic.utils.StringUtils;
import weblogic.utils.io.Chunk;

public class MuxableSocketT3 extends BaseAbstractMuxableSocket implements AsyncOutputStream {
   private static final long serialVersionUID = -3990131100112713491L;
   private static final byte[] CONNECT_PARAMS;
   private static final String MALFORMED_FIRST_LINE = "Malformed first line\nAre you trying to connect to a standard port using SSL or vice versa?";
   private static final boolean ASSERT = false;
   private static final int INITIAL_SO_TIMEOUT = 60000;
   private static final int HEADER_SIZE_LIMIT = 512;
   private static final int CONNECT_MAX_RETRY = 1;
   private static final int CONNECT_BACKOFF_INTERVAL = 1000;
   private boolean bootstrapped;
   protected final T3MsgAbbrevJVMConnection connection = new T3MsgAbbrevJVMConnection();
   private Chunk sendHead;
   private IOException sendException;

   protected MuxableSocketT3(ServerChannel var1) {
      super(var1);
   }

   private void readBootstrapMessage(Chunk var1) throws IOException {
      String var2 = new String(var1.buf);
      StringTokenizer var3 = new StringTokenizer(var2, "\n");
      if (!var3.hasMoreTokens()) {
         this.rejectConnection(1, "No version information");
      }

      String var4 = var3.nextToken();
      String[] var5 = StringUtils.splitCompletely(var4, " \t");
      String var6 = null;
      if (var5.length == 2) {
         var6 = var5[1];
      } else if (var5.length > 3) {
         var6 = var5[3];
      } else {
         this.rejectConnection(1, "Malformed first line\nAre you trying to connect to a standard port using SSL or vice versa?");
      }

      if (!VersionInfo.theOne().compatible(var6)) {
         this.rejectConnection(6, VersionInfo.theOne().rejectionReason(var6));
      }

      if (!var3.hasMoreTokens()) {
         this.rejectConnection(1, "Invalid parameter.");
      }

      var4 = var3.nextToken();
      T3MsgAbbrevJVMConnection var10000 = this.connection;
      int var7 = MuxableSocketT3.T3MsgAbbrevJVMConnection.ABBREV_TABLE_SIZE;
      char var12 = var4.charAt(0);
      T3MsgAbbrevJVMConnection var10001 = this.connection;
      int var8;
      if (var12 == "AS".charAt(0)) {
         var12 = var4.charAt(1);
         var10001 = this.connection;
         if (var12 == "AS".charAt(1)) {
            try {
               var8 = Integer.parseInt(var4.substring(var4.indexOf(58) + 1, var4.length()));
               var7 = Math.min(var7, var8);
            } catch (Exception var11) {
               this.rejectConnection(1, "Invalid parameter: " + var4);
            }
         }
      }

      if (!var3.hasMoreTokens()) {
         this.rejectConnection(1, "Invalid parameter.");
      }

      var4 = var3.nextToken();
      var8 = 19;
      var12 = var4.charAt(0);
      var10001 = this.connection;
      if (var12 == "HL".charAt(0)) {
         var12 = var4.charAt(1);
         var10001 = this.connection;
         if (var12 == "HL".charAt(1)) {
            try {
               var8 = Integer.parseInt(var4.substring(var4.indexOf(58) + 1, var4.length()));
            } catch (Exception var10) {
               this.rejectConnection(1, "Invalid parameter: " + var4);
            }
         }
      }

      this.connection.init(var7, var8);
      Login.connectReplyOK(this.getSocket(), CONNECT_PARAMS, VersionInfo.theOne());
   }

   private void readConnectionParams(InputStream var1) throws IOException {
      TransportUtils.BootstrapResult var2 = TransportUtils.readBootstrapParams(var1);
      if (!var2.isSuccess()) {
         this.rejectConnection(1, "Invalid parameter: " + var2.getInvalidLine());
      }

      this.connection.init(var2.getAbbrevSize(), var2.getHeaderLength());
   }

   private Socket newSocketWithRetry(InetAddress var1, int var2, int var3) throws IOException {
      int var4 = 0;

      while(true) {
         try {
            return this.createSocket(var1, var2, var3);
         } catch (SocketException var8) {
            if (var4 == 1) {
               throw var8;
            }

            try {
               Thread.sleep((long)(Math.random() * (double)(1000 << var4)));
            } catch (InterruptedException var7) {
            }

            ++var4;
         }
      }
   }

   public OutputStream getOutputStream() {
      return this.getSocketOutputStream();
   }

   public final Chunk getOutputBuffer() {
      return this.sendHead;
   }

   public final void handleException(IOException var1) {
      this.sendException = var1;
   }

   public final void handleWrite(Chunk var1) {
      this.sendHead = var1.next;
      Chunk.releaseChunk(var1);
   }

   private boolean canReadFirstMessage() {
      int var1 = this.getAvailableBytes();
      boolean var2 = false;

      for(int var3 = 0; var3 < var1 - 1; ++var3) {
         if (var3 > 512) {
            return false;
         }

         if (this.getHeaderByte(var3) == 10 && this.getHeaderByte(var3 + 1) == 10) {
            var2 = true;
            break;
         }
      }

      return var2;
   }

   protected int getHeaderLength() {
      return !this.bootstrapped ? 2 : 4;
   }

   protected int getMessageLength() {
      if (!this.bootstrapped) {
         return this.canReadFirstMessage() ? this.getAvailableBytes() : -1;
      } else {
         int var1 = this.getHeaderByte(0) & 255;
         int var2 = this.getHeaderByte(1) & 255;
         int var3 = this.getHeaderByte(2) & 255;
         int var4 = this.getHeaderByte(3) & 255;
         return var1 << 24 | var2 << 16 | var3 << 8 | var4;
      }
   }

   public final int getIdleTimeoutMillis() {
      return 0;
   }

   public final void dispatch(Chunk var1) {
      if (!this.bootstrapped) {
         try {
            this.readBootstrapMessage(var1);
            this.bootstrapped = true;
         } catch (IOException var3) {
            SocketMuxer.getMuxer().deliverHasException(this.getSocketFilter(), var3);
         }
      } else {
         this.connection.dispatch(var1);
      }

   }

   public final void hasException(Throwable var1) {
      this.connection.gotExceptionReceiving(var1);
      super.hasException(var1);
   }

   public final boolean timeout() {
      this.connection.gotExceptionReceiving(new EOFException("Connection timed out"));
      return super.timeout();
   }

   public final void endOfStream() {
      this.connection.gotExceptionReceiving(new EOFException());
      super.endOfStream();
   }

   public void incrementBufferOffset(int var1) throws MaxMessageSizeExceededException {
      super.incrementBufferOffset(var1);
      if (var1 > 0 && this.getConnection().getDispatcher() != null) {
         this.getConnection().getDispatcher().messageReceived();
      }

   }

   private void rejectConnection(int var1, String var2) throws T3Exception, IOException {
      Login.connectReply(this.getSocket(), var1, var2);
      this.close();
      throw new T3Exception(var2);
   }

   public MsgAbbrevJVMConnection getConnection() {
      return this.connection;
   }

   public final void connect(InetAddress var1, int var2) throws UnrecoverableConnectException, IOException, UnknownHostException {
      this.connect(var1, var2, 0);
   }

   public final void connect(InetAddress var1, int var2, int var3) throws UnrecoverableConnectException, IOException, UnknownHostException {
      this.connect(this.newSocketWithRetry(var1, var2, var3));
      this.setSoTimeout(60000);
      DataOutputStream var4 = new DataOutputStream(new BufferedOutputStream(this.getSocketOutputStream()));
      var4.writeBytes(this.getRealProtocolName() + " " + VersionInfo.theOne().getReleaseVersion() + "\n");
      var4.write(CONNECT_PARAMS);
      var4.flush();
      DataInputStream var5 = new DataInputStream(this.getSocketInputStream());
      String var6 = var5.readLine();
      String var7 = Login.checkLoginSuccess(var6);
      if (var7 != null) {
         this.close();
         throw new IOException(var7);
      } else {
         String var8 = Login.getVersionString(var6);
         if (var8 == null) {
            this.connection.doDownGrade();
         }

         this.readConnectionParams(var5);
         this.bootstrapped = true;
      }
   }

   private String getRealProtocolName() {
      return ProtocolManager.getRealProtocol(this.getProtocol()).getProtocolName().toLowerCase(Locale.ENGLISH);
   }

   protected X509Certificate[] getJavaCertChain() {
      return null;
   }

   public boolean supportsGatheringWrite() {
      return false;
   }

   public long write(NIOConnection var1) throws IOException {
      throw new UnsupportedOperationException();
   }

   static {
      CONNECT_PARAMS = ("AS:" + MsgAbbrevJVMConnection.ABBREV_TABLE_SIZE + "\n" + "HL" + ":" + 19 + "\n\n").getBytes();
   }

   protected class T3MsgAbbrevJVMConnection extends MsgAbbrevJVMConnection {
      private T3MsgAbbrevJVMConnection() {
      }

      public final InetAddress getLocalAddress() {
         return MuxableSocketT3.this.getSocket().getLocalAddress();
      }

      public final int getLocalPort() {
         return MuxableSocketT3.this.getSocket().getLocalPort();
      }

      public final ServerChannel getChannel() {
         return MuxableSocketT3.this.getChannel();
      }

      public final ContextHandler getContextHandler() {
         return MuxableSocketT3.this;
      }

      public final void connect(InetAddress var1, int var2) throws UnrecoverableConnectException, IOException, UnknownHostException {
         MuxableSocketT3.this.connect(var1, var2);
      }

      public final void sendMsg(OutgoingMessage var1) throws IOException {
         if (MuxableSocketT3.this.isClosed()) {
            throw new IOException("Attempt to send message on closed socket");
         } else {
            MuxableSocketT3.this.sendHead = var1.getChunks();
            SocketMuxer.getMuxer().write(MuxableSocketT3.this);
            if (MuxableSocketT3.this.sendException != null) {
               throw MuxableSocketT3.this.sendException;
            }
         }
      }

      public final void close() {
         SocketMuxer.getMuxer().closeSocket(MuxableSocketT3.this.getSocketFilter());
      }

      public final X509Certificate[] getJavaCertChain() {
         return MuxableSocketT3.this.getJavaCertChain();
      }

      // $FF: synthetic method
      T3MsgAbbrevJVMConnection(Object var2) {
         this();
      }
   }
}
