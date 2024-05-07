package weblogic.servlet.internal.dd;

import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.ListenerMBean;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ListenerDescriptor extends BaseServletDescriptor implements ToXML, ListenerMBean {
   private static final long serialVersionUID = 9012728722776252126L;
   private static final String LISTENER_CLASS = "listener-class";
   private String listenerClassName;

   public ListenerDescriptor() {
   }

   public ListenerDescriptor(ListenerMBean var1) {
      this(var1.getListenerClassName());
   }

   public ListenerDescriptor(String var1) {
      this.listenerClassName = var1;
   }

   public ListenerDescriptor(Element var1) throws DOMProcessingException {
      this.listenerClassName = DOMUtils.getValueByTagName(var1, "listener-class");
   }

   public String getListenerClassName() {
      return this.listenerClassName;
   }

   public void setListenerClassName(String var1) {
      String var2 = this.listenerClassName;
      this.listenerClassName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("listenerClassName", var2, var1);
      }

   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      if (this.listenerClassName == null) {
         this.addDescriptorError("NO_LISTENER_CLASS");
         throw new DescriptorValidationException();
      }
   }

   public void register() throws ManagementException {
      super.register();
   }

   public String toXML(int var1) {
      String var2 = "";
      if (this.listenerClassName != null) {
         var2 = var2 + this.indentStr(var1) + "<listener>\n";
         var1 += 2;
         var2 = var2 + this.indentStr(var1) + "<listener-class>" + this.listenerClassName + "</listener-class>\n";
         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</listener>\n";
      }

      return var2;
   }
}
