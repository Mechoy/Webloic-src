package weblogic.diagnostics.instrumentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.i18n.DiagnosticsLogger;

public class ValueRenderingManager {
   private static boolean initialized = false;
   private static DebugLogger debugLog = DebugLogger.getDebugLogger("DebugDiagnosticDataGathering");
   public static final ValueRenderer defaultValueRenderer = new DefaultValueRenderer();
   private static Map<Class, ValueRenderer> typeBasedValueRendererMap = new HashMap();
   private static Map<String, ValueRenderer> rendererMap = new HashMap();

   public static Object renderReturnValue(String var0, Object var1, Map<String, PointcutHandlingInfo> var2) {
      if (var0 != null && var2 != null) {
         if (var1 == null) {
            return null;
         } else {
            PointcutHandlingInfo var3 = (PointcutHandlingInfo)var2.get(var0);
            if (var3 == null) {
               if (debugLog.isDebugEnabled()) {
                  debugLog.debug(" phInfo null, make args sensitive");
               }

               return "*****";
            } else {
               return renderValue(var1, var3.getReturnValueHandlingInfo());
            }
         }
      } else {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug(" renderReturnValue sensitive, monitorType = " + var0 + " infoMap = " + var2);
         }

         return "*****";
      }
   }

   public static Object[] renderArgumentValues(String var0, boolean var1, Object[] var2, Map<String, PointcutHandlingInfo> var3) {
      if (var2 != null && var2.length != 0) {
         if (var0 != null && var3 != null) {
            PointcutHandlingInfo var4 = (PointcutHandlingInfo)var3.get(var0);
            if (var4 == null) {
               if (debugLog.isDebugEnabled()) {
                  debugLog.debug(" phInfo null, make args sensitive");
               }

               return InstrumentationSupport.toSensitive(var2);
            } else {
               ValueHandlingInfo var5 = var4.getClassValueHandlingInfo();
               ValueHandlingInfo[] var6 = var4.getArgumentValueHandlingInfo();
               if (var6 == null && (!var1 || var1 && var5 == null)) {
                  if (debugLog.isDebugEnabled()) {
                     debugLog.debug(" sensitive 2 argInfos = " + var6 + " renderClass = " + var1 + " classInfo = " + var5);
                  }

                  return InstrumentationSupport.toSensitive(var2);
               } else {
                  Object[] var7 = new Object[var2.length];
                  int var8 = 0;
                  if (var1) {
                     var7[var8++] = renderValue(var2[0], var5);
                     if (debugLog.isDebugEnabled()) {
                        debugLog.debug(" renderClass in = " + var2[0] + " out = " + var7[0]);
                     }
                  }

                  for(int var9 = 0; var8 < var2.length; ++var8) {
                     if (var6 == null) {
                        if (debugLog.isDebugEnabled()) {
                           debugLog.debug(" argInfos null sensitive " + var8);
                        }

                        var7[var8] = "*****";
                     } else {
                        var7[var8] = renderValue(var2[var8], var6[var9++]);
                        if (debugLog.isDebugEnabled()) {
                           debugLog.debug(" renderClass in = " + var2[var8] + " out = " + var7[var8]);
                        }
                     }
                  }

                  return var7;
               }
            }
         } else {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug(" sensitive 1 monType = " + var0 + " infoMap = " + var3);
            }

            return InstrumentationSupport.toSensitive(var2);
         }
      } else {
         return var2;
      }
   }

   public static Object renderValue(Object var0, ValueHandlingInfo var1) {
      if (debugLog.isDebugEnabled()) {
         debugLog.debug("renderValue(), value = " + var0 + " valInfo = " + var1);
      }

      if (var1 != null && !var1.isSensitive()) {
         if (var0 == null) {
            if (debugLog.isDebugEnabled()) {
               debugLog.debug(" renderValue null value");
            }

            return null;
         } else {
            String var2 = var1.getRendererClassName();
            ValueRenderer var3 = null;
            if (var2 != null) {
               var3 = ((ValueHandlingInfoImpl)var1).getValueRenderer();
               if (var3 == null) {
                  if (debugLog.isDebugEnabled()) {
                     debugLog.debug(" renderValue find renderer by class");
                  }

                  var3 = findRendererByClassName(var2, var0.getClass());
                  ((ValueHandlingInfoImpl)var1).setValueRenderer(var3);
               }
            } else {
               if (debugLog.isDebugEnabled()) {
                  debugLog.debug(" renderValue find renderer by type or default");
               }

               var3 = findRendererByTypeClass(var0.getClass());
            }

            return var3.render(var0);
         }
      } else {
         if (debugLog.isDebugEnabled()) {
            debugLog.debug(" renderValue sensitive, valInfo = " + var1);
         }

         return "*****";
      }
   }

   private static ValueRenderer findRendererByClassName(String var0, Class var1) {
      if (var0 != null && var0.length() != 0) {
         ValueRenderer var2 = (ValueRenderer)rendererMap.get(var0);
         if (var2 == null) {
            Class var3 = ValueRenderingManager.class;
            synchronized(ValueRenderingManager.class) {
               var2 = (ValueRenderer)rendererMap.get(var0);
               if (var2 == null) {
                  try {
                     Class var4 = Class.forName(var0);
                     var2 = (ValueRenderer)var4.newInstance();
                  } catch (Exception var6) {
                     var2 = findRendererByTypeClass(var1);
                     DiagnosticsLogger.logCannotLoadRenderer(var0, var6, var2.getClass().getName());
                  }

                  rendererMap.put(var0, var2);
               }
            }
         }

         return var2;
      } else {
         return defaultValueRenderer;
      }
   }

   private static ValueRenderer findRendererByTypeClass(Class var0) {
      if (var0 != null && typeBasedValueRendererMap.size() != 0) {
         ValueRenderer var1 = (ValueRenderer)typeBasedValueRendererMap.get(var0);
         if (var1 == null) {
            Class var2 = ValueRenderingManager.class;
            synchronized(ValueRenderingManager.class) {
               var1 = (ValueRenderer)typeBasedValueRendererMap.get(var0);
               if (var1 == null) {
                  Iterator var3 = getCandidateClasses(var0).iterator();

                  while(var3.hasNext()) {
                     Class var4 = (Class)var3.next();
                     var1 = (ValueRenderer)typeBasedValueRendererMap.get(var0);
                     if (var1 != null) {
                        break;
                     }
                  }

                  if (var1 == null) {
                     var1 = defaultValueRenderer;
                  }

                  typeBasedValueRendererMap.put(var0, var1);
               }
            }
         }

         return var1;
      } else {
         return defaultValueRenderer;
      }
   }

   private static List<Class> getCandidateClasses(Class var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      } else if (var0.isPrimitive()) {
         var1.add(var0);
         return var1;
      } else {
         Class[] var2 = var0.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class var5 = var2[var4];
            if (var5 != null) {
               Class var6 = var5.getClass();
               if (!var6.equals(Class.class) && !var1.contains(var6)) {
                  var1.add(var6);
                  var1.addAll(getCandidateClasses(var6));
               }
            }
         }

         Class var7 = var0.getSuperclass();
         if (var7 != null) {
            var1.addAll(getCandidateClasses(var7));
         }

         return var1;
      }
   }

   public static synchronized void initialize(Map<String, String> var0) {
      if (!initialized) {
         initialized = true;
         if (var0 != null) {
            Iterator var1 = var0.entrySet().iterator();

            while(var1.hasNext()) {
               Map.Entry var2 = (Map.Entry)var1.next();

               try {
                  Class var3 = Class.forName((String)var2.getKey());
                  Class var4 = Class.forName((String)var2.getValue());
                  Object var5 = var4.newInstance();
                  typeBasedValueRendererMap.put(var3, (ValueRenderer)var5);
               } catch (Exception var6) {
                  DiagnosticsLogger.logCannotLoadTypeBasedRenderer((String)var2.getValue(), (String)var2.getKey(), var6);
               }
            }

         }
      }
   }

   private static class DefaultValueRenderer implements ValueRenderer {
      public DefaultValueRenderer() {
      }

      public Object render(Object var1) {
         if (ValueRenderingManager.debugLog.isDebugEnabled()) {
            ValueRenderingManager.debugLog.debug("DefaultValueRenderer.render() input = " + var1);
         }

         return var1 == null ? null : var1;
      }
   }
}
