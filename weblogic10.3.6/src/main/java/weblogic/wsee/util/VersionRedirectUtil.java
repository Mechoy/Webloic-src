package weblogic.wsee.util;

import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.connection.ConnectionFactory;
import weblogic.wsee.connection.transport.ServerTransport;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.local.LocalDelegateServerTransport;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsRegistry;
import weblogic.wsee.ws.WsSkel;

public class VersionRedirectUtil {
   private static final String LOCAL_TRANSPORT = "local";
   private static final String SOAP_VERSION = "SOAP11";
   private static final boolean verbose = Verbose.isVerbose(VersionRedirectUtil.class);

   public static void redirect(WlMessageContext var0, String var1) throws Throwable {
      SoapMessageContext var2 = new SoapMessageContext(AsyncUtil.isSoap12(var0));
      Transport var3 = var0.getDispatcher().getConnection().getTransport();
      String var4 = WsRegistry.getURL(var3.getServiceURI());
      if (verbose) {
         Verbose.log((Object)("VersionRedirectUtil redirect - uri = " + var4));
      }

      WsPort var5 = WsRegistry.instance().lookup(var4, var1);
      if (var5 == null) {
         throw new JAXRPCException("The application version used by the current conversation is no longer available.Please start a new conversation with the new version of the application");
      } else {
         var2.setMessage(((SoapMessageContext)var0).getMessage());
         Iterator var6 = var0.getPropertyNames();

         while(var6.hasNext()) {
            String var7 = (String)var6.next();
            var2.setProperty(var7, var0.getProperty(var7));
         }

         String var9 = var5.getWsdlPort().getBinding().getBindingType();
         Connection var10 = ConnectionFactory.instance().createServerConnection(new LocalDelegateServerTransport((ServerTransport)var3), var9);
         WsSkel var8 = (WsSkel)var5.getEndpoint();
         var0.setProperty("weblogic.wsee.util.VersionRedirectUtil.redirected", new String("true"));
         var8.invoke(var10, var5, var2);
      }
   }
}
