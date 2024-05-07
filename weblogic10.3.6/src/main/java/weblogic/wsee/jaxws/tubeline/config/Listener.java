package weblogic.wsee.jaxws.tubeline.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "listener"
)
public class Listener {
   @XmlAttribute(
      name = "class",
      required = true
   )
   protected String clazz;

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String var1) {
      this.clazz = var1;
   }
}
