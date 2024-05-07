package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weblogic.servlet.http.AbstractAsyncServlet;
import weblogic.servlet.http.RequestResponseKey;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.util.JAXWSClassLoaderFactory;
import weblogic.wsee.util.Verbose;

abstract class JAXWSServlet extends AbstractAsyncServlet {
   private static final String METHOD_GET = "GET";
   private static final String METHOD_POST = "POST";
   private static final String METHOD_HEAD = "HEAD";
   static boolean verbose = Verbose.isVerbose(JAXWSServlet.class);
   private HTTPProcessor httpProcessor = null;
   private WSEndpoint wsep;
   private JAXWSClassLoaderFactory jclf = JAXWSClassLoaderFactory.getInstance();

   public void init() throws ServletException {
      if (verbose) {
         Verbose.log((Object)"Begin JAXWS Servlet init");
      }

      super.init();
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      this.jclf.setContextLoader(var1);

      try {
         this.registerEndpoint();
      } catch (Exception var6) {
         throw new ServletException("Exception in JAXWS init", var6);
      } finally {
         Thread.currentThread().setContextClassLoader(var1);
      }

      this.setTimeout(-1);
   }

   public void destroy() {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      this.jclf.remove(var1);
   }

   protected boolean doRequest(RequestResponseKey var1) throws IOException, ServletException {
      var1.setCallDoResponse(false);
      var1.setImmediateSendOnDoRequestFalse(false);
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      this.jclf.setContextLoader(var2);
      WLSContainer var3 = this.getContainer();
      var3.setCurrent();

      boolean var7;
      try {
         HttpServletRequest var4 = var1.getRequest();
         HttpServletResponse var5 = var1.getResponse();
         String var6 = var4.getMethod();
         if (var6.equals("GET") || var6.equals("HEAD")) {
            var7 = this.httpProcessor.get(var1, false);
            return var7;
         }

         if (!var6.equals("POST")) {
            if (var4.getProtocol().endsWith("1.1")) {
               var5.sendError(405);
            } else {
               var5.sendError(400);
            }

            var7 = false;
            return var7;
         }

         var7 = this.httpProcessor.post(var1, false);
      } finally {
         var3.resetCurrent();
         Thread.currentThread().setContextClassLoader(var2);
      }

      return var7;
   }

   protected void doResponse(RequestResponseKey var1, Object var2) throws IOException, ServletException {
      if (var2 != null) {
         throw new ServletException((Throwable)var2);
      }
   }

   protected void doTimeout(RequestResponseKey var1) throws IOException, ServletException {
      var1.getResponse().setStatus(503);
   }

   abstract WSEndpoint getEndpoint() throws Exception;

   abstract String getServiceUri();

   abstract WLSContainer getContainer();

   private void registerEndpoint() throws Exception {
      WLSContainer var1 = this.getContainer();
      this.wsep = this.getEndpoint();
      ServletAdapter var2 = (ServletAdapter)var1.registerBoundEndpoint(this.wsep, this.getServiceUri());
      this.httpProcessor = new HttpServletAdapter(this.wsep, var1, (WebAppServletContext)this.getServletContext(), var2);
      var1.setCurrent();

      try {
         EndpointCreationInterceptorFeature var3 = (EndpointCreationInterceptorFeature)this.wsep.getBinding().getFeature(EndpointCreationInterceptorFeature.class);
         if (var3 != null) {
            ArrayList var4 = new ArrayList();
            Iterator var5 = var3.getInterceptors().iterator();

            WeakReference var6;
            while(var5.hasNext()) {
               var6 = (WeakReference)var5.next();
               EndpointCreationInterceptor var7 = (EndpointCreationInterceptor)var6.get();
               if (var7 != null) {
                  var7.postCreateEndpoint(this.wsep);
               } else {
                  var4.add(var6);
               }
            }

            var5 = var4.iterator();

            while(var5.hasNext()) {
               var6 = (WeakReference)var5.next();
               var3.getInterceptors().remove(var6);
            }
         }
      } finally {
         var1.resetCurrent();
      }

      if (verbose) {
         this.httpProcessor = new VerboseHttpProcessor(this.httpProcessor);
      }

   }

   static {
      System.setProperty("com.sun.xml.ws.api.BindingID.SOAP_12.canGenerateWsdl", "true");
   }
}
