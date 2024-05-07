package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JspDescriptorBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JspDescriptorBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("Keepgenerated", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isKeepgenerated", "setKeepgenerated");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("PackagePrefix", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getPackagePrefix", "setPackagePrefix");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("SuperClass", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getSuperClass", "setSuperClass");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PageCheckSeconds", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getPageCheckSeconds", "setPageCheckSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("PageCheckSecondsSet", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isPageCheckSecondsSet", (String)null);
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Precompile", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isPrecompile", "setPrecompile");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PrecompileContinue", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isPrecompileContinue", "setPrecompileContinue");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Verbose", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isVerbose", "setVerbose");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("WorkingDir", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getWorkingDir", "setWorkingDir");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PrintNulls", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isPrintNulls", "setPrintNulls");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("BackwardCompatible", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isBackwardCompatible", "setBackwardCompatible");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Encoding", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getEncoding", "setEncoding");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ExactMapping", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isExactMapping", "setExactMapping");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DefaultFileName", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getDefaultFileName", "setDefaultFileName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RtexprvalueJspParamName", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isRtexprvalueJspParamName", "setRtexprvalueJspParamName");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Debug", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isDebug", "setDebug");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Id", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getId", "setId");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("CompressHtmlTemplate", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isCompressHtmlTemplate", "setCompressHtmlTemplate");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("OptimizeJavaExpression", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isOptimizeJavaExpression", "setOptimizeJavaExpression");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceProviderClass", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "getResourceProviderClass", "setResourceProviderClass");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("StrictStaleCheck", Class.forName("weblogic.j2ee.descriptor.wl.JspDescriptorBeanDConfig"), "isStrictStaleCheck", "setStrictStaleCheck");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for JspDescriptorBeanDConfigBeanInfo");
         }
      }
   }
}
