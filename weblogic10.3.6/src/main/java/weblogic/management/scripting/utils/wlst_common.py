"""
This script defines WLST commands can be run in both online and offline modes.
These commands don't need be redefined when WLST user switches between online
and offline mode.

WARNING: This file is part of the WLST implementation and as such may
change between versions of WLST. You should not try to reuse the logic
in this script or keep copies of this script. Doing so could cause
your WLST scripts to fail when you upgrade to a different version of
WLST.
"""

import sys
import java
import jarray
import weblogic.management.scripting.WLScriptContext as wlctx
from jarray import array
from java import *
from weblogic import *
import weblogic.version
from java.lang import *
from javax.management import *
from weblogic.management.scripting import ScriptException
from org.python.core import PyArray
from org.python.core import PyClass
from org.python.core import PyComplex
from org.python.core import PyDictionary
from org.python.core import PyException
from org.python.core import PyFile
from org.python.core import PyFloat
from org.python.core import PyInteger
from org.python.core import PyList
from org.python.core import PyLong
from org.python.core import PyObject
from org.python.core import PyString
from weblogic.management.scripting.utils import WLSTUtil

### Define all the global variables here

theInterpreter=WLSTUtil.ensureInterpreter();
WLS_ON=WLSTUtil.ensureWLCtx(theInterpreter)
nmService=WLS_ON.getNodeManagerService();

home=None
adminHome=None
cmo=None
CMO=None
mbs=None
cmgr=None
domainRuntimeService=None
runtimeService=None
editService=None
_editService=None
typeService=None
myps1="wls:/offline> "
sys.ps1=myps1
ncPrompt="wls:/offline> "
serverName = "";
domainName = "";
connected = "false"
lastPrompt = ""
domainType = ""
promptt = ""
hideProm = "false"
username = ""
wlsversion = weblogic.version.getVersions()
version = weblogic.version.getVersions()
isAdminServer = ""
recording = "false"
scriptMode = "false"
dcCalled = "false"
exitonerror = "true"
wlstPrompt = "true"
wlstVersion = "dev2dev version"
oldhook=sys.displayhook
true=1
false=0
LAST = None

### JSR88

def loadApplication(appPath, planPath=None, createPlan='true'):
  global LAST
  'This will load the application and return the WLSTPlan object'
  hideDisplay()
  LAST = WLS_ON.loadApplication(appPath, planPath, createPlan)
  return LAST

def loadApp(appPath, planPath=None):
  global LAST
  LAST = loadApplication(appPath, planPath)
  return LAST

### NodeManager

def nmConnect(username=None,
              password=None,
              host='localhost',
              port=-1,
              domainName=None,
              domainDir=None,
              nmType='ssl',
              verbose = 'false',
              **useBootProperties):
  # connects to the node manager
  if nmService.isConnectedToNM():
    WLS_ON.println('Already connected to a Node Manager')
    return
  try:
    nmService.nmConnect(username,
                        password,
                        host,
                        port,
                        domainName,
                        domainDir,
                        nmType,
                        verbose,
                        useBootProperties)
    updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmDisconnect():
  # disconnects from Node Manager
  try:
    nmService.nmDisconnect()
    updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nm():
  global LAST
  # Indicated if the user is connected to Node Manager
  try:
    hideDisplay()
    LAST = nmService.nm()
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmKill(serverName='myserver', serverType = 'WebLogic'):
  global LAST
  try:
    hideDisplay()
    LAST = nmService.nmKill(serverName, serverType)
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmStart(serverName='myserver',
            domainDir = None,
            props=None,
            writer=None,
            serverType = 'WebLogic'):
  global LAST
  try:
    hideDisplay()
    result = nmService.nmStart(serverName,
                               domainDir,
                               props,
                               writer,
                               serverType)
    updateGlobals()
    LAST = result
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmServerStatus(serverName='myserver', serverType='WebLogic'):
  global LAST
  try:
    hideDisplay()
    LAST = nmService.nmServerStatus(serverName, serverType)
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmServerLog(serverName='myserver', writer=None, serverType='WebLogic'):
  global LAST
  try:
    hideDisplay()
    LAST = nmService.nmServerLog(serverName, writer, serverType)
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmLog(writer=None):
  global LAST
  try:
    hideDisplay()
    LAST = nmService.nmLog(writer)
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def nmVersion():
  global LAST
  try:
    hideDisplay()
    LAST = nmService.nmVersion()
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

