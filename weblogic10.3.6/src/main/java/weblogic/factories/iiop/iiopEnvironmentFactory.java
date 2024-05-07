package weblogic.factories.iiop;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Remote;
import javax.naming.Context;
import javax.naming.NamingException;
import org.omg.CORBA.Object;
import weblogic.corba.j2ee.naming.InitialContextFactoryImpl;
import weblogic.corba.j2ee.naming.NameParser;
import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.jndi.Environment;
import weblogic.jndi.internal.ThreadEnvironment;
import weblogic.jndi.spi.EnvironmentFactory;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.spi.HostID;
import weblogic.server.channels.ChannelService;

public class iiopEnvironmentFactory implements EnvironmentFactory {
   public Context getInitialContext(Environment var1, String var2) throws NamingException {
      Context var4;
      try {
         ThreadEnvironment.push(var1);
         Context var3 = InitialContextFactoryImpl.getInitialContext(var1.getProperties(), var1.getProviderUrl());
         if (var2 != null) {
            var3 = (Context)var3.lookup(var2);
         }

         var4 = var3;
      } finally {
         ThreadEnvironment.pop();
      }

      return var4;
   }

   public Remote getInitialReference(Environment var1, Class var2) throws NamingException {
      try {
         ThreadEnvironment.push(var1);
         NameParser.URLInfo var3 = NameParser.parseURL(var1.getProviderUrl());

         try {
            if (ChannelService.isLocalChannel(InetAddress.getByName(var3.getHost()), var3.getPort())) {
               Remote var11 = ServerHelper.getLocalInitialReference(var2);
               return var11;
            }
         } catch (IOException var9) {
         }

         Object var4 = ORBHelper.getORBHelper().getORBReference(var1.getProviderUrl(), var1.getProperties(), var2.getName());
         Remote var5 = (Remote)PortableRemoteObject.narrow(var4, Remote.class);
         return var5;
      } finally {
         ThreadEnvironment.pop();
      }
   }

   public Context getInitialContext(Environment var1, String var2, HostID var3) throws NamingException {
      return this.getInitialContext(var1, var2);
   }
}
