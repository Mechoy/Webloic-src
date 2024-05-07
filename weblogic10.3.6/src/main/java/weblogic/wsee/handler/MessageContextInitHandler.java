package weblogic.wsee.handler;

import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.MessageContext;

public class MessageContextInitHandler extends GenericHandler {
   private String contextPath;
   private String securityRealm;
   private String applicationId;
   private String serviceName;
   private Boolean streamAttachments;
   private Boolean validateRequest;

   public MessageContextInitHandler() {
      this.streamAttachments = Boolean.FALSE;
      this.validateRequest = Boolean.FALSE;
   }

   public void init(HandlerInfo var1) {
      Map var2 = var1.getHandlerConfig();
      if (var2 != null) {
         this.contextPath = (String)var2.get("weblogic.wsee.context_path");
         this.securityRealm = (String)var2.get("weblogic.wsee.security_realm");
         this.applicationId = (String)var2.get("weblogic.wsee.application_id");
         this.serviceName = (String)var2.get("weblogic.wsee.service_name");
         this.streamAttachments = (Boolean)var2.get("weblogic.wsee.stream_attachments");
         this.validateRequest = (Boolean)var2.get("weblogic.wsee.validate_request");
      }

   }

   public QName[] getHeaders() {
      return new QName[0];
   }

   public boolean handleRequest(MessageContext var1) {
      if (this.contextPath != null) {
         var1.setProperty("weblogic.wsee.context_path", this.contextPath);
      }

      if (this.securityRealm != null) {
         var1.setProperty("weblogic.wsee.security_realm", this.securityRealm);
      }

      if (this.applicationId != null) {
         var1.setProperty("weblogic.wsee.application_id", this.applicationId);
      }

      if (this.serviceName != null) {
         var1.setProperty("weblogic.wsee.service_name", this.serviceName);
      }

      var1.setProperty("weblogic.wsee.stream_attachments", this.streamAttachments);
      var1.setProperty("weblogic.wsee.soap.validating_decoder", this.validateRequest);
      return true;
   }
}
