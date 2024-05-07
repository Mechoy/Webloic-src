package weblogic.wsee.ws.dispatch.server;

import java.security.Principal;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.server.ServletEndpointContext;
import weblogic.wsee.connection.transport.servlet.HttpTransportUtils;
import weblogic.wsee.jws.context.WebSecurityContext;
import weblogic.wsee.message.WlMessageContext;

public class ServletEndpointContextImpl implements ServletEndpointContext {
   private static ThreadLocal msgCtx = new ThreadLocal();
   private ServletContext servletContext;
   private WebSecurityContext securityCtx;

   public static MessageContext getMessageContextStatic() {
      return (MessageContext)msgCtx.get();
   }

   public ServletEndpointContextImpl(ServletContext var1, Class var2) {
      this.servletContext = var1;
      this.securityCtx = new WebSecurityContext((WlMessageContext)null, var2);
   }

   public void setMessageContext(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      msgCtx.set(var2);
      this.securityCtx.setMessageContext(var2);
   }

   public void unSetMessageContext() {
      msgCtx.set((Object)null);
   }

   public MessageContext getMessageContext() {
      return (MessageContext)msgCtx.get();
   }

   public void setSecurityContext(WebSecurityContext var1) {
      this.securityCtx = var1;
   }

   public WebSecurityContext getSecurityContext() {
      return this.securityCtx;
   }

   public Principal getUserPrincipal() {
      return this.securityCtx.getCallerPrincipal();
   }

   public HttpSession getHttpSession() {
      HttpServletRequest var1 = HttpTransportUtils.getHttpServletRequest(this.getMessageContext());
      return var1 != null ? var1.getSession() : null;
   }

   public ServletContext getServletContext() {
      return this.servletContext;
   }

   public boolean isUserInRole(String var1) {
      return this.securityCtx.isCallerInRole(var1);
   }
}
