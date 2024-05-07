package weblogic.wsee.handler;

import java.util.HashMap;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPConstants;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.conversation.ConversationCMPHandler;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.conversation.LockManager;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerDispatcher;
import weblogic.wsee.jws.container.ContainerDispatcherImpl;
import weblogic.wsee.jws.container.Request;
import weblogic.wsee.jws.container.Response;
import weblogic.wsee.jws.conversation.ConversationState;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.security.AuthorizationContext;
import weblogic.wsee.security.DirectInvokeAuthorizer;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.util.Verbose;

public class DirectInvokeHandler extends ConversationCMPHandler implements WLHandler, SOAPConstants {
   private static final boolean verbose = Verbose.isVerbose(DirectInvokeHandler.class);
   public static final String DIRECT_INVOKE_DATA_PROP = "weblogic.wsee.direct.invoke.data.prop";
   public static final String DIRECT_INVOKE_RESPONSE_PROP = "weblogic.wsee.direct.invoke.response.prop";
   public static final String SECURITY_REALM = "__SECURITY_REALM__";
   public static final String CONTEXT_PATH = "__CONTEXT_PATH__";
   public static final String APPLICATION_ID = "__APPLICATION_ID__";
   private static final QName AUTHENTICATION_FAILURE = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client.Authentication", "env");
   private String actorOrRole = null;
   private DirectInvokeAuthorizer authorizer;

   public void init(HandlerInfo var1) {
   }

   public boolean handleRequest(MessageContext var1) {
      if (this.authorizer == null) {
         this.authorizer = new DirectInvokeAuthorizer(new AuthorizationContext((String)var1.getProperty("weblogic.wsee.application_id"), (String)var1.getProperty("weblogic.wsee.context_path"), (String)var1.getProperty("weblogic.wsee.security_realm")));
      }

      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Object var3 = var2.getProperty("weblogic.wsee.direct.invoke.data.prop");
      if (var3 != null && var3 instanceof DirectInvokeData) {
         try {
            this.checkRole(var2);
            DirectInvokeData var4 = (DirectInvokeData)var3;
            String var5 = var4.getConversationId();
            if (var5 != null) {
               this.handleConversationalDirectInvoke(var2, var5);
            }

            this.handleDirectInvoke(var2, var4);
         } catch (Throwable var6) {
            if (verbose) {
               Verbose.logException(var6);
            }

            var2.setProperty("weblogic.wsee.local.invoke.throwable", var6);
         }

         return false;
      } else {
         return true;
      }
   }

   private void handleConversationalDirectInvoke(WlMessageContext var1, String var2) {
      try {
         LockManager.Lock var3 = LockManager.getInstance().lock(var2);
         var1.setProperty("weblogic.wsee.conversation.ConversationId", var2);
         var1.setProperty("weblogic.wsee.conversation.Lock", var3);
      } catch (InterruptedException var5) {
         throw new JAXRPCException(var5);
      }

      Store var6 = this.getStore(var2, var1);
      ConversationState var4 = loadState(var1, var6, var2);
      var1.setProperty("weblogic.wsee.jws.container", var4);
      var1.setProperty("weblogic.wsee.ejb.altRunAs", var4.getAltAuthenticatedSubject());
   }

   private void handleDirectInvoke(WlMessageContext var1, DirectInvokeData var2) throws Exception {
      Request var3 = var2.getRequest();
      ContainerDispatcher var4 = ContainerDispatcher.getInstance();
      Response var5 = ((ContainerDispatcherImpl)var4).dispatch(var1, var3, var2.getContext());
      HashMap var6 = new HashMap();
      var6.put("weblogic.wsee.direct.invoke.response.prop", var5.retval);
      var1.setProperty("weblogic.wsee.local.invoke.response", var6);
   }

   public boolean handleResponse(MessageContext var1) {
      return this.finish(var1);
   }

   public boolean handleClosure(MessageContext var1) {
      return this.finish(var1);
   }

   public boolean handleFault(MessageContext var1) {
      return this.finish(var1);
   }

   private boolean finish(MessageContext var1) {
      Object var2 = var1.getProperty("weblogic.wsee.direct.invoke.data.prop");
      if (var2 != null && var2 instanceof DirectInvokeData) {
         String var3 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
         if (var3 == null) {
            return true;
         } else {
            ConversationPhase var4 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
            Container var5 = AsyncUtil.getContainer(var1);
            if (ConversationPhase.FINISH.equals(var4) || var5 == null || var5.isFinished()) {
               LockManager.getInstance().destroy(var3);
            }

            LockManager.Lock var6 = (LockManager.Lock)var1.getProperty("weblogic.wsee.conversation.Lock");
            if (var6 != null) {
               var6.release();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   private void checkRole(WlMessageContext var1) {
      if (this.authorizer == null) {
         throw new RuntimeException("Authorization did not initialize properly");
      } else if (!this.authorizer.isAccessAllowed(var1)) {
         throw new SOAPFaultException(AUTHENTICATION_FAILURE, "Access Denied to callback method.", this.actorOrRole, SOAPFaultUtil.newDetail(new AccessException("Access Denied to callback method."), false));
      }
   }
}
