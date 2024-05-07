package weblogic.wsee.addressing;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import weblogic.protocol.LocalServerIdentity;
import weblogic.wsee.addressing.policy.api.UsingAddressingPolicyInfo;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.jms.JMSServerTransport;
import weblogic.wsee.connection.transport.jms.JmsServerQueueTransport;
import weblogic.wsee.connection.transport.jms.JmsTransport;
import weblogic.wsee.connection.transport.servlet.HttpServerTransport;
import weblogic.wsee.connection.transport.servlet.HttpTransportUtils;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jaxrpc.soapfault.WLSOAPFaultException;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.HeaderUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public final class ServerAddressingHandler extends AddressingHandler {
   private static final boolean verbose = Verbose.isVerbose(ServerAddressingHandler.class);
   public static final String SERVER_ENDPOINT = "weblogic.wsee.addressing.ServerEndpoint";
   public static final String HAS_WSA_EXCEPTION = "weblogic.wsee.addressing.server.hasexception";

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         SOAPMessageContext var3 = (SOAPMessageContext)var1;
         if (verbose) {
            Verbose.log((Object)"Looking for WS-Addressing headers");
         }

         this.setWSAVersion(var2);
         this.validateWSAVersion(var2);
         AddressingProvider var4 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var2);
         String var5 = null;
         EndpointReference var6 = null;
         if (var2.getDispatcher() != null) {
            Transport var7 = var2.getDispatcher().getConnection().getTransport();
            var5 = (String)var2.getProperty("weblogic.wsee.connection.end_point_address");
            if (var5 == null) {
               var5 = var2.getDispatcher().getConnection().getTransport().getEndpointAddress();
            }

            if (verbose) {
               Verbose.say("ServerAddressingHandler calculating SERVER_ENDPOINT property from transport endpoint addr: " + var5);
            }

            if (var5 != null) {
               try {
                  String var8;
                  String var9;
                  HttpServletRequest var10;
                  if (!(var7 instanceof JMSServerTransport) && !(var7 instanceof JmsTransport) && !(var7 instanceof JmsServerQueueTransport)) {
                     if (var7 instanceof HttpServerTransport) {
                        var10 = HttpTransportUtils.getHttpServletRequest(var2);
                        var9 = var10.getScheme();
                        var8 = var7.getServiceURI();
                     } else {
                        var9 = "http";
                        var8 = var5;
                     }
                  } else {
                     var9 = "jms";
                     var8 = var5;
                  }

                  var10 = null;
                  String var25;
                  if (var7 instanceof HttpServerTransport && ServerUtil.useReqHostAlways) {
                     boolean var11 = var9.equals("https");
                     var25 = ServerUtil.getHTTPServerURL(var11, HttpTransportUtils.getHttpServletRequest(var2));
                  } else {
                     var25 = ServerUtil.getServerURL(var9);
                  }

                  if (var25.endsWith("/")) {
                     var25 = var25.substring(0, var25.length() - 1);
                  }

                  if (!var8.startsWith("/")) {
                     var8 = "/" + var8;
                  }

                  var5 = var25 + var8;
                  var6 = var4.createEndpointReference(var5);
                  ServiceIdentityHeader var26 = new ServiceIdentityHeader();
                  var26.setServerName(LocalServerIdentity.getIdentity().getServerName());
                  var26.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
                  var6.getReferenceParameters().addHeader(var26);
               } catch (Exception var21) {
                  throw new RuntimeException(var21.toString(), var21);
               }
            }

            var1.setProperty("weblogic.wsee.addressing.ServerEndpoint", var6);
            if (verbose) {
               Verbose.say("ServerAddressingHandler finished calculating SERVER_ENDPOINT as: " + var6);
            }
         }

         MsgHeaders var22 = var2.getHeaders();
         boolean var23 = false;
         boolean var24 = false;
         boolean var28 = false;
         ToHeader var27 = (ToHeader)var22.getHeader(ToHeader.TYPE);
         if (var27 != null) {
            var23 = true;
            var24 = true;
            var1.setProperty("weblogic.wsee.addressing.To", var27.getAddress());
            HeaderUtil.removeMustUnderstandFromHeader(var27, var3.getMessage());
         } else {
            if (var5 != null) {
               var1.setProperty("weblogic.wsee.addressing.To", var5);
            } else {
               var1.setProperty("weblogic.wsee.addressing.To", var4.getAnonymousNamespaceURI());
            }

            if (WSAVersion.WSA10.equals(var4.getWSAVersion())) {
               var27 = var4.createToHeader(var4.getAnonymousNamespaceURI());
               var24 = true;
            }
         }

         boolean var12 = false;
         boolean var13 = false;
         FromHeader var14 = (FromHeader)var22.getHeader(FromHeader.TYPE);
         if (var14 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var14, var3.getMessage());
            var23 = true;
            var1.setProperty("weblogic.wsee.addressing.From", var14.getReference());
            if (var4.isAnonymousReferenceURI(var14.getReference().getAddress())) {
               var1.setProperty("weblogic.wsee.from.anonymous", "true");
               var12 = true;
            }
         } else {
            var1.setProperty("weblogic.wsee.addressing.From", var4.createAnonymousEndpointReference());
            var1.setProperty("weblogic.wsee.from.anonymous", "true");
            var12 = true;
         }

         ReplyToHeader var15 = (ReplyToHeader)var22.getHeader(ReplyToHeader.TYPE);
         if (var15 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var15, var3.getMessage());
            var23 = true;
            var1.setProperty("weblogic.wsee.addressing.ReplyTo", var15.getReference());
            if (var4.isAnonymousReferenceURI(var15.getReference().getAddress())) {
               var1.setProperty("weblogic.wsee.reply.anonymous", "true");
               var13 = true;
            }
         } else if (var12) {
            var1.setProperty("weblogic.wsee.addressing.ReplyTo", var4.createAnonymousEndpointReference());
            var1.setProperty("weblogic.wsee.reply.anonymous", "true");
            var13 = true;
         } else {
            var1.setProperty("weblogic.wsee.addressing.ReplyTo", var14.getReference());
         }

         FaultToHeader var16 = (FaultToHeader)var22.getHeader(FaultToHeader.TYPE);
         if (var16 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var16, var3.getMessage());
            var23 = true;
            var1.setProperty("weblogic.wsee.addressing.FaultTo", var16.getReference());
            if (var4.getAnonymousNamespaceURI().equals(var16.getReference().getAddress())) {
               var1.setProperty("weblogic.wsee.fault.anonymous", "true");
            }
         } else if (var15 != null) {
            var1.setProperty("weblogic.wsee.addressing.FaultTo", var15.getReference());
            if (var13) {
               var1.setProperty("weblogic.wsee.fault.anonymous", "true");
            }
         } else if (var14 != null) {
            var1.setProperty("weblogic.wsee.addressing.FaultTo", var14.getReference());
            if (var12) {
               var1.setProperty("weblogic.wsee.fault.anonymous", "true");
            }
         } else {
            var1.setProperty("weblogic.wsee.addressing.FaultTo", var4.createAnonymousEndpointReference());
            var1.setProperty("weblogic.wsee.fault.anonymous", "true");
         }

         MessageIdHeader var17 = (MessageIdHeader)var22.getHeader(MessageIdHeader.TYPE);
         if (var17 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var17, var3.getMessage());
            var23 = true;
            var1.setProperty("weblogic.wsee.addressing.MessageId", var17.getMessageId());
         }

         if (var23) {
            var1.setProperty("weblogic.wsee.complex", "true");
            this.checkDuplicatedAddressingHeader(var2);
         }

         ActionHeader var18 = (ActionHeader)var22.getHeader(ActionHeader.TYPE);
         if (var18 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var18, var3.getMessage());
            var23 = true;
            var28 = true;
            var1.setProperty("weblogic.wsee.addressing.Action", var18.getActionURI());
         }

         RelatesToHeader var19 = (RelatesToHeader)var22.getHeader(RelatesToHeader.TYPE);
         if (var19 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var19, var3.getMessage());
            var23 = true;
            var1.setProperty("weblogic.wsee.addressing.RelatesTo", var19.getRelatedMessageId());
         }

         if (var23) {
            if (!var24) {
               throw new AddressingHeaderException(var4.getMessageAddressingHeaderRequiredReason(), "To", new QName[]{var4.getMessageAddressingHeaderRequiredFaultQName()});
            }

            if (!var28) {
               throw new AddressingHeaderException(var4.getMessageAddressingHeaderRequiredReason(), "Action", new QName[]{var4.getMessageAddressingHeaderRequiredFaultQName()});
            }
         }

         CallbackToHeader var20 = (CallbackToHeader)var22.getHeader(CallbackToHeader.TYPE);
         if (var20 != null) {
            HeaderUtil.removeMustUnderstandFromHeader(var20, var3.getMessage());
            var1.setProperty("weblogic.wsee.addressing.CallbackTo", var20.getReference());
         }

         if (verbose) {
            if (var27 != null) {
               Verbose.log((Object)("<To> " + var27.getAddress()));
            }

            if (var14 != null) {
               Verbose.log((Object)("<From> " + var14.getReference()));
            }

            if (var15 != null) {
               Verbose.log((Object)("<ReplyTo> " + var15.getReference()));
            }

            if (var16 != null) {
               Verbose.log((Object)("<FaultTo> " + var16.getReference()));
            }

            if (var18 != null) {
               Verbose.log((Object)("<Action> " + var18.getActionURI()));
            }

            if (var17 != null) {
               Verbose.log((Object)("<MessageId> " + var17.getMessageId()));
            }

            if (var20 != null) {
               Verbose.log((Object)("<CallbackTo> " + var20.getReference()));
            }
         }

         return true;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      if (var1.getProperty("weblogic.wsee.complex") == null) {
         return true;
      } else {
         return var1.getProperty("weblogic.wsee.reliable.oneway.reply") != null ? true : this.handleOutbound(WlMessageContext.narrow(var1), (EndpointReference)var1.getProperty("weblogic.wsee.addressing.ReplyTo"));
      }
   }

   public boolean handleFault(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      if (var1.getProperty("weblogic.wsee.complex") == null) {
         return true;
      } else {
         EndpointReference var3 = (EndpointReference)var1.getProperty("weblogic.wsee.faultto.override");
         if (var3 == null) {
            var3 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.FaultTo");
         }

         if (var3 == null) {
            return true;
         } else {
            AddressingProvider var4 = AddressingProviderFactory.getInstance().getAddressingProvider(var1);
            boolean var5 = var4.isAnonymousReferenceURI(var3.getAddress());
            if (var5 && var2.getProperty("weblogic.wsee.oneway.confirmed") != null) {
               return false;
            } else {
               Throwable var6 = var2.getFault();
               if (var6 instanceof AddressingHeaderException) {
                  AddressingHeaderException var7 = (AddressingHeaderException)var6;
                  this.convertToWSAFault(var7, var2);
               } else {
                  this.addFaultActionHeader(var2);
               }

               return this.handleOutbound(var2, var3);
            }
         }
      }
   }

   private boolean handleOutbound(WlMessageContext var1, EndpointReference var2) {
      AddressingProvider var3 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      ActionHeader var4 = (ActionHeader)var1.getHeaders().getHeader(ActionHeader.TYPE);
      if (var4 == null) {
         var1.removeProperty("weblogic.wsee.addressing.Action");
         var4 = var1.hasFault() ? var3.createFaultActionHeader() : var3.createActionHeader(this.getActionURI(var1, false));
         var1.getHeaders().addHeader(var4);
      }

      String var5 = AddressingUtil.wrapSOAPAction(var4.getActionURI());
      this.setSoapActionHeader(var1, var5);
      MessageIdHeader var6 = (MessageIdHeader)var1.getHeaders().getHeader(MessageIdHeader.TYPE);
      MessageIdHeader var7 = var3.createMessageIdHeader(Guid.generateGuid());
      if (var6 != null) {
         ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var7);
      } else {
         var1.getHeaders().addHeader(var7);
      }

      this.addRelatesToHeader(var1);
      ToHeader var8 = (ToHeader)var1.getHeaders().getHeader(ToHeader.TYPE);
      ToHeader var9 = var3.createToHeader(var2.getAddress());
      if (var8 != null) {
         ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var9);
      } else {
         var1.getHeaders().addHeader(var9);
      }

      var1.setProperty("javax.xml.rpc.service.endpoint.address", var9.getAddress());
      Iterator var10 = var2.getReferenceProperties().listHeaders();

      MsgHeader var11;
      while(var10.hasNext()) {
         var11 = (MsgHeader)var10.next();
         if (var1.getHeaders().getHeader(var11.getType()) != null) {
            ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var11);
         } else {
            var1.getHeaders().addHeader(var11);
         }
      }

      var10 = var2.getReferenceParameters().listHeaders();

      while(var10.hasNext()) {
         var11 = (MsgHeader)var10.next();
         if (!WSAVersion.MemberSubmission.equals(var3.getWSAVersion())) {
            var11.setRefParam(true);
         }

         if (var1.getHeaders().getHeader(var11.getType()) != null) {
            ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var11);
         } else {
            var1.getHeaders().addHeader(var11);
         }
      }

      return true;
   }

   private void addRelatesToHeader(WlMessageContext var1) {
      String var2 = (String)var1.getProperty("weblogic.wsee.addressing.MessageId");
      if (var2 != null) {
         RelatesToHeader var3 = (RelatesToHeader)var1.getHeaders().getHeader(RelatesToHeader.TYPE);
         AddressingProvider var4 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
         QName var5 = "http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var4.getNamespaceURI()) ? WSAddressingConstants.WSA_RESPONSE : null;
         RelatesToHeader var6 = var4.createRelatesToHeader(var2, var5);
         if (var3 != null) {
            ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var6);
         } else {
            var1.getHeaders().addHeader(var6);
         }
      }

   }

   protected void validateWSAVersion(WlMessageContext var1) {
      UsingAddressingPolicyInfo var2 = this.getAddressingPolicyInfo(var1);
      if (var2 != null && var2.getUsingAddressingWSAVersionInfo() != null) {
         WSAVersion var3 = (WSAVersion)var1.getProperty("weblogic.wsee.addressing.version");
         if (var3 == null) {
            var3 = WSAVersion.WSA10;
         }

         if (!var2.isValidWSAVersion(var3)) {
            throw SOAPFaultUtil.newWLSOAPFaultException(var1, (String)"Client", "Sender", "The WS-Addressing version of incoming request does not matched with the WS-Addresssing version which defines with UsingAddressing Policy in WSDL!", (String)null, (Throwable)null);
         }
      }
   }

   protected void checkDuplicatedAddressingHeader(WlMessageContext var1) {
      MsgHeaders var2 = var1.getHeaders();
      ActionHeader var3 = (ActionHeader)var2.getHeader(ActionHeader.TYPE);
      if (var3 != null && var3.hasDuplicated()) {
         throw this.newCardinalityAddressingHeaderException(var1, "Action");
      } else {
         ToHeader var4 = (ToHeader)var2.getHeader(ToHeader.TYPE);
         if (var4 != null && var4.hasDuplicated()) {
            throw this.newCardinalityAddressingHeaderException(var1, "To");
         } else {
            MessageIdHeader var5 = (MessageIdHeader)var2.getHeader(MessageIdHeader.TYPE);
            if (var5 != null && var5.hasDuplicated()) {
               throw this.newCardinalityAddressingHeaderException(var1, "MessageID");
            } else {
               ReplyToHeader var6 = (ReplyToHeader)var2.getHeader(ReplyToHeader.TYPE);
               if (var6 != null && var6.hasDuplicated()) {
                  throw this.newCardinalityAddressingHeaderException(var1, "ReplyTo");
               } else {
                  FaultToHeader var7 = (FaultToHeader)var2.getHeader(FaultToHeader.TYPE);
                  if (var7 != null && var7.hasDuplicated()) {
                     var1.setProperty("weblogic.wsee.addressing.FaultTo", AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).createAnonymousEndpointReference());
                     throw this.newCardinalityAddressingHeaderException(var1, "FaultTo");
                  }
               }
            }
         }
      }
   }

   private AddressingHeaderException newCardinalityAddressingHeaderException(WlMessageContext var1, String var2) {
      AddressingProvider var3 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      return new AddressingHeaderException("A header representing a Message Addressing Property is not valid and the message cannot be processed!", var2, new QName[]{var3.getInvalidAddressingHeaderFaultQName(), WSAddressingConstants.WSA_INVALID_CARDINALITY});
   }

   private void setWSAVersion(WlMessageContext var1) {
      MsgHeaders var2 = var1.getHeaders();
      var1.removeProperty("weblogic.wsee.addressing.version");
      ActionHeader var3 = (ActionHeader)var2.getHeader(ActionHeader.TYPE);
      if (var3 != null) {
         if ("http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var3.getName().getNamespaceURI())) {
            var1.setProperty("weblogic.wsee.addressing.version", WSAVersion.MemberSubmission);
            return;
         }

         if ("http://www.w3.org/2005/08/addressing".equals(var3.getName().getNamespaceURI())) {
            return;
         }
      }

      ToHeader var4 = (ToHeader)var2.getHeader(ToHeader.TYPE);
      if (var4 != null && "http://schemas.xmlsoap.org/ws/2004/08/addressing".equals(var4.getName().getNamespaceURI())) {
         var1.setProperty("weblogic.wsee.addressing.version", WSAVersion.MemberSubmission);
      }
   }

   private void convertToWSAFault(AddressingHeaderException var1, WlMessageContext var2) {
      if (var1 != null) {
         boolean var3 = AsyncUtil.isSoap12(var2);
         WLSOAPFaultException var4;
         QName var5;
         if (var3) {
            var4 = SOAPFaultUtil.newWLSOAP12FaultException(var1.getFaultCode().getLocalPart(), var1.getFaultString(), (String)null, (Throwable)null, var1.getHeaderFaultCode());
            var5 = WSAVersion.MemberSubmission.equals(AddressingHelper.getWSAVersion((MessageContext)var2)) ? WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME : WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
            Detail var6 = SOAPFaultUtil.createDetail(var5, "wsa:" + var1.getFaultHeader(), var3);

            try {
               SOAPFaultUtil.fillSoapDetail(var6, var4.getFault());
            } catch (SOAPException var8) {
               Verbose.logException(var8);
            }

            var2.setFault(var4);
         } else {
            var5 = null;
            if (var1.getHeaderFaultCode() != null && var1.getHeaderFaultCode().length > 0) {
               var5 = var1.getHeaderFaultCode()[0];
            }

            var4 = SOAPFaultUtil.newWLSOAP11FaultException((QName)var5, var1.getFaultString(), (String)null, (Throwable)null);
            var2.setFault(var4);
            this.addFaultActionHeader(var2);
            FaultDetailHeader var9 = AddressingHelper.getAddressingProvider(var2).createFaultDetailHeader();
            var9.setProblemHeader(AddressingHelper.getAddressingProvider(var2).createProblemHeaderQNameHeader("wsa:" + var1.getFaultHeader()));
            var2.getHeaders().addHeader(var9);
         }

      }
   }

   private void addFaultActionHeader(WlMessageContext var1) {
      AddressingProvider var2 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      ActionHeader var3 = var2.createActionHeader(var2.getFaultActionUri());
      if (var1.getHeaders().getHeader(ActionHeader.TYPE) == null) {
         var1.getHeaders().addHeader(var3);
      }

   }
}
