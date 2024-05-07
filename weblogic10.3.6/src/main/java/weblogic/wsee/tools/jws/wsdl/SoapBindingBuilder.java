package weblogic.wsee.tools.jws.wsdl;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.jws.decl.SOAPBindingDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingMessage;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBody;
import weblogic.wsee.wsdl.soap11.SoapFault;
import weblogic.wsee.wsdl.soap11.SoapHeader;
import weblogic.wsee.wsdl.soap11.SoapMessageBase;
import weblogic.wsee.wsdl.soap12.Soap12Binding;
import weblogic.wsee.wsdl.soap12.Soap12BindingOperation;
import weblogic.wsee.wsdl.soap12.Soap12Body;
import weblogic.wsee.wsdl.soap12.Soap12Fault;
import weblogic.wsee.wsdl.soap12.Soap12Header;

class SoapBindingBuilder {
   private WsdlDefinitionsBuilder definitions;
   private SOAPBindingDecl bindingDecl;
   boolean isSoap12 = false;
   private static final String ENCODING_STYLE = "http://schemas.xmlsoap.org/soap/encoding/";
   private static final String SOAP12_ENCODING_STYLE = "http://www.w3.org/2003/05/soap-encoding";

   SoapBindingBuilder(WsdlDefinitions var1) {
      this.definitions = (WsdlDefinitionsBuilder)var1;
   }

   WsdlBinding buildSoapBinding(WebServiceSEIDecl var1, WsdlPortTypeBuilder var2, PortDecl var3, boolean var4, int var5) throws WsdlException {
      this.isSoap12 = var4;
      this.bindingDecl = var1.getSoapBinding();
      String var6 = this.getBindingName(var1, var3.getProtocol(), var5);
      WsdlBindingBuilder var7 = this.definitions.addBinding(new QName(this.definitions.getTargetNamespace(), var6), var2);
      this.fillWsdlBinding(var7, var1, var2, var3);
      return var7;
   }

   private String getBindingName(WebServiceDecl var1, String var2, int var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var1.getServiceQName().getLocalPart() + "SoapBinding");
      if (var3 > 1) {
         var4.append(var2);
      }

