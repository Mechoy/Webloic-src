package weblogic.metadata.management;

import java.util.Collections;
import java.util.Map;
import weblogic.application.ApplicationAccess;
import weblogic.j2ee.descriptor.wl.AnnotationOverridesBean;

public class AnnotationOverridesFinder {
   public static final String ROOT_MODULE_URI = "__WLS ROOT MODULE URI__";
   static final String APP_PARM_KEY = "weblogic.metadata.management.AnnotationOverrideDescriptors";
   static final String APP_VERSION_KEY = "weblogic.metadata.management.AnnotationOverrideDescriptorsVersionID";

   public static final Map getAnnotationOverrideDescriptors() {
      Map var0 = getDescriptors();

      assert var0 != null;

      return var0 == null ? null : Collections.unmodifiableMap(var0);
   }

   public static final AnnotationOverridesBean getAnnotationOverrideDescriptor() {
      String var0 = ApplicationAccess.getApplicationAccess().getCurrentModuleName();
      return getAnnotationOverrideDescriptor(var0);
   }

   public static final AnnotationOverridesBean getAnnotationOverrideDescriptor(String var0) {
      Map var1 = getDescriptors();

      assert var1 != null;

      return var1 == null ? null : (AnnotationOverridesBean)var1.get(var0);
   }

   public static final AnnotationOverridesBean getAppScopedAnnotationOverride() {
      return getAnnotationOverrideDescriptor("__WLS ROOT MODULE URI__");
   }

   public static final long getCurrentOverrideVersion() {
      Long var0 = (Long)ApplicationAccess.getApplicationAccess().getCurrentApplicationContext().getApplicationParameters().get("weblogic.metadata.management.AnnotationOverrideDescriptorsVersionID");
      return var0 == null ? 0L : var0;
   }

   private static Map getDescriptors() {
      Map var0 = (Map)ApplicationAccess.getApplicationAccess().getCurrentApplicationContext().getApplicationParameters().get("weblogic.metadata.management.AnnotationOverrideDescriptors");
      return var0;
   }
}
