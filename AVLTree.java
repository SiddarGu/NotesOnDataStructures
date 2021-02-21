public class AVLTree extends BST {
    
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
        int h = 0;
        return insertAux(k, v, p, h);
    }

    private Node insertAux(int k, int v, Node p, int height) {
        if (p == null) {
            p = new Node(k, v, height);
        } else if (k < p.key) {
            p.left = insertAux(k, v, p.left, (height + 1));
        } else if (k > p.key) {
            p.right = insertAux(k, v, p.right, (height + 1));
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
