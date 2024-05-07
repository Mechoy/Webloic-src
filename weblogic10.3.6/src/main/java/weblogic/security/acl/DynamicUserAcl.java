package weblogic.security.acl;

import java.security.acl.Permission;

/** @deprecated */
public interface DynamicUserAcl extends BasicRealm {
   void newUserAcl(String var1, char var2, Permission[] var3) throws SecurityException;
}
