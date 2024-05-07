package weblogic.wsee.jaxws.framework.jaxrpc;

import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.util.Set;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class TubelineDeploymentListener implements weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener {
   private WsDeploymentListener listener;
   private Class<? extends WsDeploymentListener> listenerClass;
   private ListenerUsage usage;

   public TubelineDeploymentListener(Class<? extends WsDeploymentListener> var1) {
      this(var1, ListenerUsage.BOTH);
   }

   public TubelineDeploymentListener(Class<? extends WsDeploymentListener> var1, ListenerUsage var2) {
      this.listenerClass = var1;
      this.usage = var2;
   }

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      BindingID var3 = var1.getBinding().getBindingId();
      if (var3.equals(BindingID.SOAP11_HTTP) || var3.equals(BindingID.SOAP12_HTTP) || var3.equals(BindingID.SOAP11_HTTP_MTOM) || var3.equals(BindingID.SOAP12_HTTP_MTOM) || var3.equals(BindingID.X_SOAP12_HTTP)) {
         if (!ListenerUsage.SERVER_ONLY.equals(this.usage)) {
            WSDLPort var4 = var1.getWsdlModel();
            if (var4 != null) {
               EnvironmentFactory var5 = this.getEnvironmentFactory(var1);
               WsDeploymentContext var6 = var5.getDeploymentContext();

               try {
                  HandlerList var7 = var5.buildClientHandlerList(var2);
                  String var8 = var4.getName().getLocalPart();
                  var6.getWsService().getPort(var8).setInternalHandlerList(var7);
                  this.getListener().process(var6);
               } catch (WsDeploymentException var9) {
                  throw new WebServiceException(var9);
               }
            }
         }
      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      BindingID var3 = var1.getEndpoint().getBinding().getBindingId();
      if ((var3.equals(BindingID.SOAP11_HTTP) || var3.equals(BindingID.SOAP12_HTTP) || var3.equals(BindingID.SOAP11_HTTP_MTOM) || var3.equals(BindingID.SOAP12_HTTP_MTOM) || var3.equals(BindingID.X_SOAP12_HTTP)) && var1.getWsdlModel() != null) {
         if (!ListenerUsage.CLIENT_ONLY.equals(this.usage)) {
            EnvironmentFactory var4 = this.getEnvironmentFactory(var1);
            WsDeploymentContext var5 = var4.getDeploymentContext();

            try {
               HandlerList var6 = var4.buildServerHandlerList(var2);
               String var7 = var1.getWsdlModel().getName().getLocalPart();
               var5.getWsService().getPort(var7).setInternalHandlerList(var6);
               this.getListener().process(var5);
            } catch (WsDeploymentException var8) {
               throw new WebServiceException(var8);
            }
         }
      }
   }

   protected EnvironmentFactory getEnvironmentFactory(ClientTubeAssemblerContext var1) {
      EnvironmentFactory var2 = JAXRPCEnvironmentFeature.getFactory(var1);
      return var2;
   }

   protected EnvironmentFactory getEnvironmentFactory(ServerTubeAssemblerContext var1) {
      EnvironmentFactory var2 = JAXRPCEnvironmentFeature.getFactory(var1.getEndpoint());
      return var2;
   }

   private WsDeploymentListener getListener() {
      if (this.listener == null) {
         try {
            this.listener = (WsDeploymentListener)this.listenerClass.newInstance();
         } catch (Exception var2) {
            throw new WebServiceException(var2);
         }
      }

      return this.listener;
   }
}
