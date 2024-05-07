package weblogic.wsee.jaxws.owsm;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.ws.WebServiceException;
import weblogic.j2ee.descriptor.wl.OwsmSecurityPolicyBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.webservice.WebServiceLogger;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.ListenerUsage;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.mtom.internal.MtomXopServerHandler;

public class ServerTubelineDeploymentListener extends TubelineDeploymentListener {
   private static Class handlerClass;
   private static final String ENABLED = "enabled";
   private List<String> preceding = Collections.singletonList("PRE_INVOKE_HANDLER");
   private List<String> following = Collections.singletonList("CONNECTION_HANDLER");

   public ServerTubelineDeploymentListener() {
      super(ServerTubelineDeploymentListener.class, ListenerUsage.SERVER_ONLY);
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (this.hasSecurityPolicy(var1)) {
         if (var1.getWsdlModel() == null) {
            String var3 = WebServiceLogger.logOWSMPolicyWithNoWsdlLoggable().getMessage();
            throw new IllegalStateException(var3);
         }

         handlerClass = this.getHandler(var1).getClass();
         this.setServer(var1);
         super.createServer(var1, var2);
      }

   }

   protected boolean hasSecurityPolicy(ServerTubeAssemblerContext var1) {
      EnvironmentFactory var2 = this.getEnvironmentFactory(var1);
      List var3 = this.getSecurityPolicyURIs(var2);
      return var3 != null && !var3.isEmpty();
   }

   protected SecurityPolicyHandler getHandler(ServerTubeAssemblerContext var1) throws WebServiceException {
      ClassLoader var2 = var1.getEndpoint().getImplementationClass().getClassLoader();
      return this.loadHandler(var2);
   }

   private void setServer(ServerTubeAssemblerContext var1) {
      WSBinding var2 = var1.getEndpoint().getBinding();
      ServerEnvironmentFeature var3 = new ServerEnvironmentFeature();
      ((WebServiceFeatureList)var2.getFeatures()).add(var3);
   }

   Class getHandlerClass() {
      return handlerClass;
   }

   protected List<String> getPrecedingHandlers() {
      return this.preceding;
   }

   protected List<String> getFollowingHandlers() {
      return this.following;
   }

   protected void setProperties(EnvironmentFactory var1, Map var2) {
      WSBinding var3 = var1.getBinding();
      List var4 = this.getSecurityPolicyURIs(var1);
      var2.put("weblogic.wsee.jaxws.owsm.SecurityPolicyURIList", var4);
      if (var3.getFeature(ServerEnvironmentFeature.class) != null) {
         var2.put("weblogic.wsee.jaxws.owsm.Server", true);
      }

      this.setAdditionalOwsmProperties(var1, var2, var4, false);
   }

   private List<String> getSecurityPolicyURIs(EnvironmentFactory var1) {
      List var2 = this.getSecurityPolicyFeatureURIs(var1);
      this.addDDPolicies(var1, var2);
      return var2;
   }

   private void addDDPolicies(EnvironmentFactory var1, List<String> var2) {
      DeployInfo var3 = (DeployInfo)var1.getContainer().getSPI(DeployInfo.class);
      WebservicePolicyRefBean var4 = var3 != null ? var3.getPolicyRef() : null;
      if (var4 != null) {
         WSDLPort var5 = var1.getPort();
         if (var5 != null) {
            String var6 = var1.getPort().getName().getLocalPart();
            PortPolicyBean[] var7 = var4.getPortPolicy();
            if (var7 != null) {
               PortPolicyBean[] var8 = var7;
               int var9 = var7.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  PortPolicyBean var11 = var8[var10];
                  if (var6.equals(var11.getPortName())) {
                     OwsmSecurityPolicyBean[] var12 = var11.getOwsmSecurityPolicy();
                     if (var12 != null) {
                        OwsmSecurityPolicyBean[] var13 = var12;
                        int var14 = var12.length;

                        for(int var15 = 0; var15 < var14; ++var15) {
                           OwsmSecurityPolicyBean var16 = var13[var15];
                           if (var16.getStatus().equals("enabled")) {
                              var2.add(var16.getUri());
                           } else {
                              var2.remove(var16.getUri());
                           }
                        }
                     }
                  }
               }
            }

         }
      }
   }

   List getFollowingXopHandlers() {
      return Collections.singletonList("CONNECTION_HANDLER");
   }

   List getPrecedingXopHandlers() {
      return Collections.singletonList("OWSM_SECURITY_POLICY_HANDLER");
   }

   HandlerInfo getXopHandlerInfo() {
      return new HandlerInfo(MtomXopServerHandler.class, new HashMap(), (QName[])null);
   }
}
