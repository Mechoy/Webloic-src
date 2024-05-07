package weblogic.wsee.wstx.wsat.v10.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = ""
)
@XmlRootElement(
   name = "ReplayResponse"
)
public class ReplayResponse {
   @XmlAttribute(
      name = "outcome"
   )
   protected Outcome outcome;

   public Outcome getOutcome() {
      return this.outcome;
   }

   public void setOutcome(Outcome var1) {
      this.outcome = var1;
   }
}
