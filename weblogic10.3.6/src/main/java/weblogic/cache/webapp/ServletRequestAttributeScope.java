package weblogic.cache.webapp;

import java.util.Iterator;
import javax.servlet.ServletRequest;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.EnumerationIterator;

public class ServletRequestAttributeScope implements CacheScope {
   private ServletRequest request;

   public ServletRequestAttributeScope() {
   }

   public ServletRequestAttributeScope(ServletRequest var1) {
      this.setRequest(var1);
   }

   public void setRequest(ServletRequest var1) {
      this.request = var1;
   }

   public ServletRequest getRequest() {
      return this.request;
   }

   public boolean isReadOnly() {
      return false;
   }

   public Iterator getAttributeNames() throws CacheException {
      return new EnumerationIterator(this.request.getAttributeNames());
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      this.request.setAttribute(var1, var2);
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.request.getAttribute(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      this.request.removeAttribute(var1);
   }
}
