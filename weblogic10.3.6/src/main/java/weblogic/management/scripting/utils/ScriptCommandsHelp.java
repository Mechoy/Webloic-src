package weblogic.management.scripting.utils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.diagnostics.i18n.DiagnosticsTextTextFormatter;
import weblogic.management.scripting.ScriptException;
import weblogic.management.scripting.WLScriptContext;

public class ScriptCommandsHelp {
   private static boolean DUMP_MISSING = Boolean.getBoolean("DumpHelpCatalog");
   private static Class offlineHelpHandler = null;
   private static boolean loadedHelp = false;
   private static String wlstHelpFileBase = "WLSTHelp";
   private static String NAME = "name";
   private static String TYPE = "type";
   private static String TYPE_ID = "typeId";
   private static String COMMAND_ID = "commandId";
   private static String OFFLINE = "offline";
   private static String ONLINE = "online";
   private static String HELP_TOPIC = "helpTopic";
   private static String HELP_STRING = "helpString";
   private static String KEY = "key";
   private static String MESSAGE = "message";
   private static String DESCRIPTION = "description";
   private static String SHORT_DESCRIPTION = "shortDescription";
   private static String EXAMPLE = "example";
   private static String SYNTAX = "syntax";
   private static String COMMON = "common";
   private static String MAIN_DESCRIPTION = "MainDescription";
   private static TreeMap helpStrings = new TreeMap();
   private static TreeMap helpTable = new TreeMap();
   private static TreeMap helpTypes = null;
   private static int curCustomIntValue = 100;
   private static final DiagnosticsTextTextFormatter DTF = DiagnosticsTextTextFormatter.getInstance();
   private WLSTHelpTextFormatter helpFmt = new WLSTHelpTextFormatter();
   private WLSTMsgTextFormatter txtFmt;
   private WLScriptContext ctx = null;

   private void addCmd(String cmd, CommandDescription descr) {
      helpTable.put(cmd, descr);
   }

   public ScriptCommandsHelp(WLScriptContext ctx) {
      this.ctx = ctx;
      this.txtFmt = ctx.getWLSTMsgFormatter();
      this.loadHelp();
      this.addHelpType("control");
      this.addHelpType("lifecycle");
      this.addHelpType("browse");
      this.addHelpType("editing");
      this.addHelpType("information");
      this.addHelpType("deployment");
      this.addHelpType("variables");
      this.addHelpType("trees");
      this.addHelpType("nodemanager");
      this.addHelpType("offline");
      this.addHelpType("online");
      this.addHelpType("common");
      this.addHelpType("diagnostics");
      this.addHelpType("storeadmin");
   }

   public String getHelpString(String key) {
      String result = (String)helpStrings.get(key);
      if (result == null) {
         throw new AssertionError("No message text for " + key);
      } else {
         return result;
      }
   }

   public void print(String s) {
      this.ctx.print(s);
   }

   public void println(String s) {
      this.ctx.println(s);
   }

   private void displayAllHelp() {
      Iterator iter = helpTable.values().iterator();

      while(iter.hasNext()) {
         CommandDescription cd = (CommandDescription)iter.next();
         if (cd.getGenericType() != 0) {
            this.print("    help('" + cd.getCommand() + "')" + calculateTabSpace("help('" + cd.getCommand() + "')") + cd.getShortDescription());
            this.print("\n");
         }
      }

   }

   private void handleCommonCommands() {
      Iterator iter = helpTable.values().iterator();

      while(iter.hasNext()) {
         CommandDescription cd = (CommandDescription)iter.next();
         if (cd.isCommon()) {
            this.print("    help('" + cd.getCommand() + "')" + calculateTabSpace("help('" + cd.getCommand() + "')") + cd.getShortDescription());
            this.print("\n");
         }
      }

   }

