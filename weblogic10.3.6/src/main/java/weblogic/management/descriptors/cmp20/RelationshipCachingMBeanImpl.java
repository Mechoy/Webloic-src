package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class RelationshipCachingMBeanImpl extends XMLElementMBeanDelegate implements RelationshipCachingMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_cachingName = false;
   private String cachingName;
   private boolean isSet_cachingElements = false;
   private List cachingElements;

   public String getCachingName() {
      return this.cachingName;
   }

   public void setCachingName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cachingName;
      this.cachingName = var1;
      this.isSet_cachingName = var1 != null;
      this.checkChange("cachingName", var2, this.cachingName);
   }

   public CachingElementMBean[] getCachingElements() {
      if (this.cachingElements == null) {
         return new CachingElementMBean[0];
      } else {
         CachingElementMBean[] var1 = new CachingElementMBean[this.cachingElements.size()];
         var1 = (CachingElementMBean[])((CachingElementMBean[])this.cachingElements.toArray(var1));
         return var1;
      }
   }

   public void setCachingElements(CachingElementMBean[] var1) {
      CachingElementMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getCachingElements();
      }

      this.isSet_cachingElements = true;
      if (this.cachingElements == null) {
         this.cachingElements = Collections.synchronizedList(new ArrayList());
      } else {
         this.cachingElements.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.cachingElements.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("CachingElements", var2, this.getCachingElements());
      }

   }

   public void addCachingElement(CachingElementMBean var1) {
      this.isSet_cachingElements = true;
      if (this.cachingElements == null) {
         this.cachingElements = Collections.synchronizedList(new ArrayList());
      }

      this.cachingElements.add(var1);
   }

   public void removeCachingElement(CachingElementMBean var1) {
      if (this.cachingElements != null) {
         this.cachingElements.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<relationship-caching");
      var2.append(">\n");
      if (null != this.getCachingName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<caching-name>").append(this.getCachingName()).append("</caching-name>\n");
      }

      if (null != this.getCachingElements()) {
         for(int var3 = 0; var3 < this.getCachingElements().length; ++var3) {
            var2.append(this.getCachingElements()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</relationship-caching>\n");
      return var2.toString();
   }
}
