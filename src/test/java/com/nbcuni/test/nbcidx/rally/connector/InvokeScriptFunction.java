package com.nbcuni.test.nbcidx.rally.connector;

import javax.script.*;

import org.testng.annotations.Test;

	public class InvokeScriptFunction {
	@Test(groups = {"full"})
	public static void abc() throws Exception {
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = manager.getEngineByName("JavaScript");

	    // JavaScript code in a String
	    String script1 = (String)"function hello(name) {print ('Hello, ' + name);}";
	    String script2 = (String)"function getValue(a,b) { if (a==='Number') return 1; else return b;}";

	    // evaluate script
	    engine.eval(script1);
	    engine.eval(script2);

	    Invocable inv = (Invocable) engine;

	    inv.invokeFunction("hello", "Scripting!!");
   
	 }
}
