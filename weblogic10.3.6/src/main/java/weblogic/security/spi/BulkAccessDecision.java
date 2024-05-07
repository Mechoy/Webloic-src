package weblogic.security.spi;

import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.SecurityRole;

public interface BulkAccessDecision {
   Map<Resource, Result> isAccessAllowed(Subject var1, Map<Resource, Map<String, SecurityRole>> var2, List<Resource> var3, ContextHandler var4, Direction var5) throws InvalidPrincipalException;
}
