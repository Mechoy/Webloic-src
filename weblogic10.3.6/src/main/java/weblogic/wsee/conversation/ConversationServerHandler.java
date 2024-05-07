package weblogic.wsee.conversation;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.protocol.LocalServerIdentity;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.PathServiceUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;
import weblogic.wsee.wsdl.WsdlBindingOperation;

public class ConversationServerHandler extends ConversationHandler implements WLHandler {
   private static final boolean verbose = Verbose.isVerbose(ConversationServerHandler.class);
   public static final String SERVER_ASSIGNED = "weblogic.wsee.conversation.IsServerAssigned";
   public static final String ASYNC_CONV_ID = "weblogic.wsee.conversation.AsyncConvId";
   public static final String CONV_ID_ACTION = "http://www.bea.com/wsee/ConversationId";
   public static final String CONVERSATION_ID_MSG = "weblogic.wsee.conversation.id.msg";

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ConversationPhase var3 = this.getConversationPhase(var2);
      if (var3 != null && var3 != ConversationPhase.NONE) {
         WsdlBindingOperation var5 = var2.getDispatcher().getBindingOperation();
         QName var4 = var5.getName();
         if (verbose) {
            Verbose.log((Object)("** Handling method " + var4 + " on conversation of phase: " + var3));
         }

         var1.setProperty("weblogic.wsee.conversation.ConversationPhase", var3);
         MsgHeaders var6 = var2.getHeaders();
         ContinueHeader var7;
         if (var3 == ConversationPhase.START) {
            var7 = (ContinueHeader)var6.getHeader(ContinueHeader.TYPE);
            if (var7 != null) {
               throw new JAXRPCException("Cannot invoke start method ('" + var4 + "') when conversation has already started.");
            }

            StartHeader var8 = (StartHeader)var6.getHeader(StartHeader.TYPE);
            if (verbose && var8 != null) {
               Verbose.log((Object)("Received StartHeader " + var8));
            }

            String var9;
            if (var8 != null && !StringUtil.isEmpty(var8.getConversationId())) {
               if (!Boolean.TRUE.equals(var1.getProperty("weblogic.wsee.handler.wlw81BufferCompatFlat")) || !Boolean.TRUE.equals(var1.getProperty("weblogic.wsee.conversation.IsServerAssigned"))) {
                  if (verbose) {
                     Verbose.log((Object)("Received 8.1 style: conversation id =  " + var8.getConversationId()));
                  }

                  try {
                     PathServiceUtil.saveConversationIdMapping(var8.getConversationId());
                  } catch (Throwable var10) {
                     new JAXRPCException("Failed to access routing info from path service for method " + var4 + " on conversation " + var8.getConversationId(), var10);
                  }

                  var1.setProperty("weblogic.wsee.conversation.ConversationId", var8.getConversationId());
               }
            } else {
               var9 = Guid.generateGuid();
               if (verbose) {
                  Verbose.log((Object)("Conversation server assigning conversation ID: " + var9));
               }

               if (var2.getDispatcher().getOperation().getType() != 0 || !"true".equals(var1.getProperty("weblogic.wsee.reply.anonymous"))) {
                  var1.setProperty("weblogic.wsee.conversation.AsyncConvId", "true");
               }

               var1.setProperty("weblogic.wsee.conversation.IsServerAssigned", Boolean.TRUE);
               var1.setProperty("weblogic.wsee.conversation.ConversationId", var9);
            }

            var9 = ApplicationVersionUtils.getCurrentVersionId();
            if (var9 != null) {
               var1.setProperty("weblogic.wsee.version.appversion.id", var9);
            }
         } else {
            var7 = (ContinueHeader)var6.getHeader(ContinueHeader.TYPE);
            if (var7 == null) {
               throw new JAXRPCException("Incoming message for a " + var3 + " operation ('" + var4 + "') does not contain a ContinueHeader");
            }

            if (verbose) {
               Verbose.log((Object)("Received ContinueHeader for conversation " + var7.getConversationId() + " hosted by server " + var7.getServerName() + ": " + var7.toString()));
            }

            String var11 = var7.getAppVersionId();
            if (var11 != null) {
               var1.setProperty("weblogic.wsee.version.appversion.id", var11);
            }

            var1.setProperty("weblogic.wsee.conversation.ConversationId", var7.getConversationId());
            var1.setProperty("weblogic.wsee.conversation.IsServerAssigned", new Boolean(var7.isServerAssigned()));
         }

         if (verbose) {
            Verbose.log((Object)("Leaving conversation server handleRequest with conversation ID: " + var1.getProperty("weblogic.wsee.conversation.ConversationId")));
         }

         return true;
      } else {
         return true;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ConversationPhase var3 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
      if (var3 != null && var3 != ConversationPhase.NONE && var3 != ConversationPhase.CONTINUE) {
         Container var4 = AsyncUtil.getContainer(var1);

         assert var4 != null;

         if (var3 == ConversationPhase.START && !var4.isFinished()) {
            this.addContinueHeader(var2);
         }

         return true;
      } else {
         return true;
      }
   }

