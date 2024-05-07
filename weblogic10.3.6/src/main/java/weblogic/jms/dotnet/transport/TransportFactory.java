package weblogic.jms.dotnet.transport;

import weblogic.jms.dotnet.transport.internal.TransportImpl;

public class TransportFactory {
   private static ServiceOneWay listener;
   private static final TransportFactoryLock lock = new TransportFactoryLock();

   public static void registerTransportListener(ServiceOneWay var0) {
      synchronized(lock) {
         listener = var0;
      }
   }

   public static Transport createTransport(TransportPluginSPI var0, TransportThreadPool var1) {
      return new TransportImpl(var0, var1);
   }

   public static void announceTransport(Transport var0) {
      ServiceOneWay var1;
      synchronized(lock) {
         var1 = listener;
      }

      if (var1 != null) {
         var1.invoke(new TransportAnnouncement(var0));
      }
   }
}
