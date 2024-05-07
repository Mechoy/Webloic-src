package weblogic.server.channels;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import weblogic.kernel.NetworkAccessPointMBeanStub;
import weblogic.management.configuration.NetworkAccessPointMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.Protocol;
import weblogic.protocol.ProtocolManager;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerIdentity;
import weblogic.protocol.UnknownProtocolException;
import weblogic.rmi.spi.Channel;

public class BasicServerChannelImpl implements Externalizable, ServerChannel, Comparable {
   static final long serialVersionUID = 3682806476156685669L;
   private static final boolean DEBUG = false;
   protected static final byte HAS_PUBLIC_ADDRESS = 1;
   protected static final byte HAS_LISTEN_PORT = 2;
   protected static final byte HAS_PUBLIC_PORT = 4;
   protected static final byte HAS_LISTEN_ADDRESS = 8;
   protected static final byte SUPPORTS_HTTP = 16;
   protected static final byte SUPPORTS_TLS = 32;
   protected static final byte SUPPORTS_SDP = 64;
   private static final InetAddress LOCALHOST = getLocalHost();
   protected byte flags;
   protected String address;
   private String resolvedAddress;
   protected String channelName;
   protected int listenPort;
   protected int publicPort;
   protected String publicAddress;
   protected int rawAddress;
   protected Protocol protocol;
   protected int priority;
   protected int weight;
   protected transient InetAddress inetAddress;
   protected transient InetSocketAddress inSockAddress;
   protected transient NetworkAccessPointMBean config;
   protected transient boolean implicitChannel;
   protected transient boolean isLocal;
   protected transient String displayName;
   private transient boolean t3SenderQueueDisabled;

   private static final InetAddress getLocalHost() {
      try {
         return InetAddress.getLocalHost();
      } catch (UnknownHostException var2) {
         AssertionError var1 = new AssertionError("Could not obtain the localhost address. The most likely cause is an error in the network configuration of this machine.");
         var1.initCause(var2);
         throw var1;
      }
   }

   public BasicServerChannelImpl() {
      this.listenPort = -1;
      this.publicPort = -1;
   }

