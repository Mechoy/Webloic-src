package weblogic.security.ldaprealmv1;

import java.security.AccessController;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Hashtable;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.LDAPRealmMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.AbstractListableRealm;
import weblogic.security.acl.DebuggableRealm;
import weblogic.security.acl.User;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public final class LDAPRealm extends AbstractListableRealm implements DebuggableRealm, weblogic.security.ldaprealm.LDAPRealm {
   private static final int POOL_SIZE = 8;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private LDAPDelegate delegate = new LDAPDelegate(this);
   LogOutputStream log;

   public LDAPRealm() {
      super("LDAP Realm");
      LDAPRealmMBean var1 = (LDAPRealmMBean)ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getRealm().getCachingRealm().getBasicRealm();
      this.setDebug(ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getDebugSecurityRealm());
      SecurityLogger.logLDAPRealmV1DeprecatedWarning();
   }

   public User getUser(String var1) {
      if (this.log != null) {
         this.log.debug("getUser(\"" + var1 + "\")");
      }

      return this.delegate.userExists(var1) ? new LDAPUser(var1, this) : null;
   }

   protected User authUserPassword(String var1, String var2) {
      if (this.log != null) {
         this.log.debug("authUserPassword(\"" + var1 + "\")");
      }

      return this.delegate.authenticate(var1, var2) ? new LDAPUser(var1, this) : null;
   }

   public Group getGroup(String var1) {
      if (this.log != null) {
         this.log.debug("getGroup(\"" + var1 + "\")");
      }

      return this.delegate.getGroup(var1);
   }

   protected Hashtable getGroupMembersInternal(String var1) {
      return this.delegate.getGroupMembers(var1);
   }

   public Enumeration getUsers() {
      if (this.log != null) {
         this.log.debug("getUsers()");
      }

      return this.delegate.getUsers();
   }

   public Enumeration getGroups() {
      if (this.log != null) {
         this.log.debug("getGroups()");
      }

      return this.delegate.getGroups();
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
