package weblogic.security.acl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.cert.X509Certificate;
import java.util.Vector;

/** @deprecated */
public class DefaultUserInfoImpl implements UserInfo, Serializable {
   private static final long serialVersionUID = -419061834872911373L;
   protected String realmName;
   protected String id;
   protected transient String password;
   protected Vector certificates;
   private byte[] obfuscatedPassword;
   private static final String CHAR_ENCODING = "UTF-8";

   public DefaultUserInfoImpl() {
      this.realmName = null;
      this.id = null;
      this.password = null;
      this.certificates = new Vector(0);
      this.obfuscatedPassword = null;
   }

   public String getRealmName() {
      return this.realmName;
   }

   public String getName() {
      return this.id;
   }

   public void setName(String var1) {
      this.id = var1;
   }

   public String toString() {
      return "{" + this.getRealmName() + "," + this.getName() + "}";
   }

   public DefaultUserInfoImpl(String var1, Object var2) {
      this(var1, var2, "weblogic");
   }

   public DefaultUserInfoImpl(String var1, Object var2, String var3) {
      this.realmName = null;
      this.id = null;
      this.password = null;
      this.certificates = new Vector(0);
      this.obfuscatedPassword = null;
      this.id = var1;
      this.realmName = var3;
      this.setCredential(var2);
      if (this.id == null && this.certificates != null && this.certificates.size() > 0) {
         this.id = ((X509Certificate)this.certificates.elementAt(0)).getSubjectDN().getName();
      }

   }

   protected void setCredential(Object var1) {
      if (var1 instanceof String) {
         this.password = (String)var1;
         this.obfuscatedPassword = obfuscate(this.password);
      } else if (var1 instanceof X509Certificate) {
         this.certificates.addElement(var1);
      } else if (var1 instanceof Object[]) {
         Object[] var2 = (Object[])((Object[])var1);

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.setCredential(var2[var3]);
         }
      }

   }

   public boolean hasPassword() {
      return this.password != null;
   }

   public String getPassword() {
      return this.password;
   }

   public boolean hasCertificates() {
      return this.certificates.size() > 0;
   }

   /** @deprecated */
   public Vector getCertificates() {
      return this.certificates;
   }

   private static byte[] obfuscate(String var0) {
      try {
         byte[] var1 = var0.getBytes("UTF-8");
         byte[] var2 = flipBytes(var1);
         return var2;
      } catch (UnsupportedEncodingException var3) {
         System.out.println("The impossible happened: 1");
         return null;
      }
   }

   private static String unobfuscate(byte[] var0) {
      try {
         byte[] var1 = flipBytes(var0);
         String var2 = new String(var1, "UTF-8");
         return var2;
      } catch (UnsupportedEncodingException var3) {
         System.out.println("The impossible happened: 2");
         return null;
      }
   }

   private static byte[] flipBytes(byte[] var0) {
      if (var0 == null) {
         return null;
      } else {
         byte[] var1 = new byte[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
            var1[var2] = (byte)(var1[var2] ^ 255);
         }

         return var1;
      }
   }

   public int hashCode() {
      return (this.realmName == null ? 0 : this.realmName.hashCode()) ^ (this.id == null ? 0 : this.id.hashCode()) ^ (this.password == null ? 0 : this.password.hashCode()) ^ (this.certificates == null ? 0 : this.certificates.hashCode());
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof DefaultUserInfoImpl) {
         boolean var10000;
         label21: {
            DefaultUserInfoImpl var2 = (DefaultUserInfoImpl)var1;
            if (this.id == null) {
               if (var2.id != null) {
                  break label21;
               }
            } else if (!this.id.equals(var2.id)) {
               break label21;
            }

            if (this.equalsInAllButName(var2)) {
               var10000 = true;
               return var10000;
            }
         }

         var10000 = false;
         return var10000;
      } else {
         return false;
      }
   }

   public boolean equalsInAllButName(DefaultUserInfoImpl var1) {
      boolean var10000;
      label30: {
         String var2 = var1.getRealmName();
         String var3 = var1.getPassword();
         Vector var4 = var1.getCertificates();
         if (this.realmName == null) {
            if (var2 != null) {
               break label30;
            }
         } else if (!this.realmName.equals(var2)) {
            break label30;
         }

         if (this.password == null) {
            if (var3 != null) {
               break label30;
            }
         } else if (!this.password.equals(var3)) {
            break label30;
         }

         if (this.equalsCertificatesOnly(var1)) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   private boolean equalsCertificatesOnly(DefaultUserInfoImpl var1) {
      Vector var2 = var1.getCertificates();
      int var3 = this.certificates.size();
      int var4 = var2.size();
      if (var3 == 0 && var4 == 0) {
         return true;
      } else if (var3 != var4) {
         return false;
      } else {
         Vector var5 = (Vector)this.certificates.clone();
         Vector var6 = (Vector)var2.clone();
         return var5.equals(var6);
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.password = this.obfuscatedPassword != null ? unobfuscate(this.obfuscatedPassword) : null;
   }
}
