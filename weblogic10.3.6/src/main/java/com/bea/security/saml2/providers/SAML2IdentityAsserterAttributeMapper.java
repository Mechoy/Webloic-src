package com.bea.security.saml2.providers;

import java.util.Collection;
import weblogic.security.service.ContextHandler;

public interface SAML2IdentityAsserterAttributeMapper {
   Collection<?> mapAttributeInfo(Collection<SAML2AttributeStatementInfo> var1, ContextHandler var2);
}
