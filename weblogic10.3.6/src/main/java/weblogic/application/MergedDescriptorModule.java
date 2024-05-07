package weblogic.application;

import java.util.Map;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.ClassFinder;

/** @deprecated */
public interface MergedDescriptorModule {
   Map<String, DescriptorBean> getDescriptorMappings();

   void handleMergedFinder(ClassFinder var1);
}
