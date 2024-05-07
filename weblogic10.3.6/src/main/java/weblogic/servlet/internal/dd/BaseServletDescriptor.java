package weblogic.servlet.internal.dd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.WebElementMBean;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.utils.io.XMLWriter;

public abstract class BaseServletDescriptor extends XMLElementMBeanDelegate {
   private static final long serialVersionUID = -1306936740447325550L;
   private List errorList = null;

   public void addDescriptorError(String var1) {
      this.addDescriptorError(new DescriptorError(var1));
   }

   public void addDescriptorError(String var1, String var2) {
      this.addDescriptorError(new DescriptorError(var1, var2));
   }

   public void addDescriptorError(String var1, String var2, String var3) {
      this.addDescriptorError(new DescriptorError(var1, var2, var3));
   }

   public void addDescriptorError(DescriptorError var1) {
      if (this.errorList == null) {
         this.errorList = new ArrayList();
      }

      if (var1 != null) {
         this.errorList.add(var1);
      }

   }

   public void removeDescriptorError(String var1) {
      if (var1 != null && this.errorList != null) {
         Iterator var2 = this.errorList.iterator();

         while(var2.hasNext()) {
            DescriptorError var3 = (DescriptorError)var2.next();
            if (var1.equals(var3.getError())) {
               this.errorList.remove(var3);
               break;
            }
         }

      }
   }

   public void addDescriptorError(String[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            this.addDescriptorError(var1[var2]);
         }

      }
   }

   public void removeDescriptorErrors() {
      this.errorList = new ArrayList();
   }

   public void setDescriptorErrors(String[] var1) {
      this.removeDescriptorErrors();
      this.addDescriptorError(var1);
   }

   public String[] getDescriptorErrors() {
      if (this.errorList != null && this.errorList.size() != 0) {
         DescriptorError[] var1 = (DescriptorError[])((DescriptorError[])this.errorList.toArray(new DescriptorError[this.errorList.size()]));
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = var1[var3].toString();
         }

         return var2;
      } else {
         return null;
      }
   }

   protected String indentStr(int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         StringBuffer var2 = new StringBuffer(var1);

         for(int var3 = 0; var3 < var1; ++var3) {
            var2.append(" ");
         }

         return var2.toString();
      }
   }

   public abstract void validate() throws DescriptorValidationException;

   public boolean isValid() {
      try {
         this.validate();
         return true;
      } catch (DescriptorValidationException var2) {
         return false;
      }
   }

   protected boolean check(WebElementMBean var1) {
      if (var1.isValid()) {
         return true;
      } else {
         this.addDescriptorError(var1.getDescriptorErrors());
         return false;
      }
   }

   public void toXML(XMLWriter var1) {
      var1.println(this.toXML());
   }

   public String toXML() {
      return this.toXML(0);
   }

   public abstract String toXML(int var1);

   protected String arrayToString(String[] var1) {
      if (var1 != null && var1.length != 0) {
         String var2 = "{";

         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var3 > 0) {
               var2 = var2 + ",";
            }

            var2 = var2 + var1[var3];
         }

         var2 = var2 + "}";
         return var2;
      } else {
         return "";
      }
   }
}
