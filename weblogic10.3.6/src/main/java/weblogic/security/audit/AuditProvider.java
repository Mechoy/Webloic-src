package weblogic.security.audit;

import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.Permission;
import weblogic.security.acl.User;
import weblogic.security.acl.UserInfo;

/** @deprecated */
public interface AuditProvider {
   void authenticateUser(String var1, UserInfo var2, User var3);

   void checkPermission(String var1, Acl var2, Principal var3, Permission var4, boolean var5);
}
