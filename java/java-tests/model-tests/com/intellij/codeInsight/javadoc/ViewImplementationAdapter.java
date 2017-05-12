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

import com.intellij.codeInsight.hint.ImplementationViewComponent;
import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import se.chalmers.dat261.adapter.BaseAdapter;

/**
 * Created by David on 2017-05-08.
 */
@SuppressWarnings({"JUnitTestCaseWithNoTests", "JUnitTestCaseWithNonTrivialConstructors", "JUnitTestClassNamingConvention"})
public class ViewImplementationAdapter extends BaseAdapter {
  private PsiClassImpl javaClass;

  private PsiField selectedVariable;
  private PsiMethod selectedMethod;
  private PsiClass selectedEnum;

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
    PsiField[] fields = javaClass.getFields();
    for (PsiField field : fields) {
      int startOffset = field.getTextRange().getStartOffset();
      myEditor.getCaretModel().moveToOffset(startOffset);
      invokeAction(IdeActions.ACTION_EDITOR_DELETE_LINE);
    }

    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void removeEnum() {
    PsiClass[] enums = javaClass.getInnerClasses();
    for (PsiClass innerEnum : enums) {
      if (innerEnum.isEnum()) {
        int startOffset = innerEnum.getTextRange().getStartOffset();
        myEditor.getCaretModel().moveToOffset(startOffset);
        invokeAction(IdeActions.ACTION_EDITOR_DELETE_LINE);
      }
    }
    PsiDocumentManager.getInstance(ourProject).commitAllDocuments();
    updateClassVariable();
  }

  public void placeCaretAtUndefined() {
    selectedVariable = null;
    selectedEnum = null;
    selectedMethod = null;
  }

  public void placeCaretAtEnum() {
    PsiClass[] enums = javaClass.getInnerClasses();
    for (PsiClass innerEnum : enums) {
      if (innerEnum.isEnum()) {
        this.selectedEnum = innerEnum;
      }
    }
  }

  public void placeCaretAtVariable() {
    PsiField[] fields = javaClass.getAllFields();
    for (PsiField field : fields) {
      this.selectedVariable = field;
    }
  }

  public String viewEnumImplementation() {
    return ImplementationViewComponent.getNewText(selectedEnum);
  }

  public String viewEnumDocumentation() {
    return JavaDocumentationProvider.generateExternalJavadoc(selectedEnum);
  }

  public String viewVariableImplementation() {
    return ImplementationViewComponent.getNewText(selectedVariable);
  }

  public String viewVariableDocumentation() {
    return JavaDocumentationProvider.generateExternalJavadoc(selectedVariable);
  }

  public String getContent() {
    return getEditor().getDocument().getText();
  }

  @Override
  public String getName() {
    return "testViewImplementation";
  }
}
