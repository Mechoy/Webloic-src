package weblogic.servlet.internal;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import weblogic.servlet.security.Utils;
import weblogic.utils.StringUtils;
import weblogic.utils.enumerations.EmptyEnumerator;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.http.BytesToString;

public final class RequestHeaders {
   private ArrayList headerNames;
   private ArrayList headerValues;
   private final ArrayList cookies = new ArrayList();
   private int contentLength = -1;
   private String contentType;
   private String proxyAuthType;
   private String remoteUser = null;
   private String host;
   private int port = -1;
   private String transferEncoding;
   private boolean isChunked = false;
   private String acceptLanguages;
   private String expect;
   private boolean contentLengthSet = false;
   private String connection;
   private String userAgent;
   private boolean wlProxyFound = false;
   private String authorization;
   private String xWeblogicRequestClusterInfo;
   private String xWeblogicClusterHash;
   private String xWeblogicClusterList;
   private String xWeblogicLoad;
   private String xWeblogicJvmId;
   private String xWeblogicForceJvmId;
   private String xWeblogicKeepaliveSecs;

   public void reset() {
      this.headerNames = null;
      this.headerValues = null;
      this.cookies.clear();
      this.contentLength = -1;
      this.contentType = null;
      this.proxyAuthType = null;
      this.host = null;
      this.port = -1;
      this.transferEncoding = null;
      this.isChunked = false;
      this.acceptLanguages = null;
      this.expect = null;
      this.contentLengthSet = false;
      this.remoteUser = null;
      this.connection = null;
      this.userAgent = null;
      this.wlProxyFound = false;
      this.authorization = null;
      this.xWeblogicRequestClusterInfo = null;
      this.xWeblogicClusterHash = null;
      this.xWeblogicClusterList = null;
      this.xWeblogicLoad = null;
      this.xWeblogicJvmId = null;
      this.xWeblogicForceJvmId = null;
      this.xWeblogicKeepaliveSecs = null;
   }

   public String getHeader(String var1, String var2) {
      if (this.headerNames == null) {
         return null;
      } else {
         int var3 = 0;

         for(int var4 = this.headerNames.size(); var3 < var4; ++var3) {
            if (var1.equalsIgnoreCase((String)this.headerNames.get(var3))) {
               return this.getHeaderValue(var3, var2);
            }
         }

         return null;
      }
   }

   public byte[] getHeaderAsBytes(String var1) {
      if (this.headerNames == null) {
         return null;
      } else {
         int var2 = 0;

         for(int var3 = this.headerNames.size(); var2 < var3; ++var2) {
            if (var1.equalsIgnoreCase((String)this.headerNames.get(var2))) {
               return (byte[])((byte[])this.headerValues.get(var2));
            }
         }

         return null;
      }
   }

   public Enumeration getHeaderNames() {
      ArrayList var1 = this.getHeaderNamesAsArrayList();
      return (Enumeration)(var1 == null ? new EmptyEnumerator() : new IteratorEnumerator(var1.iterator()));
   }

   public Enumeration getHeaders(String var1, String var2) {
      if (this.headerNames == null) {
         return new EmptyEnumerator();
      } else {
         ArrayList var3 = new ArrayList(16);
         int var4 = 0;

         for(int var5 = this.headerNames.size(); var4 < var5; ++var4) {
            if (var1.equalsIgnoreCase((String)this.headerNames.get(var4))) {
               var3.add(this.getHeaderValue(var4, var2));
            }
         }

         return new IteratorEnumerator(var3.iterator());
      }
   }

   String getHeaderValue(int var1, String var2) {
      byte[] var3 = (byte[])((byte[])this.headerValues.get(var1));
      return var3 == null ? null : BytesToString.newString(var3, var2);
   }

   public ArrayList getHeaderNamesAsArrayList() {
      return this.headerNames;
   }

   public ArrayList getHeaderValuesAsArrayList() {
      return this.headerValues;
   }

