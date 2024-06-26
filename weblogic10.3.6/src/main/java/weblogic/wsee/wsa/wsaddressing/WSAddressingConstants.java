package weblogic.wsee.wsa.wsaddressing;

import javax.xml.namespace.QName;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.wsa.wsutility.WSUtilityConstants;

public interface WSAddressingConstants {
   String USE_WSADDRESSING_10 = "weblogic.wsee.addressing.10";
   String USE_WSADDRESSING = "weblogic.wsee.addressing.version";
   String WSA_PREFIX = "wsa";
   String WSA_10_NS = "http://www.w3.org/2005/08/addressing";
   String WSA_Aug_2004_NS = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
   /** @deprecated */
   @Deprecated
   String WSA_NS = System.getProperty("weblogic.wsee.addressing.10") != null ? "http://www.w3.org/2005/08/addressing" : "http://schemas.xmlsoap.org/ws/2004/08/addressing";
   String WSAW10_NS = "http://www.w3.org/2006/05/addressing/wsdl";
   String WSAW_PREFIX = "wsaw";
   String WSAP_PREFIX = "wsap";
   String WSAW_LOCAL_NAME = "UsingAddressing";
   String WSAP_NS = "http://schemas.xmlsoap.org/ws/2004/08/addressing/policy";
   String WSAX_PREFIX = "wsax";
   String WSAX_NS = "http://schemas.xmlsoap.org/ws/2004/01/addressingx";
   String XML_RELATION_SHIP_VALUE_10 = "http://www.w3.org/2005/08/addressing/reply";
   String XML_TAG_ADDRESS = "Address";
   String XML_TAG_ACTION = "Action";
   String XML_TAG_PORT_TYPE = "PortType";
   String XML_TAG_SERVICE_NAME = "ServiceName";
   String XML_TAG_REFERENCE_PROPERTIES = "ReferenceProperties";
   String XML_TAG_REFERENCE_PARAMETERS = "ReferenceParameters";
   String XML_TAG_METADATA = "Metadata";
   String XML_TAG_INTERFACE_NAME = "InterfaceName";
   String XML_TAG_ENDPOINT_NAME = "EndpointName";
   String XML_TAG_PORT_NAME = "PortName";
   String XML_TAG_POLICY = "Policy";
   String XML_TAG_REPLY_TO = "ReplyTo";
   String XML_TAG_FAULT_TO = "FaultTo";
   String XML_TAG_RECIPIENT = "Recipient";
   String XML_TAG_FROM = "From";
   String XML_TAG_TO = "To";
   String XML_TAG_MESSAGE_ID = "MessageID";
   String XML_TAG_RELATES_TO = "RelatesTo";
   String XML_TAG_RELATIONSHIP_TYPE = "RelationshipType";
   String XML_TAG_DEFAULT_RELATIONSHIP_TYPE = "Reply";
   QName WSA_RESPONSE = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Reply", "wsa");
   QName WSA_RESPONSE_10 = new QName("http://www.w3.org/2005/08/addressing", "Reply", "wsa");
   String UNSPECIFIED_MESSAGE_ID_URI = WSA_NS + "/id/unspecified";
   String ANONYMOUS_REFERENCE_URI_10 = "http://www.w3.org/2005/08/addressing/anonymous";
   String ANONYMOUS_REFERENCE_URI_SUBMISSION = "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
   String ANONYMOUS_REFERENCE_URI = System.getProperty("weblogic.wsee.addressing.10") != null ? "http://www.w3.org/2005/08/addressing/anonymous" : "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";
   String XML_TAG_COOKIE = "Cookie";
   String XML_TAG_SET_COOKIE = "SetCookie";
   String XML_TAG_SESSION_REDIRECT = "SessionRedirect";
   String XML_TAG_IS_REF_PARAM = "IsReferenceParameter";
   EndpointReference ANONYMOUS_REFERENCE = new EndpointReference(ANONYMOUS_REFERENCE_URI);
   QName WSA_HEADER_REPLY_TO = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ReplyTo", "wsa");
   QName WSA_HEADER_REPLY_TO_10 = new QName("http://www.w3.org/2005/08/addressing", "ReplyTo", "wsa");
   QName WSA_HEADER_FAULT_TO = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "FaultTo", "wsa");
   QName WSA_HEADER_FAULT_TO_10 = new QName("http://www.w3.org/2005/08/addressing", "FaultTo", "wsa");
   QName WSA_HEADER_SOURCE = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "From", "wsa");
   QName WSA_HEADER_SOURCE_10 = new QName("http://www.w3.org/2005/08/addressing", "From", "wsa");
   QName WSA_HEADER_RECIPIENT = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Recipient", "wsa");
   QName WSA_HEADER_TO = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "To", "wsa");
   QName WSA_HEADER_TO_10 = new QName("http://www.w3.org/2005/08/addressing", "To", "wsa");
   QName WSA_HEADER_MESSAGE_ID = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "MessageID", "wsa");
   QName WSA_HEADER_MESSAGE_ID_10 = new QName("http://www.w3.org/2005/08/addressing", "MessageID", "wsa");
   QName WSA_HEADER_RELATES_TO = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "RelatesTo", "wsa");
   QName WSA_HEADER_RELATES_TO_10 = new QName("http://www.w3.org/2005/08/addressing", "RelatesTo", "wsa");
   QName WSA_HEADER_ACTION = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Action", "wsa");
   QName WSA_HEADER_ACTION_10 = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
   String XML_TAG_PROBLEM_HEADER = "ProblemHeaderQName";
   QName WSA_HEADER_PROBLEM_HEADER_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ProblemHeaderQName", "wsa");
   QName WSA_HEADER_PROBLEM_HEADER_QNAME_10 = new QName("http://www.w3.org/2005/08/addressing", "ProblemHeaderQName", "wsa");
   String XML_TAG_FAULT_DETAIL = "FaultDetail";
   QName WSA_HEADER_FAULT_DETAIL = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "FaultDetail", "wsa");
   QName WSA_HEADER_FAULT_DETAIL_10 = new QName("http://www.w3.org/2005/08/addressing", "FaultDetail", "wsa");
   QName WSAX_HEADER_SET_COOKIE = new QName("http://schemas.xmlsoap.org/ws/2004/01/addressingx", "SetCookie", "wsax");
   QName WSAX_HEADER_SESSION_REDIRECT = new QName("http://schemas.xmlsoap.org/ws/2004/01/addressingx", "SessionRedirect", "wsax");
   QName WSA_HEADER_METADATA_INTERFACE_NAME_10 = new QName("http://www.w3.org/2006/05/addressing/wsdl", "InterfaceName", "wsaw");
   QName WSA_HEADER_METADATA_SERVICE_NAME_10 = new QName("http://www.w3.org/2006/05/addressing/wsdl", "ServiceName", "wsaw");
   QName WSA_HEADER_METADATA_SERVICE_ENDPOINT_ATTR = new QName("http://www.w3.org/2006/05/addressing/wsdl", "EndpointName", "wsaw");
   QName WSA_METADATA_EMBEDDED_WSDL_NAME_11 = new QName("http://schemas.xmlsoap.org/wsdl/", "definitions");
   QName WSA_METADATA_EMBEDDED_WSDL_NAME_20 = new QName("http://www.w3.org/ns/wsdl", "definitions");
   QName[] WSA_HEADERS = new QName[]{WSA_HEADER_REPLY_TO, WSA_HEADER_REPLY_TO_10, WSA_HEADER_FAULT_TO, WSA_HEADER_FAULT_TO_10, WSA_HEADER_SOURCE, WSA_HEADER_SOURCE_10, WSA_HEADER_RECIPIENT, WSA_HEADER_TO, WSA_HEADER_TO_10, WSA_HEADER_MESSAGE_ID, WSA_HEADER_MESSAGE_ID_10, WSA_HEADER_RELATES_TO, WSA_HEADER_RELATES_TO_10, WSAX_HEADER_SET_COOKIE, WSAX_HEADER_SESSION_REDIRECT, WSUtilityConstants.WSU_HEADER_TIMESTAMP};
   String FAULT_ACTION_SUFFIX = "/fault";
   String FAULT_ACTION_URI = WSA_NS + "/fault";
   String WSA_SOAP_FAULT_ACTION = "http://www.w3.org/2005/08/addressing/soap/fault";
   QName WSA_MESSAGE_HEADER_REQUIRED_FC = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "MessageInformationHeaderRequired", "wsa");
   String WSA_MESSAGE_HEADER_REQUIRED_REASON = "A required message information header, To, MessageID, or Action, is not present.";
   QName WSA_MESSAGE_HEADER_REQUIRED_FC_10 = new QName("http://www.w3.org/2005/08/addressing", "MessageAddressingHeaderRequired", "wsa");
   String WSA_MESSAGE_HEADER_REQUIRED_REASON_10 = "A required header representing a Message Addressing Property is not present.";
   String WSA_ACTION_HEADER_REQUIRED_REASON = "A required message information header, Action is not present.";
   String WSA_TO_HEADER_REQUIRED_REASON = "A required message information header, To is not present.";
   QName WSA_DESTINATION_UNREACHABLE_FC = new QName(WSA_NS, "DestinationUnreachable", "wsa");
   QName WSA_ACTION_NOT_SUPPORTED_FC = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "ActionNotSupported", "wsa");
   QName WSA_ACTION_NOT_SUPPORTED_FC_10 = new QName("http://www.w3.org/2005/08/addressing", "ActionNotSupported", "wsa");
   QName WSA_ACTION_MISMATCH_FC = new QName("http://www.w3.org/2005/08/addressing", "ActionMismatch", "wsa");
   String WSA_ACTION_NOT_SUPPORTED_REASON = "The action is not supported in this endpoint, or it is mismatched with the action defined in WSDL.";
   QName WSA_INVALID_MESSAGE_HEADER_FC = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "InvalidMessageInformationHeader", "wsa");
   QName WSA_ENDPOINT_UNAVAILABLE_FC = new QName(WSA_NS, "EndpointUnavailable", "wsa");
   QName WSA_INVALID_ADDRESSING_HEADER_FC = new QName("http://www.w3.org/2005/08/addressing", "InvalidAddressingHeader", "wsa");
   QName WSA_INVALID_CARDINALITY = new QName("http://www.w3.org/2005/08/addressing", "InvalidCardinality", "wsa");
   String FROM_ANONYMOUS = "weblogic.wsee.from.anonymous";
   String FAULT_ANONYMOUS = "weblogic.wsee.fault.anonymous";
   String REPLY_ANONYMOUS = "weblogic.wsee.reply.anonymous";
   QName WSAW_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing/policy", "UsingAddressing", "wsap");
   QName WSAW_QNAME_10 = new QName("http://www.w3.org/2006/05/addressing/wsdl", "UsingAddressing", "wsaw");
   QName WSAW_ATT_QNAME = new QName("http://schemas.xmlsoap.org/wsdl/", "required", "wsdl");
   String WSA_VERSION_MISMATCH_REASON = "The WS-Addressing version of incoming request does not matched with the WS-Addresssing version which defines with UsingAddressing Policy in WSDL!";
   String ISSUE_ADDRESS = "Address";
   QName ISSUE_ADDRESS_QNAME_10 = new QName("http://www.w3.org/2005/08/addressing", "Address", "wsa");
   QName ISSUE_ADDRESS_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/08/addressing", "Address", "wsa");
   String WSA_INVALIDE_ADDRESSING_HEADER_REASON = "A header representing a Message Addressing Property is not valid and the message cannot be processed!";
}
