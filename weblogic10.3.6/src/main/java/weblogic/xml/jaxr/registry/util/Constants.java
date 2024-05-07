package weblogic.xml.jaxr.registry.util;

public class Constants {
   public static final String QUERY_URL_KEY = "javax.xml.registry.queryManagerURL";
   public static final String PUBLISH_URL_KEY = "javax.xml.registry.lifeCycleManagerURL";
   public static final String SEMANTIC_EQUIVALENCES_KEY = "javax.xml.registry.semanticEquivalences";
   public static final String UDDI_MAXROWS_KEY = "javax.xml.registry.uddi.maxRows";
   public static final String POSTAL_ADDRESS_SCHEME_KEY = "javax.xml.registry.postalAddressScheme";
   public static final String AUTHENTICATION_METHOD_KEY = "javax.xml.registry.security.authenticationMethod";
   public static final String AUTHENTICATION_METHOD_UDDI = "UDDI_GET_AUTHTOKEN";
   public static final String AUTHENTICATION_METHOD_HTTP = "HTTP_BASIC";
   public static final String AUTHENTICATION_METHOD_CERTIFICATE = "CLIENT_CERTIFICATE";
   public static final String AUTHENTICATION_METHOD_PASSPORT = "MS_PASSPORT";
   public static final String UTF8 = "UTF-8";

   private Constants() {
   }
}
