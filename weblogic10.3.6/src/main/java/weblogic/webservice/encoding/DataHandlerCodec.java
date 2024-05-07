package weblogic.webservice.encoding;

import javax.activation.DataHandler;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import weblogic.xml.schema.binding.DeserializationContext;
import weblogic.xml.schema.binding.SerializationContext;
import weblogic.xml.stream.XMLName;

/** @deprecated */
public class DataHandlerCodec extends AttachmentCodec {
   protected String getContentType() {
      throw new Error("should not be called");
   }

   protected Object serializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserializeContent(Object var1) {
      throw new Error("should not be called");
   }

   protected Object deserialize(XMLName var1, SOAPMessage var2, DeserializationContext var3) throws JAXRPCException {
      AttachmentPart var4 = this.getAttachmentPart(var1, var2, var3);
      if (var4 == null) {
         return null;
      } else {
         try {
            DataHandler var5 = var4.getDataHandler();
            return var5;
         } catch (SOAPException var6) {
            throw new JAXRPCException("unable to deserialize", var6);
         }
      }
   }

   protected void serialize(Object var1, XMLName var2, SOAPMessage var3, SerializationContext var4) throws SOAPFaultException {
      try {
         this.addBodyElement(var2, var3);
      } catch (SOAPException var7) {
         throw new JAXRPCException("failed to serialize the attachment " + var2, var7);
      }

      if (var1 != null) {
         DataHandler var5 = (DataHandler)var1;
         AttachmentPart var6 = var3.createAttachmentPart();
         var6.setDataHandler(var5);
         var6.setContentId("<" + var2.getLocalName() + ">");
         var6.addMimeHeader("Content-Type", var5.getContentType());
         var3.addAttachmentPart(var6);
      }
   }
}
