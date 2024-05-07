package weblogic.wsee.context;

import javax.xml.rpc.handler.soap.SOAPMessageContext;

public class WebServiceContextImpl extends WebServiceContext {
   private WebServiceHeader header;
   private WebServiceSession session;
   private SOAPMessageContext messageContext;

   public WebServiceHeader getHeader() {
      if (this.header == null) {
         this.header = new WebServiceHeaderImpl();
      }

      return this.header;
   }

   public WebServiceSession getSession() {
      if (this.session == null) {
         this.session = new WebServiceSessionImpl();
      }

      return this.session;
   }

   public void setSession(WebServiceSession var1) {
      if (this.session != null) {
         throw new IllegalStateException("session allready set");
      } else {
         this.session = var1;
      }
   }

   public SOAPMessageContext getLastMessageContext() {
      return this.messageContext;
   }

   public void setLastMessageContext(SOAPMessageContext var1) {
      this.messageContext = var1;
   }
}
