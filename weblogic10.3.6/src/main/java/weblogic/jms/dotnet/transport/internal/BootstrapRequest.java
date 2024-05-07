package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;

class BootstrapRequest implements MarshalReadable, MarshalWritable {
   private String bootstrapServiceClassName;
   private int heartBeatInterval = 0;
   private int allowedMissedBeats = 0;

   BootstrapRequest() {
   }

   BootstrapRequest(String var1) {
      this.bootstrapServiceClassName = var1;
   }

   synchronized String getBootstrapServiceClassName() {
      return this.bootstrapServiceClassName;
   }

   synchronized int getHeartbeatInterval() {
      return this.heartBeatInterval;
   }

   synchronized int getAllowedMissedBeats() {
      return this.allowedMissedBeats;
   }

   public int getMarshalTypeCode() {
      return 20000;
   }

   public void marshal(MarshalWriter var1) {
      var1.writeUnsignedByte(1);
      var1.writeUnsignedByte(0);
      var1.writeString(this.bootstrapServiceClassName);
      var1.writeInt(this.heartBeatInterval);
      var1.writeInt(this.allowedMissedBeats);
   }

   public synchronized void unmarshal(MarshalReader var1) {
      int var2 = var1.read();

      while((var1.readByte() & 1) != 0) {
      }

      this.bootstrapServiceClassName = var1.readString();
      this.heartBeatInterval = var1.readInt();
      this.allowedMissedBeats = var1.readInt();
   }
}
