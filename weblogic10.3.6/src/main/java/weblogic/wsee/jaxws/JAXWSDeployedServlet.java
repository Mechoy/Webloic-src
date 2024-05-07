package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.server.SDDocumentSource;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.MTOMFeature;
import weblogic.diagnostics.instrumentation.DelegatingMonitor;
import weblogic.diagnostics.instrumentation.DynamicJoinPoint;
import weblogic.diagnostics.instrumentation.InstrumentationSupport;
import weblogic.diagnostics.instrumentation.JoinPoint;
import weblogic.diagnostics.instrumentation.PointcutHandlingInfo;
import weblogic.diagnostics.instrumentation.ValueHandlingInfo;
import weblogic.j2ee.descriptor.wl.PortComponentBean;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.handler.ServerHandlerChainsResolver;
import weblogic.wsee.jaxws.injection.WSEEComponentContributor;
import weblogic.wsee.jaxws.spi.WLSProvider;
import weblogic.wsee.jaxws.spi.WorkManagerExecutor;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wstx.wsat.TransactionalFeature;
import weblogic.wsee.wstx.wsat.config.DDHelper;

public abstract class JAXWSDeployedServlet extends JAXWSServlet {
   DeployInfo info;
   WSEEComponentContributor wseeComponentContributor;
   private WLSContainer container;
   private ServerHandlerChainsResolver handlerChainsResolver;
   private WSEndpoint wse = null;
   static final long serialVersionUID = 3763231725950058651L;
   public static final String _WLDF$INST_VERSION = "9.0.0";
   // $FF: synthetic field
   static Class _WLDF$INST_FLD_class = Class.forName("weblogic.wsee.jaxws.JAXWSDeployedServlet");
   public static final DelegatingMonitor _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
   public static final JoinPoint _WLDF$INST_JPFLD_0;

   abstract DeployInfo loadDeployInfo() throws WsException;

   public void init() throws ServletException {
      try {
         this.info = this.loadDeployInfo();
         this.info.setServletContext(this.getServletContext());
         this.handlerChainsResolver = this.info.createServerHandlerChainsResolver();
         this.wseeComponentContributor = this.info.loadComponentContributor();
         this.info.validate();
      } catch (Exception var2) {
         throw new ServletException("Exception in JAXWS init", var2);
      }

      super.init();
   }

   public void destroy() {
      if (this.wse != null) {
         this.wse.dispose();
         this.wse = null;
      }

      super.destroy();
   }

   private Collection<SDDocumentSource> getImportedWsdlsAndSchemas(WsdlDefinitions var1) throws MalformedURLException {
      HashSet var2 = new HashSet();
      this.buildImportedWsdls(var1, var2);
      this.buildImportedSchemas(var1, var2);
      return var2;
   }

