package weblogic.wsee.wsa.wsutility;

import javax.xml.namespace.QName;

public interface WSUtilityConstants {
   String WSU_NS = "http://schemas.xmlsoap.org/ws/2002/07/utility";
   String WSU_PREFIX = "wsu";
   String XML_TAG_TIMESTAMP = "Timestamp";
   String XML_TAG_CREATED = "Created";
   String XML_TAG_EXPIRES = "Expires";
   String XML_TAG_IDENTIFIER = "Identifier";
   QName WSU_HEADER_IDENTIFIER = new QName("http://schemas.xmlsoap.org/ws/2002/07/utility", "Identifier", "wsu");
   QName WSU_HEADER_TIMESTAMP = new QName("http://schemas.xmlsoap.org/ws/2002/07/utility", "Timestamp", "wsu");
   String SEQUENCE_EXPIRES_PROPERTY = "weblogic.wsee.expires";
}
