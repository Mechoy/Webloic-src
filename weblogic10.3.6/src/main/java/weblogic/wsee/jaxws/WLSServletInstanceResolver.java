package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.WSWebServiceContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.xml.ws.WebServiceException;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.wsee.jaxws.injection.WSEEComponentContributor;

public class WLSServletInstanceResolver extends WLSInstanceResolver<Object> implements WLSInstanceResolver.Factory<Object> {
   private WSEEComponentContributor cc;
   private WebAppServletContext servletContext;
   private Class c;

   public WLSServletInstanceResolver(WSEEComponentContributor var1, WebAppServletContext var2, Class var3) {
      this.cc = var1;
      this.servletContext = var2;
      this.c = var3;
   }

   protected WLSInstanceResolver.Factory<Object> getFactory() {
      return this;
   }

   protected boolean isJsr250Needed() {
      return false;
   }

   public Object create() {
      try {
         return this.cc.newInstance(this.c);
      } catch (InstantiationException var2) {
         throw new WebServiceException(var2);
      } catch (IllegalAccessException var3) {
         throw new WebServiceException(var3);
      }
   }

   public void publishContext(WSWebServiceContext var1) {
      try {
         this.servletContext.getEnvironmentContext().bind("comp/WebServiceContext", var1);
      } catch (NameAlreadyBoundException var3) {
      } catch (NamingException var4) {
         throw new WebServiceException(var4);
      }

   }
}
