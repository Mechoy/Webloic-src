package weblogic.wsee.jaxrpc;

import com.bea.xml.XmlException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.w3c.dom.Node;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.internal.GenericRuntimeBindingsImpl;
import weblogic.wsee.deploy.ClientDeployInfo;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.ReliabilityErrorListenerRegistry;
import weblogic.wsee.reliability.WsrmProtocolUtils;
import weblogic.wsee.util.SaajUtil;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.util.WLSOAPFactory;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsFactory;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlFactory;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlUtils;

/** @deprecated */
public final class SoapDispatch {
   public static final String SOAP_DISPATCH_INITIATED_OPERATION_PROPERTY = "weblogic.wsee.jaxrpc.SoapDispatchInitiatedOperation";
   private QName serviceName;
   private QName portName;
   private WsService webservice;
   private WsdlDefinitions definitions;
   private WsPort port;
   private Map properties;
   private WsrmConfigImpl _wsrmConfig;
   private WsrmUtilsImpl _wsrmUtils;
   private SOAPFactory soapFactory;

   private SoapDispatch(String var1) throws ServiceException {
      this(createWsdlDefinitions(ServiceImpl.getWsdlUrl(var1)));
   }

   private SoapDispatch(WsdlDefinitions var1) {
      this.properties = new HashMap();
      this.soapFactory = WLSOAPFactory.createSOAPFactory();
      this.definitions = var1;
      this._wsrmUtils = new WsrmUtilsImpl();
   }

   public static SoapDispatch create(String var0) throws ServiceException {
      return new SoapDispatch(var0);
   }

   public static SoapDispatch create(WsdlDefinitions var0) {
      return new SoapDispatch(var0);
   }

   public String getWsdlTargetNamespace() {
      return this.definitions.getTargetNamespace();
   }

   public void setServiceName(QName var1) {
      this.serviceName = var1;
   }

   public void setPortName(QName var1) {
      this.portName = var1;
   }

   public SOAPElement createSoapElement(String var1, String var2, String var3) throws ServiceException {
      try {
         return this.soapFactory.createElement(var1, var2, var3);
      } catch (SOAPException var5) {
         ServiceImpl.throwServiceException("Failed to create SOAP Element", var5);
         throw new Error("Should never reach here");
      }
   }

