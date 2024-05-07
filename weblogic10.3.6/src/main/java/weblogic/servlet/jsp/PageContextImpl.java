package weblogic.servlet.jsp;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import javax.el.ELContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;
import weblogic.servlet.internal.ChunkOutputWrapper;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.enumerations.IteratorEnumerator;
import weblogic.utils.io.FilenameEncoder;

public final class PageContextImpl extends PageContext {
   private HttpServletRequest rq;
   private HttpServletResponse rp;
   protected Servlet servlet;
   protected ServletConfig config;
   protected ServletContext context;
   protected JspFactory factory;
   protected ELContext elContext;
   protected JspApplicationContextImpl jaCtxImpl;
   protected boolean needsSession;
   protected String errorPageURL;
   protected boolean autoFlush;
   protected int bufferSize;
   protected transient HashMap attributes;
   protected transient JspWriter out;
   private Stack writers = new Stack();

   public void initialize(Servlet var1, ServletRequest var2, ServletResponse var3, String var4, boolean var5, int var6, boolean var7) throws IllegalStateException, IllegalArgumentException {
      this.servlet = var1;
      this.config = var1.getServletConfig();
      this.context = var1.getServletConfig().getServletContext();
      this.rq = (HttpServletRequest)var2;
      this.rp = (HttpServletResponse)var3;
      this.errorPageURL = var4;
      this.needsSession = var5;
      this.bufferSize = var6;
      this.autoFlush = var7;
      this.attributes = new HashMap();
      var3.setBufferSize(var6);
      this.setAttribute("javax.servlet.jsp.jspOut", this.out);
      this.setAttribute("javax.servlet.jsp.jspRequest", this.rq);
      this.setAttribute("javax.servlet.jsp.jspResponse", this.rp);
      this.setAttribute("javax.servlet.jsp.jspPage", this.servlet);
      this.setAttribute("javax.servlet.jsp.jspConfig", this.config);
      this.setAttribute("javax.servlet.jsp.jspPageContext", this);
      this.setAttribute("javax.servlet.jsp.jspApplication", this.context);
      this.setAttribute("weblogic.servlet.jsp", "true", 2);
   }

   protected PageContextImpl(JspFactory var1, Servlet var2, ServletRequest var3, ServletResponse var4, String var5, boolean var6, int var7, boolean var8) throws IOException, IllegalStateException, IllegalArgumentException {
      this.out = new JspWriterImpl(var4, var7, var8);
      this.initialize(var2, var3, var4, var5, var6, var7, var8);
   }

   private RequestDispatcher getRD(String var1) throws ServletException {
      if (var1 == null) {
         throw new ServletException("requested URL string is null");
      } else {
         if (!var1.startsWith("/")) {
            String var2 = (String)this.rq.getAttribute("javax.servlet.include.request_uri");
            if (var2 == null) {
               var2 = ServletRequestImpl.getResolvedURI(this.rq);
            }

            int var3 = var2.lastIndexOf(47);
            if (var3 == -1) {
               var1 = '/' + var1;
            } else if (var3 == var2.length() - 1) {
               var1 = var2 + var1;
            } else {
               var1 = var2.substring(0, var3 + 1) + var1;
            }

            if (!"/".equals(this.context.getContextPath())) {
               var1 = var1.substring(this.context.getContextPath().length());
            }

            var3 = var1.indexOf(63);
            String var4 = null;
            if (var3 != -1) {
               var4 = var1.substring(var3);
               var1 = var1.substring(0, var3);
            }

            var1 = FilenameEncoder.resolveRelativeURIPath(var1);
            if (var4 != null) {
               var1 = var1 + var4;
            }
         }

         RequestDispatcher var5 = this.servlet.getServletConfig().getServletContext().getRequestDispatcher(var1);
         if (var5 == null) {
            throw new ServletException("no request dispatcher available for '" + var1 + "'");
         } else {
            return var5;
         }
      }
   }

   public void forward(String var1) throws IOException, ServletException {
      try {
         this.out.clear();
      } catch (IOException var3) {
         throw new IllegalStateException(var3);
      }

      this.getRD(var1).forward(this.rq, this.rp);
   }

   public void include(String var1) throws IOException, ServletException {
      this.getRD(var1).include(this.rq, this.rp);
   }

   public void include(String var1, boolean var2) throws IOException, ServletException {
      if (var2 && !(this.out instanceof BodyContent)) {
         this.out.flush();
      }

      this.include(var1);
   }

