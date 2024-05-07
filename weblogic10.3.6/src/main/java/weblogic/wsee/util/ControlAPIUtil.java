package weblogic.wsee.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.wsee.component.pojo.JavaClassComponent;
import weblogic.wsee.jaxrpc.StubImpl;
import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.ws.WsSkel;

public class ControlAPIUtil {
   public static final String ENCLOSING_CONTAINER = "weblogic.wsee.enclosing.container";

   private static List convertOutputHeaders(Element[] var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Element var3 = var0[var2];
            if (var3 != null) {
               if (("http://schemas.xmlsoap.org/soap/envelope/".equals(var3.getNamespaceURI()) || "http://www.w3.org/2003/05/soap-envelope".equals(var3.getNamespaceURI())) && "Header".equals(var3.getLocalName())) {
                  NodeList var4 = var3.getChildNodes();

                  for(int var5 = 0; var5 < var4.getLength(); ++var5) {
                     if (var4.item(var5) instanceof Element) {
                        var1.add((Element)var4.item(var5));
                     }
                  }
               } else {
                  var1.add(var3);
               }
            }
         }

         return var1;
      }
   }

   public static Element[] getOutputHeaders(MessageContext var0) {
      if (var0 == null) {
         return null;
      } else {
         List var1 = (List)var0.getProperty("weblogic.wsee.OutputHeaders");
         if (var1 == null) {
            return null;
         } else {
            Element[] var2 = (Element[])((Element[])var1.toArray(new Element[var1.size()]));
            return var2;
         }
      }
   }

   public static void setInputHeaders(MessageContext var0, Element[] var1) {
      if (var1 != null) {
         var0.setProperty("weblogic.wsee.InputHeaders", var1);
      }

   }

   public static void setOutputHeaders(MessageContext var0, Element[] var1) {
      List var2 = convertOutputHeaders(var1);
      if (var2 != null) {
         var0.setProperty("weblogic.wsee.OutputHeaders", var2);
      }

   }

   public static void unsetOutputHeaders(MessageContext var0) {
      var0.removeProperty("weblogic.wsee.OutputHeaders");
   }

   public static void setOutputHeaders(Stub var0, Element[] var1) {
      List var2 = convertOutputHeaders(var1);
      if (var2 != null) {
         var0._setProperty("weblogic.wsee.OutputHeaders", var2);
      }

   }

   public static void unsetOutputHeaders(Stub var0) {
      if (var0 instanceof StubImpl) {
         ((StubImpl)var0)._removeProperty("weblogic.wsee.OutputHeaders");
      }

   }

   public static void setURI(MessageContext var0, String var1) {
      var0.setProperty("weblogic.wsee.service_uri", var1);
   }

   public static String getURI(MessageContext var0) {
      return (String)var0.getProperty("weblogic.wsee.service_uri");
   }

   public static Element[] getInputHeadersFromMsgContextThenSoap(MessageContext var0) {
      Element[] var1 = (Element[])((Element[])var0.getProperty("weblogic.wsee.InputHeaders"));
      if (var1 != null) {
         return var1;
      } else if (var0 instanceof SoapMessageContext) {
         SoapMessageContext var2 = (SoapMessageContext)var0;

         try {
            Element[] var3 = new Element[]{var2.getMessage().getSOAPHeader()};
            return var3[0] == null ? null : var3;
         } catch (SOAPException var4) {
            throw new JAXRPCException(var4);
         }
      } else {
         throw new JAXRPCException("Unsupported message context type");
      }
   }

   public static Element[] getInputHeaders(MessageContext var0) {
      if (var0 == null) {
         return null;
      } else {
         Element[] var1 = getInputHeadersFromMsgContextThenSoap(var0);
         return var1;
      }
   }

   public static Element[] getInputHeaders(Stub var0) {
      if (var0 != null) {
         Map var1 = (Map)var0._getProperty("weblogic.wsee.invoke_properties");
         if (var1 != null) {
            return (Element[])((Element[])var1.get("weblogic.wsee.InputHeaders"));
         }
      }

      return null;
   }

   public static JwsContext getJwsContext(MessageContext var0) {
      WlMessageContext var1 = WlMessageContext.narrow(var0);
      JavaClassComponent var2 = (JavaClassComponent)((WsSkel)var1.getDispatcher().getWsPort().getEndpoint()).getComponent();
      return ContainerFactory.getContainer(var1, var2.getTargetClass());
   }

   public static void finishCallbackStubConversation(Stub var0) {
      Container var1 = (Container)var0._getProperty("weblogic.wsee.enclosing.container");
      if (var1 != null) {
         var1.finishConversation();
         if (var0 instanceof StubImpl) {
            ((StubImpl)var0)._removeProperty("weblogic.wsee.enclosing.container");
         }

      } else {
         throw new IllegalStateException("Attemp to finish a conversation when the service is not conversational or has been finished.");
      }
   }
}
