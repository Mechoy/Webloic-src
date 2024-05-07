package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.istack.NotNull;
import com.sun.xml.ws.addressing.model.InvalidAddressingHeaderException;
import com.sun.xml.ws.api.Component;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.client.ServiceCreationInterceptor;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.developer.WSBindingProvider;
import com.sun.xml.ws.message.RelatesToHeader;
import com.sun.xml.ws.message.StringHeader;
import java.util.Arrays;
import java.util.Collections;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.Binding;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.WebServiceException;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.management.runtime.WseePortRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jaxws.EndpointCreationInterceptor;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.tubeline.FlowControl;
import weblogic.wsee.jaxws.tubeline.FlowControlAware;
import weblogic.wsee.jaxws.tubeline.FlowControlRequired;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.monitoring.WseeClientRuntimeData;
import weblogic.wsee.monitoring.WseeClientRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WsspStats;

public class TubeFactory implements weblogic.wsee.jaxws.tubeline.TubeFactory {
   public static final String PRE_THROW_CONTEXT_PROPERTY = "weblogic.wsee.jaxws.framework.jaxrpc.PreThrowContext";
   private HandlerInfo info;

   public TubeFactory(HandlerInfo var1) {
      this.info = var1;
   }

   public Tube createClient(Tube var1, ClientTubeAssemblerContext var2) {
      EnvironmentFactory var3 = this.getEnvironmentFactory(var2);

      try {
         return new JAXRPCTube(var3, this.newHandler(), var1);
      } catch (HandlerException var5) {
         throw new WebServiceException(var5);
      }
   }

