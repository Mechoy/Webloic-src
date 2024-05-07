package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.TLDMBean;
import weblogic.management.descriptors.webapp.TagLibMBean;
import weblogic.servlet.jsp.dd.TLDDescriptor;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class TaglibDescriptor extends BaseServletDescriptor implements ToXML, TagLibMBean {
   private static final long serialVersionUID = -2310746458588034306L;
   private static final String TAGLIB = "taglib";
   private static final String TAGLIB_URI = "taglib-uri";
   private static final String TAGLIB_LOCATION = "taglib-location";
   private String uri;
   private String location;
   private TLDDescriptor tld;

   public TaglibDescriptor(TagLibMBean var1) {
      this(var1.getURI(), var1.getLocation());
   }

   public TaglibDescriptor() {
      this("", "");
   }

   public TaglibDescriptor(String var1, String var2) {
      this.uri = var1;
      this.location = var2;
   }

   public TaglibDescriptor(Element var1) throws DOMProcessingException {
      this.uri = DOMUtils.getValueByTagName(var1, "taglib-uri");
      this.location = DOMUtils.getValueByTagName(var1, "taglib-location");
   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      String var2 = this.uri;
      this.uri = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("uri", var2, var1);
      }

   }

   public String getLocation() {
      return this.location;
   }

   public void setLocation(String var1) {
      String var2 = this.location;
      this.location = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("location", var2, var1);
      }

   }

   public TLDMBean getTLD() {
      return this.tld;
   }

   public void setTLD(TLDMBean var1) {
      this.tld = (TLDDescriptor)var1;
   }

   public void validate() throws DescriptorValidationException {
      boolean var1 = true;
      this.removeDescriptorErrors();
      String var2 = this.getURI();
      if (var2 != null) {
         var2 = var2.trim();
         this.setURI(var2);
      }

      String var3 = this.getLocation();
      if (var3 != null) {
         var3 = var3.trim();
         this.setLocation(var3);
      }

      if (var2 == null || var2.length() == 0) {
         this.addDescriptorError("NO_TAGLIB_URI");
         var1 = false;
      }

      if (var3 == null || var3.length() == 0) {
         this.addDescriptorError("NO_TAGLIB_LOCATION", var2);
         var1 = false;
      }

      if (!var1) {
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      var2 = var2 + this.indentStr(var1) + "<taglib>\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<taglib-uri>" + this.uri + "</taglib-uri>\n";
      var2 = var2 + this.indentStr(var1) + "<taglib-location>" + this.location + "</taglib-location>\n";
      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</taglib>\n";
      return var2;
   }
}
