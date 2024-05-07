package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webappext.CharsetMappingMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class CharsetMapping extends BaseServletDescriptor implements CharsetMappingMBean {
   private static final long serialVersionUID = -5431702633984221708L;
   private static String IANA_NAME = "iana-charset-name";
   private static String JAVA_NAME = "java-charset-name";
   String ianaName;
   String javaName;

   public String getIANACharsetName() {
      return this.ianaName;
   }

   public void setIANACharsetName(String var1) {
      String var2 = this.ianaName;
      this.ianaName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("IANACharsetName", var2, var1);
      }

   }

   public String getJavaCharsetName() {
      return this.javaName;
   }

   public void setJavaCharsetName(String var1) {
      String var2 = this.javaName;
      this.javaName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("JavaCharsetName", var2, var1);
      }

   }

   public CharsetMapping() {
   }

   public CharsetMapping(Element var1) throws DOMProcessingException {
      this.ianaName = DOMUtils.getValueByTagName(var1, IANA_NAME);
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
      var2.append("<charset-mapping>\n");
      var1 += 2;
      var2.append(this.indentStr(var1));
      var2.append("<iana-charset-name>");
      var2.append(this.ianaName);
      var2.append("</iana-charset-name>\n");
      var2.append(this.indentStr(var1));
      var2.append("<java-charset-name>" + this.javaName + "</java-charset-name>\n");
      var1 -= 2;
      var2.append(this.indentStr(var1));
      var2.append("</charset-mapping>\n");
      return var2.toString();
   }
}
