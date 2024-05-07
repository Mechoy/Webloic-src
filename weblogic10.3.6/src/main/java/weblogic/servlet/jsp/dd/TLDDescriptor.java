package weblogic.servlet.jsp.dd;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.management.descriptors.webapp.ListenerMBean;
import weblogic.management.descriptors.webapp.TLDMBean;
import weblogic.management.descriptors.webapp.TagMBean;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.management.descriptors.webapp.ValidatorMBean;
import weblogic.servlet.internal.dd.BaseServletDescriptor;
import weblogic.servlet.internal.dd.ListenerDescriptor;
import weblogic.servlet.internal.dd.ToXML;
import weblogic.utils.io.XMLWriter;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public final class TLDDescriptor extends BaseServletDescriptor implements ToXML, TLDMBean, UIMBean {
   static final long serialVersionUID = 8049213100848306898L;
   private String taglibVersion;
   private String jspVersion;
   private String shortName;
   private String uri;
   private String displayName;
   private String smallIcon;
   private String largeIcon;
   private String description;
   private ValidatorDescriptor validator;
   private ListenerMBean[] listeners;
   private TagMBean[] tags;
   private boolean _12;
   private static final String ID_11 = "<!DOCTYPE taglib PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN\"\n\"http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd\">\n";
   private static final String ID_12 = "<!DOCTYPE taglib PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN\"\n\"http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd\">\n";
   static final String CDATA_BEGIN = "<![CDATA[";
   static final String CDATA_END = "]]>";

   static void p(String var0) {
      System.err.println("[TLDDescriptor]: " + var0);
   }

   private String getTlibVersionName() {
      return this.is12() ? "tlib-version" : "tlibversion";
   }

   private String getJspVersionName() {
      return this.is12() ? "jsp-version" : "jspversion";
   }

   private String getShortNameName() {
      return this.is12() ? "short-name" : "shortname";
   }

   public TLDDescriptor() {
      this._12 = true;
   }

   public TLDDescriptor(Document var1) throws DOMProcessingException {
      String var2;
      if (var1.getDoctype() != null) {
         var2 = var1.getDoctype().getPublicId();
         if ("-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN".equals(var2)) {
            this._12 = true;
         } else {
            if (!"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN".equals(var2)) {
               throw new DOMProcessingException("Invalid DTD for taglib: cannot resolve '" + var2 + "'");
            }

            this._12 = false;
         }
      } else {
         this._12 = true;
      }

      var2 = null;
      Element var3 = var1.getDocumentElement();
      Element var8 = DOMUtils.getOptionalElementByTagName(var3, this.getTlibVersionName());
      if (var8 != null) {
         this.taglibVersion = DOMUtils.getTextData(var8);
      } else {
         this.taglibVersion = "1.0";
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, this.getJspVersionName());
      if (var8 != null) {
         this.jspVersion = DOMUtils.getTextData(var8);
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, this.getShortNameName());
      if (var8 != null) {
         this.shortName = DOMUtils.getTextData(var8);
      } else {
         this.shortName = "";
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, "uri");
      if (var8 != null) {
         this.uri = DOMUtils.getTextData(var8);
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, "display-name");
      if (var8 != null) {
         this.displayName = DOMUtils.getTextData(var8);
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, "small-icon");
      if (var8 != null) {
         this.smallIcon = DOMUtils.getTextData(var8);
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, "large-icon");
      if (var8 != null) {
         this.largeIcon = DOMUtils.getTextData(var8);
      }

      var8 = DOMUtils.getOptionalElementByTagName(var3, "description");
      if (var8 != null) {
         this.description = DOMUtils.getTextData(var8);
      }

      List var4 = null;
      Iterator var5 = null;
      ArrayList var6 = new ArrayList();
      if (this.is12()) {
         var8 = DOMUtils.getOptionalElementByTagName(var3, "validator");
         if (var8 != null) {
            this.validator = new ValidatorDescriptor(var8);
         }

         var4 = DOMUtils.getOptionalElementsByTagName(var3, "listener");
         var5 = var4.iterator();
         var6.clear();

         while(var5.hasNext()) {
            ListenerDescriptor var7 = new ListenerDescriptor((Element)var5.next());
            var6.add(var7);
         }

         this.listeners = new ListenerMBean[var6.size()];
         var6.toArray(this.listeners);
      }

      var4 = DOMUtils.getOptionalElementsByTagName(var3, "tag");
      var5 = var4.iterator();
      var6.clear();

      while(var5.hasNext()) {
         var6.add(new TagDescriptor((Element)var5.next(), this.is12()));
      }

      this.tags = new TagMBean[var6.size()];
      var6.toArray(this.tags);
      this.set12(true);
   }

   public boolean is12() {
      return this._12;
   }

   public void set12(boolean var1) {
      if (this._12 != var1) {
         this._12 = var1;
         TagMBean[] var2 = this.getTags();

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            if (var2[var3] instanceof TagDescriptor) {
               TagDescriptor var4 = (TagDescriptor)var2[var3];
               var4.set12(this._12);
            }
         }

      }
   }

   public String getTaglibVersion() {
      return this.taglibVersion;
   }

   public void setTaglibVersion(String var1) {
      String var2 = this.taglibVersion;
      this.taglibVersion = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("taglibVersion", var2, var1);
      }

   }

   public String getJspVersion() {
      return this.jspVersion;
   }

   public void setJspVersion(String var1) {
      String var2 = this.jspVersion;
      this.jspVersion = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("jspVersion", var2, var1);
      }

   }

   public String getShortName() {
      return this.shortName;
   }

   public void setShortName(String var1) {
      String var2 = this.shortName;
      this.shortName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("shortName", var2, var1);
      }

   }

   public String getURI() {
      return this.uri;
   }

   public void setURI(String var1) {
      String var2 = this.uri;
      this.uri = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("uri", var2, var1);
      }

   }

   public void setLargeIconFileName(String var1) {
      this.setLargeIcon(var1);
   }

   public String getLargeIconFileName() {
      return this.getLargeIcon();
   }

   public void setSmallIconFileName(String var1) {
      this.setSmallIcon(var1);
   }

   public String getSmallIconFileName() {
      return this.getSmallIcon();
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      String var2 = this.displayName;
      this.displayName = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("displayName", var2, var1);
      }

   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      String var2 = this.description;
      this.description = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("description", var2, var1);
      }

   }

   public String getSmallIcon() {
      return this.smallIcon;
   }

   public void setSmallIcon(String var1) {
      String var2 = this.smallIcon;
      this.smallIcon = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("smallIcon", var2, var1);
      }

   }

   public String getLargeIcon() {
      return this.largeIcon;
   }

   public void setLargeIcon(String var1) {
      String var2 = this.largeIcon;
      this.largeIcon = var1;
      if (!comp(var2, var1)) {
         this.firePropertyChange("largeIcon", var2, var1);
      }

   }

   public ValidatorMBean getValidator() {
      return this.validator;
   }

   public void setValidator(ValidatorMBean var1) {
      this.validator = (ValidatorDescriptor)var1;
   }

   public TagMBean[] getTags() {
      if (this.tags == null) {
         this.tags = new TagMBean[0];
      }

      return (TagMBean[])((TagMBean[])this.tags.clone());
   }

   public void setTags(TagMBean[] var1) {
      TagMBean[] var2 = this.tags;
      if (var1 != null) {
         this.tags = (TagMBean[])((TagMBean[])var1.clone());
         if (!comp(var2, var1)) {
            this.firePropertyChange("tags", var2, var1);
         }

      }
   }

   public ListenerMBean[] getListeners() {
      if (this.listeners == null) {
         this.listeners = new ListenerMBean[0];
      }

      return (ListenerMBean[])((ListenerMBean[])this.listeners.clone());
   }

   public void setListeners(ListenerMBean[] var1) {
      ListenerMBean[] var2 = this.listeners;
      if (var1 == null) {
         this.listeners = new ListenerMBean[0];
      }

      this.listeners = (ListenerMBean[])((ListenerMBean[])var1.clone());
      if (!comp(var2, var1)) {
         this.firePropertyChange("listeners", var2, var1);
      }

   }

   public void validate() {
      throw new Error("NYI");
   }

   private String getPreamble() {
      return this.is12() ? "<!DOCTYPE taglib PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN\"\n\"http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd\">\n" : "<!DOCTYPE taglib PUBLIC \"-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.1//EN\"\n\"http://java.sun.com/j2ee/dtds/web-jsptaglibrary_1_1.dtd\">\n";
   }

   public void toXML(XMLWriter var1) {
      var1.println(this.getPreamble());
      var1.println("<taglib>");
      var1.incrIndent();
      var1.println("<" + this.getTlibVersionName() + ">" + this.getTaglibVersion() + "</" + this.getTlibVersionName() + ">");
      if (this.jspVersion != null) {
         var1.println("<" + this.getJspVersionName() + ">" + this.getJspVersion() + "</" + this.getJspVersionName() + ">");
      }

      var1.println("<" + this.getShortNameName() + ">" + this.getShortName() + "</" + this.getShortNameName() + ">");
      if (this.getURI() != null) {
         var1.println("<uri>" + this.getURI() + "</uri>");
      }

      int var3;
      if (this.is12()) {
         if (this.getDisplayName() != null) {
            var1.println("<display-name>" + this.getDisplayName() + "</display-name>");
         }

         if (this.getSmallIcon() != null) {
            var1.println("<small-icon>" + this.getSmallIcon() + "</small-icon>");
         }

         if (this.getLargeIcon() != null) {
            var1.println("<large-icon>" + this.getLargeIcon() + "</large-icon>");
         }

         if (this.getDescription() != null) {
            var1.println("<description>" + cdata(this.getDescription()) + "</description>");
         }

         if (this.getValidator() != null) {
            ValidatorDescriptor var2 = (ValidatorDescriptor)this.getValidator();
            var2.toXML(var1);
         }

         ListenerMBean[] var5 = this.getListeners();

         for(var3 = 0; var5 != null && var3 < var5.length; ++var3) {
            ListenerDescriptor var4 = (ListenerDescriptor)var5[var3];
            var4.toXML(var1);
         }
      } else if (this.getDescription() != null) {
         var1.println("<info>" + cdata(this.getDescription()) + "</info>");
      }

      TagMBean[] var6 = this.getTags();

      for(var3 = 0; var6 != null && var3 < var6.length; ++var3) {
         TagDescriptor var7 = (TagDescriptor)var6[var3];
         var7.toXML(var1);
      }

      var1.decrIndent();
      var1.println("</taglib>");
   }

   public static String toXML(ToXML var0, int var1) {
      StringWriter var2 = new StringWriter();
      XMLWriter var3 = new XMLWriter(var2);
      var0.toXML(var3);
      var3.flush();
      return var2.toString();
   }

   public String toXML(int var1) {
      return toXML(this, var1);
   }
}