from weblogic.utils import JavaExec
from java.io import File
from java.util import Properties
from weblogic.management.scripting.utils import ScriptCommands
myChild=None
def startNodeManager(verbose='true', 
                     block='false',
                     timeout=120000,
                     username=None,
                     password=None,
                     host='localhost',
                     port=-1,
                     **nmProperties):
    try:
        global myChild
        if myChild != None:
          WLS_ON.println('A Node Manager has already been started. ')
          WLS_ON.println('Cannot start another Node Manager process via WLST')
          return
        WLS_ON.setCommandType(ScriptCommands.START_NODE_MANAGER)
        nmProcess = JavaExec.createCommand("weblogic.NodeManager")
        nmProcess.addDefaultClassPath()
        if verbose=='true':
          nmProcess.addArg("-v")
        javaVendor = System.getProperty("java.vendor")
        javaDataArch = System.getProperty("sun.arch.data.model")
        doNotUseD64 = System.getProperty("do.not.use.d64")
        osName = System.getProperty("os.name") 
        if doNotUseD64 is None or doNotUseD64=='false':
          if javaDataArch=='64' and (javaVendor=='Sun Microsystems Inc.' or javaVendor=='Hewlett-Packard Co.') and (osName=='Solaris' or osName=='HP-UX' or osName=='SunOS'):
            nmProcess.addJvmArg("-d64")
        keys = nmProperties.keys()
        keys.sort()
        systemProperties = Properties()
        systemProperties.setProperty("QuitEnabled","true")
        nmDir = "."
	nmProps="{";
        for kw in keys:
          if kw == "NodeManagerHome":
            nmDir = nmProperties[kw]
          systemProperties.setProperty(kw, nmProperties[kw])
          nmProps += kw+"="+nmProperties[kw]+","
        nmProps+="}"
        nmCommand = nmProcess.getCommand()
        msg = "Launching NodeManager with: "+nmCommand+" ... "
	fmtr = WLS_ON.getWLSTMsgFormatter()
	msg = fmtr.getLaunchingNodeManager(nmProps, nmCommand)
        WLS_ON.print(msg)
        nmProcess.addSystemProps(systemProperties)
        #
        # SubProcess is exec-ed here
        #
        myChild = nmProcess.getProcess()
        #
        # startProcess is a misnomer. The process is already started.
        # It is setting up the threads to read stdout and stderr
        #
        nmLogDir = File(nmDir)
        WLSTUtil.startProcess(myChild, "NMProcess",1)
        java.lang.Thread.sleep(10000)
        WLS_ON.println('Successfully launched the Node Manager.')
        WLS_ON.println('The Node Manager process is running independent of the WLST process.')
        WLS_ON.println('Exiting WLST will not stop the Node Manager process. Please refer ')
        WLS_ON.println('to the Node Manager logs for more information.')
        WLS_ON.println('The Node Manager logs will be under '+nmLogDir.getAbsolutePath())
    except Exception, e:
        e.printStackTrace()
        WLS_ON.println('Problem starting Node Manager')
        # kill the process
        myChild.destroy()
        return
    # wait for node manager process to start if requested
    if block=='true':
        WLS_ON.println("Waiting for Node Manager to start")
        isConnected = 0
        quitTime = java.lang.System.currentTimeMillis() + float(timeout)
        finalException = None
        while not isConnected and not isTimeoutExpired(quitTime):
            try:
                nmConnect(username,password,host,port)
                isConnected = 1
            except Exception, e:
                finalException = e
                java.lang.Thread.sleep(2000)
            if not isConnected and isTimeoutExpired(quitTime):
                WLS_ON.println("Connection Failed.")
                raise finalException
    else:
        WLS_ON.println("Node Manager starting in the background")

# call the function here
def stopNodeManager():
    global myChild
    if myChild!=None:
        WLS_ON.setCommandType(ScriptCommands.STOP_NODE_MANAGER)
        myChild.destroy()
        myChild = None
        print 'Stopped Node Manager Process successfully'
    else:
        nmService.nmQuit()
    updateGlobals()


# Determine if the connect retry count has reached the limit.
# Un-documented
# @exclude
def isTimeoutExpired(quitTime):
    return java.lang.System.currentTimeMillis() > quitTime

