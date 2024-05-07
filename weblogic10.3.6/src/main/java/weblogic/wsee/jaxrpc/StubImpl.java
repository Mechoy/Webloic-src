package weblogic.wsee.jaxrpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.Stub;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.holders.Holder;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPMessage;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.util.HolderUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsStub;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;
import weblogic.xml.dom.Util;

public class StubImpl implements WLStub, Stub, InvocationHandler {
   private WsdlPort port;
   private ServiceImpl serviceImpl;
   private Map properties = new HashMap();
   private static final ArrayList propertyNames = new ArrayList();
   private static final boolean verbose = Verbose.isVerbose(StubImpl.class);

   protected StubImpl(WsdlPort var1, Class var2, ServiceImpl var3) {
      this.port = var1;
      this.serviceImpl = var3;
      if (var1 == null) {
         throw new IllegalArgumentException("port can not be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("service can not be null");
      } else {
         SoapAddress var4 = WsdlUtils.getSoapAddress(var1);
         if (var4 != null) {
            this._setProperty("javax.xml.rpc.service.endpoint.address", var4.getLocation());
         }

         this.properties.put("weblogic.wsee.handler.registry", this.serviceImpl._internalGetHandlerRegistry());
         this.setCallbackProperty();
      }
   }

   protected void setCallbackProperty() {
      WsdlPartnerLinkType var1 = (WsdlPartnerLinkType)this.port.getService().getDefinitions().getExtension("PartnerLinkType");
      if (var1 != null) {
         WsdlDefinitions var2 = this.port.getService().getDefinitions();
         Iterator var3 = var2.getServices().values().iterator();

         while(var3.hasNext()) {
            WsdlService var4 = (WsdlService)var3.next();
            Iterator var5 = var4.getPortTypes().iterator();

            while(var5.hasNext()) {
               WsdlPortType var6 = (WsdlPortType)var5.next();

               try {
                  if (verbose) {
                     Verbose.log((Object)("Port type " + var6.getName() + " link name " + var1.getRole2Name() + ", " + var1.getPortTypeName("Callback")));
                  }

                  if (var6.getName().equals(var1.getPortTypeName("Callback"))) {
                     this._setProperty("weblogic.wsee.callback.contextpath", var4.getName().getLocalPart() + "Cb");
                     this._setProperty("weblogic.wsee.callback.serviceuri", var4.getName().getLocalPart() + "Cb");
                     return;
                  }
               } catch (WsdlException var8) {
                  throw new JAXRPCException("Failed to parse WSDL", var8);
               }
            }
         }
      }

   }

   protected String _getPortName() {
      return this.port.getName().getLocalPart();
   }

   protected Map _getUserProperties() {
      return (Map)((Map)((HashMap)this.properties).clone());
   }

   public void _setUserProperties(Map var1) {
      if (var1 != null) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            this._setProperty(var3, var1.get(var3));
         }

      }
   }

   public void _setProperty(String var1, Object var2) throws UnsupportedOperationException {
      if (isValidProperty(var1)) {
         this.properties.put(var1, var2);
      } else {
         throw new JAXRPCException("unknown property:" + var1);
      }
   }

   public Object _getProperty(String var1) {
      if (!isValidProperty(var1)) {
         throw new JAXRPCException("unknown property:" + var1);
      } else if ("weblogic.wsee.ws.WsStub".equals(var1)) {
         WsStub var3 = this.serviceImpl.getWsStub(this.port);
         return var3;
      } else if ("weblogic.wsee.ws.WsService".equals(var1)) {
         return this.serviceImpl.getWebService();
      } else {
         Object var2 = this.properties.get(var1);
         if (var2 == null) {
            var2 = this._getInternalProperty(var1);
         }

         return var2;
      }
   }

   private Object _getInternalProperty(String var1) {
      if ("javax.xml.rpc.service.endpoint.address".equals(var1)) {
         SoapAddress var2 = WsdlUtils.getSoapAddress(this.port);
         if (var2 != null) {
            return var2.getLocation();
         }
      }

      return null;
   }

   public Object _removeProperty(String var1) {
      if (!isValidProperty(var1)) {
         throw new JAXRPCException("unknown property:" + var1);
      } else {
         return this.properties.remove(var1);
      }
   }

   static boolean isValidProperty(String var0) {
      if (var0 == null) {
         throw new JAXRPCException("property name can not be null");
      } else {
         return propertyNames.contains(var0) || var0.startsWith("com.bea") || var0.startsWith("weblogic.wsee");
      }
   }

   public Iterator _getPropertyNames() {
      return propertyNames.iterator();
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      WsStub var4 = this.serviceImpl.getWsStub(this.port);
      if (var4 == null) {
         throw new IllegalStateException("Couldn't find indicated port " + (this.port != null ? this.port.getName() : null) + " in stub's service configuration for service " + (this.serviceImpl != null ? this.serviceImpl.getWebService().getWsdlService().getName() : null));
      } else {
         WsMethod var5 = this.findWsMethod(var4, var2);
         if (var5 == null) {
            return this.invokeObjectMethod(var2, var3);
         } else {
            try {
               return this._invoke(var5.getOperationName().getLocalPart(), var3);
            } catch (SOAPFaultException var7) {
               return this.throwRemoteException(this._soapFault2String(var7), var7);
            } catch (InvocationException var8) {
               return this.throwRemoteException("Failed to invoke", var8);
            } catch (JAXRPCException var9) {
               return this.throwRemoteException("Failed to invoke", var9);
            }
         }
      }
   }

   private Object invokeObjectMethod(Method var1, Object[] var2) throws Throwable {
      if (var1.getDeclaringClass().isAssignableFrom(StubImpl.class)) {
         try {
            return var1.invoke(this, var2);
         } catch (InvocationTargetException var4) {
            throw var4.getTargetException();
         }
      } else {
         throw new JAXRPCException("unable to find operation:" + var1);
      }
   }

   private WsMethod findWsMethod(WsStub var1, Method var2) {
      Iterator var3 = var1.getMethods();

      WsMethod var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (WsMethod)var3.next();
      } while(!var2.getName().equals(var4.getMethodName()));

      return var4;
   }

   private Object throwRemoteException(String var1, Throwable var2) throws Throwable {
      throw new RemoteException(var1, var2);
   }

   protected Object _invoke(String var1, Object[] var2) throws Throwable {
      if (verbose) {
         Verbose.banner("Invoke : " + var1);
      }

      if (var2 == null) {
         var2 = new Object[0];
      }

      WsStub var3 = this.serviceImpl.getWsStub(this.port);
      WsMethod var4 = var3.getMethod(var1);
      LinkedHashMap var5 = new LinkedHashMap();
      LinkedHashMap var6 = new LinkedHashMap();
      HashMap var7 = new HashMap();
      this.fillArgs(var4, var2, var7, var5);
      Object var8 = var3.invoke(var1, this.port.getName().getLocalPart(), var5, var6, this.properties);
      this.setHolderValues(var6, var7);
      return var8;
   }

   public Object _asyncResponse(String var1, SOAPMessage var2, MessageContext var3) throws Throwable {
      if (verbose) {
         Verbose.banner("AsyncResponse: " + var1);
      }

      WsStub var4 = this.serviceImpl.getWsStub(this.port);
      var4.getMethod(var1);
      LinkedHashMap var6 = new LinkedHashMap();
      HashMap var7 = new HashMap();
      Object var8 = var4._asyncResponse(var1, this.port.getName().getLocalPart(), var6, var2, var3, this.properties);
      this.setHolderValues(var6, var7);
      return var8;
   }

   private void setHolderValues(Map var1, HashMap var2) {
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Object var5 = var2.get(var4);
         Object var6 = var1.get(var4);
         if (var5 == null) {
            throw new JAXRPCException("Found an output param :" + var4 + ", but" + "a corresponding holder class not passed in");
         }

         HolderUtil.setHolderValue(var5, var6);
      }

   }

   private void fillArgs(WsMethod var1, Object[] var2, HashMap var3, Map var4) {
      int var5 = 0;

      for(Iterator var6 = var1.getParameters(); var6.hasNext(); ++var5) {
         WsParameterType var7 = (WsParameterType)var6.next();
         if (var5 >= var2.length) {
            throw new JAXRPCException("There are no enough args.");
         }

         Object var8 = var2[var5];
         if (var7.getMode() != 0) {
            if (!(var8 instanceof Holder)) {
               throw new JAXRPCException("Parameter \"" + var7.getName() + "\" is an in-out or out param." + "but the holder class is not passed in.");
            }

            var3.put(var7.getName(), var8);
         }

         if (var7.getMode() != 1) {
            var4.put(var7.getName(), HolderUtil.getHolderValue(var8));
         }
      }

      if (var5 != var2.length) {
         throw new JAXRPCException("There are more args than required.");
      }
   }

   public static Object implementInterface(Class var0, WsdlPort var1, Map var2, ServiceImpl var3) {
      ClassLoader var4 = var0.getClassLoader();
      ArrayList var5 = new ArrayList();
      var5.add(var0);
      if (!Remote.class.isAssignableFrom(var0)) {
         var5.add(Remote.class);
      }

      if (!Stub.class.isAssignableFrom(var0)) {
         var5.add(Stub.class);
      }

      StubImpl var6 = new StubImpl(var1, var0, var3);
      if (var2 != null) {
         Iterator var7 = var2.entrySet().iterator();

         while(var7.hasNext()) {
            Map.Entry var8 = (Map.Entry)var7.next();
            var6._setProperty((String)var8.getKey(), var8.getValue());
         }
      }

      Class[] var9 = (Class[])((Class[])var5.toArray(new Class[var5.size()]));
      return Proxy.newProxyInstance(var4, var9, var6);
   }

   public Object _wrap(int var1) {
      return new Integer(var1);
   }

   public Object _wrap(float var1) {
      return new Float(var1);
   }

   public Object _wrap(double var1) {
      return new Double(var1);
   }

   public Object _wrap(short var1) {
      return new Short(var1);
   }

   public Object _wrap(long var1) {
      return new Long(var1);
   }

   public Object _wrap(boolean var1) {
      return new Boolean(var1);
   }

   public Object _wrap(Object var1) {
      return var1;
   }

   public Object _wrap(char var1) {
      return "" + var1;
   }

   public Object _wrap(byte var1) {
      return new Byte(var1);
   }

   protected String _soapFault2String(SOAPFaultException var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("SOAPFaultException - ");
      var2.append("FaultCode [" + var1.getFaultCode() + "]");
      var2.append(" FaultString [" + var1.getFaultString() + "]");
      var2.append(" FaultActor [" + var1.getFaultActor() + "]");
      if (var1.getDetail() != null) {
         var2.append(" Detail [" + Util.printNode(var1.getDetail()) + "]");
      } else {
         var2.append("No Detail");
      }

      return var2.toString();
   }

   static {
      propertyNames.add("javax.xml.rpc.service.endpoint.address");
      propertyNames.add("javax.xml.rpc.security.auth.username");
      propertyNames.add("javax.xml.rpc.security.auth.password");
      propertyNames.add("weblogic.webservice.client.proxyusername");
      propertyNames.add("weblogic.webservice.client.proxypassword");
      propertyNames.add("javax.xml.rpc.service.endpoint.address");
      propertyNames.add("javax.xml.rpc.session.maintain");
      propertyNames.add("weblogic.wsee.client.ssladapter");
      propertyNames.add("weblogic.wsee.invoke_properties");
      propertyNames.add("weblogic.wsee.policy.default.uri");
      propertyNames.add("weblogic.wsee.security.wss.CredentialProviderList");
      propertyNames.add("weblogic.wsee.security.wss.TrustManager");
      propertyNames.add("weblogic.wsee.connection.transportinfo");
      propertyNames.add("weblogic.wsee.wst.sts_endpoint_uri");
      propertyNames.add("weblogic.wsee.security.bst.serverEncryptCert");
      propertyNames.add("weblogic.wsee.security.bst.serverVerifyCert");
      propertyNames.add("weblogic.wsee.transport.connection.timeout");
      propertyNames.add("weblogic.wsee.transport.read.timeout");
      propertyNames.add("weblogic.wsee.wssc.sct.lifetime");
      propertyNames.add("weblogic.wsee.wssc.dk.label");
      propertyNames.add("weblogic.wsee.wssc.dk.length");
      propertyNames.add("weblogic.wsee.security.message_age");
      propertyNames.add("weblogic.wsee.wsrm.sequence.expiration");
      propertyNames.add("weblogic.wsee.wsrm.offer.sequence.expiration");
      propertyNames.add("weblogic.wsee.ackstoanon");
      propertyNames.add("weblogic.wsee.lastmessage");
      propertyNames.add("weblogic.wsee.policy.selection.preference");
      propertyNames.add("weblogic.wsee.policy.compat.preference");
      propertyNames.add("weblogic.wsee.security.bst.stsEncryptCert");
      propertyNames.add("weblogic.wsee.wst.saml.sts_endpoint_uri");
      propertyNames.add("oracle.contextelement.saml2.AttributeOnly");
      propertyNames.add("weblogic.wsee.security.saml.attributies");
   }
}
