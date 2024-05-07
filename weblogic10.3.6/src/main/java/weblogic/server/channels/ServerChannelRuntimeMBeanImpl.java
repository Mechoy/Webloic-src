package weblogic.server.channels;

import java.util.Iterator;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.ServerChannelRuntimeMBean;
import weblogic.management.runtime.ServerConnectionRuntime;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.ServerChannel;
import weblogic.utils.collections.WeakConcurrentHashMap;

public class ServerChannelRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ServerChannelRuntimeMBean {
   private final transient WeakConcurrentHashMap connectedSockets = new WeakConcurrentHashMap();
   private final ServerChannel channel;
   private long pastBytesSent;
   private long pastBytesReceived;
   private long pastMessagesSent;
   private long pastMessagesReceived;
   private long acceptCount;

   ServerChannelRuntimeMBeanImpl(ServerChannelImpl var1) throws ManagementException {
      super(var1.getChannelName().replace(':', '_'));
      this.channel = var1;
   }

   public void addServerConnectionRuntime(ServerConnectionRuntime var1) {
      this.connectedSockets.put(var1.getSocketRuntime(), var1);
      ++this.acceptCount;
   }

   public void removeServerConnectionRuntime(SocketRuntime var1) {
      ServerConnectionRuntime var2 = (ServerConnectionRuntime)this.connectedSockets.remove(var1);
      if (var2 != null) {
         this.pastBytesReceived += var2.getBytesReceivedCount();
         this.pastBytesSent += var2.getBytesSentCount();
         this.pastMessagesSent += var2.getMessagesSentCount();
         this.pastMessagesReceived += var2.getMessagesReceivedCount();
      }

   }

   public ServerConnectionRuntime[] getServerConnectionRuntimes() {
      return (ServerConnectionRuntime[])((ServerConnectionRuntime[])this.connectedSockets.values().toArray(new ServerConnectionRuntime[this.connectedSockets.size()]));
   }

   public long getConnectionsCount() {
      return (long)this.connectedSockets.size();
   }

   public long getBytesReceivedCount() {
      long var1 = this.pastBytesReceived;

      for(Iterator var3 = this.connectedSockets.values().iterator(); var3.hasNext(); var1 += ((ServerConnectionRuntime)var3.next()).getBytesReceivedCount()) {
      }

      return var1;
   }

   public long getAcceptCount() {
      return this.acceptCount;
   }

   public long getBytesSentCount() {
      long var1 = this.pastBytesSent;

      for(Iterator var3 = this.connectedSockets.values().iterator(); var3.hasNext(); var1 += ((ServerConnectionRuntime)var3.next()).getBytesSentCount()) {
      }

      return var1;
   }

   public String getChannelName() {
      return this.channel.getChannelName();
   }

   public long getMessagesReceivedCount() {
      long var1 = this.pastMessagesReceived;

      for(Iterator var3 = this.connectedSockets.values().iterator(); var3.hasNext(); var1 += ((ServerConnectionRuntime)var3.next()).getMessagesReceivedCount()) {
      }

      return var1;
   }

   public long getMessagesSentCount() {
      long var1 = this.pastMessagesSent;

      for(Iterator var3 = this.connectedSockets.values().iterator(); var3.hasNext(); var1 += ((ServerConnectionRuntime)var3.next()).getMessagesSentCount()) {
      }

      return var1;
   }

   public String getPublicURL() {
      return this.channel.getProtocolPrefix() + "://" + this.channel.getPublicAddress() + ":" + this.channel.getPublicPort();
   }
}
