package weblogic.cache.webapp;

import java.util.Iterator;
import javax.servlet.ServletRequest;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.EnumerationIterator;

public class ServletRequestParameterScope implements CacheScope {
   private ServletRequest request;

   public ServletRequestParameterScope() {
   }

   public ServletRequestParameterScope(ServletRequest var1) {
      this.setRequest(var1);
   }

   public void setRequest(ServletRequest var1) {
      this.request = var1;
   }

   public ServletRequest getRequest() {
      return this.request;
   }

   public boolean isReadOnly() {
      return true;
   }

   public Iterator getAttributeNames() throws CacheException {
      return new EnumerationIterator(this.request.getParameterNames());
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      throw new CacheException("Servlet request parameter scope is read only, cannot write " + var1 + " = " + var2);
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.request.getParameter(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      throw new CacheException("Servlet request parameter scope is read only, cannot remove " + var1);
   }
}
