package weblogic.wsee.codec.soap11;

import com.bea.xml.XmlException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.codec.soap12.Soap12Encoder;
import weblogic.wsee.mtom.api.MtomPolicyInfo;
import weblogic.wsee.mtom.api.MtomPolicyInfoFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.WsServiceImpl;
import weblogic.wsee.ws.WsType;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBody;

public class SoapEncoder {
   private SOAPMessage soapMessage;
   private WsdlBindingMessage message;
   private WsMethod method;
   private Map args;
   private RuntimeBindings bindings;
   private SerializerContext serContext;
   private SoapBody soapBody;
   protected MessageContext messageContext = null;
   private boolean useMTOMmessage = true;
   private static final String ENCODING_STYLE = "http://schemas.xmlsoap.org/soap/encoding/";
   private static final String SOAP12_ENCODING_STYLE = "http://www.w3.org/2003/05/soap-encoding";
   private static final boolean verbose = Verbose.isVerbose(SoapEncoder.class);

   protected SoapEncoder(SOAPMessage var1, WsdlBindingMessage var2, WsMethod var3, MessageContext var4) {
      this.soapMessage = var1;
      this.message = var2;
      this.method = var3;
      this.messageContext = var4;

      try {
         MtomPolicyInfo var5 = MtomPolicyInfoFactory.getInstance(var4);
         if (var5.isMtomDisable()) {
            this.useMTOMmessage = false;
         }

         if (verbose) {
            Verbose.log((Object)("useMTOMmessage set to '" + this.useMTOMmessage + "'"));
         }
      } catch (PolicyException var7) {
         this.useMTOMmessage = false;
         if (verbose) {
            Verbose.log((Object)(" unable to get MtomPolicyInfo.  useMTOMmessage will have value '" + this.useMTOMmessage + "'"));
         }
      }

      if (var4.getProperty("weblogic.wsee.xop.normal") != null) {
         try {
            var1.setProperty("weblogic.wsee.xop.normal", "true");
         } catch (SOAPException var6) {
            if (verbose) {
               Verbose.log((Object)" unable to enable normal xop");
            }
         }
      }

   }

   protected void encode(Map var1) throws SOAPException, CodecException, WsdlException, XmlException, XMLStreamException {
      this.args = var1;
      SOAPEnvelope var2 = this.soapMessage.getSOAPPart().getEnvelope();
      WsdlBindingOperation var3 = this.message.getBindingOperation();
      WsdlBinding var4 = var3.getBinding();
      SoapBinding var5 = this.getSoapBinding(var4);
      if (var5 == null) {
         throw new WsdlException("SoapBinding extension is not found for binding");
      } else {
         SoapBindingOperation var6 = this.getSoapBindingOperation(var3);
         if (var6 == null) {
            throw new WsdlException("SoapOperation extension is not found for operation");
         } else {
            this.addSoapActionHeader(this.soapMessage, var6);
            this.soapBody = this.getSoapBody(this.message);
            if (this.soapBody == null) {
               throw new WsdlException("SoapBody extension is not found for message");
            } else {
               Object var7 = null;
               String var8 = getStyle(var6, var5);
               if ("document".equals(var8)) {
                  if (this.method.isWrapped()) {
                     var7 = this.createWrapElementForDoc(var2);
                  } else {
                     var7 = var2.getBody();
                  }
               } else {
                  if (!"rpc".equals(var8)) {
                     throw new CodecException("unknown style '" + var8 + "'");
                  }

                  var7 = this.createWrapElementForRpc(var2);
               }

               this.encodeParts((SOAPElement)var7);
               if (this.soapMessage.getProperty("weblogic.wsee.xop.normal.set") != null) {
                  this.messageContext.setProperty("weblogic.wsee.xop.normal.set", this.soapMessage.getProperty("weblogic.wsee.xop.normal.set"));
                  this.soapMessage.setProperty("weblogic.wsee.xop.normal.set", (Object)null);
               }

            }
         }
      }
   }

