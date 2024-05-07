package weblogic.wsee.security;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jaxrpc.soapfault.SOAPFaultUtil;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMessageContext;
import weblogic.wsee.util.AccessException;
import weblogic.wsee.ws.WsMethod;

public final class AuthorizationHandler implements WLHandler {
   private static final QName AUTHENTICATION_FAILURE_11 = new QName("http://schemas.xmlsoap.org/soap/envelope/", "Client.Authentication", "env");
   private static final QName AUTHENTICATION_FAILURE_12 = new QName("http://www.w3.org/2003/05/soap-envelope", "Client.Authentication", "env");
   private String actorOrRole = null;
   private WLAuthorizer authorizer;

   public void init(HandlerInfo var1) {
   }

   public void destroy() {
   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      if (this.authorizer == null) {
         this.authorizer = new WLAuthorizer(new AuthorizationContext((String)var1.getProperty("weblogic.wsee.application_id"), (String)var1.getProperty("weblogic.wsee.context_path"), (String)var1.getProperty("weblogic.wsee.security_realm")));
      }

      WlMessageContext var2 = (WlMessageContext)var1;
      if (!this.authorizer.isAccessAllowed(var2)) {
         WsMethod var3 = var2.getDispatcher().getWsMethod();
         String var4 = "Access Denied to operation " + var3.getMethodName();
         throw new SOAPFaultException(this.getAuthenticationFailure(((SoapMessageContext)var2).isSoap12()), var4, this.actorOrRole, SOAPFaultUtil.newDetail(new AccessException(var4), ((SoapMessageContext)var2).isSoap12()));
      } else {
         return true;
      }
   }

   private QName getAuthenticationFailure(boolean var1) {
      return var1 ? AUTHENTICATION_FAILURE_12 : AUTHENTICATION_FAILURE_11;
   }

   public boolean handleResponse(MessageContext var1) {
      return true;
   }

   public boolean handleFault(MessageContext var1) {
      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      return true;
   }
}
