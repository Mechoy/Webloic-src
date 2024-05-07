package weblogic.wsee.handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.management.descriptors.webservice.HandlerChainMBean;
import weblogic.management.descriptors.webservice.HandlerChainMBeanImpl;
import weblogic.management.descriptors.webservice.HandlerChainsMBean;
import weblogic.management.descriptors.webservice.HandlerChainsMBeanImpl;
import weblogic.management.descriptors.webservice.HandlerMBean;
import weblogic.management.descriptors.webservice.HandlerMBeanImpl;
import weblogic.management.descriptors.webservice.InboundHandlerChainMBean;
import weblogic.management.descriptors.webservice.InboundHandlerChainMBeanImpl;
import weblogic.management.descriptors.webservice.InitParamMBean;
import weblogic.management.descriptors.webservice.InitParamMBeanImpl;
import weblogic.management.descriptors.webservice.InitParamsMBean;
import weblogic.management.descriptors.webservice.InitParamsMBeanImpl;
import weblogic.management.descriptors.webservice.OutboundHandlerChainMBean;
import weblogic.management.descriptors.webservice.OutboundHandlerChainMBeanImpl;
import weblogic.management.descriptors.webservice.ServerHandlerChainMBean;
import weblogic.management.descriptors.webservice.ServerHandlerChainMBeanImpl;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.xml.schema.binding.util.ClassUtil;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLOutputStreamFactory;
import weblogic.xml.stream.XMLStreamException;

public class GlobalHandlerChainHelper {
   private static final boolean verbose = Verbose.isVerbose(GlobalHandlerChainHelper.class);
   private static final String SHC_DD_FILE = "server-handler-chain.xml";
   private static final String SYSTEM_SHC_DD_FILE = getSHCDDFileProp();
   private static final String SERVER_HANDLER_CHAIN = "server-handler-chain";
   private static final String INBOUND_HANDLER_CHAIN = "inbound-handler-chain";
   private static final String OUTBOUND_HANDLER_CHAIN = "outbound-handler-chain";
   private static final String HANDLER_CHAINS = "handler-chains";
   private static final String HANDLER_CHAIN = "handler-chain";
   private static final String NAME = "name";
   private static final String HANDLER = "handler";
   private static final String INIT_PARAMS = "init-params";
   private static final String INIT_PARAM = "init-param";
   private static final String PARAM = "param";
   private static final String VALUE = "value";
   private static final String CLASS_NAME = "class-name";
   private ServerHandlerChainMBean serverHandlerChain = null;
   private List inboundHandlerInfoList = new ArrayList();
   private boolean loadedInboundHandlerInfos = false;
   private List outboundHandlerInfoList = new ArrayList();
   private boolean loadedOutboundHandlerInfos = false;
   private boolean noGlobalHandlerChainConfigured = false;
   private static GlobalHandlerChainHelper theInstance = null;

   private static String getSHCDDFileProp() {
      return System.getProperty("weblogic.wsee.ServerHandlerChainConfig");
   }

   private GlobalHandlerChainHelper() {
      try {
         this.loadConfig();
      } catch (DDProcessingException var2) {
         Verbose.log((Object)var2);
         this.noGlobalHandlerChainConfigured = false;
      }

   }

   private void loadConfig() throws DDProcessingException {
      DDLoader var1 = new DDLoader();
      this.serverHandlerChain = var1.load();
      if (this.serverHandlerChain == null) {
         this.noGlobalHandlerChainConfigured = true;
      }

   }

   private ServerHandlerChainMBean getServerHandlerChain() {
      return this.noGlobalHandlerChainConfigured ? null : this.serverHandlerChain;
   }

   private void dump() throws XMLStreamException {
      ServerHandlerChainMBeanImpl var1 = (ServerHandlerChainMBeanImpl)this.getServerHandlerChain();
      if (var1 != null) {
         XMLOutputStream var2 = XMLOutputStreamFactory.newInstance().newDebugOutputStream(System.out);
         var2.add(var1.toXML(0));
         var2.flush();
      }
   }

   public static void main(String[] var0) throws Exception {
      getInstance().dump();
   }

   public static GlobalHandlerChainHelper getInstance() {
      if (theInstance == null) {
         Class var0 = GlobalHandlerChainHelper.class;
         synchronized(GlobalHandlerChainHelper.class) {
            if (theInstance == null) {
               theInstance = new GlobalHandlerChainHelper();
            }
         }
      }

      return theInstance;
   }