   protected void addContinueHeader(WlMessageContext var1) {
      ReplyToHeader var2 = (ReplyToHeader)var1.getHeaders().getHeader(ReplyToHeader.TYPE);
      AddressingProvider var3 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1);
      String var4 = (String)var1.getProperty("weblogic.wsee.addressing.To");
      if (var2 == null) {
         EndpointReference var5 = var3.createEndpointReference(var4);
         var2 = var3.createReplyToHeader(var5);
      } else if (var3.isAnonymousReferenceURI(var2.getReference().getAddress())) {
         var2.getReference().setAddress(var4);
      }

      if (var2.getReference().getReferenceParameters().getHeader(ServiceIdentityHeader.TYPE) == null) {
         ServiceIdentityHeader var9 = new ServiceIdentityHeader();
         var9.setServerName(LocalServerIdentity.getIdentity().getServerName());
         var9.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
         var2.getReference().getReferenceParameters().addHeader(var9);
      }

      Boolean var10 = (Boolean)var1.getProperty("weblogic.wsee.conversation.IsServerAssigned");
      if (var10 != null && var10 && var2.getReference().getReferenceParameters().getHeader(ContinueHeader.TYPE) == null) {
         String var6 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
         String var7 = ApplicationVersionUtils.getCurrentVersionId();
         if (verbose) {
            Verbose.log((Object)("Adding conv version " + var7));
         }

         ContinueHeader var8 = new ContinueHeader(var6, LocalServerIdentity.getIdentity().getServerName(), var7);
         var2.getReference().getReferenceParameters().addHeader(var8);
      }

      ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(var2);
   }

   public boolean handleClosure(MessageContext var1) {
      if (!var1.containsProperty("weblogic.wsee.conversation.AsyncConvId")) {
         return true;
      } else if (((WlMessageContext)var1).hasFault()) {
         return true;
      } else {
         this.constructMessage(var1);
         return true;
      }
   }

   private void constructMessage(MessageContext var1) {
      SOAPMessageContext var2 = (SOAPMessageContext)var1;
      WlMessageContext var3 = WlMessageContext.narrow(var1);

      try {
         MessageFactory var4 = WLMessageFactory.getInstance().getMessageFactory(((SoapMessageContext)var3).isSoap12());
         SOAPMessage var5 = var4.createMessage();
         var2.setMessage(var5);
      } catch (SOAPException var14) {
         throw new JAXRPCException(var14);
      }

      EndpointReference var15 = (EndpointReference)var1.getProperty("weblogic.wsee.addressing.ReplyTo");
      String var16 = (String)var1.getProperty("weblogic.wsee.addressing.To");
      String var6 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
      AddressingProvider var7 = AddressingProviderFactory.getInstance().getAddressingProvider(var1);
      EndpointReference var8 = var7.createEndpointReference();
      FreeStandingMsgHeaders var9 = new FreeStandingMsgHeaders();
      String var10 = ApplicationVersionUtils.getCurrentVersionId();
      if (verbose) {
         Verbose.log((Object)("Adding conv version " + var10));
      }

      ContinueHeader var11 = new ContinueHeader(var6, LocalServerIdentity.getIdentity().getServerName(), var10);
      ServiceIdentityHeader var12 = new ServiceIdentityHeader();
      var12.setServerName(LocalServerIdentity.getIdentity().getServerName());
      var12.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
      var9.addHeader(var11);
      var9.addHeader(var12);
      var8.setAddress(var16);
      var8.setReferenceParameters(var9);
      var1.setProperty("javax.xml.rpc.service.endpoint.address", var15.getAddress());
      Iterator var13 = var15.getReferenceProperties().listHeaders();

      while(var13.hasNext()) {
         var3.getHeaders().addHeader((MsgHeader)var13.next());
      }

      var13 = var15.getReferenceParameters().listHeaders();

      while(var13.hasNext()) {
         var3.getHeaders().addHeader((MsgHeader)var13.next());
      }

      var3.getHeaders().addHeader(var7.createToHeader(var15.getAddress()));
      var3.getHeaders().addHeader(var7.createReplyToHeader(var8));
      var3.getHeaders().addHeader(var7.createActionHeader("http://www.bea.com/wsee/ConversationId"));
      ((SOAPMessageContext)var3).getMessage().getMimeHeaders().setHeader("SOAPAction", "http://www.bea.com/wsee/ConversationId");
      var3.getHeaders().addHeader(var7.createMessageIdHeader(Guid.generateGuid()));
      var3.setProperty("weblogic.wsee.conversation.id.msg", "true");
   }
}
