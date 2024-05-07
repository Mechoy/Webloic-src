package weblogic.wsee.codec.soap11;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.XmlException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.codec.Codec;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jaxrpc.soapfault.WLSOAPFaultException;
import weblogic.wsee.jws.wlw.JwsSoapFaultHelper;
import weblogic.wsee.jws.wlw.SoapFaultException;
import weblogic.wsee.jws.wlw.UnRecognizedFaultException;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.mtom.api.MtomPolicyInfo;
import weblogic.wsee.mtom.api.MtomPolicyInfoFactory;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.util.SaajUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLSOAPFactory;
import weblogic.wsee.ws.WsFault;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsServiceImpl;
import weblogic.wsee.ws.dispatch.Dispatcher;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlMessage;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.wsee.wsdl.soap11.SoapFault;
import weblogic.xml.saaj.SOAPMessageImpl;

public class SoapCodec implements Codec {
   private static final boolean verbose = Verbose.isVerbose(SoapCodec.class);
   public static final Name VOID_NAME_KEY;
   public static final String VALIDATING_DECODER = "weblogic.wsee.soap.validating_decoder";
   static final String PREFIX = "m";
   static final String DOCUMENT = "document";
   static final String RPC = "rpc";
   static final String LITERAL = "literal";
   static final String ENCODED = "encoded";
   static final short NOT_SPECIAL_EXCEPTION = 0;
   static final short WLW_SOAPFAULT_EXCEPTION = 1;
   static final short JAXRPC_SOAPFAULT_EXCEPTION = 2;
   static final boolean is81CustomException;

   public MessageContext createContext() {
      return new SoapMessageContext();
   }

   public void encode(MessageContext var1, WsdlBindingMessage var2, WsMethod var3, Map var4) throws CodecException {
      SoapMessageContext var5 = this.getSoapMessageContext(var1);

      try {
         MtomPolicyInfo var6 = MtomPolicyInfoFactory.getInstance(var1);
         boolean var7 = false;
         if (var6.isMtomOptional()) {
            if ((WlMessageContext)var1.getProperty("weblogic.wsee.mtom_message_recvd") != null) {
               var7 = true;
            }
         } else if (!var6.isMtomDisable()) {
            var7 = true;
         }

         SOAPMessage var8 = var5.clearMessage(var7);
         this.createEncoder(var8, var2, var3, var1).encode(var4);
      } catch (SOAPException var9) {
         throw new CodecException("Failed to encode message", var9);
      } catch (WsdlException var10) {
         throw new CodecException("Failed to encode message", var10);
      } catch (XmlException var11) {
         throw new CodecException("Failed to encode message", var11);
      } catch (XMLStreamException var12) {
         throw new CodecException("Failed to encode message", var12);
      } catch (PolicyException var13) {
         throw new CodecException("Failed to encode message", var13);
      }
   }

   protected SoapEncoder createEncoder(SOAPMessage var1, WsdlBindingMessage var2, WsMethod var3, MessageContext var4) {
      return new SoapEncoder(var1, var2, var3, var4);
   }

   public void decode(MessageContext var1, WsdlBindingMessage var2, WsMethod var3, Map var4) throws CodecException {
      if (verbose) {
         Verbose.here();
      }

      SoapMessageContext var5 = this.getSoapMessageContext(var1);
      SOAPMessage var6 = var5.getMessage();

      try {
         MtomPolicyInfo var7 = MtomPolicyInfoFactory.getInstance(var1);
         if (var7 != null && ((SOAPMessageImpl)var6).getIsMTOMmessage() && var7.isMtomDisable()) {
            throw new CodecException("non-MTOM endpoint received a MTOM message");
         } else {
            boolean var8 = false;
            if (var5.containsProperty("weblogic.wsee.soap.validating_decoder")) {
               var8 = (Boolean)var5.getProperty("weblogic.wsee.soap.validating_decoder");
            }

            this.createDecoder(var5, var2, var3).decode(var4, var8);
         }
      } catch (SOAPException var9) {
         throw new CodecException("Failed to encode message", var9);
      } catch (WsdlException var10) {
         throw new CodecException("Failed to decode message", var10);
      } catch (XmlException var11) {
         throw new CodecException("Failed to decode message", var11);
      } catch (XMLStreamException var12) {
         throw new CodecException("Failed to decode message", var12);
      } catch (PolicyException var13) {
         throw new CodecException("Failed to encode message", var13);
      }
   }

