package weblogic.jms.dotnet.proxy.internal;

import javax.jms.JMSException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dotnet.proxy.protocol.ProxyBootstrapResponse;
import weblogic.jms.dotnet.transport.MarshalReadable;
import weblogic.jms.dotnet.transport.MarshalWritable;
import weblogic.jms.dotnet.transport.ReceivedTwoWay;
import weblogic.jms.dotnet.transport.ServiceTwoWay;
import weblogic.jms.dotnet.transport.Transport;
import weblogic.jms.dotnet.transport.TransportError;

public class BootstrapService implements ServiceTwoWay {
   private static final ProxyManagerImpl manager = ProxyManagerImpl.getProxyManager();

   public final void invoke(ReceivedTwoWay var1) {
      if (manager.isShutdown()) {
         var1.send(new TransportError(new JMSException("The JMS service is shutting down")));
      } else {
         Object var2 = null;
         MarshalReadable var3 = var1.getRequest();
         switch (var3.getMarshalTypeCode()) {
            case 20000:
               if (JMSDebug.JMSDotNetProxy.isDebugEnabled()) {
                  JMSDebug.JMSDotNetProxy.debug("Got bootstrap request -- ");
               }

               Transport var4 = var1.getTransport();
               var4.addMarshalReadableFactory(manager);
               var4.registerService(10004L, manager);
               var2 = new ProxyBootstrapResponse(10004L);
               break;
            default:
               var2 = new TransportError("Invalid MarshalReadableType : " + var3.getMarshalTypeCode(), false);
         }

         var1.send((MarshalWritable)var2);
      }
   }

   public void onPeerGone(TransportError var1) {
   }

   public void onShutdown() {
   }

   public void onUnregister() {
   }
}
