package weblogic.wsee.reliability.headers;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.namespace.QName;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import weblogic.wsee.message.MsgHeaderException;
import weblogic.wsee.message.MsgHeaderType;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.compat.SimpleElement;

public class AcknowledgementHeader extends WsrmHeader {
   private static final long serialVersionUID = 3068970050564544792L;
   public static final String LOCAL_NAME;
   public static final MsgHeaderType TYPE;
   private SortedSet<MessageRange> ranges;
   private boolean none;
   private boolean finalFlag;
   private String sequenceId;
   private long nack;
   private boolean nackSet;

   public AcknowledgementHeader() {
      this(WsrmConstants.RMVersion.latest());
   }

   public AcknowledgementHeader(WsrmConstants.RMVersion var1) {
      super(var1, LOCAL_NAME);
      this.ranges = new TreeSet();
      this.nack = 0L;
      this.nackSet = false;
   }

   public MsgHeaderType getType() {
      return TYPE;
   }

   public String getSequenceId() {
      return this.sequenceId;
   }

   public void setSequenceId(String var1) {
      this.sequenceId = var1;
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

   public SortedSet<MessageRange> getAcknowledgementRanges() {
      return this.ranges;
   }

   public boolean isAcknowledged(int var1) {
      Iterator var2 = this.listMessageRanges();

      MessageRange var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (MessageRange)var2.next();
      } while((long)var1 < var3.lowerBounds || (long)var1 > var3.upperBounds);

      return true;
   }

   public boolean getNone() {
      return this.none;
   }

   public void setNone(boolean var1) {
      this.none = var1;
   }

   public boolean getFinal() {
      return this.finalFlag;
   }

   public void setFinal(boolean var1) {
      this.finalFlag = var1;
   }

