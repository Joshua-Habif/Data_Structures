import java.util.Scanner;
import java.io.*;


public class Solution {
    
    public static BufferedWriter output;
    
    public static void main(String[] args) throws IOException {
        
        
        output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);
        
        Scanner sc = new Scanner(System.in);
        
        TwoThreeTree tree = new TwoThreeTree();
        
        int databaseSize=Integer.parseInt(sc.nextLine());
        
        for(int i=0; i<databaseSize; i++) {
            String line = sc.nextLine();
            String[] lineArray = line.split("\\s+");
            twothree.insert(lineArray[0], Integer.parseInt(lineArray[1]), tree);
        }
        
        int queries = Integer.parseInt(sc.nextLine());
        
        for(int i=0; i<queries; i++) {
            String line = sc.nextLine();
            String[] lineArray = line.split("\\s+");
            try {
                if(lineArray[0].compareTo(lineArray[1])<=0)
                    twothree.printRange(tree.root, lineArray[0], lineArray[1], tree.height);
                else
                    twothree.printRange(tree.root, lineArray[1], lineArray[0], tree.height);
            }
            catch (ArrayIndexOutOfBoundsException e){
                String[] newArray = {lineArray[0], " "};
                if(newArray[0].compareTo(newArray[1])<=0)
                    twothree.printRange(tree.root, newArray[0], newArray[1], tree.height);
                else
                    twothree.printRange(tree.root, newArray[1], newArray[0], tree.height);
            }
            
        }
        
        output.flush();
        output.close();
        sc.close();
    }
    
}



class twothree {

   static void insert(String key, int value, TwoThreeTree tree) {
   // insert a key value pair into tree (overwrite existsing value
   // if key is already present)

      int h = tree.height;

      if (h == -1) {
          LeafNode newLeaf = new LeafNode();
          newLeaf.guide = key;
          newLeaf.value = value;
          tree.root = newLeaf; 
          tree.height = 0;
      }
      else {
         WorkSpace ws = doInsert(key, value, tree.root, h);

         if (ws != null && ws.newNode != null) {
         // create a new root

            InternalNode newRoot = new InternalNode();
            if (ws.offset == 0) {
               newRoot.child0 = ws.newNode; 
               newRoot.child1 = tree.root;
            }
            else {
               newRoot.child0 = tree.root; 
               newRoot.child1 = ws.newNode;
            }
            resetGuide(newRoot);
            tree.root = newRoot;
            tree.height = h+1;
         }
      }
   }

   static WorkSpace doInsert(String key, int value, Node p, int h) {
   // auxiliary recursive routine for insert

      if (h == 0) {
         // we're at the leaf level, so compare and 
         // either update value or insert new leaf

         LeafNode leaf = (LeafNode) p; //downcast
         int cmp = key.compareTo(leaf.guide);

         if (cmp == 0) {
            leaf.value = value; 
            return null;
         }

         // create new leaf node and insert into tree
         LeafNode newLeaf = new LeafNode();
         newLeaf.guide = key; 
         newLeaf.value = value;

         int offset = (cmp < 0) ? 0 : 1;
         // offset == 0 => newLeaf inserted as left sibling
         // offset == 1 => newLeaf inserted as right sibling

         WorkSpace ws = new WorkSpace();
         ws.newNode = newLeaf;
         ws.offset = offset;
         ws.scratch = new Node[4];

         return ws;
      }
      else {
         InternalNode q = (InternalNode) p; // downcast
         int pos;
         WorkSpace ws;

         if (key.compareTo(q.child0.guide) <= 0) {
            pos = 0; 
            ws = doInsert(key, value, q.child0, h-1);
         }
         else if (key.compareTo(q.child1.guide) <= 0 || q.child2 == null) {
            pos = 1;
            ws = doInsert(key, value, q.child1, h-1);
         }
         else {
            pos = 2; 
            ws = doInsert(key, value, q.child2, h-1);
         }

         if (ws != null) {
            if (ws.newNode != null) {
               // make ws.newNode child # pos + ws.offset of q

               int sz = copyOutChildren(q, ws.scratch);
               insertNode(ws.scratch, ws.newNode, sz, pos + ws.offset);
               if (sz == 2) {
                  ws.newNode = null;
                  ws.guideChanged = resetChildren(q, ws.scratch, 0, 3);
               }
               else {
                  ws.newNode = new InternalNode();
                  ws.offset = 1;
                  resetChildren(q, ws.scratch, 0, 2);
                  resetChildren((InternalNode) ws.newNode, ws.scratch, 2, 2);
               }
            }
            else if (ws.guideChanged) {
               ws.guideChanged = resetGuide(q);
            }
         }

         return ws;
      }
   }


   static int copyOutChildren(InternalNode q, Node[] x) {
   // copy children of q into x, and return # of children

      int sz = 2;
      x[0] = q.child0; x[1] = q.child1;
      if (q.child2 != null) {
         x[2] = q.child2; 
         sz = 3;
      }
      return sz;
   }

   static void insertNode(Node[] x, Node p, int sz, int pos) {
   // insert p in x[0..sz) at position pos,
   // moving existing extries to the right

      for (int i = sz; i > pos; i--)
         x[i] = x[i-1];

      x[pos] = p;
   }

   static boolean resetGuide(InternalNode q) {
   // reset q.guide, and return true if it changes.

      String oldGuide = q.guide;
      if (q.child2 != null)
         q.guide = q.child2.guide;
      else
         q.guide = q.child1.guide;

      return q.guide != oldGuide;
   }


