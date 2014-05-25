/*
 * This file explores trees and other related materials
 */

function Node(val) {
    var value = val;
    this.left = null;
    this.right = null;
    
    this.getValue = function() { return value; }
    this.setValue = function(val) {
        value = val;
    }
}

function BST() {
    var root = null;
    this.getRoot = function(){ return root; }
    this.setRoot = function(node) {
        root = node;
    }

    this.createTree = function(arr) {
        var i = 0, len = arr.length, nd = null;
        for(var i = 0; i < len; i++) {
            nd = new Node(arr[i]);
            this.addNewNode(nd);
        }    
    }
    
    this.addNewNode = function(node) {
        var temp = root;
        if (temp == null) {
            root = node;
            return;
        }
        
        while(1) {
            if (node.getValue() <= temp.getValue()) {
                if (temp.left == null) break;
                else temp = temp.left;
            }
            else {
                if (temp.right == null) break;
                else temp = temp.right;
            }
        }
        
        if (node.getValue() <= temp.getValue()) temp.left = node;
        else temp.right = node;
    }
    
    this.preOrderTraversal = function(node) {
        if (node == null) {
            return;
        }
        
        document.write(node.getValue() + ", ");
        this.preOrderTraversal(node.left);
        this.preOrderTraversal(node.right);
    }
    
    this.inOrderTraversal = function(node) {
        if (node == null) {
            return;
        }
        this.inOrderTraversal(node.left);
        console.log(node.getValue());
        this.inOrderTraversal(node.right);
    }
    
    this.postOrderTraversal = function(node) {
        if (node == null) {
            return;
        }
        this.postOrderTraversal(node.left);
        this.postOrderTraversal(node.right);
        document.write(node.getValue() + ", ");
    }
    
    this.breadthFirstSearch = function() {
        var queue = [], node = root;
        queue.push(root);
        while(queue.length > 0) {
            node = queue.shift();
            if (node.left != null) queue.push(node.left);
            if (node.right != null) queue.push(node.right);
            document.write(node.getValue() + ", ");
        }
    }
    
    this.preOrderTraversalIterative = function() {
        var stack = [], node = root;
        if (node == null) {
            return;
        }
        stack.push(node);
        while(stack.length > 0) {
            node = stack.pop();
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
            document.write(node.getValue() + ", ");
        }
    }

    this.inOrderTraversalIterative = function() {
        var stack = [], parent = root, node = null;
        if (parent == null) {
            return;
        }
        
        stack.push(node);
        if (node.left != null) stack.push(node.left);

        while(stack.length > 0) {
            node = stack.pop();
            if (node.right != null) stack.push(node.right);
            if (node.left != null) stack.push(node.left);
            document.write(node.getValue() + ", ");
        }
    }
    
    this.getDepth = function(node) {
        var leftd = 0, rightd = 0;
        if (node == null) {
            return 0;
        }
        leftd = this.getDepth(node.left);
        rightd = this.getDepth(node.right);
        if (leftd > rightd) return leftd +1;
        else return rightd + 1;
    }
}

var tree = new BST();
tree.createTree([7,4,10, 2, 8, 1, 3, 11, 14]);
document.write("<h4>Breadth First Traversal </h4>");
tree.breadthFirstSearch();
document.write("<h4>Pre Order Traversal </h4>");
tree.preOrderTraversal(tree.getRoot());
document.write("<h4>Pre Order Traversal Iterative</h4>");
tree.preOrderTraversalIterative();
document.write("<h4>Depth</h4>");
document.write(tree.getDepth(tree.getRoot()));
