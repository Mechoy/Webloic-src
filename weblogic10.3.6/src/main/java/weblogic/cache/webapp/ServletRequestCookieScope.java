package weblogic.cache.webapp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;

public class ServletRequestCookieScope implements CacheScope {
   private Map cookies = new HashMap();

   public ServletRequestCookieScope() {
   }

   public ServletRequestCookieScope(HttpServletRequest var1) {
      this.setRequest(var1);
   }

   public void setRequest(HttpServletRequest var1) {
      Cookie[] var2 = var1.getCookies();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         this.cookies.put(var2[var3].getName(), var2[var3].getValue());
      }

   }

   public boolean isReadOnly() {
      return true;
   }

   public Iterator getAttributeNames() throws CacheException {
      return this.cookies.keySet().iterator();
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      throw new CacheException("Servlet request cookie scope is read only, cannot write " + var1 + " = " + var2);
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.cookies.get(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      throw new CacheException("Servlet request cookie scope is read only, cannot remove " + var1);
   }
}
