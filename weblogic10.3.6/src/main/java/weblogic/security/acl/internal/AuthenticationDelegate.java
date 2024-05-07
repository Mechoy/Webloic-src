package weblogic.security.acl.internal;

import java.util.Enumeration;

public interface AuthenticationDelegate {
   boolean authenticate(String var1, String var2);

   Enumeration getUserNames();

   Enumeration getGroupNames();

   Enumeration getGroupsForUser(String var1);

   boolean isUserInGroup(String var1, String var2);
}