   protected void addSoapActionHeader(SOAPMessage var1, SoapBindingOperation var2) {
      String var3 = var2.getSoapAction();
      if (var3 != null) {
         var3 = this.wrap(var3);
         if (verbose) {
            Verbose.log((Object)("Soap action: " + var3));
         }

         var1.getMimeHeaders().setHeader("SOAPAction", var3);
      } else {
         if (verbose) {
            Verbose.log((Object)"No soap action found using \"\"");
         }

         var1.getMimeHeaders().setHeader("SOAPAction", "\"\"");
      }

   }

   protected String wrap(String var1) {
      StringBuffer var2 = new StringBuffer();
      if (!var1.startsWith("\"")) {
         var2.append("\"");
      }

      var2.append(var1);
      if (!var1.endsWith("\"")) {
         var2.append("\"");
      }

      return var2.toString();
   }

   static String getStyle(SoapBindingOperation var0, SoapBinding var1) {
      String var2 = var0.getStyle();
      if (var2 != null && !"".equals(var2)) {
         return var2;
      } else {
         var2 = var1.getStyle();
         return var2 != null && !"".equals(var2) ? var2 : "document";
      }
   }

   private void encodeParts(SOAPElement var1) throws CodecException, SOAPException, XmlException, XMLStreamException, WsdlException {
      this.bindings = ((WsServiceImpl)this.method.getEndpoint().getService()).getBindingProvider();
      this.serContext = SerializationContextUtil.createSerializerContext(this.bindings, this.messageContext, this.soapBody.getUse(), this instanceof Soap12Encoder ? "http://www.w3.org/2003/05/soap-encoding" : "http://schemas.xmlsoap.org/soap/encoding/");
      if ("encoded".equals(this.soapBody.getUse()) && !(this instanceof Soap12Encoder)) {
         this.soapMessage.getSOAPPart().getEnvelope().setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
      }

      assert this.serContext != null;

      this.serContext.setMessage(this.soapMessage);
      this.serContext.setDocument(this.soapMessage.getSOAPPart());
      if (this.message.getType() == 0) {
         this.encodeParams(var1, this.soapMessage.getSOAPHeader());
      } else {
         this.encodeReturn(var1, this.soapMessage.getSOAPHeader());
      }

      this.serContext.serializeReferencedObjects(this.soapMessage.getSOAPBody());
   }

   private void encodeReturn(SOAPElement var1, SOAPHeader var2) throws XmlException, XMLStreamException, WsdlException {
      WsReturnType var3 = this.method.getReturnType();
      if (var3 != null) {
         Object var4 = this.args.get(var3.getName());
         this.encodePart(var1, var3, var4);
      }

      Iterator var8 = this.method.getParameters();

      while(true) {
         WsParameterType var5;
         do {
            if (!var8.hasNext()) {
               return;
            }

            var5 = (WsParameterType)var8.next();
         } while(var5.getMode() != 2 && var5.getMode() != 1);

         Object var6 = this.args.get(var5.getName());
         Object var7 = var5.isHeader() ? var2 : var1;
         this.encodePart((SOAPElement)var7, var5, var6);
      }
   }

   private void encodeParams(SOAPElement var1, SOAPHeader var2) throws XmlException, XMLStreamException, CodecException, WsdlException {
      int var3 = 0;
      Iterator var4 = this.method.getParameters();

      while(var4.hasNext()) {
         WsParameterType var5 = (WsParameterType)var4.next();
         if (verbose) {
            Verbose.log((Object)("Is header: " + var5.isHeader()));
         }

         if (var5.getMode() != 1) {
            Object var6 = var5.isHeader() ? var2 : var1;
            if (var3 >= this.args.size()) {
               throw new CodecException("Not enough args to invoke '" + this.method.getOperationName() + "'. Number of args = " + this.args.size() + " method = " + this.method);
            }

            this.encodePart((SOAPElement)var6, var5, this.args.get(var5.getName()));
            ++var3;
         }
      }

      if (var4.hasNext()) {
         throw new CodecException("Too many args to invoke '" + this.method.getOperationName() + "'. Number of args = " + this.args.size() + " method = " + this.method);
      }
   }

