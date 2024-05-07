package weblogic.wsee.jaxws.tubeline.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "assembler-item",
   propOrder = {"after", "before", "required"}
)
public class AssemblerItem {
   protected List after;
   protected List before;
   protected List required;
   @XmlAttribute(
      name = "class",
      required = true
   )
   protected String clazz;
   @XmlAttribute(
      name = "name"
   )
   protected String name;

   public List getAfter() {
      return this.after;
   }

   public void setAfter(List var1) {
      this.after = var1;
   }

   public List getBefore() {
      return this.before;
   }

   public void setBefore(List var1) {
      this.before = var1;
   }

   public List getRequired() {
      return this.required;
   }

   public void setRequired(List var1) {
      this.required = var1;
   }

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String var1) {
      this.clazz = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }
}
