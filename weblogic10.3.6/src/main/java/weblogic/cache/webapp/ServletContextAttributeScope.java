package weblogic.cache.webapp;

import java.util.Iterator;
import javax.servlet.ServletContext;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.EnumerationIterator;

public class ServletContextAttributeScope implements CacheScope {
   private ServletContext context;

   public ServletContextAttributeScope() {
   }

   public ServletContextAttributeScope(ServletContext var1) {
      this.setContext(var1);
   }

   public void setContext(ServletContext var1) {
      this.context = var1;
   }

   public ServletContext getContext() {
      return this.context;
   }

   public boolean isReadOnly() {
      return false;
   }

   public Iterator getAttributeNames() throws CacheException {
      return new EnumerationIterator(this.context.getAttributeNames());
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      this.context.setAttribute(var1, var2);
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.context.getAttribute(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      this.context.removeAttribute(var1);
   }
}
