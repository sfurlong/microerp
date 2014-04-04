//-------------------------------------------------------------------------------
// This win32 c++ program will start a java program via a ".exe" program.
//
// For VC++ compile at the command line, use the following:  cl -MT jexe.cpp
//
//
//-------------------------------------------------------------------------------
#include <windows.h>
#include <stdio.h>
#include <jni.h>
#include <iostream.h>

#define MAIN_CLASS  "MyApp2"
int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{

	JNIEnv        *env;
	JavaVM        *jvm;
	jint           rc;
	jclass         cls;
	jmethodID      mainId;

	
    //Get CLASSPATH environment variable setting
	char* szClasspath = getenv( "CLASSPATH" );
    
	//Set all the java VM options here.
	JavaVMOption vm_Options[3]; 
	vm_Options[0].optionString="-Djava.class.path=e:;e:\\"; 
	vm_Options[1].optionString="-Djava.compiler=NONE"; 
	vm_Options[2].optionString="-verbose:class,jni"; 

	JavaVMInitArgs vm_InitArgs; 
	vm_InitArgs.version = JNI_VERSION_1_2; 
	vm_InitArgs.options = vm_Options; 
	vm_InitArgs.nOptions = 3; 
	vm_InitArgs.ignoreUnrecognized = JNI_FALSE; 


	//Create the JVM
	rc = JNI_CreateJavaVM( &jvm, (void**)&env, &vm_InitArgs);
	if ( rc < 0 ) {
		return 1;
	}

	//Load the class containing the static main() method
	cls = env->FindClass( "temp/MyApp2");
	if ( cls == 0 ) {
		cout << "Couldn't find class" << endl;
		return -2;
	}

	//Find the java main() method
	mainId = env->GetStaticMethodID(cls, "main", "([Ljava/lang/String;)V");
	if ( mainId == 0 ) {
		return 1;
	}

	//Call the java main
	env->CallStaticVoidMethod(cls, mainId, 0);

	//Kill the JVM
	jvm->DestroyJavaVM();

	return 5;
}

