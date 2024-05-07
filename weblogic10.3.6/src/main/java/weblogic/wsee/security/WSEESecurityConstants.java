package weblogic.wsee.security;

public interface WSEESecurityConstants {
   String TRANSPORT_INFO = "weblogic.wsee.security.wst_transportinfo";
   String SSL_ADAPTER = "weblogic.wsee.security.wst_ssladapter";
   String ON_BEHALF_OF_USER = "weblogic.wsee.security.wst_onbehalfof_user";
   String TRUST_CLAIM = "weblogic.wsee.security.trust_claim";
   String TRUST_VERSION = "weblogic.wsee.security.trust_version";
   String TRUST_SOAP_VERSION = "weblogic.wsee.security.trust_soap_version";
   String TRUST_KEY_TYPE = "weblogic.wsee.security.trust_key_type";
   String TRUST_KEY_TYPE_BEARER = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer";
   String TRUST_KEY_TYPE_SYMMETRIC_KEY = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/SymmetricKey";
   String TRUST_KEY_TYPE_PUBLIC_KEY = "http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey";
}
