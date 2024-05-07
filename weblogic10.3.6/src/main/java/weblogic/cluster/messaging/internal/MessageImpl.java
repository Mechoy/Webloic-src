package weblogic.cluster.messaging.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

final class MessageImpl implements Message, Externalizable {
   private byte[] data;
   private String serverName;
   private long startTime;
   private long id;
   private String version;
   private ServerConfigurationInformation configuration;
   private transient ServerConfigurationInformation forwardConfiguration;

   MessageImpl(byte[] var1, String var2, long var3, long var5) {
      this.data = var1;
      this.serverName = var2;
      this.startTime = var3;
      this.id = var5;
      this.version = "9.5";
      this.configuration = Environment.getConfiguredServersMonitor().getConfiguration(var2);
   }

   public String getVersion() {
      return this.version;
   }

   public String getServerName() {
      return this.serverName;
   }

   public long getServerStartTime() {
      return this.startTime;
   }

   public byte[] getData() {
      return this.data;
   }

   public ServerConfigurationInformation getSenderConfiguration() {
      return this.configuration;
   }

   public ServerConfigurationInformation getForwardingServer() {
      return this.forwardConfiguration;
   }

   public String toString() {
      return " server " + this.serverName + ", id=" + this.id;
   }

   public MessageImpl() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF("9.5");
      var1.writeInt(this.data.length);
      var1.write(this.data);
      var1.writeUTF(this.serverName);
      var1.writeObject(this.configuration);
      var1.writeObject(Environment.getGroupManager().getServerConfigForWire());
      var1.writeLong(this.startTime);
      var1.writeLong(this.id);
   }

   public void readExternal(ObjectInput var1) throws IOException {
      this.version = var1.readUTF();
      int var2 = var1.readInt();
      this.data = new byte[var2];
      var1.readFully(this.data);
      this.serverName = var1.readUTF();

      try {
         this.configuration = (ServerConfigurationInformation)var1.readObject();
         this.forwardConfiguration = (ServerConfigurationInformation)var1.readObject();
      } catch (ClassNotFoundException var4) {
         throw new AssertionError(var4);
      }

      this.startTime = var1.readLong();
      this.id = var1.readLong();
   }
}
