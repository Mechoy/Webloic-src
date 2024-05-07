package weblogic.protocol;

import java.util.List;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.rmi.extensions.ConnectMonitor;
import weblogic.server.channels.RemoteChannelServiceImpl;

public class ConnectMonitorFactory {
   public static ConnectMonitor getConnectMonitor() {
      return RemoteChannelServiceImpl.getInstance();
   }

   public static void registerForever(Environment var0) throws NamingException {
      RemoteChannelServiceImpl.registerForever(var0);
   }

   public static void registerForever(List<Environment> var0) throws NamingException {
      RemoteChannelServiceImpl.registerForever(var0);
   }

   public static void unregister() throws NamingException {
      RemoteChannelServiceImpl.unregister();
   }
}