   private void encodePart(SOAPElement var1, WsType var2, Object var3) throws XmlException, XMLStreamException, WsdlException {
      if (verbose) {
         Verbose.logArgs("xml root", var1.getElementName(), "parameter", var2.getName(), "xml type", var2.getXmlName(), "java type", var2.getJavaType(), "value", var3);
      }

      String var4 = var2.getName();
      WsdlPart var5 = (WsdlPart)this.message.getMessage().getParts().get(var4);

      assert var5 != null;

      if (var2.isAnyXmlObject()) {
         QName var6 = null;
         if (this.method.isWrapped()) {
            var6 = var2.getElementName();
         } else if (var2.getElementName() != null) {
            var6 = var2.getElementName();
         } else {
            var6 = var2.getXmlName().getQName();
         }

         this.serContext.serializeXmlObjects(this.method.isWrapped(), var2.isXmlObjectForDocument() | var2.isArrayOfXmlObjectForDocument(), var2.isArrayOfXmlObjectForType() | var2.isArrayOfXmlObjectForDocument(), var1, var2.getJavaType(), var3, var6);
      } else if (this.method.isWrapped() && !var2.isHeader()) {
         this.serContext.serializeType(var1, var3, var2.getJavaType(), var2.getXmlName(), var2.getElementName(), this.useMTOMmessage, var5 == null ? null : WsdlUtils.getMimeType(var5.getName(), this.message));
      } else if (var5.getType() != null) {
         this.serializeType(var2.isHeader(), var4, var1, var3, var2, var5 == null ? null : WsdlUtils.getMimeType(var5.getName(), this.message));
      } else {
         this.serContext.serializeElement(var1, var3, var2.getJavaType(), var2.getXmlName(), this.useMTOMmessage, var5 == null ? null : WsdlUtils.getMimeType(var5.getName(), this.message));
      }

   }

   private void serializeType(boolean var1, String var2, SOAPElement var3, Object var4, WsType var5, String var6) throws XmlException, XMLStreamException {
      QName var7 = var1 ? new QName(this.soapBody.getNamespace(), var2) : new QName(var2);
      this.serContext.serializeType(var3, var4, var5.getJavaType(), var5.getXmlName(), var7, this.useMTOMmessage, var6);
   }

   private SOAPElement createWrapElementForDoc(SOAPEnvelope var1) throws SOAPException {
      QName var2 = null;
      if (this.message.getType() == 0) {
         var2 = this.method.getWrapperElement();
      } else {
         var2 = this.method.getReturnWrapperElement();
      }

      return this.addWrapper(var2.getNamespaceURI(), var2.getLocalPart(), var1);
   }

   private SOAPElement createWrapElementForRpc(SOAPEnvelope var1) throws SOAPException {
      String var2 = null;
      if (this.message.getType() == 0) {
         var2 = this.method.getOperationName().getLocalPart();
      } else {
         var2 = this.method.getOperationName().getLocalPart() + "Response";
      }

      String var3 = this.soapBody.getNamespace();
      return this.addWrapper(var3, var2, var1);
   }

   private SOAPElement addWrapper(String var1, String var2, SOAPEnvelope var3) throws SOAPException {
      Name var4 = var1 != null && !"".equals(var1) ? var3.createName(var2, "m", var1) : var3.createName(var2);
      SOAPBody var5 = var3.getBody();
      SOAPBodyElement var6 = var5.addBodyElement(var4);
      if (var1 != null && !"".equals(var1)) {
         var6.addNamespaceDeclaration("m", var1);
      }

      return var6;
   }

   protected SoapBody getSoapBody(WsdlBindingMessage var1) {
      return SoapBody.narrow(var1);
   }

   protected SoapBindingOperation getSoapBindingOperation(WsdlBindingOperation var1) {
      return SoapBindingOperation.narrow(var1);
   }

   protected SoapBinding getSoapBinding(WsdlBinding var1) {
      return SoapBinding.narrow(var1);
   }
}
