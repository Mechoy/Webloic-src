package weblogic.wsee.jaxws.cluster.spi;

public interface ServerNameMapService {
   ServerAddress getServerAddress(String var1);

   void addServerAddressChangeListener(ServerAddressChangeListener var1);

   void removeServerAddressChangeListener(ServerAddressChangeListener var1);

   public interface ServerAddressChangeListener {
      void serverAddressChanged(String var1, ServerNameMapService var2);
   }

   public static class ServerAddress {
      public String serverName;
      public String host;
      public int port;
      public int sslPort;
   }
}