### utilities

def loadProperties(fileName):
  # verify theInterpreter is set
  if WLSTUtil.runningWLSTAsModule():
    WLS_ON.println('Cannot use loadProperties while using wlst as a module')
    return
  global theInterpreter
  WLS_ON.loadProperties(fileName, theInterpreter)

def storeUserConfig(userConfigFile=None, userKeyFile=None, nm='false'):
  try:
    WLS_ON.storeUserConfig(userConfigFile, userKeyFile, nm)
  except ScriptException,e:
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def lookup(name, childMBeanType=None):
  global LAST
  try:
    hideDisplay()
    LAST = WLS_ON.lookup(name,childMBeanType)
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def configToScript(configPath=None,
                   pyPath=None,
                   overWrite="true",
                   propertiesFile=None,
                   createDeploymentScript="false",
                   convertTheseResourcesOnly="",
                   debug = "false"
                   ):
  try:
    WLS_ON.config2Py(configPath,
                     pyPath,
                     overWrite,
                     propertiesFile,
                     createDeploymentScript,
                     convertTheseResourcesOnly,
                     debug)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def migrateProviders(oldBeaHome=None, newBeaHome=None, verbose='false'):
  'This will migrate old security jars to new '
  hideDisplay()
  WLS_ON.migrateProviders(oldBeaHome, newBeaHome, verbose)

def encrypt(obj, domainDir=None):
  global LAST
  try:
    result = WLS_ON.encrypt(obj, domainDir)
    hideDisplay()
    LAST = result
    return LAST
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

# lifecycle
def startServer(adminServerName="myserver",
                domainName="mydomain",
                url=None,
                username=None,
                password=None,
                domainDir=".",
                block='true',
                timeout=120000,
                useNM='true',
                overWriteRootDir='false',
                serverLog=None,
                systemProperties=None,
                jvmArgs = None,
                spaceAsJvmArgsDelimiter = 'false',
	        generateDefaultConfig = 'false'):
  global LAST
  try:
    LAST = WLS_ON.startSvr(domainName,
                           adminServerName,
                           username,
                           password,
                           url,
                           domainDir,
                           generateDefaultConfig,
                           overWriteRootDir,
                           block,
                           timeout,
                           useNM,
                           serverLog,
                           systemProperties,
                           jvmArgs,
                           spaceAsJvmArgsDelimiter)
    return LAST
    updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

### Custom help commands

def addHelpCommandGroup(groupName, resourceBundleName):
  try:
    WLS_ON.addHelpCommandGroup(groupName, resourceBundleName)
  except ScriptException,e:
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def addHelpCommand(commandName, groupName, offline='false', online='false'):
  try:
    WLS_ON.addHelpCommand(commandName, groupName, offline, online)
  except ScriptException,e:
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())


### Miscellaneous commands (debug, command syntax, prompt, etc.)

# Un-documented
# @exclude
def debug(val=None):
  try:
    WLS_ON.debug(val)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())


# Un-documented
# @exclude
def easeSyntax():
  WLS_ON.easeSyntax()

# Un-documented
# @exclude
def eatdisplay(dummy):
  pass

# Un-documented
# @exclude
def hideDisplay():
  sys.displayhook=eatdisplay

# Un-documented
# @exclude
def restoreDisplay():
  'This will restore the Display to Default'
  global oldhook,myps1
  sys.displayhook=oldhook
  myps1=sys.ps1

# @exclude
def hideDumpStack(value="true"):
    WLS_ON.setHideDumpStack(value)

# Un-documented
# @exclude
def setDumpStackThrowable(value=None):
    WLS_ON.setDumpStackThrowable(value)
    
