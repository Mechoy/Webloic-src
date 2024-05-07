package weblogic.xml.dom;

import com.bea.xbean.common.InvalidLexicalValueException;
import com.bea.xbean.common.XmlWhitespace;
import com.bea.xbean.richParser.XMLStreamReaderExt;
import com.bea.xbean.util.Base64;
import com.bea.xbean.util.HexBin;
import com.bea.xbean.util.XsTypeConverter;
import com.bea.xml.GDate;
import com.bea.xml.GDateBuilder;
import com.bea.xml.GDuration;
import com.bea.xml.XmlCalendar;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Node;
import weblogic.xml.util.WhitespaceUtils;

public class DOMStreamReaderExt extends DOMStreamReader implements XMLStreamReaderExt {
   private final List contents = new ArrayList();
   private String _defaultValue;

   public DOMStreamReaderExt(Node var1) throws XMLStreamException {
      super(var1);
   }

   public String getStringValue() throws XMLStreamException {
      return this.getCurrentPreserveCharSeq().toString();
   }

   public String getStringValue(int var1) throws XMLStreamException {
      return XmlWhitespace.collapse(this.getCurrentPreserveCharSeq().toString(), var1);
   }

   public boolean getBooleanValue() throws XMLStreamException, InvalidLexicalValueException {
      return XsTypeConverter.lexBoolean(this.getCurrentTrimCharSeq());
   }

   public byte getByteValue() throws XMLStreamException, InvalidLexicalValueException {
      return XsTypeConverter.lexByte(this.getCurrentTrimCharSeq());
   }

   public short getShortValue() throws XMLStreamException, InvalidLexicalValueException {
      return XsTypeConverter.lexShort(this.getCurrentTrimCharSeq());
   }

   public int getIntValue() throws XMLStreamException, InvalidLexicalValueException {
      return XsTypeConverter.lexInt(this.getCurrentTrimCharSeq());
   }

   public long getLongValue() throws XMLStreamException {
      return XsTypeConverter.lexLong(this.getCurrentTrimCharSeq());
   }

   public BigInteger getBigIntegerValue() throws XMLStreamException {
      CharSequence var1 = this.getCurrentTrimCharSeq();
      return "".equals(var1.toString()) ? null : XsTypeConverter.lexInteger(var1);
   }

   public BigDecimal getBigDecimalValue() throws XMLStreamException {
      CharSequence var1 = this.getCurrentTrimCharSeq();
      return "".equals(var1.toString()) ? null : XsTypeConverter.lexDecimal(var1);
   }

   public float getFloatValue() throws XMLStreamException {
      return XsTypeConverter.lexFloat(this.getCurrentTrimCharSeq());
   }

   public double getDoubleValue() throws XMLStreamException {
      return XsTypeConverter.lexDouble(this.getCurrentTrimCharSeq());
   }

   public InputStream getHexBinaryValue() throws XMLStreamException, InvalidLexicalValueException {
      String var1 = this.getCurrentTrimCharSeq().toString();
      byte[] var2 = HexBin.decode(var1.getBytes());
      if (var2 != null) {
         return new ByteArrayInputStream(var2);
      } else {
         throw new InvalidLexicalValueException("invalid hexBinary value");
      }
   }

   public InputStream getBase64Value() throws XMLStreamException, InvalidLexicalValueException {
      String var1 = this.getCurrentTrimCharSeq().toString();
      byte[] var2 = Base64.decode(var1.getBytes());
      if (var2 != null) {
         return new ByteArrayInputStream(var2);
      } else {
         throw new InvalidLexicalValueException("invalid base64Binary value");
      }
   }

   public XmlCalendar getCalendarValue() throws XMLStreamException, InvalidLexicalValueException {
      try {
         CharSequence var1 = this.getCurrentTrimCharSeq();
         return var1.length() == 0 ? null : (new GDateBuilder(var1)).getCalendar();
      } catch (IllegalArgumentException var2) {
         throw new InvalidLexicalValueException(var2);
      }
   }

