package weblogic.wsee.reliability;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.faults.SequenceFaultException;
import weblogic.wsee.reliability.faults.SequenceFaultMsg;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.util.Verbose;

public class WsrmHelper {
   private static final boolean verbose = Verbose.isVerbose(WsrmHelper.class);

   public static void sendFault(SOAPMessageContext var0, WsrmFaultMsg var1) {
      sendFault(var0, var1, (EndpointReference)null);
   }

   public static void sendFault(SOAPMessageContext var0, WsrmFaultMsg var1, EndpointReference var2) {
      try {
         ((WlMessageContext)var0).setFault(new SequenceFaultException(var1.getSubCodeLocalName().toString()));
         SOAPMessage var3 = getMessageFactory(var0).createMessage();
         var0.setMessage(var3);
         AddressingProvider var4 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var0);
         if (WsrmConstants.RMVersion.RM_11 == var1.getRmVersion()) {
            ActionHeader var5 = var4.createActionHeader(WsrmConstants.RMVersion.RM_11.getNamespaceUri() + "/fault");
            ((WlMessageContext)var0).getHeaders().addHeader(var5);
         }

         if (var1 instanceof SequenceFaultMsg) {
            String var8 = ((SequenceFaultMsg)var1).getSequenceId();
            if (var8 != null && !var8.equals("New")) {
               if (var0.getProperty("weblogic.wsee.faultto.override") == null) {
                  if (var2 == null) {
                     var2 = (EndpointReference)var0.getProperty("weblogic.wsee.addressing.ReplyTo");
                  }

                  var0.setProperty("weblogic.wsee.faultto.override", var2);
               }

               var0.setProperty("weblogic.wsee.reliability.asyncfault", "true");
            }
         }

         var1.write(var3);
         if (verbose) {
            WsrmUtils.printSoapMsg(var3);
         }

      } catch (SOAPException var6) {
         if (verbose) {
            Verbose.logException(var6);
         }

         throw new JAXRPCException(var6);
      } catch (SequenceFaultException var7) {
         if (verbose) {
            Verbose.logException(var7);
         }

         throw new JAXRPCException("Failed to write Sequence Fault message", var7);
      }
   }

   private static MessageFactory getMessageFactory(SOAPMessageContext var0) {
      return ((SoapMessageContext)var0).getMessageFactory();
   }
}
