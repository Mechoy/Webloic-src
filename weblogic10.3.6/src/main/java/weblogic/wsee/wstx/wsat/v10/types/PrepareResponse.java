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
   name = "PrepareResponse"
)
public class PrepareResponse {
   @XmlAttribute(
      name = "vote"
   )
   protected Vote vote;

   public Vote getVote() {
      return this.vote;
   }

   public void setVote(Vote var1) {
      this.vote = var1;
   }
}
