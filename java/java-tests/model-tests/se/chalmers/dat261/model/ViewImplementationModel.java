/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.chalmers.dat261.model;

import com.intellij.codeInsight.javadoc.ViewImplementationAdapter;
import com.intellij.execution.testframework.sm.TestHistoryConfiguration;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 2017-05-08
 */
public class ViewImplementationModel implements FsmModel {

  private String[] variableRef = new String[]{"private final int fooInt = 1;\n", "<PRE>private final int <b>fooInt = 1</b></PRE>", ""};
  public boolean isVariableAdded = false;

  private String[] methodRef = new String[]{"private void fooMethod(){}\n", "", " "};
  public boolean isMethodAdded = false;

  private String[] enumRef = new String[]{"private enum Foo{A, B,};\n", "Foo", " "};
  public boolean isEnumAdded = false;

  private State state = State.CARET_AT_UNDEFINED;
  private ViewImplementationAdapter adapter;


  public ViewImplementationModel() throws Exception {
    this.adapter = new ViewImplementationAdapter();
  }

  private enum State {
    CARET_AT_ENUM,
    CARET_AT_METHOD,
    CARET_AT_VARIABLE,
    ENUM_IMPL,
    ENUM_DOCU,
    METHOD_IMPL,
    METHOD_DOCU,
    VARIABLE_IMPL,
    VARIABLE_DOCU,
    CARET_AT_UNDEFINED
  }

  public boolean addVariableGuard() {
    return (state == State.CARET_AT_UNDEFINED && !isVariableAdded);
  }

  public boolean addEnumGuard() {
    return (state == State.CARET_AT_UNDEFINED && !isEnumAdded);
  }

  public boolean addMethodGuard() { return (state == State.CARET_AT_UNDEFINED && !isMethodAdded); }

  public boolean rmVariableGuard() {
    return (state == State.CARET_AT_UNDEFINED && isVariableAdded);
  }

  public boolean rmEnumGuard() {
    return (state == State.CARET_AT_UNDEFINED && isEnumAdded);
  }

  public boolean rmMethodGuard() { return (state == State.CARET_AT_UNDEFINED && isMethodAdded); }

  public boolean placeCaretAtUndefinedGuard() {
    return (state == State.CARET_AT_VARIABLE ||
            state == State.CARET_AT_METHOD ||
            state == State.CARET_AT_ENUM);
  }

  public boolean placeCaretAtVariableGuard() {
    return (state == State.CARET_AT_UNDEFINED && isVariableAdded);
  }

  public boolean placeCaretAtEnumGuard() {
    return (state == State.CARET_AT_UNDEFINED && isEnumAdded);
  }

  public boolean placeCaretAtMethodGuard() {
    return (state == State.CARET_AT_UNDEFINED && isMethodAdded);
  }

  public boolean viewImpl1Guard() {
    return (state == State.CARET_AT_ENUM);
  }

  public boolean viewImpl2Guard() {
    return (state == State.CARET_AT_METHOD);
  }

  public boolean viewImpl3Guard() {
    return (state == State.CARET_AT_VARIABLE);
  }

  public boolean viewDocu1Guard(){
    return (state == State.CARET_AT_ENUM);
  }

  public boolean viewDocu2Guard(){
    return (state == State.CARET_AT_METHOD);
  }

  public boolean viewDocu3Guard() {
    return (state == State.CARET_AT_VARIABLE);
  }

  public boolean closeVariableWindowGuard() {
    return (state == State.VARIABLE_IMPL);
  }

  public boolean closeEnumWindowGuard() {
    return (state == State.ENUM_IMPL);
  }

  public boolean closeMethodWindowGuard() {
    return (state == State.METHOD_IMPL);
  }

  public boolean closeVariableDocWindowGuard() {
    return (state == State.VARIABLE_DOCU);
  }

  public boolean closeEnumDocWindowGuard() {
    return (state == State.ENUM_DOCU);
  }

  public boolean closeMethodDocWindowGuard() {return (state == State.METHOD_DOCU);}