import weblogic.management.scripting.WLSTConstants as tree
# Un-documented
# @exclude
def evaluatePrompt():
  if WLSTUtil.runningWLSTAsModule():
    return
  global wlstPrompt,dcCalled,lastPrompt,domainName,hideProm
  global domainType,showStack,wlsversion,mbs,connected,myps1,promptt
  global ncPrompt,serverName
  if wlstPrompt=="false":
    myps1=sys.ps1
    sys.ps1=myps1
    return
  if hideProm == "false":
    if nmService.isConnectedToNM() == 1 and connected == "false":
      myps1="wls:/nm/"+str(nmService.getDomainName())+"> "
      sys.ps1 = myps1
      return
    if domainType == tree.DEPRECATED_ADMIN_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.DEPRECATED_CONFIG_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.DEPRECATED_RUNTIME_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.DEPRECATED_RUNTIME_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.CUSTOM_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.CUSTOM_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.DOMAIN_CUSTOM_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.DOMAIN_CUSTOM_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.JNDI_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.JNDI_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.CONFIG_RUNTIME_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.CONFIG_RUNTIME_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.RUNTIME_RUNTIME_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.RUNTIME_RUNTIME_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.RUNTIME_DOMAINRUNTIME_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.RUNTIME_DOMAINRUNTIME_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.CONFIG_DOMAINRUNTIME_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.CONFIG_DOMAINRUNTIME_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.EDIT_TREE:
      if WLS_ON.isEditSessionInProgress() == 1:
        myps1="wls:/"+str(domainName)+"/"+tree.EDIT_PROMPT+promptt+" !> "
      else:
        myps1="wls:/"+str(domainName)+"/"+tree.EDIT_PROMPT+promptt+"> "
      sys.ps1=myps1
    elif domainType == tree.DEPRECATED_CONFIG_TREE:
      myps1="wls:/"+str(domainName)+"/"+tree.DEPRECATED_CONFIG_PROMPT+promptt+"> "
      sys.ps1=myps1
  elif hideProm == "true":
    myps1="wls:/> "
    sys.ps1=myps1
  if connected=="false":
    # see if readTemplate or anything is intact
    offlinePrompt = java.lang.String(WLS.getAbsPwd())
    if offlinePrompt != None and offlinePrompt.length()!= 0:
      myps1 = "wls:/offline"+str(offlinePrompt)+"> "
    else:
      myps1=ncPrompt
    sys.ps1=myps1

# Un-documented
# @exclude
def updateGlobals():
  global isAdminServer,version,username,cmo,home,adminHome,wlstPrompt,dcCalled
  global lastPrompt,domainName,hideProm,domainType,showStack,wlsversion
  global mbs,connected,myps1,promptt,ncPrompt,serverName,cmgr,domainRuntimeService
  global hideProm,editService,runtimeService,typeService,scriptMode
  cmo = WLS_ON.getCmo()
  promptt = WLS_ON.getPrompt()
  domainName = WLS_ON.getDomainName()
  connected = WLS_ON.getConnected()
  domainType = WLS_ON.getDomainType()
  mbs = WLS_ON.getMBeanServer()
  home = WLS_ON.getHome()
  adminHome = WLS_ON.getAdminHome()
  serverName = WLS_ON.getServerName()
  wlsversion = WLS_ON.getVersion()
  username = String(WLS_ON.getUsername_bytes())
  cmgr = WLS_ON.getConfigManager()
  version = wlsversion
  isAdminServer = WLS_ON.isAdminServer()
  domainRuntimeService = WLS_ON.getDomainRuntimeServiceMBean()
  runtimeService = WLS_ON.getRuntimeServiceMBean()
  editService = WLS_ON.getEditServiceMBean()
  typeService = WLS_ON.getMBeanTypeService()
  scriptMode = WLS_ON.getScriptMode()
  evaluatePrompt()

# Un-documented
# @exclude
class WLSTException(Exception):
  cause = None
  def __init__(self, value):
    self.value = value
  def __str__(self):
    # See CR344726.
    # return repr(self.value)
    return PyString(self.value)
  def getCause(self):
    return self.cause
  def setCause(self, acause):
    self.cause = acause

# Un-documented
# @exclude
def raiseWLSTException(e):
  wlste = WLSTException(e.getMessage())
  wlste.setCause(e.getCause())
  raise wlste

# Un-documented
# @exclude
def makePropertiesObject(value):
  try:
    hideDisplay()
    return WLS_ON.makePropertiesObject(value)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

# Un-documented
# @exclude
def makeArrayObject(value):
  try:
    hideDisplay()
    return WLS_ON.makeArrayObject(value)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def redirect(outputFile=None, toStdOut="true"):
  try:
    WLS_ON.redirect(outputFile, toStdOut)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

def stopRedirect():
  try:
    WLS_ON.stopRedirect()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      WLS_ON.println(e.getMessage())

