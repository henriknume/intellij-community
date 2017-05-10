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
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.FsmModel;

/**
 * Created by David on 2017-05-08
 */
public class ViewImplementationModel implements FsmModel {

  private String[] variableRef = new String[]{"public final int fooInt = 1;\n","",""};
  public boolean isVariableAdded = false;

  private String[] methodRef = new String[]{"public void fooMethod(){}\n"," "," " };
  public boolean isMethodAdded = false;

  private String[] enumRef = new String[]{"public enum Foo{A, B,};\n",""," " };
  public boolean isEnumAdded = false;

  private State state = State.CARET_AT_UNDEFINED;
  private ViewImplementationAdapter adapter;

  private int enumCount = 0;
  private int methodCount = 0;
  private int variableCount = 0;

  public ViewImplementationModel() throws Exception {
    this.adapter = new ViewImplementationAdapter();
  }

  private enum State {
    //CARET_AT_ENUM,
    CARET_AT_METHOD,
    //CARET_AT_VARIABLE,
    //ENUM_IMPL,
    //ENUM_DOCU,
    //METHOD_IMPL,
    //METHOD_DOCU,
    //VARIABLE_IMPL,
    //VARIABLE_DOCU,
    CARET_AT_UNDEFINED
  }

  public boolean placeCaretAtUndefinedGuard() {
    return !(state == State.CARET_AT_UNDEFINED);
  }

  public boolean placeCaretAtMethodGuard() {
    return state == State.CARET_AT_UNDEFINED;
  }

  public boolean addVariableGuard() {
    return (state == State.CARET_AT_UNDEFINED && !isVariableAdded);
  }

  @Action
  public void placeCaretAtUndefined() {
    adapter.placeCaretAtUndefined();
    state = State.CARET_AT_UNDEFINED;
  }

  @Override
  public Object getState() {
    return state;
  }

  @Override
  public void reset(boolean b) {
    state = State.CARET_AT_UNDEFINED;
    adapter.placeCaretAtUndefined();
    //And clear the class
  }

  public void cleanup() throws Exception {
    adapter.cleanup();
  }
}
