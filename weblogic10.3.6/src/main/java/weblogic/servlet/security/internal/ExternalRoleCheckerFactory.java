package weblogic.servlet.security.internal;

import weblogic.servlet.internal.WebAppServletContext;

public interface ExternalRoleCheckerFactory {
   ExternalRoleChecker getExternalRoleChecker(WebAppServletContext var1);
}
