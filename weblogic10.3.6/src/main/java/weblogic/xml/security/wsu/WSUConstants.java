package weblogic.xml.security.wsu;

import java.util.TimeZone;
import weblogic.xml.stream.XMLName;

public interface WSUConstants {
   String WSU_URI = weblogic.xml.security.wsu.v200207.WSUConstants.WSU_URI;
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
   XMLName QNAME_FAULT_TIMESTAMP_EXPIRED = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_TIMESTAMP_EXPIRED;
   XMLName QNAME_FAULT_CONTEXT_ESTABLISHED = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_CONTEXT_ESTABLISHED;
   XMLName QNAME_FAULT_CONTEXT_UNKNOWN = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_CONTEXT_UNKNOWN;
   XMLName QNAME_FAULT_CONTEXT_NOT_SUPPORTED = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_CONTEXT_NOT_SUPPORTED;
   XMLName QNAME_FAULT_CONTEXT_REFUSED = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_CONTEXT_REFUSED;
   XMLName QNAME_FAULT_CONTEXT_EXPIRED = weblogic.xml.security.wsu.v200207.WSUConstants.QNAME_FAULT_CONTEXT_EXPIRED;
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
   TimeZone TZ_ZULU = weblogic.xml.security.wsu.v200207.WSUConstants.TZ_ZULU;
}
