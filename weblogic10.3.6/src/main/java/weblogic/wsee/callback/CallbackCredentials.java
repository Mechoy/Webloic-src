package weblogic.wsee.callback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import weblogic.apache.xerces.impl.dv.util.Base64;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionService;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.wsee.connection.transport.https.HttpsTransportInfo;
import weblogic.wsee.security.bst.ClientBSTCredentialProvider;
import weblogic.wsee.security.bst.StubPropertyBSTCredProv;
import weblogic.wsee.security.unt.ClientUNTCredentialProvider;
import weblogic.xml.crypto.wss.provider.CredentialProvider;

public class CallbackCredentials implements Serializable {
   static final long serialVersionUID = 3L;
   private static final AuthenticatedSubject kernelID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static ClearOrEncryptedService _coes;
   private static final String DEFAULT_KeyStoreType = "JKS";
   private String _keyStoreLocation;
   private String _keyStorePassword;
   private String _keyStoreType = "JKS";
   private String _trustStoreLocation;
   private String _trustStorePassword;
   private String _trustStoreType = "JKS";
   private String _keyAlias;
   private X509Certificate _serverCert;
   private String _keyPassword;
   private String _password = null;
   private String _username = null;
   private boolean _useClientCerts;

   public void setUsername(String var1) {
      this._username = var1;
   }

   public void setPassword(String var1) {
      this._password = var1;
   }

   public String getUsername() {
      return this._username;
   }

   public String getPassword() {
      return this._password;
   }

   public void setKeystore(String var1, String var2, String var3) {
      if (var1 != null && var2 != null) {
         this._keyStoreLocation = var1;
         this._keyStorePassword = var2;
         if (var3 != null) {
            this._keyStoreType = var3;
         }

      } else {
         throw new IllegalArgumentException("Key store location and password must both be specified in a service control");
      }
   }

   public void setTruststore(String var1, String var2, String var3) {
      if (var1 != null && var2 != null) {
         this._trustStoreLocation = var1;
         this._trustStorePassword = var2;
         if (var3 != null) {
            this._trustStoreType = var3;
         }

      } else {
         throw new IllegalArgumentException("Trust store location and password must both be specified in a service control");
      }
   }

   public void setClientCert(String var1, String var2) {
      this._keyAlias = var1;
      this._keyPassword = var2;
   }

   public void setServerCert(X509Certificate var1) {
      this._serverCert = var1;
   }

   public void useClientKeySSL(boolean var1) {
      this._useClientCerts = var1;
   }

   public boolean useClientKeySSL() {
      return this._useClientCerts;
   }

   public String getHttpBasicAuth() {
      String var1 = this._username + ":" + this._password;
      byte[] var2 = Base64.encode(var1.getBytes());
      return "Basic " + new String(var2);
   }

   public List<CredentialProvider> getMessageCredentialProviders() {
      ArrayList var1 = new ArrayList();
      if (this._password != null || this._username != null) {
         var1.add(new ClientUNTCredentialProvider(this._username != null ? this._username.getBytes() : null, this._password != null ? this._password.getBytes() : null));
      }

      try {
         if (this._keyStoreLocation != null && this._keyStorePassword != null && this._keyAlias != null) {
            var1.add(new ClientBSTCredentialProvider(this._keyStoreLocation, this._keyStorePassword, this._keyAlias, this._keyPassword, this._keyStoreType, this._serverCert));
         } else if (this._serverCert != null) {
            var1.add(new StubPropertyBSTCredProv(this._serverCert, this._serverCert));
         }

         return var1;
      } catch (Exception var3) {
         throw new RuntimeException("Error processing keystore.", var3);
      }
   }

