package weblogic.wsee.security.saml;

import com.bea.security.saml2.providers.SAML2AttributeInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import weblogic.security.providers.saml.SAMLAttributeInfo;

public class SAMLAttributeDataImpl implements SAMLAttributeData {
   public static final String SAML_2_0_ATTRNAME_FORMAT_BASIC = "urn:oasis:names:tc:SAML:2.0:attrname-format:basic";
   private String attributeName;
   private String attributeNameSpace;
   private String attributeNameFormat = "urn:oasis:names:tc:SAML:2.0:attrname-format:basic";
   private String attributeFriendlyName;
   private Collection<String> attributeValues;
   private boolean isSAML20;

   public SAMLAttributeDataImpl() {
   }

   public SAMLAttributeDataImpl(String var1, Collection<String> var2) {
      this.attributeName = var1;
      this.attributeValues = var2;
   }

   public SAMLAttributeDataImpl(String var1, String var2, String var3, String var4, Collection<String> var5) {
      this.attributeName = var1;
      this.attributeNameFormat = var2;
      this.attributeFriendlyName = var3;
      this.attributeValues = var5;
      this.attributeNameSpace = var4;
   }

   public SAMLAttributeDataImpl(SAML2AttributeInfo var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null SAML2AttributeInfo found ");
      } else {
         this.attributeName = var1.getAttributeName();
         this.attributeNameFormat = var1.getAttributeNameFormat();
         this.attributeFriendlyName = var1.getAttributeFriendlyName();
         this.attributeValues = var1.getAttributeValues();
         this.isSAML20 = true;
      }
   }

   public SAMLAttributeDataImpl(SAMLAttributeInfo var1) {
      if (null == var1) {
         throw new IllegalArgumentException("Null SAMLAttributeInfo found ");
      } else {
         this.attributeName = var1.getAttributeName();
         this.attributeNameSpace = var1.getAttributeNamespace();
         this.attributeValues = var1.getAttributeValues();
         this.isSAML20 = false;
      }
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   public void setAttributeName(String var1) {
      if (null == var1) {
         throw new IllegalArgumentException("attributeName cannot be null");
      } else {
         this.attributeName = var1;
      }
   }

   public String getAttributeNameFormat() {
      return this.attributeNameFormat;
   }

   public void setAttributeNameFormat(String var1) {
      this.attributeNameFormat = var1;
   }

   public String getAttributeFriendlyName() {
      return this.attributeFriendlyName;
   }

   public void setAttributeFriendlyName(String var1) {
      this.attributeFriendlyName = var1;
   }

   public Collection<String> getAttributeValues() {
      return this.attributeValues;
   }

   public void setAttributeValues(Collection<String> var1) {
      this.attributeValues = var1;
   }

   public void addAttributeValue(String var1) {
      if (this.attributeValues == null) {
         this.attributeValues = new ArrayList();
      }

      if (null == var1) {
         this.attributeValues.add("");
      } else {
         this.attributeValues.add(var1);
      }

   }

   public void addAttributeValues(Collection<String> var1) {
      if (this.attributeValues != null && !this.attributeValues.isEmpty()) {
         if (null != var1 && !var1.isEmpty()) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               this.attributeValues.add((String)var2.next());
            }

         } else {
            this.attributeValues.add("");
         }
      } else {
         this.setAttributeValues(var1);
      }
   }

   public String getAttributeNameSpace() {
      return this.attributeNameSpace;
   }

   public void setAttributeNameSpace(String var1) {
      this.attributeNameSpace = var1;
   }

   public void setSAML20(boolean var1) {
      this.isSAML20 = var1;
   }

   public boolean isSAML20() {
      return this.isSAML20;
   }

   public SAML2AttributeInfo getSAML2AttributeInfo() {
      SAML2AttributeInfo var1 = new SAML2AttributeInfo();
      var1.setAttributeFriendlyName(this.attributeFriendlyName);
      var1.setAttributeName(this.attributeName);
      if (null != this.attributeNameFormat && this.attributeNameFormat.length() != 0) {
         var1.setAttributeNameFormat(this.attributeNameFormat);
      } else {
         var1.setAttributeNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:basic");
      }

      var1.addAttributeValues(this.attributeValues);
      return var1;
   }

   public SAMLAttributeInfo getSAMLAttributeInfo() {
      SAMLAttributeInfo var1 = new SAMLAttributeInfo();
      if (null == this.attributeNameSpace) {
         var1.setAttributeName(this.attributeName, "");
      } else {
         var1.setAttributeName(this.attributeName, this.attributeNameSpace);
      }

      var1.setAttributeValues(this.attributeValues);
      return var1;
   }

   public static SAMLAttributeData consolation(List<SAMLAttributeData> var0) {
      if (null != var0 && var0.size() != 0) {
         if (var0.size() == 1) {
            var0.get(0);
         }

         SAMLAttributeData var1 = (SAMLAttributeData)var0.get(0);

         for(int var2 = 1; var2 < var0.size(); ++var2) {
            var1.addAttributeValues(((SAMLAttributeData)var0.get(var2)).getAttributeValues());
         }

         return var1;
      } else {
         return null;
      }
   }

   public boolean isEmpty() {
      if (null != this.attributeValues && !this.attributeValues.isEmpty()) {
         if (this.attributeValues.size() == 1) {
            Object[] var1 = this.attributeValues.toArray();
            if ("".equals(var1[0])) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public String valuesToString(String var1) {
      if (null != this.attributeValues && !this.attributeValues.isEmpty()) {
         Object[] var2 = this.attributeValues.toArray();
         if (this.attributeValues.size() == 1) {
            if (var2[0] == null) {
               return var1;
            } else {
               return var1 == null ? (String)var2[0] : var1 + "; " + (String)var2[0];
            }
         } else {
            StringBuffer var3 = new StringBuffer();
            if (var1 != null) {
               var3.append(var1);
            }

            for(int var4 = 0; var4 < var2.length; ++var4) {
               var3.append("; ");
               if (var2[var4] != null) {
                  var3.append((String)var2[var4]);
               }
            }

            return var3.toString();
         }
      } else {
         return var1;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name=" + this.attributeName);
      if (this.isSAML20()) {
         if (null != this.attributeFriendlyName) {
            var1.append(" FriendlyName=" + this.attributeFriendlyName);
         }
      } else if (null != this.attributeNameSpace) {
         var1.append(" Namespace=" + this.attributeNameSpace);
      }

      String var2 = this.valuesToString((String)null);
      if (null != var2) {
         var1.append(" Value=" + var2);
      }

      return var1.toString();
   }
}