   public List getInboundHandlerInfos() {
      if (this.noGlobalHandlerChainConfigured) {
         return new ArrayList();
      } else {
         if (!this.loadedInboundHandlerInfos) {
            synchronized(this) {
               if (!this.loadedInboundHandlerInfos) {
                  ServerHandlerChainMBean var2 = this.getServerHandlerChain();
                  if (var2 == null) {
                     return new ArrayList();
                  }

                  InboundHandlerChainMBean var3 = var2.getInboundHandlerChain();
                  if (var3 == null) {
                     return new ArrayList();
                  }

                  HandlerChainsMBean var4 = var3.getHandlerChains();
                  if (var4 == null) {
                     return new ArrayList();
                  }

                  HandlerChainMBean[] var5 = var4.getHandlerChains();
                  if (var5 == null || var5.length == 0) {
                     return new ArrayList();
                  }

                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     HandlerMBean[] var7 = var5[var6].getHandlers();

                     for(int var8 = 0; var8 < var7.length; ++var8) {
                        Class var9 = null;

                        try {
                           var9 = this.loadClass(var7[var8].getClassName());
                        } catch (WsException var15) {
                           Verbose.log((Object)var15);
                           continue;
                        }

                        HashMap var10 = new HashMap();
                        InitParamsMBean var11 = var7[var8].getInitParams();
                        if (var11 != null) {
                           InitParamMBean[] var12 = var11.getInitParams();

                           for(int var13 = 0; var13 < var12.length; ++var13) {
                              var10.put(var12[var13].getParamName(), var12[var13].getParamValue());
                           }
                        }

                        this.inboundHandlerInfoList.add(new HandlerInfo(var9, var10, (QName[])null));
                     }
                  }

                  this.loadedInboundHandlerInfos = true;
               }
            }
         }

         return this.inboundHandlerInfoList;
      }
   }

   public List getOutboundHandlerInfos() {
      if (this.noGlobalHandlerChainConfigured) {
         return new ArrayList();
      } else {
         if (!this.loadedOutboundHandlerInfos) {
            synchronized(this) {
               if (!this.loadedOutboundHandlerInfos) {
                  ServerHandlerChainMBean var2 = this.getServerHandlerChain();
                  if (var2 == null) {
                     return new ArrayList();
                  }

                  OutboundHandlerChainMBean var3 = var2.getOutboundHandlerChain();
                  if (var3 == null) {
                     return new ArrayList();
                  }

                  HandlerChainsMBean var4 = var3.getHandlerChains();
                  if (var4 == null) {
                     return new ArrayList();
                  }

                  HandlerChainMBean[] var5 = var4.getHandlerChains();
                  if (var5 == null || var5.length == 0) {
                     return new ArrayList();
                  }

                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     HandlerMBean[] var7 = var5[var6].getHandlers();

                     for(int var8 = 0; var8 < var7.length; ++var8) {
                        Class var9 = null;

                        try {
                           var9 = this.loadClass(var7[var8].getClassName());
                        } catch (WsException var15) {
                           Verbose.log((Object)var15);
                           continue;
                        }

                        HashMap var10 = new HashMap();
                        InitParamsMBean var11 = var7[var8].getInitParams();
                        if (var11 != null) {
                           InitParamMBean[] var12 = var11.getInitParams();

                           for(int var13 = 0; var13 < var12.length; ++var13) {
                              var10.put(var12[var13].getParamName(), var12[var13].getParamValue());
                           }
                        }

                        this.outboundHandlerInfoList.add(new HandlerInfo(var9, var10, (QName[])null));
                     }
                  }

                  this.loadedOutboundHandlerInfos = true;
               }
            }
         }

         return this.outboundHandlerInfoList;
      }
   }

   private Class loadClass(String var1) throws WsException {
      try {
         return ClassUtil.loadClass(var1);
      } catch (ClassUtil.ClassUtilException var3) {
         throw new WsException("Failed to load class", var3);
      }
   }

   private static class DDLoader {
      private Map handlerChains;
      private ParsingHelper ph;

      private DDLoader() {
         this.handlerChains = new HashMap();
      }

      private ServerHandlerChainMBean load() throws DDProcessingException {
         Object var1 = null;

         ServerHandlerChainMBean var2;
         try {
            if (GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE != null && GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE.length() != 0) {
               try {
                  var1 = new FileInputStream(GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE);
               } catch (IOException var13) {
               }

               if (GlobalHandlerChainHelper.verbose && var1 != null) {
                  Verbose.log((Object)(" +++ Found " + GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE));
               }

               if (var1 == null) {
                  var1 = this.getClass().getClassLoader().getResourceAsStream(GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE);
               }

               if (GlobalHandlerChainHelper.verbose && var1 != null) {
                  Verbose.log((Object)(" +++ Found " + GlobalHandlerChainHelper.SYSTEM_SHC_DD_FILE + " ----- In SystemClassPath"));
               }
            }

            if (var1 == null) {
               var1 = this.getClass().getClassLoader().getResourceAsStream("server-handler-chain.xml");
               if (GlobalHandlerChainHelper.verbose && var1 != null) {
                  Verbose.log((Object)" +++ Found server-handler-chain.xml in server classpath");
               }
            }

            if (var1 == null) {
               var1 = this.getClass().getClassLoader().getResourceAsStream("weblogic/wsee/handler/server-handler-chain.xml");
               if (GlobalHandlerChainHelper.verbose && var1 != null) {
                  Verbose.log((Object)" +++ Found server-handler-chain.xml in server classpath under weblogic/wsee/handler");
               }
            }

            if (var1 != null) {
               var2 = this.load((InputStream)var1);
               ServerHandlerChainMBean var3 = var2;
               return var3;
            }

            if (GlobalHandlerChainHelper.verbose) {
               Verbose.log((Object)" +++ Doesn't define server-handler-chain.xml file.  So, not configuring server handler-chains.");
            }

            var2 = null;
         } finally {
            try {
               if (var1 != null) {
                  ((InputStream)var1).close();
               }
            } catch (Throwable var12) {
            }

         }

         return var2;
      }

      private ServerHandlerChainMBean load(InputStream var1) throws DDProcessingException {
         try {
            this.ph = new ParsingHelper(var1);
            ServerHandlerChainMBean var2 = this.processServerHandlerChainElement();
            this.ph.matchDocumentEnd();
            return var2;
         } catch (XMLStreamException var3) {
            throw new DDProcessingException("Problem parsing deployment descriptor", var3);
         }
      }

      private ServerHandlerChainMBean processServerHandlerChainElement() throws DDProcessingException, XMLStreamException {
         ServerHandlerChainMBeanImpl var1 = new ServerHandlerChainMBeanImpl();
         this.ph.matchStartElement("server-handler-chain");
         InboundHandlerChainMBean var2 = this.processInboundHandlerChainElement();
         if (var2 != null) {
            var1.setInboundHandlerChain(var2);
         }

         OutboundHandlerChainMBean var3 = this.processOutboundHandlerChainElement();
         if (var3 != null) {
            var1.setOutboundHandlerChain(var3);
         }

         this.ph.matchEndElement("server-handler-chain");
         return var1;
      }

      private InboundHandlerChainMBean processInboundHandlerChainElement() throws DDProcessingException, XMLStreamException {
         InboundHandlerChainMBeanImpl var1 = new InboundHandlerChainMBeanImpl();
         this.ph.matchStartElement("inbound-handler-chain");
         HandlerChainsMBean var2 = this.processHandlerChainsElement();
         if (var2 != null) {
            var1.setHandlerChains(var2);
         }

         this.ph.matchEndElement("inbound-handler-chain");
         return var1;
      }

      private OutboundHandlerChainMBean processOutboundHandlerChainElement() throws DDProcessingException, XMLStreamException {
         OutboundHandlerChainMBeanImpl var1 = new OutboundHandlerChainMBeanImpl();
         this.ph.matchStartElement("outbound-handler-chain");
         HandlerChainsMBean var2 = this.processHandlerChainsElement();
         if (var2 != null) {
            var1.setHandlerChains(var2);
         }

         this.ph.matchEndElement("outbound-handler-chain");
         return var1;
      }

      private HandlerChainsMBean processHandlerChainsElement() throws DDProcessingException, XMLStreamException {
         HandlerChainsMBeanImpl var1 = null;
         XMLEvent var2 = this.ph.matchStartElement("handler-chains");
         if (var2 != null) {
            var1 = new HandlerChainsMBeanImpl();
            HandlerChainMBean[] var3 = this.processHandlerChainElements();
            var1.setHandlerChains(var3);
            this.ph.matchEndElement("handler-chains");
         }

         return var1;
      }

      private HandlerChainMBean[] processHandlerChainElements() throws DDProcessingException, XMLStreamException {
         HandlerChainMBean var1 = this.processHandlerChainElement();
         if (var1 == null) {
            throw new DDProcessingException("There must be at least one <handler-chain> element in <handler-chains>", this.ph.getLocation());
         } else {
            ArrayList var2;
            for(var2 = new ArrayList(); var1 != null; var1 = this.processHandlerChainElement()) {
               var2.add(var1);
            }

            return (HandlerChainMBean[])((HandlerChainMBean[])var2.toArray(new HandlerChainMBeanImpl[0]));
         }
      }

      private HandlerChainMBean processHandlerChainElement() throws DDProcessingException, XMLStreamException {
         HandlerChainMBeanImpl var1 = null;
         XMLEvent var2 = this.ph.matchOptionalStartElement("handler-chain");
         if (var2 != null) {
            ParsingHelper.checkAttributes((StartElement)var2, new String[]{"name"});
            var1 = new HandlerChainMBeanImpl();
            String var3 = ParsingHelper.getRequiredAttribute((StartElement)var2, "name").getValue();
            var1.setHandlerChainName(var3);
            HandlerMBean[] var4 = this.processHandlerElements();
            var1.setHandlers(var4);
            if (this.handlerChains.put(var3, var1) != null) {
               throw new DDProcessingException("<handler-chain> must have a unique \"name\" attribute within the scope of a <server-handler-chain>; the name \"" + var3 + "\" was used in a previous <" + "handler-chain" + "> element", var2.getLocation());
            }

            this.ph.matchEndElement("handler-chain");
         }

         return var1;
      }

      private HandlerMBean[] processHandlerElements() throws DDProcessingException, XMLStreamException {
         ArrayList var1 = new ArrayList();
         HandlerMBean var2 = this.processHandlerElement();
         if (var2 == null) {
            throw new DDProcessingException("There must be at least one <handler> element in <handler-chain>", this.ph.getLocation());
         } else {
            while(var2 != null) {
               var1.add(var2);
               var2 = this.processHandlerElement();
            }

            return (HandlerMBean[])((HandlerMBean[])var1.toArray(new HandlerMBean[0]));
         }
      }

      private HandlerMBean processHandlerElement() throws DDProcessingException, XMLStreamException {
         HandlerMBeanImpl var1 = null;
         XMLEvent var2 = this.ph.matchOptionalStartElement("handler");
         if (var2 != null) {
            ParsingHelper.checkAttributes((StartElement)var2, new String[]{"class-name"});
            var1 = new HandlerMBeanImpl();
            String var3 = ParsingHelper.getRequiredAttribute((StartElement)var2, "class-name").getValue();
            var1.setClassName(var3);
            InitParamsMBean var4 = this.processInitParamsElement();
            if (var4 != null) {
               var1.setInitParams(var4);
            }

            this.ph.matchEndElement("handler");
         }

         return var1;
      }

      private InitParamsMBean processInitParamsElement() throws DDProcessingException, XMLStreamException {
         InitParamsMBeanImpl var1 = null;
         XMLEvent var2 = this.ph.matchOptionalStartElement("init-params");
         if (var2 != null) {
            var1 = new InitParamsMBeanImpl();
            InitParamMBean[] var3 = this.processInitParamElements();
            var1.setInitParams(var3);
            this.ph.matchEndElement("init-params");
         }

         return var1;
      }

      private InitParamMBean[] processInitParamElements() throws DDProcessingException, XMLStreamException {
         InitParamMBean var1 = this.processInitParamElement();
         if (var1 == null) {
            throw new DDProcessingException("There must be at least one <param> element in <init-params>", this.ph.getLocation());
         } else {
            ArrayList var2;
            for(var2 = new ArrayList(); var1 != null; var1 = this.processInitParamElement()) {
               var2.add(var1);
            }

            return (InitParamMBeanImpl[])((InitParamMBeanImpl[])var2.toArray(new InitParamMBeanImpl[0]));
         }
      }

      private InitParamMBean processInitParamElement() throws DDProcessingException, XMLStreamException {
         InitParamMBeanImpl var1 = null;
         XMLEvent var2 = this.ph.matchOptionalStartElement("init-param");
         if (var2 != null) {
            ParsingHelper.checkAttributes((StartElement)var2, new String[]{"name", "value"});
            var1 = new InitParamMBeanImpl();
            String var3 = ParsingHelper.getRequiredAttribute((StartElement)var2, "name").getValue();
            var1.setParamName(var3);
            String var4 = ParsingHelper.getRequiredAttribute((StartElement)var2, "value").getValue();
            var1.setParamValue(var4);
            this.ph.matchEndElement("init-param");
         }

         return var1;
      }

      // $FF: synthetic method
      DDLoader(Object var1) {
         this();
      }
   }
}
