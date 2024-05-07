package weblogic.wsee.security.saml;

import com.bea.security.saml2.providers.SAML2AttributeStatementInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.security.providers.saml.SAMLAttributeStatementInfo;
import weblogic.wsee.util.Verbose;

public class SAMLAttributeStatementDataImpl implements SAMLAttributeStatementData {
   private static boolean verbose = Verbose.isVerbose(SAMLAttributeStatementDataImpl.class);
   private boolean isAttributeOnlyRequest = false;
   private Collection<SAMLAttributeData> attributeData;

   public SAMLAttributeStatementDataImpl() {
      this.initAttributeInfo();
   }

   private void initAttributeInfo() {
      if (null == this.attributeData) {
         this.attributeData = new ArrayList();
      }

   }

   public SAMLAttributeStatementDataImpl(Collection<SAMLAttributeData> var1) {
      this.initAttributeInfo();
      this.attributeData.addAll(var1);
   }

   public void addAttributeInfo(SAMLAttributeData var1) {
      this.initAttributeInfo();
      this.attributeData.add(var1);
   }

   public void addAttributeInfo(Collection<SAMLAttributeData> var1) {
      this.initAttributeInfo();
      this.attributeData.addAll(var1);
   }

   public Collection<SAMLAttributeData> getAttributeInfo() {
      return this.attributeData;
   }

   public boolean isAttributeOnlyRequest() {
      return this.isAttributeOnlyRequest;
   }

   public void setAttributeOnlyRequest(boolean var1) {
      this.isAttributeOnlyRequest = var1;
   }

   public boolean isEmpty() {
      return this.attributeData == null ? true : this.attributeData.isEmpty();
   }

   public int size() {
      return this.attributeData == null ? 0 : this.attributeData.size();
   }

   public SAMLAttributeData getAttributeInfo(String var1) {
      if (null != var1 && null != this.attributeData && !this.attributeData.isEmpty()) {
         Iterator var2 = this.attributeData.iterator();
         ArrayList var3 = new ArrayList();

         while(var2.hasNext()) {
            SAMLAttributeData var4 = (SAMLAttributeData)var2.next();
            if (var1.equals(var4.getAttributeName())) {
               var3.add(var4);
            }
         }

         if (var3.isEmpty()) {
            return null;
         } else if (var3.size() == 1) {
            return (SAMLAttributeData)var3.get(0);
         } else {
            return SAMLAttributeDataImpl.consolation(var3);
         }
      } else {
         return null;
      }
   }

   public boolean hasAttributeInfo(String var1) {
      if (null == var1) {
         return null == this.attributeData || this.attributeData.isEmpty();
      } else {
         Iterator var2 = this.attributeData.iterator();

         SAMLAttributeData var3;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            var3 = (SAMLAttributeData)var2.next();
         } while(!var1.equals(var3.getAttributeName()));

         return true;
      }
   }

   public boolean hasAttributeValue(String var1, String var2) {
      if (null == var1) {
         return null == this.attributeData || this.attributeData.isEmpty();
      } else {
         Iterator var3 = this.attributeData.iterator();

         while(true) {
            SAMLAttributeData var4;
            do {
               do {
                  if (!var3.hasNext()) {
                     return false;
                  }

                  var4 = (SAMLAttributeData)var3.next();
               } while(!var1.equals(var4.getAttributeName()));

               if (var2 == null && var4.isEmpty()) {
                  return true;
               }
            } while(var4.isEmpty());

            Iterator var5 = var4.getAttributeValues().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               if (var2.equals(var6)) {
                  return true;
               }
            }
         }
      }
   }

   public Collection<SAML2AttributeStatementInfo> getCollectionsForSAML2AttributeStatementInfo() {
      if (null != this.attributeData && !this.attributeData.isEmpty()) {
         if (verbose) {
            Verbose.log((Object)("Generating a colliction of <SAML2AttributeStatementInfo> for SAML 2.0 with size = " + this.attributeData.size()));
         }

         SAML2AttributeStatementInfo var1 = new SAML2AttributeStatementInfo();
         Iterator var2 = this.attributeData.iterator();

         while(var2.hasNext()) {
            SAMLAttributeData var3 = (SAMLAttributeData)var2.next();
            var1.addAttributeInfo(var3.getSAML2AttributeInfo());
         }

         ArrayList var4 = new ArrayList();
         var4.add(var1);
         return var4;
      } else {
         if (verbose) {
            Verbose.log((Object)"No SAML Attributes found on SAMLAttributeStatementData");
         }

         return null;
      }
   }

   public Collection<SAMLAttributeStatementInfo> getCollectionsForSAMLAttributeStatementInfo() {
      if (null != this.attributeData && !this.attributeData.isEmpty()) {
         if (verbose) {
            Verbose.log((Object)("Generating a colliction of <SAMLAttributeStatementInfo> for SAML 1.1 with size = " + this.attributeData.size()));
         }

         SAMLAttributeStatementInfo var1 = new SAMLAttributeStatementInfo();
         Iterator var2 = this.attributeData.iterator();

         while(var2.hasNext()) {
            SAMLAttributeData var3 = (SAMLAttributeData)var2.next();
            var1.addAttributeInfo(var3.getSAMLAttributeInfo());
         }

         ArrayList var4 = new ArrayList();
         var4.add(var1);
         return var4;
      } else {
         if (verbose) {
            Verbose.log((Object)"No SAML Attributes found on SAMLAttributeStatementData");
         }

         return null;
      }
   }

   public Map<String, String> getNameValuePair() {
      if (this.isEmpty()) {
         return null;
      } else {
         HashMap var1 = new HashMap();
         Iterator var2 = this.attributeData.iterator();

         while(var2.hasNext()) {
            SAMLAttributeData var3 = (SAMLAttributeData)var2.next();
            String var4 = (String)var1.get(var3.getAttributeName());
            var1.put(var3.getAttributeName(), ((SAMLAttributeDataImpl)var3).valuesToString(var4));
         }

         return var1;
      }
   }

   public String toString() {
      if (this.isEmpty()) {
         return "";
      } else {
         Iterator var1 = this.attributeData.iterator();
         SAMLAttributeData var2 = (SAMLAttributeData)var1.next();
         if (this.size() == 1) {
            return var2.toString();
         } else {
            StringBuffer var3 = new StringBuffer(var2.toString());

            while(var1.hasNext()) {
               var3.append("\n");
               var3.append(((SAMLAttributeData)var1.next()).toString());
            }

            return var3.toString();
         }
      }
   }
}
