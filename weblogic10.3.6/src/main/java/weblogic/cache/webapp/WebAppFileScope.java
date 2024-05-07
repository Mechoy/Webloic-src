package weblogic.cache.webapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import weblogic.cache.CacheException;
import weblogic.management.DeploymentException;
import weblogic.persist.TxIndexedFile;
import weblogic.persist.TxIndexedFileStub;
import weblogic.servlet.WebLogicServletContext;

public class WebAppFileScope extends ServletContextAttributeScope {
   private static final Map caches = new HashMap();
   private TxIndexedFile file;
   private boolean inited;

   public WebAppFileScope() {
   }

   public WebAppFileScope(ServletContext var1) {
      super(var1);
   }

   public void setAttribute(String var1, Object var2) throws CacheException {
      this.init();
      if (var1.endsWith(".lock")) {
         super.setAttribute("filescope-" + var1, var2);
      } else {
         try {
            this.file.store(var1, var2);
         } catch (IOException var4) {
            throw new CacheException("Could not write to file store", var4);
         }
      }

   }

   public Object getAttribute(String var1) throws CacheException {
      this.init();
      return var1.endsWith(".lock") ? super.getAttribute("filescope-" + var1) : this.file.retrieve(var1);
   }

   public void removeAttribute(String var1) throws CacheException {
      this.init();
      if (var1.endsWith(".lock")) {
         super.removeAttribute("filescope-" + var1);
      } else {
         try {
            this.file.store(var1, (Object)null);
         } catch (IOException var3) {
            throw new CacheException("Could not write to file store", var3);
         }
      }

   }

   public Iterator getAttributeNames() throws CacheException {
      this.init();
      final Enumeration var1 = this.file.keys();
      ArrayList var2 = new ArrayList();
      final Iterator var3 = super.getAttributeNames();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (var4.startsWith("filescope-") && var4.endsWith(".lock")) {
            var2.add(var4);
         }
      }

      var3 = var2.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return var1.hasMoreElements() || var3.hasNext();
         }

         public Object next() {
            return var1.hasMoreElements() ? var1.nextElement() : var3.next();
         }

         public void remove() {
         }
      };
   }

   private void init() throws CacheException {
      Class var1 = WebAppFileScope.class;
      synchronized(WebAppFileScope.class) {
         if (!this.inited) {
            if (this.file != null) {
               this.file.shutdown();
            }

            File var2 = (File)this.getContext().getAttribute("javax.servlet.context.tempdir");
            String var3 = var2.getAbsolutePath();
            this.file = (TxIndexedFile)caches.get(var3);
            if (this.file == null) {
               try {
                  this.file = new TxIndexedFileStub("cache", var3, var3);
                  caches.put(var3, this.file);
                  this.registerContextListener();
               } catch (NamingException var6) {
                  throw new CacheException("Could not create file store", var6);
               }
            }

            this.inited = true;
         }

      }
   }

   private void registerContextListener() throws CacheException {
      ServletContext var1 = this.getContext();
      if (var1 != null && var1 instanceof WebLogicServletContext) {
         try {
            ((WebLogicServletContext)var1).registerListener(TxFileStubWiper.class.getName());
         } catch (DeploymentException var3) {
            throw new CacheException(var3.getMessage(), var3);
         } catch (Exception var4) {
            throw new CacheException(var4.getMessage(), var4);
         }
      }

   }

   public static class TxFileStubWiper implements ServletContextListener {
      public void contextInitialized(ServletContextEvent var1) {
      }

      public void contextDestroyed(ServletContextEvent var1) {
         ServletContext var2 = var1.getServletContext();
         if (var2 != null) {
            File var3 = (File)var2.getAttribute("javax.servlet.context.tempdir");
            if (var3 != null) {
               String var4 = var3.getAbsolutePath();
               Object var5 = WebAppFileScope.caches.get(var4);
               if (var5 != null && var5 instanceof TxIndexedFileStub) {
                  ((TxIndexedFileStub)var5).shutdown();
                  WebAppFileScope.caches.remove(var4);
               }
            }
         }
      }
   }
}
