package weblogic.xml.security.wsu.v200207;

import java.util.TimeZone;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.stream.XMLName;

public interface WSUConstants {
   String WSU_URI_PROPERTY = "weblogic.webservice.namespace.wsu";
   String DEFAULT_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
   String OASIS_INTEROP_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
   String PROVIDED_URI = System.getProperty("weblogic.webservice.namespace.wsu");
   String WSU_URI = PROVIDED_URI == null ? "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" : PROVIDED_URI;
   String DEFAULT_PREFIX = "wsu";
   String ATTR_ID = "Id";
   String ATTR_MUST_UNDERSTAND = "mustUnderstand";
   String ATTR_VALUETYPE = "ValueType";
   String ATTR_DELAY = "Delay";
   String ATTR_ACTOR = "Actor";
   String FAULT_TIMESTAMP_EXPIRED = "MessageExpired";
   String FAULT_CONTEXT_ESTABLISHED = "ContextEstablished";
   String FAULT_CONTEXT_UNKNOWN = "ContextUnknown";
   String FAULT_CONTEXT_NOT_SUPPORTED = "ContextNotSupported";
   String FAULT_CONTEXT_REFUSED = "ContextRefused";
   String FAULT_CONTEXT_EXPIRED = "ContextExpired";
   XMLName QNAME_FAULT_TIMESTAMP_EXPIRED = ElementFactory.createXMLName(WSU_URI, "MessageExpired");
   XMLName QNAME_FAULT_CONTEXT_ESTABLISHED = ElementFactory.createXMLName(WSU_URI, "ContextEstablished");
   XMLName QNAME_FAULT_CONTEXT_UNKNOWN = ElementFactory.createXMLName(WSU_URI, "ContextUnknown");
   XMLName QNAME_FAULT_CONTEXT_NOT_SUPPORTED = ElementFactory.createXMLName(WSU_URI, "ContextNotSupported");
   XMLName QNAME_FAULT_CONTEXT_REFUSED = ElementFactory.createXMLName(WSU_URI, "ContextRefused");
   XMLName QNAME_FAULT_CONTEXT_EXPIRED = ElementFactory.createXMLName(WSU_URI, "ContextExpired");
   String TAG_RECEIVED = "Received";
   String TAG_TIMESTAMP = "Timestamp";
   String TAG_CREATED = "Created";
   String TAG_EXPIRES = "Expires";
   String TAG_CONTEXT = "Context";
   String TAG_IDENTIFIER = "Identifier";
   String TAG_PORT_REFERENCE = "PortReference";
   String TAG_ADDRESS = "Address";
   int TC_RECEIVED = 0;
   int TC_TIMESTAMP = 1;
   int TC_CREATED = 2;
   int TC_EXPIRES = 3;
   int TC_CONTEXT = 4;
   int TC_IDENTIFIER = 5;
   int TC_PORT_REFERENCE = 6;
   int TC_ADDRESS = 7;
   TimeZone TZ_ZULU = TimeZone.getTimeZone("UTC");
}
