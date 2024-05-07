package weblogic.cache.webapp;

import java.util.Iterator;
import javax.servlet.http.HttpSession;
import weblogic.cache.CacheException;
import weblogic.cache.CacheScope;
import weblogic.cache.EmptyIterator;
import weblogic.cache.EnumerationIterator;
import weblogic.servlet.internal.session.SessionInternal;

public class ServletSessionAttributeScope implements CacheScope {
   private SessionInternal session;

   public ServletSessionAttributeScope() {
   }

   public ServletSessionAttributeScope(HttpSession var1) {
      this.session = (SessionInternal)var1;
   }

   public void setSession(HttpSession var1) {
      this.session = (SessionInternal)var1;
   }

   public boolean isReadOnly() {
      return false;
   }

   public Iterator getAttributeNames() throws CacheException {
      return (Iterator)(this.session == null ? new EmptyIterator() : new EnumerationIterator(this.session.getInternalAttributeNames()));
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      if (this.session == null) {
         throw new CacheException("Tried to set a value in a null session scope");
      } else {
         this.session.setInternalAttribute(var1, var2);
      }
   }

   public Object getAttribute(String var1) throws CacheException {
      return this.session == null ? null : this.session.getInternalAttribute(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      if (this.session != null) {
         this.session.removeInternalAttribute(var1);
      }
   }
}
