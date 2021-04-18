class Node<K,V> {
    private K key; // key must be comparable, value not so
    private Node<K,V> left, right;
    private V value;
    private int height; // new param to keep track of tree balance

    // constructor for node
    public Node(K k, V v, int h) {
        this.key = k;
        this.value = v;
        this.left = null;
        this.right = null;
        this.height = h;
    }

    // getters
    public K getKey() {
        return key;
    }

    public V getVal() {
        return value;
    }

    public Node<K,V> getLeft() {
        return left;
    }

    public Node<K,V> getRight() {
        return right;
    }

    public int getHeight() {
        return height;
    }

    // setters
    public void setLeft(Node<K,V> p) {
        left = p;
    }

    public void setRight(Node<K,V> p) {
        right = p;
    }

    public void setVal(V v) {
        this.value = v;
    }

    public void setHeight(int h) {
        height = h;
    }
}

public class AVLTree<K extends Comparable<K>, V> {
    private Node<K,V> root;

    public AVLTree() {
        root = null;
    }

    // rotations
    public Node<K,V> rotateRight(Node<K,V> p) {
        Node<K,V> q = p.getLeft();
        p.setLeft(q.getRight());
        q.setRight(p);
        return q;
    }

    public Node<K,V> rotateLeft(Node<K,V> p) {
        Node<K,V> q = p.getRight();
        p.setRight(q.getLeft());
        q.setLeft(p);
        return q;
    }

    public Node<K,V> rotateLeftRight(Node<K,V> p) {
        p.setLeft(rotateLeft(p.getLeft()));
        return rotateRight(p);
    }

    public Node<K,V> rotateRightLeft(Node<K,V> p) {
        p.setRight(rotateRight(p.getRight()));
        return rotateLeft(p);
    }

    // utilities

    public int height(Node<K,V> p) {
        return (p == null) ? -1 : p.getHeight();
    }

    public void updateHeight(Node<K,V> p) {
        int lh = p.getLeft().getHeight();
        int rh = p.getRight().getHeight();

        int childrenHeight = lh > rh ? lh : rh;
        
        p.setHeight(1 + childrenHeight);
    }

    public int balanceFactor(Node<K,V> p) {
        return p.getRight().getHeight() - p.getLeft().getHeight();
    }

    public Node<K,V> insert(K k, V v, Node<K,V> p) throws Exception {
        if (p == null) {
            p = new Node<K,V>(k, v, 0);
        } else {
            K pKey = p.getKey();

            if (k.compareTo(pKey) < 0) {
                p.setLeft(insert(k, v, p.getLeft()));
            } else if (k.compareTo(pKey) > 0) {
                p.setRight(insert(k, v, p.getRight()));
            } else {
                throw new Exception("Duplicate found.");
            }
        }
        return rebalance(p);
    }

    public Node<K,V> rebalance(Node<K,V> p) {
        if (p == null) { return p; }
        if (balanceFactor(p) < -1) {
            if (height(p.getLeft().getLeft()) > height(p.getRight().getRight())) {
                p = rotateRight(p);
            } else {
                p = rotateLeftRight(p);
            }

        } else if (balanceFactor(p) > 1) {
            if (height(p.getRight().getRight()) >= height(p.getRight().getLeft())) { 
                p = rotateLeft(p); 
            } else {
                p = rotateRightLeft(p);
            }
        }
        updateHeight(p);
        return p;
    }
}