   public Date getDateValue() throws XMLStreamException, InvalidLexicalValueException {
      try {
         return (new GDateBuilder(this.getCurrentTrimCharSeq())).getDate();
      } catch (IllegalArgumentException var2) {
         throw new InvalidLexicalValueException(var2);
      }
   }

   public GDate getGDateValue() throws XMLStreamException, InvalidLexicalValueException {
      try {
         return XsTypeConverter.lexGDate(this.getCurrentTrimCharSeq());
      } catch (IllegalArgumentException var2) {
         throw new InvalidLexicalValueException(var2);
      }
   }

   public GDuration getGDurationValue() throws XMLStreamException, InvalidLexicalValueException {
      try {
         return new GDuration(this.getCurrentTrimCharSeq());
      } catch (IllegalArgumentException var2) {
         throw new InvalidLexicalValueException(var2);
      }
   }

   public QName getQNameValue() throws XMLStreamException, InvalidLexicalValueException {
      try {
         return XsTypeConverter.lexQName(this.getCurrentTrimCharSeq(), this.getNamespaceContext());
      } catch (InvalidLexicalValueException var2) {
         throw new InvalidLexicalValueException(var2);
      }
   }

   public String getAttributeStringValue(int var1) throws XMLStreamException {
      return this.getAttributeValue(var1);
   }

   public String getAttributeStringValue(int var1, int var2) throws XMLStreamException {
      return XmlWhitespace.collapse(this.getAttributeValue(var1), var2);
   }

