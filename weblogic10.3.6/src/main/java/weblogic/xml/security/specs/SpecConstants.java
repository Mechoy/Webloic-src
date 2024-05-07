package weblogic.xml.security.specs;

/** @deprecated */
public interface SpecConstants {
   String SPEC_URI = "http://www.openuri.org/2002/11/wsse/spec";
   String DEFAULT_PREFIX = "spec";
   String RESTRICTION_BODY = "body";
   String RESTRICTION_HEADER = "header";
   String ATTR_PASSWORDTYPE = "PasswordType";
   String ATTR_ENCODING = "EncodingType";
   String ATTR_VALUETYPE = "ValueType";
   String ATTR_REALM = "Realm";
   String ATTR_LOCALPART = "LocalPart";
   String ATTR_NAMESPACE = "Namespace";
   String ATTR_RESTRICTION = "Restriction";
   String ATTR_ENCRYPT_BODY = "EncryptBody";
   String ATTR_SIGN_BODY = "SignBody";
   String ATTR_ENCRYPTION_METHOD = "EncryptionMethod";
   String ATTR_KEYWRAPPING_METHOD = "KeyWrappingMethod";
   String ATTR_SIGNATURE_METHOD = "SignatureMethod";
   String ATTR_CANONICALIZATION_METHOD = "CanonicalizationMethod";
   String ATTR_ID = "Id";
   String ATTR_REF_ID = "RefId";
   String ATTR_KEY_ALIAS = "keyAlias";
   String ATTR_KEY_PASSWORD = "keyPassword";
   String SOAP_ATTR_ROLE = "Role";
   String SOAP_ENV_PREFIX = "env";
   String TAG_DD_SECURITY = "security";
   String TAG_ENCRYPTION_KEY = "encryptionKey";
   String TAG_SIGNATURE_KEY = "signatureKey";
   String TAG_USER = "user";
   String TAG_ENTITY_NAME = "name";
   String TAG_ENTITY_PASSWORD = "password";
   String TAG_TS_CONFIG = "timestamp";
   String TAG_REQUIRE_TS = "require-signature-timestamp";
   String TAG_GENERATE_TS = "generate-signature-timestamp";
   String TAG_CLOCKS_SYNCHRONIZED = "clocks-synchronized";
   String TAG_ENFORCE_PRECISION = "enforce-precision";
   String TAG_CLOCK_PRECISION = "clock-precision";
   String TAG_FRESHNESS = "inbound-expiry";
   String TAG_VALIDITY = "outbound-expiry";
   String TAG_SECURITY_SPEC = "SecuritySpec";
   String TAG_SECURITY_SPEC_REF = "SecuritySpecRef";
   String TAG_USERNAME_TOKEN_SPEC = "UsernameTokenSpec";
   String TAG_BINARY_SECURITY_TOKEN_SPEC = "BinarySecurityTokenSpec";
   String TAG_ENCRYPTION_SPEC = "EncryptionSpec";
   String TAG_SIGNATURE_SPEC = "SignatureSpec";
   String TAG_TYPE_IDENTIFIER = "ElementIdentifier";
   String XSD_TRUE = "true";
   String XSD_FALSE = "false";
}