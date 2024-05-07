package weblogic.xml.security;

/** @deprecated */
public interface SecurityAssertion {
   String IDENTITY_ASSERTION = "http://www.bea.com/servers/xml/security/assertion/Identity";
   int IDENTITY_CODE = 0;
   String INTEGRITY_ASSERTION = "http://www.bea.com/servers/xml/security/assertion/Integrity";
   int INTEGRITY_CODE = 1;
   String TYPE_INTEGRITY_ASSERTION = "http://www.bea.com/servers/xml/security/assertion/TypeIntegrity";
   int TYPE_INTEGRITY_CODE = 2;
   String CONFIDENTIALITY_ASSERTION = "http://www.bea.com/servers/xml/security/assertion/Confidentiality";
   int CONFIDENTIALITY_CODE = 3;
   String TYPE_CONFIDENTIALITY_ASSERTION = "http://www.bea.com/servers/xml/security/assertion/TypeConfidentiality";
   int TYPE_CONFIDENTIALITY_CODE = 4;

   String getAssertionType();

   int getAssertionTypeCode();

   boolean isAssertionType(String var1);

   boolean repudiable();
}
