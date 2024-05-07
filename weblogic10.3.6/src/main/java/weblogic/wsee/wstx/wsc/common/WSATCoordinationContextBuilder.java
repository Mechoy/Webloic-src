package weblogic.wsee.wstx.wsc.common;

import com.sun.xml.ws.api.SOAPVersion;
import weblogic.wsee.wstx.wsat.Transactional;
import weblogic.wsee.wstx.wsat.Transactional.Version;
import weblogic.wsee.wstx.wsc.common.types.CoordinationContextIF;
import weblogic.wsee.wstx.wsc.v10.WSATCoordinationContextBuilderImpl;

public abstract class WSATCoordinationContextBuilder {
   protected String coordinationType;
   protected String identifier;
   protected long expires;
   protected String registrationCoordinatorAddress;
   protected String txId;
   protected boolean mustUnderstand = false;
   protected SOAPVersion soapVersion;

   public WSATCoordinationContextBuilder() {
      this.soapVersion = SOAPVersion.SOAP_11;
   }

   public static WSATCoordinationContextBuilder newInstance(Transactional.Version var0) {
      if (Version.WSAT10 == var0) {
         return new WSATCoordinationContextBuilderImpl();
      } else if (Version.WSAT11 != var0 && Version.WSAT12 != var0) {
         throw new IllegalArgumentException(var0 + "is not a supported ws-at version");
      } else {
         return new weblogic.wsee.wstx.wsc.v11.WSATCoordinationContextBuilderImpl();
      }
   }

   public WSATCoordinationContextBuilder txId(String var1) {
      this.txId = var1;
      return this;
   }

   public WSATCoordinationContextBuilder registrationCoordinatorAddress(String var1) {
      this.registrationCoordinatorAddress = var1;
      return this;
   }

   public WSATCoordinationContextBuilder soapVersion(SOAPVersion var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("SOAP version can't null!");
      } else {
         this.soapVersion = var1;
         return this;
      }
   }

   public WSATCoordinationContextBuilder mustUnderstand(boolean var1) {
      this.mustUnderstand = var1;
      return this;
   }

   public WSATCoordinationContextBuilder expires(long var1) {
      this.expires = var1;
      return this;
   }

   public CoordinationContextIF build() {
      CoordinationContextBuilder var1 = this.configBuilder();
      return var1.build();
   }

   private CoordinationContextBuilder configBuilder() {
      if (this.registrationCoordinatorAddress == null) {
         this.registrationCoordinatorAddress = this.getDefaultRegistrationCoordinatorAddress();
      }

      CoordinationContextBuilder var1 = this.newCoordinationContextBuilder();
      var1.coordinationType(this.getCoordinationType()).address(this.registrationCoordinatorAddress).identifier("urn:uuid:" + this.txId).txId(this.txId).expires(this.expires).soapVersion(this.soapVersion).mustUnderstand(this.mustUnderstand);
      return var1;
   }

   protected abstract CoordinationContextBuilder newCoordinationContextBuilder();

   protected abstract String getCoordinationType();

   protected abstract String getDefaultRegistrationCoordinatorAddress();
}
