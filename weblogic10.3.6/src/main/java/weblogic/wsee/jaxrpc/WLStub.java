package weblogic.wsee.jaxrpc;

public interface WLStub {
   String INVOKE_PROPERTIES = "weblogic.wsee.invoke_properties";
   String SSL_ADAPTER = "weblogic.wsee.client.ssladapter";
   String PROXY_USERNAME = "weblogic.webservice.client.proxyusername";
   String PROXY_PASSWORD = "weblogic.webservice.client.proxypassword";
   String HANDLER_REGISTRY = "weblogic.wsee.handler.registry";
   String USE_WSADDRESSING = "weblogic.wsee.addressing.version";
   String TARGET_REFERENCE = "weblogic.wsee.addressing.Target";
   String SERVER_VERIFY_CERT = "weblogic.wsee.security.bst.serverVerifyCert";
   String SERVER_ENCRYPT_CERT = "weblogic.wsee.security.bst.serverEncryptCert";
   String WST_STS_ENDPOINT_ON_WSSC = "weblogic.wsee.wst.sts_endpoint_uri";
   String WST_STS_ENDPOINT_ON_SAML = "weblogic.wsee.wst.saml.sts_endpoint_uri";
   String STS_ENCRYPT_CERT = "weblogic.wsee.security.bst.stsEncryptCert";
   String REPLY_TO = "weblogic.wsee.addressing.ReplyTo";
   String FROM = "weblogic.wsee.addressing.From";
   String FAULT_TO = "weblogic.wsee.addressing.FaultTo";
   String CALLBACK_TO = "weblogic.wsee.addressing.CallbackTo";
   String MESSAGE_ID = "weblogic.wsee.addressing.MessageId";
   String ACTION = "weblogic.wsee.addressing.Action";
   String COOKIES = "weblogic.wsee.addressing.Cookie";
   String CONVERSATION_VERSION_PROPERTY = "weblogic.wsee.conversation.ConversationVersion";
   Integer CONVERSATION_VERSION_ONE = new Integer(1);
   Integer CONVERSATION_VERSION_TWO = new Integer(2);
   String CONVERSATION_ID = "weblogic.wsee.conversation.ConversationId";
   String CONVERSATIONAL_METHOD_BLOCK_TIMEOUT = "weblogic.wsee.conversation.method.block.timeout";
   String WSRM_SEQUENCE_EXPIRATION = "weblogic.wsee.wsrm.sequence.expiration";
   String WSRM_OFFER_SEQUENCE_EXPIRATION = "weblogic.wsee.wsrm.offer.sequence.expiration";
   String WSRM_ACKSTO_ANONYMOUS = "weblogic.wsee.ackstoanon";
   String WSRM_LAST_MESSAGE = "weblogic.wsee.lastmessage";
   String WSRM_FINAL_MESSAGE = "weblogic.wsee.reliability.FinalMessage";
   String ENABLE_ADDRESSING = "weblogic.wsee.use_addressing";
   String COMPLEX = "weblogic.wsee.complex";
   String WSS_SUBJECT_PROPERTY = "weblogic.wsee.wss.subject";
   String TRANSPORT_SUBJECT_PROPERTY = "weblogic.wsee.subject";
   String JMS_TRANSPORT_JNDI_URL = "weblogic.wsee.transport.jms.url";
   String JMS_TRANSPORT_MESSAGE_TYPE = "weblogic.wsee.transport.jms.messagetype";
   String JMS_TEXTMESSAGE = "TextMessage";
   String JMS_BYTESMESSAGE = "BytesMessage";
   String MARSHAL_FORCE_INCLUDE_XSI_TYPE = "weblogic.wsee.marshal.forceIncludeXsiType";
   String FORCE_DOTNET_COMPATIBLE_BINDING = "weblogic.wsee.dotnet.compatible.binding";
   String VALIDATE_WEBSERVICE_RESPONSE = "weblogic.wsee.client.validate_response";
   String CHARACTER_SET_ENCODING = "weblogic.wsee.client.xmlcharset";
   String POLICY_SELECTION_PREFERENCE = "weblogic.wsee.policy.selection.preference";
   String POLICY_PREFERENCE_SECURITY = "S";
   String POLICY_PREFERENCE_PERFORMANCE = "P";
   String POLICY_PREFERENCE_COMPATIBILITY = "C";
   String PREFERENCE_SECURITY_INTEROPERABILITY_PERFORMANCE = "SCP";
   String PREFERENCE_SECURITY_PERFORMANCE_INTEROPERABILITY = "SPC";
   String PREFERENCE_INTEROPERABILITY_SECURITY_PERFORMANCE = "CSP";
   String PREFERENCE_INTEROPERABILITY_PERFORMANCE_SECURITY = "CPS";
   String PREFERENCE_PERFORMANCE_INTEROPERABILITY_SECURITY = "PCS";
   String PREFERENCE_PERFORMANCE_SECURITY_INTEROPERABILITY = "PSC";
   String PREFERENCE_SECURITY_COMPATIBILITY_PERFORMANCE = "SCP";
   String PREFERENCE_SECURITY_PERFORMANCE_COMPATIBILITY = "SPC";
   String PREFERENCE_COMPATIBILITY_SECURITY_PERFORMANCE = "CSP";
   String PREFERENCE_COMPATIBILITY_PERFORMANCE_SECURITY = "CPS";
   String PREFERENCE_PERFORMANCE_COMPATIBILITY_SECURITY = "PCS";
   String PREFERENCE_PERFORMANCE_SECURITY_COMPATIBILITY = "PSC";
   String PREFERENCE_DEFAULT = "NONE";
   String POLICY_COMPATIBILITY_PREFERENCE = "weblogic.wsee.policy.compat.preference";
   String POLICY_COMPATIBILITY_MSFT = "msft";
   String POLICY_COMPATIBILITY_WSSC13 = "wssc1.3";
   String POLICY_COMPATIBILITY_WSSC14 = "wssc1.4";
   String POLICY_COMPATIBILITY_METRO = "metro";
   String POLICY_COMPATIBILITY_ORDERING_PREFERENCE = "weblogic.wsee.policy.compat.ordering.preference";
   String PREFERENCE_WSSC14_WSSC13_MSFT = "wssc1.4_wssc1.3_msft";
   String PREFERENCE_WSSC13_WSSC14_MSFT = "wssc1.3_wssc1.4_msft";
   String PREFERENCE_WSSC14_MSFT_WSSC13 = "wssc1.4_msft_wssc1.3";
   String PREFERENCE_WSSC13_MSFT_WSSC14 = "wssc1.3_msft_wssc1.4";
   String PREFERENCE_MSFT_WSSC14_WSSC13 = "msft_wssc1.4_wssc1.3";
   String PREFERENCE_MSFT_WSSC13_WSSC14 = "msft_wssc1.3_wssc1.4";
   String HIGH_PERFORMANCE_SECURE_CONVERSATION = "weblogic.wsee.security.wssc.highPerformanceSecureConversationOption";
   String PROACTIVE_SCT_RENEWAL = "weblogic.wsee.security.wssc.proactiveSCTRenewal";
   String CHECKING_SCT_EXPIRATION = "weblogic.wsee.security.wssc.checkingSCTExpiration";
   String STRICT_CHECKING_SCT_EXPIRATION = "strictCheckingSCTExpiration";
   String TOLERANT_CHECKING_SCT_EXPIRATION = "tolerantCheckingSCTExpiration";
   String LAX_CHECKING_SCT_EXPIRATION = "laxCheckingSCTExpiration";
   String MARSHAL_FORCE_ORACLE1012_COMPATIBLE = "weblogic.wsee.marshal.forceOracle1012CompatibleMarshal";
   String ENFORCE_ASYNC_TRUST_EXCHANGE = "weblogic.wsee.security.wst.enforceAsyncTrustExchange";
   String SAML_ATTRIBUTES = "weblogic.wsee.security.saml.attributies";
   String SAML_ATTRIBUTE_ONLY = "oracle.contextelement.saml2.AttributeOnly";
}