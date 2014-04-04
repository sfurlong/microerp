; CLW file contains information for the MFC ClassWizard

[General Info]
Version=1
LastClass=MainView
LastTemplate=CDialog
NewFileInclude1=#include "stdafx.h"
NewFileInclude2=#include "mycall.h"
LastPage=0

ClassCount=12
Class1=CAreaCombo
Class2=CCursorBox
Class3=CLogonServer
Class4=CMainFrame
Class5=MainView
Class6=CMYCALLApp
Class7=CAboutDlg
Class8=CMYCALLDoc
Class9=CRPEMDIChildWnd
Class10=SelectEvent
Class11=CTableLogon
Class12=WindowOptions

ResourceCount=7
Resource1=IDD_TABLELOGON
Resource2=IDD_LOGONSERVER
Resource3=IDD_SELECTEVENTS (English (Canada))
Resource4=IDR_MAINFRAME
Resource5=IDD_CURSORBAR (English (Canada))
Resource6=ID_MAINFORM (English (Canada))
Resource7=IDD_WINDOWOPTIONS (English (Canada))

[CLS:CAreaCombo]
Type=0
BaseClass=CComboBox
HeaderFile=Areacmbo.h
ImplementationFile=Areacmbo.cpp

[CLS:CCursorBox]
Type=0
BaseClass=CComboBox
HeaderFile=Cursorbx.h
ImplementationFile=Cursorbx.cpp

[CLS:CLogonServer]
Type=0
BaseClass=CDialog
HeaderFile=logonser.h
ImplementationFile=logonser.cpp

[CLS:CMainFrame]
Type=0
BaseClass=CFrameWnd
HeaderFile=MainFrm.h
ImplementationFile=MainFrm.cpp
LastObject=CMainFrame

[CLS:MainView]
Type=0
BaseClass=CFormView
HeaderFile=MainView.h
ImplementationFile=MainView.cpp
Filter=D
VirtualFilter=VWC
LastObject=MainView

[CLS:CMYCALLApp]
Type=0
BaseClass=CWinApp
HeaderFile=MYCALL.h
ImplementationFile=MYCALL.cpp

[CLS:CAboutDlg]
Type=0
BaseClass=CDialog
HeaderFile=MYCALL.cpp
ImplementationFile=MYCALL.cpp
LastObject=CAboutDlg

[CLS:CMYCALLDoc]
Type=0
BaseClass=CDocument
HeaderFile=Mycalldc.h
ImplementationFile=Mycalldc.cpp

[CLS:CRPEMDIChildWnd]
Type=0
BaseClass=CMDIChildWnd
HeaderFile=peplus.h
ImplementationFile=peplus.cpp

[CLS:SelectEvent]
Type=0
BaseClass=CPropertyPage
HeaderFile=Selectev.h
ImplementationFile=Selectev.cpp

[CLS:CTableLogon]
Type=0
BaseClass=CDialog
HeaderFile=tablelog.h
ImplementationFile=tablelog.cpp

[CLS:WindowOptions]
Type=0
BaseClass=CPropertyPage
HeaderFile=Windowop.h
ImplementationFile=Windowop.cpp

[DLG:IDD_LOGONSERVER]
Type=1
Class=CLogonServer
ControlCount=13
Control1=IDLOGONSERVER,button,1342242817
Control2=IDOK,button,1342242816
Control3=IDC_STATIC,static,1342308352
Control4=IDC_STATIC,static,1342308352
Control5=IDC_STATIC,static,1342308352
Control6=IDC_STATIC,static,1342308352
Control7=IDC_STATIC,static,1342308352
Control8=IDC_STATIC,button,1342177287
Control9=IDC_EDIT1,edit,1350631552
Control10=IDC_EDIT2,edit,1350631552
Control11=IDC_EDIT3,edit,1350631552
Control12=IDC_EDIT4,edit,1350631552
Control13=IDC_EDIT5,edit,1350631584

[DLG:ID_MAINFORM]
Type=1
Class=MainView

[DLG:IDD_SELECTEVENTS]
Type=1
Class=SelectEvent

