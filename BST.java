public class BST {
    // define a node
    class Node {
        int key, value; // key must be comparable, value is not necessary
        Node left, right;

        // constructor for node
        public Node(int k, int v) {
            this.key = k;
            this.value = v;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    // constructor for bst
    public BST() {
        root = null;
    }
    
    public int find(int key, Node n) {
        if (n == null) { return null; }
        curr = n.key;
        if (curr == key) { return n.value; }
        else if (key < curr) {
            return find(key, n.left);
        }
        else {
            return find(key, n.right);
        }
    }

    public Node insert(int k, int v, Node p) {
        if (p == null) {
            p = new Node(k, v);
        } else if (k < p.key) {
            p.left = insert(k, v, p.left);
        } else if (k > p.key) {
            p.right = insert(k, v, p.right);
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
            } else if (x > p.key) {
                p.right = delete(k, p.right);
            } else if (p.left == null || p.right == null) {
                if (p.left == null) {
                    return p.right;
                }
                if (p.right == null) {
                    return p.left;
                }
            } else {
                r = findReplacement(p);
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
