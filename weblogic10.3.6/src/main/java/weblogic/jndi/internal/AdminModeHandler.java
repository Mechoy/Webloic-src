package weblogic.jndi.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import javax.naming.NameNotFoundException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

final class AdminModeHandler extends AbstractAdminModeHandler {
   private Hashtable listeners = new Hashtable();

   AdminModeHandler(ServerNamingNode var1) {
      super(var1);
   }

   private void addListener(ApplicationRuntimeMBean var1, String var2, AdminModeStateChangeListener var3) {
      if (this.debug) {
         NamingDebugLogger.debug("+++ AdminMode.addListener: name=" + var2 + ")");
      }

      AdminModeStateChangeListener var4 = (AdminModeStateChangeListener)this.listeners.put(var2, var3);
      if (var4 != null) {
         var1.removePropertyChangeListener(var4);
      }

      var1.addPropertyChangeListener(var3);
   }

   private void removeListener(String var1) {
      if (!this.listeners.isEmpty()) {
         AdminModeStateChangeListener var2 = (AdminModeStateChangeListener)this.listeners.remove(var1);
         if (var2 != null) {
            ApplicationRuntimeMBean var3 = ApplicationVersionUtils.getCurrentApplicationRuntime();
            if (var3 != null) {
               if (this.debug) {
                  NamingDebugLogger.debug("+++ AdminMode.removeListener: name=" + var1);
               }

               ((RuntimeMBeanDelegate)var3).removePropertyChangeListener(var2);
            }
         }
      }
   }

   void checkBind(String var1, boolean var2) {
      if (!var2) {
         ApplicationRuntimeMBean var3 = ApplicationVersionUtils.getCurrentApplicationRuntime();
         if (var3 != null) {
            this.addListener(var3, var1, new AdminModeStateChangeListener(var1, this));
            if (var3.getActiveVersionState() == 1) {
               this.setAdminMode(var1);
            }

         }
      }
   }

   void checkLookup(String var1, String var2, Hashtable var3) throws NameNotFoundException {
      if ((var2 == null || var2.length() <= 0) && !this.node.isVersioned() && this.isAdminMode(var1)) {
         if (this.debug) {
            NamingDebugLogger.debug("+++ AdminMode.checkLookup: Attempt to look up admin mode binding, name=" + var1 + "), " + "adminRequest=" + ApplicationVersionUtils.isAdminModeRequest());
         }

         if (!ApplicationVersionUtils.isAdminModeRequest()) {
            if (this.debug) {
               NamingDebugLogger.debug("+++ AdminMode.checkLookup failed", new Exception());
            }

            throw this.node.newNameNotFoundException(new AdminModeAccessException("Unable to resolve '" + var1 + "'. Resolved '" + this.node.getNameInNamespace() + "'"), var2, var3);
         }
      }
   }

   void checkUnbind(String var1, boolean var2) {
      if (!var2) {
         this.unsetAdminMode(var1);
         this.removeListener(var1);
      }
   }

   private final class AdminModeStateChangeListener implements PropertyChangeListener {
      String name;
      AdminModeHandler handler;

      private AdminModeStateChangeListener(String var2, AdminModeHandler var3) {
         this.name = var2;
         this.handler = var3;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equalsIgnoreCase("ActiveVersionState")) {
            Object var2 = var1.getOldValue();
            Object var3 = var1.getNewValue();
            if (var2 instanceof Integer && var3 instanceof Integer) {
               int var4 = (Integer)var2;
               int var5 = (Integer)var3;
               if (AdminModeHandler.this.debug) {
                  NamingDebugLogger.debug("+++ AdminModeStateChange for name=" + this.name + ", oldState=" + var4 + ", newState=" + var5);
               }

               if (var4 == var5) {
                  return;
               }

               if (var4 == 1) {
                  this.handler.unsetAdminMode(this.name);
               } else if (var5 == 1) {
                  this.handler.setAdminMode(this.name);
               }

               if (var5 == 0) {
                  this.handler.removeListener(this.name);
               }
            }
         }

      }

      // $FF: synthetic method
      AdminModeStateChangeListener(String var2, AdminModeHandler var3, Object var4) {
         this(var2, var3);
      }
   }
}
