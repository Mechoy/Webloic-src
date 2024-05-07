package weblogic.wsee.tools.jws.wsdl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import java.util.ArrayList;
import java.util.Iterator;
import javax.jws.WebParam.Mode;
import javax.xml.namespace.QName;
import weblogic.jws.Binding;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.CallbackServiceDecl;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.builder.WsdlTypesBuilder;
import weblogic.wsee.wsdl.soap11.SoapAddress;
import weblogic.wsee.wsdl.soap12.Soap12Address;

public class WsdlBuilder extends JAXRPCProcessor {
   private WsdlDefinitionsBuilder definitions;
   private BuildtimeBindings bindings;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getCowReader() == null) {
         this.bindings = var1.createBindings();

         try {
            WebServiceSEIDecl var2 = var1.getWebService();
            var1.setDefinitions(this.createDefinitions(var2));
            this.buildWsdl(var2);
         } catch (WsdlException var3) {
            throw new WsBuildException("Failed to generate WSDL", var3);
         }
      }
   }

   private WsdlDefinitions createDefinitions(WebServiceSEIDecl var1) {
      WsdlFactory var2 = WsdlFactory.getInstance();
      this.definitions = var2.create();
      this.definitions.setName(var1.getServiceName() + "Definitions");
      this.definitions.setTargetNamespace(var1.getTargetNamespace());
      return this.definitions;
   }

   private void buildWsdl(WebServiceSEIDecl var1) throws WsdlException, WsBuildException {
      this.addTypes();
      WsdlServiceBuilder var2 = this.buildService(var1);
      WsdlPortTypeBuilder var3 = this.buildPortType(var1);
      this.buildBindings(var1, var2, var3, true);
      this.addCallBack(var1);
   }

   private boolean isSoap12(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(Binding.class);
      return var2 != null ? Binding.Type.SOAP12.toString().equals(var2.getValue("value").asString()) : false;
   }

   private void buildBindings(WebServiceSEIDecl var1, WsdlServiceBuilder var2, WsdlPortTypeBuilder var3, boolean var4) throws WsdlException {
      ArrayList var5 = new ArrayList();
      Iterator var6 = var1.getPorts();

      while(var6.hasNext()) {
         var5.add(var6.next());
      }

      var6 = var5.iterator();

      while(var6.hasNext()) {
         PortDecl var7 = (PortDecl)var6.next();
         SoapBindingBuilder var8 = new SoapBindingBuilder(this.definitions);
         boolean var9 = this.isSoap12(var1.getEIClass());
         WsdlBinding var10 = var8.buildSoapBinding(var1, var3, var7, var9, var5.size());
         WsdlPort var11 = this.buildSoapPort(var7, var2, var10);
         if (var4) {
            if (var9) {
               Soap12Address var12 = Soap12Address.attach(var11);
               var12.setLocation(var7.getURI());
            } else {
               SoapAddress var13 = SoapAddress.attach(var11);
               var13.setLocation(var7.getURI());
            }
         }
      }

   }

   private WsdlPort buildSoapPort(PortDecl var1, WsdlServiceBuilder var2, WsdlBinding var3) {
      QName var4 = new QName(this.definitions.getTargetNamespace(), var1.getPortName());
      return var2.addPort(var4, var3);
   }

   private void addCallBack(WebServiceSEIDecl var1) throws WsdlException, WsBuildException {
      CallbackServiceDecl var2 = var1.getCallbackService();
      if (var2 != null) {
         WsdlServiceBuilder var3 = this.buildService(var2);
         WsdlPortTypeBuilder var4 = this.buildPortType(var2);
         this.buildBindings(var2, var3, var4, false);
      }

   }

   private WsdlServiceBuilder buildService(WebServiceSEIDecl var1) {
      return this.definitions.addService(var1.getServiceQName());
   }

   private void addTypes() throws WsdlException {
      WsdlTypesBuilder var1 = this.definitions.addTypes();
      SchemaDocument[] var2 = this.bindings.getSchemas();
      var1.addSchemas(var2);
   }

   private WsdlPortTypeBuilder buildPortType(WebServiceSEIDecl var1) throws WsBuildException {
      WsdlPortTypeBuilder var2 = this.definitions.addPortType(new QName(this.definitions.getTargetNamespace(), var1.getPortType()));
      Iterator var3 = var1.getWebMethods();

      while(var3.hasNext()) {
         this.populateOperation(var2, (WebMethodDecl)var3.next());
      }

      return var2;
   }

   private void populateOperation(WsdlPortTypeBuilder var1, WebMethodDecl var2) throws WsBuildException {
      QName var3 = new QName(this.definitions.getTargetNamespace(), var2.getName());
      WsdlOperationBuilder var4 = var1.addOperation(var3);
      WsdlMessageBuilder var5 = this.buildInputMessage(var3, var2);
      this.setParameterOrder(var4, var2);
      var4.setInput(var5);
      if (!var2.isOneway()) {
         WsdlMessageBuilder var6 = this.buildOutputMessage(var3, var2);
         var4.setOutput(var6);
      }

      this.buildFaultMessage(var4, var2.getJMethod());
   }

   private void setParameterOrder(WsdlOperationBuilder var1, WebMethodDecl var2) {
      StringBuffer var3 = new StringBuffer();
      Iterator var4;
      WebParamDecl var5;
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         var3.append("parameters");

         for(var4 = var2.getHeaderParams(); var4.hasNext(); var3.append(var5.getPartName())) {
            var5 = (WebParamDecl)var4.next();
            if (var3.length() > 0) {
               var3.append(' ');
            }
         }
      } else {
         for(var4 = var2.getWebParams(); var4.hasNext(); var3.append(var5.getPartName())) {
            var5 = (WebParamDecl)var4.next();
            if (var3.length() > 0) {
               var3.append(' ');
            }
         }
      }

      String var6 = var3.toString();
      if (!StringUtil.isEmpty(var6)) {
         var1.setParameterOrder(var3.toString());
      }

   }

   private void buildFaultMessage(WsdlOperationBuilder var1, JMethod var2) {
      JClass[] var3 = var2.getExceptionTypes();
      JClass var4 = (JClass)var2.getParent();

      for(int var5 = 0; var5 < var3.length; ++var5) {
         JClass var6 = var3[var5];
         if (ExceptionUtil.isUserException(var6)) {
            QName var7 = new QName(this.definitions.getTargetNamespace(), var6.getSimpleName());
            WsdlMessageBuilder var8 = (WsdlMessageBuilder)this.definitions.getMessages().get(var7);
            if (var8 == null) {
               var8 = this.definitions.addMessage(var7);
               WsdlPartBuilder var9 = var8.addPart(var6.getSimpleName());
               if (ExceptionUtil.isMarshalPropertyException(var6)) {
                  var9.setElement(ExceptionUtil.exceptionMarshalPropertyElementName(var4, var6));
               } else {
                  var9.setElement(this.bindings.getSchemaType(var6.getQualifiedName()));
               }
            }

            var1.addFault(var8, var6.getSimpleName());
         }
      }

   }

   private WsdlMessageBuilder buildOutputMessage(QName var1, WebMethodDecl var2) throws WsBuildException {
      QName var3 = new QName(var1.getNamespaceURI(), var1.getLocalPart() + "Response");
      WsdlMessageBuilder var4 = this.definitions.addMessage(var3);
      Iterator var6;
      WebParamDecl var7;
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         WsdlPartBuilder var5 = var4.addPart(var2.getWebResult().getPartName());
         var5.setElement(var3);
         var6 = var2.getHeaderParams();

         while(var6.hasNext()) {
            var7 = (WebParamDecl)var6.next();
            if (var7.getMode() != Mode.IN) {
               var5 = var4.addPart(var7.getPartName());
               if (var7.isXmlElement()) {
                  var5.setElement(var7.getElementQName());
               } else {
                  var5.setElement(var7.getElementQName());
               }
            }
         }
      } else {
         JClass var9 = var2.getJMethod().getReturnType();
         if (!var2.isOneway()) {
            if (!var9.isVoidType()) {
               WsdlPartBuilder var10 = var4.addPart(var2.getWebResult().getPartName());
               if (var2.getSoapBinding().isDocumentStyle()) {
                  if (var2.getWebResult().isXmlElement()) {
                     var10.setElement(var2.getWebResult().getElementQName());
                  } else {
                     var10.setElement(var2.getWebResult().getElementQName());
                  }
               } else {
                  this.setWsdlType(var10, var9);
               }
            }

            var6 = var2.getWebParams();

            while(true) {
               while(true) {
                  do {
                     if (!var6.hasNext()) {
                        return var4;
                     }

                     var7 = (WebParamDecl)var6.next();
                  } while(var7.getMode() == Mode.IN);

                  WsdlPartBuilder var8 = var4.addPart(var7.getPartName());
                  if (!var2.getSoapBinding().isDocumentStyle() && !var7.isHeader()) {
                     this.setWsdlType(var8, var7.getRealType());
                  } else if (var7.isXmlElement()) {
                     var8.setElement(var7.getElementQName());
                  } else {
                     var8.setElement(var7.getElementQName());
                  }
               }
            }
         }
      }

      return var4;
   }

   private WsdlMessageBuilder buildInputMessage(QName var1, WebMethodDecl var2) throws WsBuildException {
      WsdlMessageBuilder var3 = this.definitions.addMessage(var1);
      if (var2.getSoapBinding().isDocLiteralWrapped()) {
         WsdlPartBuilder var4 = var3.addPart("parameters");
         var4.setElement(var1);
         Iterator var5 = var2.getHeaderParams();

         while(var5.hasNext()) {
            WebParamDecl var6 = (WebParamDecl)var5.next();
            if (var6.getMode() != Mode.OUT) {
               var4 = var3.addPart(var6.getPartName());
               if (var6.isXmlElement()) {
                  var4.setElement(var6.getElementQName());
               } else {
                  var4.setElement(var6.getElementQName());
               }
            }
         }

         return var3;
      } else {
         Iterator var7 = var2.getWebParams();

         while(true) {
            while(true) {
               WebParamDecl var8;
               do {
                  if (!var7.hasNext()) {
                     return var3;
                  }

                  var8 = (WebParamDecl)var7.next();
               } while(var8.getMode() == Mode.OUT);

               WsdlPartBuilder var9 = var3.addPart(var8.getPartName());
               if (!var2.getSoapBinding().isDocumentStyle() && (!var8.isHeader() || !var2.getSoapBinding().isLiteral())) {
                  this.setWsdlType(var9, var8.getRealType());
               } else if (var8.isXmlElement()) {
                  var9.setElement(var8.getElementQName());
               } else {
                  var9.setElement(var8.getElementQName());
               }
            }
         }
      }
   }

   public QName getXmlTypeFromParam() {
      return null;
   }

   private void setWsdlType(WsdlPartBuilder var1, JClass var2) throws WsBuildException {
      QName var3 = this.getXmlTypeFromParam();
      if (var3 != null) {
         var1.setType(var3);
      } else {
         QName var4 = this.bindings.getSchemaType(var2.getQualifiedName());
         if (var4 == null) {
            throw new WsBuildException("Unable to find XML type for java class:" + var2);
         }

         var1.setType(var4);
      }

   }
}
