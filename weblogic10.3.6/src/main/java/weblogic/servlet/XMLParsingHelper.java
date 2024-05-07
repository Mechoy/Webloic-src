package weblogic.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParsingHelper implements Filter {
   public void init(FilterConfig var1) throws ServletException {
   }

   public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
      XMLParsingRequestWrapper var4 = new XMLParsingRequestWrapper(var1);
      var3.doFilter(var4, var2);
   }

   public void destroy() {
   }

   private class XMLParsingRequestWrapper extends HttpServletRequestWrapper {
      public XMLParsingRequestWrapper(ServletRequest var2) {
         super((HttpServletRequest)var2);
      }

      public Object getAttribute(String var1) {
         Object var2 = super.getAttribute(var1);
         if (var2 != null) {
            return var2;
         } else {
            String var3 = this.getContentType();
            if (var1 == null || var3 == null || !"POST".equals(this.getMethod()) || !var1.equals("org.w3c.dom.Document") || !var3.startsWith("text/xml") && !var3.startsWith("application/xml")) {
               return null;
            } else {
               try {
                  DocumentBuilderFactory var4 = DocumentBuilderFactory.newInstance();
                  var4.setNamespaceAware(true);
                  DocumentBuilder var5 = var4.newDocumentBuilder();
                  EntityResolver var6 = (EntityResolver)this.getAttribute("org.xml.sax.EntityResolver");
                  if (var6 != null) {
                     var5.setEntityResolver(var6);
                  }

                  return var5.parse(this.getInputStream());
               } catch (Exception var7) {
                  throw new XMLProcessingException("Could not parse XML into a document", var7);
               }
            }
         }
      }

      public void setAttribute(String var1, Object var2) {
         if (var1 != null) {
            String var3 = this.getContentType();
            if (var2 != null && var3 != null && "POST".equals(this.getMethod()) && (var3.startsWith("text/xml") || var3.startsWith("application/xml"))) {
               boolean var4 = false;
               boolean var5 = false;
               if (var1.equals("org.xml.sax.HandlerBase") && var2 instanceof HandlerBase) {
                  var4 = true;
               } else if (var1.equals("org.xml.sax.helpers.DefaultHandler") && var2 instanceof DefaultHandler) {
                  var5 = true;
               }

               if (var4 || var5) {
                  this.removeAttribute(var1);

                  try {
                     SAXParserFactory var6 = SAXParserFactory.newInstance();
                     var6.setNamespaceAware(true);
                     SAXParser var7 = var6.newSAXParser();
                     if (var4) {
                        var7.parse(this.getInputStream(), (HandlerBase)var2);
                     } else {
                        var7.parse(this.getInputStream(), (DefaultHandler)var2);
                     }

                     return;
                  } catch (ParserConfigurationException var8) {
                     throw new XMLProcessingException("Could not parse posted XML into SAX events. " + var8.getMessage(), var8);
                  } catch (SAXException var9) {
                     throw new XMLProcessingException("Could not parse posted XML into SAX events." + var9.getMessage(), var9);
                  } catch (IOException var10) {
                     throw new XMLProcessingException("Could not parse posted XML into SAX events." + var10.getMessage(), var10);
                  }
               }
            }

            super.setAttribute(var1, var2);
         }
      }
   }
}
