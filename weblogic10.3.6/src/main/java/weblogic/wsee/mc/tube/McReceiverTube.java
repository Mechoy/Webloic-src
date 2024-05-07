package weblogic.wsee.mc.tube;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.api.pipe.NextAction;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.fault.SOAPFaultBuilder;
import com.sun.xml.ws.model.CheckedExceptionImpl;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.jaxws.MonitoringPipe;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.mc.api.McPolicyInfo;
import weblogic.wsee.mc.api.McPolicyInfoFactory;
import weblogic.wsee.mc.exception.McException;
import weblogic.wsee.mc.faults.MissingSelectionFaultMsg;
import weblogic.wsee.mc.faults.UnsupportedSelectionFaultMsg;
import weblogic.wsee.mc.headers.MessagePendingHeader;
import weblogic.wsee.mc.mbean.WseeMcRuntimeMBeanImpl;
import weblogic.wsee.mc.messages.McMsg;
import weblogic.wsee.mc.processor.McPending;
import weblogic.wsee.mc.processor.McPendingManager;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.policy.framework.NormalizedExpression;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyServer;
import weblogic.wsee.ws.WsPort;

public class McReceiverTube extends AbstractFilterTubeImpl {
   private static final Logger LOGGER = Logger.getLogger(McReceiverTube.class.getName());
   private ServerTubeAssemblerContext _context;
   private WSEndpointImpl _endpoint;
   private WSBinding _binding;
   private AddressingVersion _addrVersion;
   private SOAPVersion _soapVersion;
   private WSDLPort _wsdlPort;
   private McPolicyInfo _policy;
   private McTubeUtils _tubeUtils;
   private static final Object _createPendingLock = "CreatePendingLock";

   public McReceiverTube(Tube var1, ServerTubeAssemblerContext var2) {
      super(var1);
      WSEndpoint var3 = var2.getEndpoint();
      WSBinding var4 = var2.getEndpoint().getBinding();
      WSDLPort var5 = var2.getWsdlModel();
      this.commonConstructorCode(var2, var3, var4, var5);
   }

   public McReceiverTube(McReceiverTube var1, TubeCloner var2) {
      super(var1, var2);
      this.commonConstructorCode(var1._context, var1._endpoint, var1._binding, var1._wsdlPort);
   }

   private void commonConstructorCode(ServerTubeAssemblerContext var1, WSEndpoint var2, WSBinding var3, WSDLPort var4) {
      this._context = var1;
      this._endpoint = (WSEndpointImpl)var2;
      this._binding = var3;
      this._addrVersion = var3.getAddressingVersion();
      this._soapVersion = var3.getSOAPVersion();
      this._wsdlPort = var4;
      this.setMcPolicy();
      this._tubeUtils = new McTubeUtils(this._endpoint, (WSService)null, this._binding);
   }

   @NotNull
   public NextAction processRequest(@NotNull Packet var1) {
      Message var2 = var1.getMessage();
      if (var2 == null) {
         return this.doInvoke(this.next, var1);
      } else {
         HeaderList var3 = var2.getHeaders();
         String var4 = var3.getAction(this._addrVersion, this._soapVersion);
         McPendingManager var5 = McPendingManager.getInstance();
         boolean var9;
         if (!McConstants.Action.MC.matchesAnyMCVersion(var4)) {
            WSEndpointReference var19 = var3.getReplyTo(this._addrVersion, this._soapVersion);
            WSEndpointReference var20 = var3.getFaultTo(this._addrVersion, this._soapVersion);
            if (var20 == null) {
               var20 = var19;
            }

            boolean var22 = McTubeUtils.isMcAnonURI(var19);
            var9 = McTubeUtils.isMcAnonURI(var20);
            if (this._policy.isMcOptional() || var22 && var9) {
               return this.doInvoke(this.next, var1);
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.log(Level.FINE, "processRequest failed: policy required mandatory MC anonymous URI(s)");
               }

               return this.doThrow(new McException(WseeMCLogger.logPolicyURIMismatchLoggable().getMessage()));
            }
         } else {
            McMsg var6 = new McMsg();

            try {
               var6.readFromSOAPMsg(var2.readAsSOAPMessage());
            } catch (SOAPException var18) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.log(Level.FINE, "processRequest failed: exception reading SOAP message: " + var18);
               }

               WseeMCLogger.logUnexpectedException(var18.toString(), var18);
               return this.doThrow(var18);
            }

