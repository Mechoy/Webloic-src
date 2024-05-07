package weblogic.wsee.reliability.faults;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.util.Verbose;
import weblogic.xml.dom.DOMProcessingException;
import weblogic.xml.dom.DOMUtils;

public class InvalidAckFaultMsg extends SequenceFaultMsg {
   public static final SequenceFaultMsgType TYPE = new SequenceFaultMsgType();
   private static final boolean verbose = Verbose.isVerbose(InvalidAckFaultMsg.class);
   private SortedSet<MessageRange> ranges = new TreeSet();
   private String seqId = null;
   private long nack = 0L;
   private boolean nackSet = false;

   public InvalidAckFaultMsg(WsrmConstants.RMVersion var1) {
      super(var1, WsrmConstants.FaultCode.SENDER, "InvalidAcknowledgement", "The SequenceAcknowledgement violates the cumulative acknowledgement invariant.", TYPE);
   }

   public void setNack(long var1) {
      this.nack = var1;
      this.nackSet = true;
   }

   public long getNack() {
      return this.nack;
   }

   public void acknowledgeMessages(long var1, long var3) {
      this.ranges.add(new MessageRange(var1, var3));
   }

   public void clear() {
      this.ranges.clear();
   }

   public Iterator listMessageRanges() {
      return this.ranges.iterator();
   }

   public void setAcknowledgementRanges(SortedSet<MessageRange> var1) {
      this.ranges = var1;
   }

   public SortedSet getAcknowledgementRanges() {
      return this.ranges;
   }

   public void writeDetail(Element var1) throws SequenceFaultException {
      if (this.seqId == null) {
         throw new SequenceFaultException("Sequence ID is not set");
      } else {
         DOMUtils.addNamespaceDeclaration(var1, this.getRmVersion().getPrefix(), this.getRmVersion().getNamespaceUri());
         Element var2 = var1.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACK.getQualifiedName(this.getRmVersion()));
         DOMUtils.addNamespaceDeclaration(var2, "wsu", "http://schemas.xmlsoap.org/ws/2002/07/utility");
         var1.appendChild(var2);
         DOMUtils.addValueNS(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.seqId);
         if (this.nackSet) {
            DOMUtils.addValueNS(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.NACK.getQualifiedName(this.getRmVersion()), Long.toString(this.nack));
         }

         Iterator var3 = this.listMessageRanges();

         while(var3.hasNext()) {
            MessageRange var4 = (MessageRange)var3.next();
            Element var5 = var2.getOwnerDocument().createElementNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACK_RANGE.getQualifiedName(this.getRmVersion()));
            var5.setAttributeNS((String)null, WsrmConstants.Element.LOWER.getQualifiedName(this.getRmVersion()), Long.toString(var4.lowerBounds));
            var5.setAttributeNS((String)null, WsrmConstants.Element.UPPER.getQualifiedName(this.getRmVersion()), Long.toString(var4.upperBounds));
            var2.appendChild(var5);
         }

      }
   }

   public void readDetail(Element var1) throws SequenceFaultException {
      try {
         Element var2 = DOMUtils.getElementByTagNameNS(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACK.getElementName());
         this.seqId = DOMUtils.getValueByTagNameNS(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
         String var3 = DOMUtils.getOptionalValueByTagNameNS(var2, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.NACK.getElementName());
         if (var3 != null) {
            try {
               this.nack = Long.parseLong(var3);
               this.nackSet = true;
            } catch (NumberFormatException var13) {
               if (verbose) {
                  Verbose.logException(var13);
               }
            }
         }

         NodeList var4 = var1.getElementsByTagNameNS(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACK_RANGE.getElementName());

         for(int var5 = 0; var5 < var4.getLength(); ++var5) {
            Element var6 = (Element)var4.item(var5);
            long var7 = 0L;
            String var9 = var6.getAttributeNS((String)null, WsrmConstants.Element.LOWER.getElementName());
            if (var9 != null) {
               var7 = Long.parseLong(var9);
            }

            long var10 = 0L;
            String var12 = var6.getAttributeNS((String)null, WsrmConstants.Element.UPPER.getElementName());
            if (var12 != null) {
               var10 = Long.parseLong(var12);
            }

            this.acknowledgeMessages(var7, var10);
         }

      } catch (DOMProcessingException var14) {
         throw new SequenceFaultException("DOMProcessingException", var14);
      }
   }
}
