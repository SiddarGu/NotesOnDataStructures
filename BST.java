import java.util.ArrayList;

class Node<K,V> {
    private K key; // key must be comparable, value not so
    private Node<K,V> left, right;
    private V value;

    // constructor for node
    public Node(K k, V v) {
        this.key = k;
        this.value = v;
        this.left = null;
        this.right = null;
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
}

public class BST<K extends Comparable<K>, V> {
    private Node<K,V> root;

    // constructor for bst
    public BST() {
        root = null;
    }
    
    public V find(K key, Node<K,V> n) {
        if (n == null) { return null; }
        K curr = n.getKey();
        if (curr.compareTo(key) == 0) { return n.getVal(); }
        else if (key.compareTo(curr) < 0) {
            return find(key, n.getLeft());
        }
        else {
            return find(key, n.getRight());
        }
    }

    public Node<K,V> insert(K k, V v, Node<K,V> p) throws Exception {
        if (p == null) {
            p = new Node<K,V>(k, v);
        } else {
            K pKey = p.getKey();
            
            if (k.compareTo(pKey) == 0) {
                throw new Exception("Duplicate found.");
            } else if (k.compareTo(pKey) > 0) {
                p.setRight(insert(k, v, p.getRight()));
            } else {
                p.setLeft(insert(k, v, p.getLeft()));
            }
        }
        return p;
    }

    public Node<K,V> delete(K k, Node<K,V> p) throws Exception {
        if (p == null) {
            throw new Exception("Key not found.");
        } else {
            K pKey = p.getKey();

            if (k.compareTo(pKey) == 0) {
                Node<K,V> r = findReplacement(p);
                // copy r's value to p and delete r
                p.setVal(r.getVal());
                p.setRight(delete(r.getKey(), p.getRight()));
                return r;
            } else if (k.compareTo(pKey) > 0) {
                p.setRight(delete(k, p.getRight()));
            } else if (k.compareTo(pKey) < 0) {
                p.setLeft(delete(k, p.getLeft()));
            }
            return p;
        }
    }

    // find a replacement node when deleting an internal node
    private Node<K,V> findReplacement(Node<K,V> p) {
        Node<K,V> r = p.getRight();
        while (r.getLeft() != null) {
            r = r.getLeft();
        }
        return r;
    }

    public int size() {
        if (root == null) {
            return 0;
        } else {
            return sizeAux(root);
        }
    }

    private int sizeAux(Node<K,V> p) {
        if (p == null) {
            return 0;
        } else {
            return 1 + sizeAux(p.getLeft()) + sizeAux(p.getRight());
        }
    }

    public ArrayList<Node<K,V>> inorder() {
        ArrayList<Node<K,V>> arr = new ArrayList<>();
        
        if (root != null) {
            inorderAux(arr, root);
        }
        return arr;
    }

    public void inorderAux(ArrayList<Node<K,V>> arr, Node<K,V> p) {
        inorderAux(arr, p.getLeft());
        arr.add(p);
        inorderAux(arr, p.getRight());
    }
}
