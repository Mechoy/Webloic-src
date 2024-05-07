package weblogic.wsee.connection.transport.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Proxy;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.server.EncryptionUtil;
import weblogic.wsee.util.MimeHeadersUtil;

public class HttpTransportInfo implements TransportInfo, Serializable {
   private static final long serialVersionUID = 5812892933940605683L;
   private byte[] username = null;
   private byte[] password = null;
   private byte[] proxyUsername = null;
   private byte[] proxyPassword = null;
   private transient Proxy proxy = null;
   private int chunkLen = -1;
   private boolean passwordsEncrypted = false;

   public byte[] getUsername() {
      return this.username;
   }

   public void setUsername(byte[] var1) {
      this.username = var1;
   }

   public byte[] getPassword() {
      if (this.passwordsEncrypted) {
         this.decryptPasswords();
      }

      return this.password;
   }

   public void setPassword(byte[] var1) {
      if (this.passwordsEncrypted) {
         this.decryptPasswords();
      }

      this.password = var1;
   }

   public byte[] getProxyUsername() {
      return this.proxyUsername;
   }

   public void setProxyUsername(byte[] var1) {
      this.proxyUsername = var1;
   }

   public byte[] getProxyPassword() {
      if (this.passwordsEncrypted) {
         this.decryptPasswords();
      }

      return this.proxyPassword;
   }

   public void setProxyPassword(byte[] var1) {
      if (this.passwordsEncrypted) {
         this.decryptPasswords();
      }

      this.proxyPassword = var1;
   }

   public Proxy getProxy() {
      if (this.proxy == null) {
         this.proxy = MimeHeadersUtil.getProxyFromSysProps();
      }

      return this.proxy;
   }

   public void setProxy(Proxy var1) {
      this.proxy = var1;
   }

   public void setChunkedStreamingMode(int var1) {
      this.chunkLen = var1;
   }

   int getChunkedStreamingMode() {
      return this.chunkLen;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (!this.passwordsEncrypted) {
         this.encryptPasswords();
      }

      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.passwordsEncrypted) {
         this.decryptPasswords();
      }

   }

   private void encryptPasswords() {
      if (!this.passwordsEncrypted) {
         if (this.password != null) {
            this.password = EncryptionUtil.encrypt(this.password);
         }

         if (this.proxyPassword != null) {
            this.proxyPassword = EncryptionUtil.encrypt(this.proxyPassword);
         }

         this.passwordsEncrypted = true;
      }
   }

   private void decryptPasswords() {
      if (this.passwordsEncrypted) {
         if (this.password != null) {
            this.password = EncryptionUtil.decrypt(this.password);
         }

         if (this.proxyPassword != null) {
            this.proxyPassword = EncryptionUtil.decrypt(this.proxyPassword);
         }

         this.passwordsEncrypted = false;
      }
   }
}
