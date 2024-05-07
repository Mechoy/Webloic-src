package weblogic.xml.crypto.dsig;

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.xml.crypto.NodeSetDataImpl;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.NodeSetData;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.dsig.api.Transform;
import weblogic.xml.crypto.dsig.api.XMLSignatureException;
import weblogic.xml.crypto.dsig.api.spec.TransformParameterSpec;
import weblogic.xml.crypto.dsig.api.spec.XPathFilter2ParameterSpec;
import weblogic.xml.crypto.utils.DOMUtils;
import weblogic.xml.crypto.utils.DataUtils;
import weblogic.xml.crypto.utils.DebugStreamReader;
import weblogic.xml.crypto.utils.LogUtils;
import weblogic.xml.dom.DOMStreamReader;
import weblogic.xml.dom.DOMStreamWriter;

public class XPathFilter2Transform extends TransformImpl implements NodeTransform, TransformFactory {
   private static final String XPATH_ELEMENT = "XPath";
   private static final String XPATH_NS_PREFIX = "dsxp";
   private static final String XPATH_NS_URI = "http://www.w3.org/2002/06/xmldsig-filter2";
   public static final String FILTER_ATTRIBUTE = "Filter";
   private static final TransformFactory factory = new XPathFilter2Transform();
   private XPathFilter2ParameterSpec params;
   private List hereNodes = new ArrayList();

   protected XPathFilter2Transform() {
   }

