package weblogic.wsee.codec.soap12;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.codec.soap11.SoapEncoder;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Body;

public class Soap12Encoder extends SoapEncoder {
   private static final boolean verbose = Verbose.isVerbose(Soap12Encoder.class);

   Soap12Encoder(SOAPMessage var1, WsdlBindingMessage var2, WsMethod var3, MessageContext var4) {
      super(var1, var2, var3, var4);
   }

   protected Soap12Body getSoapBody(WsdlBindingMessage var1) {
      return Soap12Body.narrow(var1);
   }

   protected Soap12BindingOperation getSoapBindingOperation(WsdlBindingOperation var1) {
      return Soap12BindingOperation.narrow(var1);
   }

   protected Soap12Binding getSoapBinding(WsdlBinding var1) {
      return Soap12Binding.narrow(var1);
   }

   protected void addSoapActionHeader(SOAPMessage var1, SoapBindingOperation var2) {
      String var3 = var2.getSoapAction();
      if (!StringUtil.isEmpty(var3)) {
         try {
            var1.setProperty("weblogic.xml.saaj.action-parameter", var3);
         } catch (SOAPException var10) {
            if (verbose) {
               Verbose.logException(var10);
            }
         }

         var3 = this.wrap(var3);
         if (verbose) {
            Verbose.log((Object)("Adding action parameter: " + var3 + " to Content-Type"));
         }

         String var4 = "application/soap+xml";
         String[] var5 = var1.getMimeHeaders().getHeader("Content-Type");
         if (var5 != null && var5.length > 0) {
            var4 = var5[0];
         }

         var4 = var4.trim();
         int var6 = var4.indexOf("action=");
         byte var7 = 7;
         String var8 = null;
         if (var6 > 0) {
            var8 = var4.substring(0, var6 + var7) + var3;
            int var9 = var4.indexOf(";", var6);
            if (var9 >= 0) {
               var8 = var8 + var4.substring(var9);
            }
         } else {
            StringBuilder var11 = new StringBuilder(var4);
            if (!var4.endsWith(";")) {
               var11.append(";");
            }

            var11.append(" action=");
            var11.append(var3);
            var8 = var11.toString();
         }

         var1.getMimeHeaders().setHeader("Content-Type", var8);
         if (verbose) {
            Verbose.log((Object)("Set Content-Type to: " + var8));
         }
      }

   }
}
