package weblogic.wsee.ws.dispatch;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.codec.Codec;
import weblogic.wsee.codec.CodecException;
import weblogic.wsee.codec.soap12.Soap12Codec;
import weblogic.wsee.connection.Connection;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.wsdl.WsdlBindingOperation;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlPort;

public abstract class DispatcherImpl extends Dispatcher {
   private WsMethod wsMethod;
   private WsdlBindingOperation bindingOperation;
   private QName opName;
   private WsdlOperation op;
   private Codec codec;
   private WlMessageContext context;
   private Connection connection;
   private Map inParams;
   private Map outParams;
   private WsPort port;
   private HandlerIterator handlerChain;

   protected DispatcherImpl() {
   }

   public WsdlBindingOperation getBindingOperation() {
      if (this.bindingOperation == null) {
         this.bindingOperation = (WsdlBindingOperation)this.getWsdlPort().getBinding().getOperations().get(this.getOperationName());
      }

      return this.bindingOperation;
   }

   public void setBindingOperation(WsdlBindingOperation var1) {
      this.bindingOperation = var1;
   }

   public Codec getCodec() {
      return this.codec;
   }

   public void setCodec(Codec var1) {
      this.codec = var1;
   }

   public boolean isSOAP12() {
      return this.codec instanceof Soap12Codec;
   }

   public Connection getConnection() {
      return this.connection;
   }

   public void setConnection(Connection var1) {
      this.connection = var1;
   }

   public WlMessageContext getContext() {
      return this.context;
   }

   public void setContext(WlMessageContext var1) {
      this.context = var1;
   }

   public HandlerIterator getHandlerChain() {
      return this.handlerChain;
   }

   public void setHandlerChain(HandlerIterator var1) {
      this.handlerChain = var1;
   }

   public Map getInParams() {
      return this.inParams;
   }

   public void setInParams(Map var1) {
      this.inParams = var1;
   }

   public Map getOutParams() {
      return this.outParams;
   }

   public void setOutParams(Map var1) {
      this.outParams = var1;
   }

   public WsMethod getWsMethod() {
      if (this.wsMethod == null) {
         QName var1 = this.getOperationName();
         if (var1 != null) {
            this.wsMethod = this.getWsPort().getEndpoint().getMethod(var1.getLocalPart());
         }
      }

      return this.wsMethod;
   }

   public void setWsMethod(WsMethod var1) {
      this.wsMethod = var1;
   }

   public QName getOperationName() {
      if (this.opName == null) {
         this.opName = (QName)this.getContext().getProperty("weblogic.wsee.ws.server.OperationName");
         if (this.opName == null) {
            try {
               this.opName = this.getCodec().getOperation(this.getContext());
            } catch (CodecException var2) {
               throw new JAXRPCException(var2);
            }
         }
      }

      return this.opName;
   }

   public WsdlOperation getOperation() {
      if (this.op == null) {
         QName var1 = this.opName;
         if (var1 == null && this.getWsMethod() != null) {
            var1 = this.wsMethod.getOperationName();
         }

         if (var1 == null) {
            return null;
         }

         this.op = (WsdlOperation)this.getWsdlPort().getPortType().getOperations().get(var1);
      }

      return this.op;
   }

   public WsPort getWsPort() {
      return this.port;
   }

   public WsdlPort getWsdlPort() {
      return this.port.getWsdlPort();
   }

   public void setWsPort(WsPort var1) {
      this.port = var1;
   }

   public QName getPortName() {
      return this.port.getWsdlPort().getName();
   }

   public QName getServiceName() {
      return this.port.getWsdlPort().getService().getName();
   }

   public static class ServiceContext {
      private static ThreadLocal<EndpointReference> endpointRef = new ThreadLocal<EndpointReference>() {
         protected synchronized EndpointReference initialValue() {
            return null;
         }
      };
      private static Map<String, Object> stubProps = new HashMap();

      public static EndpointReference get() {
         return (EndpointReference)endpointRef.get();
      }

      public static void set(EndpointReference var0) {
         endpointRef.set(var0);
      }

      public static Map<String, Object> getStubProperties() {
         return stubProps;
      }

      public static void propagatePropertiesToClient(WlMessageContext var0) {
         String[] var1 = WsrmConstants.PROP_NAMES_FOR_RM_SOURCE;
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            propagateProperty(var5, var0);
         }

      }

      public static void acceptPropertiesIntoClient(WlMessageContext var0) {
         String[] var1 = WsrmConstants.PROP_NAMES_FOR_RM_SOURCE;
         String[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];
            acceptProperty(var5, var0);
         }

      }

      private static void propagateProperty(String var0, WlMessageContext var1) {
         if (var1.containsProperty(var0)) {
            stubProps.put(var0, var1.getProperty(var0));
         }

      }

      private static void acceptProperty(String var0, WlMessageContext var1) {
         if (!var1.containsProperty(var0) && stubProps.containsKey(var0)) {
            var1.setProperty(var0, stubProps.get(var0));
         }

      }
   }
}
