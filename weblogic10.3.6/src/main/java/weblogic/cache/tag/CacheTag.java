package weblogic.cache.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;
import weblogic.cache.CacheException;
import weblogic.cache.CacheValue;
import weblogic.cache.KeyEnumerator;
import weblogic.cache.webapp.KeySet;
import weblogic.cache.webapp.ServletCacheUtils;
import weblogic.cache.webapp.WebAppCacheSystem;
import weblogic.servlet.internal.ServletOutputStreamImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.utils.StackTraceUtils;

public class CacheTag extends BodyTagSupport implements TryCatchFinally {
   static final long serialVersionUID = 2706226325452664446L;
   private static boolean debug = false;
   private static boolean verbose = false;
   private int timeout = -1;
   private String timeoutString;
   private String scope = "application";
   private String name;
   private int size = -1;
   private String key;
   private boolean async = false;
   private String vars;
   private String flush;
   private boolean trigger;
   private static boolean initialized;
   private long startTime;
   private KeySet keySet;
   private WebAppCacheSystem cs = new WebAppCacheSystem();
   private CacheValue cache;
   private static boolean nestwarning;
   private CacheValue oldCache;
   private boolean failed;
   protected Map savedPageContextValues;
   private static final Object DELETE = new Object();
   private boolean cacheUpdate = false;
   private Map savedPageScope;
   private Map savedRequestScope;
   private Map savedSessionScope;
   private Map savedApplicationScope;
   private Map updatedPageScope;
   private Map updatedRequestScope;
   private Map updatedSessionScope;
   private Map updatedApplicationScope;

   public static boolean getDebug() {
      return debug;
   }

   public static void setDebug(boolean var0) {
      debug = var0;
   }

   public static boolean getVerbose() {
      return verbose;
   }

   public static void setVerbose(boolean var0) {
      verbose = var0;
   }

   public void setTimeout(String var1) throws CacheException {
      this.timeout = ServletCacheUtils.getTimeout(var1);
      if (debug) {
         System.out.println("Timeout: " + this.timeout);
      }

      this.timeoutString = var1;
   }

   public String getTimeout() {
      return this.timeoutString;
   }

   public void setScope(String var1) {
      this.scope = var1;
   }

   public String getScope() {
      return this.scope;
   }

   public void setName(String var1) {
      this.name = var1.intern();
   }

   public String getName() {
      HttpServletRequest var1 = (HttpServletRequest)this.pageContext.getRequest();
      if (this.name == null) {
         Integer var3 = (Integer)this.pageContext.getAttribute("weblogicx.cache.tag.CacheTag.instanceNum");
         int var2;
         if (var3 == null) {
            var2 = 0;
         } else {
            var2 = var3 + 1;
         }

         this.pageContext.setAttribute("weblogicx.cache.tag.CacheTag.instanceNum", new Integer(var2));
         Object var4 = var1.getAttribute("javax.servlet.include.request_uri");
         if (var4 == null) {
            var4 = var1.getRequestURI();
         }

         this.name = ("weblogicx.cache.tag.CacheTag." + var4 + "." + var2).intern();
      }

      return this.name;
   }

   public void setSize(int var1) {
      this.size = var1;
   }

   public int getSize() {
      return this.size;
   }

   public void setKey(String var1) {
      this.key = var1;
   }

   public String getKey() {
      return this.key;
   }

   public void setAsync(boolean var1) {
      this.async = var1;
   }

   public boolean getAsync() {
      return this.async;
   }

   public void setVars(String var1) {
      this.vars = var1;
   }

   public String getVars() {
      return this.vars;
   }

   public void setFlush(String var1) {
      this.flush = var1;
   }

   public String getFlush() {
      return this.flush;
   }

   public void setTrigger(boolean var1) {
      this.trigger = var1;
   }

   public boolean getTrigger() {
      return this.trigger;
   }

