package weblogic.wsee.ws;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.handler.JaxrpcHandlerChain;
import weblogic.wsee.jaxrpc.HandlerRegistryImpl;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.wsee.ws.dispatch.server.JaxrpcChainHandler;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;

public class WsStub extends WsEndpointImpl {
   private static final boolean verbose = Verbose.isVerbose(WsStub.class);

   WsStub(WsService var1, WsdlPortType var2) throws WsException {
      super(var1, var2);
   }

   public Object invoke(String var1, String var2, Map var3, Map var4, Map var5) throws Throwable {
      if (verbose) {
         Verbose.log((Object)("invoking method " + var1 + " with args: " + var3));
      }

      WsPort var6 = this.getService().getPort(var2);
      if (var6 == null) {
         throw new WsException("Unable to find port '" + var2 + "'");
      } else {
         WsdlPort var7 = var6.getWsdlPort();

         assert var7 != null;

         WsMethod var8 = this.getMethod(var1);
         if (var8 == null) {
            throw new WsException("Remote method '" + var1 + "' not found");
         } else {
            if (verbose) {
               Verbose.log((Object)("Found Webservice method : " + var8.getOperationName()));
            }

            String var9 = this.getPortType().getName().getNamespaceURI();
            QName var10 = new QName(var9, var1);
            WsdlBindingOperation var11 = (WsdlBindingOperation)var7.getBinding().getOperations().get(var10);
            this.updateJaxrpcHandlerChain(var6, var5);
            ClientDispatcher var12 = new ClientDispatcher(var8, var6, var11, var3, var4, var5);
            var12.dispatch();
            Object var13 = var8.getReturnType() == null ? null : var4.remove(var8.getReturnType().getName());
            if (verbose) {
               Verbose.log((Object)("Return Value = " + var13));
               Verbose.log((Object)("Out Parameters = " + var4));
            }

            return var13;
         }
      }
   }

   public Object _asyncResponse(String var1, String var2, Map var3, SOAPMessage var4, MessageContext var5, Map var6) throws Throwable {
      if (verbose) {
         Verbose.log((Object)("async response " + var1));
      }

      WsPort var7 = this.getService().getPort(var2);
      if (var7 == null) {
         throw new WsException("Unable to find port '" + var2 + "'");
      } else {
         WsdlPort var8 = var7.getWsdlPort();

         assert var8 != null;

         WsMethod var9 = this.getMethod(var1);
         if (var9 == null) {
            throw new WsException("Remote method '" + var1 + "' not found");
         } else {
            if (verbose) {
               Verbose.log((Object)("Found Webservice method : " + var9.getOperationName()));
            }

            String var10 = this.getPortType().getName().getNamespaceURI();
            QName var11 = new QName(var10, var1);
            WsdlBindingOperation var12 = (WsdlBindingOperation)var8.getBinding().getOperations().get(var11);
            this.updateJaxrpcHandlerChain(var7, var6);
            ClientDispatcher var13 = new ClientDispatcher(var9, var7, var12, (Map)null, var3, var6);
            var13.handleAsyncResponse(var4, var5);
            Object var14 = var9.getReturnType() == null ? null : var3.remove(var9.getReturnType().getName());
            if (verbose) {
               Verbose.log((Object)("Return Value = " + var14));
               Verbose.log((Object)("Out Parameters = " + var3));
            }

            return var14;
         }
      }
   }

   private void updateJaxrpcHandlerChain(WsPort var1, Map var2) throws HandlerException {
      HandlerRegistryImpl var3 = (HandlerRegistryImpl)var2.get("weblogic.wsee.handler.registry");
      if (var3 != null && var3.isChanged()) {
         QName var4 = var1.getWsdlPort().getName();
         List var5 = var3.getHandlerChain(var4);
         HandlerListImpl var6 = new HandlerListImpl();
         Iterator var7 = var5.iterator();

         while(var7.hasNext()) {
            HandlerInfo var8 = (HandlerInfo)var7.next();
            var6.add("[USER HANDLER]", var8);
         }

         JaxrpcChainHandler var9 = this.findJaxrpcChainHandler(var1);
         JaxrpcHandlerChain var10 = new JaxrpcHandlerChain(var6);
         var9.resetWithNewChain(var10);
         var3.setChanged(false);
      }
   }

   private JaxrpcChainHandler findJaxrpcChainHandler(WsPort var1) {
      HandlerListImpl var2 = (HandlerListImpl)var1.getInternalHandlerList();
      String[] var3 = var2.getHandlerNames();
      boolean var4 = false;

      int var6;
      for(var6 = 0; var6 < var3.length && !"JAX_RPC_CHAIN_HANDLER".equals(var3[var6]); ++var6) {
      }

      if (var6 == var3.length) {
         throw new AssertionError("Jaxrpc chain handler is not found.");
      } else {
         JaxrpcChainHandler var5 = (JaxrpcChainHandler)var2.get(var6);
         return var5;
      }
   }
}
