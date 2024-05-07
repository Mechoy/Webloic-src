package weblogic.wsee.reliability2.tube;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.addressing.WsaPropertyBag;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.j2ee.descriptor.wl.ReliabilityConfigBean;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;
import weblogic.wsee.reliability2.exception.WsrmAbortSendException;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.property.WsrmConfig;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.property.WsrmPropertyBag;
import weblogic.wsee.reliability2.saf.WsrmSAFDispatchFactory;
import weblogic.wsee.reliability2.sequence.SequenceIdFactory;
import weblogic.wsee.ws.WsPort;

public class WsrmServerTube extends AbstractWsrmTube {
   private static final Logger LOGGER = Logger.getLogger(WsrmServerTube.class.getName());
   private ServerTubeAssemblerContext _context;
   private WSEndpointImpl _endpoint;
   private WSBinding _binding;
   private WSDLPort _wsdlPort;
   private WsPort _wsPort;
   private WsrmPolicyHelper _policyHelper;
   @Nullable
   private ReliabilityConfigBean _rmConfig;
   private WsrmTubeUtils _tubeUtil;
   private TubelineSpliceFactory.DispatchFactory _spliceDispatchFactory;

   public WsrmServerTube(Tube var1, ServerTubeAssemblerContext var2, TubelineSpliceFactory.DispatchFactory var3) {
      super(var1);
      WSEndpoint var4 = var2.getEndpoint();
      WSBinding var5 = var2.getEndpoint().getBinding();
      WsPort var6 = WsrmTubelineDeploymentListener.getWsPort(var2);
      WsrmPolicyHelper var7 = new WsrmPolicyHelper(var6);
      PortComponentBean var8 = var6.getPortComponent();
      ReliabilityConfigBean var9 = var8 != null ? var8.getReliabilityConfig() : null;
      this.commonConstructorCode(var2, var4, var5, var2.getWsdlModel(), var6, var7, var9, var3);
   }

   public WsrmServerTube(WsrmServerTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode(var1._context, var1._endpoint, var1._binding, var1._wsdlPort, var1._wsPort, var1._policyHelper, var1._rmConfig, var1._spliceDispatchFactory);
   }

   private void commonConstructorCode(ServerTubeAssemblerContext var1, WSEndpoint var2, WSBinding var3, WSDLPort var4, WsPort var5, WsrmPolicyHelper var6, ReliabilityConfigBean var7, TubelineSpliceFactory.DispatchFactory var8) {
      this._context = var1;
      this._endpoint = (WSEndpointImpl)var2;
      this._binding = var3;
      this._wsdlPort = var4;
      this._wsPort = var5;
      this._policyHelper = var6;
      this._rmConfig = var7;
      this._spliceDispatchFactory = var8;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WsrmServerTube created for port " + (var4 != null ? var4.getName() : null));
         WsrmConfig.Destination var9 = WsrmConfig.getDestination((ServerTubeAssemblerContext)this._context, (Packet)null, false);
         LOGGER.fine("WsrmServerTube: RM Destination Config:\n" + var9);
      }

