package weblogic.security.acl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** @deprecated */
public class DefaultUserImpl extends User implements Serializable {
   private DefaultUserImplUserInfo userInfo = null;
   private BasicRealm realm = null;

   public DefaultUserImpl() {
      super((String)null);
   }

   public DefaultUserImpl(String var1, Object var2, BasicRealm var3) {
      super(var1);
      this.userInfo = new DefaultUserImplUserInfo(var1, var2, var3.getName());
      this.realm = var3;
   }

   public BasicRealm getRealm() {
      return this.realm;
   }

   public Object getCredential(Object var1) {
      if (this.realm.getAclOwner(var1) != null && this.userInfo instanceof DefaultUserInfoImpl) {
         DefaultUserImplUserInfo var2 = this.userInfo;
         return var2.hasCertificates() ? var2.getCertificates() : var2.getPassword();
      } else {
         return null;
      }
   }

   protected void setCredential(Object var1) {
      this.userInfo.changeCredential(var1);
   }

   public boolean hasMatchingInfo(UserInfo var1) {
      if (var1 instanceof DefaultUserInfoImpl && this.userInfo instanceof DefaultUserInfoImpl) {
         DefaultUserInfoImpl var2 = (DefaultUserInfoImpl)var1;
         DefaultUserImplUserInfo var3 = this.userInfo;
         if (this.realm.getName() != null && this.realm.getName().equals(var1.getRealmName())) {
            return var3.getPassword() != null && var3.getName() != null && var3.getName().equals(var2.getName()) && var3.getPassword().equals(var2.getPassword());
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this.userInfo);
      var1.writeObject(this.realm.getName());
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.userInfo = (DefaultUserImplUserInfo)var1.readObject();
      this.realm = Realm.getRealm((String)var1.readObject());
   }

   private class DefaultUserImplUserInfo extends DefaultUserInfoImpl {
      private static final long serialVersionUID = 7058785679087969048L;

      DefaultUserImplUserInfo(String var2, Object var3, String var4) {
         super(var2, var3, var4);
      }

      void changeCredential(Object var1) {
         this.setCredential(var1);
      }
   }
}