   public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
      if (var2.getEndpoint().getContainer() instanceof WLSContainer) {
         EnvironmentFactory var3 = this.getEnvironmentFactory(var2.getEndpoint());

         try {
            return new ServerJAXRPCTube(var3, this.newHandler(), var1);
         } catch (HandlerException var5) {
            throw new WebServiceException(var5);
         }
      } else {
         return var1;
      }
   }

   HandlerInfo getHandlerInfo() {
      return this.info;
   }

   protected EnvironmentFactory getEnvironmentFactory(ClientTubeAssemblerContext var1) {
      return JAXRPCEnvironmentFeature.getFactory(var1);
   }

   protected EnvironmentFactory getEnvironmentFactory(WSEndpoint var1) {
      return JAXRPCEnvironmentFeature.getFactory(var1);
   }

   private Handler newHandler() throws HandlerException {
      Class var1 = this.info.getHandlerClass();

      try {
         Handler var2 = (Handler)var1.newInstance();
         var2.init(this.info);
         return var2;
      } catch (InstantiationException var3) {
         throw new HandlerException("Exception in handler:" + var1.getName(), var3);
      } catch (IllegalAccessException var4) {
         throw new AssertionError(var4);
      }
   }

   private class ServerJAXRPCTube extends JAXRPCTube {
      public ServerJAXRPCTube(EnvironmentFactory var2, Handler var3, Tube var4) {
         super(var2, var3, var4);
      }

      protected ServerJAXRPCTube(ServerJAXRPCTube var2, TubeCloner var3) {
         super(var2, var3);
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return TubeFactory.this.new ServerJAXRPCTube(this, var1);
      }

      protected Packet createThrowablePacket(Packet var1, Throwable var2) {
         return var1.createServerResponse(this.createThrowableMessage(var2), var1.endpoint.getPort(), (SEIModel)null, var1.endpoint.getBinding());
      }

      private void _populateAddressingHeaders(Packet var1) {
         AddressingVersion var2 = var1.getBinding().getAddressingVersion();
         SOAPVersion var3 = var1.getBinding().getSOAPVersion();
         HeaderList var4 = var1.getMessage().getHeaders();
         WSEndpointReference var5 = var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest") ? (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest") : null;
         WSEndpointReference var6 = var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest") ? (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest") : null;
         String var7 = var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest") ? (String)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest") : null;
         boolean var8 = false;

         String var9;
         try {
            var9 = var4.getTo(var2, var3);
            if (var9 == null || var2.anonymousUri.equals(var9)) {
               WSEndpointReference var10 = var1.getMessage().isFault() ? var6 : var5;
               if (var10 != null && !var10.isAnonymous()) {
                  var8 = true;
                  var4.add(new StringHeader(var2.toTag, var10.getAddress()));
               }
            }
         } catch (InvalidAddressingHeaderException var11) {
         }

         if (var4.getAction(var2, var3) == null) {
            var9 = null;
            if (var1.getMessage().isFault()) {
               var9 = var2.getDefaultFaultAction();
            } else {
               var9 = var1.soapAction;
               if (var9 != null && !"".equals(var9) && !"\"\"".equals(var9)) {
                  if (var9.startsWith("\"") && var9.endsWith("\"")) {
                     var9 = var9.substring(1, var9.length() - 1);
                  }
               } else {
                  var9 = "http://jax-ws.dev.java.net/addressing/output-action-not-set";
               }
            }

            var4.add(new StringHeader(var2.actionTag, var9));
         }

         if (var4.getMessageID(var2, var3) == null) {
            var4.add(new StringHeader(var2.messageIDTag, var1.getMessage().getID(var2, var3)));
         }

         if (var4.getRelatesTo(var2, var3) == null && var7 != null) {
            var4.add(new RelatesToHeader(var2.relatesToTag, var7));
         }

         if (var8) {
            WSEndpointReference var12;
            if (var1.getMessage().isFault()) {
               var12 = var6;
               if (var6 == null) {
                  var12 = var5;
               }
            } else {
               var12 = var5;
            }

            if (var12 != null) {
               var12.addReferenceParameters(var4);
            }
         }

      }

      private void populateAddressingHeaders(Packet var1) {
         SOAPMessageRefactor var2 = new SOAPMessageRefactor();
         var2.init(var1.getMessage());
         this._populateAddressingHeaders(var1);
         var1.setMessage(var2.refator(var1.getMessage()));
      }

      private boolean isAddressingRequired(Packet var1) {
         WSBinding var2 = this.fac.getBinding();
         AddressingVersion var3 = var2.getAddressingVersion();
         if (var3 == null) {
            return false;
         } else {
            String var4 = var1.supports("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest") ? (String)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.MessageIdFromRequest") : null;
            if (var4 == null) {
               return false;
            } else {
               return var1.getMessage() != null;
            }
         }
      }

      protected void processReturn(Packet var1) {
         if (this.isAddressingRequired(var1)) {
            this.populateAddressingHeaders(var1);
         }

      }
   }

   private class JAXRPCTube extends AbstractFilterTubeImpl implements ServiceCreationInterceptor, EndpointCreationInterceptor, FlowControlAware {
      protected EnvironmentFactory fac;
      private Handler handler;

      public JAXRPCTube(EnvironmentFactory var2, Handler var3, Tube var4) {
         super(var4);
         this.fac = var2;
         this.handler = var3;
         QName[] var5 = var3.getHeaders();
         if (var5 != null) {
            Collections.addAll(((BindingImpl)var2.getBinding()).getKnownHeaders(), var5);
         }

      }

      protected JAXRPCTube(JAXRPCTube var2, TubeCloner var3) {
         super(var2, var3);
         this.fac = var2.fac;
         this.handler = var2.handler;
      }

      public boolean isFlowControlRequired() {
         return this.handler instanceof FlowControlRequired;
      }

      public void preDestroy() {
         try {
            this.handler.destroy();
         } catch (JAXRPCException var2) {
            throw new WebServiceException(var2);
         }

         super.preDestroy();
      }

      protected Message createThrowableMessage(Throwable var1) {
         return Messages.create(var1, this.fac.getBinding().getSOAPVersion());
      }

      protected Packet createThrowablePacket(Packet var1, Throwable var2) {
         return var1.createClientResponse(this.createThrowableMessage(var2));
      }

      public NextAction processException(Throwable var1) {
         if (!(var1 instanceof ProtocolException)) {
            return this.doThrow(var1);
         } else {
            FlowControl var3;
            try {
               Packet var2 = Fiber.current().getPacket();
               SOAPMessageContext var15 = this.fac.getContext(var2);
               Packet var4 = this.createThrowablePacket(var2, var1);
               SOAPMessageContext var5 = this.fac.getContext(var4);
               var5.setFault(var1, false);
               var5.setProperty("weblogic.wsee.jaxws.framework.jaxrpc.PreThrowContext", var15);

               try {
                  if (!this.handler.handleFault(var5)) {
                     FlowControl var6 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
                     if (var6 != null) {
                        var6.doSkip();
                     }
                  }
               } finally {
                  var5.updatePacket();
                  var5.removeProperty("weblogic.wsee.jaxws.framework.jaxrpc.PreThrowContext");
               }

               return this.doReturnWith(var4);
            } catch (SOAPFaultException var13) {
               var3 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
               if (var3 != null) {
                  var3.doSkip();
               }

               throw new javax.xml.ws.soap.SOAPFaultException(this.createSOAPFault(var13));
            } catch (JAXRPCException var14) {
               var3 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
               if (var3 != null) {
                  var3.doSkip();
               }

               throw new WebServiceException(var14);
            }
         }
      }

      private SOAPFault createSOAPFault(SOAPFaultException var1) {
         try {
            SOAPFault var2 = this.fac.getBinding().getSOAPVersion().getSOAPFactory().createFault();
            SOAPFaultUtil.fillFault(var2, var1);
            return var2;
         } catch (SOAPException var3) {
            throw new WebServiceException(var3);
         }
      }

      public NextAction processRequest(Packet var1) {
         SOAPMessageContext var2 = this.fac.getContext(var1);
         this.setupWsspStats(var1.component, var1.getBinding(), var2);

         try {
            try {
               try {
                  if (this.handler.handleRequest(var2)) {
                     NextAction var3 = this.doInvoke(this.next, var1);
                     return var3;
                  }
               } finally {
                  var2.updatePacket();
               }

               this.processReturn(var1);
               return this.processResponse(var1);
            } catch (SOAPFaultException var11) {
               throw new javax.xml.ws.soap.SOAPFaultException(this.createSOAPFault(var11));
            } catch (JAXRPCException var12) {
               throw new WebServiceException(var12);
            }
         } catch (WebServiceException var13) {
            return this.processException(var13);
         }
      }

      public NextAction processResponse(Packet var1) {
         if (var1.getMessage() == null) {
            return this.doReturnWith(var1);
         } else {
            FlowControl var3;
            try {
               SOAPMessageContext var2 = this.fac.getContext(var1);

               boolean var12;
               try {
                  var12 = var2.hasFault() ? this.handler.handleFault(var2) : this.handler.handleResponse(var2);
               } finally {
                  var2.updatePacket();
               }

               if (!var12) {
                  FlowControl var4 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
                  if (var4 != null) {
                     var4.doSkip();
                  }
               }

               return this.doReturnWith(var1);
            } catch (SOAPFaultException var10) {
               var3 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
               if (var3 != null) {
                  var3.doSkip();
               }

               throw new javax.xml.ws.soap.SOAPFaultException(this.createSOAPFault(var10));
            } catch (JAXRPCException var11) {
               var3 = (FlowControl)Fiber.current().getSPI(FlowControl.class);
               if (var3 != null) {
                  var3.doSkip();
               }

               throw new WebServiceException(var11);
            }
         }
      }

      protected void processReturn(Packet var1) {
      }

      public AbstractTubeImpl copy(TubeCloner var1) {
         return TubeFactory.this.new JAXRPCTube(this, var1);
      }

      private void setupWsspStats(Component var1, WSBinding var2, SOAPMessageContext var3) {
         WseeV2RuntimeMBean var4 = var1 != null ? (WseeV2RuntimeMBean)var1.getSPI(WseeV2RuntimeMBean.class) : null;
         WlMessageContext var5 = WlMessageContext.narrow(var3);
         if (var4 != null && var5 != null) {
            if (var5.getDispatcher().getWsPort().getWsspStats() == null) {
               WseePortRuntimeMBean[] var10 = var4.getPorts();
               int var11 = var10.length;

               for(int var8 = 0; var8 < var11; ++var8) {
                  WseePortRuntimeMBean var9 = var10[var8];
                  if (var9.getName().equals(var5.getDispatcher().getWsdlPort().getName().getLocalPart())) {
                     var5.getDispatcher().getWsPort().setWsspStats((WsspStats)var9.getPortPolicy());
                     break;
                  }
               }
            }
         } else {
            ClientIdentityFeature var6 = null;
            if (var2 != null) {
               var6 = (ClientIdentityFeature)var2.getFeature(ClientIdentityFeature.class);
            }

            if (var6 != null) {
               WseeClientRuntimeMBeanImpl var7 = ClientIdentityRegistry.getClientRuntimeMBean(var6.getClientId());
               if (var7 != null && var7.getData() != null && ((WseeClientRuntimeData)var7.getData()).getPort() != null) {
                  var5.getDispatcher().getWsPort().setWsspStats((WsspStats)((WsspStats)var7.getPort().getPortPolicy()));
               }
            }
         }

      }

      public void postCreateProxy(@NotNull WSBindingProvider var1, @NotNull Class<?> var2) {
         this.addKnownHeadersToBinding(var1.getBinding());
      }

      public void postCreateDispatch(@NotNull WSBindingProvider var1) {
         this.addKnownHeadersToBinding(var1.getBinding());
      }

      public void postCreateEndpoint(WSEndpoint var1) {
         var1.getBinding();
      }

      private void addKnownHeadersToBinding(Binding var1) {
         QName[] var2 = this.handler.getHeaders();
         if (var2 != null && var1 instanceof BindingImpl) {
            ((BindingImpl)var1).getKnownHeaders().addAll(Arrays.asList(var2));
         }

      }

      public String toString() {
         return super.toString() + "[" + this.handler.toString() + "]";
      }
   }
}
