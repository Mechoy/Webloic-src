package weblogic.security.SSL.jsseadapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

abstract class JaAbstractLayeredSSLSocket extends JaAbstractSSLSocket {
   private final Socket socket;
   private final boolean autoClose;

   public void connect(SocketAddress var1) throws IOException {
      this.socket.connect(var1);
   }

   public void connect(SocketAddress var1, int var2) throws IOException {
      this.socket.connect(var1, var2);
   }

   public void bind(SocketAddress var1) throws IOException {
      this.socket.bind(var1);
   }

   public InetAddress getInetAddress() {
      return this.socket.getInetAddress();
   }

   public InetAddress getLocalAddress() {
      return this.socket.getLocalAddress();
   }

   public int getPort() {
      return this.socket.getPort();
   }

   public int getLocalPort() {
      return this.socket.getLocalPort();
   }

   public SocketAddress getRemoteSocketAddress() {
      return this.socket.getRemoteSocketAddress();
   }

   public SocketAddress getLocalSocketAddress() {
      return this.socket.getLocalSocketAddress();
   }

   public SocketChannel getChannel() {
      return this.socket.getChannel();
   }

   public void setTcpNoDelay(boolean var1) throws SocketException {
      this.socket.setTcpNoDelay(var1);
   }

   public boolean getTcpNoDelay() throws SocketException {
      return this.socket.getTcpNoDelay();
   }

   public void setSoLinger(boolean var1, int var2) throws SocketException {
      this.socket.setSoLinger(var1, var2);
   }

   public int getSoLinger() throws SocketException {
      return this.socket.getSoLinger();
   }

   public void sendUrgentData(int var1) throws IOException {
      this.socket.sendUrgentData(var1);
   }

   public void setOOBInline(boolean var1) throws SocketException {
      this.socket.setOOBInline(var1);
   }

   public boolean getOOBInline() throws SocketException {
      return this.socket.getOOBInline();
   }

   public void setSoTimeout(int var1) throws SocketException {
      this.socket.setSoTimeout(var1);
   }

   public int getSoTimeout() throws SocketException {
      return this.socket.getSoTimeout();
   }

   public void setSendBufferSize(int var1) throws SocketException {
      this.socket.setSendBufferSize(var1);
   }

   public int getSendBufferSize() throws SocketException {
      return this.socket.getSendBufferSize();
   }

   public void setReceiveBufferSize(int var1) throws SocketException {
      this.socket.setReceiveBufferSize(var1);
   }

   public int getReceiveBufferSize() throws SocketException {
      return this.socket.getReceiveBufferSize();
   }

   public void setKeepAlive(boolean var1) throws SocketException {
      this.socket.setKeepAlive(var1);
   }

   public boolean getKeepAlive() throws SocketException {
      return this.socket.getKeepAlive();
   }

   public void setTrafficClass(int var1) throws SocketException {
      this.socket.setTrafficClass(var1);
   }

   public int getTrafficClass() throws SocketException {
      return this.socket.getTrafficClass();
   }

   public void setReuseAddress(boolean var1) throws SocketException {
      this.socket.setReuseAddress(var1);
   }

   public boolean getReuseAddress() throws SocketException {
      return this.socket.getReuseAddress();
   }

   public void close() throws IOException {
      try {
         super.close();
      } catch (Exception var2) {
      }

      if (this.autoClose) {
         this.socket.close();
      }

   }

   public void shutdownInput() throws IOException {
      try {
         super.shutdownInput();
      } catch (Exception var2) {
      }

      this.socket.shutdownInput();
   }

   public void shutdownOutput() throws IOException {
      try {
         super.shutdownOutput();
      } catch (Exception var2) {
      }

      this.socket.shutdownOutput();
   }

   public boolean isConnected() {
      return this.socket.isConnected();
   }

   public boolean isBound() {
      return this.socket.isBound();
   }

   public boolean isClosed() {
      return this.socket.isClosed();
   }

   public boolean isInputShutdown() {
      return this.socket.isInputShutdown();
   }

   public boolean isOutputShutdown() {
      return this.socket.isOutputShutdown();
   }

   public void setPerformancePreferences(int var1, int var2, int var3) {
      this.socket.setPerformancePreferences(var1, var2, var3);
   }

   public int hashCode() {
      return this.socket.hashCode();
   }

   public boolean equals(Object var1) {
      return this.socket.equals(var1);
   }

   public String toString() {
      return this.socket.toString();
   }

   JaAbstractLayeredSSLSocket(Socket var1, JaSSLContext var2, boolean var3) throws IOException {
      super(var2);
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null Socket.");
      } else {
         this.socket = var1;
         this.autoClose = var3;
      }
   }
}
