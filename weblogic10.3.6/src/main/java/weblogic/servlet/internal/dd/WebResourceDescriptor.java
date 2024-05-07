package weblogic.servlet.internal.dd;

import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.webapp.WebResourceCollectionMBean;
import weblogic.servlet.HTTPLogger;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class WebResourceDescriptor extends BaseServletDescriptor implements ToXML, WebResourceCollectionMBean {
   private static final long serialVersionUID = -7968184076073383050L;
   private static final String WEB_RESOURCE_COLLECTION = "web-resource-collection";
   private static final String WEB_RESOURCE_NAME = "web-resource-name";
   private static final String URL_PATTERN = "url-pattern";
   private static final String HTTP_METHOD = "http-method";
   private String resourceName;
   private String description;
   private String[] urlPatterns;
   private String[] httpMethods;

   public WebResourceDescriptor() {
      this("");
   }

   public WebResourceDescriptor(String var1) {
      this.resourceName = var1;
   }

   public WebResourceDescriptor(WebResourceCollectionMBean var1) {
      this.setResourceName(var1.getResourceName());
      this.setDescription(var1.getDescription());
      this.setUrlPatterns(var1.getUrlPatterns());
      this.setHttpMethods(var1.getHttpMethods());
   }

   public WebResourceDescriptor(Element var1) throws DOMProcessingException {
      Element var3 = DOMUtils.getElementByTagName(var1, "web-resource-name");
      if (var3 == null) {
         throw new DOMProcessingException("You must specify '<web-resource-name>' within web-resource-collection");
      } else {
         this.resourceName = DOMUtils.getValueByTagName(var1, "web-resource-name");
         this.description = DOMUtils.getOptionalValueByTagName(var1, "description");
         List var2 = DOMUtils.getOptionalElementsByTagName(var1, "url-pattern");
         if (var2 != null && var2.size() > 0) {
            List var4 = DOMUtils.getTextDataValues(var2);
            String[] var5;
            Iterator var6;
            int var7;
            if (var4 != null) {
               var5 = new String[var4.size()];
               var6 = var4.iterator();

               for(var7 = 0; var6.hasNext() && var7 < var5.length; ++var7) {
                  var5[var7] = (String)var6.next();
                  int var8 = var5[var7].length();
                  if (var8 >= 2 && var5[var7].charAt(var8 - 1) == '*' && var5[var7].charAt(var8 - 2) != '/') {
                     throw new DOMProcessingException("The <url-pattern> : \"" + var5[var7] + "\", specified for the <" + "web-resource-collection" + "> with the <" + "web-resource-name" + "> : \"" + this.resourceName + "\", is an illegal non-exact pattern as per the Servlet specification.");
                  }
               }

               this.urlPatterns = var5;
            }

            var2 = DOMUtils.getOptionalElementsByTagName(var1, "http-method");
            if (var2 != null && var2.size() > 0) {
               var4 = DOMUtils.getTextDataValues(var2);
               if (var4 != null) {
                  var5 = new String[var4.size()];
                  var6 = var4.iterator();

                  for(var7 = 0; var6.hasNext() && var7 < var5.length; ++var7) {
                     var5[var7] = (String)var6.next();
                  }

                  this.httpMethods = var5;
               }
            }

         } else {
            HTTPLogger.logUrlPatternMissingFromWebResource(this.resourceName);
            throw new DOMProcessingException("<url-pattern> not specified for <web-resource-name> : " + this.resourceName);
         }
      }
   }

   public String getResourceName() {
      return this.resourceName;
   }

   public void setResourceName(String var1) {
      String var2 = this.resourceName;
      this.resourceName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("resourceName", var2, var1);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, this.description)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String[] getUrlPatterns() {
      return this.urlPatterns;
   }

   public void setUrlPatterns(String[] var1) {
      String[] var2 = this.urlPatterns;
      this.urlPatterns = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("urlPatterns", var2, var1);
      }

   }

   public void addUrlPattern(String var1) {
      String[] var2 = this.getUrlPatterns();
      if (var2 == null) {
         var2 = new String[]{var1};
         this.setUrlPatterns(var2);
      } else {
         String[] var3 = new String[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setUrlPatterns(var3);
      }
   }

   public void removeUrlPattern(String var1) {
      String[] var2 = this.getUrlPatterns();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            String[] var5 = new String[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setUrlPatterns(var5);
         }

      }
   }

   public String[] getHttpMethods() {
      return this.httpMethods;
   }

   public void setHttpMethods(String[] var1) {
      String[] var2 = this.httpMethods;
      this.httpMethods = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("httpMethods", var2, var1);
      }

   }

   public void addHttpMethod(String var1) {
      String[] var2 = this.getHttpMethods();
      if (var2 == null) {
         var2 = new String[]{var1};
         this.setHttpMethods(var2);
      } else {
         String[] var3 = new String[var2.length + 1];
         System.arraycopy(var2, 0, var3, 0, var2.length);
         var3[var2.length] = var1;
         this.setHttpMethods(var3);
      }
   }

   public void removeHttpMethod(String var1) {
      String[] var2 = this.getHttpMethods();
      if (var2 != null) {
         int var3 = -1;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4].equals(var1)) {
               var3 = var4;
               break;
            }
         }

         if (var3 >= 0) {
            String[] var5 = new String[var2.length - 1];
            System.arraycopy(var2, 0, var5, 0, var3);
            System.arraycopy(var2, var3 + 1, var5, var3, var2.length - (var3 + 1));
            this.setHttpMethods(var5);
         }

      }
   }

   public void validate() throws DescriptorValidationException {
      this.removeDescriptorErrors();
      boolean var1 = true;
      if (this.resourceName == null || (this.resourceName = this.resourceName.trim()).length() == 0) {
         this.addDescriptorError("NO_WEB_RESOURCE");
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
      var2 = var2 + this.indentStr(var1) + "<" + "web-resource-collection" + ">\n";
      var1 += 2;
      var2 = var2 + this.indentStr(var1) + "<web-resource-name>" + this.resourceName + "</web-resource-name>\n";
      if (this.description != null) {
         var2 = var2 + this.indentStr(var1) + "<description>" + this.description + "</description>\n";
      }

      int var3;
      if (this.urlPatterns != null) {
         for(var3 = 0; var3 < this.urlPatterns.length; ++var3) {
            var2 = var2 + this.indentStr(var1) + "<url-pattern>" + this.urlPatterns[var3] + "</url-pattern>\n";
         }
      }

      if (this.httpMethods != null) {
         for(var3 = 0; var3 < this.httpMethods.length; ++var3) {
            var2 = var2 + this.indentStr(var1) + "<http-method>" + this.httpMethods[var3] + "</http-method>\n";
         }
      }

      var1 -= 2;
      var2 = var2 + this.indentStr(var1) + "</web-resource-collection>\n";
      return var2;
   }

   public String toString() {
      String var1 = "WebResourceDescriptor(";
      var1 = var1 + "description=" + this.getDescription() + ",";
      var1 = var1 + "resource=" + this.getResourceName() + ",";
      String[] var2 = this.getHttpMethods();
      if (var2 == null) {
         var1 = var1 + "methods=null,";
      } else {
         String var3 = "{";

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3 = var3 + var2[var4];
            if (var4 == var2.length - 1) {
               var3 = var3 + "}";
            } else {
               var3 = var3 + ",";
            }
         }

         var1 = var1 + "methods=" + var3 + ",";
      }

      String[] var6 = this.getUrlPatterns();
      if (var6 == null) {
         var1 = var1 + "UrlPatterns=null,";
      } else {
         String var7 = "{";

         for(int var5 = 0; var5 < var6.length; ++var5) {
            var7 = var7 + var6[var5];
            if (var5 == var6.length - 1) {
               var7 = var7 + "}";
            } else {
               var7 = var7 + ",";
            }
         }

         var1 = var1 + "UrlPatterns=" + var7 + ",";
      }

      var1 = var1 + ")";
      return var1;
   }
}