   public int doStartTag() throws JspException {
      try {
         if (this.flush == null) {
            ServletRequest var1 = this.pageContext.getRequest();
            if (var1.getAttribute("weblogic.cache.tag.CacheTag.caching") == null) {
               var1.setAttribute("weblogic.cache.tag.CacheTag.caching", "true");
            } else if (!nestwarning) {
               this.pageContext.getServletContext().log("Nested cache tags have been detected.  Inner cache tags will only be updated when their containing tag is updated.");
               nestwarning = true;
            }
         }

         this.cs.setPageContext(this.pageContext);
         String var3 = ((HttpServletRequest)this.pageContext.getRequest()).getMethod();
         if (var3.equals("POST") || var3.equals("PUT")) {
            this.async = false;
         }

         return this.key == null ? this.doStartCache() : this.doStartKeyedCache();
      } catch (CacheException var2) {
         throw new JspException("We experienced an underlying cache exception during revert, cache may be corrupt: " + StackTraceUtils.throwable2StackTrace(var2));
      }
   }

   protected int doStartCache() throws CacheException, JspException {
      boolean var1 = "true".equals(this.flush) || "now".equals(this.flush);
      if (var1 || this.trigger) {
         this.cs.flushCache(this.scope, this.getName());
         if (var1) {
            return 0;
         }
      }

      if (this.async) {
         this.oldCache = this.cs.getCurrentCache(this.scope, this.getName());
      }

      this.cache = this.cs.getCache(this.scope, this.getName());
      if (this.cache != null && "lazy".equals(this.flush)) {
         this.cache.setFlush(true);
         return 0;
      } else if (this.cache == null) {
         if (this.async && this.oldCache != null) {
            this.asyncForward();
         }

         this.startTime = System.currentTimeMillis();
         this.cache = new CacheValue();
         this.pageContext.setAttribute(this.getName(), this.cache);
         return 2;
      } else {
         this.pageContext.setAttribute(this.getName(), this.cache);

         try {
            this.pageContext.getOut().print((String)this.cache.getContent());
         } catch (IOException var3) {
         }

         this.revertPageContextVariables();
         ServletCacheUtils.restoreVars(this.cs, this.cache, this.vars, "request");
         return 0;
      }
   }

   protected int doEndCache() throws CacheException {
      this.cache.setContent(this.getBodyContent().getString());
      this.cache.setTimeout(this.timeout);
      ServletCacheUtils.saveVars(this.cs, this.cache, this.vars, "request");
      int var1 = (int)(System.currentTimeMillis() - this.startTime);
      this.cs.setCache(this.scope, this.getName(), this.cache, var1);

      try {
         if (!this.cacheUpdate) {
            this.getBodyContent().getEnclosingWriter().print((String)this.cache.getContent());
         }
      } catch (IOException var3) {
      }

      return 0;
   }

   protected int doStartKeyedCache() throws CacheException, JspException {
      KeySet var1 = this.getKeySet();
      boolean var2 = "true".equals(this.flush) || "now".equals(this.flush);
      if (var2 || this.trigger) {
         this.cs.flushCache(this.scope, this.getName(), var1);
         if (var2) {
            return 0;
         }
      }

      if (this.async) {
         this.oldCache = this.cs.getCurrentCache(this.scope, this.getName(), this.size, var1);
      }

      this.cache = this.cs.getCache(this.scope, this.getName(), this.size, var1);
      if (this.cache != null && "lazy".equals(this.flush)) {
         this.cache.setFlush(true);
         return 0;
      } else if (this.cache == null) {
         if (this.async && this.oldCache != null) {
            this.asyncForward();
         }

         this.startTime = System.currentTimeMillis();
         this.cache = new CacheValue();
         this.pageContext.setAttribute(this.getName(), this.cache);
         return 2;
      } else {
         this.pageContext.setAttribute(this.getName(), this.cache);

         try {
            if (!this.cacheUpdate) {
               this.pageContext.getOut().print((String)this.cache.getContent());
            }
         } catch (IOException var4) {
         }

         this.revertPageContextVariables();
         ServletCacheUtils.restoreVars(this.cs, this.cache, this.vars, "request");
         return 0;
      }
   }

