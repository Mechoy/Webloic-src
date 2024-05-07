package weblogic.xml.security.encryption;

public interface XMLEncConstants {
   String XMLENC_URI = "http://www.w3.org/2001/04/xmlenc#";
   String XMLENC_PREFIX = "xenc";
   String ATTR_ALGORITHM = "Algorithm";
   String ATTR_ENCODING = "Encoding";
   String ATTR_ID = "Id";
   String ATTR_MIME_TYPE = "MimeType";
   String ATTR_RECIPIENT = "Recipient";
   String ATTR_TYPE = "Type";
   String ATTR_URI = "URI";
   String TAG_CARRIED_KEY_NAME = "CarriedKeyName";
   String TAG_CIPHER_DATA = "CipherData";
   String TAG_CIPHER_VALUE = "CipherValue";
   String TAG_DATA_REFERENCE = "DataReference";
   String TAG_ENCRYPTED_DATA = "EncryptedData";
   String TAG_ENCRYPTED_KEY = "EncryptedKey";
   String TAG_ENCRYPTED_TYPE = "EncryptedType";
   String TAG_ENCRYPTION_METHOD = "EncryptionMethod";
   String TAG_KEY_REFERENCE = "KeyReference";
   String TAG_KEY_SIZE = "KeySize";
   String TAG_OAEP_PARAMS = "OAEPparams";
   String TAG_REFERENCE_LIST = "ReferenceList";
   String DEFAULT_ENC_ENCODING = "UTF-8";
   String VERBOSE_PROPERTY = "weblogic.xml.security.encryption.verbose";
   String VERBOSE_PROPERTY_ALT = "weblogic.xml.encryption.verbose";
   boolean VERBOSE = Boolean.getBoolean("weblogic.xml.security.encryption.verbose") || Boolean.getBoolean("weblogic.xml.encryption.verbose");
   String CONTENT_ONLY_PROPERTY = "weblogic.xml.security.encryption.contentOnly";
   boolean CONTENT_ONLY = Boolean.getBoolean("weblogic.xml.security.encryption.contentOnly");
   int TC_ENCRYPTED_DATA = 1;
   int TC_ENCRYPTED_KEY = 2;
   int TC_ENCRYPTION_METHOD = 3;
   int TC_REFERENCE_LIST = 4;
}
