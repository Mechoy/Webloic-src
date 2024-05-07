package weblogic.wsee.connection.transport.https;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.Properties;
import weblogic.net.http.HttpsURLConnection;
import weblogic.security.SSL.TrustManager;
import weblogic.security.utils.KeyStoreUtils;
import weblogic.wsee.connection.transport.TransportInfo;
import weblogic.wsee.server.EncryptionUtil;
import weblogic.wsee.util.Verbose;

public class WlsSSLAdapter implements SSLAdapter, Serializable {
   private static final long serialVersionUID = 1L;
   private static final boolean verbose = Verbose.isVerbose(WlsSSLAdapter.class);
   private String _keystore;
   private char[] _keystorePassword;
   private String _keystoreType;
   private String _keyAlias;
   private char[] _keyPassword;
   private transient KeyStore clientKeyStore = null;
   private transient PrivateKey key = null;
   private transient Certificate[] certs = null;
   private transient TrustManager tm = null;

   public WlsSSLAdapter() {
      Properties var1 = System.getProperties();
      String var2 = var1.getProperty("java.protocol.handler.pkgs");
      if (var2 == null) {
         var2 = "weblogic.net";
      } else if (var2.indexOf("weblogic.net") == -1) {
         var2 = var2 + "|weblogic.net";
      }

      var1.put("java.protocol.handler.pkgs", var2);
      System.setProperties(var1);
   }

   public HttpURLConnection openConnection(URL var1, Proxy var2, TransportInfo var3) throws IOException {
      if (verbose) {
         Verbose.log((Object)("openConnection(" + var1 + (var2 != null ? ", " + var2 : "") + ")"));
      }

      try {
         URLConnection var5 = null;
         if (var2 == null) {
            var5 = var1.openConnection();
         } else {
            var5 = var1.openConnection(var2);
         }

         if (!(var5 instanceof HttpsURLConnection)) {
            String var6 = "----  openConnection returned class='" + var5.getClass().getName() + "'." + "  We were expecting an HTTPS connection object: weblogic.net.http.HttpsURLConnection" + "\nCheck that your SSL environment is setup correctly.";
            throw new IOException(var6);
         } else {
            HttpsURLConnection var4 = (HttpsURLConnection)var5;
            if (this.certs != null && this.key != null) {
               var4.loadLocalIdentity(this.certs, this.key);
            }

            if (this.tm != null) {
               var4.setTrustManager(this.tm);
            }

            return var4;
         }
      } catch (IOException var7) {
         if (verbose) {
            var7.printStackTrace();
         }

         throw var7;
      }
   }

   public void setClientCert(String var1, char[] var2) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException {
      this._keyAlias = var1;
      this._keyPassword = var2;
      this.initClientCert();
   }

   private void initClientCert() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
      if (this.clientKeyStore != null) {
         KeyStore.PrivateKeyEntry var1 = (KeyStore.PrivateKeyEntry)this.clientKeyStore.getEntry(this._keyAlias, new KeyStore.PasswordProtection(this._keyPassword));
         if (var1 != null) {
            this.key = var1.getPrivateKey();
         }

         this.certs = this.clientKeyStore.getCertificateChain(this._keyAlias);
      }

   }

   public void setKeystore(String var1, char[] var2, String var3) {
      this._keystore = var1;
      this._keystorePassword = var2;
      this._keystoreType = var3;
      this.initClientKeyStore();
   }

   private void initClientKeyStore() {
      this.clientKeyStore = KeyStoreUtils.load(KeyStoreUtils.getFile(this._keystore), this._keystorePassword, this._keystoreType);
   }

   public void setTrustManager(TrustManager var1) {
      this.tm = var1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject(this._keystore);
      this.writeEncryptedField(this._keystorePassword, var1);
      var1.writeObject(this._keystoreType);
      var1.writeObject(this._keyAlias);
      this.writeEncryptedField(this._keyPassword, var1);
   }

   private void writeEncryptedField(Object var1, ObjectOutputStream var2) throws IOException {
      if (var1 == null) {
         var2.writeInt(-1);
         var2.writeObject(var1);
      } else {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         ObjectOutputStream var4 = new ObjectOutputStream(var3);
         var4.writeObject(var1);
         var4.flush();
         byte[] var5 = var3.toByteArray();
         var5 = EncryptionUtil.encrypt(var5);
         var2.writeInt(var5.length);
         var2.write(var5);
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this._keystore = (String)var1.readObject();
      this._keystorePassword = (char[])((char[])this.readEncryptedField(var1));
      this._keystoreType = (String)var1.readObject();
      this._keyAlias = (String)var1.readObject();
      this._keyPassword = (char[])((char[])this.readEncryptedField(var1));
      this.initialize();
   }

   private Object readEncryptedField(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if (var2 <= 0) {
         return var1.readObject();
      } else {
         byte[] var3 = new byte[var2];
         var1.readFully(var3);
         var3 = EncryptionUtil.decrypt(var3);
         ByteArrayInputStream var4 = new ByteArrayInputStream(var3);
         ObjectInputStream var5 = new ObjectInputStream(var4);
         return var5.readObject();
      }
   }

   private void initialize() {
      if (this._keystore != null) {
         this.initClientKeyStore();
      }

      if (this._keyAlias != null) {
         try {
            this.initClientCert();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }

   }

   static {
      if (verbose) {
         Verbose.log((Object)"WlsSSLAdapter: verbose output enabled");
      }

   }
}
