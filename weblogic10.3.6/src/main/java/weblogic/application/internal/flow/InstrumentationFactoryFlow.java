package weblogic.application.internal.flow;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.Module;
import weblogic.management.DeploymentException;

public final class InstrumentationFactoryFlow extends BaseFlow {
   private static final String DIAGNOSTICS_DD = "META-INF/weblogic-diagnostics.xml";
   private static final String WLDF_MODULE_CLASSNAME = "weblogic.diagnostics.module.WLDFModule";

   public InstrumentationFactoryFlow(ApplicationContextInternal var1) {
      super(var1);
   }

   public void prepare() throws DeploymentException {
      if (!this.appCtx.getStagingPath().endsWith(".xml")) {
         try {
            String var1 = "META-INF/weblogic-diagnostics.xml";
            Class var2 = Class.forName("weblogic.diagnostics.module.WLDFModule");
            Class[] var3 = new Class[]{String.class};
            Object[] var4 = new Object[]{var1};
            Constructor var5 = var2.getConstructor(var3);
            Module var6 = (Module)var5.newInstance(var4);
            Module[] var7 = this.appCtx.getApplicationModules();
            if (var7 == null) {
               var7 = new Module[0];
            }

            Module[] var8 = new Module[var7.length + 1];
            var8[0] = var6;
            System.arraycopy(var7, 0, var8, 1, var7.length);
            this.appCtx.setApplicationModules(var8);
         } catch (ClassNotFoundException var9) {
            throw new DeploymentException(var9);
         } catch (NoSuchMethodException var10) {
            throw new DeploymentException(var10);
         } catch (InvocationTargetException var11) {
            throw new DeploymentException(var11);
         } catch (IllegalAccessException var12) {
            throw new DeploymentException(var12);
         } catch (InstantiationException var13) {
            throw new DeploymentException(var13);
         }
      }
   }
}
