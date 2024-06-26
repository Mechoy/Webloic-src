"""
This is WLST Module helper that defines the common commands between
offline and online wlst
@author Satya Ghattu
Copyright (c) 2004 by BEA Systems, Inc. All Rights Reserved.

WARNING: This file is part of the WLST implementation and as such may
change between versions of WLST. You should not try to reuse the logic
in this script or keep copies of this script. Doing so could cause
your WLST scripts to fail when you upgrade to a different version of
WLST.

"""

from weblogic.management.scripting.utils import WLSTUtil

theInterpreter=WLSTUtil.ensureInterpreter();
WLS_ON=WLSTUtil.ensureWLCtx(theInterpreter)

def connect(username="",password="",url="",**options):
  """
  The connect function allows a user to connect to a weblogic
  Server instance. The user provides a valid username and password
  or the location to the userConfig and userKey files.
  """
  try:
    global connected, WLS_ON, _editService
    WLS_ON.connect(username,password,url, options)
    _editService = WLS_ON.getEditService()
    updateGlobals()
    print ""
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
        print e.getMessage()

def cd(mbeanName):
  """
  This function allows a user to navigate from one MBean instance
  to another instance and viceversa
  """
  if WLS_ON.isConnected() == 0:
    return WLS.cd(mbeanName)
  try:
    global cmo, hideProm
    WLS_ON.cd(mbeanName)
    print ""
    updateGlobals()
    hideDisplay()
    return cmo
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()
      hideProm="true"
    if hideProm == "true":
      hideProm = "false"
      updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()

def pwd():
  if WLS_ON.isConnected() == 0:
    return WLS.getAbsPwd()
  else:
    thePr = WLS_ON.getPrompt()
    if thePr == "":
      return WLS_ON.getTree()+":/"
    else:
      return WLS_ON.getTree()+":"+thePr

def create(name, childMBeanType=None, baseProviderType=None):
  if WLS_ON.isConnected() == 0:
    return WLS.create(name,childMBeanType)
  try:
    hideDisplay()
    return WLS_ON.create(name,childMBeanType,baseProviderType)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()

def delete(name, childMBeanType=None):
  if WLS_ON.isConnected() == 0:
    return WLS.delete(name,childMBeanType)
  try:
    hideDisplay()
    return WLS_ON.delete(name,childMBeanType)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()
      

def get(attrName):
  if WLS_ON.isConnected() == 0:
    return WLS.get(attrName)
  try:
    getObj = WLS_ON.get(attrName)
    updateGlobals()
    return getObj
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()

def set(attrName, value):
  if WLS_ON.isConnected() == 0:
    return WLS.set(attrName,value)
  try:
    setObj = WLS_ON.set(attrName,value)
    updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()

def disconnect(force="false"):
  try:
    global connected,cmo,home,adminHome,mbs,promptt,WLS_ON
    cmo=None
    mbs=None
    connected="false"
    adminHome=None
    home=None
    promptt=""
    myps1=ncPrompt
    WLS_ON.dc(force)
    home = WLS_ON.getHome()
    adminHome = WLS_ON.getAdminHome()
    cmo = WLS_ON.getCmo()
    mbs = WLS_ON.getMBeanServer()
    cmgr = WLS_ON.getConfigManager()
    serverName = "";
    domainName = "";
    connected = "false"
    lastPrompt = ""
    domainType = ""
    hideProm = "false"
    username = ""
    wlsversion = weblogic.version.getVersions()
    version = weblogic.version.getVersions()
    isAdminServer = ""
    recording = "false"
    dcCalled = "false"
    exitonerror = "true"
    wlstPrompt = "true"
    wlstVersion = "dev2dev version"
    oldhook=sys.displayhook
    cmgr = WLS_ON.getConfigManager()
    scriptMode = WLS_ON.getScriptMode()
    true=1
    false=0
    domainRuntimeService = WLS_ON.getDomainRuntimeServiceMBean()
    runtimeService = WLS_ON.getRuntimeServiceMBean()
    editService = WLS_ON.getEditServiceMBean()
    typeService = WLS_ON.getMBeanTypeService()
    _editService = WLS_ON.getEditService()
    LAST = None
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()
  evaluatePrompt()

def ls(attrName=None, returnMap="false", returnType=None):
  if WLS_ON.isConnected() == 0:
    if attrName == None:
       attrName = 'x'
    if returnType == None:
       returnType = 'c'

    return WLS.ls(attrName, returnMap, returnType)
  try:
    obj = WLS_ON.ls(attrName, returnType)
    print("")
    hideDisplay()
    if returnMap == "true":
      return _getList(obj, attrName)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()


def dumpVariables():
  try:
    if WLS_ON.isConnected() == 0:
      return WLS.dumpVariables()
    WLS_ON.dumpVariables()
    print 'exitonerror'+WLS_ON.calculateTabSpace("exitonerror",30)+exitonerror
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()
  

def find(name=None, type=None, searchInstancesOnly="true"):
  if WLS_ON.isConnected() == 0:
    return WLS.find(name,type)
  hideDisplay()
  return WLS_ON.find(name,type,searchInstancesOnly)    

def startRecording(filePath, recordAll='false'):
  if WLS_ON.isConnected() == 0:
    return WLS.startRecording(filePath)
  global recording
  recording='true'
  try:
    WLS_ON.startRecording(filePath, recordAll)
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()

def stopRecording():
  if WLS_ON.isConnected() == 0:
    return WLS.stopRecording()
  global recording
  recording='false'
  try:
    WLS_ON.stopRecording()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=="true":
      raiseWLSTException(e)
    else:
      print e.getMessage()
      
