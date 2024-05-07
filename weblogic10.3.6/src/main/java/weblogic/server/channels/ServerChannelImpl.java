package weblogic.server.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import weblogic.kernel.KernelStatus;
import weblogic.kernel.NetworkAccessPointMBeanStub;
import weblogic.management.ManagementException;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerChannelRuntimeMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.UnknownProtocolException;
import weblogic.protocol.configuration.NetworkAccessPointDefaultMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;

public final class ServerChannelImpl extends BasicServerChannelImpl {
   static final long serialVersionUID = 3682806476156685669L;
   private static final boolean DEBUG = false;
   private transient ServerChannelRuntimeMBean runtime;

   public ServerChannelImpl() {
   }

   public ServerChannelRuntimeMBean getRuntime() {
      return this.runtime;
   }

   final synchronized ServerChannelRuntimeMBean createRuntime() throws ManagementException {
      Debug.assertion(this.runtime == null);
      this.runtime = new ServerChannelRuntimeMBeanImpl(this);
      return this.runtime;
   }

   final ServerChannelRuntimeMBean deleteRuntime() {
      Debug.assertion(this.runtime != null);
      ServerChannelRuntimeMBeanImpl var1 = (ServerChannelRuntimeMBeanImpl)this.runtime;
      this.runtime = null;

      try {
         var1.unregister();
      } catch (ManagementException var3) {
      }

      return var1;
   }

   ServerChannelImpl(NetworkAccessPointMBean var1, ServerIdentity var2) throws UnknownHostException, UnknownProtocolException {
      this(var1, ProtocolManager.findProtocol(var1.getProtocol()), var1.getName(), var2, (String)null);
   }

   public ServerChannelImpl(NetworkAccessPointMBean var1, Protocol var2, ServerIdentity var3) throws UnknownHostException {
      this(var1, var2, encodeName(var2, var1.getName()), var3, (String)null);
      this.displayName = var1.getName();
   }

   private ServerChannelImpl(NetworkAccessPointMBean var1, Protocol var2, String var3, ServerIdentity var4, String var5) throws UnknownHostException {
      super(var1, var2, var3, var4, var5);
   }

   ServerChannelImpl(ServerChannelImpl var1, String var2, String var3) throws UnknownHostException {
      this.flags = var1.flags;
      this.channelName = var1.channelName + var3;
      if (var1.displayName != null) {
         this.displayName = var1.displayName + var3;
      }

      this.listenPort = var1.listenPort;
      this.publicPort = var1.publicPort;
      this.protocol = var1.protocol;
      this.priority = var1.priority;
      this.weight = var1.weight;
      this.config = var1.config;
      this.implicitChannel = var1.implicitChannel;
      this.isLocal = var1.isLocal;
      if (var1.hasPublicAddress()) {
         this.setPublicAddress(var1.publicAddress);
      }

      this.inetAddress = InetAddress.getByName(var2);
      if (var2.indexOf(":") != -1) {
         var2 = "[" + this.inetAddress.getHostAddress() + "]";
      }

      this.setListenAddress(var2);
      byte[] var4 = this.inetAddress.getAddress();
      this.rawAddress = ((var4[0] & 255) << 24) + ((var4[1] & 255) << 16) + ((var4[2] & 255) << 8) + ((var4[3] & 255) << 0);
      if (this.getPublicPort() >= 0) {
         this.inSockAddress = new InetSocketAddress(this.getPublicAddress(), this.getPublicPort());
      }

   }

   public ServerChannelImpl createImplicitChannel(Protocol var1) {
      return new ServerChannelImpl(this, var1);
   }

   ServerChannelImpl cloneChannel(String var1) {
      ServerChannelImpl var2 = new ServerChannelImpl(this, this.protocol);
      var2.implicitChannel = false;
      var2.channelName = this.channelName + "{" + var1 + "}";
      String var3 = this.displayName != null ? this.displayName : this.channelName;
      var2.displayName = var3 + "{" + var1 + "}";
      return var2;
   }

   private ServerChannelImpl(ServerChannelImpl var1, Protocol var2) {
      this.flags = var1.flags;
      this.displayName = var1.displayName;
      this.listenPort = var1.listenPort;
      this.publicPort = var1.publicPort;
      this.weight = var1.weight;
      this.config = var1.config;
      this.publicAddress = var1.publicAddress;
      this.address = var1.address;
      this.inetAddress = var1.inetAddress;
      this.rawAddress = var1.rawAddress;
      this.inSockAddress = var1.inSockAddress;
      this.implicitChannel = true;
      this.protocol = var2;
      this.isLocal = var1.isLocal;
      this.priority = var2.getHandler().getPriority();
      if (this.displayName == null) {
         this.displayName = var1.channelName;
      }

      this.channelName = encodeName(var2, this.displayName);
   }

   private static void p(String var0) {
      System.out.println("<ServerChannelImpl>: " + var0);
   }

   public static ServerChannel createDefaultServerChannel(Protocol var0) {
      try {
         if (KernelStatus.isServer()) {
            AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            return new ServerChannelImpl(new NetworkAccessPointDefaultMBean(var0, ManagementService.getRuntimeAccess(var1).getServer()), var0, LocalServerIdentity.getIdentity());
         } else {
            return new ServerChannelImpl(new NetworkAccessPointMBeanStub(var0.getProtocolName()), var0, LocalServerIdentity.getIdentity());
         }
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }
}
