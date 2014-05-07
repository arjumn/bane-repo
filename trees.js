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
        
        console.log(node.getValue());
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
        console.log(node.getValue());
    }
}

var tree = new BST();
var nd = new Node(7);
tree.addNewNode(nd);
nd = new Node(1);
tree.addNewNode(nd);
nd = new Node(0);
tree.addNewNode(nd);
nd = new Node(9);
tree.addNewNode(nd);
nd = new Node(8);
tree.addNewNode(nd);
nd = new Node(10);
tree.addNewNode(nd);
nd = new Node(3);
tree.addNewNode(nd);
nd = new Node(5);
tree.addNewNode(nd);
nd = new Node(2);
tree.addNewNode(nd);
nd = new Node(4);
tree.addNewNode(nd);
nd = new Node(6);
tree.addNewNode(nd);
tree.postOrderTraversal(tree.getRoot());