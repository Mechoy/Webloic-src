package weblogic.wsee.codec.soap11;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xbeanmarshal.buildtime.internal.util.XmlBeanUtil;
import com.bea.xbeanmarshal.runtime.ElementWildcardHelper;
import com.bea.xml.XmlException;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.util.HeaderUtil;
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
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.xml.saaj.SOAPMessageImpl;

public class SoapDecoder {
   private SOAPMessage soapMessage;
   private WsdlBindingMessage message;
   private WsMethod method;
   private SoapBody soapBody;
   private RuntimeBindings bindings;
   private boolean isMTOMmessage;
   private static final boolean verbose = Verbose.isVerbose(SoapDecoder.class);
   private static boolean WRITE_ELEMENT_WILDCARD_ARRAY_WRAPPER = false;
   private SOAPMessageContext soapMessageCtx;

   protected SoapDecoder(SOAPMessageContext var1, WsdlBindingMessage var2, WsMethod var3) {
      this.soapMessageCtx = var1;
      this.soapMessage = var1.getMessage();
      this.message = var2;
      this.method = var3;
      this.isMTOMmessage = ((SOAPMessageImpl)this.soapMessage).getIsMTOMmessage();
   }

   public void decode(Map var1, boolean var2) throws WsdlException, SOAPException, CodecException, XMLStreamException, XmlException {
      if (verbose) {
         Verbose.log((Object)"Decoding SOAP Message");
      }

      SOAPEnvelope var3 = this.soapMessage.getSOAPPart().getEnvelope();
      WsdlBindingOperation var4 = this.message.getBindingOperation();
      SoapBinding var5 = this.getSoapBinding(var4.getBinding());
      if (var5 == null) {
         throw new WsdlException("SoapBinding extension is not found for binding");
      } else {
         SoapBindingOperation var6 = this.getSoapBindingOperation(var4);
         if (var6 == null) {
            throw new WsdlException("SoapOperation extension is not found for operation");
         } else {
            Object var7 = null;
            String var8 = SoapEncoder.getStyle(var6, var5);
            Iterator var9;
            if ("document".equals(var8)) {
               if (this.method.isWrapped()) {
                  var9 = var3.getBody().getChildElements();
                  var7 = this.getFirstElement(var9);
               } else {
                  var7 = var3.getBody();
               }
            } else {
               if (!"rpc".equals(var8)) {
                  throw new CodecException("unknown style '" + var8 + "'");
               }

               var9 = var3.getBody().getChildElements();
               var7 = this.getFirstElement(var9);
               if (var7 == null) {
                  throw new CodecException("For RPC style web service, Soap Body element must have a child element.");
               }
            }

            this.decodeParts((SOAPElement)var7, var1, var2);
            HeaderUtil.checkMustUnderstandHeader(var3.getHeader());
         }
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

   private void decodeParts(SOAPElement var1, Map var2, boolean var3) throws WsdlException, CodecException, XMLStreamException, XmlException {
      this.bindings = ((WsServiceImpl)this.method.getEndpoint().getService()).getBindingProvider();
      this.soapBody = this.getSoapBody(this.message);
      if (this.soapBody == null) {
         throw new WsdlException("SoapBody extension is not found for message");
      } else {
         byte var4;
         if ("literal".equals(this.soapBody.getUse())) {
            var4 = 0;
         } else {
            if (!"encoded".equals(this.soapBody.getUse())) {
               throw new AssertionError("unknown encoding: " + this.soapBody.getUse());
            }

            var4 = 1;
         }

         DeserializerContext var5 = this.bindings.createDeserializerContext(var4, var1.getOwnerDocument().getDocumentElement(), var3);
         var5.setMessage(this.soapMessage);
         var5.setMessageContext(this.soapMessageCtx);
         Iterator var6 = var1.getChildElements();
         if (this.message.getType() == 0) {
            this.decodeParams(var6, var2, var5);
         } else {
            this.decodeReturn(var6, var2, var5);
         }

         SOAPElement var7 = this.getNextElement(var6);
         if (var7 != null) {
            throw new CodecException("Found more elements in the soap envelope than required by WSDL:" + var7.getElementName() + ". WSDL Message" + " for this operation is: " + this.message);
         }
      }
   }

   private void decodeParams(Iterator var1, Map var2, DeserializerContext var3) throws XMLStreamException, XmlException, WsdlException, CodecException {
      SOAPElement var4 = null;
      Iterator var5 = this.method.getParameters();

      while(true) {
         while(true) {
            WsParameterType var6;
            do {
               if (!var5.hasNext()) {
                  return;
               }

               var6 = (WsParameterType)var5.next();
            } while(var6.getMode() != 2 && var6.getMode() != 0);

            boolean var8 = false;
            SOAPElement var7;
            if (var6.isHeader()) {
               var7 = this.getHeaderElement(var6);
               var8 = true;
            } else {
               if (var4 == null) {
                  var7 = this.getNextElement(var1);
               } else {
                  var7 = var4;
                  var4 = null;
               }

               if (this.isOptionalParam(var6)) {
                  if (var7 == null) {
                     var2.put(var6.getName(), (Object)null);
                     continue;
                  }

                  if (!this.match(var7, var6)) {
                     var2.put(var6.getName(), (Object)null);
                     if (!var5.hasNext()) {
                        throw new CodecException("Found more elements in the soap envelope than required by WSDL:" + var7.getElementName() + ". WSDL Message" + " for this operation is: " + this.message);
                     }

                     var4 = var7;
                     continue;
                  }
               }
            }

            if (!var6.getXmlName().isElementWildcardArrayType() && !var6.getXmlName().isElementWildcardType()) {
               if (var6.getJavaType().getName().equals(WildcardUtil.SOAPELEMENT_CLASSNAME)) {
                  if (var7 == null) {
                     var2.put(var6.getName(), (Object)null);
                     continue;
                  }
               } else {
                  this.checkNullElement(var7, var6);
               }
            } else {
               if (var7 == null) {
                  var2.put(var6.getName(), (Object)null);
                  continue;
               }

               if (var6.getXmlName().isElementWildcardArrayType() && !WRITE_ELEMENT_WILDCARD_ARRAY_WRAPPER) {
                  try {
                     var7 = ElementWildcardHelper.createSyntheticWrappedArray(var7, var1, verbose);
                  } catch (Throwable var10) {
                     throw new CodecException(" Error while trying to create array element wrapper for extensibility elements.  Exception '" + var10.getMessage() + "'");
                  }
               }
            }

            Object var9 = this.decodePart(var6, var7, var3);
            var2.put(var6.getName(), var9);
            if (var8) {
               var7.getParentElement().removeChild(var7);
            }
         }
      }
   }

   private boolean isOptionalParam(WsParameterType var1) {
      return var1.isOptionalElement();
   }

   private boolean match(SOAPElement var1, WsParameterType var2) {
      XmlTypeName var3 = var2.getXmlName();
      if (var3 == null || !var3.isElementWildcardArrayType() && !var3.isElementWildcardType() && !var3.isElementWildcardArrayElement() && !var3.isElementWildcardElement()) {
         QName var4 = var2.getElementName();
         if (var4 == null && var2.getXmlName() != null && var2.getXmlName().isElement()) {
            var4 = var2.getXmlName().getQName();
         }

         return var1 != null && var1.getElementQName() != null && var1.getElementQName().equals(var4);
      } else {
         return true;
      }
   }

   private SOAPElement getHeaderElement(WsParameterType var1) throws CodecException {
      try {
         SOAPHeader var2 = this.soapMessage.getSOAPHeader();
         if (var2 == null) {
            throw new CodecException("Soap header element is not found");
         } else {
            Iterator var3 = var2.getChildElements();

            while(var3.hasNext()) {
               SOAPElement var4 = this.getNextElement(var3);
               if (var4 == null) {
                  break;
               }

               XmlTypeName var5 = var1.getXmlName();
               if (var5.isElement()) {
                  QName var6 = var5.getQName();
                  String var7 = var4.getNamespaceURI();
                  if (var7 == null) {
                     var7 = "";
                  }

                  if (var6.getLocalPart().equals(var4.getLocalName()) && var6.getNamespaceURI().equals(var7)) {
                     return var4;
                  }
               } else if (var1.getName().equals(var4.getLocalName())) {
                  return var4;
               }
            }

            return null;
         }
      } catch (SOAPException var8) {
         throw new CodecException("failed to get soap header", var8);
      }
   }

   private void checkNullElement(SOAPElement var1, WsParameterType var2) throws CodecException {
      if (var1 == null) {
         throw new CodecException("Unable to find xml element for parameter: " + var2.getName());
      } else {
         XmlTypeName var3 = var2.getXmlName();
         if (var3.isElement()) {
            QName var6 = var3.getQName();
            String var5 = var1.getNamespaceURI();
            if (var5 == null) {
               var5 = "";
            }

            if (!var6.getLocalPart().equals(var1.getLocalName()) || !var6.getNamespaceURI().equals(var5)) {
               throw new CodecException("Unable to find xml element for parameter: " + var2.getName());
            }
         } else {
            String var4 = var3.toString();
            var4 = var4 == null ? "" : var4;
            if (var4.startsWith("t=") && !var2.getName().equals(var1.getLocalName())) {
               throw new CodecException("Unable to find xml element for parameter: " + var2.getName());
            }
         }

      }
   }

   private void decodeReturn(Iterator var1, Map var2, DeserializerContext var3) throws XMLStreamException, XmlException, WsdlException, CodecException {
      boolean var4 = false;
      WsReturnType var5 = this.method.getReturnType();
      if (var5 != null) {
         SOAPElement var6 = this.getNextElement(var1);
         if (var6 != null) {
            if (var5.getXmlName().isElementWildcardArrayType()) {
               var4 = true;

               try {
                  var6 = ElementWildcardHelper.createSyntheticWrappedArray(var6, var1, verbose);
               } catch (Throwable var13) {
                  throw new CodecException(" Error while trying to create array element wrapper for extensibility elements.  Exception '" + var13.getMessage() + "'");
               }
            }

            var2.put(var5.getName(), this.decodePart(var5, var6, var3));
         } else {
            var2.put(var5.getName(), (Object)null);
         }
      }

      Iterator var14 = this.method.getParameters();

      while(true) {
         WsParameterType var7;
         do {
            if (!var14.hasNext()) {
               return;
            }

            var7 = (WsParameterType)var14.next();
         } while(var7.getMode() != 2 && var7.getMode() != 1);

         SOAPElement var8 = var7.isHeader() ? this.getHeaderElement(var7) : this.getNextElement(var1);
         if (!var7.isHeader()) {
            if (var4) {
               String var9 = "";

               try {
                  var9 = XmlBeanUtil.toXMLString(this.soapMessage.getSOAPPart().getEnvelope().getBody());
               } catch (Throwable var11) {
               }

               throw new IllegalArgumentException(" WebService operation='" + this.message.getBindingOperation().getName() + "' has a return type that is mapped to an unbounded element wildcard <any maxOccurs='unbounded'../>. " + "\n This operation also has parameters that are either INOUT or OUT parameters. " + "\n Because the names of the element wildcard array elements are indeterminant, " + "\n the xml decoder cannot tell where the return type wildcard array elements end and where the " + "INOUT or OUT parameters begin.  Decoding of this message is aborted." + "\n  Message: \n\n" + var9 + "\n\n\n");
            }

            if (var7.getXmlName().isElementWildcardArrayType()) {
               try {
                  var8 = ElementWildcardHelper.createSyntheticWrappedArray(var8, var1, verbose);
               } catch (Throwable var12) {
                  throw new CodecException(" Error while trying to create array element wrapper for extensibility elements.  Exception '" + var12.getMessage() + "'");
               }
            }
         } else {
            HeaderUtil.removeMustUnderstandFromHeader(var8);
         }

         var2.put(var7.getName(), this.decodePart(var7, var8, var3));
      }
   }

   private SOAPElement getNextElement(Iterator var1) {
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

   private Object decodePart(WsType var1, SOAPElement var2, DeserializerContext var3) throws XMLStreamException, XmlException, WsdlException {
      String var4 = var1.getName();
      WsdlPart var5 = (WsdlPart)this.message.getMessage().getParts().get(var4);
      Object var6;
      if (var1.isAnyXmlObject()) {
         var6 = var3.deserializeXmlObjects(this.method.isWrapped(), var1.isXmlObjectForDocument() | var1.isArrayOfXmlObjectForDocument(), var1.isArrayOfXmlObjectForType() | var1.isArrayOfXmlObjectForDocument(), var2, var1.getJavaType(), var1.getElementName());
      } else if (this.method.isWrapped() && !var1.isHeader()) {
         var6 = var3.deserializeWrappedElement(var2, var1.getJavaType(), var1.getXmlName(), this.isMTOMmessage);
      } else if (var5.getType() != null) {
         var6 = var3.deserializeType(var2, var1.getJavaType(), var1.getXmlName(), this.isMTOMmessage);
      } else {
         var6 = var3.deserializeElement(var2, var1.getJavaType(), var1.getXmlName(), this.isMTOMmessage);
      }

      if (verbose) {
         Verbose.logArgs("name", var1.getName(), "XML root", var2.getElementName(), "value", var6);
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
