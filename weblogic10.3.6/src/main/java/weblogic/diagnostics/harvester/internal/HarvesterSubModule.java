package weblogic.diagnostics.harvester.internal;

import weblogic.application.ApplicationContext;
import weblogic.descriptor.DescriptorDiff;
import weblogic.diagnostics.descriptor.WLDFResourceBean;
import weblogic.diagnostics.module.WLDFModuleException;
import weblogic.diagnostics.module.WLDFSubModule;

public class HarvesterSubModule implements WLDFSubModule {
   private static final MetricArchiver archiverSingleton = MetricArchiver.getInstance();

   private HarvesterSubModule() {
   }

   public static final WLDFSubModule createInstance() {
      return new HarvesterSubModule();
   }

   public void init(ApplicationContext var1, WLDFResourceBean var2) throws WLDFModuleException {
      archiverSingleton.init(var1, var2);
   }

   public void prepare() throws WLDFModuleException {
      archiverSingleton.prepare();
   }

   public void activate() throws WLDFModuleException {
      archiverSingleton.activate();
   }

   public void deactivate() throws WLDFModuleException {
      archiverSingleton.deactivate();
   }

   public void unprepare() throws WLDFModuleException {
      archiverSingleton.unprepare();
   }

   public void destroy() throws WLDFModuleException {
      archiverSingleton.destroy();
   }

   public void prepareUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      archiverSingleton.prepareUpdate(var1, var2);
   }

   public void activateUpdate(WLDFResourceBean var1, DescriptorDiff var2) throws WLDFModuleException {
      archiverSingleton.activateUpdate(var1, var2);
   }

   public void rollbackUpdate(WLDFResourceBean var1, DescriptorDiff var2) {
      archiverSingleton.rollbackUpdate(var1, var2);
   }
}
