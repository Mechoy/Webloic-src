package weblogic.security.spi;

import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.SecurityRole;

public interface BulkRoleMapper {
   Map<Resource, Map<String, SecurityRole>> getRoles(Subject var1, List<Resource> var2, ContextHandler var3);
}
