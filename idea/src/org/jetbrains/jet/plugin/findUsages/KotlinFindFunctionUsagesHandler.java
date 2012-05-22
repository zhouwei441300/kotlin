/*
 * Copyright 2010-2012 JetBrains s.r.o.
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

package org.jetbrains.jet.plugin.findUsages;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.psi.PsiCompiledElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jet.asJava.JetLightClass;
import org.jetbrains.jet.lang.psi.JetClass;
import org.jetbrains.jet.lang.psi.JetClassBody;
import org.jetbrains.jet.lang.psi.JetFunction;

/**
 * @author yole
 */
public class KotlinFindFunctionUsagesHandler extends FindUsagesHandler {
    protected KotlinFindFunctionUsagesHandler(@NotNull PsiElement psiElement) {
        super(psiElement);
    }

    @NotNull
    @Override
    public PsiElement[] getSecondaryElements() {
        JetFunction function = (JetFunction) getPsiElement();
        if (function.getParent() instanceof JetClassBody) {
            JetClass containingClass = PsiTreeUtil.getParentOfType(function, JetClass.class);
            JetLightClass wrapper = JetLightClass.wrapDelegate(containingClass);
            for (PsiMethod method : wrapper.getMethods()) {
                if (method instanceof PsiCompiledElement && ((PsiCompiledElement) method).getMirror() == function) {
                    return new PsiElement[] { method };
                }
            }
        }
        return PsiElement.EMPTY_ARRAY;
    }
}