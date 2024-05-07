package weblogic.jms.dotnet.transport.t3plugin;

import weblogic.jms.dotnet.t3.server.JMSCSharp;
import weblogic.jms.dotnet.t3.server.spi.T3Connection;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandle;
import weblogic.jms.dotnet.t3.server.spi.T3ConnectionHandleFactory;

public class T3ServerPlugin implements T3ConnectionHandleFactory {
   public static final T3ServerPlugin singleton = new T3ServerPlugin();

   public static void register() {
      JMSCSharp.getInstance();
      JMSCSharp.setT3ConnectionHandleFactory(1, singleton);
   }

   public T3ConnectionHandle createHandle(T3Connection var1) {
      return new T3ConnectionHandleImpl(var1);
   }

   public static void main(String[] var0) {
      register();
   }
}
