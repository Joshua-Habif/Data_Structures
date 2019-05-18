

package project5;

/**
 * Implementation of an AVL Tree
 * @author Joshua Habif
 * @param <T> generic type of data that is stored in nodes of the tree; needs to
 *            implement Comparable<T> interface
 */
public class AVLTree<T extends Comparable<T>> {

	// root of the tree
	protected Node<T> root;
	// current number of nodes in the tree
	protected int numOfElements;
	//helper variable used by the remove methods
	private boolean found;
	
	/**
	 * This method checks if an item is in the AVLTree
	 * @param data
	 * @param currentNode
	 * @return true if it's in the tree; false otherwise. 
	 */
	private boolean recContains(T data, Node<T> currentNode) {
		if(currentNode == null) 
			return false;
		else if(data.compareTo(currentNode.data)>0) 
			return recContains(data, currentNode.right);
		else if(data.compareTo(currentNode.data)<0) 
			return recContains(data, currentNode.left);
		else
			return true;
			
	}
	
	/**
	 * Wrapper method for recContains
	 * @param data
	 * @return true if it's in the tree; false otherwise. 
	 */
	public boolean contains(T data) {
		return recContains(data, root);
	}
	
	/**
	 * This method searches for a specific item in the AVL Tree.
	 * @param data
	 * @param currentNode
	 * @return the item if it was found; null otherwise.
	 */
	private T recBinarySearch(T data, Node<T> currentNode) {
		if(currentNode == null) 
			return null;
		else if(data.compareTo(currentNode.data)>0) 
			return recBinarySearch(data, currentNode.right);
		else if(data.compareTo(currentNode.data)<0) 
			return recBinarySearch(data, currentNode.left);
		else
			return currentNode.data;
	}
	
	/**
	 * Wrapper method for recBinarySerach
	 * @param data
	 * @return the item if it was found; null otherwise.
	 */
	public T binarySearch(T data) {
		return recBinarySearch(data, root);
	}	
	
	/**
	 * Balance LL function
	 * @param node
	 * @return the new "root"
	 */
	private Node<T> balanceLL(Node<T> A) {
		Node<T> B = A.left;
		A.left = B.right;
		B.right = A;
		updateHeight(A);
		A.balanceFactor=calculateBalanceFactor(A);
		updateHeight(B);
		B.balanceFactor=calculateBalanceFactor(B);
		return B;	
	}
	
	/**
	 * Balance RR function
	 * @param node
	 * @return the new "root"
	 */
	private Node<T> balanceRR(Node<T> A) {
		Node<T> B = A.right;
		A.right = B.left;
		B.left = A;
		updateHeight(A);
		A.balanceFactor=calculateBalanceFactor(A);
		updateHeight(B);
		B.balanceFactor=calculateBalanceFactor(B);
		return B;	
	}
	
	/**
	 * Balance LR function
	 * @param node
	 * @return the new "root"
	 */
	private Node<T> balanceLR(Node<T> A) {
		Node<T> B = A.left;
		Node<T> C = B.right;
		A.left = C.right;
		B.right = C.left;
		C.left = B;
		C.right = A;
		updateHeight(A);
		A.balanceFactor=calculateBalanceFactor(A);
		updateHeight(B);
		B.balanceFactor=calculateBalanceFactor(B);
		updateHeight(C);
		C.balanceFactor=calculateBalanceFactor(C);
		return C;	
	}
	
	/**
	 * Balance RL function
	 * @param node
	 * @return the new "root"
	 */
	private Node<T> balanceRL(Node<T> A){
		Node<T> B = A.right;
		Node<T> C = B.left;
		A.right = C.left;
		B.left = C.right;
		C.right = B;
		C.left = A;
		updateHeight(A);
		A.balanceFactor=calculateBalanceFactor(A);
		updateHeight(B);
		B.balanceFactor=calculateBalanceFactor(B);
		updateHeight(C);
		C.balanceFactor=calculateBalanceFactor(C);
		return C;	
	}
	
	/**
	 * Calculates the balance factor 
	 * @param a node
	 * @return the balance factor
	 */
	private int calculateBalanceFactor(Node<T> node) {
		if(node.left==null && node.right==null)
			return 0;
		else if(node.left==null)
			return -node.height;
		else if(node.right==null)
			return node.height;
		else
			return node.left.height-node.right.height;
	}
	
