package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Representing a node in an abstract syntax tree, a tree bijective
 * representation of the syntactic structure of source code written in s-java,
 * assuming the order of method declarations does not matter, so each code could
 * have only one way to be interpreted as a tree and vice versa. Each node
 * denotes a construct occurring in the source code.
 */
public abstract class AstNode {

    /**
     * An enum of the different nodes types.
     */
    public static enum NodeType {
        RETURN, CODE_SCOPE, METHOD, ARGUMENT, GLOBAL, WHILE, IF,
        VAR_DECLARATION, CALL_METHOD, ASSIGNMENT, LITERAL, VAR_VAL, OR, AND
    }

    // The original position in the file.
    private final Position position;

    /**
     * @param position The position in the file of the node's creation.
     */
    public AstNode(Position position) {
        this.position = position;
    }

    /**
     * @return The type of the node.
     */
    public abstract NodeType getNodeType();

    /**
     * @return The position in the file of the node's creation.
     */
    public Position getPosition() {
        return position;
    }

}
