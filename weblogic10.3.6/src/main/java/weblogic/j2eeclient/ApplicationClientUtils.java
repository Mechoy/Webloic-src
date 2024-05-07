package weblogic.j2eeclient;

import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.dd.xml.AnnotationProcessor;
import weblogic.j2ee.descriptor.ApplicationClientBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;

public class ApplicationClientUtils {
   private static final boolean DEBUG = Boolean.getBoolean("weblogic.debug.DebugJ2EEClient");

   public static ApplicationClientBean getAnnotationProcessedDescriptor(GenericClassLoader var0, ApplicationClientDescriptor var1, Class var2) throws Exception {
      ApplicationClientBean var3 = var1.getApplicationClientBean();
      if (!var3.isMetadataComplete()) {
         if (DEBUG) {
            Debug.say(" The Descriptor is not full. Processing annotations");
         }

         if (DEBUG) {
            Debug.say("Before annotations the client bean is ");
            ((DescriptorBean)var3).getDescriptor().toXML(System.out);
         }

         AnnotationProcessor var4 = getAnnotationProcessor();
         var4.processJ2eeAnnotations(var2, var3, true);
         var4.validate(var0, (DescriptorBean)var3, true);
         if (DEBUG) {
            Debug.say("After annotations the client bean is ");
            ((DescriptorBean)var3).getDescriptor().toXML(System.out);
         }
      }

      return var3;
   }

   private static AnnotationProcessor getAnnotationProcessor() throws Exception {
      Class var0 = Class.forName("weblogic.j2ee.dd.xml.BaseJ2eeAnnotationProcessor");
      return (AnnotationProcessor)var0.newInstance();
   }
}
