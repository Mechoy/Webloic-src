package weblogic.security.ldaprealmv2;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import weblogic.logging.LogOutputStream;
import weblogic.security.acl.AbstractListableRealm;
import weblogic.security.acl.DebuggableRealm;
import weblogic.security.acl.FlatGroup;
import weblogic.security.acl.User;

/** @deprecated */
public final class LDAPRealm extends AbstractListableRealm implements DebuggableRealm, FlatGroup.Source {
   private static final int POOL_SIZE = 8;
   private LDAPDelegate delegate = new LDAPDelegate(this);
   LogOutputStream log;

   public LDAPRealm() {
      super("LDAP Realm");

      try {
         this.setDebug(Boolean.getBoolean("weblogic.security.ldaprealm.verbose"));
      } catch (Exception var2) {
      }

   }

   public User getUser(String var1) {
      if (this.log != null) {
         this.log.debug("getUser(\"" + var1 + "\")");
      }

      return this.delegate.getUser(var1);
   }

   protected User authUserPassword(String var1, String var2) {
      if (this.log != null) {
         this.log.debug("authUserPassword(\"" + var1 + "\")");
      }

      return this.delegate.authenticate(var1, var2);
   }

   public Enumeration getUsers() {
      if (this.log != null) {
         this.log.debug("getUsers()");
      }

      if (!this.delegate.getAllowEnumeration()) {
         throw new UnsupportedOperationException("getUsers not supported");
      } else {
         return this.delegate.getUsers();
      }
   }

   public Group getGroup(String var1) {
      if (this.log != null) {
         this.log.debug("getGroup(\"" + var1 + "\")");
      }

      return this.delegate.getGroup(var1);
   }

   public Enumeration getGroups() {
      if (this.log != null) {
         this.log.debug("getGroups()");
      }

      if (!this.delegate.getAllowEnumeration()) {
         throw new UnsupportedOperationException("getGroups not supported");
      } else {
         return this.delegate.getGroups();
      }
   }

   boolean groupContains(String var1, String var2) {
      return this.delegate.groupContains(var1, var2);
   }

   public Hashtable getGroupMembersInternal(String var1) {
      LDAPGroup var2 = (LDAPGroup)this.getGroup(var1);
      if (var2 == null) {
         return null;
      } else {
         Enumeration var3 = this.delegate.groupMembers(var2.getDN());
         Hashtable var4 = new Hashtable();
         if (var3 != null && var3.hasMoreElements()) {
            while(var3.hasMoreElements()) {
               Object var5 = var3.nextElement();
               var4.put(((Principal)var5).getName(), var5);
            }
         }

         return var4;
      }
   }

   public void setDebug(boolean var1) {
      if (var1 && this.log == null) {
         this.log = new LogOutputStream("LDAPRealm");
      }

      if (!var1) {
         this.log = null;
      }

      this.delegate.setDebugLog(this.log);
   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }
}