   public void setHeaders(ArrayList var1, ArrayList var2) {
      this.headerNames = var1;
      this.headerValues = var2;

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         String var4 = (String)this.headerNames.get(var3);
         byte[] var5 = (byte[])((byte[])this.headerValues.get(var3));
         this.processHeader(var4, var5);
      }

   }

   private void processHeader(String var1, byte[] var2) {
      int var3 = var1.length();
      String var4;
      switch (var3) {
         case 4:
            if (ServletRequestImpl.eq(var1, "Host", 4)) {
               var4 = StringUtils.getString(var2);
               int var5 = var4.lastIndexOf(58);
               if (var5 < 0) {
                  this.host = Utils.encodeXSS(var4);
                  this.port = -1;
               } else {
                  if (var5 != 0) {
                     this.host = Utils.encodeXSS(var4.substring(0, var5));
                  }

                  if (var5 < var4.length() - 1) {
                     try {
                        this.port = Integer.parseInt(var4.substring(var5 + 1));
                     } catch (NumberFormatException var8) {
                        this.port = -1;
                     }
                  }
               }

               return;
            }
         case 5:
         case 7:
         case 8:
         case 10:
         case 18:
         case 19:
         case 20:
         case 21:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         default:
            break;
         case 6:
            if (ServletRequestImpl.eq(var1, "Cookie", 6)) {
               this.cookies.add(var2);
               return;
            }

            if (ServletRequestImpl.eq(var1, "Expect", 6)) {
               this.expect = StringUtils.getString(var2);
               return;
            }
            break;
         case 9:
            if (ServletRequestImpl.eq(var1, "AUTH_TYPE", 9)) {
               this.proxyAuthType = StringUtils.getString(var2);
               return;
            }
            break;
         case 11:
            if (ServletRequestImpl.eq(var1, "REMOTE_USER", 11)) {
               this.remoteUser = StringUtils.getString(var2);
               return;
            }
            break;
         case 12:
            if (ServletRequestImpl.eq(var1, "Content-Type", 12)) {
               this.contentType = StringUtils.getString(var2);
               return;
            }
            break;
         case 13:
            if (ServletRequestImpl.eq(var1, "Authorization", 13)) {
               this.authorization = StringUtils.getString(var2);
               return;
            }
            break;
         case 14:
            if (ServletRequestImpl.eq(var1, "Content-Length", 14)) {
               if (this.contentLengthSet) {
                  throw new IllegalArgumentException("Duplicate content-length header. This request may be malicious.");
               }

               try {
                  var4 = StringUtils.getString(var2);
                  this.contentLength = Integer.parseInt(var4.trim());
                  this.contentLengthSet = true;
               } catch (NumberFormatException var7) {
               }

               return;
            }
            break;
         case 15:
            if (ServletRequestImpl.eq(var1, "Proxy-Auth-Type", 15)) {
               this.proxyAuthType = StringUtils.getString(var2);
               return;
            }

            if (ServletRequestImpl.eq(var1, "Accept-Language", 15)) {
               this.acceptLanguages = StringUtils.getString(var2);
               return;
            }

            if (ServletRequestImpl.eq(var1, "X-WebLogic-Load", 15)) {
               this.xWeblogicLoad = StringUtils.getString(var2);
               return;
            }
            break;
         case 16:
            if (ServletRequestImpl.eq(var1, "X-WebLogic-JVMID", 16)) {
               this.xWeblogicJvmId = StringUtils.getString(var2);
               return;
            }
            break;
         case 17:
            if (ServletRequestImpl.eq(var1, "Transfer-Encoding", 17)) {
               this.transferEncoding = StringUtils.getString(var2);
               this.isChunked = ServletRequestImpl.eq(this.transferEncoding, "chunked", 7);
               return;
            }

            if (ServletRequestImpl.eq(var1, "Proxy-Remote-User", 17)) {
               this.remoteUser = StringUtils.getString(var2);
               return;
            }
            break;
         case 22:
            if (ServletRequestImpl.eq(var1, "X-WebLogic-Force-JVMID", 22)) {
               this.xWeblogicForceJvmId = StringUtils.getString(var2);
               return;
            }
            break;
         case 23:
            if (ServletRequestImpl.eq(var1, "X-WebLogic-Cluster-Hash", 23)) {
               this.xWeblogicClusterHash = StringUtils.getString(var2);
               return;
            }

            if (ServletRequestImpl.eq(var1, "X-WebLogic-Cluster-List", 23)) {
               this.xWeblogicClusterList = StringUtils.getString(var2);
               return;
            }
            break;
         case 24:
            if (ServletRequestImpl.eq(var1, "X-WebLogic-KeepAliveSecs", 24)) {
               this.xWeblogicKeepaliveSecs = StringUtils.getString(var2);
               return;
            }
            break;
         case 30:
            if (ServletRequestImpl.eq(var1, "X-WebLogic-Request-ClusterInfo", 30)) {
               this.xWeblogicRequestClusterInfo = StringUtils.getString(var2);
               return;
            }
      }

      if (StringUtils.indexOfIgnoreCase(var1, "WL-Proxy-") >= 0) {
         this.wlProxyFound = true;
      }

   }

   public int getContentLength() {
      return this.contentLength;
   }

   public void ignoreContentLength() {
      this.contentLength = -1;
   }

   public String getContentType() {
      return this.contentType;
   }

   public String getProxyAuthType() {
      return this.proxyAuthType;
   }

   String getRemoteUser() {
      return this.remoteUser;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String getTransferEncoding() {
      return this.transferEncoding;
   }

   public boolean isChunked() {
      return this.isChunked;
   }

   public String getAcceptLanguages() {
      return this.acceptLanguages;
   }

   public String getExpect() {
      return this.expect;
   }

   public List getCookieHeaders() {
      return this.cookies;
   }

   public String getConnection() {
      return this.connection;
   }

   public String getUserAgent() {
      return this.userAgent;
   }

   public boolean isWlProxyFound() {
      return this.wlProxyFound;
   }

   public String getAuthorization() {
      return this.authorization;
   }

   public String getXWeblogicRequestClusterInfo() {
      return this.xWeblogicRequestClusterInfo;
   }

   public String getXWeblogicClusterHash() {
      return this.xWeblogicClusterHash;
   }

   public String getXWeblogicClusterList() {
      return this.xWeblogicClusterList;
   }

   public String getXWeblogicLoad() {
      return this.xWeblogicLoad;
   }

   public String getXWeblogicForceJvmId() {
      return this.xWeblogicForceJvmId;
   }

   public String getXWeblogicJvmId() {
      return this.xWeblogicJvmId;
   }

   public String getXWeblogicKeepaliveSecs() {
      return this.xWeblogicKeepaliveSecs;
   }
}
