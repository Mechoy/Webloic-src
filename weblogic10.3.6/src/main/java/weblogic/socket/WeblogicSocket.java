package weblogic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public abstract class WeblogicSocket extends Socket {
   private final Socket socket;

   public WeblogicSocket(Socket var1) {
      this.socket = var1;
   }

   public final InetAddress getInetAddress() {
      return this.socket.getInetAddress();
   }

   public final InetAddress getLocalAddress() {
      return this.socket.getLocalAddress();
   }

   public final int getPort() {
      return this.socket.getPort();
   }

   public final int getLocalPort() {
      return this.socket.getLocalPort();
   }

   public InputStream getInputStream() throws IOException {
      return this.socket.getInputStream();
   }

   public OutputStream getOutputStream() throws IOException {
      return this.socket.getOutputStream();
   }

   public final void setTcpNoDelay(boolean var1) throws SocketException {
      this.socket.setTcpNoDelay(var1);
   }

   public final boolean getTcpNoDelay() throws SocketException {
      return this.socket.getTcpNoDelay();
   }

   public final void setSoLinger(boolean var1, int var2) throws SocketException {
      this.socket.setSoLinger(var1, var2);
   }

   public final int getSoLinger() throws SocketException {
      return this.socket.getSoLinger();
   }

   public final void setSoTimeout(int var1) throws SocketException {
      this.socket.setSoTimeout(var1);
   }

   public final int getSoTimeout() throws SocketException {
      return this.socket.getSoTimeout();
   }

   public void close() throws IOException {
      this.socket.close();
   }

   public final Socket getSocket() {
      return this.socket;
   }

   public final String toString() {
      return this.socket.toString();
   }

   public final void setSendBufferSize(int var1) throws SocketException {
      this.socket.setSendBufferSize(var1);
   }

   public final int getSendBufferSize() throws SocketException {
      return this.socket.getSendBufferSize();
   }

   public final void setReceiveBufferSize(int var1) throws SocketException {
      this.socket.setReceiveBufferSize(var1);
   }

   public final int getReceiveBufferSize() throws SocketException {
      return this.socket.getReceiveBufferSize();
   }

   public final void setKeepAlive(boolean var1) throws SocketException {
      this.socket.setKeepAlive(var1);
   }

   public final boolean getKeepAlive() throws SocketException {
      return this.socket.getKeepAlive();
   }

   public final void shutdownInput() throws IOException {
      this.socket.shutdownInput();
   }

   public final void shutdownOutput() throws IOException {
      this.socket.shutdownOutput();
   }

   public final void connect(SocketAddress var1) throws IOException {
      this.socket.connect(var1);
   }

   public final void connect(SocketAddress var1, int var2) throws IOException {
      this.socket.connect(var1, var2);
   }

   public final void bind(SocketAddress var1) throws IOException {
      this.socket.bind(var1);
   }

   public final SocketAddress getRemoteSocketAddress() {
      return this.socket.getRemoteSocketAddress();
   }

   public final SocketAddress getLocalSocketAddress() {
      return this.socket.getLocalSocketAddress();
   }

   public final SocketChannel getChannel() {
      return this.socket.getChannel();
   }

   public final void sendUrgentData(int var1) throws IOException {
      this.socket.sendUrgentData(var1);
   }

   public final void setOOBInline(boolean var1) throws SocketException {
      this.socket.setOOBInline(var1);
   }

   public final boolean getOOBInline() throws SocketException {
      return this.socket.getOOBInline();
   }

   public final void setTrafficClass(int var1) throws SocketException {
      this.socket.setTrafficClass(var1);
   }

   public final int getTrafficClass() throws SocketException {
      return this.socket.getTrafficClass();
   }

   public final void setReuseAddress(boolean var1) throws SocketException {
      this.socket.setReuseAddress(var1);
   }

   public final boolean getReuseAddress() throws SocketException {
      return this.socket.getReuseAddress();
   }

   public final boolean isConnected() {
      return this.socket.isConnected();
   }

   public final boolean isBound() {
      return this.socket.isBound();
   }

   public final boolean isClosed() {
      return this.socket.isClosed();
   }

   public final boolean isInputShutdown() {
      return this.socket.isInputShutdown();
   }

   public final boolean isOutputShutdown() {
      return this.socket.isOutputShutdown();
   }
}
