package weblogic.application.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationLifecycleListener;

public final class ApplicationEventManager {
   private List<ApplicationLifecycleListenerFactory> factories = new ArrayList();
   private static ApplicationEventManager instance = new ApplicationEventManager();

   private ApplicationEventManager() {
   }

   public static ApplicationEventManager getInstance() {
      return instance;
   }

   public void registerListenerFactory(ApplicationLifecycleListenerFactory var1) {
      this.factories.add(var1);
   }

   public List<ApplicationLifecycleListener> createListeners(ApplicationContext var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.factories.iterator();

      while(var3.hasNext()) {
         ApplicationLifecycleListenerFactory var4 = (ApplicationLifecycleListenerFactory)var3.next();
         ApplicationLifecycleListener var5 = var4.createListener(var1);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }
}
