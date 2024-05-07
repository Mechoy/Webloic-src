package weblogic.deployment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;

public class PersistenceUnitRegistryInitializer {
   private List<PersistenceUnitRegistryPrepareTask> prepareTasks = new ArrayList();

   public static synchronized PersistenceUnitRegistryInitializer getInstance(ApplicationContextInternal var0) {
      PersistenceUnitRegistryInitializer var1 = (PersistenceUnitRegistryInitializer)var0.getUserObject(PersistenceUnitRegistryInitializer.class);
      if (var1 == null) {
         var1 = new PersistenceUnitRegistryInitializer();
         var0.putUserObject(PersistenceUnitRegistryInitializer.class, var1);
      }

      return var1;
   }

   public synchronized void addPersistenceUnitRegistryPrepareTask(PersistenceUnitRegistryPrepareTask var1) {
      this.prepareTasks.add(var1);
   }

   public synchronized void setupPersistenceUnitRegistries() throws ModuleException {
      Iterator var1 = this.prepareTasks.iterator();

      while(var1.hasNext()) {
         PersistenceUnitRegistryPrepareTask var2 = (PersistenceUnitRegistryPrepareTask)var1.next();
         var2.execute();
      }

      this.prepareTasks.clear();
   }

   public interface PersistenceUnitRegistryPrepareTask {
      void execute() throws ModuleException;
   }
}
