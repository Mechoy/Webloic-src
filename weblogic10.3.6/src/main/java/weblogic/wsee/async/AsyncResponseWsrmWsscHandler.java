package weblogic.wsee.async;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.RelatesToHeader;
import weblogic.wsee.cluster.CorrelationHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSequenceContext;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.security.wssp.handlers.WssServerHandler;
import weblogic.wsee.server.WsLifeCycleEvent;
import weblogic.wsee.server.WsLifeCycleListenerRegistry;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;

public class AsyncResponseWsrmWsscHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(AsyncResponseWsrmWsscHandler.class);
   public static final String RM_SECURE = "weblogic.wsee.rm.secure";

   public QName[] getHeaders() {
      return null;
   }

   public boolean handleRequest(MessageContext var1) {
      if (var1 == null) {
         return true;
      } else if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         MsgHeaders var3 = var2.getHeaders();
         if (var3 == null) {
            return true;
         } else {
            if (verbose) {
               Verbose.log((Object)"AsyncResponseWsrmWsscHandler.handleRequest");
            }

            if (this.isRMActions(var3) && this.handleRequestSecurity(var1)) {
               return true;
            } else {
               SequenceHeader var4 = (SequenceHeader)var3.getHeader(SequenceHeader.TYPE);
               if (var4 == null) {
                  return true;
               } else {
                  WsrmSequenceContext var5 = WsrmServerHandshakeHandler.getRMSequenceContext(false, var4.getSequenceId());

                  assert var5 != null;

                  if (!var5.getWsrmSecurityContext().isSecureWithWssc()) {
                     return true;
                  } else if (this.handleAsyncResponseSecurity(var3, var1, var5)) {
                     return true;
                  } else if (this.handleConvIdSecurity(var3, var1, var5)) {
                     return true;
                  } else {
                     WsLifeCycleListenerRegistry.getInstance().onEvent(WsLifeCycleEvent.WSRM_SEND_AFTER_RES_SEC_VALIDATION);
                     return true;
                  }
               }
            }
         }
      }
   }

   private boolean handleConvIdSecurity(MsgHeaders var1, MessageContext var2, WsrmSequenceContext var3) {
      CorrelationHeader var4 = (CorrelationHeader)var1.getHeader(CorrelationHeader.TYPE);
      if (var4 == null) {
         return false;
      } else {
         Throwable var5;
         if (!AsyncUtil.getWssServerHandler(var3).handleRequest(var2) && (var5 = WlMessageContext.narrow(var2).getFault()) != null) {
            throw new JAXRPCException(var5);
         } else {
            return true;
         }
      }
   }

   private boolean handleRequestSecurity(MessageContext var1) throws JAXRPCException {
      if (!(new WssServerHandler()).handleRequest(var1)) {
         Throwable var2 = WlMessageContext.narrow(var1).getFault();
         if (var2 != null) {
            throw new JAXRPCException(var2);
         }
      }

      var1.setProperty("weblogic.wsee.rm.secure", "true");
      return true;
   }

   private boolean handleAsyncResponseSecurity(MsgHeaders var1, MessageContext var2, WsrmSequenceContext var3) {
      RelatesToHeader var4 = (RelatesToHeader)var1.getHeader(RelatesToHeader.TYPE);
      if (var4 == null) {
         return false;
      } else {
         String var5 = var4.getRelatedMessageId();
         WsStorage var6 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());
         AsyncInvokeState var7 = this.getAsyncInvokeState(var6, var5);
         synchronized(var7) {
            try {
               this.runSecurityHandlers(var7, var2, var3);
            } catch (JAXRPCException var12) {
               var6.remove(var5);
               throw var12;
            } catch (UndeclaredThrowableException var13) {
               var6.remove(var5);
               throw var13;
            }

            try {
               var6.persistentPut(var5, var7);
            } catch (PersistentStoreException var11) {
               var6.remove(var5);
               throw new JAXRPCException(var11);
            }

            var2.setProperty("weblogic.wsee.rm.secure", "true");
            var2.setProperty("weblogic.wsee.wssc.sct", var7.getMessageContext().getProperty("weblogic.wsee.wssc.sct"));
            return true;
         }
      }
   }

   private void runSecurityHandlers(AsyncInvokeState var1, MessageContext var2, WsrmSequenceContext var3) {
      AuthenticatedSubject var4 = var1.getSubject();
      AuthorizedInvoke var5 = new AuthorizedInvoke(var1, var2, var3);
      if (var4 != null) {
         try {
            SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var4, var5);
         } catch (PrivilegedActionException var8) {
            if (var8.getException() instanceof IOException) {
               throw new JAXRPCException(var8.getException());
            }

            throw new UndeclaredThrowableException(var8.getException());
         }
      } else {
         try {
            var5.run();
         } catch (Exception var7) {
            throw new JAXRPCException(var7);
         }
      }

   }

   private boolean isSecure(AsyncInvokeState var1) {
      WlMessageContext var2 = var1.getMessageContext();
      return var2.containsProperty("weblogic.xml.crypto.wss.WSSecurityContext");
   }

   private AsyncInvokeState getAsyncInvokeState(WsStorage var1, String var2) {
      try {
         AsyncInvokeState var3 = (AsyncInvokeState)var1.persistentGet(var2);
         if (var3 == null) {
            throw new JAXRPCException("Cannot retrieve request information for message " + var2);
         } else {
            return var3;
         }
      } catch (PersistentStoreException var4) {
         if (verbose) {
            Verbose.logException(var4);
         }

         throw new JAXRPCException(var4);
      }
   }

   private boolean isRMActions(MsgHeaders var1) {
      ActionHeader var2 = (ActionHeader)var1.getHeader(ActionHeader.TYPE);
      if (var2 == null) {
         return false;
      } else {
         String var3 = var2.getActionURI();
         return var3.startsWith(WsrmConstants.RMVersion.RM_10.getNamespaceUri()) || var3.startsWith(WsrmConstants.RMVersion.RM_11.getNamespaceUri());
      }
   }

   private class AuthorizedInvoke implements PrivilegedExceptionAction {
      private AsyncInvokeState ais = null;
      private MessageContext mc;
      private WsrmSequenceContext seqCtx;

      AuthorizedInvoke(AsyncInvokeState var2, MessageContext var3, WsrmSequenceContext var4) {
         this.ais = var2;
         this.mc = var3;
         this.seqCtx = var4;
      }

      public Object run() throws Exception {
         SoapMessageContext var1 = (SoapMessageContext)this.mc;
         WlMessageContext var2 = this.ais.getMessageContext();
         if (var2 == null) {
            throw new JAXRPCException("No message context saved");
         } else {
            ((SoapMessageContext)var2).setMessage(var1.getMessage());
            Throwable var3 = null;
            if (!AsyncUtil.getWssClientHandler(this.seqCtx).handleResponse(var2) && (var3 = WlMessageContext.narrow(var2).getFault()) != null) {
               throw new JAXRPCException(var3);
            } else {
               return null;
            }
         }
      }
   }
}