   public void setProperty(String var1, Object var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("name == null");
      } else {
         this.properties.put(var1, var2);
      }
   }

   public Object getProperty(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name == null");
      } else {
         return this.properties.get(var1);
      }
   }

   public Map getUpdateablePropertyMap() {
      return this.properties;
   }

   public Map getUpdateableInvokePropertyMap() {
      Object var1 = (Map)this.properties.get("weblogic.wsee.invoke_properties");
      if (var1 == null) {
         var1 = new ConcurrentHashMap();
         this.properties.put("weblogic.wsee.invoke_properties", var1);
      }

      return (Map)var1;
   }

   public String getEndpointAddressFromInvokeProperties() {
      return (String)this.getUpdateableInvokePropertyMap().get("javax.xml.rpc.service.endpoint.address");
   }

   public SOAPElement invoke(QName var1, SOAPElement var2) throws ServiceException, RemoteException {
      SOAPMessage var3 = null;

      try {
         var3 = WLMessageFactory.getInstance().getMessageFactory(WsdlUtils.isSoap12(this.port)).createMessage();
         SOAPPart var4 = var3.getSOAPPart();
         Node var5 = var4.importNode(var2, true);
         var3.getSOAPBody().appendChild(var5);
      } catch (SOAPException var7) {
         ServiceImpl.throwServiceException("Failed to construct request", var7);
      }

      SOAPMessage var8 = this.invoke(var1, var3);

      try {
         return SaajUtil.getFirstChild(var8.getSOAPBody());
      } catch (SOAPException var6) {
         ServiceImpl.throwServiceException("Failed to get element from response", var6);
         throw new Error("Should never reach here");
      }
   }

   public static String getRequestingServiceURI(JwsContext var0) {
      Container var1 = (Container)var0;
      SoapMessageContext var2 = (SoapMessageContext)var1.getUnfilteredMessageContext();
      return var2.getDispatcher().getConnection().getTransport().getServiceURI();
   }

   public static String getRequestingServiceURI(MessageContext var0) {
      return ((SoapMessageContext)var0).getDispatcher().getConnection().getTransport().getServiceURI();
   }

   public String invokeAsync(String var1, QName var2, SOAPElement var3, EndpointReference var4) throws ServiceException, RemoteException {
      SOAPMessage var5 = null;

      try {
         var5 = WLMessageFactory.getInstance().getMessageFactory(WsdlUtils.isSoap12(this.port)).createMessage();
         SOAPPart var6 = var5.getSOAPPart();
         Node var7 = var6.importNode(var3, true);
         var5.getSOAPBody().appendChild(var7);
      } catch (SOAPException var8) {
         ServiceImpl.throwServiceException("Failed to construct request", var8);
      }

      return this.invokeAsync(var1, var2, var5, var4);
   }

   public String invokeAsync(String var1, QName var2, SOAPMessage var3, EndpointReference var4) throws RemoteException, ServiceException {
      return (String)this.invoke(var1, var2, var3, true, var4);
   }

   public SOAPMessage invoke(QName var1, SOAPMessage var2) throws RemoteException, ServiceException {
      return (SOAPMessage)this.invoke((String)null, var1, var2, false, (EndpointReference)null);
   }

   public WsrmConfig getWsrmConfig() {
      if (this._wsrmConfig == null) {
         this._wsrmConfig = new WsrmConfigImpl();
      }

      return this._wsrmConfig;
   }

   public WsrmUtils getWsrmUtils() {
      return this._wsrmUtils;
   }

   private Object invoke(String var1, QName var2, SOAPMessage var3, boolean var4, EndpointReference var5) throws RemoteException, ServiceException {
      if (var2 == null) {
         throw new IllegalArgumentException("null method");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null request");
      } else {
         this.init();
         WsMethod var6 = this.port.getEndpoint().getMethod(var2.getLocalPart());
         if (var6 == null) {
            throw new ServiceException("Unable to find method with name: " + var2);
         } else {
            WsdlBindingOperation var7 = (WsdlBindingOperation)this.port.getWsdlPort().getBinding().getOperations().get(var2);
            if (var7 == null) {
               throw new ServiceException("Unable to find binding operation with name: " + var2);
            } else {
               try {
                  HashSet var8 = new HashSet();
                  this.properties.put("weblogic.wsee.jaxrpc.SoapDispatchInitiatedOperation", "true");
                  if (var4) {
                     this.setTransientProperty("weblogic.wsee.async.invoke", "true", var8);
                     this.setTransientProperty("weblogic.wsee.async.invoke", "true", var8);
                     this.setTransientProperty("weblogic.wsee.async.invokeNonJws", "true", var8);
                     this.setTransientProperty("weblogic.wsee.method.name", var2.getLocalPart(), var8);
                     this.setTransientProperty("weblogic.wsee.operation.name", var2.getLocalPart(), var8);
                     if (var5 == null && var1 == null) {
                        throw new JAXRPCException("You must specify one of (requestingServiceUri, epr) when invoking SoapDispatch.invokeAsync");
                     }

                     if (var5 == null) {
                        WsdlPort var9 = this.port.getWsdlPort();
                        WsdlBinding var10 = var9.getBinding();
                        String var11 = var10.getBindingType();
                        String var12 = var9.getTransport();
                        boolean var13 = "SOAP12".equals(var11);
                        var5 = AsyncUtil.getDefaultAsyncResponseServiceEPR(var12, var13);
                     }

                     this.setTransientProperty("weblogic.wsee.async.res.epr", var5, var8);
                     if (var1 != null) {
                        this.setTransientProperty("weblogic.wsee.enclosing.jws.serviceuri", var1, var8);
                     }

                     if (this._wsrmConfig != null && this._wsrmConfig.getReliabilityErrorListenerKey() != null) {
                        this.setTransientProperty("weblogic.wsee.reliable.errorlistener", this._wsrmConfig.getReliabilityErrorListenerKey(), var8);
                     }
                  }

                  LinkedHashMap var22 = new LinkedHashMap();
                  LinkedHashMap var23 = new LinkedHashMap();
                  WsReturnType var24 = var6.getReturnType();
                  if (var24 != null) {
                     var23.put(var24.getName(), (Object)null);
                  }

                  ClientDispatcher var25 = new ClientDispatcher(var6, this.port, var7, var22, var23, this.properties);
                  SOAPMessageContext var26 = (SOAPMessageContext)var25.getContext();
                  var26.setMessage(var3);

                  try {
                     var25.dispatch();
                  } finally {
                     Iterator var16 = var8.iterator();

                     while(var16.hasNext()) {
                        String var17 = (String)var16.next();
                        this.properties.remove(var17);
                     }

                  }

                  if (var4) {
                     MessageIdHeader var14 = (MessageIdHeader)var25.getContext().getHeaders().getHeader(MessageIdHeader.TYPE);
                     return var14.getMessageId();
                  } else {
                     return var26.getMessage();
                  }
               } catch (Throwable var21) {
                  throw new RemoteException("invoke failed", var21);
               }
            }
         }
      }
   }

   private void setTransientProperty(String var1, Object var2, Set<String> var3) {
      this.properties.put(var1, var2);
      var3.add(var1);
   }

   private void init() throws ServiceException {
      if (this.webservice == null) {
         ClientDeployInfo var1 = new ClientDeployInfo();
         var1.setWsdlDef(this.definitions);
         var1.setRuntimeBindings(this.createRuntimeBinding());
         this.setServiceName(var1);
         this.createWsService(var1);
         this.setPort();
         HandlerList var2 = this.port.getInternalHandlerList();
         var2.remove("CODEC_HANDLER");
      }
   }

   private void setPort() throws ServiceException {
      if (this.port == null) {
         if (this.portName == null) {
            this.port = this.findPort();
         } else {
            this.port = this.webservice.getPort(this.portName.getLocalPart());
         }
      }

   }

   private void createWsService(ClientDeployInfo var1) throws ServiceException {
      WsFactory var2 = WsFactory.instance();

      try {
         this.webservice = var2.createClientService(var1);
      } catch (WsException var4) {
         ServiceImpl.throwServiceException("Failed to create Web Service OM", var4);
      }

   }

   private void setServiceName(ClientDeployInfo var1) throws ServiceException {
      if (this.serviceName == null) {
         var1.setServiceName(this.findServiceName());
      } else {
         var1.setServiceName(this.serviceName);
      }

   }

   private WsPort findPort() throws ServiceException {
      Iterator var1 = this.webservice.getPorts();
      if (var1.hasNext()) {
         WsPort var2 = (WsPort)var1.next();
         if (var1.hasNext()) {
            throw new ServiceException("More than one port found. Use setPortName() to specify the port");
         } else {
            return var2;
         }
      } else {
         throw new JAXRPCException("No port found in the WSDL");
      }
   }

   private QName findServiceName() throws ServiceException {
      WsdlService var1 = this.findService();
      return var1.getName();
   }

   private WsdlService findService() throws ServiceException {
      Iterator var1 = this.definitions.getServices().values().iterator();
      if (var1.hasNext()) {
         WsdlService var2 = (WsdlService)var1.next();
         if (var1.hasNext()) {
            throw new ServiceException("More than one service available in thisWSDL. Use setServiceName() to specifiy the service name");
         } else {
            return var2;
         }
      } else {
         throw new JAXRPCException("Unable to find WSDL service");
      }
   }

   private RuntimeBindings createRuntimeBinding() {
      try {
         return new GenericRuntimeBindingsImpl();
      } catch (IOException var2) {
         throw new JAXRPCException("Failed to create generic binding provider", var2);
      } catch (XmlException var3) {
         throw new JAXRPCException("Failed to create generic binding provider", var3);
      }
   }

   private static WsdlDefinitions createWsdlDefinitions(String var0) throws ServiceException {
      try {
         return WsdlFactory.getInstance().parse(var0);
      } catch (WsdlException var2) {
         ServiceImpl.throwServiceException("Failed to parse WSDL", var2);
         throw new Error("should never reach here");
      }
   }

   private class WsrmUtilsImpl implements WsrmUtils {
      public WsrmUtilsImpl() {
      }

      public void setFinalMessage() {
         WsrmProtocolUtils.setFinalMessage(SoapDispatch.this.getUpdateablePropertyMap());
      }

      public void setLastMessage() {
         WsrmProtocolUtils.setLastMessage(SoapDispatch.this.getUpdateablePropertyMap());
      }

      public void sendEmptyLastMessage() {
         WsrmProtocolUtils.sendEmptyLastMessage(SoapDispatch.this.getUpdateableInvokePropertyMap(), SoapDispatch.this.getEndpointAddressFromInvokeProperties());
      }

      public void terminateSequence() {
         WsrmProtocolUtils.terminateSequence(SoapDispatch.this.getUpdateableInvokePropertyMap());
      }
   }

   private class WsrmConfigImpl implements WsrmConfig {
      private String _listenerKey;

      public WsrmConfigImpl() {
      }

      public void setReliabilityErrorListenerKey(String var1) throws IllegalArgumentException {
         if (ReliabilityErrorListenerRegistry.getInstance().getListener(var1) == null) {
            throw new IllegalArgumentException("No ReliabilityErrorListener has been registered with the given key: " + var1);
         } else {
            this._listenerKey = var1;
         }
      }

      public String getReliabilityErrorListenerKey() {
         return this._listenerKey;
      }
   }

   /** @deprecated */
   public interface WsrmUtils {
      void setFinalMessage();

      void setLastMessage();

      void sendEmptyLastMessage();

      void terminateSequence();
   }

   /** @deprecated */
   public interface WsrmConfig {
      void setReliabilityErrorListenerKey(String var1);

      String getReliabilityErrorListenerKey();
   }
}
