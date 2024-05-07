package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DelegateTubelineDeploymentListener implements TubelineDeploymentListener {
   private static ThreadLocal<List<TubelineDeploymentListener>> clientDelegates = new ThreadLocal();
   private static ThreadLocal<List<TubelineDeploymentListener>> serverDelegates = new ThreadLocal();

   public static void registerClientDeployementListener(TubelineDeploymentListener var0) {
      Object var1 = (List)clientDelegates.get();
      if (var1 == null) {
         var1 = new ArrayList();
         clientDelegates.set(var1);
      }

      ((List)var1).add(var0);
   }

   public static void registerServerDeploymentListener(TubelineDeploymentListener var0) {
      Object var1 = (List)serverDelegates.get();
      if (var1 == null) {
         var1 = new ArrayList();
         serverDelegates.set(var1);
      }

      ((List)var1).add(var0);
   }

   public void createClient(ClientTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      List var3 = (List)clientDelegates.get();
      if (var3 != null && !var3.isEmpty()) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            TubelineDeploymentListener var5 = (TubelineDeploymentListener)var4.next();
            var5.createClient(var1, var2);
         }

      }
   }

   public void createServer(ServerTubeAssemblerContext var1, Set<TubelineAssemblerItem> var2) {
      List var3 = (List)serverDelegates.get();
      if (var3 != null && !var3.isEmpty()) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            TubelineDeploymentListener var5 = (TubelineDeploymentListener)var4.next();
            var5.createServer(var1, var2);
         }

      }
   }
}