   protected SoapDecoder createDecoder(SOAPMessageContext var1, WsdlBindingMessage var2, WsMethod var3) {
      return new SoapDecoder(var1, var2, var3);
   }

   public QName getOperation(MessageContext var1) throws CodecException {
      SoapMessageContext var2 = this.getSoapMessageContext(var1);
      SOAPMessage var3 = var2.getMessage();

      try {
         WlMessageContext var4 = WlMessageContext.narrow(var1);
         Dispatcher var5 = var4.getDispatcher();
         ActionHeader var6 = (ActionHeader)var4.getHeaders().getHeader(ActionHeader.TYPE);
         String var7 = var6 == null ? null : var6.getActionURI();
         Map var8 = var5.getWsPort().getActionDispatchMap(var7);
         if (var8 == null) {
            var8 = var5.getWsPort().getSoapDispatchMap();
         }

         if (var8 == null) {
            throw new AssertionError("Soap dispatch map is null");
         } else {
            SOAPBody var9 = var3.getSOAPPart().getEnvelope().getBody();

            for(Node var10 = var9.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
               if (1 == var10.getNodeType() && var8.get(((SOAPElement)var10).getElementName()) != null) {
                  return (QName)var8.get(((SOAPElement)var10).getElementName());
               }
            }

            SOAPHeader var13 = var3.getSOAPHeader();
            if (var13 != null) {
               for(Node var11 = var13.getFirstChild(); var11 != null; var11 = var11.getNextSibling()) {
                  if (1 == var11.getNodeType() && var8.get(((SOAPElement)var11).getElementName()) != null) {
                     return (QName)var8.get(((SOAPElement)var11).getElementName());
                  }
               }
            }

            return (QName)var8.get(VOID_NAME_KEY);
         }
      } catch (SOAPException var12) {
         throw new CodecException("Failed to find web service method name", var12);
      }
   }

