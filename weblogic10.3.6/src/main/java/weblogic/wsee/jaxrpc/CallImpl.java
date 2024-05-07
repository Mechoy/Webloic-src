package weblogic.wsee.jaxrpc;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.ParameterMode;
import javax.xml.soap.SOAPException;
import weblogic.utils.StackTraceUtils;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.WsStub;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.builder.WsdlBindingBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlBindingOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.soap11.SoapAddress;
import weblogic.wsee.wsdl.soap11.SoapBinding;
import weblogic.wsee.wsdl.soap11.SoapBindingOperation;
import weblogic.wsee.wsdl.soap11.SoapBody;

public class CallImpl implements WLCall {
   private boolean isOneWay = false;
   private QName operationName;
   private QName portTypeName;
   private boolean changed = true;
   private HashMap properties = new HashMap();
   private ArrayList parameters = new ArrayList();
   private ParameterInfo returnInfo;
   private Map lastResult;
   private ServiceImpl serviceImpl;
   private WsdlPortBuilder wsdlPort;
   private static final boolean verbose = Verbose.isVerbose(CallImpl.class);
   private static ArrayList validProperties = new ArrayList();

   CallImpl(ServiceImpl var1) {
      this.serviceImpl = var1;
      this.properties.put("weblogic.wsee.handler.registry", var1._internalGetHandlerRegistry());
   }

   public boolean isParameterAndReturnSpecRequired(QName var1) {
      return this.serviceImpl.getWsdlStatus() == 3;
   }

   public void addParameter(String var1, QName var2, Class var3, ParameterMode var4) throws JAXRPCException {
      this.markChanged();
      ParameterInfo var5 = new ParameterInfo();
      var5.name = var1;
      var5.xmlType = var2;
      var5.javaType = var3;
      var5.mode = var4;
      this.parameters.add(var5);
   }

   public void addParameter(String var1, QName var2, ParameterMode var3) throws JAXRPCException {
      this.addParameter(var1, var2, (Class)null, var3);
   }

   public QName getParameterTypeByName(String var1) {
      if (this.serviceImpl.getWsdlStatus() == 3) {
         Iterator var2 = this.parameters.iterator();

         while(var2.hasNext()) {
            ParameterInfo var3 = (ParameterInfo)var2.next();
            if (var1.equals(var3.name)) {
               return var3.xmlType;
            }
         }
      } else {
         WsMethod var5 = this.getWsMethod();
         Iterator var6 = var5.getParameters();

         while(var6.hasNext()) {
            WsParameterType var4 = (WsParameterType)var6.next();
            if (var4.getName().equals(var1)) {
               return var4.getXmlName().getQName();
            }
         }
      }

      throw new JAXRPCException("Can't find parameter \"" + var1 + "\"");
   }

   public Iterator getParameterNames() {
      HashSet var1 = new HashSet();
      if (this.serviceImpl.getWsdlStatus() == 3) {
         Iterator var2 = this.parameters.iterator();

         while(var2.hasNext()) {
            ParameterInfo var3 = (ParameterInfo)var2.next();
            var1.add(var3.name);
         }
      } else {
         WsMethod var5 = this.getWsMethod();
         Iterator var6 = var5.getParameters();

         while(var6.hasNext()) {
            WsParameterType var4 = (WsParameterType)var6.next();
            var1.add(var4.getName());
         }
      }

      return var1.iterator();
   }

   private WsMethod getWsMethod() {
      if (this.changed) {
         this.syncWithWsdl();
      }

      WsStub var1 = this.serviceImpl.getWsStub(this.wsdlPort);
      return var1.getMethod(this.operationName.getLocalPart());
   }

   public Class getParameterJavaType(String var1) {
      if (this.serviceImpl.getWsdlStatus() == 3) {
         Iterator var2 = this.parameters.iterator();

         while(var2.hasNext()) {
            ParameterInfo var3 = (ParameterInfo)var2.next();
            if (var1.equals(var3.name)) {
               return var3.javaType;
            }
         }
      } else {
         WsMethod var5 = this.getWsMethod();
         Iterator var6 = var5.getParameters();

         while(var6.hasNext()) {
            WsParameterType var4 = (WsParameterType)var6.next();
            if (var4.getName().equals(var1)) {
               return var4.getJavaType();
            }
         }
      }

      throw new JAXRPCException("Can't find parameter \"" + var1 + "\"");
   }