   protected XPathFilter2Transform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      if (var1 != null) {
         if (!(var1 instanceof XPathFilter2ParameterSpec)) {
            throw new InvalidAlgorithmParameterException("expected type: XPathFilter2ParameterSpec");
         }

         this.params = (XPathFilter2ParameterSpec)var1;
      }

   }

   public static void init() {
      register(factory);
   }

   public String getAlgorithm() {
      return "http://www.w3.org/2002/06/xmldsig-filter2";
   }

   public AlgorithmParameterSpec getParameterSpec() {
      return this.params;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public Data transform(Data var1, XMLCryptoContext var2) throws XMLSignatureException {
      if (this.params == null) {
         throw new XMLSignatureException("No XPath Filter2 parameter.");
      } else {
         NodeSetData var3 = DataUtils.extractNodeSetData(var1);
         if (!var3.iterator().hasNext()) {
            LogUtils.logDsig("empty input set, empty output set.");
            return var1;
         } else {
            List var4 = null;

            try {
               Node var5 = (Node)var3.iterator().next();
               Document var6 = var5 instanceof Document ? (Document)var5 : var5.getOwnerDocument();
               var4 = this.evaluate(this.params, this.hereNodes, var6);
            } catch (XPathExpressionException var11) {
               throw new XMLSignatureException("Failed to evaluate xpath expression.", var11);
            }

            if (var4.isEmpty()) {
               LogUtils.logDsig("no xpath filter exists, output set is same as input set.");
               return var1;
            } else {
               if (var4.size() == 1) {
                  NodeSetFilterBlock var12 = (NodeSetFilterBlock)var4.get(0);
                  if (var12.intersect.isEmpty() && var12.subtract.isEmpty()) {
                     LogUtils.logDsig("only union xpath filters exist, output set is same as input set.");
                     return var1;
                  }
               }

               LinkedHashSet var13 = new LinkedHashSet();
               int var14 = 0;
               Iterator var7 = var3.iterator();

               while(true) {
                  while(var7.hasNext()) {
                     Node var8 = (Node)var7.next();
                     ++var14;
                     Iterator var9 = var4.iterator();

                     while(var9.hasNext()) {
                        NodeSetFilterBlock var10 = (NodeSetFilterBlock)var9.next();
                        if (var10.filterTest(var8)) {
                           var13.add(var8);
                           break;
                        }
                     }
                  }

                  LogUtils.logDsig(var14 + " nodes in input set, " + var13.size() + " nodes in result set");
                  return new NodeSetDataImpl(var13);
               }
            }
         }
      }
   }

   private List evaluate(XPathFilter2ParameterSpec var1, List var2, Document var3) throws XPathExpressionException, XMLSignatureException {
      ArrayList var4 = new ArrayList();
      LinkedHashSet var5 = new LinkedHashSet();
      var5.add(var3);
      NodeSetFilter var6 = new NodeSetFilter(1, var5);
      NodeSetFilterBlock var7 = new NodeSetFilterBlock(var6);
      var4.add(var7);
      XPath var8 = XPathFactory.newInstance().newXPath();
      Iterator var9 = var2.iterator();
      NamespaceContextImpl var10 = new NamespaceContextImpl();
      XPathFunctionResolverImpl var11 = new XPathFunctionResolverImpl();

      for(Iterator var12 = var1.getXPathList().iterator(); var12.hasNext(); var8.reset()) {
         weblogic.xml.crypto.dsig.api.spec.XPath var13 = (weblogic.xml.crypto.dsig.api.spec.XPath)var12.next();
         Node var14 = null;
         if (var9.hasNext()) {
            var14 = (Node)var9.next();
         }

         var10.setNamespaceDeclarations(var13.getNamespaceMap());
         var8.setNamespaceContext(var10);
         var11.setHere(var14);
         var8.setXPathFunctionResolver(var11);
         String var15 = var13.getExpression();
         var15 = XPathFilter2Transform.XPathFunctionResolverImpl.translateHereFunc(var15);
         NodeList var16 = (NodeList)var8.evaluate(var15, var3.getDocumentElement(), XPathConstants.NODESET);
         String var17 = var13.getFilter().toString();
         NodeSetFilter var18 = XPathFilter2Transform.NodeSetFilter.create(var16, var17);
         switch (var18.operation) {
            case 0:
            default:
               throw new XMLSignatureException("Failed to evaluate: unkown operation");
            case 1:
               if (var7.intersect.isEmpty() && var7.subtract.isEmpty()) {
                  var7.union.add(var18);
               } else {
                  var7 = new NodeSetFilterBlock(var18);
                  var4.add(var7);
               }
               break;
            case 2:
               var7.intersect.add(var18);
               break;
            case 3:
               var7.subtract.add(var18);
         }
      }

      return var4;
   }

   public static void main(String[] var0) {
      try {
         System.out.println(XPathFactory.newInstance().newXPath().evaluate("string-length('abc')", new Object()));
      } catch (XPathExpressionException var2) {
         var2.printStackTrace();
      }

      System.out.println(XPathFilter2Transform.XPathFunctionResolverImpl.translateHereFunc("a-here ( ) + b"));
   }

   protected void writeParameters(XMLStreamWriter var1) throws MarshalException {
      this.hereNodes.clear();

      try {
         var1.writeNamespace("dsxp", "http://www.w3.org/2002/06/xmldsig-filter2");
         Iterator var2 = this.params.getXPathList().iterator();

         while(var2.hasNext()) {
            weblogic.xml.crypto.dsig.api.spec.XPath var3 = (weblogic.xml.crypto.dsig.api.spec.XPath)var2.next();
            var1.writeStartElement("http://www.w3.org/2002/06/xmldsig-filter2", "XPath");
            if (var1 instanceof DOMStreamWriter) {
               DOMStreamWriter var4 = (DOMStreamWriter)var1;
               Node var5 = var4.getCurrentNode();
               this.hereNodes.add(var5);
            } else {
               this.hereNodes.add((Object)null);
            }

            var1.writeAttribute("Filter", var3.getFilter().toString());
            this.writeNamespaces(var3.getNamespaceMap(), var1);
            var1.writeCharacters(var3.getExpression());
            var1.writeEndElement();
         }

      } catch (XMLStreamException var6) {
         throw new MarshalException("Failed to write XPath transform element.", var6);
      }
   }

   protected void readParameters(XMLStreamReader var1) throws MarshalException {
      this.hereNodes.clear();
      ArrayList var2 = new ArrayList();

      try {
         while(true) {
            var1.nextTag();
            var1.require(1, "http://www.w3.org/2002/06/xmldsig-filter2", "XPath");
            DOMStreamReader var3 = null;
            if (var1 instanceof DOMStreamReader) {
               var3 = (DOMStreamReader)var1;
            } else if (var1 instanceof DebugStreamReader) {
               XMLStreamReader var4 = ((DebugStreamReader)var1).getDelegate();
               if (var4 instanceof DOMStreamReader) {
                  var3 = (DOMStreamReader)var4;
               }
            }

            Node var9 = null;
            if (var3 != null) {
               var9 = var3.current();
               this.hereNodes.add(var9);
            } else {
               this.hereNodes.add((Object)null);
            }

            Map var5 = this.readNamespaces(var9);
            String var6 = var1.getAttributeValue((String)null, "Filter");
            String var7 = var1.getElementText();
            var2.add(new weblogic.xml.crypto.dsig.api.spec.XPath(var7, getFilter(var6), var5));
         }
      } catch (XMLStreamException var8) {
         if (var2.size() <= 0) {
            throw new MarshalException("Failed to read XPath transform element.", var8);
         } else {
            this.params = new XPathFilter2ParameterSpec(var2);
         }
      }
   }

   private Map readNamespaces(Node var1) {
      return (Map)(var1 != null ? DOMUtils.getNamespaceMap(var1) : new HashMap());
   }

   private void writeNamespaces(Map var1, XMLStreamWriter var2) throws XMLStreamException {
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         String var5 = (String)var1.get(var4);
         var2.writeNamespace(var4, var5);
      }

   }

   public static weblogic.xml.crypto.dsig.api.spec.XPath.Filter getFilter(String var0) {
      if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.INTERSECT.toString().equals(var0)) {
         return weblogic.xml.crypto.dsig.api.spec.XPath.Filter.INTERSECT;
      } else if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.SUBTRACT.toString().equals(var0)) {
         return weblogic.xml.crypto.dsig.api.spec.XPath.Filter.SUBTRACT;
      } else if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.UNION.toString().equals(var0)) {
         return weblogic.xml.crypto.dsig.api.spec.XPath.Filter.UNION;
      } else {
         throw new IllegalArgumentException("illegal xpath filter: " + var0);
      }
   }

   public Transform newTransform(TransformParameterSpec var1) throws InvalidAlgorithmParameterException {
      return new XPathFilter2Transform(var1);
   }

   public String getURI() {
      return "http://www.w3.org/2002/06/xmldsig-filter2";
   }

   class NodeSetFilterBlock {
      public List union = new ArrayList();
      public List intersect = new ArrayList();
      public List subtract = new ArrayList();

      public NodeSetFilterBlock(NodeSetFilter var2) {
         this.union.add(var2);
      }

      public NodeSetFilterBlock(List var2) {
         this.union.addAll(var2);
      }

      public boolean filterTest(Node var1) throws XMLSignatureException {
         if (this.union.isEmpty()) {
            throw new XMLSignatureException("Failed to do filter test: at least one union node-set filter must exist.");
         } else {
            boolean var2 = false;
            Iterator var3 = this.union.iterator();

            NodeSetFilter var4;
            while(var3.hasNext()) {
               var4 = (NodeSetFilter)var3.next();
               if (var4.contains(var1)) {
                  var2 = true;
                  break;
               }
            }

            if (!var2) {
               return false;
            } else {
               var2 = true;
               var3 = this.intersect.iterator();

               while(var3.hasNext()) {
                  var4 = (NodeSetFilter)var3.next();
                  if (!var4.contains(var1)) {
                     var2 = false;
                     break;
                  }
               }

               if (!var2) {
                  return false;
               } else {
                  var2 = true;
                  var3 = this.subtract.iterator();

                  while(var3.hasNext()) {
                     var4 = (NodeSetFilter)var3.next();
                     if (var4.contains(var1)) {
                        var2 = false;
                        break;
                     }
                  }

                  return var2;
               }
            }
         }
      }
   }

   static class NodeSetFilter {
      private static final int UNKOWN = 0;
      public static final int UNION = 1;
      public static final int INTERSECT = 2;
      public static final int SUBTRACT = 3;
      public int operation = 0;
      public Set nodeSet = null;

      public NodeSetFilter(int var1, Set var2) {
         this.operation = var1;
         this.nodeSet = var2;
      }

      public static NodeSetFilter create(NodeList var0, String var1) {
         byte var2 = 0;
         if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.UNION.toString().equals(var1)) {
            var2 = 1;
         } else if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.INTERSECT.toString().equals(var1)) {
            var2 = 2;
         } else if (weblogic.xml.crypto.dsig.api.spec.XPath.Filter.SUBTRACT.toString().equals(var1)) {
            var2 = 3;
         }

         LinkedHashSet var3 = new LinkedHashSet();
         if (var0 instanceof Node) {
            var3.add(var0);
         } else {
            for(int var4 = 0; var4 < var0.getLength(); ++var4) {
               Node var5 = var0.item(var4);
               var3.add(var5);
            }
         }

         NodeSetFilter var6 = new NodeSetFilter(var2, var3);
         return var6;
      }

      public boolean contains(Node var1) {
         ArrayList var2 = new ArrayList();

         while(var1 != null) {
            if (this.nodeSet.contains(var1)) {
               this.nodeSet.addAll(var2);
               return true;
            }

            var2.add(var1);
            if (((Node)var1).getNodeType() == 2) {
               var1 = ((Attr)var1).getOwnerElement();
            } else {
               var1 = ((Node)var1).getParentNode();
            }
         }

         return false;
      }
   }

   static class NodeListImpl implements NodeList {
      private List nodes = null;

      public NodeListImpl(List var1) {
         this.setNodes(var1);
      }

      public NodeListImpl(Node[] var1) {
         this.setNodes(var1);
      }

      public NodeListImpl(Node var1) {
         this.setNode(var1);
      }

      public void setNodes(List var1) {
         this.nodes = new ArrayList(var1);
      }

      public void setNodes(Node[] var1) {
         this.setNodes(Arrays.asList(var1));
      }

      public void setNode(Node var1) {
         this.setNodes(new Node[]{var1});
      }

      public void addNode(Node var1) {
         this.nodes.add(var1);
      }

      public int getLength() {
         return this.nodes.size();
      }

      public Node item(int var1) {
         return (Node)this.nodes.get(var1);
      }
   }

   static class HereFunction implements XPathFunction {
      private NodeListImpl hereList = null;

      public HereFunction(Node var1) throws NullPointerException {
         this.setHere(var1);
      }

      public void setHere(Node var1) throws NullPointerException {
         if (var1 == null) {
            throw new NullPointerException("here node must be not null.");
         } else {
            this.hereList = new NodeListImpl(var1);
         }
      }

      public Object evaluate(List var1) throws XPathFunctionException {
         if (this.hereList.getLength() == 0) {
            throw new XPathFunctionException("can not evaluate here() function, there is no here node exists.");
         } else if (!(this.hereList.item(0) instanceof Node)) {
            throw new XPathFunctionException("can not evaluate here() function, the here node is invalid: " + this.hereList.item(0));
         } else {
            return this.hereList;
         }
      }
   }

   static class XPathFunctionResolverImpl implements XPathFunctionResolver {
      public static final String XPATH_FILTER2_BUILT_IN_FUNCTION_NS_PREFIX = "__xpath_filter2_built_in_function";
      public static final String XPATH_FILTER2_BUILT_IN_FUNCTION_NS_URI = "http://www.bea.com/weblogic/xml/crypto/dsig/xpath_filter2_built_in_function";
      private static String here_regex = null;
      private static String here_replacement = null;
      private static Pattern here_pattern = null;
      private Node here;

      public XPathFunctionResolverImpl() {
         this((Node)null);
      }

      public XPathFunctionResolverImpl(Node var1) {
         this.here = null;
         this.setHere(var1);
      }

      public void setHere(Node var1) {
         this.here = var1;
      }

      public static String translateHereFunc(String var0) {
         Matcher var1 = here_pattern.matcher(var0);
         return var1.replaceAll(here_replacement);
      }

      public XPathFunction resolveFunction(QName var1, int var2) {
         QName var3 = new QName("http://www.bea.com/weblogic/xml/crypto/dsig/xpath_filter2_built_in_function", "here", "__xpath_filter2_built_in_function");
         return var3.equals(var1) && var2 == 0 ? new HereFunction(this.here) : null;
      }

      static {
         here_regex = "(?<![-:])\\b(here\\s*\\(\\s*\\))";
         here_replacement = "__xpath_filter2_built_in_function:$1";
         here_pattern = Pattern.compile(here_regex);
      }
   }

   static class NamespaceContextImpl implements NamespaceContext {
      private Map nsMap;

      public NamespaceContextImpl() {
         this((Map)null);
      }

      public NamespaceContextImpl(Map var1) {
         this.nsMap = null;
         this.setNamespaceDeclarations(var1);
      }

      public void setNamespaceDeclarations(Map var1) {
         if (this.nsMap == null) {
            this.nsMap = new HashMap();
         } else {
            this.nsMap.clear();
         }

         if (var1 != null) {
            this.nsMap.putAll(var1);
         }

         this.nsMap.put("__xpath_filter2_built_in_function", "http://www.bea.com/weblogic/xml/crypto/dsig/xpath_filter2_built_in_function");
      }

      public void addNamespaceDeclaration(String var1, String var2) {
         if (this.nsMap == null) {
            this.nsMap = new HashMap();
            this.nsMap.put("__xpath_filter2_built_in_function", "http://www.bea.com/weblogic/xml/crypto/dsig/xpath_filter2_built_in_function");
         }

         this.nsMap.put(var1, var2);
      }

      public String getNamespaceURI(String var1) {
         if (var1 == null) {
            throw new IllegalArgumentException("not expected prefix: null");
         } else if (this.nsMap != null && this.nsMap.containsKey(var1)) {
            return (String)this.nsMap.get(var1);
         } else if ("xml".equals(var1)) {
            return "http://www.w3.org/XML/1998/namespace";
         } else {
            return "xmlns".equals(var1) ? "http://www.w3.org/2000/xmlns/" : "";
         }
      }

      public String getPrefix(String var1) {
         return null;
      }

      public Iterator getPrefixes(String var1) {
         return null;
      }
   }
}
