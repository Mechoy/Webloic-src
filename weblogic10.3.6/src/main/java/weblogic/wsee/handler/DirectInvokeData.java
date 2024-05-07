package weblogic.wsee.handler;

import javax.xml.rpc.server.ServletEndpointContext;
import weblogic.wsee.jws.container.Request;

public class DirectInvokeData {
   private String convId;
   private Request req;
   private ServletEndpointContext context;

   public String getConversationId() {
      return this.convId;
   }

   public void setConversationId(String var1) {
      this.convId = var1;
   }

   public Request getRequest() {
      return this.req;
   }

   public void setRequest(Request var1) {
      this.req = var1;
   }

   public ServletEndpointContext getContext() {
      return this.context;
   }

   public void setContext(ServletEndpointContext var1) {
      this.context = var1;
   }
}
