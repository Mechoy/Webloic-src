package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.EjbRefMBean;
import weblogic.management.descriptors.webapp.ResourceRefMBean;
import weblogic.management.descriptors.webappext.EjbReferenceDescriptionMBean;
import weblogic.management.descriptors.webappext.ReferenceDescriptorMBean;
import weblogic.management.descriptors.webappext.ResourceDescriptionMBean;
import weblogic.management.descriptors.weblogic.ResourceEnvDescriptionMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.utils.AssertionError;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class ReferenceDescriptor extends BaseServletDescriptor implements ToXML, ReferenceDescriptorMBean {
   private static final long serialVersionUID = 5931465487784198233L;
   private static final String EJB_REF_DESCRIPTION = "ejb-reference-description";
   private static final String EJB_REF_NAME = "ejb-ref-name";
   private static final String JNDI_NAME = "jndi-name";
   private static final String RESOURCE_DESCRIPTION = "resource-description";
   private static final String RESOURCE_ENV_DESCRIPTION = "resource-env-description";
   private static final String RES_ENV_REF_NAME = "res-env-ref-name";
   private static final String RES_REF_NAME = "res-ref-name";
   private ArrayList resRefs = new ArrayList();
   private ArrayList resEnvRefs = new ArrayList();
   private ArrayList ejbRefs = new ArrayList();
   private static String resRefErr = "Can't define resource-definition in weblogic.xml because web.xml has no matching resource-ref";
   private static String EJBRefErr = "Can't define ejb-reference-description in weblogic.xml because web.xml has no matching ejb-ref";

   public ReferenceDescriptor() {
   }

   public ReferenceDescriptor(ReferenceDescriptorMBean var1) {
      ResourceDescriptionMBean[] var2 = var1.getResourceReferences();
      this.resRefs.clear();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         ResourceDescriptionMBean var3 = var2[var5];
         ResourceDescription var4 = new ResourceDescription();
         var4.setResourceReference(var3.getResourceReference());
         var4.setJndiName(var3.getJndiName());
         this.resRefs.add(var4);
      }

      ResourceEnvDescriptionMBean[] var10 = var1.getResourceEnvReferences();
      this.resEnvRefs.clear();

      for(int var6 = 0; var6 < var10.length; ++var6) {
         ResourceEnvDescriptionMBean var7 = var10[var6];
         ResourceEnvDescriptionMBean var8 = newResourceEnvDescriptionMBean();
         var8.setResEnvRefName(var7.getResEnvRefName());
         var8.setJNDIName(var7.getJNDIName());
         this.resEnvRefs.add(var8);
      }

      EjbReferenceDescriptionMBean[] var11 = var1.getEjbReferences();
      this.ejbRefs.clear();

      for(int var9 = 0; var9 < var11.length; ++var9) {
         EjbReferenceDescriptionMBean var12 = var11[var9];
         EjbReferenceDescription var13 = new EjbReferenceDescription();
         var13.setEjbReference(var12.getEjbReference());
         var13.setJndiName(var12.getJndiName());
         this.ejbRefs.add(var13);
      }

   }

   private static ResourceEnvDescriptionMBean newResourceEnvDescriptionMBean() {
      try {
         return (ResourceEnvDescriptionMBean)Class.forName("weblogic.management.descriptors.weblogic.ResourceEnvDescriptionMBeanImpl").newInstance();
      } catch (ClassNotFoundException var1) {
         throw new AssertionError(var1);
      } catch (IllegalAccessException var2) {
         throw new AssertionError(var2);
      } catch (InstantiationException var3) {
         throw new AssertionError(var3);
      }
   }

   public ReferenceDescriptor(WebAppDescriptor var1, Element var2) throws DOMProcessingException {
      List var3 = DOMUtils.getOptionalElementsByTagName(var2, "resource-description");
      this.resRefs.clear();
      Iterator var4 = var3.iterator();

      Element var5;
      String var6;
      String var7;
      while(var4.hasNext()) {
         var5 = (Element)var4.next();
         var6 = DOMUtils.getValueByTagName(var5, "res-ref-name");
         var7 = DOMUtils.getValueByTagName(var5, "jndi-name");
         ResourceDescription var8 = new ResourceDescription();
         ResourceRefMBean var9 = this.findResRef(var1, var6);
         if (var9 != null) {
            var8.setResourceReference(var9);
            var8.setJndiName(var7);
            this.resRefs.add(var8);
         }
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "resource-env-description");
      this.resEnvRefs.clear();
      var4 = var3.iterator();

      while(var4.hasNext()) {
         var5 = (Element)var4.next();
         ResourceEnvDescriptionMBean var10 = newResourceEnvDescriptionMBean();
         var10.setResEnvRefName(DOMUtils.getValueByTagName(var5, "res-env-ref-name"));
         var10.setJNDIName(DOMUtils.getValueByTagName(var5, "jndi-name"));
         this.resEnvRefs.add(var10);
      }

      var3 = DOMUtils.getOptionalElementsByTagName(var2, "ejb-reference-description");
      this.ejbRefs.clear();
      var4 = var3.iterator();

      while(var4.hasNext()) {
         var5 = (Element)var4.next();
         var6 = DOMUtils.getValueByTagName(var5, "ejb-ref-name");
         var7 = DOMUtils.getValueByTagName(var5, "jndi-name");
         EjbReferenceDescription var11 = new EjbReferenceDescription();
         EjbRefMBean var12 = this.findEjbRef(var1, var6);
         if (var12 != null) {
            var11.setEjbReference(var12);
            var11.setJndiName(var7);
            this.ejbRefs.add(var11);
         }
      }

   }

   private ResourceRefMBean findResRef(WebAppDescriptor var1, String var2) {
      ResourceRefMBean[] var3 = var1.getResourceReferences();
      if (var3 == null) {
         HTTPLogger.logNoResourceRefs();
         return null;
      } else {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getRefName().equals(var2)) {
               return var3[var4];
            }
         }

         HTTPLogger.logResourceRefNotFound(var2);
         return null;
      }
   }

   private EjbRefMBean findEjbRef(WebAppDescriptor var1, String var2) {
      EjbRefMBean[] var3 = var1.getEJBReferences();
      EjbRefMBean[] var4 = var1.getEJBLocalReferences();
      if (var3 == null && var4 == null) {
         HTTPLogger.logNoEjbRefs();
         return null;
      } else {
         int var5;
         for(var5 = 0; var3 != null && var5 < var3.length; ++var5) {
            if (var3[var5].getEJBRefName().equals(var2)) {
               return var3[var5];
            }
         }

         for(var5 = 0; var4 != null && var5 < var4.length; ++var5) {
            if (var4[var5].getEJBRefName().equals(var2)) {
               return var4[var5];
            }
         }

         HTTPLogger.logEjbRefNotFound(var2);
         return null;
      }
   }

   public EjbReferenceDescriptionMBean[] getEjbReferences() {
      return (EjbReferenceDescriptionMBean[])((EjbReferenceDescriptionMBean[])this.ejbRefs.toArray(new EjbReferenceDescription[0]));
   }

   public void setEjbReferences(EjbReferenceDescriptionMBean[] var1) {
      if (var1 != null && var1.length >= 1) {
         EjbReferenceDescriptionMBean[] var2 = this.getEjbReferences();
         this.ejbRefs.clear();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.ejbRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("ejbReferences", var2, var1);
         }

      }
   }

   public void addEjbReference(EjbReferenceDescriptionMBean var1) {
      this.ejbRefs.add(var1);
   }

   public void removeEjbReference(EjbReferenceDescriptionMBean var1) {
      if (this.ejbRefs != null) {
         this.ejbRefs.remove(var1);
      }

   }

   public ResourceDescriptionMBean[] getResourceReferences() {
      return (ResourceDescriptionMBean[])((ResourceDescriptionMBean[])this.resRefs.toArray(new ResourceDescription[0]));
   }

   public void setResourceReferences(ResourceDescriptionMBean[] var1) {
      ResourceDescriptionMBean[] var2 = this.getResourceReferences();
      this.resRefs.clear();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resRefs.add(var1[var3]);
         }
      }

      if (!comp(var2, var1)) {
         this.firePropertyChange("resourceReferences", var2, var1);
      }

   }

   public void addResourceReference(ResourceDescriptionMBean var1) {
      this.resRefs.add(var1);
   }

   public void removeResourceReference(ResourceDescriptionMBean var1) {
      if (this.resRefs != null) {
         this.resRefs.remove(var1);
      }

   }

   public void setResourceEnvReferences(ResourceEnvDescriptionMBean[] var1) {
      ResourceEnvDescriptionMBean[] var2 = this.getResourceEnvReferences();
      this.resEnvRefs.clear();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.resEnvRefs.add(var1[var3]);
         }

         if (!comp(var2, var1)) {
            this.firePropertyChange("resourceEnvReferences", var2, var1);
         }

      }
   }

   public ResourceEnvDescriptionMBean[] getResourceEnvReferences() {
      return (ResourceEnvDescriptionMBean[])((ResourceEnvDescriptionMBean[])this.resEnvRefs.toArray(new ResourceEnvDescriptionMBean[this.resEnvRefs.size()]));
   }

   public void addResourceEnvReference(ResourceEnvDescriptionMBean var1) {
      this.resEnvRefs.add(var1);
   }

   public void removeResourceEnvReference(ResourceEnvDescriptionMBean var1) {
      this.resEnvRefs.remove(var1);
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
   }

   public String toXML(int var1) {
      String var2 = "";
      if (this.resRefs.size() > 0 || this.resEnvRefs.size() > 0 || this.ejbRefs.size() > 0) {
         var2 = var2 + this.indentStr(var1) + "<reference-descriptor>\n";
         var1 += 2;

         int var3;
         for(var3 = 0; var3 < this.resRefs.size(); ++var3) {
            ResourceDescription var4 = (ResourceDescription)this.resRefs.get(var3);
            var2 = var2 + var4.toXML(var1);
         }

         for(var3 = 0; var3 < this.resEnvRefs.size(); ++var3) {
            ResourceEnvDescriptionMBean var6 = (ResourceEnvDescriptionMBean)this.resEnvRefs.get(var3);
            var2 = var2 + var6.toXML(var1);
         }

         for(var3 = 0; var3 < this.ejbRefs.size(); ++var3) {
            EjbReferenceDescription var7 = (EjbReferenceDescription)this.ejbRefs.get(var3);
            var2 = var2 + this.indentStr(var1) + "<ejb-reference-description>\n";
            var1 += 2;
            EjbRefMBean var5 = var7.getEjbReference();
            if (var5 != null) {
               var2 = var2 + this.indentStr(var1) + "<ejb-ref-name>" + var5.getEJBRefName() + "</ejb-ref-name>\n";
            }

            var2 = var2 + this.indentStr(var1) + "<jndi-name>" + var7.getJndiName() + "</jndi-name>\n";
            var1 -= 2;
            var2 = var2 + this.indentStr(var1) + "</ejb-reference-description>\n";
         }

         var1 -= 2;
         var2 = var2 + this.indentStr(var1) + "</reference-descriptor>\n";
      }

      return var2;
   }
}