   protected int doEndKeyedCache() throws CacheException {
      KeySet var1 = this.getKeySet();
      this.cache.setContent(this.getBodyContent().getString());
      this.cache.setTimeout(this.timeout);
      ServletCacheUtils.saveVars(this.cs, this.cache, this.vars, "request");
      int var2 = (int)(System.currentTimeMillis() - this.startTime);
      this.cs.setCache(this.scope, this.getName(), this.size, var1, this.cache, var2);

      try {
         this.getBodyContent().getEnclosingWriter().print((String)this.cache.getContent());
      } catch (IOException var4) {
      }

      return 0;
   }

   public int doEndTag() {
      return this.cacheUpdate ? 5 : 6;
   }

   public int doAfterBody() throws JspException {
      if (this.failed) {
         return 0;
      } else {
         try {
            this.revertPageContextVariables();
            if (this.cacheUpdate) {
               this.revertKeysAfterCacheUpdate();
            }

            return this.key == null ? this.doEndCache() : this.doEndKeyedCache();
         } catch (CacheException var2) {
            throw new JspException("We experienced an underlying cache exception during revert, cache may be corrupt: " + StackTraceUtils.throwable2StackTrace(var2));
         }
      }
   }

   public void release() {
      try {
         this.cs.releaseAllLocks();
      } catch (CacheException var2) {
      }

      this.timeout = -1;
      this.timeoutString = null;
      this.scope = "application";
      this.name = null;
      this.size = -1;
      this.key = null;
      this.vars = null;
      this.flush = null;
      this.keySet = null;
      this.savedPageContextValues = null;
      this.cache = null;
      this.cacheUpdate = false;
      this.savedPageScope = null;
      this.savedRequestScope = null;
      this.savedSessionScope = null;
      this.savedApplicationScope = null;
      this.updatedPageScope = null;
      this.updatedRequestScope = null;
      this.updatedSessionScope = null;
      this.updatedApplicationScope = null;
   }

   public void doCatch(Throwable var1) throws Throwable {
      this.pageContext.getServletContext().log("Cache " + this.getName() + " failed to refresh with an exception: " + StackTraceUtils.throwable2StackTrace(var1));
      CacheValue var2 = null;
      boolean var20 = false;

      try {
         var20 = true;
         this.revertPageContextVariables();
         if (this.key == null) {
            var2 = this.cs.getCurrentCache(this.scope, this.getName());
         } else {
            var2 = this.cs.getCurrentCache(this.scope, this.getName(), this.size, this.getKeySet());
         }

         if (var2 == null) {
            this.pageContext.removeAttribute(this.getName());
            throw var1;
         }

         try {
            this.pageContext.getOut().print(var2.getContent());
         } catch (IOException var23) {
         }

         ServletCacheUtils.restoreVars(this.cs, this.cache, this.vars, "request");
         this.failed = true;
         this.pageContext.setAttribute(this.getName(), var2);
         var20 = false;
      } finally {
         if (var20) {
            int var6 = (int)(System.currentTimeMillis() - this.startTime);

            try {
               if (this.key == null) {
                  this.cs.setCache(this.scope, this.getName(), var2, var6);
               } else {
                  this.cs.setCache(this.scope, this.getName(), this.size, this.keySet, var2, var6);
               }
            } finally {
               this.release();
            }

         }
      }

      int var3 = (int)(System.currentTimeMillis() - this.startTime);

      try {
         if (this.key == null) {
            this.cs.setCache(this.scope, this.getName(), var2, var3);
         } else {
            this.cs.setCache(this.scope, this.getName(), this.size, this.keySet, var2, var3);
         }
      } finally {
         this.release();
      }

   }

   public void doFinally() {
      this.oldCache = null;
      this.failed = false;
      ServletRequest var1 = this.pageContext.getRequest();
      var1.removeAttribute("weblogic.cache.tag.CacheTag.caching");
   }