   public ParameterMode getParameterMode(String var1) {
      if (this.serviceImpl.getWsdlStatus() == 3) {
         Iterator var2 = this.parameters.iterator();

         while(var2.hasNext()) {
            ParameterInfo var3 = (ParameterInfo)var2.next();
            if (var1.equals(var3.name)) {
               return var3.mode;
            }
         }
      } else {
         WsMethod var5 = this.getWsMethod();
         Iterator var6 = var5.getParameters();

         while(var6.hasNext()) {
            WsParameterType var4 = (WsParameterType)var6.next();
            if (var4.getName().equals(var1)) {
               switch (var4.getMode()) {
                  case 0:
                     return ParameterMode.IN;
                  case 1:
                     return ParameterMode.OUT;
                  case 2:
                     return ParameterMode.INOUT;
               }
            }
         }
      }

      throw new JAXRPCException("Can't find parameter \"" + var1 + "\"");
   }

   public void setReturnType(QName var1, Class var2) throws JAXRPCException {
      this.markChanged();
      this.returnInfo = new ParameterInfo();
      String var3 = "__bea_noname_result";
      this.returnInfo.name = var3;
      this.returnInfo.xmlType = var1;
      this.returnInfo.javaType = var2;
   }

   public void setReturnType(QName var1) throws JAXRPCException {
      this.setReturnType(var1, (Class)null);
   }

   public QName getReturnType() {
      if (this.operationName == null) {
         return null;
      } else if (this.serviceImpl.getWsdlStatus() == 3) {
         if (this.returnInfo == null) {
            throw new JAXRPCException("Return type is not set.");
         } else {
            return this.returnInfo.xmlType;
         }
      } else {
         WsMethod var1 = this.getWsMethod();
         return null;
      }
   }

   public void removeAllParameters() {
      this.markChanged();
      this.returnInfo = null;
      this.parameters.clear();
   }

   private void markChanged() {
      if (this.serviceImpl.getWsdlStatus() != 3) {
         throw new JAXRPCException("This call is created from WSDL, you can not add/remove parameters or set return type for this call");
      } else {
         this.changed = true;
      }
   }

   public QName getOperationName() {
      return this.operationName;
   }

   public void setOperationName(QName var1) {
      if (verbose) {
         Verbose.log((Object)("Setting operation name to: " + var1));
      }

      if (!var1.equals(this.operationName)) {
         this.operationName = var1;
         this.changed = true;
      }
   }

   public QName getPortTypeName() {
      return this.portTypeName == null ? new QName("") : this.portTypeName;
   }