  @Action
  public void addVariable() {
    adapter.addVariable(variableRef[0]);
    isVariableAdded = true;
    assertTrue(documentContains(variableRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void addEnum() {
    adapter.addEnum(enumRef[0]);
    isEnumAdded = true;
    assertTrue(documentContains(enumRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void addMethod() {
    adapter.addMethod(methodRef[0]);
    isMethodAdded = true;
    assertTrue(documentContains(methodRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void rmVariable() {
    adapter.removeVariable();
    isVariableAdded = false;
    assertFalse(documentContains(variableRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void rmEnum() {
    adapter.removeEnum();
    isEnumAdded = false;
    assertFalse(documentContains(enumRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void rmMethod() {
    adapter.removeMethod();
    isMethodAdded = false;
    assertFalse(documentContains(methodRef[0]));
    assertEquals(nrOfAddedElements(), adapter.countNrOfAddedElements());
  }

  @Action
  public void placeCaretAtUndefined() {
    adapter.placeCaretAtUndefined();
    state = State.CARET_AT_UNDEFINED;
    //printAdapter();
  }


  @Action
  public void placeCaretAtVariable() {
    adapter.placeCaretAtVariable();
    state = State.CARET_AT_VARIABLE;
  }

  @Action
  public void placeCaretAtEnum() {
    adapter.placeCaretAtEnum();
    state = State.CARET_AT_ENUM;
  }

  @Action
  public void placeCaretAtMethod() {
    adapter.placeCaretAtMethod();
    state = State.CARET_AT_METHOD;
  }

  @Action
  public void viewImpl1() {
    String impl = adapter.viewEnumImplementation();
    assertEquals(enumRef[0].trim(), impl.trim());
    state = State.ENUM_IMPL;
  }

  @Action
  public void viewImpl2() {
    String impl = adapter.viewMethodImplementation();
    assertEquals(methodRef[0].trim(), impl.trim());
    state = State.METHOD_IMPL;
  }

  @Action
  public void viewImpl3() {
    String impl = adapter.viewVariableImplementation();
    assertEquals(variableRef[0].trim(), impl.trim());
    state = State.VARIABLE_IMPL;
  }

  @Action
  public void viewDocu1() {
    String docu = adapter.viewEnumDocumentation();
    assertTrue(docu.contains(enumRef[1]));
    state = State.ENUM_DOCU;
  }

  @Action
  public void viewDocu2() {
    String docu = adapter.viewMethodDocumentation();
    assertTrue(docu.contains(methodRef[1]));
    state = State.METHOD_DOCU;
  }

  @Action
  public void viewDocu3() {
    String docu = adapter.viewVariableDocumentation();
    assertTrue(docu.contains(variableRef[1]));
    state = State.VARIABLE_DOCU;
  }

  @Action
  public void closeVariableWindow() {
    state = State.CARET_AT_VARIABLE;
  }

  @Action
  public void closeMethodWindow() {
    state = State.CARET_AT_METHOD;
  }

  @Action
  public void closeEnumWindow() {
    state = State.CARET_AT_ENUM;
  }


  @Action
  public void closeVariableDocWindow() {
    state = State.CARET_AT_VARIABLE;
  }

  @Action
  public void closeMethodDocWindow() {
    state = State.CARET_AT_METHOD;
  }

  @Action
  public void closeEnumDocWindow() {
    state = State.CARET_AT_ENUM;
  }


  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {
    //reset model
    state = State.CARET_AT_UNDEFINED;
    isMethodAdded = false;
    isEnumAdded = false;
    isVariableAdded = false;

    //reset sut
    adapter.reset();

    //verify
    String expected = "\npublic class ViewImplementation {\n" +
                     "  \n" +
                     "}\n";
    assertEquals(expected, adapter.getContent());
  }

  public void cleanup() throws Exception {
    adapter.cleanup();
  }

  public void printAdapter(){
    System.out.println("-----------------------");
    System.out.println(adapter.getContent());
    System.out.println("-----------------------");
  }

  private boolean documentContains(String text){
    return adapter.getContent().contains(text);
  }

  private int nrOfAddedElements(){
    int c = 0;
    if(isVariableAdded){
      c++;
    }
    if(isEnumAdded){
      c++;
    }
    if(isMethodAdded){
      c++;
    }
    return c;
  }
}