	/**
	 * Updates the height
	 * @param node
	 */
	private void updateHeight(Node<T> node) {
		if(node.left==null && node.right==null)
			node.height=0;
		else if(node.left==null)
			node.height=node.right.height+1;
		else if(node.right==null)
			node.height=node.left.height+1;
		else
			node.height=Math.max(node.left.height, node.right.height)+1;
	}
	
	/**
	 * This function balances the tree
	 * @param node
	 */
	private Node<T> balance(Node<T> node) {
		
		if(node.balanceFactor<=-2 && node.right.balanceFactor<0)  
			return balanceRR(node); //balance rr if node.bf=<-2 && node.right.bf<0
		else if(node.balanceFactor>=2 && node.left.balanceFactor>0)
			return balanceLL(node); //balance ll if node.bf=>2 && node.left.bf>0
		else if(node.balanceFactor>=2 && node.left.balanceFactor<0)
			return balanceLR(node); //balance lr if node.bf=>2 && node.left.bf<0
		else if(node.balanceFactor<=-2 &&( node.left==null || node.left.balanceFactor>0) )
			return balanceRL(node); //balance rl if node.bf<=-2 && node.left.bf>0
		return node;
	
	}
	
	/**
	 * Default constructor that creates an empty tree.
	 */
	public AVLTree() {
		this.root = null;
		numOfElements = 0;
	}

	/**
	 * Add the given data item to the tree. If item is null, the tree does not
	 * change. If item already exists, the tree does not change.
	 *
	 * @param item the new element to be added to the tree
	 */
	public void add(T item) {
		if (item == null)
			return;
		root = add (root, item);

	}

	/**
	 * Actual recursive implementation of add.
	 * @param item the new element to be added to the tree
	 * @param node
	 * @return node
	 */
	private Node<T> add(Node<T> node, T item) {
		if (node == null) {
			numOfElements++;
			return new Node<T>(item);
		}
		if (node.data.compareTo(item) > 0)
			node.left = add(node.left, item);
		else if (node.data.compareTo(item) < 0)
			node.right = add(node.right, item);
		
		updateHeight(node);
		node.balanceFactor=calculateBalanceFactor(node);
		node=balance(node);
		return node;
	}

	/**
	 * Remove the item from the tree. If item is null the tree remains unchanged. If
	 * item is not found in the tree, the tree remains unchanged.
	 *
	 * @param target the item to be removed from this tree
	 */
	public boolean remove(T target)
	{
		root = recRemove(target, root);
		if (found) numOfElements--; 
		return found;
	}

	/*
	 * Actual recursive implementation of remove method: find the node to remove.
	 *
	 * @param target the item to be removed from this tree
	 */
	private Node<T> recRemove(T target, Node<T> node)
	{
		if (node == null)
			found = false;
		else if (target.compareTo(node.data) < 0)
			node.left = recRemove(target, node.left);
		else if (target.compareTo(node.data) > 0)
			node.right = recRemove(target, node.right );
		else {			
			node = removeNode(node);
			found = true;
		}
		return node;
	}

	/*
	 * Actual recursive implementation of remove method: perform the removal.
	 *
	 * @param target the item to be removed from this tree
	 * @return a reference to the node itself, or to the modified subtree
	 */
	private Node<T> removeNode(Node<T> node)
	{
		T data;
		if (node.left == null) 
			return node.right ;
		else if (node.right  == null) 
			return node.left;
		else {			
			data = getPredecessor(node.left);
			node.data = data;
			node.left = recRemove(data, node.left);
	
			return removeBalance(node);//return node;
		}
	}
	
	
	private Node<T> removeBalance(Node<T> node){
		if(node.balanceFactor<=-2 && node.right.balanceFactor<0)  
			return balanceRR(node); //balance rr if node.bf=<-2 && node.right.bf<0
		else if(node.balanceFactor>=2 && node.left.balanceFactor>0)
			return balanceLL(node); //balance ll if node.bf=>2 && node.left.bf>0
		else if(node.balanceFactor>=2 && node.left.balanceFactor<0)
			return balanceLR(node); //balance lr if node.bf=>2 && node.left.bf<0
		else if(node.balanceFactor<=-2 &&( node.left==null || node.left.balanceFactor>0))
			return balanceRL(node); //balance rl if node.bf<=-2 && node.left.bf>0
		
		
		updateHeight(node);
		node.balanceFactor=calculateBalanceFactor(node);
		node=balance(node);
		return node;
			
	}