      this._tubeUtil = new WsrmTubeUtils(false, this._binding, this._wsdlPort, this._policyHelper, new WsrmServerDispatchFactory(this._endpoint), (ClientTubeAssemblerContext)null, this._context);
      this._tubeUtil.setEndpoint(this._endpoint);
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      try {
         WsrmTubeUtils.InboundMessageResult var2 = this._tubeUtil.processInboundMessage(var1);
         if (var2.needSuspendOnCurrentFiber) {
            Fiber.current().addListener(var2.currentFiberSuspendingCallback);
            return this.doSuspend(this.next);
         }

         if (var2.message != null && var2.rmFault == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("WsrmServerTube handling response after processInboundMessage");
            }

            String var3 = var2.message.getHeaders().getAction(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
            Packet var4 = var1.createServerResponse(var2.message, this._binding.getAddressingVersion(), this._binding.getSOAPVersion(), var3);
            var4.soapAction = var3;
            var4.put("javax.xml.ws.soap.http.soapaction.uri", var3);
            return this.processResponse(var4);
         }

         if (var2.handled) {
            return this.doReturnWith(var1.createServerResponse((Message)null, this._binding.getAddressingVersion(), this._binding.getSOAPVersion(), (String)null));
         }
      } catch (RuntimeException var5) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmServerTube processRequest failed: " + var5.toString(), var5);
         }

         throw var5;
      } catch (Exception var6) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmServerTube processRequest failed: " + var6.toString(), var6);
         }

         throw new RuntimeException(var6.toString(), var6);
      }

      return this.doInvoke(this.next, var1);
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      try {
         String var2;
         if (var1.getMessage() == null) {
            var2 = this._binding.getAddressingVersion().anonymousUri;
         } else {
            var2 = var1.getMessage().getHeaders().getTo(this._binding.getAddressingVersion(), this._binding.getSOAPVersion());
         }

         WsrmTubeUtils.OutboundMessageResult var7 = this._tubeUtil.processOutboundMessage(var1, new MySequenceIdFactory(), var2);
         if (var7.messageBuffered || var7.messageAborted) {
            var1.setMessage((Message)null);
         }
      } catch (WsrmAbortSendException var4) {
         var1.setMessage((Message)null);
         return this.doReturnWith(var1);
      } catch (WsrmException var5) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmServerTube processResponse failed: " + var5.toString(), var5);
         }

         var1 = this._tubeUtil.handleOutboundException(var1, var5);
         if (var1 != null) {
            return this.doReturnWith(var1);
         }

         throw new RuntimeException(var5.toString(), var5);
      } catch (Exception var6) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, "WsrmServerTube processResponse failed: " + var6.toString(), var6);
         }

         WsrmException var3 = new WsrmException(var6.toString(), var6);
         var3.fillInStackTrace();
         var1 = this._tubeUtil.handleOutboundException(var1, var3);
         if (var1 != null) {
            return this.doReturnWith(var1);
         }

         return this.doThrow(var6);
      }

      return this.doReturnWith(var1);
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      return super.processException(var1);
   }

   public void preDestroy() {
      ServerTubeAssemblerContext var1 = this._tubeUtil.getServerContext();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("WsrmServerTube preDestroy() unRegistering BufferingFeature. ");
      }

      BufferingFeature.unRegisterBufferingFeature(var1);
      DispatchFactoryResolver.unregisterServerDispatchFactory(this._context.getEndpoint().getEndpointId());
      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   public AbstractFilterTubeImpl copy(TubeCloner var1) {
      return new WsrmServerTube(this, var1);
   }

   private class MySequenceIdFactory implements SequenceIdFactory {
      public MySequenceIdFactory() {
      }

      public SequenceIdFactory.Info getSequenceId(String var1, boolean var2, Packet var3) {
         WsrmPropertyBag var4 = (WsrmPropertyBag)WsrmPropertyBag.propertySetRetriever.getFromPacket(var3);
         SequenceIdFactory.Info var5 = new SequenceIdFactory.Info();
         var5.id = var4.getOutboundSequenceId();
         var5.preExisting = var5.id != null;
         if (var5.id == null && !var2) {
            if (WsrmServerTube.LOGGER.isLoggable(Level.FINE)) {
               WsrmServerTube.LOGGER.fine("WsrmServerTube.MySequenceIdFactory.getSequenceId: Offer sequence is not found.");
            }

            WsaPropertyBag var6 = (WsaPropertyBag)var3.getSatellite(WsaPropertyBag.class);
            WSEndpointReference var7 = var6.getReplyToFromRequest();
            if (var7 == null || var7.isAnonymous()) {
               throw new RuntimeException("ReplyTo EPR found anonymous in the request. No offer scenario is not supported for this case.");
            }
         }

         if (var5.id == null) {
            WsrmInvocationPropertyBag var8 = (WsrmInvocationPropertyBag)var3.invocationProperties.get(WsrmInvocationPropertyBag.key);
            var5.id = var8.getSequenceId();
         }

         var5.safDispatchKey = new WsrmSAFDispatchFactory.ServerSideKey(WsrmServerTube.this._endpoint.getEndpointId());
         return var5;
      }
   }
}
