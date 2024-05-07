package weblogic.jms.dotnet.transport.t3client;

import weblogic.jms.dotnet.transport.Transport;

public class EchoClient {
   public static void main(String[] var0) throws Exception {
      int var1 = 7001;
      String var2 = "localhost";
      if (var0 != null && var0.length == 2) {
         var2 = var0[0];
         var1 = Integer.parseInt(var0[1]);
      }

      T3Connection var3 = new T3Connection(var2, var1, (T3PeerInfo)null, (byte)0);
      System.out.println("Connection 1 is " + var3);
      T3Connection var4 = new T3Connection(var2, var1, (T3PeerInfo)null, (byte)0);
      System.out.println("Connection 2 is " + var4);

      for(int var5 = 0; var5 < 1000000; ++var5) {
         if (var5 % 1000 == 0) {
            System.out.println("SGC iter " + var5);
         }

         MarshalWriterImpl var6 = T3Connection.createOneWay((MarshalWriterImpl)null);
         var6.writeInt(var5);
         var3.sendOneWay(var6);
         var6 = T3Connection.createOneWay((MarshalWriterImpl)null);
         var6.writeInt(var5);
         var4.sendOneWay(var6);
         MarshalReaderImpl var7 = var3.receiveOneWay((Transport)null);
         MarshalReaderImpl var8 = var4.receiveOneWay((Transport)null);
         if (var7.readInt() != var5 + 1) {
            System.out.println("SGC connection1 is failing at " + var5);
            return;
         }

         if (var8.readInt() != var5 + 1) {
            System.out.println("SGC connection2 is failing at " + var5);
            return;
         }

         var7.internalClose();
         var8.internalClose();
      }

   }
}