	/*
	 * Returns the information held in the rightmost node of subtree
	 *
	 * @param subtree root of the subtree within which to search for the rightmost node
	 * @return returns data stored in the rightmost node of subtree
	 */
	private T getPredecessor(Node<T> subtree)
	{
		if (subtree==null) throw new NullPointerException("getPredecessor called with an empty subtree");
		Node<T> temp = subtree;
		while (temp.right  != null)
			temp = temp.right ;
		return temp.data;
	}

	/**
	 * Determines the number of elements stored in this BST.
	 * @return number of elements in this BST
	 */
	public int size() {
		return numOfElements;
	}

	/**
	 * Returns a string representation of this tree using an inorder traversal .
	 * @see java.lang.Object#toString()
	 * @return string representation of this tree
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		inOrderPrint(root, s);
		return s.toString();
	}

	/*
	 * Actual recursive implementation of inorder traversal to produce string
	 * representation of this tree.
	 *
	 * @param tree the root of the current subtree
	 * @param s the string that accumulated the string representation of this BST
	 */
	private void inOrderPrint(Node<T> tree, StringBuilder s) {
		if (tree != null) {
			inOrderPrint(tree.left, s);
			s.append(tree.data.toString() + "  ");
			inOrderPrint(tree.right , s);
		}
	}

	/**
	 * DO NOT MOFIFY THIS METHOD.
	 * INCLUDE IT AS-IS IN YOUR CODE.
	 *
	 * Produces tree like string representation of this BST.
	 * @return string containing tree-like representation of this BST.
	 */
	public String toStringTreeFormat() {

		StringBuilder s = new StringBuilder();

		preOrderPrint(root, 0, s);
		return s.toString();
	}

	/*
	 * 
	 *
	 * Actual recursive implementation of preorder traversal to produce tree-like string
	 * representation of this tree.
	 *
	 * @param tree the root of the current subtree
	 * @param level level (depth) of the current recursive call in the tree to
	 *   determine the indentation of each item
	 * @param output the string that accumulated the string representation of this
	 *   BST
	 */
	private void preOrderPrint(Node<T> tree, int level, StringBuilder output) {
		if (tree != null) {
			String spaces = "\n";
			if (level > 0) {
				for (int i = 0; i < level - 1; i++)
					spaces += "   ";
				spaces += "|--";
			}
			output.append(spaces);
			output.append(tree.data);
			preOrderPrint(tree.left, level + 1, output);
			preOrderPrint(tree.right , level + 1, output);
		}
		// uncomment the part below to show "null children" in the output
		else {
			String spaces = "\n";
			if (level > 0) {
				for (int i = 0; i < level - 1; i++)
					spaces += "   ";
				spaces += "|--";
			}
			output.append(spaces);
			output.append("null");
		}	
	}	
	
	/**
	 * Node class is used to represent nodes in a binary search tree.
	 * It contains a data item that has to implement Comparable interface
	 * and references to left and right subtrees.
	 *
	 *
	 *
	 * @param <T> a reference type that implements Comparable<T> interface
	 */
	public class Node <T extends Comparable <T>>implements Comparable <Node <T> > {


		protected Node <T> left;  //reference to the left subtree
		protected Node <T> right; //reference to the right subtree
		protected T data;            //data item stored in the node

		
		protected int height;
		protected int balanceFactor;
		protected int desc; 		//num of descendants


		/**
		 * Constructs a BSTNode initializing the data part
		 * according to the parameter and setting both
		 * references to subtrees to null.
		 * @param data
		 *    data to be stored in the node
		 */
		protected Node(T data) {
			this.data = data;
			left = null;
			right = null;
			height = 0;
			desc = 0;
		}


		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Node<T> other) {
			return this.data.compareTo(other.data);
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return data.toString();
		}


	}

	
}
