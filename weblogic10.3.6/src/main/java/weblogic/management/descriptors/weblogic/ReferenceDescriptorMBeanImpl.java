package weblogic.management.descriptors.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ReferenceDescriptorMBeanImpl extends XMLElementMBeanDelegate implements ReferenceDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_resourceDescriptions = false;
   private List resourceDescriptions;
   private boolean isSet_ejbLocalReferenceDescriptions = false;
   private List ejbLocalReferenceDescriptions;
   private boolean isSet_resourceEnvDescriptions = false;
   private List resourceEnvDescriptions;
   private boolean isSet_ejbReferenceDescriptions = false;
   private List ejbReferenceDescriptions;

   public ResourceDescriptionMBean[] getResourceDescriptions() {
      if (this.resourceDescriptions == null) {
         return new ResourceDescriptionMBean[0];
      } else {
         ResourceDescriptionMBean[] var1 = new ResourceDescriptionMBean[this.resourceDescriptions.size()];
         var1 = (ResourceDescriptionMBean[])((ResourceDescriptionMBean[])this.resourceDescriptions.toArray(var1));
         return var1;
      }
   }

   public void setResourceDescriptions(ResourceDescriptionMBean[] var1) {
      ResourceDescriptionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getResourceDescriptions();
      }

      this.isSet_resourceDescriptions = true;
      if (this.resourceDescriptions == null) {
         this.resourceDescriptions = Collections.synchronizedList(new ArrayList());
      } else {
         this.resourceDescriptions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceDescriptions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ResourceDescriptions", var2, this.getResourceDescriptions());
      }

   }

   public void addResourceDescription(ResourceDescriptionMBean var1) {
      this.isSet_resourceDescriptions = true;
      if (this.resourceDescriptions == null) {
         this.resourceDescriptions = Collections.synchronizedList(new ArrayList());
      }

      this.resourceDescriptions.add(var1);
   }

   public void removeResourceDescription(ResourceDescriptionMBean var1) {
      if (this.resourceDescriptions != null) {
         this.resourceDescriptions.remove(var1);
      }
   }

   public EJBLocalReferenceDescriptionMBean[] getEJBLocalReferenceDescriptions() {
      if (this.ejbLocalReferenceDescriptions == null) {
         return new EJBLocalReferenceDescriptionMBean[0];
      } else {
         EJBLocalReferenceDescriptionMBean[] var1 = new EJBLocalReferenceDescriptionMBean[this.ejbLocalReferenceDescriptions.size()];
         var1 = (EJBLocalReferenceDescriptionMBean[])((EJBLocalReferenceDescriptionMBean[])this.ejbLocalReferenceDescriptions.toArray(var1));
         return var1;
      }
   }

   public void setEJBLocalReferenceDescriptions(EJBLocalReferenceDescriptionMBean[] var1) {
      EJBLocalReferenceDescriptionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBLocalReferenceDescriptions();
      }

      this.isSet_ejbLocalReferenceDescriptions = true;
      if (this.ejbLocalReferenceDescriptions == null) {
         this.ejbLocalReferenceDescriptions = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbLocalReferenceDescriptions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbLocalReferenceDescriptions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBLocalReferenceDescriptions", var2, this.getEJBLocalReferenceDescriptions());
      }

   }

   public void addEJBLocalReferenceDescription(EJBLocalReferenceDescriptionMBean var1) {
      this.isSet_ejbLocalReferenceDescriptions = true;
      if (this.ejbLocalReferenceDescriptions == null) {
         this.ejbLocalReferenceDescriptions = Collections.synchronizedList(new ArrayList());
      }

      this.ejbLocalReferenceDescriptions.add(var1);
   }

   public void removeEJBLocalReferenceDescription(EJBLocalReferenceDescriptionMBean var1) {
      if (this.ejbLocalReferenceDescriptions != null) {
         this.ejbLocalReferenceDescriptions.remove(var1);
      }
   }

   public ResourceEnvDescriptionMBean[] getResourceEnvDescriptions() {
      if (this.resourceEnvDescriptions == null) {
         return new ResourceEnvDescriptionMBean[0];
      } else {
         ResourceEnvDescriptionMBean[] var1 = new ResourceEnvDescriptionMBean[this.resourceEnvDescriptions.size()];
         var1 = (ResourceEnvDescriptionMBean[])((ResourceEnvDescriptionMBean[])this.resourceEnvDescriptions.toArray(var1));
         return var1;
      }
   }

   public void setResourceEnvDescriptions(ResourceEnvDescriptionMBean[] var1) {
      ResourceEnvDescriptionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getResourceEnvDescriptions();
      }

      this.isSet_resourceEnvDescriptions = true;
      if (this.resourceEnvDescriptions == null) {
         this.resourceEnvDescriptions = Collections.synchronizedList(new ArrayList());
      } else {
         this.resourceEnvDescriptions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resourceEnvDescriptions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ResourceEnvDescriptions", var2, this.getResourceEnvDescriptions());
      }

   }

   public void addResourceEnvDescription(ResourceEnvDescriptionMBean var1) {
      this.isSet_resourceEnvDescriptions = true;
      if (this.resourceEnvDescriptions == null) {
         this.resourceEnvDescriptions = Collections.synchronizedList(new ArrayList());
      }

      this.resourceEnvDescriptions.add(var1);
   }

   public void removeResourceEnvDescription(ResourceEnvDescriptionMBean var1) {
      if (this.resourceEnvDescriptions != null) {
         this.resourceEnvDescriptions.remove(var1);
      }
   }

   public EJBReferenceDescriptionMBean[] getEJBReferenceDescriptions() {
      if (this.ejbReferenceDescriptions == null) {
         return new EJBReferenceDescriptionMBean[0];
      } else {
         EJBReferenceDescriptionMBean[] var1 = new EJBReferenceDescriptionMBean[this.ejbReferenceDescriptions.size()];
         var1 = (EJBReferenceDescriptionMBean[])((EJBReferenceDescriptionMBean[])this.ejbReferenceDescriptions.toArray(var1));
         return var1;
      }
   }

   public void setEJBReferenceDescriptions(EJBReferenceDescriptionMBean[] var1) {
      EJBReferenceDescriptionMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getEJBReferenceDescriptions();
      }

      this.isSet_ejbReferenceDescriptions = true;
      if (this.ejbReferenceDescriptions == null) {
         this.ejbReferenceDescriptions = Collections.synchronizedList(new ArrayList());
      } else {
         this.ejbReferenceDescriptions.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbReferenceDescriptions.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("EJBReferenceDescriptions", var2, this.getEJBReferenceDescriptions());
      }

   }

   public void addEJBReferenceDescription(EJBReferenceDescriptionMBean var1) {
      this.isSet_ejbReferenceDescriptions = true;
      if (this.ejbReferenceDescriptions == null) {
         this.ejbReferenceDescriptions = Collections.synchronizedList(new ArrayList());
      }

      this.ejbReferenceDescriptions.add(var1);
   }

   public void removeEJBReferenceDescription(EJBReferenceDescriptionMBean var1) {
      if (this.ejbReferenceDescriptions != null) {
         this.ejbReferenceDescriptions.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<reference-descriptor");
      var2.append(">\n");
      int var3;
      if (null != this.getResourceDescriptions()) {
         for(var3 = 0; var3 < this.getResourceDescriptions().length; ++var3) {
            var2.append(this.getResourceDescriptions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getResourceEnvDescriptions()) {
         for(var3 = 0; var3 < this.getResourceEnvDescriptions().length; ++var3) {
            var2.append(this.getResourceEnvDescriptions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getEJBReferenceDescriptions()) {
         for(var3 = 0; var3 < this.getEJBReferenceDescriptions().length; ++var3) {
            var2.append(this.getEJBReferenceDescriptions()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getEJBLocalReferenceDescriptions()) {
         for(var3 = 0; var3 < this.getEJBLocalReferenceDescriptions().length; ++var3) {
            var2.append(this.getEJBLocalReferenceDescriptions()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</reference-descriptor>\n");
      return var2.toString();
   }
}
