package weblogic.wsee.jaxws.tubeline.config;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "listeners",
   propOrder = {"listener", "assemblerItem"}
)
public class Listeners {
   protected java.util.List<Listener> listener;
   @XmlElement(
      name = "assembler-item"
   )
   protected java.util.List<AssemblerItem> assemblerItem;

   public java.util.List<Listener> getListener() {
      if (this.listener == null) {
         this.listener = new ArrayList();
      }

      return this.listener;
   }

   public java.util.List<AssemblerItem> getAssemblerItem() {
      if (this.assemblerItem == null) {
         this.assemblerItem = new ArrayList();
      }

      return this.assemblerItem;
   }
}
