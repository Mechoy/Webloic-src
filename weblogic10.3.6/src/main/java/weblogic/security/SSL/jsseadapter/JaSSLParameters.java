package weblogic.security.SSL.jsseadapter;

import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

final class JaSSLParameters {
   private String[] enabledCipherSuites = new String[0];
   private String[] enabledProtocols = new String[0];
   private boolean needClientAuth = false;
   private boolean wantClientAuth = false;
   private boolean useClientMode = true;
   private boolean enableSessionCreation = true;
   private boolean unencryptedNullCipherEnabled = false;
   private Set<String> NULL_CIPHERS = new HashSet();
   private Set<String> ANON_CIPHERS = new HashSet();

   public JaSSLParameters(JaSSLParameters var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SSLParameters.");
      } else {
         this.setEnabledCipherSuites(var1.enabledCipherSuites);
         this.setEnabledProtocols(var1.enabledProtocols);
         this.setWantClientAuth(var1.wantClientAuth);
         this.setNeedClientAuth(var1.needClientAuth);
         this.setUseClientMode(var1.useClientMode);
         this.setEnableSessionCreation(var1.enableSessionCreation);
      }
   }

   public JaSSLParameters(SSLContext var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null javax.net.ssl.SSLContext.");
      } else {
         SSLEngine var2 = var1.createSSLEngine();
         this.setEnabledCipherSuites(var2.getEnabledCipherSuites());
         this.setEnabledProtocols(var2.getEnabledProtocols());
         this.setWantClientAuth(var2.getWantClientAuth());
         this.setNeedClientAuth(var2.getNeedClientAuth());
      }
   }

   public String[] getEnabledCipherSuites() {
      return cloneArray(this.enabledCipherSuites);
   }

   public void setEnabledCipherSuites(String[] var1) {
      this.enabledCipherSuites = cloneArray(var1);
   }

   public String[] getEnabledProtocols() {
      return cloneArray(this.enabledProtocols);
   }

   public void setEnabledProtocols(String[] var1) {
      this.enabledProtocols = cloneArray(var1);
   }

   public void setNeedClientAuth(boolean var1) {
      this.needClientAuth = var1;
   }

   public boolean getNeedClientAuth() {
      return this.needClientAuth;
   }

   public void setWantClientAuth(boolean var1) {
      this.wantClientAuth = var1;
   }

   public boolean getWantClientAuth() {
      return this.wantClientAuth;
   }

   public void setUseClientMode(boolean var1) {
      this.useClientMode = var1;
   }

   public boolean getUseClientMode() {
      return this.useClientMode;
   }

   public void setEnableSessionCreation(boolean var1) {
      this.enableSessionCreation = var1;
   }

   public boolean getEnableSessionCreation() {
      return this.enableSessionCreation;
   }

   public void configureSslEngine(SSLEngine var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Expected non-null SSLEngine.");
      } else {
         String[] var2 = this.unencryptedNullCipherEnabled ? JaSSLSupport.combineCiphers(this.getEnabledCipherSuites(), this.getNullCiphers(var1)) : this.getEnabledCipherSuites();
         if (JaSSLSupport.isAnonymousCipherAllowed()) {
            var2 = JaSSLSupport.combineCiphers(var2, this.getAnonymousCiphers(var1));
         }

         var1.setEnabledCipherSuites(var2);
         String[] var3 = JaSSLSupport.getEnabledProtocols(var1.getSupportedProtocols());
         String[] var4 = var3 != null ? var3 : this.getEnabledProtocols();
         var1.setEnabledProtocols(var4);
         var1.setEnableSessionCreation(this.getEnableSessionCreation());
         var1.setUseClientMode(this.getUseClientMode());
         var1.setWantClientAuth(this.getWantClientAuth());
         var1.setNeedClientAuth(this.getNeedClientAuth());
      }
   }

   private static String[] cloneArray(String[] var0) {
      if (null == var0) {
         return var0;
      } else {
         String[] var1 = new String[var0.length];
         System.arraycopy(var0, 0, var1, 0, var0.length);
         return var1;
      }
   }

   void setUnencryptedNullCipherEnabled(boolean var1) {
      this.unencryptedNullCipherEnabled = var1;
   }

   synchronized String[] getNullCiphers(SSLEngine var1) {
      if (var1 != null && this.NULL_CIPHERS.size() == 0) {
         this.selectCiphers(var1, "_NULL_", this.NULL_CIPHERS);
      }

      String[] var2 = new String[this.NULL_CIPHERS.size()];
      return (String[])this.NULL_CIPHERS.toArray(var2);
   }

   synchronized String[] getAnonymousCiphers(SSLEngine var1) {
      if (var1 != null && this.ANON_CIPHERS.size() == 0) {
         this.selectCiphers(var1, "_anon_", this.ANON_CIPHERS);
      }

      String[] var2 = new String[this.ANON_CIPHERS.size()];
      return (String[])this.ANON_CIPHERS.toArray(var2);
   }

   private void selectCiphers(SSLEngine var1, String var2, Set<String> var3) {
      if (var1 instanceof JaSSLEngine) {
         var1 = ((JaSSLEngine)var1).getDelegate();
      }

      String[] var4 = var1.getSupportedCipherSuites();
      String[] var5 = var4;
      int var6 = var4.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         if (var8.toUpperCase().indexOf(var2.toUpperCase()) > -1) {
            var3.add(var8);
         }
      }

   }
}
