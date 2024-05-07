package weblogic.security.utils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.security.auth.Subject;
import weblogic.entitlement.engine.ESubject;
import weblogic.security.SecurityLogger;
import weblogic.security.principal.RealmAdapterUser;
import weblogic.security.spi.InvalidPrincipalException;
import weblogic.security.spi.WLSGroup;

public class ESubjectImpl implements ESubject {
   public static final String EVERYONE_GROUP = "everyone";
   public static final String USERS_GROUP = "users";
   private Subject subject;
   private Map users;
   private Map groups;
   private Map roles;
   private Map notGroups;
   private RealmAdapterUser realmAdapter;

   public ESubjectImpl(Subject var1) {
      this(var1, (Map)null);
   }

   public ESubjectImpl(Subject var1, Map var2) {
      if (var1 == null) {
         throw new NullPointerException(SecurityLogger.getNullSubject());
      } else {
         this.subject = var1;
         this.users = new HashMap();
         this.groups = new HashMap();
         this.roles = var2;
         Iterator var3 = var1.getPrincipals().iterator();

         while(var3.hasNext()) {
            Principal var4 = (Principal)var3.next();
            String var5 = var4.getName();
            if (var4 instanceof WLSGroup) {
               this.groups.put(var5, var5);
            } else {
               this.users.put(var5, var5);
               if (this.realmAdapter == null && var4 instanceof RealmAdapterUser) {
                  this.realmAdapter = (RealmAdapterUser)var4;
                  this.notGroups = new HashMap();
               }
            }
         }

         if (this.users.size() > 0 || this.groups.size() > 0) {
            this.groups.put("users", "users");
         }

         this.groups.put("everyone", "everyone");
      }
   }

   public Subject getSubject() {
      return this.subject;
   }

   public boolean isUser(String var1) {
      return this.users.containsKey(var1);
   }

   public boolean isMemberOf(String var1) {
      boolean var2 = this.groups.containsKey(var1);
      if (this.realmAdapter != null && !var2 && !this.notGroups.containsKey(var1)) {
         try {
            var2 = this.realmAdapter.isUserInGroup(var1);
         } catch (InvalidPrincipalException var4) {
            var2 = false;
         } catch (SecurityException var5) {
            var2 = false;
         }

         if (var2) {
            this.groups.put(var1, var1);
         } else {
            this.notGroups.put(var1, var1);
         }
      }

      return var2;
   }

   public boolean isInRole(String var1) {
      return this.roles != null && this.roles.containsKey(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[Users: ").append(this.toString(this.users.keySet().iterator()));
      var1.append("|Groups: ").append(this.toString(this.groups.keySet().iterator()));
      var1.append("|Roles: ").append(this.roles == null ? "null" : "not null");
      var1.append("]");
      return var1.toString();
   }

   private String toString(Iterator var1) {
      StringBuffer var2 = new StringBuffer();
      if (var1.hasNext()) {
         var2.append(var1.next());
      }

      while(var1.hasNext()) {
         var2.append(",").append(var1.next());
      }

      return var2.toString();
   }
}
