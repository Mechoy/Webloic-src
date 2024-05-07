package weblogic.wsee.callback;

import java.security.AccessController;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.connection.transport.Transport;
import weblogic.wsee.connection.transport.servlet.HttpServerTransport;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.WLMessageFactory;

public class CallbackServiceHandler extends CallbackHandler {
   private static final boolean verbose = Verbose.isVerbose(CallbackServiceHandler.class);
   public static final String SECURITY_REALM = "__SECURITY_REALM__";
   private String securityRealm = null;

   public void init(HandlerInfo var1) {
      Map var2 = var1.getHandlerConfig();
      this.securityRealm = (String)var2.get("__SECURITY_REALM__");
   }

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         SoapMessageContext var3 = (SoapMessageContext)var2;
         MsgHeaders var4 = var2.getHeaders();
         CallbackInfoHeader var5 = (CallbackInfoHeader)var4.getHeader(CallbackInfoHeader.TYPE);
         if (var5 != null) {
            if (verbose) {
               Verbose.log((Object)"Callback message received");
            }

            String var6;
            if ((var6 = var5.getAppVersion()) != null) {
               if (verbose) {
                  Verbose.log((Object)("Setting version to send callback to " + var6));
               }

               var2.setProperty("weblogic.wsee.callback.appversion", var6);
            } else if (verbose) {
               Verbose.log((Object)"No app version in callback info header");
            }

            if (var5.isRoleRequired()) {
               if (verbose) {
                  Verbose.log((Object)"Callback requires roles");
               }

               AuthenticatedSubject var7 = ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()));
               if (SubjectUtils.isUserAnonymous(var7)) {
                  if (verbose) {
                     Verbose.log((Object)"Anonymous user, will request authentication information");
                  }

                  this.setAuthFault(var3, "Role information required for callback.");
               }
            }
         }

         return true;
      }
   }

   private void setAuthFault(SoapMessageContext var1, String var2) {
      Transport var3 = var1.getDispatcher().getConnection().getTransport();
      if (var3 instanceof HttpServerTransport) {
         HttpServletResponse var4 = ((HttpServerTransport)var3).getResponse();
         var4.setHeader("WWW-Authenticate", "Basic realm=\"" + this.securityRealm + "\"");
      }

      try {
         var1.setProperty("weblogic.wsee.AuthRequired", "true");
         SOAPMessage var7 = WLMessageFactory.getInstance().getMessageFactory(var1.isSoap12()).createMessage();
         SOAPFault var5 = var7.getSOAPPart().getEnvelope().getBody().addFault();
         var5.setFaultCode("env:Client.Authentication");
         var5.setFaultString(var2);
         var1.setMessage(var7);
      } catch (SOAPException var6) {
         throw new JAXRPCException("Unable to send error", var6);
      }
   }

   public boolean handleResponse(MessageContext var1) {
      return true;
   }
}
