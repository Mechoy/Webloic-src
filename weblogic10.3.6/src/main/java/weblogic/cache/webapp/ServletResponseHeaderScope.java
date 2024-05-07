package weblogic.cache.webapp;

import java.util.Iterator;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.servlet.internal.ServletResponseImpl;

public class ServletResponseHeaderScope implements CacheScope {
   private ServletResponseImpl response;

   public ServletResponseHeaderScope() {
   }

   public ServletResponseHeaderScope(ServletResponseImpl var1) {
      this.setResponse(var1);
   }

   public void setResponse(ServletResponseImpl var1) {
      this.response = var1;
   }

   public ServletResponseImpl getResponse() {
      return this.response;
   }

   public boolean isReadOnly() {
      return false;
   }

   public Iterator getAttributeNames() throws CacheException {
      throw new CacheException("You cannot list the headers in the repsponse");
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      this.response.setHeader(var1, var2.toString());
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.response == null ? null : this.response.getHeader(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
   }
}
