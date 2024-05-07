package weblogic.wsee.security.wssc.sct;

import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;

public class SCTStore {
   private static final Logger LOGGER = Logger.getLogger(SCTStore.class.getName());
   public static final String WSSC_STORE_NAME_PREFIX = "weblogic.wsee.wssc.store";
   public static final String WSSC_SERVER_STORE_NAME = ".server";
   public static final String WSSC_CLIENT_STORE_NAME = ".client";
   private static WsStorage rpcServerStore;
   private static WsStorage rpcStore;

   private static void initializeRPCStores() {
      if (rpcServerStore == null) {
         rpcServerStore = WsStorageFactory.getStorage("weblogic.wsee.wssc.store.server", new SCTObjectHandler());
         rpcStore = WsStorageFactory.getStorage("weblogic.wsee.wssc.store.client", new SCTObjectHandler());
      }
   }

   public static SCCredential get(String var0, String var1) {
      WsStorage var2 = getServerStore(var1);
      WsStorage var3 = getClientStore(var1);

      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Trying to get SCT '" + var0 + "' in in-memory serverStore");
         }

         SCCredential var4 = (SCCredential)var2.get(var0);
         if (var4 == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Couldn't find SCT '" + var0 + "' in in-memory serverStore. Trying in-memory clientStore");
            }

            var4 = (SCCredential)var3.get(var0);
            if (var4 == null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Couldn't find SCT '" + var0 + "' in in-memory clientStore. Trying persistent serverStore");
               }

               var4 = (SCCredential)var2.persistentGet(var0);
               if (var4 == null) {
                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Couldn't find SCT '" + var0 + "' in persistent serverStore(" + var1 + "). Trying persistent clientStore");
                  }

                  var4 = (SCCredential)var3.persistentGet(var0);
                  if (var4 == null) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Couldn't find SCT '" + var0 + "' in persistent clientStore(" + var1 + "). Giving up.");
                     }
                  } else if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Found SCT '" + var0 + "' in persistent clientStore(" + var1 + ").");
                  }
               } else if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Found SCT '" + var0 + "' in persistent serverStore(" + var1 + ").");
               }
            } else if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Found SCT '" + var0 + "' in in-memory clientStore.");
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Found SCT '" + var0 + "' in in-memory serverStore");
         }

         return var4;
      } catch (PersistentStoreException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   public static SCCredential getFromServer(String var0, String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Trying to get SCT '" + var0 + "' from server store with physicalStoreName '" + var1 + "'");
      }

      WsStorage var2 = getServerStore(var1);
      return getFromStore(var2, var0);
   }

   public static SCCredential getFromClient(String var0, String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Trying to get SCT '" + var0 + "' from client store with physicalStoreName '" + var1 + "'");
      }

      WsStorage var2 = getClientStore(var1);
      return getFromStore(var2, var0);
   }

   private static SCCredential getFromStore(WsStorage var0, String var1) {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Trying to get SCT '" + var1 + "' from store '" + var0.getName() + "'");
         }

         SCCredential var2 = (SCCredential)var0.get(var1);
         if (var2 == null) {
            var2 = (SCCredential)var0.persistentGet(var1);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine((var2 == null ? "Couldn't find" : "Found") + " SCT '" + var1 + "' in store '" + var0.getName() + "'");
         }

         return var2;
      } catch (PersistentStoreException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static boolean addToServer(SCCredential var0, boolean var1, String var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Adding SCT '" + var0.getIdentifier() + "' to server SCT store with physicalStoreName '" + var2 + "'");
      }

      WsStorage var3 = getServerStore(var2);
      return addToStore(var3, var0, var1);
   }

   private static WsStorage getClientStore(String var0) {
      if (var0 == null) {
         initializeRPCStores();
         return rpcStore;
      } else {
         return WsStorageFactory.getStorage("weblogic.wsee.wssc.store.client" + var0, new SCTObjectHandler());
      }
   }

   private static WsStorage getServerStore(String var0) {
      if (var0 == null) {
         initializeRPCStores();
         return rpcServerStore;
      } else {
         return WsStorageFactory.getStorage("weblogic.wsee.wssc.store.server" + var0, new SCTObjectHandler());
      }
   }

   public static boolean addToClient(SCCredential var0, boolean var1, String var2) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Adding SCT '" + var0.getIdentifier() + "' to client SCT store with physicalStoreName '" + var2 + "'");
      }

      WsStorage var3 = getClientStore(var2);
      return addToStore(var3, var0, var1);
   }

   private static boolean addToStore(WsStorage var0, SCCredential var1, boolean var2) {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Adding SCT '" + var1.getIdentifier() + "' to store '" + var0.getName() + "'");
         }

         return var2 ? var0.put(var1.getIdentifier(), var1) : var0.persistentPut(var1.getIdentifier(), var1);
      } catch (PersistentStoreException var4) {
         return false;
      }
   }

   public static boolean remove(String var0, String var1) {
      return removeFromServer(var0, var1) || removeFromClient(var0, var1);
   }

   public static boolean removeFromServer(String var0, String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Removing SCT '" + var0 + "' from server SCT store with physicalStoreName '" + var1 + "'");
      }

      WsStorage var2 = getServerStore(var1);
      return removeFromStore(var2, var0);
   }

   public static boolean removeFromClient(String var0, String var1) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Removing SCT '" + var0 + "' from client SCT store with physicalStoreName '" + var1 + "'");
      }

      WsStorage var2 = getClientStore(var1);
      return removeFromStore(var2, var0);
   }

   private static boolean removeFromStore(WsStorage var0, String var1) {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Removing SCT '" + var1 + "' from store '" + var0.getName() + "'");
         }

         Object var2 = var0.get(var1);
         return var2 != null ? var0.remove(var1) : var0.persistentRemove(var1);
      } catch (PersistentStoreException var3) {
         return false;
      }
   }
}