   HttpsTransportInfo getHttpsTransportInfo() {
      if (!this._useClientCerts) {
         return null;
      } else {
         HttpsTransportInfo var1 = new HttpsTransportInfo();
         var1.setTrustManagers(new X509TrustManager[]{new RelaxedX509TrustManager()});
         KeyStore var3;
         if (this._keyStoreLocation != null && this._keyStorePassword != null) {
            try {
               KeyManagerFactory var2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
               var3 = KeyStore.getInstance(this._keyStoreType);
               var3.load(new FileInputStream(this._keyStoreLocation), this._keyStorePassword.toCharArray());
               String var4 = this._keyPassword;
               if (var4 == null) {
                  var4 = this._keyStorePassword;
               }

               if (this._keyAlias == null) {
                  var2.init(var3, var4.toCharArray());
               } else {
                  Certificate[] var5 = var3.getCertificateChain(this._keyAlias);
                  if (var5 == null) {
                     throw new SecurityException("No such key with alias '" + this._keyAlias + "' in key-store '" + this._keyStoreLocation + "'");
                  }

                  Key var6 = var3.getKey(this._keyAlias, var4.toCharArray());
                  KeyStore var7 = KeyStore.getInstance(this._keyStoreType);
                  var7.load((InputStream)null, this._keyStorePassword.toCharArray());
                  var7.setKeyEntry(this._keyAlias, var6, var4.toCharArray(), var5);
                  var2.init(var7, var4.toCharArray());
               }

               var1.setKeyManagers(var2.getKeyManagers());
            } catch (Exception var9) {
               throw new SecurityException("Cannot load key-store '" + this._keyStoreLocation + "' " + var9);
            }
         }

         if (this._trustStoreLocation != null && this._trustStorePassword != null) {
            try {
               TrustManagerFactory var10 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
               var3 = KeyStore.getInstance(this._trustStoreType);
               var3.load(new FileInputStream(this._trustStoreLocation), this._trustStorePassword.toCharArray());
               var10.init(var3);
               var1.setTrustManagers(var10.getTrustManagers());
            } catch (Exception var8) {
               throw new SecurityException("Cannot load trust-store '" + this._trustStoreLocation + "' " + var8);
            }
         }

         return var1;
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      synchronized(this) {
         String var3 = this._keyStoreLocation;
         String var4 = this._keyStorePassword;
         String var5 = this._keyStoreType;
         String var6 = this._trustStoreLocation;
         String var7 = this._trustStorePassword;
         String var8 = this._trustStoreType;
         String var9 = this._password;
         String var10 = this._username;
         String var11 = this._keyAlias;
         String var12 = this._keyPassword;
         if (this._keyStoreLocation != null) {
            this._keyStoreLocation = _coes.encrypt(this._keyStoreLocation);
         }

         if (this._keyStorePassword != null) {
            this._keyStorePassword = _coes.encrypt(this._keyStorePassword);
         }

         if (this._keyStoreType != null) {
            this._keyStoreType = _coes.encrypt(this._keyStoreType);
         }

         if (this._trustStoreLocation != null) {
            this._trustStoreLocation = _coes.encrypt(this._trustStoreLocation);
         }

         if (this._trustStorePassword != null) {
            this._trustStorePassword = _coes.encrypt(this._trustStorePassword);
         }

         if (this._trustStoreType != null) {
            this._trustStoreType = _coes.encrypt(this._trustStoreType);
         }

         if (this._password != null) {
            this._password = _coes.encrypt(this._password);
         }

         if (this._username != null) {
            this._username = _coes.encrypt(this._username);
         }

         if (this._keyAlias != null) {
            this._keyAlias = _coes.encrypt(this._keyAlias);
         }

         if (this._keyPassword != null) {
            this._keyPassword = _coes.encrypt(this._keyPassword);
         }

         var1.defaultWriteObject();
         this._keyStoreLocation = var3;
         this._keyStorePassword = var4;
         this._keyStoreType = var5;
         this._trustStoreLocation = var6;
         this._trustStorePassword = var7;
         this._trustStoreType = var8;
         this._password = var9;
         this._username = var10;
         this._keyAlias = var11;
         this._keyPassword = var12;
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this._keyStoreLocation != null) {
         this._keyStoreLocation = _coes.decrypt(this._keyStoreLocation);
      }

      if (this._keyStoreType != null) {
         this._keyStoreType = _coes.decrypt(this._keyStoreType);
      }

      if (this._keyStorePassword != null) {
         this._keyStorePassword = _coes.decrypt(this._keyStorePassword);
      }

      if (this._trustStoreLocation != null) {
         this._trustStoreLocation = _coes.decrypt(this._trustStoreLocation);
      }

      if (this._trustStoreType != null) {
         this._trustStoreType = _coes.decrypt(this._trustStoreType);
      }

      if (this._trustStorePassword != null) {
         this._trustStorePassword = _coes.decrypt(this._trustStorePassword);
      }

      if (this._password != null) {
         this._password = _coes.decrypt(this._password);
      }

      if (this._username != null) {
         this._username = _coes.decrypt(this._username);
      }

      if (this._keyPassword != null) {
         this._keyPassword = _coes.decrypt(this._keyPassword);
      }

      if (this._keyAlias != null) {
         this._keyAlias = _coes.decrypt(this._keyAlias);
      }

   }

   static {
      try {
         SecurityServiceManager.runAs(kernelID, kernelID, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               EncryptionService var1 = SerializedSystemIni.getEncryptionService();
               CallbackCredentials._coes = new ClearOrEncryptedService(var1);
               return null;
            }
         });
      } catch (PrivilegedActionException var1) {
      }

   }

   private static class RelaxedX509TrustManager implements X509TrustManager {
      private RelaxedX509TrustManager() {
      }

      public boolean isClientTrusted(X509Certificate[] var1) {
         return true;
      }

      public boolean isServerTrusted(X509Certificate[] var1) {
         return true;
      }

      public X509Certificate[] getAcceptedIssuers() {
         return null;
      }

      public void checkClientTrusted(X509Certificate[] var1, String var2) {
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) {
      }

      // $FF: synthetic method
      RelaxedX509TrustManager(Object var1) {
         this();
      }
   }
}
