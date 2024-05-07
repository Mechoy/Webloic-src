package weblogic.cache.webapp;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.EnumerationIterator;

public class ServletRequestHeaderScope implements CacheScope {
   private HttpServletRequest request;

   public ServletRequestHeaderScope() {
   }

   public ServletRequestHeaderScope(HttpServletRequest var1) {
      this.setRequest(var1);
   }

   public void setRequest(HttpServletRequest var1) {
      this.request = var1;
   }

   public HttpServletRequest getRequest() {
      return this.request;
   }

   public boolean isReadOnly() {
      return true;
   }

   public Iterator getAttributeNames() throws CacheException {
      return new EnumerationIterator(this.request.getHeaderNames());
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      throw new CacheException("Read only scope");
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.request.getHeader(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      throw new CacheException("You cannot remove a header once set: " + var1);
   }
}