   public void decodeFault(MessageContext var1, Collection<? extends WsdlBindingMessage> var2, WsMethod var3) throws CodecException, SOAPException {
      SoapMessageContext var4 = this.getSoapMessageContext(var1);
      SOAPMessage var5 = var4.getMessage();
      if (!var5.getSOAPBody().hasFault()) {
         throw new CodecException("SOAP Body does not contain SOAP Fault");
      } else {
         SOAPFault var6 = var5.getSOAPBody().getFault();
         Object var7 = null;
         Detail var8 = var6.getDetail();
         SOAPElement var9 = this.getTopLevel(var8);
         if (var9 != null && var2 != null && var2.size() > 0 && var7 == null) {
            WsdlBindingMessage var10 = (WsdlBindingMessage)var2.iterator().next();
            SoapFault var11 = this.getSoapFault(var10);
            String var12 = null;
            if (var11 == null) {
               SoapBody var14 = this.getSoapBody(var10);
               if (var14 != null) {
                  var12 = var14.getUse();
               }
            } else {
               var12 = var11.getUse();
            }

            byte var13;
            if ("literal".equals(var12)) {
               var13 = 0;
               if (verbose) {
                  Verbose.log((Object)"  using LITERAL BindingContext");
               }
            } else {
               if (!"encoded".equals(var12)) {
                  throw new AssertionError("unknown encoding: " + var12);
               }

               var13 = 1;
               if (verbose) {
                  Verbose.log((Object)"  using SOAP11 BindingContext");
               }
            }

            WlMessageContext var24 = WlMessageContext.narrow(var1);
            String var15 = null;

            try {
               var15 = this.findStyle(var24.getDispatcher());
            } catch (WsdlException var17) {
               throw new CodecException(" WsdlException while trying to findStyle for Fault in wsMethod " + var3, var17);
            }

            try {
               var7 = this.createExceptionFromFault(var2, var3, var9, var5, var15, var13);
            } catch (Throwable var20) {
               if (verbose) {
                  Verbose.log((Object)(" got Throwable " + var20.getClass().getName() + " while attempting " + " to createExceptionFromFault. " + var20.getMessage()));
               }

               var7 = null;
            }
         }

         if (is81CustomException && var7 != null && ((Throwable)var7).getCause() == null) {
            try {
               SOAPFaultException var21 = new SOAPFaultException(this.getFaultCode(var6), var6.getFaultString(), var6.getFaultActor(), var6.getDetail());
               ((Throwable)var7).initCause(var21);
            } catch (RuntimeException var19) {
               if (verbose) {
                  Verbose.log((Object)("Exception in setting cause for current exception" + var19));
               }
            }
         }

         if (var7 == null && var1.getProperty("com.bea.SOAPFAULTS_CONTAIN_XMLBEANS") != null) {
            try {
               var7 = this.createExceptionFromFault(var6);
            } catch (UnRecognizedFaultException var18) {
               if (verbose) {
                  Verbose.log("Unable to unmarshal fault into a wlw SoapFaultException", var18);
               }

               var7 = null;
            }
         }

         if (var7 == null) {
            if (var6.getFaultCodeAsQName() == null) {
               throw new CodecException("SOAP Body does not contain a validate SOAP Fault!");
            }

            var7 = new WLSOAPFaultException(var6);
            if (verbose) {
               Verbose.log((Object)" could not create exception from known exception types.  Created general SOAPFaultException instead");
            }
         }

         WlMessageContext var22 = (WlMessageContext)var1;
         Throwable var23 = var22.getFault();
         if (var23 != null) {
            if (verbose) {
               Verbose.log((Object)(" Throwable from context.getFault() not null.  Setting initCause on exception to " + var23.getMessage()));
            }

            ((Throwable)var7).initCause(var23);
         }

         var22.setFault((Throwable)var7);
      }
   }

   protected SoapFault getSoapFault(WsdlBindingMessage var1) {
      return SoapFault.narrow(var1);
   }

   protected SoapBody getSoapBody(WsdlBindingMessage var1) {
      return SoapBody.narrow(var1);
   }

   protected SoapBinding getSoapBinding(WsdlBinding var1) {
      return SoapBinding.narrow(var1);
   }

   protected SoapBindingOperation getSoapBindingOperation(WsdlBindingOperation var1) {
      return SoapBindingOperation.narrow(var1);
   }

   protected void fillFault(SOAPFault var1, SoapFaultException var2) throws SOAPException {
      JwsSoapFaultHelper.fillFault(var1, 1, var2);
   }

   protected SoapFaultException createExceptionFromFault(SOAPFault var1) throws UnRecognizedFaultException {
      return JwsSoapFaultHelper.createExceptionFromFault(var1, 1);
   }

