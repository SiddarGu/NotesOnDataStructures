public class AVLTree extends BST {
    class Node {
        int key, value; // key must be comparable, value is not necessary
        Node left, right;
        int height; // for AVL trees

        // constructor for node
        public Node(int k, int v, int height) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
            this.height = height;
        }
    }


    public AVLTree() {
        super();
    }

    // rotations
    public Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        return q;
    }

    public Node rotateLeft(Node p) {
        Node q = p.right;
        p.right = q.left;
        q.left = p;
        return q;
    }

    public Node rotateLeftRight(Node p) {
        p.left = rotateLeft(p.left);
        return rotateRight(p);
    }

    public Node rotateRightLeft(Node p) {
        p.right = rotateRight(p.right);
        return rotateLeft(p);
    }

    // utilities

    public int height(Node p) {
        return (p == null) ? -1 : p.height;
    }

    public void updateHeight(Node p) {
        int childrenHeight =
         (height(p.left) > height(p.right)) ?
          height(p.left) :
          height(p.right);
        
        p.height = 1 + childrenHeight;
    }

    public int balanceFactor(Node p) {
        return height(p.right) - height(p.left);
    }

    public Node insert(int k, int v, Node p) {
        if (p == null) {
            p = new Node(k, v, 0);
        } else if (k < p.key) {
            p.left = insert(k, v, p.left);
        } else if (k > p.key) {
            p.right = insert(k, v, p.right);
        } else {
            throw Exception("Duplicate found.");
        }
        return rebalance(p);
    }

    public Node rebalance(Node p) {
        if (p == null) { return p; }
        if (balanceFactor(p) < -1) {
            if (height(p.left.left) > height(p.left.right)) {
                p = rotateRight(p);
            } else {
                p = rotateLeftRight(p);
            }

        } else if (balanceFactor(p) > 1) {
            if (height(p.right.right) >= height(p.right.left)) { 
                p = rotateLeft(p); 
            } else {
                p = rotateRightLeft(p);
            }
        }
        updateHeight(p);
        return p;
    }
}
