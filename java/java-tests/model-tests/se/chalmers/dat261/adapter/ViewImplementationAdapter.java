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
package se.chalmers.dat261.adapter;

/**
 * Created by David on 2017-05-08.
 */
@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class ViewImplementationAdapter extends BaseAdapter{

  public ViewImplementationAdapter() throws Exception {
    super("/model-based/ViewImplementation.java");
  }

  public void addEnum() {
    type("public Enum FooEnum {\n A,\nB\n}");
  }

  public void addMethod() {
    type("public void FooMethod(){\n}");
  }

  public void addVariable() {
    type("public final int fooInt = 1;");
  }

  public void placeCaretAtUndefined() {

  }

  @Override
  public String getName() {
    return "testViewImplementation";
  }
}
