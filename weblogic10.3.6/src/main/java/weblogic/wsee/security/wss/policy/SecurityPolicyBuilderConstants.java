package weblogic.wsee.security.wss.policy;

import javax.xml.namespace.QName;

public interface SecurityPolicyBuilderConstants {
   String STRICT = "Strict";
   String LAX_TS_FIRST = "LaxTimestampFirst";
   String LAX_TS_LAST = "LaxTimestampLast";
   String LAX = "Lax";
   String NEVER = "/IncludeToken/Never";
   String ONCE = "/IncludeToken/Once";
   String ALWAYS_TO_RECIPIENT = "/IncludeToken/AlwaysToRecipient";
   String ALWAYS_TO_INITIATOR = "/IncludeToken/AlwaysToInitiator";
   String ALWAYS = "/IncludeToken/Always";
   String POLICY_URI = "http://www.bea.com/wls90/security/policy";
   QName POLICY_USE_PASSWD = new QName("http://www.bea.com/wls90/security/policy", "UsePassword");
   QName POLICY_PASSWD_TYPE = new QName("Type");
   QName POLICY_PASSWD_ATTR = new QName("Attribute");
}
