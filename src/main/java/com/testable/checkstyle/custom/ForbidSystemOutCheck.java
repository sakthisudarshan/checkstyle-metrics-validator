package com.testable.checkstyle.custom;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Project-specific custom Checkstyle rule: forbids System.out.println usage.
 */
public class ForbidSystemOutCheck extends AbstractCheck {

    private static final String MSG_KEY = "forbid.system.out";

    @Override
    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] {TokenTypes.METHOD_CALL};
    }

    @Override
    public void visitToken(DetailAST methodCallAst) {
        DetailAST selector = methodCallAst.getFirstChild();
        if (selector != null && selector.getType() == TokenTypes.DOT) {
            String chain = extractDotChain(selector);
            if (chain.startsWith("System.out")) {
                log(methodCallAst.getLineNo(), MSG_KEY,
                        "System.out usage is forbidden by project policy");
            }
        }
    }

    private static String extractDotChain(DetailAST dotAst) {
        DetailAST left = dotAst.getFirstChild();
        DetailAST right = dotAst.getLastChild();
        String rightText = right != null ? right.getText() : "";
        if (left == null) {
            return rightText;
        }
        if (left.getType() == TokenTypes.IDENT) {
            return left.getText() + "." + rightText;
        }
        if (left.getType() == TokenTypes.DOT) {
            return extractDotChain(left) + "." + rightText;
        }
        return rightText;
    }
}
