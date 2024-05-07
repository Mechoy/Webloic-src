package weblogic.wsee.jaxws.workcontext;

import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.pipe.Tube;
import com.sun.xml.ws.api.pipe.TubeCloner;
import com.sun.xml.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextInput;
import weblogic.workarea.WorkContextMap;
import weblogic.workarea.WorkContextOutput;
import weblogic.workarea.spi.WorkContextMapInterceptor;
import weblogic.workarea.utils.WorkContextInputAdapter;
import weblogic.workarea.utils.WorkContextOutputAdapter;
import weblogic.wsee.workarea.WorkContextXmlInputAdapter;
import weblogic.wsee.workarea.WorkContextXmlOutputAdapter;

public abstract class WorkContextTube extends AbstractFilterTubeImpl {
   String JAX_WS_WORK_NS = "http://oracle.com/weblogic/soap/workarea/";
   QName JAX_WS_WORK_AREA_HEADER;
   private static final boolean skipWorkAreaHeader = Boolean.getBoolean("weblogic.wsee.workarea.skipWorkAreaHeader");

   public WorkContextTube(Tube var1) {
      super(var1);
      this.JAX_WS_WORK_AREA_HEADER = new QName(this.JAX_WS_WORK_NS, "WorkContext", "work");
   }

   protected WorkContextTube(WorkContextTube var1, TubeCloner var2) {
      super(var1, var2);
      this.JAX_WS_WORK_AREA_HEADER = new QName(this.JAX_WS_WORK_NS, "WorkContext", "work");
   }

   protected static final WorkContextMapInterceptor getContext() {
      return WorkContextHelper.getWorkContextHelper().getLocalInterceptor();
   }

   protected boolean hasContext(int var1, boolean var2) {
      if (skipWorkAreaHeader) {
         return false;
      } else {
         WorkContextMap var3 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
         if (var3 == null) {
            return false;
         } else {
            Iterator var4 = var3.keys();

            int var6;
            do {
               do {
                  if (!var4.hasNext()) {
                     return false;
                  }

                  String var5 = (String)var4.next();
                  var6 = var3.getPropagationMode(var5);
               } while((var6 & var1) == 0);
            } while(var2 && (var6 & 256) != 0);

            return true;
         }
      }
   }

   protected abstract void receive(WorkContextInput var1) throws IOException;

   protected abstract void send(WorkContextMapInterceptor var1, WorkContextOutput var2, int var3) throws IOException;

   protected void readHeader(Header var1) {
      try {
         this.receive(new WorkContextInputAdapter(new ObjectInputStream(new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(var1.getStringContent())))));
      } catch (IOException var3) {
         throw new WebServiceException(var3);
      }
   }

   protected void readHeaderOld(Header var1) {
      try {
         XMLStreamReader var2 = var1.readHeader();
         var2.nextTag();
         var2.nextTag();
         XMLStreamReaderToXMLStreamWriter var3 = new XMLStreamReaderToXMLStreamWriter();
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         XMLStreamWriter var5 = XMLStreamWriterFactory.create(var4);
         var3.bridge(var2, var5);
         var5.close();
         WorkContextXmlInputAdapter var6 = new WorkContextXmlInputAdapter(new ByteArrayInputStream(var4.toByteArray()));
         this.receive(var6);
      } catch (XMLStreamException var7) {
         throw new WebServiceException(var7);
      } catch (IOException var8) {
         throw new WebServiceException(var8);
      }
   }

   protected void writeHeader(WorkContextMapInterceptor var1, HeaderList var2) {
      this.writeHeader(var1, var2, false);
   }

   protected void writeHeader(WorkContextMapInterceptor var1, HeaderList var2, boolean var3) {
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      if (var3) {
         WorkContextXmlOutputAdapter var5 = new WorkContextXmlOutputAdapter(var4);

         try {
            this.send(var1, var5, 64);
            var5.close();
            DocumentBuilderFactory var6 = DocumentBuilderFactory.newInstance();
            var6.setNamespaceAware(true);
            var6.setValidating(false);
            DocumentBuilder var7 = var6.newDocumentBuilder();
            Document var8 = var7.parse(new ByteArrayInputStream(var4.toByteArray()));
            Element var9 = var8.getDocumentElement();
            if (var9 != null) {
               var9.removeAttribute("version");
            }

            Document var10 = var7.newDocument();
            Element var11 = var10.createElementNS("http://bea.com/2004/06/soap/workarea/", "work:WorkContext");
            var11.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:work", "http://bea.com/2004/06/soap/workarea/");
            var11.appendChild(var11.getOwnerDocument().importNode(var9, true));
            var2.add(Headers.create(var11));
         } catch (IOException var13) {
            throw new WebServiceException(var13);
         } catch (ParserConfigurationException var14) {
            throw new WebServiceException(var14);
         } catch (SAXException var15) {
            throw new WebServiceException(var15);
         }
      } else {
         try {
            ObjectOutputStream var16 = new ObjectOutputStream(var4);
            this.send(var1, new WorkContextOutputAdapter(var16), 64);
            var16.close();
            var2.add(Headers.create(this.JAX_WS_WORK_AREA_HEADER, DatatypeConverter.printBase64Binary(var4.toByteArray())));
         } catch (IOException var12) {
            throw new WebServiceException(var12);
         }
      }

   }
}
