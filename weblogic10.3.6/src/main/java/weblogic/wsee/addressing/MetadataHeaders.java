package weblogic.wsee.addressing;

import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.UnknownMsgHeader;

public class MetadataHeaders extends FreeStandingMsgHeaders {
   protected InterfaceNameHeader interfaceNameHeader;
   protected ServiceNameHeader serviceNameHeader;
   protected EmbeddedWsdlHeader embeddedWSDL;

   public MetadataHeaders() {
   }

   public MetadataHeaders(InterfaceNameHeader var1, ServiceNameHeader var2) {
      if (var1 != null) {
         this.setInterfaceNameHeader(var1);
      }

      if (var2 != null) {
         this.setServiceNameHeader(var2);
      }

   }

   public MetadataHeaders(EmbeddedWsdlHeader var1) {
      if (var1 != null) {
         this.setEmbeddedWSDL(var1);
      }

   }

   public InterfaceNameHeader getInterfaceNameHeader() {
      return this.interfaceNameHeader;
   }

   private void setInterfaceNameHeader(InterfaceNameHeader var1) {
      this.interfaceNameHeader = var1;
      this.addHeader(var1);
   }

   public ServiceNameHeader getServiceNameHeader() {
      return this.serviceNameHeader;
   }

   private void setServiceNameHeader(ServiceNameHeader var1) {
      this.serviceNameHeader = var1;
      this.addHeader(var1);
   }

   public UnknownMsgHeader getEmbeddedWSDL() {
      return this.embeddedWSDL;
   }

   private void setEmbeddedWSDL(EmbeddedWsdlHeader var1) {
      this.embeddedWSDL = var1;
      this.addHeader(var1);
   }

   public void addHeader(MsgHeader var1) throws MsgHeaderException {
      super.addHeader(var1);
      if (var1 instanceof InterfaceNameHeader) {
         this.interfaceNameHeader = (InterfaceNameHeader)var1;
      } else if (var1 instanceof ServiceNameHeader) {
         this.serviceNameHeader = (ServiceNameHeader)var1;
      } else if (var1 instanceof EmbeddedWsdlHeader) {
         this.embeddedWSDL = (EmbeddedWsdlHeader)var1;
      }

   }
}