      return var4.toString();
   }

   private void fillWsdlBinding(WsdlBindingBuilder var1, WebServiceSEIDecl var2, WsdlPortTypeBuilder var3, PortDecl var4) throws WsdlException {
      SoapBinding var5 = this.createSoapBinding(var1);
      var5.setStyle(this.bindingDecl.getStyle().toString().toLowerCase(Locale.ENGLISH));
      var5.setTransport(var4.getWsdlTransportNS());
      Iterator var6 = var2.getWebMethods();

      while(var6.hasNext()) {
         this.populateSoapOperation((WebMethodDecl)var6.next(), var3, var1);
      }

   }

   private void populateSoapOperation(WebMethodDecl var1, WsdlPortTypeBuilder var2, WsdlBindingBuilder var3) throws WsdlException {
      QName var4 = new QName(this.definitions.getTargetNamespace(), var1.getName());
      WsdlOperationBuilder var5 = (WsdlOperationBuilder)var2.getOperations().get(var4);

      assert var5 != null;

      WsdlBindingOperationBuilder var6 = var3.addOperation(var5);
      SoapBindingOperation var7 = this.createSoapBindingOperation(var6);
      var7.setSoapAction(var1.getAction().trim());
      var7.setStyle(var1.getSoapBinding().getStyle().toString().toLowerCase(Locale.ENGLISH));
      this.buildSoapInputMessage(var6, var1);
      if (!var1.isOneway()) {
         this.buildSoapOutputMessage(var6, var1);
      }

      this.buildSoapFaultMessage(var5, var6, var1);
   }

   private void buildSoapInputMessage(WsdlBindingOperationBuilder var1, WebMethodDecl var2) throws WsdlException {
      WsdlBindingMessageBuilder var3 = var1.createInput();
      this.buildSoapMessage(var3, var2, Mode.IN);
   }

   private void buildSoapOutputMessage(WsdlBindingOperationBuilder var1, WebMethodDecl var2) throws WsdlException {
      WsdlBindingMessageBuilder var3 = var1.createOutput();
      this.buildSoapMessage(var3, var2, Mode.OUT);
   }

   private void buildSoapMessage(WsdlBindingMessage var1, WebMethodDecl var2, WebParam.Mode var3) throws WsdlException {
      SoapBody var4 = this.createSoapBody(var1);
      this.setUse(var2, var4);
      if (!var2.getSoapBinding().isDocumentStyle()) {
         var4.setNamespace(this.definitions.getTargetNamespace());
      }

      StringBuffer var5 = new StringBuffer();
      if (var3 == Mode.OUT) {
         if (var2.getWebResult().hasReturn()) {
            var5.append(var2.getWebResult().getPartName());
         }
      } else if (var2.getSoapBinding().isDocLiteralWrapped()) {
         var5.append("parameters");
      }

      Iterator var6 = var2.getWebParams();

      while(true) {
         WebParamDecl var7;
         do {
            if (!var6.hasNext()) {
               if (var5.length() > 0) {
                  var4.setParts(var5.toString());
               }

               return;
            }

            var7 = (WebParamDecl)var6.next();
         } while(var7.getMode() != var3 && var7.getMode() != Mode.INOUT);

         if (var7.isHeader()) {
            SoapHeader var8 = this.createSoapHeader(var1);
            this.setUse(var2, var8);
            var8.setMessage(var1.getMessage().getName());
            var8.setPart(var7.getPartName());
            if (var5.length() == 0) {
               var5.append(" ");
            }
         } else if (!var2.getSoapBinding().isDocLiteralWrapped()) {
            if (var5.length() > 0) {
               var5.append(" ");
            }

            var5.append(var7.getPartName());
         }
      }
   }

   private void buildSoapFaultMessage(WsdlOperation var1, WsdlBindingOperationBuilder var2, WebMethodDecl var3) {
      Map var4 = var1.getFaults();

      SoapFault var9;
      for(Iterator var5 = var4.entrySet().iterator(); var5.hasNext(); var9.setUse(Use.LITERAL.toString().toLowerCase(Locale.ENGLISH))) {
         Map.Entry var6 = (Map.Entry)var5.next();
         String var7 = (String)var6.getKey();
         WsdlBindingMessageBuilder var8 = var2.createFault(var7);
         var9 = this.createSoapFault(var8);
         var9.setName(var7);
         if (!var3.getSoapBinding().isDocumentStyle() && !var3.getSoapBinding().isLiteral()) {
            var9.setNamespace(this.definitions.getTargetNamespace());
         }
      }

   }

   private void setUse(WebMethodDecl var1, SoapMessageBase var2) {
      var2.setUse(var1.getSoapBinding().getUse().toString().toLowerCase(Locale.ENGLISH));
      if (!var1.getSoapBinding().isLiteral()) {
         if (this.isSoap12) {
            var2.setEncodingStyle("http://www.w3.org/2003/05/soap-encoding");
         } else {
            var2.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");
         }
      }

   }

   private SoapBinding createSoapBinding(WsdlBinding var1) throws WsdlException {
      return (SoapBinding)(this.isSoap12 ? Soap12Binding.attach(var1) : SoapBinding.attach(var1));
   }

   private SoapBindingOperation createSoapBindingOperation(WsdlBindingOperation var1) {
      return (SoapBindingOperation)(this.isSoap12 ? Soap12BindingOperation.attach(var1) : SoapBindingOperation.attach(var1));
   }

   private SoapBody createSoapBody(WsdlBindingMessage var1) {
      return (SoapBody)(this.isSoap12 ? Soap12Body.attach(var1) : SoapBody.attach(var1));
   }

   private SoapHeader createSoapHeader(WsdlBindingMessage var1) {
      return (SoapHeader)(this.isSoap12 ? Soap12Header.attach(var1) : SoapHeader.attach(var1));
   }

   private SoapFault createSoapFault(WsdlBindingMessage var1) {
      return (SoapFault)(this.isSoap12 ? Soap12Fault.attach(var1) : SoapFault.attach(var1));
   }
}
