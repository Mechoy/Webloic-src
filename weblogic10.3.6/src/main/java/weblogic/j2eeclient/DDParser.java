package weblogic.j2eeclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.utils.NestedError;
import weblogic.xml.jaxp.WebLogicSAXParserFactory;

public final class DDParser extends HandlerBase {
   public static final String APPCLIENT_SUN_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN";
   public static final String APPCLIENT_SUN_SYSTEM_ID = "http://java.sun.com/dtd/application-client_1_3.dtd";
   public static final String APPCLIENT_SUN_LOCAL_ID = "application-client_1_3.dtd";
   public static final String APPCLIENT_SUN_PUBLIC_ID12 = "-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.2//EN";
   public static final String APPCLIENT_SUN_SYSTEM_ID12 = "http://java.sun.com/j2ee/dtds/application-client_1_2.dtd";
   public static final String APPCLIENT_SUN_LOCAL_ID12 = "application-client_1_2.dtd";
   public static final String APPCLIENT_BEA_PUBLIC_ID = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 J2EE Application Client//EN";
   public static final String APPCLIENT_BEA_SYSTEM_ID = "http://www.bea.com/servers/wls700/dtd/weblogic-appclient.dtd";
   public static final String APPCLIENT_BEA_LOCAL_ID = "weblogic-appclient.dtd";
   public static final String APPCLIENT_BEA_PUBLIC_ID12 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 J2EE Application Client//EN";
   public static final String APPCLIENT_BEA_SYSTEM_ID12 = "http://www.bea.com/servers/wls600/dtd/weblogic-appclient.dtd";
   public static final String APPCLIENT_BEA_LOCAL_ID12 = "weblogic-appclient12.dtd";
   private static Map element2Field = new HashMap();
   private static int NAME_LENGTH;
   private SAXParser parser;
   private Map map = new HashMap();
   private String body;
   private Descriptor currentDescriptor;
   private boolean isJ2eeRi = false;
   private static final Map localMap;

   public DDParser() throws ParserConfigurationException, SAXException, IOException {
      WebLogicSAXParserFactory var1 = new WebLogicSAXParserFactory();
      var1.setFeature("http://apache.org/xml/features/allow-java-encodings", true);
      this.parser = var1.newSAXParser();
   }

   public Descriptor[] parse(InputStream var1) throws IOException, SAXException {
      this.parser.parse(new FilterInputStream(var1) {
         public void close() throws IOException {
         }
      }, this);
      return (Descriptor[])((Descriptor[])this.map.values().toArray(new Descriptor[this.map.size()]));
   }

   public Descriptor[] parse(File var1) throws IOException, SAXException {
      return this.parse((InputStream)(new FileInputStream(var1)));
   }

   public void startElement(String var1, AttributeList var2) {
      if ("j2ee-ri-specific-information".equals(var1)) {
         this.isJ2eeRi = true;
      }

   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      String var4 = (new String(var1, var2, var3)).trim();
      if (this.body != null) {
         this.body = this.body + var4;
      } else {
         this.body = var4;
      }

   }

   public void endElement(String var1) throws SAXException {
      Field var2 = (Field)element2Field.get(var1);
      if (var2 == Descriptor.NAME) {
         this.currentDescriptor = (Descriptor)this.map.get(this.body);
         if (this.currentDescriptor == null) {
            this.currentDescriptor = new Descriptor();
            this.currentDescriptor.type = var1.substring(0, var1.length() - NAME_LENGTH);
            this.map.put(this.body, this.currentDescriptor);
         }
      }

      if (var2 != null) {
         if (this.currentDescriptor == null) {
            throw new SAXException("Tag " + var1 + " without preceding naming tag");
         }

         if (this.isJ2eeRi && "jndi-name".equals(var1) && !"java.net.URL".equals(this.currentDescriptor.type)) {
            this.body = this.body.replace('.', '_');
         }

         if ("ejb-link".equals(var1)) {
            this.body = "link:" + this.body;
         }

         try {
            var2.set(this.currentDescriptor, this.body);
         } catch (Exception var4) {
            throw new NestedError(var4);
         }
      }

      this.body = null;
   }

   public InputSource resolveEntity(String var1, String var2) throws SAXException {
      String var3 = (String)localMap.get(var1);
      return var3 == null ? super.resolveEntity(var1, var2) : new InputSource(this.getClass().getResourceAsStream(var3));
   }

   public static void main(String[] var0) throws Exception {
      DDParser var1 = new DDParser();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         System.out.println(Arrays.asList((Object[])var1.parse(new File(var0[var2]))));
      }

   }

   static {
      element2Field.put("ejb-link", Descriptor.VALUE);
      element2Field.put("ejb-ref-name", Descriptor.NAME);
      element2Field.put("env-entry-name", Descriptor.NAME);
      element2Field.put("env-entry-type", Descriptor.TYPE);
      element2Field.put("env-entry-value", Descriptor.VALUE);
      element2Field.put("jndi-name", Descriptor.VALUE);
      element2Field.put("res-ref-name", Descriptor.NAME);
      element2Field.put("res-type", Descriptor.TYPE);
      element2Field.put("resource-env-ref-name", Descriptor.NAME);
      element2Field.put("resource-env-ref-type", Descriptor.TYPE);
      element2Field.put("resource-env-ref-value", Descriptor.VALUE);
      NAME_LENGTH = "-name".length();
      localMap = new HashMap();
      localMap.put("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN", "application-client_1_3.dtd");
      localMap.put("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.2//EN", "application-client_1_2.dtd");
      localMap.put("-//BEA Systems, Inc.//DTD WebLogic 7.0.0 J2EE Application Client//EN", "weblogic-appclient.dtd");
      localMap.put("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 J2EE Application Client//EN", "weblogic-appclient12.dtd");
   }
}
