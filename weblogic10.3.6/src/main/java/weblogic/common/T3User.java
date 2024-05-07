package weblogic.common;

import java.security.cert.Certificate;
import java.util.Vector;
import weblogic.security.acl.DefaultUserInfoImpl;

/** @deprecated */
public final class T3User extends DefaultUserInfoImpl {
   private static final long serialVersionUID = -4431340916243112533L;
   public static final T3User GUEST = new T3User("guest", "guest");

   public T3User() {
   }

   public T3User(String var1, String var2) {
      super(var1, var2, "weblogic");
   }

   public T3User(Certificate var1) {
      super((String)null, var1, "weblogic");
   }

   public T3User username(String var1) {
      this.setName(var1);
      return this;
   }

   public T3User password(String var1) {
      this.setCredential(var1);
      return this;
   }

   public String getUsername() {
      return this.getName();
   }

   public Vector getCertificates() {
      return this.hasCertificates() ? this.getCertificates() : null;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof T3User)) {
         return false;
      } else {
         T3User var2 = (T3User)var1;
         return this.equals(var2.getName(), var2.getPassword(), var2.getRealmName());
      }
   }

   public boolean equals(String var1, String var2, String var3) {
      String var4 = this.getPassword();
      String var5 = this.getRealmName();
      String var6 = this.getName();
      return var6 != null && var6.length() > 0 && var6.equals(var1) && var4 != null && var4.length() > 0 && var4.equals(var2) && var5 != null && var5.length() > 0 && var5.equals(var3);
   }

   public int hashCode() {
      return (this.getName() == null ? 0 : this.getName().hashCode()) | (this.getPassword() == null ? 0 : this.getPassword().hashCode()) | (this.getRealmName() == null ? 0 : this.getRealmName().hashCode());
   }
}