   public boolean getAttributeBooleanValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexBoolean(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (InvalidLexicalValueException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public byte getAttributeByteValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexByte(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public short getAttributeShortValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexShort(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public int getAttributeIntValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexInt(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public long getAttributeLongValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexLong(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public BigInteger getAttributeBigIntegerValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexInteger(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public BigDecimal getAttributeBigDecimalValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexDecimal(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public float getAttributeFloatValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexFloat(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public double getAttributeDoubleValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexDouble(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (NumberFormatException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public InputStream getAttributeHexBinaryValue(int var1) throws XMLStreamException {
      String var2 = this.getCurrentAttributeTrimCharSeq(var1).toString();
      byte[] var3 = HexBin.decode(var2.getBytes());
      if (var3 != null) {
         return new ByteArrayInputStream(var3);
      } else {
         throw new InvalidLexicalValueException("invalid hexBinary value");
      }
   }

   public InputStream getAttributeBase64Value(int var1) throws XMLStreamException {
      String var2 = this.getCurrentAttributeTrimCharSeq(var1).toString();
      byte[] var3 = Base64.decode(var2.getBytes());
      if (var3 != null) {
         return new ByteArrayInputStream(var3);
      } else {
         throw new InvalidLexicalValueException("invalid base64Binary value");
      }
   }

   public XmlCalendar getAttributeCalendarValue(int var1) throws XMLStreamException {
      try {
         return (new GDateBuilder(this.getCurrentAttributeTrimCharSeq(var1))).getCalendar();
      } catch (IllegalArgumentException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public Date getAttributeDateValue(int var1) throws XMLStreamException {
      try {
         return (new GDateBuilder(this.getCurrentAttributeTrimCharSeq(var1))).getDate();
      } catch (IllegalArgumentException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public GDate getAttributeGDateValue(int var1) throws XMLStreamException {
      try {
         return new GDate(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (IllegalArgumentException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public GDuration getAttributeGDurationValue(int var1) throws XMLStreamException {
      try {
         return new GDuration(this.getCurrentAttributeTrimCharSeq(var1));
      } catch (IllegalArgumentException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public QName getAttributeQNameValue(int var1) throws XMLStreamException {
      try {
         return XsTypeConverter.lexQName(this.getCurrentAttributeTrimCharSeq(var1), this.getNamespaceContext());
      } catch (InvalidLexicalValueException var3) {
         throw new InvalidLexicalValueException(var3);
      }
   }

   public String getAttributeStringValue(String var1, String var2) throws XMLStreamException {
      return this.getAttributeValue(var1, var2);
   }

   public String getAttributeStringValue(String var1, String var2, int var3) throws XMLStreamException {
      return XmlWhitespace.collapse(this.getAttributeValue(var1, var2), var3);
   }

   public boolean getAttributeBooleanValue(String var1, String var2) throws XMLStreamException {
      return XsTypeConverter.lexBoolean(this.getCurrentAttributeTrimCharSeq(var1, var2));
   }

   public byte getAttributeByteValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexByte(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public short getAttributeShortValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexShort(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public int getAttributeIntValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexInt(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public long getAttributeLongValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexLong(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public BigInteger getAttributeBigIntegerValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexInteger(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public BigDecimal getAttributeBigDecimalValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexDecimal(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public float getAttributeFloatValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexFloat(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public double getAttributeDoubleValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexDouble(var3);
      } catch (NumberFormatException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public InputStream getAttributeHexBinaryValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);
      String var4 = var3.toString();
      byte[] var5 = HexBin.decode(var4.getBytes());
      if (var5 != null) {
         return new ByteArrayInputStream(var5);
      } else {
         throw new InvalidLexicalValueException("invalid hexBinary value");
      }
   }

   public InputStream getAttributeBase64Value(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);
      String var4 = var3.toString();
      byte[] var5 = Base64.decode(var4.getBytes());
      if (var5 != null) {
         return new ByteArrayInputStream(var5);
      } else {
         throw new InvalidLexicalValueException("invalid base64Binary value");
      }
   }

   public XmlCalendar getAttributeCalendarValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return (new GDateBuilder(var3)).getCalendar();
      } catch (IllegalArgumentException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public Date getAttributeDateValue(String var1, String var2) throws XMLStreamException {
      try {
         CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);
         return (new GDateBuilder(var3)).getDate();
      } catch (IllegalArgumentException var4) {
         throw new InvalidLexicalValueException(var4);
      }
   }

   public GDate getAttributeGDateValue(String var1, String var2) throws XMLStreamException {
      try {
         CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);
         return new GDate(var3);
      } catch (IllegalArgumentException var4) {
         throw new InvalidLexicalValueException(var4);
      }
   }

   public GDuration getAttributeGDurationValue(String var1, String var2) throws XMLStreamException {
      try {
         return new GDuration(this.getCurrentAttributeTrimCharSeq(var1, var2));
      } catch (IllegalArgumentException var4) {
         throw new InvalidLexicalValueException(var4);
      }
   }

   public QName getAttributeQNameValue(String var1, String var2) throws XMLStreamException {
      CharSequence var3 = this.getCurrentAttributeTrimCharSeq(var1, var2);

      try {
         return XsTypeConverter.lexQName(var3, this.getNamespaceContext());
      } catch (InvalidLexicalValueException var5) {
         throw new InvalidLexicalValueException(var5);
      }
   }

   public void setDefaultValue(String var1) throws XMLStreamException {
      this._defaultValue = var1;
   }

   private CharSequence getCurrentTrimCharSeq() throws XMLStreamException {
      assert this.contents.isEmpty();

      this.fillContents();
      CharSequence var1 = this.getTrimCharSeq();
      var1 = this.handleDefault(var1);
      this.contents.clear();
      return var1;
   }

   private CharSequence getCurrentPreserveCharSeq() throws XMLStreamException {
      assert this.contents.isEmpty();

      this.fillContents();
      CharSequence var1 = this.getPreserveCharSeq();
      var1 = this.handleDefault(var1);
      this.contents.clear();
      return var1;
   }

   private CharSequence handleDefault(CharSequence var1) {
      if (var1.length() == 0 && this._defaultValue != null) {
         var1 = DOMStreamReaderExt.SingleStringTrimWsCharSeq.create(this._defaultValue);
      }

      this._defaultValue = null;
      return var1;
   }

   private CharSequence getTrimCharSeq() {
      int var1 = this.contents.size();
      if (var1 == 0) {
         return "";
      } else {
         return var1 == 1 ? DOMStreamReaderExt.SingleStringTrimWsCharSeq.create((CharSequence)this.contents.get(0)) : DOMStreamReaderExt.MultiStringTrimWsCharSeq.create(this.contents);
      }
   }

   private CharSequence getPreserveCharSeq() {
      int var1 = this.contents.size();
      if (var1 == 0) {
         return "";
      } else {
         return var1 == 1 ? (CharSequence)this.contents.get(0) : DOMStreamReaderExt.MultiStringCharSeq.create(this.contents);
      }
   }

   private void fillContents() throws XMLStreamException {
      if (this.getEventType() == 7) {
         this.next();
      }

      if (this.isStartElement()) {
         this.next();
      }

      int var1 = 0;
      String var2 = null;
      int var3 = this.getEventType();

      label35:
      while(true) {
         switch (var3) {
            case 1:
               ++var1;
               var2 = "Unexpected element '" + this.getName() + "' in text content.";
               break;
            case 2:
               --var1;
               if (var1 < 0) {
                  break label35;
               }
            case 3:
            case 5:
            case 7:
            case 10:
            case 11:
            default:
               break;
            case 4:
            case 6:
            case 9:
            case 12:
               if (var1 == 0) {
                  this.contents.add(this.getText());
               }
               break;
            case 8:
               break label35;
         }

         var3 = this.next();
      }

      if (var2 != null) {
         throw new XMLStreamException(var2);
      }
   }

   private CharSequence getCurrentAttributeTrimCharSeq(int var1) {
      return DOMStreamReaderExt.SingleStringTrimWsCharSeq.create(this.getAttributeValue(var1));
   }

   private CharSequence getCurrentAttributeTrimCharSeq(String var1, String var2) {
      return DOMStreamReaderExt.SingleStringTrimWsCharSeq.create(this.getAttributeValue(var1, var2));
   }

   private static final class MultiStringCharSeq {
      public static CharSequence create(List var0) {
         if (var0.isEmpty()) {
            return "";
         } else {
            StringBuilder var1 = new StringBuilder();
            int var2 = 0;

            for(int var3 = var0.size(); var2 < var3; ++var2) {
               var1.append(var0.get(var2));
            }

            return var1;
         }
      }
   }

   private static final class MultiStringTrimWsCharSeq {
      public static CharSequence create(List var0) {
         assert !var0.isEmpty();

         StringBuilder var1 = new StringBuilder();
         int var2 = 0;

         for(int var3 = var0.size(); var2 < var3; ++var2) {
            var1.append(var0.get(var2));
         }

         return DOMStreamReaderExt.SingleStringTrimWsCharSeq.create(var1);
      }
   }

   private static final class SingleStringTrimWsCharSeq implements CharSequence {
      private final String str;
      private final int start;
      private final int length;

      static CharSequence create(CharSequence var0) {
         int var1 = var0.length();
         if (var1 == 0) {
            return "";
         } else {
            int var2;
            for(var2 = 0; var2 < var1 && WhitespaceUtils.isWhitespace(var0.charAt(var2)); ++var2) {
            }

            int var3;
            for(var3 = var1 - 1; var3 >= var2 && WhitespaceUtils.isWhitespace(var0.charAt(var3)); --var3) {
            }

            return (CharSequence)(var2 == var3 + 1 ? "" : var0.subSequence(var2, var3 + 1));
         }
      }

      private SingleStringTrimWsCharSeq(String var1, int var2, int var3) {
         this.str = var1;
         this.start = var2;
         this.length = var3;
      }

      public int length() {
         return this.length;
      }

      public char charAt(int var1) {
         return this.str.charAt(this.start + var1);
      }

      public CharSequence subSequence(int var1, int var2) {
         return this.str.subSequence(this.start + var1, this.start + var2);
      }
   }
}
