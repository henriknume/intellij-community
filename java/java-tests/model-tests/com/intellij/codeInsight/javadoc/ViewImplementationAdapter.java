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
package com.intellij.codeInsight.javadoc;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.PsiClassImpl;
import se.chalmers.dat261.adapter.BaseAdapter;

/**
 * Created by David on 2017-05-08.
 */
@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class ViewImplementationAdapter extends BaseAdapter {
  private PsiClassImpl javaClass;

  public ViewImplementationAdapter() throws Exception {
    super("/model-based/ViewImplementation.java");
    updateClassVariable();
  }

  private void updateClassVariable() {
    for (PsiElement child : myFile.getChildren()) {
      if (child instanceof PsiClassImpl) {
        javaClass = (PsiClassImpl)child;
      }
    }
  }

  public void addEnum(String stringEnum) {
    type(stringEnum);
    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void addMethod(String stringMethod) {
    type(stringMethod);
    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void addVariable(String stringVariable) {
    type(stringVariable);
    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void removeVariable() {
    Document document = PsiDocumentManager.getInstance(ourProject).getDocument(myFile);
    boolean writable = document.isWritable();
    updateClassVariable();
    PsiField[] fields = javaClass.getAllFields();
    int startOffset = 0, endOffset = 0;
    for (PsiField field : fields) {
      startOffset = field.getTextRange().getStartOffset();
      endOffset = field.getTextRange().getEndOffset();
      myEditor.getCaretModel().moveToOffset(startOffset);
      invokeAction(IdeActions.ACTION_EDITOR_DELETE_LINE);
    }
    down();
    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void placeCaretAtUndefined() {

  }

  public void reset() {

  }

  public String getContent() {
    return getEditor().getDocument().getText();
  }

  @Override
  public String getName() {
    return "testViewImplementation";
  }
}
