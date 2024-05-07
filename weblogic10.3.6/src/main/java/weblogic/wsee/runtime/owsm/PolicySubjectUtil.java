package weblogic.wsee.runtime.owsm;

import com.sun.xml.ws.api.server.ContainerResolver;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.kernel.KernelStatus;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public final class PolicySubjectUtil {
   private static final boolean verbose = Verbose.isVerbose(PolicySubjectUtil.class);
   private static final String UNKNOWN = "unknown";

   public static String formatEndpointPortResourcePattern(String var0, String var1, String var2) {
      if (!KernelStatus.isServer()) {
         return null;
      } else {
         String var3 = ServerUtil.getDomainName();
         String var4 = ServerUtil.getServerName();
         String var5 = "unknown";
         String var6 = "unknowns";
         var1 = StringUtil.isEmpty(var1) ? "unknown" : var1;
         var0 = StringUtil.isEmpty(var0) ? "unknown" : var0;
         if (ClientContainerUtil.getContainingApplicationRuntime() != null) {
            var5 = ClientContainerUtil.getContainingApplicationRuntime().getApplicationName();
         }

         ComponentRuntimeMBeanImpl var7 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(var2);
         if (var7 instanceof WebAppComponentRuntimeMBean) {
            var6 = "WEBs";
         } else if (var7 instanceof EJBComponentRuntimeMBean) {
            var6 = "EJBs";
         }

         if (StringUtil.isEmpty(var2)) {
            var2 = var7 instanceof WebAppComponentRuntimeMBean ? ((WebAppComponentRuntimeMBean)var7).getModuleURI() : ClientContainerUtil.getContainingModuleName();
         }

         StringBuilder var8 = new StringBuilder();
         var8.append('/').append(var3).append('/').append(var4).append('/').append(var5).append('/').append(var6).append('/').append(var2).append('/').append("WLSWEBSERVICEs").append('/').append(var1).append('/').append("PORTs").append('/').append(var0);
         String var9 = rmDoubleForward(var8.toString());
         if (verbose) {
            Verbose.log((Object)("PolicySubjectUtil[" + var1 + ", " + var0 + "] with pattern " + var9));
         }

         return var9;
      }
   }

   public static String formatReferencedPortResourcePattern(String var0, String var1) {
      String var2;
      if (!KernelStatus.isServer()) {
         var2 = "/null/null/null/JSEs/null/WLSWEBSERVICECLIENTs/" + var1 + "/PORTs/" + var0;
      } else {
         String var3 = ServerUtil.getDomainName();
         String var4 = ServerUtil.getServerName();
         String var5 = "unknown";
         String var6 = "unknowns";
         var1 = StringUtil.isEmpty(var1) ? "unknown" : var1;
         var0 = StringUtil.isEmpty(var0) ? "unknown" : var0;
         String var7 = null;
         if (ClientContainerUtil.getContainingApplicationRuntime() != null) {
            var5 = ClientContainerUtil.getContainingApplicationRuntime().getApplicationName();
         }

         ComponentRuntimeMBeanImpl var8 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class) == null ? null : ((DeployInfo)ContainerResolver.getInstance().getContainer().getSPI(DeployInfo.class)).getModuleName());
         if (var8 instanceof WebAppComponentRuntimeMBean) {
            var6 = "WEBs";
         } else if (var8 instanceof EJBComponentRuntimeMBean) {
            var6 = "EJBs";
         }

         if (StringUtil.isEmpty(var7)) {
            var7 = var8 instanceof WebAppComponentRuntimeMBean ? ((WebAppComponentRuntimeMBean)var8).getModuleURI() : ClientContainerUtil.getContainingModuleName();
         }

         StringBuilder var9 = new StringBuilder();
         var9.append('/').append(var3).append('/').append(var4).append('/').append(var5).append('/').append(var6).append('/').append(var7).append('/').append("WLSWEBSERVICECLIENTs").append('/').append(var1).append('/').append("PORTs").append('/').append(var0);
         var2 = var9.toString();
         if (verbose) {
            Verbose.log((Object)("PolicySubjectUtil[" + var1 + ", " + var0 + "] with pattern " + var2));
         }
      }

      return rmDoubleForward(var2);
   }

   private static String rmDoubleForward(String var0) {
      while(var0.contains("//")) {
         var0 = var0.replace("//", "/");
      }

      return var0;
   }
}
