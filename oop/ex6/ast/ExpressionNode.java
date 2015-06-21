package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */

public abstract class ExpressionNode extends AstNode {

    public static enum ExpressionType {
        INT,
        DOUBLE,
        CHAR,
        STRING,
        BOOLEAN;

        public boolean accept(ExpressionType expType) {
            return (this == expType) ||
                    (this == DOUBLE && expType == INT) ||
                    (this == BOOLEAN && (expType == INT || expType == DOUBLE));
        }
    }


    public ExpressionNode(Position position) {
        super(position);
    }

    public abstract ExpressionType getType();

}