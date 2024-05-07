package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.WSEndpoint;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.servlet.http.RequestResponseKey;

public class JAXWSPublishServlet extends JAXWSServlet {
   public static final String CONFIGURATION_KEY = "CONFIGURATION_KEY";
   public static final String SERVICEURI_KEY = "SERVICEURI_KEY";
   private String serviceUri;
   private Configuration config;
   static final long serialVersionUID = 8254975160342537385L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.jaxws.JAXWSPublishServlet");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
   public static final DelegatingMonitor _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   public void init() throws ServletException {
      ServletContext var1 = this.getServletContext();
      this.serviceUri = this.getServletConfig().getInitParameter("SERVICEURI_KEY");
      if (this.serviceUri == null) {
         this.serviceUri = (String)var1.getAttribute("SERVICEURI_KEY");
      }

      Map var2 = (Map)var1.getAttribute("CONFIGURATION_KEY");
      this.config = (Configuration)var2.get(this.serviceUri);
      super.init();
   }

   protected boolean doRequest(RequestResponseKey var1) throws IOException, ServletException {
      if (this.config.stopped) {
         var1.getResponse().sendError(404);
         return false;
      } else {
         return super.doRequest(var1);
      }
   }

   WSEndpoint getEndpoint() throws Exception {
      return this.config.endpoint;
   }

   String getServiceUri() {
      boolean var8 = false;

      String var10000;
      String var3;
      try {
         var8 = true;
         var10000 = this.serviceUri;
         var8 = false;
      } finally {
         if (var8) {
            var3 = null;
            if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var10002 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var3);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
               InstrumentationSupport.process(var10002, var10003, var10003.getActions());
            }

         }
      }

      var3 = var10000;
      if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var3);
         DelegatingMonitor var10 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
         InstrumentationSupport.process(var10001, var10, var10.getActions());
      }

      return var10000;
   }

   WLSContainer getContainer() {
      return this.config.container;
   }

   static {
      _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXWS_Diagnostic_Resource_After_Low");
      _WLDF$INST_FLD_Servlet_Async_Action_Around_Medium = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Servlet_Async_Action_Around_Medium");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "JAXWSPublishServlet.java", "weblogic.wsee.jaxws.JAXWSPublishServlet", "getServiceUri", "()Ljava/lang/String;", 50, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXWS_Diagnostic_Resource_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, InstrumentationSupport.createValueHandlingInfo("uri", "weblogic.diagnostics.instrumentation.gathering.WebservicesJAXWSUriStringRenderer", false, true), (ValueHandlingInfo[])null)}), (boolean)0);
   }

   public static class Configuration {
      public WLSContainer container;
      public WSEndpoint endpoint;
      public boolean stopped;

      public Configuration(WLSContainer var1, WSEndpoint var2) {
         this.container = var1;
         this.endpoint = var2;
         this.stopped = false;
      }
   }
}