   public void printHelp(String cmd) {
      Iterator iter;
      String cmdName;
      if (cmd.endsWith("*")) {
         String myCmd = cmd.substring(0, cmd.length() - 1);
         iter = helpTable.keySet().iterator();

         while(iter.hasNext()) {
            cmdName = (String)iter.next();
            if (cmdName.startsWith(myCmd)) {
               this.print("\n" + this.getHelpString("HelpFor") + " " + cmdName + ":\n");
               this.printHelp(cmdName);
               this.print("--------------------------------------------------------------------");
            }
         }

      } else if (cmd.equals("all")) {
         this.displayAllHelp();
      } else if (!cmd.equals("online") && !cmd.equals("offline")) {
         if (helpTypes.containsKey(cmd)) {
            int key = (Integer)helpTypes.get(cmd);
            Collection coll = helpTable.values();
            Iterator iter = coll.iterator();
            CommandDescription description = (CommandDescription)helpTable.get(cmd);
            if (description != null) {
               String ds = description.getDescription();
               this.print("\n");
               this.print(ds);
               this.print("\n");
               if (description.getCommand() == "common") {
                  this.handleCommonCommands();
               } else {
                  String tabSpace = calculateTabSpace("help('STOREUSERCONFIGXXXXXXXX')");

                  while(iter.hasNext()) {
                     CommandDescription cd = (CommandDescription)iter.next();
                     if (cd.getGenericType() == key) {
                        this.print("    help('" + cd.getCommand() + "')" + "   " + calculateTabSpace("help('" + cd.getCommand() + "')") + cd.getShortDescription());
                        this.print("\n");
                     }
                  }

                  this.print("");
               }
            }
         } else {
            CommandDescription desc = (CommandDescription)helpTable.get(cmd);
            if (desc == null) {
               this.print(this.getHelpString("NoHelp1") + " " + cmd + " " + this.getHelpString("NoHelp2"));
            } else {
               String[] _desc = desc.getDescription().split("-NL");
               this.print("\n" + this.getHelpString("Description") + ": \n\n");

               for(int i = 0; i < _desc.length; ++i) {
                  this.print(_desc[i]);
               }

               int k;
               String[] _ex;
               if (desc.getSyntax() != null) {
                  this.print("\n" + this.getHelpString("Syntax") + ": \n\n");
                  _ex = desc.getSyntax().split("-NL");

                  for(k = 0; k < _ex.length; ++k) {
                     this.print(_ex[k]);
                  }
               }

               if (desc.getExample() != null) {
                  this.print("\n" + this.getHelpString("Example") + ": \n\n");
                  _ex = desc.getExample().split("-NL");

                  for(k = 0; k < _ex.length; ++k) {
                     this.println(_ex[k]);
                  }
               }

            }
         }
      } else {
         CommandDescription cd;
         if (cmd.equals("online")) {
            iter = helpTable.keySet().iterator();

            while(iter.hasNext()) {
               cmdName = (String)iter.next();
               cd = (CommandDescription)helpTable.get(cmdName);
               if (cd.isOnline()) {
                  this.print("    help('" + cd.getCommand() + "')" + "   " + calculateTabSpace("help('" + cd.getCommand() + "')") + cd.getShortDescription());
                  this.print("\n");
               }
            }
         } else if (cmd.equals("offline")) {
            iter = helpTable.keySet().iterator();

            while(iter.hasNext()) {
               cmdName = (String)iter.next();
               cd = (CommandDescription)helpTable.get(cmdName);
               if (cd.isOffline()) {
                  this.print("    help('" + cd.getCommand() + "')" + "   " + calculateTabSpace("help('" + cd.getCommand() + "')") + cd.getShortDescription());
                  this.print("\n");
               }
            }
         }

      }
   }

   public void getCommandAutoCompletions(String cmd, List list) {
      Iterator enume = helpTable.keySet().iterator();

      while(enume.hasNext()) {
         String cmdName = (String)enume.next();
         if (cmdName.startsWith(cmd)) {
            list.add(cmdName);
         }
      }

   }