[DLG:IDD_TABLELOGON]
Type=1
Class=CTableLogon
ControlCount=15
Control1=ID_SETLOGON,button,1342242817
Control2=IDCANCEL,button,1342242816
Control3=65535,static,1342308352
Control4=65535,static,1342308352
Control5=65535,static,1342308352
Control6=65535,static,1342308352
Control7=65535,button,1342177287
Control8=IDC_EDIT1,edit,1350631552
Control9=IDC_EDIT2,edit,1350631552
Control10=IDC_EDIT3,edit,1350631552
Control11=IDC_EDIT4,edit,1350631584
Control12=IDC_Tables,combobox,1344340034
Control13=IDC_TABLE,static,1342308352
Control14=IDC_PROPAGATE,button,1342177283
Control15=IDC_TEST,button,1342242816

[DLG:IDD_WINDOWOPTIONS]
Type=1
Class=WindowOptions

[TB:IDR_MAINFRAME]
Type=1
Class=?
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_FILE_SAVE
Command4=ID_EDIT_CUT
Command5=ID_EDIT_COPY
Command6=ID_EDIT_PASTE
Command7=ID_FILE_PRINT
Command8=ID_APP_ABOUT
CommandCount=8

[MNU:IDR_MAINFRAME]
Type=1
Class=CMainFrame
Command1=ID_OPENJOB
Command2=ID_CLOSEPRINTJOB
Command3=ID_START
Command4=ID_DISCARDSAVEDDATA
Command5=ID_APP_EXIT
Command6=ID_LOGONTABLE
Command7=ID_LOGONSERVER
CommandCount=7

[ACL:IDR_MAINFRAME]
Type=1
Class=?
Command1=ID_FILE_NEW
Command2=ID_FILE_OPEN
Command3=ID_FILE_SAVE
Command4=ID_FILE_PRINT
Command5=ID_EDIT_UNDO
Command6=ID_EDIT_CUT
Command7=ID_EDIT_COPY
Command8=ID_EDIT_PASTE
Command9=ID_EDIT_UNDO
Command10=ID_EDIT_CUT
Command11=ID_EDIT_COPY
Command12=ID_EDIT_PASTE
Command13=ID_NEXT_PANE
Command14=ID_PREV_PANE
CommandCount=14

[DLG:IDD_SELECTEVENTS (English (Canada))]
Type=1
Class=?
ControlCount=11
Control1=IDC_CHECK1,button,1342242819
Control2=IDC_CHECK9,button,1342242819
Control3=IDC_CHECK10,button,1342242819
Control4=IDC_CHECK12,button,1342242819
Control5=IDC_CHECK13,button,1342242819
Control6=IDC_CHECK14,button,1342242819
Control7=IDC_CHECK15,button,1342244099
Control8=IDC_CHECK16,button,1342373891
Control9=IDC_STATIC,button,1342177543
Control10=IDC_STATIC,button,1342177287
Control11=IDC_FILELOCATION,static,1342308352

[DLG:ID_MAINFORM (English (Canada))]
Type=1
Class=?
ControlCount=1
Control1=IDC_LIST1,listbox,1353789441

[DLG:IDD_WINDOWOPTIONS (English (Canada))]
Type=1
Class=?
ControlCount=13
Control1=IDC_CHECK1,button,1342242819
Control2=IDC_CHECK2,button,1342242819
Control3=IDC_CHECK3,button,1342242819
Control4=IDC_CHECK4,button,1342242819
Control5=IDC_CHECK6,button,1342242819
Control6=IDC_CHECK7,button,1342242819
Control7=IDC_CHECK8,button,1342242819
Control8=IDC_CHECK9,button,1342242819
Control9=IDC_CHECK10,button,1342242819
Control10=IDC_CHECK11,button,1342242819
Control11=IDC_CHECK12,button,1342242819
Control12=IDC_STATIC,button,1342177287
Control13=IDC_CHECK13,button,1342242819

[DLG:IDD_CURSORBAR (English (Canada))]
Type=1
Class=?
ControlCount=3
Control1=IDC_STATIC,static,1342308352
Control2=IDC_STATIC,static,1342308352
Control3=IDC_SETCURSOR,button,1342251776

