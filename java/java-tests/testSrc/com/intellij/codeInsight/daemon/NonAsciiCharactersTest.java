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

/*
 * Created by IntelliJ IDEA.
 * User: max
 * Date: Apr 11, 2002
 * Time: 7:51:16 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.intellij.codeInsight.daemon;

import com.intellij.codeInspection.NonAsciiCharactersInspection;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class NonAsciiCharactersTest extends DaemonAnalyzerTestCase {
  @NonNls private static final String BASE_PATH = "/codeInsight/daemonCodeAnalyzer/nonAsciiCharacters";

  @NotNull
  @Override
  protected LocalInspectionTool[] configureLocalInspectionTools() {
    NonAsciiCharactersInspection inspection = new NonAsciiCharactersInspection();
    inspection.CHECK_FOR_DIFFERENT_LANGUAGES_IN_IDENTIFIER_NAME = true;
    inspection.CHECK_FOR_NOT_ASCII_IDENTIFIER_NAME = true;
    inspection.CHECK_FOR_NOT_ASCII_COMMENT = true;
    inspection.CHECK_FOR_NOT_ASCII_STRING_LITERAL = true;
    inspection.CHECK_FOR_FILES_CONTAINING_BOM = true;
    return new LocalInspectionTool[]{inspection};
  }

  private void doTest() throws Exception {
    doTest(BASE_PATH + "/" + getTestName(false)+".java", true, false);
    UIUtil.dispatchAllInvocationEvents();
  }

  public void testSimple() throws Exception {
    doTest();
  }
}
