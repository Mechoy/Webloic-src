package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.InputCharsetDescriptorMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class InputCharsetDescriptor extends BaseServletDescriptor implements InputCharsetDescriptorMBean {
   private static final long serialVersionUID = 379779722485658071L;
   private static String RESOURCE_PATH = "resource-path";
   private static String JAVA_NAME = "java-charset-name";
   String resourcePath;
   String javaName;

   public String getResourcePath() {
      return this.resourcePath;
   }

   public void setResourcePath(String var1) {
      String var2 = this.resourcePath;
      this.resourcePath = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("resourcePath", var2, var1);
      }

   }

   public String getJavaCharsetName() {
      return this.javaName;
   }

   public void setJavaCharsetName(String var1) {
      String var2 = this.javaName;
      this.javaName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("javaCharsetName", var2, var1);
      }

   }

   public InputCharsetDescriptor() {
   }

   public InputCharsetDescriptor(Element var1) throws DOMProcessingException {
      this.resourcePath = DOMUtils.getValueByTagName(var1, RESOURCE_PATH);
      this.javaName = DOMUtils.getValueByTagName(var1, JAVA_NAME);
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.indentStr(var1));
      var2.append("<input-charset>\n");
      var1 += 2;
      var2.append(this.indentStr(var1));
      var2.append("<resource-path>");
      var2.append(this.resourcePath);
      var2.append("</resource-path>\n");
      var2.append(this.indentStr(var1));
      var2.append("<java-charset-name>");
      var2.append(this.javaName);
      var2.append("</java-charset-name>\n");
      var1 -= 2;
      var2.append(this.indentStr(var1));
      var2.append("</input-charset>\n");
      return var2.toString();
   }
}
