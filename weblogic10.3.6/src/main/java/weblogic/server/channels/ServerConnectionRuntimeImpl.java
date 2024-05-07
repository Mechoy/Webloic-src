package weblogic.server.channels;

import java.io.ObjectStreamException;
import java.lang.ref.WeakReference;
import weblogic.management.runtime.ServerConnectionRuntime;
import weblogic.management.runtime.SocketRuntime;
import weblogic.protocol.MessageReceiverStatistics;
import weblogic.protocol.MessageSenderStatistics;
import weblogic.utils.Debug;

public class ServerConnectionRuntimeImpl implements ServerConnectionRuntime {
   private WeakReference sender;
   private final WeakReference receiver;
   private final WeakReference socket;

   public ServerConnectionRuntimeImpl(MessageSenderStatistics var1, MessageReceiverStatistics var2, SocketRuntime var3) {
      Debug.assertion(var2 != null);
      this.sender = new WeakReference(var1);
      this.receiver = new WeakReference(var2);
      this.socket = new WeakReference(var3);
   }

   public long getBytesReceivedCount() {
      MessageReceiverStatistics var1 = this.getReceiver();
      return var1 == null ? 0L : var1.getBytesReceivedCount();
   }

   public long getBytesSentCount() {
      MessageSenderStatistics var1 = this.getSender();
      return var1 == null ? 0L : var1.getBytesSentCount();
   }

   public long getConnectTime() {
      MessageReceiverStatistics var1 = this.getReceiver();
      return var1 == null ? 0L : var1.getConnectTime();
   }

   public long getMessagesReceivedCount() {
      MessageReceiverStatistics var1 = this.getReceiver();
      return var1 == null ? 0L : var1.getMessagesReceivedCount();
   }

   public long getMessagesSentCount() {
      MessageSenderStatistics var1 = this.getSender();
      return var1 == null ? 0L : var1.getMessagesSentCount();
   }

   public void addSender(MessageSenderStatistics var1) {
      this.sender = new WeakReference(var1);
   }

   private MessageSenderStatistics getSender() {
      return (MessageSenderStatistics)this.sender.get();
   }

   private MessageReceiverStatistics getReceiver() {
      return (MessageReceiverStatistics)this.receiver.get();
   }

   public SocketRuntime getSocketRuntime() {
      return (SocketRuntime)this.socket.get();
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializableConnectionRuntime(this);
   }

   private static class SerializableConnectionRuntime implements ServerConnectionRuntime {
      private long bytesIn;
      private long bytesOut;
      private long msgIn;
      private long msgOut;
      private long connect;
      private SocketRuntime sock;

      private SerializableConnectionRuntime(ServerConnectionRuntime var1) {
         this.bytesIn = var1.getBytesReceivedCount();
         this.bytesOut = var1.getBytesSentCount();
         this.msgIn = var1.getMessagesReceivedCount();
         this.msgOut = var1.getMessagesSentCount();
         this.connect = var1.getConnectTime();
         SocketRuntime var2 = var1.getSocketRuntime();
         if (var2 != null) {
            this.sock = new SocketRuntimeImpl(var2);
         }

      }

      public long getBytesReceivedCount() {
         return this.bytesIn;
      }

      public long getBytesSentCount() {
         return this.bytesOut;
      }

      public long getConnectTime() {
         return this.connect;
      }

      public long getMessagesReceivedCount() {
         return this.msgIn;
      }

      public long getMessagesSentCount() {
         return this.msgOut;
      }

      public SocketRuntime getSocketRuntime() {
         return this.sock;
      }

      // $FF: synthetic method
      SerializableConnectionRuntime(ServerConnectionRuntime var1, Object var2) {
         this(var1);
      }
   }
}