   public int hashCode() {
      return this.channelName.hashCode() ^ this.rawAddress ^ this.listenPort ^ this.protocol.getAsURLPrefix().hashCode();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof BasicServerChannelImpl)) {
         return false;
      } else {
         BasicServerChannelImpl var2 = (BasicServerChannelImpl)var1;
         return var2.channelName.equals(this.channelName) && var2.rawAddress == this.rawAddress && var2.listenPort == this.listenPort && var2.protocol.getAsURLPrefix().equals(this.protocol.getAsURLPrefix());
      }
   }

   public boolean requiresNAT() {
      return this.hasPublicAddress() || this.hasPublicPort();
   }

   public boolean supportsTLS() {
      return (this.flags & 32) != 0;
   }

   boolean isImplicitChannel() {
      return this.implicitChannel;
   }

   boolean isLocal() {
      return this.isLocal;
   }

   public boolean supportsHttp() {
      return (this.flags & 16) != 0;
   }

   public NetworkAccessPointMBean getConfig() {
      return this.config;
   }

   public final String getAddress() {
      return this.address;
   }

   public final String getResolvedAddress() {
      if (this.resolvedAddress == null) {
         if (this.isLocal) {
            assert this.inetAddress != null;

            this.resolvedAddress = this.inetAddress.getHostAddress();
         } else {
            this.resolvedAddress = this.address == null ? this.publicAddress : this.address;
         }
      }

      return this.resolvedAddress;
   }

   public final int getRawAddress() {
      return this.rawAddress;
   }

   public String getChannelName() {
      return this.channelName;
   }

   public int getChannelWeight() {
      return this.weight;
   }

   public String getPublicAddress() {
      return this.publicAddress != null ? this.publicAddress : this.getAddress();
   }

   public int getPublicPort() {
      return this.hasPublicPort() ? this.publicPort : this.listenPort;
   }

   public int getPort() {
      return this.listenPort;
   }

   public Protocol getProtocol() {
      return this.protocol;
   }

   public String getProtocolPrefix() {
      return this.protocol.getAsURLPrefix();
   }

   public String getProtocolName() {
      return this.protocol.getProtocolName();
   }

   /** @deprecated */
   public final InetAddress address() {
      return this.getInetAddress();
   }

   public InetSocketAddress getPublicInetAddress() {
      return this.inSockAddress;
   }

   public final String getClusterAddress() {
      String var1 = this.getConfig().getClusterAddress();
      return var1 == null ? this.getPublicAddress() : var1;
   }

   public InetAddress getInetAddress() {
      return this.inetAddress;
   }

   public String getListenerKey() {
      return this.getResolvedAddress().toLowerCase() + this.getPort();
   }

   public boolean hasPublicAddress() {
      return (this.flags & 1) != 0;
   }

   protected void setPublicAddress(String var1) {
      if (var1 != null && var1.length() > 0 && !var1.equals(this.address)) {
         this.publicAddress = var1;
         this.flags = (byte)(this.flags | 1);
      }

   }

   protected boolean hasListenAddress() {
      return (this.flags & 8) != 0;
   }

   protected void setListenAddress(String var1) {
      if (var1 != null && var1.length() > 0) {
         this.address = var1;
         this.flags = (byte)(this.flags | 8);
      }

   }

   protected boolean hasListenPort() {
      return (this.flags & 2) != 0;
   }

   protected void setListenPort(int var1) {
      if (var1 != -1) {
         this.listenPort = var1;
         this.flags = (byte)(this.flags | 2);
      }

   }

   protected boolean hasPublicPort() {
      return (this.flags & 4) != 0;
   }

   protected void setPublicPort(int var1) {
      if (var1 != -1 && var1 != this.listenPort) {
         this.publicPort = var1;
         this.flags = (byte)(this.flags | 4);
      }

   }

   public boolean isSDPEnabled() {
      return (this.flags & 64) != 0;
   }

   protected void setSDPEnabled(boolean var1) {
      if (var1) {
         this.flags = (byte)(this.flags | 64);
      } else {
         this.flags &= -65;
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(this.channelName);
      var1.append(':').append(this.getProtocol().getProtocolName()).append('(').append(this.getProtocol().getAsURLPrefix()).append(')');
      var1.append(':').append(this.address);
      var1.append(":" + this.listenPort);
      var1.append(':').append(this.publicAddress);
      var1.append(":" + this.publicPort);
      return var1.toString();
   }

   public int compareTo(Object var1) {
      BasicServerChannelImpl var2 = (BasicServerChannelImpl)var1;
      int var3 = this.priority - var2.priority;
      if (var3 == 0 && this.isImplicitChannel() != var2.isImplicitChannel()) {
         var3 = this.isImplicitChannel() ? 1 : -1;
      }

      if (var3 == 0) {
         NetworkAccessPointMBean var4 = var2.getConfig();
         if (this.config.isOutboundEnabled() && !var4.isOutboundEnabled()) {
            var3 = -1;
         } else if (!this.config.isOutboundEnabled() && var4.isOutboundEnabled()) {
            var3 = 1;
         }
      }

      boolean var5;
      if (var3 == 0) {
         var5 = this.getChannelName().startsWith("Default");
         if (var5 != var2.getChannelName().startsWith("Default")) {
            var3 = var5 ? -1 : 1;
         }
      }

      if (var3 == 0) {
         var5 = this.requiresNAT();
         if (var5 != var2.requiresNAT()) {
            var3 = var5 ? 1 : -1;
         }
      }

      if (var3 == 0) {
         var5 = this.address() != null && this.address().isLoopbackAddress();
         if (var5 != (var2.address() != null && var2.address().isLoopbackAddress())) {
            var3 = var5 ? 1 : -1;
         }
      }

      if (var3 == 0) {
         var3 = this.getChannelName().compareTo(var2.getChannelName());
      }

      return var3;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(this.flags);
      var1.writeUTF(this.channelName);
      if (this.hasListenAddress()) {
         var1.writeUTF(this.address);
      }

      var1.writeInt(this.rawAddress);
      var1.writeObject(this.protocol);
      var1.writeInt(this.priority);
      var1.writeInt(this.weight);
      if (this.hasPublicAddress() || !this.hasListenAddress()) {
         var1.writeUTF(this.publicAddress);
      }

      if (this.hasListenPort()) {
         var1.writeInt(this.listenPort);
      }

      if (this.hasPublicPort()) {
         var1.writeInt(this.publicPort);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.flags = var1.readByte();
      this.channelName = var1.readUTF();
      if (this.hasListenAddress()) {
         this.address = var1.readUTF();
      }

      this.rawAddress = var1.readInt();
      this.protocol = (Protocol)var1.readObject();
      this.priority = var1.readInt();
      this.weight = var1.readInt();
      if (this.hasPublicAddress() || !this.hasListenAddress()) {
         this.publicAddress = var1.readUTF();
      }

      if (this.hasListenPort()) {
         this.listenPort = var1.readInt();
      }

      if (this.hasPublicPort()) {
         this.publicPort = var1.readInt();
      }

      if (this.getPublicPort() >= 0) {
         this.inSockAddress = new InetSocketAddress(this.getPublicAddress(), this.getPublicPort());
      }

   }

   BasicServerChannelImpl(NetworkAccessPointMBean var1, ServerIdentity var2) throws UnknownHostException, UnknownProtocolException {
      this(var1, ProtocolManager.findProtocol(var1.getProtocol()), var1.getName(), var2, (String)null);
   }

   public BasicServerChannelImpl(NetworkAccessPointMBean var1, Protocol var2, ServerIdentity var3) throws UnknownHostException {
      this(var1, var2, encodeName(var2, var1.getName()), var3, (String)null);
      this.displayName = var1.getName();
   }

   protected static final String encodeName(Protocol var0, String var1) {
      return var1 + "[" + var0.getAsURLPrefix() + "]";
   }

   String getRealName() {
      return this.displayName != null ? this.displayName : this.channelName;
   }

   protected BasicServerChannelImpl(NetworkAccessPointMBean var1, Protocol var2, String var3, ServerIdentity var4, String var5) throws UnknownHostException {
      this.listenPort = -1;
      this.publicPort = -1;
      if (var2.isSecure()) {
         this.flags = (byte)(this.flags | 32);
      }

      if (!var2.getProtocolName().equalsIgnoreCase(var1.getProtocol())) {
         this.implicitChannel = true;
      }

      this.protocol = var2;
      this.priority = var2.getHandler().getPriority();
      this.channelName = var3;
      this.config = var1;
      this.setListenAddress(var5);
      this.update(var4);
   }

   public static ServerChannel createBootstrapChannel(String var0) throws UnknownHostException {
      BasicServerChannelImpl var1 = new BasicServerChannelImpl();
      if (var0.equalsIgnoreCase("https")) {
         var1.flags = (byte)(var1.flags | 32);
      }

      var1.channelName = "BootstrapChannel";
      var1.config = NetworkAccessPointMBeanStub.createBootstrapStub();
      if (var1.config.getListenAddress() == null) {
         return null;
      } else {
         var1.update((ServerIdentity)null);
         return var1;
      }
   }

   void update(ServerIdentity var1) throws UnknownHostException {
      this.setListenAddress(this.config.getListenAddress());
      InetAddress var2 = null;
      this.setListenPort(this.config.getListenPort());
      this.setPublicPort(this.config.getPublicPort());
      this.setPublicAddress(this.config.getPublicAddress());
      this.setSDPEnabled(this.config.isSDPEnabled());
      if (!this.config.isHttpEnabledForThisProtocol() && !this.config.isTunnelingEnabled()) {
         this.flags &= -17;
      } else {
         this.flags = (byte)(this.flags | 16);
      }

      if (var1 != null && var1.isLocal()) {
         this.isLocal = true;
         if (this.address == null) {
            var2 = LOCALHOST;
            if (!this.hasPublicAddress()) {
               this.publicAddress = var2.getHostAddress();
            }
         } else {
            var2 = InetAddress.getByName(this.address);
            this.inetAddress = var2;
            if (this.address.indexOf(":") != -1) {
               this.address = "[" + this.inetAddress.getHostAddress() + "]";
            }
         }
      } else {
         String var3 = this.getPublicAddress();
         if (var3 == null) {
            if (var1 instanceof Channel) {
               var3 = ((Channel)var1).getPublicAddress();
            }

            if (var3 == null) {
               throw new UnknownHostException("Couldn't determine usable host address for remote channel: " + this.config.getName());
            }

            this.setPublicAddress(var3);
         }

         var2 = InetAddress.getByName(var3);
         this.inetAddress = var2;
      }

      byte[] var4 = var2.getAddress();
      this.rawAddress = ((var4[0] & 255) << 24) + ((var4[1] & 255) << 16) + ((var4[2] & 255) << 8) + ((var4[3] & 255) << 0);
      if (this.getPublicPort() >= 0) {
         this.inSockAddress = new InetSocketAddress(this.getPublicAddress(), this.getPublicPort());
      }

      this.weight = this.config.getChannelWeight();
   }

   void update() {
      this.weight = this.config.getChannelWeight();
      if (this.address != null) {
         this.setPublicAddress(this.config.getPublicAddress());
      }

      this.setPublicPort(this.config.getPublicPort());
   }

   protected BasicServerChannelImpl(BasicServerChannelImpl var1, Protocol var2) {
      this.listenPort = -1;
      this.publicPort = -1;
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
      System.out.println("<BasicServerChannelImpl>: " + var0);
   }

   public static ServerChannel createDefaultServerChannel(Protocol var0) {
      try {
         return new BasicServerChannelImpl(new NetworkAccessPointMBeanStub(var0.getProtocolName()), var0, LocalServerIdentity.getIdentity());
      } catch (IOException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getConfiguredProtocol() {
      return this.config.getProtocol();
   }

   public int getAcceptBacklog() {
      return this.config.getAcceptBacklog();
   }

   public int getCompleteMessageTimeout() {
      return this.config.getCompleteMessageTimeout();
   }

   public int getConnectTimeout() {
      return this.config.getConnectTimeout();
   }

   public int getIdleConnectionTimeout() {
      return this.config.getIdleConnectionTimeout();
   }

   public int getLoginTimeoutMillis() {
      return this.config.getLoginTimeoutMillis();
   }

   public int getMaxBackoffBetweenFailures() {
      return this.config.getMaxBackoffBetweenFailures();
   }

   public int getMaxConnectedClients() {
      return this.config.getMaxConnectedClients();
   }

   public int getMaxMessageSize() {
      return this.config.getMaxMessageSize();
   }

   public String getProxyAddress() {
      return this.config.getProxyAddress();
   }

   public int getProxyPort() {
      return this.config.getProxyPort();
   }

   public boolean getTimeoutConnectionWithPendingResponses() {
      return this.config.getTimeoutConnectionWithPendingResponses();
   }

   public int getTunnelingClientPingSecs() {
      return this.config.getTunnelingClientPingSecs();
   }

   public int getTunnelingClientTimeoutSecs() {
      return this.config.getTunnelingClientTimeoutSecs();
   }

   public boolean getUseFastSerialization() {
      return this.config.getUseFastSerialization();
   }

   public boolean isClientCertificateEnforced() {
      return this.config.isClientCertificateEnforced();
   }

   public boolean isHttpEnabledForThisProtocol() {
      return this.config.isHttpEnabledForThisProtocol();
   }

   public boolean isOutboundEnabled() {
      return this.config.isOutboundEnabled();
   }

   public boolean isOutboundPrivateKeyEnabled() {
      return this.config.isOutboundPrivateKeyEnabled();
   }

   public boolean isTunnelingEnabled() {
      return this.config.isTunnelingEnabled();
   }

   public boolean isTwoWaySSLEnabled() {
      return this.config.isTwoWaySSLEnabled();
   }

   public boolean isT3SenderQueueDisabled() {
      return this.t3SenderQueueDisabled;
   }

   void setT3SenderQueueDisabled(boolean var1) {
      this.t3SenderQueueDisabled = var1;
   }
}
