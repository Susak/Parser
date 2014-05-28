package main.parser;

import org.StructureGraphic.v1.DSTreeNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: Ruslan Sokolov
 * date: 4/1/14
 */
public class Tree implements DSTreeNode {
    private Token node;
    private List<Tree> children;
    private Color color;

    public Tree(Token node, Color color) {
        this.node = node;
        this.color = color;
    }

    public Token getNode() {
        return node;
    }

    public void addChild(Tree child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
        if (children != null) {
            return children.toArray(new Tree[children.size()]);
        } else {
            return Collections.emptyList().toArray(new Tree[0]);
        }
    }

    @Override
    public Object DSgetValue() {
        return node.getValue();
    }

    @Override
    public Color DSgetColor() {
        return color;
    }

    public String getExpression() {
        StringBuilder expression = new StringBuilder();
        if (!node.getType().equals(TokenType.NON_TERMINAL)) {
            return expression.append(node.getValue()).toString();
        }
        if (children == null) return "";
        for (Tree child : children) {
            expression.append(child.getExpression());
        }
        return expression.toString();
    }
}