   static boolean resetChildren(InternalNode q, Node[] x, int pos, int sz) {
   // reset q's children to x[pos..pos+sz), where sz is 2 or 3.
   // also resets guide, and returns the result of that

      q.child0 = x[pos]; 
      q.child1 = x[pos+1];

      if (sz == 3) 
         q.child2 = x[pos+2];
      else
         q.child2 = null;

      return resetGuide(q);
   }
   
   
   static int Search(Node p, String x, int h){
    
       if(h==0){
           if(x.equals(p.guide))
               return ((LeafNode) p).value;
           else
               return -1; //return -1 if key was not found. 
       }
       else if (x.compareTo(((InternalNode) p).child0.guide)<=0) {      
           return Search(((InternalNode) p).child0,x,h-1); 
       }
       else if(((InternalNode) p).child2==null || x.compareTo(((InternalNode) p).child1.guide)<=0) {
           return Search(((InternalNode) p).child1,x,h-1);
       }
       else {
           return Search(((InternalNode) p).child2,x,h-1);
       }
       
   }
   
   static void printAll(Node p, int h) throws IOException {
       if(h==0)
           Solution.output.write(p.guide + " " + ((LeafNode)p).value + "\n");
       else {
            printAll(((InternalNode)p).child0,h-1);
            printAll(((InternalNode)p).child1,h-1);
            if(((InternalNode)p).child2!=null)
                printAll(((InternalNode)p).child2,h-1);
       }
   }
   
   static void printRange(Node p, String x, String y, int h) throws IOException {
       //reach a leaf
       if(h==0) {
           if(x.compareTo(p.guide)<=0 && y.compareTo(p.guide)>=0) {
               Solution.output.write(p.guide + " " + ((LeafNode)p).value + "\n");
           }
       }
       //case 0: if x is in child 0
       else if(x.compareTo(((InternalNode)p).child0.guide)<=0) {
           //if y is in child 0
           if(y.compareTo(((InternalNode)p).child0.guide)<=0){
               printRange(((InternalNode)p).child0,x,y,h-1);
           }
           //y is not in child 0
           else {
               //print GE child 0 with x
               printGE(((InternalNode)p).child0,x,h-1);
               //if y is in child 1 or if child 2 is null
               if(y.compareTo(((InternalNode)p).child1.guide)<=0 || ((InternalNode)p).child2==null) {
                   //print LE in child 1 using y
                   printLE(((InternalNode)p).child1,y,h-1);
               }
               //y is not in child 1 or child 2 is not null
               else {
                   printAll(((InternalNode)p).child1,h-1);
                   if(((InternalNode)p).child2!=null)
                       printLE(((InternalNode)p).child2,y,h-1);
               }     
           }
       }
       //case 1: if x is in child 1
       else if(((InternalNode)p).child2==null || x.compareTo(((InternalNode)p).child1.guide)<=0) {
           //if y is in child 1 or child 2 is null
           if(((InternalNode)p).child2==null || y.compareTo(((InternalNode)p).child1.guide)<=0) {
               printRange(((InternalNode)p).child1,x,y,h-1);
           }
           //if child 2 is not null or y is not in child 1
           else {
               printGE(((InternalNode)p).child1,x,h-1);
               if(((InternalNode)p).child2!=null)
                   printLE(((InternalNode)p).child2,y,h-1);       
           }
       }
       else {
           printRange(((InternalNode)p).child2,x,y,h-1);
       }
       
      
   }
   
   static void printGE(Node p, String x, int h) throws IOException{
       if(h==0) {
           if(x.compareTo(p.guide)<=0) {
               Solution.output.write(p.guide + " " + ((LeafNode)p).value + "\n");
           }
               
       }
       else if(x.compareTo(((InternalNode) p).child0.guide)<=0){
           printGE(((InternalNode) p).child0, x, h-1);
           printAll(((InternalNode) p).child1, h-1);
           if(((InternalNode) p).child2!=null)
               printAll(((InternalNode) p).child2,h-1);
       }
       else if(((InternalNode) p).child2==null || x.compareTo(((InternalNode) p).child1.guide)<=0) {
           printGE(((InternalNode) p).child1,x,h-1);
           if(((InternalNode) p).child2!=null)
               printAll(((InternalNode) p).child2, h-1);
       }
       else
           printGE(((InternalNode) p).child2, x, h-1);       
   }
   
   
   static void printLE(Node p, String x, int h) throws IOException{
       if(h==0) {
           if(x.compareTo(p.guide)>=0)
               Solution.output.write(p.guide + " " + ((LeafNode)p).value + "\n");
       }
       else if(x.compareTo(((InternalNode) p).child0.guide)<=0) {
          printLE(((InternalNode) p).child0, x, h-1);
       }
       else if(((InternalNode) p).child2==null || x.compareTo(((InternalNode) p).child1.guide)<=0) {
           printAll(((InternalNode) p).child0, h-1);
           printLE(((InternalNode) p).child1, x, h-1);
       }
       else {
           printAll(((InternalNode) p).child0, h-1);
           printAll(((InternalNode) p).child1, h-1);
           printLE(((InternalNode) p).child2, x, h-1);
       }  
   }

}


class Node {
   String guide;
   // guide points to max key in subtree rooted at node
}

class InternalNode extends Node {
   Node child0, child1, child2;
   // child0 and child1 are always non-null
   // child2 is null iff node has only 2 children
}

class LeafNode extends Node {
   // guide points to the key

   int value;
}

class TwoThreeTree {
   Node root;
   int height;

   TwoThreeTree() {
      root = null;
      height = -1;
   }
}

class WorkSpace {
// this class is used to hold return values for the recursive doInsert
// routine

   Node newNode;
   int offset;
   boolean guideChanged;
   Node[] scratch;
}
