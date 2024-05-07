package weblogic.security.utils;

import weblogic.management.configuration.EmbeddedLDAPMBean;
import weblogic.management.utils.LDAPServerMBean;

public final class LDAPServerInfo {
   private EmbeddedLDAPMBean embeddedMBean = null;
   private LDAPServerMBean serverMBean = null;
   private boolean embeddedLDAPInit = false;
   private boolean useLocalConnection;
   private String host;
   private int port;
   private boolean SSLEnabled;
   private String principal;
   private String credential;
   private boolean cacheEnabled;
   private int cacheSize;
   private int cacheTTL;
   private int connectionRetryLimit;

   public LDAPServerInfo(boolean var1, String var2, int var3, boolean var4, String var5, String var6, boolean var7, int var8, int var9, int var10) {
      this.useLocalConnection = var1;
      this.host = var2;
      this.port = var3;
      this.SSLEnabled = var4;
      this.principal = var5;
      this.credential = var6;
      this.cacheEnabled = var7;
      this.cacheSize = var8;
      this.cacheTTL = var9;
   }

   public LDAPServerInfo(boolean var1, LDAPServerMBean var2) {
      this.useLocalConnection = var1;
      this.serverMBean = var2;
   }

   public LDAPServerInfo(boolean var1, String var2, int var3, boolean var4, String var5, int var6, EmbeddedLDAPMBean var7) {
      this.useLocalConnection = var1;
      this.host = var2;
      this.port = var3;
      this.SSLEnabled = var4;
      this.principal = var5;
      this.connectionRetryLimit = var6;
      this.embeddedMBean = var7;
   }

   public boolean getUseLocalConnection() {
      return this.useLocalConnection;
   }

   public String getHost() {
      return this.serverMBean != null ? this.serverMBean.getHost() : this.host;
   }

   public int getPort() {
      return this.serverMBean != null ? this.serverMBean.getPort() : this.port;
   }

   public boolean isSSLEnabled() {
      return this.serverMBean != null ? this.serverMBean.isSSLEnabled() : this.SSLEnabled;
   }

   public String getPrincipal() {
      return this.serverMBean != null ? this.serverMBean.getPrincipal() : this.principal;
   }

   public String getCredential() {
      if (this.embeddedMBean != null) {
         return this.embeddedMBean.getCredential();
      } else {
         return this.serverMBean != null ? this.serverMBean.getCredential() : this.credential;
      }
   }

   public boolean getCacheEnabled() {
      if (this.embeddedMBean != null) {
         return this.embeddedMBean.isCacheEnabled();
      } else {
         return this.serverMBean != null ? this.serverMBean.isCacheEnabled() : this.cacheEnabled;
      }
   }

   public int getCacheSize() {
      if (this.embeddedMBean != null) {
         return this.embeddedMBean.getCacheSize();
      } else {
         return this.serverMBean != null ? this.serverMBean.getCacheSize() : this.cacheSize;
      }
   }

   public int getCacheTTL() {
      if (this.embeddedMBean != null) {
         return this.embeddedMBean.getCacheTTL();
      } else {
         return this.serverMBean != null ? this.serverMBean.getCacheTTL() : this.cacheTTL;
      }
   }

   public int getConnectionRetryLimit() {
      return this.serverMBean != null ? this.serverMBean.getConnectionRetryLimit() : this.connectionRetryLimit;
   }
}
