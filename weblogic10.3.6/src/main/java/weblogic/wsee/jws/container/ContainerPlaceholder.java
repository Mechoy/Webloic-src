package weblogic.wsee.jws.container;

import java.io.Serializable;
import javax.xml.rpc.server.ServiceLifecycle;
import weblogic.wsee.message.WlMessageContext;

public class ContainerPlaceholder implements ContainerMarker, Serializable {
   private WlMessageContext messageContext;
   private Object target;
   private String id;

   public ContainerPlaceholder(Object var1) {
      this.target = var1;
   }

   public ContainerPlaceholder(Object var1, WlMessageContext var2) {
      this.messageContext = var2;
      this.target = var1;
   }

   public ContainerPlaceholder(Object var1, WlMessageContext var2, String var3) {
      this.messageContext = var2;
      this.target = var1;
      this.id = var3;
   }

   public WlMessageContext getUnfilteredMessageContext() {
      return this.messageContext;
   }

   public void setMessageContext(WlMessageContext var1) {
      this.messageContext = var1;
      var1.setProperty("weblogic.wsee.jws.container", this);
   }

   public Object getTargetJWS() {
      return this.target;
   }

   public String getId() {
      return this.id;
   }

   public void destroy() {
      if (ServiceLifecycle.class.isInstance(this.target)) {
         ServiceLifecycle var1 = (ServiceLifecycle)this.target;
         var1.destroy();
      }

   }
}
