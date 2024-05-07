package weblogic.wsee.addressing;

import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class ProblemHeaderQNameHeader extends MsgHeader implements AddressingHeader {
   public static final MsgHeaderType TYPE = new MsgHeaderType();
   private QName name;
   private String problemName;

   public ProblemHeaderQNameHeader() {
      this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
   }

   public ProblemHeaderQNameHeader(QName var1) {
      this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
      this.name = var1;
   }

   public ProblemHeaderQNameHeader(String var1) {
      this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
      this.problemName = var1;
   }

   public ProblemHeaderQNameHeader(QName var1, String var2) {
      this.name = WSAddressingConstants.WSA_HEADER_PROBLEM_HEADER_QNAME_10;
      this.name = var1;
      this.problemName = var2;
   }

   public QName getName() {
      return this.name;
   }

   public void setName(QName var1) {
      this.name = var1;
   }

   public String getProblemName() {
      return this.problemName;
   }

   public void setProblemName(String var1) {
      this.problemName = var1;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public void read(Element var1) throws MsgHeaderException {
      try {
         this.problemName = DOMUtils.getTextData(var1);
      } catch (DOMProcessingException var3) {
         throw new MsgHeaderException("Could not get actionURI", var3);
      }
   }

   public void write(Element var1) throws MsgHeaderException {
      DOMUtils.addTextData(var1, this.problemName);
   }
}
