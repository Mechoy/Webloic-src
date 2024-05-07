package weblogic.wsee.addressing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Element;
import weblogic.wsee.addressing.policy.api.UsingAddressingPolicyInfo;
import weblogic.wsee.addressing.policy.api.UsingAddressingPolicyInfoFactory;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.UnknownMsgHeader;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;

public class AddressingHandler extends GenericHandler {
   private final boolean verbose = Verbose.isVerbose(AddressingHandler.class);
   public static final String OUTPUT_HEADERS = "weblogic.wsee.addressing.OutputHeaders";
   public static final String INPUT_HEADERS = "weblogic.wsee.addressing.InputHeaders";
   public static final String CALLBACK_TO = "weblogic.wsee.addressing.CallbackTo";
   public static final String REPLY_TO = "weblogic.wsee.addressing.ReplyTo";
   public static final String FROM = "weblogic.wsee.addressing.From";
   public static final String FAULT_TO = "weblogic.wsee.addressing.FaultTo";
   public static final String TO = "weblogic.wsee.addressing.To";
   public static final String MESSAGE_ID = "weblogic.wsee.addressing.MessageId";
   public static final String RELATES_TO = "weblogic.wsee.addressing.RelatesTo";
   public static final String ACTION = "weblogic.wsee.addressing.Action";

   public QName[] getHeaders() {
      return WSAddressingConstants.WSA_HEADERS;
   }

   protected void handleInbound(WlMessageContext var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.getHeaders().listHeaders();

      while(var3.hasNext()) {
         MsgHeader var4 = (MsgHeader)var3.next();
         if (var4 instanceof UnknownMsgHeader) {
            var2.add(((UnknownMsgHeader)var4).getElement());
         }
      }

   }

   protected void handleOutbound(WlMessageContext var1) {
      List var2 = (List)var1.getProperty("weblogic.wsee.addressing.OutputHeaders");
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            Element var4 = (Element)var3.next();
            var1.getHeaders().addHeader(new UnknownMsgHeader(var4));
         }
      }

   }

   protected String getActionURI(WlMessageContext var1, boolean var2) {
      String var3 = (String)var1.getProperty("weblogic.wsee.addressing.Action");
      if (var3 == null) {
         var3 = AddressingHelper.getAddressingWSDLActionURI(var1, var2);
      }

      if (var3 == null) {
         var3 = this.getSOAPActionFromWSDLBinding(var1);
      }

      if (var3 == null || var3.length() == 0) {
         WsdlDefinitions var4 = var1.getDispatcher().getWsdlPort().getService().getDefinitions();
         String var5 = var4.getTargetNamespace();
         StringBuffer var6 = new StringBuffer(var5);
         if (!var5.endsWith("/")) {
            var6.append("/");
         }

         String var7 = var1.getDispatcher().getWsdlPort().getPortType().getName().getLocalPart();
         var6.append(var7);
         var6.append("/");
         var6.append(this.getMessageName(var1.getDispatcher().getOperation(), var2));
         var3 = var6.toString();
      }

      if (this.verbose) {
         Verbose.log((Object)("ActionURI " + var3));
      }

      return var3;
   }

   protected String getSOAPActionFromWSDLBinding(WlMessageContext var1) {
      if (var1.getDispatcher() == null) {
         return null;
      } else {
         WsdlBindingOperation var2 = var1.getDispatcher().getBindingOperation();
         if (var2 == null) {
            return null;
         } else if (!AsyncUtil.isSoap12(var1)) {
            SoapBindingOperation var4 = SoapBindingOperation.narrow(var2);
            return var4.getSoapAction();
         } else {
            Soap12BindingOperation var3 = Soap12BindingOperation.narrow(var2);
            return var3.getSoapAction();
         }
      }
   }

   protected void setSoapActionHeader(WlMessageContext var1, String var2) {
      if (!AsyncUtil.isSoap12(var1)) {
         ((SOAPMessageContext)var1).getMessage().getMimeHeaders().setHeader("SOAPAction", var2);
      } else {
         String[] var3 = ((SOAPMessageContext)var1).getMessage().getMimeHeaders().getHeader("Content-Type");
         if (var3 != null && var3.length == 1) {
            String var4 = var3[0];
            int var5 = var4.indexOf("action=");
            byte var6 = 7;
            if (var5 > 0) {
               String var7 = var4.substring(0, var5 + var6) + var2;
               int var8 = var4.indexOf(";", var5);
               if (var8 >= 0) {
                  var7 = var7 + var4.substring(var8);
               }

               ((SOAPMessageContext)var1).getMessage().getMimeHeaders().setHeader("Content-Type", var7);
            }
         }

      }
   }

   private String getMessageName(WsdlOperation var1, boolean var2) {
      return var2 ? var1.getInputName() : var1.getOutputName();
   }

   protected UsingAddressingPolicyInfo getAddressingPolicyInfo(MessageContext var1) {
      UsingAddressingPolicyInfo var2 = null;

      try {
         var2 = UsingAddressingPolicyInfoFactory.getInstance().getAddressigPolicyInfo(var1);
      } catch (PolicyException var4) {
         Verbose.log((Object)("Warning: Got exception when read UsingAddressing assertion from policy, exception is: " + var4));
         if (this.verbose) {
            Verbose.logException(var4);
         }
      }

      return var2;
   }
}
