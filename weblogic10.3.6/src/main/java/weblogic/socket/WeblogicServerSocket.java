package weblogic.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class WeblogicServerSocket extends ServerSocket {
   private final ServerSocket serverSocket;
   private final boolean nio;

   public WeblogicServerSocket(ServerSocket var1, boolean var2) throws IOException {
      this.serverSocket = var1;
      this.nio = var2;
   }

   public Socket accept() throws IOException {
      Socket var1;
      if (this.nio) {
         SocketChannel var2 = this.serverSocket.getChannel().accept();
         var2.configureBlocking(false);
         var1 = var2.socket();
      } else {
         ServerSocketChannel var3 = this.serverSocket.getChannel();
         if (var3 != null) {
            var1 = var3.accept().socket();
         } else {
            var1 = this.serverSocket.accept();
         }
      }

      return SocketMuxer.getMuxer().newWeblogicSocket(var1);
   }

   public void bind(SocketAddress var1) throws IOException {
      this.serverSocket.bind(var1);
   }

   public void bind(SocketAddress var1, int var2) throws IOException {
      this.serverSocket.bind(var1, var2);
   }

   public InetAddress getInetAddress() {
      return this.serverSocket.getInetAddress();
   }

   public int getLocalPort() {
      return this.serverSocket.getLocalPort();
   }

   public SocketAddress getLocalSocketAddress() {
      return this.serverSocket.getLocalSocketAddress();
   }

   public void close() throws IOException {
      this.serverSocket.close();
   }

   public ServerSocketChannel getChannel() {
      return this.serverSocket.getChannel();
   }

   public boolean isBound() {
      return this.serverSocket.isBound();
   }

   public boolean isClosed() {
      return this.serverSocket.isClosed();
   }

   public void setSoTimeout(int var1) throws SocketException {
      this.serverSocket.setSoTimeout(var1);
   }

   public int getSoTimeout() throws IOException {
      return this.serverSocket.getSoTimeout();
   }

   public void setReuseAddress(boolean var1) throws SocketException {
      this.serverSocket.setReuseAddress(var1);
   }

   public boolean getReuseAddress() throws SocketException {
      return this.serverSocket.getReuseAddress();
   }

   public String toString() {
      return this.serverSocket.toString();
   }

   public void setReceiveBufferSize(int var1) throws SocketException {
      this.serverSocket.setReceiveBufferSize(var1);
   }

   public int getReceiveBufferSize() throws SocketException {
      return this.serverSocket.getReceiveBufferSize();
   }
}
