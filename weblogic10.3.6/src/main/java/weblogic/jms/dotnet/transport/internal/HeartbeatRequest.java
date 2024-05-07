package weblogic.jms.dotnet.transport.internal;

import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalReader;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.MarshalWriter;

class HeartbeatRequest implements MarshalWritable, MarshalReadable {
   private int heartbeatNumber;

   public HeartbeatRequest() {
   }

   public HeartbeatRequest(int var1) {
      this.heartbeatNumber = var1;
   }

   int getHeartbeatNumber() {
      return this.heartbeatNumber;
   }

   public int getMarshalTypeCode() {
      return 20003;
   }

   public synchronized void unmarshal(MarshalReader var1) {
      int var2 = var1.read();

      while((var1.readByte() & 1) != 0) {
      }

      this.heartbeatNumber = var1.readInt();
   }

   public synchronized void marshal(MarshalWriter var1) {
      var1.writeUnsignedByte(1);
      var1.writeUnsignedByte(0);
      var1.writeInt(this.heartbeatNumber);
   }
}