   public void readFromSimpleElement(SimpleElement var1) throws MsgHeaderException {
      super.setRmVersionFromSimpleElement(var1);
      this.sequenceId = SimpleElement.getContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getElementName());
      String var2 = SimpleElement.getOptionalContentForChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.NACK.getElementName());
      if (var2 != null) {
         try {
            this.nack = Long.parseLong(var2);
         } catch (NumberFormatException var14) {
            throw new MsgHeaderException("Nack number format error: " + var2, var14);
         }

         if (this.nack <= 0L) {
            throw new MsgHeaderException("Nack is not a positive number: " + this.nack);
         }

         this.nackSet = true;
      }

      List var3 = var1.getChildren(this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.ACK_RANGE.getElementName());
      int var4 = var3.size();
      if (var2 != null && var4 > 0) {
         throw new MsgHeaderException("Nack and AcknowledgementRange cannot coexist");
      } else {
         Iterator var5 = var3.iterator();

         SimpleElement var6;
         while(var5.hasNext()) {
            var6 = (SimpleElement)var5.next();
            String var9 = var6.getAttr((String)null, WsrmConstants.Element.LOWER.getElementName());
            if (var9 == null) {
               throw new MsgHeaderException("No lower bound specified in AcknowledgementRange");
            }

            long var7;
            try {
               var7 = Long.parseLong(var9);
               if (var7 <= 0L) {
                  throw new MsgHeaderException("AcknowledgementRange lower bound is not a positive message number: " + var7);
               }
            } catch (NumberFormatException var16) {
               throw new MsgHeaderException("AcknowledgementRange lower bound number format error: " + var9, var16);
            }

            String var12 = var6.getAttr((String)null, WsrmConstants.Element.UPPER.getElementName());
            if (var12 == null) {
               throw new MsgHeaderException("No upper bound specified in AcknowledgementRange");
            }

            long var10;
            try {
               var10 = Long.parseLong(var12);
               if (var10 <= 0L) {
                  throw new MsgHeaderException("AcknowledgementRange upper bound is not a positive message number: " + var10);
               }
            } catch (NumberFormatException var15) {
               throw new MsgHeaderException("AcknowledgementRange upper bound number format error: " + var12, var15);
            }

            if (var10 < var7) {
               throw new MsgHeaderException("AcknowledgementRange upper bound is lower than lower bound: upper = " + var10 + " lower = " + var7);
            }

            this.acknowledgeMessages(var7, var10);
         }

         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            SimpleElement var17 = SimpleElement.getOptionalChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.NONE.getElementName());
            if (var17 != null) {
               if (this.ranges.size() > 0) {
                  throw new MsgHeaderException("Cannot have None element *and* AcknowledgementRange elements in a SequenceAcknowledgement header");
               }

               this.setNone(true);
            }

            var6 = SimpleElement.getOptionalChild(var1, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.FINAL.getElementName());
            if (var6 != null) {
               this.setFinal(true);
            }
         }

      }
   }

   public SimpleElement writeToSimpleElement() throws MsgHeaderException {
      try {
         return this.writeToSimpleElementOrContentHandler((ContentHandler)null);
      } catch (SAXException var2) {
         throw new MsgHeaderException(var2.getMessage());
      }
   }

   public void writeTo(ContentHandler var1, ErrorHandler var2) throws SAXException {
      try {
         this.writeToSimpleElementOrContentHandler(var1);
      } catch (MsgHeaderException var4) {
         throw new SAXException(var4);
      }
   }

   private SimpleElement writeToSimpleElementOrContentHandler(ContentHandler var1) throws MsgHeaderException, SAXException {
      boolean var2 = var1 == null;
      QName var3 = this.getName();
      String var4 = var3.getNamespaceURI();
      String var5 = var3.getLocalPart();
      SimpleElement var6 = null;
      if (var2) {
         var6 = new SimpleElement(var3);
      } else {
         var1.startPrefixMapping("", var4);
         var1.startElement(var4, var5, var5, EMPTY_ATTS);
      }

      if (this.sequenceId == null) {
         String var19 = "Sequence ID is not set";
         if (var2) {
            throw new MsgHeaderException(var19);
         } else {
            throw new SAXException(var19);
         }
      } else {
         if (var2) {
            SimpleElement.addChild(var6, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.IDENTIFIER.getQualifiedName(this.getRmVersion()), this.sequenceId);
         } else {
            this.writeToIdentifierSubElement(var1, this.sequenceId);
         }

         boolean var7 = this.nackSet;
         long var8 = this.nack;
         if (!this.listMessageRanges().hasNext() && this.getRmVersion() == WsrmConstants.RMVersion.RM_10 && !var7) {
            var7 = true;
            var8 = 1L;
         }

         String var13;
         if (var7) {
            if (var8 <= 0L) {
               throw new MsgHeaderException("Nack is not a positive number: " + var8);
            }

            if (this.ranges.size() > 0) {
               throw new MsgHeaderException("Nack and AcknowledgementRange cannot coexist");
            }

            String var10 = Long.toString(var8);
            if (var2) {
               SimpleElement.addChild(var6, var4, WsrmConstants.Element.NACK.getQualifiedName(this.getRmVersion()), var10);
            } else {
               QName var11 = WsrmConstants.Element.NACK.getQName(this.getRmVersion());
               String var12 = var11.getNamespaceURI();
               var13 = var11.getLocalPart();
               var1.startElement(var12, var13, var13, EMPTY_ATTS);
               var1.characters(var10.toCharArray(), 0, var10.length());
               var1.endElement(var12, var13, var13);
            }
         }

         boolean var20 = false;
         Iterator var21 = this.listMessageRanges();

         String var14;
         while(var21.hasNext()) {
            var20 = true;
            MessageRange var22 = (MessageRange)var21.next();
            if (var22.lowerBounds <= 0L) {
               throw new MsgHeaderException("AcknowledgementRange lower bound is not a positive message number: " + var22.lowerBounds);
            }

            if (var22.upperBounds <= 0L) {
               throw new MsgHeaderException("AcknowledgementRange upper bound is not a positive message number: " + var22.upperBounds);
            }

            if (var22.upperBounds < var22.lowerBounds) {
               throw new MsgHeaderException("AcknowledgementRange upper bound is lower than lower bound: upper = " + var22.upperBounds + " lower = " + var22.lowerBounds);
            }

            var13 = Long.toString(var22.lowerBounds);
            var14 = Long.toString(var22.upperBounds);
            if (var2) {
               SimpleElement var15 = SimpleElement.createChild(var6, var4, WsrmConstants.Element.ACK_RANGE.getQualifiedName(this.getRmVersion()));
               var15.setAttr((String)null, WsrmConstants.Element.LOWER.getElementName(), var13);
               var15.setAttr((String)null, WsrmConstants.Element.UPPER.getElementName(), var14);
            } else {
               AttributesImpl var24 = new AttributesImpl();
               var24.addAttribute(var4, WsrmConstants.Element.LOWER.getElementName(), WsrmConstants.Element.LOWER.getQualifiedName(this.getRmVersion()), "messageNumberType", var13);
               var24.addAttribute(var4, WsrmConstants.Element.UPPER.getElementName(), WsrmConstants.Element.UPPER.getQualifiedName(this.getRmVersion()), "messageNumberType", var14);
               QName var16 = WsrmConstants.Element.ACK_RANGE.getQName(this.getRmVersion());
               String var17 = var16.getNamespaceURI();
               String var18 = var16.getLocalPart();
               var1.startElement(var17, var18, var18, var24);
               var1.endElement(var17, var18, var18);
            }
         }

         if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            QName var23;
            if (this.none || !var20) {
               if (this.ranges.size() > 0) {
                  throw new MsgHeaderException("Cannot have None element *and* AcknowledgementRange elements in a SequenceAcknowledgement header");
               }

               if (var2) {
                  SimpleElement.addChild(var6, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.NONE.getQualifiedName(this.getRmVersion()), "");
               } else {
                  var23 = WsrmConstants.Element.NONE.getQName(this.getRmVersion());
                  var13 = var23.getNamespaceURI();
                  var14 = var23.getLocalPart();
                  var1.startElement(var13, var14, var14, EMPTY_ATTS);
                  var1.endElement(var13, var14, var14);
               }
            }

            if (this.finalFlag) {
               if (var2) {
                  SimpleElement.addChild(var6, this.getRmVersion().getNamespaceUri(), WsrmConstants.Element.FINAL.getQualifiedName(this.getRmVersion()), "");
               } else {
                  var23 = WsrmConstants.Element.FINAL.getQName(this.getRmVersion());
                  var13 = var23.getNamespaceURI();
                  var14 = var23.getLocalPart();
                  var1.startElement(var13, var14, var14, EMPTY_ATTS);
                  var1.endElement(var13, var14, var14);
               }
            }
         }

         if (!var2) {
            var1.endElement(var4, var5, var5);
         }

         return var6;
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Sequence ID: ").append(this.sequenceId);
      if (this.nackSet) {
         var1.append(" Nack is ").append(this.nack);
      } else {
         Iterator var2 = this.listMessageRanges();

         while(var2.hasNext()) {
            MessageRange var3 = (MessageRange)var2.next();
            var1.append(" (");
            var1.append(var3.lowerBounds);
            var1.append("-");
            var1.append(var3.upperBounds);
            var1.append(")");
         }
      }

      if (this.getRmVersion().isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
         var1.append("\nNone=" + this.none);
         var1.append("\nFinal=" + this.finalFlag);
      }

      var1.append("\n");
      return var1.toString();
   }

   static {
      LOCAL_NAME = WsrmConstants.Element.ACK.getElementName();
      TYPE = new MsgHeaderType();
   }
}
