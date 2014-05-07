/*
 * This file explores trees and other related materials
 */

function Node(val) {
    var value =val;
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
        if (temp == null) { // tree does not exist. Start building the root
            root = node;
            return;
        }
        for(;temp.left != null && temp.right != null;) {
            if (node.getValue() <= temp.getValue()) temp = temp.left;
            else temp = temp.right;
        }
        if (node.getValue() <= temp.value()) temp.left = node;
        else temp.right = node;
    }
    
    this.printPreOrder = function(node) {        
        if (node == null) {
            return;
        }
        else {
            console.log(node.getValue());
            printPreOrder(node.left);
            printPreOrder(node.right);
        }
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
tree.printPreOrder(tree.getRoot());