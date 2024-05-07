package weblogic.wsee.deploy;

import com.sun.xml.ws.api.server.InstanceResolver;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.xml.ws.WebServiceException;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.injection.PitchforkContext;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.jaxws.JAXWSWebAppServlet;
import weblogic.wsee.jaxws.WLSServletAdapterList;
import weblogic.wsee.jaxws.WLSServletInstanceResolver;
import weblogic.wsee.jaxws.injection.WSEEComponentContributor;
import weblogic.wsee.jaxws.injection.WSEEWebComponentContributor;
import weblogic.wsee.server.servlet.WebappWSServlet;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsFactory;
import weblogic.wsee.ws.WsService;

public class ServletDeployInfo extends DeployInfo {
   private static final String SERVLET_DEPLOY_INFO = "weblogic.wsee.ServletDeployInfo";
   private WSEEComponentContributor cc;

   ServletDeployInfo() {
   }

   public void setServiceURIs(String[] var1) {
      this.serviceURIs = var1;
   }

   public String getApplication() {
      String var1 = super.getApplication();
      if (var1 != null) {
         return var1;
      } else if (this.servletContext instanceof WebAppServletContext) {
         return ((WebAppServletContext)this.servletContext).getApplicationId();
      } else {
         throw new AssertionError("Unable to determine Application from ServletContext");
      }
   }

   public String getSecurityRealmName() {
      String var1 = super.getSecurityRealmName();
      if (var1 != null) {
         return null;
      } else if (this.servletContext instanceof WebAppServletContext) {
         return ((WebAppServletContext)this.servletContext).getSecurityRealmName();
      } else {
         throw new AssertionError("Unable to determine security realm from ServletContext");
      }
   }

   public String getContextPath() {
      String var1 = super.getContextPath();
      if (var1 != null) {
         return var1;
      } else if (this.servletContext instanceof WebAppServletContext) {
         return ((WebAppServletContext)this.servletContext).getContextPath();
      } else {
         throw new AssertionError("Unable to determine ContextPath from ServletContext");
      }
   }

   String getServlet() {
      return this.getWebServicesType() == WebServiceType.JAXWS ? JAXWSWebAppServlet.class.getName() : WebappWSServlet.class.getName();
   }

   public WsService createWsService() throws WsException {
      return WsFactory.instance().createServerService(this);
   }

   public void store(ServletContext var1) {
      var1.setAttribute("weblogic.wsee.ServletDeployInfo" + this.linkName, this);
   }

   public static ServletDeployInfo load(HttpServlet var0) {
      ServletContext var1 = var0.getServletContext();
      ServletDeployInfo var2 = (ServletDeployInfo)var1.getAttribute("weblogic.wsee.ServletDeployInfo" + var0.getServletName());
      var1.removeAttribute("weblogic.wsee.ServletDeployInfo" + var0.getServletName());
      return var2;
   }

   ServletAdapterList createServletAdapterList() {
      return new WLSServletAdapterList();
   }

   public InstanceResolver createInstanceResolver() {
      return new WLSServletInstanceResolver(this.loadComponentContributor(), (WebAppServletContext)this.servletContext, this.getJwsClass());
   }

   public WSEEComponentContributor loadComponentContributor() {
      if (this.cc == null) {
         List var1 = this.createServerHandlerChainsResolver().getMatchingHandlers();
         WebAppBean var2 = ((WebAppServletContext)this.servletContext).getWebAppModule().getWebAppBean();
         PitchforkContext var3 = new PitchforkContext((String)null);
         this.cc = new WSEEWebComponentContributor(var2, this.getJwsClass(), var1, var3);

         try {
            this.cc.init();
         } catch (Throwable var5) {
            throw new WebServiceException(var5);
         }
      }

      return this.cc;
   }
}
