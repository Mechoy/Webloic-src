package weblogic.servlet.internal.session;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import weblogic.servlet.internal.WebAppServletContext;

public final class SharedSessionData implements HttpSession, SessionInternal {
   private HttpSession session;
   private WebAppServletContext context;
   private String versionId;

   public SharedSessionData(HttpSession var1, WebAppServletContext var2) {
      this.session = var1;
      this.context = var2;
      this.versionId = this.context.getVersionId();
   }

   public HttpSession getSession() {
      return this.session;
   }

   public long getCreationTime() {
      return this.session.getCreationTime();
   }

   public String getId() {
      return this.session.getId();
   }

   public long getLastAccessedTime() {
      return this.session.getLastAccessedTime();
   }

   public ServletContext getServletContext() {
      return this.context;
   }

   public void setMaxInactiveInterval(int var1) {
      this.session.setMaxInactiveInterval(var1);
   }

   public int getMaxInactiveInterval() {
      return this.session.getMaxInactiveInterval();
   }

   public HttpSessionContext getSessionContext() {
      return this.session.getSessionContext();
   }

   public Object getAttribute(String var1) {
      return this.session.getAttribute(var1);
   }

   public Object getValue(String var1) {
      return this.session.getValue(var1);
   }

   public Enumeration getAttributeNames() {
      return this.session.getAttributeNames();
   }

   public String[] getValueNames() {
      return this.session.getValueNames();
   }

   public void setAttribute(String var1, Object var2) {
      this.session.setAttribute(var1, var2);
   }

   public void putValue(String var1, Object var2) {
      this.session.putValue(var1, var2);
   }

   public void removeAttribute(String var1) {
      this.session.removeAttribute(var1);
   }

   public void removeValue(String var1) {
      this.session.removeValue(var1);
   }

   public boolean isNew() {
      return this.session.isNew();
   }

   public void invalidate() {
      this.session.invalidate();
   }

   public void invalidate(boolean var1) {
      ((SessionInternal)this.session).invalidate(var1);
   }

   public void setAttribute(String var1, Object var2, boolean var3) {
      ((SessionInternal)this.session).setAttribute(var1, var2, var3);
   }

   public String getInternalId() {
      return ((SessionInternal)this.session).getInternalId();
   }

   public String getIdWithServerInfo() {
      return ((SessionInternal)this.session).getIdWithServerInfo();
   }

   public SessionContext getContext() {
      return ((SessionInternal)this.session).getContext();
   }

   public boolean isValid() {
      return ((SessionInternal)this.session).isValid();
   }

   public int getConcurrentRequestCount() {
      return ((SessionInternal)this.session).getConcurrentRequestCount();
   }

   public long getLAT() {
      return ((SessionInternal)this.session).getLAT();
   }

   public void setLastAccessedTime(long var1) {
      ((SessionInternal)this.session).setLastAccessedTime(var1);
   }

   public void setNew(boolean var1) {
      ((SessionInternal)this.session).setNew(var1);
   }

   public Object getInternalAttribute(String var1) throws IllegalStateException {
      return ((SessionInternal)this.session).getInternalAttribute(var1);
   }

   public void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      ((SessionInternal)this.session).setInternalAttribute(var1, var2);
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      ((SessionInternal)this.session).removeInternalAttribute(var1);
   }

   public Enumeration getInternalAttributeNames() {
      return ((SessionInternal)this.session).getInternalAttributeNames();
   }

   public boolean hasStateAttributes() {
      return ((SessionInternal)this.session).hasStateAttributes();
   }

   public String getVersionId() {
      return this.versionId;
   }

   public final void setVersionId(String var1) {
      this.versionId = var1;
   }
}
