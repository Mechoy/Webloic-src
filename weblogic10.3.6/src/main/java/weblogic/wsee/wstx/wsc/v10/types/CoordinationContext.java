package weblogic.wsee.wstx.wsc.v10.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "",
   propOrder = {"any"}
)
@XmlRootElement(
   name = "CoordinationContext"
)
public class CoordinationContext extends CoordinationContextType {
   @XmlAnyElement(
      lax = true
   )
   protected List<Object> any;

   public List<Object> getAny() {
      if (this.any == null) {
         this.any = new ArrayList();
      }

      return this.any;
   }
}
