package weblogic.management.descriptors.cmp20;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class CachingElementMBeanImpl extends XMLElementMBeanDelegate implements CachingElementMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_groupName = false;
   private String groupName;
   private boolean isSet_cmrField = false;
   private String cmrField;
   private boolean isSet_cachingElements = false;
   private List cachingElements;

   public String getGroupName() {
      return this.groupName;
   }

   public void setGroupName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.groupName;
      this.groupName = var1;
      this.isSet_groupName = var1 != null;
      this.checkChange("groupName", var2, this.groupName);
   }

   public String getCmrField() {
      return this.cmrField;
   }

   public void setCmrField(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.cmrField;
      this.cmrField = var1;
      this.isSet_cmrField = var1 != null;
      this.checkChange("cmrField", var2, this.cmrField);
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
      var2.append(ToXML.indent(var1)).append("<caching-element");
      var2.append(">\n");
      if (null != this.getCmrField()) {
         var2.append(ToXML.indent(var1 + 2)).append("<cmr-field>").append(this.getCmrField()).append("</cmr-field>\n");
      }

      if (null != this.getGroupName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<group-name>").append(this.getGroupName()).append("</group-name>\n");
      }

      if (null != this.getCachingElements()) {
         for(int var3 = 0; var3 < this.getCachingElements().length; ++var3) {
            var2.append(this.getCachingElements()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</caching-element>\n");
      return var2.toString();
   }
}