   public boolean encodeFault(MessageContext var1, WsMethod var2, Throwable var3) throws CodecException {
      this.getSoapMessageContext(var1).clearMessage();
      boolean var4 = false;
      byte var5 = 0;
      if (var3 instanceof SOAPFaultException) {
         var5 = 2;
      } else if (var3 instanceof SoapFaultException) {
         var5 = 1;
      }

      if (var5 != 0) {
         if (verbose) {
            Verbose.log((Object)("+++  encode Exception " + var3 + " as a special case   ++++"));
         }

         SOAPMessage var13 = ((SOAPMessageContext)var1).getMessage();

         try {
            SOAPBody var14 = var13.getSOAPBody();
            SOAPFault var8 = var14.addFault();
            if (var5 == 2) {
               SOAPFaultUtil.fillFault(var8, var3);
            } else {
               if (var5 != 1) {
                  throw new CodecException(" Unhandled special exception of type '" + var5 + "', " + "class '" + var3.getClass().getName() + "', '" + var3.getMessage() + "'");
               }

               this.fillFault(var8, (SoapFaultException)var3);
            }
         } catch (SOAPException var9) {
            throw new CodecException("Failed to encode", var9);
         }

         var4 = true;
         return true;
      } else {
         Iterator var6 = var2.getExceptions();

         while(var6.hasNext()) {
            WsFault var7 = (WsFault)var6.next();
            if (var7.getExceptionClass().equals(var3.getClass())) {
               try {
                  this.encodeFault(var1, var2, var7, var3);
               } catch (SOAPException var10) {
                  throw new CodecException("Failed to encode", var10);
               } catch (XmlException var11) {
                  throw new CodecException("Failed to encode", var11);
               } catch (XMLStreamException var12) {
                  throw new CodecException("Failed to encode", var12);
               }

               var4 = true;
            }
         }

         return var4;
      }
   }

   private boolean encodeWlwFault(MessageContext var1, WsMethod var2, Throwable var3) throws CodecException {
      this.getSoapMessageContext(var1).clearMessage();
      if (var3 instanceof SOAPFaultException) {
         if (verbose) {
            Verbose.log((Object)("+++  encode SOAPFaultException " + var3 + " as a special case   ++++"));
         }

         SOAPMessage var4 = ((SOAPMessageContext)var1).getMessage();

         try {
            SOAPBody var5 = var4.getSOAPBody();
            SOAPFault var6 = var5.addFault();
            SOAPFaultUtil.fillFault(var6, var3);
            return true;
         } catch (SOAPException var7) {
            throw new CodecException("Failed to encode", var7);
         }
      } else {
         return false;
      }
   }

