package weblogic.servlet.internal.session;

import java.util.Enumeration;

public interface SessionInternal {
   String getInternalId();

   String getIdWithServerInfo();

   SessionContext getContext();

   boolean isValid();

   int getConcurrentRequestCount();

   long getLAT();

   void setLastAccessedTime(long var1);

   void setNew(boolean var1);

   void invalidate();

   void invalidate(boolean var1);

   void setVersionId(String var1);

   String getVersionId();

   void setAttribute(String var1, Object var2, boolean var3) throws IllegalStateException, IllegalArgumentException;

   Object getInternalAttribute(String var1) throws IllegalStateException;

   void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException;

   void removeInternalAttribute(String var1) throws IllegalStateException;

   Enumeration getInternalAttributeNames();

   boolean hasStateAttributes();
}
