package weblogic.wsee.jaxws.tubeline.config;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "list",
   propOrder = {"item"}
)
public class List {
   @XmlElement(
      required = true
   )
   protected java.util.List<String> item;

   public java.util.List<String> getItem() {
      if (this.item == null) {
         this.item = new ArrayList();
      }

      return this.item;
   }
}