   public JspWriter _createOut(int var1, boolean var2) {
      return this.out;
   }

   public void setAttribute(String var1, Object var2) {
      if (var1 == null) {
         throw new NullPointerException("null name");
      } else {
         this.attributes.put(var1, var2);
      }
   }

   public Object getAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null name");
      } else {
         return this.attributes.get(var1);
      }
   }

   public void removeAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException("removeAttribute called with null name");
      } else {
         this.attributes.remove(var1);
         this.removeAttribute(var1, 2);
         HttpSession var2 = this.getSession();
         if (var2 != null) {
            this.removeAttribute(var1, 3);
         }

         this.removeAttribute(var1, 4);
      }
   }

   public JspWriter getOut() {
      return this.out;
   }

   public HttpSession getSession() {
      if (this.needsSession) {
         this.needsSession = false;
         return this.rq.getSession(true);
      } else {
         return this.rq.getSession(false);
      }
   }

   private HttpSession getSessionForSessionScope() {
      HttpSession var1 = this.getSession();
      if (var1 == null) {
         throw new IllegalStateException("Either the page doesn't participate in a session, or the session is not valid any longer");
      } else {
         return var1;
      }
   }

   public Object getPage() {
      return this.servlet;
   }

   public ServletRequest getRequest() {
      return this.rq;
   }

   public ServletResponse getResponse() {
      return this.rp;
   }

   public Exception getException() {
      return (Exception)this.rq.getAttribute("javax.servlet.jsp.jspException");
   }

   public ServletConfig getServletConfig() {
      return this.servlet.getServletConfig();
   }

   public ServletContext getServletContext() {
      return this.getServletConfig().getServletContext();
   }

   public void release() {
      this.rq = null;
      this.rp = null;
      this.context = null;
      this.errorPageURL = null;
      this.attributes.clear();
      this.out = null;
      this.writers = null;
   }

   public void setAttribute(String var1, Object var2, int var3) {
      switch (var3) {
         case 1:
            this.setAttribute(var1, var2);
            break;
         case 2:
            this.rq.setAttribute(var1, var2);
            break;
         case 3:
            HttpSession var4 = this.getSessionForSessionScope();
            var4.setAttribute(var1, var2);
            break;
         case 4:
            this.context.setAttribute(var1, var2);
            break;
         default:
            throw new IllegalArgumentException("illegal scope: " + var3);
      }

   }

   public Object getAttribute(String var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("null name");
      } else {
         switch (var2) {
            case 1:
               return this.getAttribute(var1);
            case 2:
               return this.rq.getAttribute(var1);
            case 3:
               HttpSession var3 = this.getSessionForSessionScope();
               return var3.getAttribute(var1);
            case 4:
               return this.context.getAttribute(var1);
            default:
               throw new IllegalArgumentException("illegal scope: " + var2);
         }
      }
   }

   public Object findAttribute(String var1) {
      if (var1 == null) {
         throw new NullPointerException("findAttribute called with a null value");
      } else {
         Object var2 = null;
         var2 = this.getAttribute(var1, 1);
         if (var2 == null) {
            var2 = this.getAttribute(var1, 2);
            if (var2 == null) {
               HttpSession var3 = this.getSession();
               if (var3 != null) {
                  var2 = this.getAttribute(var1, 3);
               }

               if (var2 == null) {
                  var2 = this.getAttribute(var1, 4);
               }
            }
         }

         return var2;
      }
   }

   public void removeAttribute(String var1, int var2) {
      if (var1 == null) {
         throw new NullPointerException("name is null");
      } else {
         switch (var2) {
            case 1:
               this.attributes.remove(var1);
               break;
            case 2:
               this.rq.removeAttribute(var1);
               break;
            case 3:
               HttpSession var3 = this.getSessionForSessionScope();
               var3.removeAttribute(var1);
               break;
            case 4:
               this.context.removeAttribute(var1);
               break;
            default:
               throw new IllegalArgumentException("illegal scope: " + var2);
         }

      }
   }

   public int getAttributesScope(String var1) {
      if (var1 == null) {
         throw new NullPointerException("null name");
      } else if (this.getAttribute(var1, 1) != null) {
         return 1;
      } else if (this.getAttribute(var1, 2) != null) {
         return 2;
      } else if (this.getAttribute(var1, 3) != null) {
         return 3;
      } else {
         return this.getAttribute(var1, 4) != null ? 4 : 0;
      }
   }

   public Enumeration getAttributeNamesInScope(int var1) {
      switch (var1) {
         case 1:
            return new IteratorEnumerator(this.attributes.keySet().iterator());
         case 2:
            return this.rq.getAttributeNames();
         case 3:
            HttpSession var2 = this.getSessionForSessionScope();
            return var2.getAttributeNames();
         case 4:
            return this.context.getAttributeNames();
         default:
            throw new IllegalArgumentException("illegal scope: " + var1);
      }
   }

   public void handlePageException(Throwable var1) throws ServletException, IOException {
      if (var1 == null) {
         throw new NullPointerException("null Throwable");
      } else {
         Throwable var2 = var1;
         if (var1 instanceof ServletException) {
            var2 = WebAppServletContext.getRootCause((ServletException)var1);
         }

         String var3 = this.rq.getRequestURI();
         this.rq.setAttribute("javax.servlet.jsp.jspException", var2);
         this.rq.setAttribute("javax.servlet.error.request_uri", var3);
         if (this.errorPageURL != null) {
            ((WebAppServletContext)this.context).getErrorManager().setErrorAttributes(this.rq, var3, var2);
            if (this.rp.isCommitted()) {
               this.include(this.errorPageURL);
            } else {
               this.forward(this.errorPageURL);
            }

         } else {
            if (var1 instanceof javax.servlet.jsp.JspException) {
               Throwable var4 = ((javax.servlet.jsp.JspException)var1).getRootCause();
               if (var4 != null) {
                  var1 = var4;
               }
            }

            if (var1 instanceof Error) {
               throw (Error)var1;
            } else if (var1 instanceof RuntimeException) {
               throw (RuntimeException)var1;
            } else if (var1 instanceof ServletException) {
               throw (ServletException)var1;
            } else if (var1 instanceof IOException) {
               throw (IOException)var1;
            } else {
               throw new ServletException(var1);
            }
         }
      }
   }

   public void handlePageException(Exception var1) throws ServletException, IOException {
      this.handlePageException((Throwable)var1);
   }

   public JspWriter pushBody(Writer var1) {
      this.writers.push(this.out);
      this.out = new BodyContentImpl(this.out, this, var1);
      this.rp = new NestedBodyResponse(this.rp, (BodyContentImpl)this.out);
      this.setAttribute("javax.servlet.jsp.jspOut", this.out);
      ChunkOutputWrapper var2 = ((BodyContentImpl)this.out).co;
      this.rq.setAttribute("weblogic.servlet.BodyTagOutput", var2);
      return (BodyContent)this.out;
   }

   public BodyContent pushBody() {
      return (BodyContent)this.pushBody((Writer)null);
   }

   public JspWriter popBody() {
      this.out = (JspWriter)this.writers.pop();
      if (this.rp instanceof NestedBodyResponse) {
         this.rp = (HttpServletResponse)((NestedBodyResponse)this.rp).getResponse();
      }

      if (this.out instanceof BodyContentImpl) {
         ChunkOutputWrapper var1 = ((BodyContentImpl)this.out).co;
         this.rq.setAttribute("weblogic.servlet.BodyTagOutput", var1);
      } else {
         this.rq.removeAttribute("weblogic.servlet.BodyTagOutput");
      }

      this.setAttribute("javax.servlet.jsp.jspOut", this.out);
      return this.out;
   }

   public ExpressionEvaluator getExpressionEvaluator() {
      return JspConfig.COMMON_UTILS != null ? JspConfig.COMMON_UTILS.getExpressionEvaluator() : null;
   }

   public VariableResolver getVariableResolver() {
      return JspConfig.COMMON_UTILS != null ? JspConfig.COMMON_UTILS.getVariableResolver(this) : null;
   }

   public ELContext getELContext() {
      if (this.elContext == null) {
         this.elContext = this.getJspApplicationContextImpl().createELContext(this);
      }

      return this.elContext;
   }

   public JspApplicationContextImpl getJspApplicationContextImpl() {
      if (this.jaCtxImpl == null) {
         JspFactoryImpl.init();
         this.jaCtxImpl = (JspApplicationContextImpl)JspFactoryImpl.getDefaultFactory().getJspApplicationContext(this.context);
      }

      return this.jaCtxImpl;
   }
}
