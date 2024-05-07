package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.SessionConfigMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class SessionDescriptor extends BaseServletDescriptor implements ToXML, SessionConfigMBean {
   private static final long serialVersionUID = 4376958427997195978L;
   private static final String SESSION_TIMEOUT = "session-timeout";
   private int sessionTimeout = -2;

   public SessionDescriptor() {
   }

   public SessionDescriptor(SessionConfigMBean var1) {
      this.setSessionTimeout(var1.getSessionTimeout());
   }

   public SessionDescriptor(int var1) {
      this.sessionTimeout = var1;
   }

   public SessionDescriptor(Element var1) throws DOMProcessingException {
      String var2 = DOMUtils.getOptionalValueByTagName(var1, "session-timeout");
      if (var2 != null) {
         try {
            this.sessionTimeout = Integer.parseInt(var2);
         } catch (NumberFormatException var4) {
         }
      }

   }

   public int getSessionTimeout() {
      return this.sessionTimeout;
   }

   public void setSessionTimeout(int var1) {
      int var2 = this.sessionTimeout;
      this.sessionTimeout = var1;
      if (var2 != var1) {
         this.firePropertyChange("sessionTimeout", new Integer(var2), new Integer(var1));
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      if (this.sessionTimeout != -2 && this.sessionTimeout < 0) {
         this.addDescriptorError("INVALID_SESSION_TIMEOUT", "" + this.sessionTimeout);
         throw new DescriptorValidationException("INVALID_SESSION_TIMEOUT");
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      if (this.sessionTimeout != -2) {
         var2 = var2 + this.indentStr(var1) + "<session-config>\n";
         var1 += 2;
         var2 = var2 + this.indentStr(var1) + "<session-timeout>" + this.sessionTimeout + "</session-timeout>\n";
         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</session-config>\n";
      }

      return var2;
   }
}
