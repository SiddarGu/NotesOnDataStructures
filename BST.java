public class BST {
    // define a node
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

    private Node root;

    // constructor for bst
    public BST() {
        root = null;
    }
    
    public int find(int key, Node n) {
        if (n == null) { return -1; }
        int curr = n.key;
        if (curr == key) { return n.value; }
        else if (key < curr) {
            return find(key, n.left);
        }
        else {
            return find(key, n.right);
        }
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
        return p;
    }

    public Node delete(int k, Node p) {
        if (p == null) {
            throw Exception("Key not found.");
        } else {
            if (k < p.key) {
                p.left = delete(k, p.left);
            } else if (k > p.key) {
                p.right = delete(k, p.right);
            } else if (p.left == null || p.right == null) {
                if (p.left == null) {
                    return p.right;
                }
                if (p.right == null) {
                    return p.left;
                }
            } else {
                Node r = findReplacement(p);
                // copy r's value to p and delete r
                p.value = r.value;
                p.right = delete(r.key, p.right);
            }
        }
    }

    // find a replacement node when deleting an internal node
    private Node findReplacement(Node p) {
        Node r = p.right;
        while (r.left != null) {
            r = r.left;
        }
        return r;
    }
}