            List var7 = var6.getOther();
            if (var7.size() > 0) {
               UnsupportedSelectionFaultMsg var21 = new UnsupportedSelectionFaultMsg(McConstants.McVersion.MC_11);
               var21.setUnsupported(var7);

               Message var24;
               try {
                  var24 = McProtocolUtils.createMessageFromFaultMessage(var21, this._addrVersion, this._soapVersion);
               } catch (SOAPException var16) {
                  WseeMCLogger.logUnexpectedException(var16.toString(), var16);
                  return this.doThrow(var16);
               }

               return this.doReturnWith(var1.createServerResponse(var24, this._addrVersion, this._soapVersion, (String)null));
            } else {
               String var8 = var6.getAddress();
               if (var8 == null) {
                  MissingSelectionFaultMsg var23 = new MissingSelectionFaultMsg(McConstants.McVersion.MC_11);

                  Message var25;
                  try {
                     var25 = McProtocolUtils.createMessageFromFaultMessage(var23, this._addrVersion, this._soapVersion);
                  } catch (SOAPException var17) {
                     WseeMCLogger.logUnexpectedException(var17.toString(), var17);
                     return this.doThrow(var17);
                  }

                  return this.doReturnWith(var1.createServerResponse(var25, this._addrVersion, this._soapVersion, (String)null));
               } else {
                  var9 = McTubeUtils.isMcAnonURI(var8);
                  if (!var9) {
                     return this.doReturnWith(var1.createServerResponse((Message)null, this._addrVersion, this._soapVersion, (String)null));
                  } else {
                     String var10 = McTubeUtils.getUUID(var8);
                     McPending var11 = var5.getPending(var10);
                     if (var11 == null) {
                        return this.doReturnWith(var1.createServerResponse((Message)null, this._addrVersion, this._soapVersion, (String)null));
                     } else {
                        String var12 = var11.getAddress();
                        if (var12 == null) {
                           if (LOGGER.isLoggable(Level.FINE)) {
                              LOGGER.log(Level.FINE, "processRequest: no address associated with pending list, target: " + McProtocolUtils.decodeId(var10));
                           }

                           return this.doReturnWith(var1.createServerResponse((Message)null, this._addrVersion, this._soapVersion, (String)null));
                        } else if (!var12.equals(McTubeUtils.getUriPattern(var1))) {
                           return this.doThrow(new WebServiceException("MakeConnection sent to different service address than original request that used the shared MC anon URI. Anon URI=" + var8 + " Original request address: " + var12 + " Address of MakeConnection: " + McTubeUtils.getUriPattern(var1)));
                        } else {
                           Packet var13 = var11.removeMessage(var1);
                           if (var13 == null) {
                              if (LOGGER.isLoggable(Level.FINE)) {
                                 LOGGER.log(Level.FINE, "processRequest: no pending packet in store, target: " + McProtocolUtils.decodeId(var10));
                              }

                              return this.doReturnWith(var1.createServerResponse((Message)null, this._addrVersion, this._soapVersion, (String)null));
                           } else {
                              if (LOGGER.isLoggable(Level.FINE)) {
                                 HeaderList var14 = var13.getMessage().getHeaders();
                                 String var15 = var14.getAction(this._addrVersion, this._soapVersion);
                                 LOGGER.log(Level.FINE, "processRequest: removed pending packet from store, target: " + McProtocolUtils.decodeId(var10) + " action = " + var15);
                              }

                              var5.updatePending(var11);
                              if (var11.size() > 0) {
                                 MessagePendingHeader var26 = new MessagePendingHeader();
                                 var26.setPending(true);
                                 var3 = var13.getMessage().getHeaders();
                                 var3.addOrReplace(var26);
                              }

                              return this.doReturnWith(var13);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @NotNull
   public NextAction processResponse(@NotNull Packet var1) {
      Message var2 = var1.getMessage();
      if (var2 == null) {
         return this.doReturnWith(var1);
      } else {
         WSEndpointReference var3 = (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.ReplyToFromRequest");
         WSEndpointReference var4 = (WSEndpointReference)var1.get("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest");
         if (var4 == null) {
            var4 = var3;
         }

         WSEndpointReference var5 = var2.isFault() ? var4 : var3;
         HeaderList var6 = var2.getHeaders();
         String var7 = var6.getAction(this._addrVersion, this._soapVersion);
         if (!McTubeUtils.isMcAnonURI(var5)) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "processResponse: non MC anon target msg being passed through, target: " + var5.getAddress() + " action: " + var7);
            }

            return this.doReturnWith(var1);
         } else {
            McPendingManager var8 = McPendingManager.getInstance();
            String var9 = McTubeUtils.getUUID(var5);
            McPending var10;
            synchronized(_createPendingLock) {
               var10 = var8.getPending(var9);
               if (var10 == null) {
                  PersistenceConfig.Service var12 = PersistenceConfig.getServiceConfig(this._context);
                  String var13 = var12.getLogicalStoreName();
                  var10 = new McPending(var9, var13, this._addrVersion, this._soapVersion);
                  var10.setAddress(McTubeUtils.getUriPattern((WSEndpoint)this._endpoint));
                  var8.addPending(var10);
               }

               WseeMcRuntimeMBeanImpl var17 = McTubeUtils.getMcRuntimeFromPacket(var1);
               if (var17 != null) {
                  var17.addAnonymousEndpointId(var9);
               }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               String var11 = var6.getRelatesTo(this._addrVersion, this._soapVersion);
               LOGGER.log(Level.FINE, "processResponse: adding packet to pending list, target: " + McProtocolUtils.decodeId(var9) + " action = " + var7 + " relatesTo " + var11);
            }

            var10.addMessage(var1);
            var8.updatePending(var10);
            var1.setMessage((Message)null);
            MonitoringPipe.MonitoringPropertySet var16 = (MonitoringPipe.MonitoringPropertySet)var1.getSatellite(MonitoringPipe.MonitoringPropertySet.class);
            if (var16 != null) {
               var16.setOpName("Ws-Protocol");
            }

            return this.doReturnWith(var1);
         }
      }
   }

   @NotNull
   public NextAction processException(@NotNull Throwable var1) {
      Packet var2 = Fiber.current().getPacket();
      WSEndpointReference var3 = (WSEndpointReference)var2.get("com.sun.xml.ws.addressing.WsaPropertyBag.FaultToFromRequest");
      if (!McTubeUtils.isMcAnonURI(var3)) {
         return this.doThrow(var1);
      } else {
         Packet var4 = var2.createServerResponse(SOAPFaultBuilder.createSOAPFaultMessage(this._soapVersion, (CheckedExceptionImpl)null, var1), this._wsdlPort, var2.endpoint.getSEIModel(), this._binding);
         McPendingManager var5 = McPendingManager.getInstance();
         String var6 = McTubeUtils.getUUID(var3);
         McPending var7;
         synchronized(_createPendingLock) {
            var7 = var5.getPending(var6);
            if (var7 == null) {
               PersistenceConfig.Service var9 = PersistenceConfig.getServiceConfig(this._context);
               String var10 = var9.getLogicalStoreName();
               var7 = new McPending(var6, var10, this._addrVersion, this._soapVersion);
               var7.setAddress(McTubeUtils.getUriPattern((WSEndpoint)this._endpoint));
               var5.addPending(var7);
            }

            WseeMcRuntimeMBeanImpl var13 = McTubeUtils.getMcRuntimeFromPacket(var4);
            if (var13 != null) {
               var13.addAnonymousEndpointId(var6);
            }
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "processException: adding pending packet to store, target: " + McProtocolUtils.decodeId(var6));
         }

         var7.addMessage(var4);
         var5.updatePending(var7);
         var4 = var4.copy(false);
         return this.doReturnWith(var4);
      }
   }

   private void setMcPolicy() {
      EnvironmentFactory var1 = JAXRPCEnvironmentFeature.getFactory((WSEndpoint)this._endpoint);
      EnvironmentFactory.SingletonService var2 = var1.getService();
      QName var3 = var1.getPort().getName();
      WsPort var4 = var2.getPort(var3.getLocalPart());
      PolicyServer var5 = var2.getPolicyServer();

      try {
         NormalizedExpression var6 = var5.getEndpointPolicy(var4.getWsdlPort());
         this._policy = McPolicyInfoFactory.getInstance(var6);
      } catch (PolicyException var8) {
         WseeMCLogger.logUnexpectedException(var8.toString(), var8);
         throw new RuntimeException(var8.toString(), var8);
      }
   }

   public void preDestroy() {
      if (this.next != null) {
         this.next.preDestroy();
      }

   }

   public AbstractTubeImpl copy(TubeCloner var1) {
      return new McReceiverTube(this, var1);
   }
}
