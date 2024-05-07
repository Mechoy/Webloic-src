package weblogic.wsee.jaxrpc;

import java.util.HashMap;
import java.util.Map;

public class Mime2JavaMapping {
   private static final String MIME_IMAGE_GIF = "image/gif";
   private static final String MIME_IMAGE_JPG = "image/jpeg";
   private static final String MIME_TEXT = "text/plain";
   private static final String MIME_MULTIPART = "multipart";
   private static final String MIME_TEXT_XML = "text/xml";
   private static final String MIME_APP_XML = "application/xml";
   private static final String JAVA_IMAGE = "java.awt.Image";
   private static final String JAVA_TEXT = "java.lang.String";
   private static final String JAVA_MULTIPART = "javax.mail.internet.MimeMultipart";
   private static final String JAVA_XML = "javax.xml.transform.Source";
   private static Map<String, String> mime2JavaMapping = new HashMap();

   public static String getJavaTypeFromMime(String var0) {
      return var0.startsWith("multipart") ? "javax.mail.internet.MimeMultipart" : (String)mime2JavaMapping.get(var0);
   }

   static {
      mime2JavaMapping.put("image/gif", "java.awt.Image");
      mime2JavaMapping.put("image/jpeg", "java.awt.Image");
      mime2JavaMapping.put("text/plain", "java.lang.String");
      mime2JavaMapping.put("text/xml", "javax.xml.transform.Source");
      mime2JavaMapping.put("application/xml", "javax.xml.transform.Source");
   }
}
