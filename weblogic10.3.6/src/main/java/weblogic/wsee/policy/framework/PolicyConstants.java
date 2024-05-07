package weblogic.wsee.policy.framework;

import javax.xml.namespace.QName;
import weblogic.xml.schema.binding.util.StdNamespace;

public final class PolicyConstants {
   public static final String POLICY = "Policy";
   public static final String POLICY_REFERENCE = "PolicyReference";
   public static final String POLICY_REFERENCE_URI = "URI";
   public static final String POLICY_PREFIX = "wsp";
   public static final String POLICY_NAMESPACE_URI = "http://schemas.xmlsoap.org/ws/2004/09/policy";
   public static final String POLICY15_PREFIX = "wsp15";
   public static final String POLICY15_NAMESPACE_URI = "http://www.w3.org/ns/ws-policy";
   public static final String UTIL_NAMESPACE_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
   public static final QName POLICY_STATEMENT_ELEMENT = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "Policy", "wsp");
   public static final QName POLICY_STATEMENT_ELEMENT_15 = new QName("http://www.w3.org/ns/ws-policy", "Policy", "wsp");
   public static final QName POLICY_STATEMENT_NAME_ATTRIBUTE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "Name", "wsp");
   public static final QName POLICY_STATEMENT_NAME_ATTRIBUTE_15 = new QName("http://www.w3.org/ns/ws-policy", "Name", "wsp");
   public static final QName POLICY_STATEMENT_TARGETNS_ATTRIBUTE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "TargetNamespace", "wsp");
   public static final QName POLICY_STATEMENT_TARGETNS_ATTRIBUTE_15 = new QName("http://www.w3.org/ns/ws-policy", "TargetNamespace", "wsp");
   public static final QName POLICY_STATEMENT_ID_ATTRIBUTE = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");
   public static final QName ONE_OR_MORE_15 = new QName("http://www.w3.org/ns/ws-policy", "OneOrMore", "wsp");
   public static final QName ONE_OR_MORE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "OneOrMore", "wsp");
   public static final QName EXACTLY_ONE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "ExactlyOne", "wsp");
   public static final QName EXACTLY_ONE_15 = new QName("http://www.w3.org/ns/ws-policy", "ExactlyOne", "wsp");
   public static final QName ALL = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "All", "wsp");
   public static final QName ALL_15 = new QName("http://www.w3.org/ns/ws-policy", "All", "wsp");
   public static final QName POLICY_INCLUDE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "PolicyReference", "wsp");
   public static final QName POLICY_INCLUDE_15 = new QName("http://www.w3.org/ns/ws-policy", "PolicyReference", "wsp");
   public static final QName POLICY_INCLUDE_QNAME_REF_ATTRIBUTE = new QName("Ref");
   public static final QName POLICY_INCLUDE_URI_REF_ATTRIBUTE = new QName("URI");
   public static final QName POLICY_INCLUDE_DIGEST_ATTRIBUTE = new QName("Digest");
   public static final QName POLICY_INCLUDE_DIGEST_ALGORITHM_ATTRIBUTE = new QName("DigestAlgorithm");
   public static final QName OPTIONAL = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "Optional");
   public static final QName OPTIONAL_15 = new QName("http://www.w3.org/ns/ws-policy", "Optional");
   public static final String USING_POLICY_ELEMENT = "UsingPolicy";
   public static final String POLICY_URIS_ATTRIBUTE = "PolicyURIs";
   public static final String WSDL_NAMESPACE_URI = StdNamespace.instance().wsdl();
   public static final String WSSE_NAMESPACE_URI = "http://www.bea.com/wls90/security/policy";
   public static final QName SHA1 = new QName("http://www.bea.com/wls90/security/policy", "Sha1Exc");
   public static final String DEFAULT_POLICY_URI_PROPERTY = "weblogic.wsee.policy.default.uri";
   public static final String EFFECTIVE_POLICY_PROPERTY_NAME = "weblogic.wsee.policy.EffectivePolicy";
   public static final String BUILTIN_PROVIDER_REGISTRY_NAME = "weblogic/wsee/policy/deployment/wls-policy-providers.xml";
   public static final QName POLICY_VALIDATION_FAULT_CODE = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "PolicyFault", "wsp");
   public static final String POLICY_JAR_DIR_NAME = "policies";
}