   private void buildImportedSchemas(WsdlDefinitions var1, Collection<SDDocumentSource> var2) throws MalformedURLException {
      if (var1.getTypes() != null && var1.getTypes().getImportedWsdlSchemas() != null) {
         List var3 = var1.getTypes().getImportedWsdlSchemas();
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            WsdlSchema var5 = (WsdlSchema)var4.next();
            SDDocumentSource var6 = this.createSDDocumentSource(var5.getLocationUrl());
            if (var6 != null) {
               var2.add(var6);
            }
         }
      }

   }

   private void buildImportedWsdls(WsdlDefinitions var1, Collection<SDDocumentSource> var2) throws MalformedURLException {
      Set var3 = var1.getKnownImportedWsdlLocations();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         SDDocumentSource var6 = this.createSDDocumentSource(var5);
         if (var6 != null) {
            var2.add(var6);
         }
      }

   }

   private SDDocumentSource createSDDocumentSource(String var1) throws MalformedURLException {
      SDDocumentSource var2 = null;
      URL var3 = null;
      if (var1 != null) {
         var3 = new URL(var1);
      }

      if (verbose) {
         Verbose.log((Object)("Constructing SDDocumentSource :" + var3));
      }

      if (var3 != null) {
         var2 = SDDocumentSource.create(var3);
      }

      return var2;
   }

   WLSContainer getContainer() {
      if (this.container == null) {
         this.container = new WLSContainer(this.getServletContext(), this.info);
      }

      return this.container;
   }

   String getServiceUri() {
      boolean var9 = false;

      String var10000;
      String var4;
      try {
         var9 = true;
         String var1 = this.info.getServiceURIs()[0];
         if (var1 == null || var1.equals("/")) {
            var1 = "/" + this.info.getPortComp().getWsdlService().getLocalPart();
         }

         var10000 = var1;
         var9 = false;
      } finally {
         if (var9) {
            var4 = null;
            if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
               DynamicJoinPoint var10002 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
               DelegatingMonitor var10003 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
               InstrumentationSupport.process(var10002, var10003, var10003.getActions());
            }

         }
      }

      var4 = var10000;
      if (_WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low.isEnabledAndNotDyeFiltered()) {
         DynamicJoinPoint var10001 = InstrumentationSupport.createDynamicJoinPoint(_WLDF$INST_JPFLD_0, (Object[])null, var4);
         DelegatingMonitor var11 = _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low;
         InstrumentationSupport.process(var10001, var11, var11.getActions());
      }

      return var10000;
   }

   WSEndpoint getEndpoint() throws Exception {
      WebServiceFeatureList var1 = new WebServiceFeatureList(this.info.getJwsClass());
      WLSProvider.parseAnnotations(var1, this.info.getJwsClass(), true);
      String var2 = this.info.getProtocolBinding();
      WSBinding var3 = null;
      if (this.info.getPortComp().isEnableMtom()) {
         var1.add(new MTOMFeature());
      }

      this.addWSATFeature(var1);
      var3 = BindingID.parse(var2).createBinding(var1);
      this.handlerChainsResolver.configureHandlers(var3, this.wseeComponentContributor);
      String var4 = null;
      Object var5 = new ArrayList();
      WsdlDefinitions var6 = this.info.getWsdlDef();
      if (var6 != null) {
         var4 = var6.getWsdlLocation();
         var5 = this.getImportedWsdlsAndSchemas(var6);
      }

      SDDocumentSource var7 = this.createSDDocumentSource(var4);
      URL var8 = this.info.getCatalog();
      if (verbose) {
         Verbose.log((Object)"Constructing WSEndpoint:");
         Verbose.say("  JWS = " + this.info.getJwsClass());
         Verbose.say("  wsdlService = " + this.info.getPortComp().getWsdlService());
         Verbose.say("  wsdlPort = " + this.info.getPortComp().getWsdlPort());
         Verbose.say("  bindingID =" + var3.getBindingId());
         Verbose.say("  MTOM Enbaled =" + var3.isFeatureEnabled(MTOMFeature.class));
         Verbose.say("  SOAP Version =" + var3.getSOAPVersion());
         Verbose.say("  primaryWsdl = " + var7);
         Verbose.say("  catalogURL = " + var8);
         Verbose.say("");
      }

      QName var9 = this.info.getPortComp().getWsdlService();
      QName var10 = this.info.getPortComp().getWsdlPort();
      this.container.registerPendingBoundEndpoint(var9, var10, this.getServiceUri());
      this.container.setCurrent();

      WSEndpoint var11;
      try {
         this.wse = WSEndpoint.create(this.info.getJwsClass(), false, this.info.createInstanceResolver().createInvoker(), var9, var10, this.container, var3, var7, (Collection)var5, var8);
         this.wse.setExecutor(WorkManagerExecutor.getExecutor());
         var11 = this.wse;
      } finally {
         this.container.resetCurrent();
      }

      return var11;
   }

   private void addWSATFeature(WebServiceFeatureList var1) {
      PortComponentBean var2 = this.info.getWlPortComp();
      if (var2 != null) {
         TransactionalFeature var3 = (TransactionalFeature)var1.get(TransactionalFeature.class);
         if (var3 != null) {
            DDHelper.updateFeatureFromServiceDD(var3, var2);
         } else {
            var3 = DDHelper.buildFeatureFromServiceDD(var2);
         }

         if (var3 != null) {
            var1.add(var3);
         }
      }

   }

   static {
      _WLDF$INST_FLD_Webservices_JAXWS_Diagnostic_Resource_After_Low = (DelegatingMonitor)InstrumentationSupport.getMonitor(_WLDF$INST_FLD_class, "Webservices_JAXWS_Diagnostic_Resource_After_Low");
      _WLDF$INST_JPFLD_0 = InstrumentationSupport.createJoinPoint(_WLDF$INST_FLD_class, "JAXWSDeployedServlet.java", "weblogic.wsee.jaxws.JAXWSDeployedServlet", "getServiceUri", "()Ljava/lang/String;", 125, InstrumentationSupport.makeMap(new String[]{"Webservices_JAXWS_Diagnostic_Resource_After_Low"}, new PointcutHandlingInfo[]{InstrumentationSupport.createPointcutHandlingInfo((ValueHandlingInfo)null, InstrumentationSupport.createValueHandlingInfo("uri", "weblogic.diagnostics.instrumentation.gathering.WebservicesJAXWSUriStringRenderer", false, true), (ValueHandlingInfo[])null)}), (boolean)0);
   }
}
