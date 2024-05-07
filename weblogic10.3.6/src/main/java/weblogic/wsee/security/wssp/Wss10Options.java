package weblogic.wsee.security.wssp;

public interface Wss10Options {
   boolean isMustSupportKeyIdentiferReference();

   boolean isMustSupportIssuerSerialReference();

   boolean isMustSupportExternalUriReference();

   boolean isMustSupportEmbeddedTokenReference();
}
