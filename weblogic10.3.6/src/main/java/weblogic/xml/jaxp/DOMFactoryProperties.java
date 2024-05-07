package weblogic.xml.jaxp;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class DOMFactoryProperties {
   public static String COALESCING = "Coalescing";
   public static String EXPANDENTITYREFERENCES = "ExpandEntityReferences";
   public static String IGNORINGCOMMENTS = "IgnoringComments";
   public static String IGNORINGELEMENTCONTENTWHITESPACE = "IgnoringElementContentWhitespace";
   public static String NAMESPACEAWARE = "Namespaceaware";
   public static String VALIDATING = "Validating";
   public static String SCHEMA = "Schema";
   public static String XINCL = "XIncludeAware";
   private Hashtable factoryProperties = new Hashtable();
   private Set facPropertySettingMarks;
   private LinkedHashMap attributes;

   public DOMFactoryProperties() {
      this.factoryProperties.put(COALESCING, Boolean.FALSE);
      this.factoryProperties.put(EXPANDENTITYREFERENCES, Boolean.TRUE);
      this.factoryProperties.put(IGNORINGCOMMENTS, Boolean.FALSE);
      this.factoryProperties.put(IGNORINGELEMENTCONTENTWHITESPACE, Boolean.FALSE);
      this.factoryProperties.put(NAMESPACEAWARE, Boolean.FALSE);
      this.factoryProperties.put(VALIDATING, Boolean.FALSE);
      this.factoryProperties.put(XINCL, Boolean.FALSE);
      this.facPropertySettingMarks = new HashSet();
      this.attributes = new LinkedHashMap();
   }

   public void put(String var1, boolean var2) {
      this.factoryProperties.put(var1, new Boolean(var2));
      this.facPropertySettingMarks.add(var1);
   }

   public boolean get(String var1) {
      Boolean var2 = (Boolean)this.factoryProperties.get(var1);
      return var2;
   }

   public boolean isSetExplicitly(String var1) {
      return this.facPropertySettingMarks.contains(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }

   public Object getAttribute(String var1) {
      Object var2 = this.attributes.get(var1);
      return var2;
   }

   public Iterator attributes() {
      return this.attributes.keySet().iterator();
   }

   public Object clone() {
      DOMFactoryProperties var1 = new DOMFactoryProperties();
      var1.factoryProperties.clear();
      var1.facPropertySettingMarks.clear();
      var1.attributes.clear();
      var1.factoryProperties.putAll(this.factoryProperties);
      var1.facPropertySettingMarks.addAll(this.facPropertySettingMarks);
      var1.attributes.putAll(this.attributes);
      return var1;
   }
}
