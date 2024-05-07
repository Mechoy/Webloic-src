package weblogic.wsee.ws.dispatch.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import weblogic.utils.collections.StackPool;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.handler.JaxrpcHandlerChain;
import weblogic.wsee.message.soap.SoapMessageContext;

public class JaxrpcChainHandler extends GenericHandler {
   public static final String JAXRPC_HANDLER_CHAIN = "weblogic.wsee.handler.jaxrpcHandlerChain";
   public static final String HANDLER_CHAIN_POOLSIZE = "weblogic.wsee.handler.jaxrpcHandlerChainPoolSize";
   private static boolean freeModify = Boolean.getBoolean("weblogic.wsee.handler.allowAllModification");
   private StackPool handlerChainPool;
   private JaxrpcHandlerChain theChain;

   public void init(HandlerInfo var1) {
      Map var2 = var1.getHandlerConfig();
      this.theChain = (JaxrpcHandlerChain)var2.get("weblogic.wsee.handler.jaxrpcHandlerChain");
      String var3 = (String)var2.get("weblogic.wsee.handler.jaxrpcHandlerChainPoolSize");

      assert var3 != null;

      int var4 = Integer.parseInt(var3);
      if (var4 > 1) {
         this.handlerChainPool = new StackPool(var4);
      } else {
         this.handlerChainPool = null;
      }

   }

   public JaxrpcHandlerChain getBaseChain() {
      return this.theChain;
   }

   public void prepareForFirstHandleResponse(MessageContext var1) {
      JaxrpcHandlerChain var2 = this.getHandlerChain();
      var1.setProperty("weblogic.wsee.handler.jaxrpcHandlerChain", var2);
   }

   public void resetWithNewChain(JaxrpcHandlerChain var1) {
      this.theChain = var1;
      if (this.handlerChainPool != null) {
         while(true) {
            if (this.handlerChainPool.remove() != null) {
               continue;
            }
         }
      }

   }

   public boolean handleRequest(MessageContext var1) {
      if (this.theChain == null) {
         return true;
      } else {
         JaxrpcHandlerChain var2 = this.getHandlerChain();
         var1.setProperty("weblogic.wsee.handler.jaxrpcHandlerChain", var2);
         boolean var3 = true;

         try {
            if (freeModify) {
               var3 = var2.handleRequest(var1);
            } else {
               RequestMessage var4 = JaxrpcChainHandler.RequestMessage.recordMessage(var1);
               var3 = var2.handleRequest(var1);
               if (var3) {
                  RequestMessage var5 = JaxrpcChainHandler.RequestMessage.recordMessage(var1);
                  String var6 = var4.checkChange(var5);
                  if (var6 != null) {
                     throw new JAXRPCException(var6);
                  }
               }
            }

            return var3;
         } catch (SOAPException var7) {
            throw new JAXRPCException(var7.getMessage(), var7);
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      if (this.theChain == null) {
         return true;
      } else {
         JaxrpcHandlerChain var2 = (JaxrpcHandlerChain)var1.getProperty("weblogic.wsee.handler.jaxrpcHandlerChain");

         assert var2 != null;

         var2.handleResponse(var1);
         this.freeHandlerChain(var2);
         return true;
      }
   }

   public boolean handleFault(MessageContext var1) {
      if (this.theChain == null) {
         return true;
      } else {
         JaxrpcHandlerChain var2 = (JaxrpcHandlerChain)var1.getProperty("weblogic.wsee.handler.jaxrpcHandlerChain");
         if (var2 == null) {
            return true;
         } else {
            var2.handleResponse(var1);
            this.freeHandlerChain(var2);
            return true;
         }
      }
   }

   private JaxrpcHandlerChain getHandlerChain() {
      JaxrpcHandlerChain var1 = null;

      try {
         if (this.handlerChainPool == null) {
            var1 = JaxrpcHandlerChain.cloneChain(this.theChain);
            var1.init();
         } else {
            var1 = (JaxrpcHandlerChain)this.handlerChainPool.remove();
            if (var1 == null) {
               var1 = JaxrpcHandlerChain.deepCloneChain(this.theChain);
               var1.init();
            }
         }

         return var1;
      } catch (HandlerException var3) {
         throw new JAXRPCException("Failed to initialize Jaxrpc handler chain " + var3, var3);
      }
   }

   private void freeHandlerChain(JaxrpcHandlerChain var1) {
      if (this.handlerChainPool != null) {
         if (var1.hasRuntimeException()) {
            var1.destroy();
         } else if (!this.handlerChainPool.add(var1)) {
            var1.destroy();
         }

      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public void destroy() {
      if (this.theChain != null) {
         this.theChain.destroy();
      }

      if (this.handlerChainPool != null) {
         JaxrpcHandlerChain var1 = null;

         while((var1 = (JaxrpcHandlerChain)this.handlerChainPool.remove()) != null) {
            var1.destroy();
         }
      }

      this.theChain = null;
      this.handlerChainPool = null;
   }

   private static class RequestMessage {
      private Name topElemement;
      private List params = new ArrayList();

      static RequestMessage recordMessage(MessageContext var0) throws SOAPException {
         RequestMessage var1 = new RequestMessage();
         if (var0 instanceof SoapMessageContext) {
            SoapMessageContext var2 = (SoapMessageContext)var0;
            SOAPBody var3 = var2.getMessage().getSOAPBody();
            if (var3 == null) {
               throw new InvocationException("Unable to find SOAP Body");
            }

            Iterator var4 = var3.getChildElements();

            while(var4.hasNext()) {
               Object var5 = var4.next();
               if (var5 instanceof SOAPElement) {
                  SOAPElement var6 = (SOAPElement)var5;
                  var1.topElemement = var6.getElementName();
                  fillinParams(var1, var6);
               }
            }
         }

         return var1;
      }

      private static void fillinParams(RequestMessage var0, SOAPElement var1) {
         Iterator var2 = var1.getChildElements();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof SOAPElement) {
               SOAPElement var4 = (SOAPElement)var3;
               var0.params.add(var4.getElementName());
            }
         }

      }

      String checkChange(RequestMessage var1) {
         if (this.topElemement != null && !this.nameEquals(this.topElemement, var1.topElemement)) {
            return "First child element of SOAP body is changed from " + this.topElemement + " to " + var1.topElemement;
         } else if (this.params.size() != var1.params.size()) {
            return "The number of parameters is changed from " + this.params.size() + " to " + var1.params.size();
         } else {
            int var2 = 0;

            for(Iterator var3 = this.params.iterator(); var3.hasNext(); ++var2) {
               Name var4 = (Name)var3.next();
               if (!this.nameEquals(var4, (Name)var1.params.get(var2))) {
                  return "Parameter in position " + var2 + " is changed from " + var4 + " to " + var1.params.get(var2);
               }
            }

            return null;
         }
      }

      private boolean nameEquals(Name var1, Name var2) {
         if (var1 == null) {
            return var2 == null;
         } else if (var1.getURI() == null) {
            return var2.getURI() == null;
         } else {
            return var1.getURI().equals(var2.getURI()) && var1.getLocalName().equals(var2.getLocalName());
         }
      }
   }
}
