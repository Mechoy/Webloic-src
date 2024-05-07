package weblogic.wsee.security.wssp.tube;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.Tube;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import weblogic.wsee.jaxws.WLSContainer;
import weblogic.wsee.jaxws.framework.jaxrpc.EnvironmentFactory;
import weblogic.wsee.jaxws.framework.jaxrpc.JAXRPCEnvironmentFeature;
import weblogic.wsee.jaxws.tubeline.AbstractTubeFactory;
import weblogic.wsee.jaxws.tubeline.TubelineAssemblerItem;
import weblogic.wsee.jaxws.tubeline.TubelineDeploymentListener;
import weblogic.wsee.security.wssc.utils.WSSCCompatibilityUtil;

public class WSTHeuristicServerTubelineDeploymentListener implements TubelineDeploymentListener {
   public static final String TUBE_NAME = "WS_TRUST_HEURISTIC_HANDLER";

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      if (WSSCCompatibilityUtil.isHeuristicCompatibility()) {
         TubelineAssemblerItem var3 = new TubelineAssemblerItem("WS_TRUST_HEURISTIC_HANDLER", new AbstractTubeFactory() {
            public Tube createServer(Tube var1, ServerTubeAssemblerContext var2) {
               if (var2.getEndpoint().getContainer() instanceof WLSContainer) {
                  EnvironmentFactory var3 = JAXRPCEnvironmentFeature.getFactory(var2.getEndpoint());
                  WSTHeuristicTube var4 = new WSTHeuristicTube(var3, var1);
                  return var4;
               } else {
                  return var1;
               }
            }
         });
         HashSet var4 = new HashSet(this.getPrecedingWstHeuristicNames());
         HashSet var5 = new HashSet(this.getFollowingWstHeuristicNames());
         var3.setGoBefore(var4);
         var3.setGoAfter(var5);
         var2.add(var3);
      }
   }

   private List getPrecedingWstHeuristicNames() {
      return Arrays.asList((Object[])(new String[]{"CONNECTION_HANDLER", "FORWARDING_HANDLER"}));
   }

   private List getFollowingWstHeuristicNames() {
      return Arrays.asList((Object[])(new String[]{"WS_TRUST_POLICY_HANDLER"}));
   }
}
