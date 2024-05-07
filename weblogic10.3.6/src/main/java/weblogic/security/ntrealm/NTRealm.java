package weblogic.security.ntrealm;

import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.Vector;
import weblogic.kernel.Kernel;
import weblogic.logging.LogOutputStream;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.AbstractListableRealm;
import weblogic.security.acl.BasicRealm;
import weblogic.security.acl.DebuggableRealm;
import weblogic.security.acl.DefaultUserInfoImpl;
import weblogic.security.acl.User;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class NTRealm extends AbstractListableRealm implements DebuggableRealm {
   private String name;
   LogOutputStream log;
   private NTDelegate ntDelegate = new NTDelegate(this);
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public NTRealm() {
      super("NT Realm");
      ServerMBean var1 = null;
      if (Kernel.isServer()) {
         var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      }

      if (var1 != null) {
         this.setDebug(var1.getServerDebug().getDebugSecurityRealm());
      } else {
         this.setDebug(false);
      }

   }

   public User getUser(String var1) {
      if (this.log != null) {
         this.log.debug("getUser(\"" + var1 + "\")");
      }

      return this.ntDelegate.getUser(var1) ? new NTUser(var1) : null;
   }

   protected User authUserPassword(String var1, String var2) {
      NTUser var3 = null;
      if (this.log != null) {
         this.log.debug("authUserPassword(\"" + var1 + ")");
      }

      if (this.ntDelegate.authenticate(var1, var2)) {
         var3 = new NTUser(var1);
      }

      return var3;
   }

   public Group getGroup(String var1) {
      if (this.log != null) {
         this.log.debug("getGroup(" + var1 + ")");
      }

      Enumeration var2 = this.ntDelegate.getGroupNames();
      if (var2 != null) {
         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            if (var3.equals(var1)) {
               return new NTGroup(var1);
            }
         }
      }

      return super.getGroup(var1);
   }

   public Enumeration getUsers() {
      if (this.log != null) {
         this.log.debug("getUsers");
      }

      return new Enumeration() {
         private Enumeration users;

         private Enumeration getUsers() {
            if (this.users == null) {
               this.users = NTRealm.this.ntDelegate.getUserNames();
            }

            return this.users;
         }

         public boolean hasMoreElements() {
            Enumeration var1 = this.getUsers();
            return var1 != null ? var1.hasMoreElements() : false;
         }

         public Object nextElement() {
            String var1 = (String)this.getUsers().nextElement();
            return NTRealm.this.new NTUser(var1);
         }
      };
   }

   public Enumeration getGroups() {
      if (this.log != null) {
         this.log.debug("getGroups");
      }

      return new Enumeration() {
         private Enumeration groups;

         private Enumeration getGroups() {
            if (this.groups == null) {
               this.groups = NTRealm.this.ntDelegate.getGroupNames();
            }

            return this.groups;
         }

         public boolean hasMoreElements() {
            Enumeration var1 = this.getGroups();
            return var1 != null ? var1.hasMoreElements() : false;
         }

         public Object nextElement() {
            String var1 = (String)this.getGroups().nextElement();
            return NTRealm.this.getGroup(var1);
         }
      };
   }

   public void setDebug(boolean var1) {
      if (var1 && this.log == null) {
         this.log = new LogOutputStream("NTRealm");
      }

      if (!var1) {
         this.log = null;
      }

   }

   public LogOutputStream getDebugLog() {
      return this.log;
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length < 2) {
         System.err.println("usage: NTDelegate username password <domain>");
      } else {
         NTRealm var1 = new NTRealm();
         if (var0.length >= 3) {
            var1.ntDelegate.addAltDomains(var0[2]);
         }

         System.out.println("NTDelegate using Primary Domain Controller " + var1.ntDelegate.getDomain());
         int var4;
         if (var1.ntDelegate.getUseAltDomain()) {
            String[] var2 = var1.ntDelegate.getAltDomains();
            if (var2 != null) {
               StringBuffer var3 = new StringBuffer("NTDelegate using Alternate Primary Domain Controllers: ");

               for(var4 = 0; var4 < var2.length; ++var4) {
                  var3.append(var2[var4]);
                  var3.append(" ");
               }

               System.out.println(var3.toString());
            }
         }

         User var6 = var1.authenticate(new DefaultUserInfoImpl(var0[0], var0[1]));
         System.out.println("auth ? " + var6);
         System.out.println("enum users:");
         Enumeration var7 = var1.getUsers();
         var4 = 1;

         Object var5;
         while(var7.hasMoreElements()) {
            var5 = var7.nextElement();
            System.out.println("   #" + var4++ + " '" + var5 + "'");
         }

         System.out.println("enum groups:");
         var7 = var1.getGroups();
         var4 = 1;

         while(var7.hasMoreElements()) {
            var5 = var7.nextElement();
            System.out.println("   #" + var4++ + " '" + var5 + "'");
         }

         System.out.println("enum groups for user: " + var0[0]);
         var7 = var1.ntDelegate.getGroupsForUser(var0[0]);
         var4 = 1;

         while(var7.hasMoreElements()) {
            var5 = var7.nextElement();
            System.out.println("   #" + var4++ + " '" + var5 + "'");
         }

         System.out.println("is user " + var0[0] + " an Administrator? " + var1.ntDelegate.isUserInGroup(var0[0], "Administrators"));
         System.out.println("done");
      }
   }

   public class NTGroup implements Group {
      private String name;

      NTGroup(String var2) {
         this.name = var2;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.name;
      }

      public boolean addMember(Principal var1) {
         throw new SecurityException("Cannot add members to groups in realm " + NTRealm.this.getName());
      }

      public boolean removeMember(Principal var1) {
         throw new SecurityException("Cannot remove members to groups in realm " + NTRealm.this.getName());
      }

      public Enumeration members() {
         if (NTRealm.this.log != null) {
            NTRealm.this.log.debug("members()");
         }

         return new Enumeration() {
            private Enumeration enum_;

            private Enumeration getEnum() {
               if (this.enum_ == null) {
                  Vector var1 = new Vector();
                  Enumeration var2 = NTRealm.this.ntDelegate.getUserNames();

                  while(var2.hasMoreElements()) {
                     String var3 = (String)var2.nextElement();
                     if (NTRealm.this.ntDelegate.isUserInGroup(var3, NTGroup.this.getName())) {
                        var1.addElement(var3);
                     }
                  }

                  this.enum_ = var1.elements();
               }

               return this.enum_;
            }

            public boolean hasMoreElements() {
               return this.getEnum().hasMoreElements();
            }

            public Object nextElement() {
               String var1 = (String)this.getEnum().nextElement();
               return NTRealm.this.getUser(var1);
            }
         };
      }

      public boolean isMember(Principal var1) {
         if (NTRealm.this.log != null) {
            NTRealm.this.log.debug("isMember(\"" + var1.getName() + "\")");
         }

         return NTRealm.this.ntDelegate.isUserInGroup(var1.getName(), this.getName());
      }
   }

   public class NTUser extends User {
      private static final long serialVersionUID = -3724078631048539618L;

      NTUser(String var2) {
         super(var2);
      }

      public BasicRealm getRealm() {
         return NTRealm.this;
      }
   }
}
