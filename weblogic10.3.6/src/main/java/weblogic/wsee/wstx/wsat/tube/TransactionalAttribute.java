package weblogic.wsee.wstx.wsat.tube;

import com.sun.xml.ws.api.SOAPVersion;
import weblogic.wsee.wstx.wsat.Transactional;

public class TransactionalAttribute {
   private boolean enabled;
   private boolean required;
   private Transactional.Version version;
   private SOAPVersion soapVersion;

   public TransactionalAttribute(boolean var1, boolean var2, Transactional.Version var3) {
      this.enabled = var1;
      this.required = var2;
      this.version = var3;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public boolean isRequired() {
      return this.required;
   }

   public void setRequired(boolean var1) {
      this.required = var1;
   }

   public Transactional.Version getVersion() {
      return this.version;
   }

   public void setVersion(Transactional.Version var1) {
      this.version = var1;
   }

   public SOAPVersion getSoapVersion() {
      if (this.soapVersion == null) {
         this.soapVersion = SOAPVersion.SOAP_11;
      }

      return this.soapVersion;
   }

   public void setSoapVersion(SOAPVersion var1) {
      this.soapVersion = var1;
   }
}
