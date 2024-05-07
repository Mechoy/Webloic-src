package weblogic.diagnostics.instrumentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DelegatingMonitorControl extends StandardMonitorControl implements DelegatingMonitor {
   static final long serialVersionUID = -3716624702862137600L;
   private List actionList;
   private int locationType;
   private String[] compatibleActionTypes;
   private static final DiagnosticAction[] EMPTY_ACTIONS_ARR = new DiagnosticAction[0];
   private volatile DiagnosticAction[] cachedActions;

   public DelegatingMonitorControl(DelegatingMonitorControl var1) {
      super((StandardMonitorControl)var1);
      this.setLocationType(var1.getLocationType());
      this.setCompatibleActionTypes(var1.getCompatibleActionTypes());
   }

   public DelegatingMonitorControl(String var1) {
      this("", var1);
   }

   public DelegatingMonitorControl(String var1, String var2) {
      super(var1, var2);
      this.actionList = null;
      this.cachedActions = EMPTY_ACTIONS_ARR;
   }

   public DiagnosticAction[] getActions() {
      return this.cachedActions;
   }

   private synchronized void computeCachedActions() {
      DiagnosticAction[] var1 = EMPTY_ACTIONS_ARR;
      int var2 = this.actionList != null ? this.actionList.size() : 0;
      if (var2 > 0) {
         var1 = new DiagnosticAction[var2];
         var1 = (DiagnosticAction[])((DiagnosticAction[])this.actionList.toArray(var1));
      }

      this.cachedActions = var1;
   }

   public String[] getCompatibleActionTypes() {
      return this.compatibleActionTypes;
   }

   public void setCompatibleActionTypes(String[] var1) {
      this.compatibleActionTypes = var1;
   }

   public int getLocationType() {
      return this.locationType;
   }

   public void setLocationType(int var1) {
      this.locationType = var1;
   }

   public synchronized void addAction(DiagnosticAction var1) throws DuplicateActionException, IncompatibleActionException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Adding action of type " + var1.getType() + " to " + this.getName());
      }

      if (this.actionList == null) {
         this.actionList = new ArrayList();
      }

      if (this.actionList.contains(var1)) {
         throw new DuplicateActionException("Action " + var1 + " already attached to " + this.getType());
      } else {
         String[] var2 = this.getCompatibleActionTypes();
         int var3 = var2 != null ? var2.length : 0;
         boolean var4 = false;
         String var5 = var1.getType();

         for(int var6 = 0; !var4 && var6 < var3; ++var6) {
            if (var5.equals(var2[var6])) {
               var4 = true;
            }
         }

         if (!var4) {
            throw new IncompatibleActionException("Attempt to use incompatible action type " + var5 + " with delegating monitor type " + this.getType());
         } else {
            this.actionList.add(var1);
            if (var1.requiresArgumentsCapture()) {
               if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
                  InstrumentationDebug.DEBUG_CONFIG.debug("setting arguments capture flag to true");
               }

               this.argumentsCaptureNeeded = true;
            }

            var1.setDiagnosticMonitor(this);
            this.computeCachedActions();
         }
      }
   }

   public synchronized void removeAllActions() {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Removing all actions from " + this.getName());
      }

      if (this.actionList != null) {
         this.actionList.clear();
         this.actionList = null;
      }

      this.argumentsCaptureNeeded = false;
      this.computeCachedActions();
   }

   public synchronized void removeAction(DiagnosticAction var1) throws ActionNotFoundException {
      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("Removing action of type " + var1.getType() + " from " + this.getName());
      }

      if (this.actionList != null && this.actionList.contains(var1)) {
         this.actionList.remove(var1);
         if (this.argumentsCaptureNeeded) {
            this.checkArgsCaptureNeeded();
         }

         this.computeCachedActions();
      } else {
         throw new ActionNotFoundException("Attempt to remove non-existent action " + var1 + " from " + this.getType());
      }
   }

   public synchronized boolean merge(DiagnosticMonitorControl var1) {
      boolean var2 = false;
      if (super.merge(var1) && var1 instanceof DelegatingMonitorControl) {
         DelegatingMonitorControl var3 = (DelegatingMonitorControl)var1;
         this.actionList = var3.actionList;
         if (this.actionList != null) {
            Iterator var4 = this.actionList.iterator();

            while(var4.hasNext()) {
               DiagnosticAction var5 = (DiagnosticAction)var4.next();
               var5.setDiagnosticMonitor(this);
            }
         }

         this.locationType = var3.locationType;
         this.checkArgsCaptureNeeded();
         var2 = true;
         this.computeCachedActions();
      }

      return var2;
   }

   private void checkArgsCaptureNeeded() {
      if (this.actionList != null) {
         Iterator var1 = this.actionList.iterator();

         while(var1.hasNext()) {
            if (((DiagnosticAction)var1.next()).requiresArgumentsCapture()) {
               this.argumentsCaptureNeeded = true;
               return;
            }
         }
      }

      if (InstrumentationDebug.DEBUG_CONFIG.isDebugEnabled()) {
         InstrumentationDebug.DEBUG_CONFIG.debug("setting arguments capture flag to false");
      }

      this.argumentsCaptureNeeded = false;
   }

   void subvertArgumentsCaptureNeededCheck() {
      this.argumentsCaptureNeeded = true;
   }
}
