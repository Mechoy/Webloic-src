package weblogic.wsee.addressing;

import java.util.Iterator;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.wsee.addressing.policy.api.UsingAddressingPolicyInfo;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.HeaderUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsa.wsaddressing.WSAVersion;
import weblogic.wsee.wsdl.WsdlUtils;
import weblogic.wsee.wsdl.soap11.SoapAddress;

public class ClientAddressingHandler extends AddressingHandler {
   private static final boolean verbose = Verbose.isVerbose(ClientAddressingHandler.class);
   public static final String COOKIES = "weblogic.wsee.addressing.Cookie";
   public static final String TARGET_REFERENCE = "weblogic.wsee.addressing.Target";
   public static final String RELATED_MESSAGE_ID = "weblogic.wsee.addressing.RelatedMessageId";
   public static final String HAS_WSA_EXCEPTION = "weblogic.wsee.addressing.client.hasexception";
   public static final String ACCEPT_EXTERNAL_SETTING_ONLY = "weblogic.wsee.addressing.client.AcceptExternalSettingOnly";
   private static boolean acceptExternalSettingOnly = false;

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         String var2 = (String)var1.getProperty("weblogic.wsee.complex");
         boolean var3 = this.detectUsingAddressingPolicy(var1);
         if ((var2 != null || var3) && !Boolean.FALSE.toString().equalsIgnoreCase(var2)) {
            if (WsrmConstants.RMVersion.RM_10 == var1.getProperty("weblogic.wsee.wsrm.RMVersion") && var1.getProperty("weblogic.wsee.addressing.version") == null && !var3) {
               var1.setProperty("weblogic.wsee.addressing.version", WSAVersion.MemberSubmission);
            }

            if (verbose) {
               Verbose.log((Object)"Adding addressing headers");
            }

            acceptExternalSettingOnly = var1.containsProperty("weblogic.wsee.addressing.client.AcceptExternalSettingOnly");
            WlMessageContext var4 = WlMessageContext.narrow(var1);
            AddressingProvider var5 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var4);
            String var6 = (String)var4.getProperty("weblogic.wsee.addressing.MessageId");
            if (acceptExternalSettingOnly) {
               if (var6 != null) {
                  this.addHeaderNoDups(var4, var5.createMessageIdHeader(var6));
               }
            } else {
               if (var6 == null) {
                  var6 = Guid.generateGuid();
               }

               this.addHeaderNoDups(var4, var5.createMessageIdHeader(var6));
            }

            String var7 = (String)var4.getProperty("weblogic.wsee.addressing.Action");
            if (acceptExternalSettingOnly) {
               if (var7 != null) {
                  this.addHeaderNoDups(var4, var5.createActionHeader(var7));
               }
            } else {
               if (var7 == null) {
                  var7 = this.getActionURI(var4, true);
               }

               if (var7 != null) {
                  String var8 = AddressingUtil.wrapSOAPAction(var7);
                  this.setSoapActionHeader(var4, var8);
               }

               this.addHeaderNoDups(var4, var5.createActionHeader(var7));
            }

            EndpointReference var15 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.Target");
            if (var15 != null) {
               var1.setProperty("javax.xml.rpc.service.endpoint.address", var15.getAddress());
               Iterator var9 = var15.getReferenceProperties().listHeaders();

               while(var9.hasNext()) {
                  this.addHeaderNoDups(var4, (MsgHeader)var9.next());
               }

               var9 = var15.getReferenceParameters().listHeaders();

               while(var9.hasNext()) {
                  MsgHeader var10 = (MsgHeader)var9.next();
                  var10.setRefParam(true);
                  this.addHeaderNoDups(var4, var10);
               }
            }

            String var16 = (String)var4.getProperty("weblogic.wsee.addressing.To");
            if (acceptExternalSettingOnly) {
               if (var16 != null) {
                  this.addHeaderNoDups(var4, var5.createToHeader(var16));
               }
            } else {
               if (var16 == null) {
                  var16 = this.getTargetAddress(var4);

                  assert var16 != null;
               }

               this.addHeaderNoDups(var4, var5.createToHeader(var16));
            }

            MsgHeaders var17 = (MsgHeaders)var1.getProperty("weblogic.wsee.addressing.Cookie");
            if (var17 != null) {
               Iterator var11 = var17.listHeaders();

               while(var11.hasNext()) {
                  this.addHeaderNoDups(var4, (MsgHeader)var11.next());
               }
            }

            EndpointReference var18 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.ReplyTo");
            if (var18 != null) {
               this.addHeaderNoDups(var4, var5.createReplyToHeader(var18));
            } else {
               this.addHeaderNoDups(var4, var5.createReplyToHeader(var5.createAnonymousEndpointReference()));
            }

            EndpointReference var12 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.FaultTo");
            if (var12 != null) {
               this.addHeaderNoDups(var4, var5.createFaultToHeader(var12));
            }

            EndpointReference var13 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.From");
            if (var13 != null && !StringUtil.isEmpty(var13.getAddress())) {
               FromHeader var14 = var5.createFromHeader(var13);
               this.addHeaderNoDups(var4, var14);
            }

