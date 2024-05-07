package weblogic.wsee.security;

import weblogic.wsee.message.WlMessageContext;

public interface Authorizer {
   boolean isAccessAllowed(WlMessageContext var1);
}
