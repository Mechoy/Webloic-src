package weblogic.wsee.buffer;

import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPMessage;
import weblogic.jws.MessageBuffer;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.conversation.ConversationServerHandler;
import weblogic.wsee.conversation.StartHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;

public class Wlw81CompatBufferingHandler extends ConversationServerHandler {
   private static final boolean verbose = Verbose.isVerbose(Wlw81CompatBufferingHandler.class);

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      MessageBuffer var3 = ServerBufferingHandler.getMessageBufferConfig(var2);
      if (var3 != null) {
         if (verbose) {
            Verbose.log((Object)"Upgraded JWS Method is buffered.  Overriding ONEWAY requirement.");
         }

         var2.setProperty("weblogic.wsee.handler.wlw81BufferCompatFlat", Boolean.TRUE);
         ConversationPhase var4 = this.getConversationPhase(var2);
         if (var4 == ConversationPhase.START) {
            if (verbose) {
               Verbose.log((Object)"Upgraded JWS Method is buffered and conversational.");
            }

            if (var1.getProperty("weblogic.wsee.conversation.ConversationId") == null) {
               MsgHeaders var5 = var2.getHeaders();
               StartHeader var6 = (StartHeader)var5.getHeader(StartHeader.TYPE);
               if (verbose && var6 != null) {
                  Verbose.log((Object)("WLW Buffering Compat Handler Received StartHeader " + var6));
               }

               if (var6 == null || StringUtil.isEmpty(var6.getConversationId())) {
                  if (verbose) {
                     Verbose.log((Object)"Inserting conversation Id for upgraded buffered method before buffering.");
                  }

                  String var7 = Guid.generateGuid();
                  if (verbose) {
                     Verbose.log((Object)("Inserting conversation Id " + var7));
                  }

                  if (var6 == null) {
                     var2.getHeaders().addHeader(new StartHeader(var7));
                  } else {
                     var6.setConversationId(var7);
                  }

                  var1.setProperty("weblogic.wsee.conversation.IsServerAssigned", Boolean.TRUE);
                  var1.setProperty("weblogic.wsee.conversation.ConversationId", var7);
                  var1.setProperty("weblogic.wsee.conversation.ConversationPhase", var4);
               }
            }
         }
      }

      return true;
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (var2.containsProperty("weblogic.wsee.handler.wlw81BufferCompatFlat")) {
         if (verbose) {
            Verbose.log((Object)"Upgraded JWS Method is buffered.  Creating null response.");
         }

         QName var3 = null;
         WsdlOperation var4 = var2.getDispatcher().getOperation();
         if (var4 != null) {
            WsdlMessage var5 = var4.getOutput();
            if (var5 != null) {
               var3 = var5.getName();
            }
         }

         if (verbose) {
            Verbose.log((Object)("QName of returnElement:" + var3));
         }

         if (var1 instanceof SOAPMessageContext) {
            SOAPMessageContext var6 = (SOAPMessageContext)var1;
            var6.setMessage(this.createWlw81Response(var6, var3));
         }

         ConversationPhase var7 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
         if (var7 == ConversationPhase.START) {
            if (verbose) {
               Verbose.log((Object)("Upgraded JWS Method is buffered and conversational.  Adding conv header. Id = " + var2.getProperty("weblogic.wsee.conversation.ConversationId")));
            }

            this.addContinueHeader(var2);
         }
      }

      return true;
   }

   private SOAPMessage createWlw81Response(SOAPMessageContext var1, QName var2) {
      SOAPMessage var3 = null;

      try {
         var3 = ((SoapMessageContext)var1).getMessageFactory().createMessage();
         if (var3 == null) {
            throw new JAXRPCException("Could not construct message using message factory.");
         } else {
            if (var2 != null) {
               Name var4 = var3.getSOAPPart().getEnvelope().createName(var2.getLocalPart(), var2.getPrefix(), var2.getNamespaceURI());
               if (var4 != null) {
                  var3.getSOAPPart().getEnvelope().getBody().addBodyElement(var4);
               }
            }

            return var3;
         }
      } catch (Exception var5) {
         if (var5 instanceof JAXRPCException) {
            throw (JAXRPCException)var5;
         } else {
            throw new JAXRPCException("Could not construct valid SOAP response", var5);
         }
      }
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleClosure(MessageContext var1) {
      return true;
   }
}
