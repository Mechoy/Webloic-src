package weblogic.management.descriptors.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class RelationshipDescriptorMBeanImpl extends XMLElementMBeanDelegate implements RelationshipDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_ejbEntityRefDescriptions = false;
   private List ejbEntityRefDescriptions;

   public EJBEntityRefDescriptionMBean[] getEJBEntityRefDescriptions() {
      if (this.ejbEntityRefDescriptions == null) {
         return new EJBEntityRefDescriptionMBean[0];
      } else {
         EJBEntityRefDescriptionMBean[] var1 = new EJBEntityRefDescriptionMBean[this.ejbEntityRefDescriptions.size()];
         var1 = (EJBEntityRefDescriptionMBean[])((EJBEntityRefDescriptionMBean[])this.ejbEntityRefDescriptions.toArray(var1));
         return var1;
      }
   }

   public void setEJBEntityRefDescriptions(EJBEntityRefDescriptionMBean[] var1) {
      EJBEntityRefDescriptionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBEntityRefDescriptions();
      }

      this.isSet_ejbEntityRefDescriptions = true;
      if (this.ejbEntityRefDescriptions == null) {
         this.ejbEntityRefDescriptions = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbEntityRefDescriptions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbEntityRefDescriptions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBEntityRefDescriptions", var2, this.getEJBEntityRefDescriptions());
      }

   }

   public void addEJBEntityRefDescription(EJBEntityRefDescriptionMBean var1) {
      this.isSet_ejbEntityRefDescriptions = true;
      if (this.ejbEntityRefDescriptions == null) {
         this.ejbEntityRefDescriptions = Collections.synchronizedList(new ArrayList());
      }

      this.ejbEntityRefDescriptions.add(var1);
   }

   public void removeEJBEntityRefDescription(EJBEntityRefDescriptionMBean var1) {
      if (this.ejbEntityRefDescriptions != null) {
         this.ejbEntityRefDescriptions.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<relationship-descriptor");
      var2.append(">\n");
      if (null != this.getEJBEntityRefDescriptions()) {
         for(int var3 = 0; var3 < this.getEJBEntityRefDescriptions().length; ++var3) {
            var2.append(this.getEJBEntityRefDescriptions()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</relationship-descriptor>\n");
      return var2.toString();
   }
}