   private Throwable createExceptionFromFault(Collection<? extends WsdlBindingMessage> var1, WsMethod var2, SOAPElement var3, SOAPMessage var4, String var5, int var6) throws CodecException {
      Throwable var7 = null;
      RuntimeBindings var8 = ((WsServiceImpl)var2.getEndpoint().getService()).getBindingProvider();
      Object var9 = null;

      try {
         SOAPEnvelope var10 = var4.getSOAPPart().getEnvelope();
         Iterator var11;
         if ("document".equals(var5)) {
            if (var2.isWrapped()) {
               var11 = var10.getBody().getChildElements();
               var9 = this.getFirstElement(var11);
            } else {
               var9 = var10.getBody();
            }
         } else {
            if (!"rpc".equals(var5)) {
               throw new CodecException("unknown style '" + var5 + "'");
            }

            var11 = var10.getBody().getChildElements();
            var9 = this.getFirstElement(var11);
            if (var9 == null) {
               throw new CodecException("For RPC style web service, Soap Body element must have a child element.");
            }
         }
      } catch (SOAPException var22) {
         throw new CodecException("Error while trying to find topElement from SOAPMessage " + var4, var22);
      }

      DeserializerContext var24 = var8.createDeserializerContext(var6, (Element)var9, false);
      var24.setMessage(var4);
      QName var25 = this.findXsiType(var4, var3);
      QName var12 = new QName(var3.getElementName().getURI(), var3.getElementName().getLocalName());
      Object var13 = null;
      Iterator var14 = var2.getExceptions();
      boolean var15 = false;
      int var16 = 0;
      WsFault var17 = null;

      WsdlPart var19;
      while(var14.hasNext() && var7 == null) {
         WsFault var18 = (WsFault)var14.next();
         var19 = this.getPart(var18);
         if (var19.getElement() != null) {
            if (var19.getElement().equals(var12)) {
               var15 = true;
               var17 = var18;
               break;
            }
         } else if (var3.getElementName().getLocalName().equals(var19.getName())) {
            ++var16;
            var17 = var18;
         }
      }

      WsdlPart var20;
      if (!var15 && var16 > 1) {
         var17 = null;
         Iterator var26 = var2.getExceptions();

         while(var26.hasNext()) {
            WsFault var28 = (WsFault)var14.next();
            var20 = this.getPart(var28);
            if (var20.getType() != null && var20.getType().equals(var25)) {
               var17 = var28;
               break;
            }
         }
      }

      if (var17 == null) {
         if (verbose) {
            Verbose.log((Object)("could not find matching fault for faultElement '" + var12 + "', xsiType '" + var25 + "'"));
         }

         return null;
      } else {
         if (verbose) {
            Verbose.log((Object)("found matching fault: '" + var17 + "', for faultElement '" + var12 + "'"));
         }

         boolean var27 = var17.marshalProperty();
         var19 = null;
         var20 = null;
         Class var30;
         if (var27) {
            var30 = var17.getMarshalPropertyClass();
         } else {
            var30 = var17.getExceptionClass();
         }

         try {
            WsdlPart var21 = this.getPart(var17);
            XmlTypeName var29;
            if (var21.getElement() != null) {
               var29 = XmlTypeName.forGlobalName('e', var21.getElement());
               if (verbose) {
                  Verbose.log((Object)("createExceptionFromFault:   about to call deserializeElement with , exceptionPropertyClass " + var30.getName() + ", xmlElementName " + var29 + ", on element with tag name '" + var3.getTagName() + "'"));
               }

               var13 = var24.deserializeElement(var3, var30, var29, false);
               if (var27) {
                  var7 = (Throwable)var17.getMarshalPropertyExceptionConstructor().newInstance(var13);
               } else {
                  var7 = (Throwable)var13;
               }
            } else {
               if (var27) {
                  var29 = XmlTypeName.forGlobalName('t', var17.getMarshalPropertyQName());
               } else {
                  var29 = XmlTypeName.forGlobalName('t', var21.getType());
               }

               if (verbose) {
                  Verbose.log((Object)("createExceptionFromFault:   about to call deserializeType with , exceptionPropertyClass " + var30.getName() + ", message part type " + var21.getType() + ", xmlTypeName " + var29 + ", on element with tag name '" + var3.getTagName() + "'"));
               }

               var13 = var24.deserializeType(var3, var30, var29, false);
               if (var27) {
                  var7 = (Throwable)var17.getMarshalPropertyExceptionConstructor().newInstance(var13);
               } else {
                  var7 = (Throwable)var13;
               }
            }
         } catch (Throwable var23) {
            if (verbose) {
               Verbose.logException(var23);
            }

            throw new CodecException("Failed to decode exception for name " + (var19 == null ? "<null>" : var19) + ", " + var23);
         }

         if (verbose) {
            Verbose.log((Object)(" deserialization complete, exception is " + var7));
         }

         return var7;
      }
   }

