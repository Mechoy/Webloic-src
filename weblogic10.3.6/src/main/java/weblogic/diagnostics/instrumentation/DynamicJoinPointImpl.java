package weblogic.diagnostics.instrumentation;

import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;

public class DynamicJoinPointImpl implements DynamicJoinPoint {
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   private JoinPoint delegateJp;
   private String monitorType = null;
   private Object[] unrenderedArgs;
   private Object unrenderedRetVal;
   private boolean argumentsSensitive = false;

   public DynamicJoinPointImpl(JoinPoint var1, Object[] var2, Object var3) {
      this.delegateJp = var1;
      this.argumentsSensitive = InstrumentationSupport.isSensitiveArray(var2);
      if (var1.getPointcutHandlingInfoMap() == null) {
         if (!this.argumentsSensitive) {
            this.unrenderedArgs = InstrumentationSupport.toSensitive(var2);
            this.argumentsSensitive = true;
         }

         String var4 = var1.getMethodDescriptor();
         if (var4 != null && var4.endsWith(")V")) {
            this.unrenderedRetVal = null;
         } else {
            this.unrenderedRetVal = "*****";
         }
      } else {
         this.unrenderedArgs = var2;
         this.unrenderedRetVal = var3;
      }

   }

   public String getModuleName() {
      return this.delegateJp.getModuleName();
   }

   public String getSourceFile() {
      return this.delegateJp.getSourceFile();
   }

   public String getClassName() {
      return this.delegateJp.getClassName();
   }

   public String getMethodName() {
      return this.delegateJp.getMethodName();
   }

   public String getMethodDescriptor() {
      return this.delegateJp.getMethodDescriptor();
   }

   public int getLineNumber() {
      return this.delegateJp.getLineNumber();
   }

   public Object[] getArguments() {
      if (this.unrenderedArgs == null) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("DynamicJoinPointImpl.getArguments() for " + this.monitorType + " unrenderedArgs is null, return null");
         }

         return null;
      } else if (this.argumentsSensitive) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("DynamicJoinPointImpl.getArguments() for " + this.monitorType + " handling info map is null, return pre-processed sensitive args");
         }

         return this.unrenderedArgs;
      } else {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("DynamicJoinPointImpl.getArguments() for " + this.monitorType + " render args for return using info map");
         }

         Map var1 = this.delegateJp.getPointcutHandlingInfoMap();
         return ValueRenderingManager.renderArgumentValues(this.monitorType, !this.delegateJp.isStatic(), this.unrenderedArgs, var1);
      }
   }

   public void setArguments(Object[] var1) {
      this.argumentsSensitive = InstrumentationSupport.isSensitiveArray(var1);
      if (!this.argumentsSensitive && this.delegateJp.getPointcutHandlingInfoMap() == null) {
         this.unrenderedArgs = InstrumentationSupport.toSensitive(var1);
         this.argumentsSensitive = true;
      } else {
         this.unrenderedArgs = var1;
      }

   }

   public Object getReturnValue() {
      if (this.unrenderedRetVal == null) {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug("DynamicJoinPointImpl.getReturnValue() for " + this.monitorType + " unrenderedRetVal is null, return null");
         }

         return null;
      } else {
         Map var1 = this.delegateJp.getPointcutHandlingInfoMap();
         if (var1 == null) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("DynamicJoinPointImpl.getReturnValue() for " + this.monitorType + " handling info map is null, return null");
            }

            return this.unrenderedRetVal;
         } else {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug("DynamicJoinPointImpl.getReturnValue() for " + this.monitorType + " render return value and return");
            }

            return ValueRenderingManager.renderReturnValue(this.monitorType, this.unrenderedRetVal, var1);
         }
      }
   }

   public void setReturnValue(Object var1) {
      if (this.delegateJp.getPointcutHandlingInfoMap() == null) {
         this.unrenderedRetVal = "*****";
      } else {
         this.unrenderedRetVal = var1;
      }

   }

   public JoinPoint getDelegate() {
      return this.delegateJp;
   }

   public String toString() {
      return this.delegateJp.toString();
   }

   public GatheredArgument[] getGatheredArguments() {
      return this.delegateJp.getGatheredArguments(this.monitorType);
   }

   public boolean isReturnGathered() {
      return this.delegateJp.isReturnGathered(this.monitorType);
   }

   public GatheredArgument[] getGatheredArguments(String var1) {
      return this.delegateJp.getGatheredArguments(var1);
   }

   public boolean isReturnGathered(String var1) {
      return this.delegateJp.isReturnGathered(var1);
   }

   public Map<String, PointcutHandlingInfo> getPointcutHandlingInfoMap() {
      return this.delegateJp.getPointcutHandlingInfoMap();
   }

   public boolean isStatic() {
      return this.delegateJp.isStatic();
   }

   public void setMonitorType(String var1) {
      if (debugLog.isDebugEnabled()) {
         debugLog.debug("DynamicJoinPointImpl.setMonitorType(" + var1 + ")");
      }

      this.monitorType = var1;
   }
}