   private void loadHelp() {
      helpTypes = new TreeMap();
      helpTypes.put("browse", new Integer(11));
      helpTypes.put("control", new Integer(17));
      helpTypes.put("deployment", new Integer(14));
      helpTypes.put("editing", new Integer(13));
      helpTypes.put("information", new Integer(15));
      helpTypes.put("lifecycle", new Integer(12));
      helpTypes.put("diagnostics", new Integer(18));
      helpTypes.put("variables", new Integer(19));
      helpTypes.put("trees", new Integer(20));
      helpTypes.put("nodemanager", new Integer(21));
      helpTypes.put("common", new Integer(22));
      helpTypes.put("offline", new Integer(23));
      helpTypes.put("online", new Integer(24));
      helpTypes.put("storeadmin", new Integer(25));
      Locale loc = Locale.getDefault();
      String lang = loc.getLanguage();
      String country = loc.getCountry();
      String helpFile = wlstHelpFileBase + "_" + lang + "_" + country + ".xml";
      InputStream is = ScriptCommandsHelp.class.getResourceAsStream(helpFile);
      if (is == null) {
         helpFile = wlstHelpFileBase + "_" + lang + ".xml";
         is = ScriptCommandsHelp.class.getResourceAsStream(helpFile);
      }

      if (is == null) {
         helpFile = wlstHelpFileBase + ".xml";
         is = ScriptCommandsHelp.class.getResourceAsStream(helpFile);
      }

      if (is == null) {
         throw new AssertionError("Could not find " + helpFile);
      } else {
         try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            if (nodes == null) {
               return;
            }

            Node topNode = null;

            for(int i = 0; i < nodes.getLength(); ++i) {
               topNode = nodes.item(i);
               if (topNode.getNodeType() == 1) {
                  if (topNode.getNodeName().equals(HELP_TOPIC)) {
                     this.handleHelpTopic(topNode);
                  } else if (topNode.getNodeName().equals(HELP_STRING)) {
                     this.handleHelpString(topNode);
                  }
               }
            }
         } catch (Exception var12) {
            var12.printStackTrace();
         }

      }
   }

   private void handleHelpString(Node helpNode) {
      NodeList nodes = helpNode.getChildNodes();
      if (nodes != null) {
         String key = null;
         String message = null;
         Node currNode = null;

         for(int i = 0; i < nodes.getLength(); ++i) {
            currNode = nodes.item(i);
            if (currNode.getNodeName().equals(KEY)) {
               key = getText(currNode);
               String tmp = this.getStringFromCatalog(key + "_HelpString");
               if (tmp != null) {
                  message = tmp;
               }
            }

            if (message == null && currNode.getNodeName().equals(MESSAGE)) {
               message = getText(currNode);
               if (DUMP_MISSING) {
                  this.dumpMissingCatalogEntry(key + "_HelpString", message);
               }
            }
         }

         if (key != null && message != null) {
            helpStrings.put(key, message);
         }

      }
   }

   private void handleHelpTopic(Node helpNode) {
      NamedNodeMap attrs = helpNode.getAttributes();
      if (attrs != null) {
         String commandName = null;
         String genericType = null;
         String genericTypeId = null;
         String commandId = null;
         String offline = "false";
         String online = "true";
         String common = "false";
         String description = null;
         String shortDescription = null;
         String example = null;
         String syntax = null;

         for(int i = 0; i < attrs.getLength(); ++i) {
            if (attrs.item(i).getNodeName().equals(NAME)) {
               commandName = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(TYPE)) {
               genericType = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(TYPE_ID)) {
               genericTypeId = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(COMMAND_ID)) {
               commandId = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(OFFLINE)) {
               offline = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(ONLINE)) {
               online = attrs.item(i).getNodeValue();
            } else if (attrs.item(i).getNodeName().equals(COMMON)) {
               common = attrs.item(i).getNodeValue();
            }
         }

         String descrKey = commandName + "_description";
         String shortDescrKey = commandName + "_shortDescription";
         String syntaxKey = commandName + "_syntax";
         description = this.getStringFromCatalog(descrKey);
         shortDescription = this.getStringFromCatalog(shortDescrKey);
         syntax = this.getStringFromCatalog(syntaxKey);
         NodeList helpContentNodes = helpNode.getChildNodes();
         if (helpContentNodes != null) {
            Node helpContentNode = null;

            for(int j = 0; j < helpContentNodes.getLength(); ++j) {
               helpContentNode = helpContentNodes.item(j);
               if (helpContentNode.getNodeType() == 1) {
                  String nodeName = helpContentNode.getNodeName();
                  if (DESCRIPTION.equals(nodeName)) {
                     if (description == null) {
                        description = getText(helpContentNode);
                        if (DUMP_MISSING) {
                           this.dumpMissingCatalogEntry(descrKey, description);
                        }
                     }
                  } else if (SYNTAX.equals(nodeName)) {
                     if (syntax == null) {
                        syntax = getText(helpContentNode);
                        if (DUMP_MISSING) {
                           this.dumpMissingCatalogEntry(syntaxKey, syntax);
                        }
                     }
                  } else if (EXAMPLE.equals(nodeName)) {
                     example = getText(helpContentNode);
                  } else if (SHORT_DESCRIPTION.equals(nodeName) && shortDescription == null) {
                     shortDescription = getText(helpContentNode);
                     if (DUMP_MISSING) {
                        this.dumpMissingCatalogEntry(shortDescrKey, shortDescription);
                     }
                  }
               }
            }

            CommandDescription eDesc = new CommandDescription();
            eDesc.setCommand(commandName);
            eDesc.setSyntax(syntax);
            eDesc.setDescription(description);
            eDesc.setExample(example);
            eDesc.setCommandId(Integer.parseInt(commandId));
            if (offline.toLowerCase(Locale.US).equals("true")) {
               eDesc.setOffline(true);
            } else {
               eDesc.setOffline(false);
            }

            if (online.toLowerCase(Locale.US).equals("true")) {
               eDesc.setOnline(true);
            } else {
               eDesc.setOnline(false);
            }

            if (common.toLowerCase(Locale.US).equals("true")) {
               eDesc.setCommon(true);
            } else {
               eDesc.setCommon(false);
            }

            eDesc.setShortDescription(shortDescription);
            eDesc.setGenericType(Integer.parseInt(genericTypeId));
            this.addCmd(commandName, eDesc);
         }
      }
   }

   private String getStringFromCatalog(String key) {
      String result = null;
      Class clazz = this.helpFmt.getClass();
      String methodName = "get_" + key;

      try {
         Method m = clazz.getDeclaredMethod(methodName);
         result = (String)m.invoke(this.helpFmt);
         return result;
      } catch (InvocationTargetException var6) {
         return null;
      } catch (IllegalAccessException var7) {
         return null;
      } catch (NoSuchMethodException var8) {
         return null;
      }
   }

   private static String getText(Node helpContentNode) {
      NodeList itemNodes = helpContentNode.getChildNodes();
      if (itemNodes == null) {
         return "";
      } else {
         for(int k = 0; k < itemNodes.getLength(); ++k) {
            Node aNode = itemNodes.item(k);
            if (aNode.getNodeType() == 3) {
               String text = aNode.getNodeValue();
               return text;
            }
         }

         return "";
      }
   }

   private static String calculateTabSpace(String s) {
      String tabSpace = "";

      for(int k = 0; k < 25 - s.length(); ++k) {
         tabSpace = tabSpace + " ";
      }

      return tabSpace;
   }

   public void printDefaultHelp() {
      this.println(this.helpFmt.getWLSTMainDescription());
      this.println("    help('all')" + calculateTabSpace("help('all')") + this.helpFmt.get_all_shortDescription());
      Iterator keys = helpTypes.keySet().iterator();

      while(keys.hasNext()) {
         String cmdName = (String)keys.next();
         CommandDescription cmdDesc = (CommandDescription)helpTable.get(cmdName);
         if (cmdDesc == null) {
            throw new AssertionError("No message text for " + cmdName);
         }

         this.println("    help('" + cmdName + "')" + calculateTabSpace("help('" + cmdName + "')") + cmdDesc.getShortDescription());
      }

   }

   public void addHelpCommandGroup(String groupName, String resourceBundleName) throws ScriptException {
      if (groupName != null && resourceBundleName != null) {
         if (helpTypes.containsKey(groupName)) {
            throw new ScriptException(this.txtFmt.getDuplicateGroupName(groupName), "addHelpCommandGroup");
         } else {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceBundleName);
            helpTypes.put(groupName, new Integer(curCustomIntValue++));
            CommandDescription commandDesc = new CommandDescription(groupName, this.getCustomHelp(resourceBundle, groupName, DESCRIPTION), this.getCustomHelp(resourceBundle, groupName, SHORT_DESCRIPTION));
            commandDesc.setResourceBundle(resourceBundle);
            this.addCmd(groupName, commandDesc);
         }
      } else {
         throw new ScriptException(this.txtFmt.getGroupOrResourceNameNull(), "addHelpCommandGroup");
      }
   }

   public void addHelpCommand(String commandName, String groupName, boolean offline, boolean online) throws ScriptException {
      if (!helpTypes.containsKey(groupName)) {
         throw new ScriptException(this.txtFmt.getGroupNameNotExist(groupName), "addHelpCommand");
      } else if (helpTable.containsKey(commandName)) {
         throw new ScriptException(this.txtFmt.getDuplicateCommandName(commandName), "addHelpCommand");
      } else {
         int key = (Integer)helpTypes.get(groupName);
         CommandDescription gDesc = (CommandDescription)helpTable.get(groupName);
         ResourceBundle resourceBundle = gDesc.getResourceBundle();
         CommandDescription eDesc = new CommandDescription();
         eDesc.setCommand(commandName);
         eDesc.setSyntax(this.getCustomHelp(resourceBundle, commandName, SYNTAX));
         eDesc.setDescription(this.getCustomHelp(resourceBundle, commandName, DESCRIPTION));
         eDesc.setExample(this.getCustomHelp(resourceBundle, commandName, EXAMPLE));
         eDesc.setOffline(offline);
         eDesc.setOnline(online);
         eDesc.setShortDescription(this.getCustomHelp(resourceBundle, commandName, SHORT_DESCRIPTION));
         eDesc.setGenericType(key);
         this.addCmd(commandName, eDesc);
      }
   }

   private void addHelpType(String typeName) {
      String shortDesc = this.getStringFromCatalog(typeName + "_" + SHORT_DESCRIPTION);
      CommandDescription cmdDesc = new CommandDescription(typeName, this.getHelpString(typeName), shortDesc);
      this.addCmd(typeName, cmdDesc);
   }

   private String getCustomHelp(ResourceBundle resBundle, String prefix, String key) {
      if (resBundle == null) {
         return this.txtFmt.getNoResourceFoundForCmd(prefix, key);
      } else {
         String lookup = prefix + "_" + key;

         try {
            return resBundle.getString(lookup);
         } catch (Exception var6) {
            if (this.ctx.isDebug()) {
               this.ctx.println("Error getting the resource string for " + lookup);
               var6.printStackTrace();
            }

            return this.txtFmt.getNoResourceFoundForCmd(prefix, lookup);
         }
      }
   }

   public static void main(String[] args) {
      WLSTInterpreter interp = new WLSTInterpreter();
      File f;
      File[] scripts;
      int j;
      File file;
      if (args != null && args.length != 0) {
         if (args.length == 1) {
            f = new File(args[0]);
            if (f.isDirectory()) {
               scripts = f.listFiles();

               for(j = 0; j < scripts.length; ++j) {
                  file = scripts[j];

                  try {
                     if (file.getName().endsWith(".py")) {
                        interp.execfile(file.getAbsolutePath());
                     }
                  } catch (Throwable var9) {
                     System.out.println("Error running the script " + file.getAbsolutePath());
                     var9.printStackTrace();
                  }
               }
            } else {
               try {
                  interp.execfile(args[0]);
               } catch (Throwable var8) {
                  var8.printStackTrace();
               }
            }
         } else if (args.length > 1) {
            for(int i = 0; i < args.length; ++i) {
               try {
                  interp.execfile(args[i]);
               } catch (Throwable var7) {
                  System.out.println("Error running the script " + args[i]);
                  var7.printStackTrace();
               }
            }
         } else {
            interp.execfile(args[0]);
         }
      } else {
         f = new File("./oam/unit/wlst/wls_checkintest");
         scripts = f.listFiles();

         for(j = 0; j < scripts.length; ++j) {
            file = scripts[j];

            try {
               if (file.getName().endsWith(".py")) {
                  interp.execfile(file.getAbsolutePath());
               }
            } catch (Throwable var10) {
               System.out.println("Error running the script " + file.getAbsolutePath());
               var10.printStackTrace();
            }
         }
      }

   }

   private void dumpMissingCatalogEntry(String key, String message) {
      try {
         System.err.println("   <!--  -->");
         System.err.println("   <message");
         System.err.println("      messageid=\"" + key + "\"");
         System.err.println("      datehash=\"-408200560\"");
         System.err.println("      datelastchanged=\"1079826352747\"");
         System.err.println("      method=\"get_" + key + "()\"");
         System.err.println("      >");
         System.err.println("      <messagebody>");
         message = message.replace("\n", "\n\\n");
         message = message.replace("'", "''");
         System.err.println("      " + message);
         System.err.println("      </messagebody>");
         System.err.println("</message>");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   static {
      helpTypes = new TreeMap();
      helpTypes.put("browse", new Integer(11));
      helpTypes.put("control", new Integer(17));
      helpTypes.put("deployment", new Integer(14));
      helpTypes.put("editing", new Integer(13));
      helpTypes.put("information", new Integer(15));
      helpTypes.put("lifecycle", new Integer(12));
      helpTypes.put("diagnostics", new Integer(18));
      helpTypes.put("variables", new Integer(19));
      helpTypes.put("trees", new Integer(20));
      helpTypes.put("nodemanager", new Integer(21));
      helpTypes.put("common", new Integer(22));
   }
}
