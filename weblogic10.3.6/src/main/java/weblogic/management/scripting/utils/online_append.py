"""
@author Satya Ghattu
Copyright (c) 2004 by BEA Systems, Inc. All Rights Reserved.

WARNING: This file is part of the WLST implementation and as such may
change between versions of WLST. You should not try to reuse the logic
in this script or keep copies of this script. Doing so could cause
your WLST scripts to fail when you upgrade to a different version of
WLST.
"""
import weblogic.management.scripting.utils.WLSTUtil
online_util = weblogic.management.scripting.utils.WLSTUtil()

def connect(username='', password='', url='', **options):
  try:
      online_util.initializeOnlineWLST(username, password, url, theInterpreter, options)
      updateGlobals()
  except ScriptException,e:
    updateGlobals()
    if exitonerror=='true':
      WLSTUtil.setupOffline(theInterpreter)
      raiseWLSTException(e)
    else:
      WLSTUtil.setupOffline(theInterpreter)
      print e.getMessage()

def help(type='default',name='default1'):
  try:
    if type=='default' and name=='default1':
      WLS_ON.help(name)
      print ''
    elif type!='default' and name=='default1':
      WLS_ON.help(type)
      print ''
    elif type!='default' and name!='default1':
      WLS_ON.helpMe(type,name)
      print ''
  except ScriptException,e:
    updateGlobals()
    if exitonerror=='true':
      raiseWLSTException(e)
    else:
      print e.getMessage()

def exit(defaultAnswer=None, exitcode=0):
  WLS_ON.exit(defaultAnswer,exitcode)
  
def dumpStack():
  if WLS_ON.isConnected() == 0 and WLS_ON.getStackTrace() == None:
    return WLS.dumpStack()
  else:
    return WLS_ON.dumpStack()
  
myps1="wls:/offline> "

def writeIniFile(filePath):
  WLS_ON.writeIniFile(filePath)

def startRecording(filePath, recordAll='true'):
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

