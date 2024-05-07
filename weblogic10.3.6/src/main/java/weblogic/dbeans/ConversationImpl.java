package weblogic.dbeans;

import java.util.Collection;
import java.util.Map;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.application.ModuleWrapper;
import weblogic.dbeans.internal.QueryImpl;
import weblogic.ejb.container.cache.EntityCache;
import weblogic.ejb.container.deployer.EJBModule;
import weblogic.ejb.container.manager.BaseEntityManager;

public class ConversationImpl implements Conversation {
   private String name = null;
   private String objectSchemaName = null;
   private EJBModule module = null;
   private Map class2manager = null;
   private EntityCache cache = null;

   public ConversationImpl() {
      ApplicationAccess var1 = ApplicationAccess.getApplicationAccess();
      ApplicationContextInternal var2 = var1.getCurrentApplicationContext();
      Module[] var3 = var2.getApplicationModules();
      EJBModule var4 = null;
      int var5 = 0;
      String var6 = "";

      for(int var7 = 0; var7 < var3.length; ++var7) {
         if (var3[var7] instanceof ModuleWrapper) {
            Module var8 = ((ModuleWrapper)var3[var7]).unwrap();
            if (var8 instanceof EJBModule) {
               EJBModule var9 = (EJBModule)var8;
               if (var9.getObjectSchemaName() != null) {
                  ++var5;
                  if (var5 > 1) {
                     var6 = var6 + ", ";
                  }

                  var6 = var6 + var9.getObjectSchemaName();
                  var4 = var9;
               }
            }
         }
      }

      if (var5 > 1) {
         throw new DataBeansException("Application contains multiple object schemas: " + var6 + ".  You must pass the Conversation an object schema " + "name when it is created.");
      } else if (var5 == 0) {
         throw new DataBeansException("Application does not contain any DataBean object schemas.  Check that the DataBean modules deployed successfully.");
      } else {
         this.module = var4;
         this.class2manager = this.module.getDataBeansMap();
         this.cache = new EntityCache(this.name, Integer.MAX_VALUE);
      }
   }

   public ConversationImpl(String var1) {
      this.objectSchemaName = var1;
      ApplicationAccess var2 = ApplicationAccess.getApplicationAccess();
      ApplicationContextInternal var3 = var2.getCurrentApplicationContext();
      Module[] var4 = var3.getApplicationModules();
      EJBModule var5 = null;
      int var6 = 0;
      String var7 = "";

      for(int var8 = 0; var8 < var4.length; ++var8) {
         if (var4[var8] instanceof ModuleWrapper) {
            ModuleWrapper var9 = (ModuleWrapper)var4[var8];
            Module var10 = var9.unwrap();
            if (var10 instanceof EJBModule) {
               EJBModule var11 = (EJBModule)var10;
               if (var11.getObjectSchemaName() != null) {
                  ++var6;
                  if (var11.getObjectSchemaName().equals(var1)) {
                     var5 = var11;
                     break;
                  }

                  if (var6 > 1) {
                     var7 = var7 + ", ";
                  }

                  var7 = var7 + var11.getObjectSchemaName();
               }
            }
         }
      }

      if (var5 == null) {
         throw new DataBeansException("Application contains one or more object schemas: " + var7 + ", however, none of them match '" + var1 + "'.  Please use one of the object schemas listed.");
      } else if (var6 == 0) {
         throw new DataBeansException("Application does not contain any DataBean object schemas.  Check that the DataBean modules deployed successfully.");
      } else {
         this.module = var5;
         this.class2manager = this.module.getDataBeansMap();
         this.cache = new EntityCache(this.name, Integer.MAX_VALUE);
      }
   }

   public Query createSQLQuery(String var1) {
      BaseEntityManager var2 = (BaseEntityManager)this.class2manager.values().iterator().next();
      return new QueryImpl(this, var2, var1);
   }

   public Query createSQLQuery(String var1, Class var2, String var3) {
      BaseEntityManager var4 = (BaseEntityManager)this.class2manager.get(var2);
      if (var4 == null) {
         throw new DataBeansException("Unrecognized DataBean: " + var2.getName());
      } else {
         return new QueryImpl(this, var4, var1, var3);
      }
   }

   public Object findByPrimaryKey(Class var1, Object var2) {
      return null;
   }

   public Object getByPrimaryKey(Class var1, Object var2) {
      return null;
   }

   public Object create(Object var1) {
      return null;
   }

   public Collection create(Collection var1) {
      return null;
   }

   public Object update(Object var1) {
      return null;
   }

   public void update(Collection var1) {
   }

   public void remove(Object var1) {
   }

   public void remove(Collection var1) {
   }

   public void evict(Object var1) {
   }

   public void evict(Collection var1) {
   }

   public void refresh(Object var1) {
   }

   public void refresh(Collection var1) {
   }

   public void disconnect() {
   }

   public void reconnect() {
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public EntityCache getCache() {
      return this.cache;
   }
}
