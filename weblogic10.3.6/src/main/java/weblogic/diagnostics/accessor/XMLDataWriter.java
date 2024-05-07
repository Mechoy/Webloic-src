package weblogic.diagnostics.accessor;

import java.io.OutputStream;
import java.util.Iterator;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.utils.PlatformConstants;
import weblogic.xml.stax.XMLStreamOutputFactory;

public final class XMLDataWriter {
   private static final XMLOutputFactory xmlOutputFactory = XMLStreamOutputFactory.newInstance();
   private static final String DIAG_DATA = "DiagnosticData";
   private static final String DATA_INFO = "DataInfo";
   private static final String COLUMN_INFO = "ColumnInfo";
   private static final String COL_NAME = "Name";
   private static final String COL_TYPE = "Type";
   private static final String DATA_REC = "DataRecord";
   private static final String COL_DATA = "ColumnData";
   private static final String INDENT = "  ";
   static final String EXPORT_SCHEMA_NAME_SPACE = "http://www.bea.com/ns/weblogic/90/diagnostics/accessor/Export";
   static final String EXPORT_SCHEMA_URL = "http://www.bea.com/ns/weblogic/90/diagnostics/accessor/export.xsd export.xsd";
   private XMLStreamWriter writer;
   private boolean autoFlush;

   public XMLDataWriter(OutputStream var1) throws XMLStreamException {
      this(var1, true);
   }

   public XMLDataWriter(OutputStream var1, boolean var2) throws XMLStreamException {
      this.writer = xmlOutputFactory.createXMLStreamWriter(var1, "UTF-8");
      this.autoFlush = var2;
   }

   public void exportDiagnosticDataToXML(ColumnInfo[] var1, Iterator var2) throws XMLStreamException {
      this.writer.writeStartDocument();
      this.writer.writeCharacters(PlatformConstants.EOL);
      this.writer.writeStartElement("DiagnosticData");
      this.writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      this.writer.writeAttribute("xsi:schemaLocation", "http://www.bea.com/ns/weblogic/90/diagnostics/accessor/export.xsd export.xsd");
      this.writer.writeAttribute("xmlns", "http://www.bea.com/ns/weblogic/90/diagnostics/accessor/Export");
      this.writer.writeCharacters(PlatformConstants.EOL);
      this.writeColumnInfo(var1);
      this.writeData(var1.length, var2);
      this.writer.writeEndElement();
      this.writer.writeCharacters(PlatformConstants.EOL);
      this.writer.writeEndDocument();
      if (this.autoFlush) {
         this.writer.flush();
      }

      this.writer.close();
   }

   private void writeColumnInfo(ColumnInfo[] var1) throws XMLStreamException {
      if (var1 != null) {
         this.writer.writeCharacters("  ");
         this.writer.writeStartElement("DataInfo");
         this.writer.writeCharacters(PlatformConstants.EOL);

         for(int var2 = 0; var2 < var1.length; ++var2) {
            ColumnInfo var3 = var1[var2];
            this.writer.writeCharacters("    ");
            this.writer.writeStartElement("ColumnInfo");
            this.writeElement("Name", var3.getColumnName());
            this.writeElement("Type", var3.getColumnTypeName());
            this.writer.writeEndElement();
            this.writer.writeCharacters(PlatformConstants.EOL);
            if (this.autoFlush) {
               this.writer.flush();
            }
         }

         this.writer.writeCharacters("  ");
         this.writer.writeEndElement();
         this.writer.writeCharacters(PlatformConstants.EOL);
      }
   }

   private void writeData(int var1, Iterator var2) throws XMLStreamException {
      if (var2 != null) {
         while(var2.hasNext()) {
            this.writer.writeCharacters("  ");
            this.writer.writeStartElement("DataRecord");
            DataRecord var3 = (DataRecord)var2.next();

            for(int var4 = 0; var4 < var1; ++var4) {
               this.writer.writeStartElement("ColumnData");
               Object var5 = var3.get(var4);
               this.writer.writeCharacters(var5 == null ? "" : var5.toString());
               this.writer.writeEndElement();
            }

            this.writer.writeEndElement();
            this.writer.writeCharacters(PlatformConstants.EOL);
            if (this.autoFlush) {
               this.writer.flush();
            }
         }

      }
   }

   private void writeElement(String var1, String var2) throws XMLStreamException {
      this.writer.writeStartElement(var1);
      this.writer.writeCharacters(var2);
      this.writer.writeEndElement();
   }
}
