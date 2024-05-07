package weblogic.webservice.encoding;

/** @deprecated */
public class StringAttachmentCodec extends AttachmentCodec {
   protected String getContentType() {
      return "text/plain";
   }

   protected Object serializeContent(Object var1) {
      return (String)var1;
   }

   protected Object deserializeContent(Object var1) {
      return (String)var1;
   }
}
