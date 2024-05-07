package weblogic.server.channels;

import java.io.Serializable;
import weblogic.management.runtime.SocketRuntime;

public class SocketRuntimeImpl implements SocketRuntime, Serializable {
   private final int fd;
   private final String localAddress;
   private final int localPort;
   private final String remoteAddress;
   private final int remotePort;

   public SocketRuntimeImpl(SocketRuntime var1) {
      this.fd = var1.getFileDescriptor();
      this.remoteAddress = var1.getRemoteAddress();
      this.localAddress = var1.getLocalAddress();
      this.remotePort = var1.getRemotePort();
      this.localPort = var1.getLocalPort();
   }

   public int getFileDescriptor() {
      return this.fd;
   }

   public String getLocalAddress() {
      return this.localAddress;
   }

   public int getLocalPort() {
      return this.localPort;
   }

   public String getRemoteAddress() {
      return this.remoteAddress;
   }

   public int getRemotePort() {
      return this.remotePort;
   }
}
