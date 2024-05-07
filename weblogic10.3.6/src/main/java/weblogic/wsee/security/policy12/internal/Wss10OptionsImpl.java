package weblogic.wsee.security.policy12.internal;

import weblogic.wsee.security.policy12.assertions.Wss10;
import weblogic.wsee.security.wssp.Wss10Options;

public class Wss10OptionsImpl implements Wss10Options {
   private Wss10 wss10;

   Wss10OptionsImpl(Wss10 var1) {
      this.wss10 = var1;
   }

   protected Wss10 getWssOptions() {
      return this.wss10;
   }

   public boolean isMustSupportKeyIdentiferReference() {
      return this.getWssOptions().getMustSupportRefKeyIdentifier() != null;
   }

   public boolean isMustSupportIssuerSerialReference() {
      return this.getWssOptions().getMustSupportRefIssuerSerial() != null;
   }

   public boolean isMustSupportExternalUriReference() {
      return this.getWssOptions().getMustSupportRefExternalURI() != null;
   }

   public boolean isMustSupportEmbeddedTokenReference() {
      return this.getWssOptions().getMustSupportRefEmbeddedToken() != null;
   }
}