   private SOAPElement getTopLevel(Detail var1) {
      if (var1 == null) {
         return null;
      } else {
         Iterator var2 = var1.getChildElements();

         Object var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = var2.next();
         } while(!(var3 instanceof SOAPElement));

         return (SOAPElement)var3;
      }
   }

   private WsdlPart getPart(WsFault var1) {
      WsdlMessage var2 = var1.getFaultMessage();
      Iterator var3 = var2.getParts().values().iterator();
      if (var3.hasNext()) {
         WsdlPart var4 = (WsdlPart)var3.next();
         return var4;
      } else {
         throw new IllegalArgumentException("A fault message must have at leastone part");
      }
   }

   private QName findXsiType(SOAPMessage var1, SOAPElement var2) {
      Name var3 = null;

      try {
         var3 = var1.getSOAPPart().getEnvelope().createName("type");
      } catch (Throwable var8) {
         return null;
      }

      String var4 = var2.getAttributeValue(var3);
      if (var4 == null) {
         return null;
      } else if (var4.indexOf(":") != -1) {
         String var5 = var4.substring(0, var4.indexOf(":"));
         String var6 = var4.substring(var4.indexOf(":") + 1);
         String var7 = var2.getNamespaceURI(var5);
         return var7 == null ? new QName(var4) : new QName(var7, var6);
      } else {
         return new QName(var4);
      }
   }

   private void encodeFault(MessageContext var1, WsMethod var2, WsFault var3, Throwable var4) throws SOAPException, XmlException, XMLStreamException {
      SOAPMessage var5 = ((SOAPMessageContext)var1).getMessage();
      RuntimeBindings var6 = ((WsServiceImpl)var2.getEndpoint().getService()).getBindingProvider();
      SerializerContext var7 = null;
      WlMessageContext var8 = WlMessageContext.narrow(var1);
      WsdlBindingOperation var9 = var8.getDispatcher().getBindingOperation();
      WsdlMessage var10 = var3.getFaultMessage();
      WsdlBindingMessage var11 = null;
      Iterator var12 = var9.getFaults().values().iterator();

      while(var12.hasNext()) {
         WsdlBindingMessage var13 = (WsdlBindingMessage)var12.next();
         WsdlMessage var14 = null;

         try {
            var14 = var13.getMessage();
            if (var14.equals(var10)) {
               var11 = var13;
            }
         } catch (Throwable var22) {
            Verbose.log((Object)("WsdlBindingMessage.getMessage caught " + var22.getMessage()));
         }
      }

      if (var11 != null) {
         if (verbose) {
            Verbose.log((Object)("\n\n ++++ encodeFault matched up wsdlBindingMessage for fault: " + var3 + ", wsdlBindinMessage is: " + var11));
         }

         SoapFault var24 = SoapFault.narrow(var11);
         if (var24 != null) {
            String var26 = var24.getUse();
            var7 = SerializationContextUtil.createSerializerContext(var6, var1, var26, var24.getEncodingStyle());
         } else if (verbose) {
            Verbose.log((Object)("\n\n ++++ encodeFault COULD NOT get SoapFault from the wsdlBindingMessage for fault: " + var3));
         }
      } else if (verbose) {
         Verbose.log((Object)("\n\n ++++ encodeFault COULD NOT match up wsdlBindingMessage for fault: " + var3));
      }

      if (var7 == null) {
         if (verbose) {
            Verbose.log((Object)("\n\n ++++ encodeFault for fault: " + var3 + " use LITERAL serializer as a default"));
         }

         var7 = var6.createSerializerContext(0);
      }

      var7.setMessage(var5);
      var7.setDocument(var5.getSOAPPart());
      SOAPBody var25 = var5.getSOAPBody();
      SOAPFault var27 = var25.addFault();
      var27.setFaultString(var4.getMessage());
      Detail var28 = var27.addDetail();
      WsdlPart var15 = this.getPart(var3);
      Method var16;
      Object var17;
      if (var3.marshalProperty()) {
         if (verbose) {
            Verbose.log((Object)("+++++  encodeFault Serialize xsd builtin pn wire for fault " + var3));
         }

         var16 = var3.getMarshalPropertyGetterMethod();
         if (var16 != null) {
            var17 = null;

            try {
               var17 = var16.invoke(var4);
            } catch (Exception var23) {
               if (verbose) {
                  Verbose.log((Object)(" encodeFault Could not serialize user exception.  Unable to execute getter on exception " + var16.getName() + " due to " + var23.getMessage()));
               }

               return;
            }

            Class var18 = var3.getMarshalPropertyClass();
            QName var19 = var3.getMarshalPropertyQName();
            XmlTypeName var20 = XmlTypeName.forTypeNamed(var19);
            QName var21 = null;
            if (var15.getElement() == null) {
               var21 = new QName(var15.getName());
               if (verbose) {
                  Verbose.log((Object)("++++ encodeFault serialize as TYPE, class '" + var18 + "', using XmlTypeName '" + var20 + "', partName '" + var21 + "'"));
               }

               var7.serializeType(var28, var17, var18, var20, var21, false, WsdlUtils.getMimeType(var15.getName(), var11));
            } else {
               var20 = XmlTypeName.forGlobalName('e', var15.getElement());
               if (verbose) {
                  Verbose.log((Object)("++++ encodeFault serialize as ELEMENT, class '" + var18 + "', using XmlTypeName '" + var20 + "'"));
               }

               var7.serializeElement(var28, var17, var18, var20, false, WsdlUtils.getMimeType(var15.getName(), var11));
            }
         }
      } else {
         if (verbose) {
            Verbose.log((Object)("++++  encodeFault Serialize complexException for fault " + var3));
         }

         var16 = null;
         var17 = null;
         XmlTypeName var29;
         if (var15.getElement() == null) {
            var29 = XmlTypeName.forGlobalName('t', var15.getType());
            QName var30 = new QName(var15.getName());
            if (verbose) {
               Verbose.log((Object)("++++ encodeFault Serialize as TYPE, using XmlTypeName '" + var29 + "'"));
            }

            var7.serializeType(var28, var4, var3.getExceptionClass(), var29, var30, false, WsdlUtils.getMimeType(var15.getName(), var11));
         } else {
            var29 = XmlTypeName.forGlobalName('e', var15.getElement());
            if (verbose) {
               Verbose.log((Object)("++++  encodeFault Serialize as ELEMENT, using XmlTypeName '" + var29 + "'"));
            }

            var7.serializeElement(var28, var4, var3.getExceptionClass(), var29, false, WsdlUtils.getMimeType(var15.getName(), var11));
         }
      }

      if (verbose) {
         Verbose.log((Object)("++++  encodeFault Exception encoded " + var5));
      }

   }

   private String findStyle(Dispatcher var1) throws WsdlException {
      Iterator var2 = var1.getWsdlPort().getPortType().getOperations().values().iterator();
      WsdlOperation var3 = (WsdlOperation)var2.next();
      WsdlBindingOperation var4 = (WsdlBindingOperation)var1.getWsdlPort().getBinding().getOperations().get(var3.getName());
      SoapBinding var5 = this.getSoapBinding(var4.getBinding());
      if (var5 == null) {
         throw new WsdlException("SoapBinding extension is not found for binding");
      } else {
         SoapBindingOperation var6 = this.getSoapBindingOperation(var4);
         if (var6 == null) {
            throw new WsdlException("SoapOperation extension is not found for operation");
         } else {
            return SoapEncoder.getStyle(var6, var5);
         }
      }
   }

   private QName getFaultCode(SOAPFault var1) {
      Name var2 = var1.getFaultCodeAsName();
      if (var2 == null) {
         return null;
      } else {
         QName var3 = SaajUtil.qnameFromName(var2);
         return var3;
      }
   }

   private SoapMessageContext getSoapMessageContext(MessageContext var1) throws CodecException {
      if (!(var1 instanceof SoapMessageContext)) {
         throw new CodecException("SoapCodec can only handle SOAPMessageContext but found '" + var1.getClass() + "'");
      } else {
         SoapMessageContext var2 = (SoapMessageContext)var1;
         return var2;
      }
   }

   private SOAPElement getFirstElement(Iterator var1) {
      while(true) {
         if (var1.hasNext()) {
            Object var2 = var1.next();
            if (!(var2 instanceof SOAPElement)) {
               continue;
            }

            return (SOAPElement)var2;
         }

         return null;
      }
   }

   static {
      SOAPFactory var0 = WLSOAPFactory.createSOAPFactory();
      String var1 = "something_unlikely";
      String var2 = "http://void.operation.org";

      try {
         VOID_NAME_KEY = var0.createName(var1, (String)null, var2);
      } catch (SOAPException var4) {
         throw new IllegalStateException("Failed to create javax.xml.soap.Name with namespace " + var2 + " localpart " + var1);
      }

      is81CustomException = Boolean.getBoolean("weblogic.wsee.soap.81CustomException");
   }
}