   public void setPortTypeName(QName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("port type name can not be null");
      } else {
         this.portTypeName = var1;
         if (this.wsdlPort != null && !this.wsdlPort.getPortType().getName().equals(var1)) {
            this.wsdlPort = null;
         }

         this.changed = true;
      }
   }

   void setWsdlPort(WsdlPortBuilder var1) {
      this.wsdlPort = var1;
      this.setPortTypeName(var1.getPortType().getName());
   }

   public void setTargetEndpointAddress(String var1) {
      this.setProperty("javax.xml.rpc.service.endpoint.address", var1);
   }

   public String getTargetEndpointAddress() {
      return (String)this.getProperty("javax.xml.rpc.service.endpoint.address");
   }

   static boolean isValidProperty(String var0) {
      if (var0 == null) {
         throw new JAXRPCException("property name can not be null");
      } else {
         return validProperties.contains(var0) || var0.startsWith("com.bea") || var0.startsWith("weblogic.wsee");
      }
   }

   public void setProperty(String var1, Object var2) throws JAXRPCException {
      if (!isValidProperty(var1)) {
         throw new JAXRPCException("unknown property: " + var1);
      } else {
         this.properties.put(var1, var2);
      }
   }

   public Object getProperty(String var1) {
      if (!isValidProperty(var1)) {
         throw new JAXRPCException("unknown property: " + var1);
      } else {
         return this.properties.get(var1);
      }
   }

   public void removeProperty(String var1) {
      this.properties.remove(var1);
   }

   public Iterator getPropertyNames() {
      return this.properties.keySet().iterator();
   }

   private WsdlDefinitions createDummyWsdl(QName var1) throws WsdlException {
      if (verbose) {
         Verbose.log((Object)("Creating dummy wsdl for service: " + var1));
      }

      if (this.operationName == null) {
         throw new JAXRPCException("operation name not set");
      } else {
         String var2 = this.operationName.getNamespaceURI();
         boolean var3 = this.isDocumentStyle();
         if (this.portTypeName == null) {
            this.portTypeName = new QName(var2, "DefaultPortTypeName");
         }

         WsdlDefinitionsBuilder var4 = WsdlFactory.getInstance().create();
         var4.setTargetNamespace(var2);
         WsdlPortTypeBuilder var5 = var4.addPortType(this.portTypeName);
         WsdlOperationBuilder var6 = var5.addOperation(this.operationName);
         WsdlMessageBuilder var7 = var4.addMessage(new QName(var2, this.operationName.getLocalPart()));
         var6.setInput(var7);
         WsdlMessageBuilder var8 = null;
         if (this.isOneWay) {
            var6.setType(1);
         } else {
            var8 = var4.addMessage(new QName(var2, this.operationName.getLocalPart() + "Response"));
            var6.setOutput(var8);
         }

         Iterator var9 = this.parameters.iterator();

         while(var9.hasNext()) {
            ParameterInfo var10 = (ParameterInfo)var9.next();
            WsdlPartBuilder var11;
            if (var10.mode == ParameterMode.IN) {
               var11 = var7.addPart(var10.name);
               this.setXmlTypeOrElement(var3, var11, var10);
            }

            if (var10.mode == ParameterMode.OUT) {
               if (this.isOneWay) {
                  throw new JAXRPCException("One way method can not have out parames" + var10);
               }

               var11 = var8.addPart(var10.name);
               this.setXmlTypeOrElement(var3, var11, var10);
            }
         }

         if (this.returnInfo != null) {
            if (this.isOneWay) {
               throw new JAXRPCException("One way method can not have return type:" + this.returnInfo);
            }

            WsdlPartBuilder var16 = var8.addPart(this.returnInfo.name);
            this.setXmlTypeOrElement(var3, var16, this.returnInfo);
         }

         WsdlBindingBuilder var17 = var4.addBinding(this.portTypeName, var5);
         SoapBinding var18 = SoapBinding.attach(var17);
         if (var3) {
            var18.setStyle("document");
         } else {
            var18.setStyle("rpc");
         }

         WsdlBindingOperationBuilder var19 = var17.addOperation(var6);
         SoapBindingOperation.attach(var19);
         WsdlBindingMessageBuilder var12 = var19.createInput();
         SoapBody var13 = SoapBody.attach(var12);
         var13.setNamespace(this.operationName.getNamespaceURI());
         if (var3) {
            var13.setUse("literal");
         } else {
            var13.setUse("encoded");
         }

         if (!this.isOneWay) {
            WsdlBindingMessageBuilder var14 = var19.createOutput();
            SoapBody var15 = SoapBody.attach(var14);
            var15.setNamespace(this.operationName.getNamespaceURI());
            if (var3) {
               var15.setUse("literal");
            } else {
               var15.setUse("encoded");
            }
         }

         WsdlServiceBuilder var20 = var4.addService(var1);
         this.wsdlPort = var20.addPort(this.portTypeName, var17);
         SoapAddress var21 = SoapAddress.attach(this.wsdlPort);
         return var4;
      }
   }

   private void setXmlTypeOrElement(boolean var1, WsdlPartBuilder var2, ParameterInfo var3) {
      if (var1) {
         var2.setElement(var3.xmlType);
      } else {
         var2.setType(var3.xmlType);
      }

   }

   private boolean isDocumentStyle() {
      String var1 = (String)this.properties.get("javax.xml.rpc.soap.operation.style");
      boolean var2 = false;
      if (var1 != null && "document".equals(var1)) {
         var2 = true;
      }

      return var2;
   }

   private Map getInputParamMap(WsStub var1, Object[] var2) {
      LinkedHashMap var3 = new LinkedHashMap();
      WsMethod var4 = var1.getMethod(this.operationName.getLocalPart());

      try {
         for(int var5 = 0; var5 < var2.length; ++var5) {
            WsParameterType var6 = var4.getParameter(var5);
            var3.put(var6.getName(), var2[var5]);
         }

         return var3;
      } catch (IndexOutOfBoundsException var7) {
         throw new JAXRPCException("There are more parameters passed in than required.");
      }
   }

   private void syncWithWsdl() {
      if (this.operationName == null) {
         throw new JAXRPCException("operation name not set.");
      } else if (this.wsdlPort == null) {
         if (this.portTypeName == null) {
            boolean var1 = false;
            Iterator var2 = ((WsdlServiceBuilder)this.serviceImpl.getWebService().getWsdlService()).getPorts().values().iterator();

            while(var2.hasNext()) {
               WsdlPortBuilder var3 = (WsdlPortBuilder)var2.next();
               if (var3.getPortType().getOperations().get(this.operationName) != null) {
                  if (var1) {
                     throw new JAXRPCException("There are more than one port types that have operation " + this.operationName + " please set a " + "port type name using Call.setPortTypeName(QName name).");
                  }

                  this.wsdlPort = var3;
                  this.portTypeName = var3.getPortType().getName();
                  var1 = true;
               }
            }
         } else {
            Iterator var4 = ((WsdlServiceBuilder)this.serviceImpl.getWebService().getWsdlService()).getPorts().values().iterator();

            while(var4.hasNext()) {
               WsdlPortBuilder var5 = (WsdlPortBuilder)var4.next();
               if (this.portTypeName.equals(var5.getPortType().getName())) {
                  this.wsdlPort = var5;
                  if (var5.getPortType().getOperations().get(this.operationName) == null) {
                     throw new JAXRPCException("Can't find operation \"" + this.operationName + "\" in portType \"" + this.portTypeName + "\"" + " supported operations are [" + this.getOperationNames(var5.getPortType()) + "]");
                  }
               }
            }
         }

         if (this.wsdlPort == null) {
            throw new JAXRPCException("Can't find and wsdl port that implementsport type \"" + this.portTypeName + "\".");
         }
      }
   }

   private String getOperationNames(WsdlPortType var1) {
      StringBuffer var2 = new StringBuffer();
      Iterator var3 = var1.getOperations().values().iterator();

      while(var3.hasNext()) {
         WsdlOperation var4 = (WsdlOperation)var3.next();
         var2.append(var4.getName());
         var2.append(" ");
      }

      return var2.toString();
   }

   public Object invoke(Object[] var1) throws RemoteException {
      if (verbose) {
         Verbose.log((Object)("Goint to call operation " + this.operationName));
      }

      if (this.changed) {
         this.applyChanges();
      }

      WsStub var2 = this.serviceImpl.getWsStub(this.wsdlPort);
      Map var3 = this.getInputParamMap(var2, var1);
      this.lastResult = new LinkedHashMap();

      try {
         return var2.invoke(this.operationName.getLocalPart(), this.wsdlPort.getName().getLocalPart(), var3, this.lastResult, this.properties);
      } catch (SOAPException var5) {
         throw this.getRemoteException(var5);
      } catch (Throwable var6) {
         throw new RemoteException("failed to invoke operation '" + this.operationName + StackTraceUtils.throwable2StackTrace(var6), var6);
      }
   }

   private RemoteException getRemoteException(SOAPException var1) {
      Object var2 = var1.getCause();
      if (var2 == null) {
         var2 = var1;
      }

      return new RemoteException("failed to invoke operation '" + this.operationName + "' due to an error in the " + "soap layer (SAAJ); nested exception is: " + StackTraceUtils.throwable2StackTrace(var1), (Throwable)var2);
   }

   private void applyChanges() throws RemoteException {
      if (this.serviceImpl.getWsdlStatus() == 3) {
         String var1 = this.operationName.getNamespaceURI();
         QName var2 = new QName(var1, "DefaultServiceName");
         WsdlDefinitions var3 = null;

         try {
            var3 = this.createDummyWsdl(var2);
         } catch (WsdlException var8) {
            throw new RemoteException("Failed to create dummy WSDL", var8);
         }

         RuntimeBindings var4 = (RuntimeBindings)this.properties.get("weblogic.wsee.bind.runtimeBindingProvider");
         WsService var5 = this.serviceImpl.createWebService(var3, var2, var4);
         WsPort var6 = var5.getPort(this.portTypeName.getLocalPart());
         WsMethod var7 = var6.getEndpoint().getMethod(this.operationName.getLocalPart());
         this.setJavaTypes(var7, this.parameters);
      } else {
         this.syncWithWsdl();
      }

   }

   private void setJavaTypes(WsMethod var1, ArrayList var2) {
      Iterator var3 = var1.getParameters();

      while(var3.hasNext()) {
         WsParameterType var4 = (WsParameterType)var3.next();
         ParameterInfo var5 = this.getParameterInfo(var4.getName(), var2);
         if (var5.javaType != null) {
            var4.setJavaType(var5.javaType);
         }
      }

      WsReturnType var6 = var1.getReturnType();
      if (var6 != null && this.returnInfo != null && this.returnInfo.javaType != null) {
         var6.setJavaType(this.returnInfo.javaType);
      }

   }

   private ParameterInfo getParameterInfo(String var1, ArrayList var2) {
      Iterator var3 = var2.iterator();

      ParameterInfo var4;
      do {
         if (!var3.hasNext()) {
            throw new AssertionError("Unable to find parameterInfo");
         }

         var4 = (ParameterInfo)var3.next();
      } while(!var1.equals(var4.name));

      return var4;
   }

   public Object invoke(QName var1, Object[] var2) throws JAXRPCException, RemoteException {
      this.setOperationName(var1);
      return this.invoke(var2);
   }

   public void invokeOneWay(Object[] var1) throws JAXRPCException {
      this.isOneWay = true;

      try {
         this.invoke(var1);
      } catch (RemoteException var3) {
         throw new JAXRPCException(var3);
      }

      this.isOneWay = false;
   }

   private void makeOperationOneway() {
      WsdlBindingBuilder var1 = this.wsdlPort.getBinding();
      WsdlOperationBuilder var2 = (WsdlOperationBuilder)var1.getPortType().getOperations().get(this.operationName);
      var2.setType(1);
   }

   public List getOutputValues() throws JAXRPCException {
      Map var1 = this.getOutputParams();
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.values().iterator();

         while(var3.hasNext()) {
            var2.add(var3.next());
         }

         return var2;
      }
   }

   public Map getOutputParams() throws JAXRPCException {
      if (this.lastResult == null) {
         throw new JAXRPCException("Invoke not called");
      } else {
         return this.lastResult;
      }
   }

   static {
      validProperties.add("javax.xml.rpc.service.endpoint.address");
      validProperties.add("javax.xml.rpc.security.auth.username");
      validProperties.add("javax.xml.rpc.security.auth.password");
      validProperties.add("javax.xml.rpc.soap.operation.style");
      validProperties.add("javax.xml.rpc.soap.http.soapaction.use");
      validProperties.add("javax.xml.rpc.soap.http.soapaction.uri");
      validProperties.add("javax.xml.rpc.encodingstyle.namespace.uri");
      validProperties.add("javax.xml.rpc.session.maintain");
      validProperties.add("weblogic.wsee.client.ssladapter");
      validProperties.add("weblogic.wsee.bind.runtimeBindingProvider");
      validProperties.add("weblogic.wsee.security.wss.CredentialProviderList");
      validProperties.add("weblogic.wsee.security.wss.TrustManager");
      validProperties.add("weblogic.wsee.connection.transportinfo");
      validProperties.add("weblogic.wsee.wst.sts_endpoint_uri");
      validProperties.add("weblogic.wsee.security.bst.serverEncryptCert");
      validProperties.add("weblogic.wsee.security.bst.serverVerifyCert");
      validProperties.add("weblogic.wsee.transport.connection.timeout");
      validProperties.add("weblogic.wsee.transport.read.timeout");
      validProperties.add("weblogic.wsee.wssc.sct.lifetime");
      validProperties.add("weblogic.wsee.wssc.dk.label");
      validProperties.add("weblogic.wsee.wssc.dk.length");
      validProperties.add("weblogic.wsee.security.message_age");
      validProperties.add("weblogic.wsee.policy.selection.preference");
      validProperties.add("weblogic.wsee.policy.compat.preference");
      validProperties.add("weblogic.wsee.security.bst.stsEncryptCert");
      validProperties.add("weblogic.wsee.wst.saml.sts_endpoint_uri");
      validProperties.add("oracle.contextelement.saml2.AttributeOnly");
      validProperties.add("weblogic.wsee.security.saml.attributies");
   }

   private static class ParameterInfo {
      String name;
      QName xmlType;
      Class javaType;
      ParameterMode mode;

      private ParameterInfo() {
      }

      // $FF: synthetic method
      ParameterInfo(Object var1) {
         this();
      }
   }
}