            EndpointReference var19 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.CallbackTo");
            if (var19 != null) {
               this.addHeaderNoDups(var4, new CallbackToHeader(var19));
            }

            return true;
         } else {
            return true;
         }
      }
   }

   private void addHeaderNoDups(WlMessageContext var1, MsgHeader var2) {
      if (var1.getHeaders().getHeader(var2.getType()) != null) {
         SoapMsgHeaders var3 = (SoapMsgHeaders)var1.getHeaders();
         var3.replaceHeader(var2);
      } else {
         var1.getHeaders().addHeader(var2);
      }

   }

   public boolean handleResponse(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         SOAPMessageContext var3 = (SOAPMessageContext)var1;
         Map var4 = (Map)var2.getProperty("weblogic.wsee.invoke_properties");
         if (verbose) {
            Verbose.log((Object)"Looking for WS-Addressing headers");
         }

         assert var4 != null;

         SetCookieHeader var5 = (SetCookieHeader)var2.getHeaders().getHeader(SetCookieHeader.TYPE);
         if (var5 != null) {
            var4.put("weblogic.wsee.addressing.Cookie", var5.getCookies());
            if (verbose) {
               Verbose.log((Object)"Received cookies from server");
            }
         }

         ConversationPhase var6 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
         if (var6 == ConversationPhase.START) {
            this.handleConversationReplyTo(var2, var4);
         }

         this.discardAddressingHeaders(var2, var3);
         return true;
      }
   }

   private void handleConversationReplyTo(WlMessageContext var1, Map var2) {
      ReplyToHeader var3 = (ReplyToHeader)var1.getHeaders().getHeader(ReplyToHeader.TYPE);
      if (var3 != null) {
         EndpointReference var4 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.Target");
         EndpointReference var5 = var3.getReference();
         if (var4 != null) {
            if (!var4.getAddress().equals(var5.getAddress())) {
               throw new JAXRPCException("The address in the new conversational EPR does not match the existing address, new: " + var5.getAddress() + " old: " + var4.getAddress());
            }

            ((FreeStandingMsgHeaders)var4.getReferenceProperties()).merge((FreeStandingMsgHeaders)var5.getReferenceProperties());
            ((FreeStandingMsgHeaders)var4.getReferenceParameters()).merge((FreeStandingMsgHeaders)var5.getReferenceParameters());
            var2.put("weblogic.wsee.addressing.Target", var4);
         } else {
            var2.put("weblogic.wsee.addressing.Target", var5);
         }

         ContinueHeader var6 = (ContinueHeader)var5.getReferenceParameters().getHeader(ContinueHeader.TYPE);
         if (var6 == null) {
            var6 = (ContinueHeader)var5.getReferenceProperties().getHeader(ContinueHeader.TYPE);
         }

         if (var6 != null) {
            String var7 = var6.getConversationId();
            var2.put("weblogic.wsee.conversation.ConversationId", var7);
         }
      }

   }

   public boolean handleFault(MessageContext var1) {
      return this.handleResponse(var1);
   }

   private String getTargetAddress(WlMessageContext var1) {
      String var2 = (String)var1.getProperty("javax.xml.rpc.service.endpoint.address");
      if (var2 == null) {
         SoapAddress var3 = WsdlUtils.getSoapAddress(var1.getDispatcher().getWsdlPort());

         assert var3 != null;

         var2 = var3.getLocation();
      }

      return var2;
   }

   private void discardAddressingHeaders(WlMessageContext var1, SOAPMessageContext var2) {
      if (verbose) {
         Verbose.here();
      }

      ToHeader var3 = (ToHeader)var1.getHeaders().getHeader(ToHeader.TYPE);
      if (var3 != null) {
         if (verbose) {
            Verbose.log((Object)"Removing ToHeader mustUnderstand");
         }

         HeaderUtil.removeMustUnderstandFromHeader(var3, var2.getMessage());
      }

      RelatesToHeader var4 = (RelatesToHeader)var1.getHeaders().getHeader(RelatesToHeader.TYPE);
      if (var4 != null) {
         if (verbose) {
            Verbose.log((Object)"Removing RelatesToHeader mustUnderstand");
         }

         HeaderUtil.removeMustUnderstandFromHeader(var4, var2.getMessage());
      }

      ActionHeader var5 = (ActionHeader)var1.getHeaders().getHeader(ActionHeader.TYPE);
      if (var5 != null) {
         if (verbose) {
            Verbose.log((Object)"Removing ActionHeader mustUnderstand");
         }

         HeaderUtil.removeMustUnderstandFromHeader(var5, var2.getMessage());
      }

   }

   private boolean detectUsingAddressingPolicy(MessageContext var1) {
      UsingAddressingPolicyInfo var2 = this.getAddressingPolicyInfo(var1);
      if (var2 == null) {
         return false;
      } else if (var2.getUsingAddressingWSAVersionInfo() != null) {
         if (var1.getProperty("weblogic.wsee.addressing.version") == null) {
            var1.setProperty("weblogic.wsee.addressing.version", var2.getUsingAddressingWSAVersionInfo().getWSAVersion());
         }

         return true;
      } else {
         return false;
      }
   }
}
