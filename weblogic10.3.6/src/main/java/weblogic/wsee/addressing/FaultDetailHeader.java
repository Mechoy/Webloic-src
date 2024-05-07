package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class FaultDetailHeader extends MsgHeader implements AddressingHeader {
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private QName name;
   private ProblemHeaderQNameHeader problemHeader;

   public FaultDetailHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_DETAIL_10;
   }

   public FaultDetailHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_DETAIL_10;
      this.name = var1;
   }

   public FaultDetailHeader(ProblemHeaderQNameHeader var1) {
      this.name = WSAddressingConstants.WSA_HEADER_FAULT_DETAIL_10;
      this.problemHeader = var1;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public ProblemHeaderQNameHeader getProblemHeader() {
      return this.problemHeader;
   }

   public void setProblemHeader(ProblemHeaderQNameHeader var1) {
      this.problemHeader = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      if (this.name == null) {
         throw new AssertionError("FaultDetailHeader QName should not be null!");
      } else {
         try {
            Element var2 = DOMUtils.getOptionalElementByTagNameNS(var1, this.name.getNamespaceURI(), "ProblemHeaderQName");
            if (var2 != null) {
               if ("http://www.w3.org/2005/08/addressing".equals(this.name.getNamespaceURI())) {
                  this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
               } else {
                  this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME;
               }

               this.problemHeader = new ProblemHeaderQNameHeader(this.name);
               this.problemHeader.setProblemName(DOMUtils.getTextData(var2));
            }

         } catch (DOMProcessingException var3) {
            throw new MsgHeaderException("Could not parse endpoint reference", var3);
         }
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      if (this.problemHeader != null) {
         this.problemHeader.writeToParent(var1);
      }

   }
}
