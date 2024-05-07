package weblogic.wsee.security.wssp;

public interface SecureConversationTokenAssertion extends SecurityContextTokenAssertion {
   String SCT_TOKEN_TYPE_V200502 = "http://schemas.xmlsoap.org/ws/2005/02/sc/sct";
   String DK_TOKEN_TYPE_V200502 = "http://schemas.xmlsoap.org/ws/2005/02/sc/dk";
   String WS_SX_SCT_TOKEN_TYPE_V200512 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/sct";
   String WS_SX_DK_TOKEN_TYPE_V200512 = "http://docs.oasis-open.org/ws-sx/ws-secureconversation/200512/dk";

   String getIssuerForSecurityContextToken();

   SecurityPolicyAssertionInfo[] getBootstrapPolicy();

   boolean isSC200502SecurityContextToken();
}
