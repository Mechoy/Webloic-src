package weblogic.servlet.jsp;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;

public class JspContextWrapper extends PageContext implements VariableResolver {
   private PageContext _invokingContext;
   private Map _pageAttributes;
   private List _nestedVars;
   private List _atBeginVars;
   private List _atEndVars;
   private Map _aliases;
   private Map _originalNestedVars;
   private ELContext _elContext;

   public JspContextWrapper(JspContext var1, List var2, List var3, List var4, Map var5) {
      this._invokingContext = (PageContext)var1;
      this._nestedVars = var2;
      this._atBeginVars = var3;
      this._atEndVars = var4;
      this._pageAttributes = new HashMap(16);
      this._aliases = var5;
      if (var2 != null) {
         this._originalNestedVars = new HashMap(var2.size());
      }

      this.syncBeginTagFile();
   }

   public void initialize(Servlet var1, ServletRequest var2, ServletResponse var3, String var4, boolean var5, int var6, boolean var7) throws IOException, IllegalStateException, IllegalArgumentException {
   }

   public Object getAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return this._pageAttributes.get(var1);
      }
   }

   public Object getAttribute(String var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var2 == 1 ? this._pageAttributes.get(var1) : this._invokingContext.getAttribute(var1, var2);
      }
   }

   public void setAttribute(String var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (var2 != null) {
            this._pageAttributes.put(var1, var2);
         } else {
            this.removeAttribute(var1, 1);
         }

      }
   }

   public void setAttribute(String var1, Object var2, int var3) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (var3 == 1) {
            if (var2 != null) {
               this._pageAttributes.put(var1, var2);
            } else {
               this.removeAttribute(var1, 1);
            }
         } else {
            this._invokingContext.setAttribute(var1, var2, var3);
         }

      }
   }

   public Object findAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         Object var2 = this._pageAttributes.get(var1);
         if (var2 == null) {
            var2 = this._invokingContext.getAttribute(var1, 2);
            if (var2 == null) {
               if (this.getSession() != null) {
                  var2 = this._invokingContext.getAttribute(var1, 3);
               }

               if (var2 == null) {
                  var2 = this._invokingContext.getAttribute(var1, 4);
               }
            }
         }

         return var2;
      }
   }

   public void removeAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this._pageAttributes.remove(var1);
         this._invokingContext.removeAttribute(var1, 2);
         if (this.getSession() != null) {
            this._invokingContext.removeAttribute(var1, 3);
         }

         this._invokingContext.removeAttribute(var1, 4);
      }
   }

   public void removeAttribute(String var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         if (var2 == 1) {
            this._pageAttributes.remove(var1);
         } else {
            this._invokingContext.removeAttribute(var1, var2);
         }

      }
   }

   public int getAttributesScope(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return this._pageAttributes.get(var1) != null ? 1 : this._invokingContext.getAttributesScope(var1);
      }
   }

   public Enumeration getAttributeNamesInScope(int var1) {
      return var1 == 1 ? Collections.enumeration(this._pageAttributes.keySet()) : this._invokingContext.getAttributeNamesInScope(var1);
   }

   public void release() {
      this._invokingContext.release();
   }

   public JspWriter getOut() {
      return this._invokingContext.getOut();
   }

   public HttpSession getSession() {
      return this._invokingContext.getSession();
   }

   public Object getPage() {
      return this._invokingContext.getPage();
   }

   public ServletRequest getRequest() {
      return this._invokingContext.getRequest();
   }

   public ServletResponse getResponse() {
      return this._invokingContext.getResponse();
   }

   public Exception getException() {
      return this._invokingContext.getException();
   }

   public ServletConfig getServletConfig() {
      return this._invokingContext.getServletConfig();
   }

   public ServletContext getServletContext() {
      return this._invokingContext.getServletContext();
   }

   public void forward(String var1) throws ServletException, IOException {
      this._invokingContext.forward(var1);
   }

   public void include(String var1) throws ServletException, IOException {
      this._invokingContext.include(var1);
   }

   public void include(String var1, boolean var2) throws ServletException, IOException {
      this.include(var1, false);
   }

   public VariableResolver getVariableResolver() {
      return this;
   }

   public ELContext getELContext() {
      if (this._elContext == null) {
         PageContext var1;
         for(var1 = this._invokingContext; var1 instanceof JspContextWrapper; var1 = ((JspContextWrapper)var1)._invokingContext) {
         }

         PageContextImpl var2 = (PageContextImpl)var1;
         this._elContext = var2.getJspApplicationContextImpl().createELContext(this);
      }

      return this._elContext;
   }

   public BodyContent pushBody() {
      return this._invokingContext.pushBody();
   }

   public JspWriter pushBody(Writer var1) {
      return this._invokingContext.pushBody(var1);
   }

   public JspWriter popBody() {
      return this._invokingContext.popBody();
   }

   public ExpressionEvaluator getExpressionEvaluator() {
      return this._invokingContext.getExpressionEvaluator();
   }

   public void handlePageException(Exception var1) throws IOException, ServletException {
      this.handlePageException((Throwable)var1);
   }

   public void handlePageException(Throwable var1) throws IOException, ServletException {
      this._invokingContext.handlePageException(var1);
   }

   public Object resolveVariable(String var1) throws ELException {
      Object var2 = null;
      ELContext var3 = this.getELContext();

      try {
         var2 = var3.getELResolver().getValue(var3, (Object)null, var1);
         return var2;
      } catch (javax.el.ELException var5) {
         throw new ELException();
      }
   }

   public void syncBeginTagFile() {
      this.saveNestedVariables();
   }

   public void syncBeforeInvoke() {
      this.copyTagToPageScope(0);
      this.copyTagToPageScope(1);
   }

   public void syncEndTagFile() {
      this.copyTagToPageScope(1);
      this.copyTagToPageScope(2);
      this.restoreNestedVariables();
   }

   private void copyTagToPageScope(int var1) {
      Iterator var2 = null;
      switch (var1) {
         case 0:
            if (this._nestedVars != null) {
               var2 = this._nestedVars.iterator();
            }
            break;
         case 1:
            if (this._atBeginVars != null) {
               var2 = this._atBeginVars.iterator();
            }
            break;
         case 2:
            if (this._atEndVars != null) {
               var2 = this._atEndVars.iterator();
            }
      }

      while(var2 != null && var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = this.getAttribute(var3);
         var3 = this.findAlias(var3);
         if (var4 != null) {
            this._invokingContext.setAttribute(var3, var4);
         } else {
            this._invokingContext.removeAttribute(var3, 1);
         }
      }

   }

   private void saveNestedVariables() {
      if (this._nestedVars != null) {
         Iterator var1 = this._nestedVars.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            var2 = this.findAlias(var2);
            Object var3 = this._invokingContext.getAttribute(var2);
            if (var3 != null) {
               this._originalNestedVars.put(var2, var3);
            }
         }
      }

   }

   private void restoreNestedVariables() {
      if (this._nestedVars != null) {
         Iterator var1 = this._nestedVars.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            var2 = this.findAlias(var2);
            Object var3 = this._originalNestedVars.get(var2);
            if (var3 != null) {
               this._invokingContext.setAttribute(var2, var3);
            } else {
               this._invokingContext.removeAttribute(var2, 1);
            }
         }
      }

   }

   private String findAlias(String var1) {
      if (this._aliases == null) {
         return var1;
      } else {
         String var2 = (String)this._aliases.get(var1);
         return var2 == null ? var1 : var2;
      }
   }
}