   protected void revertPageContextVariables() {
      if (this.key != null && this.savedPageContextValues != null) {
         Iterator var1 = this.savedPageContextValues.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            Object var3 = this.savedPageContextValues.get(var2);
            if (var3 == DELETE) {
               this.pageContext.removeAttribute(var2, 1);
            } else {
               this.pageContext.setAttribute(var2, var3);
            }
         }
      }

   }

   protected void saveKeysBeforeForward() throws CacheException, JspException {
      if (this.key != null) {
         this.savedPageScope = new HashMap();
         this.savedRequestScope = new HashMap();
         this.savedSessionScope = new HashMap();
         this.savedApplicationScope = new HashMap();
         this.storeKeyValues(this.savedPageScope, this.savedRequestScope, this.savedSessionScope, this.savedApplicationScope);
      }
   }

   protected void revertKeysAfterForward() throws JspException {
      if (this.key != null) {
         this.revertValues(this.savedPageScope, this.savedRequestScope, this.savedSessionScope, this.savedApplicationScope);
      }
   }

   protected void saveKeysBeforeCacheUpdate() throws CacheException, JspException {
      if (this.key != null) {
         this.updatedPageScope = new HashMap();
         this.updatedRequestScope = new HashMap();
         this.updatedSessionScope = new HashMap();
         this.updatedApplicationScope = new HashMap();
         this.storeKeyValues(this.updatedPageScope, this.updatedRequestScope, this.updatedSessionScope, this.updatedApplicationScope);
      }
   }

   protected void revertKeysAfterCacheUpdate() throws JspException {
      if (this.key != null) {
         this.revertValues(this.updatedPageScope, this.updatedRequestScope, this.updatedSessionScope, this.updatedApplicationScope);
      }
   }

   protected void storeKeyValues(Map var1, Map var2, Map var3, Map var4) throws CacheException, JspException {
      KeyEnumerator var5 = new KeyEnumerator(this.key);

      while(true) {
         while(true) {
            String var6;
            Object var8;
            label50:
            do {
               while(var5.hasMoreKeys()) {
                  var6 = var5.getNextKey();
                  String var7 = var5.getKeyScope();
                  var8 = this.cs.getValueFromScope(var7, var6);
                  if (var7.equals("any")) {
                     continue label50;
                  }

                  if (var7.equals("page")) {
                     if (var8 != null) {
                        var1.put(var6, var8);
                     }
                  } else if (var7.equals("request")) {
                     if (var8 != null) {
                        var2.put(var6, var8);
                     }
                  } else if (var7.equals("session")) {
                     if (var8 != null) {
                        var3.put(var6, var8);
                     }
                  } else if (var7.equals("application") && var8 != null) {
                     var4.put(var6, var8);
                  }
               }

               return;
            } while(this.pageContext.getRequest().getParameter(var6) != null);

            if (this.pageContext.getAttribute(var6) != null) {
               var1.put(var6, var8);
            } else if (this.pageContext.getRequest().getAttribute(var6) != null) {
               var2.put(var6, var8);
            } else {
               HttpSession var9 = this.pageContext.getSession();
               if (var9 != null && var9.getAttribute(var6) != null) {
                  var3.put(var6, var8);
               } else if (this.pageContext.getServletContext().getAttribute(var6) != null) {
                  var4.put(var6, var8);
               }
            }
         }
      }
   }

   protected void revertValues(Map var1, Map var2, Map var3, Map var4) throws JspException {
      KeyEnumerator var5 = new KeyEnumerator(this.key);

      while(var5.hasMoreKeys()) {
         String var6 = var5.getNextKey();
         String var7 = var5.getKeyScope();
         Object var8;
         if (var7.equals("any")) {
            if ((var8 = var1.get(var6)) != null) {
               this.pageContext.setAttribute(var6, var8);
            } else if ((var8 = var2.get(var6)) != null) {
               this.pageContext.getRequest().setAttribute(var6, var8);
            } else {
               HttpSession var9;
               if ((var8 = var3.get(var6)) != null) {
                  var9 = this.pageContext.getSession();
                  if (var9 == null) {
                     throw new JspException("Session scope specified but this page has no session");
                  }

                  var9.setAttribute(var6, var8);
               } else if ((var8 = var4.get(var6)) != null) {
                  this.pageContext.getServletContext().setAttribute(var6, var8);
               } else if (this.pageContext.getRequest().getParameter(var6) == null) {
                  this.pageContext.removeAttribute(var6);
                  this.pageContext.getRequest().removeAttribute(var6);
                  var9 = this.pageContext.getSession();
                  if (var9 != null) {
                     var9.removeAttribute(var6);
                  }

                  this.pageContext.getServletContext().removeAttribute(var6);
               }
            }
         } else if (var7.equals("page")) {
            var8 = var1.get(var6);
            if (var8 == null) {
               this.pageContext.removeAttribute(var6);
            } else {
               this.pageContext.setAttribute(var6, var8);
            }
         } else if (var7.equals("request")) {
            var8 = var2.get(var6);
            if (var8 == null) {
               this.pageContext.getRequest().removeAttribute(var6);
            } else {
               this.pageContext.getRequest().setAttribute(var6, var8);
            }
         } else if (var7.equals("session")) {
            HttpSession var11 = this.pageContext.getSession();
            if (var11 == null) {
               throw new JspException("Session scope specified but this page has no session");
            }

            Object var10 = var3.get(var6);
            if (var10 == null) {
               var11.removeAttribute(var6);
            } else {
               var11.setAttribute(var6, var10);
            }
         } else if (var7.equals("application")) {
            var8 = var4.get(var6);
            if (var8 == null) {
               this.pageContext.getServletContext().removeAttribute(var6);
            } else {
               this.pageContext.getServletContext().setAttribute(var6, var8);
            }
         }
      }

   }

   protected void asyncForward() throws CacheException, JspException {
      try {
         this.saveKeysBeforeForward();
         if (verbose) {
            System.out.println("Forwarding request");
         }

         this.pageContext.forward(((HttpServletRequest)this.pageContext.getRequest()).getRequestURI().substring(((WebAppServletContext)this.pageContext.getServletContext()).getContextPath().length()));
         ServletOutputStreamImpl var1 = (ServletOutputStreamImpl)this.pageContext.getResponse().getOutputStream();
         if (!this.pageContext.getResponse().isCommitted()) {
            ((HttpServletResponse)this.pageContext.getResponse()).setHeader("Content-Length", "" + var1.getCount());
            var1.flush();
         }

         this.saveKeysBeforeCacheUpdate();
         this.revertKeysAfterForward();
      } catch (IOException var2) {
         throw new JspException("Could not asynchronously execute page: " + var2);
      } catch (ServletException var3) {
         throw new JspException("Could not asynchronously execute page: " + var3);
      }

      this.cacheUpdate = true;
      this.startTime = System.currentTimeMillis();
      if (verbose) {
         System.out.println("Doing cache update");
      }

   }

   protected KeySet getKeySet() throws CacheException {
      if (this.keySet != null) {
         return this.keySet;
      } else {
         this.keySet = new KeySet(this.cs);
         KeyEnumerator var1 = new KeyEnumerator(this.key);

         while(var1.hasMoreKeys()) {
            String var2 = var1.getNextKey();
            String var3 = var1.getKeyScope();
            Object var4 = this.keySet.addKey(var3, var2);
            if (this.savedPageContextValues == null) {
               this.savedPageContextValues = new HashMap();
            }

            Object var5;
            if ((var5 = this.pageContext.getAttribute(var2)) != null) {
               this.savedPageContextValues.put(var2, var5);
            } else {
               this.savedPageContextValues.put(var2, DELETE);
            }

            if (var4 == null) {
               this.pageContext.removeAttribute(var2);
            } else {
               this.pageContext.setAttribute(var2, var4);
            }
         }

         return this.keySet;
      }
   }
}
