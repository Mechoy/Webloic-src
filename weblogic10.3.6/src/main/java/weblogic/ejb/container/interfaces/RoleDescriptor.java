package weblogic.ejb.container.interfaces;

import java.util.Collection;

public interface RoleDescriptor {
   String getName();

   Collection getAllSecurityPrincipals();

   boolean isExternallyDefined();
}
