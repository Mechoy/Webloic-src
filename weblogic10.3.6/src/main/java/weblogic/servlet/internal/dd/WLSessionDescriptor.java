package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.SessionDescriptorMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WLSessionDescriptor extends BaseServletDescriptor implements ToXML, SessionDescriptorMBean {
   private static final long serialVersionUID = 6160172527197013767L;
   private static final String SESSION_PARAM = "session-param";
   private List sessionParams;
   public static final String IDLEN = (new Integer(52)).toString();
   public static final String SESS_TIMEOUT = (new Integer(3600)).toString();
   public static final String INV_INTERVAL_SECS = (new Integer(60)).toString();

   public WLSessionDescriptor() {
      this.sessionParams = new ArrayList();
   }

   public WLSessionDescriptor(List var1) {
      this.sessionParams = var1;
   }

   public WLSessionDescriptor(SessionDescriptorMBean var1) {
      this();
      if (var1 != null) {
         this.setURLRewritingEnabled(var1.isURLRewritingEnabled());
         this.setIDLength(var1.getIDLength());
         this.setCacheSize(var1.getCacheSize());
         this.setCookieComment(var1.getCookieComment());
         this.setCookieDomain(var1.getCookieDomain());
         this.setCookieMaxAgeSecs(var1.getCookieMaxAgeSecs());
         this.setCookieName(var1.getCookieName());
         this.setEncodeSessionIdInQueryParams(var1.isEncodeSessionIdInQueryParams());
         this.setCacheSessionCookie(var1.isCacheSessionCookie());
         this.setCookiePath(var1.getCookiePath());
         this.setInvalidationIntervalSecs(var1.getInvalidationIntervalSecs());
         this.setJDBCConnectionTimeoutSecs(var1.getJDBCConnectionTimeoutSecs());
         this.setPersistentStoreCookieName(var1.getPersistentStoreCookieName());
         this.setPersistentStoreDir(var1.getPersistentStoreDir());
         this.setPersistentStorePool(var1.getPersistentStorePool());
         this.setPersistentStoreType(var1.getPersistentStoreType());
         this.setPersistentStoreTable(var1.getPersistentStoreTable());
         this.setPersistentDataSourceJNDIName(var1.getPersistentDataSourceJNDIName());
         this.setJDBCColumnName_MaxInactiveInterval(var1.getJDBCColumnName_MaxInactiveInterval());
         this.setCookiesEnabled(var1.isCookiesEnabled());
         this.setCookieSecure(var1.isCookieSecure());
         this.setTrackingEnabled(var1.isTrackingEnabled());
         this.setPersistentStoreShared(var1.isPersistentStoreShared());
         this.setSwapIntervalSecs(var1.getSwapIntervalSecs());
         this.setTimeoutSecs(var1.getTimeoutSecs());
         this.setConsoleMainAttribute(var1.getConsoleMainAttribute());
         if (!this.checkMinIDLength(this.getIDLength())) {
            this.setIDLength(8);
         }
      }

   }

   public WLSessionDescriptor(Element var1) throws DOMProcessingException {
      List var2 = DOMUtils.getOptionalElementsByTagName(var1, "session-param");
      Iterator var3 = var2.iterator();
      this.sessionParams = new ArrayList(var2.size());

      while(var3.hasNext()) {
         ParameterDescriptor var4 = new ParameterDescriptor((Element)var3.next());
         String var5 = var4.getParamName();
         if (isValidParam(var5)) {
            this.sessionParams.add(var4);
         } else {
            if (!isDeprecatedParam(var5)) {
               if (var5 != null && var5.length() == 0) {
                  HTTPLogger.logEmptySessionParamName();
               } else {
                  HTTPLogger.logInvalidSessionParamName(var5);
               }

               throw new DOMProcessingException();
            }

            HTTPLogger.logDeprecatedSessionParamName(var5);
         }
      }

      if (!this.checkMinIDLength(this.getIDLength())) {
         this.setIDLength(8);
      }

   }

   private static boolean isValidParam(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.equalsIgnoreCase("CacheSize") || var0.equalsIgnoreCase("ConsoleMainAttribute") || var0.equalsIgnoreCase("CookieComment") || var0.equalsIgnoreCase("CookieDomain") || var0.equalsIgnoreCase("CookieMaxAgeSecs") || var0.equalsIgnoreCase("CookieName") || var0.equalsIgnoreCase("EncodeSessionIdInQueryParams") || var0.equalsIgnoreCase("CacheSessionCookie") || var0.equalsIgnoreCase("CookiePath") || var0.equalsIgnoreCase("CookiesEnabled") || var0.equalsIgnoreCase("CookieSecure") || var0.equalsIgnoreCase("IDLength") || var0.equalsIgnoreCase("InvalidationIntervalSecs") || var0.equalsIgnoreCase("JDBCConnectionTimeoutSecs") || var0.equalsIgnoreCase("JDBCColumnName_MaxInactiveInterval") || var0.equalsIgnoreCase("PersistentStoreCookieName") || var0.equalsIgnoreCase("PersistentStoreDir") || var0.equalsIgnoreCase("PersistentStorePool") || var0.equalsIgnoreCase("PersistentDataSourceJNDIName") || var0.equalsIgnoreCase("PersistentStoreTable") || var0.equalsIgnoreCase("PersistentStoreType") || var0.equalsIgnoreCase("SessionDebuggable") || var0.equalsIgnoreCase("SwapIntervalSecs") || var0.equalsIgnoreCase("TimeoutSecs") || var0.equalsIgnoreCase("TrackingEnabled") || var0.equalsIgnoreCase("URLRewritingEnabled");
      }
   }

   private static boolean isDefaultValue(String var0, String var1) {
      if (var0 == null) {
         return false;
      } else {
         return var0.equalsIgnoreCase("CacheSize") && var1.equals("SessionConstants.DEFAULT_CACHE_SIZE") || var0.equalsIgnoreCase("CookieComment") && var1.equals("WebLogic Server Session Tracking Cookie") || var0.equalsIgnoreCase("CookieMaxAgeSecs") && var1.equals("-1") || var0.equalsIgnoreCase("CookieName") && var1.equals("JSESSIONID") || var0.equalsIgnoreCase("CookiePath") && var1.equals("/") || var0.equalsIgnoreCase("CookiesEnabled") && var1.equals("true") || var0.equalsIgnoreCase("IDLength") && var1.equals(IDLEN) || var0.equalsIgnoreCase("InvalidationIntervalSecs") && var1.equals(INV_INTERVAL_SECS) || var0.equalsIgnoreCase("JDBCConnectionTimeoutSecs") && var1.equals("120") || var0.equalsIgnoreCase("PersistentStoreCookieName") && var1.equals("WLCOOKIE") || var0.equalsIgnoreCase("PersistentStoreDir") && var1.equals("session_db") || var0.equalsIgnoreCase("PersistentStoreType") && var1.equals("memory") || var0.equalsIgnoreCase("TimeoutSecs") && var1.equals(SESS_TIMEOUT) || var0.equalsIgnoreCase("TrackingEnabled") && var1.equals("true") || var0.equalsIgnoreCase("URLRewritingEnabled") && var1.equals("true");
      }
   }

   private static boolean isDeprecatedParam(String var0) {
      return var0.equalsIgnoreCase("PersistentStoreShared");
   }

   public boolean isURLRewritingEnabled() {
      return "true".equalsIgnoreCase(this.getProp("URLRewritingEnabled", "true"));
   }

   public void setURLRewritingEnabled(boolean var1) {
      if (var1 != this.isURLRewritingEnabled()) {
         this.setProp("URLRewritingEnabled", "" + var1);
         this.firePropertyChange("URLRewritingEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public String getCookiePath() {
      return this.getProp("CookiePath", "/");
   }

   public void setCookiePath(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         if (var1.charAt(0) != '/') {
            var1 = '/' + var1;
         }

         String var2 = this.getCookiePath();
         this.setProp("CookiePath", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("CookiePath", var2, var1);
         }

      }
   }

   public int getIDLength() {
      return this.getIntProp("IDLength", 52);
   }

   public void setIDLength(int var1) {
      int var2 = this.getIDLength();
      if (var2 != var1) {
         this.setProp("IDLength", "" + var1);
         this.firePropertyChange("IDLength", new Integer(var2), new Integer(var1));
      }

   }

   private boolean checkMinIDLength(int var1) {
      if (var1 < 8) {
         HTTPLogger.logError("ERROR", "IDLength cannot be less than 8 as declared in deployment descriptor. Initializing with minimum value 8.");
         return false;
      } else {
         return true;
      }
   }

   public int getCacheSize() {
      return this.getIntProp("CacheSize", 1024);
   }

   public void setCacheSize(int var1) {
      int var2 = this.getCacheSize();
      this.setProp("CacheSize", "" + var1);
      if (var2 != var1) {
         this.firePropertyChange("CacheSize", new Integer(var2), new Integer(var1));
      }

   }

   public String getCookieComment() {
      return this.getProp("CookieComment", "WebLogic Server Session Tracking Cookie");
   }

   public void setCookieComment(String var1) {
      String var2 = this.getCookieComment();
      this.setProp("CookieComment", var1);
      if (!comp(var2, var1)) {
         this.firePropertyChange("CookieComment", var2, var1);
      }

   }

   public String getCookieDomain() {
      return this.getProp("CookieDomain", (String)null);
   }

   public void setCookieDomain(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getCookieDomain();
         if (!comp(var2, var1)) {
            this.setProp("CookieDomain", var1);
            this.firePropertyChange("CookieDomain", var2, var1);
         }
      }

   }

   public int getCookieMaxAgeSecs() {
      return this.getIntProp("CookieMaxAgeSecs", -1);
   }

   public void setCookieMaxAgeSecs(int var1) {
      int var2 = this.getCookieMaxAgeSecs();
      if (var1 != var2) {
         this.setProp("CookieMaxAgeSecs", "" + var1);
         this.firePropertyChange("CookieMaxAgeSecs", new Integer(var2), new Integer(var1));
      }

   }

   public String getCookieName() {
      return this.getProp("CookieName", "JSESSIONID", true);
   }

   public void setCookieName(String var1) {
      var1 = isNull(var1);
      if (var1 == null) {
         var1 = "JSESSIONID";
      }

      String var2 = this.getCookieName();
      if (!comp(var2, var1)) {
         this.setProp("CookieName", var1);
         this.firePropertyChange("CookieName", var2, var1);
      }

   }

   public boolean isEncodeSessionIdInQueryParams() {
      return "true".equalsIgnoreCase(this.getProp("EncodeSessionIdInQueryParams", "false"));
   }

   public void setEncodeSessionIdInQueryParams(boolean var1) {
      this.setProp("EncodeSessionIdInQueryParams", "" + var1);
   }

   public void setCacheSessionCookie(boolean var1) {
      this.setProp("CacheSessionCookie", "" + var1);
   }

   public boolean isCacheSessionCookie() {
      return "true".equalsIgnoreCase(this.getProp("CacheSessionCookie", "true"));
   }

   public void setCookieSecure(boolean var1) {
      if (var1 != this.isCookieSecure()) {
         this.setProp("CookieSecure", "" + var1);
         this.firePropertyChange("CookieSecure", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isCookieSecure() {
      return "true".equalsIgnoreCase(this.getProp("CookieSecure", "false"));
   }

   public int getInvalidationIntervalSecs() {
      return this.getIntProp("InvalidationIntervalSecs", 60);
   }

   public void setInvalidationIntervalSecs(int var1) {
      int var2 = this.getInvalidationIntervalSecs();
      if (var2 != var1) {
         this.setProp("InvalidationIntervalSecs", "" + var1);
         this.firePropertyChange("InvalidationIntervalSecs", new Integer(var2), new Integer(var1));
      }

   }

   public int getJDBCConnectionTimeoutSecs() {
      return this.getIntProp("JDBCConnectionTimeoutSecs", 120);
   }

   public void setJDBCConnectionTimeoutSecs(int var1) {
      var1 = Math.max(var1, 0);
      int var2 = this.getJDBCConnectionTimeoutSecs();
      if (var1 != var2) {
         this.setProp("JDBCConnectionTimeoutSecs", "" + var1);
         this.firePropertyChange("JDBCConnectionTimeoutSecs", new Integer(var2), new Integer(var1));
      }

   }

   public void setPersistentStoreCookieName(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getPersistentStoreCookieName();
         this.setProp("PersistentStoreCookieName", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentStoreCookieName", var2, var1);
         }
      }

   }

   public String getPersistentStoreCookieName() {
      return this.getProp("PersistentStoreCookieName", "WLCOOKIE", true);
   }

   public String getPersistentStoreDir() {
      return this.getProp("PersistentStoreDir", "session_db", true);
   }

   public void setPersistentStoreDir(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getPersistentStoreDir();
         this.setProp("PersistentStoreDir", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentStoreDir", var2, var1);
         }
      }

   }

   public String getPersistentStorePool() {
      return this.getProp("PersistentStorePool", (String)null);
   }

   public void setPersistentStorePool(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getPersistentStorePool();
         this.setProp("PersistentStorePool", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentStorePool", var2, var1);
         }
      }

   }

   public String getPersistentDataSourceJNDIName() {
      return this.getProp("PersistentDataSourceJNDIName", (String)null);
   }

   public void setPersistentDataSourceJNDIName(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getPersistentDataSourceJNDIName();
         this.setProp("PersistentDataSourceJNDIName", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentDataSourceJNDIName", var2, var1);
         }
      }

   }

   public String getPersistentStoreTable() {
      return this.getProp("PersistentStoreTable", "wl_servlet_sessions", true);
   }

   public void setPersistentStoreTable(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getPersistentStoreTable();
         this.setProp("PersistentStoreTable", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentStoreTable", var2, var1);
         }
      }

   }

   public String getJDBCColumnName_MaxInactiveInterval() {
      return this.getProp("JDBCColumnName_MaxInactiveInterval", "wl_max_inactive_interval", true);
   }

   public void setJDBCColumnName_MaxInactiveInterval(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         this.setProp("JDBCColumnName_MaxInactiveInterval", var1);
      }

   }

   public boolean isPersistentStoreShared() {
      return "true".equalsIgnoreCase(this.getProp("PersistentStoreShared", "false"));
   }

   public void setPersistentStoreShared(boolean var1) {
      if (var1 != this.isPersistentStoreShared()) {
         this.setProp("PersistentStoreShared", "" + var1);
         this.firePropertyChange("PersistentStoreShared", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isDebugEnabled() {
      return "true".equalsIgnoreCase(this.getProp("SessionDebuggable", "false"));
   }

   public void setDebugEnabled(boolean var1) {
      if (var1 != this.isDebugEnabled()) {
         this.setProp("SessionDebuggable", "" + var1);
         this.firePropertyChange("SessionDebuggable", new Boolean(!var1), new Boolean(var1));
      }

   }

   private static boolean validStoreType(String var0) {
      return "memory".equals(var0) || "file".equals(var0) || "replicated".equals(var0) || "replicated_if_clustered".equals(var0) || "cookie".equals(var0) || "jdbc".equals(var0);
   }

   public String getPersistentStoreType() {
      String var1 = this.getProp("PersistentStoreType", "memory");
      return !validStoreType(var1) ? "memory" : var1;
   }

   public void setPersistentStoreType(String var1) {
      var1 = isNull(var1);
      if (var1 != null && validStoreType(var1)) {
         String var2 = this.getPersistentStoreType();
         this.setProp("PersistentStoreType", var1);
         if (!comp(var2, var1)) {
            this.firePropertyChange("PersistentStoreType", var2, var1);
         }
      }

   }

   public int getSwapIntervalSecs() {
      return this.getIntProp("SwapIntervalSecs", 10);
   }

   public void setSwapIntervalSecs(int var1) {
      int var2 = this.getSwapIntervalSecs();
      if (var2 != var1) {
         this.setProp("SwapIntervalSecs", "" + var1);
         this.firePropertyChange("SwapIntervalSecs", new Integer(var2), new Integer(var1));
      }

   }

   public int getTimeoutSecs() {
      return this.getIntProp("TimeoutSecs", 3600);
   }

   public void setTimeoutSecs(int var1) {
      int var2 = this.getTimeoutSecs();
      if (var1 != var2) {
         this.setProp("TimeoutSecs", "" + var1);
         this.firePropertyChange("TimeoutSecs", new Integer(var2), new Integer(var1));
      }

   }

   public String getConsoleMainAttribute() {
      return this.getProp("ConsoleMainAttribute", (String)null);
   }

   public void setConsoleMainAttribute(String var1) {
      var1 = isNull(var1);
      if (var1 != null) {
         String var2 = this.getConsoleMainAttribute();
         if (!comp(var2, var1)) {
            this.setProp("ConsoleMainAttribute", var1);
            this.firePropertyChange("ConsoleMainAttribute", var2, var1);
         }
      }

   }

   public void setCookiesEnabled(boolean var1) {
      if (var1 != this.isCookiesEnabled()) {
         this.setProp("CookiesEnabled", "" + var1);
         this.firePropertyChange("CookiesEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isCookiesEnabled() {
      return "true".equalsIgnoreCase(this.getProp("CookiesEnabled", "true"));
   }

   public void setTrackingEnabled(boolean var1) {
      if (var1 != this.isTrackingEnabled()) {
         this.setProp("TrackingEnabled", "" + var1);
         this.firePropertyChange("TrackingEnabled", new Boolean(!var1), new Boolean(var1));
      }

   }

   public boolean isTrackingEnabled() {
      return "true".equalsIgnoreCase(this.getProp("TrackingEnabled", "true"));
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   private String getProp(String var1, String var2) {
      return this.getProp(var1, var2, false);
   }

   private String getProp(String var1, String var2, boolean var3) {
      Iterator var4 = this.sessionParams.iterator();

      ParameterDescriptor var5;
      do {
         if (!var4.hasNext()) {
            if (var2 != null && var2.length() > 0) {
               var5 = new ParameterDescriptor();
               var5.setParamName(var1);
               var5.setParamValue(var2);
               this.sessionParams.add(var5);
            }

            return var2;
         }

         var5 = (ParameterDescriptor)var4.next();
      } while(!var1.equalsIgnoreCase(var5.getParamName()));

      var5.setParamName(var1);
      if (!var3) {
         return var5.getParamValue();
      } else {
         String var6 = var5.getParamValue();
         var6 = isNull(var6);
         if (var6 != null) {
            return var6;
         } else {
            if (var2 != null) {
               var5.setParamValue(var2);
            }

            return var2;
         }
      }
   }

   private void setProp(String var1, String var2) {
      Iterator var3 = this.sessionParams.iterator();

      ParameterDescriptor var4;
      do {
         if (!var3.hasNext()) {
            var4 = new ParameterDescriptor();
            var4.setParamName(var1);
            var4.setParamValue(var2);
            this.sessionParams.add(var4);
            return;
         }

         var4 = (ParameterDescriptor)var3.next();
      } while(!var1.equalsIgnoreCase(var4.getParamName()));

      var4.setParamName(var1);
      var4.setParamValue(var2);
   }

   private int getIntProp(String var1, int var2) {
      try {
         String var3 = this.getProp(var1, "" + var2);
         var3 = var3.trim();
         return Integer.parseInt(var3);
      } catch (NumberFormatException var4) {
         return var2;
      }
   }

   private static String isNull(String var0) {
      if (var0 == null) {
         return null;
      } else {
         var0 = var0.trim();
         return var0.length() > 0 ? var0 : null;
      }
   }

   public String toXML(int var1) {
      boolean var2 = false;
      String var3 = "";
      if (this.sessionParams != null && this.sessionParams.size() > 0) {
         Iterator var4 = this.sessionParams.iterator();

         while(var4.hasNext()) {
            ParameterDescriptor var5 = (ParameterDescriptor)var4.next();
            if (!isDefaultValue(var5.getParamName(), var5.getParamValue())) {
               if (!var2) {
                  var3 = var3 + this.indentStr(var1) + "<session-descriptor>\n";
                  var1 += 2;
                  var2 = true;
               }

               var3 = var3 + this.indentStr(var1) + "<session-param>\n";
               var1 += 2;
               var3 = var3 + this.indentStr(var1) + "<param-name>" + var5.getParamName() + "</param-name>\n";
               var3 = var3 + this.indentStr(var1) + "<param-value>" + var5.getParamValue() + "</param-value>\n";
               var1 -= 2;
               var3 = var3 + this.indentStr(var1) + "</session-param>\n";
            }
         }

         if (var2) {
            var1 -= 2;
            var3 = var3 + this.indentStr(var1) + "</session-descriptor>\n";
         }
      }

      return var3;
   }
}
