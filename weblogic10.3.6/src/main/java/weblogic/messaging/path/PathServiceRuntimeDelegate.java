package weblogic.messaging.path;

import java.util.Iterator;
import java.util.LinkedList;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PSAssemblyRuntimeMBean;
import weblogic.management.runtime.PathServiceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.messaging.path.helper.KeyString;
import weblogic.store.xa.PersistentStoreXA;

public class PathServiceRuntimeDelegate extends RuntimeMBeanDelegate implements PathServiceRuntimeMBean {
   private transient PathServiceMap pathService;

   public PathServiceRuntimeDelegate(String var1, PathServiceMap var2) throws ManagementException {
      super(var1);
      this.pathService = var2;
   }

   public PSAssemblyRuntimeMBean[] getAssemblies() throws ManagementException {
      PersistentStoreXA var1 = this.pathService.getStore();
      Iterator var2 = var1.getMapConnectionNames();
      LinkedList var3 = new LinkedList();

      while(var2.hasNext()) {
         PathServiceMap var10000 = this.pathService;
         KeyString var4 = PathServiceMap.sampleKeyFromMapName((String)var2.next());
         if (var4 != null) {
            var3.add(new PSAssemblyRuntimeDelegate(var4, this, this.pathService));
         }
      }

      PSAssemblyRuntimeDelegate[] var5 = new PSAssemblyRuntimeDelegate[var3.size()];
      var3.toArray(var5);
      return var5;
   }
}
